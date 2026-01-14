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

package org.jscience.computing.kmeans;

/**
 * This class implements the Gaussian activation function.
 *
 * @author Fred Corbett
 * @version April 15, 1997
 */
public class Gaussian {
    /*******************/
    /* Class Constants */
    /**
     * *************** Default value of mu.
     * Default value of mu.
     * Default value of mu.
     * Default value of mu.
     * Default value of mu.
     */

    /** Default value of mu. */
    public final static float DEF_MU = 0.0f;

    /**
     * Minimum value for sigma.
     */
    public final static float MIN_SIGMA = 0.0f;

    /**
     * Default value of sigma.
     */
    public final static float DEF_SIGMA = 0.159155f;

    /**********************/
    /* Instance Variables */
    /**
     * ****************** Gaussian mean parameter.
     * Gaussian mean parameter.
     * Gaussian mean parameter.
     * Gaussian mean parameter.
     * Gaussian mean parameter.
     */

    /** Gaussian mean parameter. */
    private float mu;

    /**
     * Gaussian standard deviation parameter.
     */
    private float sigma;

    /**
     * Gaussian constant term. Used to speed calculations.
     */
    private float k1;

    /**
     * Gaussian constant term. Used to speed calculations.
     */
    private float k2;

    /***********************/
    /* Constructor Methods */
    /**
     * *******************
     * Default constructor.
     * <p/>
     * Default constructor.
     * <p/>
     * Default constructor.
     * <p/>
     * Default constructor.
     * <p/>
     * Default constructor.
     */

    /**
     * Default constructor.
     */
    public Gaussian() {
        initDefaults();
    }

    /*****************************/
    /* Acccessor/Mutator Methods */
    /**
     * *************************
     * Accessor for Gaussian mu (mean) parameter.
     *
     * @return float - the value of mu.
     */

    /**
     * Accessor for Gaussian mu (mean) parameter.
     *
     * @return float - the value of mu.
     */
    public float getMu() {
        return mu;
    }

    /**
     * Mutator for Gaussian mu parameter.
     *
     * @param m m - the new value for mu.
     */
    public void setMu(float m) {
        mu = m;
    }

    /**
     * Accessor for Gaussian sigma (std. dev.) parameter.
     *
     * @return float - the value of sigma.
     */
    public float getSigma() {
        return sigma;
    }

    /**
     * Mutator for Gaussian sigma parameter.
     *
     * @param s s - the new value for sigma.
     */
    public void setSigma(float s) {
        if (s > MIN_SIGMA) {
            k1 = (float) (1 / Math.sqrt(2 * Math.PI * s));
            k2 = -1 / (2 * s * s);
            sigma = s;
        } else {
            System.err.println("Error in anns.Gaussain.setSigma()");
            System.err.println("\tthe value of sigma is to small: " + s);
            System.err.println("\tit must be greater than " + MIN_SIGMA + "\n");
        }
    }

    /******************/
    /* Public Methods */
    /**
     * **************
     * Initialize the function parameters to their default values.
     * <p/>
     * Initialize the function parameters to their default values.
     * <p/>
     * Initialize the function parameters to their default values.
     * <p/>
     * Initialize the function parameters to their default values.
     * <p/>
     * Initialize the function parameters to their default values.
     */

    /**
     * Initialize the function parameters to their default values.
     */
    public void initDefaults() {
        setMu(DEF_MU);
        setSigma(DEF_SIGMA);
    }

    /**
     * Calculate the value of a Gaussian function for a given input. The
     * Gaussian or normal probability function is given by:
     * <PRE>
     * 1                -(x-mu)^2 / (2 * sigma^2)
     * y  =  ------------------- *  e
     * (2 * pi * sigma)^0.5
     * <p/>
     * </PRE>
     * The variable k1 holds the value of the first term, and k2 holds the
     * value of the 1 / (2  simga^2) term for the current value of sigma.
     *
     * @param x - the input value.
     * @return float - the corresponding value of y.
     */
    public float getOutput(float x) {
        return (float) (k1 * Math.exp((x - mu) * (x - mu) * k2));
    }

    /**
     * Calculate the value of the derivative of a Gaussian function for a given
     * input. The derivative of the Gaussian function is given by:
     * <PRE>
     * <p/>
     * y'(x)  =  y(x) * -2k2(x - mu)
     * <p/>
     * <p/>
     * </PRE>
     * The variable k1 holds the value of the first term, and k2 holds the
     * value of the 1 / (2  simga^2) term for the current value of sigma.
     *
     * @param x - the input value.
     * @return float - the corresponding value of y prime.
     */
    public float getOutputPrime(float x) {
        return getOutput(x) * -2 * k2 * (x - mu);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Gaussian x = new Gaussian();
        float sigma = 3.6612446302486625f;
        x.setSigma(sigma);

        float mu = -0.6450563873333334f;
        x.setMu(mu);

        float v = 4.77821f;
        System.out.println(x.getOutput(v));
    }
} // End of class Gaussian
