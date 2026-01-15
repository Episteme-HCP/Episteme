package org.jscience.astronomy.solarsystem.ephemeris.ELP2000;


//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//the author agreed we reuse his code under GPL
import org.jscience.astronomy.solarsystem.ephemeris.DataFormatException;
import org.jscience.astronomy.solarsystem.ephemeris.Matrix3D;
import org.jscience.astronomy.solarsystem.ephemeris.Vector3;

import java.io.DataInputStream;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ELP2000 {
    /**
     * DOCUMENT ME!
     */
    static final double radians = 4.8481368110953598E-006D;

    /**
     * DOCUMENT ME!
     */
    static final double ath = 384747.98067431652D;

    /**
     * DOCUMENT ME!
     */
    static final double a0 = 384747.9806448954D;

    /**
     * DOCUMENT ME!
     */
    static final double am = 0.074801329518000004D;

    /**
     * DOCUMENT ME!
     */
    static final double alfa = 0.0025718813350000002D;

    /**
     * DOCUMENT ME!
     */
    static final double dtasm = 0.022921886117733679D;

    /**
     * DOCUMENT ME!
     */
    static final double[] W1_ = {
            3.8103444305883079D, 8399.6847317739157D, -2.8547283984772807E-005D,
            3.2017095500473753E-008D, -1.5363745554361197E-010D
        };

    /**
     * DOCUMENT ME!
     */
    static final double[] W2_ = {
            1.4547885323225089D, 70.993304818359618D, -0.00018557504160038375D,
            -2.1839401892941265E-007D, 1.0327016221314225E-009D
        };

    /**
     * DOCUMENT ME!
     */
    static final double[] W3_ = {
            2.1824391972168398D, -33.781426356625921D, 3.08448160195509E-005D,
            3.6967043184602116E-008D, -1.738541860458796E-010D
        };

    /**
     * DOCUMENT ME!
     */
    static final double[] T_ = {
            1.7534703431506578D, 628.30758496215537D, -9.7932363584126268E-008D,
            4.3633231299858238E-011D, 7.2722052166430391E-013D
        };

    /**
     * DOCUMENT ME!
     */
    static final double[] omega_bar_ = {
            1.7965955233587827D, 0.0056297936673156855D,
            2.5826024792704981E-006D, -6.6904287993115966E-010D, 0.0D
        };

    /**
     * DOCUMENT ME!
     */
    static final double precess = 0.024381748353014515D;

    /**
     * DOCUMENT ME!
     */
    static final double delnu = 2.6957579924414636E-006D / W1_[1];

    /**
     * DOCUMENT ME!
     */
    static final double dele = 8.6733167550495987E-008D;

    /**
     * DOCUMENT ME!
     */
    static final double delg = -3.910507151829517E-007D;

    /**
     * DOCUMENT ME!
     */
    static final double delnp = -3.1144430874476595E-007D / W1_[1];

    /**
     * DOCUMENT ME!
     */
    static final double delep = -6.2439153990097128E-007D;

    /**
     * DOCUMENT ME!
     */
    double[] P_ = {
            0.0D, 1.0180391E-005D, 4.7020439E-007D, -5.4173670000000002E-010D,
            -2.5079480000000001E-012D, 4.6348599999999997E-015D
        };

    /**
     * DOCUMENT ME!
     */
    double[] Q_ = {
            0.0D, -0.000113469002D, 1.2372673999999999E-007D, 1.265417E-009D,
            -1.371808E-012D, -3.2033400000000001E-015D
        };

    /**
     * DOCUMENT ME!
     */
    double[] tval;

    /**
     * DOCUMENT ME!
     */
    double[][][] ivals;

    /**
     * DOCUMENT ME!
     */
    double[][][] cvals;

    /**
     * DOCUMENT ME!
     */
    double[][] vect;

    /**
     * DOCUMENT ME!
     */
    boolean doubleprecision;

    /**
     * DOCUMENT ME!
     */
    private Matrix3D FK5rot;

    /**
     * Creates a new ELP2000 object.
     *
     * @param fin DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public ELP2000(DataInputStream fin) throws IOException {
        tval = new double[36];
        ivals = new double[36][][];
        cvals = new double[36][][];
        vect = new double[36][];
        readTerms(fin);
        initFK5();
    }

    /**
     * DOCUMENT ME!
     *
     * @param fin DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private double readFloat(DataInputStream fin) throws IOException {
        if (doubleprecision) {
            return fin.readDouble();
        } else {
            return (double) fin.readFloat();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param fin DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataFormatException DOCUMENT ME!
     */
    public void readTerms(DataInputStream fin)
        throws IOException, DataFormatException {
        int MAGIC = fin.readShort();

        if (MAGIC == 1) {
            doubleprecision = false;
        } else if (MAGIC == 2) {
            doubleprecision = true;
        } else {
            throw new DataFormatException("Bad Magic Reading ELP2000 data");
        }

        for (int i = 0; i < 3; i++) {
            int nrecords = fin.readShort();
            ivals[i] = new double[nrecords][];
            cvals[i] = new double[nrecords][];

            for (int j = 0; j < nrecords; j++) {
                double[] I = new double[4];
                double[] C = new double[7];

                for (int k = 0; k < 4; k++)
                    I[k] = fin.readByte();

                for (int k = 0; k < 7; k++)
                    C[k] = readFloat(fin);

                ivals[i][j] = I;
                cvals[i][j] = C;
            }
        }

        for (int i = 3; i < 9; i++) {
            int nrecords = fin.readShort();
            ivals[i] = new double[nrecords][];
            cvals[i] = new double[nrecords][];

            for (int j = 0; j < nrecords; j++) {
                double[] I = new double[6];
                double[] C = new double[2];

                for (int k = 0; k < 5; k++)
                    I[k] = fin.readByte();

                I[5] = (readFloat(fin) * 3.1415926535897931D) / 180D;

                for (int k = 0; k < 2; k++)
                    C[k] = readFloat(fin);

                ivals[i][j] = I;
                cvals[i][j] = C;
            }
        }

        for (int i = 9; i < 15; i++) {
            int nrecords = fin.readShort();
            ivals[i] = new double[nrecords][];
            cvals[i] = new double[nrecords][];

            for (int j = 0; j < nrecords; j++) {
                double[] I = new double[12];
                double[] C = new double[2];

                for (int k = 0; k < 11; k++)
                    I[k] = fin.readByte();

                I[11] = (readFloat(fin) * 3.1415926535897931D) / 180D;

                for (int k = 0; k < 2; k++)
                    C[k] = readFloat(fin);

                ivals[i][j] = I;
                cvals[i][j] = C;
            }
        }

        for (int i = 15; i < 21; i++) {
            int nrecords = fin.readShort();
            ivals[i] = new double[nrecords][];
            cvals[i] = new double[nrecords][];

            for (int j = 0; j < nrecords; j++) {
                double[] I = new double[12];
                double[] C = new double[2];

                for (int k = 0; k < 11; k++)
                    I[k] = fin.readByte();

                I[11] = (readFloat(fin) * 3.1415926535897931D) / 180D;

                for (int k = 0; k < 2; k++)
                    C[k] = readFloat(fin);

                ivals[i][j] = I;
                cvals[i][j] = C;
            }
        }

        for (int i = 21; i < 36; i++) {
            int nrecords = fin.readShort();
            ivals[i] = new double[nrecords][];
            cvals[i] = new double[nrecords][];

            for (int j = 0; j < nrecords; j++) {
                double[] I = new double[6];
                double[] C = new double[2];

                for (int k = 0; k < 5; k++)
                    I[k] = fin.readByte();

                I[5] = (readFloat(fin) * 3.1415926535897931D) / 180D;

                for (int k = 0; k < 2; k++)
                    C[k] = readFloat(fin);

                ivals[i][j] = I;
                cvals[i][j] = C;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param X DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double sum(double[] X, double t, int n) {
        double S = 0.0D;

        for (int i = n - 1; i >= 0; i--) {
            S *= t;
            S += X[i];
        }

        return S;
    }

    /**
     * DOCUMENT ME!
     *
     * @param JDE DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 getGeoRect_km(double JDE) {
        double t = (JDE - 2451545D) / 36525D;
        double W1 = sum(W1_, t, 5);
        double W2 = sum(W2_, t, 5);
        double W3 = sum(W3_, t, 5);
        double T = sum(T_, t, 5);
        double omega_bar = sum(omega_bar_, t, 5);
        double D = (W1 - T) + 3.1415926535897931D;
        double F = W1 - W3;
        double l = W1 - W2;
        double lprime = T - omega_bar;
        double zeta = W1_[0] + (t * (W1_[1] + 0.024381748353014515D));
        double Me = (908103.25985999999D + (t * 538101628.68897998D)) * 4.8481368110953598E-006D;
        double Ve = (655127.28304999997D + (t * 210664136.43355D)) * 4.8481368110953598E-006D;
        double Ma = (1279559.7886600001D + (t * 68905077.592840001D)) * 4.8481368110953598E-006D;
        double Ju = (123665.34212D + (t * 10925660.428610001D)) * 4.8481368110953598E-006D;
        double Sa = (180278.89694000001D + (t * 4399609.6593199996D)) * 4.8481368110953598E-006D;
        double Ur = (1130598.01841D + (t * 1542481.1939300001D)) * 4.8481368110953598E-006D;
        double Ne = (1095655.19575D + (t * 786550.32074D)) * 4.8481368110953598E-006D;
        double[] TEMP1 = { D, lprime, l, F };
        vect[0] = vect[1] = vect[2] = TEMP1;
        W1 = sum(W1_, t, 2);
        W2 = sum(W2_, t, 2);
        W3 = sum(W3_, t, 2);
        T = sum(T_, t, 2);
        omega_bar = sum(omega_bar_, t, 2);
        D = (W1 - T) + 3.1415926535897931D;
        F = W1 - W3;
        l = W1 - W2;
        lprime = T - omega_bar;

        double[] TEMP2 = { zeta, D, lprime, l, F, 1.0D };

        for (int i = 3; i < 9; i++)
            vect[i] = TEMP2;

        double[] TEMP3 = { Me, Ve, T, Ma, Ju, Sa, Ur, Ne, D, l, F, 1.0D };

        for (int i = 9; i < 15; i++)
            vect[i] = TEMP3;

        double[] TEMP4 = { Me, Ve, T, Ma, Ju, Sa, Ur, D, lprime, l, F, 1.0D };

        for (int i = 15; i < 21; i++)
            vect[i] = TEMP4;

        double[] TEMP5 = { zeta, D, lprime, l, F, 1.0D };

        for (int i = 21; i < 36; i++)
            vect[i] = TEMP5;

        for (int i = 0; i < 36; i++)
            tval[i] = 1.0D;

        tval[6] = tval[7] = tval[8] = tval[12] = tval[13] = tval[14] = tval[18] = tval[19] = tval[20] = tval[24] = tval[25] = tval[26] = t;
        tval[33] = tval[34] = tval[35] = t * t;

        double[] r = { 0.0D, 0.0D, 0.0D };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < cvals[i].length; j++) {
                double[] coef = cvals[i][j];
                double tgv = coef[1] + (0.022921886117733679D * coef[5]);
                double A1 = coef[0];

                if (i == 2) {
                    A1 -= ((2D * A1 * delnu) / 3D);
                }

                double A = A1 +
                    (tgv * (delnp - (0.074801329518000004D * delnu))) +
                    (coef[2] * -3.910507151829517E-007D) +
                    (coef[3] * 8.6733167550495987E-008D) +
                    (coef[4] * -6.2439153990097128E-007D);
                double y = dotprod(vect[i], ivals[i][j]);

                if (i == 2) {
                    y += 1.5707963267948966D;
                }

                r[i] += (A * Math.sin(y));
            }
        }

        for (int i = 3; i < 36; i++) {
            for (int j = 0; j < cvals[i].length; j++) {
                double A = cvals[i][j][0] * tval[i];
                double y = dotprod(vect[i], ivals[i][j]);
                r[i % 3] += (A * Math.sin(y));
            }
        }

        double lon = (r[0] * 4.8481368110953598E-006D) + sum(W1_, t, 5);
        double lat = r[1] * 4.8481368110953598E-006D;
        double dist = (r[2] * 384747.9806448954D) / 384747.98067431652D;
        Vector3 V = new Vector3(dist * Math.cos(lon) * Math.cos(lat),
                dist * Math.sin(lon) * Math.cos(lat), dist * Math.sin(lat));
        double P = sum(P_, t, 6);
        double Q = sum(Q_, t, 6);
        double root = Math.sqrt(1.0D - (P * P) - (Q * Q));
        double[][] TEMP6 = {
                { 1.0D - (2D * P * P), 2D * P * Q, 2D * P * root },
                { 2D * P * Q, 1.0D - (2D * Q * Q), -2D * Q * root },
                {
                    -2D * P * root, 2D * Q * root,
                    1.0D - (2D * ((P * P) + (Q * Q)))
                }
            };
        Matrix3D rotation = new Matrix3D(TEMP6);
        rotation.transform(V);

        return V;
    }

    /**
     * DOCUMENT ME!
     *
     * @param JDE DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 getGeoRect_au(double JDE) {
        Vector3 V = getGeoRect_km(JDE);
        V.scale(6.6845871535470389E-009D);

        return V;
    }

    /**
     * DOCUMENT ME!
     *
     * @param JDE DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 findFK5(double JDE) {
        Vector3 V = getGeoRect_au(JDE);
        toFK5(V);

        return V;
    }

    /**
     * DOCUMENT ME!
     *
     * @param A DOCUMENT ME!
     * @param B DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    private double dotprod(double[] A, double[] B) throws ArithmeticException {
        if (A.length != B.length) {
            throw new ArithmeticException(
                "Dot product on different size vectors.");
        }

        double S = 0.0D;

        for (int i = 0; i < B.length; i++)
            S += (A[i] * B[i]);

        return S;
    }

    /**
     * DOCUMENT ME!
     */
    private void initFK5() {
        double[][] x = {
                { 1.0D, 4.3791300000000001E-007D, -1.8985900000000001E-007D },
                {
                    -4.7729900000000004E-007D, 0.91748213760700004D,
                    -0.39777698170100001D
                },
                { 0.0D, 0.39777698170100001D, 0.91748213760700004D }
            };
        FK5rot = new Matrix3D(x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param X DOCUMENT ME!
     */
    public void toFK5(Vector3 X) {
        FK5rot.transform(X);
    }
}
