package org.episteme.benchmarks.util;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.ServiceLoader;

public class ProviderDiscoveryVerification {
    public static void main(String[] args) {
        System.out.println("=== Episteme Algorithm Provider Discovery Verification ===");
        ServiceLoader<AlgorithmProvider> loader = ServiceLoader.load(AlgorithmProvider.class);
        int count = 0;
        for (AlgorithmProvider p : loader) {
            count++;
            System.out.println(String.format("[%d] %s: %s (Available: %b)", 
                count, p.getAlgorithmType(), p.getName(), p.isAvailable()));
        }
        System.out.println("Total providers discovered: " + count);
        System.out.println("==========================================================");
    }
}
