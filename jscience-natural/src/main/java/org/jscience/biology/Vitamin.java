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

package org.jscience.biology;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a vitamin with nutritional data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Vitamin implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String id;
    public final String name;
    public final List<String> aliases;
    public final String solubility;
    public final double rdaAdult;
    public final String rdaUnit;
    public final List<String> sources;
    public final List<String> functions;

    public Vitamin(String id, String name, List<String> aliases, String solubility,
            double rdaAdult, String rdaUnit, List<String> sources, List<String> functions) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.aliases = aliases != null ? aliases : Collections.emptyList();
        this.solubility = solubility;
        this.rdaAdult = rdaAdult;
        this.rdaUnit = rdaUnit;
        this.sources = sources != null ? sources : Collections.emptyList();
        this.functions = functions != null ? functions : Collections.emptyList();
    }

    public boolean isFatSoluble() {
        return "fat".equals(solubility);
    }

    public boolean isWaterSoluble() {
        return "water".equals(solubility);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - RDA: %.1f %s, %s-soluble",
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
