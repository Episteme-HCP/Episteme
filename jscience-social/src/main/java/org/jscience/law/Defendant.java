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
import org.jscience.sociology.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the defendant in a legal proceeding. A defendant is 
 * an individual or entity against whom a lawsuit or criminal charge is brought.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Defendant extends Role {
    
    private Set<String> charges;

    /**
     * Creates a new Defendant object.
     *
     * @param individual the individual taking the role of defendant
     * @param lawSuitSituation the legal situation or trial context
     */
    public Defendant(Individual individual, LawSuitSituation lawSuitSituation) {
        super(individual, "Defendant", lawSuitSituation, Role.CLIENT);
        this.charges = new HashSet<>();
    }

    /**
     * Returns the set of charges brought against the defendant.
     * @return a set of charge descriptions
     */
    public Set<String> getCharges() {
        return charges;
    }

    /**
     * Sets the legal charges for this defendant.
     *
     * @param charges a set of strings representing individual charges
     * @throws IllegalArgumentException if the set is null or contains non-string elements
     */
    public void setCharges(Set<String> charges) {
        if (charges == null) {
            throw new IllegalArgumentException("The set of charges cannot be null.");
        }

        for (Object charge : charges) {
            if (!(charge instanceof String)) {
                throw new IllegalArgumentException("Charges must be represented by Strings.");
            }
        }

        this.charges = new HashSet<>(charges);
    }

    /**
     * Adds a specific charge to the defendant's record.
     * @param charge the charge description
     */
    public void addCharge(String charge) {
        if (charge != null) {
            charges.add(charge);
        }
    }

    /**
     * Removes a specific charge from the defendant's record.
     * @param charge the charge description to remove
     */
    public void removeCharge(String charge) {
        charges.remove(charge);
    }
}
