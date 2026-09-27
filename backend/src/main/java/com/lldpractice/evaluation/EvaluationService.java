package com.lldpractice.evaluation;

import com.lldpractice.attempt.Attempt;
import com.lldpractice.attempt.AttemptService;
import com.lldpractice.evaluator.EvaluationResult;
import com.lldpractice.evaluator.Evaluator;
import com.lldpractice.evaluator.FeedbackItem;
import com.lldpractice.submission.Submission;
import com.lldpractice.submission.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluationService {

    private final SubmissionService submissionService;
    private final Evaluator evaluator;
    private final EvaluationRepository evaluationRepository;
    private final FeedbackItemRepository feedbackItemRepository;
    private final AttemptService attemptService;

    public EvaluationService(
            SubmissionService submissionService,
            Evaluator evaluator,
            EvaluationRepository evaluationRepository,
            FeedbackItemRepository feedbackItemRepository,
            AttemptService attemptService) {

        this.submissionService = submissionService;
        this.evaluator = evaluator;
        this.evaluationRepository = evaluationRepository;
        this.feedbackItemRepository = feedbackItemRepository;
        this.attemptService = attemptService;
    }

@Transactional
public Evaluation evaluate(Long attemptId) {

    Evaluation existingEvaluation =
            evaluationRepository.findByAttemptId(attemptId)
                    .orElse(null);

    if (existingEvaluation != null) {
        return existingEvaluation;
    }

    Submission submission =
            submissionService.getSubmissionByAttemptId(attemptId);

    Attempt attempt = submission.getAttempt();

    attemptService.updateStatus(attemptId, "EVALUATING");

    try {

        EvaluationResult result =
                evaluator.evaluate(
                        attempt.getProblem(),
                        submission
                );

        Evaluation evaluation =
                new Evaluation(
                        attempt,
                        result.getSummary(),
                        "COMPLETED"
                );

        Evaluation savedEvaluation =
                evaluationRepository.save(evaluation);

        for (FeedbackItem item : result.getFeedbackItems()) {

            FeedbackItemEntity feedbackItem =
                    new FeedbackItemEntity(
                            savedEvaluation,
                            item.getCriterion(),
                            item.getSeverity(),
                            item.getObservation(),
                            item.getSuggestion()
                    );

            feedbackItemRepository.save(feedbackItem);
        }

        attemptService.updateStatus(attemptId, "COMPLETED");

        return savedEvaluation;

    } catch (Exception e) {

        attemptService.updateStatus(attemptId, "FAILED");

        throw e;
    }
}
}