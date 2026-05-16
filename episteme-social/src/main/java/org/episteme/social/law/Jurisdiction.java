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

package org.episteme.social.law;

import org.episteme.natural.earth.Place;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a legal jurisdiction, defined by its name, territory, and set of applicable statutes.
 * Jurisdictions can be hierarchical (e.g., a state jurisdiction within a federal jurisdiction).
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Jurisdiction implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place territory;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Statute> statutes;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Jurisdiction parentJurisdiction; // e.g., State in a Country

    public Jurisdiction(String name, Place territory) {
        this(name, territory, null);
    }

    public Jurisdiction(String name, Place territory, Jurisdiction parentJurisdiction) {
        this.id = new SimpleIdentification("Jurisdiction:" + UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        this.territory = territory;
        this.parentJurisdiction = parentJurisdiction;
        this.statutes = new HashSet<>();
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public Place getTerritory() {
        return territory;
    }

    public Jurisdiction getParentJurisdiction() {
        return parentJurisdiction;
    }

    public void addStatute(Statute statute) {
        if (statute != null) {
            statutes.add(statute);
        }
    }

    public Set<Statute> getStatutes() {
        return Collections.unmodifiableSet(statutes);
    }

    /**
     * Checks if a statute is applicable in this jurisdiction (including inherited).
     */
    public boolean isApplicable(Statute statute) {
        if (statutes.contains(statute))
            return true;
        if (parentJurisdiction != null)
            return parentJurisdiction.isApplicable(statute);
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jurisdiction that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}

