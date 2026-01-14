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

package org.jscience.distributed;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Registry for distributed task implementations.
 * 
 * <p>
 * The TaskRegistry provides dynamic task discovery and instantiation,
 * eliminating
 * the need for hardcoded task handlers in WorkerNode. Tasks can be registered
 * programmatically or discovered via ServiceLoader.
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * // Register a task
 * TaskRegistry.register("MANDELBROT", MandelbrotTask.class);
 * 
 * // Get a task instance
 * DistributedTask&lt;?, ?&gt; task = TaskRegistry.getInstance().get("MANDELBROT")
 *         .orElseThrow(() -&gt; new IllegalArgumentException("Unknown task"));
 * </pre>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class TaskRegistry {

    private static final Logger LOGGER = Logger.getLogger(TaskRegistry.class.getName());
    private static final TaskRegistry INSTANCE = new TaskRegistry();

    private final Map<String, Class<? extends DistributedTask<?, ?>>> taskClasses = new ConcurrentHashMap<>();
    private final Map<String, TaskProvider<?, ?>> taskProviders = new ConcurrentHashMap<>();

    private TaskRegistry() {
        // Discover tasks via ServiceLoader
        discoverTasks();
    }

    /**
     * Returns the singleton TaskRegistry instance.
     *
     * @return the TaskRegistry instance
     */
    public static TaskRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a task class with the given type identifier.
     *
     * @param taskType  the task type identifier (e.g., "MANDELBROT")
     * @param taskClass the task implementation class
     */
    public static void register(String taskType, Class<? extends DistributedTask<?, ?>> taskClass) {
        INSTANCE.taskClasses.put(taskType.toUpperCase(), taskClass);
        LOGGER.info("Registered task: " + taskType);
    }

    /**
     * Registers a task provider for the given type identifier.
     * <p>
     * Providers allow switching between Primitive and Real implementations.
     * </p>
     *
     * @param taskType the task type identifier
     * @param provider the task provider
     */
    public static void registerProvider(String taskType, TaskProvider<?, ?> provider) {
        INSTANCE.taskProviders.put(taskType.toUpperCase(), provider);
        LOGGER.info("Registered task provider: " + taskType);
    }

    /**
     * Gets a task instance for the given type.
     *
     * @param taskType the task type identifier
     * @return Optional containing the task instance, or empty if not found
     */
    public Optional<DistributedTask<?, ?>> get(String taskType) {
        String key = taskType.toUpperCase();

        // Check providers first
        TaskProvider<?, ?> provider = taskProviders.get(key);
        if (provider != null) {
            return Optional.of(provider.createTask());
        }

        // Fall back to class registry
        Class<? extends DistributedTask<?, ?>> clazz = taskClasses.get(key);
        if (clazz != null) {
            try {
                return Optional.of(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                LOGGER.warning("Failed to instantiate task " + taskType + ": " + e.getMessage());
            }
        }

        return Optional.empty();
    }

    /**
     * Gets a task instance with specific precision mode.
     *
     * @param taskType the task type identifier
     * @param mode     the precision mode (PRIMITIVE or REAL)
     * @return Optional containing the task instance
     */
    public Optional<DistributedTask<?, ?>> get(String taskType, PrecisionMode mode) {
        String key = taskType.toUpperCase();
        TaskProvider<?, ?> provider = taskProviders.get(key);

        if (provider != null) {
            return Optional.of(provider.createTask(mode));
        }

        // Fall back to default (no mode-specific variant)
        return get(taskType);
    }

    /**
     * Checks if a task type is registered.
     *
     * @param taskType the task type identifier
     * @return true if the task is registered
     */
    public boolean isRegistered(String taskType) {
        String key = taskType.toUpperCase();
        return taskClasses.containsKey(key) || taskProviders.containsKey(key);
    }

    /**
     * Returns all registered task types.
     *
     * @return iterable of task type identifiers
     */
    public Iterable<String> getRegisteredTypes() {
        return taskClasses.keySet();
    }

    @SuppressWarnings("unchecked")
    private void discoverTasks() {
        // ServiceLoader-based discovery for Tasks
        ServiceLoader<DistributedTask<?, ?>> taskLoader = (ServiceLoader<DistributedTask<?, ?>>) (Object) ServiceLoader
                .load(DistributedTask.class);
        for (DistributedTask<?, ?> task : taskLoader) {
            taskClasses.put(task.getTaskType().toUpperCase(),
                    (Class<? extends DistributedTask<?, ?>>) task.getClass());
            LOGGER.info("Discovered task via ServiceLoader: " + task.getTaskType());
        }

        // ServiceLoader-based discovery for Providers
        ServiceLoader<TaskProvider<?, ?>> providerLoader = (ServiceLoader<TaskProvider<?, ?>>) (Object) ServiceLoader
                .load(TaskProvider.class);
        for (TaskProvider<?, ?> provider : providerLoader) {
            taskProviders.put(provider.getTaskType().toUpperCase(), provider);
            LOGGER.info("Discovered provider via ServiceLoader: " + provider.getTaskType() + " ("
                    + provider.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Precision mode for task execution.
     */
    public enum PrecisionMode {
        /** Use primitive double - fastest, no GPU */
        PRIMITIVE,
        /** Use Real API - configurable precision, GPU-enabled */
        REAL
    }
}
