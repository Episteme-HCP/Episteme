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

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.jscience.server.proto.ComputeServiceGrpc;
import org.jscience.server.proto.TaskRequest;
import org.jscience.server.proto.TaskResponse;
import org.jscience.server.proto.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Coordinator Server for Distributed JScience Computing.
 * Receives tasks via gRPC and executes them (mimicking a cluster).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JscienceServer extends ComputeServiceGrpc.ComputeServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(JscienceServer.class);
    private final int port;
    private final Server server;
    private final ExecutorService workerPool;

    public JscienceServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(this)
                .build();
        this.workerPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void start() throws IOException {
        server.start();
        LOG.info("JScience Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down gRPC server since JVM is shutting down");
            JscienceServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    @Override
    public void submitTask(TaskRequest request, StreamObserver<TaskResponse> responseObserver) {
        String taskId = request.getTaskId().isEmpty() ? UUID.randomUUID().toString() : request.getTaskId();
        LOG.info("Received task submission: " + taskId);

        // Submit to local worker pool (Simulation of dispatching to cluster nodes)
        workerPool.submit(() -> executeTask(taskId, request.getSerializedTask().toByteArray()));

        // Acknowledge receipt
        TaskResponse response = TaskResponse.newBuilder()
                .setTaskId(taskId)
                .setStatus(Status.QUEUED)
                .setMessage("Task accepted for processing")
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void executeTask(String taskId, byte[] serializedTask) {
        try {
            // Deserialize
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedTask));
            Callable<?> task = (Callable<?>) ois.readObject();

            LOG.info("Executing task " + taskId + "...");
            Object result = task.call();
            LOG.info("Task " + taskId + " completed successfully.");

            // In a real system, we would store this result in a DB or cache for retrieval via StreamResults
            // For this simplified implementation, we don't have a callback store yet.
            // Future improvement: Implement a ResultStore map.

        } catch (Exception e) {
            LOG.error("Error executing task " + taskId, e);
        }
    }
    
    // NOTE: StreamResults would be implemented here to poll the ResultStore and push updates to the client.

    public static void main(String[] args) throws IOException, InterruptedException {
        JscienceServer server = new JscienceServer(50051);
        server.start();
        server.blockUntilShutdown();
    }
}
