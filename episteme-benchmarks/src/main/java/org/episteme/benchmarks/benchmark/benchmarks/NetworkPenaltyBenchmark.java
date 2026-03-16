/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.mathematics.sets.Reals;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * Benchmark specifically designed to quantify the network penalty of gRPC 
 * distribution compared to high-performance local backends (Panama-FFM).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(RunnableBenchmark.class)
public class NetworkPenaltyBenchmark implements RunnableBenchmark {

    private static final int DEFAULT_SIZE = 512;
    private static final int DRY_RUN_SIZE = 16;
    
    private int size = DEFAULT_SIZE;
    private boolean dryRun = false;
    
    private RealDoubleMatrix A;
    private RealDoubleMatrix B;
    
    private LinearAlgebraProvider<Real> panamaProvider;
    private LinearAlgebraProvider<Real> grpcProvider;
    
    private boolean useGrpc = false;

    @Override
    public String getId() {
        return "network-penalty-" + (useGrpc ? "grpc" : "panama");
    }

    @Override
    public String getName() {
        return "Network Penalty (" + (useGrpc ? "gRPC" : "Panama") + ")";
    }

    @Override
    public String getDescription() {
        return "Measures Matrix Multiplication overhead. Compare this result with the Panama version to isolate network/serialization latency.";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra (Distribution Overhead)";
    }

    @Override
    public String getAlgorithmProvider() {
        return useGrpc ? "gRPC Remote" : "Panama-FFM";
    }

    @Override
    public String getAlgorithmType() {
        return "Matrix Multiplication";
    }

    public void setUseGrpc(boolean useGrpc) {
        this.useGrpc = useGrpc;
    }

    @Override
    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    @Override
    public boolean isDryRun() {
        return dryRun;
    }

    @Override
    public void setup() {
        int actualSize = dryRun ? DRY_RUN_SIZE : size;
        double[][] dataA = new double[actualSize][actualSize];
        double[][] dataB = new double[actualSize][actualSize];
        Random r = new Random(42);
        for (int i = 0; i < actualSize; i++) {
            for (int j = 0; j < actualSize; j++) {
                dataA[i][j] = r.nextDouble();
                dataB[i][j] = r.nextDouble();
            }
        }
        A = RealDoubleMatrix.of(dataA);
        B = RealDoubleMatrix.of(dataB);

        // Discovery
        var providers = AlgorithmManager.getProviders(LinearAlgebraProvider.class);
        
        for (LinearAlgebraProvider<Real> p : providers) {
            if (p.getName().contains("Native SIMD") || p.getName().contains("Panama")) {
                panamaProvider = p;
            } else if (p.getName().contains("gRPC Remote")) {
                grpcProvider = p;
            }
        }
        
        if (panamaProvider == null) {
             org.slf4j.LoggerFactory.getLogger(NetworkPenaltyBenchmark.class).warn("Panama provider not found, fallback to default provider.");
             panamaProvider = AlgorithmManager.getProvider(LinearAlgebraProvider.class);
        }
    }

    @Override
    public void run() {
        LinearAlgebraProvider<Real> provider = useGrpc ? grpcProvider : panamaProvider;
        if (provider != null && provider.isAvailable()) {
            provider.multiply(A, B);
        } else {
            throw new RuntimeException("Target provider not available: " + (useGrpc ? "gRPC" : "Panama"));
        }
    }

    @Override
    public void teardown() {
        A = null;
        B = null;
    }

    @Override
    public int getSuggestedIterations() {
        return useGrpc ? 5 : 20; // gRPC is much slower, fewer iterations needed for stability
    }

    @Override
    public Map<String, String> getMetadata() {
        Map<String, String> meta = new HashMap<>();
        meta.put("Matrix Size", String.valueOf(dryRun ? DRY_RUN_SIZE : size));
        meta.put("Mode", useGrpc ? "Remote (gRPC)" : "Local (Panama)");
        return meta;
    }
    
    @Override
    public boolean isAvailable() {
        setup(); // Check availability
        return (useGrpc ? grpcProvider : panamaProvider) != null;
    }
}
