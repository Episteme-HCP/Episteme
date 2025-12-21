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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.util;

import org.jscience.mathematics.numbers.real.Real;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Utility class for converting between collections of primitives and JScience
 * types.
 * <p>
 * Provides collection conversion methods for:
 * <ul>
 * <li>List&lt;Double&gt; to List&lt;Real&gt;</li>
 * <li>List&lt;Real&gt; to List&lt;Double&gt;</li>
 * </ul>
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CollectionConverters {

    private CollectionConverters() {
        // Utility class - no instantiation
    }

    /**
     * Converts a List&lt;Double&gt; to List&lt;Real&gt;.
     * 
     * @param list the input List of Double objects
     * @return List of Real values
     */
    @SuppressWarnings("null")
    public static List<Real> toReal(List<Double> list) {
        if (list == null)
            return null;
        return list.stream()
                .map(Real::of)
                .collect(Collectors.toList());
    }

    /**
     * Converts a List&lt;Real&gt; to List&lt;Double&gt;.
     * 
     * @param list the input List of Real values
     * @return List of Double objects
     */
    public static List<Double> toDouble(List<Real> list) {
        if (list == null)
            return null;
        return list.stream()
                .map(Real::doubleValue)
                .collect(Collectors.toList());
    }

    /**
     * Converts a List&lt;Real&gt; to primitive double array.
     * 
     * @param list the input List of Real values
     * @return array of primitive double values
     */
    public static double[] toDoubleArray(List<Real> list) {
        if (list == null)
            return null;
        return list.stream()
                .mapToDouble(Real::doubleValue)
                .toArray();
    }

    /**
     * Converts a primitive double array to List&lt;Real&gt;.
     * 
     * @param arr the input double array
     * @return List of Real values
     */
    public static List<Real> fromDoubleArray(double[] arr) {
        if (arr == null)
            return null;
        List<Real> result = new ArrayList<>(arr.length);
        for (double v : arr) {
            result.add(Real.of(v));
        }
        return result;
    }
}