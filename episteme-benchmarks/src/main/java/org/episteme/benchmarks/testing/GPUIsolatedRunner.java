/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility to run GPU benchmarks/tests in an isolated process.
 * Simulates Docker-like isolation by controlling environment variables
 * and JVM arguments.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class GPUIsolatedRunner {
    private static final Logger logger = LoggerFactory.getLogger(GPUIsolatedRunner.class);

    public static int runIsolated(Class<?> mainClass, Map<String, String> envOverrides, String... jvmArgs) {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        for (String arg : jvmArgs) {
            command.add(arg);
        }
        command.add("-cp");
        command.add(classpath);
        command.add(mainClass.getName());

        ProcessBuilder builder = new ProcessBuilder(command);
        Map<String, String> processEnv = builder.environment();
        processEnv.putAll(envOverrides);

        logger.info("Starting isolated GPU process for {}: {}", mainClass.getSimpleName(), String.join(" ", command));

        try {
            Process process = builder.start();
            
            // Redirect output to logger
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info("[ISOLATED] {}", line);
                    }
                } catch (Exception e) {
                    logger.error("Error reading isolated process stdout", e);
                }
            }).start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.error("[ISOLATED-ERR] {}", line);
                    }
                } catch (Exception e) {
                    logger.error("Error reading isolated process stderr", e);
                }
            }).start();

            return process.waitFor();
        } catch (Exception e) {
            logger.error("Failed to launch isolated GPU process", e);
            return -1;
        }
    }
}
