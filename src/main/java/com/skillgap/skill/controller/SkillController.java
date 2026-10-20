package com.skillgap.skill.controller;

import com.skillgap.skill.model.Skill;
import com.skillgap.skill.repository.SkillRepository;

import com.skillgap.userSkill.repository.UserSkillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;
    public SkillController(SkillRepository skillRepository, UserSkillRepository userSkillRepository){
        this.skillRepository=skillRepository;
        this.userSkillRepository = userSkillRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Skill createSkill(@RequestBody Skill skill){
        if(skillRepository.existsByNameIgnoreCase(skill.getName())){
            throw new RuntimeException("Skill already exists");
        }
        return skillRepository.save(skill);
    }

    @GetMapping
    public List<Skill> getAllSkills(){
        return skillRepository.findAll();
    }
}
