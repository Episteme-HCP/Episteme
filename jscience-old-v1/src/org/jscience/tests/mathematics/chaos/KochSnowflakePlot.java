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

import org.jscience.mathematics.chaos.*;

import java.applet.Applet;

import java.awt.*;


/**
 * Plot of the Koch snowflake.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class KochSnowflakePlot extends Applet {
    /**
     * DOCUMENT ME!
     */
    private final int N = 5;

    /**
     * DOCUMENT ME!
     */
    private Image img;

    /**
     * DOCUMENT ME!
     */
    private KochCurveGraphic curve;

    /**
     * DOCUMENT ME!
     */
    private int width;

    /**
     * DOCUMENT ME!
     */
    private int height;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        width = getSize().width;
        height = getSize().height;
        img = createImage(width, height);
        curve = new KochCurveGraphic(img.getGraphics());

        final int len = width / 2;
        final int h_2 = (int) Math.round((len * Math.sqrt(3.0)) / 4.0);
        curve.draw((width - len) / 2, (height / 2) - h_2, width / 2,
            (height / 2) + h_2, N);
        curve.draw(width / 2, (height / 2) + h_2, (width + len) / 2,
            (height / 2) - h_2, N);
        curve.draw((width + len) / 2, (height / 2) - h_2, (width - len) / 2,
            (height / 2) - h_2, N);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    class KochCurveGraphic extends KochCurve {
        /**
         * DOCUMENT ME!
         */
        private final Graphics g;

        /**
         * Creates a new KochCurveGraphic object.
         *
         * @param grafixs DOCUMENT ME!
         */
        public KochCurveGraphic(Graphics grafixs) {
            g = grafixs;
        }

        /**
         * DOCUMENT ME!
         *
         * @param startX DOCUMENT ME!
         * @param startY DOCUMENT ME!
         * @param endX DOCUMENT ME!
         * @param endY DOCUMENT ME!
         * @param n DOCUMENT ME!
         */
        public void draw(int startX, int startY, int endX, int endY, int n) {
            g.setColor(Color.black);
            g.drawLine(startX, height - startY, endX, height - endY);
            recurse(startX, startY, endX, endY, n);
        }

        /**
         * DOCUMENT ME!
         *
         * @param startX DOCUMENT ME!
         * @param startY DOCUMENT ME!
         * @param endX DOCUMENT ME!
         * @param endY DOCUMENT ME!
         */
        protected void drawLine(double startX, double startY, double endX,
            double endY) {
            g.drawLine((int) Math.round(startX),
                height - (int) Math.round(startY), (int) Math.round(endX),
                height - (int) Math.round(endY));
        }

        /**
         * DOCUMENT ME!
         *
         * @param startX DOCUMENT ME!
         * @param startY DOCUMENT ME!
         * @param endX DOCUMENT ME!
         * @param endY DOCUMENT ME!
         */
        protected void eraseLine(double startX, double startY, double endX,
            double endY) {
            g.setColor(getBackground());
            drawLine(startX, startY, endX, endY);
            g.setColor(Color.black);
        }
    }
}
