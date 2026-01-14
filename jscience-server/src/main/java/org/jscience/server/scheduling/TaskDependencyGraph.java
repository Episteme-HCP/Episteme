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

package org.jscience.server.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Task Dependency DAG (Directed Acyclic Graph) for workflow scheduling.
 * 
 * Features:
 * - Define task dependencies
 * - Topological ordering
 * - Parallel execution of independent tasks
 * - Completion tracking
 */
public class TaskDependencyGraph<T> {

    private static final Logger LOG = LoggerFactory.getLogger(TaskDependencyGraph.class);

    /**
     * Node in the dependency graph.
     */
    public static class TaskNode<T> {
        private final String id;
        private final T task;
        private final Set<String> dependencies = new HashSet<>();
        private final Set<String> dependents = new HashSet<>();
        private volatile boolean completed = false;
        private volatile boolean failed = false;
        private Object result;

        public TaskNode(String id, T task) {
            this.id = id;
            this.task = task;
        }

        public String getId() {
            return id;
        }

        public T getTask() {
            return task;
        }

        public Set<String> getDependencies() {
            return Collections.unmodifiableSet(dependencies);
        }

        public Set<String> getDependents() {
            return Collections.unmodifiableSet(dependents);
        }

        public boolean isCompleted() {
            return completed;
        }

        public boolean isFailed() {
            return failed;
        }

        public Object getResult() {
            return result;
        }
    }

    private final Map<String, TaskNode<T>> nodes = new ConcurrentHashMap<>();
    private final Set<String> readyTasks = ConcurrentHashMap.newKeySet();
    private final Set<String> runningTasks = ConcurrentHashMap.newKeySet();
    private final Set<String> completedTasks = ConcurrentHashMap.newKeySet();
    private Consumer<TaskNode<T>> onTaskReady;
    private Consumer<TaskNode<T>> onTaskComplete;
    private Consumer<TaskNode<T>> onTaskFailed;

    /**
     * Add a task to the graph.
     */
    public TaskDependencyGraph<T> addTask(String id, T task) {
        nodes.put(id, new TaskNode<>(id, task));
        readyTasks.add(id); // Initially ready until dependencies are added
        return this;
    }

    /**
     * Add a dependency: taskId depends on dependsOnId.
     */
    public TaskDependencyGraph<T> addDependency(String taskId, String dependsOnId) {
        TaskNode<T> task = nodes.get(taskId);
        TaskNode<T> dependency = nodes.get(dependsOnId);

        if (task == null || dependency == null) {
            throw new IllegalArgumentException("Task not found: " +
                    (task == null ? taskId : dependsOnId));
        }

        task.dependencies.add(dependsOnId);
        dependency.dependents.add(taskId);

        // No longer ready if it has incomplete dependencies
        readyTasks.remove(taskId);

        return this;
    }

    /**
     * Validate the graph has no cycles.
     */
    public boolean validate() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String nodeId : nodes.keySet()) {
            if (hasCycle(nodeId, visited, recursionStack)) {
                LOG.error("Ã¢ÂÅ’ Cycle detected in task graph at node: {}", nodeId);
                return false;
            }
        }

        LOG.info("Ã¢Å“â€¦ Task graph validated: {} nodes, no cycles", nodes.size());
        return true;
    }

    private boolean hasCycle(String nodeId, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(nodeId)) {
            return true;
        }
        if (visited.contains(nodeId)) {
            return false;
        }

        visited.add(nodeId);
        recursionStack.add(nodeId);

        TaskNode<T> node = nodes.get(nodeId);
        for (String depId : node.dependencies) {
            if (hasCycle(depId, visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(nodeId);
        return false;
    }

    /**
     * Get topologically sorted list of tasks.
     */
    public List<String> getTopologicalOrder() {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String nodeId : nodes.keySet()) {
            if (!visited.contains(nodeId)) {
                topologicalSort(nodeId, visited, stack);
            }
        }

        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    private void topologicalSort(String nodeId, Set<String> visited, Deque<String> stack) {
        visited.add(nodeId);
        TaskNode<T> node = nodes.get(nodeId);

        for (String depId : node.dependents) {
            if (!visited.contains(depId)) {
                topologicalSort(depId, visited, stack);
            }
        }

        stack.push(nodeId);
    }

    /**
     * Get tasks that are ready to execute (all dependencies completed).
     */
    public List<TaskNode<T>> getReadyTasks() {
        List<TaskNode<T>> ready = new ArrayList<>();

        for (String nodeId : nodes.keySet()) {
            if (runningTasks.contains(nodeId) || completedTasks.contains(nodeId)) {
                continue;
            }

            TaskNode<T> node = nodes.get(nodeId);
            boolean allDepsComplete = node.dependencies.stream()
                    .allMatch(completedTasks::contains);

            if (allDepsComplete) {
                ready.add(node);
            }
        }

        return ready;
    }

    /**
     * Mark a task as started.
     */
    public void markStarted(String taskId) {
        readyTasks.remove(taskId);
        runningTasks.add(taskId);
        LOG.debug("Ã¢â€“Â¶Ã¯Â¸Â Task started: {}", taskId);
    }

    /**
     * Mark a task as completed.
     */
    public void markCompleted(String taskId, Object result) {
        TaskNode<T> node = nodes.get(taskId);
        if (node == null)
            return;

        node.completed = true;
        node.result = result;
        runningTasks.remove(taskId);
        completedTasks.add(taskId);

        LOG.debug("Ã¢Å“â€¦ Task completed: {}", taskId);

        if (onTaskComplete != null) {
            onTaskComplete.accept(node);
        }

        // Check if any dependent tasks are now ready
        for (String depId : node.dependents) {
            TaskNode<T> dependent = nodes.get(depId);
            boolean allDepsComplete = dependent.dependencies.stream()
                    .allMatch(completedTasks::contains);

            if (allDepsComplete) {
                readyTasks.add(depId);
                LOG.debug("Ã°Å¸Å¸Â¢ Task now ready: {}", depId);
                if (onTaskReady != null) {
                    onTaskReady.accept(dependent);
                }
            }
        }
    }

    /**
     * Mark a task as failed.
     */
    public void markFailed(String taskId, Exception error) {
        TaskNode<T> node = nodes.get(taskId);
        if (node == null)
            return;

        node.failed = true;
        runningTasks.remove(taskId);
        completedTasks.add(taskId); // Count as "complete" for dependency purposes

        LOG.error("Ã¢ÂÅ’ Task failed: {} - {}", taskId, error.getMessage());

        if (onTaskFailed != null) {
            onTaskFailed.accept(node);
        }
    }

    /**
     * Check if all tasks are completed.
     */
    public boolean isComplete() {
        return completedTasks.size() == nodes.size();
    }

    /**
     * Get overall progress percentage.
     */
    public int getProgress() {
        if (nodes.isEmpty())
            return 100;
        return (int) ((completedTasks.size() * 100.0) / nodes.size());
    }

    // --- Callbacks ---

    public TaskDependencyGraph<T> onTaskReady(Consumer<TaskNode<T>> callback) {
        this.onTaskReady = callback;
        return this;
    }

    public TaskDependencyGraph<T> onTaskComplete(Consumer<TaskNode<T>> callback) {
        this.onTaskComplete = callback;
        return this;
    }

    public TaskDependencyGraph<T> onTaskFailed(Consumer<TaskNode<T>> callback) {
        this.onTaskFailed = callback;
        return this;
    }

    // --- Getters ---

    public int size() {
        return nodes.size();
    }

    public int getCompletedCount() {
        return completedTasks.size();
    }

    public int getRunningCount() {
        return runningTasks.size();
    }

    public TaskNode<T> getNode(String id) {
        return nodes.get(id);
    }
}


