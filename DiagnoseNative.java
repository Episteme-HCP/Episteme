
import java.io.File;
import java.lang.reflect.Method;

public class DiagnoseNative {
    public static void main(String[] args) {
        System.out.println("=== JScience Native Diagnosis ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        
        String libPath = System.getProperty("java.library.path");
        System.out.println("java.library.path: " + libPath);
        
        // Check availability of key libraries
        checkLibrary("OpenBLAS", "org.bytedeco.openblas.global.openblas", "get_blas_server");
        checkLibrary("HDF5", "io.jhdf.HdfFile", null); // JHDF is pure Java?
        checkLibrary("MPJ Express", "mpi.MPI", "Init");
        checkLibrary("JBullet", "com.bulletphysics.linearmath.Transform", null);
        checkLibrary("FFTW3", "org.bytedeco.fftw.global.fftw3", "fftw_malloc"); 
        
        // Check for specific files in C:\JScience-Native
        checkFile("C:\\JScience-Native\\MPJ\\lib\\mpj.jar");
        checkFile("C:\\JScience-Native\\HDF5\\bin\\hdf5.dll");
    }

    private static void checkLibrary(String name, String className, String methodName) {
        System.out.println("\nChecking " + name + "...");
        try {
            Class<?> clazz = Class.forName(className);
            System.out.println("  [OK] Class found: " + className);
            
            if (methodName != null) {
                try {
                    Method m = clazz.getMethod(methodName, args(methodName));
                    System.out.println("  [OK] Method found: " + methodName);
                } catch (NoSuchMethodException e) {
                     // Try finding any method
                     if (clazz.getMethods().length > 0) {
                         System.out.println("  [OK] Methods found (signature mismatch for " + methodName + "?)");
                     } else {
                         System.out.println("  [WARN] Method " + methodName + " not found.");
                     }
                }
            }
            
            // Try loading library if applicable
            try {
                if (name.equals("OpenBLAS")) {
                     // System.loadLibrary("openblas"); // JavaCPP does this automatically
                }
            } catch (Throwable t) {
                System.out.println("  [FAIL] Native Load Error: " + t.getMessage());
            }

        } catch (ClassNotFoundException e) {
            System.out.println("  [FAIL] Class NOT FOUND: " + className);
            System.out.println("         -> Check CLASSPATH.");
        } catch (Throwable t) {
            System.out.println("  [FAIL] Error: " + t.getClass().getName() + ": " + t.getMessage());
        }
    }
    
    private static Class<?>[] args(String name) {
        if (name.equals("Init")) return new Class<?>[]{String[].class};
        if (name.equals("fftw_malloc")) return new Class<?>[]{long.class};
        return new Class<?>[0];
    }

    private static void checkFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            System.out.println("[OK] File exists: " + path);
        } else {
            System.out.println("[FAIL] File missing: " + path);
        }
    }
}
