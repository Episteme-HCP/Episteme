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
