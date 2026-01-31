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

package org.jscience.social.linguistics.loaders.tigerxml.core;

import org.w3c.dom.Element;

import java.io.Serializable;


/**
 * Represents the TIGER-XML source document of a corpus.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TigerXMLDocument implements Serializable {
    private String fileName;

    private Element documentRoot;

    private int verbosity = 0;

    /**
     * Creates a new TigerXMLDocument object.
     *
     * @param corpusFileName path to the corpus file
     */
    public TigerXMLDocument(String corpusFileName) {
        init(corpusFileName);
    }

    /**
     * Creates a new TigerXMLDocument object.
     *
     * @param corpusFileName path to the corpus file
     * @param verbosity      logging verbosity level
     */
    public TigerXMLDocument(String corpusFileName, int verbosity) {
        this.verbosity = verbosity;
        init(corpusFileName);
    }

    /**
     * Initializes the document by parsing the XML file.
     *
     * @param corpusFileName path to the corpus file
     */
    private void init(String corpusFileName) {
        this.fileName = corpusFileName;

        XMLParser xmlP = new XMLParser(corpusFileName, this.verbosity);
        this.documentRoot = xmlP.getDOMRootElement();
    }

    /**
     * Returns the DOM root element of the parsed document.
     *
     * @return the root Element
     */
    public Element getDocumentRoot() {
        return this.documentRoot;
    }

    /**
     * Returns the source file name.
     *
     * @return the absolute path to the XML file
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Resets the document state, clearing the DOM tree.
     */
    public void reset() {
        this.fileName = null;
        this.documentRoot.getOwnerDocument().removeChild(documentRoot);
        this.documentRoot = null;
    }

    /**
     * Gets the currently set level of verbosity of this instance. The
     * higher the value the more information is written to stderr.
     *
     * @return The level of verbosity.
     */
    public int getVerbosity() {
        return this.verbosity;
    }

    /**
     * Sets the currently set level of verbosity of this instance. The
     * higher the value the more information is written to stderr.
     *
     * @param verbosity The level of verbosity.
     */
    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }
}

