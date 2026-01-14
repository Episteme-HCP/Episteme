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

// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package org.jscience.mathematics.analysis.elliptic;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * Implementation for Jacobi's elliptic functions.
 * <p/>
 * Attention! This class is not thread save.
 * This is usually no problem, when you do numerics.
 *
 * @author Markus Schmies, Boris Springborn,
 */

public class Jacobi
        extends Elliptic {

    Jacobi() {
    }

    static Complex z = Complex.ZERO;
    static Complex u = Complex.ZERO;
    final static Complex Z = Complex.ZERO;
    final static Complex c = Complex.ZERO;
    static Complex g = Complex.ZERO;
    final static Complex m = Complex.ZERO;

    static Complex k = Complex.ZERO;
    final static Complex K = Complex.ZERO;

    final static Complex kPrime = Complex.ZERO;
    final static Complex KPrime = Complex.ZERO;

    final static Complex tau = Complex.ZERO;

    static Complex an = Complex.ZERO;
    static Complex bn = Complex.ZERO;

    final static Complex ONE = new Complex(1);

    static Complex logOfC = Complex.ZERO;

    final static Complex theta1 = Complex.ZERO;
    final static Complex theta2 = Complex.ZERO;
    final static Complex theta3 = Complex.ZERO;
    final static Complex theta4 = Complex.ZERO;

    final static Complex snOfU = Complex.ZERO;
    final static Complex cnOfU = Complex.ZERO;
    final static Complex dnOfU = Complex.ZERO;

    final static Complex[] thetaConstants = new Complex[4];

    final static Complex logOfCForTheta1 = Complex.ZERO;
    final static Complex logOfCForTheta2 = Complex.ZERO;
    final static Complex logOfCForTheta3 = Complex.ZERO;
    final static Complex logOfCForTheta4 = Complex.ZERO;

    /**
     * Computes sn(u,k(&tau;)) for given latiuce paramter &tau;, and theta constants.
     *
     * @param u              argument
     * @param tau            lattice parameter
     * @param thetaConstants theta constants
     * @param snOfU          sn(u,k(&tau;)) (on output)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #sn(Complex,Complex)
     * @see #sn(double,double)
     */
    public static void sn(final Complex u, final Complex tau,
                          final Complex[] thetaConstants,
                          Complex snOfU) {

        z = u.divide(thetaConstants[3]);
        z = z.divide(thetaConstants[3]);

        theta1(z, tau, EPS, Z, logOfCForTheta1, theta1);
        theta4(z, tau, EPS, Z, logOfCForTheta4, theta4);

        logOfC = logOfCForTheta1.subtract(logOfCForTheta4);

        snOfU = Complex.exp(logOfC);

        snOfU = snOfU.divide(thetaConstants[2]);
        snOfU = snOfU.multiply(thetaConstants[3]);
        snOfU = snOfU.multiply(theta1);
        snOfU = snOfU.divide(theta4);
    }

    /**
     * Returns sn(u,k). It computes the lattice paramter
     * &tau; as function of k, and further the @theta;-constants.
     * Therefore in most cases it is more efficient to hash
     * these values and call
     * {@link #sn(Complex,Complex,Complex[],Complex)}.
     *
     * @param u argument
     * @param k modulus
     * @return sn(u,k)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #sn(Complex,Complex,Complex[],Complex)
     * @see #sn(double,double)
     */
    public static Complex sn(final Complex u, final Complex k) {

        tau_from_k(k, tau);

        thetaConstants(tau, EPS, thetaConstants);

        final Complex snOfU = Complex.ZERO;

        sn(u, tau, thetaConstants, snOfU);

        return snOfU;
    }

    /**
     * Returns sn(u,k). It computes the lattice paramter
     * &tau; as function of k, and further the @theta;-constants.
     * Therefore in most cases it is more efficient to hash
     * these values and call
     * {@link #sn(Complex,Complex,Complex[],Complex)}.
     *
     * @param u argument
     * @param k modulus
     * @return sn(u,k)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #sn(Complex,Complex,Complex[],Complex)
     * @see #sn(Complex,Complex)
     */
    public static double sn(final double u, final double k) {

        Jacobi.k = new Complex(k);
        Jacobi.u = new Complex(u);

        tau_from_k(Jacobi.k, tau);

        thetaConstants(tau, EPS, thetaConstants);

        sn(Jacobi.u, tau, thetaConstants, snOfU);

        return snOfU.real();
    }

    /**
     * Computes cn(u,k(&tau;)) for given latiuce paramter &tau;, and theta constants.
     *
     * @param u              argument
     * @param tau            lattice parameter
     * @param thetaConstants theta constants
     * @param cnOfU          sn(u,k(&tau;)) (on output)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #cn(Complex,Complex)
     */
    public static void cn(final Complex u, final Complex tau,
                          final Complex[] thetaConstants,
                          Complex cnOfU) {

        z = u.divide(thetaConstants[3]);
        z = z.divide(thetaConstants[3]);

        theta2(z, tau, EPS, Z, logOfCForTheta2, theta2);
        theta4(z, tau, EPS, Z, logOfCForTheta4, theta4);

        logOfC = logOfCForTheta2.subtract(logOfCForTheta4);

        cnOfU = Complex.exp(logOfC);

        cnOfU = cnOfU.divide(thetaConstants[2]);
        cnOfU = cnOfU.multiply(thetaConstants[0]);
        cnOfU = cnOfU.multiply(theta2);
        cnOfU = cnOfU.divide(theta4);
    }

    /**
     * Returns cn(u,k). It computes the lattice paramter
     * &tau; as function of k, and further the @theta;-constants.
     * Therefore in most cases it is more efficient to hash
     * these values and call
     * {@link #cn(Complex,Complex,Complex[],Complex)}.
     *
     * @param u argument
     * @param k modulus
     * @return cn(u,k)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #cn(Complex,Complex,Complex[],Complex)
     */
    public static Complex cn(final Complex u, final Complex k) {

        tau_from_k(k, tau);

        thetaConstants(tau, EPS, thetaConstants);

        final Complex cnOfU = Complex.ZERO;

        cn(u, tau, thetaConstants, cnOfU);

        return cnOfU;
    }

    /**
     * Returns cn(u,k). It computes the lattice paramter
     * &tau; as function of k, and further the @theta;-constants.
     * Therefore in most cases it is more efficient to hash
     * these values and call
     * {@link #cn(Complex,Complex,Complex[],Complex)}.
     *
     * @param u argument
     * @param k modulus
     * @return cn(u,k)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #cn(Complex,Complex,Complex[],Complex)
     * @see #cn(Complex,Complex)
     */
    public static double cn(final double u, final double k) {

        Jacobi.k = new Complex(k);
        Jacobi.u = new Complex(u);

        tau_from_k(Jacobi.k, tau);

        thetaConstants(tau, EPS, thetaConstants);

        cn(Jacobi.u, tau, thetaConstants, cnOfU);

        return cnOfU.real();
    }

    /**
     * Computes dn(u,k(&tau;)) for given latiuce paramter &tau;, and theta constants.
     *
     * @param u              argument
     * @param tau            lattice parameter
     * @param thetaConstants theta constants
     * @param dnOfU          sn(u,k(&tau;)) (on output)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #dn(Complex,Complex)
     */
    public static void dn(final Complex u, final Complex tau,
                          final Complex[] thetaConstants,
                          Complex dnOfU) {

        z = u.divide(thetaConstants[3]);
        z = z.divide(thetaConstants[3]);

        theta3(z, tau, EPS, Z, logOfCForTheta3, theta3);
        theta4(z, tau, EPS, Z, logOfCForTheta4, theta4);

        logOfC = logOfCForTheta3.subtract(logOfCForTheta4);

        dnOfU = Complex.exp(logOfC);

        dnOfU = dnOfU.divide(thetaConstants[3]);
        dnOfU = dnOfU.multiply(thetaConstants[0]);
        dnOfU = dnOfU.multiply(theta3);
        dnOfU = dnOfU.divide(theta4);
    }

    /**
     * Returns dn(u,k). It computes the lattice paramter
     * &tau; as function of k, and further the @theta;-constants.
     * Therefore in most cases it is more efficient to hash
     * these values and call
     * {@link #dn(Complex,Complex,Complex[],Complex)}.
     *
     * @param u argument
     * @param k modulus
     * @return dn(u,k)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #dn(Complex,Complex,Complex[],Complex)
     */
    public static Complex dn(final Complex u, final Complex k) {

        tau_from_k(k, tau);

        thetaConstants(tau, EPS, thetaConstants);

        final Complex dnOfU = Complex.ZERO;

        dn(u, tau, thetaConstants, dnOfU);

        return dnOfU;
    }


    /**
     * Returns dn(u,k). It computes the lattice paramter
     * &tau; as function of k, and further the @theta;-constants.
     * Therefore in most cases it is more efficient to hash
     * these values and call
     * {@link #dn(Complex,Complex,Complex[],Complex)}.
     *
     * @param u argument
     * @param k modulus
     * @return dn(u,k)
     * @see #thetaConstants(Complex)
     * @see #tau_from_k(Complex)
     * @see #dn(Complex,Complex,Complex[],Complex)
     * @see #dn(Complex,Complex)
     */
    public static double dn(final double u, final double k) {

        Jacobi.k = new Complex(k);
        Jacobi.u = new Complex(u);

        tau_from_k(Jacobi.k, tau);

        thetaConstants(tau, EPS, thetaConstants);

        dn(Jacobi.u, tau, thetaConstants, dnOfU);

        return dnOfU.real();
    }

    /**
     * Computes complementary modulus k' as function of the modulus.
     *
     * @param k      modulus
     * @param kPrime complementary modulus k'= sqrt( 1-k*k ) (on output)
     * @see #kPrime_from_k(Complex)
     */
    public static void kPrime_from_k(final Complex k, Complex kPrime) {
        kPrime = k.sqr();
        kPrime = (Complex) kPrime.negate();
        kPrime = kPrime.add(ONE);
        kPrime = kPrime.sqrt();
    }

    /**
     * Returns complementary modulus k' as function of the modulus.
     *
     * @param k modulus
     * @return complementary modulus k'= sqrt( 1-k*k )
     * @see #kPrime_from_k(Complex,Complex)
     */
    public static Complex kPrime_from_k(final Complex k) {
        final Complex kPrime = Complex.ZERO;
        kPrime_from_k(k, kPrime);
        return kPrime;
    }

    /**
     * Returns complementary modulus k' as function of the modulus.
     *
     * @param k modulus
     * @return complementary modulus k'= sqrt( 1-k*k )
     * @see #kPrime_from_k(Complex,Complex)
     */
    public static double kPrime_from_k(final double k) {
        return Math.sqrt(1 - k * k);
    }

    /**
     * Computes  m = a(infty) = b(infty)
     * pair series a(n+1) = ( a(n)+b(n) )/2, b(n+1) = sqrt(a(n)b(n))
     *
     * @param a   a(0)
     * @param b   b(0)
     * @param eps relative error
     * @param m   a(infty) = b(infty)
     * @see D.F.Lawden, Elliptic Functinos and Applications, page 81
     */
    static void arithmeticGeometricMeanAlgorigthm
            (final Complex a, final Complex b,
             final double eps,
             Complex m) {

        an = new Complex(a);
        bn = new Complex(b);

        for (int i = 0; i < 100; i++) {

            m = new Complex((an.real() + bn.real()) / 2, (an.imag() + bn.imag()) / 2);

            if (distSqr(an, bn) < absSqr(an) * eps * eps) {
                return;
            }

            g = an.multiply(bn);
            bn = g.sqrt();
            an = new Complex(m);
        }

        throw new RuntimeException("reached max count; algorithm failed");
    }

    /**
     * Returns r = |u-v|^2
     */
    private final static double distSqr(final Complex u, final Complex v) {
        double subRe = u.real() - v.real();
        double subIm = u.imag() - v.imag();

        return subRe * subRe + subIm * subIm;
    }

    /**
     * Returns |u.re+iu.im|^2
     */
    private final static double absSqr(final Complex u) {
        return u.real() * u.real() + u.imag() * u.imag();
    }

    /**
     * Computes quarter period K as function of the modulus k.
     *
     * @param k modulus
     * @param K quarter period K (on output)
     *          #see K_from_k(Complex)
     */
    public static void K_from_k(final Complex k, Complex K) {

        kPrime_from_k(k, kPrime);

        arithmeticGeometricMeanAlgorigthm(ONE, kPrime, EPS, m);

        K = new Complex(Math.PI / 2);
        K = K.divide(m);
    }

    /**
     * Returns quarter period K as function of the modulus k.
     *
     * @param k modulus
     * @return quarter period
     *         #see K_from_k(Complex,Complex)
     */
    public static Complex K_from_k(final Complex k) {
        final Complex K = Complex.ZERO;
        K_from_k(k, K);
        return K;
    }

    /**
     * Computes cmplementary quarter period K' as function of the modulus k.
     *
     * @param k      modulus
     * @param KPrime complementary quarter period K' (on output)
     * @see #KPrime_from_k(Complex)
     */
    public static void KPrime_from_k(final Complex k, Complex KPrime) {

        arithmeticGeometricMeanAlgorigthm(ONE, k, EPS, m);

        KPrime = new Complex(Math.PI / 2);
        KPrime = KPrime.divide(m);
    }

    /**
     * Returns complementary quarter Period K' as function of the modulus k.
     *
     * @param k modulus
     * @return complementary quarter Period K'
     * @see #KPrime_from_k(Complex,Complex)
     */
    public static Complex KPrime_from_k(final Complex k) {
        final Complex KPrime = Complex.ZERO;
        KPrime_from_k(k, KPrime);
        return KPrime;
    }

    /**
     * Computes modulus k as function of theta constants
     *
     * @param thetaConstants theta constants
     * @param k              modulus (on output)
     * @see #thetaConstants(Complex)
     * @see #k_from_thetaConstants(Complex[])
     */
    public static void k_from_thetaConstants(
            final Complex[] thetaConstants,
            Complex k) {

        k = thetaConstants[2].divide(thetaConstants[3]);
        k = k.sqr();
    }

    /**
     * Returns modulus k as function of theta constants.
     *
     * @param thetaConstants theta constants
     * @return modulus
     * @see #thetaConstants(Complex)
     * @see #k_from_thetaConstants(Complex[],Complex)
     */
    public static Complex k_from_thetaConstants(final Complex[] thetaConstants) {
        final Complex k = Complex.ZERO;
        k_from_thetaConstants(thetaConstants, k);
        return k;
    }

    /**
     * Computes complementary modulus k' as function theta constants.
     *
     * @param thetaConstants theta constants
     * @param kPrime         complementary modulus k' (on output)
     * @see #thetaConstants(Complex)
     * @see #kPrime_from_thetaConstants(Complex[])
     */
    public static void kPrime_from_thetaConstants(
            final Complex[] thetaConstants,
            Complex kPrime) {

        kPrime = thetaConstants[0].divide(thetaConstants[3]);
        kPrime = kPrime.sqr();
    }

    /**
     * Returns complementary modulus k' as function of theta constants.
     *
     * @param thetaConstants theta constants
     * @return complementary modulus
     * @see #thetaConstants(Complex)
     * @see #kPrime_from_thetaConstants(Complex[],Complex)
     */
    public static Complex kPrime_from_thetaConstants(
            final Complex[] thetaConstants) {
        final Complex kPrime = Complex.ZERO;
        kPrime_from_thetaConstants(thetaConstants, kPrime);
        return kPrime;
    }

    /**
     * Computes quarter period K as function of theta constants.
     *
     * @param thetaConstants theta constants
     * @param K              quarter period (on output)
     * @see #thetaConstants(Complex)
     * @see #K_from_thetaConstants(Complex[])
     */
    public static void K_from_thetaConstants(
            final Complex[] thetaConstants, Complex K) {

        K = thetaConstants[3].sqr();
        K = K.multiply(Math.PI / 2);
    }

    /**
     * Returns quarter period K as function of theta constants.
     *
     * @param thetaConstants theta constants
     * @return quarter period
     * @see #thetaConstants(Complex)
     * @see #K_from_thetaConstants(Complex[],Complex)
     */
    public static Complex K_from_thetaConstants(
            final Complex[] thetaConstants) {
        final Complex K = Complex.ZERO;
        K_from_thetaConstants(thetaConstants, K);
        return K;
    }

    /**
     * Computes complementary quarter period K' as function of
     * quarter period K and lattice parameter &tau;.
     *
     * @param K      quarter period
     * @param tau    lattice parameter
     * @param KPrime complementary quarter period (on output)
     * @see #KPrime_from_K_and_tau(Complex,Complex)
     */
    public static void KPrime_from_K_and_tau(
            final Complex K, final Complex tau, Complex KPrime) {
        KPrime = tau.multiply(K);
        KPrime = new Complex(KPrime.imag(), -KPrime.real());
    }

    /**
     * Returns complementary quarter period K' as function of
     * quarter period K and lattice parameter &tau;.
     *
     * @param K   quarter period
     * @param tau lattice parameter
     * @return complementary quarter period
     * @see #KPrime_from_K_and_tau(Complex,Complex,Complex)
     */
    public static Complex KPrime_from_K_and_tau(
            final Complex K, final Complex tau) {
        final Complex KPrime = Complex.ZERO;
        KPrime_from_K_and_tau(K, tau, KPrime);
        return KPrime;
    }

    /**
     * Computes quarter period K as function of
     * complementary quarter period K' and lattice parameter &tau;.
     *
     * @param KPrime complementary quarter period
     * @param tau    lattice parameter
     * @param K      quarter period (on output)
     * @see #K_from_KPrime_and_tau(Complex,Complex)
     */
    public static void K_from_KPrime_and_tau(
            final Complex KPrime, final Complex tau, Complex K) {
        K = KPrime.divide(tau);
        K = new Complex(-K.imag(), K.real());
    }

    /**
     * Returns quarter period K as function of
     * complementary quarter period K' and lattice parameter &tau;.
     *
     * @param KPrime complementary quarter period
     * @param tau    lattice parameter
     * @return quarter period (on output)
     * @see #K_from_KPrime_and_tau(Complex,Complex,Complex)
     */
    public static Complex K_from_KPrime_and_tau(
            final Complex KPrime, final Complex tau) {
        final Complex K = Complex.ZERO;
        K_from_KPrime_and_tau(KPrime, tau, K);
        return K;
    }

    /**
     * Computes complementary lattice paramter &tau;' = -1/&tau; for lattice
     * parameter &tau;.
     * tau and tauPrime may coinside.
     *
     * @param tau      lattice paramter &tau;
     * @param tauPrime complementary lattice paramter &tau;'(on output)
     * @see #tauPrime_from_tau(Complex)
     */
    public static void tauPrime_from_tau(final Complex tau,
                                         Complex tauPrime) {

        tauPrime = assignInvert(tau);
        tauPrime = (Complex) tauPrime.negate();
    }


    /**
     * Assigns <code>this</code> with inverse of <code>u</code>.
     * this=1/u.
     * In case of |u|==0 the following result occurs:
     * this.re = Double.POSITIVE_INFINITY, this.im = 0
     */
    private static final Complex assignInvert(Complex u) {
        double nn = u.real() * u.real() + u.imag() * u.imag();
        if (nn == 0.) {
            double re = Double.POSITIVE_INFINITY;
            double im = 0;
            return new Complex(re, im);
        } else {
            double re = u.real() / nn;
            double im = -u.imag() / nn;
            return new Complex(re, im);
        }
    }

    /**
     * Returns complementary lattice paramter &tau;' = -1/&tau; for lattice
     * parameter &tau;.
     *
     * @param tau lattice paramter &tau;
     * @return complementary lattice paramter &tau;'
     * @see #tauPrime_from_tau(Complex,Complex)
     */
    public static Complex tauPrime_from_tau(final Complex tau) {
        final Complex tauPrime = Complex.ZERO;
        tauPrime_from_tau(tau, tauPrime);
        return tauPrime;
    }

    /**
     * Computes lattice parameter &tau; as function of modulus k.
     *
     * @param k   modulus
     * @param tau lattice paramter &tau;
     * @see #tau_from_k(Complex)
     */
    public static void tau_from_k(final Complex k, Complex tau) {

        Jacobi.KPrime_from_k(k, KPrime);
        Jacobi.K_from_k(k, K);

        tau = KPrime.divide(K);
        tau = new Complex(-tau.imag(), tau.real());
    }


    /**
     * Computes lattice parameter &tau; as function of modulus k.
     *
     * @param k modulus
     * @return lattice paramter
     * @see #tau_from_k(Complex,Complex)
     */
    public static Complex tau_from_k(final Complex k) {
        final Complex tau = Complex.ZERO;
        tau_from_k(k, tau);
        return tau;
    }


}
