import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import java.util.ServiceLoader;

public class ProviderAudit {
    public static void main(String[] args) {
        System.out.println("Episteme Linear Algebra Provider Audit");
        System.out.println("======================================");
        
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        int count = 0;
        for (LinearAlgebraProvider<?> provider : loader) {
            count++;
            System.out.println("[" + count + "] Name: " + provider.getName());
            System.out.println("    Class: " + provider.getClass().getName());
            System.out.println("    Available: " + provider.isAvailable());
            System.out.println("    Description: " + provider.getDescription());
            System.out.println("--------------------------------------");
        }
        
        if (count == 0) {
            System.out.println("NO PROVIDERS FOUND!");
        } else {
            System.out.println("Audit Complete. " + count + " providers found.");
        }
    }
}
