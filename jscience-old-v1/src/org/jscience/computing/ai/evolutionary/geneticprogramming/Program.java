package org.jscience.computing.ai.evolutionary.geneticprogramming;

/**
 * Abstraction for the programs in genetic programming.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public abstract class Program implements Cloneable {
    /**
     * Returns a string representation of this program. (Instead of
     * toString - Used by Javolution)
     *
     * @return number of function nodes
     */
    public abstract String toText();

    /**
     * Returns the name of this program.
     *
     * @return name of this program
     */
    public abstract String getName();

    /**
     * Returns evaluation of this program with the given parameter
     *
     * @param fitnessCase fitness case to evaluate
     *
     * @return evaluation result
     */
    public abstract Object eval(Object fitnessCase);

    /**
     * Returns the number of nodes this program has.
     *
     * @return number of children nodes
     */
    public abstract int countNodes();

    /**
     * Returns the number of program nodes this program has. This
     * method traverses all the children nodes recursively until the leaf
     * nodes are reached and counts the number of programs.
     *
     * @return number of program nodes
     */
    public abstract int countProgramNodes();

    /**
     * Returns the number of function nodes this program has. This
     * method traverses all the children nodes recursively until the leaf
     * nodes are reached and counts the number of functions.
     *
     * @return number of function nodes
     */
    public abstract int countFunctionNodes();

    /**
     * Returns a deep copy of this Program instance
     *
     * @return deep copy of this program
     */
    public abstract Object clone();
}
