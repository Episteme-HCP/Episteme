/*
 *        @(#)Cylinder.java 1.19 98/11/23 10:23:00
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
/*
---------------------------------------------------------------------
VIRTUAL PLANTS
==============

University of applied sciences Biel

hosted by Claude Schwab

modified by Rene Gressly

March - July 1999

---------------------------------------------------------------------
*/
package org.jscience.biology.lsystems.common;

import javax.media.j3d.*;
import javax.vecmath.Matrix4d;

/**
 * Truncated Cone is a geometry primitive defined with two radius and a height.
 * It is a truncated cone centered at the origin with its central axis
 * aligned along the Y-axis.
 * <p/>
 * When a texture is applied to a truncated cone, the texture is applied to the
 * caps and the body different. A texture is mapped CCW from the back of the
 * body. The top and bottom caps are mapped such that the texture appears
 * front facing when the caps are rotated 90 degrees toward the viewer.
 */
public class TruncatedCone extends Primitive {
    static final int MID_REZ_DIV_X = 15;
    static final int MID_REZ_DIV_Y = 1;

    /**
     * Designates the body of the truncated cone.  Used by <code>getShape</code>.
     */
    public static final int BODY = 0;

    /**
     * Designates the top end-cap of the truncated cone.
     * Used by <code>getShape</code>.
     */
    public static final int TOP = 1;

    /**
     * Designates the bottom end-cap of the truncated cone.
     * Used by <code>getShape</code>.
     */
    public static final int BOTTOM = 2;
    float radius1;
    float radius2;
    float height;
    int xdivisions;
    int ydivisions;
    boolean bDrawCaps;

    /**
     * Constructs a default truncated cone of bottom radius of 1.0, top radius 0.5 and height
     * of 2.0. Resolution defaults to 15 divisions along X axis and
     * 1 along the Y axis.
     */
    public TruncatedCone() {
        this(1.0f, 0.5f, 2.0f, GENERATE_NORMALS, MID_REZ_DIV_X, MID_REZ_DIV_Y,
                null /*,true*/);
    }

    /**
     * Constructs a default truncated cone of two given radius and height.
     *
     * @param radius1 Bottom Radius
     * @param radius2 Top Radius
     * @param height  Height
     */
    public TruncatedCone(float radius1, float radius2, float height) {
        this(radius1, radius2, height, GENERATE_NORMALS, MID_REZ_DIV_X,
                MID_REZ_DIV_Y, null /*, true*/);
    }

    /**
     * Constructs a default truncated cone of two given radius, height height and appearance.
     *
     * @param radius1   Bottom Radius
     * @param radius2   Top Radius
     * @param height    Height
     * @param ap        Appearance
     * @param bDrawCaps Indicates if top and bottom caps shall be drawn
     */
    public TruncatedCone(float radius1, float radius2, float height,
                         Appearance ap /*, boolean bDrawCaps*/) {
        this(radius1, radius2, height, GENERATE_NORMALS, MID_REZ_DIV_X,
                MID_REZ_DIV_Y, ap /*, bDrawCaps*/);
    }

    /**
     * Constructs a default truncated cone of two given radius, height, primitive flags and appearance.
     *
     * @param radius1   Bottom Radius
     * @param radius2   Top Radius
     * @param height    Height
     * @param primflags Flags
     * @param ap        Appearance
     * @param bDrawCaps Indicates if top and bottom caps shall be drawn.
     */
    public TruncatedCone(float radius1, float radius2, float height,
                         int primflags, Appearance ap /*, boolean bDrawCaps*/) {
        this(radius1, radius2, height, primflags, MID_REZ_DIV_X, MID_REZ_DIV_Y,
                ap /*, bDrawCaps*/);
    }

    /**
     * Constructs a customized truncated cone of two given radius, height,
     * resolution (X and Y dimensions), and appearance. The
     * resolution is defined in terms of number of subdivisions
     * along the object's X axis (width) and Y axis (height). More divisions
     * lead to more finely tesselated objects.
     *
     * @param radius    Radius
     * @param height    Height
     * @param xdivision Number of divisions along X direction.
     * @param ydivision Number of divisions along height of cylinder.
     * @param primflags Primitive flags.
     * @param ap        Appearance
     * @param bDrawCaps Indicates if top and bottom caps shall be drawn.
     */
    public TruncatedCone(float radius1, float radius2, float height,
                         int primflags, int xdivision, int ydivision,
                         Appearance ap /*, boolean bDrawCaps*/) {
        super();

        this.radius1 = radius1;
        this.radius2 = radius2;
        this.height = height;
        this.xdivisions = xdivision;
        this.ydivisions = ydivision;
        flags = primflags;

        //this.bDrawCaps = bDrawCaps;
        boolean outside = (flags & GENERATE_NORMALS_INWARD) == 0;

        // Create many body of the cylinder.
        Quadrics q = new Quadrics();
        GeomBuffer gbuf = null;
        Shape3D[] shape = new Shape3D[3];

        GeomBuffer cache = getCachedGeometry(Primitive.CYLINDER, radius1,
                radius2, height, xdivision, ydivision, primflags);

        if (cache != null) {
            shape[BODY] = new Shape3D(cache.getComputedGeometry());
            numVerts += cache.getNumVerts();
            numTris += cache.getNumTris();
        } else {
            gbuf = q.cylinder((double) radius1, (double) radius2,
                    (double) height, xdivision, ydivision, outside);
            shape[BODY] = new Shape3D(gbuf.getGeom(flags));
            numVerts += gbuf.getNumVerts();
            numTris += gbuf.getNumTris();

            if ((primflags & Primitive.GEOMETRY_NOT_SHARED) == 0) {
                cacheGeometry(Primitive.CYLINDER, radius1, radius2, height,
                        xdivision, ydivision, primflags, gbuf);
            }
        }

        if ((flags & ENABLE_APPEARANCE_MODIFY) != 0) {
            (shape[BODY]).setCapability(Shape3D.ALLOW_APPEARANCE_READ);
            (shape[BODY]).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        }

        // Rotate cylinder 90 deg to align it in the Y-axis.
        Matrix4d rotMat = new Matrix4d();
        Matrix4d objectMat = new Matrix4d();
        rotMat.setIdentity();
        objectMat.setIdentity();

        Transform3D t = new Transform3D();
        rotMat.rotX(Math.PI / -2.0);
        objectMat.mul(objectMat, rotMat);
        t.set(objectMat);

        // Add body to transform
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(ALLOW_CHILDREN_READ);
        objTrans.setTransform(t);
        objTrans.addChild(shape[BODY]);
        this.addChild(objTrans);

        /*if ( bDrawCaps )
        {
                // Create top of cylinder
                gbuf = q.disk((double)radius2, xdivision, outside);
                shape[TOP] = new Shape3D(gbuf.getGeom(flags));

                if ((flags & ENABLE_APPEARANCE_MODIFY) != 0)
                {
                        (shape[TOP]).setCapability(Shape3D.ALLOW_APPEARANCE_READ);
                        (shape[TOP]).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                }

                numVerts += gbuf.getNumVerts();
                numTris += gbuf.getNumTris();

                //move it up
                Transform3D t2 = new Transform3D();
                Vector3d vec = new Vector3d(0.0, 0.0, (double)height/2.0);
                t2.setTranslation(vec);

                TransformGroup objTrans2 = new TransformGroup();
                objTrans2.setTransform(t2);
                objTrans2.addChild(shape[TOP]);
                objTrans.addChild(objTrans2);

                // Create bottom
                gbuf = q.disk((double)radius1, xdivision, !outside);
                shape[BOTTOM] = new Shape3D(gbuf.getGeom(flags));

                if ((flags & ENABLE_APPEARANCE_MODIFY) != 0)
                {
                        (shape[BOTTOM]).setCapability(Shape3D.ALLOW_APPEARANCE_READ);
                        (shape[BOTTOM]).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                }

                numVerts += gbuf.getNumVerts();
                numTris += gbuf.getNumTris();

                // Flip the cap so that texture coords are right.
                Transform3D t3 = new Transform3D();
                rotMat = new Matrix4d();
                objectMat = new Matrix4d();
                rotMat.setIdentity();
                objectMat.setIdentity();
                // rotMat.rotZ(Math.PI);
                objectMat.mul(objectMat, rotMat);
                t3.set(objectMat);


                // Move it down
                vec = new Vector3d(0.0, 0.0, -1.0*(double)height/2.0);
                t3.setTranslation(vec);

                TransformGroup objTrans3 = new TransformGroup();
                objTrans3.setTransform(t3);
                objTrans3.addChild(shape[BOTTOM]);
                objTrans.addChild(objTrans3);

        }*/

        // Set Appearance
        if (ap == null) {
            setAppearance();
        } else {
            setAppearance(ap);
        }
    }

    /**
     * Obtains the Shape3D node associated with a given part of the truncated cone.
     * This allows users to modify the appearance or geometry of individual parts.
     *
     * @param partId The part to return (BODY, TOP, or BOTTOM).
     * @return The Shape3D object associated with the partID.  If an
     *         invalid partId is passed in, null is returned.
     */
    public Shape3D getShape(int partId) {
        if (partId == BODY) {
            return (Shape3D) ((Group) getChild(0)).getChild(BODY);
        } else if (partId == TOP) {
            return (Shape3D) ((Group) ((Group) getChild(0)).getChild(TOP)).getChild(0);
        } else if (partId == BOTTOM) {
            return (Shape3D) ((Group) ((Group) getChild(0)).getChild(BOTTOM)).getChild(0);
        }

        return null;
    }

    /**
     * Sets appearance of the truncated cone. This will set each part of the
     * truncated cone (TOP,BOTTOM,BODY) to the same appearance. To set each
     * part's appearance separately, use getShape(partId) to get the
     * individual shape and call shape.setAppearance(ap).
     */
    public void setAppearance(Appearance ap) {
        ((Shape3D) ((Group) getChild(0)).getChild(BODY)).setAppearance(ap);

        //((Shape3D)((Group)((Group)getChild(0)).getChild(TOP)).getChild(0)).setAppearance(ap);
        //((Shape3D)((Group)((Group)getChild(0)).getChild(BOTTOM)).getChild(0)).setAppearance(ap);
    }

    /**
     * Used to create a new instance of the node.  This routine is called
     * by <code>cloneTree</code> to duplicate the current node.
     * <code>cloneNode</code> should be overridden by any user subclassed
     * objects.  All subclasses must have their <code>cloneNode</code>
     * method consist of the following lines:
     * <P><blockquote><pre>
     *     public Node cloneNode(boolean forceDuplicate) {
     *         UserSubClass usc = new UserSubClass();
     *         usc.duplicateNode(this, forceDuplicate);
     *         return usc;
     *     }
     * </pre></blockquote>
     *
     * @param forceDuplicate when set to <code>true</code>, causes the
     *                       <code>duplicateOnCloneTree</code> flag to be ignored.  When
     *                       <code>false</code>, the value of each node's
     *                       <code>duplicateOnCloneTree</code> variable determines whether
     *                       NodeComponent data is duplicated or copied.
     * @see Node#cloneTree
     * @see Node#duplicateNode
     * @see NodeComponent#setDuplicateOnCloneTree
     */
    public Node cloneNode(boolean forceDuplicate) {
        TruncatedCone c = new TruncatedCone(radius1, radius2, height, flags,
                xdivisions, ydivisions, getAppearance() /*, bDrawCaps*/);
        c.duplicateNode(this, forceDuplicate);

        return c;
    }

    /**
     * Copies all node information from <code>originalNode</code> into
     * the current node.  This method is called from the
     * <code>cloneNode</code> method which is, in turn, called by the
     * <code>cloneTree</code> method.
     * <p/>
     * For any <i>NodeComponent</i> objects
     * contained by the object being duplicated, each <i>NodeComponent</i>
     * object's <code>duplicateOnCloneTree</code> value is used to determine
     * whether the <i>NodeComponent</i> should be duplicated in the new node
     * or if just a reference to the current node should be placed in the
     * new node.  This flag can be overridden by setting the
     * <code>forceDuplicate</code> parameter in the <code>cloneTree</code>
     * method to <code>true</code>.
     *
     * @param originalNode   the original node to duplicate.
     * @param forceDuplicate when set to <code>true</code>, causes the
     *                       <code>duplicateOnCloneTree</code> flag to be ignored.  When
     *                       <code>false</code>, the value of each node's
     *                       <code>duplicateOnCloneTree</code> variable determines whether
     *                       NodeComponent data is duplicated or copied.
     * @see Node#cloneTree
     * @see Node#cloneNode
     * @see NodeComponent#setDuplicateOnCloneTree
     */
    public void duplicateNode(Node originalNode, boolean forceDuplicate) {
        super.duplicateNode(originalNode, forceDuplicate);
    }
}
