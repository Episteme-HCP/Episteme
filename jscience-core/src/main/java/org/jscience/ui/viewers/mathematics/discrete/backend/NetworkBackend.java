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

package org.jscience.ui.viewers.mathematics.discrete.backend;

/**
 * Enumeration of available network/graph rendering backends.
 * Similar to {@link org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend}
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public enum NetworkBackend {
    /**
     * Auto-detect best available backend.
     */
    AUTO(true, true),

    /**
     * JavaFX - Native rendering (Default).
     * Always available, basic graph visualization.
     */
    JAVAFX(true, true),

    /**
     * GraphStream - Dynamic graph visualization (Optional).
     * Real-time graph changes and animations.
     * @see <a href="https://graphstream-project.org/">GraphStream</a>
     */
    GRAPHSTREAM(true, true),

    /**
     * JGraphX - Advanced graph component (Optional).
     * Feature-rich graph editing and layout.
     * @see <a href="https://github.com/jgraph/jgraphx">JGraphX</a>
     */
    JGRAPHX(true, true),

    /**
     * JUNG - Java Universal Network/Graph Framework (Optional).
     * Comprehensive graph analysis and visualization.
     * @see <a href="http://jung.sourceforge.net/">JUNG</a>
     */
    JUNG(true, true),

    /**
     * Gephi Toolkit - Network analysis platform (Optional).
     * Advanced network analysis algorithms.
     * @see <a href="https://gephi.org/toolkit/">Gephi</a>
     */
    GEPHI(true, false),

    /**
     * Prefuse - Interactive visualization toolkit (Optional).
     * Data-driven graph visualization.
     * @see <a href="http://prefuse.org/">Prefuse</a>
     */
    PREFUSE(true, true);

    private final boolean supports2D;
    private final boolean supports3D;

    NetworkBackend(boolean supports2D, boolean supports3D) {
        this.supports2D = supports2D;
        this.supports3D = supports3D;
    }

    public boolean isSupports2D() {
        return supports2D;
    }

    public boolean isSupports3D() {
        return supports3D;
    }
}
