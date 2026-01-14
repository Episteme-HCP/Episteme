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

import org.jscience.util.Commented;

import java.util.Collections;
import java.util.Map;


/**
 * A class representing the events that happen during a lifetime in an
 * ordonned manner. It is not meant to be as readble as a real biography but
 * to store the useful information.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//from a Biography, you should be able to rebuilt the Set of Events (see org.jscience.psychology.events).
//However, note that a Biography is considering a whole lifetime with general events and may NOT be suitable to represent the much fine grained day life activities supported by Events.
public class Biography extends Object implements Commented {
    /** DOCUMENT ME! */
    private Map timelines;

    /** DOCUMENT ME! */
    private String comments;

    //sets a new timeline for each of the default categories
    /**
     * Creates a new Biography object.
     */
    public Biography() {
        timelines = Collections.EMPTY_MAP;
        timelines.put(HumanTimeline.HOME, new HumanTimeline(HumanTimeline.HOME));
        timelines.put(HumanTimeline.SOCIAL,
            new HumanTimeline(HumanTimeline.SOCIAL));
        timelines.put(HumanTimeline.SEX, new HumanTimeline(HumanTimeline.SEX));
        timelines.put(HumanTimeline.FRIENDS,
            new HumanTimeline(HumanTimeline.FRIENDS));
        timelines.put(HumanTimeline.LEISURE,
            new HumanTimeline(HumanTimeline.LEISURE));
        timelines.put(HumanTimeline.HEALTH,
            new HumanTimeline(HumanTimeline.HEALTH));
        timelines.put(HumanTimeline.AWE, new HumanTimeline(HumanTimeline.AWE));
        timelines.put(HumanTimeline.RELIGIOUS,
            new HumanTimeline(HumanTimeline.RELIGIOUS));
        timelines.put(HumanTimeline.PERSONAL,
            new HumanTimeline(HumanTimeline.PERSONAL));
        timelines.put(HumanTimeline.EMOTION,
            new HumanTimeline(HumanTimeline.EMOTION));
        timelines.put(HumanTimeline.WORK, new HumanTimeline(HumanTimeline.WORK));
        timelines.put(HumanTimeline.MONEY,
            new HumanTimeline(HumanTimeline.MONEY));
    }

    /**
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HumanTimeline getTimeline(String category) {
        return (HumanTimeline) timelines.get(category);
    }

    /**
     * DOCUMENT ME!
     *
     * @param timeline DOCUMENT ME!
     */
    public void addTimeline(HumanTimeline timeline) {
        timelines.put(timeline.getCategory(), timeline);
    }

    //you cannot remove timelines
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    //you could also set up a timeline
    /**
     * DOCUMENT ME!
     */
    public void setComments() {
        this.comments = comments;
    }
}
