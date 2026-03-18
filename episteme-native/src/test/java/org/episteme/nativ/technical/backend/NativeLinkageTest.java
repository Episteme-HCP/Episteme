package org.episteme.nativ.technical.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import java.lang.foreign.SymbolLookup;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Proactive tests to verify native library linkage using FFM.
 * This should catch configuration issues early.
 */
@Tag("native")
public class NativeLinkageTest {

    @Test
    public void testFFMLoaderAvailability() {
        assertNotNull(NativeFFMLoader.getLinker(), "Native FFM Linker must be available on this JDK.");
    }

    @Test
    public void testMPFRLinkage() {
        // MPFR is often a core requirement for high-precision backends
        Optional<SymbolLookup> mpfr = NativeFFMLoader.loadLibrary("mpfr", java.lang.foreign.Arena.global());
        if (mpfr.isPresent()) {
            Optional<?> symbol = NativeFFMLoader.findSymbol(mpfr.get(), "mpfr_add");
            assertTrue(symbol.isPresent(), "mpfr_add symbol should be found in loaded MPFR library.");
        } else {
            System.out.println("MPFR not found. Skipping linkage test.");
        }
    }

    @Test
    public void testCUDALinkage() {
        // Only verify if CUDA is expected to be present
        Optional<SymbolLookup> cuda = NativeFFMLoader.loadLibrary("cuda", java.lang.foreign.Arena.global());
        if (cuda.isPresent()) {
            Optional<?> symbol = NativeFFMLoader.findSymbol(cuda.get(), "cuInit");
            assertTrue(symbol.isPresent(), "cuInit symbol should be found in loaded CUDA library.");
        }
    }

    @Test
    public void testOpenCLLinkage() {
        Optional<SymbolLookup> opencl = NativeFFMLoader.loadLibrary("opencl", java.lang.foreign.Arena.global());
        if (opencl.isPresent()) {
            Optional<?> symbol = NativeFFMLoader.findSymbol(opencl.get(), "clGetPlatformIDs");
            assertTrue(symbol.isPresent(), "clGetPlatformIDs symbol should be found in loaded OpenCL library.");
        }
    }
}
