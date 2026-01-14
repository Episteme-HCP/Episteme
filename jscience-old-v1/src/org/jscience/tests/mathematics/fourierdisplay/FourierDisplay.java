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
import org.jscience.awt.LineGraph;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Sample program demonstrating use of FourierMath and LineGraph classes.
 *
 * @author Mark Hale
 * @version 1.3
 */
public final class FourierDisplay extends Frame {
    /**
     * DOCUMENT ME!
     */
    private final int N = 128;

    /**
     * DOCUMENT ME!
     */
    private List fns = new List(4);

    /**
     * DOCUMENT ME!
     */
    private Checkbox inverse = new Checkbox("inverse");

    /**
     * DOCUMENT ME!
     */
    private DefaultGraph2DModel model = new DefaultGraph2DModel();

    /**
     * DOCUMENT ME!
     */
    private double[] signal;

    /**
     * DOCUMENT ME!
     */
    private boolean doInverse = false;

    /**
     * Creates a new FourierDisplay object.
     */
    public FourierDisplay() {
        super("Fourier Display");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });

        float[] xAxis = new float[N];

        for (int i = 0; i < N; i++)
            xAxis[i] = i - (N / 2);

        model.setXAxis(xAxis);
        model.addSeries(xAxis);
        model.addSeries(xAxis);
        fns.add("Gaussian");
        fns.add("Top hat");
        fns.add("Constant");
        fns.add("Square");
        fns.add("Triangle");
        fns.add("Sine");
        fns.select(5);
        fns.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                    switch (fns.getSelectedIndex()) {
                    case 0:
                        signal = gaussianSignal(N);

                        break;

                    case 1:
                        signal = tophatSignal(1.0, N);

                        break;

                    case 2:
                        signal = constantSignal(1.0, N);

                        break;

                    case 3:
                        signal = squareWave(1.0, N);

                        break;

                    case 4:
                        signal = triangleWave(1.0, N);

                        break;

                    case 5:
                        signal = sineWave(1.0, N);

                        break;
                    }

                    displayTransform();
                }
            });
        inverse.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                    doInverse = !doInverse;
                    displayTransform();
                }
            });

        LineGraph graph = new LineGraph(model);
        graph.setColor(0, Color.red);

        Panel cntrl = new Panel();
        cntrl.add(fns);
        cntrl.add(inverse);
        add(graph, "Center");
        add(cntrl, "South");
        signal = sineWave(1.0, N);
        displayTransform();
        setDefaultSize(this, 400, 400);
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        new FourierDisplay();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param defaultWidth DOCUMENT ME!
     * @param defaultHeight DOCUMENT ME!
     */
    private static void setDefaultSize(Component c, int defaultWidth,
        int defaultHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = (defaultWidth < screenSize.width) ? defaultWidth
                                                            : screenSize.width;
        final int height = (defaultHeight < screenSize.height) ? defaultHeight
                                                               : screenSize.height;
        c.setSize(width, height);
    }

    /**
     * DOCUMENT ME!
     */
    private void displayTransform() {
        Complex[] result;

        if (doInverse) {
            result = FourierMath.sort(FourierMath.inverseTransform(signal));
        } else {
            result = FourierMath.sort(FourierMath.transform(signal));
        }

        float[] realpart = new float[N];
        float[] imagpart = new float[N];

        for (int i = 0; i < N; i++) {
            realpart[i] = (float) result[i].real();
            imagpart[i] = (float) result[i].imag();
        }

        model.changeSeries(0, realpart);
        model.changeSeries(1, imagpart);
    }

    // A selection of test signals
    /**
     * Under transform should give something like exp(-x^2). Real
     * spectrum.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] gaussianSignal(int n) {
        double[] data = new double[n];
        double x;

        for (int i = 0; i < n; i++) {
            x = (i - (n / 2));
            data[i] = Math.exp(-x * x);
        }

        return data;
    }

    /**
     * Under transform should give something like cos(x)/x. Real
     * spectrum.
     *
     * @param amplitude DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] tophatSignal(double amplitude, int n) {
        double[] data = new double[n];
        int i = 0;

        for (; i < (n / 4); i++)
            data[i] = 0.0;

        for (; i < ((3 * n) / 4); i++)
            data[i] = amplitude;

        for (; i < n; i++)
            data[i] = 0.0;

        return data;
    }

    /**
     * Under transform should give a delta-function at origin. Real
     * spectrum.
     *
     * @param amplitude DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] constantSignal(double amplitude, int n) {
        double[] data = new double[n];

        for (int i = 0; i < n; i++)
            data[i] = amplitude;

        return data;
    }

    /**
     * Under transform should give something like isin(x)/x. Complex
     * spectrum.
     *
     * @param amplitude DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] squareWave(double amplitude, int n) {
        double[] data = new double[n];
        int i = 0;

        for (; i < (n / 2); i++)
            data[i] = -amplitude;

        for (; i < n; i++)
            data[i] = amplitude;

        return data;
    }

    /**
     * Under transform should give something like isin(x)/x^2. Complex
     * spectrum.
     *
     * @param amplitude DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] triangleWave(double amplitude, int n) {
        double[] data = new double[n];
        double gradient = (amplitude * 4.0) / n;
        int i = 0;

        for (; i < (n / 4); i++)
            data[i] = -gradient * i;

        for (; i < ((3 * n) / 4); i++)
            data[i] = (-2.0 * amplitude) + (gradient * i);

        for (; i < n; i++)
            data[i] = (4.0 * amplitude) - (gradient * i);

        return data;
    }

    /**
     * Under transform should give two delta-functions at +/-
     * frequency. Complex spectrum.
     *
     * @param amplitude DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] sineWave(double amplitude, int n) {
        double[] data = new double[n];
        double w = MathConstants.TWO_PI / n * 16.0;

        for (int i = 0; i < n; i++)
            data[i] = amplitude * Math.sin((i - (n / 2)) * w);

        return data;
    }
}
