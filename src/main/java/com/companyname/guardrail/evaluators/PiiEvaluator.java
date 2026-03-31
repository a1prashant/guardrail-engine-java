package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import com.companyname.guardrail.observability.AiModerationClient;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ConditionalOnProperty(name = "guardrail.evaluators.pii.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class PiiEvaluator implements Evaluator {

    // Tier 3 Client (optional, can be mocked)
    private final AiModerationClient aiModerationClient;

    @Override
    public List<Violation> doEvaluate(String input, InputRulesPatterns rules) {
        List<Violation> violations = new ArrayList<>();
        if (input == null) {
            return violations;
        }

        // Tier 1: Regex-based detection
        if (!CollectionUtils.isEmpty(rules.getPii_patterns())) {
            violations.addAll(evaluateWithRegex(input, rules.getPii_patterns()));
        }

        // Tier 2: Library-based detection
        violations.addAll(evaluateWithLibraries(input));


        // Tier 3: AI/ML-based detection (optional)
        if (aiModerationClient.isEnabled()) {
            try {
                if (aiModerationClient.containsPII(input)) {
                    violations.add(new Violation(
                            Violation.ViolationType.PII,
                            "AI model detected potential PII.",
                            Violation.Severity.HIGH
                    ));
                }
            } catch (Exception e) {
                log.warn("Tier 3 AI/ML PII check failed.", e);
            }
        }

        return violations;
    }

    private List<Violation> evaluateWithRegex(String input, Map<String, String> piiPatterns) {
        List<Violation> violations = new ArrayList<>();
        for (Map.Entry<String, String> entry : piiPatterns.entrySet()) {
            String piiType = entry.getKey();
            String regex = entry.getValue();
            try {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    violations.add(new Violation(
                            Violation.ViolationType.PII,
                            "Potential PII detected: " + piiType,
                            Violation.Severity.HIGH
                    ));
                }
            } catch (Exception e) {
                log.error("Invalid regex pattern for PII type: {}", piiType, e);
            }
        }
        return violations;
    }

    private List<Violation> evaluateWithLibraries(String input) {
        List<Violation> violations = new ArrayList<>();

        // Using Apache Commons Validator for email
        if (EmailValidator.getInstance().isValid(input)) {
             violations.add(new Violation(
                Violation.ViolationType.PII,
                "Potential PII detected: Email Address",
                Violation.Severity.HIGH
            ));
        }

        // Using Google's libphonenumber
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        if (phoneUtil.findNumbers(input, "US").iterator().hasNext()) {
             violations.add(new Violation(
                Violation.ViolationType.PII,
                "Potential PII detected: Phone Number",
                Violation.Severity.HIGH
            ));
        }

        return violations;
    }
}
