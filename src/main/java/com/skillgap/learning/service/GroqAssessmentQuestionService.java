
package com.skillgap.learning.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.skillgap.learning.dto.GeneratedQuestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// @Service
public class GroqAssessmentQuestionService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public GroqAssessmentQuestionService(
            @Value("${ai.api.key}") String apiKey,
            @Value("${ai.model}") String model) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Gemini API key is missing. Check GEMINI_API_KEY."
            );
        }

        this.objectMapper = new ObjectMapper();
        this.model = model;

        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader(
                        "x-goog-api-key",
                        apiKey.trim()
                )
                .defaultHeader(
                        "Content-Type",
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }

    public List<GeneratedQuestion> generateQuestions(
            String skillName,
            String topicName,
            int questionCount) {

        if (skillName == null || skillName.isBlank()) {
            throw new IllegalArgumentException("Skill name is required.");
        }

        if (topicName == null || topicName.isBlank()) {
            throw new IllegalArgumentException("Topic name is required.");
        }

        if (questionCount < 1 || questionCount > 10) {
            throw new IllegalArgumentException(
                    "Question count must be between 1 and 10."
            );
        }

        String prompt = """
                Generate exactly %d multiple-choice questions.

                Skill: %s
                Topic: %s

                Return one valid JSON object with a "questions" array.
                The array must contain exactly %d questions.

                Every question must contain:
                skillName, topicName, question, optionA, optionB,
                optionC, optionD, correctAnswer, difficulty, explanation.

                Rules:
                - Use the exact supplied skill and topic names.
                - Test practical technical understanding.
                - Provide four distinct options, A, B, C and D.
                - Exactly one option must be correct.
                - correctAnswer must be A, B, C or D.
                - difficulty must be an integer from 1 to 5.
                - Keep questions, options and explanations concise.
                - Return JSON only, without markdown.
                """.formatted(
                questionCount,
                skillName,
                topicName,
                questionCount
        );

        try {
            ObjectNode requestNode = objectMapper.createObjectNode();

            ArrayNode contents = objectMapper.createArrayNode();
            ObjectNode content = objectMapper.createObjectNode();
            ArrayNode parts = objectMapper.createArrayNode();

            parts.addObject().put("text", prompt);
            content.set("parts", parts);
            contents.add(content);

            requestNode.set("contents", contents);

            ObjectNode generationConfig =
                    objectMapper.createObjectNode();

            generationConfig.put("temperature", 0);
            generationConfig.put("maxOutputTokens", 4096);
            generationConfig.put("responseMimeType", "application/json");

            requestNode.set("generationConfig", generationConfig);

            String requestBody =
                    objectMapper.writeValueAsString(requestNode);

            String response = restClient.post()
                    .uri("/models/{model}:generateContent", model)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                throw new IllegalStateException(
                        "Gemini returned an empty API response."
                );
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode candidates = root.path("candidates");

            if (!candidates.isArray() || candidates.isEmpty()) {
                String blockReason = root.path("promptFeedback")
                        .path("blockReason")
                        .asText("No candidate returned");

                throw new IllegalStateException(
                        "Gemini returned no assessment content. Reason: "
                                + blockReason
                );
            }

            JsonNode candidate = candidates.get(0);
            String finishReason =
                    candidate.path("finishReason").asText("");

            if ("MAX_TOKENS".equalsIgnoreCase(finishReason)) {
                throw new IllegalStateException(
                        "Gemini truncated the response. Try fewer questions."
                );
            }

            JsonNode partsNode = candidate.path("content").path("parts");
            StringBuilder contentBuilder = new StringBuilder();

            if (partsNode.isArray()) {
                for (JsonNode part : partsNode) {
                    String text = part.path("text").asText("");
                    if (!text.isBlank()) {
                        contentBuilder.append(text);
                    }
                }
            }

            String generatedContent = contentBuilder.toString();

            if (generatedContent.isBlank()) {
                throw new IllegalStateException(
                        "Gemini returned empty question content."
                );
            }

            JsonNode questionRoot;

            try {
                questionRoot = objectMapper.readTree(generatedContent);
            } catch (JsonProcessingException exception) {
                throw new IllegalStateException(
                        "Gemini returned invalid JSON. Response length: "
                                + generatedContent.length() + " characters.",
                        exception
                );
            }

            JsonNode questionNodes = questionRoot.path("questions");

            if (!questionNodes.isArray()
                    || questionNodes.size() != questionCount) {
                throw new IllegalStateException(
                        "Gemini returned "
                                + (questionNodes.isArray()
                                ? questionNodes.size()
                                : "an invalid question array")
                                + " instead of "
                                + questionCount
                                + " questions."
                );
            }

            List<GeneratedQuestion> questions = new ArrayList<>();

            for (JsonNode node : questionNodes) {
                GeneratedQuestion question =
                        objectMapper.treeToValue(
                                node,
                                GeneratedQuestion.class
                        );

                validateQuestion(question, skillName, topicName);
                questions.add(question);
            }

            return questions;

        } catch (RestClientResponseException exception) {
            throw new RuntimeException(
                    "Gemini API returned HTTP "
                            + exception.getStatusCode().value()
                            + ". Details: "
                            + exception.getResponseBodyAsString(),
                    exception
            );

        } catch (JsonProcessingException exception) {
            throw new RuntimeException(
                    "Could not process the Gemini request or response JSON.",
                    exception
            );

        } catch (RuntimeException exception) {
            throw exception;

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to generate assessment questions using Gemini.",
                    exception
            );
        }
    }

    private void validateQuestion(
            GeneratedQuestion question,
            String expectedSkill,
            String expectedTopic) {

        if (question == null
                || !expectedSkill.equalsIgnoreCase(
                        question.getSkillName() == null
                                ? ""
                                : question.getSkillName().trim()
                )
                || !expectedTopic.equalsIgnoreCase(
                        question.getTopicName() == null
                                ? ""
                                : question.getTopicName().trim()
                )
                || isBlank(question.getQuestion())
                || isBlank(question.getOptionA())
                || isBlank(question.getOptionB())
                || isBlank(question.getOptionC())
                || isBlank(question.getOptionD())
                || isBlank(question.getExplanation())
                || question.getDifficulty() < 1
                || question.getDifficulty() > 5
                || question.getCorrectAnswer() == null
                || !List.of("A", "B", "C", "D").contains(
                        question.getCorrectAnswer()
                                .trim()
                                .toUpperCase(Locale.ROOT)
                )) {

            throw new IllegalStateException(
                    "Gemini returned an invalid assessment question."
            );
        }

        List<String> options = List.of(
                question.getOptionA().trim(),
                question.getOptionB().trim(),
                question.getOptionC().trim(),
                question.getOptionD().trim()
        );

        long distinctOptions = options.stream()
                .map(option -> option.toLowerCase(Locale.ROOT))
                .distinct()
                .count();

        if (distinctOptions != 4) {
            throw new IllegalStateException(
                    "Gemini returned duplicate assessment options."
            );
        }

        question.setCorrectAnswer(
                question.getCorrectAnswer()
                        .trim()
                        .toUpperCase(Locale.ROOT)
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
