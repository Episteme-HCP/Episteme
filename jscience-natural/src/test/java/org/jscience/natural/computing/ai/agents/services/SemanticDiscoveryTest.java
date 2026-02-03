package org.jscience.natural.computing.ai.agents.services;

import org.jscience.core.mathematics.ml.generative.GenerativeModel;
import org.jscience.core.mathematics.ml.generative.OllamaBackend;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;

public class SemanticDiscoveryTest {

    @Test
    public void testSemanticSearch() {
        YellowPages yp = YellowPages.getInstance();
        
        // Mocking an embedding model that returns fixed vectors for known words
        GenerativeModel mockModel = new OllamaBackend("mock") {
            @Override
            public CompletableFuture<float[]> embed(String text) {
                float[] vec = new float[768];
                if (text.toLowerCase().contains("weather") || text.toLowerCase().contains("meteorology")) {
                    vec[0] = 1.0f; // Similarity dimension
                }
                return CompletableFuture.completedFuture(vec);
            }
        };
        
        yp.setEmbeddingModel(mockModel);
        
        ServiceDescription sd = new ServiceDescription("weather-reporter", "GlobalWeather");
        yp.register("agent-1", List.of(sd));
        
        // Search for something semantically similar
        List<String> results = yp.semanticSearch("meteorology forecast", 1);
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("agent-1", results.get(0));
    }
}
