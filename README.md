# HR-Ai — Interview Studio

React + TypeScript interface and Java 21 / Spring Boot API for generating interview questions from a job description through Yandex AI Studio Agent.

## Features

- Generates Russian-language interview plans grouped by competencies, with guidance for strong answers.
- Vacancy context: role, seniority, description, and interview focus.
- Markdown output with copy and download actions.
- Example job descriptions, loading/error states, and responsive layout.
- Layered backend: Controller → Service → AiGateway → LlmClient → YandexAgentClient.
- Validation, HTTP timeouts, retry, circuit breaker, Actuator metrics, Prometheus, OpenAPI, and unit tests.

## Configuration and secrets

Required: Java 21, Maven, Node.js 20+ and npm. Docker Engine/Desktop can run the full app.

Copy .env.example to a local .env and set YANDEX_API_KEY. Never commit the real key. You may override YANDEX_PROJECT_ID and YANDEX_AGENT_ID. Defaults match the supplied project and agent configuration.

- YANDEX_API_KEY — secret Yandex AI Studio API key.
- YANDEX_PROJECT_ID — value sent in the OpenAI-Project header.
- YANDEX_AGENT_ID — prompt/agent ID used by the Responses API.
- SERVER_PORT — backend port, default 8080.
- VITE_API_BASE_URL — optional only if configuring a separate frontend origin; do not put secrets in VITE_ variables.

## Run locally

Set YANDEX_API_KEY in your environment, then run the backend from the repository root:

    mvn spring-boot:run

In another terminal:

    cd frontend
    npm install
    npm run dev

Open http://localhost:5173. Vite proxies /api requests to http://localhost:8080. Swagger UI is at http://localhost:8080/swagger-ui.html.

## Docker

Set YANDEX_API_KEY in your shell and run:

    docker compose up --build

Open http://localhost:8080. Compose requires YANDEX_API_KEY to be set.

## API

POST /api/interviews/generate

Request JSON fields:
- vacancyDescription (required, max 12,000 characters)
- position (optional, max 120 characters)
- seniority (optional, max 80 characters)
- questionFocus (optional, max 80 characters)

The response includes questions, provider, and requestId.

- Swagger UI: /swagger-ui.html
- OpenAPI JSON: /v3/api-docs
- Health: /actuator/health
- Metrics: /actuator/metrics
- Prometheus: /actuator/prometheus

## Checks

    mvn test
    cd frontend && npm run build

A real-agent smoke test requires a valid API key and access to the configured Yandex project/agent. Do not claim that integration is verified without running that check.
