import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  getSkillGap,
  getSkillGapSummary,
  getRecommendations,
} from "../api/dashboardApi";
import "./JobDetails.css";

function JobDetails() {
  const { jobId } = useParams();
  const navigate = useNavigate();

  const [summary, setSummary] = useState(null);
  const [skills, setSkills] = useState([]);
  const [recommendations, setRecommendations] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadJobDetails() {
      try {
        setError("");

        const [summaryData, skillData, recommendationData] =
          await Promise.all([
            getSkillGapSummary(jobId),
            getSkillGap(jobId),
            getRecommendations(jobId),
          ]);

        setSummary(summaryData);
        setSkills(skillData);
        setRecommendations(recommendationData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    loadJobDetails();
  }, [jobId]);

  if (loading) {
    return (
      <div className="job-details-page">
        <p>Loading job analysis...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="job-details-page">
        <h1>Job Analysis</h1>

        <p>Failed to load analysis: {error}</p>

        <button
          className="back-button"
          onClick={() => navigate("/dashboard")}
        >
          ← Back to Dashboard
        </button>
      </div>
    );
  }

  return (
    <div className="job-details-page">

      <button
        className="back-button"
        onClick={() => navigate("/dashboard")}
      >
        ← Back to Dashboard
      </button>

      <h1>Skill Gap Analysis</h1>

      {summary && (
        <div className="summary-card">

          <div className="summary-main">

            <div>
              <div className="summary-score">
                {summary.matchPercentage}%
              </div>

              <p>Overall Match</p>
            </div>

            <div className="summary-readiness">
              {summary.readinessLevel}
            </div>

          </div>

          <div className="summary-stats">

            <div className="summary-stat">
              <strong>{summary.matchedSkills}</strong>
              <span>Matched</span>
            </div>

            <div className="summary-stat">
              <strong>{summary.weakSkills}</strong>
              <span>Weak</span>
            </div>

            <div className="summary-stat">
              <strong>{summary.missingSkills}</strong>
              <span>Missing</span>
            </div>

            <div className="summary-stat">
              <strong>{summary.gapScore}</strong>
              <span>Gap Score</span>
            </div>

          </div>

        </div>
      )}

      <h2>Skill Analysis</h2>

      {skills.length === 0 ? (
        <p>No skill requirements found for this job.</p>
      ) : (
        <div className="skill-list">

          {skills.map((skill) => (
            <div
              className="skill-card"
              key={skill.skillId}
            >

              <h3>{skill.skillName}</h3>

              <p>
                <strong>Your Proficiency:</strong>{" "}
                {skill.userProficiency ?? "Not Added"}
              </p>

              <p>
                <strong>Required Proficiency:</strong>{" "}
                {skill.requiredProficiency}
              </p>

              <p>
                <strong>Status:</strong>{" "}
                <span
                  className={`status status-${skill.status}`}
                >
                  {skill.status}
                </span>
              </p>

              <p>
                <strong>Requirement:</strong>{" "}
                {skill.requirementType}
              </p>

            </div>
          ))}

        </div>
      )}

      <h2>Recommendations</h2>

      {recommendations.length === 0 ? (
        <p>No skill improvements required.</p>
      ) : (
        <div className="recommendation-list">

          {recommendations.map((recommendation) => (
            <div
              className="recommendation-card"
              key={recommendation.skillId}
            >

              <h3>{recommendation.skillName}</h3>

              <p>
                <strong>Priority:</strong>{" "}
                <span className="priority">
                  {recommendation.priority}
                </span>
              </p>

              <p>
                <strong>Impact:</strong>{" "}
                <span className="impact">
                  {recommendation.impact}
                </span>
              </p>

              <p>
                {recommendation.recommendationReason}
              </p>

            </div>
          ))}

        </div>
      )}

    </div>
  );
}

export default JobDetails;