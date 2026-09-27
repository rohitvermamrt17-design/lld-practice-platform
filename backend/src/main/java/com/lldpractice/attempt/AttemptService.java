package com.lldpractice.attempt;

import com.lldpractice.problem.Problem;
import com.lldpractice.problem.ProblemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final ProblemService problemService;

    public AttemptService(
            AttemptRepository attemptRepository,
            ProblemService problemService) {

        this.attemptRepository = attemptRepository;
        this.problemService = problemService;
    }

    // Start a new practice attempt
    public Attempt startAttempt(Long problemId) {

        Problem problem = problemService.getProblemById(problemId);

        Attempt attempt = new Attempt(problem);

        return attemptRepository.save(attempt);
    }

    // Get a single attempt
    public Attempt getAttemptById(Long id) {

        return attemptRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attempt not found"));
    }

    // Submit an attempt
    public Attempt submitAttempt(Long id) {

        Attempt attempt = getAttemptById(id);

        attempt.setStatus("SUBMITTED");
        attempt.setSubmittedAt(LocalDateTime.now());

        return attemptRepository.save(attempt);
    }

    // Update attempt status
    public Attempt updateStatus(Long id, String status) {

        Attempt attempt = getAttemptById(id);

        attempt.setStatus(status);

        return attemptRepository.save(attempt);
    }

    // Get all attempts for Attempt History
    public List<Attempt> getAllAttempts() {

        return attemptRepository.findAllByOrderByStartedAtDesc();
    }
}