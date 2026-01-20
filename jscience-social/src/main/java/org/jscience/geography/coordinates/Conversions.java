package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class Conversions {
    /** DOCUMENT ME! */
    private SRM_SRFT_Code _src;

    /** DOCUMENT ME! */
    private SRM_SRFT_Code[] _dest;

    /** DOCUMENT ME! */
    private OrmData _ormData;

/**
     * Creates a new Conversions object.
     *
     * @param src  DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected Conversions(SRM_SRFT_Code src, SRM_SRFT_Code[] dest) {
        // set the source SRF
        _src = src;

        // set the destination SRFs
        _dest = dest;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srfType DOCUMENT ME!
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected abstract SRM_Coordinate_Valid_Region_Code convert(
        SRM_SRFT_Code srfType, BaseSRF srcSrf, BaseSRF destSrf, double[] src,
        double[] dest) throws SrmException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract Conversions makeClone();

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     */
    protected void setOrmData(SRM_ORM_Code orm) {
        _ormData = new OrmData(orm);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     */
    protected void setOrmData(OrmData ormData) {
        _ormData = ormData;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected SRM_SRFT_Code getSrc() {
        return _src;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected SRM_SRFT_Code[] getDest() {
        return _dest;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected OrmData getOrmData() {
        return _ormData;
    }
}
