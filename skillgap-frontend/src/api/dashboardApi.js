import { authenticatedRequest } from "./authenticatedApi";

export async function getJobMatches() {
  return authenticatedRequest("/api/dashboard/job-matches");
}

export async function getSkillGap(jobId) {
  return authenticatedRequest(`/api/skill-gap/jobs/${jobId}`);
}

export async function getSkillGapSummary(jobId) {
  return authenticatedRequest(`/api/skill-gap/jobs/${jobId}/summary`);
}

export async function getRecommendations(jobId) {
  return authenticatedRequest(
    `/api/skill-gap/jobs/${jobId}/recommendations`
  );
}