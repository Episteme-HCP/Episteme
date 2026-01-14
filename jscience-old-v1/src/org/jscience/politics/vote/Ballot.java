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

package org.jscience.politics.vote;

import java.util.Set;


/**
 * This class represents the different choices available to someone in a
 * specific vote session.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//inspired by http://www.cs.unc.edu/~wluebke/comp204/liberty/
public abstract class Ballot implements Cloneable {
    // Adds a choice to the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     */
    public abstract void addChoice(String title);

    //  Adds an option to a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     */
    public abstract void addOptionToChoice(String title, String option);

    // Returns the choices on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getChoices();

    //  Returns a set of options for a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getOptionsForChoice(String title);

    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isOptionSelected(String title, String option);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Object clone();
}
