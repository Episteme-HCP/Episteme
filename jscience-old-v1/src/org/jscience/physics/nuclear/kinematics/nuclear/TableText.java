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

/*
 * Created on Dec 8, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jscience.physics.nuclear.kinematics.nuclear;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class TableText implements java.io.Serializable {
    /** DOCUMENT ME! */
    public static final TableText TABLE_1995 = new TableText("mass_rmd.mas95",
            5, 11, 9, 1995);

    /** DOCUMENT ME! */
    public static final TableText TABLE_2003 = new TableText("mass.mas03", 5,
            13, 11, 2003);

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private int colsSkip;

    /** DOCUMENT ME! */
    private int colsME;

    /** DOCUMENT ME! */
    private int colsUNC;

    /** DOCUMENT ME! */
    private int year;

/**
     * Creates a new TableText object.
     */
    public TableText() {
        //does nothing...only here for serializing
    }

/**
     * Creates a new TableText object.
     *
     * @param name     DOCUMENT ME!
     * @param colsSkip DOCUMENT ME!
     * @param colsME   DOCUMENT ME!
     * @param colsUNC  DOCUMENT ME!
     * @param year     DOCUMENT ME!
     */
    private TableText(String name, int colsSkip, int colsME, int colsUNC,
        int year) {
        this.name = name;
        this.colsSkip = colsSkip;
        this.colsME = colsME;
        this.colsUNC = colsUNC;
        this.year = year;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColsToSkip() {
        return colsSkip;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColsMassExcess() {
        return colsME;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColsUncertainty() {
        return colsUNC;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getYear() {
        return year;
    }
}
