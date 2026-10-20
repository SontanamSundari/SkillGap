import { authenticatedRequest } from "./authenticatedApi";

export async function createJob(jobData) {
  return authenticatedRequest("/api/jobs", {
    method: "POST",
    body: JSON.stringify(jobData),
  });
}

export async function extractJobSkills(jobId) {
  return authenticatedRequest(
    `/api/jobs/${encodeURIComponent(jobId)}/requirements/ai`,
    {
      method: "POST",
    }
  );
}

export async function getJobRequirements(jobId) {
  return authenticatedRequest(
    `/api/jobs/${encodeURIComponent(jobId)}/requirements`
  );
}
