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

package org.jscience.server.context;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jscience.distributed.DistributedContext;
import org.jscience.server.proto.ComputeServiceGrpc;
import org.jscience.server.proto.TaskRequest;
import org.jscience.server.proto.ServerStatus;
import org.jscience.server.proto.Empty;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * DistributedContext implementation that delegates to a remote gRPC server.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GrpcDistributedContext implements DistributedContext {

    private final ManagedChannel channel;
    private final ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;

    public GrpcDistributedContext(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // For demo/dev (no encryption)
                .build();
        this.blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task) {
        // Serialize Task
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(task);
            oos.flush();
            byte[] bytes = bos.toByteArray();

            TaskRequest.newBuilder()
                    .setSerializedTask(ByteString.copyFrom(bytes))
                    .build();

            // Submit
            // Note: The blockingStub returns a Response saying "Queued".
            // It does NOT return the result of the calculation.
            // In a full implementation, we would need to poll / stream for the result.
            // For this version, we will return a Dummy Future that throws NotSupported
            // OR ideally, we wait (but then it's blocking).

            // FIXME: The gRPC definition separates submission from result retrieval.
            // The DistributedContext.submit() expects a Future that eventually yields the
            // result.
            // To make this work, we'd need a local Future that uses a separate gRPC call
            // (StreamResults)
            // to complete itself.

            throw new UnsupportedOperationException(
                    "Asynchronous gRPC result retrieval not fully implemented yet. Use LocalDistributedContext for now.");

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize or submit task", e);
        }
    }

    @Override
    public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
        return tasks.stream().map(this::submit).collect(Collectors.toList());
    }

    @Override
    public int getParallelism() {
        // Query server for status
        try {
            ServerStatus status = blockingStub.getStatus(Empty.newBuilder().build());
            return status.getActiveWorkers();
        } catch (Exception e) {
            return 1; // Fallback
        }
    }

    @Override
    public void shutdown() {
        channel.shutdown();
    }
}
