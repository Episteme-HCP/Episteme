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

package org.jscience.ui.viewers.mathematics.analysis.plotting;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import java.util.List;

/**
 * 3D plotting interface for JScience.
 * <p>
 * Surface plots, 3D scatter, mesh visualization.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Plot3D {

    /**
     * Adds surface: z = f(x, y)
     */
    Plot3D addSurface(Function<Real[], Real> function,
            Real xMin, Real xMax,
            Real yMin, Real yMax,
            String label);

    /**
     * Adds 3D scatter data.
     */
    Plot3D addScatter(List<Real> xData, List<Real> yData, List<Real> zData, String label);

    /**
     * Adds parametric curve: (x(t), y(t), z(t))
     */
    Plot3D addParametricCurve(Function<Real, Real> xFunc,
            Function<Real, Real> yFunc,
            Function<Real, Real> zFunc,
            Real tMin, Real tMax,
            String label);

    /**
     * Sets plot title.
     */
    Plot3D setTitle(String title);

    /**
     * Sets axis labels.
     */
    Plot3D setAxisLabels(String xLabel, String yLabel, String zLabel);

    /**
     * Sets camera position (azimuth, elevation).
     */
    Plot3D setViewAngle(double azimuth, double elevation);

    /**
     * Enables/disables interactive rotation.
     */
    Plot3D setInteractive(boolean enabled);

    /**
     * Sets colormap for surface.
     */
    Plot3D setColormap(Colormap colormap);

    /**
     * Displays plot.
     */
    void show();

    /**
     * Saves plot.
     */
    void save(String filename, PlotFormat format);

    /**
     * Predefined colormaps.
     */
    public enum Colormap {
        JET, // Rainbow (blue -> red)
        VIRIDIS, // Perceptually uniform
        PLASMA, // High contrast
        GRAYSCALE, // Black to white
        HOT, // Black -> red -> yellow -> white
        COOL // Cyan to magenta
    }
}



