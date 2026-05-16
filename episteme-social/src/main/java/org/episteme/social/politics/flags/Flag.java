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

package org.episteme.social.politics.flags;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.episteme.core.util.Commented;
import org.episteme.core.util.Named;

/**
 * Represents a flag as a symbolic and visual identifier for a political or social entity.
 * This class stores both the physical image and the structural/symbolic data (colors, symbols, meaning).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Flag implements Named, Commented, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final transient Image image;
    private final List<Color> colors;
    private final Set<String> symbols;
    private final String meaning;
    private final double aspectRatio; // width / height

    /**
     * Creates a new flag.
     *
     * @param name        the name of the flag (e.g., "Tricolore", "Stars and Stripes")
     * @param image       the visual representation
     * @param colors      the primary colors used in the flag
     * @param symbols     the key symbols present on the flag (e.g., "Star", "Crescent")
     * @param meaning     the symbolic meaning or history of the design
     * @param aspectRatio the standard width-to-height ratio (e.g., 1.5 for 3:2)
     */
    public Flag(String name, Image image, List<Color> colors, Set<String> symbols, String meaning, double aspectRatio) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.image = image;
        this.colors = new ArrayList<>(Objects.requireNonNull(colors, "Colors list cannot be null"));
        this.symbols = new HashSet<>(Objects.requireNonNull(symbols, "Symbols set cannot be null"));
        this.meaning = Objects.requireNonNull(meaning, "Meaning cannot be null");
        this.aspectRatio = aspectRatio > 0 ? aspectRatio : 1.5;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getComments() {
        return meaning;
    }

    /**
     * Returns the visual representation of the flag.
     * @return the image, or null if not unmanaged
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns an unmodifiable list of the primary colors.
     * @return the colors
     */
    public List<Color> getColors() {
        return Collections.unmodifiableList(colors);
    }

    /**
     * Returns an unmodifiable set of the symbols.
     * @return the symbols (strings representing names of symbols)
     */
    public Set<String> getSymbols() {
        return Collections.unmodifiableSet(symbols);
    }

    /**
     * Returns the symbolic meaning of the flag.
     * @return the meaning string
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * Returns the standard width-to-height ratio.
     * @return the aspect ratio
     */
    public double getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public String toString() {
        return String.format("Flag: %s (Ratio: %.2f, Symbols: %s)", name, aspectRatio, symbols);
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return java.util.Collections.emptyMap();
    }
}

