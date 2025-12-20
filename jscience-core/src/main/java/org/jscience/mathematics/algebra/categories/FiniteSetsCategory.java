/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.algebra.categories;

import java.util.function.Function;
import org.jscience.mathematics.structures.categories.Category;
import org.jscience.mathematics.structures.sets.Set;

/**
 * Represents the Category of Finite Sets (FinSet).
 * <p>
 * Objects are finite sets.
 * Morphisms are functions between sets.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FiniteSetsCategory implements Category<java.util.Set<?>, Function<Object, Object>> {

    private static final FiniteSetsCategory INSTANCE = new FiniteSetsCategory();

    public static FiniteSetsCategory getInstance() {
        return INSTANCE;
    }

    private FiniteSetsCategory() {
    }

    @Override
    public Function<Object, Object> compose(Function<Object, Object> f, Function<Object, Object> g) {
        // f: B -> C, g: A -> B => f o g: A -> C
        return f.compose(g);
    }

    @Override
    public Function<Object, Object> identity(java.util.Set<?> object) {
        return Function.identity();
    }

    @Override
    public java.util.Set<?> domain(Function<Object, Object> morphism) {
        // Standard functions in Java don't carry domain information
        // Domain must be tracked externally or use a custom MorphismWithDomain class
        throw new UnsupportedOperationException(
                "Java Function does not carry domain information. Use typed morphisms.");
    }

    @Override
    public java.util.Set<?> codomain(Function<Object, Object> morphism) {
        // Standard functions in Java don't carry codomain information
        throw new UnsupportedOperationException(
                "Java Function does not carry codomain information. Use typed morphisms.");
    }

    @Override
    public Set<Function<Object, Object>> hom(java.util.Set<?> source,
            java.util.Set<?> target) {
        // Generating hom-set for finite sets is exponential in size
        // and impractical for general use. Would need specialized FiniteSet class.
        throw new UnsupportedOperationException(
                "Generating hom-set for finite sets requires specialized FiniteSet type.");
    }
}
