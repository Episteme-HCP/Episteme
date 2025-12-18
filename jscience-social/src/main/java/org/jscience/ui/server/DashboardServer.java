package org.jscience.ui.server;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.jscience.ui.api.SimulationController;

/**
 * Embedded web server for the JScience Dashboard.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 4.0
 */
public class DashboardServer {

    private final Javalin app;
    private final SimulationController controller;

    public DashboardServer(int port) {
        this.controller = new SimulationController();

        this.app = Javalin.create(config -> {
            // Serve static files from resources/web
            config.staticFiles.add("/web", Location.CLASSPATH);
        });

        setupRoutes();

        app.start(port);
        System.out.println("JScience Dashboard running at http://localhost:" + port + "/web/index.html");
    }

    private void setupRoutes() {
        // API Routes
        app.get("/api/status", controller::getStatus);
        app.post("/api/simulation/start", controller::startSimulation);
        app.post("/api/simulation/stop", controller::stopSimulation);

        // WebSocket for real-time data streaming
        app.ws("/stream", ws -> {
            ws.onConnect(controller::onConnect);
            ws.onClose(controller::onClose);
            ws.onMessage(controller::onMessage);
        });
    }

    public void stop() {
        app.stop();
    }

    public static void main(String[] args) {
        new DashboardServer(8080);
    }
}
