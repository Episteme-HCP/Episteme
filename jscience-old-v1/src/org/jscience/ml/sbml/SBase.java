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

package org.jscience.ml.sbml;

import java.util.Collection;
import java.util.Iterator;


/**
 * The base class for the main SBML Level 2 data types. This code is
 * licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more
 * details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class SBase {
    /** DOCUMENT ME! */
    private Annotations annotations;

    /** DOCUMENT ME! */
    private Notes notes;

    /** DOCUMENT ME! */
    private String metaid;

    /** DOCUMENT ME! */
    private String rdf;

/**
     * Creates a new instance of SBase
     */
    protected SBase() {
        annotations = new Annotations();
        notes = new Notes();
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     * @param item DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static boolean isItemOfType(String type, String item) {
        return item.startsWith("<" + type) || item.startsWith("<sbml:" + type);
    }

    /**
     * Getter for property annotations.
     *
     * @return Value of property annotations.
     */
    public Annotations getAnnotations() {
        return annotations;
    }

    /**
     * Getter for property metaid.
     *
     * @return Value of property metaid.
     */
    public String getMetaid() {
        return metaid;
    }

    /**
     * Getter for property notes.
     *
     * @return Value of property notes.
     */
    public Notes getNotes() {
        return notes;
    }

    /**
     * Gets the RDF String for this {@link SBase} object.
     *
     * @return The RDF String.
     */
    public String getRDF() {
        return rdf;
    }

    /**
     * Setter for property metaid.
     *
     * @param metaid New value of property metaid.
     */
    public void setMetaid(String metaid) {
        this.metaid = metaid;
    }

    /**
     * Sets the RDF String for the {@link SBase} object.
     *
     * @param rdf The RDF data to add.
     */
    public void setRDF(String rdf) {
        this.rdf = rdf;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rdf DOCUMENT ME!
     * @param metaid DOCUMENT ME!
     */
    public void setRDF(String rdf, String metaid) {
        setRDF(rdf);
        setMetaid(metaid);
    }

    /**
     * The SBML for this element.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return notes.toString() + annotations.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param list DOCUMENT ME!
     * @param header DOCUMENT ME!
     * @param footer DOCUMENT ME!
     */
    protected void printList(StringBuffer buffer, Collection list,
        String header, String footer) {
        if ((list == null) || (list.size() == 0)) {
            return;
        }

        buffer.append(header + "\n");

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
            buffer.append(iterator.next());

        buffer.append(footer + "\n");
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param footer DOCUMENT ME!
     */
    protected void printShortForm(StringBuffer buffer, String footer) {
        if ((notes.size() == 0) && (rdf == null) && (metaid == null) &&
                (annotations.size() == 0)) {
            buffer.append("/>\n");

            return;
        }

        buffer.append(">\n");
        buffer.append(annotations.toString());
        buffer.append(notes.toString());
        buffer.append(footer + "\n");
    }
}
