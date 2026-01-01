/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.server.scaling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Auto-Scaler for the JScience Distributed Grid.
 * Monitors queue depth and worker load to dynamically scale workers up/down.
 *
 * Scaling policies:
 * - Scale UP: Queue depth > threshold AND all workers busy
 * - Scale DOWN: Queue empty AND workers idle > timeout
 * - Cooldown: Prevent rapid scaling oscillations
 */
public class AutoScaler {

    private static final Logger LOG = LoggerFactory.getLogger(AutoScaler.class);

    // Scaling thresholds
    private int scaleUpQueueThreshold = 10; // Scale up when queue > this
    private int targetWorkerUtilization = 80; // Target 80% utilization
    private int minWorkers = 1;
    private int maxWorkers = 20;
    private int scaleUpStep = 2; // Add 2 workers at a time
    private int scaleDownStep = 1; // Remove 1 worker at a time
    private int cooldownSeconds = 60; // Wait between scaling actions
    private int idleTimeoutSeconds = 120; // Remove idle workers after 2 min

    // Current state
    private final AtomicInteger currentWorkerCount = new AtomicInteger(0);
    private final AtomicInteger queueDepth = new AtomicInteger(0);
    private final ConcurrentHashMap<String, WorkerMetrics> workerMetrics = new ConcurrentHashMap<>();
    private volatile long lastScaleTime = 0;
    private volatile boolean enabled = true;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ScalingCallback callback;

    /**
     * Callback interface for scaling actions.
     */
    public interface ScalingCallback {
        void onScaleUp(int targetCount);

        void onScaleDown(int targetCount);

        void onWorkerSpawn(String workerId);

        void onWorkerTerminate(String workerId);
    }

    /**
     * Worker metrics for scaling decisions.
     */
    public static class WorkerMetrics {
        public final String workerId;
        public volatile int tasksCompleted;
        public volatile long lastActiveTime;
        public volatile double cpuUsage;
        public volatile long memoryUsed;
        public volatile boolean busy;

        public WorkerMetrics(String workerId) {
            this.workerId = workerId;
            this.lastActiveTime = System.currentTimeMillis();
            this.busy = false;
        }
    }

    public AutoScaler(ScalingCallback callback) {
        this.callback = callback;
    }

    /**
     * Start the auto-scaler monitoring loop.
     */
    public void start() {
        LOG.info("Starting AutoScaler with thresholds: scaleUp={}, minWorkers={}, maxWorkers={}",
                scaleUpQueueThreshold, minWorkers, maxWorkers);

        scheduler.scheduleAtFixedRate(this::evaluateScaling, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * Stop the auto-scaler.
     */
    public void stop() {
        enabled = false;
        scheduler.shutdown();
        LOG.info("AutoScaler stopped");
    }

    /**
     * Main scaling evaluation loop.
     */
    private void evaluateScaling() {
        if (!enabled)
            return;

        try {
            int workers = currentWorkerCount.get();
            int queue = queueDepth.get();
            double avgUtilization = calculateAverageUtilization();
            int idleWorkers = countIdleWorkers();

            LOG.debug("AutoScaler check: workers={}, queue={}, util={}%, idle={}",
                    workers, queue, String.format("%.1f", avgUtilization), idleWorkers);

            // Check cooldown
            if (System.currentTimeMillis() - lastScaleTime < cooldownSeconds * 1000) {
                return;
            }

            // Scale UP decision
            if (shouldScaleUp(workers, queue, avgUtilization)) {
                int targetCount = Math.min(workers + scaleUpStep, maxWorkers);
                if (targetCount > workers) {
                    LOG.info("Ã°Å¸Å¡â‚¬ SCALE UP: {} -> {} workers (queue={}, util={}%)",
                            workers, targetCount, queue, String.format("%.1f", avgUtilization));
                    callback.onScaleUp(targetCount);
                    lastScaleTime = System.currentTimeMillis();
                }
            }
            // Scale DOWN decision
            else if (shouldScaleDown(workers, queue, idleWorkers)) {
                int targetCount = Math.max(workers - scaleDownStep, minWorkers);
                if (targetCount < workers) {
                    LOG.info("Ã°Å¸â€œâ€° SCALE DOWN: {} -> {} workers (queue={}, idle={})",
                            workers, targetCount, queue, idleWorkers);
                    callback.onScaleDown(targetCount);
                    lastScaleTime = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            LOG.error("AutoScaler evaluation error", e);
        }
    }

    private boolean shouldScaleUp(int workers, int queue, double utilization) {
        // Scale up if queue is backing up and workers are busy
        return workers < maxWorkers &&
                (queue > scaleUpQueueThreshold || utilization > targetWorkerUtilization);
    }

    private boolean shouldScaleDown(int workers, int queue, int idleWorkers) {
        // Scale down if queue is empty and workers are idle
        return workers > minWorkers &&
                queue == 0 &&
                idleWorkers > 0;
    }

    private double calculateAverageUtilization() {
        if (workerMetrics.isEmpty())
            return 0;
        int busy = 0;
        for (WorkerMetrics m : workerMetrics.values()) {
            if (m.busy)
                busy++;
        }
        return (busy * 100.0) / workerMetrics.size();
    }

    private int countIdleWorkers() {
        long now = System.currentTimeMillis();
        int idle = 0;
        for (WorkerMetrics m : workerMetrics.values()) {
            if (!m.busy && (now - m.lastActiveTime) > idleTimeoutSeconds * 1000) {
                idle++;
            }
        }
        return idle;
    }

    // --- Public API for updating metrics ---

    public void updateQueueDepth(int depth) {
        queueDepth.set(depth);
    }

    public void registerWorker(String workerId) {
        workerMetrics.put(workerId, new WorkerMetrics(workerId));
        currentWorkerCount.incrementAndGet();
        LOG.info("Worker registered: {} (total={})", workerId, currentWorkerCount.get());
    }

    public void unregisterWorker(String workerId) {
        workerMetrics.remove(workerId);
        currentWorkerCount.decrementAndGet();
        LOG.info("Worker unregistered: {} (total={})", workerId, currentWorkerCount.get());
    }

    public void updateWorkerMetrics(String workerId, boolean busy, double cpuUsage, long memoryUsed) {
        WorkerMetrics m = workerMetrics.get(workerId);
        if (m != null) {
            m.busy = busy;
            m.cpuUsage = cpuUsage;
            m.memoryUsed = memoryUsed;
            if (busy) {
                m.lastActiveTime = System.currentTimeMillis();
            }
        }
    }

    public void taskCompleted(String workerId) {
        WorkerMetrics m = workerMetrics.get(workerId);
        if (m != null) {
            m.tasksCompleted++;
            m.lastActiveTime = System.currentTimeMillis();
        }
    }

    // --- Configuration setters ---

    public AutoScaler setScaleUpQueueThreshold(int threshold) {
        this.scaleUpQueueThreshold = threshold;
        return this;
    }

    public AutoScaler setTargetWorkerUtilization(int utilization) {
        this.targetWorkerUtilization = utilization;
        return this;
    }

    public AutoScaler setMinWorkers(int min) {
        this.minWorkers = min;
        return this;
    }

    public AutoScaler setMaxWorkers(int max) {
        this.maxWorkers = max;
        return this;
    }

    public AutoScaler setScaleUpStep(int step) {
        this.scaleUpStep = step;
        return this;
    }

    public AutoScaler setScaleDownStep(int step) {
        this.scaleDownStep = step;
        return this;
    }

    public AutoScaler setCooldownSeconds(int seconds) {
        this.cooldownSeconds = seconds;
        return this;
    }

    public AutoScaler setIdleTimeoutSeconds(int seconds) {
        this.idleTimeoutSeconds = seconds;
        return this;
    }

    // --- Status getters ---

    public int getCurrentWorkerCount() {
        return currentWorkerCount.get();
    }

    public int getQueueDepth() {
        return queueDepth.get();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}


