import { useEffect, useState } from "react";
import "./App.css";

const API = "http://localhost:8080/api";

function App() {
  const [problems, setProblems] = useState([]);
  const [selectedProblem, setSelectedProblem] = useState(null);
  const [attempt, setAttempt] = useState(null);
  const [content, setContent] = useState("");
  const [evaluation, setEvaluation] = useState(null);
  const [history, setHistory] = useState([]);
  const [showHistory, setShowHistory] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadProblems();
    loadHistory();
  }, []);

  const loadProblems = async () => {
    try {
      const response = await fetch(`${API}/problems`);

      if (!response.ok) {
        throw new Error("Could not load problems");
      }

      const data = await response.json();
      setProblems(data);
    } catch (error) {
      console.error("Could not load problems:", error);
    }
  };

  const loadHistory = async () => {
    try {
      const response = await fetch(`${API}/attempts`);

      if (!response.ok) {
        throw new Error("Could not load history");
      }

      const data = await response.json();
      setHistory(data);
    } catch (error) {
      console.error("Could not load history:", error);
    }
  };

  const startPractice = async (problem) => {
    try {
      setSelectedProblem(problem);
      setEvaluation(null);
      setContent("");
      setShowHistory(false);

      const response = await fetch(
        `${API}/attempts/start/${problem.id}`,
        {
          method: "POST",
        }
      );

      if (!response.ok) {
        throw new Error("Could not start attempt");
      }

      const data = await response.json();
      setAttempt(data);

      loadHistory();
    } catch (error) {
      console.error(error);
      alert("Could not start the practice attempt.");
    }
  };

  const submitSolution = async () => {
    if (!content.trim()) {
      alert("Please write your design explanation first.");
      return;
    }

    if (!attempt) {
      alert("No active attempt found.");
      return;
    }

    setLoading(true);

    try {
      const submissionResponse = await fetch(
        `${API}/submissions/${attempt.id}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            format: "TEXT",
            content: content,
          }),
        }
      );

      if (!submissionResponse.ok) {
        throw new Error("Could not save submission");
      }

      const submitResponse = await fetch(
        `${API}/attempts/${attempt.id}/submit`,
        {
          method: "POST",
        }
      );

      if (!submitResponse.ok) {
        throw new Error("Could not submit attempt");
      }

      const evaluationResponse = await fetch(
        `${API}/evaluations/${attempt.id}`,
        {
          method: "POST",
        }
      );

      if (!evaluationResponse.ok) {
        throw new Error("Could not evaluate submission");
      }

      const data = await evaluationResponse.json();

      setEvaluation(data);

      loadHistory();
    } catch (error) {
      console.error(error);
      alert("Something went wrong while evaluating your submission.");
    } finally {
      setLoading(false);
    }
  };

  const goBack = () => {
    setSelectedProblem(null);
    setAttempt(null);
    setEvaluation(null);
    setContent("");
    setShowHistory(false);

    loadProblems();
    loadHistory();
  };

  const openHistory = () => {
    setSelectedProblem(null);
    setAttempt(null);
    setEvaluation(null);
    setContent("");
    setShowHistory(true);

    loadHistory();
  };

  return (
    <div className="app">

      {/* Header */}
      <header className="header">
        <div className="header-content">
          <div>
            <h1>LLD Coach</h1>
            <p>Practice • Submit • Understand • Improve</p>
          </div>

          <button
            className="history-button"
            onClick={openHistory}
          >
            Attempt History
          </button>
        </div>
      </header>


      {/* Problem List */}
      {!selectedProblem && !showHistory && (
        <main className="container">

          <section className="hero">
            <h2>Choose an LLD Problem</h2>

            <p>
              Practice object-oriented design and receive
              structured feedback on your solution.
            </p>
          </section>


          <div className="problem-grid">

            {problems.length === 0 && (
              <div className="problem-card">
                <h3>No problems found</h3>

                <p>
                  Make sure the Spring Boot backend is running
                  on port 8080.
                </p>
              </div>
            )}


            {problems.map((problem) => (
              <div
                className="problem-card"
                key={problem.id}
              >

                <span className="difficulty">
                  {problem.difficulty}
                </span>

                <h3>{problem.title}</h3>

                <p>
                  {problem.description}
                </p>

                <button
                  onClick={() => startPractice(problem)}
                >
                  Start Practice
                </button>

              </div>
            ))}

          </div>

        </main>
      )}


      {/* Practice Screen */}
      {selectedProblem && !evaluation && (
        <main className="container">

          <button
            className="back-button"
            onClick={goBack}
          >
            ← Back to Problems
          </button>


          <section className="practice-card">

            <span className="difficulty">
              {selectedProblem.difficulty}
            </span>

            <h2>{selectedProblem.title}</h2>


            <h3>Problem</h3>

            <p>
              {selectedProblem.description}
            </p>


            <h3>Requirements</h3>

            <p>
              {selectedProblem.requirements}
            </p>


            <h3>Your Design</h3>

            <p className="hint">
              Explain the requirements, assumptions,
              classes, responsibilities, relationships,
              design patterns and trade-offs.
            </p>


            <textarea
              value={content}
              onChange={(event) =>
                setContent(event.target.value)
              }
              placeholder={`Example:

1. Requirements and assumptions

2. Main classes

3. Responsibilities

4. Relationships

5. Design patterns

6. Trade-offs`}
            />


            <button
              className="submit-button"
              onClick={submitSolution}
              disabled={loading}
            >
              {loading
                ? "Evaluating..."
                : "Submit for Feedback"}
            </button>

          </section>

        </main>
      )}


      {/* Feedback Screen */}
      {evaluation && (
        <main className="container">

          <button
            className="back-button"
            onClick={goBack}
          >
            ← Back to Problems
          </button>


          <section className="feedback-card">

            <div className="success">
              ✓ Evaluation Completed
            </div>


            <h2>
              {selectedProblem.title} — Feedback
            </h2>


            <p className="summary">
              {evaluation.evaluation.summary}
            </p>


            <h3>Design Feedback</h3>


            <div className="feedback-list">

              {evaluation.feedbackItems.map((item) => (

                <div
                  className="feedback-item"
                  key={item.id}
                >

                  <div className="feedback-header">

                    <h3>
                      {item.criterion}
                    </h3>

                    <span
                      className={`severity ${item.severity}`}
                    >
                      {item.severity}
                    </span>

                  </div>


                  <p>
                    <strong>Observation:</strong>{" "}
                    {item.observation}
                  </p>


                  <p>
                    <strong>Suggestion:</strong>{" "}
                    {item.suggestion}
                  </p>

                </div>

              ))}

            </div>


            <button onClick={goBack}>
              Try Another Problem
            </button>

          </section>

        </main>
      )}


      {/* Attempt History */}
      {showHistory && (
        <main className="container">

          <button
            className="back-button"
            onClick={goBack}
          >
            ← Back to Problems
          </button>


          <section className="history-card">

            <div className="history-title">

              <div>
                <h2>Attempt History</h2>

                <p>
                  Review your previous LLD practice attempts.
                </p>
              </div>

              <button onClick={loadHistory}>
                Refresh
              </button>

            </div>


            {history.length === 0 ? (
              <div className="empty-history">

                <h3>No attempts yet</h3>

                <p>
                  Start practicing a problem and your
                  attempts will appear here.
                </p>

              </div>
            ) : (

              <div className="history-list">

                {history.map((item) => (

                  <div
                    className="history-item"
                    key={item.id}
                  >

                    <div>

                      <h3>
                        {item.problem?.title ||
                          "Unknown Problem"}
                      </h3>

                      <p>
                        Started:{" "}
                        {item.startedAt
                          ? new Date(
                              item.startedAt
                            ).toLocaleString()
                          : "N/A"}
                      </p>

                      {item.submittedAt && (
                        <p>
                          Submitted:{" "}
                          {new Date(
                            item.submittedAt
                          ).toLocaleString()}
                        </p>
                      )}

                    </div>


                    <span
                      className={`status ${item.status}`}
                    >
                      {item.status}
                    </span>

                  </div>

                ))}

              </div>

            )}

          </section>

        </main>
      )}

    </div>
  );
}

export default App;