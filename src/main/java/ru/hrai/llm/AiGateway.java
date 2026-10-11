package ru.hrai.llm;
import org.springframework.stereotype.Component;
@Component
public class AiGateway { private final LlmClient client; public AiGateway(LlmClient client) { this.client = client; } public String generateInterviewQuestions(String prompt) { return client.generate(prompt); } }
