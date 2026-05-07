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

package org.episteme.client.client.mathematics.linearalgebra.backends;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.episteme.server.server.proto.*;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.technical.backend.HardwareAccelerator;
import com.google.auto.service.AutoService;

import com.google.protobuf.ByteString;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A LinearAlgebraProvider that offloads operations to a remote gRPC service.
 * <p>
 * This provider enables distributed computing by delegating matrix and vector
 * operations to a remote server. Useful for:
 * <ul>
 *   <li>Offloading heavy computations to powerful servers</li>
 *   <li>Utilizing GPU clusters remotely</li>
 *   <li>Collaborative scientific computing</li>
 * </ul>
 * </p>
 *
 * @param <E> The element type (typically Real for network transfer)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({LinearAlgebraBackend.class, LinearAlgebraProvider.class, org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider.class, org.episteme.core.technical.backend.ComputeBackend.class, Backend.class})
public class GRPCLinearAlgebraBackend<E> implements org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<E>, LinearAlgebraBackend<E> {
    
    private ManagedChannel channel;
    private MatrixServiceGrpc.MatrixServiceBlockingStub blockingStub;
    private final Field<E> field;
    private final String serverAddress;

    /**
     * Public no-arg constructor required by ServiceLoader.
     * Defaults to localhost:50051.
     */
    @SuppressWarnings("unchecked")
    public GRPCLinearAlgebraBackend() {
        this("localhost", 50051, (Field<E>) org.episteme.core.mathematics.sets.Reals.getInstance());
    }

    /**
     * Creates a gRPC provider connected to the specified server.
     *
     * @param host  Server hostname or IP
     * @param port  Server port
     * @param field The field for element operations
     */
    public GRPCLinearAlgebraBackend(String host, int port, Field<E> field) {
        this.serverAddress = host + ":" + port;
        this.field = field;
        
        // Define retry policy
        java.util.Map<String, Object> retryPolicy = new java.util.HashMap<>();
        retryPolicy.put("maxAttempts", 5.0);
        retryPolicy.put("initialBackoff", "0.5s");
        retryPolicy.put("maxBackoff", "30s");
        retryPolicy.put("backoffMultiplier", 2.0);
        retryPolicy.put("retryableStatusCodes", java.util.List.of("UNAVAILABLE", "DEADLINE_EXCEEDED"));

        java.util.Map<String, Object> methodConfig = new java.util.HashMap<>();
        java.util.Map<String, Object> name = new java.util.HashMap<>();
        name.put("service", "org.episteme.server.server.proto.MatrixService");
        methodConfig.put("name", java.util.List.of(name));
        methodConfig.put("retryPolicy", retryPolicy);

        java.util.Map<String, Object> serviceConfig = new java.util.HashMap<>();
        serviceConfig.put("methodConfig", java.util.List.of(methodConfig));

        try {
            this.channel = ManagedChannelBuilder.forAddress(host, port)
                    .defaultServiceConfig(serviceConfig)
                    .enableRetry()
                    .usePlaintext()
                    .build();
            this.blockingStub = MatrixServiceGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            this.channel = null;
            this.blockingStub = null;
        }
    }

    private RuntimeException mapException(String operation, StatusRuntimeException e) {
        String msg = String.format("gRPC %s failed: [%s] %s", operation, e.getStatus().getCode(), e.getStatus().getDescription());
        if (e.getStatus().getCode() == io.grpc.Status.Code.UNAVAILABLE) {
            return new RuntimeException("Remote server is unavailable at " + serverAddress + ". Check if the Episteme server is running.", e);
        }
        return new RuntimeException(msg, e);
    }

    @Override
    public boolean isAvailable() {
        return channel != null && !channel.isShutdown() && !isExplicitlyDisabled();
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public String getDescription() {
        return "Remote linear algebra provider using gRPC for offloading computations.";
    }

    @Override
    public String getName() {
        return "Episteme gRPC Remote (" + serverAddress + ")";
    }

    @Override
    public String getId() {
        return "grpc-math";
    }

    @Override
    public String getType() {
        return "linearalgebra";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.DISTRIBUTED;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override public <T> T execute(org.episteme.core.technical.backend.Operation<T> op) { return op.compute(this); }
            @Override public void close() {}
        };
    }

    @Override
    public int getPriority() {
        return 0; // Low priority - typically selected manually or via config
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) return true;
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex) return true;
        return ring.zero() instanceof Real;
    }



    /**
     * Shuts down the gRPC channel gracefully.
     */
    @Override
    public void shutdown() {
        if (channel != null) {
            try {
                channel.shutdown().awaitTermination(2, TimeUnit.SECONDS);
                if (!channel.isTerminated()) {
                    channel.shutdownNow();
                }
            } catch (InterruptedException e) {
                channel.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    // ==================== Matrix Operations ====================

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        MatrixData protoA = toProtoMatrix(a);
        MatrixData protoB = toProtoMatrix(b);

        MatrixRequest request = MatrixRequest.newBuilder()
                .setMatrixA(protoA)
                .setMatrixB(protoB)
                .build();

        try {
            MatrixResponse response = blockingStub.matrixAdd(request);
            return fromProtoMatrix(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("matrixAdd", e);
        }
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        MatrixData protoA = toProtoMatrix(a);
        MatrixData protoB = toProtoMatrix(b);

        MatrixRequest request = MatrixRequest.newBuilder()
                .setMatrixA(protoA)
                .setMatrixB(protoB)
                .build();

        try {
            MatrixResponse response = blockingStub.matrixSubtract(request);
            return fromProtoMatrix(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("matrixSubtract", e);
        }
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        MatrixData protoA = toProtoMatrix(a);
        MatrixData protoB = toProtoMatrix(b);

        MatrixRequest request = MatrixRequest.newBuilder()
                .setMatrixA(protoA)
                .setMatrixB(protoB)
                .build();

        try {
            MatrixResponse response = blockingStub.matrixMultiply(request);
            return fromProtoMatrix(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("matrixMultiply", e);
        }
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        SingleMatrixRequest request = SingleMatrixRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .build();

        try {
            MatrixResponse response = blockingStub.matrixTranspose(request);
            return fromProtoMatrix(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("matrixTranspose", e);
        }
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        SingleMatrixRequest request = SingleMatrixRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .build();

        try {
            MatrixResponse response = blockingStub.matrixInverse(request);
            return fromProtoMatrix(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("matrixInverse", e);
        }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        ScaleRequest.Builder builder = ScaleRequest.newBuilder()
                .setMatrix(toProtoMatrix(a));
        
        if (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            builder.setScalar(c.real())
                   .setImaginary(c.imaginary())
                   .setIsComplex(true);
        } else if (scalar instanceof org.episteme.core.mathematics.numbers.real.RealBig rb) {
            builder.setHpScalar(rb.toString())
                   .setIsComplex(false);
        } else {
            builder.setScalar(toDouble(scalar))
                   .setIsComplex(false);
        }

        try {
            MatrixResponse response = blockingStub.matrixScale(builder.build());
            return fromProtoMatrix(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("matrixScale", e);
        }
    }

    @Override
    public E determinant(Matrix<E> a) {
        SingleMatrixRequest request = SingleMatrixRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .build();

        try {
            ScalarResponse response = blockingStub.matrixDeterminant(request);
            return fromProtoScalar(response);
        } catch (StatusRuntimeException e) {
            throw mapException("matrixDeterminant", e);
        }
    }

    // ==================== Vector Operations ====================

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        VectorRequest request = VectorRequest.newBuilder()
                .setVectorA(toProtoVector(a))
                .setVectorB(toProtoVector(b))
                .build();

        try {
            VectorResponse response = blockingStub.vectorAdd(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("vectorAdd", e);
        }
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        VectorRequest request = VectorRequest.newBuilder()
                .setVectorA(toProtoVector(a))
                .setVectorB(toProtoVector(b))
                .build();

        try {
            VectorResponse response = blockingStub.vectorSubtract(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("vectorSubtract", e);
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> v, E scalar) {
        VectorScaleRequest.Builder builder = VectorScaleRequest.newBuilder()
                .setVector(toProtoVector(v));
        
        if (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            builder.setScalar(c.real())
                   .setImaginary(c.imaginary())
                   .setIsComplex(true);
        } else if (scalar instanceof org.episteme.core.mathematics.numbers.real.RealBig rb) {
            builder.setHpScalar(rb.toString())
                   .setIsComplex(false);
        } else {
            builder.setScalar(toDouble(scalar))
                   .setIsComplex(false);
        }

        try {
            VectorResponse response = blockingStub.vectorScale(builder.build());
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("vectorScale", e);
        }
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        VectorRequest request = VectorRequest.newBuilder()
                .setVectorA(toProtoVector(a))
                .setVectorB(toProtoVector(b))
                .build();

        try {
            ScalarResponse response = blockingStub.vectorDot(request);
            return fromProtoScalar(response);
        } catch (StatusRuntimeException e) {
            throw mapException("vectorDot", e);
        }
    }

    @Override
    public E norm(Vector<E> a) {
        SingleVectorRequest request = SingleVectorRequest.newBuilder()
                .setVector(toProtoVector(a))
                .build();

        try {
            ScalarResponse response = blockingStub.vectorNorm(request);
            return fromProtoScalar(response);
        } catch (StatusRuntimeException e) {
            throw mapException("vectorNorm", e);
        }
    }

    // ==================== Matrix-Vector Operations ====================

    @Override
    public Vector<E> multiply(Matrix<E> m, Vector<E> v) {
        MatrixVectorRequest request = MatrixVectorRequest.newBuilder()
                .setMatrix(toProtoMatrix(m))
                .setVector(toProtoVector(v))
                .build();

        try {
            VectorResponse response = blockingStub.matrixVectorMultiply(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("matrixVectorMultiply", e);
        }
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        MatrixVectorRequest request = MatrixVectorRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .setVector(toProtoVector(b))
                .build();

        try {
            VectorResponse response = blockingStub.linearSolve(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("linearSolve", e);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) {
        try {
            LUResponse response = blockingStub.matrixLU(SingleMatrixRequest.newBuilder().setMatrix(toProtoMatrix(a)).build());
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(
                fromProtoMatrix(response.getL()),
                fromProtoMatrix(response.getU()),
                fromProtoVector(response.getP())
            );
        } catch (StatusRuntimeException e) {
            throw mapException("matrixLU", e);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) {
        try {
            QRResponse response = blockingStub.matrixQR(SingleMatrixRequest.newBuilder().setMatrix(toProtoMatrix(a)).build());
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(
                fromProtoMatrix(response.getQ()),
                fromProtoMatrix(response.getR())
            );
        } catch (StatusRuntimeException e) {
            throw mapException("matrixQR", e);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) {
        try {
            SVDResponse response = blockingStub.matrixSVD(SingleMatrixRequest.newBuilder().setMatrix(toProtoMatrix(a)).build());
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
                fromProtoMatrix(response.getU()),
                fromProtoVector(response.getS()),
                fromProtoMatrix(response.getV())
            );
        } catch (StatusRuntimeException e) {
            throw mapException("matrixSVD", e);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) {
        try {
            CholeskyResponse response = blockingStub.matrixCholesky(SingleMatrixRequest.newBuilder().setMatrix(toProtoMatrix(a)).build());
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(
                fromProtoMatrix(response.getL())
            );
        } catch (StatusRuntimeException e) {
            throw mapException("matrixCholesky", e);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
        try {
            EigenResponse response = blockingStub.matrixEigen(SingleMatrixRequest.newBuilder().setMatrix(toProtoMatrix(a)).build());
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                fromProtoMatrix(response.getV()),
                fromProtoVector(response.getD())
            );
        } catch (StatusRuntimeException e) {
            throw mapException("matrixEigen", e);
        }
    }

    // ==================== Conversion Utilities ====================

    private MatrixData toProtoMatrix(Matrix<E> matrix) {
        int rows = matrix.rows();
        int cols = matrix.cols();
        
        // Correctly detect complex ring
        Object zero = matrix.getScalarRing().zero();
        boolean isComplex = zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        boolean isHPContext = org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
        String zeroClass = zero.getClass().getName();
        boolean isHPRing = zeroClass.contains("RealBig") || zeroClass.contains("Complex");
        boolean finalIsHP = isHPContext || isHPRing;

        MatrixData.Builder builder = MatrixData.newBuilder()
                .setRows(rows)
                .setCols(cols)
                .setIsComplex(isComplex);

        if (finalIsHP) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    E val = matrix.get(i, j);
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) val;
                        builder.addHpData(c.getReal().toString());
                        builder.addHpData(c.getImaginary().toString());
                    } else {
                        builder.addHpData(val.toString());
                    }
                }
            }
        } else {
            int multiplier = isComplex ? 2 : 1;
            ByteBuffer bb = ByteBuffer.allocate(rows * cols * 8 * multiplier).order(ByteOrder.LITTLE_ENDIAN);
            DoubleBuffer db = bb.asDoubleBuffer();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) matrix.get(i, j);
                        db.put(c.getReal().doubleValue());
                        db.put(c.getImaginary().doubleValue());
                    } else {
                        db.put(toDouble(matrix.get(i, j)));
                    }
                }
            }
            builder.setData(ByteString.copyFrom(bb));
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromProtoMatrix(MatrixData data) {
        int rows = data.getRows();
        int cols = data.getCols();
        boolean isComplex = data.getIsComplex();
        
        if (!data.getHpDataList().isEmpty()) {
            List<String> hpData = data.getHpDataList();
            List<List<E>> matrixRows = new ArrayList<>();
            // Use RealBig.ZERO or Complex for ring
            Object ring = isComplex ? org.episteme.core.mathematics.numbers.complex.Complex.of(RealBig.ZERO, RealBig.ZERO) : RealBig.ZERO;
            
            org.episteme.core.mathematics.context.MathContext.exact().compute(() -> {
                int idx = 0;
                for (int i = 0; i < rows; i++) {
                    List<E> row = new ArrayList<>();
                    for (int j = 0; j < cols; j++) {
                        if (isComplex) {
                            Real re = RealBig.of(hpData.get(idx++));
                            Real im = RealBig.of(hpData.get(idx++));
                            row.add((E) org.episteme.core.mathematics.numbers.complex.Complex.of(re, im));
                        } else {
                            String s = hpData.get(idx++);
                            Real val = RealBig.of(s);
                            row.add((E) val);
                        }
                    }
                    matrixRows.add(row);
                }
                return null;
            });
            return DenseMatrix.of(matrixRows, (Field<E>) ring);
        }

        ByteString byteData = data.getData();
        int multiplier = isComplex ? 2 : 1;
        double[] raw = new double[rows * cols * multiplier];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(raw);

        List<List<E>> matrixRows = new ArrayList<>();
        int dataIdx = 0;

        for (int i = 0; i < rows; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
                    double re = raw[dataIdx++];
                    double im = raw[dataIdx++];
                    row.add((E) org.episteme.core.mathematics.numbers.complex.Complex.of(Real.of(re), Real.of(im)));
                } else {
                    row.add((E) Real.of(raw[dataIdx++]));
                }
            }
            matrixRows.add(row);
        }

        return DenseMatrix.of(matrixRows, field);
    }

    private VectorData toProtoVector(Vector<E> vector) {
        int size = vector.dimension();
        
        // Correctly detect complex ring
        Object zero = vector.getScalarRing().zero();
        boolean isComplex = zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        boolean isHPContext = org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
        String zeroClass = zero.getClass().getName();
        boolean isHPRing = zeroClass.contains("RealBig") || zeroClass.contains("Complex");
        boolean isHP = isHPContext || isHPRing;


        VectorData.Builder builder = VectorData.newBuilder()
                .setSize(size)
                .setIsComplex(isComplex);

        if (isHP) {
            for (int i = 0; i < size; i++) {
                E val = vector.get(i);
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) val;
                    builder.addHpData(c.getReal().toString());
                    builder.addHpData(c.getImaginary().toString());
                } else {
                    builder.addHpData(val.toString());
                }
            }
        } else {
            int multiplier = isComplex ? 2 : 1;
            ByteBuffer bb = ByteBuffer.allocate(size * 8 * multiplier).order(ByteOrder.LITTLE_ENDIAN);
            DoubleBuffer db = bb.asDoubleBuffer();

            for (int i = 0; i < size; i++) {
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) vector.get(i);
                    db.put(c.getReal().doubleValue());
                    db.put(c.getImaginary().doubleValue());
                } else {
                    db.put(toDouble(vector.get(i)));
                }
            }
            builder.setData(ByteString.copyFrom(bb));
        }
        return builder.build();
    }
    
    @SuppressWarnings("unchecked")
    private Vector<E> fromProtoVector(VectorData data) {
        int size = data.getSize();
        boolean isComplex = data.getIsComplex();
        
        if (!data.getHpDataList().isEmpty()) {
            List<String> hpData = data.getHpDataList();
            List<E> elements = new ArrayList<>();
            // Use RealBig.ZERO or Complex for ring
            Object ring = isComplex ? org.episteme.core.mathematics.numbers.complex.Complex.of(RealBig.ZERO, RealBig.ZERO) : RealBig.ZERO;
            
            org.episteme.core.mathematics.context.MathContext.exact().compute(() -> {
                int idx = 0;
                while (idx < hpData.size()) {
                    if (isComplex) {
                        Real re = RealBig.of(hpData.get(idx++));
                        Real im = RealBig.of(hpData.get(idx++));
                        elements.add((E) org.episteme.core.mathematics.numbers.complex.Complex.of(re, im));
                    } else {
                        elements.add((E) RealBig.of(hpData.get(idx++)));
                    }
                }
                return null;
            });
            return DenseVector.of(elements, (Field<E>) ring);
        }

        ByteString byteData = data.getData();
        int multiplier = isComplex ? 2 : 1;
        double[] raw = new double[size * multiplier];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(raw);
        
        List<E> elements = new ArrayList<>();
        int dataIdx = 0;
        for (int i = 0; i < size; i++) {
            if (isComplex) {
                double re = raw[dataIdx++];
                double im = raw[dataIdx++];
                elements.add((E) org.episteme.core.mathematics.numbers.complex.Complex.of(Real.of(re), Real.of(im)));
            } else {
                elements.add((E) Real.of(raw[dataIdx++]));
            }
        }

        return DenseVector.of(elements, field);
    }

    @SuppressWarnings("unchecked")
    private E fromProtoScalar(ScalarResponse response) {
        if (!response.getHpValue().isEmpty()) {
            return org.episteme.core.mathematics.context.MathContext.exact().compute(() -> {
                String val = response.getHpValue();
                if (response.getIsComplex() && val.contains(";")) {
                    String[] parts = val.split(";");
                    return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(
                        RealBig.of(parts[0]), RealBig.of(parts[1]));
                }
                return (E) org.episteme.core.mathematics.numbers.real.RealBig.of(val);
            });
        }
        if (response.getIsComplex()) {
            return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(
                Real.of(response.getValue()), Real.of(response.getImaginary()));
        } else {
            return (E) Real.of(response.getValue());
        }
    }

    private ScalarResponse toProtoScalar(E scalar) {
        ScalarResponse.Builder builder = ScalarResponse.newBuilder();
        if (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            builder.setValue(c.getReal().doubleValue())
                   .setImaginary(c.getImaginary().doubleValue())
                   .setIsComplex(true);
        } else if (scalar instanceof org.episteme.core.mathematics.numbers.real.RealBig rb) {
            builder.setHpValue(rb.toString())
                   .setIsComplex(false);
        } else {
            builder.setValue(toDouble(scalar))
                   .setIsComplex(false);
        }
        return builder.build();
    }

    private double toDouble(E value) {
        if (value instanceof Real) {
            return ((Real) value).doubleValue();
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to double for network transfer");
    }

    // ==================== Sparse Iterative Solvers ====================
    
    @Override
    public org.episteme.core.mathematics.linearalgebra.Vector<E> bicgstab(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, org.episteme.core.mathematics.linearalgebra.Vector<E> b, org.episteme.core.mathematics.linearalgebra.Vector<E> x0, E tolerance, int maxIterations) {
        IterativeSolverRequest request = IterativeSolverRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .setB(toProtoVector(b))
                .setX0(toProtoVector(x0))
                .setTolerance(toProtoScalar(tolerance))
                .setMaxIterations(maxIterations)
                .build();
        try {
            VectorResponse response = blockingStub.biCGSTAB(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("biCGSTAB", e);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.Vector<E> conjugateGradient(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, org.episteme.core.mathematics.linearalgebra.Vector<E> b, org.episteme.core.mathematics.linearalgebra.Vector<E> x0, E tolerance, int maxIterations) {
        IterativeSolverRequest request = IterativeSolverRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .setB(toProtoVector(b))
                .setX0(toProtoVector(x0))
                .setTolerance(toProtoScalar(tolerance))
                .setMaxIterations(maxIterations)
                .build();
        try {
            VectorResponse response = blockingStub.conjugateGradient(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("conjugateGradient", e);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.Vector<E> gmres(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, org.episteme.core.mathematics.linearalgebra.Vector<E> b, org.episteme.core.mathematics.linearalgebra.Vector<E> x0, E tolerance, int maxIterations, int restart) {
        GMRESRequest request = GMRESRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .setB(toProtoVector(b))
                .setX0(toProtoVector(x0))
                .setTolerance(toProtoScalar(tolerance))
                .setMaxIterations(maxIterations)
                .setRestart(restart)
                .build();
        try {
            VectorResponse response = blockingStub.gMRES(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw mapException("gmres", e);
        }
    }
}
