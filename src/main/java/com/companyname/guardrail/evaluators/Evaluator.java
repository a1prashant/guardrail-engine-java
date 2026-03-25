package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;

import java.util.List;

/**
 * Interface for all guardrail evaluators. Each evaluator is responsible for
 * checking a specific type of violation in the input text.
 */
public interface Evaluator {

    /**
     * Evaluates the input string against a set of rules.
     *
     * @param input The user-provided text to evaluate.
     * @param rules The loaded configuration of rules and patterns.
     * @return A list of violations found. The list is empty if no violations are detected.
     */
    List<Violation> evaluate(String input, InputRulesPatterns rules);
}
