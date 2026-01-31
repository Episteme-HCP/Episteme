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

package org.jscience.social.economics;

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;

/**
 * Extensible enumeration for Industry Category.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class IndustryCategory extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final IndustryCategory TRANSPORT = new IndustryCategory("TRANSPORT", true);
    public static final IndustryCategory HYGIENE = new IndustryCategory("HYGIENE", true);
    public static final IndustryCategory ALIMENTATION = new IndustryCategory("ALIMENTATION", true);
    public static final IndustryCategory HABITAT = new IndustryCategory("HABITAT", true);
    public static final IndustryCategory TECHNOLOGY = new IndustryCategory("TECHNOLOGY", true);
    public static final IndustryCategory ENERGY = new IndustryCategory("ENERGY", true);
    public static final IndustryCategory SOCIETY = new IndustryCategory("SOCIETY", true);
    public static final IndustryCategory FINANCE = new IndustryCategory("FINANCE", true);
    public static final IndustryCategory EDUCATION = new IndustryCategory("EDUCATION", true);
    public static final IndustryCategory HEALTHCARE = new IndustryCategory("HEALTHCARE", true);
    public static final IndustryCategory OTHER = new IndustryCategory("OTHER", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(IndustryCategory.class, TRANSPORT);
        EnumRegistry.register(IndustryCategory.class, HYGIENE);
        EnumRegistry.register(IndustryCategory.class, ALIMENTATION);
        EnumRegistry.register(IndustryCategory.class, HABITAT);
        EnumRegistry.register(IndustryCategory.class, TECHNOLOGY);
        EnumRegistry.register(IndustryCategory.class, ENERGY);
        EnumRegistry.register(IndustryCategory.class, SOCIETY);
        EnumRegistry.register(IndustryCategory.class, FINANCE);
        EnumRegistry.register(IndustryCategory.class, EDUCATION);
        EnumRegistry.register(IndustryCategory.class, HEALTHCARE);
        EnumRegistry.register(IndustryCategory.class, OTHER);
    }

    public IndustryCategory(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(IndustryCategory.class, this);
    }

    private IndustryCategory(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static IndustryCategory valueOf(String name) {
        return EnumRegistry.getRegistry(IndustryCategory.class).valueOfRequired(name);
    }
    
    public static IndustryCategory[] values() {
        return EnumRegistry.getRegistry(IndustryCategory.class).values().toArray(new IndustryCategory[0]);
    }
}

