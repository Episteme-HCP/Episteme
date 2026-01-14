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

package org.jscience.architecture.traffic;

import java.awt.*;


/*
 *The Splash screen for the program, which gets the focus on starting up the application
 */
public class SplashScreen extends Window {
    /** DOCUMENT ME! */
    private static final int WIDTH = 550;

    /** DOCUMENT ME! */
    private static final int HEIGHT = 424;

    /** DOCUMENT ME! */
    Image image;

/**
     * Creates a new SplashScreen object.
     *
     * @param parent DOCUMENT ME!
     */
    public SplashScreen(Controller parent) {
        super(parent);

        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.createImage("org/jscience/architecture/images/splash.jpg");
        requestFocus();

        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 0);

        try {
            mt.waitForID(0);
        } catch (InterruptedException e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        setSize(WIDTH, HEIGHT);
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(image, 0, 0, this);
        toFront();
    }
}
