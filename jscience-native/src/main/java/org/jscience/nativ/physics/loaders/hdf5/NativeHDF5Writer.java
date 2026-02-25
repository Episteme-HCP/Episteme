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
import org.jscience.core.io.AbstractResourceWriter;
import org.jscience.nativ.mathematics.linearalgebra.matrices.storage.NativeDoubleMatrixStorage;

/**
 * High-performance HDF5 writer using Panama.
 */
public class NativeHDF5Writer extends AbstractResourceWriter<NativeDoubleMatrixStorage> implements AutoCloseable {

    private final long fileId;
    private final boolean isShared;
    private static final MethodHandle H5F_CREATE;
    private static final MethodHandle H5F_CLOSE;
    private static final MethodHandle H5S_CREATE_SIMPLE;
    private static final MethodHandle H5D_CREATE2;
    private static final MethodHandle H5D_WRITE;
    private static final MethodHandle H5D_CLOSE;
    private static final MethodHandle H5S_CLOSE;
    private static final MethodHandle H5P_CREATE;
    private static final MethodHandle H5P_SET_CHUNK;
    private static final MethodHandle H5P_SET_DEFLATE;
    private static final MethodHandle H5P_SET_SZIP;
    private static final MethodHandle H5P_SET_FILTER;
    private static final MethodHandle H5P_CLOSE;
    private static final long H5T_NATIVE_DOUBLE;
    private static final long H5P_DATASET_CREATE;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        Optional<SymbolLookup> lookupOpt = NativeLibraryLoader.loadLibrary("hdf5", Arena.global());
        SymbolLookup lookup = lookupOpt.orElse(null);
        
        if (lookup != null && lookup.find("H5Fcreate").isPresent()) {
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
            H5P_CREATE = linker.downcallHandle(lookup.find("H5Pcreate").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
            H5P_SET_CHUNK = linker.downcallHandle(lookup.find("H5Pset_chunk").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            H5P_SET_DEFLATE = linker.downcallHandle(lookup.find("H5Pset_deflate").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            H5P_SET_SZIP = linker.downcallHandle(lookup.find("H5Pset_szip").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            H5P_SET_FILTER = linker.downcallHandle(lookup.find("H5Pset_filter").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
            H5P_CLOSE = linker.downcallHandle(lookup.find("H5Pclose").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            
            if (lookup.find("H5T_NATIVE_DOUBLE_g").isPresent()) {
                MemorySegment seg = lookup.find("H5T_NATIVE_DOUBLE_g").get();
                H5T_NATIVE_DOUBLE = seg.get(ValueLayout.JAVA_LONG, 0);
            } else {
                H5T_NATIVE_DOUBLE = 0L;
            }

            if (lookup.find("H5P_CLS_DATASET_CREATE_ID_g").isPresent()) {
                 MemorySegment seg = lookup.find("H5P_CLS_DATASET_CREATE_ID_g").get();
                 H5P_DATASET_CREATE = seg.get(ValueLayout.JAVA_LONG, 0);
            } else {
                 H5P_DATASET_CREATE = 0L;
            }
            
            AVAILABLE = true;
        } else {
            H5F_CREATE = H5F_CLOSE = H5S_CREATE_SIMPLE = H5D_CREATE2 = H5D_WRITE = H5D_CLOSE = H5S_CLOSE = null;
            H5P_CREATE = H5P_SET_CHUNK = H5P_SET_DEFLATE = H5P_SET_SZIP = H5P_SET_FILTER = H5P_CLOSE = null;
            H5T_NATIVE_DOUBLE = 0L;
            H5P_DATASET_CREATE = 0L;
            AVAILABLE = false;
        }
    }

    public NativeHDF5Writer() {
        this.fileId = 0;
        this.isShared = false;
    }

    public NativeHDF5Writer(Path path) {
        if (!AVAILABLE) throw new UnsupportedOperationException("HDF5 native library not found");
        this.isShared = true;
        long fId = 0;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment pathSegment = arena.allocateFrom(path.toString());
            // H5F_ACC_TRUNC = 0x0002u
            fId = (long) H5F_CREATE.invokeExact(pathSegment, 2, 0L, 0L);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create HDF5 file: " + path, t);
        }
        this.fileId = fId;
    }

    @Override public String getName() { return "Native HDF5 Writer"; }
    @Override public String getDescription() { return "HDF5 writer using Panama and native HDF5 library."; }
    @Override public String getCategory() { return "I/O / Native"; }
    @Override public Class<NativeDoubleMatrixStorage> getResourceType() { return NativeDoubleMatrixStorage.class; }
    @Override public String getResourcePath() { return null; }
    @Override public String getLongDescription() { return "High-performance HDF5 writer leveraging Project Panama for zero-copy data persistence from NativeDoubleMatrixStorage objects into HDF5 files."; }
    @Override public String[] getSupportedVersions() { return new String[] { "1.10", "1.12" }; }
    @Override public String[] getSupportedExtensions() { return new String[] { ".h5", ".hdf5" }; }

    @Override
    public void save(NativeDoubleMatrixStorage resource, String destinationId) throws Exception {
        Path path = Paths.get(destinationId);
        try (NativeHDF5Writer writer = new NativeHDF5Writer(path)) {
            writer.writeMatrix("data", resource);
        }
    }

    public void writeMatrix(String datasetName, NativeDoubleMatrixStorage matrix, String compression, int level) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dims = arena.allocate(ValueLayout.JAVA_LONG, 2);
            dims.set(ValueLayout.JAVA_LONG, 0, matrix.rows());
            dims.set(ValueLayout.JAVA_LONG, 1, matrix.cols());
            
            long spaceId = (long) H5S_CREATE_SIMPLE.invokeExact(2, dims, MemorySegment.NULL);
            try {
                long dcpl = 0L;
                if (compression != null && !compression.equalsIgnoreCase("none")) {
                    dcpl = (long) H5P_CREATE.invokeExact(H5P_DATASET_CREATE);
                    // Standard HDF5 chunking (e.g. 128x128)
                    MemorySegment chunks = arena.allocate(ValueLayout.JAVA_LONG, 2);
                    chunks.set(ValueLayout.JAVA_LONG, 0, Math.min(128L, matrix.rows()));
                    chunks.set(ValueLayout.JAVA_LONG, 1, Math.min(128L, matrix.cols()));
                    H5P_SET_CHUNK.invokeExact(dcpl, 2, chunks);

                    if (compression.equalsIgnoreCase("gzip")) {
                        H5P_SET_DEFLATE.invokeExact(dcpl, level);
                    } else if (compression.equalsIgnoreCase("szip")) {
                        // H5_SZIP_NN_OPTION_MASK = 32
                        H5P_SET_SZIP.invokeExact(dcpl, 32, level);
                    } else if (compression.equalsIgnoreCase("blosc")) {
                        // H5Z_FILTER_BLOSC = 32001
                        // cd_values: [0, 0, 0, level, 1 (shuffle), 1 (blosclz)]
                        MemorySegment cdValues = arena.allocate(ValueLayout.JAVA_INT, 7);
                        cdValues.set(ValueLayout.JAVA_INT, 0, 0); // Reserved
                        cdValues.set(ValueLayout.JAVA_INT, 4, 0); // Reserved
                        cdValues.set(ValueLayout.JAVA_INT, 8, 0); // Reserved
                        cdValues.set(ValueLayout.JAVA_INT, 12, level);
                        cdValues.set(ValueLayout.JAVA_INT, 16, 1); // Shuffle
                        cdValues.set(ValueLayout.JAVA_INT, 20, 1); // Compressor (blosclz)
                        H5P_SET_FILTER.invokeExact(dcpl, 32001, 1, 6L, cdValues);
                    }
                }

                MemorySegment nameSegment = arena.allocateFrom(datasetName);
                long datasetId = (long) H5D_CREATE2.invokeExact(fileId, nameSegment, H5T_NATIVE_DOUBLE, spaceId, 0L, dcpl, 0L);
                try {
                    H5D_WRITE.invokeExact(datasetId, H5T_NATIVE_DOUBLE, 0L, 0L, 0L, matrix.segment());
                } finally {
                    H5D_CLOSE.invokeExact(datasetId);
                }
                
                if (dcpl != 0) H5P_CLOSE.invokeExact(dcpl);
            } finally {
                H5S_CLOSE.invokeExact(spaceId);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to write matrix with compression: " + datasetName, t);
        }
    }

    public void writeMatrix(String datasetName, NativeDoubleMatrixStorage matrix) {
        writeMatrix(datasetName, matrix, null, 0);
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





