package org.episteme.benchmarks.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Utility to programmatically start and stop Episteme server and worker processes.
 */
public class ServerOrchestrator {
    private final List<Process> processes = new ArrayList<>();
    private final String javaPath = System.getProperty("java.home") + "/bin/java";

    public void startServer() throws IOException {
        System.out.println("[Orchestrator] Starting Episteme Server...");
        ProcessBuilder pb = new ProcessBuilder(
            javaPath,
            "--enable-preview",
            "-cp", System.getProperty("java.class.path"),
            "org.episteme.core.mathematics.linearalgebra.GrpcTestApplication"
        );
        pb.redirectErrorStream(true);
        Process p = pb.start();
        processes.add(p);
        
        // Wait for server to be ready (simplified)
        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void stopAll() {
        System.out.println("[Orchestrator] Stopping all processes...");
        for (Process p : processes) {
            p.destroy();
            try {
                if (!p.waitFor(5, TimeUnit.SECONDS)) {
                    p.destroyForcibly();
                }
            } catch (InterruptedException e) {
                p.destroyForcibly();
            }
        }
        processes.clear();
    }
}
