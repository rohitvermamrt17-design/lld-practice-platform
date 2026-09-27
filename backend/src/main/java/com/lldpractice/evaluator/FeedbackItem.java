package com.lldpractice.evaluator;

public class FeedbackItem {

    private String criterion;
    private String severity;
    private String observation;
    private String suggestion;

    public FeedbackItem(
            String criterion,
            String severity,
            String observation,
            String suggestion) {

        this.criterion = criterion;
        this.severity = severity;
        this.observation = observation;
        this.suggestion = suggestion;
    }

    public String getCriterion() {
        return criterion;
    }

    public String getSeverity() {
        return severity;
    }

    public String getObservation() {
        return observation;
    }

    public String getSuggestion() {
        return suggestion;
    }
}