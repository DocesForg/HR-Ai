package ru.hrai.common;
import java.time.Instant;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.hrai.interview.service.AiUnavailableException;
@RestControllerAdvice
public class ApiExceptionHandler {
 private static final Logger log=LoggerFactory.getLogger(ApiExceptionHandler.class);
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException ex) { String m=ex.getBindingResult().getFieldErrors().stream().findFirst().map(e->e.getDefaultMessage()).orElse("Проверьте введённые данные"); return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now().toString(),"message",m)); }
 @ExceptionHandler(AiUnavailableException.class) ResponseEntity<?> unavailable(AiUnavailableException ex) { log.warn("AI generation unavailable: {}",ex.getMessage()); return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("timestamp",Instant.now().toString(),"message",ex.getMessage())); }
 @ExceptionHandler(Exception.class) ResponseEntity<?> unexpected(Exception ex) { log.error("Unexpected API error: {}",ex.getClass().getSimpleName()); return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("timestamp",Instant.now().toString(),"message","Не удалось получить ответ от AI. Попробуйте позже.")); }
}
