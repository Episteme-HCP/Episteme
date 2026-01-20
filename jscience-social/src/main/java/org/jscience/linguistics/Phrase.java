package org.jscience.linguistics;

import java.util.Iterator;
import java.util.Vector;


/**
 * The Phrase class corresponds to a meaningful morphological unit, a
 * substring of a sentence.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a phrase is a vector of words
public class Phrase extends Object {
    /** DOCUMENT ME! */
    private Vector words;

/**
     * Creates a new Phrase object.
     */
    public Phrase() {
        words = new Vector();
    }

/**
     * Creates a new Phrase object.
     *
     * @param words DOCUMENT ME!
     */
    public Phrase(Vector words) {
        Iterator iterator;
        boolean valid;

        if (words != null) {
            iterator = words.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Word;
            }

            if (valid) {
                this.words = words;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of Words must consist only of Words.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Phrase constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getWords() {
        return words;
    }
}
