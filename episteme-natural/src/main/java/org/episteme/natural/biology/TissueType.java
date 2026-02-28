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

package org.episteme.natural.biology;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;


/**
 * Extensible categorization of biological tissues (e.g. Muscle, Nerve, Epithelial).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class TissueType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<TissueType> REGISTRY = EnumRegistry.getRegistry(TissueType.class);

    public static final TissueType EPITHELIAL = new TissueType("EPITHELIAL", true);
    public static final TissueType CONNECTIVE = new TissueType("CONNECTIVE", true);
    public static final TissueType MUSCLE = new TissueType("MUSCLE", true);
    public static final TissueType NERVOUS = new TissueType("NERVOUS", true);
    public static final TissueType XYLEM = new TissueType("XYLEM", true);
    public static final TissueType PHLOEM = new TissueType("PHLOEM", true);
    public static final TissueType PARENCHYMA = new TissueType("PARENCHYMA", true);
    public static final TissueType UNKNOWN = new TissueType("UNKNOWN", true);

    private final boolean builtIn;

    public TissueType(String name) {
        this(name, false);
    }

    private TissueType(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static TissueType valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}

