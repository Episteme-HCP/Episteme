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

package org.jscience.demo.exactvsapproximate;

import org.jscience.mathematics.structures.rings.Semiring;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.integers.Natural;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.sets.Naturals;

public class ExactVsApproximateDemo {

    /**
     * Generic Fibonacci algorithm that works with ANY Semiring.
     * This is the SAME code for exact and approximate!
     */
    public static <E> E fibonacci(int n, Semiring<E> structure) {
        E a = structure.zero();
        E b = structure.one();

        for (int i = 0; i < n; i++) {
            E temp = structure.add(a, b);
            a = b;
            b = temp;
        }
        return b;
    }

    /**
     * Generic power function: computes base^exponent
     */
    public static <E> E power(E base, int exponent, Semiring<E> structure) {
        if (exponent == 0) {
            return structure.one();
        }

        E result = structure.one();
        for (int i = 0; i < exponent; i++) {
            result = structure.multiply(result, base);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== EXACT vs APPROXIMATE Numbers ===\n");

        // ============================================================
        // Example 1: Fibonacci Numbers
        // ============================================================
        System.out.println("1. Fibonacci Numbers (n=100):");

        // EXACT computation (Natural - uses BigInteger internally if needed)
        long startExact = System.nanoTime();
        Natural fibExact = fibonacci(100, Naturals.getInstance());
        long timeExact = System.nanoTime() - startExact;

        // APPROXIMATE computation (Real - uses double by default)
        long startApprox = System.nanoTime();
        Real fibApprox = fibonacci(100, Reals.getInstance());
        long timeApprox = System.nanoTime() - startApprox;

        System.out.println("   Exact:        " + fibExact);
        System.out.println("   Approximate:  " + fibApprox);
        System.out.println("   Exact time:   " + timeExact + " ns");
        System.out.println("   Approx time:  " + timeApprox + " ns");
        System.out.println("   Speedup:      " + (timeExact / (double) timeApprox) + "x\n");

        // ============================================================
        // Example 2: Large Powers
        // ============================================================
        System.out.println("2. Computing 2^100:");

        Natural baseExact = Natural.of(2);
        Natural powerExact = power(baseExact, 100, Naturals.getInstance());

        Real baseApprox = Real.of(2.0);
        Real powerApprox = power(baseApprox, 100, Reals.getInstance());

        System.out.println("   Exact:        " + powerExact);
        System.out.println("   Approximate:  " + powerApprox);
        System.out.println("   (Note: approximate shows scientific notation)\n");

        // ============================================================
        // Example 3: Ergonomic Instance Methods Work Too!
        // ============================================================
        System.out.println("3. Instance Method API (ergonomic!):");

        // Exact - fluent API
        Natural a = Natural.of(1000000);
        Natural b = Natural.of(500000);
        Natural sumExact = a.add(b).multiply(Natural.of(2));
        System.out.println("   Exact: (1000000 + 500000) * 2 = " + sumExact);

        // Approximate - same fluent API!
        Real x = Real.of(1e6);
        Real y = Real.of(5e5);
        Real sumApprox = x.add(y).multiply(Real.of(2.0));
        System.out.println("   Approx: (1e6 + 5e5) * 2 = " + sumApprox + "\n");

        // ============================================================
        // Example 4: Type Safety Prevents Mixing!
        // ============================================================
        System.out.println("4. Type Safety:");
        System.out.println("   Natural and Real are DIFFERENT types");
        System.out.println("   This prevents accidental precision loss:");
        System.out.println("   // a.add(x)  <- COMPILE ERROR! Cannot mix!");
        System.out.println("   Type system protects you!\n");

        // ============================================================
        // Example 5: When Approximate Breaks Down
        // ============================================================
        System.out.println("5. Precision Limits:");

        // Exact: can handle ANY size
        Natural huge = Natural.of(Long.MAX_VALUE);
        Natural hugeSquared = huge.multiply(huge);
        System.out.println("   Exact: INT64_MAX^2 = " + hugeSquared);
        System.out.println("   (85 digit number, exact!)");

        // Approximate: limited precision
        Real approxHuge = Real.of(Long.MAX_VALUE);
        Real approxSquared = approxHuge.multiply(approxHuge);
        System.out.println("   Approx: INT64_MAX^2 Ã¢â€°Ë† " + approxSquared);
        System.out.println("   (Approximate representation)\n");

        // ============================================================
        // Summary
        // ============================================================
        System.out.println("=== SUMMARY ===");
        System.out.println("Ã¢Å“â€œ Same algorithm works with both (Semiring<E> abstraction)");
        System.out.println("Ã¢Å“â€œ Type safety prevents accidental mixing");
        System.out.println("Ã¢Å“â€œ Performance vs Precision trade-off is explicit");
        System.out.println("Ã¢Å“â€œ Ergonomic instance methods for both");
        System.out.println("\nUse Exact when: correctness matters (crypto, number theory)");
        System.out.println("Use Approximate when: speed matters (simulations, statistics)");
    }
}
