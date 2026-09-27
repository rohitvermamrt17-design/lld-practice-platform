package com.lldpractice.api;

import com.lldpractice.submission.Submission;
import com.lldpractice.submission.SubmissionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "http://localhost:5173")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/{attemptId}")
    public Submission createSubmission(
            @PathVariable Long attemptId,
            @RequestBody SubmissionRequest request) {

        return submissionService.createSubmission(
                attemptId,
                request.format(),
                request.content()
        );
    }

    @GetMapping("/attempt/{attemptId}")
    public Submission getSubmission(
            @PathVariable Long attemptId) {

        return submissionService.getSubmissionByAttemptId(attemptId);
    }

    public record SubmissionRequest(
            String format,
            String content
    ) {
    }
}