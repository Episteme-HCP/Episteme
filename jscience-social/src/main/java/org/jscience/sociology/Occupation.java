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

package org.jscience.sociology;

import org.jscience.util.ExtensibleEnum;
import org.jscience.util.EnumRegistry;

/**
 * Extensible occupation type for persons.
 * <p>
 * Unlike a fixed enum, new occupation types can be registered at runtime.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Occupation implements ExtensibleEnum<Occupation> {

    private static final EnumRegistry<Occupation> REGISTRY = new EnumRegistry<>();

    // Built-in occupations
    public static final Occupation UNEMPLOYED = register("UNEMPLOYED", "Not employed", true);
    public static final Occupation STUDENT = register("STUDENT", "Enrolled in education", true);
    public static final Occupation TEACHER = register("TEACHER", "Education profession", true);
    public static final Occupation ENGINEER = register("ENGINEER", "Engineering profession", true);
    public static final Occupation DOCTOR = register("DOCTOR", "Medical profession", true);
    public static final Occupation SCIENTIST = register("SCIENTIST", "Research profession", true);
    public static final Occupation LAWYER = register("LAWYER", "Legal profession", true);
    public static final Occupation ARTIST = register("ARTIST", "Creative profession", true);
    public static final Occupation FARMER = register("FARMER", "Agriculture profession", true);
    public static final Occupation MERCHANT = register("MERCHANT", "Commerce profession", true);
    public static final Occupation RETIRED = register("RETIRED", "No longer working", true);

    private final String name;
    private final String description;
    private final int ordinal;
    private final boolean builtIn;

    private Occupation(String name, String description, int ordinal, boolean builtIn) {
        this.name = name;
        this.description = description;
        this.ordinal = ordinal;
        this.builtIn = builtIn;
    }

    private static Occupation register(String name, String description, boolean builtIn) {
        Occupation occ = new Occupation(name, description, REGISTRY.size(), builtIn);
        REGISTRY.register(occ);
        return occ;
    }

    /**
     * Registers a custom occupation type.
     */
    public static Occupation registerCustom(String name, String description) {
        Occupation existing = REGISTRY.valueOf(name);
        if (existing != null)
            return existing;
        return register(name, description, false);
    }

    public static Occupation valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static java.util.List<Occupation> values() {
        return REGISTRY.values();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    @Override
    public String toString() {
        return name;
    }
}


