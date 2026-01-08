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
import org.jscience.server.proto.MatrixServiceGrpc;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.Operation;

/**
 * A LinearAlgebraProvider that offloads operations to a remote gRPC service.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GRPCLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final ManagedChannel channel;
    private final MatrixServiceGrpc.MatrixServiceBlockingStub blockingStub;

    public GRPCLinearAlgebraProvider(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = MatrixServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public String getName() {
        return "gRPC Linear Algebra";
    }

    @Override
    public boolean isAvailable() {
        return channel != null && !channel.isShutdown();
    }

    @Override
    public ExecutionContext createContext() {
        return new ExecutionContext() {
            @Override
            public <T> T execute(Operation<T> operation) {
                throw new UnsupportedOperationException("gRPC execution context not yet implemented");
            }

            @Override
            public void close() {
                // No-op for gRPC context
            }
        };
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("gRPC vector add not yet implemented");
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("gRPC vector subtract not yet implemented");
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        throw new UnsupportedOperationException("gRPC vector multiply not yet implemented");
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("gRPC dot not yet implemented");
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException("gRPC matrix add not yet implemented");
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException("gRPC matrix subtract not yet implemented");
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        throw new UnsupportedOperationException("gRPC matrix multiply not yet implemented");
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("gRPC matrix-vector multiply not yet implemented");
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        throw new UnsupportedOperationException("gRPC inverse not yet implemented");
    }

    @Override
    public E determinant(Matrix<E> a) {
        throw new UnsupportedOperationException("gRPC determinant not yet implemented");
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        throw new UnsupportedOperationException("gRPC solve not yet implemented");
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        throw new UnsupportedOperationException("gRPC transpose not yet implemented");
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        throw new UnsupportedOperationException("gRPC scale not yet implemented");
    }

    @Override
    public E norm(Vector<E> a) {
        throw new UnsupportedOperationException("gRPC norm not yet implemented");
    }
}