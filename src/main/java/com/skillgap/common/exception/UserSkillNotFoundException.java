package com.skillgap.common.exception;

public class UserSkillNotFoundException extends RuntimeException {

    public UserSkillNotFoundException(String message) {
        super(message);
    }
}