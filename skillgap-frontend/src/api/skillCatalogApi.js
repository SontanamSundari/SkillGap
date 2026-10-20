import { authenticatedRequest } from "./authenticatedApi";

export async function getSkillCatalog() {
  return authenticatedRequest("/api/skills");
}