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
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.ProviderSelector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

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

    /**
     * Executes an operation with a guaranteed high-precision provider if requested.
     */
    private <P extends AlgorithmProvider, R> R executeHP(boolean hp, Class<P> providerClass, Function<P, R> operation) {
        try {
            if (hp) {
                return MathContext.exact().compute(() -> {
                    P prov = ProviderSelector.select(
                            providerClass,
                            OperationContext.DEFAULT,
                            p -> p.getName().contains("MPFR") || p.getName().contains("Big") // Force HP-capable backend
                    );
                    return operation.apply(prov);
                });
            } else {
                P prov = ProviderSelector.select(providerClass);
                LOG.debug("Selected Standard Provider: {} for {}", prov.getName(), providerClass.getSimpleName());
                return operation.apply(prov);
            }
        } catch (Exception e) {
            throw new RuntimeException("Execution failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void matrixMultiply(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrixA()) || isHPRequest(request.getMatrixB());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                LOG.debug("Received Matrix Multiplication request (HP={})", hp);

                Matrix<?> matrixA = fromProto(request.getMatrixA());
                Matrix<?> matrixB = fromProto(request.getMatrixB());

                LOG.debug("Multiplying matrices: [{}x{}] * [{}x{}] using provider: {}",
                        matrixA.rows(), matrixA.cols(), matrixB.rows(), matrixB.cols(), prov.getName());

                @SuppressWarnings("unchecked")
                Matrix<?> resultMatrix = prov.multiply((Matrix<Object>)matrixA, (Matrix<Object>)matrixB);

                responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(resultMatrix)).build());
                responseObserver.onCompleted();

                LOG.debug("Matrix Multiplication completed successfully");
                return null;
            });
        } catch (Exception e) {
            handleError("matrixMultiply", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixAdd(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrixA()) || isHPRequest(request.getMatrixB());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> m1 = (Matrix<Object>) fromProto(request.getMatrixA());
                Matrix<Object> m2 = (Matrix<Object>) fromProto(request.getMatrixB());
                Matrix<?> result = prov.add(m1, m2);
                responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixAdd", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixSubtract(MatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrixA()) || isHPRequest(request.getMatrixB());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> m1 = (Matrix<Object>) fromProto(request.getMatrixA());
                Matrix<Object> m2 = (Matrix<Object>) fromProto(request.getMatrixB());
                Matrix<?> result = prov.subtract(m1, m2);
                responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixSubtract", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixTranspose(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Matrix<?> result = prov.transpose(matrix);
                responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixTranspose", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixInverse(SingleMatrixRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Matrix<?> result = ((LinearAlgebraProvider<Object>) prov).inverse(matrix);
                MatrixData protoResult = toProto(result);
                responseObserver.onNext(MatrixResponse.newBuilder().setResult(protoResult).build());
                responseObserver.onCompleted();
                return result;
            });
        } catch (Exception e) {
            handleError("matrixInverse", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixScale(ScaleRequest request, StreamObserver<MatrixResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix()) || !request.getHpScalar().isEmpty();
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Object scalar;
                if (request.getIsComplex()) {
                    scalar = Complex.of(request.getScalar(), request.getImaginary());
                } else if (!request.getHpScalar().isEmpty()) {
                    scalar = org.episteme.core.mathematics.numbers.real.RealBig.of(request.getHpScalar());
                } else {
                    scalar = Real.of(request.getScalar());
                }
                Matrix<?> result = prov.scale(scalar, matrix);
                responseObserver.onNext(MatrixResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixScale", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixDeterminant(SingleMatrixRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Object det = prov.determinant(matrix);
                responseObserver.onNext(toProtoScalar(det));
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixDeterminant", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorAdd(VectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getVectorA()) || isHPRequest(request.getVectorB());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Vector<Object> v1 = (Vector<Object>) fromProto(request.getVectorA());
                Vector<Object> v2 = (Vector<Object>) fromProto(request.getVectorB());
                Vector<?> result = prov.add(v1, v2);
                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("vectorAdd", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorSubtract(VectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getVectorA()) || isHPRequest(request.getVectorB());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Vector<Object> v1 = (Vector<Object>) fromProto(request.getVectorA());
                Vector<Object> v2 = (Vector<Object>) fromProto(request.getVectorB());
                Vector<?> result = prov.subtract(v1, v2);
                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("vectorSubtract", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorScale(VectorScaleRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getVector()) || !request.getHpScalar().isEmpty();
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Vector<Object> vector = (Vector<Object>) fromProto(request.getVector());
                Object scalar;
                if (request.getIsComplex()) {
                    scalar = Complex.of(request.getScalar(), request.getImaginary());
                } else if (!request.getHpScalar().isEmpty()) {
                    scalar = org.episteme.core.mathematics.numbers.real.RealBig.of(request.getHpScalar());
                } else {
                    scalar = Real.of(request.getScalar());
                }
                Vector<?> result = prov.multiply(vector, scalar);
                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("vectorScale", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorDot(VectorRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getVectorA()) || isHPRequest(request.getVectorB());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Vector<Object> v1 = (Vector<Object>) fromProto(request.getVectorA());
                Vector<Object> v2 = (Vector<Object>) fromProto(request.getVectorB());
                Object dot = prov.dot(v1, v2);
                responseObserver.onNext(toProtoScalar(dot));
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("vectorDot", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void vectorNorm(SingleVectorRequest request, StreamObserver<ScalarResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getVector());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Vector<Object> vector = (Vector<Object>) fromProto(request.getVector());
                Object norm = prov.norm(vector);
                responseObserver.onNext(toProtoScalar(norm));
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("vectorNorm", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixVectorMultiply(MatrixVectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix()) || isHPRequest(request.getVector());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Vector<Object> vector = (Vector<Object>) fromProto(request.getVector());
                Vector<?> result = prov.multiply(matrix, vector);
                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixVectorMultiply", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void linearSolve(MatrixVectorRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix()) || isHPRequest(request.getVector());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Vector<Object> vector = (Vector<Object>) fromProto(request.getVector());
                Vector<Object> result = prov.solve(matrix, vector);
                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("linearSolve", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixLU(SingleMatrixRequest request, StreamObserver<LUResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                LUResult<Object> result = prov.lu(matrix);
                responseObserver.onNext(LUResponse.newBuilder()
                    .setL(toProto(result.L()))
                    .setU(toProto(result.U()))
                    .setP(toProto(result.P()))
                    .build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixLU", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixQR(SingleMatrixRequest request, StreamObserver<QRResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                QRResult<Object> result = prov.qr(matrix);
                responseObserver.onNext(QRResponse.newBuilder()
                    .setQ(toProto(result.Q()))
                    .setR(toProto(result.R()))
                    .build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixQR", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixSVD(SingleMatrixRequest request, StreamObserver<SVDResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                SVDResult<Object> result = prov.svd(matrix);
                responseObserver.onNext(SVDResponse.newBuilder()
                    .setU(toProto(result.U()))
                    .setS(toProto(result.S()))
                    .setV(toProto(result.V()))
                    .build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixSVD", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixCholesky(SingleMatrixRequest request, StreamObserver<CholeskyResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                CholeskyResult<Object> result = prov.cholesky(matrix);
                responseObserver.onNext(CholeskyResponse.newBuilder()
                    .setL(toProto(result.L()))
                    .build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixCholesky", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void matrixEigen(SingleMatrixRequest request, StreamObserver<EigenResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix());
            executeHP(hp, LinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                EigenResult<Object> result = prov.eigen(matrix);
                responseObserver.onNext(EigenResponse.newBuilder()
                    .setV(toProto(result.V()))
                    .setD(toProto(result.D()))
                    .build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("matrixEigen", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void biCGSTAB(IterativeSolverRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix()) || isHPRequest(request.getB()) || isHPRequest(request.getX0()) || !request.getTolerance().getHpValue().isEmpty();
            executeHP(hp, SparseLinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Vector<Object> b = (Vector<Object>) fromProto(request.getB());
                Vector<Object> x0 = (Vector<Object>) fromProto(request.getX0());
                Object tol = fromProtoScalar(request.getTolerance());
                int maxIter = request.getMaxIterations();

                Vector<Object> result = prov.bicgstab(matrix, b, x0, tol, maxIter);

                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("biCGSTAB", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void conjugateGradient(IterativeSolverRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix()) || isHPRequest(request.getB()) || isHPRequest(request.getX0()) || !request.getTolerance().getHpValue().isEmpty();
            executeHP(hp, SparseLinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Vector<Object> b = (Vector<Object>) fromProto(request.getB());
                Vector<Object> x0 = (Vector<Object>) fromProto(request.getX0());
                Object tol = fromProtoScalar(request.getTolerance());
                int maxIter = request.getMaxIterations();

                Vector<Object> result = prov.conjugateGradient(matrix, b, x0, tol, maxIter);

                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
        } catch (Exception e) {
            handleError("conjugateGradient", e, responseObserver);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void gMRES(GMRESRequest request, StreamObserver<VectorResponse> responseObserver) {
        try {
            boolean hp = isHPRequest(request.getMatrix()) || isHPRequest(request.getB()) || isHPRequest(request.getX0()) || !request.getTolerance().getHpValue().isEmpty();
            executeHP(hp, SparseLinearAlgebraProvider.class, prov -> {
                Matrix<Object> matrix = (Matrix<Object>) fromProto(request.getMatrix());
                Vector<Object> b = (Vector<Object>) fromProto(request.getB());
                Vector<Object> x0 = (Vector<Object>) fromProto(request.getX0());
                Object tol = fromProtoScalar(request.getTolerance());
                int maxIter = request.getMaxIterations();
                int restart = request.getRestart();

                Vector<Object> result = prov.gmres(matrix, b, x0, tol, maxIter, restart);

                responseObserver.onNext(VectorResponse.newBuilder().setResult(toProto(result)).build());
                responseObserver.onCompleted();
                return null;
            });
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

    /** Detects whether a MatrixData payload contains high-precision data. */
    private boolean isHPRequest(MatrixData data) {
        if (data == null) return false;
        boolean hpDataPresent = !data.getHpDataList().isEmpty();
        return hpDataPresent;
    }

    /** Detects whether a VectorData payload contains high-precision data. */
    private boolean isHPRequest(VectorData data) {
        if (data == null) return false;
        boolean hpDataPresent = !data.getHpDataList().isEmpty();
        return hpDataPresent;
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
            java.util.concurrent.atomic.AtomicInteger idx = new java.util.concurrent.atomic.AtomicInteger(0);
            org.episteme.core.mathematics.context.MathContext.exact().compute(() -> {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        int currentIdx = idx.getAndIncrement();
                        String s = hpData.get(currentIdx);
                        try {
                            raw[i][j] = org.episteme.core.mathematics.numbers.real.RealBig.of(s);
                        } catch (Exception e) {
                            LOG.error("Failed to parse high-precision string at [{},{}]: '{}'", i, j, s);
                            throw e;
                        }
                    }
                }
                return null;
            });
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
        
        Object zero = matrix.getScalarRing().zero();
        boolean isComplex = zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        boolean isHPContextM = org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
        String zeroClass = zero.getClass().getName();
        boolean isHPRingM = zeroClass.contains("RealBig") || zeroClass.contains("Complex");
        
        // Element-level check
        boolean isHPElementsM = false;
        if (!isHPRingM && rows > 0 && cols > 0) {
            Object first = matrix.get(0, 0);
            if (first != null) {
                String firstClass = first.getClass().getName();
                isHPElementsM = firstClass.contains("RealBig") || firstClass.contains("Complex");
            }
        }
        
        boolean isHPM = isHPContextM || isHPRingM || isHPElementsM;

        MatrixData.Builder builder = MatrixData.newBuilder()
                .setRows(rows)
                .setCols(cols)
                .setIsComplex(isComplex);

        if (isHPM) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Object val = matrix.get(i, j);
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) val;
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
        
        // Correctly detect complex ring
        Object zero = vector.getScalarRing().zero();
        boolean isComplex = zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        
        boolean isHPContextV = org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
        String zeroClass = zero.getClass().getName();
        boolean isHPRingV = zeroClass.contains("RealBig") || zeroClass.contains("Complex");
        
        // Element-level check
        boolean isHPElementsV = false;
        if (!isHPRingV && size > 0) {
            Object first = vector.get(0);
            if (first != null) {
                String firstClass = first.getClass().getName();
                isHPElementsV = firstClass.contains("RealBig") || firstClass.contains("Complex");
            }
        }
        boolean isHPV = isHPContextV || isHPRingV || isHPElementsV;

        VectorData.Builder builder = VectorData.newBuilder()
                .setSize(size)
                .setIsComplex(isComplex);

        if (isHPV) {
            for (int i = 0; i < size; i++) {
                Object val = vector.get(i);
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) val;
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
        if (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            boolean isHP = org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
            if (isHP) {
                return ScalarResponse.newBuilder()
                        .setHpValue(c.getReal().toString() + ";" + c.getImaginary().toString())
                        .setIsComplex(true)
                        .build();
            }
            return ScalarResponse.newBuilder()
                    .setValue(c.getReal().doubleValue())
                    .setImaginary(c.getImaginary().doubleValue())
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
        return ScalarResponse.newBuilder()
                .setValue(0.0)
                .setIsComplex(false)
                .build();
    }
}
