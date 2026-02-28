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

package org.episteme.core.mathematics.ml.neural;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;

/**
 * Extensible categorization of neural network activation functions.
 * <p>
 * This class provides a standardized way to reference activation functions
 * across different layers and models.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class ActivationFunction extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<ActivationFunction> REGISTRY = EnumRegistry.getRegistry(ActivationFunction.class);

    public static final ActivationFunction SIGMOID = new ActivationFunction("SIGMOID", true);
    public static final ActivationFunction TANH = new ActivationFunction("TANH", true);
    public static final ActivationFunction RELU = new ActivationFunction("RELU", true);
    public static final ActivationFunction LEAKY_RELU = new ActivationFunction("LEAKY_RELU", true);
    public static final ActivationFunction SOFTMAX = new ActivationFunction("SOFTMAX", true);
    public static final ActivationFunction IDENTITY = new ActivationFunction("IDENTITY", true);

    private final boolean builtIn;

    public ActivationFunction(String name) {
        this(name, false);
    }

    private ActivationFunction(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static ActivationFunction valueOf(String name) {
        return REGISTRY.valueOf(name.toUpperCase());
    }

    public static ActivationFunction[] values() {
        return REGISTRY.values().toArray(new ActivationFunction[0]);
    }
    
    /**
     * Normalizes a name used for lookup.
     * 
     * @param name The name to normalize
     * @return Normalized name
     */
    public static String normalizeName(String name) {
        if (name == null) return null;
        return name.trim().toUpperCase().replace("-", "_").replace(" ", "_");
    }

    /**
     * Gets or registers an activation function by name.
     * 
     * @param name Name of the activation function
     * @return The activation function instance
     */
    public static ActivationFunction of(String name) {
        String norm = normalizeName(name);
        ActivationFunction af = valueOf(norm);
        if (af == null) {
            af = new ActivationFunction(norm);
        }
        return af;
    }
}
