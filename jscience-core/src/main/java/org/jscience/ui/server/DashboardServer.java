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

package org.jscience.ui.server;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.jscience.ui.api.SimulationController;

/**
 * Embedded web server for the JScience Dashboard.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
        System.out.println(org.jscience.ui.i18n.I18n.getInstance().get("server.running.msg", port));
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


