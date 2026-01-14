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

// BibRef.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

import embl.ebi.utils.ReflectUtils;

import java.util.Hashtable;


/**
 * Represents a bibliographic reference.
 * <p/>
 * <p/>
 * It is a CORBA-independent container of members, without any methods. Some
 * attributes can be <tt>null</tt> if they are not supported by the
 * repository.
 * </p>
 * <p/>
 * <P></p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: BibRef.java,v 1.2 2007-10-21 17:37:41 virtualcall Exp $
 */
public class BibRef {
    /**
     * DOCUMENT ME!
     */
    public Hashtable properties = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    public String identifier;

    /**
     * DOCUMENT ME!
     */
    public String type;

    /**
     * DOCUMENT ME!
     */
    public String[] crossReferences;

    /**
     * DOCUMENT ME!
     */
    public String title;

    /**
     * DOCUMENT ME!
     */
    public BiblioSubject subject;

    /**
     * DOCUMENT ME!
     */
    public BiblioDescription description;

    /**
     * DOCUMENT ME!
     */
    public BiblioScope coverage;

    /**
     * DOCUMENT ME!
     */
    public BiblioProvider[] authors;

    /**
     * DOCUMENT ME!
     */
    public BiblioProvider[] contributors;

    /**
     * DOCUMENT ME!
     */
    public BiblioProvider publisher;

    /**
     * DOCUMENT ME!
     */
    public String rights;

    /**
     * DOCUMENT ME!
     */
    public String date;

    /**
     * DOCUMENT ME!
     */
    public String language;

    /**
     * DOCUMENT ME!
     */
    public String format;

    /**
     * DOCUMENT ME!
     */
    public BiblioEntryStatus entryStatus;

    /**
     * A bit of "pretty" formatting. It even tries to make some indentations
     * :-)
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ReflectUtils.formatPublicFields(this);
    }
}
