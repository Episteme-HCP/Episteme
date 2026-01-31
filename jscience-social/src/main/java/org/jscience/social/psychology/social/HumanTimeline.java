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

import org.jscience.social.history.Timeline;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import java.util.Objects;

/**
 * A specialized timeline for tracking events in a specific category of human life.
 * Used for social, personal, and psychological history tracking.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
@SuppressWarnings("rawtypes")
public class HumanTimeline extends Timeline {

    private static final long serialVersionUID = 1L;

    /** Residence and living conditions. */
    public final static String HOME = "Home";

    /** Marital status and social standing. */
    public final static String SOCIAL = "Social";

    /** Sexual behavior and partnerships. */
    public final static String SEX = "Sex";

    /** Friendship networks. */
    public final static String FRIENDS = "Friends";

    /** Hobbies and free time activities. */
    public final static String LEISURE = "Leisure";

    /** Physical and mental health state. */
    public final static String HEALTH = "Health";

    /** Charisma, social desire, or aura. */
    public final static String AWE = "Awe";

    /** Religious conviction and faith. */
    public final static String RELIGIOUS = "Religious";

    /** Personal milestones and growth. */
    public final static String PERSONAL = "Personal";

    /** Emotional state and developments. */
    public final static String EMOTION = "Emotion";

    /** Career, employment, and education. */
    public final static String WORK = "Work";

    /** Financial status and consuming power. */
    public final static String MONEY = "Money";

    @Attribute
    private final String category;

    /**
     * Creates a new HumanTimeline for a specific category.
     *
     * @param category the category name
     * @throws NullPointerException if category is null
     * @throws IllegalArgumentException if category is empty
     */
    public HumanTimeline(String category) {
        Objects.requireNonNull(category, "Category cannot be null");
        if (category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category;
    }

    /**
     * Returns the category of this timeline.
     *
     * @return the category name
     */
    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HumanTimeline that)) return false;
        return Objects.equals(category, that.category) && super.getEvents().equals(that.getEvents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, super.getEvents());
    }
}

