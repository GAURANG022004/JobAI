package com.jobaibackend.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Defines directory management operations.
 */
public interface DirectoryManager {

    /**
     * Creates the directory if it does not already exist.
     *
     * @param directory directory path
     * @throws IOException if creation fails
     */
    void createDirectory(Path directory) throws IOException;

    /**
     * Checks whether a directory exists.
     *
     * @param directory directory path
     * @return true if the directory exists
     */
    boolean exists(Path directory);

    /**
     * Checks whether a directory is readable.
     *
     * @param directory directory path
     * @return true if readable
     */
    boolean isReadable(Path directory);

    /**
     * Checks whether a directory is writable.
     *
     * @param directory directory path
     * @return true if writable
     */
    boolean isWritable(Path directory);

    /**
     * Initializes all required application directories.
     *
     * @throws IOException if initialization fails
     */
    void initializeDirectories() throws IOException;

    /**
     * Returns all configured application directories.
     *
     * @return list of directory paths
     */
    List<Path> getDirectories();
}