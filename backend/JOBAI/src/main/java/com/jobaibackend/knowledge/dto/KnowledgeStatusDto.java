package com.jobaibackend.knowledge.dto;

import java.util.List;

/**
 * DTO representing the overall status of the knowledge storage system.
 */
public class KnowledgeStatusDto {

    private List<String> existingFolders;
    private List<KnowledgeFileDto> existingFiles;
    private List<String> missingFiles;
    private boolean storageHealthy;
    private String knowledgePath;

    public KnowledgeStatusDto() {
    }

    public KnowledgeStatusDto(List<String> existingFolders, List<KnowledgeFileDto> existingFiles,
                             List<String> missingFiles, boolean storageHealthy, String knowledgePath) {
        this.existingFolders = existingFolders;
        this.existingFiles = existingFiles;
        this.missingFiles = missingFiles;
        this.storageHealthy = storageHealthy;
        this.knowledgePath = knowledgePath;
    }

    public List<String> getExistingFolders() {
        return existingFolders;
    }

    public void setExistingFolders(List<String> existingFolders) {
        this.existingFolders = existingFolders;
    }

    public List<KnowledgeFileDto> getExistingFiles() {
        return existingFiles;
    }

    public void setExistingFiles(List<KnowledgeFileDto> existingFiles) {
        this.existingFiles = existingFiles;
    }

    public List<String> getMissingFiles() {
        return missingFiles;
    }

    public void setMissingFiles(List<String> missingFiles) {
        this.missingFiles = missingFiles;
    }

    public boolean isStorageHealthy() {
        return storageHealthy;
    }

    public void setStorageHealthy(boolean storageHealthy) {
        this.storageHealthy = storageHealthy;
    }

    public String getKnowledgePath() {
        return knowledgePath;
    }

    public void setKnowledgePath(String knowledgePath) {
        this.knowledgePath = knowledgePath;
    }
}
