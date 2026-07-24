package com.jobaibackend.knowledge.dto;

import java.time.LocalDateTime;

/**
 * Generic DTO representing a knowledge file with metadata.
 */
public class KnowledgeFileDto {

    private String name;
    private String path;
    private String folder;
    private long size;
    private LocalDateTime lastModified;
    private boolean exists;
    private boolean readable;
    private boolean writable;

    public KnowledgeFileDto() {
    }

    public KnowledgeFileDto(String name, String path, String folder, long size, 
                           LocalDateTime lastModified, boolean exists, 
                           boolean readable, boolean writable) {
        this.name = name;
        this.path = path;
        this.folder = folder;
        this.size = size;
        this.lastModified = lastModified;
        this.exists = exists;
        this.readable = readable;
        this.writable = writable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }
}
