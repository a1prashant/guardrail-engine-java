package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "guardrail.evaluators.profanity.enabled", havingValue = "true", matchIfMissing = true)
public class ProfanityEvaluator implements Evaluator {

    @Override
    public List<Violation> doEvaluate(String input, InputRulesPatterns rules) {
        List<Violation> violations = new ArrayList<>();
        if (input == null || CollectionUtils.isEmpty(rules.getProfanity_list())) {
            return violations;
        }

        String lowerCaseInput = input.toLowerCase();

        for (String profanity : rules.getProfanity_list()) {
            if (lowerCaseInput.contains(profanity.toLowerCase())) {
                violations.add(new Violation(
                        Violation.ViolationType.PROFANITY,
                        "Input contains profanity: '" + profanity + "'.",
                        Violation.Severity.HIGH
                ));
            }
        }
        return violations;
    }
}
