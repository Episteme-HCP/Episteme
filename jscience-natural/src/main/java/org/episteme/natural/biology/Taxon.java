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

package org.episteme.natural.biology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Represents a taxonomic group in a phylogenetic tree.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Taxon implements ComprehensiveIdentification {
    
    private static final long serialVersionUID = 1L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private final String parentId;
    
    @Attribute
    private final List<Taxon> children = new ArrayList<>();
    
    @Attribute
    private final Real coi;
    
    @Attribute
    private final Real rna16s;
    
    @Attribute
    private final Real cytb;
    
    // Layout properties (transient)
    public transient double x, y, angle, radius;

    public Taxon(String id, String parentId, String name, Real coi, Real rna16s, Real cytb) {
        this.id = new SimpleIdentification(id);
        setName(name);
        this.parentId = parentId;
        this.coi = coi;
        this.rna16s = rna16s;
        this.cytb = cytb;
    }

    public Taxon(String id, Object parentId, String name, Real coi, Real rna16s, Real cytb) {
        this(id, parentId != null ? parentId.toString() : "", name, coi, rna16s, cytb);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public void addChild(Taxon t) {
        children.add(t);
    }
    
    public List<Taxon> getChildren() { return children; }
    
    public String getParentId() { return parentId; }
    public Real getCoi() { return coi; }
    public Real getRna16s() { return rna16s; }
    public Real getCytb() { return cytb; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Taxon taxon)) return false;
        return Objects.equals(id, taxon.id);
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

