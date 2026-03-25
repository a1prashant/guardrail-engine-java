package com.companyname.guardrail.observability;

public interface AiModerationClient {
    /**
     * Checks if the client is configured and enabled.
     * @return true if enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Asks an external AI service if the text contains PII.
     * @param text The input text.
     * @return true if the service flags it for PII.
     */
    boolean containsPII(String text);

    /**
     * Asks an external AI service if the text is toxic.
     * @param text The input text.
     * @return true if the service flags it as toxic.
     */
    boolean isToxic(String text);
}
