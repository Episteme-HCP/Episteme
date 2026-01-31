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

package org.jscience.social.sociology;


import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;

/**
 * Extensible enumeration for adopter categories in innovation diffusion models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class AdoptionStatus extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<AdoptionStatus> REGISTRY = EnumRegistry.getRegistry(AdoptionStatus.class);

    public static final AdoptionStatus INNOVATOR = new AdoptionStatus("INNOVATOR", true);
    public static final AdoptionStatus EARLY_ADOPTER = new AdoptionStatus("EARLY_ADOPTER", true);
    public static final AdoptionStatus EARLY_MAJORITY = new AdoptionStatus("EARLY_MAJORITY", true);
    public static final AdoptionStatus LATE_MAJORITY = new AdoptionStatus("LATE_MAJORITY", true);
    public static final AdoptionStatus LAGGARD = new AdoptionStatus("LAGGARD", true);
    public static final AdoptionStatus OTHER = new AdoptionStatus("OTHER", true);
    public static final AdoptionStatus UNKNOWN = new AdoptionStatus("UNKNOWN", true);

    private final boolean builtIn;

    private AdoptionStatus(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    public static AdoptionStatus valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static AdoptionStatus valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
}

