package com.skillgap.learning.controller;

import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.service.SkillTopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning/topics")
public class SkillTopicController {

    private final SkillTopicService skillTopicService;

    public SkillTopicController(SkillTopicService skillTopicService) {
        this.skillTopicService = skillTopicService;
    }

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<SkillTopic>> getTopicsBySkillId(
            @PathVariable String skillId
    ) {
        return ResponseEntity.ok(
                skillTopicService.getTopicsBySkillId(skillId)
        );
    }
}