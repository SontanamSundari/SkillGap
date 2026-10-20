
package com.skillgap.learning.service;

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

@Service
public class OpenRouterAssessmentQuestionService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public OpenRouterAssessmentQuestionService(
            @Value("${openrouter.api.key}") String apiKey,
            @Value("${openrouter.model}") String model) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException(
                    "OpenRouter API key is missing. Check OPENROUTER_API_KEY.");
        }

        this.objectMapper = new ObjectMapper();
        this.model = model;

        this.restClient = RestClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey.trim())
                .defaultHeader(
                        "Content-Type",
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public List<GeneratedQuestion> generateQuestions(
            String skillName,
            String topicName,
            int questionCount) {

        if (skillName == null || skillName.isBlank()
                || topicName == null || topicName.isBlank()) {
            throw new IllegalArgumentException(
                    "Skill name and topic name are required.");
        }

        if (questionCount < 1 || questionCount > 10) {
            throw new IllegalArgumentException(
                    "Question count must be between 1 and 10.");
        }

        String prompt = """
                Generate exactly %d multiple-choice assessment question(s).

                Skill: %s
                Topic: %s

                Return one valid JSON object in this exact structure:
                {
                  "questions": [
                    {
                      "skillName": "%s",
                      "topicName": "%s",
                      "question": "Question text",
                      "optionA": "First option",
                      "optionB": "Second option",
                      "optionC": "Third option",
                      "optionD": "Fourth option",
                      "correctAnswer": "A",
                      "difficulty": 2,
                      "explanation": "Brief explanation"
                    }
                  ]
                }

                Rules:
                - Return exactly %d question(s).
                - Use the supplied skill and topic names exactly.
                - Test the specified topic, not unrelated topics.
                - Provide four distinct options and one correct answer.
                - correctAnswer must be A, B, C or D.
                - difficulty must be an integer from 1 to 5.
                - Keep the question and explanation concise.
                - Return valid JSON only, without markdown fences.
                """.formatted(
                questionCount,
                skillName,
                topicName,
                skillName,
                topicName,
                questionCount);

        try {
            ObjectNode request = objectMapper.createObjectNode();
            request.put("model", model);
            request.put("temperature", 0);
            request.put("max_tokens", 2000);

            ArrayNode messages = request.putArray("messages");
            ObjectNode message = messages.addObject();
            message.put("role", "user");
            message.put("content", prompt);

           String response = restClient.post()
        .uri("/chat/completions")
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(request))
        .retrieve()
        .body(String.class);

JsonNode root = objectMapper.readTree(response);

JsonNode choices = root.path("choices");
JsonNode firstChoice = choices.path(0);
JsonNode messageNode = firstChoice.path("message");
JsonNode contentNode = messageNode.path("content");

String content = contentNode.isTextual()
        ? contentNode.asText()
        : "";

if (content.isBlank()) {
    String finishReason = firstChoice.path("finish_reason").asText("unknown");
    String responseId = root.path("id").asText("unknown");

    throw new IllegalStateException(
            "OpenRouter returned empty question content."
                    + " Model: " + model
                    + ", response ID: " + responseId
                    + ", finish reason: " + finishReason
                    + ", response: " + root.toString());
}

            // Some models may wrap JSON in markdown fences.
            content = content.trim();
            if (content.startsWith("```")) {
                content = content.replaceFirst("^```(?:json)?\\s*", "");
                content = content.replaceFirst("\\s*```$", "");
            }

            JsonNode questionNodes =
                    objectMapper.readTree(content).path("questions");

            if (!questionNodes.isArray()
                    || questionNodes.size() != questionCount) {
                throw new IllegalStateException(
                        "OpenRouter returned an unexpected number of questions.");
            }

            List<GeneratedQuestion> questions = new ArrayList<>();

            for (JsonNode node : questionNodes) {
                GeneratedQuestion question =
                        objectMapper.treeToValue(
                                node, GeneratedQuestion.class);

                validateQuestion(question, skillName, topicName);
                questions.add(question);
            }

            return questions;

        } catch (RestClientResponseException exception) {
            throw new RuntimeException(
                    "OpenRouter API returned HTTP "
                            + exception.getStatusCode().value()
                            + ". Details: "
                            + exception.getResponseBodyAsString(),
                    exception);
        } catch (Exception exception) {
            if (exception instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            throw new RuntimeException(
                    "Failed to generate assessment questions using OpenRouter.",
                    exception);
        }
    }

    private void validateQuestion(
            GeneratedQuestion question,
            String expectedSkill,
            String expectedTopic) {

        if (question == null
                || !expectedSkill.equalsIgnoreCase(
                        question.getSkillName() == null
                                ? "" : question.getSkillName().trim())
                || !expectedTopic.equalsIgnoreCase(
                        question.getTopicName() == null
                                ? "" : question.getTopicName().trim())
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
                                .trim().toUpperCase(Locale.ROOT))) {

            throw new IllegalStateException(
                    "OpenRouter returned an invalid assessment question.");
        }

        List<String> options = List.of(
                question.getOptionA().trim(),
                question.getOptionB().trim(),
                question.getOptionC().trim(),
                question.getOptionD().trim());

        long distinctOptions = options.stream()
                .map(option -> option.toLowerCase(Locale.ROOT))
                .distinct()
                .count();

        if (distinctOptions != 4) {
            throw new IllegalStateException(
                    "OpenRouter returned duplicate assessment options.");
        }

        question.setCorrectAnswer(
                question.getCorrectAnswer()
                        .trim().toUpperCase(Locale.ROOT));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
