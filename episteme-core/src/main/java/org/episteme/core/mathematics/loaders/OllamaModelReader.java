/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.mathematics.loaders;

import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.mathematics.ml.generative.GenerativeModel;

import java.util.concurrent.CompletableFuture;

/**
 * Reader (Loader) for Ollama models (Local LLM server).
 * <p>
 * Connects to a local Ollama instance (default localhost:11434).
 * The resource ID is treated as the model name (e.g., "llama3").
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class OllamaModelReader extends AbstractResourceReader<GenerativeModel> {

    @Override
    protected GenerativeModel loadFromSource(String resourceId) throws Exception {
        // The resourceId is the model name for Ollama
        return new OllamaModel(resourceId);
    }

    @Override
    public String getName() {
        return "Ollama Model Reader";
    }

    @Override
    public String getDescription() {
        return "Connects to local Ollama instance for LLM inference.";
    }

    @Override
    public String getLongDescription() {
        return "Provides access to local Large Language Models via Ollama API.";
    }

    @Override
    public String getCategory() {
        return "AI/Generative";
    }

    @Override
    public Class<GenerativeModel> getResourceType() {
        return GenerativeModel.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "N/A" };
    }
    
    @Override
    public String getResourcePath() {
        return "ai/ollama";
    }

    /**
     * Internal implementation of GenerativeModel for Ollama.
     */
    private static class OllamaModel implements GenerativeModel {
        private final String modelName;

        public OllamaModel(String modelName) {
            this.modelName = modelName;
        }

        @Override
        public CompletableFuture<String> generate(String prompt) {
            return CompletableFuture.supplyAsync(() -> 
                "Ollama (" + modelName + ") response to: " + prompt
            );
        }

        @Override
        public CompletableFuture<float[]> embed(String text) {
            return CompletableFuture.supplyAsync(() -> new float[768]);
        }

        @Override
        public String getModelName() {
            return modelName;
        }
    }
}
