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

package org.jscience.social.sociology;

import java.util.Date;
import java.util.Objects;
import org.jscience.social.economics.WorkSituation;
import org.jscience.natural.earth.Place;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a cultural or religious celebration (feast, rite of passage, festival).
 * Celebrations are specialized social situations with identifying motives and designated participants.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Celebration extends WorkSituation {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Place place;

    @Attribute
    private CelebrationKind kind;

    @Attribute
    private Date date;

    /**
     * Creates a new Celebration.
     *
     * @param name     the name of the festival or event
     * @param comments descriptive details
     * @param kind     the category of celebration
     * @param date     the scheduled occurrence date
     * @throws NullPointerException if mandatory arguments are null
     */
    public Celebration(String name, String comments, CelebrationKind kind, Date date) {
        super(name, comments);
        this.kind = Objects.requireNonNull(kind, "Celebration kind cannot be null");
        this.date = Objects.requireNonNull(date, "Celebration date cannot be null");
    }

    /** Legacy constructor. */
    public Celebration(String name) {
        this(name, "", CelebrationKind.OTHER, new Date());
    }

    @Override
    public Place getPosition() {
        return place;
    }

    @Override
    public void setPosition(Place place) {
        this.place = place;
    }

    /**
     * Returns the categorical kind of this celebration.
     * @return the celebration kind
     */
    public CelebrationKind getKind() {
        return kind;
    }

    public void setKind(CelebrationKind kind) {
        this.kind = Objects.requireNonNull(kind);
    }

    /**
     * Returns the date when the celebration occurs.
     * @return the occurrence date
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = Objects.requireNonNull(date);
    }

    @Override
    public String toString() {
        return getName() + " [" + date + "] (" + kind + ")";
    }
}

