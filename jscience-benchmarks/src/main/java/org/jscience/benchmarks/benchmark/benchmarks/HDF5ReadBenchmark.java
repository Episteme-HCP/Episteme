/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.bytedeco.hdf5.*;
import static org.bytedeco.hdf5.global.hdf5.*;
import org.bytedeco.javacpp.FloatPointer;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Benchmark for HDF5 Read operations using native HDF5 library.
 */
@AutoService(RunnableBenchmark.class)
public class HDF5ReadBenchmark implements RunnableBenchmark {

    private static final String FILE_NAME = "benchmark_read_data.h5";
    private static final String DATASET_NAME = "LargeArray";
    private static final int ARRAY_SIZE = 25 * 1024 * 1024; // 25M floats = 100MB
    
    private FloatPointer buffer;

    @Override
    public String getId() {
        return "io-hdf5-read-native";
    }

    @Override
    public String getName() {
        return "HDF5 Read (100MB)";
    }

    @Override
    public String getAlgorithmProvider() {
        return "Native HDF5 (Bytedeco)";
    }

    @Override
    public String getDescription() {
        return "Benchmarks reading speed of 100MB float array from HDF5 file.";
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
            }

            // setupPath not needed for String filename usage in constructor
            
            // Generate file for reading
            H5File file = new H5File(FILE_NAME, H5F_ACC_TRUNC);
            long[] dims = {ARRAY_SIZE};
            DataSpace dataspace = new DataSpace(1, dims);
            DataSet dataset = file.createDataSet(DATASET_NAME, new DataType((long)H5T_NATIVE_FLOAT), dataspace);
            
            FloatPointer initData = new FloatPointer(ARRAY_SIZE);
            for (int i = 0; i < ARRAY_SIZE; i++) {
                initData.put(i, (float) Math.random());
            }
            dataset.write(initData, new DataType((long)H5T_NATIVE_FLOAT));
            
            dataset.close();
            dataspace.close();
            file.close();
            initData.close();
            
            // Allocate read buffer
            buffer = new FloatPointer(ARRAY_SIZE);
            
        } catch (java.lang.Exception e) {
            throw new RuntimeException("Setup failed", e);
        }
    }

    @Override
    public void run() {
        try {
            // 1. Open File
            H5File file = new H5File(FILE_NAME, H5F_ACC_RDONLY);

            // 2. Open Dataset
            DataSet dataset = file.openDataSet(DATASET_NAME);

            // 3. Read Data
            dataset.read(buffer, new DataType((long)H5T_NATIVE_FLOAT));

            // 4. Close Resources
            dataset.close();
            file.close();
        } catch (java.lang.Exception e) {
             throw new RuntimeException("HDF5 Read Failed", e);
        }
    }

    @Override
    public void teardown() {
        if (buffer != null) {
            buffer.close();
        }
        try {
             Files.deleteIfExists(Path.of(FILE_NAME));
        } catch (java.lang.Exception ignored) {}
    }

    @Override
    public int getSuggestedIterations() {
        return 5;
    }
    
    @Override
    public boolean isAvailable() {
        try {
             Class.forName("org.bytedeco.hdf5.H5");
             return true;
        } catch (Throwable t) {
             return false;
        }
    }
}
