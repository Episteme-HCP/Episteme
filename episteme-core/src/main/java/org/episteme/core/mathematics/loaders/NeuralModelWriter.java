/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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
package org.episteme.core.mathematics.loaders;

import org.episteme.core.io.AbstractResourceWriter;
import org.episteme.core.mathematics.linearalgebra.Tensor;
import org.episteme.core.mathematics.ml.neural.Layer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NeuralModelWriter extends AbstractResourceWriter<Layer<?>> {

    private static final NeuralModelWriter INSTANCE = new NeuralModelWriter();

    public static NeuralModelWriter getInstance() {
        return INSTANCE;
    }

    @Override public String getName() { return "Neural Model Writer (JSON)"; }
    @Override public String getDescription() { return "Writes Neural Network models to JSON files."; }
    @Override public String getLongDescription() { return "Serializes models to standard JSON 'State Dict' format."; }
    @Override public String getCategory() { return "AI/ML"; }
    @SuppressWarnings("unchecked")
    @Override public Class<Layer<?>> getResourceType() { return (Class<Layer<?>>) (Class<?>) Layer.class; }
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
     * Serializes layer architecture and parameters to a JSON String.
     *
     * @param layer the layer.
     * @return JSON string.
     */
    public String toJson(Layer<?> layer) {
        StringBuilder sb = new StringBuilder();
        toJsonRecursive(layer, sb, 0);
        return sb.toString();
    }

    private void toJsonRecursive(Layer<?> layer, StringBuilder sb, int indent) {
        String pad = "  ".repeat(indent);
        sb.append(pad).append("{\n");
        sb.append(pad).append("  \"type\": \"").append(layer.getClass().getName()).append("\",\n");

        if (layer instanceof org.episteme.core.mathematics.ml.neural.layers.Sequential) {
            sb.append(pad).append("  \"layers\": [\n");
            // Access layers field via reflection or if we make it public/getter
            try {
                java.lang.reflect.Field field = layer.getClass().getDeclaredField("layers");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<Layer<?>> subLayers = (List<Layer<?>>) field.get(layer);
                for (int i = 0; i < subLayers.size(); i++) {
                    toJsonRecursive(subLayers.get(i), sb, indent + 2);
                    if (i < subLayers.size() - 1) sb.append(",");
                    sb.append("\n");
                }
            } catch (Exception e) {
                sb.append(pad).append("    // Error serializing sublayers: ").append(e.getMessage()).append("\n");
            }
            sb.append(pad).append("  ]\n");
        } else if (layer instanceof org.episteme.core.mathematics.ml.neural.layers.ActivationLayer) {
            org.episteme.core.mathematics.ml.neural.layers.ActivationLayer<?> al = (org.episteme.core.mathematics.ml.neural.layers.ActivationLayer<?>) layer;
            sb.append(pad).append("  \"function\": \"").append(al.getFunction().name()).append("\"\n");
        } else {
            // Standard layer with parameters
            sb.append(pad).append("  \"parameters\": {\n");
            @SuppressWarnings("unchecked")
            Map<String, org.episteme.core.mathematics.ml.neural.autograd.GraphNode<?>> params = (Map<String, org.episteme.core.mathematics.ml.neural.autograd.GraphNode<?>>) (Map<?, ?>) layer.getParameters();
            int i = 0;
            int size = params.size();
            for (Map.Entry<String, org.episteme.core.mathematics.ml.neural.autograd.GraphNode<?>> entry : params.entrySet()) {
                Tensor<?> tensor = entry.getValue().getData();
                sb.append(pad).append("    \"").append(entry.getKey()).append("\": {\n");
                sb.append(pad).append("      \"shape\": ").append(Arrays.toString(tensor.shape())).append(",\n");
                sb.append(pad).append("      \"data\": ").append(tensorDataToJson(tensor)).append("\n");
                sb.append(pad).append("    }");
                if (i < size - 1) sb.append(",");
                sb.append("\n");
                i++;
            }
            sb.append(pad).append("  }\n");
        }

        sb.append(pad).append("}");
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
