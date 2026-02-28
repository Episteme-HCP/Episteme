import org.episteme.nativ.mathematics.linearalgebra.backends.NativeFFMBLASBackend;
import java.lang.reflect.Field;

public class TestBLAS {
    public static void main(String[] args) {
        System.out.println("Testing NativeFFMBLASBackend...");
        try {
            Class.forName("org.episteme.nativ.mathematics.linearalgebra.backends.NativeFFMBLASBackend");
            NativeFFMBLASBackend backend = new NativeFFMBLASBackend();
            System.out.println("Backend is available? " + backend.isAvailable());
            
            // If unavailable, we can't get the error directly because it's swallowed in the static block.
            // Let's manually run the code of the static block.
            System.out.println("\nRunning native loader manually:");
            java.lang.foreign.Arena arena = java.lang.foreign.Arena.global();
            java.util.Optional<java.lang.foreign.SymbolLookup> lib = org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader.loadLibrary("openblas", arena);
            if (lib.isEmpty()) {
                 lib = org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader.loadLibrary("mkl_rt", arena);
                 if (lib.isEmpty()) {
                     lib = org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader.getSystemLookup();
                 }
            }
            System.out.println("Lookup: " + (lib.isPresent() ? "Found" : "Not Found"));
            
            if (lib.isPresent()) {
                java.lang.foreign.SymbolLookup LOOKUP = lib.get();
                java.lang.foreign.Linker LINKER = org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader.getLinker();
                
                System.out.println("Trying cblas_dgemm:");
                System.out.println(LOOKUP.find("cblas_dgemm"));
                System.out.println("Trying cblas_ddot:");
                System.out.println(LOOKUP.find("cblas_ddot"));
                System.out.println("Trying cblas_dnrm2:");
                System.out.println(LOOKUP.find("cblas_dnrm2"));
                System.out.println("Trying cblas_daxpy:");
                System.out.println(LOOKUP.find("cblas_daxpy"));
                System.out.println("Trying cblas_dscal:");
                System.out.println(LOOKUP.find("cblas_dscal"));
                System.out.println("Trying cblas_dgemv:");
                System.out.println(LOOKUP.find("cblas_dgemv"));
                System.out.println("Trying cblas_domatcopy:");
                System.out.println(LOOKUP.find("cblas_domatcopy"));
                
                System.out.println("Trying LAPACKE_dgesv or dgesv:");
                System.out.println(LOOKUP.find("LAPACKE_dgesv").or(() -> LOOKUP.find("dgesv")));
                System.out.println("Trying LAPACKE_dgetrf or dgetrf:");
                System.out.println(LOOKUP.find("LAPACKE_dgetrf").or(() -> LOOKUP.find("dgetrf")));
                System.out.println("Trying LAPACKE_dgetri or dgetri:");
                System.out.println(LOOKUP.find("LAPACKE_dgetri").or(() -> LOOKUP.find("dgetri")));
                System.out.println("Trying LAPACKE_dgeqrf:");
                System.out.println(LOOKUP.find("LAPACKE_dgeqrf"));
                System.out.println("Trying LAPACKE_dorgqr:");
                System.out.println(LOOKUP.find("LAPACKE_dorgqr"));
                System.out.println("Trying LAPACKE_dgesvd:");
                System.out.println(LOOKUP.find("LAPACKE_dgesvd"));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
