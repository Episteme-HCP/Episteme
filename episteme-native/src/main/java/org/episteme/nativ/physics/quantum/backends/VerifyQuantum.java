package org.episteme.nativ.physics.quantum.backends;

public class VerifyQuantum {
    public static void main(String[] args) {
        NativeQuantumBackend backend = new NativeQuantumBackend();
        System.out.println("Backend Name: " + backend.getName());
        System.out.println("Is Available: " + backend.isAvailable());
        System.out.println("Status: " + backend.getStatusMessage());
    }
}


