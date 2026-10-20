package com.skillgap.job.service;

import com.skillgap.job.dto.ExtractedSkill;

import java.util.List;

public interface SkillExtractionService {

    List<ExtractedSkill> extractSkills(String jobDescription);
}