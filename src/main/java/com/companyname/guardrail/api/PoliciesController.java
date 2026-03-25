package com.companyname.guardrail.api;

import com.companyname.guardrail.config.InputRulesPatternLoader;
import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.PoliciesResponse;
import com.companyname.guardrail.service.PoliciesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.companyname.guardrail.api.ApiConstants.*;

/**
 * API controller for policies, rules and patterns
 */
@RestController
@RequestMapping(API_V1_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class PoliciesController {

    private final InputRulesPatternLoader ruleLoader;
    private final PoliciesService policiesService;

    @GetMapping(POLICIES_ENDPOINT)
    public ResponseEntity<PoliciesResponse> getPolicies() {
        log.info("Serving guardrail policies with rules and patterns.");
        InputRulesPatterns rules = ruleLoader.getRules();
        PoliciesResponse response = policiesService.buildPoliciesResponse(rules);
        return ResponseEntity.ok(response);
    }
}
