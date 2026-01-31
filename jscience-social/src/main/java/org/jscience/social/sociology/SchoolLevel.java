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
 * Extensible enumeration for school levels (tiers).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class SchoolLevel extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final SchoolLevel PRESCHOOL = new SchoolLevel("PRESCHOOL", true);
    public static final SchoolLevel ELEMENTARY = new SchoolLevel("ELEMENTARY", true);
    public static final SchoolLevel MIDDLE = new SchoolLevel("MIDDLE", true);
    public static final SchoolLevel HIGH = new SchoolLevel("HIGH", true);
    public static final SchoolLevel UNDERGRADUATE = new SchoolLevel("UNDERGRADUATE", true);
    public static final SchoolLevel GRADUATE = new SchoolLevel("GRADUATE", true);
    public static final SchoolLevel DOCTORAL = new SchoolLevel("DOCTORAL", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(SchoolLevel.class, PRESCHOOL);
        EnumRegistry.register(SchoolLevel.class, ELEMENTARY);
        EnumRegistry.register(SchoolLevel.class, MIDDLE);
        EnumRegistry.register(SchoolLevel.class, HIGH);
        EnumRegistry.register(SchoolLevel.class, UNDERGRADUATE);
        EnumRegistry.register(SchoolLevel.class, GRADUATE);
        EnumRegistry.register(SchoolLevel.class, DOCTORAL);
    }

    public SchoolLevel(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(SchoolLevel.class, this);
    }
    
    private SchoolLevel(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static SchoolLevel valueOf(String name) {
        return EnumRegistry.getRegistry(SchoolLevel.class).valueOfRequired(name);
    }
    
    public static SchoolLevel[] values() {
        return EnumRegistry.getRegistry(SchoolLevel.class).values().toArray(new SchoolLevel[0]);
    }
}

