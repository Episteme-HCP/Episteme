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

package org.jscience.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jscience.server.proto.MatrixData;
import org.jscience.server.proto.MatrixRequest;
import org.jscience.server.proto.MatrixResponse;
import org.jscience.server.proto.MatrixServiceGrpc;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Server-side implementation of the MatrixService via gRPC.
 * Performs high-performance matrix operations using JScience core libraries.
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

            // 1. Convert Proto to JScience Matrices
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

        // Getting the full array copy from JScience might be cleanest for now
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
