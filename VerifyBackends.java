
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;
import java.util.ServiceLoader;
import java.util.Iterator;

public class VerifyBackends {
    public static void main(String[] args) {
        System.out.println("=== AlgorithmProvider Discovery ===");
        ServiceLoader<AlgorithmProvider> loader = ServiceLoader.load(AlgorithmProvider.class);
        Iterator<AlgorithmProvider> it = loader.iterator();
        while (it.hasNext()) {
            try {
                AlgorithmProvider p = it.next();
                System.out.printf("[%s] %s (Type: %s, Available: %b, Priority: %d)%n",
                    p.getClass().getSimpleName(),
                    p.getName(),
                    p.getAlgorithmType(),
                    p.isAvailable(),
                    p.getPriority()
                );
            } catch (Throwable t) {
                System.out.println("Error loading a provider: " + t.getMessage());
                t.printStackTrace();
            }
        }

        System.out.println("\n=== Backend Discovery ===");
        ServiceLoader<Backend> bLoader = ServiceLoader.load(Backend.class);
        Iterator<Backend> bIt = bLoader.iterator();
        while (bIt.hasNext()) {
            try {
                Backend b = bIt.next();
                System.out.printf("[%s] %s (Available: %b)%n",
                    b.getClass().getSimpleName(),
                    b.getName(),
                    b.isAvailable()
                );
            } catch (Throwable t) {
                System.out.println("Error loading a backend: " + t.getMessage());
                t.printStackTrace();
            }
        }
    }
}
