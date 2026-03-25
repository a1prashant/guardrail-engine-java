package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "guardrail.evaluators.prompt-injection.enabled", havingValue = "true", matchIfMissing = true)
public class PromptInjectionEvaluator implements Evaluator {

    @Override
    public List<Violation> evaluate(String input, InputRulesPatterns rules) {
        List<Violation> violations = new ArrayList<>();
        if (input == null || CollectionUtils.isEmpty(rules.getPrompt_injection_patterns())) {
            return violations;
        }

        String lowerCaseInput = input.toLowerCase().trim();

        for (String pattern : rules.getPrompt_injection_patterns()) {
            if (lowerCaseInput.contains(pattern.toLowerCase())) {
                violations.add(new Violation(
                        Violation.ViolationType.PROMPT_INJECTION,
                        "Input contains a potential prompt injection pattern.",
                        Violation.Severity.CRITICAL
                ));
                // Typically, we can stop after finding one injection attempt.
                return violations;
            }
        }
        return violations;
    }
}
