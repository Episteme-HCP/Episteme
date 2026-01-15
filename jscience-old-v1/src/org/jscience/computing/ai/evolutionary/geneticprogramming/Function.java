package org.jscience.computing.ai.evolutionary.geneticprogramming;

/**
 * Abstraction for the functions in genetic programming.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public abstract class Function extends Program {
    /** The program which represents this function */
    public Program[] arg;

    /**
     * Returns a deep copy of this Function instance
     *
     * @return deep copy of this function
     */
    public Object clone() {
        Function temp = null;

        try {
            temp = (Function) getClass().newInstance();

            for (int i = 0; i < arg.length; i++) {
                temp.arg[i] = (Program) arg[i].clone();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return temp;
    }

    /**
     * Returns the number of nodes this function has.
     *
     * @return number of children nodes
     */
    public int countNodes() {
        int count = 1;

        for (int i = 0; i < arg.length; i++) {
            count = count + arg[i].countNodes();
        }

        return count;
    }

    /**
     * Returns the number of program nodes this function has. This
     * method traverses all the children nodes recursively until the leaf
     * nodes are reached and counts the number of programs.
     *
     * @return number of program nodes
     */
    public int countProgramNodes() {
        int count = 0;

        // Count the programs
        if (GeneticProgram.isProgram(this)) {
            count++;
        }

        for (int a = 0; a < arg.length; a++) {
            count = count + arg[a].countProgramNodes();
        }

        return count;
    }

    /**
     * Returns the number of function nodes this function has. This
     * method traverses all the children nodes recursively until the leaf
     * nodes are reached and counts the number of functions.
     *
     * @return number of function nodes
     */
    public int countFunctionNodes() {
        int count = 0;

        // Count the functions
        if (GeneticProgram.isFunction(this)) {
            count++;
        }

        for (int a = 0; a < arg.length; a++) {
            count = count + arg[a].countFunctionNodes();
        }

        return count;
    }

    /**
     * Returns a string representation of this function. (Instead of
     * toString - Used by Javolution)
     *
     * @return string representation of this function
     */
    public String toText() {
        String s = new String();

        s = getName() + "(";

        int i = 0;

        while (i < (arg.length - 1)) {
            s = s + arg[i].toText() + ",";
            i++;
        }

        if (i < arg.length) {
            s = s + arg[i].toText();
        }

        s = s + ")";

        return s;
    }
}
