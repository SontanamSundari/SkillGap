import { apiRequest } from "./api";

export async function authenticatedRequest(endpoint, options = {}) {
  const token = localStorage.getItem("skillgap_token");

  if (!token) {
    throw new Error("You are not authenticated.");
  }

  return apiRequest(endpoint, {
    ...options,
    headers: {
      ...(options.headers || {}),
      Authorization: `Bearer ${token}`,
    },
  });
}