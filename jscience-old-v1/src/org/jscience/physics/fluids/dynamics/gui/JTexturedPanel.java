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
 * DOCUMENT ME!
 *
 * @author Alejandro "balrog" Rodriguez Gallego
 * @version 0.1
 */
public class JTexturedPanel extends javax.swing.JPanel {
    /** DOCUMENT ME! */
    private Image texture;

    /** DOCUMENT ME! */
    private int xt;

    /** DOCUMENT ME! */
    private int yt;

    /** DOCUMENT ME! */
    private int xc;

    /** DOCUMENT ME! */
    private int yc;

/**
     * Crea a nuevo JTexturedPanel
     */
    public JTexturedPanel() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param resourceName DOCUMENT ME!
     */
    public void setTextura(String resourceName) {
        // Load of the imagen mediante a media Tracker
        URL imagen = getClass().getResource(resourceName);

        Toolkit t = getToolkit();
        MediaTracker tracker = new MediaTracker(this);
        texture = t.getImage(imagen);
        tracker.addImage(texture, 0);

        try {
            tracker.waitForID(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        xt = texture.getWidth(this);
        yt = texture.getHeight(this);

        //System.out.println(""+xt+" "+textura);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        // Size of the component
        xc = getWidth();
        yc = getHeight();

        //System.out.println("No " +texture);
        if (texture != null) {
            // number of times that the mosaic is reapeated
            //System.out.println("Ok");
            int xr = (xc / xt) + 1;
            int yr = (yc / yt) + 1;

            for (int i = 0; i < xr; i++)
                for (int j = 0; j < yr; j++)
                    g.drawImage(texture, i * xt, j * yt, this);
        }
    }
}
