package com.companyname.guardrail.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic evaluation response for all evaluation types
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationResponse {
    
    private String requestId;
    private String type;  // input, output, action, tool, api_request, etc.
    private boolean valid;
    private List<Violation> violations;
    private long timestamp;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Violation {
        private String evaluator;
        private String message;
        private String severity;  // INFO, WARNING, CRITICAL
    }
}
