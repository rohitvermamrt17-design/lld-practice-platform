# LLD Coach — Practice, Submit, Understand, Improve

## 🎯 Project Overview

LLD Coach is a focused Low-Level Design practice platform that helps learners:

1. Choose an LLD problem
2. Understand the requirements
3. Submit a design explanation
4. Receive structured feedback
5. Review previous attempts
6. Improve and retry

The MVP focuses on the core learning loop rather than complex infrastructure.

## ✨ Current Features

- 3 LLD practice problems
- Practice attempt lifecycle
- Structured text-based submissions
- Deterministic evaluation
- Criterion-based feedback
- Attempt history
- PostgreSQL persistence
- React frontend
- Spring Boot REST API
- Automated backend tests

## 🧩 Problems Included

| Problem | Difficulty |
|---|---|
| Parking Lot | Easy |
| Elevator System | Medium |
| Vending Machine | Medium |

## 🏗️ Architecture

React Frontend  
↓  
Spring Boot REST API  
↓  
Domain Services  
↓  
PostgreSQL

Evaluation:

Submission  
↓  
Evaluation Engine  
↓  
Deterministic Evaluator  
↓  
Structured Feedback

The evaluator abstraction allows an LLM-based evaluator to be introduced later without changing the core practice flow.
LLD Coach is a practice platform for learning and improving Low-Level Design (LLD).

The platform allows a learner to:

1. Choose an LLD problem
2. Read the requirements
3. Write a design explanation
4. Submit the solution
5. Receive structured feedback
6. Review previous attempts
7. Retry another problem

## Features

- LLD problem selection
- Practice attempt lifecycle
- Text-based design submission
- Deterministic evaluation
- Structured design feedback
- Evaluation persistence
- Attempt history
- Multiple LLD problems
- REST APIs
- React frontend
- Spring Boot backend
- PostgreSQL persistence
- Automated backend tests

## Current Problems

### 1. Parking Lot

Difficulty: Easy

Focus areas:

- Vehicle types
- Parking spots
- Entry and exit
- Fee calculation
- Responsibilities
- Relationships

### 2. Elevator System

Difficulty: Medium

Focus areas:

- Multiple elevators
- Floor requests
- Elevator state
- Direction
- Request assignment
- Pickup and drop-off

### 3. Vending Machine

Difficulty: Medium

Focus areas:

- Product selection
- Inventory
- Payment
- Change calculation
- Product dispensing
- Transaction cancellation

## Architecture
```text
React Frontend
        |
        | REST API
        v
Spring Boot Backend
        |
        +-----------------+-----------------+
        |                 |                 |
        v                 v                 v
     Problem           Attempt          Submission
     Module            Module            Module
        |                 |                 |
        +-----------------+-----------------+
                          |
                          v
                  Evaluation Engine
                          |
                 +--------+--------+
                 |                 |
                 v                 v
        Deterministic        LLM Evaluator
          Evaluator             Seam
                 |
                 v
             PostgreSQL
```
## Backend Structure

backend/src/main/java/com/lldpractice
```text
├── api
├── attempt
├── evaluator
├── evaluation
├── problem
└── submission
```
### Problem

Responsible for storing and retrieving LLD problems.

### Attempt

Responsible for the learner's practice lifecycle.
```text
IN_PROGRESS
     |
     v
SUBMITTED
     |
     v
EVALUATING
     |
     +------> FAILED
     |
     v
COMPLETED
```
### Submission

Stores the learner's design explanation.

The MVP uses text submissions because they are faster to build and easier to evaluate consistently than a custom diagram editor.

### Evaluation

Stores evaluation results and structured feedback.

### Evaluator

The evaluator is represented using an interface:

public interface Evaluator {

    EvaluationResult evaluate(
        Problem problem,
        Submission submission
    );
}

This keeps the evaluation mechanism replaceable.

## Frontend

The frontend is built using React and Vite.
```text
Main screens:

Problem List
     |
     v
Practice Screen
     |
     v
Submit Solution
     |
     v
Feedback Screen
     |
     v
Attempt History
```
## Technology Stack

### Frontend

- React
- Vite
- JavaScript
- CSS

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation

### Database

- PostgreSQL

### Testing

- JUnit 5
- Spring Boot Test

## Running the Backend

Open a terminal:

cd "C:\Users\Rohit Verma\lld-practice-platform\backend"
.\mvnw.cmd spring-boot:run

Backend:

http://localhost:8080

## Running the Frontend

Open another terminal:

cd "C:\Users\Rohit Verma\lld-practice-platform\frontend"
npm install
npm run dev

Frontend:

http://localhost:5173

## Database

Create the PostgreSQL database:

CREATE DATABASE lld_practice;

Configure the database credentials in:

backend/src/main/resources/application.properties

Example:

spring.datasource.url=jdbc:postgresql://localhost:5432/lld_practice
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD

spring.jpa.hibernate.ddl-auto=update

server.port=8080

Do not commit real database passwords or API keys to GitHub.

## Important API Endpoints

### Problems

GET  /api/problems
GET  /api/problems/{id}
POST /api/problems

### Attempts

POST /api/attempts/start/{problemId}
GET  /api/attempts
GET  /api/attempts/{id}
POST /api/attempts/{id}/submit
POST /api/attempts/{id}/complete

### Submissions

POST /api/submissions/{attemptId}
GET  /api/submissions/attempt/{attemptId}

### Evaluations

POST /api/evaluations/{attemptId}
GET  /api/evaluations/{attemptId}

## Evaluation Approach

The MVP separates deterministic checks from LLM-style evaluation.

Deterministic checks are used for predictable validation such as:

- Empty submissions
- Required concepts
- Required design sections
- Basic submission completeness

LLM evaluation is intended for qualitative design analysis such as:

- Responsibilities
- Coupling and cohesion
- Abstraction
- Encapsulation
- SOLID principles
- Extensibility
- Design patterns
- Trade-offs
- Explanation quality

This separation prevents the system from depending entirely on an LLM for basic validation.

## Feedback Model

Feedback is structured around design criteria.

Each feedback item contains:

Criterion
Severity
Observation
Suggestion

Example:

Criterion: RESPONSIBILITIES

Severity: WARNING

Observation:
The ParkingLot class appears to handle parking, fee calculation and vehicle management.

Suggestion:
Separate fee calculation into a dedicated strategy or service so responsibilities remain focused.

## Testing

Run all backend tests:

cd "C:\Users\Rohit Verma\lld-practice-platform\backend"
.\mvnw.cmd clean test

The project includes tests for:

- Complete submissions
- Empty submissions
- Missing design concepts
- Deterministic evaluator behavior

## Design Decisions

### Why a modular monolith?

The project is intentionally implemented as a Spring Boot monolith.

The assignment focuses on LLD and domain design rather than distributed systems.

A modular monolith keeps the project:

- Simple to understand
- Easy to test
- Easy to deploy
- Easy to extend

### Why text submission?

A diagram editor would require significant UI development and would reduce the time available for the core learning and evaluation experience.

Text provides a meaningful MVP while leaving diagram submissions as a future extension.

### Why an evaluator interface?

The evaluator interface makes the evaluation mechanism replaceable.

For example:
```text
Evaluator
   |
   +-- DeterministicEvaluator
   |
   +-- LlmEvaluator
```
A future evaluator can be introduced without changing the core attempt and submission domain.

## Future Improvements

Potential future improvements include:

- LLM-powered qualitative evaluation
- Diagram submissions
- Code submissions
- Rubric-based scoring
- User authentication
- Personalized learning recommendations
- Difficulty progression
- Problem authoring
- Evaluation confidence
- Retry comparison
- Progress analytics

## Project Scope

This project intentionally avoids unnecessary infrastructure such as:

- Microservices
- Kafka
- Kubernetes
- Distributed queues
- Complex cloud infrastructure

The goal is to demonstrate:

- Domain modeling
- Object-oriented design
- Separation of concerns
- Extensible evaluation
- REST API design
- Persistence
- Testing
- Product thinking

## Author

Rohit Verma

Github - (https://github.com/rohitvermamrt17-design) 

B.Tech Computer Science and Engineering

Engineering Practice Project
