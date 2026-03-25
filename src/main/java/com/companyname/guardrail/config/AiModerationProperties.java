package com.companyname.guardrail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for AI moderation services
 * Controls which external AI services are enabled/disabled
 */
@Data
@Component
@ConfigurationProperties(prefix = "guardrail.ai-moderation")
public class AiModerationProperties {
    
    private OpenAiConfig openai = new OpenAiConfig();
    private PerspectiveConfig perspective = new PerspectiveConfig();
    
    @Data
    public static class OpenAiConfig {
        private boolean enabled = false;
        private String apiKey;
        private String moderationUrl;
    }
    
    @Data
    public static class PerspectiveConfig {
        private boolean enabled = false;
        private String apiKey;
        private String moderationUrl;
    }
}
