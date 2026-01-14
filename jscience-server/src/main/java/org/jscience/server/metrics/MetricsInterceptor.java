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

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gRPC Server Interceptor that records metrics for all RPC calls.
 * 
 * Records:
 * - Call counts per method
 * - Error counts per method
 * - Call duration
 */
public class MetricsInterceptor implements ServerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsInterceptor.class);

    private final MetricsExporter metrics;

    public MetricsInterceptor(MetricsExporter metrics) {
        this.metrics = metrics;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();
        long startTime = System.nanoTime();

        metrics.recordGrpcCall(methodName);

        // Wrap the call to capture completion status
        ServerCall<ReqT, RespT> wrappedCall = new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void close(Status status, Metadata trailers) {
                long duration = System.nanoTime() - startTime;
                double durationSeconds = duration / 1_000_000_000.0;

                if (!status.isOk()) {
                    metrics.recordGrpcError(methodName);
                    LOG.debug("gRPC error for {}: {} ({}s)",
                            methodName, status.getCode(), String.format("%.3f", durationSeconds));
                } else {
                    LOG.debug("gRPC success for {} ({}s)",
                            methodName, String.format("%.3f", durationSeconds));
                }

                super.close(status, trailers);
            }
        };

        return next.startCall(wrappedCall, headers);
    }
}


