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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

/**
 * Represents a vitamin with nutritional data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Vitamin implements Serializable {
    private static final long serialVersionUID = 2L;

    @Attribute
    public final String id;
    @Attribute
    public final String name;
    @Attribute
    public final List<String> aliases;
    @Attribute
    public final String solubility;
    @Attribute
    public final Real rdaAdult;
    @Attribute
    public final String rdaUnit;
    @Attribute
    public final List<String> sources;
    @Attribute
    public final List<String> functions;

    public Vitamin(String id, String name, List<String> aliases, String solubility,
            Real rdaAdult, String rdaUnit, List<String> sources, List<String> functions) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.aliases = aliases != null ? aliases : Collections.emptyList();
        this.solubility = solubility;
        this.rdaAdult = rdaAdult;
        this.rdaUnit = rdaUnit;
        this.sources = sources != null ? sources : Collections.emptyList();
        this.functions = functions != null ? functions : Collections.emptyList();
    }

    public Vitamin(String id, String name, List<String> aliases, String solubility,
            double rdaAdult, String rdaUnit, List<String> sources, List<String> functions) {
        this(id, name, aliases, solubility, Real.of(rdaAdult), rdaUnit, sources, functions);
    }

    public boolean isFatSoluble() {
        return "fat".equals(solubility);
    }

    public boolean isWaterSoluble() {
        return "water".equals(solubility);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - RDA: %s %s, %s-soluble",
                name, id, rdaAdult, rdaUnit, solubility);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vitamin vitamin)) return false;
        return Objects.equals(id, vitamin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

