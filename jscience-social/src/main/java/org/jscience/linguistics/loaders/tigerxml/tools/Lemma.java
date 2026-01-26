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
 * @author <a href="mailto:hajo_keffer@gmx.de">Hajo Keffer</a>
 * @version 1.84 $Id: Lemma.java,v 1.3 2007-10-23 18:21:43 virtualcall Exp $
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
