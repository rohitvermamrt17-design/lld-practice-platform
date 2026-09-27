package com.lldpractice.api;

import com.lldpractice.attempt.Attempt;
import com.lldpractice.attempt.AttemptService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempts")
@CrossOrigin(origins = "http://localhost:5173")
public class AttemptController {

    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping("/start/{problemId}")
    public Attempt startAttempt(@PathVariable Long problemId) {
        return attemptService.startAttempt(problemId);
    }

    @GetMapping("/{id}")
    public Attempt getAttempt(@PathVariable Long id) {
        return attemptService.getAttemptById(id);
    }

    @PostMapping("/{id}/submit")
    public Attempt submitAttempt(@PathVariable Long id) {
        return attemptService.submitAttempt(id);
    }

    @PostMapping("/{id}/complete")
    public Attempt completeAttempt(@PathVariable Long id) {
        return attemptService.updateStatus(id, "COMPLETED");
    }
    @GetMapping
public java.util.List<Attempt> getAllAttempts() {
    return attemptService.getAllAttempts();
}
}