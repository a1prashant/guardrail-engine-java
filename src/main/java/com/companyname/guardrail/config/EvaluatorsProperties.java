package com.companyname.guardrail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for guardrail evaluators
 * Controls which evaluators are enabled/disabled
 */
@Data
@Component
@ConfigurationProperties(prefix = "guardrail.evaluators")
public class EvaluatorsProperties {
    
    private EvaluatorConfig profanity = new EvaluatorConfig();
    private EvaluatorConfig hateSpeech = new EvaluatorConfig();
    private EvaluatorConfig pii = new EvaluatorConfig();
    private EvaluatorConfig promptInjection = new EvaluatorConfig();
    private EvaluatorConfig length = new EvaluatorConfig();
    
    @Data
    public static class EvaluatorConfig {
        private boolean enabled = true;
        private String description;
    }
}
