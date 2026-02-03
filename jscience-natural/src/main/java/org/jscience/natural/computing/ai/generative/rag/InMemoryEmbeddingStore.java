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
package org.jscience.natural.computing.ai.generative.rag;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A simple in-memory Embedding Store for RAG (Retrieval Augmented Generation).
 * <p>
 * Stores document embeddings and retrieves nearest neighbors using Cosine Similarity.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class InMemoryEmbeddingStore {
    
    private static class DocumentEntry {
        @SuppressWarnings("unused")
        String id;
        String content;
        float[] embedding; // Embedding vector

        public DocumentEntry(String id, String content, float[] embedding) {
            this.id = id;
            this.content = content;
            this.embedding = embedding;
        }
    }

    private List<DocumentEntry> entries = new ArrayList<>();

    /**
     * Adds a document with its embedding vector to the store.
     * @param id unique identifier.
     * @param content text content.
     * @param embedding float array representing the embedding.
     */
    public void add(String id, String content, float[] embedding) {
        entries.add(new DocumentEntry(id, content, embedding));
    }

    /**
     * Searches for the k nearest documents to the query vector.
     * @param queryEmbedding the query vector.
     * @param k number of results to return.
     * @return list of matching document contents.
     */
    public List<String> search(float[] queryEmbedding, int k) {
        return entries.stream()
            .sorted((d1, d2) -> Double.compare(cosineSimilarity(d2.embedding, queryEmbedding), 
                                               cosineSimilarity(d1.embedding, queryEmbedding))) // Descending
            .limit(k)
            .map(d -> d.content)
            .collect(Collectors.toList());
    }

    private double cosineSimilarity(float[] v1, float[] v2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            normA += v1[i] * v1[i];
            normB += v2[i] * v2[i];
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
