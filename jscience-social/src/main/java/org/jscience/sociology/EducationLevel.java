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


import java.util.List;
import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents an extensible set of education levels, facilitating support for various national systems.
 * Standard levels are based on the International Standard Classification of Education (ISCED).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
@Persistent
public final class EducationLevel extends ExtensibleEnum implements org.jscience.util.Named {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<EducationLevel> REGISTRY = EnumRegistry.getRegistry(EducationLevel.class);

    // Built-in levels (ISCED based)
    public static final EducationLevel NONE = new EducationLevel("NONE", "No formal education", true);
    public static final EducationLevel PRIMARY = new EducationLevel("PRIMARY", "Primary education", true);
    public static final EducationLevel LOWER_SECONDARY = new EducationLevel("LOWER_SECONDARY", "Lower secondary", true);
    public static final EducationLevel UPPER_SECONDARY = new EducationLevel("UPPER_SECONDARY", "Upper secondary", true);
    public static final EducationLevel POST_SECONDARY = new EducationLevel("POST_SECONDARY", "Post-secondary non-tertiary", true);
    public static final EducationLevel SHORT_CYCLE = new EducationLevel("SHORT_CYCLE", "Short-cycle tertiary", true);
    public static final EducationLevel BACHELOR = new EducationLevel("BACHELOR", "Bachelor's or equivalent", true);
    public static final EducationLevel MASTER = new EducationLevel("MASTER", "Master's or equivalent", true);
    public static final EducationLevel DOCTORATE = new EducationLevel("DOCTORATE", "Doctoral or equivalent", true);

    @Attribute
    private final String customDescription;
    
    private final boolean builtIn;

    private EducationLevel(String name, String description, boolean builtIn) {
        super(name);
        this.customDescription = description;
        this.builtIn = builtIn;
        REGISTRY.register(this);
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
        return new EducationLevel(name, description, false);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String description() {
        return customDescription;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static EducationLevel valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static List<EducationLevel> values() {
        return REGISTRY.values();
    }
}
