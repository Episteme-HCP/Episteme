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

package org.jscience.psychology.social;


import java.util.Objects;
import org.jscience.biology.ecology.Population;
import org.jscience.biology.taxonomy.Species;

/**
 * Represents a transient gathering of individuals (a crowd) sharing a common physical proximity 
 * and motive, but lacking the structured relations found in a {@link Group}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class Crowd extends Population {

    private static final long serialVersionUID = 1L;

    private final String motive;

    /**
     * Creates a new Crowd with a specific motive.
     *
     * @param species the species of the crowd members
     * @param motive  the reason for the gathering
     * @throws NullPointerException if any argument is null
     */
    public Crowd(Species species, String motive) {
        super(species);
        this.motive = Objects.requireNonNull(motive, "Motive cannot be null");
    }

    /**
     * Returns the motive for the crowd's gathering.
     * @return the motive string
     */
    public String getMotive() {
        return motive;
    }
}
