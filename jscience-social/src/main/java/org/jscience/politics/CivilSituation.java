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

package org.jscience.politics;


import java.util.Objects;
import org.jscience.biology.Individual;
import org.jscience.sociology.Role;
import org.jscience.sociology.Situation;

/**
 * Represents a social interaction context where individuals interact as citizens 
 * within urban or civil environments.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CivilSituation extends Situation {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new CivilSituation.
     *
     * @param name     the name of the situation (e.g., "City Council Meet", "Market Day")
     * @param comments descriptive details about the civil context
     * @throws NullPointerException if any argument is null
     */
    public CivilSituation(String name, String comments) {
        super(name, comments);
    }

    /**
     * Adds an individual to the civil situation as a citizen.
     *
     * @param individual the individual taking on the citizen role
     * @throws NullPointerException if individual is null
     */
    public void addCitizen(Individual individual) {
        Objects.requireNonNull(individual, "Individual cannot be null");
        super.addParticipant(individual, "Citizen", Role.CLIENT);
    }
}
