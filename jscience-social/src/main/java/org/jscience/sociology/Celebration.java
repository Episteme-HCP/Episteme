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

package org.jscience.sociology;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.jscience.economics.WorkSituation;
import org.jscience.geography.Place;
import org.jscience.util.Positioned;

/**
 * Represents a cultural or religious celebration (feast, rite of passage, festival).
 * Celebrations are specialized social situations with identifying motives and designated participants.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class Celebration extends WorkSituation implements Positioned<Place>, Serializable {

    private static final long serialVersionUID = 1L;

    /** Rite of passage for physical birth. */
    public final static int BIRTH = 1;
    /** Ceremonial entry into a society or group. */
    public final static int INITIATION = 2;
    /** Ceremonial recognition of physical maturity. */
    public final static int PUBERTY = 4;
    /** Recognition of legal or social maturity. */
    public final static int SOCIAL_ADULTHOOD = 8;
    /** Ceremonial recognition of educational achievement. */
    public final static int GRADUATION = 16;
    /** Religious rite of immersion or naming. */
    public final static int BAPTISM = 32;
    /** Formal union of individuals. */
    public final static int MARRIAGE = 64;
    /** Recognition of biological termination. */
    public final static int DEATH = 128;
    /** Ceremonious disposal of a body. */
    public final static int BURIAL = 256;
    /** Celebration of conflict cessation. */
    public final static int PEACE = 512;
    /** Celebration of a specific success or conquest. */
    public final static int VICTORY = 1024;
    /** Formal declaration of hostilities. */
    public final static int WAR = 2048;
    /** Ceremony of sovereign investiture. */
    public final static int CORONATION = 4096;
    /** Solar-oriented festival (solstice, equinox). */
    public final static int SUN = 8192;
    /** Lunar-oriented festival. */
    public final static int MOON = 16384;
    /** Earth or agricultural festival (harvest). */
    public final static int EARTH = 32768;
    /** General seasonal celebration. */
    public final static int SEASON = 65536;
    /** Celebration of specific personal or group achievement. */
    public final static int SUCCESS = 131072;
    /** Purely recreational or festive celebration. */
    public final static int PLEASURE = 262144;
    /** Commercial or trade-related fair. */
    public final static int BUSINESS = 524288;
    /** Liturgical or holy day defined by a calendar. */
    public final static int CALENDAR_SPECIFIC = 1048576;
    /** Celebration of a specific modern event (grand opening). */
    public final static int EVENT_SPECIFIC = 2097152;
    /** Unclassified cultural event. */
    public final static int OTHER = 4194304;

    private Place place;
    private final int kind;
    private final Date date;

    /**
     * Creates a new Celebration.
     *
     * @param name     the name of the festival or event
     * @param comments descriptive details
     * @param kind     the category of celebration
     * @param date     the scheduled occurrence date
     * @throws NullPointerException if mandatory arguments are null
     */
    public Celebration(String name, String comments, int kind, Date date) {
        super(name, comments);
        this.kind = kind;
        this.date = Objects.requireNonNull(date, "Celebration date cannot be null");
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
     * @return the kind constant
     */
    public int getKind() {
        return kind;
    }

    /**
     * Returns the date when the celebration occurs.
     * @return the occurrence date
     */
    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return getName() + " [" + date + "]";
    }
}
