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

package org.jscience.geography.coordinates;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class OrmDataSet {
    /** DOCUMENT ME! */
    public String _label;

    /** DOCUMENT ME! */
    public String _description;

    /** DOCUMENT ME! */
    public SRM_ORM_Code _orm_code;

    /** DOCUMENT ME! */
    public SRM_ORMT_Code _orm_template;

    /** DOCUMENT ME! */
    public SRM_RD_Code _rd_code;

    /** DOCUMENT ME! */
    public SRM_ORM_Code _reference_orm;

    /** DOCUMENT ME! */
    public int _hsr_count;

    /** DOCUMENT ME! */
    public Vector _hsr_vector;

/**
     * Creates a new OrmDataSet object.
     *
     * @param label         DOCUMENT ME!
     * @param description   DOCUMENT ME!
     * @param orm_code      DOCUMENT ME!
     * @param orm_template  DOCUMENT ME!
     * @param rd_code       DOCUMENT ME!
     * @param reference_orm DOCUMENT ME!
     * @param hsr_count     DOCUMENT ME!
     */
    protected OrmDataSet(String label, String description,
        SRM_ORM_Code orm_code, SRM_ORMT_Code orm_template, SRM_RD_Code rd_code,
        SRM_ORM_Code reference_orm, int hsr_count) {
        _label = label;
        _description = description;
        _orm_code = orm_code;
        _orm_template = orm_template;
        _rd_code = rd_code;
        _reference_orm = reference_orm;
        _hsr_count = hsr_count;
        _hsr_vector = new Vector();
    }

    // The actual data has to be split to two table in the children classes
    /**
     * DOCUMENT ME!
     *
     * @param code DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static OrmDataSet getElem(SRM_ORM_Code code) {
        if (code.toInt() <= 241) // this table has ORM from 0 to 241
         {
            return OrmDataSet1.getElem(code);
        } else // this table has ORM from 242 to 482
         {
            return OrmDataSet2.getElem(code);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsr_data DOCUMENT ME!
     */
    public static void insertHsrElem(HsrDataSet hsr_data) {
        OrmDataSet.getElem(hsr_data._orm_code)._hsr_vector.add(hsr_data._hsr_code.toInt() -
            1, hsr_data);
    }
}
