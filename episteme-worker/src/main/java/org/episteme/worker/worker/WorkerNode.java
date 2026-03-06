/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.worker.worker;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.episteme.core.distributed.ComputationException;
import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.server.server.proto.*;

import java.io.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Episteme Worker Node.
 * 
 * <p>
 * Dynamically executes scientific computation tasks using the TaskRegistry.
 * Tasks are discovered and instantiated at runtime, eliminating the need for
 * hardcoded handlers.
 * </p>
 * 
 * <p>
 * Supports both primitive double and Real-based tasks with automatic GPU
 * acceleration when available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WorkerNode {

    private static final Logger logger = LoggerFactory.getLogger(WorkerNode.class);

    private final ManagedChannel channel;
    private final ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private final TaskRegistry taskRegistry;
    private String workerId;
    private volatile boolean running = true;

    /**
     * Creates a new WorkerNode connected to the specified server.
     *
     * @param host server hostname
     * @param port server port
     */
    public WorkerNode(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
        this.taskRegistry = TaskRegistry.getInstance();
        logger.info("WorkerNode connecting to {}:{}", host, port);
    }

    /**
     * Starts the worker node polling loop.
     */
    public void start() {
        register();
        logger.info("Worker {} started, polling for tasks...", workerId);

        while (running) {
            try {
                pollAndExecute();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.info("Worker interrupted, shutting down");
                break;
            } catch (Exception e) {
                logger.warn("Error processing task: {}", e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }

        shutdown();
    }

    /**
     * Stops the worker node.
     */
    public void stop() {
        running = false;
    }

    /**
     * Shuts down the gRPC channel.
     */
    public void shutdown() {
        channel.shutdown();
        logger.info("Worker {} shut down", workerId);
    }

    private void register() {
        WorkerRegistration reg = WorkerRegistration.newBuilder()
                .setHostname("worker-" + System.currentTimeMillis())
                .setCores(Runtime.getRuntime().availableProcessors())
                .build();
        this.workerId = blockingStub.registerWorker(reg).getWorkerId();
        logger.info("Registered as worker: {}", workerId);
    }

    private void pollAndExecute() {
        TaskRequest task = blockingStub.requestTask(
                WorkerIdentifier.newBuilder().setWorkerId(workerId).build());

        if (task.getTaskId().isEmpty()) {
            return;
        }

        logger.debug("Received task: {} type: {}", task.getTaskId(), task.getTaskType());

        try {
            byte[] resultBytes = executeTask(task);
            blockingStub.submitResult(TaskResult.newBuilder()
                    .setTaskId(task.getTaskId())
                    .setStatus(Status.COMPLETED)
                    .setSerializedData(ByteString.copyFrom(resultBytes))
                    .build());
            logger.debug("Task {} completed successfully", task.getTaskId());
        } catch (Exception e) {
            logger.warn("Task {} failed: {}", task.getTaskId(), e.getMessage());
            blockingStub.submitResult(TaskResult.newBuilder()
                    .setTaskId(task.getTaskId())
                    .setStatus(Status.FAILED)
                    .setErrorMessage(e.toString())
                    .build());
        }
    }

    @SuppressWarnings("unchecked")
    private byte[] executeTask(TaskRequest request) throws Exception {
        byte[] taskData = request.getSerializedTask().toByteArray();
        String taskType = request.getTaskType();

        // Try to get task from registry by type
        if (!taskType.isEmpty()) {
            Optional<DistributedTask<?, ?>> taskOpt = taskRegistry.get(taskType);
            if (taskOpt.isPresent()) {
                return executeRegisteredTask(taskOpt.get(), taskData);
            }
        }

        // Fallback: deserialize directly and execute
        // The serialized data contains the task with embedded input
        Object obj = deserialize(taskData);

        if (obj instanceof DistributedTask) {
            // For self-contained tasks that include their own input
            DistributedTask<Serializable, Serializable> task = (DistributedTask<Serializable, Serializable>) obj;
            // Execute with null input - task should be self-contained
            Serializable result = task.execute(null);
            return serialize(result);
        }

        // Handle legacy Runnable/Callable tasks
        if (obj instanceof Runnable) {
            ((Runnable) obj).run();
            return serialize("OK");
        }

        throw new IllegalArgumentException("Unknown task type: " + taskType);
    }

    @SuppressWarnings("unchecked")
    private byte[] executeRegisteredTask(DistributedTask<?, ?> task, byte[] inputData)
            throws Exception {
        DistributedTask<Serializable, Serializable> typedTask = (DistributedTask<Serializable, Serializable>) task;

        Serializable input = (Serializable) deserialize(inputData);

        try {
            Serializable result = typedTask.execute(input);
            return serialize(result);
        } catch (ComputationException e) {
            throw new RuntimeException("Computation failed: " + e.getMessage(), e);
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        return bos.toByteArray();
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    /**
     * Main entry point for the worker node.
     *
     * @param args [host] [port] - defaults to localhost:50051
     */
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 50051;

        WorkerNode worker = new WorkerNode(host, port);

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            worker.stop();
            worker.shutdown();
        }));

        worker.start();
    }
}

