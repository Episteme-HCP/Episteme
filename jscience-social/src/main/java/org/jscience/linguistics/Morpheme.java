package org.jscience.linguistics;

/**
 * The Morpheme class corresponds to a sub unit of a word (prefix, suffix,
 * etc.).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//morphemes can be built out of graphemes or phonemes
//as we are here in a computer system, phonemes also have a grapheme counterpart, which is why we use graphemes only
//yet with a bit of imagination we could design/enhance this class to provide morpheme or phoneme support
public class Morpheme extends Object {
    /** DOCUMENT ME! */
    private Phoneme[] phonemes;

    /** DOCUMENT ME! */
    private Grapheme[] graphemes;

    /** DOCUMENT ME! */
    private boolean isGrapheme;

/**
     * Creates a new Lexeme object.
     *
     * @param language DOCUMENT ME!
     * @param string   DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Morpheme(Language language, String string) {
        if ((language != null) && (string != null) && (string.length() > 0)) {
            phonemes = new Phoneme[0];
            graphemes = new Grapheme[string.length()];
            isGrapheme = true;

            for (int i = 0; i < string.length(); i++) {
                graphemes[i] = new Grapheme(language, string.charAt(i));
            }
        } else {
            throw new IllegalArgumentException(
                "The Morpheme constructor arguments can't be null and string can't be empty.");
        }
    }

/**
     * Creates a new Lexeme object.
     *
     * @param graphemes DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Morpheme(Grapheme[] graphemes) {
        boolean valid;
        int i;

        if ((graphemes != null) && (graphemes.length > 0)) {
            i = 1;
            valid = true;

            while ((valid) && (i < graphemes.length)) {
                valid = (graphemes[i].getLanguage() == graphemes[0].getLanguage());
            }

            if (valid) {
                phonemes = new Phoneme[0];
                this.graphemes = graphemes;
                isGrapheme = true;
            } else {
                throw new IllegalArgumentException(
                    "The graphemes must all be of the same Language.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Lexeme constructor arguments can't be null and graphemes can't be empty.");
        }
    }

/**
     * Creates a new Morpheme object.
     *
     * @param phonemes DOCUMENT ME!
     */
    public Morpheme(Phoneme[] phonemes) {
        boolean valid;
        int i;

        if ((phonemes != null) && (phonemes.length > 0)) {
            i = 1;
            valid = true;

            while ((valid) && (i < phonemes.length)) {
                valid = (phonemes[i].getLanguage() == phonemes[0].getLanguage());
            }

            if (valid) {
                phonemes = new Phoneme[0];
                this.phonemes = phonemes;
                graphemes = new Grapheme[0];
                isGrapheme = false;
            } else {
                throw new IllegalArgumentException(
                    "The phonemes must all be of the same Language.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Morpheme constructor arguments can't be null and phonemes can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return graphemes[0].getLanguage();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Grapheme[] getGraphemes() {
        return graphemes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Phoneme[] getPhonemes() {
        return phonemes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGraphemeBased() {
        return isGrapheme;
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

        if (isGrapheme) {
            for (i = 0; i < graphemes.length; i++) {
                result.append(graphemes[i].getCharacter());
            }
        } else {
            for (i = 0; i < phonemes.length; i++) {
                result.append(phonemes[i].getCharacter());
            }
        }

        return result.toString();
    }
}
