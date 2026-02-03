/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.loaders;

import org.jscience.core.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorFactory;
import org.jscience.core.mathematics.ml.neural.Layer;
import org.jscience.core.mathematics.ml.neural.layers.ActivationLayer;
import org.jscience.core.mathematics.ml.neural.layers.Linear;
import org.jscience.core.mathematics.ml.neural.layers.Sequential;
import org.jscience.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationTest {

    @TempDir
    Path tempDir;

    @Test
    public void testFullModelSerialization() throws Exception {
        // 1. Create a model
        Sequential<Real> model = new Sequential<>();
        model.add(new Linear<>(3, 5));
        model.add(new ActivationLayer<>("RELU"));
        model.add(new Linear<>(5, 2));

        // 2. Prepare test input
        Real[] inputData = {Real.of(1.0), Real.of(2.0), Real.of(3.0)};
        Tensor<Real> input = TensorFactory.of(inputData, 1, 3);

        // 3. Get reference output
        Tensor<Real> referenceOutput = model.forward(input);

        // 4. Save model
        Path modelPath = tempDir.resolve("model.json");
        NeuralModelWriter.getInstance().save(model, modelPath);

        // 5. Load model
        Layer<Real> loadedModel = (Layer<Real>) NeuralModelReader.getInstance().load(modelPath);

        // 6. Verify architecture
        assertNotNull(loadedModel);
        assertTrue(loadedModel instanceof Sequential);
        
        // 7. Verify inference consistency
        Tensor<Real> loadedOutput = loadedModel.forward(input);
        
        assertEquals(referenceOutput.shape()[0], loadedOutput.shape()[0]);
        assertEquals(referenceOutput.shape()[1], loadedOutput.shape()[1]);
        
        for (int i = 0; i < 2; i++) {
            double expected = ((Real) referenceOutput.get(0, i)).doubleValue();
            double actual = ((Real) loadedOutput.get(0, i)).doubleValue();
            assertEquals(expected, actual, 1e-6, "Output mismatch at index " + i);
        }
    }
}
