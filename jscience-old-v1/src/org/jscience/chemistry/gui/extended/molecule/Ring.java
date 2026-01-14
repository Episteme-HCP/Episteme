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

package org.jscience.chemistry.gui.extended.molecule;

/**
 * Tripos base Ring class
 *
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Orignal Version
 *         <p/>
 *         Modified: Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *         Changed x, y, z, tx, ty, tz from public to protected; added accessors
 *         date: 14 Aug 97
 */
public class Ring {
    /**
     * Ring id
     */
    protected int id;

    /**
     * Ring centroid *
     */
    protected float x;

    /**
     * Ring centroid *
     */
    protected float y;

    /**
     * Ring centroid *
     */
    protected float z;

    /**
     * Transformed ring centroid *
     */
    protected float tx;

    /**
     * Transformed ring centroid *
     */
    protected float ty;

    /**
     * Transformed ring centroid *
     */
    protected float tz;

    /**
     * Number of atoms in ring *
     */
    protected int numAtoms;

    /**
     * Ids of the atoms in the ring *
     */
    protected int[] atoms;

    /**
     * Full constructor
     *
     * @param id       ring id (1 based)
     * @param x        x coord of centroid
     * @param y        y coord of centroid
     * @param z        z coord of centroid
     * @param tx       tx coord of centroid
     * @param ty       ty coord of centroid
     * @param tz       tz coord of centroid
     * @param numAtoms number of atoms in the ring
     * @param atoms    ids of the atoms in the ring
     */
    public Ring(int id, float x, float y, float z, float tx, float ty,
                float tz, int numAtoms, int[] atoms) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.numAtoms = numAtoms;

        if (numAtoms > 0) {
            this.atoms = new int[numAtoms];
            System.arraycopy(atoms, 0, this.atoms, 0, numAtoms);
        } else {
            this.atoms = null;
        }
    }

    /**
     * Default constructor
     */
    public Ring() {
        this(0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0, null);
    }

    /**
     * Real coordinates constructor
     *
     * @param x x coord of centroid
     * @param y y coord of centroid
     * @param z z coord of centroid
     */
    public Ring(float x, float y, float z) {
        this(0, x, y, z, x, y, z, 0, null);
    }

    /**
     * Real and transformed coordinates constructor
     *
     * @param x  x coord of centroid
     * @param y  y coord of centroid
     * @param z  z coord of centroid
     * @param tx tx coord of centroid
     * @param ty ty coord of centroid
     * @param tz tz coord of centroid
     */
    public Ring(float x, float y, float z, float tx, float ty, float tz) {
        this(0, x, y, z, tx, ty, tz, 0, null);
    }

    /**
     * Copy constructor
     *
     * @param ring ring to be copied
     */
    public Ring(Ring ring) {
        if (ring == null) {
            ring = new Ring();
        }

        id = ring.id;
        x = ring.x;
        y = ring.y;
        z = ring.z;
        tx = ring.tx;
        ty = ring.ty;
        tz = ring.tz;
        numAtoms = ring.numAtoms;

        if (ring.numAtoms > 0) {
            atoms = new int[ring.numAtoms];
            System.arraycopy(ring.atoms, 0, atoms, 0, ring.numAtoms);
        } else {
            atoms = null;
        }
    }

    /**
     * Set the centroid of the ring
     *
     * @param x x coordinate of the centroid
     * @param y y coordinate of the centroid
     * @param z z coordinate of the centroid
     */
    protected void setCentroid(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Transforms center point coordinates
     *
     * @param m the trnasformation matrix to be applied
     */
    protected void transform(Matrix3D m) {
        tx = ((x * m.xx) + (y * m.xy) + (z * m.xz) + m.xo);
        ty = ((x * m.yx) + (y * m.yy) + (z * m.yz) + m.yo);
        tz = ((x * m.zx) + (y * m.zy) + (z * m.zz) + m.zo);
    }

    // additional getter section:

    /**
     * Return ring id
     */
    public int getId() {
        return id;
    }

    /**
     * Return number of atoms in the ring
     */
    public int getNumAtoms() {
        return numAtoms;
    }

    /**
     * Return ring centroid X coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Return ring centroid Y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Return ring centroid Z coordinate
     */
    public float getZ() {
        return z;
    }

    /**
     * Return transformed ring centroid X coordinate
     */
    public float getTx() {
        return tx;
    }

    /**
     * Return transformed ring centroid Y coordinate
     */
    public float getTy() {
        return ty;
    }

    /**
     * Return transformed ring centroid Z coordinate
     */
    public float getTz() {
        return tz;
    }

    /**
     * Return true if an atom is in this ring
     *
     * @param atom the atom
     */
    public boolean contains(Atom a) {
        int atomId = a.getId();

        for (int i = 0; i < atoms.length; i++) {
            if (atomId == atoms[i]) {
                return true;
            }
        }

        return false;
    }

    // additional setter methods:

    /**
     * Set ring id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set ring centroid X coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Set ring centroid Y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Set ring centroid Z coordinate
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Set transformed ring centroid X coordinate
     */
    public void setTx(float x) {
        this.x = x;
    }

    /**
     * Set transformed ring centroid Y coordinate
     */
    public void setTy(float y) {
        this.y = y;
    }

    /**
     * Set transformed ring centroid Z coordinate
     */
    public void setTz(float z) {
        this.z = z;
    }
} // End of class Ring
