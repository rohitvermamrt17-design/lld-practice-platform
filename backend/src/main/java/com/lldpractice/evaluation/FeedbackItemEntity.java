package com.lldpractice.evaluation;

import jakarta.persistence.*;

@Entity
public class FeedbackItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    private String criterion;

    private String severity;

    @Column(columnDefinition = "TEXT")
    private String observation;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    public FeedbackItemEntity() {
    }

    public FeedbackItemEntity(
            Evaluation evaluation,
            String criterion,
            String severity,
            String observation,
            String suggestion) {

        this.evaluation = evaluation;
        this.criterion = criterion;
        this.severity = severity;
        this.observation = observation;
        this.suggestion = suggestion;
    }

    public Long getId() {
        return id;
    }

    public Evaluation getEvaluation() {
        return evaluation;
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