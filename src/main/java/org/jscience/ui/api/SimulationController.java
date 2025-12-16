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
 * @author Gemini AI
 * @since 4.0
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

        // Create a dummy payload
        String payload = "{\"time\": " + System.currentTimeMillis() + ", \"value\": " + Math.random() + "}";

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
