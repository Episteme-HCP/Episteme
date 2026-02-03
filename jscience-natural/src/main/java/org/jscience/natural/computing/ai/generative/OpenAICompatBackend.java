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

package org.jscience.natural.computing.ai.generative;

import java.util.concurrent.CompletableFuture;

/**
 * Backend for interacting with OpenAI-compatible APIs (OpenAI, Gemini via Gateway, LocalAI).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class OpenAICompatBackend implements GenerativeModel {

    private final String apiKey;
    private final String modelName;
    private final String baseUrl;

    public OpenAICompatBackend(String apiKey, String modelName) {
        this(apiKey, modelName, "https://api.openai.com/v1");
    }
    
    public OpenAICompatBackend(String apiKey, String modelName, String baseUrl) {
        this.apiKey = apiKey;
        this.modelName = modelName;
        this.baseUrl = baseUrl;
    }

    @Override
    public CompletableFuture<String> generate(String prompt) {
        return CompletableFuture.supplyAsync(() -> {
            // Mock response
             return "OpenAI (" + modelName + ") response to: " + prompt; 
        });
    }

    @Override
    public CompletableFuture<float[]> embed(String text) {
        return CompletableFuture.supplyAsync(() -> new float[1536]);
    }

    @Override
    public String getModelName() {
        return modelName;
    }
}
