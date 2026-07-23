package com.jobaibackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobaibackend.api.ApiResponse;
import com.jobaibackend.constant.ApiConstants;
import com.jobaibackend.api.ApiResponseBuilder;
import com.jobaibackend.constant.ApplicationConstants;

import java.util.Map;

/**
 * Health check endpoint for the JobAI backend.
 */
@RestController
public class HealthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    @GetMapping(ApiConstants.HEALTH_ENDPOINT)
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {

        LOGGER.info("Health endpoint accessed.");

        Map<String, String> response = Map.of(
                "application", ApplicationConstants.APPLICATION_NAME,
                "status", ApplicationConstants.STATUS_UP,
                "version", ApplicationConstants.APPLICATION_VERSION
        );

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        ApplicationConstants.BACKEND_RUNNING_MESSAGE,
                        response
                )
        );
    }
}