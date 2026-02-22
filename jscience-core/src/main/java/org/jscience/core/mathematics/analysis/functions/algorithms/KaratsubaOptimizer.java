package org.jscience.core.mathematics.analysis.functions.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jscience.core.mathematics.structures.rings.Ring;

/**
 * Utility for Karatsuba multiplication of polynomials/sequences.
 * <p>
 * Karatsuba multiplication reduces the complexity of polynomial multiplication
 * from O(nÂ²) to approximately O(n^1.585) by using a divide-and-conquer approach.
 * </p>
 */
public final class KaratsubaOptimizer {

    private static final int THRESHOLD = 32;

    private KaratsubaOptimizer() {}

    /**
     * Multiplies two sequences of coefficients using the Karatsuba algorithm.
     * 
     * @param a    first sequence of coefficients [a0, a1, ...]
     * @param b    second sequence of coefficients [b0, b1, ...]
     * @param ring the coefficient ring
     * @return the product sequence
     */
    public static <R> List<R> multiply(List<R> a, List<R> b, Ring<R> ring) {
        int n = a.size();
        int m = b.size();

        // Base case: use naive multiplication for small inputs
        if (n < THRESHOLD || m < THRESHOLD) {
            return standardMultiply(a, b, ring);
        }

        int maxLen = Math.max(n, m);
        int half = maxLen / 2;

        // Split A and B
        List<R> aLow = new ArrayList<>(a.subList(0, Math.min(half, n)));
        List<R> aHigh = (half < n) ? new ArrayList<>(a.subList(half, n)) : new ArrayList<>(Collections.singletonList(ring.zero()));
        
        List<R> bLow = new ArrayList<>(b.subList(0, Math.min(half, m)));
        List<R> bHigh = (half < m) ? new ArrayList<>(b.subList(half, m)) : new ArrayList<>(Collections.singletonList(ring.zero()));

        // P0 = aLow * bLow
        List<R> p0 = multiply(aLow, bLow, ring);
        
        // P1 = aHigh * bHigh
        List<R> p1 = multiply(aHigh, bHigh, ring);

        // P2 = (aLow + aHigh) * (bLow + bHigh) - P0 - P1
        List<R> aSum = add(aLow, aHigh, ring);
        List<R> bSum = add(bLow, bHigh, ring);
        List<R> p2 = multiply(aSum, bSum, ring);
        p2 = subtract(p2, p0, ring);
        p2 = subtract(p2, p1, ring);

        // Result = p0 + p2*x^half + p1*x^(2*half)
        return combine(p0, p2, p1, half, ring);
    }

    private static <R> List<R> add(List<R> a, List<R> b, Ring<R> ring) {
        int len = Math.max(a.size(), b.size());
        List<R> res = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            R c1 = i < a.size() ? a.get(i) : ring.zero();
            R c2 = i < b.size() ? b.get(i) : ring.zero();
            res.add(ring.add(c1, c2));
        }
        return res;
    }

    private static <R> List<R> subtract(List<R> a, List<R> b, Ring<R> ring) {
        int len = Math.max(a.size(), b.size());
        List<R> res = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            R c1 = i < a.size() ? a.get(i) : ring.zero();
            R c2 = i < b.size() ? b.get(i) : ring.zero();
            res.add(ring.subtract(c1, c2));
        }
        return res;
    }

    private static <R> List<R> combine(List<R> p0, List<R> p2, List<R> p1, int m, Ring<R> ring) {
        int finalSize = p1.size() + 2 * m;
        List<R> res = new ArrayList<>(Collections.nCopies(finalSize, ring.zero()));

        // Add P0
        for (int i = 0; i < p0.size(); i++) {
            res.set(i, p0.get(i));
        }

        // Add P2 * x^m
        for (int i = 0; i < p2.size(); i++) {
            res.set(i + m, ring.add(res.get(i + m), p2.get(i)));
        }

        // Add P1 * x^(2m)
        for (int i = 0; i < p1.size(); i++) {
            res.set(i + 2 * m, ring.add(res.get(i + 2 * m), p1.get(i)));
        }

        return res;
    }

    private static <R> List<R> standardMultiply(List<R> a, List<R> b, Ring<R> ring) {
        int n = a.size();
        int m = b.size();
        List<R> res = new ArrayList<>(Collections.nCopies(n + m - 1, ring.zero()));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                R prod = ring.multiply(a.get(i), b.get(j));
                res.set(i + j, ring.add(res.get(i + j), prod));
            }
        }
        return res;
    }
}
