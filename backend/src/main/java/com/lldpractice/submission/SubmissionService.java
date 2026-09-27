package com.lldpractice.submission;

import com.lldpractice.attempt.Attempt;
import com.lldpractice.attempt.AttemptService;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AttemptService attemptService;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            AttemptService attemptService) {
        this.submissionRepository = submissionRepository;
        this.attemptService = attemptService;
    }

    public Submission createSubmission(
            Long attemptId,
            String format,
            String content) {

        Attempt attempt = attemptService.getAttemptById(attemptId);

        Submission submission =
                new Submission(attempt, format, content);

        return submissionRepository.save(submission);
    }

    public Submission getSubmissionByAttemptId(Long attemptId) {
        return submissionRepository.findByAttemptId(attemptId)
                .orElseThrow(() ->
                        new RuntimeException("Submission not found"));
    }
}