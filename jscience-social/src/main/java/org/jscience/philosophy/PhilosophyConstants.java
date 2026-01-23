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

/**
 * Defines useful constants for the scientific study of religions and 
 * philosophies.
 * 
 * <p> This class categorizes ontological domains and religious structural 
 *     archetypes used across the philosophy module.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */
public final class PhilosophyConstants {


    private PhilosophyConstants() {}

    /** Ontological domain of the physical universe. */
    public final static int REALITY = 1;

    /** Ontological domain of temporal succession. */
    public final static int TIME = 2;

    /** Domain of supernatural or transcendental entities. */
    public final static int GOD = 4;

    /** Domain of biological or existential vitality. */
    public final static int LIFE = 8;

    /** Domain of cognition, knowledge, and the spiritual world. */
    public final static int IDEAS = 16;

    /** Domain of material objects and the physical world. */
    public final static int OBJECTS = 32;

    /** Domain of human or agency-driven actions. */
    public final static int ACTS = 64;

    // Religious Structure Archetypes
    
    /** Universalist systems or global faith frameworks. */
    public final static int UNIVERSAL_RELIGION = 1;

    /** Belief systems centered on a single deity. */
    public final static int MONOTHEISTIC_RELIGION = 2;

    /** Systems with multiple deities or transcendental entities. */
    public final static int POLYTHEISTIC_RELIGION = 3;

    /** Animistic or shaman-led spiritual practices. */
    public final static int SHAMANISTIC_RELIGION = 4;

    /** Belief that the universe and the divine are identical. */
    public final static int PANTHEISTIC_RELIGION = 5;

    /** Non-theistic philosophical frameworks with ritualistic elements. */
    public final static int SPIRITUAL_PHILOSOPHY_RELIGION = 6;

    /** Securalist ideological systems with quasi-religious structure. */
    public final static int IDEOLOGICAL_RELIGION = 7;
}
