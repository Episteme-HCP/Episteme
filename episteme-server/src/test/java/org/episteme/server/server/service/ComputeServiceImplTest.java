/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.service;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.episteme.server.server.proto.*;
import org.episteme.server.server.repository.JobRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ComputeServiceImpl.
 */
public class ComputeServiceImplTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Mock
    private JobRepository jobRepository;

    private ComputeServiceImpl service;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;

    @SuppressWarnings("null")
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new ComputeServiceImpl(jobRepository);

        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(service)
                .build()
                .start());

        ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder
                .forName(serverName)
                .directExecutor()
                .build());

        blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void testSubmitTask() {
        TaskRequest request = TaskRequest.newBuilder()
                .setTaskId(UUID.randomUUID().toString())
                .setPriority(Priority.HIGH)
                .setTaskType("TEST_TASK")
                .build();

        TaskResponse response = blockingStub.submitTask(request);

        assertEquals(request.getTaskId(), response.getTaskId());
        assertEquals(Status.QUEUED, response.getStatus());
        verify(jobRepository, times(1)).save(any());
    }

    @Test
    public void testRegisterWorker() {
        WorkerRegistration registration = WorkerRegistration.newBuilder()
                .setHostname("test-worker")
                .setCores(8)
                .build();

        WorkerRegistrationResponse response = blockingStub.registerWorker(registration);

        assertTrue(response.getAuthorized());
        assertNotNull(response.getWorkerId());
    }

    @Test
    public void testGetStatus() {
        ServerStatus status = blockingStub.getStatus(Empty.newBuilder().build());
        assertEquals(0, status.getActiveWorkers());
        assertEquals(0, status.getQueuedTasks());
    }
}
