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
 * Each method at run time is represented as a class derived from this
 * abstract class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public abstract class Method extends DomainElement {
    /**
     * An array of task lists to any of which this method can decompse
     * its associated task given that the corresponding precondition is
     * satisfied.
     */
    private TaskList[] subs;

/**
     * To initialize the method.
     *
     * @param head head of the method.
     */
    public Method(Predicate head) {
        super(head);
    }

    /**
     * To get the label of a given branch of this method.
     *
     * @param which the branch the label of which is to be returned.
     *
     * @return the label for that branch.
     */
    public abstract String getLabel(int which);

    /**
     * To get the possible decompositions of this method.
     *
     * @return an array of possible decompositions.
     */
    public TaskList[] getSubs() {
        return subs;
    }

    /**
     * To set the possible decompositions of this method.
     *
     * @param subsIn an array of possible decompositions.
     */
    public void setSubs(TaskList[] subsIn) {
        subs = subsIn;
    }
}
