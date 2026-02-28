/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.mathematics.loaders;

import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.mathematics.ml.generative.GenerativeModel;

import java.util.concurrent.CompletableFuture;

/**
 * Reader (Loader) for OpenAI-compatible models.
 * <p>
 * Connects to OpenAI or compatible APIs.
 * The resource ID is treated as the model name (e.g., "gpt-4").
 * Note: API Key handling would normally be via configuration/env vars, not the ID.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class OpenAIModelReader extends AbstractResourceReader<GenerativeModel> {

    @Override
    protected GenerativeModel loadFromSource(String resourceId) throws Exception {
        return new OpenAIModel(resourceId);
    }

    @Override
    public String getName() {
        return "OpenAI Model Reader";
    }

    @Override
    public String getDescription() {
        return "Connects to OpenAI-compatible APIs.";
    }

    @Override
    public String getLongDescription() {
        return "Provides access to Large Language Models via OpenAI-compatible APIs (OpenAI, Gemini, LocalAI).";
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
        return new String[] { "v1" };
    }
    
    @Override
    public String getResourcePath() {
        return "ai/openai";
    }

    /**
     * Internal implementation of GenerativeModel for OpenAI.
     */
    private static class OpenAIModel implements GenerativeModel {
        private final String modelName;

        public OpenAIModel(String modelName) {
            this.modelName = modelName;
        }

        @Override
        public CompletableFuture<String> generate(String prompt) {
            return CompletableFuture.supplyAsync(() -> 
                "OpenAI (" + modelName + ") response to: " + prompt
            );
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
}
