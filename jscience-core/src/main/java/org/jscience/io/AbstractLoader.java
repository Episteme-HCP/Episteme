/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * Abstract base class for data loaders with built-in caching, fallback to
 * embedded sample data (MiniCatalog), and logging.
 * <p>
 * Loading strategy:
 * <ol>
 * <li>Check local cache</li>
 * <li>Try to load from primary source (API/file)</li>
 * <li>If unavailable, fallback to embedded MiniCatalog sample data</li>
 * <li>Log warning when using fallback</li>
 * </ol>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class AbstractLoader<T> implements InputLoader<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractLoader.class.getName());

    private final ConcurrentHashMap<String, T> localCache = new ConcurrentHashMap<>();
    private boolean cacheEnabled = true;
    private boolean usingFallback = false;

    /**
     * Returns the base path where this loader's resources are located.
     */
    @Override
    public abstract String getResourcePath();

    /**
     * Returns the type of resource this loader produces.
     */
    @Override
    public abstract Class<T> getResourceType();

    /**
     * Returns the embedded MiniCatalog for fallback data.
     * Subclasses should override this to provide sample data.
     *
     * @return a MiniCatalog with sample data, or null if not available
     */
    protected MiniCatalog<T> getMiniCatalog() {
        return null;
    }

    /**
     * Returns the path to embedded sample data resource.
     * Override this to provide fallback sample data file path.
     *
     * @return resource path for sample data, or null
     */
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
        // 1. Check cache first
        if (cacheEnabled) {
            T cached = localCache.get(id);
            if (cached != null) {
                return cached;
            }
        }

        // 2. Try primary source
        try {
            T obj = loadFromSource(id);
            if (obj != null) {
                usingFallback = false;
                if (cacheEnabled) {
                    localCache.put(id, obj);
                }
                return obj;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,
                    "Failed to load '" + id + "' from primary source: " + e.getMessage() +
                            ". Falling back to embedded sample data.");
        }

        // 3. Fallback to MiniCatalog
        T fallbackObj = loadFromFallback(id);
        if (fallbackObj != null) {
            usingFallback = true;
            LOGGER.log(Level.INFO, "Using embedded sample data for '" + id + "'");
            if (cacheEnabled) {
                localCache.put(id, fallbackObj);
            }
            return fallbackObj;
        }

        throw new Exception("Could not load '" + id + "' from any source");
    }

    /**
     * Loads all available items, falling back to MiniCatalog if primary fails.
     *
     * @return list of all items
     */
    public List<T> loadAll() {
        try {
            List<T> items = loadAllFromSource();
            if (items != null && !items.isEmpty()) {
                usingFallback = false;
                return items;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,
                    "Failed to load all from primary source: " + e.getMessage() +
                            ". Falling back to embedded sample data.");
        }

        // Fallback to MiniCatalog
        MiniCatalog<T> catalog = getMiniCatalog();
        if (catalog != null) {
            usingFallback = true;
            LOGGER.log(Level.INFO, "Using embedded MiniCatalog with " + catalog.size() + " items");
            return catalog.getAll();
        }

        return List.of();
    }

    /**
     * Loads a single item from the primary source.
     * Subclasses must implement this.
     */
    protected abstract T loadFromSource(String id) throws Exception;

    /**
     * Loads all items from the primary source.
     * Subclasses should override if batch loading is supported.
     */
    protected List<T> loadAllFromSource() throws Exception {
        return null;
    }

    /**
     * Loads from fallback (MiniCatalog or embedded sample data).
     */
    protected T loadFromFallback(String id) {
        // Try MiniCatalog first
        MiniCatalog<T> catalog = getMiniCatalog();
        if (catalog != null) {
            return catalog.findByName(id).orElse(null);
        }

        // Try sample data path
        String samplePath = getSampleDataPath();
        if (samplePath != null) {
            try (InputStream is = getClass().getResourceAsStream(samplePath)) {
                if (is != null) {
                    return loadFromInputStream(is, id);
                }
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Could not load sample data from " + samplePath, e);
            }
        }

        return null;
    }

    /**
     * Parses an item from an InputStream.
     * Subclasses can override for custom parsing.
     */
    protected T loadFromInputStream(InputStream is, String id) throws Exception {
        return null;
    }

    public void clearCache() {
        localCache.clear();
    }
}
