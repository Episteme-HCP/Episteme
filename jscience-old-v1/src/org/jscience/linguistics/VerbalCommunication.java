package org.jscience.linguistics;

/**
 * The Communication class provides support for communication, which is
 * what the language ultimatly stands for.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//exchange of information between locutors
//all the receivers are believed to be part of the ChatSituation
//this class is mainly targetted at verbal situation where people emit short phrases rather than complete texts (theater like)
public class VerbalCommunication extends Object {
    /** DOCUMENT ME! */
    private Locutor locutor; //emiter

    /** DOCUMENT ME! */
    private Phrase message;

/**
     * Creates a new VerbalCommunication object.
     *
     * @param locutor DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public VerbalCommunication(Locutor locutor, Phrase message) {
        if ((locutor != null) && (message != null)) {
            this.locutor = locutor;
            this.message = message;
        } else {
            throw new IllegalArgumentException(
                "The Communication constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Locutor getLocutor() {
        return locutor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Phrase getMessage() {
        return message;
    }

    //public Language getLanguage() {
    //we would have to test all the contents of the phrase are in the same language
    //}
}
