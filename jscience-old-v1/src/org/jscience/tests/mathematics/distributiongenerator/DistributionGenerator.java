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

import org.jscience.awt.AbstractGraphModel;
import org.jscience.awt.Graph2DModel;
import org.jscience.awt.ScatterGraph;

import org.jscience.mathematics.statistics.CauchyDistribution;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * DistributionGenerator generates random numbers from a probability
 * distribution.
 *
 * @author Mark Hale
 * @version 1.1
 */
public final class DistributionGenerator extends Frame implements Runnable {
    /**
     * DOCUMENT ME!
     */
    private final double mean;

    /**
     * DOCUMENT ME!
     */
    private final double width;

    /**
     * DOCUMENT ME!
     */
    private final ProbabilityDistribution dist;

    /**
     * DOCUMENT ME!
     */
    private final HistogramModel model;

/**
     * Constructs a distribution generator.
     *
     * @param pd a probability distribution
     * @param m  the mean of the sampling region
     * @param w  the width of the sampling region
     */
    public DistributionGenerator(ProbabilityDistribution pd, double m, double w) {
        super("Distribution Generator");
        dist = pd;
        mean = m;
        width = w;
        model = new HistogramModel();

        ScatterGraph graph = new ScatterGraph(model);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });
        add(graph);
        setSize(300, 300);
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        // set distribution here
        DistributionGenerator app = new DistributionGenerator(new CauchyDistribution(),
                0.0, 10.0);

        //                DistributionGenerator app=new DistributionGenerator(new PoissonDistribution(50.0), 50.0, 100.0);
        Thread thr = new Thread(app);
        thr.setPriority(Thread.MIN_PRIORITY);
        thr.start();
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        for (int i = 0; i < 10000; i++)
            model.add(random());
    }

    /**
     * Returns a random number from the distribution. Uses the
     * rejection method.
     *
     * @return DOCUMENT ME!
     */
    public double random() {
        /*
        * We use the uniform function f(x)=1 to be totally general.
        * For greater efficiency use a function f(x) which roughly
        * over-approximates the required distribution.
        */
        double x;

        /*
        * We use the uniform function f(x)=1 to be totally general.
        * For greater efficiency use a function f(x) which roughly
        * over-approximates the required distribution.
        */
        double y;

        do {
            /*
            * random x coordinate:
            * x is such that int(f(s),min,x,ds) = Math.random()*int(f(s),min,max,ds)
            */

            // uniform comparison function
            x = mean + (width * (Math.random() - 0.5));
            // Lorentzian comparison function
            // x=mean+Math.tan(Math.PI*Math.random());
            // uncomment line below if using a discrete distribution
            // x=Math.floor(x);
            /*
            * random y coordinate:
            * y = Math.random()*f(x)
            */

            // uniform comparison function
            y = Math.random();

            // Lorentzian comparison function
            // y=Math.random()/(1.0+(x-mean)*(x-mean));
        } while (y > dist.probability(x));

        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class HistogramModel extends AbstractGraphModel
        implements Graph2DModel {
        /**
         * DOCUMENT ME!
         */
        private final int[] histogram = new int[101];

        /**
         * DOCUMENT ME!
         */
        private final double binwidth = width / (histogram.length - 1);

        /**
         * Creates a new HistogramModel object.
         */
        public HistogramModel() {
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getXCoord(int i) {
            return (float) ((binwidth * (i - ((histogram.length - 1) / 2.0))) +
            mean);
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getYCoord(int i) {
            return histogram[i];
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int seriesLength() {
            return histogram.length;
        }

        /**
         * DOCUMENT ME!
         */
        public void firstSeries() {
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean nextSeries() {
            return false;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         */
        public void add(double x) {
            int bin = (int) ((x - mean) / binwidth) +
                ((histogram.length - 1) / 2);

            if ((bin >= 0) && (bin < histogram.length)) {
                histogram[bin]++;
                fireGraphSeriesUpdated(0);
            }
        }
    }
}
