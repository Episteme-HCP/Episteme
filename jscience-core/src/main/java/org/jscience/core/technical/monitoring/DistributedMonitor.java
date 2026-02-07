package org.jscience.core.technical.monitoring;

import io.javalin.Javalin;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;

import java.util.concurrent.TimeUnit;

/**
 * Master Monitoring Control for distributed benchmarks.
 * Standardizes metrics collection via Micrometer and Prometheus.
 */
public class DistributedMonitor {

    private static final DistributedMonitor INSTANCE = new DistributedMonitor();
    
    private final PrometheusMeterRegistry registry;
    private Javalin app;
    private boolean started = false;

    private DistributedMonitor() {
        this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        
        // Add common JVM/System metrics
        try {
            new JvmMemoryMetrics().bindTo(registry);
            new JvmThreadMetrics().bindTo(registry);
            new ProcessorMetrics().bindTo(registry);
        } catch (Exception e) {
            System.err.println("[MONITOR] Failed to bind JVM metrics: " + e.getMessage());
        }
        
        registry.config().commonTags("application", "JScience");
    }

    public static DistributedMonitor getInstance() {
        return INSTANCE;
    }

    public PrometheusMeterRegistry getRegistry() {
        return registry;
    }

    /**
     * Starts the metrics server on port 7070.
     */
    public synchronized void startServer() {
        if (started) return;
        
        try {
            app = Javalin.create(config -> {
                config.showJavalinBanner = false;
            }).start(7070);

            app.get("/metrics", ctx -> ctx.result(registry.scrape()));
            
            started = true;
            System.out.println("[MONITOR] Metrics server started at http://localhost:7070/metrics");
        } catch (Exception e) {
            System.err.println("[MONITOR] Failed to start metrics server: " + e.getMessage());
        }
    }

    public synchronized void stopServer() {
        if (app != null) {
            app.stop();
            started = false;
        }
    }

    // --- Metric Helpers ---

    public void recordExecution(String benchmarkId, String domain, long durationNs) {
        Timer.builder("benchmark.execution.time")
             .tag("id", benchmarkId)
             .tag("domain", domain)
             .register(registry)
             .record(durationNs, TimeUnit.NANOSECONDS);
             
        Counter.builder("benchmark.execution.count")
                .tag("id", benchmarkId)
                .tag("domain", domain)
                .register(registry)
                .increment();
    }

    public void recordDistributedTask(String taskId, String node, long latencyNs) {
        Timer.builder("distributed.task.latency")
             .tag("taskId", taskId)
             .tag("node", node)
             .register(registry)
             .record(latencyNs, TimeUnit.NANOSECONDS);
    }
}
