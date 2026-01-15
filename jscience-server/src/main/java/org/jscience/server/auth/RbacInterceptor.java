/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.server.auth;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * gRPC Server Interceptor for Role-Based Access Control (RBAC).
 * 
 * Intercepts all incoming calls and:
 * 1. Extracts JWT token from metadata
 * 2. Validates the token
 * 3. Checks if user role matches required roles
 * 4. Allows or denies the call
 */
public class RbacInterceptor implements ServerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RbacInterceptor.class);

    // Public methods that don't require authentication
    private final Set<String> publicMethods = new HashSet<>();

    // gRPC metadata and context keys
    private static final Metadata.Key<String> AUTHORIZATION_KEY = Metadata.Key.of("authorization",
            Metadata.ASCII_STRING_MARSHALLER);

    private static final Context.Key<String> USER_ID_KEY = Context.key("userId");
    private static final Context.Key<String> USER_ROLE_KEY = Context.key("userRole");

    /**
     * Add a method path to the public (no auth required) list.
     */
    public RbacInterceptor addPublicMethod(String methodPath) {
        publicMethods.add(methodPath);
        return this;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();

        // Skip authentication for public methods
        if (publicMethods.contains(methodName)) {
            LOG.debug("Public method accessed: {}", methodName);
            return next.startCall(call, headers);
        }

        // Extract token from metadata
        String authHeader = headers.get(AUTHORIZATION_KEY);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOG.warn("Missing or invalid authorization header for: {}", methodName);
            call.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid authorization token"), headers);
            return new ServerCall.Listener<ReqT>() {
            };
        }

        String token = authHeader.substring("Bearer ".length());

        // Validate token
        String userId = JWTUtil.validateAndGetUsername(token);
        if (userId == null) {
            LOG.warn("Invalid JWT token for: {}", methodName);
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid or expired token"), headers);
            return new ServerCall.Listener<ReqT>() {
            };
        }

        // Extract role from token
        String userRole = JWTUtil.getRole(token);

        LOG.debug("Authenticated user {} with role {} accessing {}", userId, userRole, methodName);

        // Attach user info to context for downstream use
        Context ctx = Context.current()
                .withValue(USER_ID_KEY, userId)
                .withValue(USER_ROLE_KEY, userRole);

        return Contexts.interceptCall(ctx, call, headers, next);
    }

    /**
     * Static utility to check if current context user has required role.
     * Call this from within gRPC service methods to check roles.
     */
    public static boolean hasRole(String... requiredRoles) {
        String userRole = USER_ROLE_KEY.get();
        if (userRole == null)
            return false;
        return Arrays.asList(requiredRoles).contains(userRole);
    }

    /**
     * Static utility to check role and throw exception if not authorized.
     */
    public static void requireRole(String... requiredRoles) throws StatusRuntimeException {
        if (!hasRole(requiredRoles)) {
            String userRole = USER_ROLE_KEY.get();
            LOG.warn("Access denied: user role {} not in required roles {}",
                    userRole, Arrays.toString(requiredRoles));
            throw Status.PERMISSION_DENIED
                    .withDescription("Access denied: insufficient permissions. Required roles: " +
                            Arrays.toString(requiredRoles))
                    .asRuntimeException();
        }
    }

    /**
     * Get current authenticated user ID from context.
     */
    public static String getCurrentUserId() {
        return USER_ID_KEY.get();
    }

    /**
     * Get current authenticated user's role from context.
     */
    public static String getCurrentUserRole() {
        return USER_ROLE_KEY.get();
    }
}
