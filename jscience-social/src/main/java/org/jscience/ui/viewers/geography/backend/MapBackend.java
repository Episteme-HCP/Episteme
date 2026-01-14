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

package org.jscience.ui.viewers.geography.backend;

/**
 * Enumeration of available map rendering backends.
 * Similar to {@link org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend}
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public enum MapBackend {
    /**
     * Auto-detect best available backend.
     */
    AUTO(true, true),

    /**
     * JavaFX - Native rendering (Default).
     * Always available, basic map support.
     */
    JAVAFX(true, true),

    /**
     * Unfolding Maps - Processing-based library (Optional).
     * Requires unfoldingMap libraries.
     * @see <a href="https://github.com/ronit0717/unfoldingMap">unfoldingMap</a>
     */
    UNFOLDING(true, true),

    /**
     * OpenMap - Java toolkit for viewing spatial data (Optional).
     * Requires OpenMap libraries.
     * @see <a href="http://openmap-java.org/">OpenMap</a>
     */
    OPENMAP(true, false),

    /**
     * GeoTools - Open source Java GIS toolkit (Optional).
     * Requires GeoTools libraries.
     * @see <a href="https://www.osgeo.org/projects/geotools/">GeoTools</a>
     */
    GEOTOOLS(true, true),

    /**
     * Google GeoChart - Web-based geographic visualization (Optional).
     * Requires WebView and network connection.
     * @see <a href="https://developers.google.com/chart/interactive/docs/gallery/geochart">GeoChart</a>
     */
    GEOCHART(false, true);

    private final boolean supportsLayering;
    private final boolean supportsInteractive;

    MapBackend(boolean supportsLayering, boolean supportsInteractive) {
        this.supportsLayering = supportsLayering;
        this.supportsInteractive = supportsInteractive;
    }

    public boolean isSupportsLayering() {
        return supportsLayering;
    }

    public boolean isSupportsInteractive() {
        return supportsInteractive;
    }
}
