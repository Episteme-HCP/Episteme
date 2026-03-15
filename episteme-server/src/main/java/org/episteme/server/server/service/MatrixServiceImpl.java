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
import net.devh.boot.grpc.server.service.GrpcService;
import org.episteme.server.server.proto.*;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.numbers.real.Real;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
            RealDoubleMatrix matrixA = fromProto(request.getMatrixA());
            RealDoubleMatrix matrixB = fromProto(request.getMatrixB());

            LOG.debug("Multiplying matrices: [{}x{}] * [{}x{}]",
                    matrixA.rows(), matrixA.cols(), matrixB.rows(), matrixB.cols());

            // 2. Perform Multiplication
            RealDoubleMatrix resultMatrix = matrixA.multiply(matrixB);

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
            RealDoubleMatrix result = (RealDoubleMatrix) fromProto(request.getMatrixA()).add(fromProto(request.getMatrixB()));
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixAdd", e, responseObserver);
        }
    }

    @Override
    public void matrixSubtract(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            RealDoubleMatrix result = (RealDoubleMatrix) fromProto(request.getMatrixA()).subtract(fromProto(request.getMatrixB()));
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixSubtract", e, responseObserver);
        }
    }

    @Override
    public void matrixTranspose(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            RealDoubleMatrix result = (RealDoubleMatrix) fromProto(request.getMatrix()).transpose();
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixTranspose", e, responseObserver);
        }
    }

    @Override
    public void matrixInverse(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            RealDoubleMatrix result = (RealDoubleMatrix) fromProto(request.getMatrix()).inverse();
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixInverse", e, responseObserver);
        }
    }

    @Override
    public void matrixScale(ScaleRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            RealDoubleMatrix result = (RealDoubleMatrix) fromProto(request.getMatrix()).scale(Real.of(request.getScalar()), fromProto(request.getMatrix()));
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
            RealDoubleVector result = (RealDoubleVector) fromProto(request.getVectorA()).add(fromProto(request.getVectorB()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void vectorSubtract(VectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            RealDoubleVector result = (RealDoubleVector) fromProto(request.getVectorA()).subtract(fromProto(request.getVectorB()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void vectorScale(VectorScaleRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            RealDoubleVector result = (RealDoubleVector) fromProto(request.getVector()).multiply(Real.of(request.getScalar()));
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
            RealDoubleVector result = (RealDoubleVector) fromProto(request.getMatrix()).multiply(fromProto(request.getVector()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void linearSolve(MatrixVectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            RealDoubleVector result = fromProto(request.getMatrix()).solve((org.episteme.core.mathematics.linearalgebra.Vector<Real>) fromProto(request.getVector()));
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
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
        List<Double> dataList = proto.getDataList();

        // Convert List<Double> to double[]
        double[] data = new double[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }

        return RealDoubleMatrix.of(data, rows, cols);
    }

    private RealDoubleVector fromProto(VectorData proto) {
        double[] data = new double[proto.getSize()];
        List<Double> list = proto.getDataList();
        for (int i = 0; i < data.length; i++) data[i] = list.get(i);
        return RealDoubleVector.of(data);
    }

    private VectorData toProto(RealDoubleVector vector) {
        VectorData.Builder builder = VectorData.newBuilder().setSize(vector.dimension());
        double[] data = vector.toDoubleArray();
        for (double d : data) builder.addData(d);
        return builder.build();
    }

    private MatrixData toProto(RealDoubleMatrix matrix) {
        int rows = matrix.rows();
        int cols = matrix.cols();

        // Use getBuffer() or iterate. RealDoubleMatrix should support getting the
        // backing array or buffer.
        // If the storage is Heap, we might access the array. If Direct, we get
        // coordinates.
        // matrix.getDoubleStorage().toDoubleArray() is safe generic way if public.
        // Looking at RealDoubleMatrix source, doubleStorage is private but we can use
        // our helper logic.

        // Using optimized loop to avoid array copy overhead if possible,
        // but protobuf needs an Iterable or adding one by one.

        MatrixData.Builder builder = MatrixData.newBuilder()
                .setRows(rows)
                .setCols(cols);

        // Getting the full array copy from Episteme might be cleanest for now
        // Assuming toDoubleArray() copies:
        // But doubleStorage.toDoubleArray() might not be directly accessible if it's
        // not exposed on RealDoubleMatrix interface publicly?
        // RealDoubleMatrix has `DoubleBuffer getBuffer()`

        java.nio.DoubleBuffer buffer = matrix.getBuffer();
        if (buffer.hasArray()) {
            double[] arr = buffer.array();
            for (double d : arr) {
                builder.addData(d);
            }
        } else {
            // Fallback for Direct buffers or non-array backed
            // We can rewind and get
            buffer.rewind();
            while (buffer.hasRemaining()) {
                builder.addData(buffer.get());
            }
        }

        return builder.build();
    }
}

