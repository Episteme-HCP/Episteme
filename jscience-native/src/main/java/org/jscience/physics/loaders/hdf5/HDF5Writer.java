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

package org.jscience.physics.loaders.hdf5;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.Linker;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jscience.io.AbstractResourceWriter;
import org.jscience.mathematics.linearalgebra.matrices.NativeMatrix;

/**
 * High-performance HDF5 writer using Panama.
 */
public class HDF5Writer extends AbstractResourceWriter<NativeMatrix> implements AutoCloseable {

    private final long fileId;
    private final boolean isShared;
    private static final MethodHandle H5F_CREATE;
    private static final MethodHandle H5F_CLOSE;
    private static final MethodHandle H5S_CREATE_SIMPLE;
    private static final MethodHandle H5D_CREATE2;
    private static final MethodHandle H5D_WRITE;
    private static final MethodHandle H5D_CLOSE;
    private static final MethodHandle H5S_CLOSE;
    private static final long H5T_NATIVE_DOUBLE;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup("hdf5", Arena.global());
        
        if (lookup.find("H5Fcreate").isPresent()) {
            H5F_CREATE = linker.downcallHandle(lookup.find("H5Fcreate").get(), 
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
            H5F_CLOSE = linker.downcallHandle(lookup.find("H5Fclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5S_CREATE_SIMPLE = linker.downcallHandle(lookup.find("H5Screate_simple").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            H5D_CREATE2 = linker.downcallHandle(lookup.find("H5Dcreate2").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
            H5D_WRITE = linker.downcallHandle(lookup.find("H5Dwrite").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
            H5D_CLOSE = linker.downcallHandle(lookup.find("H5Dclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5S_CLOSE = linker.downcallHandle(lookup.find("H5Sclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            
            if (lookup.find("H5T_NATIVE_DOUBLE_g").isPresent()) {
                MemorySegment seg = lookup.find("H5T_NATIVE_DOUBLE_g").get();
                H5T_NATIVE_DOUBLE = seg.get(ValueLayout.JAVA_LONG, 0);
            } else {
                H5T_NATIVE_DOUBLE = 0L;
            }
            
            AVAILABLE = true;
        } else {
            H5F_CREATE = H5F_CLOSE = H5S_CREATE_SIMPLE = H5D_CREATE2 = H5D_WRITE = H5D_CLOSE = H5S_CLOSE = null;
            H5T_NATIVE_DOUBLE = 0L;
            AVAILABLE = false;
        }
    }

    public HDF5Writer() {
        this.fileId = 0;
        this.isShared = false;
    }

    public HDF5Writer(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("HDF5 native library not found");
        this.isShared = true;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment pathSegment = arena.allocateFrom(path.toString());
            // H5F_ACC_TRUNC = 0x0002u
            this.fileId = (long) H5F_CREATE.invokeExact(pathSegment, 2, 0L, 0L);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create HDF5 file: " + path, t);
        }
    }

    @Override public String getName() { return "Native HDF5 Writer"; }
    @Override public String getDescription() { return "HDF5 writer using Panama and native HDF5 library."; }
    @Override public String getCategory() { return "I/O / Native"; }
    @Override public Class<NativeMatrix> getResourceType() { return NativeMatrix.class; }
    @Override public String getResourcePath() { return null; }
    @Override public String getLongDescription() { return "High-performance HDF5 writer leveraging Project Panama for zero-copy data persistence from NativeMatrix objects into HDF5 files."; }
    @Override public String[] getSupportedVersions() { return new String[] { "1.10", "1.12" }; }
    @Override public String[] getSupportedExtensions() { return new String[] { ".h5", ".hdf5" }; }

    @Override
    public void save(NativeMatrix resource, String destinationId) throws Exception {
        Path path = Paths.get(destinationId);
        try (HDF5Writer writer = new HDF5Writer(path)) {
            writer.writeMatrix("data", resource);
        }
    }

    public void writeMatrix(String datasetName, NativeMatrix matrix) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dims = arena.allocate(ValueLayout.JAVA_LONG, 2);
            dims.set(ValueLayout.JAVA_LONG, 0, matrix.rows());
            dims.set(ValueLayout.JAVA_LONG, 1, matrix.cols());
            
            long spaceId = (long) H5S_CREATE_SIMPLE.invokeExact(2, dims, MemorySegment.NULL);
            try {
                MemorySegment nameSegment = arena.allocateFrom(datasetName);
                // H5T_NATIVE_DOUBLE (Using 0L as placeholder, need actual handle in real case)
                long datasetId = (long) H5D_CREATE2.invokeExact(fileId, nameSegment, H5T_NATIVE_DOUBLE, spaceId, 0L, 0L, 0L);
                try {
                    H5D_WRITE.invokeExact(datasetId, H5T_NATIVE_DOUBLE, 0L, 0L, 0L, matrix.segment());
                } finally {
                    H5D_CLOSE.invokeExact(datasetId);
                }
            } finally {
                H5S_CLOSE.invokeExact(spaceId);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to write matrix: " + datasetName, t);
        }
    }

    @Override
    public void close() {
        if (fileId != 0 && isShared) {
            try {
                H5F_CLOSE.invokeExact(fileId);
            } catch (Throwable t) {
                // ignore
            }
        }
    }
}
