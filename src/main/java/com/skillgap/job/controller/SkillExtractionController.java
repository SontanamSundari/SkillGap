package com.skillgap.job.controller;

import com.skillgap.job.dto.ExtractedSkill;
import com.skillgap.job.dto.NormalizedSkill;
import com.skillgap.job.dto.SkillExtractionRequest;
import com.skillgap.job.service.JobSkillExtractionService;
import com.skillgap.job.service.SkillExtractionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class SkillExtractionController {

    private final SkillExtractionService skillExtractionService;
    private final JobSkillExtractionService jobSkillExtractionService;

    public SkillExtractionController(
            SkillExtractionService skillExtractionService,
            JobSkillExtractionService jobSkillExtractionService
    ) {
        this.skillExtractionService = skillExtractionService;
        this.jobSkillExtractionService = jobSkillExtractionService;
    }


    @PostMapping("/extract-skills")
    public ResponseEntity<List<ExtractedSkill>> extractSkills(
            @Valid @RequestBody SkillExtractionRequest request
    ) {

        List<ExtractedSkill> skills =
                skillExtractionService.extractSkills(
                        request.getJobDescription()
                );

        return ResponseEntity.ok(skills);
    }

    @PostMapping("/extract-and-normalize")
    public ResponseEntity<List<NormalizedSkill>> extractAndNormalize(@Valid @RequestBody SkillExtractionRequest request){
        List<NormalizedSkill> skills=jobSkillExtractionService.extractAndNormalize(request.getJobDescription());
        return ResponseEntity.ok(skills);
    }
}