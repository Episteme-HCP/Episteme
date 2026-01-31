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

package org.jscience.social.arts;

import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.natural.earth.Place;
import java.util.Objects;

/**
 * Represents a visual artwork such as a painting, drawing, or photograph.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Picture extends Artwork {

    public enum Medium {
        OIL, WATERCOLOR, CHARCOAL, PENCIL, ACRYLIC, DIGITAL, PHOTOGRAPH, OTHER
    }

    public enum Support {
        CANVAS, WOOD, PAPER, FRESCO, WALL, COPPER, GLASS, DIGITAL
    }

    private final Medium medium;
    private final Support support;
    private double widthCm;
    private double heightCm;

    public Picture(String name, String description, TimeCoordinate productionDate, 
                   Place productionPlace, ArtForm category, Medium medium, Support support) {
        super(name, description, productionDate, productionPlace, category);
        this.medium = Objects.requireNonNull(medium, "Medium cannot be null");
        this.support = Objects.requireNonNull(support, "Support cannot be null");
    }

    public Picture(String name, TimeCoordinate productionDate, Medium medium) {
        this(name, "", productionDate, null, ArtForm.PAINTING, medium, Support.CANVAS);
    }

    public Medium getMedium() {
        return medium;
    }

    public Support getSupport() {
        return support;
    }

    public double getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(double widthCm) {
        this.widthCm = widthCm;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    @Override
    public String toString() {
        return String.format("%s (%s on %s)", getName(), medium, support);
    }
}

