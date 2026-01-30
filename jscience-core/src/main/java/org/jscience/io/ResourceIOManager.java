/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.io;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * Manager for Resource Readers and Writers.
 * Discovers implementations via SPI and selects the best one for a given file.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public final class ResourceIOManager {

    private ResourceIOManager() {}

    /**
     * Finds a suitable reader for the given resource and destination.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Optional<ResourceReader<T>> getReader(String resourceId, Class<T> type) {
        ServiceLoader<ResourceReader> loader = ServiceLoader.load(ResourceReader.class);
        return StreamSupport.stream(loader.spliterator(), false)
            .filter(r -> r.getResourceType().isAssignableFrom(type))
            .filter(r -> matchesExtension(resourceId, r))
            .map(r -> (ResourceReader<T>) r)
            .findFirst();
    }

    /**
     * Finds a suitable writer for the given resource and destination.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Optional<ResourceWriter<T>> getWriter(String destinationId, Class<T> type) {
        ServiceLoader<ResourceWriter> loader = ServiceLoader.load(ResourceWriter.class);
        return StreamSupport.stream(loader.spliterator(), false)
            .filter(w -> w.getResourceType().isAssignableFrom(type))
            .filter(w -> matchesExtension(destinationId, w))
            .map(w -> (ResourceWriter<T>) w)
            .findFirst();
    }

    private static boolean matchesExtension(String path, ResourceIO<?> io) {
        String p = path.toLowerCase();
        for (String ext : io.getSupportedExtensions()) {
            if (p.endsWith(ext.toLowerCase())) return true;
        }
        return false;
    }

    /**
     * Loads a resource of the specified type.
     */
    public static <T> T load(String resourceId, Class<T> type) throws Exception {
        ResourceReader<T> reader = getReader(resourceId, type)
            .orElseThrow(() -> new IllegalArgumentException("No suitable reader found for: " + resourceId));
        return reader.load(resourceId);
    }

    /**
     * Saves a resource to the specified destination.
     */
    public static <T> void save(T resource, String destinationId) throws Exception {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) resource.getClass();
        ResourceWriter<T> writer = getWriter(destinationId, type)
            .orElseThrow(() -> new IllegalArgumentException("No suitable writer found for: " + destinationId));
        writer.save(resource, destinationId);
    }
}
