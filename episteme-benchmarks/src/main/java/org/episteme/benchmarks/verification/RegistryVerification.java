package org.episteme.benchmarks.verification;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;
import java.util.ServiceLoader;

/**
 * Verification utility to print all discovered services.
 */
public class RegistryVerification {
    public static void main(String[] args) {
        System.out.println("=== Registry Verification ===");

        System.out.println("\n--- Backends ---");
        ServiceLoader<Backend> backends = ServiceLoader.load(Backend.class);
        int bCount = 0;
        for (Backend b : backends) {
            System.out.println("Found Backend: " + b.getClass().getName() + " [" + b.getName() + "]");
            bCount++;
        }
        System.out.println("Total Backends: " + bCount);

        System.out.println("\n--- Algorithm Providers ---");
        ServiceLoader<AlgorithmProvider> providers = ServiceLoader.load(AlgorithmProvider.class);
        int pCount = 0;
        for (AlgorithmProvider p : providers) {
            System.out.println("Found Provider: " + p.getClass().getName() + " [" + p.getName() + "]");
            pCount++;
        }
        System.out.println("Total AlgorithmProviders: " + pCount);
        
        System.out.println("\n=== verification complete ===");
    }
}
