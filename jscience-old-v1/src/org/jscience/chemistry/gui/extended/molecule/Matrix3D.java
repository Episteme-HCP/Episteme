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
 * A fairly conventional 3D matrix object that can transform sets of 3D
 * points and perform a variety of manipulations on the transform.
 */
public class Matrix3D implements Cloneable {
    /** DOCUMENT ME! */
    static final double pi = Math.PI;

    /** DOCUMENT ME! */
    public float xx;

    /** DOCUMENT ME! */
    public float xy;

    /** DOCUMENT ME! */
    public float xz;

    /** DOCUMENT ME! */
    public float xo;

    /** DOCUMENT ME! */
    public float yx;

    /** DOCUMENT ME! */
    public float yy;

    /** DOCUMENT ME! */
    public float yz;

    /** DOCUMENT ME! */
    public float yo;

    /** DOCUMENT ME! */
    public float zx;

    /** DOCUMENT ME! */
    public float zy;

    /** DOCUMENT ME! */
    public float zz;

    /** DOCUMENT ME! */
    public float zo;

    /** DOCUMENT ME! */
    public float xscale;

    /** DOCUMENT ME! */
    public float yscale;

    /** DOCUMENT ME! */
    public float zscale;

/**
     * Create a new unit matrix
     */
    public Matrix3D() {
        xx = 1.0f;
        yy = 1.0f;
        zz = 1.0f;
    }

    /**
     * Returns a clone (Object) of matrix
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /**
     * Scale by f in all dimensions
     *
     * @param f DOCUMENT ME!
     */
    public void scale(float f) {
        xx *= f;
        xy *= f;
        xz *= f;
        xo *= f;
        yx *= f;
        yy *= f;
        yz *= f;
        yo *= f;
        zx *= f;
        zy *= f;
        zz *= f;
        zo *= f;
        xscale = yscale = zscale = f;
    }

    /**
     * Scale along each axis independently
     *
     * @param xf DOCUMENT ME!
     * @param yf DOCUMENT ME!
     * @param zf DOCUMENT ME!
     */
    public void scale(float xf, float yf, float zf) {
        xx *= xf;
        xy *= xf;
        xz *= xf;
        xo *= xf;
        yx *= yf;
        yy *= yf;
        yz *= yf;
        yo *= yf;
        zx *= zf;
        zy *= zf;
        zz *= zf;
        zo *= zf;
        xscale = xf;
        yscale = yf;
        zscale = zf;
    }

    /**
     * Returns X scale
     *
     * @return DOCUMENT ME!
     */
    public float getXScale() {
        return xscale;
    }

    /**
     * Returns Y scale
     *
     * @return DOCUMENT ME!
     */
    public float getYScale() {
        return yscale;
    }

    /**
     * Returns Z scale
     *
     * @return DOCUMENT ME!
     */
    public float getZScale() {
        return zscale;
    }

    /**
     * Translate the origin
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void translate(float x, float y, float z) {
        xo += x;
        yo += y;
        zo += z;
    }

    /**
     * Rotate theta degrees about the y axis
     *
     * @param theta DOCUMENT ME!
     */
    public void yrot(double theta) {
        theta *= (pi / 180);

        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        float Nxx = (float) ((xx * ct) + (zx * st));
        float Nxy = (float) ((xy * ct) + (zy * st));
        float Nxz = (float) ((xz * ct) + (zz * st));
        float Nxo = (float) ((xo * ct) + (zo * st));

        float Nzx = (float) ((zx * ct) - (xx * st));
        float Nzy = (float) ((zy * ct) - (xy * st));
        float Nzz = (float) ((zz * ct) - (xz * st));
        float Nzo = (float) ((zo * ct) - (xo * st));

        xo = Nxo;
        xx = Nxx;
        xy = Nxy;
        xz = Nxz;
        zo = Nzo;
        zx = Nzx;
        zy = Nzy;
        zz = Nzz;
    }

    /**
     * Rotate theta degrees about the x axis
     *
     * @param theta DOCUMENT ME!
     */
    public void xrot(double theta) {
        theta *= (pi / 180);

        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        float Nyx = (float) ((yx * ct) + (zx * st));
        float Nyy = (float) ((yy * ct) + (zy * st));
        float Nyz = (float) ((yz * ct) + (zz * st));
        float Nyo = (float) ((yo * ct) + (zo * st));

        float Nzx = (float) ((zx * ct) - (yx * st));
        float Nzy = (float) ((zy * ct) - (yy * st));
        float Nzz = (float) ((zz * ct) - (yz * st));
        float Nzo = (float) ((zo * ct) - (yo * st));

        yo = Nyo;
        yx = Nyx;
        yy = Nyy;
        yz = Nyz;
        zo = Nzo;
        zx = Nzx;
        zy = Nzy;
        zz = Nzz;
    }

    /**
     * Rotate theta degrees about the z axis
     *
     * @param theta DOCUMENT ME!
     */
    public void zrot(double theta) {
        theta *= (pi / 180);

        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        float Nyx = (float) ((yx * ct) + (xx * st));
        float Nyy = (float) ((yy * ct) + (xy * st));
        float Nyz = (float) ((yz * ct) + (xz * st));
        float Nyo = (float) ((yo * ct) + (xo * st));

        float Nxx = (float) ((xx * ct) - (yx * st));
        float Nxy = (float) ((xy * ct) - (yy * st));
        float Nxz = (float) ((xz * ct) - (yz * st));
        float Nxo = (float) ((xo * ct) - (yo * st));

        yo = Nyo;
        yx = Nyx;
        yy = Nyy;
        yz = Nyz;
        xo = Nxo;
        xx = Nxx;
        xy = Nxy;
        xz = Nxz;
    }

    /**
     * Multiply this matrix by a second: M = MR
     *
     * @param rhs DOCUMENT ME!
     */
    public void mult(Matrix3D rhs) {
        float lxx = (xx * rhs.xx) + (yx * rhs.xy) + (zx * rhs.xz);
        float lxy = (xy * rhs.xx) + (yy * rhs.xy) + (zy * rhs.xz);
        float lxz = (xz * rhs.xx) + (yz * rhs.xy) + (zz * rhs.xz);
        float lxo = (xo * rhs.xx) + (yo * rhs.xy) + (zo * rhs.xz) + rhs.xo;

        float lyx = (xx * rhs.yx) + (yx * rhs.yy) + (zx * rhs.yz);
        float lyy = (xy * rhs.yx) + (yy * rhs.yy) + (zy * rhs.yz);
        float lyz = (xz * rhs.yx) + (yz * rhs.yy) + (zz * rhs.yz);
        float lyo = (xo * rhs.yx) + (yo * rhs.yy) + (zo * rhs.yz) + rhs.yo;

        float lzx = (xx * rhs.zx) + (yx * rhs.zy) + (zx * rhs.zz);
        float lzy = (xy * rhs.zx) + (yy * rhs.zy) + (zy * rhs.zz);
        float lzz = (xz * rhs.zx) + (yz * rhs.zy) + (zz * rhs.zz);
        float lzo = (xo * rhs.zx) + (yo * rhs.zy) + (zo * rhs.zz) + rhs.zo;

        xx = lxx;
        xy = lxy;
        xz = lxz;
        xo = lxo;

        yx = lyx;
        yy = lyy;
        yz = lyz;
        yo = lyo;

        zx = lzx;
        zy = lzy;
        zz = lzz;
        zo = lzo;
    }

    /**
     * Reinitialize to the unit matrix
     */
    public void unit() {
        xo = 0;
        xx = 1;
        xy = 0;
        xz = 0;
        yo = 0;
        yx = 0;
        yy = 1;
        yz = 0;
        zo = 0;
        zx = 0;
        zy = 0;
        zz = 1;
    }

    /**
     * Transform last nvert points from v into tv.  v contains the
     * input coordinates in floating point.  Three successive entries in the
     * array constitute a point.  tv ends up holding the transformed points as
     * integers; three successive entries per point.
     *
     * @param v DOCUMENT ME!
     * @param tv DOCUMENT ME!
     * @param nvert DOCUMENT ME!
     */
    public void transform(float[] v, int[] tv, int nvert) {
        float lxx = xx;
        float lxy = xy;
        float lxz = xz;
        float lxo = xo;
        float lyx = yx;
        float lyy = yy;
        float lyz = yz;
        float lyo = yo;
        float lzx = zx;
        float lzy = zy;
        float lzz = zz;
        float lzo = zo;

        for (int i = nvert * 3; (i -= 3) >= 0;) {
            float x = v[i];
            float y = v[i + 1];
            float z = v[i + 2];
            tv[i] = (int) ((x * lxx) + (y * lxy) + (z * lxz) + lxo);
            tv[i + 1] = (int) ((x * lyx) + (y * lyy) + (z * lyz) + lyo);
            tv[i + 2] = (int) ((x * lzx) + (y * lzy) + (z * lzz) + lzo);
        }
    }

    /**
     * Transform last nvert points from v into tv.  v contains the
     * input coordinates in floating point.  Three successive entries in the
     * array constitute a point.  tv ends up holding the transformed points as
     * floating point; three successive entries per point.
     *
     * @param v DOCUMENT ME!
     * @param tv DOCUMENT ME!
     * @param nvert DOCUMENT ME!
     */
    public void transform(float[] v, float[] tv, int nvert) {
        float lxx = xx;
        float lxy = xy;
        float lxz = xz;
        float lxo = xo;
        float lyx = yx;
        float lyy = yy;
        float lyz = yz;
        float lyo = yo;
        float lzx = zx;
        float lzy = zy;
        float lzz = zz;
        float lzo = zo;

        for (int i = nvert * 3; (i -= 3) >= 0;) {
            float x = v[i];
            float y = v[i + 1];
            float z = v[i + 2];
            tv[i] = ((x * lxx) + (y * lxy) + (z * lxz) + lxo);
            tv[i + 1] = ((x * lyx) + (y * lyy) + (z * lyz) + lyo);
            tv[i + 2] = ((x * lzx) + (y * lzy) + (z * lzz) + lzo);
        }
    }

    /**
     * Transform first nvert points from x,y and z into tx,ty and tz.
     * x,y and z contains the input coordinates in double.  tx,ty and tz end
     * up holding the transformed points as double.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     * @param tx DOCUMENT ME!
     * @param ty DOCUMENT ME!
     * @param tz DOCUMENT ME!
     * @param nvert DOCUMENT ME!
     */
    public void transform(double[] x, double[] y, double[] z, double[] tx,
        double[] ty, double[] tz, int nvert) {
        double lxx = xx;
        double lxy = xy;
        double lxz = xz;
        double lxo = xo;
        double lyx = yx;
        double lyy = yy;
        double lyz = yz;
        double lyo = yo;
        double lzx = zx;
        double lzy = zy;
        double lzz = zz;
        double lzo = zo;

        for (int i = 0; i < nvert; i++) {
            tx[i] = ((x[i] * lxx) + (y[i] * lxy) + (z[i] * lxz) + lxo);
            ty[i] = ((x[i] * lyx) + (y[i] * lyy) + (z[i] * lyz) + lyo);
            tz[i] = ((x[i] * lzx) + (y[i] * lzy) + (z[i] * lzz) + lzo);
        }
    }

    /**
     * Transform first nvert points from x,y and z into tx,ty and tz.
     * x,y and z contains the input coordinates in floating point.  tx,ty and
     * tz end up holding the transformed points as floating point.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     * @param tx DOCUMENT ME!
     * @param ty DOCUMENT ME!
     * @param tz DOCUMENT ME!
     * @param nvert DOCUMENT ME!
     */
    public void transform(float[] x, float[] y, float[] z, float[] tx,
        float[] ty, float[] tz, int nvert) {
        float lxx = xx;
        float lxy = xy;
        float lxz = xz;
        float lxo = xo;
        float lyx = yx;
        float lyy = yy;
        float lyz = yz;
        float lyo = yo;
        float lzx = zx;
        float lzy = zy;
        float lzz = zz;
        float lzo = zo;

        for (int i = 0; i < nvert; i++) {
            tx[i] = ((x[i] * lxx) + (y[i] * lxy) + (z[i] * lxz) + lxo);
            ty[i] = ((x[i] * lyx) + (y[i] * lyy) + (z[i] * lyz) + lyo);
            tz[i] = ((x[i] * lzx) + (y[i] * lzy) + (z[i] * lzz) + lzo);
        }
    }

    /**
     * Transform first nvert Atoms in AtomVector
     *
     * @param av DOCUMENT ME!
     * @param nvert DOCUMENT ME!
     */
    public void transform(AtomVector av, int nvert) {
        float lxx = xx;
        float lxy = xy;
        float lxz = xz;
        float lxo = xo;
        float lyx = yx;
        float lyy = yy;
        float lyz = yz;
        float lyo = yo;
        float lzx = zx;
        float lzy = zy;
        float lzz = zz;
        float lzo = zo;

        for (int i = 0; i < nvert; i++) {
            Atom a = av.getAtom(i);
            a.tx = ((a.x * lxx) + (a.y * lxy) + (a.z * lxz) + lxo);
            a.ty = ((a.x * lyx) + (a.y * lyy) + (a.z * lyz) + lyo);
            a.tz = ((a.x * lzx) + (a.y * lzy) + (a.z * lzz) + lzo);
        }
    }

    /**
     * Transform everything appropriate in the molecule
     *
     * @param jmol DOCUMENT ME!
     */
    public void transform(Molecule jmol) {
        float lxx = xx;
        float lxy = xy;
        float lxz = xz;
        float lxo = xo;
        float lyx = yx;
        float lyy = yy;
        float lyz = yz;
        float lyo = yo;
        float lzx = zx;
        float lzy = zy;
        float lzz = zz;
        float lzo = zo;

        for (int i = 0; i < jmol.numAtoms; i++) {
            Atom a = jmol.myAtoms.getAtom(i);
            a.tx = ((a.x * lxx) + (a.y * lxy) + (a.z * lxz) + lxo);
            a.ty = ((a.x * lyx) + (a.y * lyy) + (a.z * lyz) + lyo);
            a.tz = ((a.x * lzx) + (a.y * lzy) + (a.z * lzz) + lzo);
        }

        for (int i = 0; i < jmol.numRings; i++) {
            Ring r = jmol.myRings.getRing(i);
            r.tx = ((r.x * lxx) + (r.y * lxy) + (r.z * lxz) + lxo);
            r.ty = ((r.x * lyx) + (r.y * lyy) + (r.z * lyz) + lyo);
            r.tz = ((r.x * lzx) + (r.y * lzy) + (r.z * lzz) + lzo);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ("[" + xo + "," + xx + "," + xy + "," + xz + ";" + yo + "," +
        yx + "," + yy + "," + yz + ";" + zo + "," + zx + "," + zy + "," + zz +
        "]");
    }
}
