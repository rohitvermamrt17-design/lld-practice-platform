# AI Usage — LLD Coach

## 1. Purpose

AI tools were used during development as engineering assistants.

The goal was to use AI to accelerate implementation and reasoning while keeping architectural decisions, validation, testing, and final code ownership with the developer.

AI was not treated as an unquestioned source of truth.

## 2. Areas Where AI Was Used

AI assistance was used for:

- Project structure suggestions
- Spring Boot implementation guidance
- REST API design
- Domain model discussion
- Evaluation architecture
- Frontend implementation
- Debugging development issues
- Test case suggestions
- Documentation drafting
- README structure
- Research organization

## 3. Architecture Decisions

AI suggested multiple possible approaches for evaluation.

One possible approach was to make the entire evaluation process dependent on an LLM.

That approach was not selected as the only evaluation mechanism.

Instead, the project separates deterministic validation from qualitative AI evaluation.

The resulting design is:

Evaluator
    |
    +-- DeterministicEvaluator
    |
    +-- LlmEvaluator

This separation was chosen because predictable checks should not require an external AI service.

Examples of deterministic checks include:

- Empty submission detection
- Required concepts
- Required sections
- Basic submission completeness
- Attempt lifecycle validation

Potential LLM responsibilities include:

- Coupling and cohesion
- Responsibility distribution
- Abstraction
- Encapsulation
- SOLID principles
- Extensibility
- Design patterns
- Trade-offs
- Explanation quality

## 4. Product Scope Decisions

AI assistance suggested several possible features including:

- Diagram editor
- Code execution
- Real-time collaboration
- Authentication
- Advanced analytics
- Complex asynchronous infrastructure
- Large problem libraries

These were intentionally excluded from the MVP.

The MVP was kept focused on:

Choose Problem
    ↓
Practice
    ↓
Submit
    ↓
Evaluate
    ↓
Review Feedback
    ↓
Retry

The reason was to maximize the value of the core learning loop within the limited development time.

## 5. Submission Format Decision

AI-assisted design exploration considered three main submission formats:

1. Diagram
2. Java code
3. Structured text

A custom diagram editor was considered too expensive for the initial scope.

Code submission would introduce additional complexity such as:

- Compilation
- Sandboxing
- Execution safety
- Language handling
- Test execution

Structured text was therefore selected for the MVP.

It provides enough information to evaluate LLD reasoning while keeping the implementation manageable.

## 6. Evaluation Feedback Design

AI-assisted brainstorming initially considered numerical scoring as the primary feedback mechanism.

The design was changed toward structured feedback because a learner benefits more from understanding:

- What was observed
- Why it matters
- What should be improved

The current feedback structure contains:

- Criterion
- Severity
- Observation
- Suggestion

Example:

Criterion:
RESPONSIBILITIES

Severity:
WARNING

Observation:
A single class appears to manage several unrelated responsibilities.

Suggestion:
Separate the responsibilities into focused classes or strategies.

This makes feedback more actionable than a score alone.

## 7. Domain Design

AI assistance was used to explore domain boundaries.

The final domain model contains:

- Problem
- Attempt
- Submission
- Evaluation
- FeedbackItem

The design was kept intentionally small.

The main relationships are:

Problem 1 ---- * Attempt

Attempt 1 ---- 1 Submission

Attempt 1 ---- 1 Evaluation

Evaluation 1 ---- * FeedbackItem

## 8. Evaluator Abstraction

AI assistance suggested using an evaluator interface.

The resulting abstraction is:

public interface Evaluator {

    EvaluationResult evaluate(
        Problem problem,
        Submission submission
    );
}

This was retained because it allows evaluation implementations to change without changing the main practice flow.

Possible implementations include:

- DeterministicEvaluator
- LlmEvaluator
- HybridEvaluator

## 9. AI Suggestions That Were Rejected

Several AI-generated approaches were intentionally rejected or simplified.

### Full LLM Dependency

Rejected because basic validation should be deterministic and testable.

### Microservices

Rejected because the application is small and the assignment focuses on LLD rather than distributed systems.

### Kafka or Message Queues

Rejected for the MVP because asynchronous infrastructure would add complexity without being necessary for the initial learning loop.

### Kubernetes

Rejected because deployment orchestration is outside the problem scope.

### Custom Diagram Editor

Rejected because it would consume significant implementation time.

### Code Execution Sandbox

Rejected because safe code execution introduces a substantial security and infrastructure problem.

### Score-Only Evaluation

Rejected because actionable observations and suggestions provide more useful learning feedback.

## 10. Debugging and Implementation Assistance

AI was also used during implementation and debugging.

Examples included:

- Maven/Spring Boot setup
- PostgreSQL configuration
- REST endpoint design
- React/Vite setup
- Frontend-backend integration
- API debugging
- Test creation
- CSS/UI improvements

AI suggestions were validated by running the application, testing endpoints, and checking build results.

## 11. Verification Approach

AI-generated code was not considered automatically correct.

Implementation was verified using:

- Maven build
- Maven tests
- Spring Boot application startup
- REST API requests
- PostgreSQL persistence
- React development server
- Browser-based frontend testing

For example, backend tests can be run with:

cd backend

.\mvnw.cmd clean test

The frontend can be run with:

cd frontend

npm run dev

## 12. Human Decision-Making

The developer made the final decisions about:

- Product scope
- Domain boundaries
- Architecture
- Submission format
- Evaluation approach
- Technology choices
- Feature prioritization
- Testing
- Documentation
- Trade-offs

AI was used as an assistant rather than as the final decision-maker.

## 13. Transparency

AI assistance contributed to implementation and documentation, but the resulting project was reviewed and tested during development.

The important architectural choices were made based on the assignment requirements, implementation constraints, and the goal of creating a small but meaningful LLD practice experience.

## 14. Final AI Usage Summary

AI was used primarily for:

- Brainstorming
- Design exploration
- Implementation assistance
- Debugging
- Test generation
- Documentation assistance

AI was deliberately not used as a replacement for:

- Engineering judgment
- Testing
- Architectural decisions
- Product prioritization
- Final verification

The final system therefore uses AI assistance while maintaining deterministic validation, explicit domain modeling, testability, and clear separation of responsibilities.