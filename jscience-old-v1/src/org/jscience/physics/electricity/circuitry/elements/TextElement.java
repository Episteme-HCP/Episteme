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

// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import org.jscience.physics.electricity.circuitry.CircuitElement;
import org.jscience.physics.electricity.circuitry.gui.EditInfo;

import java.awt.*;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class TextElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public String text;

    /**
     * DOCUMENT ME!
     */
    public int size;

    /**
     * DOCUMENT ME!
     */
    public final int FLAG_CENTER = 1;

    /**
     * DOCUMENT ME!
     */
    public final int FLAG_BAR = 2;

    /**
     * Creates a new TextElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public TextElement(int xx, int yy) {
        super(xx, yy);
        text = "hello";
        size = 24;
    }

    /**
     * Creates a new TextElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public TextElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        size = new Integer(st.nextToken()).intValue();
        text = st.nextToken();

        while (st.hasMoreTokens())
            text += (" " + st.nextToken());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return ((char) getDumpType()) + " " + x + " " + y + " " + x2 + " " +
        y2 + " " + flags + " " + size + " " + text;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'x';
    }

    /**
     * DOCUMENT ME!
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public void drag(int xx, int yy) {
        x = xx;
        y = yy;
        x2 = xx + 16;
        y2 = yy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.lightGray);

        Font f = new Font("SansSerif", 0, size);
        g.setFont(f);

        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(text);

        if ((flags & FLAG_CENTER) != 0) {
            x = (circuitFrame.winSize.width - w) / 2;
        }

        g.drawString(text, x, y);

        if ((flags & FLAG_BAR) != 0) {
            int by = y - fm.getAscent();
            g.drawLine(x, by, (x + w) - 1, by);
        }

        setBbox(x, y - fm.getAscent(), x + w, y + fm.getDescent());
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            EditInfo ei = new EditInfo("Text", 0, -1, -1);
            ei.text = text;

            return ei;
        }

        if (n == 1) {
            return new EditInfo("Size", size, 5, 100);
        }

        if (n == 2) {
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Inverted", (flags & FLAG_BAR) != 0);

            return ei;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param ei DOCUMENT ME!
     */
    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            text = ei.textf.getText();
        }

        if (n == 1) {
            size = (int) ei.value;
        }

        if (n == 2) {
            if (ei.checkbox.getState()) {
                flags |= FLAG_BAR;
            } else {
                flags &= ~FLAG_BAR;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 0;
    }
}
