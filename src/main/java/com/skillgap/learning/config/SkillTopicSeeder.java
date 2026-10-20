package com.skillgap.learning.config;

import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.repository.SkillTopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillTopicSeeder {

    @Bean
    CommandLineRunner seedSkillTopics(
            SkillTopicRepository skillTopicRepository
    ) {

        return args -> {

            String springBootSkillId =
                    "6aa57a229bc1aeb71074c959";

            if (skillTopicRepository.findBySkillId(springBootSkillId).isEmpty()) {

                skillTopicRepository.save(
                        new SkillTopic(
                                springBootSkillId,
                                "Dependency Injection",
                                "Managing dependencies and object creation using Spring."
                        )
                );

                skillTopicRepository.save(
                        new SkillTopic(
                                springBootSkillId,
                                "REST API Development",
                                "Building RESTful APIs using Spring Boot."
                        )
                );

                skillTopicRepository.save(
                        new SkillTopic(
                                springBootSkillId,
                                "Spring Data",
                                "Working with databases and repositories using Spring Data."
                        )
                );

                skillTopicRepository.save(
                        new SkillTopic(
                                springBootSkillId,
                                "Spring Security",
                                "Implementing authentication and authorization using Spring Security."
                        )
                );

                skillTopicRepository.save(
                        new SkillTopic(
                                springBootSkillId,
                                "Exception Handling",
                                "Handling application and API errors in Spring applications."
                        )
                );

                skillTopicRepository.save(
                        new SkillTopic(
                                springBootSkillId,
                                "Testing",
                                "Testing Spring applications using unit and integration testing."
                        )
                );
            }
        };
    }
}