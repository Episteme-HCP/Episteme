package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
final class Const {
    /** DOCUMENT ME! */
    protected static final double PI = Math.PI; // PI = 3.14159265358979323846

    /** DOCUMENT ME! */
    protected static final double TWO_PI = 2.0 * PI;

    /** DOCUMENT ME! */
    protected static final double PI_DIV_4 = PI / 4.0; // 0.785398163397448309615

    /** DOCUMENT ME! */
    protected static final double PI_DIV_2 = PI / 2.0; // 1.570796326794896619230

    /** DOCUMENT ME! */
    protected static final double ONE_DIV_ROOT_2 = 0.70710678118654752440;

    /** DOCUMENT ME! */
    protected static final double RADIANS_PER_DEGREE = Math.PI / 180.0;

    /** DOCUMENT ME! */
    protected static final double DEGREES_PER_RADIAN = 180.0 / Math.PI;

    /** DOCUMENT ME! */
    protected static final double EPSILON = 0.000001;

    /** DOCUMENT ME! */
    protected static final double EPSILON_SQ = 0.000000000001;

    /** DOCUMENT ME! */
    protected static final double TM_EXTENDED_LONGITUDE_RANGE = (3.5 * Const.RADIANS_PER_DEGREE);

    /** DOCUMENT ME! */
    protected static final double TM_LONGITUDE_RANGE = (3.0 * Const.RADIANS_PER_DEGREE);

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double square(double var) {
        return var * var;
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double cube(double var) {
        return var * var * var;
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double twoTimes(double var) {
        return var + var;
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double triple(double var) {
        return var + var + var;
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final int roundDown(double var) {
        if (Math.round(var) < var) {
            return (int) Math.round(var);
        } else {
            return (int) (Math.round(var) - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     * @param xlat DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param eps2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double gee(double h, double xlat, double a,
        double eps2) {
        double rn;
        double result;
        double s2 = Math.sin(xlat);
        s2 = s2 * s2;
        rn = a / Math.sqrt(1.0 - (eps2 * s2));
        result = (rn + h) / (((1.0 - eps2) * rn) + h);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     * @param e_constants DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double computeRnFast(double arg, OrmData e_constants) {
        double alpha = 1.004244;
        double d1 = -0.5 * e_constants.Eps2;
        double x = d1 * arg;
        double ak = 0.5 + x;
        double z = 1.0 - (alpha * x);

        return (e_constants.A * z * (1.5 - (ak * z * z)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     * @param e_constants DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double computeRn(double arg, OrmData e_constants) {
        double z0;
        double temp;

        temp = e_constants.Eps25 * arg;
        z0 = 0.4999986087 - temp;

        return (e_constants.A * z0) / ((square(z0) + 0.25) - temp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     * @param e_constants DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final double computeRnExact(double arg, OrmData e_constants) {
        return e_constants.A / Math.sqrt(1 - (e_constants.Eps2 * arg));
    }

    /**
     * DOCUMENT ME!
     *
     * @param longitude DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final boolean isWellFormedLongitude(double longitude) {
        return ((longitude >= (-PI - EPSILON)) &&
        (longitude <= (PI + EPSILON)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param latitude DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final boolean isWellFormedLatitude(double latitude) {
        return ((latitude >= (-PI_DIV_2 - EPSILON)) &&
        (latitude <= (PI_DIV_2 + EPSILON)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final boolean isWellFormedRadius(double radius) {
        return (radius >= EPSILON);
    }

    /**
     * DOCUMENT ME!
     *
     * @param azimuth DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static final boolean isWellFormedAzimuth(double azimuth) {
        return ((azimuth >= (-EPSILON)) && (azimuth <= (TWO_PI + EPSILON)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static boolean isEqual(double a, double b) {
        return (Math.abs(a - b) <= EPSILON);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lambda1 DOCUMENT ME!
     * @param lambda2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static double delta_lambda_min(double lambda1, double lambda2) {
        double simple_difference;

        if (lambda2 >= lambda1) {
            simple_difference = lambda2 - lambda1;

            if (Math.abs(simple_difference) >= Const.PI) {
                return Math.IEEEremainder((Const.TWO_PI - lambda2) + lambda1,
                    Const.TWO_PI);
            } else {
                return simple_difference;
            }
        } else {
            simple_difference = lambda1 - lambda2;

            if (Math.abs(simple_difference) >= Math.PI) {
                return Math.IEEEremainder((Const.TWO_PI - lambda1) + lambda2,
                    Const.TWO_PI);
            } else {
                return simple_difference;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param longitude DOCUMENT ME!
     * @param origin_longitude DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static double getLambdaStar(double longitude, double origin_longitude) {
        double lambda_star = longitude - origin_longitude;

        if (lambda_star <= -PI) {
            lambda_star += TWO_PI;
        } else if (lambda_star > PI) {
            lambda_star -= TWO_PI;
        }

        return lambda_star;
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected static void copyArray(double[] src, double[] dest) {
        dest[0] = src[0];
        dest[1] = src[1];
        dest[2] = src[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @param dest DOCUMENT ME!
     * @param factor DOCUMENT ME!
     */
    protected static void ConstTimesVect(double[] dest, int factor) {
        dest[0] *= factor;
        dest[1] *= factor;
        dest[2] *= factor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     * @param v3 DOCUMENT ME!
     */
    protected static void vectCrossProd(double[] v1, double[] v2, double[] v3) {
        v3[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
        v3[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
        v3[2] = (v1[0] * v2[1]) - (v1[1] * v2[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param A DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param rank DOCUMENT ME!
     */
    protected static void multMatrixSubsetVector(double[][] A, double[] x,
        double[] b, int rank) {
        for (int i = 0; i < rank; i++) {
            b[i] = 0.0;

            for (int j = 0; j < rank; j++) {
                b[i] += (A[i][j] * x[j]);
            }
        }
    }

    /*This function places in t a matrix for use with homogenous coordinates
     *that will perform the rotation and translation components of the
     *WGS 84 Conversion.  Scale is currently not used
     *
     *  Source is PDB: LTPxfer01.doc 11/18/2002
     */
    protected static void WGS84_Transformation_Matrix(double[][] T,
        double delta_x, double delta_y, double delta_z, double omega_x, /* omega_1 rot around x_axis*/
        double omega_y, /* omega_2 rot around y_axis*/
        double omega_z, /* omega_3 rot around z_axis*/
        double delta_scale) {
        double cox = Math.cos(-omega_x);
        double sox = Math.sin(-omega_x);

        double coy = Math.cos(-omega_y);
        double soy = Math.sin(-omega_y);

        double coz = Math.cos(-omega_z);
        double soz = Math.sin(-omega_z);

        double[][] Tsr1 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[][] Tsr2 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[][] Tsr3 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[][] result1 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };

        /*Initialize the upper left 3x3 to what we need it to be*/
        /*The two instances
          of soz seem to have had their signs swapped in this matrix.
          No fix has been applied*/
        Tsr3[0][0] = coz;
        Tsr3[0][1] = soz;
        Tsr3[0][2] = 0.0;

        Tsr3[1][0] = -soz;
        Tsr3[1][1] = coz;
        Tsr3[1][2] = 0.0;

        Tsr3[2][0] = 0.0;
        Tsr3[2][1] = 0.0;
        Tsr3[2][2] = 1.0;

        /* end m1 *************/
        /* m2 *****************/
        Tsr2[0][0] = coy;
        Tsr2[0][1] = 0.0;
        Tsr2[0][2] = -soy;

        Tsr2[1][0] = 0.0;
        Tsr2[1][1] = 1.0;
        Tsr2[1][2] = 0.0;

        Tsr2[2][0] = soy;
        Tsr2[2][1] = 0.0;
        Tsr2[2][2] = coy;

        /* end m2 *************/
        /* m3******************/
        Tsr1[0][0] = 1.0;
        Tsr1[0][1] = 0.0;
        Tsr1[0][2] = 0.0;

        Tsr1[1][0] = 0.0;
        Tsr1[1][1] = cox;
        Tsr1[1][2] = sox;

        Tsr1[2][0] = 0.0;
        Tsr1[2][1] = -sox;
        Tsr1[2][2] = cox;

        /* end m3*/
        matrixMultiply4x4(Tsr2, Tsr1, result1);
        matrixMultiply4x4(Tsr3, result1, T);

        T[0][3] = delta_x;
        T[1][3] = delta_y;
        T[2][3] = delta_z;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                T[i][j] *= (1.0 + (delta_scale * 1.0e-6));
    }

    /**
     * DOCUMENT ME!
     *
     * @param T DOCUMENT ME!
     * @param delta_x DOCUMENT ME!
     * @param delta_y DOCUMENT ME!
     * @param delta_z DOCUMENT ME!
     * @param omega_x DOCUMENT ME!
     * @param omega_y DOCUMENT ME!
     * @param omega_z DOCUMENT ME!
     * @param delta_scale DOCUMENT ME!
     */
    protected static void WGS84_InverseTransformation_Matrix(double[][] T,
        double delta_x, double delta_y, double delta_z, double omega_x, /* omega_1 rot around x_axis*/
        double omega_y, /* omega_2 rot around y_axis*/
        double omega_z, /* omega_3 rot around z_axis*/
        double delta_scale) {
        double cox = Math.cos(-omega_x);
        double sox = Math.sin(-omega_x);

        double coy = Math.cos(-omega_y);
        double soy = Math.sin(-omega_y);

        double coz = Math.cos(-omega_z);
        double soz = Math.sin(-omega_z);

        double[] delta_TR = { 0.0, 0.0, 0.0, 1.0 };
        double[] delta_RT = new double[4];

        double[][] Tsr1 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[][] Tsr2 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[][] Tsr3 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[][] result1 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[][] result2 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };

        /*Initialize the upper left 3x3 to what we need it to be*/
        /* m1******************/
        /*The two instances
          of soz seem to have had their signs swapped in this matrix.
          No fix has been applied*/
        Tsr3[0][0] = coz;
        Tsr3[0][1] = soz;
        Tsr3[0][2] = 0.0;

        Tsr3[1][0] = -soz;
        Tsr3[1][1] = coz;
        Tsr3[1][2] = 0.0;

        Tsr3[2][0] = 0.0;
        Tsr3[2][1] = 0.0;
        Tsr3[2][2] = 1.0;

        /* end m1 *************/
        /* m2 *****************/
        Tsr2[0][0] = coy;
        Tsr2[0][1] = 0.0;
        Tsr2[0][2] = -soy;

        Tsr2[1][0] = 0.0;
        Tsr2[1][1] = 1.0;
        Tsr2[1][2] = 0.0;

        Tsr2[2][0] = soy;
        Tsr2[2][1] = 0.0;
        Tsr2[2][2] = coy;

        /* end m2 *************/
        /* m3******************/
        Tsr1[0][0] = 1.0;
        Tsr1[0][1] = 0.0;
        Tsr1[0][2] = 0.0;

        Tsr1[1][0] = 0.0;
        Tsr1[1][1] = cox;
        Tsr1[1][2] = sox;

        Tsr1[2][0] = 0.0;
        Tsr1[2][1] = -sox;
        Tsr1[2][2] = cox;

        /* end m3*/
        matrixMultiply4x4(Tsr2, Tsr1, result1);
        matrixMultiply4x4(Tsr3, result1, result2);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                result2[i][j] *= (1.0 / (1.0 + (delta_scale * 1.0e-6)));

        transpose(result2, T, 4);

        delta_TR[0] = -delta_x;
        delta_TR[1] = -delta_y;
        delta_TR[2] = -delta_z;

        multMatrixSubsetVector(T, delta_TR, delta_RT, 3);

        T[0][3] = delta_RT[0];
        T[1][3] = delta_RT[1];
        T[2][3] = delta_RT[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void normalizeDirection(double[] v)
        throws SrmException {
        double magnitude = Math.sqrt((v[0] * v[0]) + (v[1] * v[1]) +
                (v[2] * v[2]));

        if ((magnitude * magnitude) > 1e-3) {
            double scale;
            scale = 1.0 / magnitude;

            v[0] *= scale;
            v[1] *= scale;
            v[2] *= scale;
        } else {
            throw new SrmException(SrmException._INACTIONABLE,
                new String("Normalization of zero magnetude vector"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lon DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static double fix_longitude(double lon) {
        if (lon > Const.PI) {
            return lon - Const.TWO_PI;
        } else if (lon <= -Const.PI) {
            return lon + Const.TWO_PI;
        } else {
            return lon;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param mtx3x3 DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected static void applyMatrix3x3(double[] src, double[][] mtx3x3,
        double[] dest) {
        // pre-multiple vector by matrix dest = src * mtx3x3
        for (int i = 0; i < 3; i++) {
            dest[i] = 0.0;

            for (int j = 0; j < 3; j++) {
                dest[i] += (src[j] * mtx3x3[i][j]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param mtx4x4 DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected static void applyMatrix4x4(double[] src, double[][] mtx4x4,
        double[] dest) {
        // pre-multiple vector by matrix dest = src * mtx4x4
        for (int i = 0; i < 4; i++) {
            dest[i] = 0.0;

            for (int j = 0; j < 4; j++) {
                dest[i] += (src[j] * mtx4x4[j][i]);
            }
        }
    }

    // 4x4 matrix multiplication c=a*b
    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    protected static void matrixMultiply4x4(double[][] a, double[][] b,
        double[][] c) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                c[i][j] = 0.0;

                for (int n = 0; n < 4; n++) {
                    c[i][j] += (a[i][n] * b[n][j]);
                }
            }
        }
    }

    // 3x3 matrix multiplication c=a*b
    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    protected static void matrixMultiply3x3(double[][] a, double[][] b,
        double[][] c) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                c[i][j] = 0.0;

                for (int n = 0; n < 3; n++) {
                    c[i][j] += (a[i][n] * b[n][j]);
                }
            }
        }
    }

    // this matrix invert method only applies to the cases
    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param invSrc DOCUMENT ME!
     */
    protected static void invert(double[][] src, double[][] invSrc) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                invSrc[i][j] = src[j][i];
            }
        }

        invSrc[3][0] = -src[3][0];
        invSrc[3][1] = -src[3][1];
        invSrc[3][2] = -src[3][2];

        invSrc[3][3] = 1.0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param delta_scale DOCUMENT ME!
     * @param invSrc DOCUMENT ME!
     */
    protected static void invert_datum(double[][] src, double delta_scale,
        double[][] invSrc) {
        double[] tmp = new double[3];
        double[] deltas = { -src[3][0], -src[3][1], -src[3][2] };

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                invSrc[i][j] = src[j][i];

        invSrc[0][3] = 0.0;
        invSrc[1][3] = 0.0;
        invSrc[2][3] = 0.0;

        applyMatrix3x3(deltas, invSrc, tmp);

        invSrc[3][0] = tmp[0];
        invSrc[3][1] = tmp[1];
        invSrc[3][2] = tmp[2];

        double factor = 1.0 / (1.0 + delta_scale);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                invSrc[i][j] *= factor;

        invSrc[3][3] = 1.0;
    }

    // transpose
    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param transposedSrc DOCUMENT ME!
     * @param rank DOCUMENT ME!
     */
    protected static void transpose(double[][] src, double[][] transposedSrc,
        int rank) {
        for (int i = 0; i < rank; i++) {
            for (int j = 0; j < rank; j++) {
                transposedSrc[i][j] = src[j][i];
            }
        }
    }

    // compute the determinantn of matrix of rank n
    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param rank DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static double det(double[][] m, int rank) {
        Matrix mtx = new Matrix(m);

        return mtx.det();
    }

    // cpMatrix
    /**
     * DOCUMENT ME!
     *
     * @param m_cp DOCUMENT ME!
     * @param m DOCUMENT ME!
     * @param rank DOCUMENT ME!
     */
    protected static void cpMatrix(double[][] m_cp, double[][] m, int rank) {
        for (int i = 0; i < rank; i++)
            for (int j = 0; j < rank; j++)
                m_cp[i][j] = m[i][j];
    }

    /**
     * DOCUMENT ME!
     *
     * @param vec DOCUMENT ME!
     */
    protected static void print(double[] vec) {
        System.out.println("[ " + vec[0] + ", " + vec[1] + ", " + vec[2] +
            " ]");
    }

    /**
     * DOCUMENT ME!
     *
     * @param lambda DOCUMENT ME!
     * @param phi DOCUMENT ME!
     * @param alpha DOCUMENT ME!
     * @param T DOCUMENT ME!
     */
    protected static void LTP_Rotation_Matrix_T(double lambda, double phi,
        double alpha, double[][] T) {
        double cl = Math.cos(lambda);
        double sl = Math.sin(lambda);

        double cp = Math.cos(phi);
        double sp = Math.sin(phi);

        double ca = Math.cos(alpha);
        double sa = Math.sin(alpha);

        T[0][0] = (-sl * ca) + (cl * sp * sa);
        T[0][1] = (-sl * sa) + (-cl * sp * ca);
        T[0][2] = cl * cp;
        T[0][3] = 0;

        T[1][0] = (cl * ca) + (sl * sp * sa);
        T[1][1] = (cl * sa) + (-sl * sp * ca);
        T[1][2] = sl * cp;
        T[1][3] = 0;

        T[2][0] = cp * -sa;
        T[2][1] = cp * ca;
        T[2][2] = sp;
        T[2][3] = 0;

        T[3][0] = 0;
        T[3][1] = 0;
        T[3][2] = 0;
        T[3][3] = 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e_constants DOCUMENT ME!
     * @param params DOCUMENT ME!
     * @param T DOCUMENT ME!
     * @param T_inv DOCUMENT ME!
     */
    protected static void calc_T(OrmData e_constants, SRF_LTSE_Params params,
        double[][] T, double[][] T_inv) {
        double azimuth;
        double Origin_Height;
        double radius;
        double rn;
        double height_radius_factor;
        double[][] R0 = {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
            };
        double[] R = new double[4];
        double[] R2 = new double[4];

        double slat;
        double clat;
        double slon;
        double clon;

        slat = Math.sin(params.geodetic_latitude);
        clat = Math.cos(params.geodetic_latitude);
        slon = Math.sin(params.geodetic_longitude);
        clon = Math.cos(params.geodetic_longitude);
        azimuth = params.azimuth;
        Origin_Height = params.height_offset;

        if (e_constants.Eps != 0.0) /* ellipsoid */ {
            rn = e_constants.A / Math.sqrt(1 -
                    (e_constants.Eps2 * (slat * slat)));

            radius = rn + Origin_Height;

            height_radius_factor = Origin_Height +
                (rn * (1.0 - e_constants.Eps2));
        } else /* spheroid */
         {
            radius = Origin_Height + e_constants.A;
            height_radius_factor = radius;
        }

        R[0] = (radius * clat * clon);
        R[1] = (radius * clat * slon);
        R[2] = (height_radius_factor * slat);
        R[3] = 1.0;

        LTP_Rotation_Matrix_T(params.geodetic_longitude,
            params.geodetic_latitude, azimuth, T);

        T[0][3] = R[0];
        T[1][3] = R[1];
        T[2][3] = R[2];
        T[3][3] = 1.0;

        transpose(T, T_inv, 4);

        R[0] *= -1;
        R[1] *= -1;
        R[2] *= -1;

        multMatrixSubsetVector(T_inv, R, R2, 3);

        T_inv[3][0] = 0.0;
        T_inv[3][1] = 0.0;
        T_inv[3][2] = 0.0;

        T_inv[0][3] = R2[0];
        T_inv[1][3] = R2[1];
        T_inv[2][3] = R2[2];
        T_inv[3][3] = 1.0;

        // 	System.out.println("R and F matrices");
        // 	Const.printMatrix( R, 4 );
        // 	Const.printMatrix( R_inv, 4 );
        // 	Const.printMatrix( F, 4 );
        // 	Const.printMatrix( F_inv, 4 );
    }

    /**
     * DOCUMENT ME!
     *
     * @param mtx DOCUMENT ME!
     * @param rank DOCUMENT ME!
     */
    public static void printMatrix(double[][] mtx, int rank) {
        for (int i = 0; i < rank; i++) {
            for (int j = 0; j < rank; j++)
                System.out.print(mtx[i][j] + ", ");

            System.out.print("\n");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        double[][] test_data = {
                { 16, 16, 19, 21, 20 },
                { 14, 17, 15, 22, 18 },
                { 24, 23, 21, 24, 20 },
                { 18, 17, 16, 15, 20 },
                { 18, 11, 9, 18, 7 }
            };

        Matrix mtx = new Matrix(test_data);

        // 	double[][] test_data = new double [][]
        // 	{{0, 0, 0},
        // 	 {0, 1, 1},
        // 	 {0, 2, 3}};
        System.out.println("determinant=> " + mtx.det());
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class GcToGdConst {
        /** DOCUMENT ME! */
        double Eps21;

        /** DOCUMENT ME! */
        double C254;

        /** DOCUMENT ME! */
        double CEEps2;

        /** DOCUMENT ME! */
        double CEE;

        /** DOCUMENT ME! */
        double TwoCEE;

        /** DOCUMENT ME! */
        double C2DA;

        /** DOCUMENT ME! */
        double tem;

        /** DOCUMENT ME! */
        double ARat1;

        /** DOCUMENT ME! */
        double ARat2;

        /** DOCUMENT ME! */
        double BRat1;

        /** DOCUMENT ME! */
        double BRat2;

        /** DOCUMENT ME! */
        double aeps21;

        /** DOCUMENT ME! */
        double ak1;

        /** DOCUMENT ME! */
        double ak2;

        /** DOCUMENT ME! */
        double rho;

        /** DOCUMENT ME! */
        double rho_inv;

        /** DOCUMENT ME! */
        double[] b1 = new double[5];

        /** DOCUMENT ME! */
        double[] b2 = new double[5];

        /** DOCUMENT ME! */
        double[] b3 = new double[5];

        /** DOCUMENT ME! */
        double[] b4 = new double[5];

        /** DOCUMENT ME! */
        double[] b5 = new double[5];

        /** DOCUMENT ME! */
        double[] u = new double[5];

        /** DOCUMENT ME! */
        double[] v = new double[5];
    }
}
