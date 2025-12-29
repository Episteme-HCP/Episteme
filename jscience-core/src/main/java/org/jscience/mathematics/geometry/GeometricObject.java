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

package org.jscience.mathematics.geometry;

/**
 * Base interface for all geometric objects.
 * <p>
 * A geometric object is any mathematical entity that exists in a geometric
 * space,
 * such as points, lines, planes, curves, surfaces, etc.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface GeometricObject<T> {

    /**
     * Returns the intrinsic dimension of this geometric object.
     * <p>
     * Examples:
     * <ul>
     * <li>Point: 0</li>
     * <li>Line/Segment: 1</li>
     * <li>Plane/Surface: 2</li>
     * <li>Volume: 3</li>
     * </ul>
     * </p>
     * 
     * @return the dimension
     */
    int dimension();

    /**
     * Returns the dimension of the ambient space containing this object.
     * <p>
     * For example, a 2D plane in 3D space has dimension=2 but ambientDimension=3.
     * </p>
     * 
     * @return the ambient space dimension
     */
    int ambientDimension();

    /**
     * Returns a human-readable description of this geometric object.
     * 
     * @return description string
     */
    String description();
}
