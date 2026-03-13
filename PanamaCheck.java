import java.lang.foreign.*;

public class PanamaCheck {
    public static void main(String[] args) {
        System.out.println("Checking Panama API...");
        try {
            Linker linker = Linker.nativeLinker();
            System.out.println("Linker: " + linker);
            SymbolLookup lookup = SymbolLookup.loaderLookup();
            System.out.println("Loader Lookup: " + lookup);
            System.out.println("Panama API is working!");
        } catch (Throwable t) {
            System.err.println("Panama API FAILURE!");
            t.printStackTrace();
        }
    }
}
