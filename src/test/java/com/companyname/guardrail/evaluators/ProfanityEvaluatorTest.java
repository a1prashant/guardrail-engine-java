package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfanityEvaluatorTest {

    private ProfanityEvaluator evaluator;
    private InputRulesPatterns rules;

    @BeforeEach
    void setUp() {
        evaluator = new ProfanityEvaluator();
        rules = new InputRulesPatterns();
        rules.setProfanity_list(List.of("inappropriate-word1", "offensive-term2"));
    }

    @Test
    void testEvaluate_NoProfanity() {
        List<Violation> violations = evaluator.evaluate("This is a clean text.", rules);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEvaluate_WithProfanity() {
        List<Violation> violations = evaluator.evaluate("This text contains inappropriate-word1.", rules);
        assertEquals(1, violations.size());
        assertEquals(Violation.ViolationType.PROFANITY, violations.get(0).getType());
        assertEquals(Violation.Severity.HIGH, violations.get(0).getSeverity());
    }

    @Test
    void testEvaluate_WithMultipleProfanity() {
        List<Violation> violations = evaluator.evaluate("This has inappropriate-word1 and offensive-term2.", rules);
        assertEquals(2, violations.size());
    }

    @Test
    void testEvaluate_CaseInsensitive() {
        List<Violation> violations = evaluator.evaluate("This text has INAPPROPRIATE-WORD1.", rules);
        assertEquals(1, violations.size());
        assertEquals(Violation.ViolationType.PROFANITY, violations.get(0).getType());
    }

    @Test
    void testEvaluate_NullInput() {
        List<Violation> violations = evaluator.evaluate(null, rules);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEvaluate_EmptyRules() {
        rules.setProfanity_list(List.of());
        List<Violation> violations = evaluator.evaluate("This text contains inappropriate-word1.", rules);
        assertTrue(violations.isEmpty());
    }
}
