/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.mathematics.algebra.algebras.providers;

import com.google.auto.service.AutoService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

import org.episteme.core.mathematics.algebra.algebras.BooleanAlgebraProvider;

/**
 * Standard JVM implementation of Boolean Algebra.
 */
@AutoService(AlgorithmProvider.class)
public class StandardBooleanAlgebraProvider implements BooleanAlgebraProvider {

    @Override
    public Boolean and(Boolean a, Boolean b) {
        return a && b;
    }

    @Override
    public Boolean or(Boolean a, Boolean b) {
        return a || b;
    }

    @Override
    public Boolean not(Boolean a) {
        return !a;
    }

    @Override
    public Boolean xor(Boolean a, Boolean b) {
        return a ^ b;
    }

    @Override
    public String getName() {
        return "Standard JVM Boolean Algebra";
    }

    @Override
    public int getPriority() {
        return 0; // Reference implementation
    }
}
