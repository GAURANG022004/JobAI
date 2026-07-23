package com.jobaibackend.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jobaibackend.config.ApplicationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Local filesystem implementation of DirectoryManager.
 */
@Service
public class LocalDirectoryManager implements DirectoryManager {

    private static final Logger logger =
            LoggerFactory.getLogger(LocalDirectoryManager.class);

    private final ApplicationProperties properties;

    public LocalDirectoryManager(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void createDirectory(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
            logger.info("Created directory: {}", directory);
        }
    }

    @Override
    public boolean exists(Path directory) {
        return Files.exists(directory);
    }

    @Override
    public boolean isReadable(Path directory) {
        return Files.isReadable(directory);
    }

    @Override
    public boolean isWritable(Path directory) {
        return Files.isWritable(directory);
    }

    @Override
    public void initializeDirectories() throws IOException {
        for (Path directory : getDirectories()) {
            createDirectory(directory);
        }
    }

    @Override
    public List<Path> getDirectories() {
        return List.of(
                Path.of(properties.storage().knowledgePath()),
                Path.of(properties.storage().generatedPath()),
                Path.of(properties.storage().templatesPath()),
                Path.of(properties.storage().uploadsPath()),
                Path.of(properties.storage().logsPath())
        );
    }
}