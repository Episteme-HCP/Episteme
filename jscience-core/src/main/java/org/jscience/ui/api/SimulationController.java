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

package org.jscience.ui.api;

import io.javalin.http.Context;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsMessageContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for handling simulation API requests and WebSocket streaming.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulationController {

    private final Set<WsConnectContext> clients = ConcurrentHashMap.newKeySet();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public SimulationController() {
        // Start a dummy simulation loop for MVP
        scheduler.scheduleAtFixedRate(this::broadcastUpdate, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void getStatus(Context ctx) {
        ctx.json(new StatusResponse(isRunning.get(), clients.size()));
    }

    public void startSimulation(Context ctx) {
        isRunning.set(true);
        ctx.result("Simulation started");
    }

    public void stopSimulation(Context ctx) {
        isRunning.set(false);
        ctx.result("Simulation stopped");
    }

    // --- WebSocket Handlers ---

    public void onConnect(WsConnectContext ctx) {
        clients.add(ctx);
        System.out.println("Client connected: " + ctx.sessionId());
    }

    public void onClose(WsCloseContext ctx) {
        clients.removeIf(client -> client.sessionId().equals(ctx.sessionId()));
        System.out.println("Client disconnected: " + ctx.sessionId());
    }

    public void onMessage(WsMessageContext ctx) {
        // Handle incoming messages from client if needed (e.g. interactive controls)
    }

    // --- Internal Logic ---

    private void broadcastUpdate() {
        if (!isRunning.get())
            return;

        // Generate synthetic but realistic metrics
        double value = 0.5 + 0.4 * Math.sin(System.currentTimeMillis() / 1000.0);
        double load = 15.0 + 10.0 * Math.random();
        int rate = 100 + (int) (20 * Math.random());

        String payload = String.format(
                "{\"time\": %d, \"value\": %.4f, \"load\": %.1f, \"rate\": %d}",
                System.currentTimeMillis(), value, load, rate);

        for (WsConnectContext client : clients) {
            if (client.session.isOpen()) {
                client.send(payload);
            }
        }
    }

    // DTO
    record StatusResponse(boolean running, int connectedClients) {
    }
}


