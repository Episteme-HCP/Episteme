package org.jscience.mathematics.linearalgebra.backends;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jscience.computing.remote.grpc.MatrixRequest;
import org.jscience.computing.remote.grpc.MatrixResponse;
import org.jscience.computing.remote.grpc.MatrixServiceGrpc;
import org.jscience.computing.remote.grpc.MatrixData;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.numbers.real.Real; // MVP limitation: Real only
import org.jscience.mathematics.structures.rings.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * A LinearAlgebraProvider that offloads operations to a remote gRPC service.
 * <p>
 * This implementation connects to a 'MatrixService' running on a remote host
 * (e.g. GPU cluster).
 * Currently supports dense matrix multiplication for Double precision Reals.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
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
        // Limitation: Only supporting Real/Double for MVP serialization
        if (!(left instanceof DenseMatrix) || !(right instanceof DenseMatrix)) {
            throw new UnsupportedOperationException("Only DenseMatrix supported for remote offloading");
        }

        // Serialize
        MatrixData protoA = toProto((DenseMatrix<E>) left);
        MatrixData protoB = toProto((DenseMatrix<E>) right);

        MatrixRequest request = MatrixRequest.newBuilder()
                .setMatrixA(protoA)
                .setMatrixB(protoB)
                .build();

        // Call RPC
        MatrixResponse response;
        try {
            response = blockingStub.matrixMultiply(request);
        } catch (Exception e) {
            throw new RuntimeException("RPC failed", e);
        }

        // Deserialize
        return fromProto(response.getResult());
    }

    // --- Helper Methods (MVP: Double mapping) ---

    private MatrixData toProto(DenseMatrix<E> matrix) {
        MatrixData.Builder builder = MatrixData.newBuilder();
        builder.setRows(matrix.rows());
        builder.setCols(matrix.cols());

        // Assume double for now
        // In real impl, checking E type or using bytes
        int rows = matrix.rows();
        int cols = matrix.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = matrix.get(i, j);
                if (val instanceof Real) {
                    builder.addData(((Real) val).doubleValue());
                } else {
                    // Fallback or error
                    builder.addData(0.0); // Simplistic
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
                // Assume Real
                // Need factory or casting: (E) Real.of(val)
                // This generic Unchecked cast is unavoidable in this MVP design
                // without passing a factory.
                // Assuming field has zero(), try to create from value?
                // Ideally LinearAlgebraProvider has 'create(double)' method.
                // For now, hardcode Real usage logic just for demo.
                row.add((E) Real.of(val));
            }
            matrixRows.add(row);
        }

        return DenseMatrix.of(matrixRows, field);
    }

    // --- Unimplemented Stubs ---
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
        return new org.jscience.technical.backend.ExecutionContext() {
            @Override
            public void close() {
                // No specific resource release needed for now per-context
            }

            @Override
            public <T> T execute(org.jscience.technical.backend.Operation<T> operation) {
                // Basic execution - directly run the operation with this context
                return operation.compute(this);
            }
        };
    }

    @Override
    public boolean isAvailable() {
        // Check if channel is not null and not shutdown
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
}
