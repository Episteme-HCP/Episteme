package org.jscience.computing.ai.evolutionary.geneticprogramming;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;


/**
 * Genetic program implementation.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class GeneticProgram {
    /** "Full" generation method */
    public static final int GENERATION_FULL = 0;

    /** "Grow" generation method */
    public static final int GENERATION_GROW = 1;

    /** "Ramped Half and Half" generation method */
    public static final int GENERATION_RAMPED_HALF_AND_HALF = 2;

    /** "Tournament" selection method */
    public static final int SELECTION_TOURNAMENT = 0;

    /** "Fitness-Proportionate" seleciton method */
    public static final int SELECTION_FITNESS_PROPORTIONATE = 1;

    /** Fitness function used in this genetic program */
    private FitnessFunction fitnessFunction = null;

    /** Random number generator used in this genetic program */
    private Random randomNumberGenerator = new Random();

    /** Population of individuals in the current generation */
    private Individual[] population;

    /** Best individual over all generations */
    private Individual bestIndividual = null;

    /** Best individual in the current generation */
    private Individual bestIndividualInCurrentGeneration = null;

    /** The generation number of the best individual found so far */
    private int generationOfBestIndividual;

    /** Size of the population */
    private int populationSize;

    /** Maximum depth for the new individuals */
    private int maxDepthForNewIndividual = 6;

    /** Maximum depth for the new individuals created by crossover operation */
    private int maxDepthForIndividualAfterCrossover = 20;

    /** Maximum depth for the new subtree created by mutation operation */
    private int maxDepthForNewSubtreeInMutant = 4;

    /** Probability of crossover operation */
    private double crossoverFraction = 0.8;

    /** Probability of mutation operation */
    private double mutationFraction = 0.2;

    /** Probability of reproduction operation */
    private double reproductionFraction = 0;

    /** Generation method used in this genetic program */
    private int generationMethod = GENERATION_FULL;

    /** Selection method used in this genetic program */
    private int selectionMethod = SELECTION_TOURNAMENT;

    /** Terminal set of this genetic program */
    private Terminal[] terminalSet;

    /** Function set of this genetic program */
    private Function[] functionSet;

    /** Current generation number */
    private int generationNo = 0;

/**
     * Creates a genetic program with given parameters.
     *
     * @param populationSize   size of the population
     * @param fitnessFunction  fitness function that will be used in this
     *                         genetic program
     * @param terminalSet      terminal set of this genetic program
     * @param functionSet      function set of this genetic program
     * @param generationMethod generation method used in this genetic program
     * @param selectionMethod  selection method used in this genetic program
     */
    public GeneticProgram(int populationSize, FitnessFunction fitnessFunction,
        Terminal[] terminalSet, Function[] functionSet, int generationMethod,
        int selectionMethod) {
        this.populationSize = populationSize;
        this.fitnessFunction = fitnessFunction;
        this.terminalSet = terminalSet;
        this.functionSet = functionSet;
        this.generationMethod = generationMethod;
        this.selectionMethod = selectionMethod;
    }

/**
     * Creates a genetic program with given parameters.
     *
     * @param populationSize  size of the population
     * @param fitnessFunction fitness function that will be used in this
     *                        genetic program
     * @param terminalSet     terminal set of this genetic program
     * @param functionSet     function set of this genetic program
     */
    public GeneticProgram(int populationSize, FitnessFunction fitnessFunction,
        Terminal[] terminalSet, Function[] functionSet) {
        this.populationSize = populationSize;
        this.fitnessFunction = fitnessFunction;
        this.terminalSet = terminalSet;
        this.functionSet = functionSet;
    }

    /**
     * Selects a terminal from terminal set randomly
     *
     * @return selected terminal
     */
    public Terminal selectTerminal() {
        Terminal terminal;

        try {
            Class cls = terminalSet[randomNumberGenerator.nextInt(terminalSet.length)].getClass();
            terminal = (Terminal) cls.newInstance();
        } catch (Exception e) {
            terminal = null;
            System.out.println(e);
        }

        return terminal;
    }

    /**
     * Selects a function from terminal set randomly
     *
     * @return selected function
     */
    public Function selectFunction() {
        Function function;

        try {
            Class cls = functionSet[randomNumberGenerator.nextInt(functionSet.length)].getClass();
            function = (Function) cls.newInstance();
        } catch (Exception e1) {
            function = null;
            System.out.println(e1);
        }

        return function;
    }

    /**
     * Creates arguments for the given function
     *
     * @param function function to create arguments
     * @param allowableDepth remaining depth of the tree we can create when we
     *        hit zero we will only select terminals.
     * @param fullP indicates whether this individual is to be a full tree
     */
    public void createArgumentsForFunction(Function function,
        int allowableDepth, boolean fullP) {
        for (int i = 0; i < function.arg.length; i++) {
            function.arg[i] = createIndividualProgram(allowableDepth, false,
                    fullP);
        }
    }

    /**
     * Creates an individual randomly with given pramaters
     *
     * @param allowableDepth remaining depth of the tree we can create when we
     *        hit zero we will only select terminals.
     * @param topNodeP is ture only when we are being called as the top node in
     *        the tree. This allows us to make sure that we always put a
     *        function at the top of the tree.
     * @param fullP indicates whether this individual is to be a full tree
     *
     * @return created individual
     */
    public Program createIndividualProgram(int allowableDepth,
        boolean topNodeP, boolean fullP) {
        Program p;
        int choice;
        Function function;

        if (allowableDepth <= 0) {
            // We've reached maxdepth, so just pick a terminal
            p = selectTerminal();
        } else {
            if (fullP || topNodeP) {
                // We are the top node or are a full tree, so pick only a function
                function = selectFunction();
                createArgumentsForFunction(function, allowableDepth - 1, fullP);
                p = function;
            } else {
                // Choose one from the bag of functions  and temrinals.
                choice = randomNumberGenerator.nextInt(terminalSet.length +
                        functionSet.length);

                if (choice < functionSet.length) {
                    // We chose a function, so pick it out
                    function = selectFunction();
                    createArgumentsForFunction(function, allowableDepth - 1,
                        fullP);
                    p = function;
                } else {
                    // We chose a terminal, so pick it out
                    p = selectTerminal();
                }
            }
        }

        return p;
    }

    /**
     * Creates the initial population
     */
    public void createPopulation() {
        int allowableDepth;
        boolean fullP;
        boolean fullCycleP = false;
        Hashtable generation0Individuals = new Hashtable();

        population = new Individual[populationSize];

        int minDepthOfTrees = 1;

        int attemptsAtThisIndividual = 0;
        int individualIndex = 0;

        while (individualIndex < populationSize) {
            switch (generationMethod) {
            case GeneticProgram.GENERATION_FULL:
                allowableDepth = maxDepthForNewIndividual;
                fullP = true;

                break;

            case GeneticProgram.GENERATION_GROW:
                allowableDepth = maxDepthForNewIndividual;
                fullP = false;

                break;

            case GeneticProgram.GENERATION_RAMPED_HALF_AND_HALF:
                /*
                * With each new individual, the allowed depth is ramped up until
                * it reaches the maximum. Then it is reset to the minimum depth again.
                *
                * At this time, we toggle between full trees and grown trees, so that
                * half of the trees are full and the other half is grown.
                *
                * Note that the switch occurs only at the first attempt to create a
                * tree. This is to avoid unnecessary toggling when several attempts
                * are necessary for the same individual.
                *
                */
                allowableDepth = minDepthOfTrees +
                    (individualIndex % (maxDepthForNewIndividual -
                    minDepthOfTrees + 1));

                if ((attemptsAtThisIndividual == 0) &&
                        ((individualIndex % (maxDepthForNewIndividual -
                        minDepthOfTrees + 1)) == 0)) {
                    fullCycleP = !fullCycleP;
                }

                fullP = fullCycleP;

                break;

            default:
                allowableDepth = maxDepthForNewIndividual;
                fullP = false;

                break;
            } // End of Switch

            Program newProgram = createIndividualProgram(allowableDepth, true,
                    fullP);

            // Check if we have already created this program.
            // If not then store it and move on.
            // If we have then try again.
            // The easiest way to compare two programs is to look at their
            // printed representation.
            String hashKey = newProgram.toString();

            if (!generation0Individuals.containsKey(hashKey)) {
                population[individualIndex] = new Individual(newProgram);
                individualIndex++;
                generation0Individuals.put(hashKey, newProgram);
                attemptsAtThisIndividual = 0;
            } else {
                attemptsAtThisIndividual++;

                if (attemptsAtThisIndividual > 20) {
                    // This depth have probably filled up, so bump the depth counter
                    // and keep the max depth in line with the new minimum
                    minDepthOfTrees++;
                    maxDepthForNewIndividual = Math.max(maxDepthForNewIndividual,
                            minDepthOfTrees);
                    attemptsAtThisIndividual = 0;
                }
            }
        } // End of While
    }

    /**
     * Breeds the new population
     */
    private void breedNewPopulation() {
        Program[] newPrograms;

        double fraction;
        int index;
        Individual individual1;
        Individual individual2;
        int i;
        double sumOfFractions = crossoverFraction + reproductionFraction +
            mutationFraction;
        double tmpCrossoverFraction = crossoverFraction / sumOfFractions;
        double tmpReproductionFraction = reproductionFraction / sumOfFractions;

        newPrograms = new Program[populationSize];
        fraction = 0.0;
        index = 0;

        newPrograms[index] = (Program) bestIndividual.getProgram().clone();

        while (index < populationSize) {
            fraction = (double) index / (double) populationSize;
            individual1 = findIndividual();

            if (fraction < tmpCrossoverFraction) {
                // Crossover
                individual2 = findIndividual();

                Program[] offspring = crossover(individual1.getProgram(),
                        individual2.getProgram());
                newPrograms[index] = offspring[0];
                index++;

                if (index < populationSize) {
                    newPrograms[index] = offspring[1];
                    index++;
                }
            } else {
                if (fraction < (tmpReproductionFraction + tmpCrossoverFraction)) {
                    // Reproduction
                    newPrograms[index] = (Program) individual1.getProgram()
                                                              .clone();
                    index++;
                } else {
                    // Mutation
                    newPrograms[index] = mutate(individual1.getProgram());
                    index++;
                }
            }
        } // End of While

        for (index = 0; index < populationSize; index++) {
            population[index].setProgram(newPrograms[index]);
        }
    }

    /**
     * Mutates a given program
     *
     * @param program program to mutate
     *
     * @return mutated program
     */
    public Program mutate(Program program) {
        // Pick the mutation point
        int mutationPoint = randomNumberGenerator.nextInt(program.countNodes());

        // Create a brand new subtree
        Program newSubtree = createIndividualProgram(maxDepthForNewSubtreeInMutant,
                true, false);
        Program newProgram = (Program) program.clone();

        // Smash in the new subtree
        TreeHook hook = getSubTree(program, mutationPoint, false);

        if (hook.getParent() == null) {
            newProgram = hook.getSubTree();
        } else {
            hook.getParent().arg[hook.getChildIndex()] = newSubtree;
        }

        return newProgram;
    }

    /**
     * Produces two new individuals by crossing over two parents.
     *
     * @param male first parent program
     * @param female second parent program
     *
     * @return offsprings
     */
    public Program[] crossover(Program male, Program female) {
        /*
         * First a crossover point is selecte randomly in each parent.
         * Then two parents are cut at their crossover points, giving
         * two tree fragments and two cut-off subtrees. The two subtrees
         * are swaped and spliced with the fragments again.
         *
         * We have to make sure that the newly formed trees do not exceed a
         * certain depth. If they do, one of the parents is used as offspring.
         */
        double crossoverAtFunctionFraction = 0.9;
        boolean crossoverAtFunction;

        // Make copies of the parents first, because they will be
        // destructively modified.
        Program[] offspring = new Program[2];
        offspring[0] = (Program) male.clone();
        offspring[1] = (Program) female.clone();

        int malePoint;
        int femalePoint;
        TreeHook maleHook;
        TreeHook femaleHook;

        crossoverAtFunction = (randomNumberGenerator.nextDouble() < crossoverAtFunctionFraction);

        if (crossoverAtFunction) {
            malePoint = randomNumberGenerator.nextInt(male.countFunctionNodes());
            maleHook = getSubTree(offspring[0], malePoint, true);
        } else {
            malePoint = randomNumberGenerator.nextInt(male.countNodes());
            maleHook = getSubTree(offspring[0], malePoint, false);
        }

        crossoverAtFunction = (randomNumberGenerator.nextDouble() < crossoverAtFunctionFraction);

        if (crossoverAtFunction) {
            femalePoint = randomNumberGenerator.nextInt(female.countFunctionNodes());
            femaleHook = getSubTree(offspring[1], femalePoint, true);
        } else {
            femalePoint = randomNumberGenerator.nextInt(female.countNodes());
            femaleHook = getSubTree(offspring[1], femalePoint, false);
        }

        // Modify the new individuals by smashing in the (copied) subtree
        // from the old individual.
        if (maleHook.getParent() == null) {
            offspring[0] = femaleHook.getSubTree();
        } else {
            maleHook.getParent().arg[maleHook.getChildIndex()] = femaleHook.getSubTree();
        }

        if (femaleHook.getParent() == null) {
            offspring[0] = maleHook.getSubTree();
        } else {
            femaleHook.getParent().arg[femaleHook.getChildIndex()] = maleHook.getSubTree();
        }

        // Make sure that the new individuals aren't too big.
        validateCrossover(male, female, offspring);

        return offspring;
    }

    /**
     * Given the parents and the two offsprings from a crossover
     * operation check to see whether we have exceeded the maximum allowed
     * depth. If a new individual has exceeded the maximum depth then one of
     * the parents is used.
     *
     * @param male first parent program
     * @param female second parent program
     * @param offspring individuals created by crossover
     */
    private void validateCrossover(Program male, Program female,
        Program[] offspring) {
        int depth;

        for (int i = 0; i < offspring.length; i++) {
            if (offspring[i] == null) {
                depth = 0;
            } else {
                depth = maxDepthOfTree(offspring[i]);
            }

            if ((depth < 1) || (depth > maxDepthForIndividualAfterCrossover)) {
                int whichParent = randomNumberGenerator.nextInt(2);

                if (whichParent == 0) {
                    offspring[i] = (Program) male.clone();
                } else {
                    offspring[i] = (Program) female.clone();
                }
            }
        }
    }

    /**
     * Returns the depth of the deepest branch of the given tree.
     *
     * @param tree tree to calculate the depth of the maximum branch
     *
     * @return DOCUMENT ME!
     */
    public int maxDepthOfTree(Program tree) {
        int maxDepth = 0;

        if (tree instanceof Function) {
            for (int i = 0; i < ((Function) tree).arg.length; i++) {
                Program s = ((Function) tree).arg[i];
                int depth = maxDepthOfTree(s);
                maxDepth = Math.max(maxDepth, depth);
            }

            return (maxDepth + 1);
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     * @param index DOCUMENT ME!
     * @param isFunction DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private TreeHook getSubTree(Program tree, int index, boolean isFunction) {
        int[] count = { index };

        return walkOnTree(tree, count, isFunction, null, -1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     * @param count DOCUMENT ME!
     * @param isFunction DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param childIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private TreeHook walkOnTree(Program tree, int[] count, boolean isFunction,
        Function parent, int childIndex) {
        if (isFunction) {
            // Test for function nodes
            if (GeneticProgram.isFunction(tree) && (count[0] == 0)) {
                return new TreeHook(tree, parent, childIndex);
            } else {
                TreeHook hook = null;

                if (tree instanceof Function) {
                    Function func = (Function) tree;

                    for (int i = 0; (i < func.arg.length) && (count[0] > 0);
                            i++) {
                        if (GeneticProgram.isFunction(func.arg[i])) {
                            count[0]--;
                        }

                        hook = walkOnTree(func.arg[i], count, isFunction, func,
                                i);
                    }
                }

                return hook;
            }
        } else {
            // Test for program nodes
            if (GeneticProgram.isProgram(tree) && (count[0] == 0)) {
                return new TreeHook(tree, parent, childIndex);
            } else {
                TreeHook hook = null;

                if (tree instanceof Function) {
                    Function func = (Function) tree;

                    for (int i = 0; (i < func.arg.length) && (count[0] > 0);
                            i++) {
                        if (GeneticProgram.isProgram(func.arg[i])) {
                            count[0]--;
                        }

                        hook = walkOnTree(func.arg[i], count, isFunction, func,
                                i);
                    }
                }

                return hook;
            }
        }
    }

    /**
     * Finds an individual based on selection criteria used in this
     * genetic program
     *
     * @return individual selected
     */
    public Individual findIndividual() {
        Individual ind = null;

        switch (selectionMethod) {
        case GeneticProgram.SELECTION_TOURNAMENT:
            ind = findIndividualUsingTournamentSelection();

            break;

        case GeneticProgram.SELECTION_FITNESS_PROPORTIONATE:
            ind = findIndividualUsingFitnessProportionateSelection(randomNumberGenerator.nextDouble());

            break;
        }

        return ind;
    }

    /**
     * Finds an individual using tournament selection
     *
     * @return individual selected
     */
    public Individual findIndividualUsingTournamentSelection() {
        // Picks some individuals from the population at random
        // and returns the best one.
        int tournamentSize = Math.min(populationSize, 7);

        Hashtable table = new Hashtable();

        while (table.size() < tournamentSize) {
            int key = randomNumberGenerator.nextInt(populationSize);
            table.put(new Integer(key), population[key]);
        }

        Enumeration e = table.elements();
        Individual best = (Individual) e.nextElement();
        double bestFitness = best.getStandardizedFitness();

        while (e.hasMoreElements()) {
            Individual individual = (Individual) e.nextElement();

            if (individual.getStandardizedFitness() > bestFitness) {
                best = individual;
                bestFitness = individual.getStandardizedFitness();
            }
        }

        return best;
    }

    /**
     * Finds an individual using fitness proportionate selection
     *
     * @param afterThisFitness DOCUMENT ME!
     *
     * @return individual selected
     */
    public Individual findIndividualUsingFitnessProportionateSelection(
        double afterThisFitness) {
        // Picks an individual in the specified population whose normalized fitness
        // is greater than the specified value(afterThisFitness).
        // All we need to do is count along the population form the begining
        // adding up the fitness until we get past the specified point.
        int indexOfSelectedIndividual;
        double sumOfFitness = 0.0;
        int index = 0;

        while ((index < populationSize) && (sumOfFitness < afterThisFitness)) {
            sumOfFitness = sumOfFitness +
                population[index].getNormalizedFitness();
            index++;
        }

        if (index >= populationSize) {
            indexOfSelectedIndividual = populationSize - 1;
        } else {
            indexOfSelectedIndividual = index - 1;
        }

        return population[indexOfSelectedIndividual];
    }

    /**
     * Evolves the population for one generation
     */
    public void evolve() {
        if (generationNo > 0) {
            breedNewPopulation();
        }

        zeroizeFitnessMeasuresOfPopulation();
        evaluateFitnessOfPopulation();
        normalizefitnessOfPopulation();

        // Sort the population so that the roulette wheel selection is easy
        sortPopulationByFitness();

        // Keep track of best-of-run individual
        bestIndividualInCurrentGeneration = population[0];

        if ((bestIndividual == null) ||
                (bestIndividual.getStandardizedFitness() > bestIndividualInCurrentGeneration.getStandardizedFitness())) {
            bestIndividual = bestIndividualInCurrentGeneration.copy();
            generationOfBestIndividual = generationNo;
        }

        generationNo++;
    }

    /**
     * DOCUMENT ME!
     */
    private void zeroizeFitnessMeasuresOfPopulation() {
        for (int i = 0; i < populationSize; i++) {
            population[i].setStandardizedFitness(0);
            population[i].setAdjustedFitness(0);
            population[i].setNormalizedFitness(0);
            population[i].setHits(0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void normalizefitnessOfPopulation() {
        // Computes the normalized and adjusted fitness of each individual
        // in the population.
        double sumOfAdjustedFitnesses = 0.0;

        for (int i = 0; i < populationSize; i++) {
            // Set the adjusted fitness
            population[i].setAdjustedFitness(1 / (population[i].getStandardizedFitness() +
                1.0));

            // Add up the adjusted fitness so that we can normalize them
            sumOfAdjustedFitnesses = sumOfAdjustedFitnesses +
                population[i].getAdjustedFitness();
        }

        // Loop through the population normalizing the adjusted fitness
        for (int i = 0; i < populationSize; i++) {
            population[i].setNormalizedFitness(population[i].getAdjustedFitness() / sumOfAdjustedFitnesses);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void sortPopulationByFitness() {
        sort(0, populationSize - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param low DOCUMENT ME!
     * @param high DOCUMENT ME!
     */
    private void sort(int low, int high) {
        // Uses quicksort to sort the population destructively
        // into descending order of normalized fitness
        int index1;

        // Uses quicksort to sort the population destructively
        // into descending order of normalized fitness
        int index2;
        double pivot;
        Individual temp;

        index1 = low;
        index2 = high;
        pivot = population[(low + high) / 2].getNormalizedFitness();

        do {
            while (population[index1].getNormalizedFitness() > pivot) {
                index1++;
            }

            while (population[index2].getNormalizedFitness() < pivot) {
                index2--;
            }

            if (index1 <= index2) {
                temp = population[index2];
                population[index2] = population[index1];
                population[index1] = temp;
                index1++;
                index2--;
            }
        } while (index1 <= index2);

        if (low < index2) {
            sort(low, index2);
        }

        if (index1 < high) {
            sort(index1, high);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void evaluateFitnessOfPopulation() {
        // Loops over the individuals in the population evaluating and
        // recording the fitness and hits.
        for (int i = 0; i < populationSize; i++) {
            fitnessFunction.evaluate(population[i], i);
        }
    }

    /**
     * Teset a given argument is a program or not
     *
     * @param program program to test whether it is a function or not
     *
     * @return true if given argument is a program
     */
    public static boolean isProgram(Program program) {
        return (program instanceof Program);
    }

    /**
     * Returns whether the given program is a function or not
     *
     * @param program program to test whether it is a function or not
     *
     * @return true if the given program is a function, else returns false
     */
    public static boolean isFunction(Program program) {
        return (program instanceof Function);
    }

    /**
     * Returns the best individual over all generations
     *
     * @return best individual so far
     */
    public Individual getBestIndividual() {
        return bestIndividual;
    }

    /**
     * Returns the genereation number of the best individual over all
     * generations
     *
     * @return index of the generation that best individual over all
     *         generations.
     */
    public int getGenerationOfBestIndividual() {
        return generationOfBestIndividual;
    }

    /**
     * Returns the best individual in the current generation
     *
     * @return best individual in the current generation
     */
    public Individual getBestIndividualInCurrentGeneration() {
        return bestIndividualInCurrentGeneration;
    }

    /**
     * Returns the average standardized fitness of the current
     * generation
     *
     * @return average standardized fitness of the current generation
     */
    public double getAverageStandardizedFitnessOfCurrentGeneration() {
        double fit = 0;

        for (int i = 0; i < population.length; i++) {
            fit = fit + population[i].getStandardizedFitness();
        }

        return (fit / population.length);
    }

    /**
     * Returns the average adjusted fitness of the current generation
     *
     * @return average adjusted fitness of the current generation
     */
    public double getAverageAdjustedFitnessOfCurrentGeneration() {
        double fit = 0;

        for (int i = 0; i < population.length; i++) {
            fit = fit + population[i].getAdjustedFitness();
        }

        return (fit / population.length);
    }

    /**
     * Returns the average normalized fitness of the current generation
     *
     * @return average normalized fitness of the current generation
     */
    public double getAverageNormalizedFitnessOfCurrentGeneration() {
        double fit = 0;

        for (int i = 0; i < population.length; i++) {
            fit = fit + population[i].getNormalizedFitness();
        }

        return (fit / population.length);
    }

    /**
     * Returns the random number generator used in this genetic program
     *
     * @return random number generator used in this genetic program
     */
    public Random getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    /**
     * Changes the random number generator used in this genetic program
     *
     * @param randGenerator new random number generator
     */
    public void setRandomNumberGenerator(Random randGenerator) {
        randomNumberGenerator = randGenerator;
    }

    /**
     * Returns the individuals in the population
     *
     * @return individuals
     */
    public Individual[] getPopulation() {
        return population;
    }

    /**
     * Returns current generation number
     *
     * @return current generation number
     */
    public int getGenerationNo() {
        return generationNo;
    }

    /**
     * Returns size of the population
     *
     * @return size of the population
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * Changes the size of the population
     *
     * @param newSize new size of the population
     */
    public void setPopulationSize(int newSize) {
        populationSize = newSize;
    }

    /**
     * Returns the maximum depth for the new individuals
     *
     * @return maximum depth for the new individuals
     */
    public int getMaxDepthForNewIndividual() {
        return maxDepthForNewIndividual;
    }

    /**
     * Changes the maximum depth for the new individuals
     *
     * @param newDepth new depth for the new individuals
     */
    public void setMaxDepthForNewIndividual(int newDepth) {
        maxDepthForNewIndividual = newDepth;
    }

    /**
     * Returns the maximum depth for the new individuals created by
     * crossover operation
     *
     * @return depth for the new individuals created by crossover operation
     */
    public int getMaxDepthForIndividualAfterCrossover() {
        return maxDepthForIndividualAfterCrossover;
    }

    /**
     * Changes the maximum depth for the new individuals created by
     * crossover operation
     *
     * @param newDepth new value for the new individuals created by crossover
     *        operation
     */
    public void setMaxDepthForIndividualAfterCrossover(int newDepth) {
        maxDepthForIndividualAfterCrossover = newDepth;
    }

    /**
     * Returns the maximum depth for the new subtree created by
     * mutation operation
     *
     * @return maximum depth for the new subtree created by mutation operation
     */
    public int getMaxDepthForNewSubtreeInMutant() {
        return maxDepthForNewSubtreeInMutant;
    }

    /**
     * Changes the maximum depth for the new subtree created by
     * mutation operation
     *
     * @param newDepth new value for the maximum depth for the new subtree
     *        created by mutation operation
     */
    public void setMaxDepthForNewSubtreeInMutant(int newDepth) {
        maxDepthForNewSubtreeInMutant = newDepth;
    }

    /**
     * Returns propability of crossover operation
     *
     * @return propability of crossover operation
     */
    public double getCrossoverFraction() {
        return crossoverFraction;
    }

    /**
     * Changes propability of crossover operation
     *
     * @param newValue new value for the propability of crossover operation
     */
    public void setCrossoverFraction(double newValue) {
        crossoverFraction = newValue;
    }

    /**
     * Return propability of mutation operation
     *
     * @return propability of mutation operation
     */
    public double getMutationFraction() {
        return mutationFraction;
    }

    /**
     * Changes propability of mutation operation
     *
     * @param newValue new value for the propability of mutation operation
     */
    public void setMutationFraction(double newValue) {
        mutationFraction = newValue;
    }

    /**
     * Return propability of reproduction operation
     *
     * @return propability of reproduction operation
     */
    public double getReproductionFraction() {
        return reproductionFraction;
    }

    /**
     * Changes the size of the population
     *
     * @param newValue fraction for the reproduction genetic operator
     */
    public void setReproductionFraction(double newValue) {
        reproductionFraction = newValue;
    }

    /**
     * Returns generation method used in this genetic program
     *
     * @return generation method used in this genetic program
     */
    public int getGenerationMethod() {
        return generationMethod;
    }

    /**
     * Changes generation method used in this genetic program
     *
     * @param newMethod new value of generation method used in this genetic
     *        program
     */
    public void setGenerationMethod(int newMethod) {
        generationMethod = newMethod;
    }

    /**
     * Returns the selection method used in this genetic program
     *
     * @return selection method used in this genetic program
     */
    public int getSelectionMethod() {
        return selectionMethod;
    }

    /**
     * Changes selection method used in this genetic program
     *
     * @param newMethod new selection method used in this genetic program
     */
    public void setSelectionMethod(int newMethod) {
        selectionMethod = newMethod;
    }

    /**
     * Returns terminal set of this genetic program
     *
     * @return terminal set
     */
    public Terminal[] getTerminalSet() {
        return terminalSet;
    }

    /**
     * Changes terminal set of this genetic program
     *
     * @param newSet new terminal set for the genetic program
     */
    public void setTerminalSet(Terminal[] newSet) {
        terminalSet = newSet;
    }

    /**
     * Returns function set of this genetic program
     *
     * @return function set
     */
    public Function[] getFunctionSet() {
        return functionSet;
    }

    /**
     * Changes function set of this genetic program
     *
     * @param newSet new function set for this genetic program
     */
    public void setFunctionSet(Function[] newSet) {
        functionSet = newSet;
    }

    /**
     * Changes the best individual of this population
     *
     * @param individualNo index of the best individual in the current
     *        population
     */
    public void setBestIndividual(int individualNo) {
        bestIndividual = population[individualNo].copy();
    }

    /**
     * Changes the generation number of the best individual
     *
     * @param generation generation of best individual
     */
    public void setGenerationOfBestIndividual(int generation) {
        generationOfBestIndividual = generation;
    }

    /**
     * Changes the best individual of the current generation
     *
     * @param individualNo index of the best individual in the current
     *        population
     */
    public void setBestIndividualInCurrentGeneration(int individualNo) {
        bestIndividualInCurrentGeneration = population[individualNo].copy();
    }

    /**
     * Return fitness function used in this genetic program
     *
     * @return fitness function
     */
    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }

    /**
     * Changes the best individual of the current generation
     *
     * @param function new fitness function
     */
    public void setFitnessFunction(FitnessFunction function) {
        fitnessFunction = function;
    }

    /**
     * Just a data structure to hold the pointer to a subtree as well
     * as to its parent.
     *
     * @author Levent Bayindir
     * @version 0.1
     */
    class TreeHook {
        /** DOCUMENT ME! */
        private Program subTree;

        /** DOCUMENT ME! */
        private Function parent;

        /** DOCUMENT ME! */
        private int childIndex;

/**
         * Create a TreeHook object with given parameters.
         *
         * @param subtree    subtree
         * @param parent     parent of this subtree
         * @param childIndex index of this subtree for the parent node
         */
        public TreeHook(Program subtree, Function parent, int childIndex) {
            this.subTree = subtree;
            this.parent = parent;
            this.childIndex = childIndex;
        }

        /**
         * Returns subtree
         *
         * @return subtree
         */
        public Program getSubTree() {
            return subTree;
        }

        /**
         * Returns parent of this subtree
         *
         * @return parent of this subtree
         */
        public Function getParent() {
            return parent;
        }

        /**
         * Returns child index
         *
         * @return child index
         */
        public int getChildIndex() {
            return childIndex;
        }
    }
}
