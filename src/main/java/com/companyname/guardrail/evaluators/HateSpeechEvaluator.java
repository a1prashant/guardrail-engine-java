package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "guardrail.evaluators.hate-speech.enabled", havingValue = "true", matchIfMissing = true)
public class HateSpeechEvaluator implements Evaluator {

    @Override
    public List<Violation> doEvaluate(String input, InputRulesPatterns rules) {
        List<Violation> violations = new ArrayList<>();
        if (input == null || CollectionUtils.isEmpty(rules.getHate_speech_list())) {
            return violations;
        }

        String lowerCaseInput = input.toLowerCase();

        for (String hateSpeech : rules.getHate_speech_list()) {
            if (lowerCaseInput.contains(hateSpeech.toLowerCase())) {
                violations.add(new Violation(
                        Violation.ViolationType.HATE_SPEECH,
                        "Input contains potential hate speech: '" + hateSpeech + "'.",
                        Violation.Severity.CRITICAL
                ));
            }
        }
        return violations;
    }
}
