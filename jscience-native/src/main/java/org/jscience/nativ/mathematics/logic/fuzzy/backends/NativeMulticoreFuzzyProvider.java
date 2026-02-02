/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.mathematics.logic.fuzzy.backends;

import org.jscience.core.mathematics.logic.fuzzy.FuzzyExpression;
import org.jscience.core.mathematics.logic.fuzzy.LinguisticVariable;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Native Multicore provider for Fuzzy Logic operations.
 * <p>
 * Accelerates bulk fuzzy evaluations using Java 8+ Parallel Streams.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class NativeMulticoreFuzzyProvider implements AlgorithmProvider {

    /**
     * Evaluates a batch of fuzzy expressions in parallel.
     * 
     * @param expressions list of expressions to evaluate.
     * @param input the input value.
     * @return list of results.
     */
    public <T> List<Double> bulkEvaluate(Collection<FuzzyExpression<T>> expressions, T input) {
        return expressions.parallelStream()
                .map(expr -> expr.evaluate(input))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "Native Multicore Fuzzy Logic";
    }
}
