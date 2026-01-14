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

import org.jscience.ml.tigerxml.Corpus;
import org.jscience.ml.tigerxml.Sentence;
import org.jscience.ml.tigerxml.tools.DomTools;
import org.jscience.ml.tigerxml.tools.GeneralTools;
import org.w3c.dom.*;

/**
 * Provides static functionality for building a <code>Corpus</code> object.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84
 *          $Id: CorpusBuilder.java,v 1.2 2007-10-21 17:47:08 virtualcall Exp $
 * @see org.jscience.ml.tigerxml.Corpus
 */

public class CorpusBuilder {

    /**
     * Builds up a given (ideally empty) <code>Corpus</code> by getting
     * all information from the given DOM </code>Element</code>
     * <code>rootElement</code>.
     *
     * @param corp        The <code>Corpus</code> instance to be built (can be empty).
     * @param rootElement The DOM <code>Element</code> containing
     *                    the root element of a TIGER XML <code>Document</code>.
     */
    public static void buildCorpus(Corpus corp, Element rootElement) {

        if (corp.getVerbosity() > 0) {
            System.err.println("org.jscience.ml.tigerxml.Corpus: Building corpus data structure ...");
            // Print a warning if the root node is not "corpus"
            DomTools.checkElementName(rootElement, "corpus");
        }
        long time = System.currentTimeMillis();
        // Read the corpus attributes <corpus attr1="val1" ...>
        NamedNodeMap attrList = rootElement.getAttributes();
        for (int i = 0; i < attrList.getLength(); i++) {
            Node node = attrList.item(i);
            Attr attr = (Attr) node;
            corp.addAttribute(attr.getName(), attr.getValue());
        }
        if (corp.hasAttribute("id")) {
            corp.setId(corp.getAttribute("id"));
        } else {
            corp.setId("");
        }
        // Init hash code and get the corpus <body>
        corp.setHashCode(0);
        Element body = DomTools.getElement(rootElement, "body");
        // Process sentences by passing <s>-nodes to the Sentence constructor
        ////corp.sentences = new ArrayList();
        NodeList sentenceNodes = body.getElementsByTagName("s");
        int length = sentenceNodes.getLength();
        for (int i = 0; i < length; i++) {
            Element currentSentenceElement = (Element) sentenceNodes.item(i);
            if (currentSentenceElement.hasAttribute("id")) {
                if (corp.getVerbosity() >= 5) {
                    System.err.println("org.jscience.ml.tigerxml.CorpusBuilder: Building and adding "
                            + "Sentence "
                            + currentSentenceElement.getAttribute("id"));
                }
                Sentence nextSent = new Sentence(currentSentenceElement, corp);
                corp.addSentence(nextSent);
            } else {
                if (corp.getVerbosity() > 0) {
                    System.err.println("org.jscience.ml.tigerxml.Corpus: Sentence without an ID found. Skipping ...");
                } // if
            } // else
        } // for i
        if (corp.getVerbosity() >= 3) {
            time = System.currentTimeMillis() - time;
            System.err.println("org.jscience.ml.tigerxml.Corpus: Building consumed: " +
                    GeneralTools.timeConvert(time));
        }
        if (corp.getVerbosity() > 0) {
            System.err.println("org.jscience.ml.tigerxml.Corpus: Corpus object " + corp +
                    " (" + corp.getSentenceCount() + " sentences) ready.");
        }
    } // buildCorpus();

}
