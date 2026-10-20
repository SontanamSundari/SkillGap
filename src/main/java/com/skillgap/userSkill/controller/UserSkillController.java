package com.skillgap.userSkill.controller;

import com.skillgap.userSkill.model.UserSkill;
import com.skillgap.userSkill.service.UserSkillService;

import jakarta.validation.Valid;
import com.skillgap.userSkill.dto.AddUserSkillRequest;
import com.skillgap.userSkill.dto.UpdateUserSkillRequest;
import com.skillgap.userSkill.dto.UpdateUserSkillRequest;
import jakarta.validation.Valid;
import com.skillgap.userSkill.dto.AddUserSkillRequest;
import com.skillgap.userSkill.dto.UserSkillResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.skillgap.userSkill.dto.UserSkillResponse;
import java.util.List;

@RestController
@RequestMapping("/api/users/me/skills")
public class UserSkillController {
    private final UserSkillService userSkillService;
    public UserSkillController(UserSkillService userSkillService){
        this.userSkillService=userSkillService;
    }   

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserSkillResponse addSkill(
            Authentication authentication,
             @Valid @RequestBody AddUserSkillRequest request) {

        String userId = authentication.getName();

        return userSkillService.addSkill(
                userId,
                request.getSkillId(),
                request.getProficiency()
        );
    }

    @GetMapping
    public List<UserSkillResponse> getMySkills(Authentication authentication){
        String userId = authentication.getName();
        return userSkillService.getUserSkills(userId);
    }

    @PutMapping("/{skillId}")
    public UserSkillResponse updateSkill(
            Authentication authentication,
            @PathVariable String skillId,
             @Valid @RequestBody UpdateUserSkillRequest request) {
        String userId = authentication.getName();
        return userSkillService.updateSkill(
                userId,
                skillId,
                 request.getProficiency()
        );
    }

    @DeleteMapping("/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(Authentication authentication,@PathVariable String skillId){
        String userId = authentication.getName();
        userSkillService.deleteSkill(userId, skillId);
    }
}
