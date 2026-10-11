package ru.hrai.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "app.yandex")
public record YandexProperties(String baseUrl, String apiKey, String projectId, String agentId, int connectTimeoutSeconds, int readTimeoutSeconds) {}
