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

package org.jscience.social.law;

import org.jscience.natural.biology.Individual;
import org.jscience.social.economics.Worker;
import org.jscience.social.politics.Administration;

/**
 * Represents a judge responsible for presiding over legal proceedings and trials.
 * A judge works within a specific administration (e.g., Department of Justice) 
 * and is assigned to a legal situation or lawsuit.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Judge extends Worker {

    /**
     * Creates a new Judge object with a specific function.
     *
     * @param individual the individual taking the role of judge
     * @param lawSuitSituation the context of the trial or lawsuit
     * @param function the specific judicial title or function (e.g., "Presiding Judge")
     * @param administration the judicial administration or court system
     */
    public Judge(Individual individual, LawSuitSituation lawSuitSituation,
        String function, Administration administration) {
        super(individual, lawSuitSituation, function, administration);
    }

    /**
     * Creates a new Judge object with the default "Judge" title.
     *
     * @param individual the individual taking the role of judge
     * @param lawSuitSituation the context of the trial or lawsuit
     * @param administration the judicial administration or court system
     */
    public Judge(Individual individual, LawSuitSituation lawSuitSituation,
        Administration administration) {
        super(individual, lawSuitSituation, "Judge", administration);
    }
}

