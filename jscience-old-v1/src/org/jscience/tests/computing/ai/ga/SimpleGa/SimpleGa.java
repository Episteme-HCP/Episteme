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

import org.jscience.computing.ai.ea.ga.BinaryCodedGA;
import org.jscience.computing.ai.ea.ga.InvalidFitnessValueException;
import org.jscience.computing.ai.util.Converter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SimpleGa extends BinaryCodedGA {
    /**
     * DOCUMENT ME!
     */
    private static double constDouble = 3.0 / 4194303.0;

    /**
     * Creates a new SimpleGa object.
     *
     * @param popSize DOCUMENT ME!
     * @param chrLength DOCUMENT ME!
     * @param crossoverProb DOCUMENT ME!
     * @param mutationProb DOCUMENT ME!
     * @param maxHist DOCUMENT ME!
     */
    public SimpleGa(int popSize, int chrLength, double crossoverProb,
        double mutationProb, int maxHist) {
        super(popSize, chrLength, crossoverProb, mutationProb, maxHist);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        SimpleGa ga = new SimpleGa(150, 22, 0.25, 0.01, 300);

        try {
            ga.initializePopulation();
        } catch (InvalidFitnessValueException e) {
            System.out.println(e);
        }

        System.out.println("Population initialized");

        for (int i = 0; i < 50; i++) {
            System.out.println("Generation-" + i + " - Best. Fitness: " +
                ga.getMaxFitness());
            ga.evolve();
        }

        //System.out.println("History");
        double[] history = ga.getAvgHistory();

        //System.out.println(ga.generationNo);
        for (int i = 0; i < ga.generationNo; i++) {
            //System.out.println("Avg. History-"+i+": "+history[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void evolve() {
        for (int i = 0; i < chromosomeLength; i++) {
            nextPopulation[0][i] = currentPopulation[bestChromosomeNo][i];
            nextPopulation[1][i] = mutateChromosome(currentPopulation[bestChromosomeNo][i]);
        }

        for (int j = 2; j < populationSize; j += 2) {
            int k = selectChromosome();
            int i1 = selectChromosome();

            for (int k1 = 0; k1 < chromosomeLength; k1++) {
                nextPopulation[j][k1] = mutateChromosome(currentPopulation[k][k1]);
                nextPopulation[j + 1][k1] = mutateChromosome(currentPopulation[i1][k1]);
                crossoverChromosomes(nextPopulation[j], nextPopulation[j + 1]);
            }
        }

        for (int l = 0; l < populationSize; l++) {
            for (int j1 = 0; j1 < chromosomeLength; j1++)
                currentPopulation[l][j1] = nextPopulation[l][j1];

            currentPopulationFitness[l] = evaluateIndividual(l,
                    currentPopulation[l]);

            if (l == 0) {
                maxFitness = currentPopulationFitness[l];
                bestChromosomeNo = l;
                minFitness = currentPopulationFitness[l];
                tmpSumOfFitnesses = 0.0;
                tmpSumx = 0.0;
            }

            if (currentPopulationFitness[l] > maxFitness) {
                maxFitness = currentPopulationFitness[l];
                bestChromosomeNo = l;
            }

            if (currentPopulationFitness[l] < minFitness) {
                minFitness = currentPopulationFitness[l];
            }

            tmpSumOfFitnesses += currentPopulationFitness[l];
            tmpSumx += currentPopulationFitness[l];
        }

        if (historyLength > generationNo) {
            historyAvgFitness[generationNo] = tmpSumx / (double) populationSize;
            historyMaxFitness[generationNo] = maxFitness;
            historyMinFitness[generationNo] = minFitness;
        }

        generationNo++;
    }

    /**
     * DOCUMENT ME!
     *
     * @param chromosomeno DOCUMENT ME!
     * @param chromosome DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double evaluateIndividual(int chromosomeno, boolean[] chromosome) {
        int intValue = Converter.booleanArrayToInt(chromosome);
        double xValue = -1.0 + ((double) intValue * constDouble);

        return (getFunctionValue(xValue));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getFunctionValue(double x) {
        return (x * Math.sin(Math.toRadians(1800 * x))) + 1.0;
    }
}
