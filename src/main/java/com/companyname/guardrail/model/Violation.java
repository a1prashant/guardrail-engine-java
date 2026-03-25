package com.companyname.guardrail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Violation {
    private ViolationType type;
    private String message;
    private Severity severity;

    public enum ViolationType {
        PII,
        PROFANITY,
        HATE_SPEECH,
        PROMPT_INJECTION,
        LENGTH
    }

    public enum Severity {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}
