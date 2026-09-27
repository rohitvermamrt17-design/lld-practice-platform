package com.lldpractice.evaluator;

import com.lldpractice.problem.Problem;
import com.lldpractice.submission.Submission;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeterministicEvaluatorTest {

    private final DeterministicEvaluator evaluator =
            new DeterministicEvaluator();

    private final Problem parkingLot = new Problem(
            "Parking Lot",
            "Easy",
            "Design a parking lot system.",
            "Support multiple vehicle types, parking spots, entry and exit, and fee calculation."
    );

    @Test
    void shouldGiveGoodFeedbackForCompleteDesign() {

        Submission submission = new Submission(
                null,
                "TEXT",
                """
                Requirements:
                Support multiple vehicle types and parking spots.

                Responsibilities:
                ParkingLot manages parking spots and vehicles.

                Relationships:
                Vehicle is assigned to a ParkingSpot.

                Entry and Exit:
                Vehicle enters and exits the parking lot.

                Fee Calculation:
                Calculate parking fee based on duration.
                """
        );

        EvaluationResult result =
                evaluator.evaluate(parkingLot, submission);

        assertNotNull(result);
        assertNotNull(result.getSummary());
        assertFalse(result.getFeedbackItems().isEmpty());

        long goodCount = result.getFeedbackItems()
                .stream()
                .filter(item -> "GOOD".equals(item.getSeverity()))
                .count();

        assertTrue(goodCount > 0);
    }

    @Test
    void shouldDetectMissingSubmission() {

        Submission submission = new Submission(
                null,
                "TEXT",
                ""
        );

        EvaluationResult result =
                evaluator.evaluate(parkingLot, submission);

        assertNotNull(result);

        assertTrue(
                result.getFeedbackItems()
                        .stream()
                        .anyMatch(item ->
                                "MISSING".equals(item.getSeverity()))
        );
    }

    @Test
    void shouldWarnWhenImportantConceptsAreMissing() {

        Submission submission = new Submission(
                null,
                "TEXT",
                """
                I will create a ParkingLot class.
                It will store some information about vehicles.
                """
        );

        EvaluationResult result =
                evaluator.evaluate(parkingLot, submission);

        assertNotNull(result);

        assertTrue(
                result.getFeedbackItems()
                        .stream()
                        .anyMatch(item ->
                                "WARNING".equals(item.getSeverity())
                                || "MISSING".equals(item.getSeverity()))
        );
    }
}