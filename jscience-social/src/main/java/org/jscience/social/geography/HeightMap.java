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

package org.jscience.social.geography;

import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.Quantity;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * A specialized map containing elevation data (Digital Elevation Model).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class HeightMap extends GeoMap {

    private static final long serialVersionUID = 1L;

    @Attribute
    private Matrix<Real> elevations;

    public HeightMap(String name, GeodeticCoordinate topLeft, Quantity<Length> width, Quantity<Length> height) {
        super(name, topLeft, null); // bottomRight unknown or need calculation, passing null is safe if optional
    }

    public Matrix<Real> getElevations() {
        return elevations;
    }

    public void setElevations(Matrix<Real> elevations) {
        this.elevations = elevations;
    }

    @Override
    public String toString() {
        return String.format("HeightMap: %s (%dx%d data points)", 
            getName(), 
            elevations != null ? elevations.rows() : 0, 
            elevations != null ? elevations.cols() : 0);
    }
}

