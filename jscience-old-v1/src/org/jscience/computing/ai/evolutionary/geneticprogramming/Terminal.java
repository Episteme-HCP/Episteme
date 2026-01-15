package org.jscience.computing.ai.evolutionary.geneticprogramming;

/**
 * Abstraction for the terminals in genetic programming.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public abstract class Terminal extends Program {
    /**
     * Returns the number of nodes this terminal has.
     *
     * @return 1 (each terminal has one node)
     */
    public int countNodes() {
        return 1;
    }

    /**
     * Returns the number of program nodes this terminal has.
     *
     * @return 1 (each terminal has one program)
     */
    public int countProgramNodes() {
        if (GeneticProgram.isProgram(this)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the number of function nodes this terminal has.
     *
     * @return 0 (this node is a terminal not a function)
     */
    public int countFunctionNodes() {
        if (GeneticProgram.isFunction(this)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns a deep copy of this Terminal instance
     *
     * @return deep copy of this terminal
     */
    public Object clone() {
        Terminal temp = null;

        try {
            temp = (Terminal) getClass().newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }

        return temp;
    }
}
