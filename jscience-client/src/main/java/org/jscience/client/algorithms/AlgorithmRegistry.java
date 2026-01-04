/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.client.algorithms;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry to manage and select the best available algorithms.
 */
public class AlgorithmRegistry {

    private static final AlgorithmRegistry INSTANCE = new AlgorithmRegistry();
    private final Map<Class<?>, List<Algorithm<?, ?>>> algorithms = new ConcurrentHashMap<>();

    private AlgorithmRegistry() {
    }

    public static AlgorithmRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers an algorithm for a specific category type (e.g. Optimizer.class).
     */
    public synchronized void register(Class<?> category, Algorithm<?, ?> algorithm) {
        algorithms.computeIfAbsent(category, k -> new ArrayList<>()).add(algorithm);
    }

    /**
     * Returns the best algorithm for the given category based on quality score.
     */
    @SuppressWarnings("unchecked")
    public <I, O> Optional<Algorithm<I, O>> getBestAlgorithm(Class<?> category) {
        List<Algorithm<?, ?>> list = algorithms.get(category);
        if (list == null || list.isEmpty())
            return Optional.empty();

        return list.stream()
                .max(Comparator.comparingDouble(Algorithm::getQualityScore))
                .map(algo -> (Algorithm<I, O>) algo);
    }

    /**
     * Returns all algorithms for a category.
     */
    @SuppressWarnings("unchecked")
    public <I, O> List<Algorithm<I, O>> getAllAlgorithms(Class<?> category) {
        List<Algorithm<?, ?>> list = algorithms.get(category);
        if (list == null)
            return Collections.emptyList();

        List<Algorithm<I, O>> result = new ArrayList<>();
        for (Algorithm<?, ?> a : list) {
            result.add((Algorithm<I, O>) a);
        }
        return result;
    }
}
