package org.jscience.io;

import java.util.*;

/**
 * Basic implementation of MiniCatalog for static resource registration.
 */
public final class BasicMiniCatalog<T> implements MiniCatalog<T> {
    
    private final Map<String, T> entries = new LinkedHashMap<>();

    public void register(String name, T entry) {
        entries.put(name, entry);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(entries.values());
    }

    @Override
    public Optional<T> findByName(String name) {
        return Optional.ofNullable(entries.get(name));
    }

    @Override
    public int size() {
        return entries.size();
    }
}
