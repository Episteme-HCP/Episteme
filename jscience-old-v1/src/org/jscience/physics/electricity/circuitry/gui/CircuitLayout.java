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

// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
package org.jscience.physics.electricity.circuitry.gui;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class CircuitLayout implements LayoutManager {
    /**
     * Creates a new CircuitLayout object.
     */
    public CircuitLayout() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void addLayoutComponent(String name, Component c) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void removeLayoutComponent(Component c) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension preferredLayoutSize(Container target) {
        return new Dimension(500, 500);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension minimumLayoutSize(Container target) {
        return new Dimension(100, 100);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void layoutContainer(Container target) {
        Insets insets = target.insets();
        int targetw = target.size().width - insets.left - insets.right;
        int cw = (targetw * 8) / 10;
        int targeth = target.size().height - (insets.top + insets.bottom);
        target.getComponent(0).move(insets.left, insets.top);
        target.getComponent(0).resize(cw, targeth);

        int barwidth = targetw - cw;
        cw += insets.left;

        int i;
        int h = insets.top;

        for (i = 1; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);

            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();

                if (m instanceof Scrollbar) {
                    d.width = barwidth;
                }

                if (m instanceof Choice && (d.width > barwidth)) {
                    d.width = barwidth;
                }

                if (m instanceof Label) {
                    h += (d.height / 5);
                    d.width = barwidth;
                }

                m.move(cw, h);
                m.resize(d.width, d.height);
                h += d.height;
            }
        }
    }
}
