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

package org.jscience.social.politics.flags;

/**
 * Constants and enums for vexillology (the study of flags).
 * Defines common patterns and symbols found in flags globally.
 * */
public final class Vexillology {

    private Vexillology() {}

    /**
     * Common geometric proportions for flags.
     */
    public static final double RATIO_2_3 = 1.5;
    public static final double RATIO_1_2 = 2.0;
    public static final double RATIO_3_5 = 1.666;
    public static final double RATIO_SQUARE = 1.0;

    /**
     * Standard flag patterns.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Pattern {
        SOLID,
        HORIZONTAL_BICOLOR,
        VERTICAL_BICOLOR,
        HORIZONTAL_TRICOLOR,
        VERTICAL_TRICOLOR,
        CANTON,         // e.g. USA, Australia
        CROSS,          // e.g. England, Nordic countries
        SALTIRE,        // X-shaped cross, e.g. Scotland
        BORDURE,        // Border
        QUARTERED,
        TRIANGLE,       // e.g. Czechia, Philippines
        PALE,           // Wide vertical stripe
        FESS            // Wide horizontal stripe
    }

    /**
     * Common symbolic elements.
     */
    public enum Symbol {
        STAR,
        CRESCENT,
        SUN,
        LION,
        EAGLE,
        DRAGON,
        CROSS,
        CIRCLE,
        COAT_OF_ARMS,
        WREATH,
        STRIPES,
        CROWN,
        HAMMER_AND_SICKLE,
        OTHER
    }
}

