package com.jobaibackend.knowledge.validation;

import com.jobaibackend.storage.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for KnowledgeValidator.
 */
class KnowledgeValidatorTest {

    @Mock
    private FileManager fileManager;

    @TempDir
    Path tempDir;

    private KnowledgeValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new KnowledgeValidator(fileManager);
    }

    @Test
    void validateFileExists_Success() {
        // Arrange
        Path filePath = tempDir.resolve("test.json");
        when(fileManager.fileExists(filePath)).thenReturn(true);
        when(fileManager.isReadable(filePath)).thenReturn(true);
        when(fileManager.isWritable(filePath)).thenReturn(true);

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateFileExists(filePath);

        // Assert
        assertTrue(result.isValid());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void validateFileExists_NotFound() {
        // Arrange
        Path filePath = tempDir.resolve("nonexistent.json");
        when(fileManager.fileExists(filePath)).thenReturn(false);

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateFileExists(filePath);

        // Assert
        assertFalse(result.isValid());
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void validateFileExists_NotReadable() {
        // Arrange
        Path filePath = tempDir.resolve("test.json");
        when(fileManager.fileExists(filePath)).thenReturn(true);
        when(fileManager.isReadable(filePath)).thenReturn(false);
        when(fileManager.isWritable(filePath)).thenReturn(true);

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateFileExists(filePath);

        // Assert
        assertFalse(result.isValid());
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void validateContent_ValidJson() {
        // Arrange
        String validJson = "{\"name\":\"test\",\"value\":123}";
        Path filePath = tempDir.resolve("test.json");

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateContent(validJson, filePath);

        // Assert
        assertTrue(result.isValid());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void validateContent_InvalidJson() {
        // Arrange
        String invalidJson = "{not valid json";
        Path filePath = tempDir.resolve("test.json");
        when(fileManager.isValidJson(invalidJson)).thenReturn(false);

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateContent(invalidJson, filePath);

        // Assert
        assertFalse(result.isValid());
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void validateContent_EmptyContent() {
        // Arrange
        String emptyContent = "";
        Path filePath = tempDir.resolve("test.json");

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateContent(emptyContent, filePath);

        // Assert
        assertFalse(result.isValid());
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void validateContent_NullContent() {
        // Arrange
        Path filePath = tempDir.resolve("test.json");

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateContent(null, filePath);

        // Assert
        assertFalse(result.isValid());
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void validateFileCreation_Success() {
        // Arrange
        Path filePath = tempDir.resolve("folder").resolve("test.json");
        when(fileManager.isWritable(filePath.getParent())).thenReturn(true);

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateFileCreation(filePath);

        // Assert
        assertTrue(result.isValid());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void validateFileCreation_ParentNotWritable() {
        // Arrange
        Path filePath = tempDir.resolve("folder").resolve("test.json");
        when(fileManager.isWritable(filePath.getParent())).thenReturn(false);

        // Act
        KnowledgeValidator.ValidationResult result = validator.validateFileCreation(filePath);

        // Assert
        assertFalse(result.isValid());
        assertFalse(result.errors().isEmpty());
    }
}
