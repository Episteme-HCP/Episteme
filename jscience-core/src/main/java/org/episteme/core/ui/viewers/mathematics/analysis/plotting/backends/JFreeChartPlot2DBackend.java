/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.ui.viewers.mathematics.analysis.plotting.backends;

import org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackend;

/**
 * Backend for JFreeChart 2D plotting.
 * Available when JFreeChart library is on classpath.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JFreeChartPlot2DBackend implements PlottingBackend {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "jfreechart";
    }

    @Override
    public String getName() {
        return "JFreeChart";
    }

    @Override
    public String getDescription() {
        return "Professional charting and plot library.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jfree.chart.JFreeChart");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50; // Medium-high priority for 2D
    }
    @Override public boolean isSupported2D() { return true; }
    @Override public boolean isSupported3D() { return false; }

    @Override public Object createBackend() {
        return new JFreeChartPlot2D("");
    }
}


