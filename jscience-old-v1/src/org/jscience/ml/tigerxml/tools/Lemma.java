/*
 * Lemma.java
 *
 * Created in November, 2003
 *
 * Copyright (C) 2003 Hajo Keffer <hajokeffer@coli.uni-sb.de>,
 *                    Oezguer Demir <oeze@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
