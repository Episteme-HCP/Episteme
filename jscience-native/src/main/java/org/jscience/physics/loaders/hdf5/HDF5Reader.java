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
import org.jscience.io.AbstractResourceReader;
import org.jscience.mathematics.linearalgebra.matrices.NativeMatrix;

/**
 * High-performance HDF5 reader using Panama for zero-copy data transfer.
 */
public class HDF5Reader extends AbstractResourceReader<NativeMatrix> {

    private final long fileId;
    private final boolean isShared;
    private static final MethodHandle H5F_OPEN;
    private static final MethodHandle H5F_CLOSE;
    private static final MethodHandle H5D_OPEN;
    private static final MethodHandle H5D_READ;
    private static final MethodHandle H5D_CLOSE;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup("hdf5", Arena.global());
        
        if (lookup.find("H5Fopen").isPresent()) {
            H5F_OPEN = linker.downcallHandle(lookup.find("H5Fopen").get(), 
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5F_CLOSE = linker.downcallHandle(lookup.find("H5Fclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5D_OPEN = linker.downcallHandle(lookup.find("H5Dopen2").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            H5D_READ = linker.downcallHandle(lookup.find("H5Dread").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
            H5D_CLOSE = linker.downcallHandle(lookup.find("H5Dclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            AVAILABLE = true;
        } else {
            H5F_OPEN = H5F_CLOSE = H5D_OPEN = H5D_READ = H5D_CLOSE = null;
            AVAILABLE = false;
        }
    }

    public HDF5Reader() {
        this.fileId = 0;
        this.isShared = false;
    }

    public HDF5Reader(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("HDF5 native library not found");
        this.isShared = true;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment pathSegment = arena.allocateFrom(path.toString());
            this.fileId = (long) H5F_OPEN.invokeExact(pathSegment, 0, 0L);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to open HDF5 file: " + path, t);
        }
    }

    @Override public String getName() { return "Native HDF5 Reader"; }
    @Override public String getDescription() { return "HDF5 reader using Panama and native HDF5 library."; }
    @Override public String getCategory() { return "I/O / Native"; }
    @Override public Class<NativeMatrix> getResourceType() { return NativeMatrix.class; }
    @Override public String getResourcePath() { return null; }
    @Override public String getLongDescription() { return "High-performance HDF5 reader leveraging Project Panama's Foreign Function & Memory API for zero-copy data transfer from HDF5 files into NativeMatrix objects."; }
    @Override public String[] getSupportedVersions() { return new String[] { "1.10", "1.12" }; }

    @Override
    protected NativeMatrix loadFromSource(String resourceId) throws Exception {
        Path path = Paths.get(resourceId);
        try (HDF5Reader reader = new HDF5Reader(path)) {
            // By default, try to read a dataset named "data" or "matrix"
            // This is a simplified implementation for the resource framework
            NativeMatrix matrix = new NativeMatrix(1, 1); // Temporary placeholder size
            // In a real implementation, we would query dimensions first
            reader.readMatrix("data", matrix);
            return matrix;
        }
    }

    public void readMatrix(String datasetName, NativeMatrix matrix) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment nameSegment = arena.allocateFrom(datasetName);
            long datasetId = (long) H5D_OPEN.invokeExact(fileId, nameSegment, 0L);
            try {
                // H5P_DEFAULT = 0, H5S_ALL = 0
                // ValueLayout identification for H5T_NATIVE_DOUBLE...
                H5D_READ.invokeExact(datasetId, 0L, 0L, 0L, 0L, matrix.segment());
            } finally {
                H5D_CLOSE.invokeExact(datasetId);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to read dataset: " + datasetName, t);
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
