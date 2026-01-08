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

package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.technical.backend.ExecutionContext;

public class CUDADenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {
    
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;

    public CUDADenseLinearAlgebraProvider(Field<E> field) {
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }

    private static boolean checkAvailability() {
        try {
            Class.forName("jcuda.jcublas.JCublas");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

    @Override
    public String getName() { 
        return "CUDA (Dense)"; 
    }

    @Override
    public boolean isAvailable() { 
        return checkAvailability(); 
    }

    @Override
    public ExecutionContext createContext() { 
        return cpuProvider.createContext(); 
    }
    
    @Override
    public int getPriority() {
        return isAvailable() ? 100 : 0;
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) { 
        return cpuProvider.add(a, b); 
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) { 
        return cpuProvider.subtract(a, b); 
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) { 
        return cpuProvider.multiply(vector, scalar); 
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) { 
        return cpuProvider.dot(a, b); 
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) { 
        return cpuProvider.add(a, b); 
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { 
        return cpuProvider.subtract(a, b); 
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { 
        return cpuProvider.multiply(a, b); 
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) { 
        return cpuProvider.multiply(a, b); 
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) { 
        return cpuProvider.inverse(a); 
    }

    @Override
    public E determinant(Matrix<E> a) { 
        return cpuProvider.determinant(a); 
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) { 
        return cpuProvider.solve(a, b); 
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) { 
        return cpuProvider.transpose(a); 
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) { 
        return cpuProvider.scale(scalar, a); 
    }

    @Override
    public E norm(Vector<E> a) {
        return cpuProvider.norm(a);
    }

    @Override
    public String getId() { 
        return "cudadense"; 
    }

    @Override
    public String getDescription() { 
        return "CUDADenseLinearAlgebraProvider"; 
    }
}
