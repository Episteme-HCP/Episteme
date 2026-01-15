/*
 * $Id: Polyhedron.java,v 1.3 2007-10-21 21:08:22 virtualcall Exp $
 *********************************************************
 * kaleido
 *
 *  Kaleidoscopic construction of uniform polyhedra
 *  Copyright © 1991-2003 Dr. Zvi Har’El <rl@math.technion.ac.il>
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *  
 *  3. The end-user documentation included with the redistribution,
 *     if any, must include the following acknowledgment:
 *  	“This product includes software developed by
 *  	 Dr. Zvi Har’El (http://www.math.technion.ac.il/~rl/).�?
 *     Alternately, this acknowledgment may appear in the software itself,
 *     if and wherever such third-party acknowledgments normally appear.
 *  
 *  This software is provided ‘as-is’, without any express or implied
 *  warranty.  In no event will the author be held liable for any
 *  damages arising from the use of this software.
 *
 *  Author:
 *	Dr. Zvi Har’El,
 *	Technion, Israel Institue of Technology,
 *	Haifa 32000, Israel.
 *	E-Mail: rl@math.technion.ac.il
 **********************************************************
 */

package org.jscience.mathematics.geometry.polyhedrons;

import org.jscience.mathematics.algebraic.numbers.Rational;
import org.jscience.mathematics.geometry.LiteralVector3D;
import org.jscience.mathematics.geometry.Vector3D;

import java.awt.*;
import java.lang.reflect.Array;

/**
 * The class <code>Polyhedron</code> contains the fields which describe a
 * uniform polyhedron and its duals and the method necessary to compute them
 * from the basic input, which is the Wythoff symbol of the polyhedron.
 *
 * @author <a href="http://www.math.technion.ac.il/~rl/">Zvi Har’El</a>
 * @version $Id: Polyhedron.java,v 1.3 2007-10-21 21:08:22 virtualcall Exp $
 * @see <a href="Polyhedron.java">Class source code</a>
 */
public class Polyhedron {

    /**
     * The index of the polyhedron in the standard list.
     *
     * @see UniformPolyhedrons#list
     */
    int index = -1;

    /**
     * The four elements of the Wythoff formula, with the vertical bar
     * represented by a zero.
     *
     * @see UniformPolyhedrons.PolyhedronEntry#wythoff
     * @see #parse
     */
    Rational[] p = new Rational[4];

    /**
     * The vertex valency. May be big for dihedral polyhedra.
     */
    int M;

    /**
     * The number of faces types. Atmost 5.
     */
    int N;

    /**
     * The vertex count.
     */
    int V;

    /**
     * The edge count.
     */
    int E;

    /**
     * The face count.
     */
    int F;

    /**
     * Euler characteristic of the polyhedron. It is given by Euler’s formula
     * <code>V - E + F = χ</code>. It is two for convex polyhedra.
     */
    int chi;

    /**
     * The density of the polyhedron. This is the number of times the spherical
     * projection of the polyhedron covers the sphere. It is one for convex
     * polyhedra.
     */
    int D;

    /**
     * The order of the symmetry group of the polyhedron.
     *
     * @see #moebius
     */
    int g;

    /**
     * The type of the symmetry group of the polyhedron. The possible values
     * are 2, 3, 4 and 5 for the dihedral, tetrahedral, octahedral and
     * icosahedral groups, respectively.
     */
    int K = 2;

    /**
     * Marks polyhedra of a “hemi�? type. Such polyhedra contain equatorial
     * faces, whose spherical projection is a hemisphere.
     *
     * @see #decompose
     */
    boolean hemi;

    /**
     * Marks one-sided, i.e., non-oriantable, polyhedra.
     *
     * @see #decompose
     */
    boolean onesided;

    /**
     * Marks the snub triangles in the vertex configuration. An array of length
     * {@link #M}
     */
    boolean[] snub;

    /**
     * A <code>boolean</code> array of length {@link #E}. It is not
     * <code>null</code> only for the duals of the uniform polyhedra of the
     * “hemi�? type, which have ideal vertices.
     *
     * @see #edgelist
     */
    boolean[] anti;

    /**
     * The type of the polygon serving as a base for a dihedral polyhedron.
     */
    double gon;

    /**
     * The smallest nonzero inradius of the polyhedron.
     */
    double minr;

    /**
     * The fundamental angles in radians. An array of length {@link #N}.
     */
    double[] gamma;
    /**
     * The number of faces at a vertex of each type. An array of length {@link
     * #N}.
     */
    double[] m;

    /**
     * The number of sides of a face of each type. An array of length {@link
     * #N}.
     */
    double[] n;

    /**
     * Marks the removed face in a polyhedron of type <code>pqr|</code>, where
     * one of the rationals has an even denominator.
     *
     * @see #decompose
     */
    int even = -1;

    /**
     * The face counts by type. An array of length {@link #N}.
     */
    int[] Fi;

    /**
     * Temporary storage for vertex generation. An array of length {@link #V}.
     */
    int[] firstrot;

    /**
     * The face types. An array of length {@link #F}.
     */
    int[] ftype;

    /**
     * The vertex configuration. A array of length {@link #M} with elements in
     * the range 0..N-1.
     */
    int[] rot;

    /**
     * The vertex-vertex adjacency matrix. A {@link #M} x {@link #V} matrix with
     * elements in the range 0..V-1.
     */
    int[][] adj;

    /**
     * The edge list. A 2 x {@link #E} matrix.
     */
    int[][] e;

    /**
     * The vertex-face incidence matrix. A {@link #M} x {@link #V} matrix with
     * elements in the range 0..F-1.
     */
    int[][] incid;

    /**
     * The printable vertex configuration.
     */
    String config;

    /**
     * The polyhedron name, standard or manifuctured.
     */
    String name;

    /**
     * The dual name, standard or manifuctured.
     */
    String dual;

    /**
     * The printable Wythoff symbol.
     */
    String wythoff = "";

    /**
     * The face coordinates. An array of length {@link #F}.
     */
    Vector3D[] f;

    /**
     * The vertex coordinates. An array of length {@link #V}.
     */
    Vector3D[] v;

    /**
     * The difference between <code>1.0</code> and the minimum
     * <code>double</code> greater than <code>1.0</code>. The exact value is
     * <code>2^-52</code>, approximately <code>2.2204460492503131e-16</code>.
     */
    static final double DBL_EPSILON =
            Double.longBitsToDouble(0X3CB0000000000000L);

    /**
     * The minimum difference in the coordinates between two distinct vertices.
     */
    static final double BIG_EPSILON = 3E-2;

    boolean drawn;
    private int cx, cy, radius;
    private Vector3D[] oldv, newv;

    Polyhedron(int i) {
        this("#" + (i + 1));
    }

    Polyhedron(String s) {
        parse(s);
        moebius();
        decompose();
        guessname();
        newton();
        exceptions();
        count();
        configuration();
        vertices();
        faces();
    }

    /**
     * Parse input symbol: Wythoff symbol or an index to {@link
     * UniformPolyhedrons#list}. The symbol is a # followed by a number, or a three
     * fractions and a bar in some order. We allow no bars only if it
     * result from the input symbol #80.
     */
    void parse(String s) {
        int pos, last;
        char c = 0;
        if (s == null || (s = s.trim()).length() == 0)
            throw new Error("no data");
        if (s.charAt(0) == '#') {
            s = s.substring(1).trim();
            if (s.length() == 0)
                throw new Error("no digit after #");
            int n = 0;
            for (pos = 0, last = s.length(); pos < last; pos++) {
                if (!Character.isDigit(c = s.charAt(pos))) break;
                n = n * 10 + c - '0';
            }
            if (pos == 0)
                throw new Error("not a digit: " + c);
            if (n == 0)
                throw new Error("zero index");
            if (n > UniformPolyhedrons.list.length)
                throw new Error("index too big");
            s = s.substring(pos).trim();
            if (s.length() != 0)
                throw new Error("data exceeded");
            s = UniformPolyhedrons.list[index = n - 1].wythoff;
        }
        int i = 0, bars = 0;
        for (; ;) {
            if (s.length() == 0) {
                if (i == 4 && (bars != 0 || index == UniformPolyhedrons.list.length - 1)) return;
                if (bars == 0)
                    throw new Error("no bars");
                throw new Error("not enough fractions");
            }
            if (i == 4)
                throw new Error("data exceeded");
            if (s.charAt(0) == '|') {
                if (++bars > 1)
                    throw new Error("too many bars");
                p[i++] = Rational.ZERO;
                s = s.substring(1).trim();
                continue;
            }
            int n = 0;
            for (pos = 0, last = s.length(); pos < last; pos++) {
                if (!Character.isDigit(c = s.charAt(pos))) break;
                n = n * 10 + c - '0';
            }
            if (pos == 0)
                throw new Error("not a digit: " + c);
            s = s.substring(pos).trim();
            if (s.length() == 0 || s.charAt(0) != '/') {
                p[i] = new Rational(n);
                if (!isValid(p[i++]))
                    throw new Error("fraction<=1");
                continue;
            }
            s = s.substring(1).trim();
            int d = 0;
            for (pos = 0, last = s.length(); pos < last; pos++) {
                if (!Character.isDigit(c = s.charAt(pos))) break;
                d = d * 10 + c - '0';
            }
            if (pos == 0)
                throw new Error("not a digit: " + c);
            if (d == 0)
                throw new Error("zero denominator");
            s = s.substring(pos).trim();
            p[i] = new Rational((double) n / d);
            if (!isValid(p[i++]))
                throw new Error("fraction<=1");
        }
    }

    /**
     * Using Wythoff symbol (p|qr, pq|r, pqr| or |pqr), find the Moebius
     * triangle (2 3 K) (or (2 2 n)) of the Schwarz triangle (pqr),
     * the order g of its symmetry group, its Euler characteristic chi,
     * and its covering density D.
     * <p/>
     * g is the number of copies of (2 3 K) covering the sphere, i.e.,
     * <pre>
     * 	    g * π * (1/2 + 1/3 + 1/K - 1) = 4 * π
     * </pre>
     * D is the number of times g copies of (pqr) cover the sphere, i.e.
     * <pre>
     * 	    D * 4 * π = g * π * (1/p + 1/q + 1/r - 1)
     * </pre>
     * chi is V - E + F, where F = g is the number of triangles, E = 3*g/2
     * is the number of triangle edges, and V = Vp+ Vq+ Vr, with Vp =
     * g/(2*np) being the number of vertices with angle π/p (np is the
     * numerator of p).
     */
    void moebius() {

        /*
        * Arrange Wythoff symbol in a presentable form. In the same
        * time check the restrictions on the three fractions: They all
        * have to be greater then one, and the numerators 4 or 5
        * cannot occur together. We count the ocurrences of 2 in
        * ‘twos’, and save the largest numerator in ‘K’, since they
        * reflect on the symmetry group.
        */
        int twos = 0;
        for (int j = 0; j < 4; j++) {
            if (!p[j].equals(0)) {
                if (j != 0 && !p[j - 1].equals(0)) wythoff += " ";
                wythoff += p[j];
                if (!p[j].equals(2)) {
                    int k = (int) p[j].getNumerator();
                    if (k > K) {
                        if (K == 4) break;
                        K = k;
                    } else if (k < K && k == 4) break;
                } else
                    twos++;
            } else {
                wythoff += "|";
            }
        }
        if (index == UniformPolyhedrons.list.length - 1)
            wythoff += "|";

        /*
        * Find the symmetry group K (where 2, 3, 4, 5 represent the
        * dihedral, tetrahedral, octahedral and icosahedral groups,
        * respectively), and its order g.
        */
        if (twos >= 2) {
            /*dihedral*/
            g = 4 * K;
            K = 2;
        } else {
            if (K > 5)
                throw new Error("numerator too large");
            g = 24 * K / (6 - K);
        }

        /*
        * Compute the nominal density D and Euler characteristic
        * chi. In few exceptional cases, these values will be
        * modified later.
        */
        if (index != UniformPolyhedrons.list.length - 1) {
            D = chi = -g;
            for (int j = 0; j < 4; j++)
                if (!p[j].equals(0)) {
                    int i = g / (int) p[j].getNumerator();
                    chi += i;
                    D += i * p[j].getDenominator();
                }
            chi /= 2;
            D /= 4;
            if (D <= 0)
                throw new Error("nonpositive density");
        }
    }

    /**
     * Decompose Schwarz triangle into N right triangles and compute the
     * vertex count V and the vertex valency M. V is computed from the
     * number g of Schwarz triangles in the cover, divided by the number of
     * triangles which share a vertex. It is halved for one-sided
     * polyhedra, because the kaleidoscopic construction really produces a
     * double orientable covering of such polyhedra. All q′ q|r are of the
     * “hemi�? type, i.e. have equatorial {2r} faces, and therefore are
     * (except 3/2 3|3 and the dihedra 2 2|r) one-sided. A well known
     * example is 3/2 3|4, the “one-sided heptahedron�?. Also, all p q r|
     * with one even denominator have a crossed parallelogram as a vertex
     * figure, and thus are one-sided as well.
     */
    void decompose() {
        if (p[1].equals(0)) {
            /* p|q r */
            N = 2;
            M = 2 * (int) p[0].getNumerator();
            V = g / M;
            n = new double[N];
            m = new double[N];
            rot = new int[M];
            for (int j = 0; j < 2; j++) {
                n[j] = p[j + 2].doubleValue();
                m[j] = p[0].doubleValue();
            }
            for (int l = 0; l < M; l++) {
                rot[l] = 0;
                rot[++l] = 1;
            }
        } else if (p[2].equals(0)) {
            /* p q|r */
            N = 3;
            M = 4;
            V = g / 2;
            n = new double[N];
            m = new double[N];
            rot = new int[M];
            n[0] = 2 * p[3].doubleValue();
            m[0] = 2;
            for (int j = 1, i = 0; j < 3; j++, i++) {
                n[j] = p[j - 1].doubleValue();
                m[j] = 1;
                rot[i] = 0;
                rot[++i] = j;
            }
            if (isCompl(p[0], p[1])) {
                /* p = q′ */
                hemi = true;
                D = 0;
                if (!p[0].equals(2) && !(p[3].equals(3) && (p[0].equals(3) ||
                        p[1].equals(3)))) {
                    onesided = true;
                    V /= 2;
                    chi /= 2;
                }
            }
        } else if (p[3].equals(0)) {
            /* p q r| */
            M = N = 3;
            V = g;
            n = new double[N];
            m = new double[N];
            rot = new int[M];
            for (int j = 0, i = 0; j < 3; j++, i++) {
                if (p[j].getDenominator() % 2 == 0) {
                    if (!p[(j + 1) % 3].equals(p[(j + 2) % 3])) {
                        /*needs postprocessing*/
                        even = j; /*memorize the removed face*/
                        chi -= g / p[j].getNumerator() / 2;
                        onesided = true;
                        D = 0;
                    } else {
                        /*for p = q we get a double 2 2r|p*/
                        /*noted by Roman Maeder <maeder@inf.ethz.ch> for 4 4 3/2|*/
                        /*Euler characteristic is still wrong*/
                        D /= 2;
                    }
                    V /= 2;
                }
                n[j] = 2 * p[j].doubleValue();
                m[j] = 1;
                rot[i] = j;
            }
        } else {
            /* |p q r - snub polyhedron*/
            N = 4;
            M = 6;
            V = g / 2; /* Only “white�? triangles carry a vertex */
            n = new double[N];
            m = new double[N];
            rot = new int[M];
            snub = new boolean[M];
            m[0] = n[0] = 3;
            for (int j = 1, i = 0; j < 4; j++, i++) {
                n[j] = p[j].doubleValue();
                m[j] = 1;
                rot[i] = 0;
                snub[i] = true;
                rot[++i] = j;
                snub[i] = false;
            }
        }

        /*
        * Sort the fundamental triangles (using bubble sort) according
        * to decreasing n[i], while pushing the trivial triangles (n[i]
        * = 2) to the end.
        */
        int J = N - 1;
        while (J != 0) {
            int last;
            last = J;
            J = 0;
            for (int j = 0; j < last; j++) {
                if ((n[j] < n[j + 1] || n[j] == 2) && n[j + 1] != 2) {
                    double tmp;
                    tmp = n[j];
                    n[j] = n[j + 1];
                    n[j + 1] = tmp;
                    tmp = m[j];
                    m[j] = m[j + 1];
                    m[j + 1] = tmp;
                    for (int i = 0; i < M; i++) {
                        if (rot[i] == j)
                            rot[i] = j + 1;
                        else if (rot[i] == j + 1) rot[i] = j;
                    }
                    if (even != -1) {
                        if (even == j)
                            even = j + 1;
                        else if (even == j + 1) even = j;
                    }
                    J = j;
                }
            }
        }

        /*
        * Get rid of repeated triangles.
        */
        for (J = 0; J < N && n[J] != 2; J++) {
            int k, j;
            for (j = J + 1; j < N && n[j] == n[J]; j++)
                m[J] += m[j];
            k = j - J - 1;
            if (k != 0) {
                for (int i = j; i < N; i++) {
                    n[i - k] = n[i];
                    m[i - k] = m[i];
                }
                N -= k;
                for (int i = 0; i < M; i++) {
                    if (rot[i] >= j)
                        rot[i] -= k;
                    else if (rot[i] > J) rot[i] = J;
                }
                if (even >= j) even -= k;
            }
        }

        /*
        * Get rid of trivial triangles.
        */
        if (J == 0) J = 1; /*hosohedron*/
        if (J < N) {
            N = J;
            for (int i = 0; i < M; i++) {
                if (rot[i] >= N) {
                    for (int j = i + 1; j < M; j++) {
                        rot[j - 1] = rot[j];
                        if (snub != null)
                            snub[j - 1] = snub[j];
                    }
                    M--;
                }
            }
        }

        /*
        * Truncate arrays
        */
        n = (double[]) resize(n, N);
        m = (double[]) resize(m, N);
        rot = (int[]) resize(rot, M);
        if (snub != null) snub = (boolean[]) resize(snub, M);
    }

    /**
     * Get the polyhedron name, using standard list or guesswork. Ideally,
     * we should try to locate the Wythoff symbol in the standard list
     * (unless, of course, it is dihedral), after doing few normalizations,
     * such as sorting angles and splitting isoceles triangles.
     */
    void guessname() {
        if (index != -1) {
            /*tabulated*/
            name = UniformPolyhedrons.list[index].name;
            dual = UniformPolyhedrons.list[index].dual;
        } else if (K == 2) {
            /*dihedral nontabulated*/
            if (p[0].equals(0)) {
                if (N == 1) {
                    name = "octahedron";
                    dual = "cube";
                } else {
                    if (n[0] > 3)
                        dihedral(n[0], "antiprism", "deltohedron");
                    else if (n[1] >= 2)
                        dihedral(n[1], "antiprism", "deltohedron");
                    else
                        dihedral(n[1], "crossed antiprism", "concave deltohedron");
                }
            } else if (p[3].equals(0) || p[2].equals(0) && p[3].equals(2)) {
                if (N == 1) {
                    name = "cube";
                    dual = "octahedron";
                } else {
                    if (n[0] > 4)
                        dihedral(n[0], "prism", "dipyramid");
                    else
                        dihedral(n[1], "prism", "dipyramid");
                }
            } else if (p[1].equals(0) && !p[0].equals(2)) {
                dihedral(m[0], "hosohedron", "dihedron");
            } else {
                dihedral(n[0], "dihedron", "hosohedron");
            }
        } else {
            /*other nontabulated*/
            final String pre[] = {"tetr", "oct", "icos"};
            name = pre[K - 3] + "ahedral ";
            if (onesided)
                name += "one-sided ";
            else if (D == 1)
                name += "convex ";
            else
                name += "nonconvex ";
            dual = name;
            name += "isogonal polyhedron";
            dual += "isohedral polyhedron";
        }
    }

    void dihedral(double gon, String name, String dual) {
        this.gon = gon;
        Rational r = new Rational(gon);
        if (gon < 2) r = compl(r);
        String pre = r + "-gonal ";
        this.name = pre + name;
        this.dual = pre + dual;
    }

    /**
     * Solve the fundamental right spherical triangles.
     */
    void newton() {

        /*
        * First, we find initial approximations.
        */
        double cosa;

        gamma = new double[N];
        if (N == 1) {
            gamma[0] = Math.PI / m[0];
            return;
        }
        for (int j = 0; j < N; j++) gamma[j] = Math.PI / 2 - Math.PI / n[j];

        /*
        * Next, iteratively find closer approximations for gamma[0]
        * and compute other gamma[j]’s from Napier’s equations.
        */
        for (; ;) {
            double delta = Math.PI, sigma = 0;
            for (int j = 0; j < N; j++) {
                delta -= m[j] * gamma[j];
            }
            if (Math.abs(delta) < 11 * DBL_EPSILON) return;
            /* On a RS/6000, fabs(delta)/DBL_EPSILON may occilate between 8 and 10. */
            /* Reported by David W. Sanderson <dws@ssec.wisc.edu> */
            for (int j = 0; j < N; j++) sigma += m[j] * Math.tan(gamma[j]);
            gamma[0] += delta * Math.tan(gamma[0]) / sigma;
            if (gamma[0] < 0 || gamma[0] > Math.PI)
                throw new Error("gamma out of bounds");
            cosa = Math.cos(Math.PI / n[0]) / Math.sin(gamma[0]);
            for (int j = 1; j < N; j++)
                gamma[j] = Math.asin(Math.cos(Math.PI / n[j]) / cosa);
        }
    }

    /**
     * Handle the exceptional cases which need postprocessing.
     * <p/>
     * Postprocess pqr| where r has an even denominator (cf.
     * Coxeter et al. Sec.9). Remove the {2r} and add a retrograde
     * {2p} and retrograde {2q}.
     * <p/>
     * Postprocess the last polyhedron |3/2 5/3 3 5/2 by taking a
     * |5/3 3 5/2, replacing the three snub triangles by four
     * equatorial squares and adding the missing {3/2} (retrograde
     * triangle, cf. Coxeter et al. Sec. 11).
     */
    void exceptions() {
        /*
        * The “even�? polyhedra
        */
        if (even != -1) {
            M = N = 4;
            n = (double[]) resize(n, N);
            m = (double[]) resize(m, N);
            gamma = (double[]) resize(gamma, N);
            rot = (int[]) resize(rot, M);
            for (int j = even + 1; j < 3; j++) {
                n[j - 1] = n[j];
                gamma[j - 1] = gamma[j];
            }
            n[2] = compl(n[1]);
            gamma[2] = -gamma[1];
            n[3] = compl(n[0]);
            m[3] = 1;
            gamma[3] = -gamma[0];
            rot[0] = 0;
            rot[1] = 1;
            rot[2] = 3;
            rot[3] = 2;
        }

        /*
        * The “last�? polyhedron
        */
        if (index == UniformPolyhedrons.list.length - 1) {
            N = 5;
            M = 8;
            n = (double[]) resize(n, N);
            m = (double[]) resize(m, N);
            gamma = (double[]) resize(gamma, N);
            rot = (int[]) resize(rot, M);
            snub = (boolean[]) resize(snub, M);
            hemi = true;
            D = 0;
            for (int j = 3; j != 0; j--) {
                m[j] = 1;
                n[j] = n[j - 1];
                gamma[j] = gamma[j - 1];
            }
            m[0] = n[0] = 4;
            gamma[0] = Math.PI / 2;
            m[4] = 1;
            n[4] = compl(n[1]);
            gamma[4] = -gamma[1];
            for (int j = 1; j < 6; j += 2) rot[j]++;
            rot[6] = 0;
            rot[7] = 4;
            snub[6] = true;
            snub[7] = false;
        }
    }

    /**
     * Compute edge and face counts, and update D and chi. Update D in the
     * few cases the density of the polyhedron is meaningful but different
     * than the density of the corresponding Schwarz triangle (cf. Coxeter
     * et al., p. 418 and p. 425). In these cases, spherical faces of one
     * type are concave (bigger than a hemisphere), and the actual density
     * is the number of these faces less the computed density. Note that
     * if j != 0, the assignment gamma[j] = asin(...) implies gamma[j]
     * cannot be obtuse. Also, compute chi for the only non-Wythoffian
     * polyhedron.
     */
    void count() {
        Fi = new int[N];
        for (int j = 0; j < N; j++) {
            int temp = V * (int) new Rational(m[j]).getNumerator();
            E += temp;
            F += Fi[j] = temp / (int) new Rational(n[j]).getNumerator();
        }
        E /= 2;
        if (D != 0 && gamma[0] > Math.PI / 2) D = Fi[0] - D;
        if (index == UniformPolyhedrons.list.length - 1) chi = V - E + F;
    }

    /**
     * Generate a printable vertex configuration symbol.
     */
    void configuration() {
        for (int j = 0; j < M; j++) {
            if (j == 0)
                config = "(";
            else
                config += ".";
            config += new Rational(n[rot[j]]).toString();
        }
        config += ")";
        int d = (int) new Rational(m[0]).getDenominator();
        if (d != 1) config += "/" + d;
    }

    /**
     * Compute polyhedron vertices and vertex adjecency lists. The
     * vertices adjacent to v[i] are v[adj[0][i], v[adj[1][i], ...
     * v[adj[M-1][i], ordered counterclockwise. The algorith is a BFS on
     * the vertices, in such a way that the vetices adjacent to a givem
     * vertex are obtained from its BFS parent by a cyclic sequence of
     * rotations. firstrot[i] points to the first rotaion in the sequence
     * when applied to v[i]. Note that for non-snub polyhedra, the
     * rotations at a child are opposite in sense when compared to the
     * rotations at the parent. Thus, we fill adj[*][i] from the end to
     * signify clockwise rotations. The firstrot[] array is not needed for
     * display thus it is freed after being used for face computations
     * below.
     */
    void vertices() {
        int newV = 2;
        v = new Vector3D[V];
        adj = new int[M][V];
        firstrot = new int[V]; /* temporary , put in Polyhedron structure so
                that may be freed on error */
        double cosa = Math.cos(Math.PI / n[0]) / Math.sin(gamma[0]);
        v[0] = new LiteralVector3D(0, 0, 1);
        firstrot[0] = 0;
        adj[0][0] = 1;
        v[1] = new LiteralVector3D(2 * cosa * Math.sqrt(1 - cosa * cosa), 0,
                2 * cosa * cosa - 1);
        if (snub == null) {
            firstrot[1] = 0;
            adj[0][1] = -1; /*start the other side*/
            adj[M - 1][1] = 0;
        } else {
            firstrot[1] = snub[M - 1] ? 0 : M - 1;
            adj[0][1] = 0;
        }
        for (int i = 0; i < newV; i++) {
            int last, one, start, limit;
            if (adj[0][i] == -1) {
                one = -1;
                start = M - 2;
                limit = -1;
            } else {
                one = 1;
                start = 1;
                limit = M;
            }
            int k = firstrot[i];
            for (int j = start; j != limit; j += one) {
                int J;
                Vector3D temp = rotate(v[adj[j - one][i]], v[i], one * 2 * gamma[rot[k]]);
                for (J = 0; J < newV && !v[J].identical(temp); J++) {
                }//same(temp, BIG_EPSILON)
                adj[j][i] = J;
                last = k;
                if (++k == M) k = 0;
                if (J == newV) {
                    /*new vertex*/
                    if (newV == V)
                        throw new Error("too many vertices");
                    v[newV++] = temp;
                    if (snub == null) {
                        firstrot[J] = k;
                        if (one > 0) {
                            adj[0][J] = -1;
                            adj[M - 1][J] = i;
                        } else {
                            adj[0][J] = i;
                        }
                    } else {
                        firstrot[J] = !snub[last] ? last :
                                !snub[k] ? (k + 1) % M : k;
                        adj[0][J] = i;
                    }
                }
            }
        }
    }

    /**
     * Compute polyhedron faces (dual vertices) and incidence matrices.
     * For orientable polyhedra, we can distinguish between the two faces
     * meeting at a given directed edge and identify the face on the left
     * and the face on the right, as seen from the outside. For one-sided
     * polyhedra, the vertex figure is a papillon (in Coxeter et al.
     * terminology, a crossed parallelogram) and the two faces meeting at
     * an edge can be identified as the side face (n[1] or n[2]) and the
     * diagonal face (n[0] or n[3]).
     */
    void faces() {
        int newF = 0;
        f = new Vector3D[F];
        ftype = new int[F];
        incid = new int[M][V];
        minr = 1 / Math.abs(Math.tan(Math.PI / n[hemi ? 1 : 0]) * Math.tan(gamma[hemi ? 1 : 0]));

        for (int i = M; --i >= 0;) {
            int j;
            for (j = V; --j >= 0;) incid[i][j] = -1;
        }
        for (int i = 0; i < V; i++) {
            int j;
            for (j = 0; j < M; j++) {
                int i0, J;
                int pap = 0; /*papillon edge type*/
                if (incid[j][i] != -1) continue;
                incid[j][i] = newF;
                if (newF == F)
                    throw new Error("too many faces");
                f[newF] = pole(minr, v[i], v[adj[j][i]],
                        v[adj[mod(j + 1, M)][i]]);
                ftype[newF] = rot[mod(firstrot[i] +
                        (adj[0][i] < adj[M - 1][i] ? j : -j - 2), M)];
                if (onesided) pap = (firstrot[i] + j) % 2;
                i0 = i;
                J = j;
                for (; ;) {
                    int k;
                    k = i0;
                    if ((i0 = adj[J][k]) == i) break;
                    for (J = 0; J < M && adj[J][i0] != k; J++) {
                    }
                    if (J == M)
                        throw new Error("too many faces");
                    if (onesided && (J + firstrot[i0]) % 2 == pap) {
                        incid[J][i0] = newF;
                        if (++J >= M) J = 0;
                    } else {
                        if (--J < 0) J = M - 1;
                        incid[J][i0] = newF;
                    }
                }
                newF++;
            }
        }
        firstrot = null;
        rot = null;
        snub = null;
    }

    /**
     * Computes edge list of the polyhedron or its dual. If the polyhedron
     * is of the “hemi�? type, each edge og the dual has one finite vertex
     * and one ideal vertex. We make sure the latter is always the
     * out-vertex, so that the edge becomes a ray (half-line). Each ideal
     * vertex is represented by a unit vector, and the direction of the ray
     * is either parallel or anti-parallel this vector. We flag this in the
     * array anti[E].
     */
    void edgelist(boolean uniform) {
        e = new int[2][E];
        if (uniform) {
            for (int i = 0, k = 0; i < V; i++) {
                for (int j = 0; j < M; j++) {
                    if (i < adj[j][i]) {
                        e[0][k] = i;
                        e[1][k++] = adj[j][i];
                    }
                }
            }
        } else {
            if (hemi) anti = new boolean[E];
            for (int i = 0, k = 0; i < V; i++) {
                for (int j = 0; j < M; j++) {
                    if (i < adj[j][i]) {
                        if (!hemi) {
                            e[0][k] = incid[mod(j - 1, M)][i];
                            e[1][k++] = incid[j][i];
                        } else {
                            if (ftype[incid[j][i]] != 0) {
                                e[0][k] = incid[j][i];
                                e[1][k] = incid[mod(j - 1, M)][i];
                            } else {
                                e[0][k] = incid[mod(j - 1, M)][i];
                                e[1][k] = incid[j][i];
                            }
                            anti[k] = f[e[0][k]].dotProduct(f[e[1][k]]) > 0;
                            k++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw a polyhedron.
     * <p/>
     * To enable drawing the duals of hemi-polyhedra, which have ideal
     * vertices as the polar reciprocals of hemispherical faces, anti[V]
     * may be given.
     * <p/>
     * anti[i] is set if the ray from the v[e[0][i]] to the ideal vertex
     * v[e[1][i]] is anti-parallel to the (unit) Vector representing that
     * vertex.
     * In a finite polyhedron, anti should be null.
     */
    void paint(PolyhedronDisplayer applet, Graphics g, double angle, boolean uniform) {
        Vector3D[] v;
        int V;
        String title;
        if (uniform) {
            v = this.v;
            V = this.V;
            title = index == -1 ? name : (index + 1) + ") " + name;
        } else {
            v = this.f;
            V = this.F;
            title = index == -1 ? dual : (index + 1) + "*) " + dual;
        }
        if (e == null) edgelist(uniform);
        if (newv == null) newv = new Vector3D[V];
        rotArray(v, newv, applet.azimuth, applet.elevation, angle);
        /*
        * Display titles.
        * Compute picture center and radius.
        */
        if (!drawn) {
            int width = applet.getSize().width;
            int height = applet.getSize().height;
            radius = Math.min((int) (height / 1.2), width) / 2;
            cx = width - radius;
            cy = height - radius;
            g.setColor(applet.color[0]);
            g.fillRect(0, 0, width, height);
            g.setColor(applet.color[15]);
            g.setFont(applet.bigFont);
            g.drawString(title, 0, 50);
            g.setFont(applet.smallFont);
            g.drawString(wythoff, 0, 75);
            g.setClip(0, 0, width, height);
            drawn = true;
        }
        /*
        * Draw new edges while erasing old ones. Edges are colored
        * according to the z-coordinate of their midpoint: the higher
        * - the dimmer (this is done because the vertices are
        * generated by BFS from (0,0,1), which implies that points
        * with lower z-coordinates are drawn later).
        */
        for (int i = 0; i < E; i++) {
            Vector3D temp;
            int j = e[0][i], k = e[1][i];
            if (anti == null) {
                if (oldv != null)
                    draw(applet, g, oldv[j], oldv[k], false);
                draw(applet, g, newv[j], newv[k], true);
            } else if (anti[i]) {
                if (oldv != null) {
                    temp = oldv[j].multiply(.5);
                    draw(applet, g, temp, temp.subtract(oldv[k]), false);
                }
                temp = newv[j].multiply(.5);
                draw(applet, g, temp, temp.subtract(newv[k]), true);
            } else {
                if (oldv != null) {
                    temp = oldv[j].multiply(.5);
                    draw(applet, g, temp, temp.add(oldv[k]), false);
                }
                temp = newv[j].multiply(.5);
                draw(applet, g, temp, temp.add(newv[k]), true);
            }
        }
        /**
         * Switch buffers
         */
        Vector3D[] tempv = newv;
        newv = oldv;
        oldv = tempv;
    }

    /*
    * draw a segment using variable brightness. when erasing a segment
    * (pen is zero), although variable brightness is not needed, we still
    * use the same partition to ensure correct erasure
    */
    void draw(PolyhedronDisplayer applet, Graphics g, Vector3D a, Vector3D b, boolean pen) {
        if (a.z() < b.z()) {
            /*swap endpoints to get increasing point size*/
            Vector3D temp;
            temp = a;
            a = b;
            b = temp;
        }
        double aa = 8 * (1 - a.z());
        double bb = 8 * (1 - b.z());
        int i1 = (int) (aa + 1);
        int i2 = (int) (bb + 1);
        if (i1 < 1) i1 = 1;
        if (i2 > 15) i2 = 15;
        double ax = cx + radius * a.x();
        double ay = cy + radius * a.y();
        double bx = cx + radius * b.x();
        double by = cy + radius * b.y();
        int x, y, newx, newy;
        x = (int) ax;
        y = (int) ay;
        for (int i = i1; i < i2; i++) {
            double f = ((double) i - aa) / (bb - aa);
            newx = (int) (ax + f * (bx - ax));
            newy = (int) (ay + f * (by - ay));
            g.setColor(applet.color[pen ? i : 0]);
            g.drawLine(x, y, newx, newy);
            x = newx;
            y = newy;
        }
        g.setColor(applet.color[pen ? i2 : 0]);
        newx = (int) bx;
        newy = (int) by;
        g.drawLine(x, y, newx, newy);
    }

    /**
     * compute the mathematical modulus function.
     */
    static int mod(int i, int j) {
        return (i %= j) >= 0 ? i : j < 0 ? i - j : i + j;
    }

    private Rational compl(Rational num) {
        return new Rational(num.getNumerator(), num.getNumerator() - num.getDenominator());
    }

    private boolean isCompl(Rational r1, Rational r2) {
        return r1.getNumerator() == r2.getNumerator() &&
                r1.getNumerator() == r1.getDenominator() + r2.getDenominator();
    }

    private boolean isValid(Rational num) {
        return num.getNumerator() > num.getDenominator();
    }

    private static double compl(double x) {
        return x / (x - 1);
    }

    private static Vector3D rotate(Vector3D vec, Vector3D axis, double angle) {
        Vector3D p = axis.multiply(vec.dotProduct(axis));
        return p.add(vec.subtract(p).multiply(Math.cos(angle))).
                subtract(vec.crossProduct(axis).multiply(Math.sin(angle)));
    }

    private final static Vector3D[] xyz = {new LiteralVector3D(1, 0, 0),
            new LiteralVector3D(0, 1, 0), new LiteralVector3D(0, 0, 1)};

    /**
     * rotates an array of vectors.
     */
    private static void rotArray(Vector3D[] v, Vector3D[] u, double azimuth,
                                 double elevation, double angle) {
        Vector3D axis = rotate(rotate(xyz[0], xyz[1], elevation), xyz[2],
                azimuth);
        Vector3D[] XYZ = new Vector3D[3];
        for (int i = 0; i < 3; i++)
            XYZ[i] = rotate(xyz[i], axis, angle);
        for (int i = 0; i < v.length; i++) {
            u[i] = XYZ[0].multiply(v[i].x()).
                    add(XYZ[1].multiply(v[i].y())).
                    add(XYZ[2].multiply(v[i].z()));
        }
    }

    /**
     * Compute the polar reciprocal of the plane containing a, b and c.
     * <p/>
     * If this plane does not contain the origin, return p such that
     * dot(p,a) = dot(p,b) = dot(p,b) = r.
     * <p/>
     * Otherwise, return p such that
     * dot(p,a) = dot(p,b) = dot(p,c) = 0
     * and
     * dot(p,p) = 1.
     */
    private static Vector3D pole(double r, Vector3D a, Vector3D b, Vector3D c) {
        Vector3D p = b.subtract(a).crossProduct(c.subtract(a));
        double k = p.dotProduct(a);
        if (Math.abs(k) < 1e-6)
            return p.multiply(1 / Math.sqrt(p.dotProduct(p)));
        else
            return p.multiply(r / k);
    }

    /**
     * Resize an array.
     */
    static Object resize(Object o, int l) {
        int ol = Array.getLength(o);
        if (ol == l) return o;
        Object no = Array.newInstance(o.getClass().getComponentType(), l);
        System.arraycopy(o, 0, no, 0, Math.min(l, ol));
        return no;
    }
}
