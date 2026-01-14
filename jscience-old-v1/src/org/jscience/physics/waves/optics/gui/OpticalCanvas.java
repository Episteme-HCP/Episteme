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

import org.jscience.physics.waves.optics.elements.OpticalDevice;
import org.jscience.physics.waves.optics.rays.RayCaster;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class OpticalCanvas extends Canvas {
    /** DOCUMENT ME! */
    Image offScreenBuffer;

    /** DOCUMENT ME! */
    OpticalDevice v = null;

    /** DOCUMENT ME! */
    RayCaster raycaster = null;

    /** DOCUMENT ME! */
    private boolean shouldRedraw = true;

/**
     * Creates a new OpticalCanvas object.
     */
    public OpticalCanvas() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void setDevice(OpticalDevice e) {
        v = e;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setRayCaster(RayCaster r) {
        raycaster = r;
    }

    /**
     * DOCUMENT ME!
     */
    public void forceRedraw() {
        shouldRedraw = true;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.clearRect(0, 0, getSize().width, getSize().height);
        v.draw(g);

        if (raycaster != null) {
            raycaster.drawRays(g, v);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void update(Graphics g) {
        Graphics gr;

        // Will hold the graphics context from the offScreenBuffer.
        // We need to make sure we keep our offscreen buffer the same size
        // as the graphics context we're working with.
        if ((offScreenBuffer == null) ||
                (!((offScreenBuffer.getWidth(this) == this.getSize().width) &&
                (offScreenBuffer.getHeight(this) == this.getSize().height)))) {
            offScreenBuffer = this.createImage(getSize().width, getSize().height);
            shouldRedraw = true;
        }

        // We need to use our buffer Image as a Graphics object:
        gr = offScreenBuffer.getGraphics();

        if (shouldRedraw) {
            paint(gr);
            shouldRedraw = false;
        }

        // Passes our off-screen buffer to our paint method, which,
        // unsuspecting, paints on it just as it would on the Graphics
        // passed by the browser or applet viewer.
        g.drawImage(offScreenBuffer, 0, 0, this);

        // And now we transfer the info in the buffer onto the
        // graphics context we got from the browser in one smooth motion.
    }
}
