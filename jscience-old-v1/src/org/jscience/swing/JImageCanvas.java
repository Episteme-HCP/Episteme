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

package org.jscience.swing;

import java.awt.*;

import javax.swing.*;


/**
 * The JImageCanvas class allows an image to be directly added to a
 * container.
 *
 * @author Daniel Lemire
 */
public class JImageCanvas extends JComponent {
    /** DOCUMENT ME! */
    protected Image image;

/**
     * Constructs an image canvas.
     *
     * @param img DOCUMENT ME!
     */
    public JImageCanvas(Image img) {
        image = img;
        waitForImage();
    }

    /**
     * Paints the canvas.
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    /**
     * DOCUMENT ME!
     */
    private void waitForImage() {
        MediaTracker tracker = new MediaTracker(this);

        try {
            tracker.addImage(image, 0);
            tracker.waitForAll();
            tracker.waitForID(0);

            if (tracker.checkID(0)) {
                repaint();
            } else {
                System.out.println("Could not load the image.");
            }
        } catch (InterruptedException e) {
        }
    }
}
