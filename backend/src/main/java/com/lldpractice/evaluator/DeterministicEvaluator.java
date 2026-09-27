package com.lldpractice.evaluator;

import com.lldpractice.problem.Problem;
import com.lldpractice.submission.Submission;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeterministicEvaluator implements Evaluator {

    @Override
    public EvaluationResult evaluate(
            Problem problem,
            Submission submission) {

        List<FeedbackItem> feedback = new ArrayList<>();

        String content = submission.getContent();

        if (content == null || content.isBlank()) {
            feedback.add(new FeedbackItem(
                    "SUBMISSION_COMPLETENESS",
                    "MISSING",
                    "The submission is empty.",
                    "Explain the main classes, responsibilities, relationships, and design decisions."
            ));

            return new EvaluationResult(
                    "The submission needs more design information.",
                    feedback
            );
        }

        String text = content.toLowerCase();

        checkKeyword(
                feedback,
                text,
                "Vehicle Types",
                List.of("vehicle", "vehicle types"),
                "Mention how different vehicle types are represented and handled."
        );

        checkKeyword(
                feedback,
                text,
                "Parking Spots",
                List.of("parking spot", "parking spots"),
                "Explain how parking spots are represented and how availability is managed."
        );

        checkKeyword(
                feedback,
                text,
                "Entry and Exit",
                List.of("entry", "exit"),
                "Explain the flow when a vehicle enters and leaves the parking lot."
        );

        checkKeyword(
                feedback,
                text,
                "Fee Calculation",
                List.of("fee", "fee calculation"),
                "Explain which class or abstraction is responsible for calculating parking fees."
        );

        checkKeyword(
                feedback,
                text,
                "Responsibilities",
                List.of("responsibilit", "manages", "handles"),
                "Clearly describe the responsibility of each important class."
        );

        checkKeyword(
                feedback,
                text,
                "Relationships",
                List.of("relationship", "represents", "class"),
                "Explain how the main classes interact with each other."
        );

        String summary;

        if (feedback.isEmpty()) {
            summary = "The submission covers the main Parking Lot requirements.";
        } else {
            summary = "The submission was reviewed against the core Parking Lot design requirements.";
        }

        return new EvaluationResult(summary, feedback);
    }

    private void checkKeyword(
            List<FeedbackItem> feedback,
            String text,
            String criterion,
            List<String> keywords,
            String suggestion) {

        boolean found = keywords.stream()
                .anyMatch(text::contains);

        if (found) {
            feedback.add(new FeedbackItem(
                    criterion,
                    "GOOD",
                    "The submission contains evidence related to " + criterion + ".",
                    "Continue explaining this design decision with clear class responsibilities."
            ));
        } else {
            feedback.add(new FeedbackItem(
                    criterion,
                    "WARNING",
                    "The submission does not clearly mention " + criterion + ".",
                    suggestion
            ));
        }
    }
}