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

/*
 * ComponentPrintable.java
 *
 * Created on October 24, 2001, 11:28 AM
 */
package org.jscience.physics.nuclear.kinematics.nuclear.gui;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.swing.*;


/**
 * Class copied from "Java 2D Graphics" by J. Knudsen, wraps AWT & Swing
 * Componenents in order to render them on a printer.
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class ComponentPrintable implements Printable {
    /**
     * DOCUMENT ME!
     */
    private Component mComponent;

/**
     * Creates new ComponentPrintable
     */
    public ComponentPrintable(Component c) {
        mComponent = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param pageFormat DOCUMENT ME!
     * @param pageIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     */
    public int print(java.awt.Graphics g, PageFormat pageFormat, int pageIndex)
        throws java.awt.print.PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        boolean wasBuffered = disableDoubleBuffering(mComponent);
        mComponent.paint(g2);
        restoreDoubleBuffering(mComponent, wasBuffered);

        return PAGE_EXISTS;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean disableDoubleBuffering(Component c) {
        if (!(c instanceof JComponent)) {
            return false;
        }

        JComponent jc = (JComponent) c;
        boolean wasBuffered = jc.isDoubleBuffered();
        jc.setDoubleBuffered(false);

        return wasBuffered;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param wasBuffered DOCUMENT ME!
     */
    private void restoreDoubleBuffering(Component c, boolean wasBuffered) {
        if (c instanceof JComponent) {
            ((JComponent) c).setDoubleBuffered(wasBuffered);
        }
    }
}
