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

package org.episteme.social.history;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;

/**
 * An extensible enumeration of major historical periods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HistoricalPeriod extends ExtensibleEnum {

    public static final EnumRegistry<HistoricalPeriod> REGISTRY = new EnumRegistry<>();

    public static final HistoricalPeriod PREHISTORY = new HistoricalPeriod("PREHISTORY", true);
    public static final HistoricalPeriod ANCIENT_HISTORY = new HistoricalPeriod("ANCIENT_HISTORY", true);
    public static final HistoricalPeriod POST_CLASSICAL = new HistoricalPeriod("POST_CLASSICAL", true);
    public static final HistoricalPeriod EARLY_MODERN = new HistoricalPeriod("EARLY_MODERN", true);
    public static final HistoricalPeriod LATE_MODERN = new HistoricalPeriod("LATE_MODERN", true);
    public static final HistoricalPeriod CONTEMPORARY = new HistoricalPeriod("CONTEMPORARY", true);
    
    public static final HistoricalPeriod OTHER = new HistoricalPeriod("OTHER", true);
    public static final HistoricalPeriod UNKNOWN = new HistoricalPeriod("UNKNOWN", true);

    public HistoricalPeriod(String name) {
        this(name, false);
    }

    private HistoricalPeriod(String name, boolean builtIn) {
        super(name);
        REGISTRY.register(this);
    }
}

