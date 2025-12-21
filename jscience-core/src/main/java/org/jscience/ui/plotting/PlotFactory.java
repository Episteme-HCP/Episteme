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
package org.jscience.ui.plotting;

import org.jscience.ui.plotting.backends.JavaFXPlot2D;

/**
 * Factory for creating plots with specified backend.
 * <p>
 * Auto-detects best available backend if not specified.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PlotFactory {

    private static PlottingBackend defaultBackend = PlottingBackend.AUTO;

    /**
     * Sets default backend for future plots.
     */
    public static void setDefaultBackend(PlottingBackend backend) {
        defaultBackend = backend;
    }

    /**
     * Creates 2D plot with default backend.
     */
    public static Plot2D create2D(String title) {
        return create2D(title, defaultBackend);
    }

    /**
     * Creates 2D plot with specified backend.
     */
    public static Plot2D create2D(String title, PlottingBackend backend) {
        if (backend == PlottingBackend.AUTO) {
            backend = detectBestBackend();
        }

        switch (backend) {
            case XCHART:
                return createXChartPlot(title);
            case JFREECHART:
                return createJFreeChartPlot(title);
            case JAVAFX:
            default:
                return new JavaFXPlot2D(title);
        }
    }

    /**
     * Creates 3D plot with default backend.
     */
    public static Plot3D create3D(String title) {
        return create3D(title, PlottingBackend.AUTO);
    }

    /**
     * Creates 3D plot with specified backend.
     */
    public static Plot3D create3D(String title, PlottingBackend backend) {
        // For now, 3D defaults to JavaFX
        // JZY3D support can be added here later
        return new org.jscience.ui.plotting.backends.JavaFXPlot3D(title);
    }

    /**
     * Auto-detects best available plotting backend.
     */
    private static PlottingBackend detectBestBackend() {
        // Try XChart first (modern, lightweight)
        if (isClassAvailable("org.knowm.xchart.XYChart")) {
            return PlottingBackend.XCHART;
        }

        // Try JFreeChart (feature-rich)
        if (isClassAvailable("org.jfree.chart.JFreeChart")) {
            return PlottingBackend.JFREECHART;
        }

        // Fallback to JavaFX (always available in Java 8+)
        return PlottingBackend.JAVAFX;
    }

    /**
     * Checks if class is available (backend library present).
     */
    private static boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Plot2D createXChartPlot(String title) {
        try {
            Class<?> clazz = Class.forName("org.jscience.ui.plotting.backends.XChartPlot2D");
            return (Plot2D) clazz.getConstructor(String.class).newInstance(title);
        } catch (Exception e) {
            System.err.println("XChart backend not available, falling back to JavaFX");
            return new JavaFXPlot2D(title);
        }
    }

    private static Plot2D createJFreeChartPlot(String title) {
        try {
            Class<?> clazz = Class.forName("org.jscience.ui.plotting.backends.JFreeChartPlot2D");
            return (Plot2D) clazz.getConstructor(String.class).newInstance(title);
        } catch (Exception e) {
            System.err.println("JFreeChart backend not available, falling back to JavaFX");
            return new JavaFXPlot2D(title);
        }
    }
}
