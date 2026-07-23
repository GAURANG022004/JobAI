package com.jobaibackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jobai")
public record ApplicationProperties(Storage storage) {

    public record Storage(
            String knowledgePath,
            String generatedPath,
            String templatesPath,
            String logsPath
    ) {}
}