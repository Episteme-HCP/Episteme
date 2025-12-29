/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 */
public class GrpcDistributedContext implements DistributedContext {

    private final ManagedChannel channel;
    private final ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private final ComputeServiceGrpc.ComputeServiceFutureStub futureStub;

    public GrpcDistributedContext(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // For demo/dev (no encryption)
                .build();
        this.blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
        this.futureStub = ComputeServiceGrpc.newFutureStub(channel);
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

            TaskRequest request = TaskRequest.newBuilder()
                    .setSerializedTask(ByteString.copyFrom(bytes))
                    .build();

            // Submit
            // Note: The blockingStub returns a Response saying "Queued".
            // It does NOT return the result of the calculation.
            // In a full implementation, we would need to poll / stream for the result.
            // For this version, we will return a Dummy Future that throws NotSupported 
            // OR ideally, we wait (but then it's blocking).
            
            // FIXME: The gRPC definition separates submission from result retrieval.
            // The DistributedContext.submit() expects a Future that eventually yields the result.
            // To make this work, we'd need a local Future that uses a separate gRPC call (StreamResults)
            // to complete itself.
            
            throw new UnsupportedOperationException("Asynchronous gRPC result retrieval not fully implemented yet. Use LocalDistributedContext for now.");

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
