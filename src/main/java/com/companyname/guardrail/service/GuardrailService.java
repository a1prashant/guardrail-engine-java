package com.companyname.guardrail.service;

import com.companyname.guardrail.config.InputRulesPatternLoader;
import com.companyname.guardrail.evaluators.Evaluator;
import com.companyname.guardrail.model.EvaluationRequest;
import com.companyname.guardrail.model.EvaluationResponse;
import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service layer for guardrail evaluation operations
 * Handles evaluation orchestration, cross-cutting concerns (MDC), and metrics
 */
@Service
@Slf4j
public class GuardrailService {

    private final List<Evaluator> evaluators;
    private final InputRulesPatternLoader ruleLoader;

    // Metrics
    private final Counter totalRequests;
    private final Counter totalViolations;

    public GuardrailService(List<Evaluator> evaluators, InputRulesPatternLoader ruleLoader, MeterRegistry meterRegistry) {
        this.evaluators = evaluators;
        this.ruleLoader = ruleLoader;
        
        // Initialize Metrics
        this.totalRequests = Counter.builder("guardrail.requests.total")
                .description("Total number of evaluation requests.")
                .register(meterRegistry);
                
        this.totalViolations = Counter.builder("guardrail.violations.total")
                .description("Total number of violations found.")
                .register(meterRegistry);
    }

    /**
     * Evaluate input using guardrail evaluators
     * Sets up MDC context with unique request ID
     */
    public EvaluationResponse evaluateInput(EvaluationRequest request) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        try {
            log.info("Received evaluation request for type: {}. Content length: {}", 
                request.getType(), request.getContent() != null ? request.getContent().length() : 0);

            totalRequests.increment();
            String content = request.getContent();
            InputRulesPatterns rules = ruleLoader.getRules();

            List<Violation> allViolations = evaluators.parallelStream()
                    .flatMap(evaluator -> {
                        try {
                            return evaluator.evaluate(content, rules).stream();
                        } catch (Exception e) {
                            log.error("Evaluator {} failed during execution.", evaluator.getClass().getSimpleName(), e);
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.toList());

            if (!allViolations.isEmpty()) {
                totalViolations.increment(allViolations.size());
            }

            EvaluationResponse response = new EvaluationResponse();
            response.setRequestId(requestId);
            response.setType(request.getType());
            response.setValid(allViolations.isEmpty());
            response.setTimestamp(System.currentTimeMillis());

            if (!allViolations.isEmpty()) {
                response.setViolations(
                    allViolations.stream()
                        .map(v -> new EvaluationResponse.Violation(
                            v.getType().toString(),
                            v.getMessage(),
                            v.getSeverity().toString()
                        ))
                        .collect(Collectors.toList())
                );
                log.warn("Evaluation failed with {} violations.", response.getViolations().size());
            } else {
                log.info("Evaluation successful.");
            }

            return response;
        } finally {
            MDC.clear();
        }
    }
}
