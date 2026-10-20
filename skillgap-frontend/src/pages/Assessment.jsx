
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import {
  createAssessmentSession,
  createSkillAssessmentSession,
  submitAssessmentSession,
} from "../api/learningApi";
import "./Assessment.css";

function Assessment() {
  const { jobId, skillId } = useParams();
  const navigate = useNavigate();

  const isSkillAssessment = Boolean(skillId);

  const [session, setSession] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [result, setResult] = useState(null);

  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadAssessment() {
      try {
        setLoading(true);
        setError("");
        setSession(null);
        setQuestions([]);
        setAnswers({});
        setResult(null);

        if (!jobId) {
          throw new Error("Job ID is missing from the URL.");
        }

        // Keep the existing dashboard flow unchanged.
        // Only the individual skill route sends skillId.
        const data = isSkillAssessment
          ? await createSkillAssessmentSession(jobId, skillId)
          : await createAssessmentSession(jobId);

        if (!cancelled) {
          setSession(data);
          setQuestions(data.questions || []);
        }
      } catch (err) {
        if (!cancelled) {
          setError(
            err.message || "Failed to create the assessment."
          );
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadAssessment();

    return () => {
      cancelled = true;
    };
  }, [jobId, skillId, isSkillAssessment]);

  function handleAnswerChange(questionId, selectedAnswer) {
    setAnswers((previousAnswers) => ({
      ...previousAnswers,
      [questionId]: selectedAnswer,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (!session?.sessionId) {
      setError("Assessment session is missing. Please try again.");
      return;
    }

    if (Object.keys(answers).length !== questions.length) {
      setError("Please answer every question before submitting.");
      return;
    }

    try {
      setError("");
      setSubmitting(true);

      const answerList = questions.map((question) => ({
        questionId: question.questionId,
        selectedAnswer: answers[question.questionId],
      }));

      const assessmentResult = await submitAssessmentSession(
        session.sessionId,
        answerList
      );

      setResult(assessmentResult);
    } catch (err) {
      setError(
        err.message || "Failed to submit the assessment."
      );
    } finally {
      setSubmitting(false);
    }
  }

  function handleBack() {
    if (isSkillAssessment) {
      navigate(`/jobs/${jobId}/learning`);
    } else {
      navigate("/dashboard");
    }
  }


if (loading) {
  return (
    <main className="assessment-page">
      <div className="assessment-container">
        <section className="assessment-state-card">
          <p className="assessment-eyebrow">SkillGap · Learning</p>
          <h2>
            {isSkillAssessment
              ? "Individual Skill Assessment"
              : "Combined Job Assessment"}
          </h2>
          <p>
            {isSkillAssessment
              ? "Preparing your skill assessment..."
              : "Preparing your combined job assessment..."}
          </p>
        </section>
      </div>
    </main>
  );
}



if (error && questions.length === 0) {
  return (
    <main className="assessment-page">
      <div className="assessment-container">
        <section className="assessment-state-card">
          <h2>Unable to Load Assessment</h2>
          <p className="assessment-error" role="alert">{error}</p>
          <button
            type="button"
            className="assessment-button assessment-button-secondary"
            onClick={handleBack}
          >
            Go Back
          </button>
        </section>
      </div>
    </main>
  );
}


  if (result) {
    const score =
      result.scorePercentage ??
      result.matchPercentage ??
      result.percentage;


return (
  <main className="assessment-page">
    <div className="assessment-container">
      <section className="assessment-result-card">
        <p className="assessment-eyebrow">Assessment completed</p>
        <h2>
          {isSkillAssessment
            ? "Your Skill Assessment Results"
            : "Your Job Assessment Results"}
        </h2>

        {score !== undefined && score !== null && (
          <>
            <p>Overall Score</p>
            <p className="assessment-result-score">{score}%</p>
          </>
        )}

        {result.totalQuestions !== undefined && (
          <p>Total questions: {result.totalQuestions}</p>
        )}

        {result.correctAnswers !== undefined && (
          <p>Correct answers: {result.correctAnswers}</p>
        )}

        {Array.isArray(result.skillResults) &&
          result.skillResults.length > 0 && (
            <div>
              <h3>Skill-wise Performance</h3>
              {result.skillResults.map((item, index) => (
                <div key={item.skillId || index}>
                  <p>
                    <strong>
                      {item.skillName || item.skillId || `Skill ${index + 1}`}
                    </strong>
                  </p>
                  {item.scorePercentage !== undefined && (
                    <p>Score: {item.scorePercentage}%</p>
                  )}
                  {item.correctAnswers !== undefined &&
                    item.totalQuestions !== undefined && (
                      <p>
                        Correct: {item.correctAnswers}/{item.totalQuestions}
                      </p>
                    )}
                </div>
              ))}
            </div>
          )}

        <button
          type="button"
          className="assessment-button assessment-button-primary"
          onClick={handleBack}
        >
          {isSkillAssessment ? "Back to Learning" : "Back to Dashboard"}
        </button>
      </section>
    </div>
  </main>
);

  }


if (questions.length === 0) {
  return (
    <main className="assessment-page">
      <div className="assessment-container">
        <section className="assessment-state-card">
          <h2>
            {isSkillAssessment
              ? "Individual Skill Assessment"
              : "Combined Job Assessment"}
          </h2>
          <p>No questions are available for this assessment.</p>
          {error && <p className="assessment-error" role="alert">{error}</p>}
          <button
            type="button"
            className="assessment-button assessment-button-secondary"
            onClick={handleBack}
          >
            Go Back
          </button>
        </section>
      </div>
    </main>
  );
}



return (
  <main className="assessment-page">
    <div className="assessment-container">
      <header className="assessment-header">
        <div>
          <p className="assessment-eyebrow">SkillGap · Learning</p>
          <h2 className="assessment-title">
            {isSkillAssessment
              ? "Individual Skill Assessment"
              : "Combined Job Assessment"}
          </h2>
          <p className="assessment-subtitle">
            Test your knowledge and identify areas to improve.
          </p>
        </div>

        <div className="assessment-count">
          {questions.length} Questions
        </div>
      </header>

      <section className="assessment-progress-card">
        <div className="assessment-progress-label">
          <span>Assessment progress</span>
          <span>
            {Object.keys(answers).length} of {questions.length} answered
          </span>
        </div>

        <div className="assessment-progress-track">
          <div
            className="assessment-progress-fill"
            style={{
              width: `${
                (Object.keys(answers).length / questions.length) * 100
              }%`,
            }}
          />
        </div>
      </section>

      {error && (
        <p className="assessment-error" role="alert">
          {error}
        </p>
      )}

      <form onSubmit={handleSubmit}>
        {questions.map((question, index) => (
          <section
            key={question.questionId}
            className="assessment-question-card"
          >
            <div className="assessment-question-top">
              <span className="assessment-question-number">
                {String(index + 1).padStart(2, "0")}
              </span>
              <p className="assessment-question-label">
                Question {index + 1} of {questions.length}
              </p>
            </div>

            <h3 className="assessment-question-text">
              {question.question}
            </h3>

            {(question.skillName || question.topicName) && (
              <p className="assessment-topic">
                {question.skillName && (
                  <span>Skill: {question.skillName}</span>
                )}
                {question.skillName && question.topicName && " · "}
                {question.topicName && (
                  <span>Topic: {question.topicName}</span>
                )}
              </p>
            )}

            <div className="assessment-options">
              {[
                { key: "A", value: question.optionA },
                { key: "B", value: question.optionB },
                { key: "C", value: question.optionC },
                { key: "D", value: question.optionD },
              ]
                .filter((option) => option.value)
                .map((option) => {
                  const selected =
                    answers[question.questionId] === option.key;

                  return (
                    <label
                      key={option.key}
                      className={`assessment-option ${
                        selected ? "selected" : ""
                      }`}
                    >
                      <input
                        type="radio"
                        name={question.questionId}
                        value={option.key}
                        checked={selected}
                        onChange={() =>
                          handleAnswerChange(question.questionId, option.key)
                        }
                        disabled={submitting}
                        required
                      />
                      <span className="assessment-option-letter">
                        {option.key}.
                      </span>
                      <span className="assessment-option-text">
                        {option.value}
                      </span>
                    </label>
                  );
                })}
            </div>
          </section>
        ))}

        <div className="assessment-actions">
          <button
            type="button"
            className="assessment-button assessment-button-secondary"
            onClick={handleBack}
          >
            Go Back
          </button>

          <button
            type="submit"
            className="assessment-button assessment-button-primary"
            disabled={submitting}
          >
            {submitting
              ? "Submitting..."
              : isSkillAssessment
                ? "Submit Skill Assessment"
                : "Submit Job Assessment"}
          </button>
        </div>
      </form>
    </div>
  </main>
);

}

export default Assessment;
