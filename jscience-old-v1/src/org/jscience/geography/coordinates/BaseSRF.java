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

// SRM SDK Release 4.0.0 - July 29, 2004
// - SRM spec. 4.0
/*
 *                             NOTICE
 *
 * This software is provided openly and freely for use in representing and
 * interchanging environmental data & databases.
 *
 * This software was developed for use by the United States Government with
 * unlimited rights.  The software was developed under contract
 * N61339-03-C-0068 by Science Applications International Corporation.
 * The software is unclassified and is deemed as Distribution A, approved
 * for Public Release.
 *
 * Use by others is permitted only upon the ACCEPTANCE OF THE TERMS AND
 * CONDITIONS, AS STIPULATED UNDER THE FOLLOWING PROVISIONS:
 *
 *    1. Recipient may make unlimited copies of this software and give
 *       copies to other persons or entities as long as the copies contain
 *       this NOTICE, and as long as the same copyright notices that
 *       appear on, or in, this software remain.
 *
 *    2. Trademarks. All trademarks belong to their respective trademark
 *       holders.  Third-Party applications/software/information are
 *       copyrighted by their respective owners.
 *
 *    3. Recipient agrees to forfeit all intellectual property and
 *       ownership rights for any version created from the modification
 *       or adaptation of this software, including versions created from
 *       the translation and/or reverse engineering of the software design.
 *
 *    4. Transfer.  Recipient may not sell, rent, lease, or sublicense
 *       this software.  Recipient may, however enable another person
 *       or entity the rights to use this software, provided that this
 *       AGREEMENT and NOTICE is furnished along with the software and
 *       /or software system utilizing this software.
 *
 *       All revisions, modifications, created by the Recipient, to this
 *       software and/or related technical data shall be forwarded by the
 *       Recipient to the Government at the following address:
 *
 *         NAVAIR Orlando TSD PJM
 *         Attention SEDRIS II TPOC
 *         12350 Research Parkway
 *         Orlando, FL 32826-3275
 *
 *         or via electronic mail to:  se-mgmt@sedris.org
 *
 *    5. No Warranty. This software is being delivered to you AS IS
 *       and there is no warranty, EXPRESS or IMPLIED, as to its use
 *       or performance.
 *
 *       The RECIPIENT ASSUMES ALL RISKS, KNOWN AND UNKNOWN, OF USING
 *       THIS SOFTWARE.  The DEVELOPER EXPRESSLY DISCLAIMS, and the
 *       RECIPIENT WAIVES, ANY and ALL PERFORMANCE OR RESULTS YOU MAY
 *       OBTAIN BY USING THIS SOFTWARE OR DOCUMENTATION.  THERE IS
 *       NO WARRANTY, EXPRESS OR, IMPLIED, AS TO NON-INFRINGEMENT OF
 *       THIRD PARTY RIGHTS, MERCHANTABILITY, OR FITNESS FOR ANY
 *       PARTICULAR PURPOSE.  IN NO EVENT WILL THE DEVELOPER, THE
 *       UNITED STATES GOVERNMENT OR ANYONE ELSE ASSOCIATED WITH THE
 *       DEVELOPMENT OF THIS SOFTWARE BE HELD LIABLE FOR ANY CONSEQUENTIAL,
 *       INCIDENTAL OR SPECIAL DAMAGES, INCLUDING ANY LOST PROFITS
 *       OR LOST SAVINGS WHATSOEVER.
 */
/*
 * COPYRIGHT 2003, SCIENCE APPLICATIONS INTERNATIONAL CORPORATION.
 *                 ALL RIGHTS RESERVED.
 *
 */
// SRM_OTHERS_GOES_HERE
/**
 @author David Shen
 @brief Declaration of Base SRF class.
 */
/** @mainpage Spatial Reference Model (SRM) Java API
 @section Introduction
 This is the documentation for the SRM Java API.

 All classes in this API are in the <i>SRM</i> package.

 The SRM classes provide the following functionality:
 - Creation
 - SRFs
 - SRF templates (e.g., LSR 3D, TM, Celestiondetic, Celestiocentric)
 - SRF set members (e.g., UTM zone 12, GTRS cell 1234, UPS northern pole)
 - SRFs (e.g., British National Grid)
 - Coordinates
 - 2D coordinate
 - 3D coordinate
 - Surface coordinate
 - Directions
 - Orientations
 - Conversion
 - Coordinate conversion between SRFs
 - Direction conversion between SRFs
 - Orientation conversion between SRFs
 - Validation
 - Coordinate validation within a SRF
 - Direction validation within a SRF
 - Orientation validation within a SRF
 - Calculations
 - Euclidean distance
 - Geodesic distance
 - Point scale
 - Vertical separation offset
 - Convergence of the Meridian
 - Map azimuth
 - Abstract space coordinate instancing in a SRF

 A sample program to convert a Celestiodetic 3D coordinate to
 a Celestiocentric 3D coordinate is as follows:
 @code public class CdToCcConv
 {

 public static void main (String args[])
 {
 System.out.println( "*** Sample program using SRM Java API to convert a 3D coordinate" );
 System.out.println( "*** from a Celestiodetic SRF to a Celestiocentric SRF." );

 // Declare reference variables for the CD and CC SRFs
 SRF_Celestiodetic CdSrf = null;
 SRF_Celestiodetic CdSrf1 = null;
 SRF_Celestiocentric CcSrf = null;

 try {

 // Create a Celestiodetic SRF with WGS 1984 and Identity transformation
 CdSrf = new SRF_Celestiodetic(SRM_ORM_Code.ORM_WGS_1984,
 SRM_HSR_Code.HSR_WGS_1984_IDENTITY);

 // Create a Celesticentric SRF with WGS 1984 and Identity transformation
 CcSrf = new SRF_Celestiocentric(SRM_ORM_Code.ORM_WGS_1984,
 SRM_HSR_Code.HSR_WGS_1984_IDENTITY);

 // Create a 3D Celestiodetic coordinate with
 // longitude           => 10.0 degrees (note: this input parameter is converted to radians)
 // latitude            => 20.0 degrees (note: this input parameter is converted to radians)
 // ellipsopidal height => 100.0 meters
 Coord3D_Celestiodetic CdCoord =
 (Coord3D_Celestiodetic)CdSrf.createCoordinate3D(Math.toRadians(10.0),
 Math.toRadians(20.0),
 100.0);

 // Instantiate a 3D Celestiocentric coordinate
 // This is an alternative method for instantiate a 3D coordinate
 Coord3D_Celestiocentric CcCoord = new Coord3D_Celestiocentric( CcSrf );

 // print out the SRF parameter calues and the coordinate component values
 System.out.println("CdSrf parameter =>  \n" + CdSrf);
 System.out.println("CcSrf parameter =>  \n" + CcSrf);
 System.out.println("CdCoord components (source) => \n" + CdCoord);

 // convert the 3D Celestiodetic coordinate to 3D Celestiocentric coordinate
 SRM_Coordinate_Valid_Region_Code valRegion =
 CcSrf.changeCoordinateSRF( CdCoord, CcCoord );

 // print out the values of the resulting 3D Celestiocentric coordinate
 System.out.println("CcCoord components (destination) => \n" + CcCoord + " is " + valRegion );

 }
 catch (SrmException ex) {
 // catch SrmException and print out the error code and text.
 System.out.println("Exception caught=> " + ex.what() + ", " + ex);
 }

 }
 }
 @endcode Running the sample program above will produce the output as follows:
 @verbatim
  *** Sample program using SRM Java API to convert a 3D coordinate
  *** from a Celestiodetic SRF to a Celestiocentric SRF.
 CdSrf parameter =>
 orm: WGS_1984
 hsr: WGS_1984_IDENTITY
 CcSrf parameter =>
 orm: WGS_1984
 hsr: WGS_1984_IDENTITY
 CdCoord components (source) =>
 [ 0.17453292519943295, 0.3490658503988659, 100.0 ]
 CcCoord components (destination) =>
 [ 5904838.698311626, 1041182.3792437915, 2167730.9898430835 ] is VALID
 @endverbatim
 */
package org.jscience.geography.coordinates;

import java.util.HashMap;

/**
 * The BaseSRF abstract class is the base class for all SRFs.
 *
 * @author David Shen
 * @see BaseSRF_2D, BaseSRF_3D
 */
public abstract class BaseSRF implements Cloneable {
    private SRM_SRF_Code _mySrfCode = SRM_SRF_Code.SRF_UNDEFINED;
    private SRM_SRFS_Code _mySrfsCode = SRM_SRFS_Code.SRFS_UNDEFINED;
    private int _mySrfsMemberCode = 0;

    // ORM code for the SRF
    protected SRM_ORM_Code _orm;

    // Hsr Code for the SRF
    protected SRM_HSR_Code _hsr;
    protected SRM_SRFT_Code _mySrftCode = SRM_SRFT_Code.SRFT_UNDEFINED;

    // The following Hash Maps does not need to be Synchrozined because the assumption
    // (constraint) is that a SRF cannot be shared by several threads.
    // this is for the caching of the interim conversion path and initialization data
    // This is a controlled version of the HashMap that currently limites the number
    // of cached OpSeq to 200 per SRF.
    protected CacheManager _myOpSeq;

    // this is for caching of the interim SRFs for conversion prior to the distance
    // computation
    protected HashMap _internalSRFs;

    /**
     * Creates a Standard SRF from its SRF code.
     *
     * @param srf_code in: the code for a standard SRF to create
     * @return a SRF template instance associated with the "Standard" SRF.
     * @note Use the concrete SRF classes to create SRFs that are not
     * "standard", which require specific constructor parameters.
     * @note All SRFs are intrinsically created from a template, hence the
     * appropriate template instance will always be returned.
     * @code ...
     * <p/>
     * // call createStandardSRF with:
     * //   SRF code     => SRF_BRITISH_NATIONAL_GRID
     * try {
     * BaseSRF bngSrf = BaseSRF.createStandardSRF( SRM_SRF_Code.SRF_BRITISH_NATIONAL_GRID );
     * } catch (SrmException ex)
     * ...
     * <p/>
     * // Note1: The returned object is of class SRF_TransverseMercator.
     * // Note2: All the Mercator SRF parameters are pre-defined for the BNG SRF.
     * @endcode
     * @see createStandardSRF(), SRM_SRF_Code
     *      <p/>
     *      A sample code to create a British National Grid (BNG) SRF is a follows:
     */
    public static BaseSRF createStandardSRF(SRM_SRF_Code srf_code)
            throws SrmException {
        if (srf_code == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createStandardSRF: null reference input parameter"));
        }

        if (srf_code == SRM_SRF_Code.SRF_UNDEFINED) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createStandardSRF: UNDEFINED SRF is not valid for this operation"));
        }

        return CreateSRF.standardSRF(srf_code);
    }

    /**
     * Creates an SRF from a SRF set code, a set member code specific to
     * that set, and an ORM code.
     *
     * @param srf_set    in: the code for an SRF set
     * @param set_member in: the code for an SRF set member.
     * @param orm        in: the ORM code associated with the created SRF
     * @param hsr        in: the HSR transformation associated with the created SRF
     * @return a SRF template instance associated with the SRF Set member.
     * @note Use the concrete SRF classes to create SRFs that are not
     * standard, which require specific constructor parameters.
     * @note All SRFs are intrinsically created from a template, hence the
     * appropriate template instance will always be returned.
     * @note The range of the set member code is defined for each SRF Set
     * as follows:
     * <ol>
     * <li>Alabama SPCS                     [ 1 <b>(West)</b>, 2 <b>(East)</b> ]</li>
     * <li>Global Coordinate System (GTRS)  [ 1, 49896 ] cell id</li>
     * <li>Lambert NTF                      [ 1 <b>(Zone I)</b>, 4 <b>(Zone IV)</b> ]</li>
     * <li>Japan Plane System               [ 1, 19 ]</li>
     * <li>Universal Polar Stereographic    [ 1 <b>(Northern Pole)</b>, 2 <b>(Southern Pole)</b>]</li>
     * <li>Universal Transverse Mercator    [ 1 <b>(Northern zone 1)</b>, 60 <b>(Northern zone 60)</b> ],
     * [ 61 <b>(Southern zone 1)</b>, 120 <b>(Southern zone 60)</b> ]</li>
     * <li>Wisconsin SPCS                   [ 1 <b>(South zone)</b>, 2 <b>(Central zone)</b>, 3 <b>(North zone)</b> ]</li>
     * </ol>
     * @note The member code is of type "int".  There are no member code predefined for GTRS in the
     * SRM_SSM_Code enumeration type due to its wide range.
     * @code ...
     * <p/>
     * // call createSRFSetMember with:
     * //   SRF Set code     => UTM
     * //   Set Member code  => Zone 12 southern hemisphere
     * //   ORM              => WGS 1984
     * //   HSR              => WGS 1984 IDENTITY transformation
     * try {
     * BaseSRF utmSrf = BaseSRF.createSRFSetMember( SRM_SRFS_Code.SRFS_UNIVERSAL_TRANSVERSE_MERCATOR,
     * SRF_SSR_Code._SSM_UTM_ZONE_12_SOUTHERN_HEMISPHERE,
     * SRM_ORM_Code.ORM_WGS_1984,
     * SRM_HSR_Code.HSR_WGS_1984_IDENTITY );
     * } catch (SrmException ex)
     * ...
     * <p/>
     * // Note1: The returned object is of class SRF_TransverseMercator.
     * // Note2: All other Transverse Mercator SRF parameters are fixed for a given Set member.
     * @endcode
     * @see createSRFSetMember(), SRM_SRFS_Code, SRM_SSM_Code
     *      <p/>
     *      A sample code to create a UTM SRF corresponding to Zone 12 Southern hemisphere based
     *      on WGS 1984  with Identity transformation is as follows:
     */
    public static BaseSRF createSRFSetMember(SRM_SRFS_Code srf_set,
                                             int set_member, SRM_ORM_Code orm, SRM_HSR_Code hsr)
            throws SrmException {
        if ((srf_set == null) || (orm == null) || (hsr == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createSRFSetMember: null reference input parameter"));
        }

        if (srf_set == SRM_SRFS_Code.SRFS_UNDEFINED) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createSRFSetMember: UNDEFINED SRF is not valid for this operation"));
        }

        return CreateSRF.srfSetMember(srf_set, set_member, orm, hsr);
    }

    /**
     * Returns this pre-defined ("standard") SRF code.
     *
     * @return a pre-defined SRF code of this SRF
     * @Note This method will return "SRF_UNDEFINED" if the SRF was not originally
     * created with the createStandardSRF method.
     * @see createStandardSRF()
     */
    public SRM_SRF_Code getSRFCode() {
        return _mySrfCode;
    }

    /**
     * Returns this SRF's Template code.
     *
     * @return an SRF Template code of this SRF
     * @see createStandardSRF()
     */
    public SRM_SRFT_Code getSRFTemplateCode() {
        return _mySrftCode;
    }

    /**
     * Returns this SRF's Set code.
     *
     * @return an SRF Set code of this SRF
     * @Note This method will return "SRFS_UNDEFINED" if the SRF Set was not originally
     * created with the createSRFSetMember method.
     * @see createSRFSetMember()
     */
    public SRM_SRFS_Code getSRFSetCode() {
        return _mySrfsCode;
    }

    /**
     * Returns this SRF's Set member code.
     *
     * @return an SRF Set member code of this SRF
     * @Note This method will return 0 (which means "UNDEFINED") if the SRF Set was not
     * originally created with the createSRFSetMember method.
     * @see createSRFSetMember()
     */
    public int getSRFSetMemberCode() {
        return _mySrfsMemberCode;
    }

    /**
     * Returns this SRF's Object Reference Model code.
     *
     * @return an ORM code of this SRF
     */
    public SRM_ORM_Code get_orm() {
        return _orm;
    }

    /**
     * Returns this SRF's Hsr code.
     *
     * @return an HSR code of this SRF
     */
    public SRM_HSR_Code get_hsr() {
        return _hsr;
    }

    /**
     * Changes a coordinate's values to this SRF.
     *
     * @param src in: the source coordinate in some other SRF
     * @param tgt in out: the target coordinate in this SRF
     * @return the Valid Region of the target coordinate
     * @Note The source and target coordinates must be of same the dimension (2D or 3D).
     */
    public SRM_Coordinate_Valid_Region_Code changeCoordinateSRF(Coord src,
                                                                Coord tgt) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid;
        double[] tgtValues = new double[3];

        if ((src == null) || (tgt == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeCoordinateSRF: null reference input parameter"));
        }

        if ((src instanceof Coord3D) && (tgt instanceof Coord3D)) {
            retValid = OpManager.instance().computeAsArray(src.getSRF(), this,
                    src.getValues(), tgtValues);
            ((Coord3D) tgt).setValues(tgtValues);

            return retValid;
        } else if ((src instanceof Coord2D) && (tgt instanceof Coord2D)) {
            double[] srcValues = {src.getValues()[0], src.getValues()[1], 0.0};

            retValid = OpManager.instance().computeAsArray(src.getSRF(), this,
                    srcValues, tgtValues);

            // this operation only uses the first two components of the tgtValues
            ((Coord2D) tgt).setValues(tgtValues);

            return retValid;
        } else {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeCoordinateSRF: Input coordinate of different dimensions"));
        }
    }

    /**
     * Check a coordinate in this SRF.
     *
     * @param src in: the source coordinate in some other SRF
     * @return the coordinate valid region code in the coordinate's SRF
     * @note The input coordinate must have been created using this SRF.
     */
    public SRM_Coordinate_Valid_Region_Code checkCoordinate(Coord src)
            throws SrmException {
        SRM_SRFT_Code myBoundaryTemplateSrf;
        SRM_SRFS_Code mySrfSet;

        if (src == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("checkCoordinate: null reference input parameter"));
        }

        if (src.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("checkCoordinate: Coordinate associated with different SRF"));
        }

        // get the template SRF where the boundary velidation will take place
        myBoundaryTemplateSrf = CoordCheck.getsrfBoundaryDefTemplate(this);

        SRM_Coordinate_Valid_Region_Code retValidity = SRM_Coordinate_Valid_Region_Code.VALID;

        mySrfSet = this.getSRFSetCode();

        if (myBoundaryTemplateSrf != this.getSRFTemplateCode()) {
            /*!\note
              This case is where the validity boundaries for
              an SRF are in a different SRF from the SRF itself
            */
            BaseSRF tmpSrf;
            double[] coord_tgt = new double[3];

            // instantiate cache for the interim SRFs
            if (this._internalSRFs == null) {
                this._internalSRFs = new HashMap();
            }

            tmpSrf = (BaseSRF) this._internalSRFs.get("IntCheckBoundSrfT");

            // create an interim SRF using the common
            if (tmpSrf == null) {
                tmpSrf = CreateSRF.fromCode(myBoundaryTemplateSrf,
                        this.get_orm(), this.get_hsr());

                // cache the created interim SRF
                this._internalSRFs.put("IntCheckBoundSrfT", tmpSrf);
            }

            // convert src coord to the interin Celestiodetic (in this case) coord.
            retValidity = OpManager.instance().computeAsArray(this, tmpSrf,
                    src.getValues(), coord_tgt);

            // Use specialized SRF coord checks if it a SRF Set member
            if (this.getSRFSetCode() != SRM_SRFS_Code.SRFS_UNDEFINED) {
                if (mySrfSet == SRM_SRFS_Code.SRFS_UNIVERSAL_TRANSVERSE_MERCATOR) {
                    retValidity = CoordCheck.forUTM_cd(((SRF_TransverseMercator) this).getSRFParameters(),
                            this.getSRFSetMemberCode(), coord_tgt);
                } else if (mySrfSet == SRM_SRFS_Code.SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM) {
                    retValidity = CoordCheck.forGTRS_cd(this.getSRFSetMemberCode(),
                            coord_tgt);
                } else if (mySrfSet == SRM_SRFS_Code.SRFS_UNIVERSAL_POLAR_STEREOGRAPHIC) {
                    retValidity = CoordCheck.forUPS_cd(((SRF_PolarStereographic) this).getSRFParameters(),
                            coord_tgt);
                } else if (mySrfSet == SRM_SRFS_Code.SRFS_ALABAMA_SPCS) {
                    retValidity = CoordCheck.forALSP_cd(new OrmData(this.get_orm()),
                            ((SRF_TransverseMercator) this).getSRFParameters(),
                            coord_tgt);
                } else if (mySrfSet == SRM_SRFS_Code.SRFS_WISCONSIN_SPCS) {
                    retValidity = CoordCheck.forWISP_cd(new OrmData(this.get_orm()), coord_tgt);
                } else if (mySrfSet == SRM_SRFS_Code.SRFS_LAMBERT_NTF) {
                    retValidity = CoordCheck.forLNTF_cd(this.getSRFSetMemberCode(),
                            new OrmData(this.get_orm()), coord_tgt);
                } else {
                    throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                            new String("Unsupported validity"));
                }
            }

            // THis case takes care of the template and the predefeind SRFs
            // Note:  As for now, all the boundary SRFs are Celestiodetic, thus the only case.
            // Since we are getting the validation result from the conversion to CD, we
            // just need to pass that result to the caller and no more checking is needed .
            // 			else
            // 			    {
            // 				if ( myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_CELESTIODETIC )
            // 				    retValidity = CoordCheck.forCelestiodetic( new OrmData(tmpSrf.get_orm()), coord_tgt );
            // 				else
            // 				    throw new SrmException( SrmException._INVALID_SOURCE_SRF,
            // 							    new String("Unsupported validity") );
            // 			    }
        } else {
            /*!\note this is the easy case where the boundaries are in the same srf*/

            // Notice that only the non projection based SRFs fall into this category
            if (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_CELESTIODETIC) {
                retValidity = CoordCheck.forCelestiodetic(new OrmData(this.get_orm()), src.getValues());
            } else if ((myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_LOCAL_SPACE_RECT_3D) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN)) {
                retValidity = CoordCheck.forNaN(src.getValues());
            } else if (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL) {
                retValidity = CoordCheck.forCylindrical(src.getValues());
            } else if (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL) {
                retValidity = CoordCheck.forAzSpherical(src.getValues());
            } else if ((myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_CELESTIOMAGNETIC) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_EQUATORIAL_INERTIAL) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_SOLAR_ECLIPTIC) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_SOLAR_EQUATORIAL) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_ECLIPTIC) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_DIPOLE) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_HELIOSPHERIC_ARIES_ECLIPTIC) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_ECLIPTIC) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_EQUATORIAL)) {
                retValidity = CoordCheck.forSpherical(src.getValues());
            }
            // !!! these need to be implemented in the future versions.
            else if ((myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_LOCAL_SPACE_RECT_2D) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_LOCAL_SPACE_AZIMUTHAL) ||
                    (myBoundaryTemplateSrf == SRM_SRFT_Code.SRFT_LOCAL_SPACE_POLAR)) {
                throw new SrmException(SrmException._NOT_IMPLEMENTED,
                        new String("checkCoordinate: Check for 2D SRF has not been implemented"));
            }
        }

        return retValidity;
    }

    /**
     * Returns the euclidean distance between two coordinates.
     *
     * @param coord1 in: a coordinate in some SRF
     * @param coord2 in: a coordinate in some SRF
     * @return the Euclidean distance between the two coordinates (in meters).
     * @note The input coordinates do not need to be from the same SRF, but must have the
     * same dimensionality, i.e., both 2D, 3D, or Surface.
     * @note This method will make (and cache) internal conversions when the inputs coordinates
     * are from SRFs other than SRF_Celestiocentric.
     */
    public static double calculateEuclideanDistance(Coord coord1, Coord coord2)
            throws SrmException {
        SRM_ORM_Code orm_ref_tgt;

        SRF_Celestiocentric tempCcSrf;
        double[] tempCcSrcCoord = new double[3];
        double[] tempCcTgtCoord = new double[3];

        double delta_x;
        double delta_y;
        double delta_z;

        /*Check for null reference*/
        if ((coord1 == null) || (coord2 == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("calculateEuclideanDistance: null reference input parameter"));
        }

        // !!! raise unimplemented exception if these are 2D coordinates Need Fix for future version
        if ((coord1.getSRF() instanceof BaseSRF_2D) ||
                (coord2.getSRF() instanceof BaseSRF_2D)) {
            throw new SrmException(SrmException._NOT_IMPLEMENTED,
                    new String("calculateEuclideanDistance: Not implemented for 2D coordinates" +
                            " in this release"));
        }

        /*Test to see if the source and target SRF's are both for the same body*/
        if (OrmDataSet.getElem(coord1.getSRF().get_orm())._reference_orm != OrmDataSet.getElem(coord2.getSRF().get_orm())._reference_orm) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("calculateEuclideanDistance: coordinates" +
                            " associated with different reference ORMs"));
        }

        // instantiate cache for the interim SRFs
        if (coord1.getSRF()._internalSRFs == null) {
            coord1.getSRF()._internalSRFs = new HashMap();
        }

        tempCcSrf = (SRF_Celestiocentric) coord1.getSRF()._internalSRFs.get("Interim_Cc");

        // create a Celestiocentric SRF using the common
        if (tempCcSrf == null) {
            tempCcSrf = new SRF_Celestiocentric(coord1.getSRF().get_orm(),
                    coord1.getSRF().get_hsr());
        }

        // cache the interim Celestiocentric in the (source) SRF for
        // coordinate 1.  We use the same one for coordinate 2.
        coord1.getSRF()._internalSRFs.put("Interim_Cc", tempCcSrf);

        // converting source coordinate to celestiocentric SRF
        OpManager.instance().computeAsArray(coord1.getSRF(), tempCcSrf,
                coord1.getValues(), tempCcSrcCoord);

        // 		System.out.println("tempCcSrcCoord=> " + tempCcSrcCoord[0] + ", "
        // 				   + tempCcSrcCoord[1] + ", "
        // 				   + tempCcSrcCoord[2]);
        // converting target coordinate to celestiocentric SRF
        OpManager.instance().computeAsArray(coord2.getSRF(), tempCcSrf,
                coord2.getValues(), tempCcTgtCoord);

        // 		System.out.println("tempCcTgtCoord=> " + tempCcTgtCoord[0] + ", "
        // 				   + tempCcTgtCoord[1] + ", "
        // 				   + tempCcTgtCoord[2]);
        delta_x = tempCcSrcCoord[0] - tempCcTgtCoord[0];
        delta_y = tempCcSrcCoord[1] - tempCcTgtCoord[1];
        delta_z = tempCcSrcCoord[2] - tempCcTgtCoord[2];

        return Math.sqrt(Const.square(delta_x) + Const.square(delta_y) +
                Const.square(delta_z));
    }

    /**
     * Returns a string representation of this SRF.
     */
    public abstract String toString();

    /**
     * @returns TRUE if the SRF parameters are equal.
     * @note This method returns FALSE if the input srf is null
     */
    public abstract boolean isEqual(BaseSRF srf);

    /**
     * Returns the shallow copy of this object instance.
     *
     * @note The cloned object will reference the same object as the
     * original object.
     */
    public BaseSRF makeClone() throws SrmException {
        try {
            return (BaseSRF) super.clone();
        } catch (java.lang.CloneNotSupportedException ex) {
            throw new SrmException(SrmException._INACTIONABLE,
                    new String("BaseSRF.makeClone(): failed"));
        }
    }

    protected void setSrfCode(SRM_SRF_Code srfCode) {
        _mySrfCode = srfCode;
    }

    protected void setSrfSetCode(SRM_SRFS_Code srfSetCode) {
        _mySrfsCode = srfSetCode;
    }

    protected void setSrfSetMemberCode(int srfSetMemberCode) {
        _mySrfsMemberCode = srfSetMemberCode;
    }
}
