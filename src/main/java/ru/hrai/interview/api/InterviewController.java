package ru.hrai.interview.api;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hrai.interview.service.InterviewService;
@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
 private final InterviewService service; public InterviewController(InterviewService service) { this.service=service; }
 @PostMapping("/generate") public ResponseEntity<InterviewResponse> generate(@Valid @RequestBody InterviewRequest request) { return ResponseEntity.ok(service.generate(request)); }
}
