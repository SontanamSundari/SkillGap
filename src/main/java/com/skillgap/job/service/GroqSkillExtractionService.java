package com.skillgap.job.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillgap.job.dto.ExtractedSkill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class GroqSkillExtractionService implements SkillExtractionService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GroqSkillExtractionService(
            @Value("${ai.api.key}") String apiKey
    ) {
        this.objectMapper = new ObjectMapper();

        String cleanApiKey = apiKey.trim();

        this.restClient = RestClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .defaultHeader("Authorization", "Bearer " + cleanApiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public List<ExtractedSkill> extractSkills(String jobDescription) {

        String prompt = """
                Extract the technical and professional skills required for the job
                from the following job description.

                Return ONLY valid JSON in this exact format:
                {
                "skills": [
                    {
                    "name": "Java",
                    "proficiency": 4,
                    "requirementType": "MUST_HAVE"
                    }
                ]
                }

                Rules:
                - Return only skills explicitly required or strongly implied.
                - Do not return company names.
                - Do not return job titles.
                - Do not invent skills.
                - Estimate required proficiency from 1 to 5.
                - 1 = beginner
                - 2 = basic
                - 3 = intermediate
                - 4 = advanced
                - 5 = expert.
                - Classify each skill as either MUST_HAVE or PREFERRED.
                - MUST_HAVE means the job clearly requires the skill.
                - PREFERRED means the job says the skill is preferred, desirable,
                nice to have, or gives it as an advantage.

                Job description:
                """ + jobDescription;

        String requestBody = """
                {
                  "model": "openai/gpt-oss-120b",
                  "messages": [
                    {
                      "role": "user",
                      "content": %s
                    }
                  ],
                  "temperature": 0
                }
                """.formatted(toJsonString(prompt));

        String response = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        return parseSkills(response);
    }

    private List<ExtractedSkill> parseSkills(String response) {

        try {
            JsonNode root = objectMapper.readTree(response);

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            JsonNode skillRoot = objectMapper.readTree(content);

            List<ExtractedSkill> skills = new ArrayList<>();

            for (JsonNode skillNode : skillRoot.path("skills")) {

                String name = skillNode.path("name").asText();
                int proficiency = skillNode.path("proficiency").asInt();
                String requirementType = skillNode.path("requirementType").asText();

                if (!name.isBlank()
                        && proficiency >= 1
                        && proficiency <= 5
                        && (requirementType.equals("MUST_HAVE")
                        || requirementType.equals("PREFERRED"))) {

                    skills.add(
                            new ExtractedSkill(
                                    name,
                                    proficiency,
                                    requirementType
                            )
                    );
                }
            }

            return skills;

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Failed to parse Groq skill extraction response",
                    exception
            );
        }
    }

    private String toJsonString(String value) {

        try {
            return objectMapper.writeValueAsString(value);

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Failed to create Groq request",
                    exception
            );
        }
    }
}