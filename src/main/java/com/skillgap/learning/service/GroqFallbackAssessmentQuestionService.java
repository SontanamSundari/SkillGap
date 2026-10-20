
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class GroqFallbackAssessmentQuestionService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public GroqFallbackAssessmentQuestionService(
            @Value("${groq.api.key}") String apiKey,
            @Value("${groq.model}") String model) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Groq API key is missing. Check GROQ_API_KEY.");
        }

        this.objectMapper = new ObjectMapper();
        this.model = model;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
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
        Generate exactly %d multiple-choice assessment questions.

        Skill name: %s
        Topic name: %s

        Return a single valid JSON object with this structure:
        {
          "questions": [
            {
              "skillName": "string",
              "topicName": "string",
              "question": "string",
              "optionA": "string",
              "optionB": "string",
              "optionC": "string",
              "optionD": "string",
              "correctAnswer": "A",
              "difficulty": 2,
              "explanation": "string"
            }
          ]
        }

        Requirements:
        - The questions array must contain exactly %d objects.
        - Set skillName to the exact supplied skill name.
        - Set topicName to the exact supplied topic name.
        - Test only the specified topic.
        - Each question must have four distinct options and one correct answer.
        - correctAnswer must be exactly A, B, C, or D.
        - difficulty must be an integer from 1 to 5.
        - Include every field shown above in every question.
        - All fields except difficulty must be JSON strings.
        - Return JSON only, without Markdown fences or extra text.
        """.formatted(
                questionCount,
                skillName,
                topicName,
                questionCount);



        try {
            ObjectNode request = objectMapper.createObjectNode();
            request.put("model", model);
            request.put("temperature", 0);
            request.put("max_completion_tokens", 2000);

            ArrayNode messages = request.putArray("messages");
            ObjectNode message = messages.addObject();
            message.put("role", "user");
            message.put("content", prompt);

            request.put("response_format",
                    objectMapper.createObjectNode()
                            .put("type", "json_object"));

            String response = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(request))
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("");

            if (content.isBlank()) {
                throw new IllegalStateException(
                        "Groq returned empty question content.");
            }

            JsonNode questionNodes =
                    objectMapper.readTree(content).path("questions");

            if (!questionNodes.isArray()
                    || questionNodes.size() != questionCount) {
                throw new IllegalStateException(
                        "Groq returned an unexpected number of questions.");
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
                    "Groq API returned HTTP "
                            + exception.getStatusCode().value()
                            + ". Details: "
                            + exception.getResponseBodyAsString(),
                    exception);
        } catch (Exception exception) {
            if (exception instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            throw new RuntimeException(
                    "Failed to generate assessment questions using Groq.",
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
                    "Groq returned an invalid assessment question.");
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
                    "Groq returned duplicate assessment options.");
        }

        question.setCorrectAnswer(
                question.getCorrectAnswer()
                        .trim().toUpperCase(Locale.ROOT));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
