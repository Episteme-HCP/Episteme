import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;
import java.lang.foreign.Arena;

public class CheckSingleLib {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java CheckSingleLib <libname>");
            return;
        }
        String lib = args[0];
        System.out.println("--- Checking library: " + lib + " ---");
        try {
            boolean found = NativeLibraryLoader.loadLibrary(lib, Arena.global()).isPresent();
            System.out.println("Result for " + lib + ": " + (found ? "SUCCESS" : "FAILED"));
        } catch (Throwable t) {
            System.out.println("CRITICAL ERROR for " + lib + ": " + t.getMessage());
            t.printStackTrace();
        }
    }
}
