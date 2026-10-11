package ru.hrai.config;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
@Configuration
public class HttpClientConfig {
 @Bean RestClient yandexRestClient(YandexProperties p) { var f = new SimpleClientHttpRequestFactory(); f.setConnectTimeout(Duration.ofSeconds(p.connectTimeoutSeconds())); f.setReadTimeout(Duration.ofSeconds(p.readTimeoutSeconds())); return RestClient.builder().baseUrl(p.baseUrl()).requestFactory(f).build(); }
}
