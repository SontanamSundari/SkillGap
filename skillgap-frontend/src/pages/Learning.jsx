
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  generateLearningPath,
  getLearningPath,
  getAssessmentHistory,
} from "../api/learningApi";
import "./Learning.css";

export default function Learning() {
  const { jobId } = useParams();
  const navigate = useNavigate();

  const [learningPath, setLearningPath] = useState(null);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);
  const [error, setError] = useState("");
  const [assessmentHistory, setAssessmentHistory] = useState([]);
const [historyLoading, setHistoryLoading] = useState(false);
const [historyError, setHistoryError] = useState("");

  async function loadLearningPath() {
    if (!jobId) {
      setError("Select a job first to view its learning plan.");
      setLoading(false);
      return;
    }

    try {
      setError("");
      const data = await getLearningPath(jobId);
      setLearningPath(data);
    } catch (err) {
      setLearningPath(null);
      setError(
        err.message ||
          "No learning plan found for this job. Generate one to begin."
      );
    } finally {
      setLoading(false);
    }
  }

async function loadAssessmentHistory() {
  console.log("loadAssessmentHistory invoked", { jobId });

  if (!jobId) return;

  try {
    setHistoryLoading(true);
    setHistoryError("");

    const data = await getAssessmentHistory(jobId);
    console.log("Assessment history response:", data);

    setAssessmentHistory(Array.isArray(data) ? data : []);
  } catch (err) {
    console.error("Assessment history error:", err);
    setHistoryError(err.message || "Unable to load assessment history.");
  } finally {
    setHistoryLoading(false);
  }
}


  useEffect(() => {
    loadLearningPath();
    loadAssessmentHistory();
  }, [jobId]);



  async function handleGenerate() {
    try {
      setGenerating(true);
      setError("");

      const data = await generateLearningPath(jobId);

      if (data && data.items) {
        setLearningPath(data);
      } else {
        await loadLearningPath();
      }
    } catch (err) {
      setError(
        err.message || "Unable to generate the learning plan."
      );
    } finally {
      setGenerating(false);
    }
  }

  if (loading) {
    return <div className="container mt-4">Loading learning plan...</div>;
  }

  if (!jobId) {
    return (
      <div className="container mt-4">

        <h2>Personalized Learning Plan</h2>
        <p>Open a job's details page to view its learning plan.</p>
        <button
          className="btn btn-primary"
          onClick={() => navigate("/dashboard")}
        >
          Go to Dashboard
        </button>
      </div>
    );
  }

  const items = learningPath?.items || [];

  return (
    <div className="learning-page">
      <div className="learning-header">
        <div>
          <h2>Personalized Learning Plan</h2>
          <p className="text-muted mb-0">
            Learn the skills required for your selected job.
          </p>
        </div>

        <div className="d-flex gap-2 flex-wrap">
          
<button
  type="button"
  className="btn btn-outline-primary"
  onClick={() => navigate("/dashboard")}
>
  ← Back to Dashboard
</button>

          <button
            className="btn btn-outline-primary"
            onClick={() => navigate(`/jobs/${jobId}`)}
          >
            View Job Skill Gaps
          </button>

          {/* <button
            className="btn btn-primary"
            onClick={() => navigate(`/jobs/${jobId}/assessment`)}
          >
            Take Assessment
          </button> */}
        </div>
        
      </div>

      {error && (
        <div className="alert alert-info" role="alert">
          {error}
        </div>
      )}

      {!learningPath && (
        <div className="card shadow-sm p-4">
          <h5>Start your learning journey</h5>
          <p>
            Generate a plan based on the missing and weak skills
            identified for this job.
          </p>

          <button
            className="btn btn-success align-self-start"
            onClick={handleGenerate}
            disabled={generating}
          >
            {generating ? "Generating..." : "Generate Learning Plan"}
          </button>
        </div>
      )}

      {learningPath && (
        <>
          <div className="learning-section">
            <div className="d-flex justify-content-between align-items-center flex-wrap gap-2">
              <div>
                <h5 className="mb-1">Your learning roadmap</h5>
                <p className="text-muted mb-0">
                  Focus on job-related skill gaps first.
                </p>
              </div>

              <button
                className="btn btn-outline-success"
                onClick={handleGenerate}
                disabled={generating}
              >
                {generating ? "Refreshing..." : "Refresh Plan"}
              </button>
            </div>
          </div>

          {items.length === 0 ? (
            <div className="alert alert-success">
              No learning gaps are currently recommended for this job.
              Review your assessment results to verify your readiness.
            </div>
          ) : (
            <div className="row g-3">
              {items.map((item, index) => (
                <div
                  className="col-12"
                  key={item.skillId || `${item.skillName}-${index}`}
                >
                  <div className="learning-recommendation-card">
                    <div className="card-body">
                      <div className="d-flex justify-content-between align-items-start flex-wrap gap-2">
                        <div>
                          <h5 className="card-title mb-1">
                            {index + 1}. {item.skillName || "Required Skill"}
                          </h5>
                          <p className="text-muted mb-2">
                            Priority: {item.priority ?? "Not specified"}
                            {item.gapType ? ` · ${item.gapType}` : ""}
                          </p>
                        </div>

                        <span className="badge bg-primary">
                          {item.progressPercentage ?? 0}% complete
                        </span>
                      </div>

                      <div
                        className="progress mb-3"
                        role="progressbar"
                        aria-label={`${item.skillName || "Skill"} learning progress`}
                        aria-valuenow={item.progressPercentage ?? 0}
                        aria-valuemin="0"
                        aria-valuemax="100"
                      >
                        <div
                          className="progress-bar"
                          style={{
                            width: `${Math.max(
                              0,
                              Math.min(100, item.progressPercentage ?? 0)
                            )}%`,
                          }}
                        />
                      </div>

                      {item.estimatedHours != null && (
                        <p>
                          <strong>Estimated time:</strong>{" "}
                          {item.estimatedHours} hours
                        </p>
                      )}

                      <h6>Topics to learn</h6>

                      {item.topics?.length > 0 ? (
                        <ul className="list-group list-group-flush mb-3">
                          {item.topics.map((topic, topicIndex) => {
                            const topicId =
                              typeof topic === "string"
                                ? topic
                                : topic.topicId || topic.id;

                            const topicName =
                              typeof topic === "string"
                                ? topic
                                : topic.topicName ||
                                  topic.name ||
                                  "Topic";

                            const completed = (
                              item.completedTopics || []
                            ).some(
                              (completedTopic) =>
                                completedTopic === topicId ||
                                completedTopic === topicName
                            );

                            return (
                              <li
                                className="list-group-item px-0"
                                key={topicId || `${topicName}-${topicIndex}`}
                              >
                                <div className="d-flex justify-content-between align-items-center gap-2">
                                  <span>{topicName}</span>
                                  {completed && (
                                    <span className="badge bg-success">
                                      Completed
                                    </span>
                                  )}
                                </div>

                                {topic.description && (
                                  <p className="text-muted small mb-1">
                                    {topic.description}
                                  </p>
                                )}
                              </li>
                            );
                          })}
                        </ul>
                      ) : (
                        <p className="text-muted">
                          No topics are configured for this skill yet.
                        </p>
                      )}

                      <h6>Learning resources</h6>

                      {item.resources?.length > 0 ? (
                        <ul>
                          {item.resources.map((resource, resourceIndex) => (
                            <li
                              key={
                                resource.id ||
                                `${resource.url}-${resourceIndex}`
                              }
                            >
                              {resource.url ? (
                                <a
                                  href={resource.url}
                                  target="_blank"
                                  rel="noopener noreferrer"
                                >
                                  {resource.title || "Open learning resource"}
                                </a>
                              ) : (
                                resource.title || "Resource link unavailable"
                              )}

                              {resource.resourceType && (
                                <span className="text-muted small">
                                  {" "}({resource.resourceType})
                                </span>
                              )}
                            </li>
                          ))}
                        </ul>
                      ) : (
                        <p className="text-muted">
                          No learning resources are configured for this skill yet.
                        </p>
                      )}


<button
  type="button"
  className="btn btn-outline-primary mt-2"
  onClick={() => {
    if (!item.skillId) {
      console.error("Missing skillId for:", item.skillName);
      alert("This skill does not have a skill ID. Please check the learning plan data.");
      return;
    }

    navigate(
      `/jobs/${jobId}/skills/${encodeURIComponent(item.skillId)}/assessment`
    );
  }}
>
  Assess Skill
</button>

                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </>
      )}
      
<section className="learning-section mt-4">
  <h5>Assessment History</h5>
  <p className="text-muted">
    Track your completed assessment attempts for this job.
  </p>

  {historyLoading && <p>Loading assessment history...</p>}

  {historyError && (
    <div className="alert alert-warning" role="alert">
      {historyError}
    </div>
  )}

  {!historyLoading &&
    !historyError &&
    assessmentHistory.filter(
      (attempt) => attempt.status === "SUBMITTED"
    ).length === 0 && (
      <p className="text-muted">
        No completed assessments yet. Complete an assessment to see your scores here.
      </p>
    )}

  {!historyLoading &&
    !historyError &&
    assessmentHistory.filter(
      (attempt) => attempt.status === "SUBMITTED"
    ).map((attempt, index, completedAttempts) => {
      const previousAttempt = completedAttempts[index + 1];
      const scoreChange =
        previousAttempt &&
        attempt.scorePercentage != null &&
        previousAttempt.scorePercentage != null
          ? attempt.scorePercentage - previousAttempt.scorePercentage
          : null;

      return (
        <div
          className="card shadow-sm p-3 mb-3"
          key={attempt.sessionId}
        >
          <div className="d-flex justify-content-between align-items-start flex-wrap gap-2">
            <div>
              <strong>
                Attempt {completedAttempts.length - index}
              </strong>
              <p className="text-muted small mb-1">
                {attempt.submittedAt
                  ? new Date(attempt.submittedAt).toLocaleString()
                  : "Submission date unavailable"}
              </p>
              <p className="mb-0">
                Questions: {attempt.totalQuestions ?? 0}
              </p>
            </div>

            <div className="text-end">
              <h4 className="mb-1">
                {attempt.scorePercentage ?? 0}%
              </h4>

              {scoreChange !== null && (
                <span
                  className={
                    scoreChange > 0
                      ? "text-success"
                      : scoreChange < 0
                      ? "text-danger"
                      : "text-muted"
                  }
                >
                  {scoreChange > 0 ? "+" : ""}
                  {scoreChange}% vs previous attempt
                </span>
              )}
            </div>
          </div>
        </div>
      );
    })}
</section>

    </div>
    
  );
}
