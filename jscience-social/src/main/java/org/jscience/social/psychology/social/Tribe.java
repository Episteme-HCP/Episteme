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

package org.jscience.social.psychology.social;

import org.jscience.natural.earth.Place;

import org.jscience.social.sociology.Culture;




import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Relation;

import java.util.Objects;

/**
 * Represents a human tribe, which is an organized human group with a distinct name and culture.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Tribe extends HumanGroup {
    /** The name of the tribe. */
    @Attribute
    private String name;

    /** The distinct culture associated with the tribe. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Culture culture;

    /**
     * Initializes a new Tribe instance.
     *
     * @param name    the name of the tribe
     * @param culture the culture of the tribe
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if name is empty
     */
    public Tribe(String name, Culture culture) {
        if (Objects.requireNonNull(name, "Name cannot be null").isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
    }

    /**
     * Initializes a new Tribe instance with a specific territory.
     *
     * @param name            the name of the tribe
     * @param formalTerritory the territory assigned to or claimed by the tribe
     * @param culture         the culture of the tribe
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if name is empty
     */
    public Tribe(String name, Place formalTerritory, Culture culture) {
        super(Objects.requireNonNull(formalTerritory, "Territory cannot be null"));
        if (Objects.requireNonNull(name, "Name cannot be null").isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
    }

    /**
     * Returns the name of the tribe.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the tribe.
     *
     * @param name the new name
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public void setName(String name) {
        if (Objects.requireNonNull(name, "Name cannot be null").isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    /**
     * Returns the culture associated with the tribe.
     *
     * @return the culture
     */
    public Culture getCulture() {
        return culture;
    }

    /**
     * Updates the tribe's culture.
     *
     * @param culture the new culture
     * @throws NullPointerException if culture is null
     */
    public void setCulture(Culture culture) {
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
    }
}

