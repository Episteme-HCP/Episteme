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

package org.jscience.core.ui.viewers.mathematics.discrete;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Common interface for graph renderers.
 * <p>
 * Abstraction layer to support different rendering backends (JavaFX, GraphStream, JGraphT visualizations, etc.)
 * while maintaining a consistent API for layout configuration and interaction.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface GraphRenderer {

    /**
     * Renders the given graph data.
     * 
     * @param graphData The graph object to render (typically org.jscience.core.mathematics.discrete.Graph)
     */
    void renderGraph(Object graphData);

    /**
     * Set repulsion force constant (for force-directed layouts).
     * 
     * @param repulsion Force constant
     */
    void setRepulsion(Real repulsion);

    /**
     * Set spring strength constant (for force-directed layouts).
     * 
     * @param strength Spring strength
     */
    void setSpringStrength(Real strength);

    /**
     * Set desired spring length (for force-directed layouts).
     * 
     * @param length Spring rest length
     */
    void setSpringLength(Real length);

    /**
     * Gets the visual component (Canvas, Panel, etc.).
     * 
     * @return The UI component
     */
    Object getViewComponent();
}
