import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import java.util.ServiceLoader;

public class DebugDiscovery {
    public static void main(String[] args) {
        System.out.println("Starting ServiceLoader discovery...");
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider prov : loader) {
            System.out.println("Found provider: " + prov.getName() + " [" + prov.getClass().getName() + "]");
        }
        
        System.out.println("\nTrying Class.forName on CUDA backend...");
        try {
            Class<?> cls = Class.forName("org.episteme.nativ.mathematics.linearalgebra.backends.NativeCUDADenseLinearAlgebraDoubleBackend");
            System.out.println("Class loaded successfully: " + cls.getName());
            Object inst = cls.getConstructor().newInstance();
            System.out.println("Instance created successfully: " + inst);
        } catch (Throwable t) {
            System.out.println("FAILED to load/instantiate CUDA backend:");
            t.printStackTrace();
        }
    }
}
