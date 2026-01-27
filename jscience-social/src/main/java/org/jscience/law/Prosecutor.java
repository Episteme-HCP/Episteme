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

package org.jscience.law;

import org.jscience.biology.Individual;
import org.jscience.economics.Worker;
import org.jscience.politics.Administration;

/**
 * Represents a prosecutor, who is a legal professional responsible for 
 * presenting the case in a criminal trial against an individual accused 
 * of breaking the law.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Prosecutor extends Worker {

    /**
     * Creates a new Prosecutor with a specific function.
     *
     * @param individual the individual taking on the role
     * @param lawSuitSituation the lawsuit context
     * @param function the specific title or function of the prosecutor
     * @param administration the judicial administration they work for
     */
    public Prosecutor(Individual individual, LawSuitSituation lawSuitSituation,
        String function, Administration administration) {
        super(individual, lawSuitSituation, function, administration);
    }

    /**
     * Creates a new Prosecutor with the default title "Prosecutor".
     *
     * @param individual the individual taking on the role
     * @param lawSuitSituation the lawsuit context
     * @param administration the judicial administration they work for
     */
    public Prosecutor(Individual individual, LawSuitSituation lawSuitSituation,
        Administration administration) {
        super(individual, lawSuitSituation, "Prosecutor", administration);
    }
}
