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
import org.jscience.server.proto.*;

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
        return submit(task, DistributedContext.Priority.NORMAL);
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task, DistributedContext.Priority priority) {
        // Map Priority
        org.jscience.server.proto.Priority protoPriority;
        switch (priority) {
            case HIGH:
                protoPriority = org.jscience.server.proto.Priority.HIGH;
                break;
            case CRITICAL:
                protoPriority = org.jscience.server.proto.Priority.CRITICAL;
                break;
            case LOW:
                protoPriority = org.jscience.server.proto.Priority.LOW;
                break;
            default:
                protoPriority = org.jscience.server.proto.Priority.NORMAL;
        }

        // Serialize Task
        final ByteString serializedTask;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(task);
            oos.flush();
            serializedTask = ByteString.copyFrom(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize task", e);
        }

        // Submit to Server
        final String taskId = java.util.UUID.randomUUID().toString();
        try {
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId(taskId)
                    .setSerializedTask(serializedTask)
                    .setPriority(protoPriority)
                    .build();

            // Submit is blocking/synchronous in this simple stub, but returns quickly
            // (QUEUED)
            blockingStub.submitTask(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit task to server", e);
        }

        // Return a Future that retrieves the result via gRPC
        return new GrpcFuture<>(taskId);
    }

    private class GrpcFuture<T> implements Future<T> {
        private final String taskId;
        private boolean cancelled = false;
        private boolean done = false;

        public GrpcFuture(String taskId) {
            this.taskId = taskId;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            // Cancellation not yet supported by gRPC protocol implementation
            this.cancelled = true;
            return true;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            try {
                return get(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                throw new ExecutionException(e);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            // Poll or Stream for result
            // For now, we make a blocking call to streamResults which waits on the server
            // side
            // or returns immediately if done.
            // Ideally we would treat this as a stream and wait for a COMPLETED status.

            try {
                org.jscience.server.proto.TaskIdentifier request = org.jscience.server.proto.TaskIdentifier.newBuilder()
                        .setTaskId(taskId)
                        .build();

                // This call might block if the server implementation blocks (which it does now)
                // But typically gRPC streaming returns an Iterator.
                java.util.Iterator<org.jscience.server.proto.TaskResult> responses = blockingStub
                        .streamResults(request);

                if (responses.hasNext()) {
                    org.jscience.server.proto.TaskResult resultProto = responses.next();

                    if (resultProto.getStatus() == org.jscience.server.proto.Status.FAILED) {
                        throw new ExecutionException(new RuntimeException(resultProto.getErrorMessage()));
                    }

                    if (!resultProto.getSerializedData().isEmpty()) {
                        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(
                                resultProto.getSerializedData().toByteArray());
                        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
                        T result = (T) ois.readObject();
                        this.done = true;
                        return result;
                    }
                }

                throw new ExecutionException(new RuntimeException("Server closed stream without result"));

            } catch (Exception e) {
                throw new ExecutionException(e);
            }
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
