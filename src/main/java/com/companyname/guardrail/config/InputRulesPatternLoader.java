package com.companyname.guardrail.config;

import com.companyname.guardrail.model.InputRulesPatterns;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;

@Component
@Slf4j
public class InputRulesPatternLoader {

    @Value("classpath:config/input-rules-patterns.yaml")
    private Resource rulesResource;

    private InputRulesPatterns rules;

    @PostConstruct
    public void init() throws IOException {
        log.info("Loading guardrail rules from YAML...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        try {
            this.rules = mapper.readValue(rulesResource.getInputStream(), InputRulesPatterns.class);
            validateRules(this.rules);
            log.info("Successfully loaded and validated guardrail rules version: {}", rules.getMetadata().getVersion());
        } catch (IOException e) {
            log.error("Failed to load or parse guardrail rules YAML file. Application startup failed.", e);
            throw e; // Fail-fast
        }
    }

    public InputRulesPatterns getRules() {
        return this.rules;
    }

    private void validateRules(InputRulesPatterns rules) {
        Assert.notNull(rules, "Rules object cannot be null.");
        Assert.notNull(rules.getMetadata(), "Metadata cannot be null.");
        if (rules.getLimits() != null) {
            Assert.isTrue(rules.getLimits().getMax_input_length() > 0, "Max input length must be positive.");
        }
        log.debug("Basic rule validation passed.");
    }
}
