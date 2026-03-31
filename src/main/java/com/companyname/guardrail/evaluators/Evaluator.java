package com.companyname.guardrail.evaluators;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.companyname.guardrail.model.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface Evaluator {

    Logger log = LoggerFactory.getLogger(Evaluator.class);

    /** Implement evaluation logic — called by the safe {@link #evaluate} wrapper. */
    List<Violation> doEvaluate(String input, InputRulesPatterns rules);

    /** Calls {@link #doEvaluate}, catches and logs any exception, returns empty list on failure. */
    default List<Violation> evaluate(String input, InputRulesPatterns rules) {
        try {
            return doEvaluate(input, rules);
        } catch (Exception e) {
            log.error("Evaluator {} failed: {}", getClass().getSimpleName(), e.getMessage(), e);
            return List.of();
        }
    }
}
