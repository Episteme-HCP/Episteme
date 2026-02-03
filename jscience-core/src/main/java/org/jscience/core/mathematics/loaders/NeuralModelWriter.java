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
package org.jscience.core.mathematics.loaders;

import org.jscience.core.io.AbstractResourceWriter;
import org.jscience.core.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.core.mathematics.ml.neural.Layer;
import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class NeuralModelWriter extends AbstractResourceWriter<Layer<?>> {

    private static final NeuralModelWriter INSTANCE = new NeuralModelWriter();

    public static NeuralModelWriter getInstance() {
        return INSTANCE;
    }

    @Override public String getName() { return "Neural Model Writer (JSON)"; }
    @Override public String getDescription() { return "Writes Neural Network models to JSON files."; }
    @Override public String getLongDescription() { return "Serializes models to standard JSON 'State Dict' format."; }
    @Override public String getCategory() { return "AI/ML"; }
    @Override public Class<Layer<?>> getResourceType() { return (Class) Layer.class; }
    @Override public String[] getSupportedVersions() { return new String[] { "1.0" }; }
    @Override public String getResourcePath() { return "ml/models"; }

    @Override
    public void save(Layer<?> resource, String id) throws Exception {
        save(resource, Paths.get(id));
    }

    /**
     * Saves the layer's parameters to a JSON file.
     *
     * @param layer the layer (or model).
     * @param path  the destination path.
     * @throws IOException if an I/O error occurs.
     */
    public void save(Layer<?> layer, Path path) throws IOException {
        String json = toJson(layer);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(json);
        }
    }

    /**
     * Serializes layer parameters to a JSON String.
     *
     * @param layer the layer.
     * @return JSON string.
     */
    public String toJson(Layer<?> layer) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"parameters\": [\n");

        java.util.Map<String, Tensor<?>> params = (java.util.Map) layer.getParameters();
        int i = 0;
        int size = params.size();
        for (java.util.Map.Entry<String, Tensor<?>> entry : params.entrySet()) {
            Tensor<?> tensor = entry.getValue();
            sb.append("    {\n");
            // Including name in JSON might be useful now that we have it
            sb.append("      \"name\": \"").append(entry.getKey()).append("\",\n"); 
            sb.append("      \"shape\": ").append(Arrays.toString(tensor.shape())).append(",\n");
            sb.append("      \"data\": ").append(tensorDataToJson(tensor));
            sb.append("\n    }");
            if (i < size - 1) {
                sb.append(",");
            }
            sb.append("\n");
            i++;
        }

        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    private String tensorDataToJson(Tensor<?> tensor) {
         Object array = tensor.toArray();
         return recursiveArrayToJson(array);
    }

    private String recursiveArrayToJson(Object array) {
        if (array == null) return "null";
        
        if (array.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int len = java.lang.reflect.Array.getLength(array);
            for (int i = 0; i < len; i++) {
                Object val = java.lang.reflect.Array.get(array, i);
                if (val.getClass().isArray()) {
                    sb.append(recursiveArrayToJson(val));
                } else {
                    sb.append(val.toString()); 
                }
                if (i < len - 1) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
        return array.toString();
    }


}
