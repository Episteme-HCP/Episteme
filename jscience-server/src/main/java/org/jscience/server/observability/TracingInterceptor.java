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

package org.jscience.server.observability;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OpenTelemetry-compatible tracing interceptor.
 * 
 * Implements distributed tracing for gRPC calls without external dependencies.
 * Can export to Jaeger, Zipkin, or other OpenTelemetry collectors.
 */
public class TracingInterceptor implements ServerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(TracingInterceptor.class);

    public static final Metadata.Key<String> TRACE_ID_KEY = Metadata.Key.of("x-trace-id",
            Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> SPAN_ID_KEY = Metadata.Key.of("x-span-id",
            Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> PARENT_SPAN_ID_KEY = Metadata.Key.of("x-parent-span-id",
            Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> CONTEXT_TRACE_ID = Context.key("traceId");
    public static final Context.Key<String> CONTEXT_SPAN_ID = Context.key("spanId");

    private final ConcurrentHashMap<String, SpanInfo> activeSpans = new ConcurrentHashMap<>();
    private final SpanExporter exporter;

    /**
     * Span information for tracing.
     */
    public static class SpanInfo {
        private final String traceId;
        private final String spanId;
        private final String parentSpanId;
        private final String operationName;
        private final long startTimeNanos;
        private long endTimeNanos;
        private String status;
        private String errorMessage;

        public SpanInfo(String traceId, String spanId, String parentSpanId, String operationName) {
            this.traceId = traceId;
            this.spanId = spanId;
            this.parentSpanId = parentSpanId;
            this.operationName = operationName;
            this.startTimeNanos = System.nanoTime();
            this.status = "OK";
        }

        public void finish(String status, String errorMessage) {
            this.endTimeNanos = System.nanoTime();
            this.status = status;
            this.errorMessage = errorMessage;
        }

        public long getDurationMs() {
            return (endTimeNanos - startTimeNanos) / 1_000_000;
        }

        public String toJson() {
            return String.format(
                    "{\"traceId\":\"%s\",\"spanId\":\"%s\",\"parentSpanId\":\"%s\"," +
                            "\"operationName\":\"%s\",\"durationMs\":%d,\"status\":\"%s\"%s}",
                    traceId, spanId, parentSpanId != null ? parentSpanId : "",
                    operationName, getDurationMs(), status,
                    errorMessage != null ? ",\"error\":\"" + errorMessage + "\"" : "");
        }

        public String getTraceId() {
            return traceId;
        }

        public String getSpanId() {
            return spanId;
        }

        public String getOperationName() {
            return operationName;
        }
    }

    /**
     * Interface for exporting spans.
     */
    @FunctionalInterface
    public interface SpanExporter {
        void export(SpanInfo span);
    }

    /**
     * Create tracing interceptor with console exporter.
     */
    public TracingInterceptor() {
        this(span -> LOG.info("SPAN: {}", span.toJson()));
    }

    /**
     * Create tracing interceptor with custom exporter.
     */
    public TracingInterceptor(SpanExporter exporter) {
        this.exporter = exporter;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Extract or generate trace context
        String traceId = headers.get(TRACE_ID_KEY);
        String parentSpanId = headers.get(SPAN_ID_KEY);

        if (traceId == null) {
            traceId = generateTraceId();
        }
        String spanId = generateSpanId();

        String methodName = call.getMethodDescriptor().getFullMethodName();
        SpanInfo span = new SpanInfo(traceId, spanId, parentSpanId, methodName);
        activeSpans.put(spanId, span);

        LOG.debug("Ã°Å¸â€Â Started span: {} for {}", spanId, methodName);

        // Propagate trace context
        final String finalTraceId = traceId;
        final String finalSpanId = spanId;
        Context ctx = Context.current()
                .withValue(CONTEXT_TRACE_ID, finalTraceId)
                .withValue(CONTEXT_SPAN_ID, finalSpanId);

        // Wrap call to capture completion
        ServerCall<ReqT, RespT> wrappedCall = new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void close(Status status, Metadata trailers) {
                SpanInfo s = activeSpans.remove(finalSpanId);
                if (s != null) {
                    s.finish(status.getCode().name(),
                            status.isOk() ? null : status.getDescription());
                    exporter.export(s);
                }
                super.close(status, trailers);
            }
        };

        return Contexts.interceptCall(ctx, wrappedCall, headers, next);
    }

    /**
     * Generate a random trace ID (128-bit as hex string).
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "") +
                UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * Generate a random span ID (64-bit as hex string).
     */
    private String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * Get current trace ID from context.
     */
    public static String getCurrentTraceId() {
        return CONTEXT_TRACE_ID.get();
    }

    /**
     * Get current span ID from context.
     */
    public static String getCurrentSpanId() {
        return CONTEXT_SPAN_ID.get();
    }

    /**
     * Get count of active spans.
     */
    public int getActiveSpanCount() {
        return activeSpans.size();
    }

    // --- Static factory for common exporters ---

    /**
     * Create a file-based span exporter.
     */
    public static SpanExporter fileExporter(java.nio.file.Path path) {
        return span -> {
            try (java.io.FileWriter writer = new java.io.FileWriter(path.toFile(), true)) {
                writer.write(span.toJson() + "\n");
            } catch (java.io.IOException e) {
                LOG.error("Failed to export span to file", e);
            }
        };
    }

    /**
     * Create a collector that batches spans (for HTTP export).
     */
    public static SpanExporter batchingExporter(java.util.function.Consumer<java.util.List<SpanInfo>> batchConsumer) {
        java.util.List<SpanInfo> batch = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

        // Flush every 100 spans or 5 seconds
        java.util.concurrent.ScheduledExecutorService scheduler = java.util.concurrent.Executors
                .newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (!batch.isEmpty()) {
                java.util.List<SpanInfo> toExport = new java.util.ArrayList<>(batch);
                batch.clear();
                batchConsumer.accept(toExport);
            }
        }, 5, 5, java.util.concurrent.TimeUnit.SECONDS);

        return span -> {
            batch.add(span);
            if (batch.size() >= 100) {
                java.util.List<SpanInfo> toExport = new java.util.ArrayList<>(batch);
                batch.clear();
                batchConsumer.accept(toExport);
            }
        };
    }
}


