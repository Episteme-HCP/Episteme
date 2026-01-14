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

package org.jscience.physics.quantum.backends;

import org.jscience.physics.quantum.QuantumBackend;
import org.jscience.physics.quantum.QuantumContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Quantum Backend that delegates to Qiskit (Python).
 */
public class QiskitQuantumProvider implements QuantumBackend {

    @Override
    public String getId() {
        return "qiskit";
    }

    @Override
    public String getName() {
        return "Qiskit (Python)";
    }

    @Override
    public String getDescription() {
        return "Executes quantum circuits via Python/Qiskit bridge.";
    }

    @Override
    public int getPriority() {
        return 100; // High priority if Python is available
    }

    @Override
    public boolean isAvailable() {
        try {
            Process p = new ProcessBuilder("python", "--version").start();
            return p.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Integer> execute(QuantumContext context) {
        Map<String, Integer> results = new HashMap<>();
        try {
            String qasm = context.toOpenQASM();
            
            // Create temp file for QASM
            Path qasmFile = Files.createTempFile("circuit", ".qasm");
            Files.write(qasmFile, qasm.getBytes());

            // Run python script
            // Assuming tools/qiskit_runner.py is relative to project root or classpath?
            // For now, let's assume it's in a known location or we pass strict path.
            // In dev environment: C:\Silvere\Encours\Developpement\JScience\tools\qiskit_runner.py
            File runnerScript = new File("tools/qiskit_runner.py");
            if (!runnerScript.exists()) {
                System.err.println("Qiskit runner script not found at " + runnerScript.getAbsolutePath());
                return results;
            }

            ProcessBuilder pb = new ProcessBuilder("python", runnerScript.getAbsolutePath(), qasmFile.toAbsolutePath().toString());
            pb.redirectErrorStream(true);
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            p.waitFor();

            // Parse simple JSON output: {"00": 512, "11": 512}
            String json = output.toString().trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                String content = json.substring(1, json.length() - 1);
                if (!content.isEmpty()) {
                    String[] pairs = content.split(",");
                    for (String pair : pairs) {
                        String[] kv = pair.split(":");
                        String key = kv[0].trim().replace("\"", "").replace("'", "");
                        int val = Integer.parseInt(kv[1].trim());
                        results.put(key, val);
                    }
                }
            } else {
                System.err.println("Qiskit Output Error: " + json);
            }

            // Cleanup
            Files.deleteIfExists(qasmFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
