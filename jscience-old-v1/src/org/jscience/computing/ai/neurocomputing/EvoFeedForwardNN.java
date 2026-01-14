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

package org.jscience.computing.ai.neurocomputing;

import org.jscience.computing.ai.evolutionary.Evolvable;


/**
 * This class implements an evolutionary feedforward network.
 *
 * @author James Matthews
 */
public class EvoFeedForwardNN extends FeedForwardNN implements Evolvable {
    /** The fitness value for this feedforward network. */
    protected double fitnessValue = 0.0;

    /** The input test data. */
    protected double[][] inputData;

/**
     * Creates a new instance of EvoFeedForwardNN
     *
     * @param hidden the number of hidden layers.
     */
    public EvoFeedForwardNN(int hidden) {
        super(hidden);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * Calculate the fitness for this network. This is defined as the
     * difference between the actual and desired output of the network.
     */
    public void calculateFitness() {
        double err = 0.0;

        for (int i = 0; i < inputData.length; i++) {
            //            err += Math.abs(run(inputData[i]));
        }
    }

    /**
     * Compare to another feedforward network.
     *
     * @param o the comparable object.
     *
     * @return -1, 0 or 1.
     */
    public int compareTo(Object o) {
        EvoFeedForwardNN object = (EvoFeedForwardNN) o;

        if (fitnessValue < object.fitnessValue) {
            return -1;
        }

        if (fitnessValue > object.fitnessValue) {
            return 1;
        }

        return 0;
    }

    /**
     * Return the fitness value for this network.
     *
     * @return the fitness value.
     */
    public double getFitness() {
        return fitnessValue;
    }

    /**
     * Mate with another network.
     *
     * @param partner the other parent network.
     *
     * @return the new child network.
     */
    public Evolvable mate(Evolvable partner) {
        return null;
    }

    /**
     * Mutate this feedforward network.
     */
    public void mutate() {
    }

    /**
     * Randomly initialize the network. The weights within the
     * feedforward are randomly initialized.
     */
    public void randomInitialize() {
    }
}
