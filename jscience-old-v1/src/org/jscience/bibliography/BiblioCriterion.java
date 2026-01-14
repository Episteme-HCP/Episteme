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

// BiblioCriterion.java
//
//    senger@ebi.ac.uk
//    April 2001
//
package org.jscience.bibliography;

/**
 * Represents an ordering or searching Criterion.
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
 * @version $Id: BiblioCriterion.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioCriterion {
    //
    // Criteria types
    //

    /**
     * DOCUMENT ME!
     */
    public static final int N_A_CRITERION = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int QUERY_CRITERION = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int SORT_CRITERION = 2;

    /**
     * DOCUMENT ME!
     */
    public String name;

    /**
     * DOCUMENT ME!
     */
    public int type = QUERY_CRITERION;

    /**
     * DOCUMENT ME!
     */
    public String[] mutuallyExclusiveWith;

    /**
     * DOCUMENT ME!
     */
    public String forSubset;
}
