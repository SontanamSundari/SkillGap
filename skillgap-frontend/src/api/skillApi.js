import { authenticatedRequest } from "./authenticatedApi";

export async function getMySkills() {
  return authenticatedRequest("/api/users/me/skills");
}

export async function addMySkill(skillId, proficiency) {
  return authenticatedRequest("/api/users/me/skills", {
    method: "POST",
    body: JSON.stringify({
      skillId,
      proficiency,
    }),
  });
}

export async function updateMySkill(skillId, proficiency) {
  return authenticatedRequest(`/api/users/me/skills/${skillId}`, {
    method: "PUT",
    body: JSON.stringify({
      proficiency,
    }),
  });
}

export async function deleteMySkill(skillId) {
  return authenticatedRequest(`/api/users/me/skills/${skillId}`, {
    method: "DELETE",
  });
}