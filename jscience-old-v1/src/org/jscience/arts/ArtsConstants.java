package org.jscience.arts;

/**
 * A class representing the egelian classical arts categories as well as
 * other useful constants.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class ArtsConstants extends Object {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    //widely used in french speaking countries at least.
    //mutually exclusive but list can be extended (by subclassing)
    /** DOCUMENT ME! */
    public final static int ARCHITECTURE = 1; //architecture

    /** DOCUMENT ME! */
    public final static int SCULPTURE = 2; //sculpture

    /** DOCUMENT ME! */
    public final static int PAINTING = 3; //peinture

    /** DOCUMENT ME! */
    public final static int MUSIC = 4; //musique

    /** DOCUMENT ME! */
    public final static int DANCE = 5; //danse

    //non hegelian arts
    /** DOCUMENT ME! */
    public final static int POETRY = 6; //poesie

    /** DOCUMENT ME! */
    public final static int CINEMA = 7; //cinema

    /** DOCUMENT ME! */
    public final static int TELEVISION = 8; //television

    /** DOCUMENT ME! */
    public final static int COMICS = 9; //bande dessinee

    //my additions
    /** DOCUMENT ME! */
    public final static int CRAFTING = 20;

    /** DOCUMENT ME! */
    public final static int LITERATURE = 21;

    /** DOCUMENT ME! */
    public final static int THEATER = 22;

    /** DOCUMENT ME! */
    public final static int RADIO = 23;

    /** DOCUMENT ME! */
    public final static int GAMING = 24; //not gambling but can be internet gaming (not internet gambling), candidate for egelian art ten

    /** DOCUMENT ME! */
    public final static int INTERNET = 25; //candidate for egelian art ten

//commercials (publicité) is also considered by some as an art

    /** DOCUMENT ME! */
    public final static int COOKING = 26;

    //may be we could add furniture
    //musical instruments kinds
    //you can mix categories
    /** DOCUMENT ME! */
    public final static int STRINGS = 1;

    /** DOCUMENT ME! */
    public final static int WINDS = 2;

    /** DOCUMENT ME! */
    public final static int PERCUSSIONS = 4;

    /** DOCUMENT ME! */
    public final static int VOICES = 8;

    /** DOCUMENT ME! */
    public final static int NATURAL = 64; //can be used with any other three

    /** DOCUMENT ME! */
    public final static int SYNTHETIC = 128; //can be used with any other three

    //musical notes (with frequency)
    /** DOCUMENT ME! */
    public final static double C = 261.6; //DO

    /** DOCUMENT ME! */
    public final static double D = 293.7; //RE

    /** DOCUMENT ME! */
    public final static double E = 329.6; //MI

    /** DOCUMENT ME! */
    public final static double F = 349.2; //FA

    /** DOCUMENT ME! */
    public final static double G = 392.0; //SOL

    /** DOCUMENT ME! */
    public final static double A = 440.0; //LA

    /** DOCUMENT ME! */
    public final static double B = 493.9; //SI

    //the classical occurence in the egelian classification
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getClassicalArtNumber(int value) {
        int result;

        result = 0;

        switch (value) {
        case UNKNOWN:
            result = 0;

            break;

        case ARCHITECTURE:
            result = 1;

            break;

        case SCULPTURE:
            result = 2;

            break;

        case PAINTING:
            result = 3;

            break;

        case MUSIC:
            result = 4;

            break;

        case DANCE:
            result = 5;

            break;

        case POETRY:
            result = 6;

            break;

        case CINEMA:
            result = 7;

            break;

        case TELEVISION:
            result = 8;

            break;

        case COMICS:
            result = 9;

            break;

        default:
            result = 0;

            break;
        }

        return result;
    }

    //the classical occurence in the egelian classification transformed as a String
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getClassicalArtString(int value) {
        String result;

        result = new String();

        switch (value) {
        case UNKNOWN:
            result = "Unknown";

            break;

        case ARCHITECTURE:
            result = "Architecture";

            break;

        case SCULPTURE:
            result = "Sculpture";

            break;

        case PAINTING:
            result = "Painting";

            break;

        case MUSIC:
            result = "Music";

            break;

        case DANCE:
            result = "Dance";

            break;

        case POETRY:
            result = "Poetry";

            break;

        case CINEMA:
            result = "Cinema";

            break;

        case TELEVISION:
            result = "Television";

            break;

        case COMICS:
            result = "Comics";

            break;

        default:
            result = "Unknown";

            break;
        }

        return result;
    }
}
