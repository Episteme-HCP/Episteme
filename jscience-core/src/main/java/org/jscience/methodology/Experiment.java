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

package org.jscience.methodology;

import org.jscience.util.Named;
import org.jscience.util.identity.Identifiable;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a scientific experiment that takes an input configuration 
 * and produces a result.
 * 
 * @param <I> The input configuration type
 * @param <R> The result type
 */
public interface Experiment<I, R> extends Named, Identifiable<String> {
    
    /**
     * Executes the experiment with the given input.
     * 
     * @param input the experimental parameters
     * @return a future providing the result
     */
    CompletableFuture<R> run(I input);
    
    /**
     * Gets the hypothesis this experiment aims to test.
     * 
     * @return the hypothesis, or null if exploratory
     */
    Hypothesis<R> getHypothesis();

    @Override
    default String getName() {
        return "Generic Experiment";
    }

    @Override
    default String getId() {
        return "EXP-" + System.currentTimeMillis();
    }
}
