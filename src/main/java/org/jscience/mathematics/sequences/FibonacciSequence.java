package org.jscience.mathematics.sequences;

import org.jscience.mathematics.numbers.integers.Integer;

/**
 * Represents the Fibonacci sequence: 0, 1, 1, 2, 3, 5, 8, ...
 * defined by F(0)=0, F(1)=1, F(n) = F(n-1) + F(n-2).
 */
public class FibonacciSequence implements InfiniteSequence<Integer> {

    @Override
    public Integer get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }
        if (index == 0)
            return Integer.ZERO;
        if (index == 1)
            return Integer.ONE;

        Integer a = Integer.ZERO;
        Integer b = Integer.ONE;
        for (int i = 2; i <= index; i++) {
            Integer temp = a.add(b);
            a = b;
            b = temp;
        }
        return b;
    }

    @Override
    public boolean isConvergent() {
        return false;
    }

    @Override
    public Integer limit() {
        throw new ArithmeticException("Fibonacci sequence diverges to infinity");
    }
}
