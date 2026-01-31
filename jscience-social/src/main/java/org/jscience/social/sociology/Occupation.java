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
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.Named;

/**
 * Represents an extensible set of occupation types for individuals.
 * Allows runtime registration of custom professions beyond the built-in defaults.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
@Persistent
public final class Occupation extends ExtensibleEnum implements Named {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<Occupation> REGISTRY = EnumRegistry.getRegistry(Occupation.class);

    // Built-in occupations
    public static final Occupation UNEMPLOYED = new Occupation("UNEMPLOYED", "Not employed", true);
    public static final Occupation STUDENT = new Occupation("STUDENT", "Enrolled in education", true);
    public static final Occupation TEACHER = new Occupation("TEACHER", "Education profession", true);
    public static final Occupation ENGINEER = new Occupation("ENGINEER", "Engineering profession", true);
    public static final Occupation DOCTOR = new Occupation("DOCTOR", "Medical profession", true);
    public static final Occupation SCIENTIST = new Occupation("SCIENTIST", "Research profession", true);
    public static final Occupation LAWYER = new Occupation("LAWYER", "Legal profession", true);
    public static final Occupation ARTIST = new Occupation("ARTIST", "Creative profession", true);
    public static final Occupation FARMER = new Occupation("FARMER", "Agriculture profession", true);
    public static final Occupation MERCHANT = new Occupation("MERCHANT", "Commerce profession", true);
    public static final Occupation RETIRED = new Occupation("RETIRED", "No longer working", true);

    @Attribute
    private String description;
    
    private final boolean builtIn;

    private Occupation(String name, String description, boolean builtIn) {
        super(name);
        this.description = description;
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    /**
     * Registers a custom occupation type.
     */
    public static Occupation registerCustom(String name, String description) {
        return new Occupation(name, description, false);
    }

    public static Occupation valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static List<Occupation> values() {
        return REGISTRY.values();
    }

    @Override
    public String getName() {
        return name();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
}

