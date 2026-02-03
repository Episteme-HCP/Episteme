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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of {@link GraphProvider}.
 * <p>
 * This implementation is thread-safe and intended for testing and 
 * small-scale persistence needs.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class InMemoryGraphProvider implements GraphProvider {

    private final Map<Class<?>, Map<Object, Object>> store = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "InMemory";
    }

    @Override
    public <T> void save(T entity) {
        if (entity == null) {
            return;
        }
        
        Class<?> type = entity.getClass();
        if (!type.isAnnotationPresent(Persistent.class)) {
            throw new IllegalArgumentException("Class " + type.getName() + " is not marked as @Persistent");
        }

        Object id = getId(entity);
        if (id == null) {
            throw new IllegalStateException("Entity of type " + type.getName() + " has no @Id field or ID is null");
        }

        store.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(id, entity);
    }

    @Override
    public <T> T load(Class<T> type, Object id) {
        Map<Object, Object> classStore = store.get(type);
        if (classStore == null) {
            return null;
        }
        Object entity = classStore.get(id);
        return entity != null ? type.cast(entity) : null;
    }

    @Override
    public <T> void delete(T entity) {
        if (entity == null) {
            return;
        }
        
        Object id = getId(entity);
        if (id != null) {
            Map<Object, Object> classStore = store.get(entity.getClass());
            if (classStore != null) {
                classStore.remove(id);
            }
        }
    }

    @Override
    public <T> List<T> query(Class<T> type, String query) {
        List<T> results = new ArrayList<>();
        Map<Object, Object> classStore = store.get(type);
        if (classStore != null) {
            // For now, this simple implementation returns all entities of the type
            // if any query is provided, or could be extended for filtering.
            for (Object value : classStore.values()) {
                results.add(type.cast(value));
            }
        }
        return results;
    }

    private Object getId(Object entity) {
        // Look for @Id field in the class hierarchy
        Class<?> current = entity.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        return field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Could not access @Id field in " + current.getName(), e);
                    }
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }
}
