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
