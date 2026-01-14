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

package org.jscience.astronomy.solarsystem.ephemeris.VSOP87;


//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//the author agreed we reuse his code under GPL
import org.jscience.astronomy.solarsystem.ephemeris.DataFormatException;
import org.jscience.astronomy.solarsystem.ephemeris.JulianDay;
import org.jscience.astronomy.solarsystem.ephemeris.Matrix3D;
import org.jscience.astronomy.solarsystem.ephemeris.Vector3;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;


// Referenced classes of packageorg.jscience.astronomy.solarsystem.ephemeris.VSOP87:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class VSOP87 {
    /** DOCUMENT ME! */
    private static final byte eof_flag = -1;

    /** DOCUMENT ME! */
    private VSOP87_term[][][] terms;

    /** DOCUMENT ME! */
    private Matrix3D FK5rot;

/**
     * Creates a new VSOP87 object.
     *
     * @param datainputstream DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public VSOP87(DataInputStream datainputstream) throws IOException {
        readTerms(datainputstream);
        initFK5();
    }

/**
     * Creates a new VSOP87 object.
     *
     * @param s DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public VSOP87(String s) throws IOException {
        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(
                    new FileInputStream(s)));
        readTerms(datainputstream);
        initFK5();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 findVector3(double d) {
        double[] ad = findCoords(d);
        Vector3 vector3 = new Vector3(ad[0], ad[1], ad[2]);

        return vector3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws IOException {
        double d = JulianDay.inDynamicalTime(JulianDay.now());
        System.out.println("VSOP87 test");

        VSOP87 vsop87 = new VSOP87("vsop87a.jup.bin");
        double[] ad = vsop87.findCoords(d);
        System.out.println("Coordinates for JDE" + d);
        System.out.println("X " + ad[0]);
        System.out.println("Y " + ad[1]);
        System.out.println("Z " + ad[2]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param datainputstream DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataFormatException DOCUMENT ME!
     */
    public void readTerms(DataInputStream datainputstream)
        throws IOException, DataFormatException {
        int i = 0;
        int j = 0;
        VSOP87_term[][] avsop87_term = new VSOP87_term[6][];
        terms = new VSOP87_term[10][][];

        do {
            byte byte0 = datainputstream.readByte();

            if ((byte0 < 1) && (byte0 != -1)) {
                throw new DataFormatException("Bad coordinate index.");
            }

            if (byte0 != (i + 1)) {
                VSOP87_term[][] avsop87_term2 = new VSOP87_term[j + 1][];
                System.arraycopy(avsop87_term, 0, avsop87_term2, 0, j + 1);
                terms[i] = avsop87_term2;

                if (byte0 == -1) {
                    break;
                }

                i = byte0 - 1;
            }

            j = datainputstream.readByte();

            int k = datainputstream.readInt();
            VSOP87_term[] avsop87_term3 = new VSOP87_term[k];

            for (int l = 0; l < k; l++) {
                VSOP87_term vsop87_term = new VSOP87_term();
                vsop87_term.A = datainputstream.readDouble();
                vsop87_term.B = datainputstream.readDouble();
                vsop87_term.C = datainputstream.readDouble();
                avsop87_term3[l] = vsop87_term;
            }

            avsop87_term[j] = avsop87_term3;
        } while (true);

        VSOP87_term[][][] avsop87_term1 = terms;
        terms = new VSOP87_term[i + 1][][];
        System.arraycopy(avsop87_term1, 0, terms, 0, i + 1);
    }

    /**
     * DOCUMENT ME!
     */
    private void initFK5() {
        double[][] ad = new double[3][3];
        ad[0][0] = 1.0D;
        ad[0][1] = 4.4036000000000001E-007D;
        ad[0][2] = -1.9091899999999999E-007D;
        ad[1][0] = -4.7996599999999997E-007D;
        ad[1][1] = 0.91748213708699999D;
        ad[1][2] = -0.39777698290199998D;
        ad[2][0] = 0.0D;
        ad[2][1] = 0.39777698290199998D;
        ad[2][2] = 0.91748213708699999D;
        FK5rot = new Matrix3D(ad);
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     */
    public void toFK5(Vector3 vector3) {
        FK5rot.transform(vector3);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 findFK5(double d) {
        Vector3 vector3 = findVector3(d);
        toFK5(vector3);

        return vector3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] findCoords(double d) {
        double[] ad = new double[terms.length];
        double d1 = (d - 2451545D) / 365250D;

        for (int i = 0; i < terms.length; i++) {
            double d2 = 0.0D;
            double d3 = 1.0D;
            int j = terms[i].length;

            for (int k = 0; k < j; k++) {
                double d4 = 0.0D;
                int l = terms[i][k].length;

                for (int i1 = 0; i1 < l; i1++) {
                    VSOP87_term vsop87_term = terms[i][k][i1];
                    d4 += (vsop87_term.A * Math.cos(vsop87_term.B +
                        (vsop87_term.C * d1)));
                }

                d2 += (d3 * d4);
                d3 *= d1;
            }

            ad[i] = d2;
        }

        return ad;
    }
}
