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

package org.jscience.medicine.volumetric;

import java.awt.*;

import java.text.NumberFormat;

import javax.media.j3d.Canvas3D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class TimingCanvas3D extends Canvas3D {
    /** DOCUMENT ME! */
    static final boolean debug = false;

    /** DOCUMENT ME! */
    VolRendListener volRend;

    /** DOCUMENT ME! */
    long startTime;

    /** DOCUMENT ME! */
    long frameStartTime;

    /** DOCUMENT ME! */
    NumberFormat numFormat;

    /** DOCUMENT ME! */
    int numFrames = 0;

/**
     * Creates a new TimingCanvas3D object.
     *
     * @param gc      DOCUMENT ME!
     * @param volRend DOCUMENT ME!
     */
    public TimingCanvas3D(GraphicsConfiguration gc, VolRendListener volRend) {
        super(gc);
        this.volRend = volRend;
        numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(2);
        frameStartTime = System.currentTimeMillis();
    }

    /**
     * DOCUMENT ME!
     */
    public void preRender() {
        startTime = System.currentTimeMillis();
    }

    /**
     * DOCUMENT ME!
     */
    public void postRender() {
        //System.out.println("frame");
        if (numFrames++ > 4) {
            long duration = System.currentTimeMillis() - startTime;

            double renderSize = volRend.calcRenderSize() / (1024.0 * 1024.0);
            double renderTime = duration / 1000.0;
            double renderRate = renderSize / renderTime;

            if (renderSize != 0) {
                System.out.println("Last frame: " +
                    numFormat.format(renderTime) + " sec " +
                    numFormat.format(renderSize) + "M pixels " +
                    numFormat.format(renderRate) + "M pixels/sec");
            }
        }

        if ((numFrames % 10) == 0) {
            double framesDuration = (System.currentTimeMillis() -
                frameStartTime) / 1000.0;

            System.out.println("Last 10 frames rendered at " +
                numFormat.format(10.0 / framesDuration) + " frames/sec");

            frameStartTime = System.currentTimeMillis();
        }
    }
}
