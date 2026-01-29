/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.physics.loaders.fits;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.Linker;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jscience.io.AbstractResourceReader;
import org.jscience.mathematics.linearalgebra.matrices.NativeMatrix;

/**
 * High-performance FITS reader using Project Panama to call cfitsio.
 */
public class FITSReader extends AbstractResourceReader<NativeMatrix> implements AutoCloseable {

    private final MemorySegment fitsPtr;
    private final boolean isShared;
    private static final MethodHandle FFOPEN;
    private static final MethodHandle FFGEKY;
    private static final MethodHandle FFCLOS;
    private static final MethodHandle FFGPVE;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup("cfitsio", Arena.global());
        
        if (lookup.find("ffopen").isPresent()) {
            // int ffopen(fitsfile **fptr, const char *filename, int iomode, int *status)
            FFOPEN = linker.downcallHandle(lookup.find("ffopen").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            // int ffgky(fitsfile *fptr, int datatype, const char *keyname, void *value, char *comment, int *status)
            FFGEKY = linker.downcallHandle(lookup.find("ffgky").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            
            // int ffclos(fitsfile *fptr, int *status)
            FFCLOS = linker.downcallHandle(lookup.find("ffclos").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

            // ffgpve(fitsfile *fptr, long group, long firstelem, long nelem, double nulval, double *array, int *anynul, int *status)
            FFGPVE = linker.downcallHandle(lookup.find("ffgpve").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            
            AVAILABLE = true;
        } else {
            FFOPEN = FFCLOS = FFGPVE = null;
            AVAILABLE = false;
        }
    }

    public FITSReader() {
        this.fitsPtr = MemorySegment.NULL;
        this.isShared = false;
    }

    public FITSReader(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("cfitsio native library not found");
        this.isShared = true;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment pathSegment = arena.allocateFrom(path.toString());
            MemorySegment fptrPtr = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment status = arena.allocate(ValueLayout.JAVA_INT);
            status.set(ValueLayout.JAVA_INT, 0, 0);

            // READONLY = 0
            int res = (int) FFOPEN.invokeExact(fptrPtr, pathSegment, 0, status);
            if (res != 0) throw new RuntimeException("cfitsio error opening file: " + res);
            
            this.fitsPtr = fptrPtr.get(ValueLayout.ADDRESS, 0);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to open FITS file: " + path, t);
        }
    }

    @Override public String getName() { return "Native FITS Reader"; }
    @Override public String getDescription() { return "FITS reader using Panama and cfitsio library."; }
    @Override public String getCategory() { return "I/O / Native / Physics"; }
    @Override public Class<NativeMatrix> getResourceType() { return NativeMatrix.class; }
    @Override public String getResourcePath() { return null; }
    @Override public String getLongDescription() { return "High-performance FITS reader leveraging Project Panama and cfitsio for fast, zero-copy data loading from scientific FITS files directly into off-heap NativeMatrix objects."; }
    @Override public String[] getSupportedVersions() { return new String[] { "4.0" }; }

    @Override
    protected NativeMatrix loadFromSource(String resourceId) throws Exception {
        Path path = Paths.get(resourceId);
        try (FITSReader reader = new FITSReader(path)) {
            long naxis1 = reader.readKeyLong("NAXIS1");
            long naxis2 = reader.readKeyLong("NAXIS2");
            
            NativeMatrix matrix = new NativeMatrix((int) naxis2, (int) naxis1);
            reader.readImage(1, naxis1 * naxis2, matrix);
            return matrix;
        }
    }

    public long readKeyLong(String keyName) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment keySegment = arena.allocateFrom(keyName);
            MemorySegment valPtr = arena.allocate(ValueLayout.JAVA_LONG);
            MemorySegment status = arena.allocate(ValueLayout.JAVA_INT);
            status.set(ValueLayout.JAVA_INT, 0, 0);
            
            // TLONGLONG = 81
            int res = (int) FFGEKY.invokeExact(fitsPtr, 81, keySegment, valPtr, MemorySegment.NULL, status);
            if (res != 0) return 0;
            return valPtr.get(ValueLayout.JAVA_LONG, 0);
        } catch (Throwable t) {
            return 0;
        }
    }

    public void readImage(long firstElem, long nElem, NativeMatrix matrix) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment status = arena.allocate(ValueLayout.JAVA_INT);
            status.set(ValueLayout.JAVA_INT, 0, 0);
            MemorySegment anynul = arena.allocate(ValueLayout.JAVA_INT);
            
            int res = (int) FFGPVE.invokeExact(fitsPtr, 1L, firstElem, nElem, 0.0, matrix.segment(), anynul, status);
            if (res != 0) throw new RuntimeException("cfitsio error reading image: " + res);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to read FITS image", t);
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
