
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createJob, extractJobSkills } from "../api/jobApi";
import "./AddJob.css";

function AddJob() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    company: "",
    location: "",
    employmentType: "Full-time",
    experienceLevel: "Fresher",
    description: "",
  });

  const [saving, setSaving] = useState(false);
  const [extracting, setExtracting] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [jobId, setJobId] = useState("");

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((previous) => ({
      ...previous,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    setError("");
    setMessage("");
    setSaving(true);

    try {
      const createdJob = await createJob(form);

      if (!createdJob?.id) {
        throw new Error(
          "The job was saved, but the server did not return its ID."
        );
      }

      setJobId(createdJob.id);
      setSaving(false);
      setExtracting(true);
      setMessage("Job saved. Extracting skills with AI...");

      await extractJobSkills(createdJob.id);

      navigate(`/jobs/${createdJob.id}`, {
        state: {
          message: "Job saved and AI skill extraction completed.",
        },
      });
    } catch (err) {
      setError(err.message || "Unable to save the job.");
    } finally {
      setSaving(false);
      setExtracting(false);
    }
  }

  return (
    <div className="page-container add-job-page">
      <div className="add-job-header">
        <div>
          <h1>Add a Job Description</h1>
          <p>
            Paste a job description to identify required skills and
            understand your skill gaps.
          </p>
        </div>

        <button
          type="button"
          className="btn btn-secondary"
          onClick={() => navigate("/dashboard")}
        >
          Back to Dashboard
        </button>
      </div>

      <form className="add-job-card" onSubmit={handleSubmit}>
        <div className="add-job-grid">
          <div className="form-group">
            <label htmlFor="title">Job title</label>
            <input
              id="title"
              name="title"
              value={form.title}
              onChange={handleChange}
              placeholder="e.g. Java Backend Developer"
              maxLength={150}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="company">Company</label>
            <input
              id="company"
              name="company"
              value={form.company}
              onChange={handleChange}
              placeholder="e.g. Example Technologies"
              maxLength={150}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="location">Location</label>
            <input
              id="location"
              name="location"
              value={form.location}
              onChange={handleChange}
              placeholder="e.g. Hyderabad or Remote"
              maxLength={150}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="employmentType">Employment type</label>
            <select
              id="employmentType"
              name="employmentType"
              value={form.employmentType}
              onChange={handleChange}
              required
            >
              <option value="Full-time">Full-time</option>
              <option value="Internship">Internship</option>
              <option value="Contract">Contract</option>
              <option value="Part-time">Part-time</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="experienceLevel">Experience level</label>
            <select
              id="experienceLevel"
              name="experienceLevel"
              value={form.experienceLevel}
              onChange={handleChange}
              required
            >
              <option value="Fresher">Fresher</option>
              <option value="Entry-level">Entry-level</option>
              <option value="Mid-level">Mid-level</option>
              <option value="Senior">Senior</option>
            </select>
          </div>
        </div>

        <div className="form-group description-group">
          <label htmlFor="description">Full job description</label>
          <textarea
            id="description"
            name="description"
            value={form.description}
            onChange={handleChange}
            placeholder="Paste the complete job description, including responsibilities and required skills..."
            minLength={50}
            maxLength={10000}
            rows={10}
            required
          />
          <small>
            {form.description.length}/10000 characters (minimum 50).
          </small>
        </div>

        {error && (
          <div className="form-error" role="alert">
            {error}
            {jobId && (
              <p>
                Your job may already be saved. You can retry skill
                extraction from the saved job later.
              </p>
            )}
          </div>
        )}

        {message && (
          <p className="form-success" role="status">
            {message}
          </p>
        )}

        <button
          type="submit"
          className="primary-button"
          disabled={saving || extracting}
        >
          {saving
            ? "Saving job..."
            : extracting
              ? "Extracting skills..."
              : "Save Job & Extract Skills"}
        </button>
      </form>
    </div>
  );
}

export default AddJob;
