package com.jobaibackend.knowledge.service;

import com.jobaibackend.config.ApplicationProperties;
import com.jobaibackend.knowledge.dto.KnowledgeContentDto;
import com.jobaibackend.knowledge.dto.KnowledgeStatusDto;
import com.jobaibackend.knowledge.validation.KnowledgeValidator;
import com.jobaibackend.storage.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for KnowledgeService.
 */
class KnowledgeServiceTest {

    @Mock
    private FileManager fileManager;

    @Mock
    private KnowledgeValidator validator;

    @Mock
    private ApplicationProperties properties;

    @TempDir
    Path tempDir;

    private KnowledgeService knowledgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(properties.storage()).thenReturn(new ApplicationProperties.Storage(
                tempDir.resolve("knowledge").toString(),
                tempDir.resolve("generated").toString(),
                tempDir.resolve("templates").toString(),
                tempDir.resolve("logs").toString(),
                tempDir.resolve("uploads").toString()
        ));
        knowledgeService = new KnowledgeService(fileManager, validator, properties);
    }

    @Test
    void readKnowledgeFile_Success() {
        // Arrange
        String validJson = "{\"name\":\"test\"}";
        when(validator.validateFileExists(any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(true, List.of()));
        when(fileManager.readFile(any(Path.class)))
                .thenReturn(Optional.of(validJson));
        when(fileManager.isValidJson(validJson)).thenReturn(true);

        // Act
        Optional<KnowledgeContentDto> result = knowledgeService.readKnowledgeFile("profile", "profile.json");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(validJson, result.get().getContent());
        assertTrue(result.get().isValidJson());
    }

    @Test
    void readKnowledgeFile_ValidationFailure() {
        // Arrange
        when(validator.validateFileExists(any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(false, List.of("File not found")));

        // Act
        Optional<KnowledgeContentDto> result = knowledgeService.readKnowledgeFile("profile", "profile.json");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void readKnowledgeFile_FileReadFailure() {
        // Arrange
        when(validator.validateFileExists(any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(true, List.of()));
        when(fileManager.readFile(any(Path.class)))
                .thenReturn(Optional.empty());

        // Act
        Optional<KnowledgeContentDto> result = knowledgeService.readKnowledgeFile("profile", "profile.json");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void createKnowledgeFile_Success() {
        // Arrange
        String validJson = "{\"name\":\"test\"}";
        when(fileManager.fileExists(any(Path.class))).thenReturn(false);
        when(validator.validateFileCreation(any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(true, List.of()));
        when(validator.validateContent(anyString(), any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(true, List.of()));
        when(fileManager.writeFile(any(Path.class), anyString())).thenReturn(true);

        // Act
        boolean result = knowledgeService.createKnowledgeFile("profile", "profile.json", validJson);

        // Assert
        assertTrue(result);
        verify(fileManager).writeFile(any(Path.class), eq(validJson));
    }

    @Test
    void createKnowledgeFile_FileAlreadyExists() {
        // Arrange
        when(fileManager.fileExists(any(Path.class))).thenReturn(true);

        // Act
        boolean result = knowledgeService.createKnowledgeFile("profile", "profile.json", "{\"name\":\"test\"}");

        // Assert
        assertFalse(result);
    }

    @Test
    void createKnowledgeFile_InvalidContent() {
        // Arrange
        String invalidJson = "not json";
        when(fileManager.fileExists(any(Path.class))).thenReturn(false);
        when(validator.validateFileCreation(any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(true, List.of()));
        when(validator.validateContent(anyString(), any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(false, List.of("Invalid JSON")));

        // Act
        boolean result = knowledgeService.createKnowledgeFile("profile", "profile.json", invalidJson);

        // Assert
        assertFalse(result);
    }

    @Test
    void updateKnowledgeFile_Success() {
        // Arrange
        String validJson = "{\"name\":\"updated\"}";
        when(validator.validateFileExists(any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(true, List.of()));
        when(validator.validateContent(anyString(), any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(true, List.of()));
        when(fileManager.updateFile(any(Path.class), anyString())).thenReturn(true);

        // Act
        boolean result = knowledgeService.updateKnowledgeFile("profile", "profile.json", validJson);

        // Assert
        assertTrue(result);
        verify(fileManager).updateFile(any(Path.class), eq(validJson));
    }

    @Test
    void updateKnowledgeFile_FileNotFound() {
        // Arrange
        when(validator.validateFileExists(any(Path.class)))
                .thenReturn(new KnowledgeValidator.ValidationResult(false, List.of("File not found")));

        // Act
        boolean result = knowledgeService.updateKnowledgeFile("profile", "profile.json", "{\"name\":\"test\"}");

        // Assert
        assertFalse(result);
    }

    @Test
    void deleteKnowledgeFile_Success() {
        // Arrange
        when(fileManager.deleteFile(any(Path.class))).thenReturn(true);

        // Act
        boolean result = knowledgeService.deleteKnowledgeFile("profile", "profile.json");

        // Assert
        assertTrue(result);
        verify(fileManager).deleteFile(any(Path.class));
    }

    @Test
    void deleteKnowledgeFile_Failure() {
        // Arrange
        when(fileManager.deleteFile(any(Path.class))).thenReturn(false);

        // Act
        boolean result = knowledgeService.deleteKnowledgeFile("profile", "profile.json");

        // Assert
        assertFalse(result);
    }

    @Test
    void fileExists_True() {
        // Arrange
        when(fileManager.fileExists(any(Path.class))).thenReturn(true);

        // Act
        boolean result = knowledgeService.fileExists("profile", "profile.json");

        // Assert
        assertTrue(result);
    }

    @Test
    void fileExists_False() {
        // Arrange
        when(fileManager.fileExists(any(Path.class))).thenReturn(false);

        // Act
        boolean result = knowledgeService.fileExists("profile", "profile.json");

        // Assert
        assertFalse(result);
    }

    @Test
    void getKnowledgeStatus() {
        // Arrange
        when(fileManager.isReadable(any(Path.class))).thenReturn(true);
        when(fileManager.isWritable(any(Path.class))).thenReturn(true);
        when(fileManager.fileExists(any(Path.class))).thenReturn(false);

        // Act
        KnowledgeStatusDto status = knowledgeService.getKnowledgeStatus();

        // Assert
        assertNotNull(status);
        assertNotNull(status.getKnowledgePath());
        assertNotNull(status.getExistingFolders());
        assertNotNull(status.getExistingFiles());
        assertNotNull(status.getMissingFiles());
    }
}
