/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.bytedeco.hdf5.*;
import static org.bytedeco.hdf5.global.hdf5.*;
import org.bytedeco.javacpp.FloatPointer;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Benchmark for HDF5 Write operations using native HDF5 library.
 */
@AutoService(RunnableBenchmark.class)
public class HDF5WriteBenchmark implements RunnableBenchmark {

    private static final String FILE_NAME = "benchmark_data.h5";
    private static final String DATASET_NAME = "LargeArray";
    private static final int ARRAY_SIZE = 25 * 1024 * 1024; // 25M floats = 100MB
    
    private FloatPointer data;

    @Override
    public String getId() {
        return "io-hdf5-write-native";
    }

    @Override
    public String getName() {
        return "HDF5 Write (100MB)";
    }

    @Override
    public String getAlgorithmProvider() {
        return "Native HDF5 (Bytedeco)";
    }

    @Override
    public String getDescription() {
        return "Benchmarks writing speed of 100MB float array to HDF5 file.";
    }

    @Override
    public String getDomain() {
        return "Input/Output";
    }

    @Override
    public void setup() {
        try {
            // Check library availability
            try {
                org.bytedeco.hdf5.global.hdf5.H5get_libversion(new int[1], new int[1], new int[1]);
            } catch (Throwable t) {
                System.err.println("HDF5 Native Lib Load Failed: " + t.getMessage());
                // Let run() fail gracefully or isAvailable check?
            }

            // data allocation
            data = new FloatPointer(ARRAY_SIZE);
            for (int i = 0; i < ARRAY_SIZE; i++) {
                data.put(i, (float) Math.random());
            }

            Files.deleteIfExists(Path.of(FILE_NAME));
        } catch (java.lang.Exception e) {
            throw new RuntimeException("Setup failed", e);
        }
    }

    @Override
    public void run() {
        try {
             // 1. Create File
            H5File file = new H5File(FILE_NAME, H5F_ACC_TRUNC);

            // 2. Define Data Space
            long[] dims = {ARRAY_SIZE};
            DataSpace dataspace = new DataSpace(1, dims);

            // 3. Create Dataset
            DataSet dataset = file.createDataSet(DATASET_NAME, new DataType((long)H5T_NATIVE_FLOAT), dataspace);

            // 4. Write Data
            dataset.write(data, new DataType((long)H5T_NATIVE_FLOAT));

            // 5. Close Resources
            dataset.close();
            dataspace.close();
            file.close();
        } catch (java.lang.Exception e) {
             throw new RuntimeException("HDF5 Write Failed", e);
        }
    }

    @Override
    public void teardown() {
        if (data != null) {
            data.close(); // Release native memory
        }
        try {
             Files.deleteIfExists(Path.of(FILE_NAME));
        } catch (java.lang.Exception ignored) {}
    }

    @Override
    public int getSuggestedIterations() {
        return 5; // I/O is slow, so few iterations
    }
    
    @Override
    public boolean isAvailable() {
        try {
            // Simple check if class is loadable and lib links
            Class.forName("org.bytedeco.hdf5.H5");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
