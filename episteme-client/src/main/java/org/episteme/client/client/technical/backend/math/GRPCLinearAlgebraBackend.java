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

package org.episteme.client.client.technical.backend.math;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.episteme.server.server.proto.*;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import com.google.auto.service.AutoService;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;

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
@AutoService({LinearAlgebraBackend.class, LinearAlgebraProvider.class, Backend.class})
public class GRPCLinearAlgebraBackend<E> implements LinearAlgebraBackend<E>, Backend {

    private ManagedChannel channel;
    private MatrixServiceGrpc.MatrixServiceBlockingStub blockingStub;
    private final Field<E> field;
    private final String serverAddress;

    /**
     * Public no-arg constructor required by ServiceLoader.
     * Defaults to localhost:50051.
     */
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
        try {
            this.channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();
            this.blockingStub = MatrixServiceGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            // Log and allow null for isAvailable() check
            this.channel = null;
            this.blockingStub = null;
        }
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
        return "gRPC Remote (" + serverAddress + ")";
    }

    @Override
    public String getId() {
        return "grpc-math";
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



    /**
     * Shuts down the gRPC channel gracefully.
     */
    @Override
    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
            throw new RuntimeException("gRPC matrixAdd failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixSubtract failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixMultiply failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixTranspose failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixInverse failed: " + e.getStatus(), e);
        }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        double scalarValue = toDouble(scalar);
        
        ScaleRequest request = ScaleRequest.newBuilder()
                .setScalar(scalarValue)
                .setMatrix(toProtoMatrix(a))
                .build();

        try {
            MatrixResponse response = blockingStub.matrixScale(request);
            return fromProtoMatrix(response.getResult());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("gRPC matrixScale failed: " + e.getStatus(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        SingleMatrixRequest request = SingleMatrixRequest.newBuilder()
                .setMatrix(toProtoMatrix(a))
                .build();

        try {
            ScalarResponse response = blockingStub.matrixDeterminant(request);
            return (E) Real.of(response.getValue());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("gRPC matrixDeterminant failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC vectorAdd failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC vectorSubtract failed: " + e.getStatus(), e);
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> v, E scalar) {
        VectorScaleRequest request = VectorScaleRequest.newBuilder()
                .setVector(toProtoVector(v))
                .setScalar(toDouble(scalar))
                .build();

        try {
            VectorResponse response = blockingStub.vectorScale(request);
            return fromProtoVector(response.getResult());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("gRPC vectorScale failed: " + e.getStatus(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        VectorRequest request = VectorRequest.newBuilder()
                .setVectorA(toProtoVector(a))
                .setVectorB(toProtoVector(b))
                .build();

        try {
            ScalarResponse response = blockingStub.vectorDot(request);
            return (E) Real.of(response.getValue());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("gRPC vectorDot failed: " + e.getStatus(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        SingleVectorRequest request = SingleVectorRequest.newBuilder()
                .setVector(toProtoVector(a))
                .build();

        try {
            ScalarResponse response = blockingStub.vectorNorm(request);
            return (E) Real.of(response.getValue());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("gRPC vectorNorm failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixVectorMultiply failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC linearSolve failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixLU failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixQR failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixSVD failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixCholesky failed: " + e.getStatus(), e);
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
            throw new RuntimeException("gRPC matrixEigen failed: " + e.getStatus(), e);
        }
    }

    // ==================== Conversion Utilities ====================

    private MatrixData toProtoMatrix(Matrix<E> matrix) {
        int rows = matrix.rows();
        int cols = matrix.cols();
        ByteBuffer bb = ByteBuffer.allocate(rows * cols * 8).order(ByteOrder.LITTLE_ENDIAN);
        DoubleBuffer db = bb.asDoubleBuffer();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                db.put(toDouble(matrix.get(i, j)));
            }
        }
        return MatrixData.newBuilder()
                .setRows(rows)
                .setCols(cols)
                .setData(ByteString.copyFrom(bb))
                .build();
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromProtoMatrix(MatrixData data) {
        int rows = data.getRows();
        int cols = data.getCols();
        ByteString byteData = data.getData();
        
        double[] raw = new double[rows * cols];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(raw);

        List<List<E>> matrixRows = new ArrayList<>();
        int dataIdx = 0;

        for (int i = 0; i < rows; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add((E) Real.of(raw[dataIdx++]));
            }
            matrixRows.add(row);
        }

        return DenseMatrix.of(matrixRows, field);
    }

    private VectorData toProtoVector(Vector<E> vector) {
        int size = vector.dimension();
        ByteBuffer bb = ByteBuffer.allocate(size * 8).order(ByteOrder.LITTLE_ENDIAN);
        DoubleBuffer db = bb.asDoubleBuffer();

        for (int i = 0; i < size; i++) {
            db.put(toDouble(vector.get(i)));
        }

        return VectorData.newBuilder()
                .setSize(size)
                .setData(ByteString.copyFrom(bb))
                .build();
    }
    
    @SuppressWarnings("unchecked")
    private Vector<E> fromProtoVector(VectorData data) {
        int size = data.getSize();
        ByteString byteData = data.getData();
        
        double[] raw = new double[size];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(raw);
        
        List<E> elements = new ArrayList<>();
        for (double val : raw) {
            elements.add((E) Real.of(val));
        }

        return DenseVector.of(elements, field);
    }

    private double toDouble(E value) {
        if (value instanceof Real) {
            return ((Real) value).doubleValue();
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to double for network transfer");
    }

    // ==================== Execution Context ====================

}

