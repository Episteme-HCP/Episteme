package org.episteme.core.mathematics.ml.generative;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class AdvancedAITest {

    @Test
    public void testEmbeddingStore() {
        InMemoryEmbeddingStore store = new InMemoryEmbeddingStore();
        
        float[] v1 = {1.0f, 0.0f}; // "Weather"
        float[] v2 = {0.0f, 1.0f}; // "Mathematics"
        float[] query = {0.9f, 0.1f}; // Query similar to Weather
        
        store.add("weather_agent", v1, Map.of("role", "reporter"));
        store.add("math_agent", v2, Map.of("role", "calculator"));
        
        List<EmbeddingStore.SearchResult> results = store.findNearest(query, 2);
        
        assertEquals(2, results.size());
        assertEquals("weather_agent", results.get(0).id());
        assertTrue(results.get(0).score() > results.get(1).score());
    }

    @Test
    public void testPromptTemplate() {
        PromptTemplate template = PromptTemplate.of("Hello {{name}}, welcome to {{place}}!");
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", "Alice");
        vars.put("place", "Wonderland");
        
        String result = template.render(vars);
        assertEquals("Hello Alice, welcome to Wonderland!", result);
    }
}
