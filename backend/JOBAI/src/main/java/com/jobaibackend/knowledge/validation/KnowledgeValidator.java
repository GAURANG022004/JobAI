package com.jobaibackend.knowledge.validation;

import com.jobaibackend.storage.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for knowledge file operations.
 * Handles validation of file existence, readability, and JSON validity.
 */
@Component
public class KnowledgeValidator {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeValidator.class);
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    private final FileManager fileManager;

    public KnowledgeValidator(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Validates that a file exists and is readable.
     *
     * @param filePath path to validate
     * @return validation result with error messages if any
     */
    public ValidationResult validateFileExists(Path filePath) {
        List<String> errors = new ArrayList<>();

        if (!fileManager.fileExists(filePath)) {
            String error = "File does not exist: " + filePath;
            errors.add(error);
            logger.warn(error);
            return new ValidationResult(false, errors);
        }

        if (!fileManager.isReadable(filePath)) {
            String error = "File is not readable: " + filePath;
            errors.add(error);
            logger.warn(error);
        }

        if (!fileManager.isWritable(filePath)) {
            String error = "File is not writable: " + filePath;
            errors.add(error);
            logger.warn(error);
        }

        if (errors.isEmpty()) {
            logger.debug("File validation passed: {}", filePath);
            return new ValidationResult(true, List.of());
        }

        return new ValidationResult(false, errors);
    }

    /**
     * Validates file content for JSON validity and size constraints.
     *
     * @param content content to validate
     * @param filePath file path for logging
     * @return validation result
     */
    public ValidationResult validateContent(String content, Path filePath) {
        List<String> errors = new ArrayList<>();

        if (content == null || content.trim().isEmpty()) {
            String error = "Content is empty for file: " + filePath;
            errors.add(error);
            logger.warn(error);
            return new ValidationResult(false, errors);
        }

        if (!fileManager.isValidJson(content)) {
            String error = "Invalid JSON content in file: " + filePath;
            errors.add(error);
            logger.warn(error);
        }

        if (content.length() > MAX_FILE_SIZE) {
            String error = "File content exceeds maximum size of 10MB: " + filePath;
            errors.add(error);
            logger.warn(error);
        }

        if (errors.isEmpty()) {
            logger.debug("Content validation passed: {}", filePath);
            return new ValidationResult(true, List.of());
        }

        return new ValidationResult(false, errors);
    }

    /**
     * Validates that a file can be created (parent directory exists and is writable).
     *
     * @param filePath path to validate
     * @return validation result
     */
    public ValidationResult validateFileCreation(Path filePath) {
        List<String> errors = new ArrayList<>();

        Path parent = filePath.getParent();
        if (parent == null) {
            String error = "Invalid file path, no parent directory: " + filePath;
            errors.add(error);
            logger.warn(error);
            return new ValidationResult(false, errors);
        }

        if (!fileManager.isWritable(parent)) {
            String error = "Parent directory is not writable: " + parent;
            errors.add(error);
            logger.warn(error);
        }

        if (errors.isEmpty()) {
            logger.debug("File creation validation passed: {}", filePath);
            return new ValidationResult(true, List.of());
        }

        return new ValidationResult(false, errors);
    }

    /**
     * Represents the result of a validation operation.
     */
    public record ValidationResult(boolean valid, List<String> errors) {
        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return String.join(", ", errors);
        }
    }
}
