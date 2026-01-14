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

package org.jscience.chemistry.gui.extended.graphics3d;

import org.jscience.chemistry.gui.extended.molecule.Atom;
import org.jscience.chemistry.gui.extended.molecule.Bond;

import javax.media.j3d.*;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class BondNode extends BranchGroup {
    /** DOCUMENT ME! */
    static final int STICK_QUALITY = 7;

    /** DOCUMENT ME! */
    Bond myBond;

    /** DOCUMENT ME! */
    Atom a1;

    /** DOCUMENT ME! */
    Atom a2;

    /** DOCUMENT ME! */
    RenderTable rTable = RenderTable.getTable();

/**
     * Creates a new BondNode object.
     *
     * @param b DOCUMENT ME!
     */
    protected BondNode(Bond b) {
        a1 = b.a1;
        a2 = b.a2;
        createStick();
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Node createWire(Bond b) {
        Atom a1 = b.a1;
        Atom a2 = b.a2;

        Group gn = new Group();
        Vector3f middle = getMiddleOfBond(a1, a2);
        float[] vert1 = new float[6];
        vert1[0] = a1.getX();
        vert1[1] = a1.getY();
        vert1[2] = a1.getZ();
        vert1[3] = (float) middle.x;
        vert1[4] = (float) middle.y;
        vert1[5] = (float) middle.z;

        float[] vert2 = new float[6];
        vert2[0] = (float) middle.x;
        vert2[1] = (float) middle.y;
        vert2[2] = (float) middle.z;
        vert2[3] = a2.getX();
        vert2[4] = a2.getY();
        vert2[5] = a2.getZ();

        LineAttributes att = new LineAttributes();
        att.setLineAntialiasingEnable(true);
        att.setLineWidth(2.5f);

        Appearance app1 = new Appearance();
        Appearance app2 = new Appearance();
        float cInc = 0.25f;
        float[] c = RenderTable.getTable().getRGBFloats(a1);
        ColoringAttributes ca1 = new ColoringAttributes(c[0] + cInc,
                c[1] + cInc, c[2] + cInc, ColoringAttributes.SHADE_FLAT);
        app1.setColoringAttributes(ca1);

        c = RenderTable.getTable().getRGBFloats(a2);

        ColoringAttributes ca2 = new ColoringAttributes(c[0] + cInc,
                c[1] + cInc, c[2] + cInc, ColoringAttributes.SHADE_FLAT);
        app2.setColoringAttributes(ca2);

        app1.setLineAttributes(att);
        app2.setLineAttributes(att);

        LineArray la1 = new LineArray(2,
                LineArray.COORDINATES | LineArray.NORMALS);
        LineArray la2 = new LineArray(2,
                LineArray.COORDINATES | LineArray.NORMALS);
        la1.setCoordinates(0, vert1);
        la1.setNormals(0, vert1);
        la2.setCoordinates(0, vert2);
        la2.setNormals(0, vert2);

        Shape3D s1 = new Shape3D(la1, app1);
        Shape3D s2 = new Shape3D(la2, app2);

        gn.addChild(s1);
        gn.addChild(s2);

        return gn;
    }

    /**
     * DOCUMENT ME!
     */
    void createStick() {
        Vector3f middle = getMiddleOfBond(a1, a2);
        float dist = calcDistance(a1, a2);
        Vector3f rel = new Vector3f((float) (a2.getX() - a1.getX()),
                (float) (a2.getY() - a1.getY()), (float) (a2.getZ() -
                a1.getZ()));

        double xrot = calcAngle(new Vector3f(0.0f, 1.0f, 0.0f), rel);

        Vector3f proj = new Vector3f(rel.x, 0.0f, rel.z);
        float yrot = calcSign(new Vector3f(1.0f, 0.0f, 0.0f), proj) * calcAngle(new Vector3f(
                    0.0f, 0.0f, 1.0f), proj);

        Node cyl1 = rTable.getSharedBondGroup(a1);
        Node cyl2 = rTable.getSharedBondGroup(a2);
        Transform3D rot = new Transform3D();
        rot.rotX(xrot);

        Transform3D t3d1 = new Transform3D();
        t3d1.rotY(yrot);
        t3d1.mul(rot);
        t3d1.setTranslation(new Vector3f(a1.getX(), a1.getY(), a1.getZ()));
        t3d1.setScale(new Vector3d(1.0, (double) (dist / 2.0f), 1.0));

        Transform3D t3d2 = new Transform3D();
        t3d2.rotY(yrot);
        t3d2.mul(rot);
        t3d2.setTranslation(middle);
        t3d2.setScale(new Vector3d(1.0, (double) (dist / 2.0f), 1.0));

        //t3d2.setScale(dist/2.0);
        TransformGroup myTrans = new TransformGroup(t3d1);
        TransformGroup mid = new TransformGroup(t3d2);

        //mid.addChild(cyl2.getShape());
        //myTrans.addChild(cyl1.getShape());
        mid.addChild(cyl2);
        myTrans.addChild(cyl1);

        addChild(myTrans);
        addChild(mid);
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float calcDistance(Atom from, Atom to) {
        double dx = (double) (to.getX() - from.getX());
        double dy = (double) (to.getY() - from.getY());
        double dz = (double) (to.getZ() - from.getZ());

        double dist = (double) Math.sqrt((double) ((dx * dx) + (dy * dy) +
                (dz * dz)));

        return (float) dist;
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static Vector3f getMiddleOfBond(Atom from, Atom to) {
        float x = (float) (((to.getX() - from.getX()) / 2.0f) + from.getX());
        float y = (float) (((to.getY() - from.getY()) / 2.0f) + from.getY());
        float z = (float) (((to.getZ() - from.getZ()) / 2.0f) + from.getZ());

        return new Vector3f(x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @param coor DOCUMENT ME!
     * @param other DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static float calcAngle(Vector3f coor, Vector3f other) {
        double scalarProduct = (double) coor.dot(other);

        if (scalarProduct == 0.0f) {
            //System.out.println("acos "+Math.acos(0.0f));
            return (float) Math.PI / 2.0f;
        }

        scalarProduct = scalarProduct / (coor.length() * other.length());

        return (float) Math.acos(scalarProduct);
    }

    /**
     * DOCUMENT ME!
     *
     * @param coor DOCUMENT ME!
     * @param other DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static float calcSign(Vector3f coor, Vector3f other) {
        double sp = (double) coor.dot(other);

        if (sp == 0.0f) {
            return 0.0f;
        }

        return (float) (sp / Math.abs(sp));
    }
}
