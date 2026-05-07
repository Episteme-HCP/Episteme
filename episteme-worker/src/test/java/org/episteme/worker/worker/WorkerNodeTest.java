/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.worker.worker;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.ByteString;
import org.episteme.server.server.proto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WorkerNode using In-Process gRPC.
 */
public class WorkerNodeTest {

    private ManagedChannel channel;
    private WorkerNode workerNode;
    private ComputeServiceGrpc.ComputeServiceImplBase serviceImpl;
    private String serverName;

    @BeforeEach
    public void setUp() throws IOException {
        serverName = InProcessServerBuilder.generateName();
        serviceImpl = mock(ComputeServiceGrpc.ComputeServiceImplBase.class, AdditionalAnswers.delegatesTo(
                new ComputeServiceGrpc.ComputeServiceImplBase() {}
        ));

        InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(serviceImpl)
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
        workerNode = new WorkerNode(channel);
    }

    @AfterEach
    public void tearDown() {
        workerNode.shutdown();
    }

    @Test
    public void testRegistration() {
        WorkerRegistrationResponse response = WorkerRegistrationResponse.newBuilder()
                .setWorkerId("test-worker-123")
                .build();

        doAnswer(invocation -> {
            StreamObserver<WorkerRegistrationResponse> responseObserver = invocation.getArgument(1);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return null;
        }).when(serviceImpl).registerWorker(any(WorkerRegistration.class), any());

        workerNode.register();
        // Since workerId is private but used in other methods, we verify it indirectly 
        // by checking if it's sent in subsequent requests if possible, or use reflection
        // for a simple check if we must. Here we just verify the call was made.
        verify(serviceImpl).registerWorker(any(WorkerRegistration.class), any());
    }

    @Test
    public void testPollAndExecuteSuccess() throws Exception {
        // Mock registration first
        testRegistration();

        // 1. Mock requestTask to return a task
        String taskId = "task-456";
        TaskRequest request = TaskRequest.newBuilder()
                .setTaskId(taskId)
                .setTaskType("test-type")
                .setSerializedTask(ByteString.EMPTY)
                .build();

        doAnswer(invocation -> {
            StreamObserver<TaskRequest> responseObserver = invocation.getArgument(1);
            responseObserver.onNext(request);
            responseObserver.onCompleted();
            return null;
        }).when(serviceImpl).requestTask(any(WorkerIdentifier.class), any());

        // 2. Mock executeTask to return some data (we'll override it in a real test logic, 
        // but here we are testing the loop in pollAndExecute)
        // We'll mock the stub for submitResult
        ArgumentCaptor<TaskResult> resultCaptor = ArgumentCaptor.forClass(TaskResult.class);

        // Since executeTask is package-private, we could spy it, but let's test the real execution
        // with a simple Runnable task if the fallback works.
        // Actually, let's just verify the submission.
        
        workerNode.pollAndExecute();

        verify(serviceImpl).submitResult(resultCaptor.capture(), any());
        TaskResult result = resultCaptor.getValue();
        assertEquals(taskId, result.getTaskId());
        // It might fail because executeTask(empty) fails, but we want to see it try
    }
}
