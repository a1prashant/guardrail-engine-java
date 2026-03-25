package com.companyname.guardrail.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class InputRulesPatterns {
    private Metadata metadata;
    private List<String> profanity_list;
    private List<String> hate_speech_list;
    private Map<String, String> pii_patterns;
    private List<String> prompt_injection_patterns;
    private Limits limits;

    @Data
    @NoArgsConstructor
    public static class Metadata {
        private String version;
        private String description;
    }

    @Data
    @NoArgsConstructor
    public static class Limits {
        private int max_input_length;
    }

}
