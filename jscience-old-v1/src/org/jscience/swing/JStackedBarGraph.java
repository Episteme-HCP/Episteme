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
import org.jscience.awt.GraphDataEvent;

import java.awt.*;

/**
 * A stacked bar graph Swing component.
 * Multiple series are stacked.
 *
 * @author Lindsay Laird
 * @version 1.2
 */
public class JStackedBarGraph extends JBarGraph {
    /**
     * Constructs a stacked bar graph.
     */
    public JStackedBarGraph(CategoryGraph2DModel gm) {
        super(gm);
    }

    /**
     * Implementation of GraphDataListener.
     * Application code will not use this method explicitly, it is used internally.
     */
    public void dataChanged(GraphDataEvent e) {
        float tmp;
        minY = 0.0f;
        maxY = Float.NEGATIVE_INFINITY;
        model.firstSeries();
        for (int i = 0; i < model.seriesLength(); i++) {
            tmp = model.getValue(i);
            while (model.nextSeries())
                tmp += model.getValue(i);
            minY = Math.min(tmp, minY);
            maxY = Math.max(tmp, maxY);
            model.firstSeries();
        }
        if (minY == maxY) {
            minY -= 0.5f;
            maxY += 0.5f;
        }
        setNumbering(numbering);
    }

    /**
     * Draws the graph bars.
     */
    protected void drawBars(Graphics g) {
// bars
        int dy, totalDy;
        for (int i = 0; i < model.seriesLength(); i++) {
            model.firstSeries();
            dy = drawStackedBar(g, i, model.getValue(i), barColor[0], 0);
            totalDy = dy;
            for (int n = 1; model.nextSeries(); n++) {
                dy = drawStackedBar(g, i, model.getValue(i), barColor[n], -totalDy);
                totalDy += dy;
            }
        }
    }

    /**
     * Draws a bar.
     *
     * @return the height of the bar.
     */
    private int drawStackedBar(Graphics g, int pos, float value, Color color, int yoffset) {
        Point p;
        if (value < 0.0f)
            p = dataToScreen(pos, 0.0f);
        else
            p = dataToScreen(pos, value);
        g.setColor(color);
        final int dy = Math.abs(p.y - origin.y);
        g.fillRect(p.x + barPad, p.y + yoffset, barWidth, dy);
        g.setColor(Color.black);
        g.drawRect(p.x + barPad, p.y + yoffset, barWidth, dy);
        return dy;
    }
}


