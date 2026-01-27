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

package org.jscience.biology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible categorization of biological reproduction modes.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class ReproductionMode extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final ReproductionMode SEXUAL = new ReproductionMode("SEXUAL", true);
    public static final ReproductionMode ASEXUAL = new ReproductionMode("ASEXUAL", true);
    public static final ReproductionMode BUDDING = new ReproductionMode("BUDDING", true);
    public static final ReproductionMode FRAGMENTATION = new ReproductionMode("FRAGMENTATION", true);
    public static final ReproductionMode PARTHENOGENESIS = new ReproductionMode("PARTHENOGENESIS", true);
    public static final ReproductionMode BINARY_FISSION = new ReproductionMode("BINARY_FISSION", true);
    public static final ReproductionMode OTHER = new ReproductionMode("OTHER", true);
    public static final ReproductionMode UNKNOWN = new ReproductionMode("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(ReproductionMode.class, SEXUAL);
        EnumRegistry.register(ReproductionMode.class, ASEXUAL);
        EnumRegistry.register(ReproductionMode.class, BUDDING);
        EnumRegistry.register(ReproductionMode.class, FRAGMENTATION);
        EnumRegistry.register(ReproductionMode.class, PARTHENOGENESIS);
        EnumRegistry.register(ReproductionMode.class, BINARY_FISSION);
        EnumRegistry.register(ReproductionMode.class, OTHER);
        EnumRegistry.register(ReproductionMode.class, UNKNOWN);
    }

    /**
     * Creates a new user-defined (non-built-in) reproduction mode.
     * @param name Name of the reproduction mode.
     */
    public ReproductionMode(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(ReproductionMode.class, this);
    }

    private ReproductionMode(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static ReproductionMode valueOf(String name) {
        return EnumRegistry.getRegistry(ReproductionMode.class).valueOfRequired(name);
    }

    public static ReproductionMode[] values() {
        return EnumRegistry.getRegistry(ReproductionMode.class).values().toArray(new ReproductionMode[0]);
    }
}
