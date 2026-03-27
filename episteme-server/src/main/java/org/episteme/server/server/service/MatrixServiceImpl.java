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
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
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

            Matrix<?> matrixA = fromProto(request.getMatrixA());
            Matrix<?> matrixB = fromProto(request.getMatrixB());

            LOG.debug("Multiplying matrices: [{}x{}] * [{}x{}]",
                    matrixA.rows(), matrixA.cols(), matrixB.rows(), matrixB.cols());

            @SuppressWarnings("unchecked")
            Matrix<?> resultMatrix = ((Matrix<Object>)matrixA).multiply((Matrix<Object>)matrixB);

            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(resultMatrix)).build());
            responseObserver.onCompleted();

            LOG.info("Matrix Multiplication completed successfully");

        } catch (Exception e) {
            handleError("matrixMultiply", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixAdd(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            Matrix<Object> m1 = (Matrix<Object>) fromProto(request.getMatrixA());
            Matrix<Object> m2 = (Matrix<Object>) fromProto(request.getMatrixB());
            Matrix<?> result = m1.add(m2);
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixAdd", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixSubtract(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            Matrix<Object> m1 = (Matrix<Object>) fromProto(request.getMatrixA());
            Matrix<Object> m2 = (Matrix<Object>) fromProto(request.getMatrixB());
            Matrix<?> result = m1.subtract(m2);
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixSubtract", e, responseObserver);
        }
    }

    @Override
    public void matrixTranspose(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            Matrix<?> result = fromProto(request.getMatrix()).transpose();
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixTranspose", e, responseObserver);
        }
    }

    @Override
    public void matrixInverse(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            Matrix<?> result = fromProto(request.getMatrix()).inverse();
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixInverse", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixScale(ScaleRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
            Object scalar;
            if (request.getIsComplex()) {
                scalar = Complex.of(request.getScalar(), request.getImaginary());
            } else if (!request.getHpScalar().isEmpty()) {
                scalar = org.episteme.core.mathematics.numbers.real.RealBig.of(request.getHpScalar());
            } else {
                scalar = Real.of(request.getScalar());
            }
            Matrix<?> result = matrix.scale(scalar);
            responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixScale", e, responseObserver);
        }
    }

    @Override
    public void matrixDeterminant(SingleMatrixRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            Object det = fromProto(request.getMatrix()).determinant();
            responseObserver.onNext(toProtoScalar(det));
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixDeterminant", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorAdd(VectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Vector<Object> v1 = (Vector<Object>) fromProto(request.getVectorA());
            Vector<Object> v2 = (Vector<Object>) fromProto(request.getVectorB());
            Vector<?> result = v1.add(v2);
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("vectorAdd", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorSubtract(VectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Vector<Object> v1 = (Vector<Object>) fromProto(request.getVectorA());
            Vector<Object> v2 = (Vector<Object>) fromProto(request.getVectorB());
            Vector<?> result = v1.subtract(v2);
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("vectorSubtract", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorScale(VectorScaleRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Vector<Object> vector = (Vector<Object>) fromProto(request.getVector());
            Object scalar;
            if (request.getIsComplex()) {
               scalar = Complex.of(request.getScalar(), request.getImaginary());
            } else if (!request.getHpScalar().isEmpty()) {
               scalar = org.episteme.core.mathematics.numbers.real.RealBig.of(request.getHpScalar());
            } else {
               scalar = Real.of(request.getScalar());
            }
            Vector<?> result = vector.multiply(scalar);
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("vectorScale", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorDot(VectorRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            Object dot = ((Vector<Object>)fromProto(request.getVectorA())).dot((Vector<Object>)fromProto(request.getVectorB()));
            responseObserver.onNext(toProtoScalar(dot));
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("vectorDot", e, responseObserver);
        }
    }

    @Override
    public void vectorNorm(SingleVectorRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            Object norm = fromProto(request.getVector()).norm();
            responseObserver.onNext(toProtoScalar(norm));
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("vectorNorm", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixVectorMultiply(MatrixVectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Matrix<Object> matrix = (Matrix<Object>)fromProto(request.getMatrix());
            Vector<Object> vector = (Vector<Object>)fromProto(request.getVector());
            Vector<?> result = matrix.multiply(vector);
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixVectorMultiply", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void linearSolve(MatrixVectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
            Vector<Object> vector = (Vector<Object>) fromProto(request.getVector());
            Vector<Object> result = matrix.getProvider().solve(matrix, vector);
            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("linearSolve", e, responseObserver);
        }
    }

    @Override
    public void matrixLU(SingleMatrixRequest request, StreamObserver<LUResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<?> result = 
                fromProto(request.getMatrix()).lu();
            responseObserver.onNext(LUResponse.newBuilder()
                .setL(toProto(result.L()))
                .setU(toProto(result.U()))
                .setP(toProto(result.P()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixLU", e, responseObserver);
        }
    }

    @Override
    public void matrixQR(SingleMatrixRequest request, StreamObserver<QRResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<?> result = 
                fromProto(request.getMatrix()).qr();
            responseObserver.onNext(QRResponse.newBuilder()
                .setQ(toProto(result.Q()))
                .setR(toProto(result.R()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixQR", e, responseObserver);
        }
    }

    @Override
    public void matrixSVD(SingleMatrixRequest request, StreamObserver<SVDResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<?> result = 
                fromProto(request.getMatrix()).svd();
            responseObserver.onNext(SVDResponse.newBuilder()
                .setU(toProto(result.U()))
                .setS(toProto(result.S()))
                .setV(toProto(result.V()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixSVD", e, responseObserver);
        }
    }

    @Override
    public void matrixCholesky(SingleMatrixRequest request, StreamObserver<CholeskyResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<?> result = 
                fromProto(request.getMatrix()).cholesky();
            responseObserver.onNext(CholeskyResponse.newBuilder()
                .setL(toProto(result.L()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixCholesky", e, responseObserver);
        }
    }

    @Override
    public void matrixEigen(SingleMatrixRequest request, StreamObserver<EigenResponse> responseObserver) {
        try {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<?> result = 
                fromProto(request.getMatrix()).eigen();
            responseObserver.onNext(EigenResponse.newBuilder()
                .setV(toProto(result.V()))
                .setD(toProto(result.D()))
                .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("matrixEigen", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void biCGSTAB(IterativeSolverRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
            Vector<Object> b = (Vector<Object>) fromProto(request.getB());
            Vector<Object> x0 = (Vector<Object>) fromProto(request.getX0());
            Object tol = fromProtoScalar(request.getTolerance());
            int maxIter = request.getMaxIterations();

            org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<Object> provider = 
                (org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<Object>) matrix.getProvider();
            Vector<Object> result = provider.bicgstab(matrix, b, x0, tol, maxIter);

            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("biCGSTAB", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void conjugateGradient(IterativeSolverRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
            Vector<Object> b = (Vector<Object>) fromProto(request.getB());
            Vector<Object> x0 = (Vector<Object>) fromProto(request.getX0());
            Object tol = fromProtoScalar(request.getTolerance());
            int maxIter = request.getMaxIterations();

            org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<Object> provider = 
                (org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<Object>) matrix.getProvider();
            Vector<Object> result = provider.conjugateGradient(matrix, b, x0, tol, maxIter);

            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("conjugateGradient", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void gMRES(GMRESRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
            Vector<Object> b = (Vector<Object>) fromProto(request.getB());
            Vector<Object> x0 = (Vector<Object>) fromProto(request.getX0());
            Object tol = fromProtoScalar(request.getTolerance());
            int maxIter = request.getMaxIterations();
            int restart = request.getRestart();

            org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<Object> provider = 
                (org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<Object>) matrix.getProvider();
            Vector<Object> result = provider.gmres(matrix, b, x0, tol, maxIter, restart);

            responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError("gMRES", e, responseObserver);
        }
    }

    private Object fromProtoScalar(ScalarResponse response) {
        if (!response.getHpValue().isEmpty()) {
            return org.episteme.core.mathematics.numbers.real.RealBig.of(response.getHpValue());
        }
        if (response.getIsComplex()) {
            return Complex.of(response.getValue(), response.getImaginary());
        } else {
            return Real.of(response.getValue());
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

    private Matrix<?> fromProto(MatrixData proto) {
        int rows = proto.getRows();
        int cols = proto.getCols();
        boolean isComplex = proto.getIsComplex();
        
        if (!proto.getHpDataList().isEmpty()) {
            List<String> hpData = proto.getHpDataList();
            org.episteme.core.mathematics.numbers.real.Real[][] raw = new org.episteme.core.mathematics.numbers.real.Real[rows][cols];
            int idx = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    raw[i][j] = org.episteme.core.mathematics.numbers.real.RealBig.of(hpData.get(idx++));
                }
            }
            return org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix.of(raw, org.episteme.core.mathematics.numbers.real.RealBig.ZERO);
        }

        ByteString byteData = proto.getData();
        int multiplier = isComplex ? 2 : 1;
        double[] raw = new double[rows * cols * multiplier];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(raw);

        if (isComplex) {
            List<List<Complex>> data = new ArrayList<>(rows);
            int idx = 0;
            for (int i = 0; i < rows; i++) {
                List<Complex> row = new ArrayList<>(cols);
                for (int j = 0; j < cols; j++) {
                    row.add(Complex.of(raw[idx++], raw[idx++]));
                }
                data.add(row);
            }
            return DenseMatrix.of(data, Complex.ZERO);
        } else {
            return RealDoubleMatrix.of(raw, rows, cols);
        }
    }

    private Vector<?> fromProto(VectorData proto) {
        int size = proto.getSize();
        boolean isComplex = proto.getIsComplex();

        if (!proto.getHpDataList().isEmpty()) {
            List<String> hpData = proto.getHpDataList();
            List<org.episteme.core.mathematics.numbers.real.Real> data = new ArrayList<>(size);
            for (String s : hpData) {
                data.add(org.episteme.core.mathematics.numbers.real.RealBig.of(s));
            }
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(data, org.episteme.core.mathematics.numbers.real.RealBig.ZERO);
        }

        ByteString byteData = proto.getData();
        int multiplier = isComplex ? 2 : 1;
        double[] raw = new double[size * multiplier];
        byteData.asReadOnlyByteBuffer().order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(raw);

        if (isComplex) {
            List<Complex> data = new ArrayList<>(size);
            int idx = 0;
            for (int i = 0; i < size; i++) {
                data.add(Complex.of(raw[idx++], raw[idx++]));
            }
            return DenseVector.of(data, Complex.ZERO);
        } else {
            return RealDoubleVector.of(raw);
        }
    }

    private MatrixData toProto(Matrix<?> matrix) {
        int rows = matrix.rows();
        int cols = matrix.cols();
        boolean isComplex = matrix.getScalarRing() instanceof Complex;
        boolean isHP = matrix.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig;

        MatrixData.Builder builder = MatrixData.newBuilder()
                .setRows(rows)
                .setCols(cols)
                .setIsComplex(isComplex);

        if (isHP && !isComplex) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    builder.addHpData(matrix.get(i, j).toString());
                }
            }
        } else {
            int multiplier = isComplex ? 2 : 1;
            ByteBuffer bb = ByteBuffer.allocate(rows * cols * 8 * multiplier).order(ByteOrder.LITTLE_ENDIAN);
            DoubleBuffer db = bb.asDoubleBuffer();

            if (matrix instanceof RealDoubleMatrix rdm && !isComplex) {
                db.put(rdm.getBuffer());
            } else {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        Object val = matrix.get(i, j);
                        if (isComplex) {
                            Complex c = (Complex) val;
                            db.put(c.real());
                            db.put(c.imaginary());
                        } else {
                            db.put(((Real)val).doubleValue());
                        }
                    }
                }
            }
            builder.setData(ByteString.copyFrom(bb));
        }
        return builder.build();
    }

    private VectorData toProto(Vector<?> vector) {
        int size = vector.dimension();
        boolean isComplex = vector.getScalarRing() instanceof Complex;
        boolean isHP = vector.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig;

        VectorData.Builder builder = VectorData.newBuilder()
                .setSize(size)
                .setIsComplex(isComplex);

        if (isHP && !isComplex) {
            for (int i = 0; i < size; i++) {
                builder.addHpData(vector.get(i).toString());
            }
        } else {
            int multiplier = isComplex ? 2 : 1;
            ByteBuffer bb = ByteBuffer.allocate(size * 8 * multiplier).order(ByteOrder.LITTLE_ENDIAN);
            DoubleBuffer db = bb.asDoubleBuffer();
            for (int i = 0; i < size; i++) {
                Object val = vector.get(i);
                if (isComplex) {
                    Complex c = (Complex) val;
                    db.put(c.real());
                    db.put(c.imaginary());
                } else {
                    db.put(((Real)val).doubleValue());
                }
            }
            builder.setData(ByteString.copyFrom(bb));
        }
        return builder.build();
    }

    private ScalarResponse toProtoScalar(Object scalar) {
        if (scalar instanceof Complex c) {
            return ScalarResponse.newBuilder()
                    .setValue(c.real())
                    .setImaginary(c.imaginary())
                    .setIsComplex(true)
                    .build();
        } else if (scalar instanceof org.episteme.core.mathematics.numbers.real.RealBig rb) {
            return ScalarResponse.newBuilder()
                    .setHpValue(rb.toString())
                    .setIsComplex(false)
                    .build();
        } else if (scalar instanceof Real r) {
            return ScalarResponse.newBuilder()
                    .setValue(r.doubleValue())
                    .setIsComplex(false)
                    .build();
        } else if (scalar instanceof Number n) {
            return ScalarResponse.newBuilder()
                    .setValue(n.doubleValue())
                    .setIsComplex(false)
                    .build();
        }
        throw new IllegalArgumentException("Unknown scalar type: " + scalar.getClass());
    }
}
