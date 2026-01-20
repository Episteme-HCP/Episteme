package org.jscience.linguistics;

import javax.sound.sampled.Clip;


/**
 * The Phoneme class defines the basic audible units of a language. They
 * are for sound what graphemes are for text. We recommend to use the Unicode
 * IPA codes values.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Phoneme extends Object {
    /** DOCUMENT ME! */
    private Language language;

    /** DOCUMENT ME! */
    private Clip clip;

    /** DOCUMENT ME! */
    private char character;

/**
     * Creates a new Phoneme object.
     *
     * @param language DOCUMENT ME!
     * @param clip     DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //please avoid this constructor
    public Phoneme(Language language, Clip clip) {
        if ((language != null) && (clip != null)) {
            this.language = language;
            this.clip = clip;
            language.addPhoneme(this);
        } else {
            throw new IllegalArgumentException(
                "The Phoneme constructor arguments can't be null.");
        }
    }

/**
     * Creates a new Phoneme object.
     *
     * @param language  DOCUMENT ME!
     * @param clip      DOCUMENT ME!
     * @param character DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Phoneme(Language language, Clip clip, char character) {
        if ((language != null) && (clip != null)) {
            this.language = language;
            this.clip = clip;
            this.character = character;
            language.addPhoneme(this);
        } else {
            throw new IllegalArgumentException(
                "The Phoneme constructor arguments can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Clip getSound() {
        return clip;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //CAUTION: the character may be undefined
    public char getCharacter() {
        return character;
    }
}
