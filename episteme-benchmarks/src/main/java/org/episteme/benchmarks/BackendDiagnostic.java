package org.episteme.benchmarks;

import org.episteme.core.media.vision.VisionBackendManager;
import org.episteme.core.technical.backend.Backend;
import org.episteme.natural.physics.quantum.QuantumBackendManager;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.backend.ComputeBackend;
import java.util.ServiceLoader;
import java.util.ServiceConfigurationError;
import java.util.Iterator;

public class BackendDiagnostic {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("      Episteme Backend Diagnostic tool");
        System.out.println("=================================================");

        System.out.println("\n--- Quantum Backends ---");
        try {
            QuantumBackendManager.staticAllBackends().forEach(b -> {
                checkAvailability(b.getName(), b::isAvailable);
            });
        } catch (ServiceConfigurationError e) {
            System.err.println("Critical failure loading Quantum backends: " + e.getMessage());
        }

        System.out.println("\n--- Vision Backends ---");
        try {
            VisionBackendManager.staticAllBackends().forEach(b -> {
                checkAvailability(b.getName(), b::isAvailable);
            });
        } catch (ServiceConfigurationError e) {
            System.err.println("Critical failure loading Vision backends: " + e.getMessage());
        }

        System.out.println("\n--- Linear Algebra Backends ---");
        safeIterate(ServiceLoader.load(LinearAlgebraProvider.class), b -> {
            checkAvailability(b.getName(), b::isAvailable);
        });

        System.out.println("\n--- Compute Backends (SPI) ---");
        safeIterate(ServiceLoader.load(ComputeBackend.class), b -> {
            if (b.getName().contains("Arrow") || b.getName().contains("ND4J") || b.getName().contains("FFM")) {
                checkAvailability(b.getName(), b::isAvailable);
            }
        });

        safeIterate(ServiceLoader.load(Backend.class), b -> {
            if (b.getName().contains("Arrow") || b.getName().contains("FFM")) {
                 checkAvailability(b.getName(), b::isAvailable);
            }
        });

        printNativeFailureDetails();
    }

    private static <T> void safeIterate(ServiceLoader<T> loader, java.util.function.Consumer<T> consumer) {
        Iterator<T> it = loader.iterator();
        while (true) {
            try {
                if (!it.hasNext()) break;
                T b = it.next();
                consumer.accept(b);
            } catch (ServiceConfigurationError e) {
                System.err.println("Error loading a provider: " + e.getMessage());
            } catch (Throwable t) {
                 System.err.println("Unexpected error during iteration: " + t.getMessage());
            }
        }
    }

    private static void checkAvailability(String name, java.util.function.BooleanSupplier supplier) {
        boolean available = false;
        String error = "";
        try {
            available = supplier.getAsBoolean();
        } catch (ExceptionInInitializerError e) {
            error = " [INIT ERROR: " + (e.getCause() != null ? e.getCause().toString() : e.toString()) + "]";
        } catch (LinkageError e) {
            error = " [LINKAGE ERROR: " + e.toString() + "]";
        } catch (Throwable t) {
            error = " [ERROR: " + t.getClass().getSimpleName() + ": " + t.getMessage() + "]";
        }
        System.out.printf("%-45s | Available: %-5b%s\n", name, available, error);
    }

    private static void printNativeFailureDetails() {
        System.out.println("\n--- Native Loading Failure Details ---");
        try {
            Class<?> loaderClass = Class.forName("org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader");
            @SuppressWarnings("unchecked")
            java.util.List<String> causes = (java.util.List<String>) loaderClass.getMethod("getAllFailureCauses").invoke(null);
            if (causes.isEmpty()) {
                System.out.println("No native loading failures recorded.");
            } else {
                causes.forEach(System.out::println);
            }
        } catch (Throwable t) {
            System.out.println("Could not retrieve native failure details: " + t.getMessage());
        }
    }
}
