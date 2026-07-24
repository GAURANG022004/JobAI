package com.jobaibackend.knowledge.dto;

/**
 * Generic DTO for knowledge file content.
 * Works with any JSON content.
 */
public class KnowledgeContentDto {

    private String content;
    private boolean validJson;

    public KnowledgeContentDto() {
    }

    public KnowledgeContentDto(String content, boolean validJson) {
        this.content = content;
        this.validJson = validJson;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isValidJson() {
        return validJson;
    }

    public void setValidJson(boolean validJson) {
        this.validJson = validJson;
    }
}
