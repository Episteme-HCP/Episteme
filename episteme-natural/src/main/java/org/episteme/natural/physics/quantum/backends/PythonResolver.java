/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.natural.physics.quantum.backends;

/**
 * Utility for resolving the correct Python executable across different platforms.
 */
public class PythonResolver {
    public static String resolve() {
        if (System.getenv("EPISTEME_PYTHON") != null) return System.getenv("EPISTEME_PYTHON");
        
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // Try 'py' launcher first on Windows
            try {
                if (new ProcessBuilder("py", "--version").start().waitFor() == 0) return "py";
            } catch (Exception ignored) {}
        }
        return "python";
    }
}
