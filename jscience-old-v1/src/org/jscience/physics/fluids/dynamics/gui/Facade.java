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

package org.jscience.physics.fluids.dynamics.gui;

import java.awt.*;

import java.net.URL;


/**
 * Shows an image centered on the screen.
 */
public class Facade extends Window // implements Runnable
 {
    /** DOCUMENT ME! */
    Image img = null;

/**
     * Creates a new Facade object.
     *
     * @param resourceName DOCUMENT ME!
     */
    public Facade(String resourceName) {
        super(new Frame());

        // Load the image
        URL imagen = getClass().getResource(resourceName);
        Toolkit t = getToolkit();
        MediaTracker tracker = new MediaTracker(this);
        img = t.getImage(imagen);
        tracker.addImage(img, 0);

        try {
            tracker.waitForID(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Center on the screen
        Dimension ds = getToolkit().getScreenSize();
        int xt = img.getWidth(null);
        int yt = img.getHeight(null);
        setLocation((ds.width - xt) / 2, (ds.height - yt) / 2);
        setSize(xt, yt);

        show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        }
    }
}
