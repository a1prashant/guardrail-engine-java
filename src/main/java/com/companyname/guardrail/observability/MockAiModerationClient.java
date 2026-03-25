package com.companyname.guardrail.observability;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mock implementation of the AI Moderation client.
 * This implementation is for demonstration purposes and does not make external calls.
 * It can be replaced by a real implementation that connects to services like
 * OpenAI, Perspective API, or a HuggingFace model.
 */
@Component
@Slf4j
public class MockAiModerationClient implements AiModerationClient {

    @Override
    public boolean isEnabled() {
        // In a real scenario, this would check for API keys or feature flags.
        return false;
    }

    @Override
    public boolean containsPII(String text) {
        log.info("[MOCK] AI client checking for PII. Returning false by default.");
        // In a real implementation, you would make an API call here.
        // Example: POST to OpenAI's moderation endpoint.
        return false;
    }

    @Override
    public boolean isToxic(String text) {
        log.info("[MOCK] AI client checking for toxicity. Returning false by default.");
        // In a real implementation, you would make an API call here.
        return false;
    }
}
