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
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.gui;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class EditDialogLayout implements LayoutManager {
    /**
     * Creates a new EditDialogLayout object.
     */
    public EditDialogLayout() {
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
        int targeth = target.size().height - (insets.top + insets.bottom);
        int i;
        int h = insets.top;
        int pw = 300;
        int x = 0;

        for (i = 0; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);
            boolean newline = true;

            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();

                if (pw < d.width) {
                    pw = d.width;
                }

                if (m instanceof Scrollbar) {
                    h += 10;
                    d.width = targetw - x;
                }

                if (m instanceof Choice && (d.width > targetw)) {
                    d.width = targetw - x;
                }

                if (m instanceof Label) {
                    Dimension d2 = target.getComponent(i + 1).getPreferredSize();

                    if (d.height < d2.height) {
                        d.height = d2.height;
                    }

                    h += (d.height / 5);
                    newline = false;
                }

                if (m instanceof Button) {
                    if (x == 0) {
                        h += 20;
                    }

                    if (i != (target.getComponentCount() - 1)) {
                        newline = false;
                    }
                }

                m.move(insets.left + x, h);
                m.resize(d.width, d.height);

                if (newline) {
                    h += d.height;
                    x = 0;
                } else {
                    x += d.width;
                }
            }
        }

        if (target.size().height < h) {
            target.resize(pw + insets.right, h + insets.bottom);
        }
    }
}
;
