/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import org.jscience.geography.Place;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a legal jurisdiction or area of authority. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class Jurisdiction {

    private final String name;
    private final Place territory;
    private final Set<Statute> statutes;
    private final Jurisdiction parentJurisdiction; // e.g., State in a Country

    public Jurisdiction(String name, Place territory) {
        this(name, territory, null);
    }

    public Jurisdiction(String name, Place territory, Jurisdiction parentJurisdiction) {
        this.name = name;
        this.territory = territory;
        this.parentJurisdiction = parentJurisdiction;
        this.statutes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Place getTerritory() {
        return territory;
    }

    public Jurisdiction getParentJurisdiction() {
        return parentJurisdiction;
    }

    public void addStatute(Statute statute) {
        statutes.add(statute);
    }

    public Set<Statute> getStatutes() {
        return Collections.unmodifiableSet(statutes);
    }

    /**
     * Checks if a statute is applicable in this jurisdiction (including inherited).
     */
    public boolean isApplicable(Statute statute) {
        if (statutes.contains(statute))
            return true;
        if (parentJurisdiction != null)
            return parentJurisdiction.isApplicable(statute);
        return false;
    }
}
