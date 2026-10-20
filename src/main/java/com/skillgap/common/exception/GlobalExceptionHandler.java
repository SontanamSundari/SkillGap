package com.skillgap.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExists(
            EmailAlreadyExistsException exception) {

        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request");

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                message,
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
@ExceptionHandler(SkillNotFoundException.class)
public ResponseEntity<ApiError> handleSkillNotFound(
        SkillNotFoundException exception) {

    ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            Instant.now()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}

@ExceptionHandler(UserSkillNotFoundException.class)
public ResponseEntity<ApiError> handleUserSkillNotFound(
        UserSkillNotFoundException exception) {

    ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            Instant.now()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}
@ExceptionHandler(JobNotFoundException.class)
public ResponseEntity<ApiError> handleJobNotFound(
        JobNotFoundException exception) {

    ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            Instant.now()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}
@ExceptionHandler(DuplicateJobRequirementException.class)
public ResponseEntity<ApiError> handleDuplicateJobRequirement(
        DuplicateJobRequirementException exception) {

    ApiError error = new ApiError(
            HttpStatus.CONFLICT.value(),
            exception.getMessage(),
            Instant.now()
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
}
@ExceptionHandler(UserSkillAlreadyExistsException.class)
public ResponseEntity<ApiError> handleUserSkillAlreadyExists(
        UserSkillAlreadyExistsException exception) {

    ApiError error = new ApiError(
            HttpStatus.CONFLICT.value(),
            exception.getMessage(),
            Instant.now()
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
}
}