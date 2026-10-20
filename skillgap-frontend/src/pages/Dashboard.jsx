
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getCurrentUser } from "../api/userApi";
import { getJobMatches } from "../api/dashboardApi";
import "./Dashboard.css";

function Dashboard() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [jobMatches, setJobMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadDashboard() {
      try {
        setError("");

        const [userData, jobData] = await Promise.all([
          getCurrentUser(),
          getJobMatches(),
        ]);

        if (cancelled) return;

        setUser(userData);
        setJobMatches(Array.isArray(jobData) ? jobData : []);
      } catch (err) {
        if (!cancelled) {
          setError(err.message || "Failed to load the dashboard.");
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadDashboard();

    return () => {
      cancelled = true;
    };
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  if (loading) {
    return <p className="page-loading">Loading dashboard...</p>;
  }

  if (error) {
    return (
      <div className="page-container">
        <div className="error-card">
          <h1>SkillGap Dashboard</h1>
          <p>Failed to load dashboard: {error}</p>

          <button
            className="btn btn-primary"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="page-container dashboard-page">
      {/* Dashboard header */}
      <div className="dashboard-header">
        <div>
          <h1>SkillGap Dashboard</h1>
          <p className="dashboard-subtitle">
            Track your skills and discover matching jobs.
          </p>
        </div>

        <button
          className="btn btn-secondary"
          onClick={handleLogout}
        >
          Logout
        </button>
      </div>

      {/* Logged-in user */}
      {user && (
        <div className="user-card">
          <div className="user-avatar">
            {user.name?.charAt(0).toUpperCase()}
          </div>

          <div>
            <h2>Welcome, {user.name}</h2>
            <p>{user.email}</p>
          </div>
        </div>
      )}

      {/* Job matches */}
      <h2 className="section-title">Job Matches</h2>

      {jobMatches.length === 0 ? (
        <div className="empty-state">
          <h3>No jobs available yet</h3>
          <p>Add a job description to get started with SkillGap.</p>

          <button
            className="primary-button"
            type="button"
            onClick={() => navigate("/jobs/new")}
          >
            + Add Job Description
          </button>
        </div>
      ) : (
        <div className="job-list">
          {jobMatches.map((job) => (
            <div className="job-card" key={job.jobId}>
              <div className="job-header">
                <div>
                  <h3>{job.title}</h3>
                  <p className="company-name">{job.company}</p>
                </div>

                <div className="match-badge">
                  {job.matchPercentage}%
                </div>
              </div>

              <div className="job-details">
                <span>📍 {job.location}</span>
                <span>💼 {job.employmentType}</span>
                <span>🎓 {job.experienceLevel}</span>
              </div>

              <hr />

              <div className="match-info">
                <div>
                  <span>Gap Score</span>
                  <strong>{job.gapScore}</strong>
                </div>

                <div>
                  <span>Readiness</span>
                  <strong>{job.readinessLevel}</strong>
                </div>

                <div>
                  <span>Total Required</span>
                  <strong>{job.totalRequiredSkills}</strong>
                </div>
              </div>

              <div className="skills-info">
                <div>
                  <h4>Matched Skills</h4>
                  <p>{job.matchedSkills}</p>
                </div>

                <div>
                  <h4>Weak Skills</h4>
                  <p>{job.weakSkills}</p>
                </div>

                <div>
                  <h4>Missing Skills</h4>
                  <p>{job.missingSkills}</p>
                </div>
              </div>

              {/* Actions specific to this job */}
              <div className="job-card-actions">
                <button
                  className="primary-button"
                  type="button"
                  onClick={() =>
                    navigate(
                      `/jobs/${encodeURIComponent(job.jobId)}`
                    )
                  }
                >
                  View Skill Gap
                </button>

                <button
                  className="primary-button"
                  type="button"
                  onClick={() =>
                    navigate(
                      `/jobs/${encodeURIComponent(job.jobId)}/learning`
                    )
                  }
                >
                  View Learning Plan
                </button>

                <button
                  className="primary-button"
                  type="button"
                  onClick={() =>
                    navigate(
                      `/jobs/${encodeURIComponent(job.jobId)}/assessment`
                    )
                  }
                >
                  Start Job Readiness Assessment
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Dashboard actions */}
      <div className="dashboard-actions">
        <button
          className="manage-skills-btn"
          type="button"
          onClick={() => navigate("/skills")}
        >
          Manage My Skills
        </button>

        <button
          className="primary-button"
          type="button"
          onClick={() => navigate("/jobs/new")}
        >
          + Add Job Description
        </button>
      </div>
    </div>
  );
}

export default Dashboard;
