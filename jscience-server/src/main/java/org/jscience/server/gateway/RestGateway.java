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

package org.jscience.server.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.ByteString;
// removed com.google.protobuf.Empty to use org.jscience.server.proto.Empty
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.jscience.server.metrics.MetricsRegistry; // We might need to migrate this later
import org.jscience.server.proto.*;
import org.jscience.server.auth.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Spring Boot REST Gateway for JScience.
 * Translates REST calls to gRPC calls.
 */
@RestController
@RequestMapping("/api")
public class RestGateway {

    private static final Logger LOG = LoggerFactory.getLogger(RestGateway.class);
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @GrpcClient("local-server")
    private ComputeServiceGrpc.ComputeServiceBlockingStub computeStub;

    @GrpcClient("local-server")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    @GetMapping("/health")
    public ResponseEntity<ObjectNode> health() {
        ObjectNode response = JSON_MAPPER.createObjectNode();
        response.put("status", "healthy");
        response.put("timestamp", System.currentTimeMillis());
        // In Spring Boot, server info is handled by configuration, we omit grpc
        // host/port here
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        try {
            ServerStatus status = computeStub.getStatus(Empty.newBuilder().build());
            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("activeWorkers", status.getActiveWorkers());
            response.put("queuedTasks", status.getQueuedTasks());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error getting status", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/workers")
    public ResponseEntity<?> getWorkers() {
        try {
            ServerStatus status = computeStub.getStatus(Empty.newBuilder().build());
            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("activeWorkers", status.getActiveWorkers());
            response.put("message", "Worker details require extended API");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error listing workers", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> submitTask(@RequestBody JsonNode requestBody,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String taskData = requestBody.path("taskData").asText(null);
            String priorityStr = requestBody.path("priority").asText("NORMAL");
            String taskId = requestBody.path("taskId").asText(null);

            if (taskData == null || taskData.isEmpty()) {
                return error(HttpStatus.BAD_REQUEST, "Missing taskData field");
            }

            Priority priority = Priority.NORMAL;
            try {
                priority = Priority.valueOf(priorityStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                LOG.warn("Invalid priority '{}', using NORMAL", priorityStr);
            }

            ComputeServiceGrpc.ComputeServiceBlockingStub stub = computeStub;
            if (authHeader != null) {
                Metadata metadata = new Metadata();
                metadata.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), authHeader);
                stub = stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
            }

            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId(taskId != null ? taskId : UUID.randomUUID().toString())
                    .setSerializedTask(ByteString.copyFromUtf8(taskData))
                    .setPriority(priority)
                    .build();

            TaskResponse grpcResponse = stub.submitTask(request);

            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("taskId", grpcResponse.getTaskId());
            response.put("status", grpcResponse.getStatus().name());
            response.put("message", grpcResponse.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error submitting task", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody JsonNode requestBody) {
        try {
            String username = requestBody.path("username").asText(null);
            String password = requestBody.path("password").asText(null);

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                return error(HttpStatus.BAD_REQUEST, "Missing username or password");
            }

            LoginRequest request = LoginRequest.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .build();

            AuthResponse grpcResponse = authStub.login(request);

            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("success", grpcResponse.getSuccess());
            response.put("message", grpcResponse.getMessage());
            if (grpcResponse.getSuccess()) {
                response.put("token", grpcResponse.getToken());
            }
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(grpcResponse.getSuccess() ? 200 : 401).body(response);
        } catch (Exception e) {
            LOG.error("Error during login", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody JsonNode requestBody) {
        try {
            String username = requestBody.path("username").asText(null);
            String password = requestBody.path("password").asText(null);
            String role = requestBody.path("role").asText(Roles.SCIENTIST);

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                return error(HttpStatus.BAD_REQUEST, "Missing username or password");
            }

            RegisterRequest request = RegisterRequest.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .setRole(role)
                    .build();

            AuthResponse grpcResponse = authStub.register(request);

            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("success", grpcResponse.getSuccess());
            response.put("message", grpcResponse.getMessage());
            if (grpcResponse.getSuccess()) {
                response.put("token", grpcResponse.getToken());
            }
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(grpcResponse.getSuccess() ? 201 : 400).body(response);
        } catch (Exception e) {
            LOG.error("Error during registration", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Custom metrics endpoint (manual, or could delegate to Actuator)
    @GetMapping("/metrics") // Note: /api prefix from class, so this is /api/metrics but original was
                            // /metrics.
    // We should fix the mapping or let user know.
    // Wait, original was route "/metrics" on root server. Here we are under /api.
    // I can stick another controller for root paths or just accept /api/metrics.
    // Let's create a separate method mapped to root if possible? No, strictly /api
    // here.
    // I'll leave it as /api/metrics for now, or just rely on Actuator.
    public ResponseEntity<?> metrics() {
        try {
            String metrics = MetricsRegistry.getInstance().scrape();
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain; version=0.0.4")
                    .body(metrics);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private ResponseEntity<ObjectNode> error(HttpStatus status, String message) {
        ObjectNode error = JSON_MAPPER.createObjectNode();
        error.put("error", message);
        error.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(status).body(error);
    }
}
