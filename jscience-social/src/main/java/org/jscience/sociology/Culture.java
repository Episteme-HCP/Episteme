/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.sociology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jscience.linguistics.Language;
import org.jscience.philosophy.Belief;
import org.jscience.util.identity.Identifiable;

/**
 * Represents the culture of a group or society.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Culture implements Identifiable<String> {

    private final String name;
    private final Language language;
    private final Set<Belief> beliefs = new HashSet<>();
    private final Set<String> celebrations = new HashSet<>(); // Simplified string representation for now
    private final Set<String> rituals = new HashSet<>();

    public Culture(String name, Language language) {
        this.name = name;
        this.language = language;
    }

    @Override
    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Language getLanguage() {
        return language;
    }

    public void addBelief(Belief belief) {
        beliefs.add(belief);
    }

    public Set<Belief> getBeliefs() {
        return Collections.unmodifiableSet(beliefs);
    }

    public void addCelebration(String celebration) {
        celebrations.add(celebration);
    }

    public Set<String> getCelebrations() {
        return Collections.unmodifiableSet(celebrations);
    }

    public void addRitual(String ritual) {
        rituals.add(ritual);
    }

    public Set<String> getRituals() {
        return Collections.unmodifiableSet(rituals);
    }

    @Override
    public String toString() {
        return name;
    }
}
