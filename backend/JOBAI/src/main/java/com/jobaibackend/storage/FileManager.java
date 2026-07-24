package com.jobaibackend.storage;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Defines file management operations for knowledge files.
 */
public interface FileManager {

    /**
     * Reads a file and returns its content as a string.
     *
     * @param filePath path to the file
     * @return file content if exists and readable
     */
    Optional<String> readFile(Path filePath);

    /**
     * Writes content to a file.
     *
     * @param filePath path to the file
     * @param content content to write
     * @return true if write successful
     */
    boolean writeFile(Path filePath, String content);

    /**
     * Updates an existing file with new content.
     *
     * @param filePath path to the file
     * @param content new content
     * @return true if update successful
     */
    boolean updateFile(Path filePath, String content);

    /**
     * Deletes a file.
     *
     * @param filePath path to the file
     * @return true if deletion successful
     */
    boolean deleteFile(Path filePath);

    /**
     * Checks if a file exists.
     *
     * @param filePath path to the file
     * @return true if file exists
     */
    boolean fileExists(Path filePath);

    /**
     * Checks if a file is readable.
     *
     * @param filePath path to the file
     * @return true if file is readable
     */
    boolean isReadable(Path filePath);

    /**
     * Checks if a file is writable.
     *
     * @param filePath path to the file
     * @return true if file is writable
     */
    boolean isWritable(Path filePath);

    /**
     * Gets the size of a file in bytes.
     *
     * @param filePath path to the file
     * @return file size in bytes, or -1 if file doesn't exist
     */
    long getFileSize(Path filePath);

    /**
     * Validates if content is valid JSON.
     *
     * @param content content to validate
     * @return true if valid JSON
     */
    boolean isValidJson(String content);
}
