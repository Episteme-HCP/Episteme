/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.distributed.backends;

import org.jscience.core.technical.backend.distributed.DistributedBackend;
import org.jscience.core.technical.backend.distributed.DistributedContext;
import org.jscience.core.distributed.SparkDistributedContext;
import com.google.auto.service.AutoService;

/**
 * Apache Spark Distributed Computing Backend.
 * Wraps Apache Spark for cluster computing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService()
public class SparkDistributedBackend implements DistributedBackend {

    private boolean available;

    public SparkDistributedBackend() {
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
    public boolean isAvailable() {
        return available;
    }

    @Override
    public int getNodeCount() {
        return available ? Runtime.getRuntime().availableProcessors() : 0;
    }

    @Override
    public DistributedContext createDistributedContext() {
        if (!available) throw new UnsupportedOperationException("Spark not available");
        return new SparkDistributedContext();
    }
}
