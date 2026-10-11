package ru.hrai.interview.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import ru.hrai.interview.api.InterviewRequest;
import ru.hrai.llm.AiGateway;
class InterviewServiceTest {
 @Test void generatesQuestionsThroughGateway() {
  AiGateway gateway=mock(AiGateway.class); when(gateway.generateInterviewQuestions(anyString())).thenReturn("## Технические вопросы\n1. Расскажите об опыте.");
  InterviewService service=new InterviewService(gateway);
  var result=service.generate(new InterviewRequest("Java, Spring Boot, REST API","Java-разработчик","Middle","Практика"));
  assertTrue(result.questions().contains("Технические вопросы")); assertEquals("Yandex AI Studio Agent",result.provider());
  verify(gateway).generateInterviewQuestions(argThat(p->p.contains("Java-разработчик")&&p.contains("Java, Spring Boot, REST API")));
 }
 @Test void rejectsBlankGeneratedText() {
  AiGateway gateway=mock(AiGateway.class); when(gateway.generateInterviewQuestions(anyString())).thenReturn(" ");
  InterviewService service=new InterviewService(gateway);
  assertThrows(IllegalStateException.class,()->service.generate(new InterviewRequest("Описание вакансии",null,null,null)));
 }
}
