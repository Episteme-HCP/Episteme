import java.lang.foreign.*;
import java.lang.invoke.*;

public class MPFRVersion {
    public static void main(String[] args) throws Throwable {
        SymbolLookup lookup = SymbolLookup.libraryLookup("C:\\Silvere\\Encours\\Developpement\\Episteme\\libs\\mpfr.dll", Arena.global());
        MethodHandle getVersion = Linker.nativeLinker().downcallHandle(
            lookup.find("mpfr_get_version").get(),
            FunctionDescriptor.of(AddressLayout.ADDRESS)
        );
        MemorySegment versionPtr = (MemorySegment) getVersion.invokeExact();
        System.out.println("MPFR Version: " + versionPtr.reinterpret(1024).getString(0));
    }
}
