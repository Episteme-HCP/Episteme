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

package org.jscience.biology.lsystems.common;

import java.util.Hashtable;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;

import javax.vecmath.Color3f;


/**
 * Base class for all Java 3D primitives.
 */
public abstract class Primitive extends Group {
    /** Specifies that normals are generated along with the positions. */
    public static final int GENERATE_NORMALS = 0x01;

    /**
     * Specifies that texture coordinates are generated along with the
     * positions.
     */
    public static final int GENERATE_TEXTURE_COORDS = 0x02;

    /** Specifies that normals are to be flipped along the surface. */
    public static final int GENERATE_NORMALS_INWARD = 0x04;

    /**
     * Specifies that the geometry being created will not be shared by
     * another scene graph node. By default all primitives created with the
     * same parameters share their geometry (you have 50 spheres in your
     * scene, but the geometry stored only once). A change to one primitive
     * will effect all shared nodes. You specify this flag if you do not wish
     * to share any geometry among primitives of the same parameters.
     */
    public static final int GEOMETRY_NOT_SHARED = 0x10;

    /**
     * Specifies that the ALLOW_INTERSECT capability bit should be set
     * on the generated geometry. This allows the object to be picked using
     * Geometry based picking.
     */
    public static final int ENABLE_GEOMETRY_PICKING = 0x20;

    /**
     * Specifies that the ALLOW_APPEARANCE_READ and
     * ALLOW_APPEARANCE_WRITE bits are to be set on the generated geometry's
     * Shape3D nodes.
     */
    public static final int ENABLE_APPEARANCE_MODIFY = 0x40;

    /** DOCUMENT ME! */
    static final int SPHERE = 0x01;

    /** DOCUMENT ME! */
    static final int CYLINDER = 0x02;

    /** DOCUMENT ME! */
    static final int CONE = 0x04;

    /** DOCUMENT ME! */
    static final int BOX = 0x08;

    /** DOCUMENT ME! */
    static Hashtable geomCache = new Hashtable();

    /** DOCUMENT ME! */
    int numTris = 0;

    /** DOCUMENT ME! */
    int numVerts = 0;

    /** Primitive flags. */
    int flags;

/**
     * Constructs a default primitive.
     */
    public Primitive() {
        flags = 0;
        setCapability(ENABLE_PICK_REPORTING);
        setCapability(ALLOW_CHILDREN_READ);
    }

    /**
     * Returns total number of triangles in this primitive.
     *
     * @return DOCUMENT ME!
     */
    public int getNumTriangles() {
        return numTris;
    }

    /**
     * Sets the total number of triangles in this primitive.
     *
     * @param num DOCUMENT ME!
     */
    public void setNumTriangles(int num) {
        numTris = num;
    }

    /**
     * Returns total number of vertices in this primitive.
     *
     * @return DOCUMENT ME!
     */
    public int getNumVertices() {
        return numVerts;
    }

    /**
     * Sets total number of vertices in this primitive.
     *
     * @param num DOCUMENT ME!
     */
    public void setNumVertices(int num) {
        numVerts = num;
    }

    /**
     * Returns the flags of primitive (generate normal, textures,
     * caching, etc).
     *
     * @return DOCUMENT ME!
     */
    public int getPrimitiveFlags() {
        return flags;
    }

    /**
     * Sets the flags of primitive (generate normal, textures, caching,
     * etc).
     *
     * @param fl DOCUMENT ME!
     */
    public void setPrimitiveFlags(int fl) {
        flags = fl;
    }

    /**
     * Obtains a shape node of a subpart of the primitive.
     *
     * @param partid identifier for a given subpart of the primitive.
     *
     * @return DOCUMENT ME!
     */
    public abstract Shape3D getShape(int partid);

    /**
     * Gets the appearance of the primitive (defaults to first
     * subpart).
     *
     * @return DOCUMENT ME!
     */
    public Appearance getAppearance() {
        return getShape(0).getAppearance();
    }

    /**
     * Sets the appearance of a subpart given a partid.
     *
     * @param partid DOCUMENT ME!
     * @param ap DOCUMENT ME!
     */
    public void setAppearance(int partid, Appearance ap) {
        getShape(partid).setAppearance(ap);
    }

    /**
     * Sets the main appearance of the primitive (all subparts) to same
     * appearance.
     *
     * @param ap DOCUMENT ME!
     */
    public abstract void setAppearance(Appearance ap);

    /**
     * Sets the main appearance of the primitive (all subparts) to a
     * default white appearance.
     */
    public void setAppearance() {
        Color3f aColor = new Color3f(0.1f, 0.1f, 0.1f);
        Color3f eColor = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f dColor = new Color3f(0.6f, 0.6f, 0.6f);
        Color3f sColor = new Color3f(1.0f, 1.0f, 1.0f);

        Material m = new Material(aColor, eColor, dColor, sColor, 100.0f);
        Appearance a = new Appearance();
        m.setLightingEnable(true);
        a.setMaterial(m);
        setAppearance(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String strfloat(float x) {
        return (new Float(x)).toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @param flags DOCUMENT ME!
     * @param geo DOCUMENT ME!
     */
    protected void cacheGeometry(int kind, float a, float b, float c, int d,
        int e, int flags, GeomBuffer geo) {
        String key = new String(kind + strfloat(a) + strfloat(b) + strfloat(c) +
                d + e + flags);
        geomCache.put(key, geo);
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @param flags DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GeomBuffer getCachedGeometry(int kind, float a, float b, float c,
        int d, int e, int flags) {
        String key = new String(kind + strfloat(a) + strfloat(b) + strfloat(c) +
                d + e + flags);
        Object cache = geomCache.get(key);

        return ((GeomBuffer) cache);
    }
}
