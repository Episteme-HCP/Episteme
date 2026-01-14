/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.server.resilience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Task Checkpoint Manager - saves task state for recovery after failures.
 * 
 * Enables:
 * - Save intermediate task state
 * - Resume from last checkpoint on failure
 * - Automatic checkpoint cleanup
 */
public class CheckpointManager {

    private static final Logger LOG = LoggerFactory.getLogger(CheckpointManager.class);

    private final Path checkpointDir;
    private final ConcurrentHashMap<String, TaskCheckpoint> checkpoints = new ConcurrentHashMap<>();

    /**
     * Checkpoint data for a task.
     */
    public static class TaskCheckpoint implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String taskId;
        private final byte[] state;
        private final int progress;
        private final Instant timestamp;
        private final int iteration;

        public TaskCheckpoint(String taskId, byte[] state, int progress, int iteration) {
            this.taskId = taskId;
            this.state = state;
            this.progress = progress;
            this.timestamp = Instant.now();
            this.iteration = iteration;
        }

        public String getTaskId() {
            return taskId;
        }

        public byte[] getState() {
            return state;
        }

        public int getProgress() {
            return progress;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public int getIteration() {
            return iteration;
        }
    }

    /**
     * Create checkpoint manager with default directory.
     */
    public CheckpointManager() {
        this(Paths.get(System.getProperty("java.io.tmpdir"), "jscience-checkpoints"));
    }

    /**
     * Create checkpoint manager with custom directory.
     */
    public CheckpointManager(Path checkpointDir) {
        this.checkpointDir = checkpointDir;
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(checkpointDir);
            LOG.info("Ã°Å¸â€œÂ Checkpoint directory: {}", checkpointDir);
        } catch (IOException e) {
            LOG.error("Failed to create checkpoint directory", e);
        }
    }

    /**
     * Save a checkpoint for a task.
     */
    public void save(String taskId, byte[] state, int progress, int iteration) {
        TaskCheckpoint checkpoint = new TaskCheckpoint(taskId, state, progress, iteration);
        checkpoints.put(taskId, checkpoint);

        // Persist to disk
        persistCheckpoint(checkpoint);
        LOG.debug("Ã°Å¸â€™Â¾ Checkpoint saved: task={}, progress={}%, iteration={}",
                taskId, progress, iteration);
    }

    /**
     * Save a checkpoint with serializable state.
     */
    public void save(String taskId, Serializable state, int progress, int iteration) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(state);
            save(taskId, bos.toByteArray(), progress, iteration);
        } catch (IOException e) {
            LOG.error("Failed to serialize checkpoint state for task: {}", taskId, e);
        }
    }

    /**
     * Load the latest checkpoint for a task.
     */
    public TaskCheckpoint load(String taskId) {
        // First check memory
        TaskCheckpoint checkpoint = checkpoints.get(taskId);
        if (checkpoint != null) {
            return checkpoint;
        }

        // Try to load from disk
        checkpoint = loadCheckpointFromDisk(taskId);
        if (checkpoint != null) {
            checkpoints.put(taskId, checkpoint);
        }
        return checkpoint;
    }

    /**
     * Load checkpoint state as a specific type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T loadState(String taskId, Class<T> type) {
        TaskCheckpoint checkpoint = load(taskId);
        if (checkpoint == null) {
            return null;
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(checkpoint.getState());
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOG.error("Failed to deserialize checkpoint state for task: {}", taskId, e);
            return null;
        }
    }

    /**
     * Check if a checkpoint exists for a task.
     */
    public boolean hasCheckpoint(String taskId) {
        return checkpoints.containsKey(taskId) ||
                Files.exists(getCheckpointPath(taskId));
    }

    /**
     * Delete checkpoint for a task (called on success).
     */
    public void delete(String taskId) {
        checkpoints.remove(taskId);
        try {
            Files.deleteIfExists(getCheckpointPath(taskId));
            LOG.debug("Ã°Å¸â€”â€˜Ã¯Â¸Â Checkpoint deleted: {}", taskId);
        } catch (IOException e) {
            LOG.warn("Failed to delete checkpoint file for task: {}", taskId, e);
        }
    }

    /**
     * Delete all checkpoints older than specified duration.
     */
    public void cleanupOldCheckpoints(java.time.Duration maxAge) {
        Instant cutoff = Instant.now().minus(maxAge);

        checkpoints.entrySet().removeIf(entry -> {
            if (entry.getValue().getTimestamp().isBefore(cutoff)) {
                delete(entry.getKey());
                return true;
            }
            return false;
        });

        LOG.info("Ã°Å¸Â§Â¹ Checkpoint cleanup complete");
    }

    /**
     * Get count of active checkpoints.
     */
    public int getCheckpointCount() {
        return checkpoints.size();
    }

    // --- Private helpers ---

    private Path getCheckpointPath(String taskId) {
        return checkpointDir.resolve(taskId + ".checkpoint");
    }

    private void persistCheckpoint(TaskCheckpoint checkpoint) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(getCheckpointPath(checkpoint.getTaskId())))) {
            oos.writeObject(checkpoint);
        } catch (IOException e) {
            LOG.error("Failed to persist checkpoint for task: {}", checkpoint.getTaskId(), e);
        }
    }

    private TaskCheckpoint loadCheckpointFromDisk(String taskId) {
        Path path = getCheckpointPath(taskId);
        if (!Files.exists(path)) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            TaskCheckpoint checkpoint = (TaskCheckpoint) ois.readObject();
            LOG.debug("Ã°Å¸â€œÂ¥ Checkpoint loaded from disk: {}", taskId);
            return checkpoint;
        } catch (IOException | ClassNotFoundException e) {
            LOG.error("Failed to load checkpoint from disk for task: {}", taskId, e);
            return null;
        }
    }
}


