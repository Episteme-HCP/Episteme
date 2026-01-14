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

import java.applet.Applet;

import java.awt.*;
import java.awt.event.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class CircuitApplet extends Applet implements ComponentListener {
    /**
     * DOCUMENT ME!
     */
    CircuitFrame ogf;

    /**
     * DOCUMENT ME!
     */
    boolean started = false;

    /**
     * DOCUMENT ME!
     */
    void destroyFrame() {
        if (ogf != null) {
            ogf.dispose();
        }

        ogf = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
        addComponentListener(this);
    }

    /**
     * DOCUMENT ME!
     */
    void showFrame() {
        if (ogf == null) {
            started = true;
            ogf = new CircuitFrame(this);
            ogf.init();
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void toggleSwitch(int x) {
        ogf.toggleSwitch(x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        String s = "Applet is open in a separate window.";

        if (!started) {
            s = "Applet is starting.";
        } else if (ogf == null) {
            s = "Applet is finished.";
        } else if (ogf.useFrame) {
            ogf.show();
        }

        g.drawString(s, 10, 30);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentHidden(ComponentEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentMoved(ComponentEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentShown(ComponentEvent e) {
        showFrame();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentResized(ComponentEvent e) {
        if (ogf != null) {
            ogf.componentResized(e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        if (ogf != null) {
            ogf.dispose();
        }

        ogf = null;
        repaint();
    }
}
