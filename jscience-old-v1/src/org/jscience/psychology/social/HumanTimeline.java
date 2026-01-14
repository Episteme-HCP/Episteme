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

package org.jscience.psychology.social;

import org.jscience.history.Timeline;

import java.util.Set;


/**
 * A class representing a Set of events.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class HumanTimeline extends Timeline {
    //predefined categories
    /** DOCUMENT ME! */
    public final static String HOME = "Home"; //where you live

    /** DOCUMENT ME! */
    public final static String SOCIAL = "Social"; //marital status...

    /** DOCUMENT ME! */
    public final static String SEX = "Sex"; //the people you have sex with

    /** DOCUMENT ME! */
    public final static String FRIENDS = "Friends";

    /** DOCUMENT ME! */
    public final static String LEISURE = "Leisure";

    /** DOCUMENT ME! */
    public final static String HEALTH = "Health";

    /** DOCUMENT ME! */
    public final static String AWE = "Awe"; //the desire that people feel when meeting this individual for the first time, time to lose, killing look

    /** DOCUMENT ME! */
    public final static String RELIGIOUS = "Religious"; //conviction, faith (which rarely changes)

    /** DOCUMENT ME! */
    public final static String PERSONAL = "Personal";

    /** DOCUMENT ME! */
    public final static String EMOTION = "Emotion";

    /** DOCUMENT ME! */
    public final static String WORK = "Work"; //where you work, including school

    /** DOCUMENT ME! */
    public final static String MONEY = "Money"; //life level, consuming power

    /** DOCUMENT ME! */
    private String category;

    /** DOCUMENT ME! */
    private Set events;

/**
     * Creates a new HumanTimeline object.
     *
     * @param category DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public HumanTimeline(String category) {
        if ((category != null) && (category.length() > 0)) {
            this.category = category;
        } else {
            throw new IllegalArgumentException(
                "The category of a HumanTimeline can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCategory() {
        return category;
    }
}
