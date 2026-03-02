package org.episteme.benchmarks;

import org.episteme.natural.physics.quantum.backends.StrangeBackend;

public class ReproStrange {
    public static void main(String[] args) {
        System.out.println("Checking StrangeBackend...");
        try {
            StrangeBackend b = new StrangeBackend();
            System.out.println("Available: " + b.isAvailable());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
