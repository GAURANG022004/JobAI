package com.jobaibackend.knowledge.controller;

import com.jobaibackend.api.ApiResponse;
import com.jobaibackend.api.ApiResponseBuilder;
import com.jobaibackend.constant.ApiConstants;
import com.jobaibackend.knowledge.dto.KnowledgeContentDto;
import com.jobaibackend.knowledge.dto.KnowledgeFileDto;
import com.jobaibackend.knowledge.dto.KnowledgeStatusDto;
import com.jobaibackend.knowledge.service.KnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for knowledge management operations.
 * Provides endpoints for reading, writing, updating, and listing knowledge files.
 */
@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/knowledge")
public class KnowledgeController {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeController.class);

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    /**
     * GET /api/v1/knowledge/profile
     * Returns profile.json content
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<KnowledgeContentDto>> getProfile() {
        logger.info("GET /api/v1/knowledge/profile");
        
        Optional<KnowledgeContentDto> content = knowledgeService.readKnowledgeFile("profile", "profile.json");
        
        if (content.isEmpty()) {
            logger.warn("Profile file not found or unreadable");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseBuilder.failure("Profile file not found or unreadable"));
        }

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Profile retrieved successfully", content.get())
        );
    }

    /**
     * POST /api/v1/knowledge/profile
     * Creates profile.json
     */
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> createProfile(@RequestBody String content) {
        logger.info("POST /api/v1/knowledge/profile");
        
        boolean success = knowledgeService.createKnowledgeFile("profile", "profile.json", content);
        
        if (!success) {
            logger.warn("Failed to create profile file");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseBuilder.failure("Failed to create profile file"));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseBuilder.success("Profile created successfully", null));
    }

    /**
     * PUT /api/v1/knowledge/profile
     * Updates profile.json
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@RequestBody String content) {
        logger.info("PUT /api/v1/knowledge/profile");
        
        boolean success = knowledgeService.updateKnowledgeFile("profile", "profile.json", content);
        
        if (!success) {
            logger.warn("Failed to update profile file");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseBuilder.failure("Failed to update profile file"));
        }

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Profile updated successfully", null)
        );
    }

    /**
     * GET /api/v1/knowledge/files
     * Returns all available knowledge files
     */
    @GetMapping("/files")
    public ResponseEntity<ApiResponse<List<KnowledgeFileDto>>> listFiles() {
        logger.info("GET /api/v1/knowledge/files");
        
        List<KnowledgeFileDto> files = knowledgeService.listKnowledgeFiles();
        
        return ResponseEntity.ok(
                ApiResponseBuilder.success("Files listed successfully", files)
        );
    }

    /**
     * GET /api/v1/knowledge/status
     * Returns storage status including existing folders, files, and health
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<KnowledgeStatusDto>> getStatus() {
        logger.info("GET /api/v1/knowledge/status");
        
        KnowledgeStatusDto status = knowledgeService.getKnowledgeStatus();
        
        return ResponseEntity.ok(
                ApiResponseBuilder.success("Status retrieved successfully", status)
        );
    }

    /**
     * GET /api/v1/knowledge/{folder}/{filename}
     * Generic endpoint to read any knowledge file
     */
    @GetMapping("/{folder}/{filename}")
    public ResponseEntity<ApiResponse<KnowledgeContentDto>> readFile(
            @PathVariable String folder,
            @PathVariable String filename) {
        logger.info("GET /api/v1/knowledge/{}/{}", folder, filename);
        
        Optional<KnowledgeContentDto> content = knowledgeService.readKnowledgeFile(folder, filename);
        
        if (content.isEmpty()) {
            logger.warn("File not found or unreadable: {}/{}", folder, filename);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseBuilder.failure("File not found or unreadable"));
        }

        return ResponseEntity.ok(
                ApiResponseBuilder.success("File retrieved successfully", content.get())
        );
    }

    /**
     * POST /api/v1/knowledge/{folder}/{filename}
     * Generic endpoint to create any knowledge file
     */
    @PostMapping("/{folder}/{filename}")
    public ResponseEntity<ApiResponse<Void>> createFile(
            @PathVariable String folder,
            @PathVariable String filename,
            @RequestBody String content) {
        logger.info("POST /api/v1/knowledge/{}/{}", folder, filename);
        
        boolean success = knowledgeService.createKnowledgeFile(folder, filename, content);
        
        if (!success) {
            logger.warn("Failed to create file: {}/{}", folder, filename);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseBuilder.failure("Failed to create file"));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseBuilder.success("File created successfully", null));
    }

    /**
     * PUT /api/v1/knowledge/{folder}/{filename}
     * Generic endpoint to update any knowledge file
     */
    @PutMapping("/{folder}/{filename}")
    public ResponseEntity<ApiResponse<Void>> updateFile(
            @PathVariable String folder,
            @PathVariable String filename,
            @RequestBody String content) {
        logger.info("PUT /api/v1/knowledge/{}/{}", folder, filename);
        
        boolean success = knowledgeService.updateKnowledgeFile(folder, filename, content);
        
        if (!success) {
            logger.warn("Failed to update file: {}/{}", folder, filename);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseBuilder.failure("Failed to update file"));
        }

        return ResponseEntity.ok(
                ApiResponseBuilder.success("File updated successfully", null)
        );
    }

    /**
     * DELETE /api/v1/knowledge/{folder}/{filename}
     * Generic endpoint to delete any knowledge file
     */
    @DeleteMapping("/{folder}/{filename}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @PathVariable String folder,
            @PathVariable String filename) {
        logger.info("DELETE /api/v1/knowledge/{}/{}", folder, filename);
        
        boolean success = knowledgeService.deleteKnowledgeFile(folder, filename);
        
        if (!success) {
            logger.warn("Failed to delete file: {}/{}", folder, filename);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseBuilder.failure("Failed to delete file"));
        }

        return ResponseEntity.ok(
                ApiResponseBuilder.success("File deleted successfully", null)
        );
    }

}
