package com.skillgap.common.exception;

public class UserSkillAlreadyExistsException extends RuntimeException {

    public UserSkillAlreadyExistsException(String message) {
        super(message);
    }
}