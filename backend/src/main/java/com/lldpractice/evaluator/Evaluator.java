package com.lldpractice.evaluator;

import com.lldpractice.problem.Problem;
import com.lldpractice.submission.Submission;

public interface Evaluator {

    EvaluationResult evaluate(
            Problem problem,
            Submission submission
    );
}