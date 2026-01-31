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

/**
 * An extensible enumeration for resource classifications.
 * Supports standard categories like RAW_MATERIAL, PRODUCT, etc., 
 * but allows for dynamic extensions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class ResourceKind extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<ResourceKind> REGISTRY = EnumRegistry.getRegistry(ResourceKind.class);

    public static final ResourceKind RAW_MATERIAL = new ResourceKind("RAW_MATERIAL", true);
    public static final ResourceKind PRODUCT = new ResourceKind("PRODUCT", true);
    public static final ResourceKind EQUIPMENT = new ResourceKind("EQUIPMENT", true);
    public static final ResourceKind FACILITY = new ResourceKind("FACILITY", true);
    public static final ResourceKind FUNDING = new ResourceKind("FUNDING", true);
    public static final ResourceKind ENERGY = new ResourceKind("ENERGY", true);
    public static final ResourceKind INFORMATION = new ResourceKind("INFORMATION", true);
    public static final ResourceKind LABOR = new ResourceKind("LABOR", true);
    public static final ResourceKind OTHER = new ResourceKind("OTHER", true);
    public static final ResourceKind UNKNOWN = new ResourceKind("UNKNOWN", true);

    private final boolean builtIn;
    private String description;

    private ResourceKind(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    public static ResourceKind valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static ResourceKind valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }

    @Override
    public String description() {
        return description != null ? description : name();
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

