package com.companyname.guardrail.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic evaluation request for all evaluation types (input, output, action, tool, api_request, etc.)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequest {
    
    @NotBlank(message = "Evaluation type cannot be blank")
    private String type;  // input, output, action, tool, api_request, etc.
    
    @NotBlank(message = "Content cannot be blank")
    private String content;
    
    private String metadata;  // Optional metadata for context
}
