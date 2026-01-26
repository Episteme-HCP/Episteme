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

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents an extensible set of occupation types for individuals.
 * Allows runtime registration of custom professions beyond the built-in defaults.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public final class Occupation implements ExtensibleEnum<Occupation>, org.jscience.util.Named, Serializable {

    private static final long serialVersionUID = 1L;

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

    @Id
    @Attribute
    private final String name;
    
    @Attribute
    private final String description;
    
    @Attribute
    private final int ordinal;
    
    @Attribute
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
     *
     * @param name        unique name for the occupation
     * @param description descriptive text for the profession
     * @return the registered Occupation instance
     */
    public static Occupation registerCustom(String name, String description) {
        Occupation existing = REGISTRY.valueOf(name);
        if (existing != null) {
            return existing;
        }
        return register(name, description, false);
    }

    /**
     * Retrieves an occupation by its name.
     * @param name the name to look up
     * @return the matching Occupation, or null if not found
     */
    public static Occupation valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    /**
     * Returns all registered occupations.
     * @return a list of all occupations
     */
    public static List<Occupation> values() {
        return REGISTRY.values();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the occupation.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    /**
     * Returns whether this occupation is a built-in standard.
     * @return true if built-in
     */
    public boolean isBuiltIn() {
        return builtIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Occupation)) return false;
        Occupation that = (Occupation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
