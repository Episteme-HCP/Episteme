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

import org.w3c.dom.Element;

import java.io.Serializable;


/**
 * Represents the TIGER-XML source document of a corpus.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de"> Oezguer Demir </a>
 * @version 1.84 $Id: TigerXmlDocument.java,v 1.3 2007-10-23 18:21:42 virtualcall Exp $
 */
public class TigerXmlDocument implements Serializable {
    /** DOCUMENT ME! */
    private String fileName;

    /** DOCUMENT ME! */
    private Element documentRoot;

    /** DOCUMENT ME! */
    private int verbosity = 0;

/**
     * Creates a new TigerXmlDocument object.
     *
     * @param corpusFileName DOCUMENT ME!
     */
    public TigerXmlDocument(String corpusFileName) {
        init(corpusFileName);
    }

/**
     * Creates a new TigerXmlDocument object.
     *
     * @param corpusFileName DOCUMENT ME!
     * @param verbosity      DOCUMENT ME!
     */
    public TigerXmlDocument(String corpusFileName, int verbosity) {
        this.verbosity = verbosity;
        init(corpusFileName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param corpusFileName DOCUMENT ME!
     */
    private void init(String corpusFileName) {
        this.fileName = corpusFileName;

        XmlParser xmlP = new XmlParser(corpusFileName, this.verbosity);
        this.documentRoot = xmlP.getDOMRootElement();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element getDocumentRoot() {
        return this.documentRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * DOCUMENT ME!
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
