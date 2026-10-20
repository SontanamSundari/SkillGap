
package com.skillgap.learning.config;

import com.skillgap.learning.model.LearningResource;
import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.repository.LearningResourceRepository;
import com.skillgap.learning.repository.SkillTopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LearningResourceSeeder {

    @Bean
    CommandLineRunner seedLearningResources(
            SkillTopicRepository skillTopicRepository,
            LearningResourceRepository learningResourceRepository) {
        return args -> {

            SkillTopic dependencyInjection = skillTopicRepository.findAll()
                    .stream()
                    .filter(topic -> topic.getName()
                            .equalsIgnoreCase("Dependency Injection"))
                    .findFirst()
                    .orElse(null);

            SkillTopic restApi = skillTopicRepository.findAll()
                    .stream()
                    .filter(topic -> topic.getName()
                            .equalsIgnoreCase("REST API Development"))
                    .findFirst()
                    .orElse(null);

            SkillTopic springData = skillTopicRepository.findAll()
                    .stream()
                    .filter(topic -> topic.getName()
                            .equalsIgnoreCase("Spring Data"))
                    .findFirst()
                    .orElse(null);

            if (dependencyInjection != null &&
                    learningResourceRepository.findByTopicId(
                            dependencyInjection.getId()).isEmpty()) {

                learningResourceRepository.save(
                        new LearningResource(
                                dependencyInjection.getId(),
                                "Spring Dependency Injection Documentation",
                                "DOCUMENTATION",
                                "https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html",
                                "BEGINNER"));

                learningResourceRepository.save(
                        new LearningResource(
                                dependencyInjection.getId(),
                                "Spring Framework Documentation",
                                "DOCUMENTATION",
                                "https://docs.spring.io/spring-framework/reference/",
                                "INTERMEDIATE"));
            }

            if (restApi != null &&
                    learningResourceRepository.findByTopicId(
                            restApi.getId()).isEmpty()) {

                learningResourceRepository.save(
                        new LearningResource(
                                restApi.getId(),
                                "Building REST Services with Spring",
                                "GUIDE",
                                "https://spring.io/guides/tutorials/rest",
                                "BEGINNER"));

                learningResourceRepository.save(
                        new LearningResource(
                                restApi.getId(),
                                "Spring Web MVC Documentation",
                                "DOCUMENTATION",
                                "https://docs.spring.io/spring-framework/reference/web/webmvc.html",
                                "INTERMEDIATE"));
            }

            if (springData != null &&
                    learningResourceRepository.findByTopicId(
                            springData.getId()).isEmpty()) {

                learningResourceRepository.save(
                        new LearningResource(
                                springData.getId(),
                                "Spring Data MongoDB Documentation",
                                "DOCUMENTATION",
                                "https://docs.spring.io/spring-data/mongodb/reference/",
                                "BEGINNER"));

                learningResourceRepository.save(
                        new LearningResource(
                                springData.getId(),
                                "Spring Data Repositories",
                                "GUIDE",
                                "https://docs.spring.io/spring-data/commons/reference/repositories.html",
                                "INTERMEDIATE"));
            }
            SkillTopic springSecurity = skillTopicRepository.findAll()
                    .stream()
                    .filter(topic -> topic.getName()
                            .equalsIgnoreCase("Spring Security"))
                    .findFirst()
                    .orElse(null);

            SkillTopic exceptionHandling = skillTopicRepository.findAll()
                    .stream()
                    .filter(topic -> topic.getName()
                            .equalsIgnoreCase("Exception Handling"))
                    .findFirst()
                    .orElse(null);

            SkillTopic testing = skillTopicRepository.findAll()
                    .stream()
                    .filter(topic -> topic.getName()
                            .equalsIgnoreCase("Testing"))
                    .findFirst()
                    .orElse(null);

            if (springSecurity != null &&
                    learningResourceRepository.findByTopicId(
                            springSecurity.getId()).isEmpty()) {

                learningResourceRepository.save(
                        new LearningResource(
                                springSecurity.getId(),
                                "Spring Security Reference",
                                "DOCUMENTATION",
                                "https://docs.spring.io/spring-security/reference/",
                                "BEGINNER"));

                learningResourceRepository.save(
                        new LearningResource(
                                springSecurity.getId(),
                                "Spring Security Architecture",
                                "GUIDE",
                                "https://docs.spring.io/spring-security/reference/servlet/architecture.html",
                                "INTERMEDIATE"));
            }

            if (exceptionHandling != null &&
                    learningResourceRepository.findByTopicId(
                            exceptionHandling.getId()).isEmpty()) {

                learningResourceRepository.save(
                        new LearningResource(
                                exceptionHandling.getId(),
                                "Spring MVC Exception Handling",
                                "DOCUMENTATION",
                                "https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-exceptionhandler.html",
                                "BEGINNER"));

                learningResourceRepository.save(
                        new LearningResource(
                                exceptionHandling.getId(),
                                "REST API Error Handling Guide",
                                "GUIDE",
                                "https://spring.io/guides/tutorials/rest",
                                "INTERMEDIATE"));
            }

            if (testing != null &&
                    learningResourceRepository.findByTopicId(
                            testing.getId()).isEmpty()) {

                learningResourceRepository.save(
                        new LearningResource(
                                testing.getId(),
                                "Spring Boot Testing Documentation",
                                "DOCUMENTATION",
                                "https://docs.spring.io/spring-boot/reference/testing/",
                                "BEGINNER"));

                learningResourceRepository.save(
                        new LearningResource(
                                testing.getId(),
                                "Testing Spring Applications",
                                "GUIDE",
                                "https://docs.spring.io/spring-framework/reference/testing.html",
                                "INTERMEDIATE"));
            }

        };

    }
}
