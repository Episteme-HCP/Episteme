/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.distributed.providers;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.distributed.DistributedComputingProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.distributed.SparkDistributedContext;

/**
 * Apache Spark Distributed Computing Provider.
 * Wraps Apache Spark for cluster computing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class SparkComputingProvider implements DistributedComputingProvider {

    private boolean available;

    public SparkComputingProvider() {
        try {
            Class.forName("org.apache.spark.SparkContext");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
    }

    @Override
    public String getName() {
        return "Apache Spark (Wrapper)";
    }

    @Override
    public String getAlgorithmType() {
        return "Distributed Computing";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    public DistributedContext createContext() {
        if (!available) throw new UnsupportedOperationException("Spark not available");
        return new SparkDistributedContext();
    }
}
