package com.lldpractice.api;

import com.lldpractice.evaluation.Evaluation;
import com.lldpractice.evaluation.FeedbackItemEntity;

import java.util.List;

public record EvaluationResponse(
        Evaluation evaluation,
        List<FeedbackItemEntity> feedbackItems
) {
}