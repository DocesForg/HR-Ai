# README-AI.md — HR-Ai engineering rules

## Product
HR-Ai generates a structured interview plan from a job description. Use Russian by default. Keep questions job-related, practical, and non-discriminatory.

## Architecture constraints
- Preserve Controller → Service → AiGateway → LlmClient → provider client layering.
- Controllers handle HTTP and DTOs only. Prompt construction and business rules belong in the service layer.
- Keep DTOs separate from persistence/domain entities.
- Do not couple business logic directly to Yandex API; a new provider should implement LlmClient.
- Never commit API keys or secrets, and never put them in application.yml, documentation, or frontend assets.
- Do not log vacancy text, model output, PII, API tokens, or authorization headers.

## Reliability
- All external HTTP calls need connect/read timeouts.
- Use bounded retries for transient errors and circuit breakers to avoid cascading failures.
- Never expose stack traces or secrets to API clients.
- Preserve required-field and maximum-length validation.

## Tests and docs
- Add JUnit/Mockito unit tests for service logic and integration tests when API contracts change.
- Update OpenAPI and README when contracts change.
- Prefer small, focused changes and minimal dependency additions.
- Verify mvn test and frontend npm run build before claiming the build passes.

## UI
- Split growing React functionality into separate components.
- Keep accessible labels, visible focus states, loading/error states, and responsive layouts.
- Never put real credentials in VITE_ variables; they are exposed to the browser.
