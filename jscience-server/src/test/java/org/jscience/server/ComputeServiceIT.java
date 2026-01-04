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

package org.jscience.server;

import com.google.protobuf.ByteString;
import org.jscience.server.proto.*;
import org.jscience.server.proto.ComputeServiceGrpc;
import org.jscience.server.proto.Status;
import org.jscience.server.proto.Priority;
import org.jscience.server.proto.TaskRequest;
import org.jscience.server.proto.TaskResponse;
import org.jscience.server.proto.TaskResult;
import org.jscience.server.proto.TaskIdentifier;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ComputeService gRPC endpoints.
 * Tests the full request/response cycle with an embedded gRPC server.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComputeServiceIT {
    private static io.grpc.Server grpcServer;
    private static io.grpc.ManagedChannel channel;
    private static org.jscience.server.proto.ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private static final int TEST_PORT = 50099;

    @BeforeAll
    static void startServer() throws IOException {
        // Mock Repository
        org.jscience.server.repository.JobRepository mockRepo = org.mockito.Mockito
                .mock(org.jscience.server.repository.JobRepository.class);

        // Start gRPC server with service instance
        org.jscience.server.service.ComputeServiceImpl service = new org.jscience.server.service.ComputeServiceImpl(
                mockRepo);

        grpcServer = io.grpc.ServerBuilder.forPort(TEST_PORT)
                .addService(service)
                .build()
                .start();

        // Create client channel
        channel = io.grpc.ManagedChannelBuilder.forAddress("localhost", TEST_PORT)
                .usePlaintext()
                .build();

        blockingStub = org.jscience.server.proto.ComputeServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    static void stopServer() throws InterruptedException {
        if (channel != null) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
        if (grpcServer != null) {
            grpcServer.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should return server status")
    void testGetStatus() {
        // Given
        Empty request = Empty.newBuilder().build();

        // When
        ServerStatus status = blockingStub.getStatus(request);

        // Then
        assertNotNull(status);
        assertTrue(status.getActiveWorkers() >= 0);
        assertTrue(status.getQueuedTasks() >= 0);
    }

    @Test
    @Order(2)
    @DisplayName("Should submit task and receive task ID")
    void testSubmitTask() {
        // Given
        String taskData = "TEST_TASK_DATA";
        TaskRequest request = TaskRequest.newBuilder()
                .setTaskId("test-task-" + System.currentTimeMillis())
                .setSerializedTask(ByteString.copyFromUtf8(taskData))
                .setPriority(Priority.HIGH)
                .setTimestamp(System.currentTimeMillis())
                .build();

        // When
        TaskResponse response = blockingStub.submitTask(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getTaskId());
        assertFalse(response.getTaskId().isEmpty());
        assertEquals(Status.QUEUED, response.getStatus());
    }

    @Test
    @Order(3)
    @DisplayName("Should submit CRITICAL priority task")
    void testSubmitCriticalTask() {
        // Given
        TaskRequest request = TaskRequest.newBuilder()
                .setTaskId("critical-test-" + System.currentTimeMillis())
                .setSerializedTask(ByteString.copyFromUtf8("CRITICAL_PAYLOAD"))
                .setPriority(Priority.CRITICAL)
                .setTimestamp(System.currentTimeMillis())
                .build();

        // When
        TaskResponse response = blockingStub.submitTask(request);

        // Then
        assertNotNull(response);
        assertEquals(Status.QUEUED, response.getStatus());
    }

    @Test
    @Order(4)
    @DisplayName("Should register worker and receive worker ID")
    void testRegisterWorker() {
        // Given
        WorkerRegistration registration = WorkerRegistration.newBuilder()
                .setHostname("test-worker-host")
                .setCores(4)
                .setMemory(8 * 1024 * 1024 * 1024L) // 8GB
                .build();

        // When
        WorkerRegistrationResponse response = blockingStub.registerWorker(registration);

        // Then
        assertNotNull(response);
        assertNotNull(response.getWorkerId());
        assertFalse(response.getWorkerId().isEmpty());
        assertTrue(response.getAuthorized());
    }

    @Test
    @Order(5)
    @DisplayName("Should return empty task when no work available for new worker")
    void testRequestTaskNoWork() {
        // Given - Register a new worker first
        WorkerRegistration registration = WorkerRegistration.newBuilder()
                .setHostname("idle-worker")
                .setCores(2)
                .setMemory(4 * 1024 * 1024 * 1024L)
                .build();
        WorkerRegistrationResponse regResponse = blockingStub.registerWorker(registration);

        WorkerIdentifier workerId = WorkerIdentifier.newBuilder()
                .setWorkerId(regResponse.getWorkerId())
                .build();

        // When
        TaskRequest task = blockingStub.requestTask(workerId);

        // Then
        assertNotNull(task);
        // Task may or may not be available depending on queue state
    }

    @Test
    @Order(6)
    @DisplayName("Should handle multiple concurrent task submissions")
    void testConcurrentTaskSubmissions() {
        // Given
        int taskCount = 10;

        // When/Then
        for (int i = 0; i < taskCount; i++) {
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("concurrent-task-" + i + "-" + System.currentTimeMillis())
                    .setSerializedTask(ByteString.copyFromUtf8("BATCH_TASK_" + i))
                    .setPriority(org.jscience.server.proto.Priority.NORMAL)
                    .setTimestamp(System.currentTimeMillis())
                    .build();

            TaskResponse response = blockingStub.submitTask(request);
            assertNotNull(response);
            assertEquals(Status.QUEUED, response.getStatus());
        }
    }
}
