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

package org.jscience.linguistics.loaders.tigerxml.tools;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;
import org.jscience.linguistics.loaders.tigerxml.T;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Represents a lemma of the Tiger Corpus.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
class Lemma {
    /** The canonical form/name of the lemma. */
    private final String lemma_name;

    /** Pattern for matching various word forms. */
    private final Pattern pattern;

    /**
     * Creates a new lemma instance.
     *
     * @param new_lemma_name  the name of the lemma
     * @param word_form_regex a regular expression that matches each word form
     *                        of the lemma
     * @see java.util.regex.Pattern
     */
    protected Lemma(String new_lemma_name, String word_form_regex) {
        this.lemma_name = new_lemma_name;
        this.pattern = Pattern.compile(word_form_regex, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Returns the name of the lemma.
     */
    protected String getName() {
        return lemma_name;
    }

    /**
     * Returns true if the word form matches one of the word forms of
     * this lemma.
     */
    protected boolean hasWordForm(String word_form) {
        if (word_form == null) return false;
        Matcher matcher = pattern.matcher(word_form);
        return matcher.matches();
    }

    /**
     * Returns true if this lemma occurs on the surface of the input node.
     */
    protected boolean occursOnSurface(GraphNode node) {
        if (node == null) return false;
        if (node.isTerminal()) {
            return this.hasWordForm(((T) node).getWord());
        } else {
            List<T> terminals = ((NT) node).getTerminals();
            for (T next_terminal : terminals) {
                if (this.hasWordForm(next_terminal.getWord())) {
                    return true;
                }
            }
            return false;
        }
    }
}
