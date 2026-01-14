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

package org.jscience.physics.nuclear.kinematics.math.analysis;

import java.io.File;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class GaussianFilter {
    /** magic number for calculating */
    static private final double a = 0.93911;

    /** magic number for calculating */
    static private final double b = 2.77066;

    /** magic number for calculating */
    static final double SIGMA_TO_FWHM = 2.354;

    /**
     * DOCUMENT ME!
     */
    private double[] inSpectrum;

    /**
     * DOCUMENT ME!
     */
    private double[] outSpectrum;

    /**
     * DOCUMENT ME!
     */
    private double width;

    /**
     * Creates a new GaussianFilter object.
     *
     * @param inSpectrum DOCUMENT ME!
     * @param width DOCUMENT ME!
     */
    public GaussianFilter(double[] inSpectrum, double width) {
        this.inSpectrum = inSpectrum;
        this.width = width;
        processSpectrum();
    }

    /**
     * DOCUMENT ME!
     */
    private void processSpectrum() {
        int length = inSpectrum.length;
        outSpectrum = new double[length];

        for (int newChannel = 0; newChannel < length; newChannel++) {
            double newCounts = 0.0;
            double scaleFactor = (a * inSpectrum[newChannel]) / width;
            int minCh = (int) Math.round(Math.max(0, newChannel - (3 * width)));
            int maxCh = (int) Math.round(Math.min(length - 1,
                        newChannel + (3 * width)));

            for (int oldChannel = minCh; oldChannel <= maxCh; oldChannel++) {
                newCounts += Math.exp(-b * Math.pow(
                        (oldChannel - newChannel) / width, 2));
            }

            newCounts *= scaleFactor;
            outSpectrum[newChannel] = newCounts;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getFilteredSpectrum() {
        return outSpectrum;
    }

    /**
     * arg1 = width, arg2= infile.dat, arg3=outfile.dat
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        double width = Double.parseDouble(args[0]);
        File infile = new File(args[1]);
        File outfile = new File(args[2]);
        DatFile inDat = new DatFile(infile);
        GaussianFilter gf = new GaussianFilter(inDat.getData(), width);
        new DatFile(outfile, gf.getFilteredSpectrum());
    }
}
