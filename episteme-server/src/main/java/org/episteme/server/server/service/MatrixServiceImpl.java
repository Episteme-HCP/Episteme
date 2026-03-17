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

package org.episteme.server.server.service;

import io.grpc.stub.StreamObserver;
import java.nio.DoubleBuffer;
import net.devh.boot.grpc.server.service.GrpcService;
import org.episteme.server.server.proto.*;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.numbers.real.Real;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * Server-side implementation of the MatrixService via gRPC.
 * Performs high-performance matrix operations using Episteme core libraries.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@GrpcService
public class MatrixServiceImpl extends MatrixServiceGrpc.MatrixServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(MatrixServiceImpl.class);

    @Override
    public void matrixMultiply(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            LOG.info("Received Matrix Multiplication request");

            // 1. Convert Proto to Episteme Matrices
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> matrixA = fromProto(request.getMatrixA());
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> matrixB = fromProto(request.getMatrixB());

            LOG.debug("Multiplying matrices: [{}x{}] * [{}x{}]",
                    matrixA.rows(), matrixA.cols(), matrixB.rows(), matrixB.cols());

            // 2. Perform Multiplication
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> resultMatrix = matrixA.multiply(matrixB);

            // 3. Convert Result to Proto
            MatrixData resultData = toProto(resultMatrix);

            MatrixResponse response = MatrixResponse.newBuilder()
                    .setResult(resultData)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

            LOG.info("Matrix Multiplication completed successfully");

        } catch (Exception e) {
            LOG.error("Error during matrix multiplication", e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Matrix multiplication failed: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException());
        }
    }

    @Override
    public void matrixAdd(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> result = fromProto(request.getMatrixA()).add(fromProto(request.getMatrixB()));
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixAdd", e, responseObserver);
        }
    }

    @Override
    public void matrixSubtract(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> result = fromProto(request.getMatrixA()).subtract(fromProto(request.getMatrixB()));
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixSubtract", e, responseObserver);
        }
    }

    @Override
    public void matrixTranspose(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> result = fromProto(request.getMatrix()).transpose();
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixTranspose", e, responseObserver);
        }
    }

    @Override
    public void matrixInverse(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> result = fromProto(request.getMatrix()).inverse();
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixInverse", e, responseObserver);
        }
    }

    @Override
    public void matrixScale(ScaleRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> matrix = fromProto(request.getMatrix());
            org.episteme.core.mathematics.linearalgebra.Matrix<Real> result = matrix.scale(Real.of(request.getScalar()), matrix);
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixScale", e, responseObserver);
        }
    }

    @Override
    public void matrixDeterminant(SingleMatrixRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            double det = fromProto(request.getMatrix()).determinant().doubleValue();
            responseObserver.onNext(ScalarResponse.newBuilder().setValue(det).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void vectorAdd(VectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Vector<Real> result = fromProto(request.getVectorA()).add(fromProto(request.getVectorB()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void vectorSubtract(VectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Vector<Real> result = fromProto(request.getVectorA()).subtract(fromProto(request.getVectorB()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void vectorScale(VectorScaleRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Vector<Real> result = fromProto(request.getVector()).multiply(Real.of(request.getScalar()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void vectorDot(VectorRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            double dot = fromProto(request.getVectorA()).dot(fromProto(request.getVectorB())).doubleValue();
            responseObserver.onNext(ScalarResponse.newBuilder().setValue(dot).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void vectorNorm(SingleVectorRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            double norm = fromProto(request.getVector()).norm().doubleValue();
            responseObserver.onNext(ScalarResponse.newBuilder().setValue(norm).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void matrixVectorMultiply(MatrixVectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Vector<Real> result = fromProto(request.getMatrix()).multiply(fromProto(request.getVector()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void linearSolve(MatrixVectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.Vector<Real> result = fromProto(request.getMatrix()).solve(fromProto(request.getVector()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void matrixLU(SingleMatrixRequest request, StreamObserver<LUResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<Real> result = 
                fromProto(request.getMatrix()).lu();
            responseObserver.onNext(LUResponse.newBuilder()
                .setL(toProto(result.L()))
                .setU(toProto(result.U()))
                .setP(toProto(result.P()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void matrixQR(SingleMatrixRequest request, StreamObserver<QRResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<Real> result = 
                fromProto(request.getMatrix()).qr();
            responseObserver.onNext(QRResponse.newBuilder()
                .setQ(toProto(result.Q()))
                .setR(toProto(result.R()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void matrixSVD(SingleMatrixRequest request, StreamObserver<SVDResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<Real> result = 
                fromProto(request.getMatrix()).svd();
            responseObserver.onNext(SVDResponse.newBuilder()
                .setU(toProto(result.U()))
                .setS(toProto(result.S()))
                .setV(toProto(result.V()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void matrixCholesky(SingleMatrixRequest request, StreamObserver<CholeskyResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> result = 
                fromProto(request.getMatrix()).cholesky();
            responseObserver.onNext(CholeskyResponse.newBuilder()
                .setL(toProto(result.L()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void matrixEigen(SingleMatrixRequest request, StreamObserver<EigenResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> result = 
                fromProto(request.getMatrix()).eigen();
            responseObserver.onNext(EigenResponse.newBuilder()
                .setV(toProto(result.V()))
                .setD(toProto(result.D()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    private void handleError(String op, Exception e, StreamObserver<?> responseObserver) {
        LOG.error("Error during " + op, e);
        responseObserver.onError(
                io.grpc.Status.INTERNAL
                        .withDescription(op + " failed: " + e.getMessage())
                        .withCause(e)
                        .asRuntimeException());
    }

    private RealDoubleMatrix fromProto(MatrixData proto) {
        int rows = proto.getRows();
        int cols = proto.getCols();
        ByteString byteData = proto.getData();
        
        double[] data = new double[rows * cols];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(data);

        return RealDoubleMatrix.of(data, rows, cols);
    }

    private RealDoubleVector fromProto(VectorData proto) {
        int size = proto.getSize();
        ByteString byteData = proto.getData();
        double[] data = new double[size];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(data);
        return RealDoubleVector.of(data);
    }

    private VectorData toProto(org.episteme.core.mathematics.linearalgebra.Vector<Real> vector) {
        int size = vector.dimension();
        ByteBuffer bb = ByteBuffer.allocate(size * 8).order(ByteOrder.LITTLE_ENDIAN);
        DoubleBuffer db = bb.asDoubleBuffer();
        for (int i = 0; i < size; i++) {
            db.put(vector.get(i).doubleValue());
        }
        return VectorData.newBuilder()
                .setSize(size)
                .setData(ByteString.copyFrom(bb))
                .build();
    }

    private MatrixData toProto(org.episteme.core.mathematics.linearalgebra.Matrix<Real> matrix) {
        int rows = matrix.rows();
        int cols = matrix.cols();

        MatrixData.Builder builder = MatrixData.newBuilder()
                .setRows(rows)
                .setCols(cols);

        ByteBuffer bb = ByteBuffer.allocate(rows * cols * 8).order(ByteOrder.LITTLE_ENDIAN);
        DoubleBuffer db = bb.asDoubleBuffer();

        if (matrix instanceof RealDoubleMatrix rdm) {
            db.put(rdm.getBuffer());
        } else {
            // Generic slow path for views (Transposed, etc.)
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    db.put(matrix.get(i, j).doubleValue());
                }
            }
        }
        builder.setData(ByteString.copyFrom(bb));
        return builder.build();
    }
}
