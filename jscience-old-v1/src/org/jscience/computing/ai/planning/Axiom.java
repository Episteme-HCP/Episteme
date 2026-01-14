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

/**
 * Each axiom at run time is represented as a class derived from this
 * abstract class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public abstract class Axiom extends DomainElement {
    /**
     * Number of branches of this axiom. Each branch represents one
     * different way to prove this axiom, in case all the previous branches
     * have already been tried and failed.
     */
    private int branchSize;

/**
     * To initialize the axiom.
     *
     * @param head         head of the axiom.
     * @param branchSizeIn number of branches in the axiom.
     */
    public Axiom(Predicate head, int branchSizeIn) {
        super(head);
        branchSize = branchSizeIn;
    }

    /**
     * To get the number of branches in this axiom.
     *
     * @return number of branches in this axiom.
     */
    public int getBranchSize() {
        return branchSize;
    }

    /**
     * To get the label of a given branch of this axiom.
     *
     * @param which the branch the label of which is to be returned.
     *
     * @return the label for that branch.
     */
    public abstract String getLabel(int which);
}
