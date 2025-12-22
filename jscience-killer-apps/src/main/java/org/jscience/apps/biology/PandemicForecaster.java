/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.biology;

import org.jscience.biology.ecology.PopulationDynamics;
import org.jscience.geography.loaders.WorldBankLoader;
import org.jscience.geography.Region;
import org.jscience.mathematics.numbers.real.Real;

import java.util.List;
import java.util.Scanner;

/**
 * Killer App: Pandemic Forecaster.
 * <p>
 * Simulates the spread of an infectious disease across a country using the SEIR model.
 * Uses real population data from the WorldBankLoader.
 * </p>
 */
public class PandemicForecaster {

    public static void main(String[] args) {
        System.out.println("=== JScience Pandemic Forecaster ===");
        System.out.println("Loading World Data...");

        try {
            // Load Data
            WorldBankLoader loader = WorldBankLoader.getInstance();
            // Pre-load some dummy data if file is empty/missing
            // In a real scenario, this would load from the JSON resource we haven't created yet
            // So we'll mock if empty, or rely on what's there.
            List<Region> regions = loader.loadAll();
            
            if (regions.isEmpty()) {
                System.out.println("No data found. creating mock data for simulation.");
                Region mock = new Region("MockCountry", Region.Type.COUNTRY);
                mock.setPopulation(10_000_000);
                regions.add(mock);
            }

            // User Selection
            System.out.println("Available Regions: " + regions.size());
            // In a real GUI/Terminal app we would ask user. For now, pick the first or 'France'.
            Region target = regions.stream()
                    .filter(r -> "France".equalsIgnoreCase(r.getName()))
                    .findFirst()
                    .orElse(regions.get(0));

            System.out.println("Simulating outbreak in: " + target.getName());
            System.out.println("Population: " + target.getPopulation());

            // Simulation Parameters
            long N = target.getPopulation();
            if (N <= 0) N = 1000; // avoid div by zero

            // COVID-19 like parameters (roughly)
            // R0 approx 2.5 - 3.0
            // Incubation period ~5 days (sigma = 1/5)
            // Infectious period ~10 days (gamma = 1/10)
            
            Real beta = Real.of(0.3); // Transmission rate (R0 * gamma) -> 3.0 * 0.1 = 0.3
            Real sigma = Real.of(1.0/5.0); // 1/incubation
            Real gamma = Real.of(1.0/10.0); // 1/recovery
            Real dt = Real.of(1.0); // 1 day step
            int days = 100;

            // Initial State
            // 100 infected, 0 exposed, rest susceptible
            Real[] initialPop = new Real[] {
                Real.of(N - 100), // S
                Real.of(0),        // E
                Real.of(100),      // I
                Real.of(0)         // R
            };

            System.out.println("\nRunning SEIR Simulation for " + days + " days...");
            Real[][] results = PopulationDynamics.seirModel(initialPop, beta, sigma, gamma, dt, days);

            // Output Results
            System.out.printf("%-5s %-12s %-12s %-12s %-12s%n", "Day", "Susceptible", "Exposed", "Infectious", "Recovered");
            for (int t = 0; t < days; t++) {
                 // Sample every 5 days to keep output clean
                 if (t % 5 == 0) {
                     long s = Math.round(results[t][0].doubleValue());
                     long e = Math.round(results[t][1].doubleValue());
                     long i = Math.round(results[t][2].doubleValue());
                     long r = Math.round(results[t][3].doubleValue());
                     System.out.printf("%-5d %-12d %-12d %-12d %-12d%n", t, s, e, i, r);
                 }
            }
            
            System.out.println("Simulation Complete.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
