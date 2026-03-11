package org.episteme.benchmarks;

import org.episteme.core.media.VisionBackendManager;
import org.episteme.core.media.VideoBackendManager;
import org.episteme.core.media.AudioBackendManager;
import org.episteme.core.mathematics.linearalgebra.tensors.TensorBackendManager;
import org.episteme.natural.physics.classical.mechanics.MechanicsBackendManager;
import org.episteme.natural.ui.viewers.chemistry.MolecularBackendManager;
import org.episteme.core.technical.backend.Backend;
import org.episteme.natural.physics.quantum.QuantumBackendManager;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.backend.ComputeBackend;
import java.util.ServiceLoader;
import java.util.ServiceConfigurationError;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collection;

public class BackendDiagnostic {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("      Episteme Backend Diagnostic tool");
        System.out.println("=================================================");

        AtomicInteger quantumCount = new AtomicInteger(0);
        System.out.println("\n--- Quantum Backends ---");
        try {
            QuantumBackendManager.staticAllBackends().forEach(b -> {
                checkAvailability(b.getName(), b);
                quantumCount.incrementAndGet();
            });
        } catch (ServiceConfigurationError | NoClassDefFoundError e) {
            System.err.println("Critical failure loading Quantum backends: " + e.getMessage());
        }
        System.out.println("Total Quantum Backends found: " + quantumCount.get());

        AtomicInteger visionCount = new AtomicInteger(0);
        System.out.println("\n--- Vision Backends ---");
        try {
            VisionBackendManager.staticAllBackends().forEach(b -> {
                checkAvailability(b.getName(), b);
                visionCount.incrementAndGet();
            });
        } catch (ServiceConfigurationError | NoClassDefFoundError e) {
            System.err.println("Critical failure loading Vision backends: " + e.getMessage());
        }
        System.out.println("Total Vision Backends found: " + visionCount.get());

        AtomicInteger linearAlgebraCount = new AtomicInteger(0);
        System.out.println("\n--- Linear Algebra Backends ---");
        safeIterate(ServiceLoader.load(LinearAlgebraProvider.class), b -> {
            if (b instanceof Backend) {
                checkAvailability(b.getName(), (Backend) b);
                linearAlgebraCount.incrementAndGet();
            }
        });
        System.out.println("Total Linear Algebra Backends found: " + linearAlgebraCount.get());

        AtomicInteger tensorCount = new AtomicInteger(0);
        System.out.println("\n--- Tensor Backends ---");
        try {
            TensorBackendManager.staticAll().forEach(b -> {
                checkAvailability(b.getName(), b);
                tensorCount.incrementAndGet();
            });
        } catch (Throwable t) { System.err.println("Failure loading Tensor backends: " + t.getMessage()); }
        System.out.println("Total Tensor Backends found: " + tensorCount.get());

        AtomicInteger audioCount = new AtomicInteger(0);
        System.out.println("\n--- Audio Backends ---");
        try {
            AudioBackendManager.staticAll().forEach(b -> {
                checkAvailability(b.getBackendName(), b);
                audioCount.incrementAndGet();
            });
        } catch (Throwable t) { System.err.println("Failure loading Audio backends: " + t.getMessage()); }
        System.out.println("Total Audio Backends found: " + audioCount.get());

        AtomicInteger videoCount = new AtomicInteger(0);
        System.out.println("\n--- Video Backends ---");
        try {
            VideoBackendManager.staticAll().forEach(b -> {
                checkAvailability(b.getBackendName(), b);
                videoCount.incrementAndGet();
            });
        } catch (Throwable t) { System.err.println("Failure loading Video backends: " + t.getMessage()); }
        System.out.println("Total Video Backends found: " + videoCount.get());

        AtomicInteger mechanicsCount = new AtomicInteger(0);
        System.out.println("\n--- Mechanics/Physics Backends ---");
        try {
            MechanicsBackendManager.staticAllBackends().forEach(b -> {
                checkAvailability(b.getName(), b);
                mechanicsCount.incrementAndGet();
            });
        } catch (Throwable t) { System.err.println("Failure loading Mechanics backends: " + t.getMessage()); }
        System.out.println("Total Mechanics Backends found: " + mechanicsCount.get());

        AtomicInteger molecularCount = new AtomicInteger(0);
        System.out.println("\n--- Molecular Backends ---");
        try {
            MolecularBackendManager.staticAllBackends().forEach(b -> {
                checkAvailability(b.getName(), b);
                molecularCount.incrementAndGet();
            });
        } catch (Throwable t) { System.err.println("Failure loading Molecular backends: " + t.getMessage()); }
        System.out.println("Total Molecular Backends found: " + molecularCount.get());

        AtomicInteger computeCount = new AtomicInteger(0);
        System.out.println("\n--- Compute Backends (SPI) ---");
        safeIterate(ServiceLoader.load(ComputeBackend.class), b -> {
            checkAvailability(b.getName(), b);
            computeCount.incrementAndGet();
        });
        System.out.println("Total Compute Backends found: " + computeCount.get());

        printNativeFailureDetails();
    }

    private static <T> void safeIterate(ServiceLoader<T> loader, java.util.function.Consumer<T> consumer) {
        Iterator<T> it = loader.iterator();
        while (true) {
            try {
                if (!it.hasNext()) break;
                T b = it.next();
                consumer.accept(b);
            } catch (ServiceConfigurationError | NoClassDefFoundError e) {
                System.err.println("Error loading a provider: " + e.getMessage());
                // e.printStackTrace(); 
            } catch (Throwable t) {
                 System.err.println("Unexpected error during iteration: " + t.getMessage());
                 // t.printStackTrace();
            }
        }
    }

    private static void checkAvailability(String name, Backend backend) {
        boolean available = false;
        String status = "";
        String error = "";
        try {
            available = backend.isAvailable();
            status = backend.getStatusMessage();
            if (!available) {
                // Try to guess why it's not available
                if (status == null || status.isEmpty() || status.equalsIgnoreCase("Unavailable")) {
                    status = "Not Supported or Missing Requirements";
                }
            }
        } catch (ExceptionInInitializerError e) {
            error = " [INIT ERROR: " + (e.getCause() != null ? e.getCause().toString() : e.toString()) + "]";
        } catch (NoClassDefFoundError e) {
             error = " [CLASS ERROR: Missing dependency: " + e.getMessage() + "]";
        } catch (LinkageError e) {
            error = " [LINKAGE ERROR: Missing native library or ABI mismatch: " + e.getMessage() + "]";
        } catch (Throwable t) {
            error = " [ERROR: " + t.getClass().getSimpleName() + ": " + t.getMessage() + "]";
        }
        System.out.printf("%-45s | Available: %-5b | Status: %-40s%s\n", name, available, status, error);
    }

    private static void printNativeFailureDetails() {
        System.out.println("\n--- Native Loading Failure Details ---");
        try {
            Class<?> loaderClass = Class.forName("org.episteme.core.technical.backend.nativ.NativeLibraryLoader");
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
