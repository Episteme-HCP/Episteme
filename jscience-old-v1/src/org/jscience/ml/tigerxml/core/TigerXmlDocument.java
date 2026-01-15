/*
 * TigerXmlDocument.java
 *
 * Created on January 29, 2004, 11:22 PM
 *
 * Copyright (C) 2003 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                    Vaclav Nemcik <vicky@coli.uni-sb.de>,
 *                    Hajo Keffer <hajokeffer@coli.uni-sb.de>
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
