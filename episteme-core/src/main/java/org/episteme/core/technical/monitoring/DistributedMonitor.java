package org.episteme.core.technical.monitoring;

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

    private static volatile DistributedMonitor INSTANCE;
    
    private PrometheusMeterRegistry registry;
    private Javalin app;
    private boolean started = false;
    private boolean available = false;

    private DistributedMonitor() {
        try {
            this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
            
            // Add common JVM/System metrics
            new JvmMemoryMetrics().bindTo(registry);
            new JvmThreadMetrics().bindTo(registry);
            new ProcessorMetrics().bindTo(registry);
            
            registry.config().commonTags("application", "Episteme");
            available = true;
        } catch (Throwable t) {
            // Prometheus/Micrometer not on classpath — degrade gracefully
            available = false;
        }
    }

    public static DistributedMonitor getInstance() {
        if (INSTANCE == null) {
            synchronized (DistributedMonitor.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = new DistributedMonitor();
                    } catch (Throwable t) {
                        // Completely unavailable — create a no-op instance
                        INSTANCE = new DistributedMonitor();
                    }
                }
            }
        }
        return INSTANCE;
    }

    public PrometheusMeterRegistry getRegistry() {
        return registry;
    }

    public boolean isAvailable() {
        return available;
    }

    /**
     * Starts the metrics server on port 7070.
     */
    public synchronized void startServer() {
        if (started || !available) return;
        
        // Silence Jetty noise on some platforms (e.g. SO_REUSEPORT issue on Windows)
        System.setProperty("org.slf4j.simpleLogger.log.org.eclipse.jetty", "warn");
        
        try {
            app = Javalin.create(config -> {
                config.showJavalinBanner = false;
            }).start(7070);

            app.get("/metrics", ctx -> ctx.result(registry.scrape()));
            
            started = true;
            System.out.println("[MONITOR] Metrics server started at http://localhost:7070/metrics");
        } catch (Throwable t) {
            // Javalin or Jetty not available
        }
    }

    public synchronized void stopServer() {
        if (app != null) {
            try { app.stop(); } catch (Throwable t) { /* ignore */ }
            started = false;
        }
    }

    // --- Metric Helpers ---

    public void recordExecution(String benchmarkId, String domain, long durationNs) {
        if (!available) return;
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
        if (!available) return;
        Timer.builder("distributed.task.latency")
             .tag("taskId", taskId)
             .tag("node", node)
             .register(registry)
             .record(latencyNs, TimeUnit.NANOSECONDS);
    }
}
