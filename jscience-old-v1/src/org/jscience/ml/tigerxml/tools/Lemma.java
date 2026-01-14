/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.ml.tigerxml.tools;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.T;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Represents a lemma of the Tiger Corpus.
 *
 * @author <a href="mailto:hajo_keffer@gmx.de">Hajo Keffer</a>
 * @version 1.84 $Id: Lemma.java,v 1.3 2007-10-23 18:21:43 virtualcall Exp $
 */
class Lemma {
    /** DOCUMENT ME! */
    private String lemma_name;

    /** DOCUMENT ME! */
    private ArrayList word_forms;

    /** DOCUMENT ME! */
    private Pattern pattern;

    /** DOCUMENT ME! */
    private Matcher matcher;

/**
     * Creates a new lemma instance.
     *
     * @param new_lemma_name  the name of the lemma
     * @param word_form_regex a regular expression that matches each word form
     *                        of the lemma
     * @see java.util.regex.Pattern
     */
    protected Lemma(String new_lemma_name, String word_form_regex) {
        lemma_name = new_lemma_name;
        pattern = Pattern.compile(word_form_regex, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Returns the name of the lemma
     *
     * @return DOCUMENT ME!
     */
    protected String getName() {
        return lemma_name;
    }

    /**
     * Returns true if the word form matches one of the word forms of
     * this lemma
     *
     * @param word_form DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasWordForm(String word_form) {
        matcher = pattern.matcher(word_form);

        return (matcher.matches());
    }

    /**
     * Returns true if this lemma occurs on the surface of the input
     * node
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean occursOnSurface(GraphNode node) {
        if (node.isTerminal()) {
            String word = ((T) node).getWord();

            return this.hasWordForm(word);
        } else {
            ArrayList terminals = ((NT) node).getTerminals();

            for (int i = 0; i < terminals.size(); i++) {
                T next_terminal = (T) terminals.get(i);
                String next_word = next_terminal.getWord();

                if (this.hasWordForm(next_word)) {
                    return true;
                }
            } // for i

            return false;
        }
    } // method matches
}
