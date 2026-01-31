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

package org.jscience.social.economics;

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;

/**
 * Extensible enumeration for Market Structure.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class MarketStructure extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final MarketStructure PERFECT_COMPETITION = new MarketStructure("PERFECT_COMPETITION", true);
    public static final MarketStructure MONOPOLISTIC_COMPETITION = new MarketStructure("MONOPOLISTIC_COMPETITION", true);
    public static final MarketStructure OLIGOPOLY = new MarketStructure("OLIGOPOLY", true);
    public static final MarketStructure MONOPOLY = new MarketStructure("MONOPOLY", true);
    public static final MarketStructure MONOPSONY = new MarketStructure("MONOPSONY", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(MarketStructure.class, PERFECT_COMPETITION);
        EnumRegistry.register(MarketStructure.class, MONOPOLISTIC_COMPETITION);
        EnumRegistry.register(MarketStructure.class, OLIGOPOLY);
        EnumRegistry.register(MarketStructure.class, MONOPOLY);
        EnumRegistry.register(MarketStructure.class, MONOPSONY);
    }

    public MarketStructure(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(MarketStructure.class, this);
    }

    private MarketStructure(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static MarketStructure valueOf(String name) {
        return EnumRegistry.getRegistry(MarketStructure.class).valueOfRequired(name);
    }
    
    public static MarketStructure[] values() {
        return EnumRegistry.getRegistry(MarketStructure.class).values().toArray(new MarketStructure[0]);
    }
}

