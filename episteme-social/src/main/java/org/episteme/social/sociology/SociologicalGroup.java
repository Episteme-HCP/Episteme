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

package org.episteme.social.sociology;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.episteme.core.util.identity.UUIDIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;
import org.episteme.natural.biology.Individual;
import org.episteme.natural.biology.HomoSapiens;
import org.episteme.natural.biology.ecology.Population;
import org.episteme.natural.engineering.eventdriven.EventDrivenEngine;
import org.episteme.natural.engineering.eventdriven.Event;

/**
 * Represents a formal or informal social SociologicalGroup of individuals.
 * Refactored to extend Population<Person> to leverage demographic features.
 */
@Persistent
public class SociologicalGroup extends Population<Person> {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Map<Individual, String> internalRoles = new HashMap<>();

    @Attribute
    private GroupKind kind;

    public SociologicalGroup(String name, GroupKind kind) {
        this(name, kind, null);
    }

    public SociologicalGroup(String name, GroupKind kind, EventDrivenEngine engine) {
        super(new UUIDIdentification(UUID.randomUUID()), name, HomoSapiens.SPECIES, null, engine);
        this.kind = Objects.requireNonNull(kind, "Kind cannot be null");
    }

    public GroupKind getKind() {
        return kind;
    }

    public void setKind(GroupKind kind) {
        this.kind = Objects.requireNonNull(kind);
    }

    public void assignRole(Individual member, String role) {
        if (members.contains(member)) {
            internalRoles.put(member, role);
        }
    }

    public String getRole(Individual member) {
        return internalRoles.get(member);
    }

    public Map<Individual, String> getInternalRoles() {
        return Collections.unmodifiableMap(internalRoles);
    }

    @Override
    public void processEvent(Event event) {
        // Default: handle SociologicalGroup-specific events or delegate to population
        super.processEvent(event);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d members)", getName(), kind, size());
    }
}
