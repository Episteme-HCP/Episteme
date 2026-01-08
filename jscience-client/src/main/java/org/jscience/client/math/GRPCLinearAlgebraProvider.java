/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
// Using server stubs (which are currently in core, but will be moved/regenerated)
// For now, assuming jscience-client will have access to stubs via jscience-server or own generation
import org.jscience.server.proto.MatrixRequest;
import org.jscience.server.proto.MatrixResponse;
import org.jscience.server.proto.MatrixServiceGrpc;
import org.jscience.server.proto.MatrixData;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A LinearAlgebraProvider that offloads operations to a remote gRPC service.
 * <p>
 * Moved from jscience-core to jscience-client to decouple core from gRPC.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GRPCLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final ManagedChannel channel;
    private final MatrixServiceGrpc.MatrixServiceBlockingStub blockingStub;
    private final Field<E> field;

    public GRPCLinearAlgebraProvider(String host, int port, Field<E> field) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = MatrixServiceGrpc.newBlockingStub(channel);
        this.field = field;
    }

    @Override
    public String getName() {
        return "gRPC Remote Provider";
    }

    @Override
    public Matrix<E> multiply(Matrix<E> left, Matrix<E> right) {
        if (!(left instanceof DenseMatrix) || !(right instanceof DenseMatrix)) {
            throw new UnsupportedOperationException("Only DenseMatrix supported for remote offloading");
        }

        MatrixData protoA = toProto((DenseMatrix<E>) left);
        MatrixData protoB = toProto((DenseMatrix<E>) right);

        MatrixRequest request = MatrixRequest.newBuilder()
                .setMatrixA(protoA)
                .setMatrixB(protoB)
                .build();

        MatrixResponse response;
        try {
            response = blockingStub.matrixMultiply(request);
        } catch (Exception e) {
            throw new RuntimeException("RPC failed", e);
        }

        return fromProto(response.getResult());
    }

    private MatrixData toProto(DenseMatrix<E> matrix) {
        MatrixData.Builder builder = MatrixData.newBuilder();
        builder.setRows(matrix.rows());
        builder.setCols(matrix.cols());

        int rows = matrix.rows();
        int cols = matrix.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = matrix.get(i, j);
                if (val instanceof Real) {
                    builder.addData(((Real) val).doubleValue());
                } else {
                    builder.addData(0.0);
                }
            }
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromProto(MatrixData data) {
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

    // Unimplemented methods omitted for brevity (same as before)
    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector<E> multiply(Vector<E> v, E s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E determinant(Matrix<E> a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public org.jscience.technical.backend.ExecutionContext createContext() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return channel != null && !channel.isShutdown();
    }

    @Override
    public Vector<E> multiply(Matrix<E> m, Vector<E> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getId() {
        return "grpc";
    }

    @Override
    public String getDescription() {
        return "GRPCLinearAlgebraProvider";
    }
}

