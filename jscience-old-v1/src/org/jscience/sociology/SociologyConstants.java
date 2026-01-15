package org.jscience.sociology;

/**
 * A class defining some useful constants for the study of societies (from
 * a scientific point of view).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class SociologyConstants extends Object {
    //mutually exclusive
    //primitive occupation: task hierarchy (and money to a lesser extend)
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int YOUNG = 1; //youngs have no sexuallity and learn tasks from adults

    /** DOCUMENT ME! */
    public final static int WOMAN = 2; //specific tasks (cooking, dressing)

    /** DOCUMENT ME! */
    public final static int MAN = 3; //with much more power than woman and specific tasks (war, thinking, hunting)

    /** DOCUMENT ME! */
    public final static int SHAMAN = 4; //medicine man and priest

    /** DOCUMENT ME! */
    public final static int LEADER = 5; //a man in nearly all cultures

    //feodal occupation: casts (and money to a lesser extend)
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int SLAVE = 1; //unpaid people serving others

    /** DOCUMENT ME! */
    public final static int PEASANT = 2; //also serf, works land

    /** DOCUMENT ME! */
    public final static int CHIVALRY = 3; //military man

    /** DOCUMENT ME! */
    public final static int CLERGY = 4; //religious people

    /** DOCUMENT ME! */
    public final static int NOBLE = 5; //ruler, king, count...

    //modern occupation: money
    //this caracterization may well depend upon the country as one individual earning a given amount may be poor in a country and at the same time rich in another
    //okay, don't expect these categories to be joyful, as you may be annoyed by fitting in.
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int STREET = 1; //cannot offer to have a permantent living place (roughtly speaking lowest 5 percents)

    /** DOCUMENT ME! */
    public final static int POOR = 2; //at poverty level (roughtly speaking lowest 15 percents) (people below half of the mean people life level)

    /** DOCUMENT ME! */
    public final static int LABOR_CLASS = 3; //above poverty level (roughtly speaking lowest 40 percents)

    /** DOCUMENT ME! */
    public final static int MIDDLE_CLASS = 4; //white colar, bourgeois and the like

    /** DOCUMENT ME! */
    public final static int UPPER_CLASS = 5; //(roughtly speaking highest 10 percents)

    /** DOCUMENT ME! */
    public final static int RICH = 6; //deciding people (roughtly speaking highest 2 percents)

    /** DOCUMENT ME! */
    public final static int MONOGAMY = 1;

    /** DOCUMENT ME! */
    public final static int POLYANDY = 2;

    /** DOCUMENT ME! */
    public final static int POLYGYNY = 4;

    /** DOCUMENT ME! */
    public final static int PALEOLITHIC = 1;

    /** DOCUMENT ME! */
    public final static int NEOLITHIC = 2;

    /** DOCUMENT ME! */
    public final static int ANTIQUE = 4;

    /** DOCUMENT ME! */
    public final static int MEDIEVAL = 8;

    /** DOCUMENT ME! */
    public final static int RENAISSANCE = 16;

    /** DOCUMENT ME! */
    public final static int EARLY_MODERN = 32;

    /** DOCUMENT ME! */
    public final static int MODERN = 64;

    /** DOCUMENT ME! */
    public final static int POST_MODERN = 128;

    /** DOCUMENT ME! */
    public final static int HIGH_TECH = 256;

    /** DOCUMENT ME! */
    public final static int HUNTER_GATHERER = 1;

    /** DOCUMENT ME! */
    public final static int NOMADIC_PASTORAL = 2;

    /** DOCUMENT ME! */
    public final static int HORTICULTURALIST = 4; //or simple farming societies,

    /** DOCUMENT ME! */
    public final static int INTENSIVE_AGRICULTURAL = 8; //also called civilizations.

    // Some consider Industrial and Post-Industrial societies to be separate from traditional agricultural societies.
    /** DOCUMENT ME! */
    public final static int INDUSTRIAL = 16;

    /** DOCUMENT ME! */
    public final static int POST_INDUSTRIAL = 32;
}
