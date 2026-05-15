import java.lang.foreign.*;
import java.lang.invoke.*;

public class MPFRInspector {
    public static void main(String[] args) throws Throwable {
        SymbolLookup lookup = SymbolLookup.libraryLookup("C:\\Silvere\\Encours\\Developpement\\Episteme\\libs\\mpfr.dll", Arena.global());
        Linker linker = Linker.nativeLinker();
        
        MethodHandle init2 = linker.downcallHandle(
            lookup.find("mpfr_init2").get(),
            FunctionDescriptor.ofVoid(AddressLayout.ADDRESS, ValueLayout.JAVA_INT)
        );
        
        MethodHandle clear = linker.downcallHandle(
            lookup.find("mpfr_clear").get(),
            FunctionDescriptor.ofVoid(AddressLayout.ADDRESS)
        );
        
        System.out.println("Testing mpfr_init2 and mpfr_clear on Windows 64-bit...");
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment mpfr = arena.allocate(24); // 24 bytes as discovered
            System.out.println("Allocated 24 bytes at " + Long.toHexString(mpfr.address()));
            
            init2.invokeExact(mpfr, 123);
            System.out.println("Initialized with precision 123.");
            
            // Inspect before clear
            System.out.println("Pointer at offset 16: 0x" + Long.toHexString(mpfr.get(ValueLayout.JAVA_LONG_UNALIGNED, 16)));
            
            System.out.println("Calling mpfr_clear...");
            clear.invokeExact(mpfr);
            System.out.println("mpfr_clear completed successfully!");
        }
    }
}
