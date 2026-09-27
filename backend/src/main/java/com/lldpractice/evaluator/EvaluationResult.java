package com.lldpractice.evaluator;

import java.util.List;

public class EvaluationResult {

    private String summary;

    private List<FeedbackItem> feedbackItems;

    public EvaluationResult(
            String summary,
            List<FeedbackItem> feedbackItems) {

        this.summary = summary;
        this.feedbackItems = feedbackItems;
    }

    public String getSummary() {
        return summary;
    }

    public List<FeedbackItem> getFeedbackItems() {
        return feedbackItems;
    }
}