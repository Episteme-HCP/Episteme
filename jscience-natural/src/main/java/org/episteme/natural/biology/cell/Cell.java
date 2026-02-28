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

package org.episteme.natural.biology.cell;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.episteme.natural.biology.DNA;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents the fundamental structural and functional unit of life.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Cell implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private DNA dna;

    @Attribute
    private final Set<Organelle> organelles = EnumSet.noneOf(Organelle.class);

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Metabolism metabolism;

    public Cell(String type) {
        this.id = new SimpleIdentification("Cell:" + UUID.randomUUID());
        setName(Objects.requireNonNull(type, "Cell type cannot be null"));
        this.metabolism = new Metabolism();
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public DNA getDna() {
        return dna;
    }

    public void setDna(DNA dna) {
        this.dna = dna;
    }

    public Set<Organelle> getOrganelles() {
        return organelles;
    }

    public void addOrganelle(Organelle organelle) {
        organelles.add(Objects.requireNonNull(organelle));
    }

    public Metabolism getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(Metabolism metabolism) {
        this.metabolism = metabolism;
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %s]", getName(), id);
    }
}


