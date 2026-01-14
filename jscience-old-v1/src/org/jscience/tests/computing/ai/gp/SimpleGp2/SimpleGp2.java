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

import org.jscience.computing.geneticprogramming.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SimpleGp2 implements FitnessFunction {
    /**
     * DOCUMENT ME!
     */
    private static final int MaxNumberOfGeneration = 50;

    /**
     * DOCUMENT ME!
     */
    private static final int PopulationSize = 600;

    /**
     * DOCUMENT ME!
     */
    private GeneticProgram geneticProgram = null;

    /**
     * DOCUMENT ME!
     */
    double[][] fitnessCases = {
            { 0.000, 0.000 },
            { 0.100, 0.005 },
            { 0.200, 0.020 },
            { 0.300, 0.045 },
            { 0.400, 0.080 },
            { 0.500, 0.125 },
            { 0.600, 0.180 },
            { 0.700, 0.245 },
            { 0.800, 0.320 },
            { 0.900, 0.405 }
        };

    /**
     * Creates a new SimpleGp2 object.
     *
     * @param populationSize DOCUMENT ME!
     * @param terminalSet DOCUMENT ME!
     * @param functionSet DOCUMENT ME!
     * @param generationMethod DOCUMENT ME!
     * @param selectionMethod DOCUMENT ME!
     */
    public SimpleGp2(int populationSize, Terminal[] terminalSet,
        Function[] functionSet, int generationMethod, int selectionMethod) {
        geneticProgram = new GeneticProgram(populationSize, this, terminalSet,
                functionSet, generationMethod, selectionMethod);
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        geneticProgram.createPopulation();

        while (!terminationPredicate()) {
            geneticProgram.evolve();
            System.out.println("Generation: " +
                geneticProgram.getGenerationNo());
            System.out.println("Best Individual: " +
                geneticProgram.getBestIndividual().getProgram().toText());
            System.out.println("Best Fitness: " +
                geneticProgram.getBestIndividual().getStandardizedFitness());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean terminationPredicate() {
        return (geneticProgram.getGenerationNo() >= MaxNumberOfGeneration);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ind DOCUMENT ME!
     * @param individualNo DOCUMENT ME!
     */
    public void evaluate(Individual ind, int individualNo) {
        double input;
        double output;
        double totError = 0;

        for (int i = 0; i < 10; i++) {
            input = fitnessCases[i][0];
            output = ((Double) ind.getProgram().eval(new Double(input))).doubleValue();
            totError = totError + Math.abs(fitnessCases[i][1] - output);
        }

        ind.setStandardizedFitness(totError);

        Individual bestIndividual = geneticProgram.getBestIndividual();

        if ((bestIndividual == null) ||
                (bestIndividual.getStandardizedFitness() > totError)) {
            geneticProgram.setBestIndividual(individualNo);
            geneticProgram.setGenerationOfBestIndividual(geneticProgram.getGenerationNo());
        }

        Individual bestIndividualInCurrentGeneration = geneticProgram.getBestIndividualInCurrentGeneration();

        if ((bestIndividualInCurrentGeneration == null) ||
                (bestIndividualInCurrentGeneration.getStandardizedFitness() > totError)) {
            geneticProgram.setBestIndividualInCurrentGeneration(individualNo);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Terminal[] terminalSet = new Terminal[2];
        Function[] functionSet = new Function[4];

        terminalSet[0] = new XTerminal();
        terminalSet[1] = new IntTerminal();

        functionSet[0] = new AddFunction();
        functionSet[1] = new SubFunction();
        functionSet[2] = new MulFunction();
        functionSet[3] = new DivFunction();

        SimpleGp2 simpleGp = new SimpleGp2(PopulationSize, terminalSet,
                functionSet, GeneticProgram.GENERATION_FULL,
                GeneticProgram.SELECTION_FITNESS_PROPORTIONATE);

        simpleGp.start();
    }
}
