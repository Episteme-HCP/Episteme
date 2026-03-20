/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.worker.integrations;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.episteme.client.client.distributed.GrpcDistributedContext;
import org.episteme.server.server.service.ComputeServiceImpl;
import org.episteme.worker.worker.WorkerNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end integration test for Episteme Grid.
 */
@SpringBootTest(classes = org.episteme.server.server.EpistemeApplication.class)
@ActiveProfiles("test")
public class DistributedIntegrationTest {

    @Autowired
    private ComputeServiceImpl computeService;

    private String serverName;
    private ManagedChannel channel;
    private WorkerNode worker;
    private GrpcDistributedContext clientContext;

    @BeforeEach
    public void setUp() throws Exception {
        serverName = InProcessServerBuilder.generateName();
        
        // Start an in-process server using the same ComputeService bean from Spring
        InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(computeService)
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
        
        // Initialize Worker and Client pointing to the in-process server
        worker = new WorkerNode(channel);
        clientContext = new GrpcDistributedContext(channel);
        
        // Rapid registration
        worker.register();
    }

    @AfterEach
    public void tearDown() {
        worker.shutdown();
        clientContext.shutdown();
        channel.shutdown();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFullComputeLoop() throws Exception {
        // Define a simple task
        Callable<String> task = (Callable<String> & Serializable) () -> "Hello from Episteme Grid!";
        
        // 1. Client submits task
        Future<String> future = clientContext.submit(task);
        assertNotNull(future);
        
        // 2. Worker polls and executes (one cycle)
        worker.pollAndExecute();
        
        // 3. Client retrieves result
        String result = future.get(10, TimeUnit.SECONDS);
        assertEquals("Hello from Episteme Grid!", result);
    }
}
