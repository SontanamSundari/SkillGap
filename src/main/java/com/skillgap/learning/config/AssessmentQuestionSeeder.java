package com.skillgap.learning.config;

import com.skillgap.learning.model.AssessmentQuestion;
import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.repository.AssessmentQuestionRepository;
import com.skillgap.learning.repository.SkillTopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssessmentQuestionSeeder {

    @Bean
    CommandLineRunner seedAssessmentQuestions(
            SkillTopicRepository skillTopicRepository,
            AssessmentQuestionRepository questionRepository
    ) {
        return args -> {

            if (questionRepository.count() > 0) {
                return;
            }

            SkillTopic dependencyInjection =
                    skillTopicRepository.findAll()
                            .stream()
                            .filter(topic -> topic.getName()
                                    .equalsIgnoreCase("Dependency Injection"))
                            .findFirst()
                            .orElse(null);

            SkillTopic restApi =
                    skillTopicRepository.findAll()
                            .stream()
                            .filter(topic -> topic.getName()
                                    .equalsIgnoreCase("REST API Development"))
                            .findFirst()
                            .orElse(null);

            SkillTopic springData =
                    skillTopicRepository.findAll()
                            .stream()
                            .filter(topic -> topic.getName()
                                    .equalsIgnoreCase("Spring Data"))
                            .findFirst()
                            .orElse(null);

            if (dependencyInjection != null) {

                questionRepository.save(
                        new AssessmentQuestion(
                                dependencyInjection.getId(),
                                "What is the main purpose of Dependency Injection in Spring?",
                                "To create database tables",
                                "To provide required dependencies to objects",
                                "To compile Java code",
                                "To handle HTTP requests",
                                "B",
                                2
                        )
                );

                questionRepository.save(
                        new AssessmentQuestion(
                                dependencyInjection.getId(),
                                "Which annotation is commonly used for constructor-based dependency injection?",
                                "@Autowired",
                                "@Entity",
                                "@RestController",
                                "@Table",
                                "A",
                                2
                        )
                );
            }

            if (restApi != null) {

                questionRepository.save(
                        new AssessmentQuestion(
                                restApi.getId(),
                                "Which HTTP method is normally used to retrieve a resource?",
                                "POST",
                                "PUT",
                                "GET",
                                "DELETE",
                                "C",
                                1
                        )
                );

                questionRepository.save(
                        new AssessmentQuestion(
                                restApi.getId(),
                                "Which annotation is commonly used to create a REST controller in Spring Boot?",
                                "@Entity",
                                "@RestController",
                                "@Repository",
                                "@Configuration",
                                "B",
                                1
                        )
                );
            }

            if (springData != null) {

                questionRepository.save(
                        new AssessmentQuestion(
                                springData.getId(),
                                "Which Spring Data interface is commonly extended to create a repository?",
                                "MongoRepository",
                                "HttpRepository",
                                "RestRepository",
                                "WebRepository",
                                "A",
                                2
                        )
                );

                questionRepository.save(
                        new AssessmentQuestion(
                                springData.getId(),
                                "What is the primary purpose of a repository in Spring Data?",
                                "To define database access operations",
                                "To create UI components",
                                "To configure CSS",
                                "To start the JVM",
                                "A",
                                2
                        )
                );
            }
        };
    }
}