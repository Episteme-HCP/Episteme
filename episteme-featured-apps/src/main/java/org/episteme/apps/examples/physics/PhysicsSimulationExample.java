/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.apps.examples.physics;
 
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.units.SI;
import org.episteme.natural.physics.classical.mechanics.MechanicsFactory;
import org.episteme.natural.physics.classical.mechanics.PhysicsWorldBridge;
 
/**
 * Demonstrates usage of the standardized Physics Simulation API.
 * <p>
 * This example shows how to use the MechanicsFactory to create a physics world
 * and run a simulation loop.
 * </p>
 */
public class PhysicsSimulationExample {
 
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      Episteme Physics Simulation");
        System.out.println("==========================================");
 
        // 1. Create a World via Factory (will pick the best available backend)
        PhysicsWorldBridge world = MechanicsFactory.createWorld();
        
        if (world == null) {
            System.err.println("\n[ERROR] No physics backend available.");
            return;
        }
 
        System.out.println("Physics World Initialized.");
 
        try {
            // 2. Run Simulation Loop (100 steps)
            System.out.println("Starting Simulation Loop (100 steps)...");
            long start = System.nanoTime();
            
            var timeStep = Quantities.create(1.0/60.0, SI.SECOND);
            for (int i = 0; i < 100; i++) {
                world.stepSimulation(timeStep);
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
        }
    }
}
