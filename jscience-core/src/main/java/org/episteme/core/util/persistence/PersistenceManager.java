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

package org.episteme.core.util.persistence;

import java.util.ServiceLoader;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Central manager for persistent scientific object graphs.
 * <p>
 * This manager discovers {@link GraphProvider} implementations via the
 * Java Service Provider Interface (SPI).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class PersistenceManager {

    private static final Logger logger = Logger.getLogger(PersistenceManager.class.getName());
    private static final PersistenceManager INSTANCE = new PersistenceManager();

    private GraphProvider provider;

    private PersistenceManager() {
        // Load provider via SPI
        ServiceLoader<GraphProvider> loader = ServiceLoader.load(GraphProvider.class);
        Optional<GraphProvider> first = loader.findFirst();
        
        if (first.isPresent()) {
            this.provider = first.get();
            logger.info("Loaded GraphProvider: " + provider.getName());
        } else {
            // Default to in-memory if no SPI provider is found
            this.provider = new InMemoryGraphProvider();
            logger.warning("No GraphProvider found via SPI, falling back to InMemoryGraphProvider");
        }
    }

    /**
     * Returns the singleton instance of the persistence manager.
     * 
     * @return the manager instance.
     */
    public static PersistenceManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the current graph provider.
     * 
     * @return the provider.
     */
    public GraphProvider getProvider() {
        return provider;
    }

    /**
     * Sets the graph provider to use.
     * 
     * @param provider the provider to set.
     */
    public void setProvider(GraphProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null");
        }
        this.provider = provider;
    }

    /**
     * Saves an entity using the current provider.
     * 
     * @param <T> the type of the entity
     * @param entity the entity to save
     */
    public <T> void save(T entity) {
        provider.save(entity);
    }

    /**
     * Loads an entity using the current provider.
     * 
     * @param <T> the type of the entity
     * @param type the class of the entity
     * @param id the unique identifier
     * @return the loaded entity
     */
    public <T> T load(Class<T> type, Object id) {
        return provider.load(type, id);
    }

    /**
     * Deletes an entity using the current provider.
     * 
     * @param <T> the type of the entity
     * @param entity the entity to delete
     */
    public <T> void delete(T entity) {
        provider.delete(entity);
    }

    /**
     * Queries for entities using the current provider.
     * 
     * @param <T> the type of the entities
     * @param type the class of the entities
     * @param query symbols or criteria for search
     * @return a list of matching entities
     */
    public <T> List<T> query(Class<T> type, String query) {
        return provider.query(type, query);
    }
}
