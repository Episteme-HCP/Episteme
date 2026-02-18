/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.examples.physics;

import org.jscience.nativ.physics.NativePhysicsProvider;

/**
 * Demonstrates usage of the Native Physics Provider (Bullet3 / Jolt).
 * <p>
 * This example shows how to handle optional native dependencies gracefully.
 * </p>
 */
public class PhysicsSimulationExample {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   JScience Native Physics Simulation");
        System.out.println("==========================================");

        // 1. Instantiate Provider
        NativePhysicsProvider physics = new NativePhysicsProvider();
        
        System.out.println("Initializing Physics Engine: " + physics.getName());

        // 2. Check Availability
        if (!physics.isAvailable()) {
            System.err.println("\n[ERROR] Native Physics Engine NOT available.");
            System.err.println("Reason: Native libraries (Bullet3C.dll or JoltC.dll) not found in libs/ directory.");
            System.err.println("Action: Please download Bullet3 or Jolt Physics C-API wrappers and place them in:");
            System.err.println("        " + System.getProperty("user.dir") + "\\libs\\");
            return;
        }

        // 3. Initialize Simulation
        try {
            physics.init();
            System.out.println("Engine Initialized Successfully.");
            
            // 4. Run Simulation Loop
            System.out.println("Starting Simulation Loop (100 steps)...");
            long start = System.nanoTime();
            
            float deltaTime = 1.0f / 60.0f;
            for (int i = 0; i < 100; i++) {
                physics.step(deltaTime);
                if (i % 10 == 0) {
                    System.out.print(".");
                }
            }
            long end = System.nanoTime();
            
            System.out.println("\nSimulation Complete.");
            System.out.println("Time: " + (end - start) / 1e6 + " ms");
            
        } catch (Exception e) {
            System.err.println("Simulation failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            physics.close();
        }
    }
}
