# guardrail-engine-java

Input guardrail engine built with **Java 21** · **Spring Boot 3.2.4** · **Maven**

Evaluates text content against configurable rules before it reaches an LLM or downstream service. Evaluators run in parallel and are individually enable/disable via `application.yaml`.

## Evaluators

| Evaluator | Violation type | Severity |
|---|---|---|
| Profanity | `PROFANITY` | HIGH |
| Hate speech | `HATE_SPEECH` | CRITICAL |
| PII (regex + library + optional AI) | `PII` | HIGH |
| Prompt injection | `PROMPT_INJECTION` | CRITICAL |
| Length | `LENGTH` | MEDIUM |

Rules and patterns live in `src/main/resources/config/input-rules-patterns.yaml`.

## API

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/evaluate/input` | Evaluate content |
| `GET` | `/api/v1/policies` | Active rules and patterns |
| `GET` | `/api/v1/health` | Health check |
| `GET` | `/api/v1/ready` | Readiness probe |

**Request**
```json
{ "type": "input", "content": "text to evaluate", "metadata": null }
```

**Response**
```json
{
  "requestId": "uuid",
  "type": "input",
  "valid": false,
  "violations": [{ "evaluator": "PROFANITY", "message": "...", "severity": "HIGH" }],
  "timestamp": 1234567890
}
```

## Commands

```bash
# Build
mvn clean compile
mvn clean package [-DskipTests]

# Test
mvn test
mvn test -Dtest=ProfanityEvaluatorTest
mvn test -Dtest=GuardrailEngineIntegrationTest#evaluateInput_withPii_returnsViolation

# Run
mvn spring-boot:run
java -jar target/guardrail-engine-java-0.0.1-SNAPSHOT.jar [--server.port=8081]

# Dependencies
mvn dependency:tree
mvn versions:display-dependency-updates
```

If `JAVA_HOME` is not set to Java 21:
```bash
export JAVA_HOME=/Users/a1prashant/.jdk/jdk-21.0.8/jdk-21.0.8+9/Contents/Home
```

## Configuration

Toggle evaluators in `application.yaml`:
```yaml
guardrail:
  evaluators:
    profanity.enabled: true
    hate-speech.enabled: true
    pii.enabled: true
    prompt-injection.enabled: true
    length.enabled: true
```

Optional AI moderation (Tier 3 PII) via `OPENAI_API_KEY` or `PERSPECTIVE_API_KEY` env vars — disabled by default.
