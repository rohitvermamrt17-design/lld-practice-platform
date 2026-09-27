package com.lldpractice.submission;

import com.lldpractice.attempt.Attempt;
import jakarta.persistence.*;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "attempt_id", nullable = false)
    private Attempt attempt;

    private String format;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Submission() {
    }

    public Submission(Attempt attempt, String format, String content) {
        this.attempt = attempt;
        this.format = format;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    public String getFormat() {
        return format;
    }

    public String getContent() {
        return content;
    }

    public void setAttempt(Attempt attempt) {
        this.attempt = attempt;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setContent(String content) {
        this.content = content;
    }
}