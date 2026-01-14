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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Affinity Scheduler for resource-aware task placement.
 * 
 * Features:
 * - GPU/CPU affinity
 * - Memory requirements
 * - Data locality
 * - Worker labels/constraints
 */
public class AffinityScheduler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AffinityScheduler.class);

    /**
     * Worker capabilities and resources.
     */
    public static class WorkerProfile {
        private final String workerId;
        private final Map<String, Object> labels = new HashMap<>();
        private int availableCpus;
        private long availableMemoryMb;
        private int availableGpus;
        private Set<String> dataLocality = new HashSet<>();

        public WorkerProfile(String workerId) {
            this.workerId = workerId;
        }

        public WorkerProfile cpus(int cpus) {
            this.availableCpus = cpus;
            return this;
        }

        public WorkerProfile memoryMb(long mb) {
            this.availableMemoryMb = mb;
            return this;
        }

        public WorkerProfile gpus(int gpus) {
            this.availableGpus = gpus;
            return this;
        }

        public WorkerProfile label(String key, Object value) {
            this.labels.put(key, value);
            return this;
        }

        public WorkerProfile hasData(String... dataIds) {
            this.dataLocality.addAll(Arrays.asList(dataIds));
            return this;
        }

        public String getWorkerId() {
            return workerId;
        }

        public int getAvailableCpus() {
            return availableCpus;
        }

        public long getAvailableMemoryMb() {
            return availableMemoryMb;
        }

        public int getAvailableGpus() {
            return availableGpus;
        }

        public Map<String, Object> getLabels() {
            return labels;
        }

        public Set<String> getDataLocality() {
            return dataLocality;
        }
    }

    /**
     * Task requirements for scheduling.
     */
    public static class TaskRequirements {
        private int requiredCpus = 1;
        private long requiredMemoryMb = 512;
        private int requiredGpus = 0;
        private Map<String, Object> requiredLabels = new HashMap<>();
        private Set<String> preferredDataLocality = new HashSet<>();
        private boolean gpuRequired = false;
        private String preferredWorkerId = null;

        public TaskRequirements cpus(int cpus) {
            this.requiredCpus = cpus;
            return this;
        }

        public TaskRequirements memoryMb(long mb) {
            this.requiredMemoryMb = mb;
            return this;
        }

        public TaskRequirements gpus(int gpus) {
            this.requiredGpus = gpus;
            this.gpuRequired = gpus > 0;
            return this;
        }

        public TaskRequirements requireLabel(String key, Object value) {
            this.requiredLabels.put(key, value);
            return this;
        }

        public TaskRequirements preferDataLocality(String... dataIds) {
            this.preferredDataLocality.addAll(Arrays.asList(dataIds));
            return this;
        }

        public TaskRequirements preferWorker(String workerId) {
            this.preferredWorkerId = workerId;
            return this;
        }

        public int getRequiredCpus() {
            return requiredCpus;
        }

        public long getRequiredMemoryMb() {
            return requiredMemoryMb;
        }

        public int getRequiredGpus() {
            return requiredGpus;
        }

        public boolean isGpuRequired() {
            return gpuRequired;
        }
    }

    /**
     * Represents a scheduling decision.
     */
    public static class SchedulingDecision {
        private final String workerId;
        private final double score;
        private final String reason;

        public SchedulingDecision(String workerId, double score, String reason) {
            this.workerId = workerId;
            this.score = score;
            this.reason = reason;
        }

        public String getWorkerId() {
            return workerId;
        }

        public double getScore() {
            return score;
        }

        public String getReason() {
            return reason;
        }
    }

    private final Map<String, WorkerProfile> workers = new ConcurrentHashMap<>();
    private final Map<T, TaskRequirements> taskRequirements = new ConcurrentHashMap<>();

    /**
     * Register a worker with its profile.
     */
    public void registerWorker(WorkerProfile profile) {
        workers.put(profile.getWorkerId(), profile);
        LOG.info("Ã°Å¸â€œÂ Registered worker: {} ({}CPUs, {}MB, {}GPUs)",
                profile.getWorkerId(), profile.getAvailableCpus(),
                profile.getAvailableMemoryMb(), profile.getAvailableGpus());
    }

    /**
     * Unregister a worker.
     */
    public void unregisterWorker(String workerId) {
        workers.remove(workerId);
    }

    /**
     * Update worker resources (after task completion).
     */
    public void updateWorkerResources(String workerId, int cpusDelta, long memoryDelta, int gpusDelta) {
        WorkerProfile profile = workers.get(workerId);
        if (profile != null) {
            profile.availableCpus += cpusDelta;
            profile.availableMemoryMb += memoryDelta;
            profile.availableGpus += gpusDelta;
        }
    }

    /**
     * Register task requirements.
     */
    public void registerTask(T task, TaskRequirements requirements) {
        taskRequirements.put(task, requirements);
    }

    /**
     * Schedule a task to the best available worker.
     */
    public Optional<SchedulingDecision> schedule(T task) {
        TaskRequirements reqs = taskRequirements.getOrDefault(task, new TaskRequirements());

        List<SchedulingDecision> candidates = new ArrayList<>();

        for (WorkerProfile worker : workers.values()) {
            double score = calculateScore(worker, reqs);
            if (score > 0) {
                candidates.add(new SchedulingDecision(worker.getWorkerId(), score, ""));
            }
        }

        if (candidates.isEmpty()) {
            LOG.warn("Ã¢Å¡Â Ã¯Â¸Â No eligible workers for task");
            return Optional.empty();
        }

        // Sort by score descending
        candidates.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        SchedulingDecision best = candidates.get(0);
        LOG.debug("Ã°Å¸â€œÂ Scheduled task to worker {} (score: {:.2f})",
                best.getWorkerId(), best.getScore());

        return Optional.of(best);
    }

    /**
     * Calculate scheduling score for a worker-task pair.
     */
    private double calculateScore(WorkerProfile worker, TaskRequirements reqs) {
        double score = 100.0;

        // Check hard constraints
        if (worker.getAvailableCpus() < reqs.requiredCpus)
            return 0;
        if (worker.getAvailableMemoryMb() < reqs.requiredMemoryMb)
            return 0;
        if (worker.getAvailableGpus() < reqs.requiredGpus)
            return 0;

        // Check required labels
        for (Map.Entry<String, Object> entry : reqs.requiredLabels.entrySet()) {
            if (!Objects.equals(worker.labels.get(entry.getKey()), entry.getValue())) {
                return 0;
            }
        }

        // Soft scoring factors

        // Prefer least loaded (more resources = higher score)
        score += worker.getAvailableCpus() * 5;
        score += worker.getAvailableMemoryMb() / 1000.0;
        score += worker.getAvailableGpus() * 20;

        // Prefer data locality
        if (!reqs.preferredDataLocality.isEmpty()) {
            long localData = reqs.preferredDataLocality.stream()
                    .filter(worker.dataLocality::contains)
                    .count();
            score += localData * 30; // Big bonus for local data
        }

        // Prefer specific worker if requested
        if (reqs.preferredWorkerId != null &&
                reqs.preferredWorkerId.equals(worker.getWorkerId())) {
            score += 50;
        }

        return score;
    }

    /**
     * Get all workers matching a predicate.
     */
    public List<WorkerProfile> findWorkers(Predicate<WorkerProfile> predicate) {
        return workers.values().stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Get workers with GPU capability.
     */
    public List<WorkerProfile> getGpuWorkers() {
        return findWorkers(w -> w.getAvailableGpus() > 0);
    }

    /**
     * Get total cluster resources.
     */
    public Map<String, Long> getClusterResources() {
        long totalCpus = workers.values().stream().mapToLong(WorkerProfile::getAvailableCpus).sum();
        long totalMemory = workers.values().stream().mapToLong(WorkerProfile::getAvailableMemoryMb).sum();
        long totalGpus = workers.values().stream().mapToLong(WorkerProfile::getAvailableGpus).sum();

        return Map.of(
                "cpus", totalCpus,
                "memoryMb", totalMemory,
                "gpus", totalGpus,
                "workers", (long) workers.size());
    }
}
