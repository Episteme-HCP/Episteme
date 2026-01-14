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

package org.jscience.computing.ai.evolutionary.geneticprogramming;

/**
 * <p/>
 * This class combines a program and its computed fitness values.
 * <p/>
 * <p/>
 * <b>Raw fitness</b> is the measurement of fitness that is stated in the natural
 * terminology of the problem itself. The most common definition of raw
 * fitness is error: If the S-expression is boolean-valued or
 * symbolic-valued, the sum of distances is equivalent to the number of
 * mismatches. If the S-expression is real-valued or integer-valued,
 * the square root of the sum of the squares of the distances can,
 * alternatively, be used to measure the fitness. (therefore increasing
 * the influence of more distant points)
 * <p/>
 * <p/>
 * <b>Standardized fitness</b> restates the raw fitness so that a lower numerical
 * value is always a better value.
 * <p/>
 * <p/>
 * It is convenient and desirable to make the best value of standardized
 * fitness equal 0. If this is not already the case, it can be made so by
 * subtracting (or adding a constant).
 * <p/>
 * <p/>
 * If for a particular problem, a greater value of raw fitness is better,
 * standardized fitness must be computed from raw fitness. In that situation,
 * standardized fitness equals the maximum possible value of raw fitness(Rmax)
 * minus the observed raw fitness. For ex. If the artifical ant finds 5 0f 89
 * pieces of food using a given computer program, the raw fitness is 5 and
 * the standardized fitness is 84.
 * <p/>
 * <p/>
 * <b>Adjusted fitness</b> a(i,t) is computed from the standardized fitness s(i,t)
 * as follows: --- a(i,t) = 1 / 1+s(i,t) --- where s(i,t) is the standardized
 * fitness for individual i at time t.
 * <p/>
 * <p/>
 * The adjusted fitness lies between 0 and 1. The adjusted fitness is bigger
 * for better individuals in the population. Note that for certain methods
 * of selection other than fitness proportionate selection (e.g. tournament
 * selection and rank selection), adjusted fitness is not relevant and not
 * used.
 * <p/>
 * <p/>
 * <b>Normalized fitness</b> is also needed if the method of selection employed is
 * fitness proportionate.
 * <p/>
 * <p/>
 * The normalized fitness n(i,t) is computed from the adjusted fitness a(i,t)
 * as described in <b>Koza's book (ISBN:0262111705)</b>
 * <p/>
 * <p/>
 * Note that for certain methods of selection other than fitness proportionate
 * selection (e.g. tournament selection and rank selection), normalized fitness
 * is not relevant and not used.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class Individual {
    private Program program;
    private double standardizedFitness;
    private double adjustedFitness;
    private double normalizedFitness;
    int mHits;

    /**
     * Create an individual with given parameters.
     *
     * @param program program of this individual
     */
    public Individual(Program program) {
        this.program = (Program) program.clone();
        standardizedFitness = 0.0;
        adjustedFitness = 0.0;
        normalizedFitness = 0.0;
        mHits = 0;
    }

    /**
     * Create a deep copy of this individual
     *
     * @return deep copy of the individual
     */
    public Individual copy() {
        Individual newInd = new Individual(program);
        newInd.standardizedFitness = standardizedFitness;
        newInd.adjustedFitness = adjustedFitness;
        newInd.normalizedFitness = normalizedFitness;
        newInd.mHits = mHits;

        return newInd;
    }

    /**
     * Returns program of this individual
     *
     * @return program of this individual
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Changes the program of this individual
     *
     * @param program new program of this individual
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * Returns normalized fitness of this individual
     *
     * @return normalized fitness of this individual
     */
    public double getNormalizedFitness() {
        return normalizedFitness;
    }

    /**
     * Returns adjusted fitness of this individual
     *
     * @return adjusted fitness of this individual
     */
    public double getAdjustedFitness() {
        return adjustedFitness;
    }

    /**
     * Returns standardized fitness of this individual
     *
     * @return standardized fitness of this individual
     */
    public double getStandardizedFitness() {
        return standardizedFitness;
    }

    /**
     * Changes normalized fitness of this individual
     *
     * @param newFitness new fitness value
     */
    public void setNormalizedFitness(double newFitness) {
        normalizedFitness = newFitness;
    }

    /**
     * Changes adjusted fitness of this individual
     *
     * @param newFitness new fitness value
     */
    public void setAdjustedFitness(double newFitness) {
        adjustedFitness = newFitness;
    }

    /**
     * Changes standardized fitness of this individual
     *
     * @param newFitness new fitness value
     */
    public void setStandardizedFitness(double newFitness) {
        standardizedFitness = newFitness;
    }

    /**
     * Changes hits of this individual
     *
     * @param newHits new hits
     */
    public void setHits(int newHits) {
        mHits = newHits;
    }
}
