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

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;

/**
 * Categorical classification of organizational sectors.
 * Modernized to extend ExtensibleEnum.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class OrganizationSector extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final OrganizationSector PUBLIC = new OrganizationSector("PUBLIC", true);
    public static final OrganizationSector PRIVATE = new OrganizationSector("PRIVATE", true);
    public static final OrganizationSector NON_PROFIT = new OrganizationSector("NON_PROFIT", true);
    public static final OrganizationSector GOVERNMENT = new OrganizationSector("GOVERNMENT", true);
    public static final OrganizationSector ACADEMIC = new OrganizationSector("ACADEMIC", true);
    public static final OrganizationSector MILITARY = new OrganizationSector("MILITARY", true);
    
    public static final OrganizationSector OTHER = new OrganizationSector("OTHER", true);
    public static final OrganizationSector UNKNOWN = new OrganizationSector("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(OrganizationSector.class, PUBLIC);
        EnumRegistry.register(OrganizationSector.class, PRIVATE);
        EnumRegistry.register(OrganizationSector.class, NON_PROFIT);
        EnumRegistry.register(OrganizationSector.class, GOVERNMENT);
        EnumRegistry.register(OrganizationSector.class, ACADEMIC);
        EnumRegistry.register(OrganizationSector.class, MILITARY);
        EnumRegistry.register(OrganizationSector.class, OTHER);
        EnumRegistry.register(OrganizationSector.class, UNKNOWN);
    }

    public OrganizationSector(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(OrganizationSector.class, this);
    }

    private OrganizationSector(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static OrganizationSector valueOf(String name) {
        return EnumRegistry.getRegistry(OrganizationSector.class).valueOfRequired(name);
    }
    
    public static OrganizationSector[] values() {
        return EnumRegistry.getRegistry(OrganizationSector.class).values().toArray(new OrganizationSector[0]);
    }
}

