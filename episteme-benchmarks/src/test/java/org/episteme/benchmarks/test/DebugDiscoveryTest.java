package org.episteme.benchmarks.test;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import java.util.ServiceLoader;
import org.junit.jupiter.api.Test;

public class DebugDiscoveryTest {
    @Test
    @SuppressWarnings("rawtypes")
    public void testDiscovery() {
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter("debug_discovery.log"))) {
            out.println("Starting ServiceLoader discovery...");
            ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
            for (LinearAlgebraProvider prov : loader) {
                out.println("Found provider: " + prov.getName() + " [" + prov.getClass().getName() + "]");
            }
            
            out.println("\nTrying Class.forName on CUDA backend...");
            try {
                Class<?> cls = Class.forName("org.episteme.nativ.mathematics.linearalgebra.backends.NativeCUDADenseLinearAlgebraDoubleBackend");
                out.println("Class loaded successfully: " + cls.getName());
                Object inst = cls.getConstructor().newInstance();
                out.println("Instance created successfully: " + inst);
            } catch (Throwable t) {
                out.println("FAILED to load/instantiate CUDA backend:");
                t.printStackTrace(out);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
