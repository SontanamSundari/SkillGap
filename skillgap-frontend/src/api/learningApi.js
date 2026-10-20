
import { authenticatedRequest } from "./authenticatedApi";

export async function getMySkills() {
  return authenticatedRequest("/api/users/me/skills");
}

export async function getSkillCatalog() {
  return authenticatedRequest("/api/skills");
}

export async function getTopicKnowledge() {
  return authenticatedRequest("/api/learning/topic-knowledge");
}

export async function getTopicGap(skillId) {
  return authenticatedRequest(
    `/api/learning/topic-gap/skill/${skillId}`
  );
}

export async function getLearningRecommendations(skillId) {
  return authenticatedRequest(
    `/api/learning/recommendations/skill/${skillId}`
  );
}

export async function getTopicQuestions(topicId) {
  return authenticatedRequest(
    `/api/learning/assessment/topics/${topicId}/questions`
  );
}

export async function submitTopicAssessment(topicId, answers) {
  return authenticatedRequest("/api/learning/assessment/submit", {
    method: "POST",
    body: JSON.stringify({
      topicId,
      answers,
    }),
  });
}

// Dashboard flow: existing Groq-generated combined assessment.
export async function createAssessmentSession(jobId) {
  return authenticatedRequest("/api/learning/assessment/sessions", {
    method: "POST",
    body: JSON.stringify({ jobId }),
  });
}

// Individual skill flow: requests an assessment for one selected skill.
export async function createSkillAssessmentSession(jobId, skillId) {
  return authenticatedRequest("/api/learning/assessment/sessions", {
    method: "POST",
    body: JSON.stringify({
      jobId,
      skillId,
    }),
  });
}

export async function submitAssessmentSession(sessionId, answers) {
  return authenticatedRequest("/api/learning/assessment/sessions/submit", {
    method: "POST",
    body: JSON.stringify({
      sessionId,
      answers,
    }),
  });
}

export async function generateLearningPath(jobId) {
  return authenticatedRequest(
    `/api/learning-paths/jobs/${encodeURIComponent(jobId)}/generate`,
    {
      method: "POST",
    }
  );
}

export async function getLearningPath(jobId) {
  return authenticatedRequest(
    `/api/learning-paths/jobs/${encodeURIComponent(jobId)}`
  );
}


export async function getAssessmentHistory(jobId) {
  return authenticatedRequest(
    `/api/learning/assessment/sessions/history?jobId=${encodeURIComponent(jobId)}`
  );
}
