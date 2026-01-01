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

import org.jscience.server.config.ApplicationConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import com.google.protobuf.ByteString;
import org.jscience.server.proto.*;
import org.jscience.server.auth.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 * Production REST Gateway for JScience gRPC services.
 * 
 * Exposes HTTP/REST endpoints that translate to gRPC calls using proper JSON
 * parsing:
 * 
 * - GET /api/status → ComputeService.GetStatus
 * - POST /api/tasks → ComputeService.SubmitTask
 * - POST /api/auth/login → AuthService.Login
 * - POST /api/auth/register → AuthService.Register
 * - GET /api/health → Health check
 * - GET /api/workers → List workers (from status)
 * 
 * All endpoints accept/return JSON using Jackson for proper serialization.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RestGateway {

    private static final Logger LOG = LoggerFactory.getLogger(RestGateway.class);
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private HttpServer server;
    private final int port;
    private final String grpcHost;
    private final int grpcPort;

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub computeStub;
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public RestGateway(int port, String grpcHost, int grpcPort) {
        this.port = port;
        this.grpcHost = grpcHost;
        this.grpcPort = grpcPort;
    }

    /**
     * Start the REST gateway.
     */
    public void start() throws IOException {
        // Connect to gRPC backend
        channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        computeStub = ComputeServiceGrpc.newBlockingStub(channel);
        authStub = AuthServiceGrpc.newBlockingStub(channel);

        // Start HTTP server
        server = HttpServer.create(new InetSocketAddress(port), 0);

        // Routes
        server.createContext("/api/status", this::handleStatus);
        server.createContext("/api/tasks", this::handleTasks);
        server.createContext("/api/auth/login", this::handleLogin);
        server.createContext("/api/auth/register", this::handleRegister);
        server.createContext("/api/health", this::handleHealth);
        server.createContext("/api/workers", this::handleWorkers);

        server.setExecutor(Executors.newFixedThreadPool(ApplicationConfig.getInstance().getRestGatewayThreads()));
        server.start();

        LOG.info("🚀 REST Gateway started on port {} → gRPC {}:{}", port, grpcHost, grpcPort);
    }

    /**
     * Stop the REST gateway.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
        if (channel != null) {
            channel.shutdown();
        }
        LOG.info("REST Gateway stopped");
    }

    /**
     * Health check endpoint.
     */
    private void handleHealth(HttpExchange exchange) throws IOException {
        ObjectNode response = JSON_MAPPER.createObjectNode();
        response.put("status", "healthy");
        response.put("grpc", grpcHost + ":" + grpcPort);
        response.put("timestamp", System.currentTimeMillis());
        sendJsonResponse(exchange, 200, response);
    }

    /**
     * Get server status endpoint.
     */
    private void handleStatus(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendError(exchange, 405, "Method not allowed");
            return;
        }

        try {
            ServerStatus status = computeStub.getStatus(Empty.newBuilder().build());
            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("activeWorkers", status.getActiveWorkers());
            response.put("queuedTasks", status.getQueuedTasks());
            response.put("timestamp", System.currentTimeMillis());
            sendJsonResponse(exchange, 200, response);
        } catch (Exception e) {
            LOG.error("Error getting status", e);
            sendError(exchange, 500, e.getMessage());
        }
    }

    /**
     * List workers endpoint.
     */
    private void handleWorkers(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendError(exchange, 405, "Method not allowed");
            return;
        }

        try {
            ServerStatus status = computeStub.getStatus(Empty.newBuilder().build());
            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("activeWorkers", status.getActiveWorkers());
            response.put("message", "Worker details require extended API");
            sendJsonResponse(exchange, 200, response);
        } catch (Exception e) {
            LOG.error("Error listing workers", e);
            sendError(exchange, 500, e.getMessage());
        }
    }

    /**
     * Submit task endpoint with proper JSON parsing.
     */
    private void handleTasks(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendError(exchange, 405, "Method not allowed");
            return;
        }

        try {
            JsonNode requestBody = readJsonBody(exchange);

            // Extract fields using Jackson
            String taskData = requestBody.path("taskData").asText(null);
            String priorityStr = requestBody.path("priority").asText("NORMAL");
            String taskId = requestBody.path("taskId").asText(null);

            if (taskData == null || taskData.isEmpty()) {
                sendError(exchange, 400, "Missing taskData field");
                return;
            }

            Priority priority = Priority.NORMAL;
            try {
                priority = Priority.valueOf(priorityStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                LOG.warn("Invalid priority '{}', using NORMAL", priorityStr);
            }

            // Get auth token from header
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            ComputeServiceGrpc.ComputeServiceBlockingStub stub = computeStub;
            if (authHeader != null) {
                Metadata metadata = new Metadata();
                metadata.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), authHeader);
                stub = stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
            }

            // Build request
            TaskRequest.Builder requestBuilder = TaskRequest.newBuilder()
                    .setTaskId(taskId != null ? taskId : java.util.UUID.randomUUID().toString())
                    .setSerializedTask(ByteString.copyFromUtf8(taskData))
                    .setPriority(priority);

            TaskRequest request = requestBuilder.build();
            TaskResponse grpcResponse = stub.submitTask(request);

            // Build JSON response
            ObjectNode response = JSON_MAPPER.createObjectNode();
            response.put("taskId", grpcResponse.getTaskId());
            response.put("status", grpcResponse.getStatus().name());
            response.put("message", grpcResponse.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            sendJsonResponse(exchange, 200, response);
        } catch (Exception e) {
            LOG.error("Error submitting task", e);
            sendError(exchange, 500, e.getMessage());
        }
    }

    /**
     * Login endpoint with proper JSON parsing.
     */
    private void handleLogin(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendError(exchange, 405, "Method not allowed");
            return;
        }

        try {
            JsonNode requestBody = readJsonBody(exchange);

            String username = requestBody.path("username").asText(null);
            String password = requestBody.path("password").asText(null);

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                sendError(exchange, 400, "Missing username or password");
                return;
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

            sendJsonResponse(exchange, grpcResponse.getSuccess() ? 200 : 401, response);
        } catch (Exception e) {
            LOG.error("Error during login", e);
            sendError(exchange, 500, e.getMessage());
        }
    }

    /**
     * Register endpoint with proper JSON parsing.
     */
    private void handleRegister(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendError(exchange, 405, "Method not allowed");
            return;
        }

        try {
            JsonNode requestBody = readJsonBody(exchange);

            String username = requestBody.path("username").asText(null);
            String password = requestBody.path("password").asText(null);
            String role = requestBody.path("role").asText(Roles.SCIENTIST);

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                sendError(exchange, 400, "Missing username or password");
                return;
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

            sendJsonResponse(exchange, grpcResponse.getSuccess() ? 201 : 400, response);
        } catch (Exception e) {
            LOG.error("Error during registration", e);
            sendError(exchange, 500, e.getMessage());
        }
    }

    // --- Helper methods ---

    /**
     * Reads and parses JSON body using Jackson.
     */
    private JsonNode readJsonBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return JSON_MAPPER.readTree(is);
        }
    }

    /**
     * Sends a JSON response using Jackson serialization.
     */
    private void sendJsonResponse(HttpExchange exchange, int code, ObjectNode json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        String jsonString = JSON_MAPPER.writeValueAsString(json);
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Sends an error response.
     */
    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        ObjectNode error = JSON_MAPPER.createObjectNode();
        error.put("error", message);
        error.put("timestamp", System.currentTimeMillis());
        sendJsonResponse(exchange, code, error);
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        String grpcHost = "localhost";
        int grpcPort = 50051;

        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) {
            grpcHost = args[1];
        }
        if (args.length >= 3) {
            grpcPort = Integer.parseInt(args[2]);
        }

        RestGateway gateway = new RestGateway(port, grpcHost, grpcPort);
        gateway.start();

        LOG.info("REST Gateway running. Press Ctrl+C to stop.");

        // Keep running
        Runtime.getRuntime().addShutdownHook(new Thread(gateway::stop));
    }
}
