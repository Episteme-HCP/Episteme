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

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Centralized metrics configuration using Micrometer with Prometheus registry.
 * 
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Fréchet, M. (1906). Sur quelques points du calcul fonctionnel. <i>Rendiconti del Circolo Matematico di Palermo</i>.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MetricsRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsRegistry.class);

    private static volatile MetricsRegistry instance;
    private final PrometheusMeterRegistry registry;

    // API Call Counters
    private final Counter worldBankApiCalls;
    private final Counter worldBankApiErrors;
    private final Counter oidcValidations;
    private final Counter oidcValidationErrors;
    private final Counter mlflowApiCalls;
    private final Counter mlflowApiErrors;

    // Cache Counters
    private final Counter cacheHits;
    private final Counter cacheMisses;

    // Task Counters
    private final Counter tasksSubmitted;
    private final Counter tasksCompleted;
    private final Counter tasksFailed;

    // Timers
    private final Timer worldBankApiLatency;
    private final Timer oidcValidationLatency;
    private final Timer taskExecutionTime;

    @SuppressWarnings("resource")
    private MetricsRegistry() {
        this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        // Register JVM metrics
        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new UptimeMetrics().bindTo(registry);

        // API Call Counters
        worldBankApiCalls = Counter.builder("jscience_api_worldbank_calls")
                .description("Total World Bank API calls")
                .register(registry);
        worldBankApiErrors = Counter.builder("jscience_api_worldbank_errors")
                .description("World Bank API errors")
                .register(registry);

        oidcValidations = Counter.builder("jscience_auth_oidc_validations")
                .description("Total OIDC token validations")
                .register(registry);
        oidcValidationErrors = Counter.builder("jscience_auth_oidc_errors")
                .description("OIDC validation errors")
                .register(registry);

        mlflowApiCalls = Counter.builder("jscience_api_mlflow_calls")
                .description("Total MLflow API calls")
                .register(registry);
        mlflowApiErrors = Counter.builder("jscience_api_mlflow_errors")
                .description("MLflow API errors")
                .register(registry);

        // Cache Counters
        cacheHits = Counter.builder("jscience_cache_hits")
                .description("Cache hits")
                .register(registry);
        cacheMisses = Counter.builder("jscience_cache_misses")
                .description("Cache misses")
                .register(registry);

        // Task Counters
        tasksSubmitted = Counter.builder("jscience_tasks_submitted")
                .description("Tasks submitted")
                .register(registry);
        tasksCompleted = Counter.builder("jscience_tasks_completed")
                .description("Tasks completed successfully")
                .register(registry);
        tasksFailed = Counter.builder("jscience_tasks_failed")
                .description("Tasks failed")
                .register(registry);

        // Timers
        worldBankApiLatency = Timer.builder("jscience_api_worldbank_latency")
                .description("World Bank API latency")
                .register(registry);
        oidcValidationLatency = Timer.builder("jscience_auth_oidc_latency")
                .description("OIDC validation latency")
                .register(registry);
        taskExecutionTime = Timer.builder("jscience_tasks_execution_time")
                .description("Task execution time")
                .register(registry);

        LOG.info("Metrics registry initialized with Prometheus");
    }

    public static MetricsRegistry getInstance() {
        if (instance == null) {
            synchronized (MetricsRegistry.class) {
                if (instance == null) {
                    instance = new MetricsRegistry();
                }
            }
        }
        return instance;
    }

    public MeterRegistry getRegistry() {
        return registry;
    }

    /** Returns Prometheus-formatted metrics for scraping. */
    public String scrape() {
        return registry.scrape();
    }

    // API Metrics
    public void recordWorldBankCall() {
        worldBankApiCalls.increment();
    }

    public void recordWorldBankError() {
        worldBankApiErrors.increment();
    }

    public void recordWorldBankLatency(long durationMs) {
        worldBankApiLatency.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public void recordOidcValidation() {
        oidcValidations.increment();
    }

    public void recordOidcError() {
        oidcValidationErrors.increment();
    }

    public void recordOidcLatency(long durationMs) {
        oidcValidationLatency.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public void recordMlflowCall() {
        mlflowApiCalls.increment();
    }

    public void recordMlflowError() {
        mlflowApiErrors.increment();
    }

    // Cache Metrics
    public void recordCacheHit() {
        cacheHits.increment();
    }

    public void recordCacheMiss() {
        cacheMisses.increment();
    }

    // Task Metrics
    public void recordTaskSubmitted() {
        tasksSubmitted.increment();
    }

    public void recordTaskCompleted() {
        tasksCompleted.increment();
    }

    public void recordTaskFailed() {
        tasksFailed.increment();
    }

    public void recordTaskExecutionTime(long durationMs) {
        taskExecutionTime.record(durationMs, TimeUnit.MILLISECONDS);
    }
}
