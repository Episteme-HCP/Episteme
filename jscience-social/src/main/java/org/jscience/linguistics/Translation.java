package org.jscience.linguistics;

/**
 * The Translation class provides a means to
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Translation extends Object {
    /** DOCUMENT ME! */
    private Text text;

    /** DOCUMENT ME! */
    private Language target;

/**
     * Creates a new Translation object.
     *
     * @param text   DOCUMENT ME!
     * @param target DOCUMENT ME!
     */
    public Translation(Text text, Language target) {
        if ((text != null) && (target != null)) {
            this.text = text;
            this.target = target;
        } else {
            throw new IllegalArgumentException(
                "The Translation constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Text getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getTargetLanguage() {
        return target;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Text getTranslatedText();
}
