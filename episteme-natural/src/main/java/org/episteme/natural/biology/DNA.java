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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;

/**
 * Represents a Deoxyribonucleic acid (DNA) strand.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class DNA implements Cloneable, ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private final List<Base> bases;

    public DNA(List<Base> bases) {
        this.id = new SimpleIdentification("DNA:" + UUID.randomUUID());
        setName("DNA Strand (" + bases.size() + " bases)");
        if (bases.contains(Base.URACIL)) {
            throw new IllegalArgumentException("DNA cannot contain Uracil.");
        }
        this.bases = new ArrayList<>(bases);
    }

    public DNA(Base... bases) {
        this(Arrays.asList(bases));
    }

    /**
     * Parses a DNA string (e.g. "ATCG").
     * 
     * @param sequence the string representation
     */
    public DNA(String sequence) {
        this.id = new SimpleIdentification("DNA:" + UUID.randomUUID());
        setName("DNA Strand (" + sequence.length() + " bases)");
        this.bases = new ArrayList<>();
        for (char c : sequence.toUpperCase().toCharArray()) {
            switch (c) {
                case 'A':
                    bases.add(Base.ADENINE);
                    break;
                case 'T':
                    bases.add(Base.THYMINE);
                    break;
                case 'C':
                    bases.add(Base.CYTOSINE);
                    break;
                case 'G':
                    bases.add(Base.GUANINE);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid DNA base: " + c);
            }
        }
    }

    public List<Base> getBases() {
        return Collections.unmodifiableList(bases);
    }

    public int getLength() {
        return bases.size();
    }

    /**
     * Returns the complementary DNA strand.
     */
    public DNA getComplementary() {
        List<Base> compl = new ArrayList<>(bases.size());
        for (Base b : bases) {
            compl.add(b.getComplementary(false));
        }
        return new DNA(compl);
    }

    /**
     * Transcribes this DNA to mRNA.
     * (Complementary, but T -> U).
     */
    public RNA transcribe() {
        List<Base> rnaBases = new ArrayList<>(bases.size());
        for (Base b : bases) {
            rnaBases.add(b.getComplementary(true));
        }
        return new RNA(rnaBases);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Base b : bases) {
            sb.append(b.name().charAt(0));
        }
        return sb.toString();
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }
}

