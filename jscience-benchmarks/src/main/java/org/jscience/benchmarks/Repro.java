package org.episteme.benchmarks;

import java.util.ServiceLoader;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

public class Repro {
    public static void main(String[] args) {
        System.out.println("Starting ServiceLoader check...");
        ServiceLoader<AlgorithmProvider> loader = ServiceLoader.load(AlgorithmProvider.class);
        int count = 0;
        for (AlgorithmProvider p : loader) {
            System.out.println(" - " + p.getClass().getName() + " [Available: " + p.isAvailable() + "]");
            count++;
        }
        System.out.println("Total providers found: " + count);
    }
}
