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

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.jscience.server.proto.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for AuthService gRPC endpoints.
 * Tests user registration, login, and token validation.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceIT {

        private static Server server;
        private static ManagedChannel channel;
        private static AuthServiceGrpc.AuthServiceBlockingStub blockingStub;
        private static final int TEST_PORT = 50098;
        private static String validToken;

        @BeforeAll
        static void startServer() throws IOException {
                // Start embedded gRPC server with AuthService
                server = ServerBuilder.forPort(TEST_PORT)
                                .addService(new AuthServiceImpl(new org.jscience.server.repository.UserRepository()))
                                .build()
                                .start();

                // Create client channel
                channel = ManagedChannelBuilder.forAddress("localhost", TEST_PORT)
                                .usePlaintext()
                                .build();

                blockingStub = AuthServiceGrpc.newBlockingStub(channel);
        }

        @AfterAll
        static void stopServer() throws InterruptedException {
                if (channel != null) {
                        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                }
                if (server != null) {
                        server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                }
        }

        @Test
        @Order(1)
        @DisplayName("Should register new user successfully")
        void testRegisterUser() {
                // Given
                String uniqueUsername = "testuser_" + System.currentTimeMillis();
                RegisterRequest request = RegisterRequest.newBuilder()
                                .setUsername(uniqueUsername)
                                .setPassword("securePassword123")
                                .setRole("SCIENTIST")
                                .build();

                // When
                AuthResponse response = blockingStub.register(request);

                // Then
                assertNotNull(response);
                assertTrue(response.getSuccess());
                assertNotNull(response.getToken());
                assertFalse(response.getToken().isEmpty());
        }

        @Test
        @Order(2)
        @DisplayName("Should login with valid credentials")
        void testLoginSuccess() {
                // Given - First register a user
                String username = "logintest_" + System.currentTimeMillis();
                RegisterRequest registerRequest = RegisterRequest.newBuilder()
                                .setUsername(username)
                                .setPassword("password123")
                                .setRole("SCIENTIST")
                                .build();
                blockingStub.register(registerRequest);

                LoginRequest loginRequest = LoginRequest.newBuilder()
                                .setUsername(username)
                                .setPassword("password123")
                                .build();

                // When
                AuthResponse response = blockingStub.login(loginRequest);

                // Then
                assertNotNull(response);
                assertTrue(response.getSuccess());
                assertNotNull(response.getToken());
                assertFalse(response.getToken().isEmpty());

                // Save token for validation test
                validToken = response.getToken();
        }

        @Test
        @Order(3)
        @DisplayName("Should reject login with invalid password")
        void testLoginInvalidPassword() {
                // Given - First register a user
                String username = "badpasstest_" + System.currentTimeMillis();
                RegisterRequest registerRequest = RegisterRequest.newBuilder()
                                .setUsername(username)
                                .setPassword("correctpassword")
                                .setRole("VIEWER")
                                .build();
                blockingStub.register(registerRequest);

                LoginRequest loginRequest = LoginRequest.newBuilder()
                                .setUsername(username)
                                .setPassword("wrongpassword")
                                .build();

                // When
                AuthResponse response = blockingStub.login(loginRequest);

                // Then
                assertNotNull(response);
                assertFalse(response.getSuccess());
        }

        @Test
        @Order(4)
        @DisplayName("Should validate valid JWT token")
        void testValidateValidToken() {
                // Given - First get a valid token
                String username = "validatetest_" + System.currentTimeMillis();
                RegisterRequest registerRequest = RegisterRequest.newBuilder()
                                .setUsername(username)
                                .setPassword("password123")
                                .setRole("ADMIN")
                                .build();
                AuthResponse registerResponse = blockingStub.register(registerRequest);
                String token = registerResponse.getToken();

                TokenRequest tokenRequest = TokenRequest.newBuilder()
                                .setToken(token)
                                .build();

                // When
                ValidationResponse response = blockingStub.validateToken(tokenRequest);

                // Then
                assertNotNull(response);
                assertTrue(response.getValid());
                assertEquals(username, response.getUsername());
                assertEquals("ADMIN", response.getRole());
        }

        @Test
        @Order(5)
        @DisplayName("Should reject invalid JWT token")
        void testValidateInvalidToken() {
                // Given
                TokenRequest tokenRequest = TokenRequest.newBuilder()
                                .setToken("invalid.jwt.token")
                                .build();

                // When
                ValidationResponse response = blockingStub.validateToken(tokenRequest);

                // Then
                assertNotNull(response);
                assertFalse(response.getValid());
        }

        @Test
        @Order(6)
        @DisplayName("Should register user with ADMIN role")
        void testRegisterAdminUser() {
                // Given
                String username = "admin_" + System.currentTimeMillis();
                RegisterRequest request = RegisterRequest.newBuilder()
                                .setUsername(username)
                                .setPassword("adminPassword!")
                                .setRole("ADMIN")
                                .build();

                // When
                AuthResponse response = blockingStub.register(request);

                // Then
                assertTrue(response.getSuccess());

                // Verify role in token
                TokenRequest tokenRequest = TokenRequest.newBuilder()
                                .setToken(response.getToken())
                                .build();
                ValidationResponse validation = blockingStub.validateToken(tokenRequest);
                assertEquals("ADMIN", validation.getRole());
        }

        @Test
        @Order(7)
        @DisplayName("Should prevent duplicate username registration")
        void testDuplicateUsername() {
                // Given
                String username = "duplicate_" + System.currentTimeMillis();
                RegisterRequest request = RegisterRequest.newBuilder()
                                .setUsername(username)
                                .setPassword("password")
                                .setRole("VIEWER")
                                .build();

                // First registration should succeed
                AuthResponse first = blockingStub.register(request);
                assertTrue(first.getSuccess());

                // When - Try to register with same username
                AuthResponse second = blockingStub.register(request);

                // Then - Second should fail
                assertFalse(second.getSuccess());
        }
}


