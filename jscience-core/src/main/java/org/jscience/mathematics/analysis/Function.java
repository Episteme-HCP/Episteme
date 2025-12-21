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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a mathematical function from a domain D to a codomain C.
 * <p>
 * A function is a relation where for every x ∈ D, there is exactly one y ∈ C
 * such that (x, y) is in the relation.
 * </p>
 * <p>
 * This interface extends {@link Relation} and
 * {@link java.util.function.Function}
 * for maximum interoperability.
 * </p>
 * 
 * @param <D> the type of the domain (input)
 * @param <C> the type of the codomain (output)
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Function<D, C> extends Relation<D, C>, java.util.function.Function<D, C> {

    /**
     * Evaluates this function at the given point.
     * 
     * @param x the input point
     * @return the value of the function at x
     */
    C evaluate(D x);

    /**
     * Java Function interface implementation - delegates to evaluate().
     * 
     * @param x input value
     * @return f(x)
     */
    @Override
    default C apply(D x) {
        return evaluate(x);
    }

    /**
     * Checks if the relation contains the pair (input, output).
     * For a function, this checks if f(input) == output.
     */
    @Override
    default boolean contains(D input, C output) {
        C result = evaluate(input);
        return result == null ? output == null : result.equals(output);
    }

    /**
     * Returns the composition of this function with another function.
     * (f o g)(x) = f(g(x))
     * 
     * @param <V>   the domain of the inner function
     * @param inner the inner function g
     * @return the composite function f o g
     */
    default <V> Function<V, C> compose(Function<V, D> inner) {
        return new Function<V, C>() {
            @Override
            public C evaluate(V x) {
                return Function.this.evaluate(inner.evaluate(x));
            }

            @Override
            public String getDomain() {
                return inner.getDomain();
            }

            @Override
            public String getCodomain() {
                return Function.this.getCodomain();
            }
        };
    }

    /**
     * Composes this function with another (from java.util.function.Function).
     * 
     * @param <V>   result type
     * @param after function to apply after this
     * @return composed function
     */
    @Override
    default <V> Function<D, V> andThen(java.util.function.Function<? super C, ? extends V> after) {
        return new Function<D, V>() {
            @Override
            public V evaluate(D x) {
                return after.apply(Function.this.evaluate(x));
            }

            @Override
            public String getDomain() {
                return Function.this.getDomain();
            }
        };
    }

    /**
     * Sets the compute backend for this function (e.g., enable GPU).
     * 
     * @param backend the compute backend
     */
    default void setBackend(org.jscience.technical.backend.ComputeBackend backend) {
        // Default implementation does nothing (stateless functions)
    }

    /**
     * Returns the currently set compute backend.
     * 
     * @return the compute backend, or null if none is explicitly set or supported.
     */
    default org.jscience.technical.backend.ComputeBackend getBackend() {
        return null;
    }

    /**
     * Evaluates the function for a batch of input values.
     * 
     * @param inputs a collection of input values
     * @return a list of corresponding output values
     */
    default List<C> evaluate(Collection<D> inputs) {
        List<C> results = new ArrayList<>(inputs.size());
        for (D input : inputs) {
            results.add(evaluate(input));
        }
        return results;
    }

    /**
     * Indicates if this function is continuous.
     * 
     * @return true if continuous, false otherwise
     */
    default boolean isContinuous() {
        return false;
    }

    /**
     * Indicates if this function is differentiable.
     * 
     * @return true if differentiable, false otherwise
     */
    default boolean isDifferentiable() {
        return false;
    }
}