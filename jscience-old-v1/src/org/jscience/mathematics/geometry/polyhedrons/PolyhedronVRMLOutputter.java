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

package org.jscience.mathematics.geometry.polyhedrons;

import org.jscience.mathematics.algebraic.numbers.Rational;
import org.jscience.mathematics.geometry.Vector3D;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;


/**
 * Class Output
 *
 * @author <a href="http://www.math.technion.ac.il/~rl/">Zvi Har’El</a>
 * @version $Id: PolyhedronVRMLOutputter.java,v 1.2 2007-10-21 17:45:55 virtualcall Exp $
 * @see <a href="Output.java">Class source code</a>
 */
public class PolyhedronVRMLOutputter extends Polyhedron {
    /**
     * DOCUMENT ME!
     */
    DecimalFormat df = new DecimalFormat();

    /**
     * Creates a new PolyhedronVRMLOutputter object.
     *
     * @param s DOCUMENT ME!
     */
    PolyhedronVRMLOutputter(String s) {
        super(s);
    }

    /**
     * Prints the polyhedron data.
     *
     * @param justList        DOCUMENT ME!
     * @param needCoordinates DOCUMENT ME!
     * @param digits          DOCUMENT ME!
     */
    void print(boolean justList, boolean needCoordinates, int digits) {
        /*
         * Print polyhedron name, Wythoff symbol, and reference figures.
         */
        PrintStream out = System.out;

        if (index != -1) {
            out.print((index + 1) + ") ");
        }

        out.print(name + " " + wythoff);

        if ((index != -1) && (UniformPolyhedrons.list[index].coxeter != 0)) {
            out.print(" [" + UniformPolyhedrons.list[index].coxeter + "," +
                    UniformPolyhedrons.list[index].wenninger + "]");
        }

        out.println();

        if (justList) {
            return;
        }

        /*
         * Print combinatorial description.
         */
        final String[] group = {"di", "tetra", "octa", "icosa"};
        final String[] alias = {"D", "A4", "S4", "A5"};
        out.println("\tdual: " + dual);
        out.print("\t" + config + ", " + group[K - 2] + "hedral group " +
                alias[K - 2]);

        if (K == 2) {
            out.print(g / 4);
        }

        out.print(", chi=" + chi);

        if (D != 0) {
            out.print(", D=" + D);
        } else if (onesided) {
            out.print(", one-sided");
        } else {
            out.print(", two-sided");
        }

        out.println();
        out.print("\tV=" + V + ", E=" + E + ", F=" + F + "=");

        for (int j = 0; j < N; j++) {
            if (j != 0) {
                out.print("+");
            }

            out.print(Fi[j]);
            out.print("{" + new Rational(n[j]).toString() + "}");
        }

        /*
         * Print solution.
         */
        int w;

        if ((index == -1) && (K == 2)) {
            w = new Rational(gon).toString().length() + 2;

            if (w < 6) {
                w = 6;
            }
        } else {
            w = 6;
        }

        out.println();
        print("", w, out);
        print("α", 6, out);
        print("gamma", digits + 3, out);
        print("a", digits + 1, out);
        print("b", digits + 1, out);
        print("c", digits + 1, out);
        print("�?/R", digits + 3, out);
        print("r/�?", digits + 3, out);
        print("l/�?", digits + 3, out);
        print("h/r", digits + 3, out);
        out.println();

        double cosa = Math.cos(Math.PI / n[0]) / Math.sin(gamma[0]);

        for (int j = 0; j < N; j++) {
            double cosc = Math.cos(gamma[j]) / Math.sin(Math.PI / n[j]);
            print("{" + new Rational(n[j]).toString() + "}", w, out);
            print(180 / n[j], 6, 1, out);
            print(180 / Math.PI * gamma[j], digits + 3, digits - 2, out);
            print(180 / Math.PI * Math.acos(cosa), digits + 1, digits - 4, out);
            print(180 / Math.PI * Math.acos(cosa * cosc), digits + 1,
                    digits - 4, out);
            print(180 / Math.PI * Math.acos(cosc), digits + 1, digits - 4, out);
            print(cosa, digits + 3, digits, out);
            print(cosc, digits + 3, digits, out);

            if ((Math.log(Math.abs(cosa)) / Math.log(Math.E)) < -digits) {
                print("∞", digits + 3, out);
            } else {
                print(Math.sqrt(1 - (cosa * cosa)) / cosa, digits + 3, digits,
                        out);
            }

            if ((Math.log(Math.abs(cosc)) / Math.log(Math.E)) < -digits) {
                print("∞", digits + 3, out);
            } else {
                print(Math.sqrt(1 - (cosc * cosc)) / cosc, digits + 3, digits,
                        out);
            }

            out.println();
        }

        out.println();

        if (!needCoordinates) {
            return;
        }

        /*
         * Print vertices
         */
        out.println("vertices:");

        for (int i = 0; i < V; i++) {
            print("v" + (i + 1), -4, out);
            out.print(' ');
            print(v[i], digits + 3, digits, out);

            for (int j = 0; j < M; j++)
                print(" v" + (adj[j][i] + 1), -5, out);

            out.println();
            print("", (3 * digits) + 20, out);

            for (int j = 0; j < M; j++)
                print(" f" + (incid[j][i] + 1), -5, out);

            out.println();
        }

        /*
         * Print faces.
         */
        out.print("faces (RHS=");
        print(minr, digits + 2, digits, out);
        out.println("):");

        for (int i = 0; i < F; i++) {
            print("f" + (i + 1), -4, out);
            out.print(' ');
            print(f[i], digits + 3, digits, out);
            out.print(" {" + new Rational(n[ftype[i]]).toString() + "}");

            if (hemi && (ftype[i] == 0)) {
                out.print("*");
            }

            out.println();
        }

        out.println();
    }

    /**
     * DOCUMENT ME!
     *
     * @param s   DOCUMENT ME!
     * @param l   DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    private void print(String s, int l, PrintStream out) {
        boolean leftAdjust;

        if (l < 0) {
            leftAdjust = true;
            l = -l;
        } else {
            leftAdjust = false;
        }

        int N = l - s.length();

        if (N < 0) {
            out.print(s.substring(0, l));
        } else {
            if (leftAdjust) {
                out.print(s);
            }

            while (N-- > 0)
                out.print(' ');

            if (!leftAdjust) {
                out.print(s);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n   DOCUMENT ME!
     * @param l   DOCUMENT ME!
     * @param p   DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    private void print(double n, int l, int p, PrintStream out) {
        if (p > 17) {
            p = 17;
        } else if (p < 0) {
            p = 0;
        }

        df.applyPattern("0.00000000000000000".substring(0, p + 2));
        print(df.format(n), l, out);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v   DOCUMENT ME!
     * @param l   DOCUMENT ME!
     * @param p   DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    private void print(Vector3D v, int l, int p, PrintStream out) {
        out.print('(');
        print(v.x(), l, p, out);
        out.print(',');
        print(v.y(), l, p, out);
        out.print(',');
        print(v.z(), l, p, out);
        out.print(')');
    }

    /**
     * Writes the uniform polyhedron or its dual as a VRML file.
     *
     * @param prefix  DOCUMENT ME!
     * @param uniform DOCUMENT ME!
     * @param digits  DOCUMENT ME!
     * @throws Error DOCUMENT ME!
     */
    void write(String prefix, boolean uniform, int digits) {
        Vector3D[] v;
        Vector3D[] f;
        int V;
        int F;
        String name;

        if (uniform) {
            v = this.v;
            V = this.V;
            f = this.f;
            F = this.F;
            name = this.name;
        } else {
            v = this.f;
            V = this.F;
            f = this.v;
            F = this.V;
            name = this.dual;
        }

        /*
         * Open file
         */
        String fn = null;
        PrintStream out = null;
        df.applyPattern("000");

        for (int i = 1; i < 1000; i++) {
            fn = prefix + df.format(i) + ".wrl";

            if (!new File(fn).exists()) {
                try {
                    out = new PrintStream(new FileOutputStream(fn));
                } catch (Exception e) {
                }

                break;
            }
        }

        if (out == null) {
            throw new Error("Cannot open VRML file");
        }

        /*
         * File header
         */
        out.println("#VRML V2.0 utf8");
        out.println("WorldInfo{");
        out.println("\tinfo[");
        out.println("\t\t\"" + PolyhedronDisplayer.version + "\"");
        out.println("\t\t\"" + PolyhedronDisplayer.copyright + "\"");
        out.println("\t]");
        out.print("\ttitle \"");

        if (index != -1) {
            out.print(index + 1);

            if (!uniform) {
                out.print("*");
            }

            out.print(") ");
        }

        out.print(name + " " + wythoff + " " + config);
        out.println("\"");
        out.println("}");
        out.println("NavigationInfo {");
        out.println("\ttype \"EXAMINE\"");

        //out.println("\theadlight TRUE");
        out.println("}");
        out.println("Shape{");
        out.println("\tappearance Appearance{");
        out.println("\t\tmaterial Material{");
        out.println("\t\t\tshininess 1");
        out.println("\t\t}");
        out.println("\t}");
        out.println("\tgeometry IndexedFaceSet{");

        if (D != 1) {
            out.println("\t\tconvex FALSE");
            out.println("\t\tsolid FALSE");
        }

        out.println("\t\tcreaseAngle 0");
        out.println("\t\tcolorPerVertex FALSE");

        /*
         * Color map
         * Face colors are assigned as a function of the face valency.
         * Thus, pentagons and pentagrams will be colored alike.
         */
        out.println("\t\tcolor Color{");
        out.print("\t\t\tcolor[");

        if (!uniform) {
            out.print(rgb(M));
        } else {
            for (int i = 0; i < N; i++)
                out.print(rgb((int) new Rational(n[i]).getNumerator()));
        }

        out.println("]");
        out.println("\t\t}");

        /*
         * Vertex list
         */
        out.println("\t\tcoord Coordinate{");
        out.println("\t\t\tpoint[");

        for (int i = 0; i < V; i++)
            write(v[i], digits, out);

        /*
         * Auxiliary vertices (needed because current VRML browsers
         * cannot handle non-simple polygons, i.e., ploygons with self
         * intersections): Each non-simple face is assigned an
         * auxiliary vertex. By connecting it to the rest of the
         * vertices the face is triangulated. The circum-center is used
         * for the regular star faces of uniform polyhedra. The
         * in-center is used for the pentagram (#79) and hexagram (#77)
         * of the high-density snub duals, and for the pentagrams (#40,
         * #58) and hexagram (#52) of the stellated duals with
         * configuration (....)/2. Finally, the self-intersection of
         * the crossed parallelogram is used for duals with form p q r|
         * with an even denominator.
         *
         * This method do not work for the hemi-duals, whose faces are
         * not star-shaped and have two self-intersections each. Thus,
         * for each face we need six auxiliary vertices: The self
         * intersections and the terminal points of the truncations of
         * the infinite edges. The ideal vertices are listed, but are
         * not used by the face-list. Note that the face of the last
         * dual (#80) is octagonal, and constists of two quadrilaterals
         * of the infinite type.
         */
        out.println("\t\t\t\t# auxiliary vertices:");

        boolean[] hit = null;

        if (!uniform && (even != -1)) {
            hit = new boolean[F];
        }

        for (int i = 0; i < F; i++) {
            if ((uniform && isStar(new Rational(n[ftype[i]]))) ||
                    (!uniform &&
                            (((K == 5) && (D > 30)) || !new Rational(m[0]).isInteger()))) {
                /*
                 * find the center of the face
                 */
                double h;

                if (uniform && hemi && (ftype[i] == 0)) {
                    h = 0;
                } else {
                    h = minr / f[i].dotProduct(f[i]);
                }

                write(f[i].multiply(h), digits, out);
            } else if (!uniform && (even != -1)) {
                /*
                 * find the self-intersection of a crossed
                 * parallelogram. hit is set if v0v1 intersects
                 * v2v3
                 */
                Vector3D v0 = v[incid[0][i]];
                Vector3D v1 = v[incid[1][i]];
                Vector3D v2 = v[incid[2][i]];
                Vector3D v3 = v[incid[3][i]];
                double d0 = Math.sqrt(v0.subtract(v2).dotProduct(v0.subtract(v2)));
                double d1 = Math.sqrt(v1.subtract(v3).dotProduct(v1.subtract(v3)));
                Vector3D c0 = v0.add(v2).multiply(d1);
                Vector3D c1 = v1.add(v3).multiply(d0);
                Vector3D p = c0.add(c1).multiply(0.5 / (d0 + d1));
                write(p, digits, out);
                p = p.subtract(v2).crossProduct(p.subtract(v3));
                hit[i] = p.dotProduct(p) < 1e-6;
            } else if (!uniform && hemi &&
                    (index != (UniformPolyhedrons.list.length - 1))) {
                /*
                 * find the terminal points of the truncation
                 * and the self-intersections.
                 *  v23       v0       v21
                 *  |  \     /  \     /  |
                 *  |   v0123    v0321   |
                 *  |  /     \  /     \  |
                 *  v01       v2       v03
                 */
                int j = (ftype[incid[0][i]] == 0) ? 1 : 0;
                Vector3D v0 = v[incid[j][i]]; /*real vertex*/
                Vector3D v1 = v[incid[j + 1][i]]; /*ideal vertex (unit vector)*/
                Vector3D v2 = v[incid[j + 2][i]]; /*real*/
                Vector3D v3 = v[incid[(j + 3) % 4][i]]; /*ideal*/

                /*
                 * compute intersections:
                 * this uses the following linear algebra:
                 *  v0123 = v0 + a v1 = v2 + b v3
                 *  v0 x v3 + a (v1 x v3) = v2 x v3
                 *  a (v1 x v3) = (v2 - v0) x v3
                 *  a (v1 x v3) . (v1 x v3) = (v2 - v0) x v3 . (v1 x v3)
                */
                Vector3D u = v1.crossProduct(v3);
                Vector3D v0123 = v0.add(v1.multiply(
                        v2.subtract(v0).crossProduct(v3).dotProduct(u) / u.dotProduct(
                                u)));
                Vector3D v0321 = v0.add(v3.multiply(
                        v0.subtract(v2).crossProduct(v1).dotProduct(u) / u.dotProduct(
                                u)));

                /*compute truncations*/
                double t = 1.5; /*truncation adjustment factor*/
                Vector3D v01 = v0.add(v0123.subtract(v0).multiply(t));
                Vector3D v23 = v2.add(v0123.subtract(v2).multiply(t));
                Vector3D v03 = v0.add(v0321.subtract(v0).multiply(t));
                Vector3D v21 = v2.add(v0321.subtract(v2).multiply(t));
                write(v01, digits, out);
                write(v23, digits, out);
                write(v0123, digits, out);
                write(v03, digits, out);
                write(v21, digits, out);
                write(v0321, digits, out);
            } else if (!uniform &&
                    (index == (UniformPolyhedrons.list.length - 1))) {
                /*
                 * find the terminal points of the truncation
                 * and the self-intersections.
                 *  v23       v0       v21
                 *  |  \     /  \     /  |
                 *  |   v0123    v0721   |
                 *  |  /     \  /     \  |
                 *  v01       v2       v07
                 *
                 *  v65       v4       v67
                 *  |  \     /  \     /  |
                 *  |   v4365    v4567   |
                 *  |  /     \  /     \  |
                 *  v43       v6       v45
                 */
                int j;

                for (j = 0; j < 8; j++)
                    if (ftype[incid[j][i]] == 3) {
                        break;
                    }

                Vector3D v0 = v[incid[j][i]]; /*real {5/3}*/
                Vector3D v1 = v[incid[(j + 1) % 8][i]]; /*ideal*/
                Vector3D v2 = v[incid[(j + 2) % 8][i]]; /*real {3}*/
                Vector3D v3 = v[incid[(j + 3) % 8][i]]; /*ideal*/
                Vector3D v4 = v[incid[(j + 4) % 8][i]]; /*real {5/2}*/
                Vector3D v5 = v[incid[(j + 5) % 8][i]]; /*ideal*/
                Vector3D v6 = v[incid[(j + 6) % 8][i]]; /*real {3/2}*/
                Vector3D v7 = v[incid[(j + 7) % 8][i]]; /*ideal*/

                /*compute intersections*/
                Vector3D u = v1.crossProduct(v3);
                Vector3D v0123 = v0.add(v1.multiply(
                        v2.subtract(v0).crossProduct(v3).dotProduct(u) / u.dotProduct(
                                u)));
                u = v7.crossProduct(v1);

                Vector3D v0721 = v0.add(v7.multiply(
                        v2.subtract(v0).crossProduct(v1).dotProduct(u) / u.dotProduct(
                                u)));
                u = v5.crossProduct(v7);

                Vector3D v4567 = v4.add(v5.multiply(
                        v6.subtract(v4).crossProduct(v7).dotProduct(u) / u.dotProduct(
                                u)));
                u = v3.crossProduct(v5);

                Vector3D v4365 = v4.add(v3.multiply(
                        v6.subtract(v4).crossProduct(v5).dotProduct(u) / u.dotProduct(
                                u)));

                /*compute truncations*/
                double t = 1.5; /*truncation adjustment factor*/
                Vector3D v01 = v0.add(v0123.subtract(v0).multiply(t));
                Vector3D v23 = v2.add(v0123.subtract(v2).multiply(t));
                Vector3D v07 = v0.add(v0721.subtract(v0).multiply(t));
                Vector3D v21 = v2.add(v0721.subtract(v2).multiply(t));
                Vector3D v45 = v4.add(v4567.subtract(v4).multiply(t));
                Vector3D v67 = v6.add(v4567.subtract(v6).multiply(t));
                Vector3D v43 = v4.add(v4365.subtract(v4).multiply(t));
                Vector3D v65 = v6.add(v4365.subtract(v6).multiply(t));
                write(v01, digits, out);
                write(v23, digits, out);
                write(v0123, digits, out);
                write(v07, digits, out);
                write(v21, digits, out);
                write(v0721, digits, out);
                write(v45, digits, out);
                write(v67, digits, out);
                write(v4567, digits, out);
                write(v43, digits, out);
                write(v65, digits, out);
                write(v4365, digits, out);
            }
        }

        out.println("\t\t\t]");
        out.println("\t\t}");

        /*
         * Face list:
         * Each face is printed in a separate line, by listing the
         * indices of its vertices. In the non-simple case, the polygon
         * is represented by the triangulation, each triangle consists
         * of two polyhedron vertices and one auxiliary vertex.
         * The total number of facelets will be used later, when we generate
         * the colorIndex entry for momochromatic polyhedra (dual and regular)
         */
        out.println("\t\tcoordIndex[");

        int ii = V;
        int facelets = 0;

        for (int i = 0; i < F; i++) {
            out.print("\t\t\t");

            if (!uniform) {
                if (((K == 5) && (D > 30)) || !new Rational(m[0]).isInteger()) {
                    for (int j = 0; j < (M - 1); j++) {
                        out.print(incid[j][i]);
                        out.print(",");
                        out.print(incid[j + 1][i]);
                        out.print(",");
                        out.print(ii);
                        out.print(",-1,");
                        facelets++;
                    }

                    out.print(incid[M - 1][i]);
                    out.print(",");
                    out.print(incid[0][i]);
                    out.print(",");
                    out.print(ii++);
                    out.print(",-1,");
                    facelets++;
                } else if (even != -1) {
                    if (hit[i]) {
                        out.print(incid[3][i]);
                        out.print(",");
                        out.print(incid[0][i]);
                        out.print(",");
                        out.print(ii);
                        out.print(",-1,");
                        facelets++;
                        out.print(incid[1][i]);
                        out.print(",");
                        out.print(incid[2][i]);
                        out.print(",");
                        out.print(ii++);
                        out.print(",-1,");
                        facelets++;
                    } else {
                        out.print(incid[0][i]);
                        out.print(",");
                        out.print(incid[1][i]);
                        out.print(",");
                        out.print(ii);
                        out.print(",-1,");
                        facelets++;
                        out.print(incid[2][i]);
                        out.print(",");
                        out.print(incid[3][i]);
                        out.print(",");
                        out.print(ii++);
                        out.print(",-1,");
                        facelets++;
                    }
                } else if (hemi &&
                        (index != (UniformPolyhedrons.list.length - 1))) {
                    int j = (ftype[incid[0][i]] == 0) ? 1 : 0;
                    out.print(ii);
                    out.print(",");
                    out.print(ii + 1);
                    out.print(",");
                    out.print(ii + 2);
                    out.print(",-1,");
                    facelets++;
                    out.print(incid[j][i]);
                    out.print(",");
                    out.print(ii + 2);
                    out.print(",");
                    out.print(incid[j + 2][i]);
                    out.print(",");
                    out.print(ii + 5);
                    out.print(",-1,");
                    facelets++;
                    out.print(ii + 3);
                    out.print(",");
                    out.print(ii + 4);
                    out.print(",");
                    out.print(ii + 5);
                    out.print(",-1,");
                    facelets++;
                    ii += 6;
                } else if (index == (UniformPolyhedrons.list.length - 1)) {
                    int j;

                    for (j = 0; j < 8; j++)
                        if (ftype[incid[j][i]] == 3) {
                            break;
                        }

                    out.print(ii);
                    out.print(",");
                    out.print(ii + 1);
                    out.print(",");
                    out.print(ii + 2);
                    out.print(",-1,");
                    facelets++;
                    out.print(incid[j][i]);
                    out.print(",");
                    out.print(ii + 2);
                    out.print(",");
                    out.print(incid[(j + 2) % 8][i]);
                    out.print(",");
                    out.print(ii + 5);
                    out.print(",-1,");
                    facelets++;
                    out.print(ii + 3);
                    out.print(",");
                    out.print(ii + 4);
                    out.print(",");
                    out.print(ii + 5);
                    out.print(",-1,");
                    facelets++;
                    out.print(ii + 6);
                    out.print(",");
                    out.print(ii + 7);
                    out.print(",");
                    out.print(ii + 8);
                    out.print(",-1,");
                    facelets++;
                    out.print(incid[(j + 4) % 8][i]);
                    out.print(",");
                    out.print(ii + 8);
                    out.print(",");
                    out.print(incid[(j + 6) % 8][i]);
                    out.print(",");
                    out.print(ii + 11);
                    out.print(",-1,");
                    facelets++;
                    out.print(ii + 9);
                    out.print(",");
                    out.print(ii + 10);
                    out.print(",");
                    out.print(ii + 11);
                    out.print(",-1,");
                    facelets++;
                    ii += 12;
                } else {
                    for (int j = 0; j < M; j++) {
                        out.print(incid[j][i]);
                        out.print(",");
                    }

                    out.print("-1,");
                    facelets++;
                }
            } else {
                boolean split = isStar(new Rational(n[ftype[i]]));
                int j;
                int k = 0;

                for (j = 0; j < V; j++) {
                    for (k = 0; k < M; k++)
                        if (incid[k][j] == i) {
                            break;
                        }

                    if (k != M) {
                        break;
                    }
                }

                if (!split) {
                    out.print(j);
                    out.print(",");
                }

                int ll = j;

                for (int l = adj[k][j]; l != j; l = adj[k][l]) {
                    for (k = 0; k < M; k++)
                        if (incid[k][l] == i) {
                            break;
                        }

                    if (adj[k][l] == ll) {
                        k = mod(k + 1, M);
                    }

                    if (!split) {
                        out.print(l);
                        out.print(",");
                    } else {
                        out.print(ll);
                        out.print(",");
                        out.print(l);
                        out.print(",");
                        out.print(ii);
                        out.print(",-1,");
                        facelets++;
                    }

                    ll = l;
                }

                if (!split) {
                    out.print("-1,");
                    facelets++;
                } else {
                    out.print(ll);
                    out.print(",");
                    out.print(j);
                    out.print(",");
                    out.print(ii++);
                    out.print(",-1,");
                    facelets++;
                }
            }

            out.println();
        }

        out.println("\t\t]");

        /*
         * Face color indices.
         * For non-simple faces, the index is repeated as many times as
         * needed by the triangulation.
         */
        out.print("\t\tcolorIndex[");

        if (uniform && (N != 1)) {
            for (int i = 0; i < F; i++) {
                if (!isStar(new Rational(n[ftype[i]]))) {
                    out.print(ftype[i]);
                    out.print(",");
                } else {
                    for (int j = 0;
                         j < new Rational(n[ftype[i]]).getNumerator();
                         j++) {
                        out.print(ftype[i]);
                        out.print(",");
                    }
                }
            }
        } else {
            for (int i = 0; i < facelets; i++) {
                out.print("0,");
            }
        }

        out.println("]");
        out.println("\t}");
        out.println("}");
        out.close();
        System.err.println(name + ": written on " + fn);
    }

    /**
     * Prints the vector in a VRML format.
     *
     * @param v      DOCUMENT ME!
     * @param digits DOCUMENT ME!
     * @param out    DOCUMENT ME!
     */
    private void write(Vector3D v, int digits, PrintStream out) {
        out.print("\t\t\t\t");
        print(v.x(), digits + 3, digits, out);
        out.print(' ');
        print(v.y(), digits + 3, digits, out);
        out.print(' ');
        print(v.z(), digits + 3, digits, out);
        out.println(',');
    }

    /**
     * DOCUMENT ME!
     *
     * @param num DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private boolean isStar(Rational num) {
        return (num.getDenominator() != 1) &&
                (num.getDenominator() != (num.getNumerator() - 1));
    }

    /**
     * Chooses a color for a given face valency.
     * <p/>
     * <ul>
     * <li>
     * 3-,4- and 5-sided polygons have the simple colors R, G, and B.
     * </li>
     * <li>
     * 6-,8- and 10-sided polygons, which also occur in the standard polyhedra,
     * have the blended colors RG (yellow), RB (magenta) and GB (cyan).
     * </li>
     * <li>
     * All othe polygons (which occur in kaleido only if a Whythoff formula is
     * entered) are colored pink (its RGB value is taken from X11’s
     * rgb.txt).
     * </li>
     * </ul>
     *
     * @param n DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private static String rgb(int n) {
        switch (n) {
            case 3:
                return "1 0 0,";

            case 4:
                return "0 1 0,";

            case 5:
                return "0 0 1,";

            case 6:
                return "1 1 0,";

            case 8:
                return "1 0 1,";

            case 10:
                return "0 1 1,";

            default:
                return "1 .752 .796,";
        }
    }
}
