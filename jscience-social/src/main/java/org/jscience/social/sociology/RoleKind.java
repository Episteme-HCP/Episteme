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
 * Categorical classification of social roles.
 * Modernized to extend ExtensibleEnum.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class RoleKind extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final RoleKind CLIENT = new RoleKind("CLIENT", "Primary role of receiving a service", true);
    public static final RoleKind SERVER = new RoleKind("SERVER", "Primary role of providing a service", true);
    public static final RoleKind SUPERVISOR = new RoleKind("SUPERVISOR", "Role of monitoring or managing others", true);
    public static final RoleKind OBSERVER = new RoleKind("OBSERVER", "Role of passive observation", true);
    
    public static final RoleKind OTHER = new RoleKind("OTHER", "Other unclassified role", true);
    public static final RoleKind UNKNOWN = new RoleKind("UNKNOWN", "Unknown role", true);

    private final String description;
    private final boolean builtIn;

    static {
        EnumRegistry.register(RoleKind.class, CLIENT);
        EnumRegistry.register(RoleKind.class, SERVER);
        EnumRegistry.register(RoleKind.class, SUPERVISOR);
        EnumRegistry.register(RoleKind.class, OBSERVER);
        EnumRegistry.register(RoleKind.class, OTHER);
        EnumRegistry.register(RoleKind.class, UNKNOWN);
    }

    public RoleKind(String name, String description) {
        super(name);
        this.description = description;
        this.builtIn = false;
        EnumRegistry.register(RoleKind.class, this);
    }

    private RoleKind(String name, String description, boolean builtIn) {
        super(name);
        this.description = description;
        this.builtIn = builtIn;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static RoleKind valueOf(String name) {
        return EnumRegistry.getRegistry(RoleKind.class).valueOfRequired(name);
    }
    
    public static RoleKind[] values() {
        return EnumRegistry.getRegistry(RoleKind.class).values().toArray(new RoleKind[0]);
    }

    /**
     * Converts an integer to a RoleKind for backward compatibility.
     * @deprecated Use RoleKind constants directly
     */
    @Deprecated
    public static RoleKind fromInt(int kind) {
        return switch (kind) {
            case 0 -> CLIENT;
            case 1 -> SERVER;
            case 2 -> SUPERVISOR;
            case 3 -> OBSERVER;
            default -> OTHER;
        };
    }
}

