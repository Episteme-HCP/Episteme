package org.jscience.linguistics;

/**
 * The Lexeme defines common rooted words.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Lexeme extends Object {
    /** DOCUMENT ME! */
    private Word[] words;

/**
     * Creates a new Lexeme object.
     *
     * @param words DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Lexeme(Word[] words) {
        boolean valid;
        int i;

        if ((words != null) && (words.length > 0)) {
            i = 1;
            valid = true;

            while ((valid) && (i < words.length)) {
                valid = (words[i].getLanguage() == words[0].getLanguage());
            }

            if (valid) {
                this.words = words;
            } else {
                throw new IllegalArgumentException(
                    "The words must all be of the same Language.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Lexeme constructor arguments can't be null and words can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return words[0].getLanguage();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Word[] getWords() {
        return words;
    }
}
