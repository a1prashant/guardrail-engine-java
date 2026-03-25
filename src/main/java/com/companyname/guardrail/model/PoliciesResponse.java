package com.companyname.guardrail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * API Response for policies and their rules
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliciesResponse {
    private Metadata metadata;
    private InputPolicyResponse input;
    private OutputPolicyResponse output;
    private ActionPolicyResponse action;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private String version;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputPolicyResponse {
        private String name;
        private String description;
        private boolean enabled;
        private InputRules rules;
        private Limits limits;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class InputRules {
            private List<String> profanity;
            private List<String> hate_speech;
            private Map<String, String> pii_patterns;
            private List<String> prompt_injection;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Limits {
            private int max_input_length;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutputPolicyResponse {
        private String name;
        private String description;
        private boolean enabled;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionPolicyResponse {
        private String name;
        private String description;
        private boolean enabled;
    }
}
