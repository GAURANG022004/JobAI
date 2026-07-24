package com.jobaibackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the application's storage directories.
 *
 * <p>
 * All storage paths are externalized in {@code application.properties}
 * to avoid hardcoded file system locations.
 * </p>
 *
 * Example:
 *
 * <pre>
 * jobai.storage.knowledge-path=./storage/knowledge
 * jobai.storage.generated-path=./storage/generated
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "jobai.storage")
public class StorageProperties {

    /**
     * Path to the knowledge directory.
     */
    private String knowledgePath;

    /**
     * Path to generated files.
     */
    private String generatedPath;

    /**
     * Path to reusable templates.
     */
    private String templatesPath;

    /**
     * Path to uploaded files.
     */
    private String uploadsPath;

    /**
     * Path to application logs.
     */
    private String logsPath;

    public String getKnowledgePath() {
        return knowledgePath;
    }

    public void setKnowledgePath(String knowledgePath) {
        this.knowledgePath = knowledgePath;
    }

    public String getGeneratedPath() {
        return generatedPath;
    }

    public void setGeneratedPath(String generatedPath) {
        this.generatedPath = generatedPath;
    }

    public String getTemplatesPath() {
        return templatesPath;
    }

    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public String getUploadsPath() {
        return uploadsPath;
    }

    public void setUploadsPath(String uploadsPath) {
        this.uploadsPath = uploadsPath;
    }

    public String getLogsPath() {
        return logsPath;
    }

    public void setLogsPath(String logsPath) {
        this.logsPath = logsPath;
    }
}