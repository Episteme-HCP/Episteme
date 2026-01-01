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

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import com.google.protobuf.ByteString;
import org.jscience.server.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * REST Gateway for JScience gRPC services.
 * 
 * Exposes HTTP/REST endpoints that translate to gRPC calls:
 * 
 * GET /api/status -> ComputeService.GetStatus
 * POST /api/tasks -> ComputeService.SubmitTask
 * POST /api/auth/login -> AuthService.Login
 * POST /api/auth/register -> AuthService.Register
 * GET /api/workers -> List workers (from status)
 * 
 * All endpoints accept/return JSON.
 */
public class RestGateway {

    private static final Logger LOG = LoggerFactory.getLogger(RestGateway.class);

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

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        LOG.info("Ã°Å¸Å’Â REST Gateway started on port {} -> gRPC {}:{}", port, grpcHost, grpcPort);
    }

    /**
     * Stop the REST gateway.
     */
    public void stop() {
        if (server != null)
            server.stop(0);
        if (channel != null)
            channel.shutdown();
        LOG.info("REST Gateway stopped");
    }

    private void handleHealth(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, 200, "{\"status\": \"healthy\"}");
    }

    private void handleStatus(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        try {
            ServerStatus status = computeStub.getStatus(Empty.newBuilder().build());
            String json = String.format(
                    "{\"activeWorkers\": %d, \"queuedTasks\": %d}",
                    status.getActiveWorkers(),
                    status.getQueuedTasks());
            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            LOG.error("Error getting status", e);
            sendJsonResponse(exchange, 500, "{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleTasks(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        try {
            String body = readBody(exchange);
            // Simple JSON parsing (in production, use Jackson/Gson)
            String taskData = extractJsonField(body, "taskData");
            String priorityStr = extractJsonField(body, "priority");

            Priority priority = Priority.NORMAL;
            if (priorityStr != null) {
                try {
                    priority = Priority.valueOf(priorityStr.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Get auth token from header
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            ComputeServiceGrpc.ComputeServiceBlockingStub stub = computeStub;
            if (authHeader != null) {
                Metadata metadata = new Metadata();
                metadata.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), authHeader);
                stub = stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
            }

            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId(java.util.UUID.randomUUID().toString())
                    .setSerializedTask(ByteString.copyFromUtf8(taskData != null ? taskData : ""))
                    .setPriority(priority)
                    .build();

            TaskResponse response = stub.submitTask(request);
            String json = String.format(
                    "{\"taskId\": \"%s\", \"status\": \"%s\", \"message\": \"%s\"}",
                    response.getTaskId(),
                    response.getStatus().name(),
                    escapeJson(response.getMessage()));
            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            LOG.error("Error submitting task", e);
            sendJsonResponse(exchange, 500, "{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        try {
            String body = readBody(exchange);
            String username = extractJsonField(body, "username");
            String password = extractJsonField(body, "password");

            if (username == null || password == null) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Missing username or password\"}");
                return;
            }

            LoginRequest request = LoginRequest.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .build();

            AuthResponse response = authStub.login(request);
            String json;
            if (response.getSuccess()) {
                json = String.format(
                        "{\"success\": true, \"token\": \"%s\", \"message\": \"%s\"}",
                        response.getToken(),
                        escapeJson(response.getMessage()));
            } else {
                json = String.format(
                        "{\"success\": false, \"message\": \"%s\"}",
                        escapeJson(response.getMessage()));
            }
            sendJsonResponse(exchange, response.getSuccess() ? 200 : 401, json);
        } catch (Exception e) {
            LOG.error("Error during login", e);
            sendJsonResponse(exchange, 500, "{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        try {
            String body = readBody(exchange);
            String username = extractJsonField(body, "username");
            String password = extractJsonField(body, "password");
            String email = extractJsonField(body, "email");

            if (username == null || password == null) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Missing username or password\"}");
                return;
            }

            RegisterRequest request = RegisterRequest.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .setRole(email != null ? email : "SCIENTIST")
                    .build();

            AuthResponse response = authStub.register(request);
            String json;
            if (response.getSuccess()) {
                json = String.format(
                        "{\"success\": true, \"token\": \"%s\", \"message\": \"%s\"}",
                        response.getToken(),
                        escapeJson(response.getMessage()));
            } else {
                json = String.format(
                        "{\"success\": false, \"message\": \"%s\"}",
                        escapeJson(response.getMessage()));
            }
            sendJsonResponse(exchange, response.getSuccess() ? 201 : 400, json);
        } catch (Exception e) {
            LOG.error("Error during registration", e);
            sendJsonResponse(exchange, 500, "{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    // --- Helper methods ---

    private void sendJsonResponse(HttpExchange exchange, int code, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private String extractJsonField(String json, String field) {
        // Simple JSON field extraction (for production, use a proper library)
        String pattern = "\"" + field + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private String escapeJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        String grpcHost = "localhost";
        int grpcPort = 50051;

        if (args.length >= 1)
            port = Integer.parseInt(args[0]);
        if (args.length >= 2)
            grpcHost = args[1];
        if (args.length >= 3)
            grpcPort = Integer.parseInt(args[2]);

        RestGateway gateway = new RestGateway(port, grpcHost, grpcPort);
        gateway.start();

        // Keep running
        Runtime.getRuntime().addShutdownHook(new Thread(gateway::stop));
    }
}


