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

import org.jscience.awt.ContourPlot;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Sample program demonstrating use of the Swing/AWT contour plot
 * component.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class ContourPlotDemo extends Frame {
    /**
     * Creates a new ContourPlotDemo object.
     */
    public ContourPlotDemo() {
        super("JScience Contour Plot Demo");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });
        setSize(250, 250);
        add(new ContourPlot(createContourData()));
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        new ContourPlotDemo();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[][] createContourData() {
        double[][] data = new double[50][50];
        double x;
        double y;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                x = ((i - (data.length / 2.0)) * 3.0) / data.length;
                y = ((j - (data[0].length / 2.0)) * 3.0) / data[0].length;
                data[i][j] = Math.exp((-x * x) - (y * y));
            }
        }

        return data;
    }
}
