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

package org.jscience.natural.biology;

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;

/**
 * Extensible categorization of human blood types.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class BloodType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final BloodType A_POSITIVE = new BloodType("A_POSITIVE", "A+", true);
    public static final BloodType A_NEGATIVE = new BloodType("A_NEGATIVE", "A-", true);
    public static final BloodType B_POSITIVE = new BloodType("B_POSITIVE", "B+", true);
    public static final BloodType B_NEGATIVE = new BloodType("B_NEGATIVE", "B-", true);
    public static final BloodType AB_POSITIVE = new BloodType("AB_POSITIVE", "AB+", true);
    public static final BloodType AB_NEGATIVE = new BloodType("AB_NEGATIVE", "AB-", true);
    public static final BloodType O_POSITIVE = new BloodType("O_POSITIVE", "O+", true);
    public static final BloodType O_NEGATIVE = new BloodType("O_NEGATIVE", "O-", true);
    public static final BloodType UNKNOWN = new BloodType("UNKNOWN", "?", true);

    private final String symbol;
    private final boolean builtIn;

    static {
        EnumRegistry.register(BloodType.class, A_POSITIVE);
        EnumRegistry.register(BloodType.class, A_NEGATIVE);
        EnumRegistry.register(BloodType.class, B_POSITIVE);
        EnumRegistry.register(BloodType.class, B_NEGATIVE);
        EnumRegistry.register(BloodType.class, AB_POSITIVE);
        EnumRegistry.register(BloodType.class, AB_NEGATIVE);
        EnumRegistry.register(BloodType.class, O_POSITIVE);
        EnumRegistry.register(BloodType.class, O_NEGATIVE);
        EnumRegistry.register(BloodType.class, UNKNOWN);
    }

    /**
     * Creates a new user-defined (non-built-in) blood type.
     * @param name Name of the blood type.
     * @param symbol Symbol for the blood type.
     */
    public BloodType(String name, String symbol) {
        super(name);
        this.symbol = symbol;
        this.builtIn = false;
        EnumRegistry.register(BloodType.class, this);
    }

    private BloodType(String name, String symbol, boolean builtIn) {
        super(name);
        this.symbol = symbol;
        this.builtIn = builtIn;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static BloodType valueOf(String name) {
        return EnumRegistry.getRegistry(BloodType.class).valueOfRequired(name);
    }

    public static BloodType[] values() {
        return EnumRegistry.getRegistry(BloodType.class).values().toArray(new BloodType[0]);
    }
}

