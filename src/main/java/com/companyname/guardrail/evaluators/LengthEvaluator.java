package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "guardrail.evaluators.length.enabled", havingValue = "true", matchIfMissing = true)
public class LengthEvaluator implements Evaluator {

    @Override
    public List<Violation> doEvaluate(String input, InputRulesPatterns rules) {
        List<Violation> violations = new ArrayList<>();
        int maxLength = rules.getLimits().getMax_input_length();

        if (input != null && input.length() > maxLength) {
            violations.add(new Violation(
                    Violation.ViolationType.LENGTH,
                    "Input exceeds maximum length of " + maxLength + " characters.",
                    Violation.Severity.MEDIUM
            ));
        }
        return violations;
    }
}
