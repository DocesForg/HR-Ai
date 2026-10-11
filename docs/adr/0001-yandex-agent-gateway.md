# ADR 0001: Yandex AI Studio Agent through an LLM Gateway

- Status: Accepted
- Date: 2026-10-11

## Context
The application generates interview questions from job descriptions. It must use Yandex AI Studio Agent while keeping business logic independent from the LLM vendor.

## Decision
The Spring Boot backend calls the Responses API through YandexAgentClient, which implements LlmClient. AiGateway is the abstraction consumed by InterviewService; InterviewService owns prompt construction and product rules. Credentials are supplied through environment variables. React calls the backend and never calls Yandex directly.

## Consequences
- API credentials are not shipped to the browser.
- Another provider can be added as a separate LlmClient implementation.
- Network calls are centralized for timeout, retry, circuit breaker, and metrics support.
- A valid API key and access to the configured agent are required for a live smoke test.
