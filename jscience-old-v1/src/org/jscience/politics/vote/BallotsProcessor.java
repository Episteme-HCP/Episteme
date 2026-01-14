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
 * This class represents a way by which you compute results.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//You have first to call validateBallots() THEN ONLY getResults() which returns the appropriate results
//and tell if you should launch one more round (shouldProceedToNextRound()) because some winners are still not known
public interface BallotsProcessor {
    //you have to call this method first
    /**
     * DOCUMENT ME!
     *
     * @param voters DOCUMENT ME!
     * @param round DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set validateBallots(Set voters, int round);

    //fills a new pseudo ballot with the summed results telling who has won for every choice
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Ballot getResults();

    //optional: implementations should also provide one of the two following depending on if there is one or multiple winners for every choice
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean shouldProceedToNextRound();
}
