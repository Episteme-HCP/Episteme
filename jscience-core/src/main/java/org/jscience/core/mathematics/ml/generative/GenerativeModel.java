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

package org.jscience.core.mathematics.ml.generative;

import java.util.concurrent.CompletableFuture;

/**
 * Interface representing a Generative AI model (e.g., Large Language Model).
 * <p>
 * Provides methods for generating text and embeddings.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface GenerativeModel {

    /**
     * Generates a response for the given prompt.
     * 
     * @param prompt the input text.
     * @return a future containing the generated text.
     */
    CompletableFuture<String> generate(String prompt);

    /**
     * Generates vector embeddings for the given text.
     * 
     * @param text the input text.
     * @return a future containing the embedding vector (as float array).
     */
    CompletableFuture<float[]> embed(String text);
    
    /**
     * Returns the name of the model being used (e.g., "gpt-4", "llama3").
     */
    String getModelName();
}
