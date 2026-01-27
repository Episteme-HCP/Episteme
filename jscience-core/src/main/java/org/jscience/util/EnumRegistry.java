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

package org.jscience.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Registry for ExtensibleEnum values.
 * <p>
 * Manages registration and lookup of enum-like values, supporting
 * both built-in and user-defined values.
 * </p>
 * <p>
 * Usage example:
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EnumRegistry<T extends ExtensibleEnum> {

    private static final Map<Class<? extends ExtensibleEnum>, EnumRegistry<?>> REGISTRIES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends ExtensibleEnum> EnumRegistry<T> getRegistry(Class<T> type) {
        return (EnumRegistry<T>) REGISTRIES.computeIfAbsent(type, k -> new EnumRegistry<>());
    }

    public static <T extends ExtensibleEnum> void register(Class<T> type, T value) {
        getRegistry(type).register(value);
    }

    public static <T extends ExtensibleEnum> T valueOf(Class<T> type, String name) {
        return getRegistry(type).valueOf(name);
    }

    public static <T extends ExtensibleEnum> List<T> values(Class<T> type) {
        return getRegistry(type).values();
    }

    private final Map<String, T> byName = new ConcurrentHashMap<>();
    private final Map<Integer, T> byOrdinal = new ConcurrentHashMap<>();
    private final AtomicInteger nextOrdinal = new AtomicInteger(0);
    private final List<T> valuesList = Collections.synchronizedList(new ArrayList<>());

    /**
     * Registers a new enum value.
     *
     * @param value the value to register
     * @throws IllegalArgumentException if name already registered
     */
    public void register(T value) {
        if (byName.containsKey(value.name())) {
            // Idempotent registration for static initializers
            return;
        }
        int ordinal = nextOrdinal.getAndIncrement();
        value.setOrdinal(ordinal);
        byName.put(value.name(), value);
        byOrdinal.put(ordinal, value);
        valuesList.add(value);
    }

    /**
     * Gets the next available ordinal for new values.
     */
    public int getNextOrdinal() {
        return nextOrdinal.getAndIncrement();
    }

    /**
     * Returns the value with the specified name.
     *
     * @param name the name
     * @return the value, or null if not found
     */
    public T valueOf(String name) {
        return byName.get(name);
    }

    /**
     * Returns the value with the specified name.
     *
     * @param name the name
     * @return the value
     * @throws IllegalArgumentException if not found
     */
    public T valueOfRequired(String name) {
        T value = byName.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Unknown value: " + name);
        }
        return value;
    }

    /**
     * Returns the value with the specified ordinal.
     */
    public T valueOf(int ordinal) {
        return byOrdinal.get(ordinal);
    }

    /**
     * Returns all registered values.
     */
    public List<T> values() {
        return Collections.unmodifiableList(new ArrayList<>(valuesList));
    }

    /**
     * Returns all registered names.
     */
    public Set<String> names() {
        return Collections.unmodifiableSet(byName.keySet());
    }

    /**
     * Returns the number of registered values.
     */
    public int size() {
        return valuesList.size();
    }

    /**
     * Checks if a name is registered.
     */
    public boolean contains(String name) {
        return byName.containsKey(name);
    }

    /**
     * Returns only built-in values.
     */
    public List<T> builtInValues() {
        return valuesList.stream()
                .filter(ExtensibleEnum::isBuiltIn)
                .toList();
    }

    /**
     * Returns only user-defined values.
     */
    public List<T> userDefinedValues() {
        return valuesList.stream()
                .filter(v -> !v.isBuiltIn())
                .toList();
    }
}


