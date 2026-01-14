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

package org.jscience.server.workflow;

import org.jscience.server.scheduling.TaskDependencyGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Workflow Engine for multi-step scientific pipelines.
 * 
 * Features:
 * - DAG-based workflow definition
 * - Parallel step execution
 * - State management
 * - Retry and error handling
 */
public class WorkflowEngine {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowEngine.class);

    /**
     * Workflow step definition.
     */
    public static class Step {
        private final String id;
        private final String name;
        private final Function<Map<String, Object>, Object> action;
        private int retries = 0;
        private long timeoutMs = 0;

        public Step(String id, String name, Function<Map<String, Object>, Object> action) {
            this.id = id;
            this.name = name;
            this.action = action;
        }

        public Step retries(int retries) {
            this.retries = retries;
            return this;
        }

        public Step timeout(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Workflow definition.
     */
    public static class Workflow {
        private final String id;
        private final String name;
        private final List<Step> steps = new ArrayList<>();
        private final List<String[]> dependencies = new ArrayList<>(); // [stepId, dependsOnId]

        public Workflow(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public Workflow addStep(Step step) {
            steps.add(step);
            return this;
        }

        public Workflow addDependency(String stepId, String dependsOnId) {
            dependencies.add(new String[] { stepId, dependsOnId });
            return this;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Step> getSteps() {
            return steps;
        }
    }

    /**
     * Workflow execution context.
     */
    public static class ExecutionContext {
        private final String executionId;
        private final Workflow workflow;
        private final Map<String, Object> inputs;
        private final Map<String, Object> outputs = new ConcurrentHashMap<>();
        private final Map<String, StepStatus> stepStatuses = new ConcurrentHashMap<>();
        private volatile WorkflowStatus status = WorkflowStatus.PENDING;
        private final long startTime;
        private long endTime;

        public ExecutionContext(Workflow workflow, Map<String, Object> inputs) {
            this.executionId = UUID.randomUUID().toString();
            this.workflow = workflow;
            this.inputs = new HashMap<>(inputs);
            this.startTime = System.currentTimeMillis();
        }

        public String getExecutionId() {
            return executionId;
        }

        public Map<String, Object> getInputs() {
            return Collections.unmodifiableMap(inputs);
        }

        public Map<String, Object> getOutputs() {
            return Collections.unmodifiableMap(outputs);
        }

        public WorkflowStatus getStatus() {
            return status;
        }

        public long getDurationMs() {
            return (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
        }
    }

    public enum WorkflowStatus {
        PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
    }

    public enum StepStatus {
        PENDING, RUNNING, COMPLETED, FAILED, SKIPPED
    }

    private final ExecutorService executor;
    private final Map<String, Workflow> workflows = new ConcurrentHashMap<>();
    private final Map<String, ExecutionContext> executions = new ConcurrentHashMap<>();

    public WorkflowEngine() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    public WorkflowEngine(int parallelism) {
        this.executor = Executors.newFixedThreadPool(parallelism, r -> {
            Thread t = new Thread(r, "workflow-executor");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Register a workflow definition.
     */
    public void registerWorkflow(Workflow workflow) {
        workflows.put(workflow.getId(), workflow);
        LOG.info("Ã°Å¸â€œâ€¹ Registered workflow: {} with {} steps", workflow.getName(), workflow.getSteps().size());
    }

    /**
     * Execute a workflow.
     */
    public CompletableFuture<ExecutionContext> execute(String workflowId, Map<String, Object> inputs) {
        Workflow workflow = workflows.get(workflowId);
        if (workflow == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Unknown workflow: " + workflowId));
        }

        ExecutionContext ctx = new ExecutionContext(workflow, inputs);
        executions.put(ctx.getExecutionId(), ctx);

        return CompletableFuture.supplyAsync(() -> {
            try {
                runWorkflow(ctx);
                return ctx;
            } catch (Exception e) {
                ctx.status = WorkflowStatus.FAILED;
                throw e;
            }
        }, executor);
    }

    private void runWorkflow(ExecutionContext ctx) {
        ctx.status = WorkflowStatus.RUNNING;
        Workflow workflow = ctx.workflow;

        LOG.info("Ã¢â€“Â¶Ã¯Â¸Â Starting workflow: {} ({})", workflow.getName(), ctx.getExecutionId());

        // Build dependency graph
        TaskDependencyGraph<Step> graph = new TaskDependencyGraph<>();
        for (Step step : workflow.getSteps()) {
            graph.addTask(step.getId(), step);
            ctx.stepStatuses.put(step.getId(), StepStatus.PENDING);
        }
        for (String[] dep : workflow.dependencies) {
            graph.addDependency(dep[0], dep[1]);
        }

        // Process steps in topological order
        while (!graph.isComplete()) {
            List<TaskDependencyGraph.TaskNode<Step>> ready = graph.getReadyTasks();

            if (ready.isEmpty() && !graph.isComplete()) {
                // Deadlock or all running
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ctx.status = WorkflowStatus.CANCELLED;
                    return;
                }
                continue;
            }

            // Execute ready steps in parallel
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (var node : ready) {
                graph.markStarted(node.getId());
                futures.add(CompletableFuture.runAsync(() -> executeStep(ctx, node.getTask(), graph), executor));
            }

            // Wait for batch to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        // Check for failures
        boolean anyFailed = ctx.stepStatuses.values().stream()
                .anyMatch(s -> s == StepStatus.FAILED);

        ctx.status = anyFailed ? WorkflowStatus.FAILED : WorkflowStatus.COMPLETED;
        ctx.endTime = System.currentTimeMillis();

        LOG.info("{} Workflow {} in {}ms",
                ctx.status == WorkflowStatus.COMPLETED ? "Ã¢Å“â€¦" : "Ã¢ÂÅ’",
                workflow.getName(), ctx.getDurationMs());
    }

    private void executeStep(ExecutionContext ctx, Step step, TaskDependencyGraph<Step> graph) {
        ctx.stepStatuses.put(step.getId(), StepStatus.RUNNING);
        LOG.debug("Ã¢â€“Â¶Ã¯Â¸Â Starting step: {}", step.getName());

        try {
            // Prepare inputs from workflow inputs and previous step outputs
            Map<String, Object> stepInputs = new HashMap<>(ctx.inputs);
            stepInputs.putAll(ctx.outputs);

            // Execute with retry
            Object result = executeWithRetry(step, stepInputs);

            ctx.outputs.put(step.getId(), result);
            ctx.stepStatuses.put(step.getId(), StepStatus.COMPLETED);
            graph.markCompleted(step.getId(), result);

            LOG.debug("Ã¢Å“â€¦ Completed step: {}", step.getName());
        } catch (Exception e) {
            ctx.stepStatuses.put(step.getId(), StepStatus.FAILED);
            graph.markFailed(step.getId(), e);
            LOG.error("Ã¢ÂÅ’ Failed step: {} - {}", step.getName(), e.getMessage());
        }
    }

    private Object executeWithRetry(Step step, Map<String, Object> inputs) throws Exception {
        int attempts = 0;
        Exception lastError = null;

        while (attempts <= step.retries) {
            try {
                if (step.timeoutMs > 0) {
                    return CompletableFuture.supplyAsync(() -> step.action.apply(inputs))
                            .get(step.timeoutMs, TimeUnit.MILLISECONDS);
                } else {
                    return step.action.apply(inputs);
                }
            } catch (Exception e) {
                lastError = e;
                attempts++;
                if (attempts <= step.retries) {
                    LOG.warn("⏱️ Retry {}/{} for step: {}", attempts, step.retries, step.getName());
                    Thread.sleep(1000L * attempts); // Backoff
                }
            }
        }

        // Throw the last error if present, otherwise create a generic exception
        if (lastError != null) {
            throw lastError;
        } else {
            throw new RuntimeException("Step failed after " + step.retries + " retries with unknown error");
        }
    }

    /**
     * Get execution status.
     */
    public ExecutionContext getExecution(String executionId) {
        return executions.get(executionId);
    }

    /**
     * Shutdown the engine.
     */
    public void shutdown() {
        executor.shutdown();
    }

    // --- DSL for building workflows ---

    public static Workflow workflow(String id, String name) {
        return new Workflow(id, name);
    }

    public static Step step(String id, String name, Function<Map<String, Object>, Object> action) {
        return new Step(id, name, action);
    }
}
