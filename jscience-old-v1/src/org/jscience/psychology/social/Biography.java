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
