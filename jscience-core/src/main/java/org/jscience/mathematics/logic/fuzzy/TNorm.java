/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.logic.fuzzy;

/**
 * T-norm (Triangular norm) for fuzzy logic.
 * <p>
 * A t-norm is a binary operation on [0,1] that generalizes the logical AND.
 * It must satisfy: commutativity, associativity, monotonicity, and have 1 as
 * identity.
 * </p>
 * <p>
 * Common t-norms include:
 * - Minimum: min(a, b)
 * - Product: a * b
 * - Łukasiewicz: max(0, a + b - 1)
 * - Drastic: min(a,b) if max(a,b)=1, else 0
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface TNorm {

    /**
     * Applies the t-norm to two values.
     * 
     * @param a first value in [0,1]
     * @param b second value in [0,1]
     * @return the t-norm result
     */
    double apply(double a, double b);

    /**
     * Applies the t-conorm (S-norm) - dual of t-norm.
     * S(a,b) = 1 - T(1-a, 1-b)
     * 
     * @param a first value in [0,1]
     * @param b second value in [0,1]
     * @return the t-conorm result
     */
    default double applyConorm(double a, double b) {
        return 1.0 - apply(1.0 - a, 1.0 - b);
    }

    /**
     * Minimum t-norm: min(a, b)
     */
    TNorm MINIMUM = new TNorm() {
        @Override
        public double apply(double a, double b) {
            return Math.min(a, b);
        }

        @Override
        public String toString() {
            return "Minimum T-norm";
        }
    };

    /**
     * Product t-norm: a * b
     */
    TNorm PRODUCT = new TNorm() {
        @Override
        public double apply(double a, double b) {
            return a * b;
        }

        @Override
        public String toString() {
            return "Product T-norm";
        }
    };

    /**
     * Łukasiewicz t-norm: max(0, a + b - 1)
     */
    TNorm LUKASIEWICZ = new TNorm() {
        @Override
        public double apply(double a, double b) {
            return Math.max(0.0, a + b - 1.0);
        }

        @Override
        public String toString() {
            return "Łukasiewicz T-norm";
        }
    };

    /**
     * Drastic t-norm: min(a,b) if max(a,b)=1, else 0
     */
    TNorm DRASTIC = new TNorm() {
        @Override
        public double apply(double a, double b) {
            if (Math.max(a, b) == 1.0) {
                return Math.min(a, b);
            }
            return 0.0;
        }

        @Override
        public String toString() {
            return "Drastic T-norm";
        }
    };

    /**
     * Nilpotent minimum t-norm: min(a,b) if a+b>1, else 0
     */
    TNorm NILPOTENT_MINIMUM = new TNorm() {
        @Override
        public double apply(double a, double b) {
            if (a + b > 1.0) {
                return Math.min(a, b);
            }
            return 0.0;
        }

        @Override
        public String toString() {
            return "Nilpotent Minimum T-norm";
        }
    };

    /**
     * Hamacher product t-norm: (a*b)/(a+b-a*b) if a+b≠0, else 0
     */
    TNorm HAMACHER_PRODUCT = new TNorm() {
        @Override
        public double apply(double a, double b) {
            if (a == 0.0 && b == 0.0) {
                return 0.0;
            }
            return (a * b) / (a + b - a * b);
        }

        @Override
        public String toString() {
            return "Hamacher Product T-norm";
        }
    };
}
