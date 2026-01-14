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

//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class SkyViewFrame extends Frame implements WindowListener {
    /** DOCUMENT ME! */
    static final int H_SIZE = 300;

    /** DOCUMENT ME! */
    static final int V_SIZE = 300;

    /** DOCUMENT ME! */
    CelestialSphere app;

    /** DOCUMENT ME! */
    Image img;

    /** DOCUMENT ME! */
    SkyViewCanvas canvas;

/**
     * Creates a new SkyViewFrame object.
     *
     * @param celestialsphere DOCUMENT ME!
     * @param s               DOCUMENT ME!
     */
    public SkyViewFrame(CelestialSphere celestialsphere, String s) {
        super("Celestial Sphere: SkyView");
        app = celestialsphere;
        addWindowListener(this);
        setBackground(Color.black);
        setForeground(Color.white);

        try {
            URL url = new URL(app.getCodeBase(),
                    "getSkyViewImage.php?VCOORD=" + s);
            img = Toolkit.getDefaultToolkit().getImage(url);
            System.out.println(url.toString());
        } catch (MalformedURLException _ex) {
        }

        canvas = new SkyViewCanvas(img);
        canvas.setSize(300, 300);
        add(canvas);
        pack();
        setResizable(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowDeactivated(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowClosing(WindowEvent windowevent) {
        dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowOpened(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowClosed(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowDeiconified(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowActivated(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowIconified(WindowEvent windowevent) {
    }
}
