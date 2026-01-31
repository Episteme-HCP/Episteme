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

package org.jscience.social.sociology;


import org.jscience.natural.biology.Individual;
import org.jscience.social.economics.Organization;

/**
 * A specialized role for the person leading, organizing, or officiating a celebration or event.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MasterOfCeremony extends Role {

    private static final long serialVersionUID = 1L;

    private final Organization organization;

    /**
     * Creates a new MasterOfCeremony.
     *
     * @param individual   the individual performing the role
     * @param situation    the event or celebration
     * @param organization the organization managing or sponsoring the event (can be null)
     */
    public MasterOfCeremony(Individual individual, Situation situation, Organization organization) {
        super(individual, "Master of Ceremony", situation, Role.SUPERVISOR);
        this.organization = organization;
    }

    /**
     * Returns the sponsoring organization, if any.
     * @return the organization or null
     */
    public Organization getOrganization() {
        return organization;
    }
}

