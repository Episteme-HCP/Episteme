/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui;

import java.util.function.Consumer;

/**
 * Metadata for a scientific parameter.
 */
public class Parameter<T> {
    private final String name;
    private final String description;
    private final T defaultValue;
    private T value;
    private final Consumer<T> onValueChange;

    public Parameter(String name, String description, T defaultValue, Consumer<T> onValueChange) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.onValueChange = onValueChange;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        if (onValueChange != null) {
            onValueChange.accept(value);
        }
    }
}
