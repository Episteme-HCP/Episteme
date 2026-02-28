package org.episteme.nativ.technical.backend.nativ;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NativeLibraryLoaderTest {

    @Test
    public void testLoadLibraries() {
        assertNotNull(NativeLibraryLoader.loadLibrary("openblas"));
        assertNotNull(NativeLibraryLoader.loadLibrary("fftw3"));
        assertNotNull(NativeLibraryLoader.loadLibrary("hdf5"));
        assertNotNull(NativeLibraryLoader.loadLibrary("bulletc"));
        assertNotNull(NativeLibraryLoader.loadLibrary("ode"));
        assertNotNull(NativeLibraryLoader.loadLibrary("sndfile"));
        assertNotNull(NativeLibraryLoader.loadLibrary("arrow"));
        assertNotNull(NativeLibraryLoader.loadLibrary("quest"));
    }
}
