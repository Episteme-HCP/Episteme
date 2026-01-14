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

import org.jscience.awt.DefaultGraph2DModel;
import org.jscience.awt.ScatterGraph;

import org.jscience.mathematics.chaos.*;

import java.applet.Applet;

import java.awt.*;


/**
 * Plot of the Henon map.
 *
 * @author Mark Hale
 * @version 1.1
 */
public final class HenonPlot extends Applet {
    /**
     * DOCUMENT ME!
     */
    private HenonMap cm;

    /**
     * DOCUMENT ME!
     */
    private ScatterGraph graph;

    /**
     * DOCUMENT ME!
     */
    private final int N = 10000;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        cm = new HenonMap(cm.A_CHAOS, cm.B_CHAOS);

        float[] xData = new float[N];
        float[] yData = new float[N];
        double[] x = { 0.0, 0.0 };

        for (int i = 0; i < N; i++) {
            xData[i] = (float) x[0];
            yData[i] = (float) x[1];
            x = cm.map(x);
        }

        DefaultGraph2DModel model = new DefaultGraph2DModel();
        model.setXAxis(xData);
        model.addSeries(yData);
        graph = new ScatterGraph(model);
        graph.setNumbering(false);
        setLayout(new BorderLayout());
        add(graph, "Center");
    }
}
