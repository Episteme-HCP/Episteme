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

package org.jscience.io;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base for resource readers with caching and fallback support.
 * Replaces AbstractLoader.
 */
public abstract class AbstractResourceReader<T> implements ResourceReader<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractResourceReader.class.getName());
    private final ConcurrentHashMap<String, T> localCache = new ConcurrentHashMap<>();
    private boolean usingFallback = false;
    private boolean cacheEnabled = true;

    protected MiniCatalog<T> getMiniCatalog() {
        return null;
    }

    protected String getSampleDataPath() {
        return null;
    }

    public void setCacheEnabled(boolean enabled) {
        this.cacheEnabled = enabled;
    }

    public boolean isUsingFallback() {
        return usingFallback;
    }

    @Override
    public T load(String id) throws Exception {
        if (cacheEnabled) {
            T cached = localCache.get(id);
            if (cached != null)
                return cached;
        }

        try {
            T obj = loadFromSource(id);
            if (obj != null) {
                usingFallback = false;
                if (cacheEnabled)
                    localCache.put(id, obj);
                return obj;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load '" + id + "': " + e.getMessage() + ". Using fallback.");
        }

        T fallbackObj = loadFromFallback(id);
        if (fallbackObj != null) {
            usingFallback = true;
            LOGGER.info("Using embedded sample data for '" + id + "'");
            if (cacheEnabled)
                localCache.put(id, fallbackObj);
            return fallbackObj;
        }

        throw new Exception("Could not load '" + id + "' from any source");
    }

    public List<T> loadAll() {
        try {
            List<T> items = loadAllFromSource();
            if (items != null && !items.isEmpty()) {
                usingFallback = false;
                return items;
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to load all from source. Using fallback.");
        }

        MiniCatalog<T> catalog = getMiniCatalog();
        if (catalog != null) {
            usingFallback = true;
            return catalog.getAll();
        }
        return List.of();
    }

    protected abstract T loadFromSource(String id) throws Exception;

    protected List<T> loadAllFromSource() throws Exception {
        return null;
    }

    protected T loadFromFallback(String id) {
        MiniCatalog<T> catalog = getMiniCatalog();
        if (catalog != null) {
            return catalog.findByName(id).orElse(null);
        }
        String samplePath = getSampleDataPath();
        if (samplePath != null) {
            try (InputStream is = getClass().getResourceAsStream(samplePath)) {
                if (is != null)
                    return loadFromInputStream(is, id);
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Could not load sample data", e);
            }
        }
        return null;
    }

    protected T loadFromInputStream(InputStream is, String id) throws Exception {
        return null;
    }

    public void clearCache() {
        localCache.clear();
    }
}
