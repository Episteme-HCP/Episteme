/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.client.client.distributed;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.episteme.server.server.proto.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import static org.mockito.AdditionalAnswers.delegatesTo;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GrpcDistributedContext.
 */
public class GrpcDistributedContextTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private GrpcDistributedContext context;
    private final ComputeServiceGrpc.ComputeServiceImplBase serviceImpl =
            mock(ComputeServiceGrpc.ComputeServiceImplBase.class, delegatesTo(new ComputeServiceGrpc.ComputeServiceImplBase() {
                @Override
                public void submitTask(TaskRequest request, StreamObserver<TaskResponse> responseObserver) {
                    responseObserver.onNext(TaskResponse.newBuilder()
                            .setTaskId(request.getTaskId())
                            .setStatus(Status.QUEUED)
                            .build());
                    responseObserver.onCompleted();
                }

                @Override
                public void getStatus(Empty request, StreamObserver<ServerStatus> responseObserver) {
                    responseObserver.onNext(ServerStatus.newBuilder()
                            .setActiveWorkers(5)
                            .build());
                    responseObserver.onCompleted();
                }
            }));

    @SuppressWarnings("null")
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(serviceImpl)
                .build()
                .start());

        ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder
                .forName(serverName)
                .directExecutor()
                .build());

        context = new GrpcDistributedContext(channel);
    }

    @Test
    public void testSubmitAndGet() throws Exception {
        Callable<String> task = () -> "test-result";
        
        // Mock streamResults
        String taskId = "task-123";
        // We need to capture the request to use its task ID or just mock any
        doAnswer(invocation -> {
            StreamObserver<TaskResult> responseObserver = invocation.getArgument(1);
            
            // Serialize "test-result"
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject("completed-result");
            oos.flush();
            
            responseObserver.onNext(TaskResult.newBuilder()
                    .setTaskId(taskId)
                    .setStatus(Status.COMPLETED)
                    .setSerializedData(com.google.protobuf.ByteString.copyFrom(bos.toByteArray()))
                    .build());
            responseObserver.onCompleted();
            return null;
        }).when(serviceImpl).streamResults(any(TaskIdentifier.class), any());

        Future<String> future = context.submit(task);
        assertNotNull(future);
        
        String result = future.get(5, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals("completed-result", result);
        assertTrue(future.isDone());
    }

    @Test
    public void testGetParallelism() {
        int parallelism = context.getParallelism();
        assertEquals(5, parallelism);
    }
}
