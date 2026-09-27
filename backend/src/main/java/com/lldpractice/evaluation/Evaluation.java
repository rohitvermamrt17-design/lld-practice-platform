package com.lldpractice.evaluation;

import com.lldpractice.attempt.Attempt;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "attempt_id", nullable = false)
    private Attempt attempt;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String status;

    private LocalDateTime createdAt;

    public Evaluation() {
    }

    public Evaluation(Attempt attempt, String summary, String status) {
        this.attempt = attempt;
        this.summary = summary;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    public String getSummary() {
        return summary;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}