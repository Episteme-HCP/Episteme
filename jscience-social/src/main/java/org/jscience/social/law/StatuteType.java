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

package org.jscience.social.law;

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;

/**
 * An extensible enumeration for statute types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StatuteType extends ExtensibleEnum {

    public static final StatuteType CONSTITUTION = new StatuteType("CONSTITUTION");
    public static final StatuteType FEDERAL_LAW = new StatuteType("FEDERAL_LAW");
    public static final StatuteType STATE_LAW = new StatuteType("STATE_LAW");
    public static final StatuteType REGULATION = new StatuteType("REGULATION");
    public static final StatuteType ORDINANCE = new StatuteType("ORDINANCE");
    public static final StatuteType TREATY = new StatuteType("TREATY");
    public static final StatuteType DIRECTIVE = new StatuteType("DIRECTIVE");
    public static final StatuteType DECREE = new StatuteType("DECREE");
    public static final StatuteType ACT = new StatuteType("ACT");
    
    public static final StatuteType OTHER = new StatuteType("OTHER");

    public StatuteType(String name) {
        super(name);
        EnumRegistry.register(StatuteType.class, this);
    }
    
    public static StatuteType valueOf(String name) {
        return EnumRegistry.getRegistry(StatuteType.class).valueOf(name);
    }
}

