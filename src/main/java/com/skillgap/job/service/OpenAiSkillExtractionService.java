package com.skillgap.job.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;
import com.skillgap.job.dto.ExtractedSkill;
import com.skillgap.job.dto.SkillExtractionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiSkillExtractionService implements SkillExtractionService {

    private final OpenAIClient openAIClient;

    public OpenAiSkillExtractionService(
            @Value("${ai.api.key}") String apiKey
    ) {
        this.openAIClient = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }

    @Override
    public List<ExtractedSkill> extractSkills(String jobDescription) {

        String prompt = """
                Extract the technical and professional skills required for the job
                from the following job description.

                Rules:
                - Return only skills explicitly required or strongly implied.
                - Do not return company names.
                - Do not return job titles.
                - Do not return generic words such as teamwork unless clearly
                  relevant as a professional skill.
                - Do not invent skills that are not supported by the description.
                - Estimate required proficiency from 1 to 5.
                - 1 = beginner
                - 2 = basic
                - 3 = intermediate
                - 4 = advanced
                - 5 = expert.

                Job description:
                """ + jobDescription;

        StructuredChatCompletionCreateParams<SkillExtractionResult> params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_5_2)
                        .responseFormat(SkillExtractionResult.class)
                        .addUserMessage(prompt)
                        .build();

        var completion = openAIClient
                .chat()
                .completions()
                .create(params);

        return completion.choices()
                .stream()
                .flatMap(choice -> choice.message().content().stream())
                .findFirst()
                .map(SkillExtractionResult::getSkills)
                .orElse(List.of());
    }
}