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

package org.jscience.computing.ai.planning;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * This class represent a plan as a <code>LinkedList</code> of ground
 * instances of operators.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class Plan {
    /** The new line character in the platform Planner is running on. */
    final static String endl = System.getProperty("line.separator");

    /** The cost of the plan. */
    private double cost;

    /**
     * The plan as a <code>LinkedList</code> of ground instances of
     * operators.
     */
    private LinkedList ops;

/**
     * To initialize the plan to an empty list.
     */
    public Plan() {
        ops = new LinkedList();
        cost = 0;
    }

/**
     * This function is used by objects of this class to clone themselves.
     *
     * @param opsIn  the operators in the plan.
     * @param costIn the cost of the plan.
     */
    private Plan(LinkedList opsIn, double costIn) {
        ops = opsIn;
        cost = costIn;
    }

    /**
     * To add an operator instance to the end of the plan.
     *
     * @param op the operator the instance of which is being added.
     * @param binding the binding to instantiate the operator.
     *
     * @return the cost of the operator instance being added.
     */
    public double addOperator(Operator op, Term[] binding) {
        ops.addLast(op.getHead().applySubstitution(binding));
        cost += op.getCost(binding);

        return op.getCost(binding);
    }

    /**
     * To clone an object of this class.
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Plan((LinkedList) ops.clone(), cost);
    }

    /**
     * To remove the operator instance at the end of the plan.
     *
     * @param opCost the cost of the operator instance to be removed.
     */
    public void removeOperator(double opCost) {
        ops.removeLast();
        cost -= opCost;
    }

    /**
     * This function returns a printable <code>String</code>
     * representation of this plan.
     *
     * @return the <code>String</code> representation of this plan.
     */
    public String toString() {
        //-- The value to be returned.
        String retVal = "Plan cost: " + cost + endl + endl;

        //-- Get the names of the operators in this domain.
        String[] primitiveTasks = Planner.getDomain().getPrimitiveTasks();

        //-- Iterate over the operator instances in the plan and print them.
        Iterator e = ops.iterator();

        while (e.hasNext())
            retVal += (((Predicate) e.next()).toString(primitiveTasks) + endl);

        return retVal + "--------------------" + endl;
    }
}
