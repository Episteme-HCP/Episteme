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

package org.jscience.engineering;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible types of system or entity transformations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class TransformationType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final TransformationType DISPLACEMENT = new TransformationType("DISPLACEMENT", true);
    public static final TransformationType SIMPLIFICATION = new TransformationType("SIMPLIFICATION", true);
    public static final TransformationType COMPLEXIFICATION = new TransformationType("COMPLEXIFICATION", true);
    public static final TransformationType MODIFICATION = new TransformationType("MODIFICATION", true);
    public static final TransformationType TRANSFORMATION = new TransformationType("TRANSFORMATION", true);
    public static final TransformationType UNKNOWN = new TransformationType("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(TransformationType.class, DISPLACEMENT);
        EnumRegistry.register(TransformationType.class, SIMPLIFICATION);
        EnumRegistry.register(TransformationType.class, COMPLEXIFICATION);
        EnumRegistry.register(TransformationType.class, MODIFICATION);
        EnumRegistry.register(TransformationType.class, TRANSFORMATION);
        EnumRegistry.register(TransformationType.class, UNKNOWN);
    }

    /**
     * Creates a new user-defined transformation type.
     * @param name Name of the transformation type.
     */
    public TransformationType(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(TransformationType.class, this);
    }

    private TransformationType(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static TransformationType valueOf(String name) {
        return EnumRegistry.getRegistry(TransformationType.class).valueOfRequired(name);
    }

    public static TransformationType[] values() {
        return EnumRegistry.getRegistry(TransformationType.class).values().toArray(new TransformationType[0]);
    }
}
