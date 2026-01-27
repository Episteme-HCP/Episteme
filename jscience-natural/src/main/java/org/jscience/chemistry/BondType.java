/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;


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
