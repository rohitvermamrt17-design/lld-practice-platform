# Research Note — LLD Coach

## 1. Problem Understanding

Low-Level Design practice is commonly performed through interview-style problems such as Parking Lot, Elevator System, Vending Machine, Library Management, and similar object-oriented design exercises.

The learner is usually expected to identify requirements, assumptions, domain entities, responsibilities, relationships, interfaces, and appropriate design patterns.

A major difficulty for learners is that simply reading a reference solution does not provide enough feedback about whether their own design is well structured.

The proposed product, LLD Coach, addresses this gap by creating a short practice loop:

Choose Problem → Think → Submit → Receive Feedback → Review → Retry.

The product focuses on learning and iteration rather than simply displaying a model answer.

## 2. Target User

The primary user is a software engineering student or early-career developer preparing for Low-Level Design interviews.

The user should be able to start practicing without requiring extensive configuration or knowledge of distributed systems.

The MVP therefore focuses on a small number of representative LLD problems.

## 3. Selected Problems

Three problems were selected for the MVP:

### Parking Lot

Parking Lot is an accessible starting problem because it introduces several fundamental object-oriented design concepts.

Important concepts include:

- Vehicle types
- Parking spots
- Parking lot
- Entry and exit
- Fee calculation
- Assignment of suitable spots
- Relationships between domain objects

### Elevator System

Elevator System introduces more complex behavior and state.

Important concepts include:

- Elevator state
- Direction
- Floor requests
- Multiple elevators
- Request assignment
- Pickup and drop-off behavior

### Vending Machine

Vending Machine introduces transaction-oriented behavior.

Important concepts include:

- Product selection
- Inventory
- Payment
- Change calculation
- Product dispensing
- Transaction cancellation
- State transitions

Together, these problems provide different levels of object-oriented design complexity while remaining small enough for an MVP.

## 4. Product Research

The product should avoid becoming a generic coding platform.

The primary learning loop should remain focused on design thinking.

A learner should first understand the problem and make design decisions before seeing feedback.

The submission should therefore capture:

- Requirements
- Assumptions
- Classes
- Responsibilities
- Relationships
- Design patterns
- Trade-offs

This format provides enough structure for meaningful evaluation without requiring a complex diagram editor.

## 5. Submission Format Decision

Several submission formats were considered.

### Option 1 — Diagram Editor

A diagram editor would allow learners to create class diagrams directly.

Advantages:

- Visual representation
- Close to traditional UML practice
- Strong representation of relationships

Disadvantages:

- Significant frontend implementation effort
- More difficult to validate consistently
- Reduces time available for evaluation and feedback
- More complex persistence model

### Option 2 — Java Code Submission

Learners could submit Java implementations of their designs.

Advantages:

- Real implementation
- Can potentially be compiled and tested
- Useful for advanced practice

Disadvantages:

- Moves the product toward a coding platform
- Requires code execution or compilation infrastructure
- Makes the evaluation problem more complex

### Option 3 — Structured Text Submission

Learners explain their design using a structured text format.

Advantages:

- Fast to implement
- Easy to persist
- Easy to evaluate
- Encourages design reasoning
- Does not require code execution
- Allows the product to focus on LLD concepts

For the MVP, structured text was selected.

## 6. Evaluation Research

Evaluation should not depend entirely on an AI model.

Some checks are predictable and should be handled deterministically.

Examples include:

- Empty submission detection
- Required section detection
- Required concept detection
- Submission completeness
- Attempt lifecycle validation

Qualitative design evaluation is more suitable for an LLM-based evaluator.

Examples include:

- Whether responsibilities are well distributed
- Whether coupling is unnecessarily high
- Whether abstractions are appropriate
- Whether classes have focused responsibilities
- Whether the design is extensible
- Whether design patterns are justified
- Whether trade-offs are clearly explained

Therefore, the proposed architecture separates deterministic evaluation from qualitative evaluation.

## 7. Evaluation Criteria

The evaluation model focuses on the following criteria:

### Requirement Understanding

Does the learner identify the important functional requirements and reasonable assumptions?

### Responsibilities

Are responsibilities distributed appropriately among classes?

### Coupling and Cohesion

Are classes reasonably independent while keeping related behavior together?

### Encapsulation

Does the design protect internal state and expose meaningful operations?

### Abstraction

Are interfaces or abstractions introduced where they provide useful flexibility?

### Extensibility

Can the design accommodate likely future changes without major restructuring?

### Testability

Can important behavior be tested without excessive dependencies?

### Design Explanation

Does the learner clearly explain relationships, patterns, and trade-offs?

## 8. Feedback Model

Feedback should be actionable rather than only providing a numerical score.

Each feedback item contains:

- Criterion
- Severity
- Observation
- Suggestion

For example:

Criterion:

RESPONSIBILITIES

Severity:

WARNING

Observation:

The ParkingLot class appears to manage parking allocation, fee calculation, and vehicle management.

Suggestion:

Move fee calculation into a separate strategy or service so the ParkingLot class remains focused on parking management.

This style of feedback tells the learner what was observed and what they can improve.

## 9. Deterministic Evaluation vs AI Evaluation

The two evaluation approaches have different responsibilities.

### Deterministic Evaluation

Best suited for:

- Required fields
- Empty submissions
- Basic completeness
- Known required concepts
- Lifecycle validation

### AI Evaluation

Best suited for:

- Design quality
- Responsibility distribution
- Coupling and cohesion
- Abstraction decisions
- SOLID principles
- Extensibility
- Trade-offs
- Explanation quality

This separation also makes the system easier to test and reduces unnecessary dependence on external AI services.

## 10. Architecture Research

A modular monolith was selected for the MVP.

The basic architecture is:

React Frontend
        |
        | REST API
        v
Spring Boot Backend
        |
        +-------------------+
        |                   |
        v                   v
    PostgreSQL       Evaluation Engine
                            |
                    +-------+-------+
                    |               |
                    v               v
             Deterministic      LLM Evaluator
                Evaluator            Seam

A modular monolith is appropriate because the product is small and the main engineering challenge is domain design rather than distributed systems.

Introducing microservices, Kafka, Kubernetes, or distributed queues would add infrastructure complexity without improving the core learning experience for this MVP.

## 11. Attempt Lifecycle

A practice attempt follows a defined lifecycle:

IN_PROGRESS → SUBMITTED → EVALUATING → COMPLETED

If evaluation fails:

EVALUATING → FAILED

This lifecycle makes the state of an attempt explicit and provides a foundation for future asynchronous evaluation.

## 12. Extensibility Research

The system should allow future submission formats.

The conceptual submission model can support:

- Text
- Java code
- Diagrams

The evaluator is also represented by an interface so that different evaluation strategies can be introduced later.

Conceptually:

Evaluator
    |
    +-- DeterministicEvaluator
    |
    +-- LlmEvaluator

This reduces coupling between the core practice flow and the evaluation implementation.

## 13. MVP Scope

The MVP intentionally includes:

- Three LLD problems
- Problem selection
- Practice attempt creation
- Text submission
- Deterministic feedback
- Evaluation persistence
- Attempt history
- React frontend
- Spring Boot REST API
- PostgreSQL persistence
- Automated tests

The MVP intentionally excludes:

- Authentication
- Microservices
- Real-time collaboration
- Custom diagram editor
- Code execution sandbox
- Complex distributed queues
- Advanced analytics

These features can be considered after validating the core practice loop.

## 14. Key Findings

The research led to the following decisions:

1. The product should optimize for learning feedback rather than model-answer consumption.
2. Text submission is the smallest meaningful submission format for the MVP.
3. Deterministic checks should handle predictable validation.
4. AI evaluation should focus on qualitative design reasoning.
5. Feedback should explain observations and improvements instead of only providing scores.
6. A modular monolith is sufficient for the initial product.
7. Evaluation and submission mechanisms should remain replaceable.
8. The MVP should contain a small number of carefully selected LLD problems rather than a large problem library.

## 15. Conclusion

LLD Coach is designed around a simple learning loop:

Practice → Submit → Understand → Improve.

The research supports keeping the initial implementation narrow while creating clear extension points for richer evaluation, diagram submissions, code submissions, and personalized learning features.

The resulting design emphasizes domain modeling, separation of concerns, testability, and actionable feedback rather than unnecessary infrastructure complexity.