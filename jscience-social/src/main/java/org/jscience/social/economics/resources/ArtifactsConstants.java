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

package org.jscience.social.economics.resources;

/**
 * A class representing useful constants defining artifacts.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public final class ArtifactsConstants {

    /** 
     * Default constructor (private).
     */
    private ArtifactsConstants() {
        // Prevents instantiation
    }

    /** Constant for unknown or undefined artifact type. */
    public final static int UNKNOWN = 0;

    //places
    /** Shelter artifact type (e.g., home, castle). */
    public final static int SHELTER = 1;

    /** Gathering place artifact type (e.g., city center, forum, administrative place). */
    public final static int GATHER = 2;

    /** Leisure artifact type (e.g., feasts, amusement parks). */
    public final static int LEISURE = 3;

    /** Dining artifact type (e.g., restaurants, bars). */
    public final static int EAT = 4;

    /** Spiritual or intellectual artifact type (e.g., churches). */
    public final static int THINK = 5;

    /** Trade artifact type (e.g., markets). */
    public final static int TRADE = 6;
}

