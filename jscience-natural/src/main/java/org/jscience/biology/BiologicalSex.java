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
 * Extensible enumeration for biological sex.
 * Supports standard sexual dimorphism as well as asexual and hermaphroditic cases.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class BiologicalSex extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final BiologicalSex MALE = new BiologicalSex("MALE", true);
    public static final BiologicalSex FEMALE = new BiologicalSex("FEMALE", true);
    public static final BiologicalSex HERMAPHRODITE = new BiologicalSex("HERMAPHRODITE", true);
    public static final BiologicalSex ASEXUAL = new BiologicalSex("ASEXUAL", true);
    public static final BiologicalSex OTHER = new BiologicalSex("OTHER", true);
    public static final BiologicalSex UNKNOWN = new BiologicalSex("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(BiologicalSex.class, MALE);
        EnumRegistry.register(BiologicalSex.class, FEMALE);
        EnumRegistry.register(BiologicalSex.class, HERMAPHRODITE);
        EnumRegistry.register(BiologicalSex.class, ASEXUAL);
        EnumRegistry.register(BiologicalSex.class, OTHER);
        EnumRegistry.register(BiologicalSex.class, UNKNOWN);
    }

    /**
     * Creates a new user-defined (non-built-in) biological sex.
     * @param name Name of the sex.
     */
    public BiologicalSex(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(BiologicalSex.class, this);
    }

    private BiologicalSex(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static BiologicalSex valueOf(String name) {
        return EnumRegistry.getRegistry(BiologicalSex.class).valueOfRequired(name);
    }

    public static BiologicalSex[] values() {
        return EnumRegistry.getRegistry(BiologicalSex.class).values().toArray(new BiologicalSex[0]);
    }
}
