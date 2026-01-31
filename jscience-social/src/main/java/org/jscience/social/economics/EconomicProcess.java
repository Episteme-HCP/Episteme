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
 * Extensible enumeration for Economic Process.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class EconomicProcess extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EconomicProcess PRODUCTION = new EconomicProcess("PRODUCTION", true);
    public static final EconomicProcess TRANSFORMATION = new EconomicProcess("TRANSFORMATION", true);
    public static final EconomicProcess DISTRIBUTION = new EconomicProcess("DISTRIBUTION", true);
    public static final EconomicProcess CONSUMPTION = new EconomicProcess("CONSUMPTION", true);
    public static final EconomicProcess RECYCLING = new EconomicProcess("RECYCLING", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(EconomicProcess.class, PRODUCTION);
        EnumRegistry.register(EconomicProcess.class, TRANSFORMATION);
        EnumRegistry.register(EconomicProcess.class, DISTRIBUTION);
        EnumRegistry.register(EconomicProcess.class, CONSUMPTION);
        EnumRegistry.register(EconomicProcess.class, RECYCLING);
    }

    public EconomicProcess(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(EconomicProcess.class, this);
    }

    private EconomicProcess(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static EconomicProcess valueOf(String name) {
        return EnumRegistry.getRegistry(EconomicProcess.class).valueOfRequired(name);
    }
    
    public static EconomicProcess[] values() {
        return EnumRegistry.getRegistry(EconomicProcess.class).values().toArray(new EconomicProcess[0]);
    }
}

