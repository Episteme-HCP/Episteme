package org.jscience.linguistics;

/**
 * The Word defines sequences of Morphemes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Word extends Object {
    /** DOCUMENT ME! */
    private Morpheme[] morphemes;

/**
     * Creates a new Word object.
     *
     * @param language DOCUMENT ME!
     * @param string   DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Word(Language language, String string) {
        if ((language != null) && (string != null) && (string.length() > 0)) {
            morphemes = new Morpheme[string.length()];

            for (int i = 0; i < string.length(); i++) {
                morphemes[i] = new Morpheme(language, string.substring(i, i +
                            1));
            }
        } else {
            throw new IllegalArgumentException(
                "The Lexeme constructor arguments can't be null and string can't be empty.");
        }
    }

/**
     * Creates a new Word object.
     *
     * @param morphemes DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Word(Morpheme[] morphemes) {
        boolean valid;
        int i;

        if ((morphemes != null) && (morphemes.length > 0)) {
            i = 1;
            valid = true;

            while ((valid) && (i < morphemes.length)) {
                valid = (morphemes[i].getLanguage() == morphemes[0].getLanguage());
            }

            if (valid) {
                this.morphemes = morphemes;
            } else {
                throw new IllegalArgumentException(
                    "The morphemes must all be of the same Language.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Word constructor arguments can't be null and morphemes can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return morphemes[0].getLanguage();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Morpheme[] getMorphemes() {
        return morphemes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getString() {
        int i;
        StringBuffer result;

        result = new StringBuffer();

        for (i = 0; i < morphemes.length; i++) {
            result.append(morphemes[i].getString());
        }

        return result.toString();
    }
}
