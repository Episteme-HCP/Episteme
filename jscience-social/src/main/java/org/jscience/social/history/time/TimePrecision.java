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

package org.jscience.social.history.time;


import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;

/**
 * Precision levels for temporal data in historical contexts.
 * Modernized to be an extensible enumeration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TimePrecision extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<TimePrecision> REGISTRY = new EnumRegistry<>();

    public static final TimePrecision EXACT = new TimePrecision("EXACT", true);
    public static final TimePrecision DAY = new TimePrecision("DAY", true);
    public static final TimePrecision MONTH = new TimePrecision("MONTH", true);
    public static final TimePrecision YEAR = new TimePrecision("YEAR", true);
    public static final TimePrecision DECADE = new TimePrecision("DECADE", true);
    public static final TimePrecision CENTURY = new TimePrecision("CENTURY", true);
    public static final TimePrecision MILLENNIUM = new TimePrecision("MILLENNIUM", true);
    public static final TimePrecision APPROXIMATE = new TimePrecision("APPROXIMATE", true);
    public static final TimePrecision UNKNOWN = new TimePrecision("UNKNOWN", true);

    private TimePrecision(String name, boolean builtIn) {
        super(name);
        REGISTRY.register(this);
    }

    public static TimePrecision valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static TimePrecision valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }
}

