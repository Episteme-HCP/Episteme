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
 * Extensible enumeration for Industry Sector.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class IndustrySector extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final IndustrySector PRIMARY = new IndustrySector("PRIMARY", true);
    public static final IndustrySector SECONDARY = new IndustrySector("SECONDARY", true);
    public static final IndustrySector TERTIARY = new IndustrySector("TERTIARY", true);
    public static final IndustrySector QUATERNARY = new IndustrySector("QUATERNARY", true);
    public static final IndustrySector QUINARY = new IndustrySector("QUINARY", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(IndustrySector.class, PRIMARY);
        EnumRegistry.register(IndustrySector.class, SECONDARY);
        EnumRegistry.register(IndustrySector.class, TERTIARY);
        EnumRegistry.register(IndustrySector.class, QUATERNARY);
        EnumRegistry.register(IndustrySector.class, QUINARY);
    }

    public IndustrySector(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(IndustrySector.class, this);
    }

    private IndustrySector(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static IndustrySector valueOf(String name) {
        return EnumRegistry.getRegistry(IndustrySector.class).valueOfRequired(name);
    }
    
    public static IndustrySector[] values() {
        return EnumRegistry.getRegistry(IndustrySector.class).values().toArray(new IndustrySector[0]);
    }
}

