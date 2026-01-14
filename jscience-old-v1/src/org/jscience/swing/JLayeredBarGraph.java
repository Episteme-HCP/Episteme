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

import org.jscience.awt.CategoryGraph2DModel;

import java.awt.*;


/**
 * A layered bar graph Swing component. Multiple series are layered.
 *
 * @author Mark Hale
 * @version 1.2
 */
public class JLayeredBarGraph extends JBarGraph {
/**
     * Constructs a layered bar graph.
     *
     * @param gm DOCUMENT ME!
     */
    public JLayeredBarGraph(CategoryGraph2DModel gm) {
        super(gm);
    }

    /**
     * Draws the graph bars.
     *
     * @param g DOCUMENT ME!
     */
    protected void drawBars(Graphics g) {
        // bars
        int numSeries = 1;
        model.firstSeries();

        while (model.nextSeries())
            numSeries++;

        if (numSeries == 1) {
            for (int i = 0; i < model.seriesLength(); i++) {
                drawBar(g, i, model.getValue(i), barColor[0]);
            }
        } else {
            float[] seriesValue = new float[numSeries];
            Color[] seriesColor = new Color[numSeries];

            for (int i = 0; i < model.seriesLength(); i++) {
                model.firstSeries();

                for (int j = 0; j < numSeries; j++) {
                    seriesValue[j] = model.getValue(i);
                    seriesColor[j] = barColor[j];
                    model.nextSeries();
                }

                // sort
                float val;
                Color col;

                for (int k, j = 1; j < numSeries; j++) {
                    val = seriesValue[j];
                    col = seriesColor[j];

                    for (k = j - 1; (k >= 0) && (seriesValue[k] < val); k--) {
                        seriesValue[k + 1] = seriesValue[k];
                        seriesColor[k + 1] = seriesColor[k];
                    }

                    seriesValue[k + 1] = val;
                    seriesColor[k + 1] = col;
                }

                // draw
                for (int j = 0; j < numSeries; j++)
                    drawBar(g, i, seriesValue[j], seriesColor[j]);
            }
        }
    }

    /**
     * Draws a bar.
     *
     * @param g DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param color DOCUMENT ME!
     */
    private void drawBar(Graphics g, int pos, float value, Color color) {
        Point p;

        if (value < 0.0f) {
            p = dataToScreen(pos, 0.0f);
        } else {
            p = dataToScreen(pos, value);
        }

        g.setColor(color);

        final int dy = Math.abs(p.y - origin.y);
        g.fillRect(p.x + barPad, p.y, barWidth, dy);
        g.setColor(Color.black);
        g.drawRect(p.x + barPad, p.y, barWidth, dy);
    }
}
