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
package org.jscience.mathematics.util;

import org.jscience.mathematics.number.Real;
import java.util.stream.Stream;
import java.util.stream.DoubleStream;

/**
 * Utility class for converting between primitive streams and JScience types.
 * <p>
 * Provides stream conversion methods for:
 * <ul>
 * <li>DoubleStream to Stream&lt;Real&gt;</li>
 * <li>Stream&lt;Real&gt; to DoubleStream</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class StreamConverters {

    private StreamConverters() {
        // Utility class - no instantiation
    }

    /**
     * Converts a DoubleStream to Stream&lt;Real&gt;.
     * 
     * @param stream the input DoubleStream
     * @return Stream of Real values
     */
    public static Stream<Real> toReal(DoubleStream stream) {
        if (stream == null)
            return null;
        return stream.mapToObj(Real::of);
    }

    /**
     * Converts a Stream&lt;Real&gt; to DoubleStream.
     * 
     * @param stream the input Stream of Real values
     * @return DoubleStream of primitive double values
     */
    public static DoubleStream toDouble(Stream<Real> stream) {
        if (stream == null)
            return null;
        return stream.mapToDouble(Real::doubleValue);
    }

    /**
     * Converts a Stream&lt;Double&gt; to Stream&lt;Real&gt;.
     * 
     * @param stream the input Stream of Double objects
     * @return Stream of Real values
     */
    public static Stream<Real> fromDoubleObjects(Stream<Double> stream) {
        if (stream == null)
            return null;
        return stream.map(Real::of);
    }

    /**
     * Converts a Stream&lt;Real&gt; to Stream&lt;Double&gt;.
     * 
     * @param stream the input Stream of Real values
     * @return Stream of Double objects
     */
    public static Stream<Double> toDoubleObjects(Stream<Real> stream) {
        if (stream == null)
            return null;
        return stream.map(Real::doubleValue);
    }
}


