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

package org.episteme.natural.engineering.fluids;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;
import java.util.Collection;



/**
 * Represents the regime of a fluid flow (Laminar, Transitional, Turbulent).
 * This class is an extensible enumeration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public final class FlowRegime extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    /**
     * Laminar flow regime (Re < 2300).
     */
    public static final FlowRegime LAMINAR = new FlowRegime("LAMINAR", true);

    /**
     * Transitional flow regime (2300 <= Re < 4000).
     */
    public static final FlowRegime TRANSITIONAL = new FlowRegime("TRANSITIONAL", true);

    /**
     * Turbulent flow regime (Re >= 4000).
     */
    public static final FlowRegime TURBULENT = new FlowRegime("TURBULENT", true);

    static {
        EnumRegistry.register(FlowRegime.class, LAMINAR);
        EnumRegistry.register(FlowRegime.class, TRANSITIONAL);
        EnumRegistry.register(FlowRegime.class, TURBULENT);
    }

    /**
     * Default constructor for new instances.
     *
     * @param name the name of the flow regime.
     */
    public FlowRegime(String name) {
        super(name);
        EnumRegistry.register(FlowRegime.class, this);
    }

    /**
     * Private constructor for built-in constants.
     *
     * @param name    the name of the flow regime.
     * @param builtIn whether this is a built-in constant.
     */
    private FlowRegime(String name, boolean builtIn) {
        super(name);
        /* builtIn ignored */
    }

    /**
     * Returns the flow regime with the specified name.
     *
     * @param name the name of the flow regime.
     * @return the corresponding flow regime, or null if not found.
     */
    public static FlowRegime valueOf(String name) {
        return EnumRegistry.valueOf(FlowRegime.class, name);
    }

    /**
     * Returns all registered flow regimes.
     *
     * @return a collection of all flow regimes.
     */
    public static Collection<FlowRegime> values() {
        return EnumRegistry.values(FlowRegime.class);
    }
}

