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

package org.jscience.natural.chemistry;

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;


/**
 * Represents the type of a chemical bond.
 * Extends {@link ExtensibleEnum} to support dynamic bond types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class BondType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<BondType> REGISTRY = EnumRegistry.getRegistry(BondType.class);

    public static final BondType SINGLE = new BondType("SINGLE", 1.0, true);
    public static final BondType DOUBLE = new BondType("DOUBLE", 2.0, true);
    public static final BondType TRIPLE = new BondType("TRIPLE", 3.0, true);
    public static final BondType AROMATIC = new BondType("AROMATIC", 1.5, true);
    public static final BondType COORDINATION = new BondType("COORDINATION", 1.0, true);
    public static final BondType HYDROGEN = new BondType("HYDROGEN", 0.1, true);
    public static final BondType IONIC = new BondType("IONIC", 1.0, true);

    private final double bondOrder;
    private final boolean builtIn;

    public BondType(String name, double bondOrder) {
        this(name, bondOrder, false);
    }

    private BondType(String name, double bondOrder, boolean builtIn) {
        super(name.toUpperCase());
        this.bondOrder = bondOrder;
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    public double getBondOrder() {
        return bondOrder;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static BondType valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}

