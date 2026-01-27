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

package org.jscience.medicine;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for major biological organ systems.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class OrganSystem extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final OrganSystem CIRCULATORY = new OrganSystem("CIRCULATORY", true);
    public static final OrganSystem DIGESTIVE = new OrganSystem("DIGESTIVE", true);
    public static final OrganSystem ENDOCRINE = new OrganSystem("ENDOCRINE", true);
    public static final OrganSystem INTEGUMENTARY = new OrganSystem("INTEGUMENTARY", true);
    public static final OrganSystem IMMUNE = new OrganSystem("IMMUNE", true);
    public static final OrganSystem LYMPHATIC = new OrganSystem("LYMPHATIC", true);
    public static final OrganSystem MUSCULAR = new OrganSystem("MUSCULAR", true);
    public static final OrganSystem NERVOUS = new OrganSystem("NERVOUS", true);
    public static final OrganSystem REPRODUCTIVE = new OrganSystem("REPRODUCTIVE", true);
    public static final OrganSystem RESPIRATORY = new OrganSystem("RESPIRATORY", true);
    public static final OrganSystem SKELETAL = new OrganSystem("SKELETAL", true);
    public static final OrganSystem URINARY = new OrganSystem("URINARY", true);
    public static final OrganSystem UNKNOWN = new OrganSystem("UNKNOWN", true);

    static {
        EnumRegistry.register(OrganSystem.class, CIRCULATORY);
        EnumRegistry.register(OrganSystem.class, DIGESTIVE);
        EnumRegistry.register(OrganSystem.class, ENDOCRINE);
        EnumRegistry.register(OrganSystem.class, INTEGUMENTARY);
        EnumRegistry.register(OrganSystem.class, IMMUNE);
        EnumRegistry.register(OrganSystem.class, LYMPHATIC);
        EnumRegistry.register(OrganSystem.class, MUSCULAR);
        EnumRegistry.register(OrganSystem.class, NERVOUS);
        EnumRegistry.register(OrganSystem.class, REPRODUCTIVE);
        EnumRegistry.register(OrganSystem.class, RESPIRATORY);
        EnumRegistry.register(OrganSystem.class, SKELETAL);
        EnumRegistry.register(OrganSystem.class, URINARY);
        EnumRegistry.register(OrganSystem.class, UNKNOWN);
    }

    private final boolean builtIn;

    public OrganSystem(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(OrganSystem.class, this);
    }
    
    private OrganSystem(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static OrganSystem valueOf(String name) {
        return EnumRegistry.getRegistry(OrganSystem.class).valueOfRequired(name);
    }
    
    public static OrganSystem[] values() {
        return EnumRegistry.getRegistry(OrganSystem.class).values().toArray(new OrganSystem[0]);
    }
}
