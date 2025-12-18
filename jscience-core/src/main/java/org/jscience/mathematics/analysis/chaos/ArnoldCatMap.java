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
package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.geometry.Point2D;

/**
 * Arnold's Cat Map:
 * x_{n+1} = (2x_n + y_n) mod 1
 * y_{n+1} = (x_n + y_n) mod 1
 * <p>
 * A chaotic map from the torus into itself. It is an example of an Anosov
 * diffeomorphism.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ArnoldCatMap implements DiscreteMap<Point2D> {

    @Override
    public Point2D evaluate(Point2D p) {
        double x = p.getX().doubleValue();
        double y = p.getY().doubleValue();

        double nextX = (2 * x + y) % 1.0;
        double nextY = (x + y) % 1.0;

        // Ensure positive result for modulo
        if (nextX < 0)
            nextX += 1.0;
        if (nextY < 0)
            nextY += 1.0;

        return Point2D.of(nextX, nextY);
    }

    @Override
    public String getDomain() {
        return "[0, 1]² (Torus)";
    }

    @Override
    public String getCodomain() {
        return "[0, 1]² (Torus)";
    }

    @Override
    public String toString() {
        return "ArnoldCatMap";
    }
}
