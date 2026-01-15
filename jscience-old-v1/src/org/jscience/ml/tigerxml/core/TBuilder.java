/*
 * TBuilder.java
 *
 * Created on February 13, 2003, 4:50 PM
 *
 * Copyright (C) 2003,2004 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                         Vaclav Nemcik <vicky@coli.uni-sb.de>,
 *                         Hajo Keffer <hajokeffer@coli.uni-sb.de>
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

package org.jscience.ml.tigerxml.core;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.Sentence;
import org.jscience.ml.tigerxml.T;
import org.jscience.ml.tigerxml.tools.DomTools;
import org.w3c.dom.Element;

public class TBuilder {

    /**
     * Builds up a given (ideally empty) <code>T</code> by getting
     * all information from the given DOM </code>Element</code>
     * <code>tElement</code>.
     *
     * @param t        The <code>T</code> instance to be built (can be empty).
     * @param sent     The <code>Sentence</code> to hold the new <code>T</code>.
     * @param tElement The DOM <code>Element</code> containing
     *                 a T element of a TIGER XML <code>Document</code>.
     * @param pos      The position of the new <code>T</code> in the
     *                 <code>Sentence</code>.
     */
    public static final void buildT(T t, Sentence sent, Element tElement, int pos) {
        t.setVerbosity(sent.getVerbosity());
        if (t.getVerbosity() > 0) {
            DomTools.checkElementName(tElement, "t");
        }
        if (t.getVerbosity() >= 4) {
            System.err.println("org.jscience.ml.tigerxml.core.TBuilder: Building and adding T "
                    + tElement.getAttribute("id"));
        }
        t.setTerminal(true);
        t.setPosition(pos);
        ((GraphNode) t).setVerbosity(sent.getVerbosity());
        if (tElement.hasAttribute("id")) {
            t.setId(tElement.getAttribute("id"));
        } else {
            if (t.getVerbosity() > 0) {
                System.err.println("org.jscience.ml.tigerxml.T: WARNING: Terminal element " +
                        t + " has no ID attribute ...");
            }
        }
        if (tElement.hasAttribute("word")) {
            t.setWord(tElement.getAttribute("word"));
        } else {
            if (t.getVerbosity() > 0) {
                System.err.println
                        ("org.jscience.ml.tigerxml.T: WARNING: Terminal element " +
                                t + " has no WORD attribute ...");
            }
        }
        if (tElement.hasAttribute("pos")) {
            t.setPos(tElement.getAttribute("pos"));
        } else {
            if (t.getVerbosity() > 0) {
                System.err.println
                        ("org.jscience.ml.tigerxml.T: WARNING: Terminal element " +
                                t + " has no POS attribute ...");
            }
        }
        if (tElement.hasAttribute("morph")) {
            t.setMorph(tElement.getAttribute("morph"));
        } else {
            //System.err.println
            //    ("org.jscience.ml.tigerxml.T: WARNING: Terminal element " +
            //     this + " has no MORPH attribute ...");
            t.setMorph("");
        }
        if (tElement.hasAttribute("lemma")) {
            t.setLemma(tElement.getAttribute("lemma"));
        } else {
            t.setLemma("");
        }
        t.setSentence(sent);
        t.setIndex(sent.getTCount());
    } // buildT

}
