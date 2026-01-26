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
 * Represents an extensible set of education levels, facilitating support for various national systems.
 * Standard levels are based on the International Standard Classification of Education (ISCED).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public final class EducationLevel implements ExtensibleEnum<EducationLevel>, org.jscience.util.Named, Serializable {

    private static final long serialVersionUID = 1L;

    private static final EnumRegistry<EducationLevel> REGISTRY = new EnumRegistry<>();

    // Built-in levels (ISCED based)
    public static final EducationLevel NONE = register("NONE", "No formal education", 0, true);
    public static final EducationLevel PRIMARY = register("PRIMARY", "Primary education", 1, true);
    public static final EducationLevel LOWER_SECONDARY = register("LOWER_SECONDARY", "Lower secondary", 2, true);
    public static final EducationLevel UPPER_SECONDARY = register("UPPER_SECONDARY", "Upper secondary", 3, true);
    public static final EducationLevel POST_SECONDARY = register("POST_SECONDARY", "Post-secondary non-tertiary", 4, true);
    public static final EducationLevel SHORT_CYCLE = register("SHORT_CYCLE", "Short-cycle tertiary", 5, true);
    public static final EducationLevel BACHELOR = register("BACHELOR", "Bachelor's or equivalent", 6, true);
    public static final EducationLevel MASTER = register("MASTER", "Master's or equivalent", 7, true);
    public static final EducationLevel DOCTORATE = register("DOCTORATE", "Doctoral or equivalent", 8, true);

    @Id
    @Attribute
    private final String name;
    
    @Attribute
    private final String description;
    
    @Attribute
    private final int ordinal;
    
    @Attribute
    private final boolean builtIn;

    private EducationLevel(String name, String description, int ordinal, boolean builtIn) {
        this.name = name;
        this.description = description;
        this.ordinal = ordinal;
        this.builtIn = builtIn;
    }

    private static EducationLevel register(String name, String description, int ordinal, boolean builtIn) {
        EducationLevel level = new EducationLevel(name, description, ordinal, builtIn);
        REGISTRY.register(level);
        return level;
    }

    /**
     * Registers a custom education level.
     *
     * @param name        unique name for the level
     * @param description descriptive level text
     * @return the registered EducationLevel instance
     */
    public static EducationLevel registerCustom(String name, String description) {
        EducationLevel existing = REGISTRY.valueOf(name);
        if (existing != null) {
            return existing;
        }
        return register(name, description, REGISTRY.size(), false);
    }

    /**
     * Retrieves an education level by its name.
     * @param name the name to look up
     * @return the matching EducationLevel, or null if not found
     */
    public static EducationLevel valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    /**
     * Returns all registered education levels.
     * @return a list of all levels
     */
    public static List<EducationLevel> values() {
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
     * Returns the description of the education level.
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
     * Returns whether this level is a built-in ISCED standard level.
     * @return true if built-in
     */
    public boolean isBuiltIn() {
        return builtIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EducationLevel)) return false;
        EducationLevel that = (EducationLevel) o;
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
