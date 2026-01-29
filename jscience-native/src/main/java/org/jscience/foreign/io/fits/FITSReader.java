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

package org.jscience.foreign.io.fits;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.Linker;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import org.jscience.foreign.matrix.NativeMatrix;

/**
 * High-performance FITS reader using Project Panama to call cfitsio.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class FITSReader implements AutoCloseable {

    private final MemorySegment fitsPtr;
    private static final MethodHandle FFOPEN;
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

    public FITSReader(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("cfitsio native library not found");
        
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
        if (fitsPtr != null && !fitsPtr.equals(MemorySegment.NULL)) {
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
