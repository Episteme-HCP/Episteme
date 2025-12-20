/*
 * Integrals.java
 *
 * Created on July 28, 2004, 7:03 AM
 */

package org.jscience.chemistry.quantum.integral;

import org.jscience.chemistry.quantum.basis.ContractedGaussian;
import org.jscience.chemistry.quantum.basis.Power;
import org.jscience.chemistry.quantum.math.util.MathUtils;
import org.jscience.chemistry.quantum.math.util.Point3D;

import java.util.ArrayList;

/**
 * The equations herein are based upon: <br>
 * 'Gaussian Expansion Methods for Molecular Orbitals.' H. Taketa,
 * S. Huzinaga, and K. O-ohata. <i> H. Phys. Soc. Japan, </i> <b>21</b>,
 * 2313, 1966. [THO paper]. <br>
 * and PyQuante (<a href="http://pyquante.sf.net"> http://pyquante.sf.net </a>).
 *
 * @author V.Ganesh
 * @version 1.0
 */
public final class Integrals {

    /**
     * No instantiation possible
     */
    private Integrals() {
    }

    /**
     * From Augspurger and Dykstra
     */
    public static double binomialPrefactor(int s, int ia, int ib,
                                           double xpa, double xpb) {
        double sum = 0.0;

        for (int t = 0; t < (s + 1); t++) {
            if (((s - ia) <= t) && (t <= ib)) {
                sum += binomial(ia, s - t) * binomial(ib, t)
                        * Math.pow(xpa, ia - s + t) * Math.pow(xpb, ib - t);
            } // end if
        } // end for

        return sum;
    }

    /**
     * the binomial coefficient
     */
    public static double binomial(int i, int j) {
        return (MathUtils.factorial(i) / MathUtils.factorial(j)
                / MathUtils.factorial(i - j));
    }

    /**
     * Overlap matrix element taken form
     * <i> Taken from THO eq. 2.12 <i>
     */
    public static double overlap(double alpha1, Power power, Point3D a,
                                 double alpha2, Power power2, Point3D b) {
        double radiusABSquared = a.distanceSquaredFrom(b);
        double gamma = alpha1 + alpha2;
        Point3D product = gaussianProductCenter(alpha1, a, alpha2, b);

        double wx = overlap1D(power.getL(), power2.getL(),
                product.getX() - a.getX(),
                product.getX() - b.getX(), gamma);
        double wy = overlap1D(power.getM(), power2.getM(),
                product.getY() - a.getY(),
                product.getY() - b.getY(), gamma);
        double wz = overlap1D(power.getN(), power2.getN(),
                product.getZ() - a.getZ(),
                product.getZ() - b.getZ(), gamma);

        return (Math.pow(Math.PI / gamma, 1.5)
                * Math.exp((-alpha1 * alpha2 * radiusABSquared) / gamma)
                * wx * wy * wz);
    }

    /**
     * 1D overlap.
     * <p/>
     * <i> Taken from THO eq. 2.12 <i>
     */
    public static double overlap1D(int l1, int l2, double pax, double pbx,
                                   double gamma) {
        double sum = 0.0;
        int k = 1 + (int) Math.floor(0.5 * (l1 + l2));

        for (int i = 0; i < k; i++) {
            sum += (binomialPrefactor(2 * i, l1, l2, pax, pbx)
                    * MathUtils.factorial2(2 * i - 1)) / Math.pow(2D * gamma, i);
        } // end for

        return sum;
    }

    /**
     * the gaussian product theorem
     */
    public static Point3D gaussianProductCenter(double alpha1, Point3D a,
                                                double alpha2, Point3D b) {
        double gamma = alpha1 + alpha2;
        return new Point3D((alpha1 * a.getX() + alpha2 * b.getX()) / gamma,
                (alpha1 * a.getY() + alpha2 * b.getY()) / gamma,
                (alpha1 * a.getZ() + alpha2 * b.getZ()) / gamma);
    }

    /**
     * The Kinetic Energy (KE) componant
     * <p/>
     * <i> Taken from THO eq. 2.12 <i>
     */
    public static double kinetic(double alpha1, Power power1, Point3D a,
                                 double alpha2, Power power2, Point3D b) {
        int l1 = power1.getL();
        int m1 = power1.getM();
        int n1 = power1.getN();
        int l2 = power2.getL();
        int m2 = power2.getM();
        int n2 = power2.getN();

        double term = alpha2 * (2 * (l2 + m2 + n2) + 3)
                * overlap(alpha1, power1, a, alpha2, power2, b);

        term += -2.0 * Math.pow(alpha2, 2.0)
                * (overlap(alpha1, power1, a, alpha2,
                new Power(l2 + 2, m2, n2), b)
                + overlap(alpha1, power1, a, alpha2,
                new Power(l2, m2 + 2, n2), b)
                + overlap(alpha1, power1, a, alpha2,
                new Power(l2, m2, n2 + 2), b));

        term += -0.5 * ((l2 * (l2 - 1)) * overlap(alpha1, power1, a, alpha2,
                new Power(l2 - 2, m2, n2), b)
                + (m2 * (m2 - 1)) * overlap(alpha1, power1, a, alpha2,
                new Power(l2, m2 - 2, n2), b)
                + (n2 * (n2 - 1)) * overlap(alpha1, power1, a, alpha2,
                new Power(l2, m2, n2 - 2), b));

        return term;
    }

    /**
     * The nuclear attraction term.
     * <p/>
     * <i> Taken from THO eq. 2.12 <i>
     */
    public static double nuclearAttraction(Point3D a, double norm1,
                                           Power power1, double alpha1, Point3D b,
                                           double norm2, Power power2, double alpha2,
                                           Point3D c) {

        Point3D product = gaussianProductCenter(alpha1, a, alpha2, b);

        double radiusABSquared = a.distanceSquaredFrom(b);
        double radiusCPSquared = c.distanceSquaredFrom(product);

        double gamma = alpha1 + alpha2;

        double[] ax = constructAArray(power1.getL(), power2.getL(),
                product.getX() - a.getX(),
                product.getX() - b.getX(),
                product.getX() - c.getX(), gamma);

        double[] ay = constructAArray(power1.getM(), power2.getM(),
                product.getY() - a.getY(),
                product.getY() - b.getY(),
                product.getY() - c.getY(), gamma);

        double[] az = constructAArray(power1.getN(), power2.getN(),
                product.getZ() - a.getZ(),
                product.getZ() - b.getZ(),
                product.getZ() - c.getZ(), gamma);

        double sum = 0.0;
        int i, j, k;
        for (i = 0; i < ax.length; i++) {
            for (j = 0; j < ay.length; j++) {
                for (k = 0; k < az.length; k++) {
                    sum += ax[i] * ay[j] * az[k]
                            * computeFGamma(i + j + k, radiusCPSquared * gamma);
                } // end for
            } // end for
        } // end for

        return (-norm1 * norm2 * 2.0 * Math.PI / gamma
                * Math.exp(-alpha1 * alpha2 * radiusABSquared / gamma) * sum);
    }

    /**
     * <i> "THO eq. 2.18 and 3.1 <i>
     */
    public static double[] constructAArray(int l1, int l2,
                                           double pa, double pb, double cp, double gamma) {
        int iMax = l1 + l2 + 1;
        double[] a = new double[iMax];

        int i, r, u, index;

        for (i = 0; i < iMax; i++) {
            for (r = 0; r < ((int) (Math.floor(i / 2.0) + 1.0)); r++) {
                for (u = 0; u < ((int) (Math.floor((i - 2.0 * r) / 2.0) + 1.0)); u++) {
                    index = i - 2 * r - u;

                    a[index] += constructATerm(i, r, u, l1, l2,
                            pa, pb, cp, gamma);
                } // end for
            } // end for
        } // end for

        return a;
    }

    /**
     * the A term <br>
     * <i> "THO eq. 2.18 <i>
     */
    public static double constructATerm(int i, int r, int u, int l1, int l2,
                                        double pax, double pbx, double cpx,
                                        double gamma) {
        return (Math.pow(-1.0, i) * binomialPrefactor(i, l1, l2, pax, pbx)
                * Math.pow(-1.0, u) * MathUtils.factorial(i)
                * Math.pow(cpx, i - 2 * r - 2 * u)
                * Math.pow(0.25 / gamma, r + u)
                / MathUtils.factorial(r)
                / MathUtils.factorial(u)
                / MathUtils.factorial(i - 2 * r - 2 * u));
    }

    /**
     * 2E coulomb interactions between 4 contracted Gaussians
     */
    public static double coulomb(ContractedGaussian a, ContractedGaussian b,
                                 ContractedGaussian c, ContractedGaussian d) {
        double jij = 0.0;

        ArrayList aExps = a.getExponents(),
                aCoefs = a.getCoefficients(),
                aNorms = a.getPrimNorms();
        Point3D aOrigin = a.getOrigin();
        Power aPower = a.getPowers();

        ArrayList bExps = b.getExponents(),
                bCoefs = b.getCoefficients(),
                bNorms = b.getPrimNorms();
        Point3D bOrigin = b.getOrigin();
        Power bPower = b.getPowers();

        ArrayList cExps = c.getExponents(),
                cCoefs = c.getCoefficients(),
                cNorms = c.getPrimNorms();
        Point3D cOrigin = c.getOrigin();
        Power cPower = c.getPowers();

        ArrayList dExps = d.getExponents(),
                dCoefs = d.getCoefficients(),
                dNorms = d.getPrimNorms();
        Point3D dOrigin = d.getOrigin();
        Power dPower = d.getPowers();

        int i, j, k, l;
        double iaExp, iaCoef, iaNorm,
                jbExp, jbCoef, jbNorm,
                kcExp, kcCoef, kcNorm;
        double repulsionTerm;

        for (i = 0; i < aExps.size(); i++) {
            iaCoef = ((Double) aCoefs.get(i)).doubleValue();
            iaExp = ((Double) aExps.get(i)).doubleValue();
            iaNorm = ((Double) aNorms.get(i)).doubleValue();

            for (j = 0; j < bExps.size(); j++) {
                jbCoef = ((Double) bCoefs.get(j)).doubleValue();
                jbExp = ((Double) bExps.get(j)).doubleValue();
                jbNorm = ((Double) bNorms.get(j)).doubleValue();

                for (k = 0; k < cExps.size(); k++) {
                    kcCoef = ((Double) cCoefs.get(k)).doubleValue();
                    kcExp = ((Double) cExps.get(k)).doubleValue();
                    kcNorm = ((Double) cNorms.get(k)).doubleValue();

                    for (l = 0; l < dExps.size(); l++) {
                        repulsionTerm = coulombRepulsion(aOrigin, iaNorm, aPower, iaExp,
                                bOrigin, jbNorm, bPower, jbExp,
                                cOrigin, kcNorm, cPower, kcExp,
                                dOrigin,
                                ((Double) dNorms.get(l)).doubleValue(),
                                dPower,
                                ((Double) dExps.get(l)).doubleValue());

                        jij += iaCoef * jbCoef * kcCoef
                                * ((Double) dCoefs.get(l)).doubleValue()
                                * repulsionTerm;
                    } // end l loop
                } // end k loop
            } // end j loop
        } // end i loop

        return (a.getNormalization() * b.getNormalization()
                * c.getNormalization() * d.getNormalization() * jij);
    }

    /**
     * coulomb repulsion term
     */
    public static double coulombRepulsion(Point3D a, double aNorm, Power aPower, double aAlpha,
                                          Point3D b, double bNorm, Power bPower, double bAlpha,
                                          Point3D c, double cNorm, Power cPower, double cAlpha,
                                          Point3D d, double dNorm, Power dPower, double dAlpha) {

        double radiusABSquared = a.distanceSquaredFrom(b);
        double radiusCDSquared = c.distanceSquaredFrom(d);

        Point3D p = gaussianProductCenter(aAlpha, a, bAlpha, b);
        Point3D q = gaussianProductCenter(cAlpha, c, dAlpha, d);

        double radiusPQSquared = p.distanceSquaredFrom(q);

        double gamma1 = aAlpha + bAlpha;
        double gamma2 = cAlpha + dAlpha;
        double delta = 0.25 * (1 / gamma1 + 1 / gamma2);

        double[] bx = constructBArray(aPower.getL(), bPower.getL(), cPower.getL(), dPower.getL(),
                p.getX(), a.getX(), b.getX(), q.getX(), c.getX(), d.getX(),
                gamma1, gamma2, delta);

        double[] by = constructBArray(aPower.getM(), bPower.getM(), cPower.getM(), dPower.getM(),
                p.getY(), a.getY(), b.getY(), q.getY(), c.getY(), d.getY(),
                gamma1, gamma2, delta);

        double[] bz = constructBArray(aPower.getN(), bPower.getN(), cPower.getN(), dPower.getN(),
                p.getZ(), a.getZ(), b.getZ(), q.getZ(), c.getZ(), d.getZ(),
                gamma1, gamma2, delta);

        double sum = 0.0;
        int i, j, k;
        for (i = 0; i < bx.length; i++) {
            for (j = 0; j < by.length; j++) {
                for (k = 0; k < bz.length; k++) {
                    sum += bx[i] * by[j] * bz[k]
                            * computeFGamma(i + j + k, 0.25 * radiusPQSquared / delta);
                } // end for
            } // end for
        } // end for

        return (2 * Math.pow(Math.PI, 2.5)
                / (gamma1 * gamma2 * Math.sqrt(gamma1 + gamma2))
                * Math.exp(-aAlpha * bAlpha * radiusABSquared / gamma1)
                * Math.exp(-cAlpha * dAlpha * radiusCDSquared / gamma2)
                * sum * aNorm * bNorm * cNorm * dNorm);
    }

    /**
     * Construct B array.
     * <p/>
     * <i> THO eq. 2.22 </i>
     */
    public static double[] constructBArray(int l1, int l2, int l3, int l4,
                                           double p, double a, double b, double q, double c, double d,
                                           double g1, double g2, double delta) {
        int iMax = l1 + l2 + l3 + l4 + 1;
        double[] bArr = new double[iMax];

        int i1, i2, r1, r2, u, index;

        for (i1 = 0; i1 < (l1 + l2 + 1); i1++) {
            for (i2 = 0; i2 < (l3 + l4 + 1); i2++) {
                for (r1 = 0; r1 < (i1 / 2 + 1); r1++) {
                    for (r2 = 0; r2 < (i2 / 2 + 1); r2++) {
                        for (u = 0; u < ((i1 + i2) / 2 - r1 - r2 + 1); u++) {
                            index = i1 + i2 - 2 * (r1 + r2) - u;

                            bArr[index] += constructBTerm(i1, i2, r1, r2, u,
                                    l1, l2, l3, l4, p, a, b, q, c, d,
                                    g1, g2, delta);
                        } // end u loop
                    } // end r2 loop
                } // end r1 loop
            } // end i2 loop
        } // end i1 loop

        return bArr;
    }

    /**
     * Construct the B term
     * <p/>
     * <i> THO eq. 2.22 </i>
     */
    public static double constructBTerm(int i1, int i2, int r1, int r2, int u,
                                        int l1, int l2, int l3, int l4, double px, double ax, double bx,
                                        double qx, double cx, double dx, double gamma1, double gamma2,
                                        double delta) {

        return (functionB(i1, l1, l2, px, ax, bx, r1, gamma1)
                * Math.pow(-1, i2)
                * functionB(i2, l3, l4, qx, cx, dx, r2, gamma2)
                * Math.pow(-1, u)
                * MathUtils.factorialRatioSquared(i1 + i2 - 2 * (r1 + r2), u)
                * Math.pow(qx - px, i1 + i2 - 2 * (r1 + r2) - 2 * u)
                / Math.pow(delta, i1 + i2 - 2 * (r1 + r2) - u));
    }

    /**
     * the function B, taken from PyQuante
     */
    public static double functionB(int i, int l1, int l2, double p, double a,
                                   double b, int r, double g) {
        return (binomialPrefactor(i, l1, l2, p - a, p - b)
                * functionB0(i, r, g));
    }

    /**
     * the function B0, taken from PyQuante
     */
    public static double functionB0(int i, int r, double g) {
        return (MathUtils.factorialRatioSquared(i, r)
                * Math.pow(4 * g, r - i));
    }

    /**
     * Indexing (i,j,k,l) into long array.
     */
    public static int ijkl2intindex(int i, int j, int k, int l) {
        int temp;

        if (i < j) {
            temp = i;
            i = j;
            j = temp;
        } // end if
        if (k < l) {
            temp = k;
            k = l;
            l = temp;
        } // end if

        int ij = i * (i + 1) / 2 + j;
        int kl = k * (k + 1) / 2 + l;

        if (ij < kl) {
            temp = ij;
            ij = kl;
            kl = temp;
        } // end id

        return (ij * (ij + 1) / 2 + kl);
    }

    /**
     * Incomplete gamma function
     */
    public static double computeFGamma(int m, double x) {
        x = Math.max(Math.abs(x), SMALL);

        return (0.5 * Math.pow(x, -m - 0.5)
                * gammaIncomplete(m + 0.5, x));
    }

    /**
     * Incomple gamma function gamma() computed from Numerical Recipes
     * routine gammp.
     */
    public static double gammaIncomplete(double a, double x) {
        double gammap = 0.0;
        double gln = 0.0;

        gln = gammln(a);

        if (x < (a + 1.0)) {
            // Series representation of Gamma. NumRec sect 6.1.
            if (x != 0.0) {
                double ap = a;
                double sum;
                double delta = sum = 1.0 / a;

                for (int i = 0; i < MAX_ITERATION; i++) {
                    ap++;
                    delta *= x / ap;
                    sum += delta;
                    if (Math.abs(delta) < Math.abs(sum) * EPS) break;
                } // end for

                gammap = sum * Math.exp(-x + a * Math.log(x) - gln);
            } else {
                gammap = 0.0;
            } // end if
        } else {
            // Continued fraction representation of Gamma. NumRec sect 6.1
            double b = (x + 1.0) - a;
            double c = 1.0 / FPMIN;
            double d = 1.0 / b;
            double h = d;
            double an, delta;

            for (int i = 1; i < (MAX_ITERATION + 1); i++) {
                an = -i * (i - a);
                b += 2.0;
                d = an * d + b;

                if (Math.abs(d) < FPMIN) d = FPMIN;

                c = b + an / c;

                if (Math.abs(c) < FPMIN) c = FPMIN;

                d = 1.0 / d;
                delta = d * c;
                h *= delta;

                if (Math.abs(delta - 1.0) < EPS) break;
            } // end for

            gammap = 1.0 - (Math.exp(-x + a * Math.log(x) - gln) * h);
        } // end if

        return (Math.exp(gln) * gammap);
    }

    /**
     * Numerical recipes, section 6.1
     */
    public static double gammln(double x) {
        double y = x;
        double tmp = x + 5.5;

        tmp -= (x + 0.5) * Math.log(tmp);

        double ser = 1.000000000190015;
        for (int j = 0; j < 6; j++) {
            y++;
            ser += cof[j] / y;
        } // end for

        return (-tmp + Math.log((2.5066282746310005 * ser) / x));
    }

    // for gammp mathod
    private static double SMALL = 0.00000001;
    private static double EPS = 3.0e-7;
    private static double FPMIN = 1.0e-30;

    private static int MAX_ITERATION = 100;

    private static double[] cof = {
            76.18009172947146, -86.50532032941677,
            24.01409824083091, -1.231739572450155,
            0.1208650973866179e-2, -0.5395239384953e-5
    };

} // end of class Integrals
