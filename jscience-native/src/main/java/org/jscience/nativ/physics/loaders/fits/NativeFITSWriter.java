/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.loaders.fits;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.Linker;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jscience.core.io.AbstractResourceWriter;
import org.jscience.nativ.mathematics.linearalgebra.matrices.storage.NativeDoubleMatrixStorage;

/**
 * High-performance FITS writer using Panama.
 */
public class NativeFITSWriter extends AbstractResourceWriter<NativeDoubleMatrixStorage> implements AutoCloseable {

    private final MemorySegment fitsPtr;
    private final boolean isShared;
    private static final MethodHandle FFINIT;
    private static final MethodHandle FFPHPS;
    private static final MethodHandle FFPPNE;
    private static final MethodHandle FFCLOS;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup("cfitsio", Arena.global());
        
        if (lookup.find("ffinit").isPresent()) {
            FFINIT = linker.downcallHandle(lookup.find("ffinit").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            FFPHPS = linker.downcallHandle(lookup.find("ffphps").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            FFPPNE = linker.downcallHandle(lookup.find("ffppne").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            FFCLOS = linker.downcallHandle(lookup.find("ffclos").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            AVAILABLE = true;
        } else {
            FFINIT = FFPHPS = FFPPNE = FFCLOS = null;
            AVAILABLE = false;
        }
    }

    public NativeFITSWriter() {
        this.fitsPtr = MemorySegment.NULL;
        this.isShared = false;
    }

    public NativeFITSWriter(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("cfitsio library not found");
        this.isShared = true;
        MemorySegment ptr = MemorySegment.NULL;
        try (Arena arena = Arena.ofConfined()) {
            // Remove file if it exists (ffinit errors if file exists)
            java.io.File file = path.toFile();
            if (file.exists()) file.delete();

            MemorySegment pathSegment = arena.allocateFrom(path.toString());
            MemorySegment fptrPtr = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment status = arena.allocate(ValueLayout.JAVA_INT);
            status.set(ValueLayout.JAVA_INT, 0, 0);

            int res = (int) FFINIT.invokeExact(fptrPtr, pathSegment, status);
            if (res != 0) throw new RuntimeException("cfitsio error creating file: " + res);
            ptr = fptrPtr.get(ValueLayout.ADDRESS, 0);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create FITS file: " + path, t);
        }
        this.fitsPtr = ptr;
    }

    @Override public String getName() { return "Native FITS Writer"; }
    @Override public String getDescription() { return "FITS writer using Panama and cfitsio library."; }
    @Override public String getCategory() { return "I/O / Native / Physics"; }
    @Override public Class<NativeDoubleMatrixStorage> getResourceType() { return NativeDoubleMatrixStorage.class; }
    @Override public String getResourcePath() { return null; }
    @Override public String getLongDescription() { return "High-performance FITS writer leveraging Project Panama and cfitsio for fast, zero-copy data persistence from off-heap NativeDoubleMatrixStorage objects into scientific FITS files."; }
    @Override public String[] getSupportedVersions() { return new String[] { "4.0" }; }
    @Override public String[] getSupportedExtensions() { return new String[] { ".fits", ".fit" }; }

    @Override
    public void save(NativeDoubleMatrixStorage resource, String destinationId) throws Exception {
        Path path = Paths.get(destinationId);
        try (NativeFITSWriter writer = new NativeFITSWriter(path)) {
            writer.writeImage(resource);
        }
    }

    public void writeImage(NativeDoubleMatrixStorage matrix) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment status = arena.allocate(ValueLayout.JAVA_INT);
            status.set(ValueLayout.JAVA_INT, 0, 0);
            
            // Write standard header: bitpix=-64 (double), naxis=2
            MemorySegment naxes = arena.allocate(ValueLayout.JAVA_LONG, 2);
            naxes.set(ValueLayout.JAVA_LONG, 0, matrix.cols());
            naxes.set(ValueLayout.JAVA_LONG, 1, matrix.rows());
            
            int res = (int) FFPHPS.invokeExact(fitsPtr, -64, 2, naxes, status);
            if (res != 0) throw new RuntimeException("cfitsio error writing PHDU: " + res);
            
            // Write pixels
            long nElem = (long) matrix.rows() * matrix.cols();
            res = (int) FFPPNE.invokeExact(fitsPtr, 1L, 1L, nElem, matrix.segment(), status);
            if (res != 0) throw new RuntimeException("cfitsio error writing pixels: " + res);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to write FITS image", t);
        }
    }

    @Override
    public void close() {
        if (fitsPtr != null && !fitsPtr.equals(MemorySegment.NULL) && isShared) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment status = arena.allocate(ValueLayout.JAVA_INT);
                status.set(ValueLayout.JAVA_INT, 0, 0);
                FFCLOS.invokeExact(fitsPtr, status);
            } catch (Throwable t) {
                // ignore
            }
        }
    }
}






