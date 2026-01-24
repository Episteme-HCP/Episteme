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

package org.jscience.linguistics.loaders;

import org.jscience.io.AbstractResourceReader;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Loader for Word2Vec models (text format).
 * Modernized to extend {@link AbstractResourceReader} for standardized embedding loading.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Word2VecLoader extends AbstractResourceReader<Map<String, Vector<Real>>> {

    /**
     * Default constructor.
     */
    public Word2VecLoader() {
    }

    @Override
    protected Map<String, Vector<Real>> loadFromSource(String path) throws Exception {
        return loadTextModel(new File(path));
    }

    @Override
    protected Map<String, Vector<Real>> loadFromInputStream(InputStream is, String id) throws Exception {
        return loadFromReader(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
    }

    /**
     * Loads a Word2Vec model in text format.
     */
    public static Map<String, Vector<Real>> loadTextModel(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            return loadFromReader(reader);
        }
    }

    private static Map<String, Vector<Real>> loadFromReader(BufferedReader reader) throws IOException {
        Map<String, Vector<Real>> model = new HashMap<>();
        String header = reader.readLine();
        if (header == null) throw new IOException("Empty file");
        
        String[] parts = header.trim().split("\\s+");
        if (parts.length < 2) throw new IOException("Invalid Word2Vec header: " + header);
        
        int numWords = Integer.parseInt(parts[0]);
        int dimensions = Integer.parseInt(parts[1]);
        
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.trim().split("\\s+");
            if (values.length < dimensions + 1) continue;
            
            String word = values[0];
            Real[] components = new Real[dimensions];
            for (int i = 0; i < dimensions; i++) {
                components[i] = Real.of(Double.parseDouble(values[i + 1]));
            }
            
            model.put(word, DenseVector.of(components));
            if (model.size() >= numWords) break;
        }
        return model;
    }

    /**
     * Calculates cosine similarity between two vectors.
     */
    public static double cosineSimilarity(Vector<Real> v1, Vector<Real> v2) {
        double dotProduct = v1.dot(v2).doubleValue();
        double norm1 = Math.sqrt(v1.dot(v1).doubleValue());
        double norm2 = Math.sqrt(v2.dot(v2).doubleValue());
        
        if (norm1 == 0 || norm2 == 0) return 0.0;
        return dotProduct / (norm1 * norm2);
    }

    @Override
    public Class<Map<String, Vector<Real>>> getResourceType() {
        @SuppressWarnings("unchecked")
        Class<Map<String, Vector<Real>>> type = (Class<Map<String, Vector<Real>>>) (Class<?>) Map.class;
        return type;
    }

    @Override
    public String getName() {
        return "Word2Vec Embeddings Loader";
    }

    @Override
    public String getDescription() {
        return "Loads word embedding vectors from Word2Vec text format models.";
    }

    @Override
    public String getCategory() {
        return "Linguistics / Machine Learning";
    }

    @Override
    public String getResourcePath() {
        return "embeddings/word2vec";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[]{"Text Format V1"};
    }
}
