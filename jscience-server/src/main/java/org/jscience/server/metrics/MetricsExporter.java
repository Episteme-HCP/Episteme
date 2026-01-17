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

package org.jscience.server.metrics;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Metrics exporter for JScience Grid - exports Prometheus-compatible metrics.
 * 
 * Exposes metrics at /metrics endpoint for scraping.
 * 
 * Metrics include:
 * - jscience_tasks_total (counter) - Total tasks submitted
 * - jscience_tasks_completed (counter) - Tasks completed successfully
 * - jscience_tasks_failed (counter) - Tasks that failed
 * - jscience_tasks_pending (gauge) - Tasks currently pending
 * - jscience_workers_active (gauge) - Active worker count
 * - jscience_task_duration_seconds (histogram) - Task execution time
 * - jscience_queue_depth (gauge) - Current queue depth
 
 * <p>
 * <b>Reference:</b><br>
 * Fréchet, M. (1906). Sur quelques points du calcul fonctionnel. <i>Rendiconti del Circolo Matematico di Palermo</i>.
 * </p>
 *
 */
public class MetricsExporter {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsExporter.class);

    // Counters
    private final LongAdder tasksTotal = new LongAdder();
    private final LongAdder tasksCompleted = new LongAdder();
    private final LongAdder tasksFailed = new LongAdder();
    private final LongAdder grpcCallsTotal = new LongAdder();
    private final LongAdder grpcErrorsTotal = new LongAdder();
    private final LongAdder bytesProcessed = new LongAdder();

    // Gauges
    private final AtomicLong tasksPending = new AtomicLong(0);
    private final AtomicLong workersActive = new AtomicLong(0);
    private final AtomicLong queueDepth = new AtomicLong(0);

    // Histogram buckets for task duration
    private final ConcurrentHashMap<String, LongAdder> durationBuckets = new ConcurrentHashMap<>();
    private static final double[] DURATION_BUCKETS = { 0.01, 0.05, 0.1, 0.25, 0.5, 1.0, 2.5, 5.0, 10.0 };

    // Per-method metrics
    private final ConcurrentHashMap<String, LongAdder> methodCallCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LongAdder> methodErrorCounts = new ConcurrentHashMap<>();

    private HttpServer server;
    private final int port;

    public MetricsExporter(int port) {
        this.port = port;
        initializeBuckets();
    }

    private void initializeBuckets() {
        for (double bucket : DURATION_BUCKETS) {
            durationBuckets.put(String.valueOf(bucket), new LongAdder());
        }
        durationBuckets.put("+Inf", new LongAdder());
    }

    /**
     * Start the metrics HTTP server.
     */
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/metrics", exchange -> {
            String response = generateMetrics();
            exchange.getResponseHeaders().set("Content-Type", "text/plain; version=0.0.4");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.createContext("/health", exchange -> {
            String response = "OK";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.setExecutor(Executors.newFixedThreadPool(2));
        server.start();
        LOG.info("Ã°Å¸â€œÅ  Metrics server started on port {}", port);
    }

    /**
     * Stop the metrics server.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
            LOG.info("Metrics server stopped");
        }
    }

    /**
     * Generate Prometheus-format metrics output.
     */
    private String generateMetrics() {
        StringBuilder sb = new StringBuilder();

        // Task counters
        appendMetric(sb, "jscience_tasks_total", "counter",
                "Total number of tasks submitted", tasksTotal.sum());
        appendMetric(sb, "jscience_tasks_completed", "counter",
                "Total number of tasks completed successfully", tasksCompleted.sum());
        appendMetric(sb, "jscience_tasks_failed", "counter",
                "Total number of tasks that failed", tasksFailed.sum());

        // Gauges
        appendMetric(sb, "jscience_tasks_pending", "gauge",
                "Number of tasks currently pending", tasksPending.get());
        appendMetric(sb, "jscience_workers_active", "gauge",
                "Number of active workers", workersActive.get());
        appendMetric(sb, "jscience_queue_depth", "gauge",
                "Current task queue depth", queueDepth.get());

        // gRPC metrics
        appendMetric(sb, "jscience_grpc_calls_total", "counter",
                "Total gRPC calls", grpcCallsTotal.sum());
        appendMetric(sb, "jscience_grpc_errors_total", "counter",
                "Total gRPC errors", grpcErrorsTotal.sum());
        appendMetric(sb, "jscience_bytes_processed_total", "counter",
                "Total bytes processed", bytesProcessed.sum());

        // Duration histogram
        appendHistogram(sb, "jscience_task_duration_seconds",
                "Task execution time in seconds");

        // Per-method call counts
        for (var entry : methodCallCounts.entrySet()) {
            sb.append("jscience_grpc_method_calls_total{method=\"")
                    .append(entry.getKey())
                    .append("\"} ")
                    .append(entry.getValue().sum())
                    .append("\n");
        }

        return sb.toString();
    }

    private void appendMetric(StringBuilder sb, String name, String type,
            String help, long value) {
        sb.append("# HELP ").append(name).append(" ").append(help).append("\n");
        sb.append("# TYPE ").append(name).append(" ").append(type).append("\n");
        sb.append(name).append(" ").append(value).append("\n");
    }

    private void appendHistogram(StringBuilder sb, String name, String help) {
        sb.append("# HELP ").append(name).append(" ").append(help).append("\n");
        sb.append("# TYPE ").append(name).append(" histogram\n");

        long cumulative = 0;
        for (double bucket : DURATION_BUCKETS) {
            LongAdder adder = durationBuckets.get(String.valueOf(bucket));
            cumulative += adder.sum();
            sb.append(name).append("_bucket{le=\"").append(bucket).append("\"} ")
                    .append(cumulative).append("\n");
        }
        cumulative += durationBuckets.get("+Inf").sum();
        sb.append(name).append("_bucket{le=\"+Inf\"} ").append(cumulative).append("\n");
        sb.append(name).append("_count ").append(cumulative).append("\n");
    }

    // --- Metric recording methods ---

    public void recordTaskSubmitted() {
        tasksTotal.increment();
        tasksPending.incrementAndGet();
    }

    public void recordTaskCompleted(double durationSeconds) {
        tasksCompleted.increment();
        tasksPending.decrementAndGet();
        recordDuration(durationSeconds);
    }

    public void recordTaskFailed() {
        tasksFailed.increment();
        tasksPending.decrementAndGet();
    }

    public void recordGrpcCall(String method) {
        grpcCallsTotal.increment();
        methodCallCounts.computeIfAbsent(method, k -> new LongAdder()).increment();
    }

    public void recordGrpcError(String method) {
        grpcErrorsTotal.increment();
        methodErrorCounts.computeIfAbsent(method, k -> new LongAdder()).increment();
    }

    public void recordBytesProcessed(long bytes) {
        bytesProcessed.add(bytes);
    }

    public void setWorkersActive(int count) {
        workersActive.set(count);
    }

    public void setQueueDepth(int depth) {
        queueDepth.set(depth);
    }

    private void recordDuration(double seconds) {
        for (double bucket : DURATION_BUCKETS) {
            if (seconds <= bucket) {
                durationBuckets.get(String.valueOf(bucket)).increment();
                return;
            }
        }
        durationBuckets.get("+Inf").increment();
    }

    // --- Getters for current values ---

    public long getTotalTasks() {
        return tasksTotal.sum();
    }

    public long getCompletedTasks() {
        return tasksCompleted.sum();
    }

    public long getFailedTasks() {
        return tasksFailed.sum();
    }

    public long getPendingTasks() {
        return tasksPending.get();
    }

    public long getActiveWorkers() {
        return workersActive.get();
    }
}


