package com.lldpractice.attempt;

import com.lldpractice.problem.Problem;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    private String status;

    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;

    public Attempt() {
    }

    public Attempt(Problem problem) {
        this.problem = problem;
        this.status = "IN_PROGRESS";
        this.startedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}