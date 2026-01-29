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

package org.jscience.foreign.io.hdf5;

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
 * High-performance HDF5 writer using Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class HDF5Writer implements AutoCloseable {

    private final long fileId;
    private static final MethodHandle H5F_CREATE;
    private static final MethodHandle H5F_CLOSE;
    private static final MethodHandle H5D_WRITE;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup("hdf5", Arena.global());
        
        if (lookup.find("H5Fcreate").isPresent()) {
            H5F_CREATE = linker.downcallHandle(lookup.find("H5Fcreate").get(), 
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
            H5F_CLOSE = linker.downcallHandle(lookup.find("H5Fclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5D_WRITE = linker.downcallHandle(lookup.find("H5Dwrite").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
            AVAILABLE = true;
        } else {
            H5F_CREATE = H5F_CLOSE = H5D_WRITE = null;
            AVAILABLE = false;
        }
    }

    public HDF5Writer(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("HDF5 native library not found");
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment pathSegment = arena.allocateFrom(path.toString());
            // H5F_ACC_TRUNC = 0x0002u
            this.fileId = (long) H5F_CREATE.invokeExact(pathSegment, 2, 0L, 0L);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create HDF5 file: " + path, t);
        }
    }

    public void writeMatrix(String datasetName, NativeMatrix matrix) {
        // Full implementation would require H5Screate_simple, H5Dcreate2 etc.
        // This is a skeleton showing the Panama call.
    }

    @Override
    public void close() {
        if (fileId != 0) {
            try {
                H5F_CLOSE.invokeExact(fileId);
            } catch (Throwable t) {
                // ignore
            }
        }
    }
}
