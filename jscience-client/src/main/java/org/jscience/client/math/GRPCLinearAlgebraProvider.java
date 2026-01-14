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

package org.jscience.client.math;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.jscience.server.proto.*;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.Operation;

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
public class GRPCLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final ManagedChannel channel;
    private final MatrixServiceGrpc.MatrixServiceBlockingStub blockingStub;
    private final Field<E> field;
    private final String serverAddress;

    /**
     * Creates a gRPC provider connected to the specified server.
     *
     * @param host  Server hostname or IP
     * @param port  Server port
     * @param field The field for element operations
     */
    public GRPCLinearAlgebraProvider(String host, int port, Field<E> field) {
        this.serverAddress = host + ":" + port;
        this.field = field;
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = MatrixServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public String getName() {
        return "gRPC Remote (" + serverAddress + ")";
    }

    @Override
    public boolean isAvailable() {
        return channel != null && !channel.isShutdown() && !channel.isTerminated();
    }

    @Override
    public ExecutionContext createContext() {
        return new GRPCExecutionContext();
    }

    /**
     * Shuts down the gRPC channel gracefully.
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
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

    // ==================== Conversion Utilities ====================

    private MatrixData toProtoMatrix(Matrix<E> matrix) {
        MatrixData.Builder builder = MatrixData.newBuilder();
        int rows = matrix.rows();
        int cols = matrix.cols();
        builder.setRows(rows);
        builder.setCols(cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = matrix.get(i, j);
                builder.addData(toDouble(val));
            }
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromProtoMatrix(MatrixData data) {
        int rows = data.getRows();
        int cols = data.getCols();
        List<Double> raw = data.getDataList();

        List<List<E>> matrixRows = new ArrayList<>();
        int dataIdx = 0;

        for (int i = 0; i < rows; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                double val = raw.get(dataIdx++);
                row.add((E) Real.of(val));
            }
            matrixRows.add(row);
        }

        return DenseMatrix.of(matrixRows, field);
    }

    private VectorData toProtoVector(Vector<E> vector) {
        VectorData.Builder builder = VectorData.newBuilder();
        int size = vector.dimension();
        builder.setSize(size);

        for (int i = 0; i < size; i++) {
            E val = vector.get(i);
            builder.addData(toDouble(val));
        }
        return builder.build();
    }
    
    @SuppressWarnings("unchecked")
    private Vector<E> fromProtoVector(VectorData data) {
        List<Double> raw = data.getDataList();
        List<E> elements = new ArrayList<>();

        for (Double val : raw) {
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

    private class GRPCExecutionContext implements ExecutionContext {
        @Override
        public <T> T execute(Operation<T> operation) {
            // Remote execution context - operations are executed on the server
            return operation.compute(this);
        }


        @Override
        public void close() {
            // Context cleanup - nothing to do for gRPC
        }
    }
}
