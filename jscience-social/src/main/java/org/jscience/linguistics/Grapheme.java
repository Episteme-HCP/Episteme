package org.jscience.linguistics;

/**
 * The Grapheme class is the minimal text unit. English letters are common
 * graphemes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://66.102.9.104/search?q=cache:jglOb1Mdnp8J:www.languefrancaise.net/dossiers/dossiers.php%3Fid_dossier%3D111++graphemes+francais&hl=fr
//http://moodle.ed.uiuc.edu/wiked/index.php/Phonemic_awareness
//space, as a delimiter, is not a grapheme
public class Grapheme extends Object {
    /** DOCUMENT ME! */
    private Language language;

    /** DOCUMENT ME! */
    private char character;

/**
     * Creates a new Grapheme object.
     *
     * @param language  DOCUMENT ME!
     * @param character DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Grapheme(Language language, char character) {
        if (language != null) {
            this.language = language;
            this.character = character;
            language.addGrapheme(this);
        } else {
            throw new IllegalArgumentException(
                "The Grapheme constructor arguments can't be null.");
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
    public char getCharacter() {
        return character;
    }
}
