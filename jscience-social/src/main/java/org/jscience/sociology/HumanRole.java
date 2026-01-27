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

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * An extensible enumeration for human historical roles and occupations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanRole extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<HumanRole> REGISTRY = new EnumRegistry<>();

    public static final HumanRole RULER = new HumanRole("RULER", true);
    public static final HumanRole MILITARY = new HumanRole("MILITARY", true);
    public static final HumanRole RELIGIOUS = new HumanRole("RELIGIOUS", true);
    public static final HumanRole PHILOSOPHER = new HumanRole("PHILOSOPHER", true);
    public static final HumanRole SCIENTIST = new HumanRole("SCIENTIST", true);
    public static final HumanRole ARTIST = new HumanRole("ARTIST", true);
    public static final HumanRole EXPLORER = new HumanRole("EXPLORER", true);
    public static final HumanRole REVOLUTIONARY = new HumanRole("REVOLUTIONARY", true);
    public static final HumanRole REFORMER = new HumanRole("REFORMER", true);
    public static final HumanRole INVENTOR = new HumanRole("INVENTOR", true);
    public static final HumanRole OTHER = new HumanRole("OTHER", true);

    private HumanRole(String name, boolean builtIn) {
        super(name);
        /* builtIn ignored */
        REGISTRY.register(this);
    }

    public static HumanRole valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static HumanRole valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }

}
