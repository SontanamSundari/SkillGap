import { authenticatedRequest } from "./authenticatedApi";

export async function getCurrentUser() {
  return authenticatedRequest("/api/users/me");
}