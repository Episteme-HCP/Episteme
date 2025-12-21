/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.io;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * This class represents a generic loader with built-in caching and
 * local backup capabilities.
 * </p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @param <T> the type of object to load.
 */
public abstract class AbstractLoader<T> implements ResourceLoader<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractLoader.class.getName());

    private final ConcurrentHashMap<String, T> _localCache = new ConcurrentHashMap<>();

    private boolean _cacheEnabled = true;
    private String _backupPath = null;

    /**
     * Returns the base path where this loader's resources are located.
     * Subclasses must implement this to enable resource auditing.
     *
     * @return the resource path (e.g., "/org/jscience/chemistry/")
     */
    @Override
    public abstract String getResourcePath();

    /**
     * Returns the type of resource this loader produces.
     * Subclasses must implement this for type-safe loading.
     *
     * @return the Class object for type T
     */
    @Override
    public abstract Class<T> getResourceType();

    public void setCacheEnabled(boolean enabled) {
        this._cacheEnabled = enabled;
    }

    public void setBackupPath(String path) {
        this._backupPath = path;
    }

    public T load(String id) throws Exception {
        if (_cacheEnabled) {
            T obj = _localCache.get(id);
            if (obj != null) {
                return obj;
            }
        }

        try {
            T obj = loadFromSource(id);
            if (obj != null) {
                if (_cacheEnabled)
                    _localCache.put(id, obj);
                return obj;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load " + id + " from source, trying backup...", e);
        }

        T obj = loadFromBackup(id);
        if (obj != null) {
            if (_cacheEnabled)
                _localCache.put(id, obj);
            return obj;
        }

        throw new Exception("Could not load " + id);
    }

    protected abstract T loadFromSource(String id) throws Exception;

    protected T loadFromBackup(String id) {
        if (_backupPath == null)
            return null;

        try {
            String resourceName = _backupPath + (id.endsWith(".xml") || id.endsWith(".json") ? id : id + ".xml");
            URL url = getClass().getResource(resourceName);
            if (url == null)
                return null;
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void clearCache() {
        _localCache.clear();
    }
}
