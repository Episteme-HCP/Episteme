/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.loaders;

import org.jscience.core.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorFactory;
import org.jscience.core.mathematics.ml.neural.ActivationFunction;
import org.jscience.core.mathematics.ml.neural.Layer;
import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.SimpleJSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Loads neural network models from JSON format.
 */
public class NeuralModelReader {

    private static final NeuralModelReader INSTANCE = new NeuralModelReader();

    public static NeuralModelReader getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public Layer<?> load(Path path) throws IOException {
        String json = Files.readString(path);
        Map<String, Object> map = (Map<String, Object>) SimpleJSON.parse(json);
        return parseLayer(map);
    }

    private Layer<?> parseLayer(Map<String, Object> map) {
        String type = (String) map.get("type");
        try {
            if (type.contains("Sequential")) {
                org.jscience.core.mathematics.ml.neural.layers.Sequential<Real> sequential = new org.jscience.core.mathematics.ml.neural.layers.Sequential<>();
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> layers = (List<Map<String, Object>>) map.get("layers");
                for (Map<String, Object> layerMap : layers) {
                    sequential.add((Layer<Real>) parseLayer(layerMap));
                }
                return sequential;
            } else if (type.contains("ActivationLayer")) {
                String funcName = (String) map.get("function");
                return new org.jscience.core.mathematics.ml.neural.layers.ActivationLayer<Real>(funcName);
            } else if (type.contains("Linear")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> params = (Map<String, Object>) map.get("parameters");
                @SuppressWarnings("unchecked")
                Map<String, Object> weightMap = (Map<String, Object>) params.get("weights");
                @SuppressWarnings("unchecked")
                Map<String, Object> biasMap = (Map<String, Object>) params.get("bias");

                Tensor<Real> weights = (Tensor<Real>) parseTensor(weightMap);
                Tensor<Real> bias = (Tensor<Real>) parseTensor(biasMap);

                int inFeatures = weights.shape()[0];
                int outFeatures = weights.shape()[1];

                org.jscience.core.mathematics.ml.neural.layers.Linear<Real> linear = new org.jscience.core.mathematics.ml.neural.layers.Linear<>(inFeatures, outFeatures);
                
                // Inject parameters
                injectParameters(linear, weights, bias);
                return linear;
            }
            // Add more layers here
        } catch (Exception e) {
            throw new RuntimeException("Error parsing layer type: " + type, e);
        }
        return null;
    }

    private Tensor<?> parseTensor(Map<String, Object> map) {
        List<Integer> shapeList = (List) map.get("shape");
        int[] shape = shapeList.stream().mapToInt(i -> i).toArray();
        List<Object> data = (List) map.get("data");
        
        Real[] realData = flattenReal(data);
        return TensorFactory.of(realData, shape);
    }

    private Real[] flattenReal(List<Object> data) {
        List<Real> flat = new ArrayList<>();
        recursiveFlatten(data, flat);
        return flat.toArray(new Real[0]);
    }

    private void recursiveFlatten(List<Object> data, List<Real> flat) {
        for (Object obj : data) {
            if (obj instanceof List) {
                recursiveFlatten((List) obj, flat);
            } else if (obj instanceof Number) {
                flat.add(Real.of(((Number) obj).doubleValue()));
            }
        }
    }

    private void injectParameters(org.jscience.core.mathematics.ml.neural.layers.Linear<Real> linear, Tensor<Real> weights, Tensor<Real> bias) {
        try {
            java.lang.reflect.Field wField = linear.getClass().getDeclaredField("weights");
            java.lang.reflect.Field bField = linear.getClass().getDeclaredField("bias");
            wField.setAccessible(true);
            bField.setAccessible(true);
            
            wField.set(linear, new GraphNode<>(weights, true));
            bField.set(linear, new GraphNode<>(bias, true));
        } catch (Exception e) {
            throw new RuntimeException("Error injecting parameters", e);
        }
    }
}
