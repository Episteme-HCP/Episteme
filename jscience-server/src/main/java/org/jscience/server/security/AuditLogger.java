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

package org.jscience.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * Audit Logger for security and compliance.
 * 
 * Logs:
 * - Authentication events
 * - Authorization decisions
 * - Task submissions
 * - Administrative actions
 */
public class AuditLogger {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogger.class);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    /**
     * Audit event types.
     */
    public enum EventType {
        AUTH_LOGIN_SUCCESS,
        AUTH_LOGIN_FAILURE,
        AUTH_LOGOUT,
        AUTH_TOKEN_EXPIRED,
        AUTHZ_ACCESS_GRANTED,
        AUTHZ_ACCESS_DENIED,
        TASK_SUBMITTED,
        TASK_COMPLETED,
        TASK_FAILED,
        WORKER_REGISTERED,
        WORKER_DISCONNECTED,
        ADMIN_CONFIG_CHANGE,
        ADMIN_USER_CREATED,
        ADMIN_USER_DELETED,
        SECURITY_RATE_LIMITED,
        SECURITY_SUSPICIOUS_ACTIVITY
    }

    /**
     * Audit event record.
     */
    public record AuditEvent(
            Instant timestamp,
            EventType type,
            String userId,
            String remoteAddress,
            String resource,
            String action,
            boolean success,
            String details) {
        public String toJson() {
            return String.format(
                    "{\"timestamp\":\"%s\",\"type\":\"%s\",\"userId\":\"%s\"," +
                            "\"remoteAddress\":\"%s\",\"resource\":\"%s\",\"action\":\"%s\"," +
                            "\"success\":%s,\"details\":\"%s\"}",
                    ISO_FORMATTER.format(timestamp),
                    type.name(),
                    userId != null ? userId : "anonymous",
                    remoteAddress != null ? remoteAddress : "unknown",
                    resource != null ? resource : "",
                    action != null ? action : "",
                    success,
                    details != null ? details.replace("\"", "\\\"") : "");
        }
    }

    private final BlockingQueue<AuditEvent> eventQueue;
    private final ExecutorService writerExecutor;
    private final Path logFile;
    private PrintWriter writer;
    private volatile boolean running = true;

    /**
     * Create audit logger with default log file.
     */
    public AuditLogger() {
        this(Paths.get("logs", "audit.log"));
    }

    /**
     * Create audit logger with custom log file.
     */
    public AuditLogger(Path logFile) {
        this.logFile = logFile;
        this.eventQueue = new LinkedBlockingQueue<>(10000);
        this.writerExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "audit-writer");
            t.setDaemon(true);
            return t;
        });

        initialize();
    }

    private void initialize() {
        try {
            Files.createDirectories(logFile.getParent());
            writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(logFile.toFile(), true)));
            LOG.info("Ã°Å¸â€œâ€¹ Audit logger initialized: {}", logFile);

            // Start async writer
            writerExecutor.submit(this::processEvents);
        } catch (IOException e) {
            LOG.error("Failed to initialize audit logger", e);
        }
    }

    private void processEvents() {
        while (running) {
            try {
                AuditEvent event = eventQueue.poll(1, TimeUnit.SECONDS);
                if (event != null) {
                    writeEvent(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Drain remaining events
        AuditEvent event;
        while ((event = eventQueue.poll()) != null) {
            writeEvent(event);
        }

        if (writer != null) {
            writer.close();
        }
    }

    private void writeEvent(AuditEvent event) {
        if (writer != null) {
            writer.println(event.toJson());
            writer.flush();
        }
    }

    /**
     * Log an audit event.
     */
    public void log(AuditEvent event) {
        if (!eventQueue.offer(event)) {
            LOG.warn("Audit queue full, event dropped: {}", event.type());
        }
    }

    /**
     * Log authentication success.
     */
    public void logLoginSuccess(String userId, String remoteAddress) {
        log(new AuditEvent(Instant.now(), EventType.AUTH_LOGIN_SUCCESS,
                userId, remoteAddress, "auth", "login", true, null));
    }

    /**
     * Log authentication failure.
     */
    public void logLoginFailure(String username, String remoteAddress, String reason) {
        log(new AuditEvent(Instant.now(), EventType.AUTH_LOGIN_FAILURE,
                username, remoteAddress, "auth", "login", false, reason));
    }

    /**
     * Log access granted.
     */
    public void logAccessGranted(String userId, String resource, String action) {
        log(new AuditEvent(Instant.now(), EventType.AUTHZ_ACCESS_GRANTED,
                userId, null, resource, action, true, null));
    }

    /**
     * Log access denied.
     */
    public void logAccessDenied(String userId, String resource, String action, String reason) {
        log(new AuditEvent(Instant.now(), EventType.AUTHZ_ACCESS_DENIED,
                userId, null, resource, action, false, reason));
    }

    /**
     * Log task submission.
     */
    public void logTaskSubmitted(String userId, String taskId) {
        log(new AuditEvent(Instant.now(), EventType.TASK_SUBMITTED,
                userId, null, "task:" + taskId, "submit", true, null));
    }

    /**
     * Log rate limiting event.
     */
    public void logRateLimited(String userId, String remoteAddress, String method) {
        log(new AuditEvent(Instant.now(), EventType.SECURITY_RATE_LIMITED,
                userId, remoteAddress, method, "call", false, "Rate limit exceeded"));
    }

    /**
     * Log suspicious activity.
     */
    public void logSuspiciousActivity(String userId, String remoteAddress, String details) {
        log(new AuditEvent(Instant.now(), EventType.SECURITY_SUSPICIOUS_ACTIVITY,
                userId, remoteAddress, "security", "alert", false, details));
    }

    /**
     * Shutdown the audit logger.
     */
    public void shutdown() {
        running = false;
        writerExecutor.shutdown();
        try {
            writerExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOG.info("Audit logger shutdown");
    }

    /**
     * Get queue size for monitoring.
     */
    public int getQueueSize() {
        return eventQueue.size();
    }
}


