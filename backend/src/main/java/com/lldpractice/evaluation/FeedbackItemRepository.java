package com.lldpractice.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackItemRepository
        extends JpaRepository<FeedbackItemEntity, Long> {

    List<FeedbackItemEntity> findByEvaluationId(Long evaluationId);
}