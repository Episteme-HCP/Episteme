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
 * An extension of Border that draws an etched border.
 * <p/>
 * Drawn etchedIn by default, drawing style used by paint() is
 * controlled by etchedIn() and etchedOut().  Note that
 * etchedIn() and etchedOut() do not result in anything being
 * painted, but only set the state for the next call to paint().
 * To set the state and paint in one operation, use
 * paintEtchedIn() and paintEtchedOut().<p>
 * <p/>
 * The current state of the border may be obtained by calling
 * isEtchedIn().<p>
 *
 * @author David Geary
 * @version 1.0, Apr 1 1996
 * @see Border
 * @see ThreeDRectangle
 * @see gjt.test.BorderTest
 */
public class EtchedBorder extends Border {
    public EtchedBorder(Component borderMe) {
        this(borderMe, _defaultThickness, _defaultGap);
    }

    public EtchedBorder(Component borderMe,
                        int borderThickness) {
        this(borderMe, borderThickness, _defaultGap);
    }

    public EtchedBorder(Component borderMe,
                        int borderThickness, int gap) {
        super(borderMe, borderThickness, gap);
    }

    public void etchedIn() {
        ((EtchedRectangle) border()).etchedIn();
    }

    public void etchedOut() {
        ((EtchedRectangle) border()).etchedOut();
    }

    public void paintEtchedIn() {
        ((EtchedRectangle) border()).paintEtchedIn();
    }

    public void paintEtchedOut() {
        ((EtchedRectangle) border()).paintEtchedOut();
    }

    public boolean isEtchedIn() {
        return ((EtchedRectangle) border()).isEtchedIn();
    }

    protected String paramString() {
        return super.paramString() + (EtchedRectangle) border();
    }

    protected DrawnRectangle border() {
        if (border == null)
            border = new EtchedRectangle(this, thickness);
        return border;
    }
}
