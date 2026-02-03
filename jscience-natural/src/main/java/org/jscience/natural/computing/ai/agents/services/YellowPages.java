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
package org.jscience.natural.computing.ai.agents.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.jscience.core.util.persistence.PersistenceManager;
import org.jscience.core.mathematics.ml.generative.EmbeddingStore;
import org.jscience.core.mathematics.ml.generative.InMemoryEmbeddingStore;
import org.jscience.core.mathematics.ml.generative.GenerativeModel;
import java.util.HashMap;

/**
 * Directory Facilitator (DF) for agent discovery.
 */
public class YellowPages {
    private static final YellowPages INSTANCE = new YellowPages();
    
    // Map of AgentID -> List of Services
    private final Map<String, List<ServiceDescription>> registry = new ConcurrentHashMap<>();
    
    // Semantic index for services
    private final EmbeddingStore embeddingStore = new InMemoryEmbeddingStore();
    private GenerativeModel embeddingModel;

    private YellowPages() {}

    public void setEmbeddingModel(GenerativeModel model) {
        this.embeddingModel = model;
    }

    public static YellowPages getInstance() {
        return INSTANCE;
    }

    /**
     * Register an agent and its services.
     */
    public void register(String agentId, List<ServiceDescription> services) {
        registry.put(agentId, new ArrayList<>(services));
        
        // Persist services to the global knowledge graph for semantic discovery
        for (ServiceDescription sd : services) {
            PersistenceManager.getInstance().save(sd);
            
            // Index semantically if an embedding model is available
            if (embeddingModel != null) {
                String content = sd.getType() + " " + sd.getName();
                embeddingModel.embed(content).thenAccept(vec -> {
                    Map<String, Object> meta = new HashMap<>();
                    meta.put("agentId", agentId);
                    meta.put("serviceName", sd.getName());
                    embeddingStore.add(sd.getType() + ":" + agentId, vec, meta);
                });
            }
        }
        
        System.out.println("YellowPages: Registered agent " + agentId + " with " + services.size() + " services");
    }

    /**
     * Unregister an agent.
     */
    public void unregister(String agentId) {
        registry.remove(agentId);
    }

    /**
     * Search for agent IDs offering a specific service type.
     */
    public List<String> search(String serviceType) {
        return registry.entrySet().stream()
            .filter(entry -> entry.getValue().stream().anyMatch(s -> s.getType().equals(serviceType)))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    /**
     * Performs a semantic search across registered services.
     * 
     * @param query the natural language query.
     * @param maxResults maximum number of agents to return.
     * @return a list of agent IDs.
     */
    public List<String> semanticSearch(String query, int maxResults) {
        if (embeddingModel == null) {
            throw new IllegalStateException("Embedding model not set for semantic search");
        }
        
        try {
            float[] queryVec = embeddingModel.embed(query).get(); // Synchronous for simplified search API
            return embeddingStore.findNearest(queryVec, maxResults).stream()
                .map(res -> (String) res.metadata().get("agentId"))
                .distinct()
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Semantic search failed", e);
        }
    }
}
