package org.jscience.linguistics;

import java.util.Iterator;
import java.util.Vector;


/**
 * The Text class provides a placeholder for texts to be analyzed by
 * linguistics. Not to be used to store or manipulate Strings.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a text is a vector of sentences.
public class Text extends Object {
    /** DOCUMENT ME! */
    private Vector sentences;

/**
     * Creates a new Text object.
     */
    public Text() {
        sentences = new Vector();
    }

/**
     * Creates a new Text object.
     *
     * @param sentences DOCUMENT ME!
     */
    public Text(Vector sentences) {
        Iterator iterator;
        boolean valid;

        if (sentences != null) {
            iterator = sentences.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Sentence;
            }

            if (valid) {
                this.sentences = sentences;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of Sentences must consist only of Sentences.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Text constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSentences() {
        return sentences;
    }
}
