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
 * Extensible enumeration for Management Level.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class ManagementLevel extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final ManagementLevel OPERATIONAL = new ManagementLevel("OPERATIONAL", true);
    public static final ManagementLevel SUPERVISORY = new ManagementLevel("SUPERVISORY", true);
    public static final ManagementLevel MIDDLE_MANAGEMENT = new ManagementLevel("MIDDLE_MANAGEMENT", true);
    public static final ManagementLevel STRATEGIC = new ManagementLevel("STRATEGIC", true);
    public static final ManagementLevel EXECUTIVE = new ManagementLevel("EXECUTIVE", true);
    public static final ManagementLevel BOARD = new ManagementLevel("BOARD", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(ManagementLevel.class, OPERATIONAL);
        EnumRegistry.register(ManagementLevel.class, SUPERVISORY);
        EnumRegistry.register(ManagementLevel.class, MIDDLE_MANAGEMENT);
        EnumRegistry.register(ManagementLevel.class, STRATEGIC);
        EnumRegistry.register(ManagementLevel.class, EXECUTIVE);
        EnumRegistry.register(ManagementLevel.class, BOARD);
    }

    public ManagementLevel(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(ManagementLevel.class, this);
    }

    private ManagementLevel(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static ManagementLevel valueOf(String name) {
        return EnumRegistry.getRegistry(ManagementLevel.class).valueOfRequired(name);
    }
    
    public static ManagementLevel[] values() {
        return EnumRegistry.getRegistry(ManagementLevel.class).values().toArray(new ManagementLevel[0]);
    }
}

