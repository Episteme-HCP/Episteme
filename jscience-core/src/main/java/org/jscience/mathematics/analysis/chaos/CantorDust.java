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

package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Cantor Dust - a 2D fractal generalization of the Cantor set.
 * <p>
 * The Cantor dust is constructed by recursively subdividing squares into 9
 * equal parts
 * and removing the center square, similar to the Cantor set but in 2D.
 * Also known as the Cantor square.
 * </p>
 *
 * <h2>Construction</h2>
 * <ol>
 * <li>Start with a unit square [0,1] Ãƒâ€” [0,1]</li>
 * <li>Divide into 9 equal squares (3Ãƒâ€”3 grid)</li>
 * <li>Remove the center square</li>
 * <li>Repeat for each remaining square</li>
 * </ol>
 *
 * <h2>Properties</h2>
 * <ul>
 * <li>Hausdorff dimension: log(8)/log(3) Ã¢â€°Ë† 1.8928</li>
 * <li>Zero Lebesgue measure (area = 0)</li>
 * <li>Uncountable number of points</li>
 * <li>Self-similar fractal</li>
 * <li>Nowhere dense but perfect set</li>
 * </ul>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Cantor, Georg (1883). "ÃƒÅ“ber unendliche, lineare
 * Punktmannigfaltigkeiten"</li>
 * <li>Mandelbrot, B. (1982). "The Fractal Geometry of Nature"</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CantorDust {

    /**
     * Represents a square region in the Cantor dust.
     */
    public static class Square {
        public final Real x, y, size;

        public Square(Real x, Real y, Real size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }

        @Override
        public String toString() {
            return String.format("Square[(%s, %s), size=%s]", x, y, size);
        }
    }

    /**
     * Generates the Cantor dust up to a given depth.
     * 
     * @param depth recursion depth (0 = initial square)
     * @return list of squares in the dust
     */
    public static List<Square> generate(int depth) {
        List<Square> squares = new ArrayList<>();
        generateRecursive(Real.ZERO, Real.ZERO, Real.ONE, depth, squares);
        return squares;
    }

    private static void generateRecursive(Real x, Real y, Real size, int depth, List<Square> squares) {
        if (depth == 0) {
            squares.add(new Square(x, y, size));
            return;
        }

        Real newSize = size.divide(Real.of(3));

        // Generate 8 sub-squares (excluding center)
        int[][] positions = {
                { 0, 0 }, { 1, 0 }, { 2, 0 }, // Bottom row
                { 0, 1 }, { 2, 1 }, // Middle row (skip center {1,1})
                { 0, 2 }, { 1, 2 }, { 2, 2 } // Top row
        };

        for (int[] pos : positions) {
            Real newX = x.add(newSize.multiply(Real.of(pos[0])));
            Real newY = y.add(newSize.multiply(Real.of(pos[1])));
            generateRecursive(newX, newY, newSize, depth - 1, squares);
        }
    }

    /**
     * Calculates the Hausdorff dimension of the Cantor dust.
     * 
     * @return log(8)/log(3) Ã¢â€°Ë† 1.8928
     */
    public static double hausdorffDimension() {
        return Math.log(8) / Math.log(3);
    }

    /**
     * Calculates the total area of the dust at a given depth.
     * This approaches 0 as depth Ã¢â€ â€™ Ã¢Ë†Å¾.
     * 
     * @param depth recursion depth
     * @return total area = (8/9)^depth
     */
    public static double totalArea(int depth) {
        return Math.pow(8.0 / 9.0, depth);
    }

    /**
     * Calculates the number of squares at a given depth.
     * 
     * @param depth recursion depth
     * @return number of squares = 8^depth
     */
    public static long squareCount(int depth) {
        return (long) Math.pow(8, depth);
    }

    /**
     * Determines if a point (x, y) is in the Cantor dust.
     * This is an approximation up to the given depth.
     * 
     * @param x     x-coordinate
     * @param y     y-coordinate
     * @param depth approximation depth
     * @return true if the point is likely in the dust
     */
    public static boolean contains(double x, double y, int depth) {
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            return false;
        }

        double currentSize = 1.0;
        double currentX = 0.0;
        double currentY = 0.0;

        for (int d = 0; d < depth; d++) {
            currentSize /= 3.0;

            // Determine which 3rd the point is in
            int xIndex = (int) ((x - currentX) / currentSize);
            int yIndex = (int) ((y - currentY) / currentSize);

            // Center square is removed
            if (xIndex == 1 && yIndex == 1) {
                return false;
            }

            currentX += xIndex * currentSize;
            currentY += yIndex * currentSize;
        }

        return true;
    }

    /**
     * Generates a 1D Cantor set for comparison.
     * 
     * @param depth recursion depth
     * @return list of intervals [start, end]
     */
    public static List<Real[]> cantorSet1D(int depth) {
        List<Real[]> intervals = new ArrayList<>();
        cantorSet1DRecursive(Real.ZERO, Real.ONE, depth, intervals);
        return intervals;
    }

    private static void cantorSet1DRecursive(Real start, Real end, int depth, List<Real[]> intervals) {
        if (depth == 0) {
            intervals.add(new Real[] { start, end });
            return;
        }

        Real length = end.subtract(start);
        Real third = length.divide(Real.of(3));

        // Left third
        cantorSet1DRecursive(start, start.add(third), depth - 1, intervals);

        // Right third (skip middle)
        cantorSet1DRecursive(start.add(third.multiply(Real.of(2))), end, depth - 1, intervals);
    }
}

