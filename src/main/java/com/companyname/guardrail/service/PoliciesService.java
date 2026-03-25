package com.companyname.guardrail.service;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.PoliciesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service layer for building policy and pattern responses
 */
@Service
@Slf4j
public class PoliciesService {

    /**
     * Build policies response from input rules patterns
     */
    public PoliciesResponse buildPoliciesResponse(InputRulesPatterns rules) {
        log.debug("Building policies response");
        PoliciesResponse response = new PoliciesResponse();

        // Metadata
        PoliciesResponse.Metadata metadata = new PoliciesResponse.Metadata();
        metadata.setVersion(rules.getMetadata().getVersion());
        metadata.setDescription("Guardrail policies configuration");
        response.setMetadata(metadata);

        // Input Policy
        PoliciesResponse.InputPolicyResponse inputPolicy = new PoliciesResponse.InputPolicyResponse();
        inputPolicy.setName("input_validation");
        inputPolicy.setDescription("Validates and sanitizes user inputs");
        inputPolicy.setEnabled(true);

        PoliciesResponse.InputPolicyResponse.InputRules inputRules = new PoliciesResponse.InputPolicyResponse.InputRules();
        inputRules.setProfanity(rules.getProfanity_list() != null ? rules.getProfanity_list() : java.util.List.of());
        inputRules.setHate_speech(rules.getHate_speech_list() != null ? rules.getHate_speech_list() : java.util.List.of());
        inputRules.setPii_patterns(rules.getPii_patterns() != null ? rules.getPii_patterns() : java.util.Map.of());
        inputRules.setPrompt_injection(rules.getPrompt_injection_patterns() != null ? rules.getPrompt_injection_patterns() : java.util.List.of());
        inputPolicy.setRules(inputRules);

        if (rules.getLimits() != null) {
            PoliciesResponse.InputPolicyResponse.Limits limits = new PoliciesResponse.InputPolicyResponse.Limits();
            limits.setMax_input_length(rules.getLimits().getMax_input_length());
            inputPolicy.setLimits(limits);
        }
        response.setInput(inputPolicy);

        // Output Policy (placeholder)
        PoliciesResponse.OutputPolicyResponse outputPolicy = new PoliciesResponse.OutputPolicyResponse();
        outputPolicy.setName("output_validation");
        outputPolicy.setDescription("Validates and filters AI-generated outputs");
        outputPolicy.setEnabled(false);
        response.setOutput(outputPolicy);

        // Action Policy (placeholder)
        PoliciesResponse.ActionPolicyResponse actionPolicy = new PoliciesResponse.ActionPolicyResponse();
        actionPolicy.setName("action_control");
        actionPolicy.setDescription("Restricts or allows specific system actions");
        actionPolicy.setEnabled(false);
        response.setAction(actionPolicy);

        return response;
    }
}
