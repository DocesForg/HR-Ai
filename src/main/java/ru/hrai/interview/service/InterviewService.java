package ru.hrai.interview.service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.hrai.interview.api.InterviewRequest;
import ru.hrai.interview.api.InterviewResponse;
import ru.hrai.llm.AiGateway;
@Service
public class InterviewService {
 private final AiGateway gateway; public InterviewService(AiGateway gateway) { this.gateway=gateway; }
 @Retry(name="yandexAgent") @CircuitBreaker(name="yandexAgent",fallbackMethod="fallback")
 public InterviewResponse generate(InterviewRequest r) {
  String description=r.vacancyDescription().trim();
  String prompt="Ты — опытный HR-эксперт и технический интервьюер. На основе описания вакансии составь сценарий собеседования на русском языке в Markdown. Сгруппируй вопросы по опыту и мотивации, профессиональным навыкам, практическим ситуациям, коммуникации и командной работе. Для каждого вопроса укажи проверяемую компетенцию; для технических вопросов добавь признаки сильного ответа. Не добавляй дискриминационные вопросы и не выдумывай требования.\nДолжность: "+safe(r.position(),"Не указана")+"\nУровень: "+safe(r.seniority(),"Не указан")+"\nФокус: "+safe(r.questionFocus(),"Сбалансированный набор")+"\nОписание вакансии:\n---\n"+description+"\n---";
  String answer=gateway.generateInterviewQuestions(prompt); if(!StringUtils.hasText(answer)) throw new IllegalStateException("AI returned an empty answer"); return new InterviewResponse(answer,"Yandex AI Studio Agent",UUID.randomUUID().toString());
 }
 public InterviewResponse fallback(InterviewRequest r, Throwable ex) { throw new AiUnavailableException("Сервис генерации временно недоступен. Попробуйте ещё раз позже.",ex); }
 private String safe(String v,String fallback) { return StringUtils.hasText(v)?v.trim():fallback; }
}
