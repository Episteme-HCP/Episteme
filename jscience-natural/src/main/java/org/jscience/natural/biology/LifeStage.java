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
 * Extensible categorization of organism life stages.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class LifeStage extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final LifeStage EMBRYO = new LifeStage("EMBRYO", true);
    public static final LifeStage JUVENILE = new LifeStage("JUVENILE", true);
    public static final LifeStage ADULT = new LifeStage("ADULT", true);
    public static final LifeStage SENESCENT = new LifeStage("SENESCENT", true);
    public static final LifeStage OTHER = new LifeStage("OTHER", true);
    public static final LifeStage UNKNOWN = new LifeStage("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(LifeStage.class, EMBRYO);
        EnumRegistry.register(LifeStage.class, JUVENILE);
        EnumRegistry.register(LifeStage.class, ADULT);
        EnumRegistry.register(LifeStage.class, SENESCENT);
        EnumRegistry.register(LifeStage.class, OTHER);
        EnumRegistry.register(LifeStage.class, UNKNOWN);
    }

    /**
     * Creates a new user-defined (non-built-in) life stage.
     * @param name Name of the life stage.
     */
    public LifeStage(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(LifeStage.class, this);
    }

    private LifeStage(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static LifeStage valueOf(String name) {
        return EnumRegistry.getRegistry(LifeStage.class).valueOfRequired(name);
    }

    public static LifeStage[] values() {
        return EnumRegistry.getRegistry(LifeStage.class).values().toArray(new LifeStage[0]);
    }
}

