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

package org.jscience.nativ.physics.loaders.hdf5;

import java.io.IOException;
import java.util.Optional;
import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.Linker;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jscience.core.io.AbstractResourceReader;
import org.jscience.nativ.mathematics.linearalgebra.matrices.storage.NativeDoubleMatrixStorage;

/**
 * High-performance HDF5 reader using Panama for zero-copy data transfer.
 */
public class NativeHDF5Reader extends AbstractResourceReader<NativeDoubleMatrixStorage> implements AutoCloseable {

    private final long fileId;
    private final boolean isShared;
    private static final MethodHandle H5F_OPEN;
    private static final MethodHandle H5F_CLOSE;
    private static final MethodHandle H5D_OPEN;
    private static final MethodHandle H5D_GET_SPACE;
    private static final MethodHandle H5S_GET_SIMPLE_EXTENT_DIMS;
    private static final MethodHandle H5S_CLOSE;
    private static final MethodHandle H5D_READ;
    private static final MethodHandle H5D_CLOSE;
    private static final MethodHandle H5S_SELECT_HYPERSLAB;
    private static final MethodHandle H5S_CREATE_SIMPLE;
    private static final long H5T_NATIVE_DOUBLE;
    private static final int H5S_SELECT_SET = 0;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        Optional<SymbolLookup> lookupOpt = NativeLibraryLoader.loadLibrary("hdf5", Arena.global());
        SymbolLookup lookup = lookupOpt.orElse(null);
        
        if (lookup != null && lookup.find("H5Fopen").isPresent()) {
            H5F_OPEN = linker.downcallHandle(lookup.find("H5Fopen").get(), 
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5F_CLOSE = linker.downcallHandle(lookup.find("H5Fclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5D_OPEN = linker.downcallHandle(lookup.find("H5Dopen2").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            H5D_GET_SPACE = linker.downcallHandle(lookup.find("H5Dget_space").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
            H5S_GET_SIMPLE_EXTENT_DIMS = linker.downcallHandle(lookup.find("H5Sget_simple_extent_dims").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            H5S_CLOSE = linker.downcallHandle(lookup.find("H5Sclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5D_READ = linker.downcallHandle(lookup.find("H5Dread").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
            H5D_CLOSE = linker.downcallHandle(lookup.find("H5Dclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            H5S_SELECT_HYPERSLAB = linker.downcallHandle(lookup.find("H5Sselect_hyperslab").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            H5S_CREATE_SIMPLE = linker.downcallHandle(lookup.find("H5Screate_simple").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            
            // Load H5T_NATIVE_DOUBLE from symbol H5T_NATIVE_DOUBLE_g
            if (lookup.find("H5T_NATIVE_DOUBLE_g").isPresent()) {
                MemorySegment seg = lookup.find("H5T_NATIVE_DOUBLE_g").get();
                H5T_NATIVE_DOUBLE = seg.get(ValueLayout.JAVA_LONG, 0);
            } else {
                H5T_NATIVE_DOUBLE = 0L; // Fallback
            }
            
            AVAILABLE = true;
        } else {
            H5F_OPEN = H5F_CLOSE = H5D_OPEN = H5D_GET_SPACE = H5S_GET_SIMPLE_EXTENT_DIMS = H5S_CLOSE = H5D_READ = H5D_CLOSE = H5S_SELECT_HYPERSLAB = H5S_CREATE_SIMPLE = null;
            H5T_NATIVE_DOUBLE = 0L;
            AVAILABLE = false;
        }
    }

    public NativeHDF5Reader() {
        this.fileId = 0;
        this.isShared = false;
    }

    public NativeHDF5Reader(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("HDF5 native library not found");
        this.isShared = true;
        long fId = 0;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment pathSegment = arena.allocateFrom(path.toString());
            fId = (long) H5F_OPEN.invokeExact(pathSegment, 0, 0L);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to open HDF5 file: " + path, t);
        }
        this.fileId = fId;
    }

    /**
     * Opens an HDF5 file and reads the dataset named "data".
     */
    public static NativeDoubleMatrixStorage open(String path) throws Exception {
        try (NativeHDF5Reader reader = new NativeHDF5Reader()) {
            return reader.load(path);
        }
    }

    @Override public String getName() { return "Native HDF5 Reader"; }
    @Override public String getDescription() { return "HDF5 reader using Panama and native HDF5 library."; }
    @Override public String getCategory() { return "I/O / Native"; }
    @Override public Class<NativeDoubleMatrixStorage> getResourceType() { return NativeDoubleMatrixStorage.class; }
    @Override public String getResourcePath() { return null; }
    @Override public String getLongDescription() { return "High-performance HDF5 reader leveraging Project Panama's Foreign Function & Memory API for zero-copy data transfer from HDF5 files into NativeDoubleMatrixStorage objects."; }
    @Override public String[] getSupportedVersions() { return new String[] { "1.10", "1.12" }; }
    @Override public String[] getSupportedExtensions() { return new String[] { ".h5", ".hdf5" }; }

    @Override
    protected NativeDoubleMatrixStorage loadFromSource(String resourceId) throws Exception {
        Path path = Paths.get(resourceId);
        try (NativeHDF5Reader reader = new NativeHDF5Reader(path)) {
            String datasetName = "data"; // Default
            long[] dims = reader.getDatasetDimensions(datasetName);
            if (dims == null || dims.length < 2) {
                throw new IOException("Dataset 'data' not found or has wrong dimensionality");
            }
            
            NativeDoubleMatrixStorage matrix = new NativeDoubleMatrixStorage((int) dims[0], (int) dims[1]);
            reader.readMatrix(datasetName, matrix);
            return matrix;
        }
    }

    public long[] getDatasetDimensions(String datasetName) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment nameSegment = arena.allocateFrom(datasetName);
            long datasetId = (long) H5D_OPEN.invokeExact(fileId, nameSegment, 0L);
            if (datasetId < 0) return null;
            
            try {
                long spaceId = (long) H5D_GET_SPACE.invokeExact(datasetId);
                try {
                    MemorySegment dimsSeg = arena.allocate(ValueLayout.JAVA_LONG, 2);
                    int ndims = (int) H5S_GET_SIMPLE_EXTENT_DIMS.invokeExact(spaceId, dimsSeg, MemorySegment.NULL);
                    long[] result = new long[ndims];
                    for (int i = 0; i < ndims; i++) {
                        result[i] = dimsSeg.get(ValueLayout.JAVA_LONG, (long) i * 8);
                    }
                    return result;
                } finally {
                    H5S_CLOSE.invokeExact(spaceId);
                }
            } finally {
                H5D_CLOSE.invokeExact(datasetId);
            }
        } catch (Throwable t) {
            return null;
        }
    }

    public void readMatrix(String datasetName, NativeDoubleMatrixStorage matrix) {
        readBlock(datasetName, matrix, 0, 0, matrix.rows(), matrix.cols());
    }

    /**
     * Reads a block of data from the dataset using hyperslabs.
     */
    public void readBlock(String datasetName, NativeDoubleMatrixStorage matrix, int startRow, int startCol, int rowCount, int colCount) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment nameSegment = arena.allocateFrom(datasetName);
            long datasetId = (long) H5D_OPEN.invokeExact(fileId, nameSegment, 0L);
            try {
                long fileSpaceId = (long) H5D_GET_SPACE.invokeExact(datasetId);
                try {
                    MemorySegment start = arena.allocate(ValueLayout.JAVA_LONG, 2);
                    MemorySegment count = arena.allocate(ValueLayout.JAVA_LONG, 2);
                    start.set(ValueLayout.JAVA_LONG, 0, (long) startRow);
                    start.set(ValueLayout.JAVA_LONG, 8, (long) startCol);
                    count.set(ValueLayout.JAVA_LONG, 0, (long) rowCount);
                    count.set(ValueLayout.JAVA_LONG, 8, (long) colCount);

                    H5S_SELECT_HYPERSLAB.invokeExact(fileSpaceId, H5S_SELECT_SET, start, MemorySegment.NULL, count, MemorySegment.NULL);

                    long memSpaceId = (long) H5S_CREATE_SIMPLE.invokeExact(2, count, MemorySegment.NULL);
                    try {
                        H5D_READ.invokeExact(datasetId, H5T_NATIVE_DOUBLE, memSpaceId, fileSpaceId, 0L, matrix.segment());
                    } finally {
                        H5S_CLOSE.invokeExact(memSpaceId);
                    }
                } finally {
                    H5S_CLOSE.invokeExact(fileSpaceId);
                }
            } finally {
                H5D_CLOSE.invokeExact(datasetId);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to read block from dataset: " + datasetName, t);
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





