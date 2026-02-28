/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sociology;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;

/**
 * An extensible enumeration for social SociologicalGroup classifications.
 * Modernized to extend ExtensibleEnum.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@Persistent
public final class GroupKind extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final GroupKind SociologicalFamily = new GroupKind("SociologicalFamily", true);
    public static final GroupKind COMMUNITY = new GroupKind("COMMUNITY", true);
    public static final GroupKind ORGANIZATION = new GroupKind("ORGANIZATION", true);
    public static final GroupKind NATION = new GroupKind("NATION", true);
    public static final GroupKind TRIBE = new GroupKind("TRIBE", true);
    public static final GroupKind TEAM = new GroupKind("TEAM", true);
    public static final GroupKind CLASS = new GroupKind("CLASS", true);
    public static final GroupKind NETWORK = new GroupKind("NETWORK", true);
    public static final GroupKind OTHER = new GroupKind("OTHER", true);
    public static final GroupKind UNKNOWN = new GroupKind("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(GroupKind.class, SociologicalFamily);
        EnumRegistry.register(GroupKind.class, COMMUNITY);
        EnumRegistry.register(GroupKind.class, ORGANIZATION);
        EnumRegistry.register(GroupKind.class, NATION);
        EnumRegistry.register(GroupKind.class, TRIBE);
        EnumRegistry.register(GroupKind.class, TEAM);
        EnumRegistry.register(GroupKind.class, CLASS);
        EnumRegistry.register(GroupKind.class, NETWORK);
        EnumRegistry.register(GroupKind.class, OTHER);
        EnumRegistry.register(GroupKind.class, UNKNOWN);
    }

    public GroupKind(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(GroupKind.class, this);
    }

    private GroupKind(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static GroupKind valueOf(String name) {
        return EnumRegistry.getRegistry(GroupKind.class).valueOfRequired(name);
    }
    
    public static GroupKind[] values() {
        return EnumRegistry.getRegistry(GroupKind.class).values().toArray(new GroupKind[0]);
    }
}

