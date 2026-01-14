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

package org.jscience.philosophy;

import java.util.HashSet;
import java.util.Set;
import org.jscience.util.identity.Identifiable;

/**
 * Represents a religion or belief system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Religion implements Identifiable<String> {

    private final String name;
    private final Set<Belief> coreBeliefs = new HashSet<>();

    public Religion(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void addBelief(Belief belief) {
        coreBeliefs.add(belief);
    }

    public Set<Belief> getCoreBeliefs() {
        return coreBeliefs;
    }

    @Override
    public String toString() {
        return name;
    }

    public static final Religion CHRISTIANITY = new Religion("Christianity");
    public static final Religion ISLAM = new Religion("Islam");
    public static final Religion HINDUISM = new Religion("Hinduism");
    public static final Religion BUDDHISM = new Religion("Buddhism");
    public static final Religion JUDAISM = new Religion("Judaism");
}


