package com.jobaibackend.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Local filesystem implementation of FileManager.
 */
@Service
public class LocalFileManager implements FileManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileManager.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<String> readFile(Path filePath) {
        try {
            if (!fileExists(filePath)) {
                logger.warn("File does not exist: {}", filePath);
                return Optional.empty();
            }
            if (!isReadable(filePath)) {
                logger.warn("File is not readable: {}", filePath);
                return Optional.empty();
            }
            String content = Files.readString(filePath);
            logger.debug("Read file successfully: {}", filePath);
            return Optional.of(content);
        } catch (IOException e) {
            logger.error("Failed to read file: {}", filePath, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean writeFile(Path filePath, String content) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, content);
            logger.info("Wrote file successfully: {}", filePath);
            return true;
        } catch (IOException e) {
            logger.error("Failed to write file: {}", filePath, e);
            return false;
        }
    }

    @Override
    public boolean updateFile(Path filePath, String content) {
        if (!fileExists(filePath)) {
            logger.warn("Cannot update non-existent file: {}", filePath);
            return false;
        }
        return writeFile(filePath, content);
    }

    @Override
    public boolean deleteFile(Path filePath) {
        try {
            if (!fileExists(filePath)) {
                logger.warn("Cannot delete non-existent file: {}", filePath);
                return false;
            }
            Files.delete(filePath);
            logger.info("Deleted file successfully: {}", filePath);
            return true;
        } catch (IOException e) {
            logger.error("Failed to delete file: {}", filePath, e);
            return false;
        }
    }

    @Override
    public boolean fileExists(Path filePath) {
        return Files.exists(filePath);
    }

    @Override
    public boolean isReadable(Path filePath) {
        return Files.isReadable(filePath);
    }

    @Override
    public boolean isWritable(Path filePath) {
        return Files.isWritable(filePath);
    }

    @Override
    public long getFileSize(Path filePath) {
        try {
            if (!fileExists(filePath)) {
                return -1;
            }
            return Files.size(filePath);
        } catch (IOException e) {
            logger.error("Failed to get file size: {}", filePath, e);
            return -1;
        }
    }

    @Override
    public boolean isValidJson(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        try {
            objectMapper.readTree(content);
            return true;
        } catch (IOException e) {
            logger.debug("Invalid JSON content detected", e);
            return false;
        }
    }
}
