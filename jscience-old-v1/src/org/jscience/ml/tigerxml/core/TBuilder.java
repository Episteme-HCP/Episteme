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
