/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage;

/**
 * Shared generic implementations of iterative solvers for Sparse Linear Algebra.
 */
public class GenericSparseSolvers {

    public static <E> Vector<E> bicgstab(SparseLinearAlgebraProvider<E> provider, Matrix<E> A, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, Field<E> f) {
        Vector<E> x = x0;
        Vector<E> r = provider.subtract(b, A.multiply(x));
        Vector<E> r0 = r;
        E rho = f.one(), alpha = f.one(), omega = f.one();
        Vector<E> v = new GenericVector<>(new SparseVectorStorage<>(b.dimension(), f.zero()), provider, f);
        Vector<E> p = new GenericVector<>(new SparseVectorStorage<>(b.dimension(), f.zero()), provider, f);

        for (int iter = 0; iter < maxIterations; iter++) {
            E rhoOld = rho;
            rho = provider.dot(r0, r);
            
            // Check for breakdown
            if (isSmaller(rho, 1e-45, f)) {
                 // Try to recover by resetting r0 if possible, or just break
                 break; 
            }

            if (iter == 0) p = r;
            else {
                if (isSmaller(omega, 1e-45, f) || isSmaller(rhoOld, 1e-45, f)) break;
                E beta = f.multiply(f.divide(rho, rhoOld), f.divide(alpha, omega));
                p = provider.add(r, provider.multiply(provider.subtract(p, provider.multiply(v, omega)), beta));
            }

            v = A.multiply(p);
            E vDotR0 = provider.dot(r0, v);
            if (isSmaller(vDotR0, 1e-45, f)) break;
            alpha = f.divide(rho, vDotR0);

            Vector<E> s = provider.subtract(r, provider.multiply(v, alpha));
            // Check intermediate convergence
            if (isSmaller(provider.norm(s), tolerance, f)) {
                x = provider.add(x, provider.multiply(p, alpha));
                r = s; // Final residual
                break;
            }

            Vector<E> t = A.multiply(s);
            E tDotT = provider.dot(t, t);
            
            if (isSmaller(tDotT, 1e-45, f)) {
                // Omega would be undefined, but we can still update x with alpha step
                x = provider.add(x, provider.multiply(p, alpha));
                r = s;
                break;
            }
            
            omega = f.divide(provider.dot(t, s), tDotT);
            x = provider.add(provider.add(x, provider.multiply(p, alpha)), provider.multiply(s, omega));
            r = provider.subtract(s, provider.multiply(t, omega));
            
            if (isSmaller(provider.norm(r), tolerance, f)) break;
        }
        return x;
    }

    public static <E> Vector<E> conjugateGradient(SparseLinearAlgebraProvider<E> provider, Matrix<E> A, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, Field<E> f) {
        Vector<E> x = x0;
        Vector<E> r = provider.subtract(b, A.multiply(x));
        Vector<E> p = r;
        E rsold = provider.dot(r, r);
        E rsinitial = rsold;

        for (int iter = 0; iter < maxIterations; iter++) {
            Vector<E> Ap = A.multiply(p);
            E pAp = provider.dot(p, Ap);
            if (isSmaller(pAp, 1e-25, f)) break;
            
            E alpha = f.divide(rsold, pAp);
            x = provider.add(x, provider.multiply(p, alpha));
            r = provider.subtract(r, provider.multiply(Ap, alpha));

            E rsnew = provider.dot(r, r);
            if (isSmaller(rsnew, tolerance, f)) break; // Square of tolerance check
            
            // Divergence check: if residual squared norm becomes huge relative to initial
            if (isGreater(rsnew, 1e50, f) && isGreater(rsnew, f.multiply(rsinitial, f.one()), f)) {
                 if (isGreater(f.divide(rsnew, rsinitial), 1e40, f)) {
                    throw new ArithmeticException("Conjugate Gradient Divergence: residual squared norm grew from " + rsinitial + " to " + rsnew);
                 }
            }

            if (isSmaller(sqrt(rsnew, f), tolerance, f)) break;

            E beta = f.divide(rsnew, rsold);
            p = provider.add(r, provider.multiply(p, beta));
            rsold = rsnew;
        }
        return x;
    }

    public static <E> Vector<E> gmres(SparseLinearAlgebraProvider<E> provider, Matrix<E> A, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts, Field<E> f) {
        Vector<E> x = x0;
        int n = b.dimension();
        for (int r_idx = 0; r_idx < restarts; r_idx++) {
            Vector<E> r0_vec = provider.subtract(b, A.multiply(x));
            E beta_val = provider.norm(r0_vec);
            if (isSmaller(beta_val, tolerance, f)) return x;

            int m = Math.min(maxIterations, n);
            @SuppressWarnings("unchecked")
            Vector<E>[] V = (Vector<E>[]) new Vector[m + 1];
            @SuppressWarnings("unchecked")
            E[][] H = (E[][]) java.lang.reflect.Array.newInstance(componentType(f), m + 1, m);
            
            V[0] = provider.multiply(r0_vec, f.divide(f.one(), beta_val));

            // Hessenberg matrix H and orthogonal basis V
            int actual_m = m;
            for (int j = 0; j < m; j++) {
                Vector<E> w = A.multiply(V[j]);
                for (int i = 0; i <= j; i++) {
                    H[i][j] = provider.dot(V[i], w);
                    w = provider.subtract(w, provider.multiply(V[i], H[i][j]));
                }
                H[j+1][j] = provider.norm(w);
                if (isSmaller(H[j+1][j], 1e-25, f)) {
                    actual_m = j + 1;
                    break;
                }
                V[j+1] = provider.multiply(w, f.divide(f.one(), H[j+1][j]));
            }

            // Solve Least Squares: min || beta*e1 - Hy || using Givens Rotations
            @SuppressWarnings("unchecked")
            E[] sn = (E[]) java.lang.reflect.Array.newInstance(componentType(f), actual_m);
            @SuppressWarnings("unchecked")
            E[] cs = (E[]) java.lang.reflect.Array.newInstance(componentType(f), actual_m);
            @SuppressWarnings("unchecked")
            E[] s_vec = (E[]) java.lang.reflect.Array.newInstance(componentType(f), actual_m + 1);
            java.util.Arrays.fill(s_vec, f.zero());
            s_vec[0] = beta_val;

            for (int i = 0; i < actual_m; i++) {
                // Apply previous rotations
                for (int k = 0; k < i; k++) {
                    E temp = f.add(f.multiply(cs[k], H[k][i]), f.multiply(sn[k], H[k+1][i]));
                    H[k+1][i] = f.add(f.multiply(f.negate(conjugate(sn[k])), H[k][i]), f.multiply(conjugate(cs[k]), H[k+1][i]));
                    H[k][i] = temp;
                }

                // Compute current rotation
                E h1 = H[i][i];
                E h2 = H[i+1][i];
                if (h1 == null || h2 == null) throw new NullPointerException("Hessenberg matrix entry is null at [" + i + "][" + i + "]");
                
                if (isSmaller(h2, 1e-31, f)) {
                    cs[i] = f.one();
                    sn[i] = f.zero();
                } else {
                    E h1sq = getSquareNorm(h1, f);
                    E h2sq = getSquareNorm(h2, f);
                    E sumSq = f.add(h1sq, h2sq);
                    if (isSmaller(sumSq, 1e-35, f)) {
                         cs[i] = f.one(); sn[i] = f.zero();
                    } else {
                        E t = sqrt(sumSq, f);
                        cs[i] = f.divide(conjugate(h1), t);
                        sn[i] = f.divide(conjugate(h2), t);
                    }
                }

                // Apply current rotation to H and s_vec
                H[i][i] = f.add(f.multiply(cs[i], h1), f.multiply(sn[i], h2));
                H[i+1][i] = f.zero();
                
                E s1 = s_vec[i];
                s_vec[i] = f.multiply(cs[i], s1);
                s_vec[i+1] = f.multiply(f.negate(sn[i]), s1);
                if (isSmaller(s_vec[i+1], tolerance, f)) {
                    actual_m = i + 1;
                    break;
                }
            }

            // Back substitution
            @SuppressWarnings("unchecked")
            E[] y = (E[]) java.lang.reflect.Array.newInstance(componentType(f), actual_m);
            for (int i = actual_m - 1; i >= 0; i--) {
                y[i] = s_vec[i];
                for (int j = i + 1; j < actual_m; j++) {
                    y[i] = f.subtract(y[i], f.multiply(H[i][j], y[j]));
                }
                if (isSmaller(H[i][i], 1e-31, f)) {
                    y[i] = f.zero();
                } else {
                    y[i] = f.divide(y[i], H[i][i]);
                }
            }

            // Update x = x + Vy
            for (int i = 0; i < actual_m; i++) {
                x = provider.add(x, provider.multiply(V[i], y[i]));
            }

            if (isSmaller(s_vec[actual_m], tolerance, f)) return x;
        }
        return x;
    }

    private static boolean isSmaller(Object element, Object tolerance, Field<?> f) {
        Object eAbs = (element instanceof Complex c) ? c.abs() : element;
        Object tAbs = (tolerance instanceof Complex ct) ? ct.abs() : tolerance;

        if (eAbs instanceof RealBig rb) {
            java.math.BigDecimal val = rb.abs().bigDecimalValue();
            if (tAbs instanceof RealBig rt) return val.compareTo(rt.abs().bigDecimalValue()) < 0;
            if (tAbs instanceof Number nt) return val.compareTo(new java.math.BigDecimal(nt.toString())) < 0;
            return val.doubleValue() < abs(tAbs, f);
        }
        
        return abs(eAbs, f) < abs(tAbs, f);
    }

    private static boolean isGreater(Object element, Object threshold, Field<?> f) {
        Object eAbs = (element instanceof Complex c) ? c.abs() : element;
        Object tAbs = (threshold instanceof Complex ct) ? ct.abs() : threshold;

        if (eAbs instanceof RealBig rb) {
            java.math.BigDecimal val = rb.abs().bigDecimalValue();
            if (tAbs instanceof RealBig rt) return val.compareTo(rt.abs().bigDecimalValue()) > 0;
            if (tAbs instanceof Number nt) return val.compareTo(new java.math.BigDecimal(nt.toString())) > 0;
            return val.doubleValue() > abs(tAbs, f);
        }
        
        return abs(eAbs, f) > abs(tAbs, f);
    }

    private static Class<?> componentType(Field<?> field) {
        Class<?> c = field.zero().getClass();
        if (Real.class.isAssignableFrom(c)) return Real.class;
        if (Complex.class.isAssignableFrom(c)) return Complex.class;
        return c;
    }

    private static double abs(Object element, Field<?> f) {
        if (element instanceof RealBig rb) return rb.abs().doubleValue();
        if (element instanceof Real r) return r.doubleValue();
        if (element instanceof Complex c) return c.abs().doubleValue();
        if (element instanceof Number n) return n.doubleValue();
        return 0.0;
    }

    @SuppressWarnings("unchecked")
    private static <E> E conjugate(E element) {
        if (element instanceof Complex c) return (E) c.conjugate();
        return element;
    }

    @SuppressWarnings("unchecked")
    private static <E> E getSquareNorm(E element, Field<E> f) {
        if (element instanceof Complex c) {
            Real abs = c.abs();
            return (E) Complex.of(abs.multiply(abs));
        }
        return f.multiply(element, element);
    }

    @SuppressWarnings("unchecked")
    private static <E> E sqrt(E element, Field<E> f) {
        Object res = null;
        if (element instanceof org.episteme.core.mathematics.numbers.real.RealBig rb) {
            try {
                // Safeguard: if it's negative and small, RealBig.sqrt() will already handle clamping.
                // If it's negative and large, it indicates divergence.
                res = rb.sqrt();
            } catch (ArithmeticException e) {
                // Better error message for solver divergence cases
                throw new ArithmeticException("Divergence in iterative solver: " + e.getMessage() + ". This may be caused by a non-positive-definite matrix or precision loss.");
            }
        }
        else if (element instanceof Real r) {
            if (r.doubleValue() < -1e-12) {
                 throw new ArithmeticException("Significant negative value passed to sqrt in solver: " + r);
            }
            res = r.sqrt();
        }
        else if (element instanceof Complex c) res = c.sqrt();
        else {
            try {
                res = element.getClass().getMethod("sqrt").invoke(element);
            } catch (Exception e) {
                res = element;
            }
        }
        
        if (f.zero() instanceof Complex && res instanceof Real) {
            return (E) Complex.of((Real) res);
        }
        return (E) res;
    }
}
