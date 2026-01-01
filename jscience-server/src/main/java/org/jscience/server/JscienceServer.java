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

import java.util.UUID;

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

    // Repositories
    private final org.jscience.server.repository.JobRepository jobRepository;
    private final org.jscience.server.repository.UserRepository userRepository;

    // mDNS Discovery
    private org.jscience.server.discovery.DiscoveryService discoveryService;

    public JscienceServer(int port) {
        this.port = port;
        // Initialize Persistent Repositories (H2)
        try {
            this.jobRepository = new org.jscience.server.repository.JobRepository();
            this.userRepository = new org.jscience.server.repository.UserRepository();
            // Create default admin for testing
            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(new org.jscience.server.model.User("admin", "secret", "ADMIN"));
                LOG.info("Created default admin user.");
            }
        } catch (Exception e) {
            LOG.error("Failed to initialize database", e);
            throw new RuntimeException(e);
        }

        // Build gRPC Server (with optional TLS)
        io.grpc.ServerBuilder<?> builder = ServerBuilder.forPort(port);

        if (Boolean.getBoolean("ssl")) {
            try {
                // Use PEM files for TLS (standard gRPC API)
                java.io.File certChain = new java.io.File("certs/server.pem");
                java.io.File privateKey = new java.io.File("certs/server-key.pem");

                io.grpc.TlsServerCredentials.Builder tlsBuilder = io.grpc.TlsServerCredentials.newBuilder()
                        .keyManager(certChain, privateKey);
                io.grpc.ServerCredentials creds = tlsBuilder.build();
                builder = io.grpc.Grpc.newServerBuilderForPort(port, creds);
                LOG.info("TLS ENABLED - using certs/server.pem");
            } catch (Exception e) {
                LOG.error("Failed to configure TLS, falling back to plaintext", e);
            }
        }

        this.server = builder
                .addService(this)
                .addService(new DeviceServiceImpl())
                .addService(new DataServiceImpl())
                .addService(new CollaborationServiceImpl(this))
                .addService(new AuthServiceImpl(userRepository))
                .build();
    }

    public void start() throws IOException {
        server.start();
        LOG.info("JScience Server started, listening on " + port);

        // Start mDNS broadcast if discovery enabled
        if (Boolean.getBoolean("discovery")) {
            discoveryService = new org.jscience.server.discovery.DiscoveryService();
            discoveryService.startBroadcast(port);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down gRPC server since JVM is shutting down");
            JscienceServer.this.stop();
        }));
    }

    public void stop() {
        if (discoveryService != null) {
            discoveryService.stop();
        }
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    // Store active tasks and their futures
    private final java.util.concurrent.ConcurrentMap<String, java.util.concurrent.CompletableFuture<Object>> taskFutures = new java.util.concurrent.ConcurrentHashMap<>();

    // Distributed Task Queue (Priority-Based)
    private final java.util.concurrent.BlockingQueue<TaskRequest> taskQueue = new java.util.concurrent.PriorityBlockingQueue<>(
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
    private final java.util.concurrent.ConcurrentMap<String, String> authorizedWorkers = new java.util.concurrent.ConcurrentHashMap<>();

    // Active Collaboration Sessions
    private final java.util.concurrent.ConcurrentMap<String, java.util.List<StreamObserver<org.jscience.server.proto.SessionEvent>>> collaborationSessions = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public void submitTask(TaskRequest request, StreamObserver<TaskResponse> responseObserver) {
        String taskId = request.getTaskId().isEmpty() ? UUID.randomUUID().toString() : request.getTaskId();
        LOG.info("Received task submission: " + taskId + " (Priority: " + request.getPriority() + ")");

        // Persist Job
        try {
            String userId = org.jscience.server.auth.RbacInterceptor.getCurrentUserId();
            if (userId == null)
                userId = "anonymous";

            org.jscience.server.model.Job job = new org.jscience.server.model.Job(
                    taskId,
                    "QUEUED",
                    userId,
                    request.getPriorityValue());
            jobRepository.save(job);
        } catch (Exception e) {
            LOG.warn("Failed to persist job " + taskId, e);
        }

        // Create a future for this task
        java.util.concurrent.CompletableFuture<Object> future = new java.util.concurrent.CompletableFuture<>();
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
    public void registerWorker(org.jscience.server.proto.WorkerRegistration request,
            StreamObserver<org.jscience.server.proto.WorkerRegistrationResponse> responseObserver) {
        String workerId = "worker-" + UUID.randomUUID().toString().substring(0, 8);
        authorizedWorkers.put(workerId, request.getHostname());
        LOG.info("Worker registered: " + workerId + " (" + request.getHostname() + ", " + request.getCores()
                + " cores)");

        org.jscience.server.proto.WorkerRegistrationResponse response = org.jscience.server.proto.WorkerRegistrationResponse
                .newBuilder()
                .setWorkerId(workerId)
                .setAuthorized(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void requestTask(org.jscience.server.proto.WorkerIdentifier request,
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
            LOG.info("Assigning task " + task.getTaskId() + " to worker " + request.getWorkerId());
            responseObserver.onNext(task);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void submitResult(org.jscience.server.proto.TaskResult request,
            StreamObserver<org.jscience.server.proto.Empty> responseObserver) {
        String taskId = request.getTaskId();
        LOG.info("Received result for task " + taskId);

        java.util.concurrent.CompletableFuture<Object> future = taskFutures.get(taskId);
        if (future != null) {
            if (request.getStatus() == Status.COMPLETED) {
                try {
                    java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(
                            request.getSerializedData().toByteArray());
                    java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
                    Object result = ois.readObject();
                    future.complete(result);
                } catch (Exception e) {
                    LOG.error("Failed to deserialize result from worker", e);
                    future.completeExceptionally(e);
                }
            } else {
                future.completeExceptionally(new RuntimeException(request.getErrorMessage()));
            }
        }

        responseObserver.onNext(org.jscience.server.proto.Empty.newBuilder().build());
        responseObserver.onCompleted();

        // MLflow Integration
        if (Boolean.getBoolean("mlflow")) {
            long duration = System.currentTimeMillis() - taskQueue.stream()
                    .filter(t -> t.getTaskId().equals(taskId))
                    .map(TaskRequest::getTimestamp)
                    .findFirst()
                    .orElse(System.currentTimeMillis()); // Approx if not found

            // Async log
            new Thread(() -> {
                try {
                    org.jscience.server.integrations.MlflowClient client = new org.jscience.server.integrations.MlflowClient(
                            "http://localhost:5000", "JScience");
                    String runId = client.startRun("Task-" + taskId).join();
                    client.logMetric(runId, "duration_ms", duration, System.currentTimeMillis());
                    client.logParam(runId, "worker_id", "unknown"); // We could extract this if we parsed the token
                    client.endRun(runId, "FINISHED");
                } catch (Exception e) {
                    LOG.warn("MLflow logging failed", e);
                }
            }).start();
        }
    }

    @Override
    public void streamResults(org.jscience.server.proto.TaskIdentifier request,
            StreamObserver<org.jscience.server.proto.TaskResult> responseObserver) {
        String taskId = request.getTaskId();
        java.util.concurrent.CompletableFuture<Object> future = taskFutures.get(taskId);

        if (future == null) {
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND.withDescription("Task not found: " + taskId).asRuntimeException());
            return;
        }

        try {
            // Wait for the result (blocking this gRPC handler thread)
            // In a real prod server, we might want to be asynchronous here too,
            // but for this implementation, blocking is acceptable as gRPC handles threads
            // well.
            Object result = future.get(); // Blocks until complete

            // Serialize Result
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject(result);
            oos.flush();
            byte[] bytes = bos.toByteArray();

            org.jscience.server.proto.TaskResult response = org.jscience.server.proto.TaskResult.newBuilder()
                    .setTaskId(taskId)
                    .setStatus(Status.COMPLETED)
                    .setSerializedData(com.google.protobuf.ByteString.copyFrom(bytes))
                    .setProgress(100)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // Cleanup
            taskFutures.remove(taskId);

        } catch (Exception e) {
            LOG.error("Failed to retrieve result for " + taskId, e);
            org.jscience.server.proto.TaskResult errorResponse = org.jscience.server.proto.TaskResult.newBuilder()
                    .setTaskId(taskId)
                    .setStatus(Status.FAILED)
                    .setErrorMessage(e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    // ---------------------------------------------------------
    // Device Service Implementation
    // ---------------------------------------------------------
    private static class DeviceServiceImpl extends org.jscience.server.proto.DeviceServiceGrpc.DeviceServiceImplBase {

        @Override
        public void listDevices(org.jscience.server.proto.Empty request,
                StreamObserver<org.jscience.server.proto.DeviceList> responseObserver) {
            // Mock discovery of devices
            org.jscience.server.proto.DeviceInfo telescope = org.jscience.server.proto.DeviceInfo.newBuilder()
                    .setDeviceId("dev-scope-01")
                    .setName("Main Observatory Telescope")
                    .setType("TELESCOPE")
                    .setConnected(true)
                    .build();

            org.jscience.server.proto.DeviceInfo sensor = org.jscience.server.proto.DeviceInfo.newBuilder()
                    .setDeviceId("dev-sensor-weather")
                    .setName("Weather Station Alpha")
                    .setType("SENSOR")
                    .setConnected(true)
                    .build();

            org.jscience.server.proto.DeviceList response = org.jscience.server.proto.DeviceList.newBuilder()
                    .addDevices(telescope)
                    .addDevices(sensor)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void sendCommand(org.jscience.server.proto.DeviceCommand request,
                StreamObserver<org.jscience.server.proto.CommandResponse> responseObserver) {
            LOG.info("Received command " + request.getCommand() + " for device " + request.getDeviceId());

            // Mock execution
            org.jscience.server.proto.CommandResponse response = org.jscience.server.proto.CommandResponse.newBuilder()
                    .setCommandId(UUID.randomUUID().toString())
                    .setSuccess(true)
                    .setResultMessage("Command " + request.getCommand() + " executed successfully.")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void subscribeTelemetry(org.jscience.server.proto.DeviceIdentifier request,
                StreamObserver<org.jscience.server.proto.DeviceData> responseObserver) {
            // Mock streaming data (send 3 updates then finish)
            new Thread(() -> {
                try {
                    for (int i = 0; i < 3; i++) {
                        org.jscience.server.proto.DeviceData data = org.jscience.server.proto.DeviceData.newBuilder()
                                .setDeviceId(request.getDeviceId())
                                .setTimestamp(System.currentTimeMillis())
                                .setDataType("LOG")
                                .putNumericReadings("temperature", 20.0 + Math.random())
                                .build();

                        responseObserver.onNext(data);
                        Thread.sleep(1000);
                    }
                    responseObserver.onCompleted();
                } catch (Exception e) {
                    // ignore
                }
            }).start();
        }
    }

    // Data Service Implementation (Mock Data Lake)
    private static class DataServiceImpl extends org.jscience.server.proto.DataServiceGrpc.DataServiceImplBase {
        @Override
        public void streamGenomeData(org.jscience.server.proto.GenomeRegionRequest request,
                StreamObserver<org.jscience.server.proto.GenomeChunk> responseObserver) {
            try {
                long current = request.getStartBase();
                long end = request.getEndBase();
                while (current < end) {
                    long chunkSize = Math.min(100, end - current);
                    StringBuilder seq = new StringBuilder();
                    for (int i = 0; i < chunkSize; i++) {
                        seq.append("AGCT".charAt((int) (Math.random() * 4)));
                    }

                    org.jscience.server.proto.GenomeChunk chunk = org.jscience.server.proto.GenomeChunk.newBuilder()
                            .setSequence(seq.toString())
                            .setStartBase(current)
                            .build();
                    responseObserver.onNext(chunk);
                    current += chunkSize;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {
                    }
                }
                responseObserver.onCompleted();
            } catch (Exception e) {
                responseObserver.onError(e);
            }
        }

        @Override
        public void streamStarCatalog(org.jscience.server.proto.SkyRegionRequest request,
                StreamObserver<org.jscience.server.proto.StarObject> responseObserver) {
            try {
                // Simulate 10 stars in the region
                for (int i = 0; i < 10; i++) {
                    org.jscience.server.proto.StarObject star = org.jscience.server.proto.StarObject.newBuilder()
                            .setStarId("Star-" + UUID.randomUUID().toString().substring(0, 8))
                            .setRa(request.getMinRa() + Math.random() * (request.getMaxRa() - request.getMinRa()))
                            .setDec(request.getMinDec() + Math.random() * (request.getMaxDec() - request.getMinDec()))
                            .setMagnitude(Math.random() * 6) // Visible stars
                            .setType("G2V")
                            .build();
                    responseObserver.onNext(star);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }
                responseObserver.onCompleted();
            } catch (Exception e) {
                responseObserver.onError(e);
            }
        }
    }

    // Collaboration Service Implementation
    private static class CollaborationServiceImpl
            extends org.jscience.server.proto.CollaborationServiceGrpc.CollaborationServiceImplBase {
        private final JscienceServer server;

        public CollaborationServiceImpl(JscienceServer server) {
            this.server = server;
        }

        @Override
        public void createSession(org.jscience.server.proto.CreateSessionRequest request,
                StreamObserver<org.jscience.server.proto.SessionResponse> responseObserver) {
            String sessionId = UUID.randomUUID().toString();
            server.collaborationSessions.put(sessionId, new java.util.concurrent.CopyOnWriteArrayList<>());
            LOG.info("Created collaboration session: " + sessionId + " by owner " + request.getOwnerId());

            responseObserver
                    .onNext(org.jscience.server.proto.SessionResponse.newBuilder().setSessionId(sessionId).build());
            responseObserver.onCompleted();
        }

        @Override
        public void joinSession(org.jscience.server.proto.SessionRequest request,
                StreamObserver<org.jscience.server.proto.SessionEvent> responseObserver) {
            String sessionId = request.getSessionId();
            java.util.List<StreamObserver<org.jscience.server.proto.SessionEvent>> subscribers = server.collaborationSessions
                    .get(sessionId);

            if (subscribers == null) {
                responseObserver
                        .onError(io.grpc.Status.NOT_FOUND.withDescription("Session not found").asRuntimeException());
                return;
            }

            subscribers.add(responseObserver);
            LOG.info("User " + request.getUserId() + " joined session " + sessionId);
            // Stream stays open
        }

        @Override
        public void publishEvent(org.jscience.server.proto.SessionEvent request,
                StreamObserver<org.jscience.server.proto.PublishAck> responseObserver) {
            String sessionId = request.getSessionId();
            java.util.List<StreamObserver<org.jscience.server.proto.SessionEvent>> subscribers = server.collaborationSessions
                    .get(sessionId);

            if (subscribers != null) {
                for (StreamObserver<org.jscience.server.proto.SessionEvent> subscriber : subscribers) {
                    try {
                        subscriber.onNext(request);
                    } catch (Exception e) {
                        subscribers.remove(subscriber);
                    }
                }
            }

            responseObserver.onNext(org.jscience.server.proto.PublishAck.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // Port priority: CLI arg > System Property > Default (50051)
        int port = 50051;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else if (System.getProperty("port") != null) {
            port = Integer.parseInt(System.getProperty("port"));
        }

        JscienceServer server = new JscienceServer(port);
        server.start();
        server.blockUntilShutdown();
    }
}


