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

package org.jscience.core.util.persistence;

import java.util.List;

/**
 * Service Provider Interface (SPI) for persisting scientific object graphs.
 * <p>
 * Implementations of this interface handle the actual storage and retrieval 
 * of entities marked with the {@link Persistent} annotation.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface GraphProvider {

    /**
     * Returns the unique name of this provider.
     * 
     * @return the provider name.
     */
    String getName();

    /**
     * Saves an entity to the persistent store.
     * 
     * @param <T> the type of the entity
     * @param entity the entity to save
     */
    <T> void save(T entity);

    /**
     * Loads an entity from the persistent store.
     * 
     * @param <T> the type of the entity
     * @param type the class of the entity
     * @param id the unique identifier of the entity
     * @return the loaded entity, or {@code null} if not found
     */
    <T> T load(Class<T> type, Object id);

    /**
     * Deletes an entity from the persistent store.
     * 
     * @param <T> the type of the entity
     * @param entity the entity to delete
     */
    <T> void delete(T entity);

    /**
     * Queries the persistent store for entities matching the given criteria.
     * 
     * @param <T> the type of the entities
     * @param type the class of the entities to search for
     * @param query symbols or criteria for the search (implementation-specific)
     * @return a list of matching entities
     */
    <T> List<T> query(Class<T> type, String query);
}
