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

import java.util.List;
import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;

/**
 * Categories of religious systems based on theological structure.
 * Modernized to follow the standard ExtensibleEnum pattern.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.1
 */
@Persistent
public final class ReligionType extends ExtensibleEnum {

    private static final long serialVersionUID = 3L;

    public static final EnumRegistry<ReligionType> REGISTRY = EnumRegistry.getRegistry(ReligionType.class);

    public static final ReligionType MONOTHEISTIC = new ReligionType("MONOTHEISTIC", true);
    public static final ReligionType POLYTHEISTIC = new ReligionType("POLYTHEISTIC", true);
    public static final ReligionType PANTHEISTIC = new ReligionType("PANTHEISTIC", true);
    public static final ReligionType ATHEISTIC = new ReligionType("ATHEISTIC", true);
    public static final ReligionType ANIMISTIC = new ReligionType("ANIMISTIC", true);
    public static final ReligionType SHAMANISTIC = new ReligionType("SHAMANISTIC", true);
    public static final ReligionType PHILOSOPHICAL = new ReligionType("PHILOSOPHICAL", true);
    
    public static final ReligionType OTHER = new ReligionType("OTHER", true);
    public static final ReligionType UNKNOWN = new ReligionType("UNKNOWN", true);

    private final boolean builtIn;

    public ReligionType(String name) {
        this(name, false);
    }

    private ReligionType(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static ReligionType valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
    
    public static List<ReligionType> values() {
        return REGISTRY.values();
    }
}

