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

package org.jscience.server.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jscience.server.model.Job;
import org.jscience.server.proto.*;
import org.jscience.server.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * gRPC Service for Compute Task Management.
 */
@GrpcService
public class ComputeServiceImpl extends ComputeServiceGrpc.ComputeServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(ComputeServiceImpl.class);

    private final JobRepository jobRepository;

    // Store active tasks and their futures
    private final ConcurrentMap<String, CompletableFuture<Object>> taskFutures = new ConcurrentHashMap<>();

    // Distributed Task Queue (Priority-Based)
    private final BlockingQueue<TaskRequest> taskQueue = new PriorityBlockingQueue<>(
            11, (t1, t2) -> {
                // Priority Descending (Highest Priority First)
                int p1 = t1.getPriorityValue();
                int p2 = t2.getPriorityValue();
                if (p1 != p2) {
                    return Integer.compare(p2, p1);
                }
                // Timestamp Ascending (Oldest First)
                return Long.compare(t1.getTimestamp(), t2.getTimestamp());
            });

    // Store authorized workers
    private final ConcurrentMap<String, String> authorizedWorkers = new ConcurrentHashMap<>();

    // Performance Metrics
    private final java.util.concurrent.atomic.AtomicInteger totalTasksCompleted = new java.util.concurrent.atomic.AtomicInteger(
            0);

    @Autowired
    public ComputeServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void getStatus(Empty request, StreamObserver<ServerStatus> responseObserver) {
        ServerStatus status = ServerStatus.newBuilder()
                .setActiveWorkers(authorizedWorkers.size())
                .setQueuedTasks(taskQueue.size())
                .setTotalTasksCompleted(totalTasksCompleted.get())
                .build();
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }

    @Override
    public void submitTask(TaskRequest request, StreamObserver<TaskResponse> responseObserver) {
        String taskId = request.getTaskId().isEmpty() ? UUID.randomUUID().toString() : request.getTaskId();
        LOG.info("Received task submission: {} (Priority: {})", taskId, request.getPriority());

        // Persist Job
        try {
            // Get authenticated user from gRPC context (set by RbacInterceptor)
            String userId = org.jscience.server.auth.RbacInterceptor.getCurrentUserId();
            if (userId == null || userId.isEmpty()) {
                userId = "anonymous";
            }

            Job job = new Job(
                    taskId,
                    "QUEUED",
                    userId,
                    request.getPriorityValue());
            jobRepository.save(job);
        } catch (Exception e) {
            LOG.warn("Failed to persist job {}", taskId, e);
        }

        // Create a future for this task
        CompletableFuture<Object> future = new CompletableFuture<>();
        taskFutures.put(taskId, future);

        // Queue for distributed workers with timestamp
        TaskRequest queuedRequest = request.toBuilder()
                .setTaskId(taskId)
                .setTimestamp(System.currentTimeMillis())
                .build();
        taskQueue.offer(queuedRequest);

        // Acknowledge receipt
        TaskResponse response = TaskResponse.newBuilder()
                .setTaskId(taskId)
                .setStatus(Status.QUEUED)
                .setMessage("Task queued for distributed execution")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerWorker(WorkerRegistration request,
            StreamObserver<WorkerRegistrationResponse> responseObserver) {
        String workerId = "worker-" + UUID.randomUUID().toString().substring(0, 8);
        authorizedWorkers.put(workerId, request.getHostname());
        LOG.info("Worker registered: {} ({}, {} cores)", workerId, request.getHostname(), request.getCores());

        WorkerRegistrationResponse response = WorkerRegistrationResponse
                .newBuilder()
                .setWorkerId(workerId)
                .setAuthorized(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void requestTask(WorkerIdentifier request,
            StreamObserver<TaskRequest> responseObserver) {
        if (!authorizedWorkers.containsKey(request.getWorkerId())) {
            responseObserver
                    .onError(io.grpc.Status.PERMISSION_DENIED.withDescription("Unknown worker").asRuntimeException());
            return;
        }

        TaskRequest task = taskQueue.poll();
        if (task == null) {
            responseObserver.onNext(TaskRequest.newBuilder().build());
        } else {
            LOG.info("Assigning task {} to worker {}", task.getTaskId(), request.getWorkerId());
            responseObserver.onNext(task);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void submitResult(TaskResult request, StreamObserver<Empty> responseObserver) {
        String taskId = request.getTaskId();
        LOG.info("Received result for task {}", taskId);

        CompletableFuture<Object> future = taskFutures.get(taskId);
        if (future != null) {
            if (request.getStatus() == Status.COMPLETED) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(
                            request.getSerializedData().toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    Object result = ois.readObject();
                    future.complete(result);
                    totalTasksCompleted.incrementAndGet();
                } catch (Exception e) {
                    LOG.error("Failed to deserialize result from worker", e);
                    future.completeExceptionally(e);
                }
            } else {
                future.completeExceptionally(new RuntimeException(request.getErrorMessage()));
            }
        }

        // Update Job Status
        try {
            jobRepository.findById(taskId).ifPresent(job -> {
                job.setStatus(request.getStatus().name());
                job.setCompletedAt(java.time.LocalDateTime.now());
                jobRepository.save(job);
            });
        } catch (Exception e) {
            LOG.warn("Failed to update job status for {}", taskId, e);
        }

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void streamResults(TaskIdentifier request,
            StreamObserver<TaskResult> responseObserver) {
        String taskId = request.getTaskId();
        CompletableFuture<Object> future = taskFutures.get(taskId);

        if (future == null) {
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND.withDescription("Task not found: " + taskId).asRuntimeException());
            return;
        }

        try {
            Object result = future.get(); // Blocks until complete

            // Serialize Result
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(result);
            oos.flush();
            byte[] bytes = bos.toByteArray();

            TaskResult response = TaskResult.newBuilder()
                    .setTaskId(taskId)
                    .setStatus(Status.COMPLETED)
                    .setSerializedData(ByteString.copyFrom(bytes))
                    .setProgress(100)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // Cleanup
            taskFutures.remove(taskId);

        } catch (Exception e) {
            LOG.error("Failed to retrieve result for {}", taskId, e);
            TaskResult errorResponse = TaskResult.newBuilder()
                    .setTaskId(taskId)
                    .setStatus(Status.FAILED)
                    .setErrorMessage(e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }
}
