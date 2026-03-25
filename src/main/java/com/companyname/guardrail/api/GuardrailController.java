package com.companyname.guardrail.api;

import com.companyname.guardrail.model.EvaluationRequest;
import com.companyname.guardrail.model.EvaluationResponse;
import com.companyname.guardrail.service.GuardrailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.companyname.guardrail.api.ApiConstants.*;

/**
 * API controller for evaluation operations
 * Supports multiple evaluation types: input, output, action, tool, api_request
 */
@RestController
@RequestMapping(API_V1_PREFIX + EVALUATE_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class GuardrailController {

    private final GuardrailService guardrailService;

    @PostMapping(EVALUATE_INPUT_ENDPOINT)
    public ResponseEntity<EvaluationResponse> evaluateInput(@Valid @RequestBody EvaluationRequest request) {
        log.info("Evaluating input.");
        EvaluationResponse response = guardrailService.evaluateInput(request);
        return ResponseEntity.ok(response);
    }
}
