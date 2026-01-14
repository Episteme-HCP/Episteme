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

import java.applet.Applet;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class OpticalApp extends Applet implements RespondToEvents {
/**
     * Creates a new OpticalApp object.
     */
    public OpticalApp() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void checkbox(String name, boolean value) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void button(String name) {
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void processEvent(AWTEvent e) {
        if (e.getID() == Event.WINDOW_DESTROY) {
            System.exit(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAppletInfo() {
        // Too funny to destroy
        // return "An interactive test of the Graphics.drawArc and \nGraphics.fillArc routines. Can be run \neither as a standalone application by typing 'java ArcTest' \nor as an applet in the AppletViewer.";
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void jbInit() throws Exception {
        this.setBackground(new Color(255, 255, 255));
    }
}
