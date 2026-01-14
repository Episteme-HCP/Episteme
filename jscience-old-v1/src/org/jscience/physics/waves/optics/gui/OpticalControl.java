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

package org.jscience.physics.waves.optics.gui;

import java.awt.*;
import java.awt.event.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class OpticalControl extends Panel implements ItemListener,
    AdjustmentListener, ActionListener {
    /** DOCUMENT ME! */
    private RespondToEvents resp;

/**
     * Creates a new OpticalControl object.
     *
     * @param a DOCUMENT ME!
     */
    public OpticalControl(RespondToEvents a) {
        resp = a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getAdjustable() instanceof Scrollbar) {
            resp.scrollbar(((Scrollbar) e.getAdjustable()).getName(),
                e.getValue());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getItemSelectable() instanceof Checkbox) {
            resp.checkbox(((Checkbox) e.getItemSelectable()).getName(),
                (e.getStateChange() == ItemEvent.SELECTED));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Button) {
            resp.button(((Button) e.getSource()).getLabel());
        }
    }
}
