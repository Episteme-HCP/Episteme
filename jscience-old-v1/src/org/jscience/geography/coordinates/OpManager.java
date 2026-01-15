package org.jscience.geography.coordinates;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class OpManager {
    // Steps to add a new SRF or new conversion routines for an existing SRF
    // 1) If new SRF
    //    - Add the SRF to the SrrfEnum
    //    - Create a XXConv.java like exisiting with new conversion routines
    //    - Register the new XXConv object in the OpManager constructor
    //      (below)
    // 2) New conversion routines for an existing SRF
    //    -  Add the conversion routines to its corresponding XXConv class
    //    -  Add the target SRF to the list of destinations in XXConc
    //    -  Add a case for the new conversion routine in XXConc
    /** DOCUMENT ME! */
    private static OpManager _mngr = new OpManager();

/**
     * Creates a new OpManager object.
     */
    private OpManager() {
        // create and register all conversion objects with the Function
        //  map for future search and chaining purpose
        FunctionMap.instance().register(new CcenConv());
        FunctionMap.instance().register(new Lsr2Conv());
        FunctionMap.instance().register(new Lsr3Conv());
        FunctionMap.instance().register(new CdetConv());
        FunctionMap.instance().register(new MercConv());
        FunctionMap.instance().register(new TmConv());
        FunctionMap.instance().register(new LccConv());
        FunctionMap.instance().register(new LteConv());
        FunctionMap.instance().register(new LctpConv());
        FunctionMap.instance().register(new LtasConv());
        FunctionMap.instance().register(new CmagConv());
        FunctionMap.instance().register(new EqinConv());
        FunctionMap.instance().register(new SeclConv());
        FunctionMap.instance().register(new SeqtConv());
        FunctionMap.instance().register(new SmagConv());
        FunctionMap.instance().register(new SmtpConv());
        FunctionMap.instance().register(new HaecConv());
        FunctionMap.instance().register(new HeecConv());
        FunctionMap.instance().register(new HeeqConv());

        // 	FunctionMap.instance().register(new PdetConv());
        // 	FunctionMap.instance().register(new EqcyConv());
        // 	FunctionMap.instance().register(new AzimConv());
        // 	FunctionMap.instance().register(new PolarConv());
        // 	FunctionMap.instance().register(new OmerConv());
        // 	FunctionMap.instance().register(new PostConv());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static OpManager instance() {
        return _mngr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param tgtSrf DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private Vector buildOpSeq(BaseSRF srcSrf, BaseSRF tgtSrf)
        throws SrmException {
        Vector opList = new Vector();

        //simply find path from the source to the target when
        // the ORMs and HSRs are the same
        if ((srcSrf.get_orm() == tgtSrf.get_orm()) &&
                (srcSrf.get_hsr() == tgtSrf.get_hsr())) {
            // find path is SRFs are different
            if (srcSrf.getClass() != tgtSrf.getClass()) {
                // 		    System.out.println("path.size=> " + srcSrf.getSRFTemplateCode() + ", " +
                // 				       tgtSrf.getSRFTemplateCode());
                Vector path = Bfs.instance()
                                 .GetConversionPath(srcSrf.getSRFTemplateCode(),
                        tgtSrf.getSRFTemplateCode());

                if (path != null) {
                    // 			    System.out.println("path.size=> " + path.size());
                    for (int i = 0; i < path.size(); i++) {
                        //  				System.out.println("Getting function for=> " + path.elementAt(i).toString());
                        try { // get the conversion object (if exists)

                            // set the ORM to the source ORM
                            // Add the conversion object to the operation chain
                            Conversions conv = (Conversions) FunctionMap.instance()
                                                                        .get((SRM_SRFT_Code) path.elementAt(
                                        i));
                            conv.setOrmData(srcSrf.get_orm());
                            opList.add(conv);
                        } catch (Exception e) {
                            throw new SrmException(SrmException._INACTIONABLE,
                                new String("Conversion not supported"));
                        }

                        // place the operation sequence in the repository for future use.
                        tgtSrf._myOpSeq.put(new Integer(srcSrf.hashCode()),
                            opList);
                    }
                } else { // no conversion possible
                    throw new SrmException(SrmException._INACTIONABLE,
                        new String("Conversion not supported"));
                }
            }
            // both the SRF type and the ORM are the same
            else {
                // if other paramters are also identical then apply identical matrix
                if (srcSrf.isEqual(tgtSrf)) {
                    try { // this should always work
                        // we need to add two of these because the Compute method expects
                        // one conversion object for every node in the path, in this
                        // case, 2 for the source and the target.  ALthough the second
                        // conversion object is never used.
                        opList.add(new IdentConv());
                        opList.add(new IdentConv());
                    } catch (Exception e) {
                        throw new SrmException(SrmException._INACTIONABLE,
                            new String("Inactionable failure"));
                    }

                    // place the operation sequence in the repository for future use.
                    tgtSrf._myOpSeq.put(new Integer(srcSrf.hashCode()), opList);
                }
                // otherwise find path as usual and it shoudl go through the CDET for
                // most cases
                else {
                    Vector path = null;

                    // this is to handle the special case with both src and tgt LTSC
                    if (srcSrf instanceof SRF_LocalTangentSpaceCylindrical) {
                        path = new Vector();
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL);
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN);
                        path.add(SRM_SRFT_Code.SRFT_CELESTIOCENTRIC);
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN);
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL);
                    }
                    // this is to handle the special case with both src and tgt LTSAS
                    else if (srcSrf instanceof SRF_LocalTangentSpaceAzimuthalSpherical) {
                        path = new Vector();
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL);
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN);
                        path.add(SRM_SRFT_Code.SRFT_CELESTIOCENTRIC);
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN);
                        path.add(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL);
                    }
                    // use the general approach to find the path
                    else {
                        path = Bfs.instance()
                                  .GetConversionPath(srcSrf.getSRFTemplateCode(),
                                tgtSrf.getSRFTemplateCode());
                    }

                    //  		    System.out.println("path.size=> " + path.size());
                    if (path != null) {
                        for (int i = 0; i < path.size(); i++) {
                            //  			    System.out.println("Getting function for=> " + path.elementAt(i).toString());
                            try { // get the conversion object (if exists)

                                // set the ORM to the source ORM
                                // Add the conversion object to the operation chain
                                Conversions conv = (Conversions) FunctionMap.instance()
                                                                            .get((SRM_SRFT_Code) path.elementAt(
                                            i));
                                conv.setOrmData(srcSrf.get_orm());
                                opList.add(conv);
                            } catch (Exception e) {
                                throw new SrmException(SrmException._INACTIONABLE,
                                    new String("Inactionable failure"));
                            }
                        }

                        // place the operation sequence in the repository for future use.
                        tgtSrf._myOpSeq.put(new Integer(srcSrf.hashCode()),
                            opList);
                    } else { // no conversion possible
                        throw new SrmException(SrmException._OPERATION_UNSUPPORTED,
                            new String("No conversion possible"));
                    }
                }
            }
        }
        // find the path from source to CCEN and then from CCEN to target when
        // the ORMs and HSRs are different
        else {
            // find path from source to CCEN for datum shitf
            Vector path1 = Bfs.instance()
                              .GetConversionPath(srcSrf.getSRFTemplateCode(),
                    SRM_SRFT_Code.SRFT_CELESTIOCENTRIC);

            if (path1 == null) {
                throw new SrmException(SrmException._OPERATION_UNSUPPORTED,
                    new String("No conversion possible"));
            }

            // find path from CCEN to target
            Vector path2 = Bfs.instance()
                              .GetConversionPath(SRM_SRFT_Code.SRFT_CELESTIOCENTRIC,
                    tgtSrf.getSRFTemplateCode());

            if (path2 == null) {
                throw new SrmException(SrmException._OPERATION_UNSUPPORTED,
                    new String("No conversion possible"));
            }

            // skip if the source is already Celestiocentric
            if (srcSrf.getSRFTemplateCode() != SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
                // compose the operation seq. from source to CCEN using ORM from source
                for (int i = 0; i < path1.size(); i++) {
                    try {
                        // 			System.out.println("Getting function for=> " + path1.elementAt(i).toString());
                        // get the conversion object (if exists)
                        // set the ORM to the source ORM
                        // Add the conversion object to the operation chain
                        Conversions conv = (Conversions) FunctionMap.instance()
                                                                    .get((SRM_SRFT_Code) path1.elementAt(
                                    i));
                        conv.setOrmData(srcSrf.get_orm());
                        opList.add(conv);
                    } catch (Exception e) {
                        throw new SrmException(SrmException._INACTIONABLE,
                            new String("Inactionable failure"));
                    }
                }
            }

            // insert the datum shift conversion object in here
            opList.add((Conversions) new DShiftConv());

            // skip if the target is already Celestiocentric
            if (tgtSrf.getSRFTemplateCode() != SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
                // compose the operation seq. from CCEN to target using ORM from target
                for (int i = 0; i < path2.size(); i++) {
                    try {
                        // 			System.out.println("Getting function for=> " + path2.elementAt(i).toString());
                        // get the conversion object (if exists)
                        // set the ORM to the target ORM
                        // Add the conversion object to the operation chain
                        Conversions conv = (Conversions) FunctionMap.instance()
                                                                    .get((SRM_SRFT_Code) path2.elementAt(
                                    i));
                        conv.setOrmData(tgtSrf.get_orm());
                        opList.add(conv);
                    } catch (Exception e) {
                        throw new SrmException(SrmException._INACTIONABLE,
                            new String("Inactionable failure"));
                    }
                }
            }

            // place the operation sequence in the repository for future use.
            tgtSrf._myOpSeq.put(new Integer(srcSrf.hashCode()), opList);
        }

        return opList;
    }

    //     protected Coord compute( BaseSRF srcSrf,
    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param tgtSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected SRM_Coordinate_Valid_Region_Code computeAsArray(BaseSRF srcSrf,
        BaseSRF tgtSrf, double[] src, double[] dest) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;
        SRM_Coordinate_Valid_Region_Code thisValid = SRM_Coordinate_Valid_Region_Code.VALID;
        Vector opList = null;
        SRM_SRFT_Code nextSrf;

        // reject the conversion if one the HSR is UNDEFINED and the other is not.
        if ((srcSrf.get_hsr() != tgtSrf.get_hsr()) &&
                ((srcSrf.get_hsr() == SRM_HSR_Code.HSR_UNDEFINED) ||
                (tgtSrf.get_hsr() == SRM_HSR_Code.HSR_UNDEFINED))) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("changeCoordinateSRF: cannot convert if one SRF specifies " +
                    "HSR_UNDEFINED and the other not"));
        }

        // instantiate a Operation sequence repository for the target SRF if
        // not already created.  We use the target SRF because that is the one that
        // computes the changeCoordinateSRF
        if (tgtSrf._myOpSeq == null) {
            tgtSrf._myOpSeq = new CacheManager();
        }

        opList = (Vector) tgtSrf._myOpSeq.get(new Integer(srcSrf.hashCode()));

        if (opList == null) {
            opList = buildOpSeq(srcSrf, tgtSrf);
        }

        // if the src and tgt ORM and HSR are the same, then no need to
        // perform the last datum shift, which is the latest conversion of the
        // sequence (this is mainly for efficiency)
        int srf_iterations;

        if (srcSrf.get_hsr() == tgtSrf.get_hsr()) {
            srf_iterations = opList.size() - 1;
        } else {
            srf_iterations = opList.size();
        }

        // perform computation
        // go through n-1 operations where n are number of SRFs in the conversion seq.
        for (int i = 0; i < srf_iterations; i++) {
            if (i < (opList.size() - 1)) {
                nextSrf = ((Conversions) (opList.elementAt(i + 1))).getSrc();
            } else { // the last SRF is the target SRFs.  NO conversion for the last one
                // except for datum shift.
                nextSrf = SRM_SRFT_Code.SRFT_UNDEFINED;
            }

            //  	    System.out.println("i=> " + i + " from " + ((Conversions) (opList.elementAt(i))).getSrc());
            // 	    System.out.println("[ " + src[0] + ", " + src[1] + ", " + src[2] + " ]");
            thisValid = ((Conversions) (opList.elementAt(i))).convert(nextSrf,
                    srcSrf, tgtSrf, src, dest);

            // copy destination coord to source coord
            System.arraycopy(dest, 0, src, 0, src.length);

            // this checks that all the validations along the way are consistent, meaning that the
            // valid regions should, at most, go from valid (1) to extended valid (2) and to defined (3) and never
            // the way around. It can only happen at the last conversion, which is a simple copy and
            // therefore would always return Valid, which should be ignored
            if (i != (opList.size() - 1)) {
                if (((retValid == SRM_Coordinate_Valid_Region_Code.VALID) &&
                        (thisValid != SRM_Coordinate_Valid_Region_Code.VALID)) ||
                        ((retValid == SRM_Coordinate_Valid_Region_Code.DEFINED) &&
                        (thisValid == SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID))) {
                    retValid = thisValid;
                }
            }
        }

        return retValid;
    }
}
