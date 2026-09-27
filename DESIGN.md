# Design Note — LLD Coach

## 1. Overview

LLD Coach is implemented as a modular monolith using React, Spring Boot, and PostgreSQL.

The system is divided into domain-oriented modules:

- Problem
- Attempt
- Submission
- Evaluation
- Evaluator
- API

The design keeps business responsibilities separated while avoiding unnecessary distributed-system complexity.

## 2. High-Level Architecture

React Frontend
        |
        | HTTP REST API
        v
Spring Boot Application
        |
        +-------------------+
        |                   |
        v                   v
    Domain Modules     Evaluation Engine
        |                   |
        |             +-----+------+
        |             |            |
        |             v            v
        |      Deterministic   LLM Evaluator
        |        Evaluator        Seam
        |
        v
    PostgreSQL


## 3. Domain Model

The primary domain objects are:

- Problem
- Attempt
- Submission
- Evaluation
- FeedbackItem

### Problem

Represents an LLD problem available to learners.

Important attributes:

- id
- title
- difficulty
- description
- requirements

Responsibilities:

- Store problem information
- Provide the requirements that the learner must solve

### Attempt

Represents one practice session for a problem.

Important attributes:

- id
- problem
- status
- startedAt
- submittedAt

Responsibilities:

- Track the learner's practice session
- Track lifecycle state
- Associate the attempt with a problem

Attempt lifecycle:

IN_PROGRESS → SUBMITTED → EVALUATING → COMPLETED

Failure path:

EVALUATING → FAILED

### Submission

Represents the learner's submitted design.

Important attributes:

- id
- attempt
- format
- content

Responsibilities:

- Store the learner's design explanation
- Associate submitted content with an attempt

The current MVP uses TEXT as the submission format.

### Evaluation

Represents the result of evaluating an attempt.

Important attributes:

- id
- attempt
- summary
- status
- createdAt

Responsibilities:

- Store the evaluation result
- Associate the evaluation with the corresponding attempt

### FeedbackItem

Represents one actionable piece of design feedback.

Important attributes:

- id
- evaluation
- criterion
- severity
- observation
- suggestion

Responsibilities:

- Explain a specific design observation
- Communicate its severity
- Suggest an improvement

## 4. Entity Relationships

The primary relationships are:

Problem 1 ---- * Attempt

Attempt 1 ---- 1 Submission

Attempt 1 ---- 1 Evaluation

Evaluation 1 ---- * FeedbackItem


A single problem can therefore have multiple learner attempts.

Each attempt has one submission in the current MVP.

Each attempt can have one completed evaluation.

An evaluation can contain multiple feedback items.

## 5. Package Structure

The backend uses domain-oriented packages:

com.lldpractice

├── api
├── attempt
├── evaluator
├── evaluation
├── problem
└── submission

### api

Contains REST controllers and API response models.

Examples:

- ProblemController
- AttemptController
- SubmissionController
- EvaluationController

### problem

Contains:

- Problem
- ProblemRepository
- ProblemService

Responsible for problem management.

### attempt

Contains:

- Attempt
- AttemptRepository
- AttemptService

Responsible for attempt creation and lifecycle management.

### submission

Contains:

- Submission
- SubmissionRepository
- SubmissionService

Responsible for learner submissions.

### evaluation

Contains:

- Evaluation
- EvaluationRepository
- FeedbackItemEntity
- FeedbackItemRepository
- EvaluationService

Responsible for persistence and orchestration of evaluation results.

### evaluator

Contains the evaluator abstraction and evaluation implementations.

Important classes:

- Evaluator
- DeterministicEvaluator
- EvaluationResult
- FeedbackItem

## 6. Service Responsibilities

### ProblemService

Responsibilities:

- Retrieve all problems
- Retrieve a problem by ID
- Create problems

### AttemptService

Responsibilities:

- Start an attempt
- Retrieve an attempt
- Submit an attempt
- Update attempt status
- Retrieve attempt history

### SubmissionService

Responsibilities:

- Create a submission
- Retrieve a submission for an attempt

### EvaluationService

Responsibilities:

- Retrieve an existing evaluation
- Load the submission
- Move the attempt into EVALUATING
- Invoke the evaluator
- Persist the evaluation
- Persist feedback items
- Move the attempt to COMPLETED
- Mark the attempt FAILED when evaluation throws an error

## 7. Evaluator Abstraction

The evaluator is represented using an interface:

public interface Evaluator {

    EvaluationResult evaluate(
        Problem problem,
        Submission submission
    );
}

The core application therefore depends on the abstraction rather than a concrete evaluation algorithm.

Conceptually:

Evaluator
    |
    +---------------------------+
    |                           |
    v                           v
DeterministicEvaluator     LlmEvaluator


This allows the evaluation strategy to change without modifying the practice flow.

## 8. Deterministic Evaluation

The current MVP contains a deterministic evaluator.

Its purpose is to perform predictable checks.

For the Parking Lot problem, it checks concepts such as:

- Vehicle Types
- Parking Spots
- Entry and Exit
- Fee Calculation
- Responsibilities
- Relationships

It also detects empty submissions.

The deterministic evaluator produces structured feedback using:

- Criterion
- Severity
- Observation
- Suggestion

This makes the output predictable and testable.

## 9. LLM Evaluation Extension

An LLM evaluator is treated as an extension point rather than a mandatory dependency of the MVP.

A future implementation can implement:

public interface Evaluator {

    EvaluationResult evaluate(
        Problem problem,
        Submission submission
    );
}

The LLM evaluator can focus on qualitative criteria such as:

- Responsibility distribution
- Coupling
- Cohesion
- Encapsulation
- Abstraction
- SOLID principles
- Extensibility
- Design patterns
- Trade-offs
- Explanation quality

The result should be converted into the same EvaluationResult model used by the deterministic evaluator.

This keeps the frontend and persistence layers independent of the AI provider.

## 10. Submission Abstraction

The current database model stores:

- format
- content

The MVP currently supports:

TEXT

The design leaves room for future formats such as:

- CODE
- DIAGRAM

A future implementation could introduce a richer submission content abstraction without changing the overall practice flow.

## 11. API Design

### Problem APIs

GET /api/problems

Returns all available problems.

GET /api/problems/{id}

Returns a specific problem.

POST /api/problems

Creates a new problem.

### Attempt APIs

POST /api/attempts/start/{problemId}

Starts a new practice attempt.

GET /api/attempts

Returns attempt history.

GET /api/attempts/{id}

Returns a specific attempt.

POST /api/attempts/{id}/submit

Marks an attempt as submitted.

POST /api/attempts/{id}/complete

Allows an attempt to be explicitly marked completed.

### Submission APIs

POST /api/submissions/{attemptId}

Creates a submission.

GET /api/submissions/attempt/{attemptId}

Retrieves the submission for an attempt.

### Evaluation APIs

POST /api/evaluations/{attemptId}

Evaluates an attempt.

GET /api/evaluations/{attemptId}

Retrieves an existing evaluation.

## 12. Evaluation Flow

The normal evaluation flow is:

1. Learner starts an attempt.
2. Backend creates an Attempt with IN_PROGRESS status.
3. Learner submits design text.
4. Submission is persisted.
5. Attempt changes to SUBMITTED.
6. Evaluation is requested.
7. Attempt changes to EVALUATING.
8. Evaluator analyzes the submission.
9. Evaluation is persisted.
10. Feedback items are persisted.
11. Attempt changes to COMPLETED.
12. Frontend displays the feedback.

If evaluation fails:

EVALUATING → FAILED

This makes failure visible instead of leaving the attempt in an unknown state.

## 13. Idempotency Consideration

Evaluation should not unnecessarily create duplicate evaluation records for the same attempt.

Before evaluating an attempt, EvaluationService checks whether an evaluation already exists.

If an evaluation already exists, the existing evaluation is returned.

This prevents accidental duplicate evaluations when the evaluation endpoint is called more than once.

## 14. Persistence Model

PostgreSQL is used as the persistent data store.

The main tables conceptually represent:

Problem
Attempt
Submission
Evaluation
FeedbackItem

Relationships are represented using JPA associations.

The current MVP uses:

- @ManyToOne for Attempt → Problem
- @OneToOne for Submission → Attempt
- @OneToOne for Evaluation → Attempt
- @ManyToOne for FeedbackItem → Evaluation

## 15. Frontend Design

The React frontend has four primary user experiences.

### Problem List

Displays available LLD problems.

The learner can select a problem and start practicing.

### Practice Screen

Displays:

- Problem description
- Requirements
- Design instructions
- Text submission area

### Feedback Screen

Displays:

- Evaluation summary
- Feedback criteria
- Severity
- Observations
- Suggestions

### Attempt History

Displays previous attempts including:

- Problem
- Status
- Start time
- Submission time

## 16. Why Modular Monolith?

The project deliberately uses a modular monolith instead of microservices.

Reasons:

- Small MVP scope
- Easier local development
- Easier debugging
- Lower infrastructure complexity
- Simple deployment
- Focus remains on LLD/domain modeling

The domain modules still have clear responsibilities, making future extraction possible if the product grows.

## 17. Design Patterns

### Strategy Pattern

The Evaluator interface creates a strategy-like extension point.

Different evaluation strategies can implement the same interface.

Examples:

- DeterministicEvaluator
- LlmEvaluator

The rest of the application can remain independent of the selected strategy.

### Repository Pattern

Spring Data JPA repositories provide persistence abstraction.

Examples:

- ProblemRepository
- AttemptRepository
- SubmissionRepository
- EvaluationRepository
- FeedbackItemRepository

### Service Layer

Services encapsulate application logic between controllers and repositories.

This prevents controllers from directly implementing business operations.

## 18. Separation of Concerns

The system follows the general separation:

Controller
    ↓
Service
    ↓
Repository
    ↓
Database

Evaluation follows:

EvaluationService
    ↓
Evaluator
    ↓
EvaluationResult
    ↓
Persistence

This keeps HTTP concerns separate from business logic and persistence.

## 19. Testability

The evaluator is designed to be independently testable.

For example, DeterministicEvaluator can be tested without starting the complete application.

Important test scenarios include:

- Complete submission
- Empty submission
- Missing concepts
- Feedback generation
- Evaluation result structure

The evaluator interface also makes future evaluator implementations independently testable.

## 20. Extensibility

The architecture provides several extension points.

### New Problems

New Problem records can be added without changing the practice flow.

### New Submission Types

Future formats can include:

- Text
- Code
- Diagram

### New Evaluators

Future evaluators can include:

- DeterministicEvaluator
- LlmEvaluator
- RuleBasedEvaluator
- HybridEvaluator

### New Feedback Criteria

Additional criteria can be introduced without changing the basic feedback structure.

### Future Async Evaluation

If LLM evaluation becomes slow, the current lifecycle can be extended to support asynchronous processing:

SUBMITTED → EVALUATING → COMPLETED

The frontend can display an evaluation-in-progress state while the backend processes the evaluation.

## 21. Important Trade-offs

### Text Instead of Diagram

Trade-off:

Less visual representation, but significantly smaller implementation scope.

### Deterministic Evaluation

Trade-off:

Less sophisticated than an LLM for qualitative design review, but predictable, testable, and inexpensive.

### Modular Monolith

Trade-off:

Less independent scalability than microservices, but much simpler and more appropriate for the MVP.

### PostgreSQL

Trade-off:

Requires database setup, but provides reliable relational persistence and supports the entity relationships naturally.

## 22. Known MVP Limitations

The current implementation has several intentional limitations:

- No authentication
- One submission per attempt
- Text-only submission
- Basic deterministic evaluation
- No real LLM provider integration yet
- No diagram editor
- No code execution
- No advanced scoring analytics

These limitations are documented as future extension points rather than being hidden.

## 23. Summary

The design of LLD Coach focuses on a small but meaningful learning workflow.

The core domain consists of:

Problem
Attempt
Submission
Evaluation
FeedbackItem

The architecture separates:

- API handling
- Domain operations
- Persistence
- Evaluation

The Evaluator abstraction provides a clear extension point for future AI-powered evaluation while the deterministic evaluator provides predictable MVP behavior.

The overall design prioritizes clarity, testability, extensibility, and a focused product scope.