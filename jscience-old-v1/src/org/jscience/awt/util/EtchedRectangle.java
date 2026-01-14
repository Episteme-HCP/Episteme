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

package org.jscience.awt.util;

import java.awt.*;

/**
 * A DrawnRectangle that draws an etched border.<p>
 * <p/>
 * Drawn etched in by default, drawing style used by paint() is
 * controlled by etchedIn() and etchedOut().  Note that
 * etchedIn() and etchedOut() do not result in anything being
 * painted, but only set the state for the next call to paint().
 * To set the state and paint in one operation, use
 * paintEtchedIn() and paintEtchedOut().<p>
 * <p/>
 * Although it is permissible to set the thickness of
 * EtchedRectangles, they tend to loose the etching effect
 * if thickness is greater than 4.<p>
 * <p/>
 * The current state of the rectangle may be obtained by
 * calling isEtchedIn().
 *
 * @author David Geary
 * @version 1.0, Apr 1 1996
 * @see DrawnRectangle
 * @see ThreeDRectangle
 * @see gjt.test.DrawnRectangleTest
 */
public class EtchedRectangle extends DrawnRectangle {
    protected static Etching _defaultEtching = Etching.IN;
    private Etching etching;

    public EtchedRectangle(Component drawInto) {
        this(drawInto, _defaultEtching,
                _defaultThickness, 0, 0, 0, 0);
    }

    public EtchedRectangle(Component drawInto, int thickness) {
        this(drawInto, _defaultEtching, thickness, 0, 0, 0, 0);
    }

    public EtchedRectangle(Component drawInto, int x, int y,
                           int w, int h) {
        this(drawInto, _defaultEtching,
                _defaultThickness, x, y, w, h);
    }

    public EtchedRectangle(Component drawInto, int thickness,
                           int x, int y,
                           int w, int h) {
        this(drawInto, _defaultEtching, thickness, x, y, w, h);
    }

    public EtchedRectangle(Component drawInto, Etching etching,
                           int thickness, int x, int y,
                           int w, int h) {
        super(drawInto, thickness, x, y, w, h);
        this.etching = etching;
    }

    public void etchedIn() {
        etching = Etching.IN;
    }

    public void etchedOut() {
        etching = Etching.OUT;
    }

    public boolean isEtchedIn() {
        return etching == Etching.IN;
    }

    public void paint() {
        if (etching == Etching.IN)
            paintEtchedIn();
        else
            paintEtchedOut();
    }

    public void paintEtchedIn() {
        Graphics g = drawInto.getGraphics();
        if (g != null)
            paintEtched(g, getLineColor(), brighter());

        etchedIn();
    }

    public void paintEtchedOut() {
        Graphics g = drawInto.getGraphics();
        if (g != null)
            paintEtched(g, brighter(), getLineColor());

        etchedOut();
    }

    public String paramString() {
        return super.paramString() + "," + etching;
    }

    private void paintEtched(Graphics g,
                             Color topLeft,
                             Color bottomRight) {
        int thickness = getThickness();
        int w = width - thickness;
        int h = height - thickness;

        g.setColor(topLeft);
        for (int i = 0; i < thickness / 2; ++i)
            g.drawRect(x + i, y + i, w, h);

        g.setColor(bottomRight);

        for (int i = 0; i < thickness / 2; ++i)
            g.drawRect(x + (thickness / 2) + i,
                    y + (thickness / 2) + i, w, h);

        g.dispose();
    }
}
