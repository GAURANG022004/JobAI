package com.jobaibackend.knowledge.service;

import com.jobaibackend.config.ApplicationProperties;
import com.jobaibackend.knowledge.dto.KnowledgeContentDto;
import com.jobaibackend.knowledge.dto.KnowledgeFileDto;
import com.jobaibackend.knowledge.dto.KnowledgeStatusDto;
import com.jobaibackend.knowledge.validation.KnowledgeValidator;
import com.jobaibackend.storage.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Service for managing knowledge files.
 * Uses the storage layer for all file operations.
 */
@Service
public class KnowledgeService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeService.class);

    private static final List<String> SUPPORTED_FOLDERS = List.of(
            "profile", "projects", "certificates", "memory", 
            "applications", "generated", "resumes"
    );

    private static final List<String> EXPECTED_FILES = List.of(
            "profile/profile.json"
    );

    private final FileManager fileManager;
    private final KnowledgeValidator validator;
    private final ApplicationProperties properties;

    public KnowledgeService(FileManager fileManager, KnowledgeValidator validator,
                           ApplicationProperties properties) {
        this.fileManager = fileManager;
        this.validator = validator;
        this.properties = properties;
    }

    /**
     * Reads a knowledge file by folder and filename.
     *
     * @param folder the folder name (e.g., "profile")
     * @param filename the filename (e.g., "profile.json")
     * @return knowledge content if successful
     */
    public Optional<KnowledgeContentDto> readKnowledgeFile(String folder, String filename) {
        Path filePath = buildFilePath(folder, filename);
        logger.info("Reading knowledge file: {}", filePath);

        KnowledgeValidator.ValidationResult validationResult = 
                validator.validateFileExists(filePath);
        
        if (!validationResult.isValid()) {
            logger.error("Validation failed for file read: {}", validationResult.getErrorMessage());
            return Optional.empty();
        }

        Optional<String> content = fileManager.readFile(filePath);
        if (content.isEmpty()) {
            logger.error("Failed to read file content: {}", filePath);
            return Optional.empty();
        }

        boolean validJson = fileManager.isValidJson(content.get());
        logger.info("Successfully read file: {}, valid JSON: {}", filePath, validJson);

        return Optional.of(new KnowledgeContentDto(content.get(), validJson));
    }

    /**
     * Creates a new knowledge file.
     *
     * @param folder the folder name
     * @param filename the filename
     * @param content the JSON content
     * @return true if creation successful
     */
    public boolean createKnowledgeFile(String folder, String filename, String content) {
        Path filePath = buildFilePath(folder, filename);
        logger.info("Creating knowledge file: {}", filePath);

        if (fileManager.fileExists(filePath)) {
            logger.warn("File already exists, use update instead: {}", filePath);
            return false;
        }

        KnowledgeValidator.ValidationResult validationResult = 
                validator.validateFileCreation(filePath);
        
        if (!validationResult.isValid()) {
            logger.error("Validation failed for file creation: {}", validationResult.getErrorMessage());
            return false;
        }

        KnowledgeValidator.ValidationResult contentValidation = 
                validator.validateContent(content, filePath);
        
        if (!contentValidation.isValid()) {
            logger.error("Content validation failed: {}", contentValidation.getErrorMessage());
            return false;
        }

        boolean success = fileManager.writeFile(filePath, content);
        if (success) {
            logger.info("Successfully created file: {}", filePath);
        } else {
            logger.error("Failed to create file: {}", filePath);
        }
        return success;
    }

    /**
     * Updates an existing knowledge file.
     *
     * @param folder the folder name
     * @param filename the filename
     * @param content the new JSON content
     * @return true if update successful
     */
    public boolean updateKnowledgeFile(String folder, String filename, String content) {
        Path filePath = buildFilePath(folder, filename);
        logger.info("Updating knowledge file: {}", filePath);

        KnowledgeValidator.ValidationResult validationResult = 
                validator.validateFileExists(filePath);
        
        if (!validationResult.isValid()) {
            logger.error("Validation failed for file update: {}", validationResult.getErrorMessage());
            return false;
        }

        KnowledgeValidator.ValidationResult contentValidation = 
                validator.validateContent(content, filePath);
        
        if (!contentValidation.isValid()) {
            logger.error("Content validation failed: {}", contentValidation.getErrorMessage());
            return false;
        }

        boolean success = fileManager.updateFile(filePath, content);
        if (success) {
            logger.info("Successfully updated file: {}", filePath);
        } else {
            logger.error("Failed to update file: {}", filePath);
        }
        return success;
    }

    /**
     * Deletes a knowledge file.
     *
     * @param folder the folder name
     * @param filename the filename
     * @return true if deletion successful
     */
    public boolean deleteKnowledgeFile(String folder, String filename) {
        Path filePath = buildFilePath(folder, filename);
        logger.info("Deleting knowledge file: {}", filePath);

        boolean success = fileManager.deleteFile(filePath);
        if (success) {
            logger.info("Successfully deleted file: {}", filePath);
        } else {
            logger.error("Failed to delete file: {}", filePath);
        }
        return success;
    }

    /**
     * Lists all available knowledge files.
     *
     * @return list of knowledge file metadata
     */
    public List<KnowledgeFileDto> listKnowledgeFiles() {
        logger.info("Listing all knowledge files");
        List<KnowledgeFileDto> files = new ArrayList<>();
        Path knowledgePath = Path.of(properties.storage().knowledgePath());

        for (String folder : SUPPORTED_FOLDERS) {
            Path folderPath = knowledgePath.resolve(folder);
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                try (Stream<Path> stream = Files.list(folderPath)) {
                    stream.filter(Files::isRegularFile)
                          .forEach(file -> files.add(buildFileDto(file, folder)));
                } catch (IOException e) {
                    logger.error("Failed to list files in folder: {}", folderPath, e);
                }
            }
        }

        logger.info("Found {} knowledge files", files.size());
        return files;
    }

    /**
     * Gets the status of the knowledge storage system.
     *
     * @return knowledge status information
     */
    public KnowledgeStatusDto getKnowledgeStatus() {
        logger.info("Getting knowledge storage status");
        Path knowledgePath = Path.of(properties.storage().knowledgePath());

        List<String> existingFolders = new ArrayList<>();
        List<KnowledgeFileDto> existingFiles = listKnowledgeFiles();
        List<String> missingFiles = new ArrayList<>();

        // Check folders
        for (String folder : SUPPORTED_FOLDERS) {
            Path folderPath = knowledgePath.resolve(folder);
            if (Files.exists(folderPath)) {
                existingFolders.add(folder);
            }
        }

        // Check expected files
        for (String expectedFile : EXPECTED_FILES) {
            Path filePath = knowledgePath.resolve(expectedFile);
            if (!fileManager.fileExists(filePath)) {
                missingFiles.add(expectedFile);
            }
        }

        boolean storageHealthy = missingFiles.isEmpty() && 
                                fileManager.isReadable(knowledgePath) && 
                                fileManager.isWritable(knowledgePath);

        logger.info("Knowledge status - healthy: {}, folders: {}, files: {}, missing: {}",
                   storageHealthy, existingFolders.size(), existingFiles.size(), missingFiles.size());

        return new KnowledgeStatusDto(
                existingFolders,
                existingFiles,
                missingFiles,
                storageHealthy,
                knowledgePath.toString()
        );
    }

    /**
     * Checks if a specific knowledge file exists.
     *
     * @param folder the folder name
     * @param filename the filename
     * @return true if file exists
     */
    public boolean fileExists(String folder, String filename) {
        Path filePath = buildFilePath(folder, filename);
        boolean exists = fileManager.fileExists(filePath);
        logger.debug("File exists check - {}: {}", filePath, exists);
        return exists;
    }

    private Path buildFilePath(String folder, String filename) {
        return Path.of(properties.storage().knowledgePath(), folder, filename);
    }

    private KnowledgeFileDto buildFileDto(Path file, String folder) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
            LocalDateTime lastModified = LocalDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            
            return new KnowledgeFileDto(
                    file.getFileName().toString(),
                    file.toString(),
                    folder,
                    attrs.size(),
                    lastModified,
                    true,
                    fileManager.isReadable(file),
                    fileManager.isWritable(file)
            );
        } catch (IOException e) {
            logger.error("Failed to read file attributes: {}", file, e);
            return new KnowledgeFileDto(
                    file.getFileName().toString(),
                    file.toString(),
                    folder,
                    -1,
                    null,
                    false,
                    false,
                    false
            );
        }
    }
}
