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

/*
 * DataSet.java
 *
 * Created on March 9, 2001, 11:34 AM
 */
package org.jscience.physics.nuclear.kinematics.montecarlo;

/**
 * Contains a set of numbers, can return set size, mean, and standard
 * deviation.
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class DataSet extends Object implements WeightingFunction {
    //java.util.Vector data;
    /**
     * DOCUMENT ME!
     */
    private double[] data = new double[100];

    /**
     * DOCUMENT ME!
     */
    private int size = 0;

    /**
     * DOCUMENT ME!
     */
    private double mean; //mean and standard deviation

    /**
     * DOCUMENT ME!
     */
    private double sd; //mean and standard deviation

    /**
     * DOCUMENT ME!
     */
    private WeightingFunction weight;

    /**
     * DOCUMENT ME!
     */
    private boolean needToCalculateStats = true;

/**
     * Creates new DataSet
     */
    public DataSet(WeightingFunction wf) {
        //data = new java.util.Vector();
        weight = wf;
    }

/**
     * Use standard non-biased weight.
     */
    public DataSet() {
        //data = new java.util.Vector();
        weight = this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void add(double x) {
        data[size] = x;
        size++;

        if (size == data.length) {
            double[] temp = new double[2 * size];
            System.arraycopy(data, 0, temp, 0, size);
            data = temp;
            System.gc();
        }

        needToCalculateStats = true;
    }

    /**
     * DOCUMENT ME!
     */
    private void calculateStats() {
        double s = 0.0;
        double ss = 0.0;
        double sw = 0.0;

        for (int i = 0; i < size; i++) {
            double w = weight(data[i]);
            double term = w * data[i];
            s += term;
            ss += (term * term);
            sw += w;
        }

        mean = s / sw;
        sd = Math.sqrt((ss - ((s * s) / sw)) / (sw - 1));
        needToCalculateStats = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getData() {
        double[] rval = new double[size];
        System.arraycopy(data, 0, rval, 0, size);

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMean() {
        if (needToCalculateStats) {
            calculateStats();
        }

        return mean;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSD() {
        if (needToCalculateStats) {
            calculateStats();
        }

        return sd;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize() {
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSEM() {
        return getSD() / Math.sqrt(size);
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param step DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getHistogram(double min, double max, double step) {
        double realMin = min - (step * 0.5);
        int[] rval = new int[(int) Math.round((max - min) / step)];

        //double [] ndata = getData();
        for (int i = 0; i < size; i++) {
            int bin = (int) Math.floor((data[i] - realMin) / step);

            if ((bin >= 0) && (bin < rval.length)) {
                rval[bin]++;
            }
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    static public void main(String[] args) {
        DataSet ds = new DataSet();
        ds.add(3);
        ds.add(4);
        ds.add(5);
        ds.add(6);
        System.out.println("Size = " + ds.getSize() + ", Mean = " +
            ds.getMean() + ", SD = " + ds.getSD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double weight(double value) {
        return 1.0;
    }
}
