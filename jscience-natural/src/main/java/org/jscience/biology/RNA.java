/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Ribonucleic acid (RNA) strand.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RNA implements Serializable, Cloneable {

    private final List<Base> bases;

    public RNA(List<Base> bases) {
        if (bases.contains(Base.THYMINE)) {
            throw new IllegalArgumentException("RNA cannot contain Thymine.");
        }
        this.bases = new ArrayList<>(bases);
    }

    public RNA(Base... bases) {
        this(Arrays.asList(bases));
    }

    public RNA(String sequence) {
        this.bases = new ArrayList<>();
        for (char c : sequence.toUpperCase().toCharArray()) {
            switch (c) {
                case 'A':
                    bases.add(Base.ADENINE);
                    break;
                case 'U':
                    bases.add(Base.URACIL);
                    break;
                case 'C':
                    bases.add(Base.CYTOSINE);
                    break;
                case 'G':
                    bases.add(Base.GUANINE);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid RNA base: " + c);
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
     * Returns the complementary RNA strand.
     */
    public RNA getComplementary() {
        List<Base> compl = new ArrayList<>(bases.size());
        for (Base b : bases) {
            compl.add(b.getComplementary(true));
        }
        return new RNA(compl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Base b : bases) {
            sb.append(b.name().charAt(0));
        }
        return sb.toString();
    }
}
