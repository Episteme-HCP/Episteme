/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.ml;

import org.episteme.core.mathematics.ml.neural.Layer;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a neural network model.
 * <p>
 * This model wraps the modern tensor-based neural network architecture
 * from {@code episteme-core} with persistent capabilities.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class NeuralNetworkModel extends Model {
    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Layer<?>> layers = new ArrayList<>();

    public NeuralNetworkModel() {
    }

    public NeuralNetworkModel(String name) {
        super(name);
    }

    public void addLayer(Layer<?> layer) {
        layers.add(layer);
    }
    
    public List<Layer<?>> getLayers() {
        return layers;
    }
}

