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

package org.jscience.ui.viewers.chemistry.backend;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import javafx.scene.paint.Color;


/**
 * Abstraction for molecular rendering engines.
 * implementations can use JavaFX (Native), Jmol, PyMOL, etc.
 */
public interface MolecularRenderer {

    /**
     * Clears the current view.
     */
    void clear();

    /**
     * Set global rendering style.
     * 
     * @param style The style to apply.
     */
    void setStyle(RenderStyle style);

    /**
     * Draw a single atom.
     * 
     * @param atom The atom data model.
     */
    void drawAtom(Atom atom);

    /**
     * Draw a bond between two atoms.
     * 
     * @param bond The bond data model.
     */
    void drawBond(Bond bond);

    /**
     * Set background color of the viewer.
     * 
     * @param color JavaFX Color (Implementations may convert this).
     */
    void setBackgroundColor(Color color);

    /**
     * Retrieves the native component (e.g. SubScene for JavaFX, JPanel for
     * Swing/Jmol).
     * 
     * @return The UI component.
     */
    Object getViewComponent();

    /**
     * Returns the backend type of this renderer.
     * 
     * @return The backend enum.
     */
    MolecularBackend getBackend();
}
