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

package org.jscience.worker;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jscience.distributed.ComputationException;
import org.jscience.distributed.DistributedTask;
import org.jscience.distributed.TaskRegistry;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * JScience Worker Node.
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

    private static final Logger LOGGER = Logger.getLogger(WorkerNode.class.getName());

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
        LOGGER.info("WorkerNode connecting to " + host + ":" + port);
    }

    /**
     * Starts the worker node polling loop.
     */
    public void start() {
        register();
        LOGGER.info("Worker " + workerId + " started, polling for tasks...");

        while (running) {
            try {
                pollAndExecute();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.info("Worker interrupted, shutting down");
                break;
            } catch (Exception e) {
                LOGGER.warning("Error processing task: " + e.getMessage());
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
        LOGGER.info("Worker " + workerId + " shut down");
    }

    private void register() {
        WorkerRegistration reg = WorkerRegistration.newBuilder()
                .setHostname("worker-" + System.currentTimeMillis())
                .setCores(Runtime.getRuntime().availableProcessors())
                .build();
        this.workerId = blockingStub.registerWorker(reg).getWorkerId();
        LOGGER.info("Registered as worker: " + workerId);
    }

    private void pollAndExecute() {
        TaskRequest task = blockingStub.requestTask(
                WorkerIdentifier.newBuilder().setWorkerId(workerId).build());

        if (task.getTaskId().isEmpty()) {
            return;
        }

        LOGGER.fine("Received task: " + task.getTaskId() + " type: " + task.getTaskType());

        try {
            byte[] resultBytes = executeTask(task);
            blockingStub.submitResult(TaskResult.newBuilder()
                    .setTaskId(task.getTaskId())
                    .setStatus(Status.COMPLETED)
                    .setSerializedData(ByteString.copyFrom(resultBytes))
                    .build());
            LOGGER.fine("Task " + task.getTaskId() + " completed successfully");
        } catch (Exception e) {
            LOGGER.warning("Task " + task.getTaskId() + " failed: " + e.getMessage());
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
