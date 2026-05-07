/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.philosophy;

/**
 * Constants and enumerations for philosophy and religious studies.
 * <p>
 * Provides classifications for fundamental philosophical concepts
 * and world religion types.
 */
public final class PhilosophyConstants {

    private PhilosophyConstants() {}

    /**
     * Fundamental philosophical ontological concepts.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Concept {
        REALITY, // The Universe
        TIME, 
        GOD,     // Supernatural beings
        LIFE, 
        IDEAS,   // Mind, knowledge, spirit
        OBJECTS, // Physical world
        ACTS,    // Agency, ethics
        NOTHINGNESS,
        CONSCIOUSNESS
    }

    /**
     * Major types of religious systems.
     */
    public enum ReligionType {
        MONOTHEISM, POLYTHEISM, PANTHEISM, PANENTHEISM, 
        ATHEISM, AGNOSTICISM, SHAMANISM, SPIRITUAL_PHILOSOPHY, 
        SECULAR_HUMANISM, UNIVERSALISM, OTHER
    }
}

