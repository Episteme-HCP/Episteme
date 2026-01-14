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

package org.jscience.awt;

import java.awt.*;


/**
 * The CategoryGraph2D superclass provides an abstract encapsulation of 2D
 * category graphs.
 *
 * @author Mark Hale
 * @version 1.2
 */
public abstract class CategoryGraph2D extends DoubleBufferedCanvas
    implements GraphDataListener {
    /** Data model. */
    protected CategoryGraph2DModel model;

    /** Origin. */
    protected Point origin = new Point();

    /** Padding. */
    protected final int scalePad = 5;

    /** DOCUMENT ME! */
    protected final int axisPad = 25;

    /** DOCUMENT ME! */
    protected int leftAxisPad;

/**
     * Constructs a 2D category graph.
     *
     * @param cgm DOCUMENT ME!
     */
    public CategoryGraph2D(CategoryGraph2DModel cgm) {
        model = cgm;
        model.addGraphDataListener(this);
    }

    /**
     * Sets the data plotted by this graph to the specified data.
     *
     * @param cgm DOCUMENT ME!
     */
    public final void setModel(CategoryGraph2DModel cgm) {
        model.removeGraphDataListener(this);
        model = cgm;
        model.addGraphDataListener(this);
        dataChanged(new GraphDataEvent(model));
    }

    /**
     * Returns the model used by this graph.
     *
     * @return DOCUMENT ME!
     */
    public final CategoryGraph2DModel getModel() {
        return model;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public abstract void dataChanged(GraphDataEvent e);

    /**
     * Returns the preferred size of this component.
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /**
     * Returns the minimum size of this component.
     *
     * @return DOCUMENT ME!
     */
    public Dimension getMinimumSize() {
        return new Dimension(200, 200);
    }
}
