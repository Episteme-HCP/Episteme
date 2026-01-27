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

package org.jscience.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class for extensible enumeration pattern.
 * <p>
 * Unlike Java enums which are closed sets, ExtensibleEnum allows
 * user-defined values to be added at runtime.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class ExtensibleEnum implements Serializable, Comparable<ExtensibleEnum> {

    private static final long serialVersionUID = 1L;

    private final String name;
    private transient int ordinal; // Assigned by registry

    protected ExtensibleEnum(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.ordinal = -1; // Will be set by registry
    }

    /**
     * Returns the name of this enum constant.
     */
    public final String name() {
        return name;
    }

    /**
     * Returns the ordinal of this enum constant.
     */
    public final int ordinal() {
        return ordinal;
    }
    
    /**
     * Internal method for registry to set ordinal.
     */
    void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * Returns a description of this enum constant.
     */
    public String description() {
        return name();
    }

    /**
     * Checks if this is a built-in (predefined) value.
     * Default implementation returns true to mimic standard Enum behavior 
     * unless explicitly overridden.
     */
    public boolean isBuiltIn() {
        return true;
    }

    @Override
    public final String toString() {
        return name;
    }

    @Override
    public final boolean equals(Object other) {
        return this == other;
    }

    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public final int compareTo(ExtensibleEnum o) {
        if (this.getClass() != o.getClass() && // optimization
            this.getDeclaringClass() != o.getDeclaringClass())
            throw new ClassCastException();
        return this.ordinal - o.ordinal;
    }
    

    public final Class<? extends ExtensibleEnum> getDeclaringClass() {
        return this.getClass();
    }
}
