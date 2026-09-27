package com.lldpractice.api;

import com.lldpractice.evaluation.Evaluation;
import com.lldpractice.evaluation.EvaluationRepository;
import com.lldpractice.evaluation.EvaluationService;
import com.lldpractice.evaluation.FeedbackItemEntity;
import com.lldpractice.evaluation.FeedbackItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "http://localhost:5173")
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final EvaluationRepository evaluationRepository;
    private final FeedbackItemRepository feedbackItemRepository;

    public EvaluationController(
            EvaluationService evaluationService,
            EvaluationRepository evaluationRepository,
            FeedbackItemRepository feedbackItemRepository) {

        this.evaluationService = evaluationService;
        this.evaluationRepository = evaluationRepository;
        this.feedbackItemRepository = feedbackItemRepository;
    }

    @PostMapping("/{attemptId}")
    public EvaluationResponse evaluate(
            @PathVariable Long attemptId) {

        Evaluation evaluation =
                evaluationService.evaluate(attemptId);

        List<FeedbackItemEntity> feedbackItems =
                feedbackItemRepository.findByEvaluationId(
                        evaluation.getId()
                );

        return new EvaluationResponse(
                evaluation,
                feedbackItems
        );
    }

    @GetMapping("/{attemptId}")
    public EvaluationResponse getEvaluation(
            @PathVariable Long attemptId) {

        Evaluation evaluation =
                evaluationRepository.findByAttemptId(attemptId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Evaluation not found"
                                ));

        List<FeedbackItemEntity> feedbackItems =
                feedbackItemRepository.findByEvaluationId(
                        evaluation.getId()
                );

        return new EvaluationResponse(
                evaluation,
                feedbackItems
        );
    }
}