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
import org.jscience.server.proto.ComputeServiceGrpc;
import org.jscience.server.proto.TaskRequest;
import org.jscience.server.proto.TaskResult;
import org.jscience.server.proto.WorkerIdentifier;
import org.jscience.server.proto.WorkerRegistration;
import org.jscience.server.proto.WorkerRegistrationResponse;
import org.jscience.server.proto.Status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

/**
 * JScience Worker Node.
 * Connects to the Grid Server, pulls tasks, executes them, and returns results.
 */
public class WorkerNode {

    private final ManagedChannel channel;
    private final ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private String workerId;

    public WorkerNode(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
    }

    public void start() {
        System.out.println("Starting JScience Worker Node...");
        register();
        
        System.out.println("Worker " + workerId + " ready. Polling for tasks...");
        
        // Main Loop
        while (true) {
            try {
                pollAndExecute();
                Thread.sleep(2000); // Poll interval
            } catch (Exception e) {
                System.err.println("Error in worker loop: " + e.getMessage());
                try { Thread.sleep(5000); } catch (InterruptedException ie) {}
            }
        }
    }

    private void register() {
        WorkerRegistration reg = WorkerRegistration.newBuilder()
                .setHostname("localhost") // Detect actual hostname in prod
                .setCores(Runtime.getRuntime().availableProcessors())
                .setMemory(Runtime.getRuntime().maxMemory())
                .build();
        
        WorkerRegistrationResponse response = blockingStub.registerWorker(reg);
        this.workerId = response.getWorkerId();
        System.out.println("Registered with Server. Worker ID: " + workerId);
    }

    private void pollAndExecute() {
        WorkerIdentifier id = WorkerIdentifier.newBuilder().setWorkerId(workerId).build();
        TaskRequest task = blockingStub.requestTask(id);

        if (task.getTaskId().isEmpty()) {
            // No work
            return;
        }

        System.out.println("Received Task: " + task.getTaskId());
        
        try {
            // Execute
            byte[] taskBytes = task.getSerializedTask().toByteArray();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(taskBytes));
            Callable<?> callable = (Callable<?>) ois.readObject();

            Object result = callable.call();

            // Serialize Result
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(result);
            oos.flush();
            byte[] resultBytes = bos.toByteArray();

            // Submit Result
            TaskResult taskResult = TaskResult.newBuilder()
                    .setTaskId(task.getTaskId())
                    .setStatus(Status.COMPLETED)
                    .setSerializedData(ByteString.copyFrom(resultBytes))
                    .build();

            blockingStub.submitResult(taskResult);
            System.out.println("Task " + task.getTaskId() + " completed and submitted.");

        } catch (Exception e) {
            System.err.println("Task failed: " + e.getMessage());
             TaskResult errorResult = TaskResult.newBuilder()
                    .setTaskId(task.getTaskId())
                    .setStatus(Status.FAILED)
                    .setErrorMessage(e.toString())
                    .build();
             blockingStub.submitResult(errorResult);
        }
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 50051;
        
        new WorkerNode(host, port).start();
    }
}


