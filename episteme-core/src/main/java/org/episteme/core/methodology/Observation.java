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

package org.episteme.core.methodology;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.UUIDIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;

/**
 * Represents a captured data point during an experiment.
 * 
 * @param <T> The data type of the observed value.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Observation<T> implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Instant timestamp;

    @Attribute
    private final T value;

    @Attribute
    private String tag;

    @Attribute
    private final Map<String, Object> traits;

    public Observation(T value) {
        this(Instant.now(), value, "default", new HashMap<>());
    }

    public Observation(Instant timestamp, T value, String tag, Map<String, Object> traits) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        this.timestamp = Objects.requireNonNull(timestamp);
        this.value = Objects.requireNonNull(value);
        this.tag = tag;
        this.traits = traits != null ? new HashMap<>(traits) : new HashMap<>();
    }

    @Override
    public Identification getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public T getValue() {
        return value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public static <T> Observation<T> of(T value) {
        return new Observation<>(value);
    }

    @Override
    public String toString() {
        return String.format("Observation{timestamp=%s, value=%s, tag='%s'}", timestamp, value, tag);
    }
}

