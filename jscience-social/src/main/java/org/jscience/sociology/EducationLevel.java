/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * Extensible education level type.
 * <p>
 * Unlike a fixed enum, new education levels can be registered at runtime
 * for different national systems.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EducationLevel implements ExtensibleEnum<EducationLevel> {

    private static final EnumRegistry<EducationLevel> REGISTRY = new EnumRegistry<>();

    // Built-in levels (ISCED based)
    public static final EducationLevel NONE = register("NONE", "No formal education", 0, true);
    public static final EducationLevel PRIMARY = register("PRIMARY", "Primary education", 1, true);
    public static final EducationLevel LOWER_SECONDARY = register("LOWER_SECONDARY", "Lower secondary", 2, true);
    public static final EducationLevel UPPER_SECONDARY = register("UPPER_SECONDARY", "Upper secondary", 3, true);
    public static final EducationLevel POST_SECONDARY = register("POST_SECONDARY", "Post-secondary non-tertiary", 4,
            true);
    public static final EducationLevel SHORT_CYCLE = register("SHORT_CYCLE", "Short-cycle tertiary", 5, true);
    public static final EducationLevel BACHELOR = register("BACHELOR", "Bachelor's or equivalent", 6, true);
    public static final EducationLevel MASTER = register("MASTER", "Master's or equivalent", 7, true);
    public static final EducationLevel DOCTORATE = register("DOCTORATE", "Doctoral or equivalent", 8, true);

    private final String name;
    private final String description;
    private final int ordinal;
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
     */
    public static EducationLevel registerCustom(String name, String description) {
        EducationLevel existing = REGISTRY.valueOf(name);
        if (existing != null)
            return existing;
        return register(name, description, REGISTRY.size(), false);
    }

    public static EducationLevel valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static java.util.List<EducationLevel> values() {
        return REGISTRY.values();
    }

    /**
     * Returns the ISCED level (0-8).
     */
    public int getIscedLevel() {
        return ordinal;
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
        return description;
    }
}
