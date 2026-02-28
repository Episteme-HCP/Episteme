/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.astronomy;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.sets.Reals;
import java.util.List;

/**
 * Represents an artificial satellite orbiting Earth.
 * Implements SGP4 propagation for TLE (Two-Line Element) sets.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ArtificialSatellite extends CelestialBody {

    // TLE Data
    @SuppressWarnings("unused")
    private String line1;
    @SuppressWarnings("unused")
    private String line2;
    @SuppressWarnings("unused")
    private int satelliteNumber;
    @SuppressWarnings("unused")
    private char classification;
    @SuppressWarnings("unused")
    private int launchYear;
    @SuppressWarnings("unused")
    private int launchNumber;
    @SuppressWarnings("unused")
    private String launchPiece;
    @SuppressWarnings("unused")
    private int epochYear;
    @SuppressWarnings("unused")
    private double epochDay;
    @SuppressWarnings("unused")
    private double firstDerivativeMeanMotion;
    @SuppressWarnings("unused")
    private double secondDerivativeMeanMotion;
    private double bStar;
    @SuppressWarnings("unused")
    private int ephemerisType;
    @SuppressWarnings("unused")
    private int elementNumber;
    private double inclination;
    private double raan;
    private double eccentricity;
    private double argumentOfPerigee;
    private double meanAnomaly;
    private double meanMotion;
    @SuppressWarnings("unused")
    private int revolutionNumber;

    // SGP4 Constants (WGS-72 / WGS-84)
    private static final double CK2 = 5.413080e-4;
    private static final double CK4 = 6.2098875e-7;
    private static final double E6A = 1.0e-6;
    private static final double QOMS2T = 1.88027916e-9;
    private static final double S = 1.01222928;
    private static final double TOTHRD = 2.0 / 3.0;
    private static final double XJ3 = -2.53881e-6;
    private static final double XKE = 7.43669161e-2;
    private static final double XKMPER = 6378.135; // Earth radius in km
    private static final double XMNPDA = 1440.0;   // Minutes per day
    private static final double AE = 1.0;
    private static final double DE2RA = 0.0174532925199433;
    private static final double PI = 3.14159265358979323846;
    private static final double PIO2 = 1.57079632679489656;
    private static final double TWOPI = 6.283185307179586;
    private static final double X3PIO2 = 4.71238898;

    // SGP4 Variables
    private int isimp;
    private double c1, c2, c3, c4, c5, d2, d3, d4, t3cof, t4cof, t5cof, eta, delmo;
    private double sinio, cosio, sinmo, omgcof, xmcof, xnodcf, t2cof, xlcof, aycof, x7thm1;
    private double aodp, xnodp, xmdot, omgdot, xnodot, xhdot1;
    private double x1mth2, x3thm1;
    private double s4, qoms24, perige, pinvsq, tsi, etasq, eeta, psisq, coef, coef1;
    private double a3ovk2;

    // Initialized flag
    private boolean initialized = false;

    public ArtificialSatellite(String name, String line1, String line2) {
        super(name, Quantities.create(1000, Units.KILOGRAM), Quantities.create(10, Units.METER), 
              DenseVector.of(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance()), 
              DenseVector.of(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance()));
        this.line1 = line1;
        this.line2 = line2;
        // set name from TLE if generic
        if(name == null || name.isEmpty()){
            this.setName(line1.substring(2, 7)); // Use sat number
        }
        parseTLE(line1, line2);
        initializeSGP4();
    }

    private void parseTLE(String l1, String l2) {
        try {
            // Line 1
            satelliteNumber = Integer.parseInt(l1.substring(2, 7).trim());
            classification = l1.charAt(7);
            launchYear = Integer.parseInt(l1.substring(9, 11).trim());
            launchNumber = Integer.parseInt(l1.substring(11, 14).trim());
            launchPiece = l1.substring(14, 17).trim();
            epochYear = Integer.parseInt(l1.substring(18, 20).trim());
            epochDay = Double.parseDouble(l1.substring(20, 32).trim());
            firstDerivativeMeanMotion = Double.parseDouble(l1.substring(33, 43).trim());
            
            String ndd6Str = l1.substring(44, 52).trim(); // " 00000-0"
            // Handle implied decimal point
            if(ndd6Str.length() > 2) {
                 double base = Double.parseDouble(ndd6Str.substring(0, ndd6Str.length()-2));
                 int exp = Integer.parseInt(ndd6Str.substring(ndd6Str.length()-2));
                 secondDerivativeMeanMotion = base * Math.pow(10, exp - 5); 
            }

            String bstarStr = l1.substring(53, 61).trim();
            if(bstarStr.length() > 2) {
                double base = Double.parseDouble(bstarStr.substring(0, bstarStr.length()-2));
                int exp = Integer.parseInt(bstarStr.substring(bstarStr.length()-2));
                bStar = base * Math.pow(10, exp - 5);
            }
            
            ephemerisType = Integer.parseInt(l1.substring(62, 63).trim());
            elementNumber = Integer.parseInt(l1.substring(64, 68).trim());

            // Line 2
            inclination = Double.parseDouble(l2.substring(8, 16).trim());
            raan = Double.parseDouble(l2.substring(17, 25).trim());
            eccentricity = Double.parseDouble("0." + l2.substring(26, 33).trim());
            argumentOfPerigee = Double.parseDouble(l2.substring(34, 42).trim());
            meanAnomaly = Double.parseDouble(l2.substring(43, 51).trim());
            meanMotion = Double.parseDouble(l2.substring(52, 63).trim());
            
            String revStr = l2.substring(63, 68).trim();
            if(!revStr.isEmpty())
                revolutionNumber = Integer.parseInt(revStr);

            // Convert units
            inclination *= DE2RA;
            raan *= DE2RA;
            argumentOfPerigee *= DE2RA;
            meanAnomaly *= DE2RA;
            
            // Mean Motion: revs/day -> rad/min
            // XNO = XNO * TEMP * C1.XMNPDA where TEMP = 2PI/1440/1440
            // so XNO (rad/min) = XNO(rev/day) * 2PI / 1440
            meanMotion = meanMotion * TWOPI / XMNPDA; 
            
            // Normalize BStar
            bStar = bStar / AE; 

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid TLE format", e);
        }
    }

    private void initializeSGP4() {
        // Recover original mean motion (xnodp) and semimajor axis (aodp)
        double a1 = Math.pow(XKE / meanMotion, TOTHRD);
        cosio = Math.cos(inclination);
        double theta2 = cosio * cosio;
        x3thm1 = 3.0 * theta2 - 1.0;
        double eosq = eccentricity * eccentricity;
        double beta02 = 1.0 - eosq;
        double beta0 = Math.sqrt(beta02);
        double del1 = 1.5 * CK2 * x3thm1 / (a1 * a1 * beta0 * beta02);
        double a0 = a1 * (1.0 - del1 * (0.5 * TOTHRD + del1 * (1.0 + 134.0 / 81.0 * del1)));
        double del0 = 1.5 * CK2 * x3thm1 / (a0 * a0 * beta0 * beta02);
        xnodp = meanMotion / (1.0 + del0); 
        aodp = a0 / (1.0 - del0); 

        // Initialization
        isimp = 0;
        if (((aodp * (1.0 - eccentricity)) / AE) < ((220.0 / XKMPER) + AE)) {
            isimp = 1;
        }

        s4 = S;
        qoms24 = QOMS2T;
        perige = ((aodp * (1.0 - eccentricity)) - AE) * XKMPER;

        if (perige < 156.0) {
            s4 = perige - 78.0;
            if (perige <= 98.0) {
                s4 = 20.0;
            }
            qoms24 = Math.pow((((120.0 - s4) * AE) / XKMPER), 4);
            s4 = (s4 / XKMPER) + AE;
        }

        pinvsq = 1.0 / (aodp * aodp * beta02 * beta02);
        tsi = 1.0 / (aodp - s4);
        eta = aodp * eccentricity * tsi;
        etasq = eta * eta;
        eeta = eccentricity * eta;
        psisq = Math.abs(1.0 - etasq);
        coef = qoms24 * Math.pow(tsi, 4);
        coef1 = coef / Math.pow(psisq, 3.5);
        
        c2 = coef1 * xnodp * ((aodp * (1.0 + (1.5 * etasq) + (eeta * (4.0 + etasq)))) +
            ((0.75 * CK2 * tsi) / psisq * x3thm1 * (8.0 + (3.0 * etasq * (8.0 + etasq)))));
        c1 = bStar * c2;
        sinio = Math.sin(inclination);
        a3ovk2 = -XJ3 / CK2 * Math.pow(AE, 3);
        c3 = (coef * tsi * a3ovk2 * xnodp * AE * sinio) / eccentricity;
        x1mth2 = 1.0 - theta2;
        
        c4 = 2.0 * xnodp * coef1 * aodp * beta02 * (((eta * (2.0 + (0.5 * etasq))) +
            (eccentricity * (0.5 + (2.0 * etasq)))) -
            ((2.0 * CK2 * tsi) / (aodp * psisq) * ((-3.0 * x3thm1 * (1.0 -
            (2.0 * eeta) + (etasq * (1.5 - (0.5 * eeta))))) +
            (0.75 * x1mth2 * ((2.0 * etasq) - (eeta * (1.0 + etasq))) * Math.cos(2.0 * argumentOfPerigee)))));
            
        c5 = 2.0 * coef1 * aodp * beta02 * (1.0 + (2.75 * (etasq + eeta)) + (eeta * etasq));
        
        double theta4 = theta2 * theta2;
        double temp1 = 3.0 * CK2 * pinvsq * xnodp;
        double temp2 = temp1 * CK2 * pinvsq;
        double temp3 = 1.25 * CK4 * pinvsq * pinvsq * xnodp;
        
        xmdot = xnodp + (0.5 * temp1 * beta0 * x3thm1) +
            (0.0625 * temp2 * beta0 * (13.0 - (78.0 * theta2) + (137.0 * theta4)));
            
        double x1m5th = 1.0 - (5.0 * theta2);
        
        omgdot = (-0.5 * temp1 * x1m5th) +
            (0.0625 * temp2 * (7.0 - (114.0 * theta2) + (395.0 * theta4))) +
            (temp3 * (3.0 - (36.0 * theta2) + (49.0 * theta4)));
            
        xhdot1 = -temp1 * cosio;
        xnodot = xhdot1 + (((0.5 * temp2 * (4.0 - (19.0 * theta2))) + (2.0 * temp3 * (3.0 - (7.0 * theta2)))) * cosio);
        
        omgcof = bStar * c3 * Math.cos(argumentOfPerigee);
        xmcof = (-TOTHRD * coef * bStar * AE) / eeta;
        xnodcf = 3.5 * beta02 * xhdot1 * c1;
        t2cof = 1.5 * c1;
        xlcof = (0.125 * a3ovk2 * sinio * (3.0 + (5.0 * cosio))) / (1.0 + cosio);
        aycof = 0.25 * a3ovk2 * sinio;
        delmo = Math.pow((1.0 + (eta * Math.cos(meanAnomaly))), 3);
        sinmo = Math.sin(meanAnomaly);
        x7thm1 = (7.0 * theta2) - 1.0;

        if (isimp != 1) {
            double c1sq = c1 * c1;
            d2 = 4.0 * aodp * tsi * c1sq;
            double temp = (d2 * tsi * c1) / 3.0;
            d3 = ((17.0 * aodp) + s4) * temp;
            d4 = 0.5 * temp * aodp * tsi * ((221.0 * aodp) + (31.0 * s4)) * c1;
            t3cof = d2 + (2.0 * c1sq);
            t4cof = 0.25 * ((3.0 * d3) + (c1 * ((12.0 * d2) + (10.0 * c1sq))));
            t5cof = 0.2 * ((3.0 * d4) + (12.0 * c1 * d3) + (6.0 * d2 * d2) +
                (15.0 * c1sq * ((2.0 * d2) + c1sq)));
        }
        
        initialized = true;
    }

    /**
     * Propagates the satellite state to the given time since epoch.
     * @param tSince minutes since epoch
     */
    public void propagate(double tSince) {
        if (!initialized) return;

        // Update for secular gravity and atmospheric drag
        double xmdf = meanAnomaly + (xmdot * tSince);
        double omgadf = argumentOfPerigee + (omgdot * tSince);
        double xnoddf = raan + (xnodot * tSince);
        double omega = omgadf;
        double xmp = xmdf;
        double tsq = tSince * tSince;
        double xnode = xnoddf + (xnodcf * tsq);
        double tempa = 1.0 - (c1 * tSince);
        double tempe = bStar * c4 * tSince;
        double templ = t2cof * tsq;

        if (isimp != 1) {
            double delomg = omgcof * tSince;
            double delm = xmcof * (Math.pow((1.0 + (eta * Math.cos(xmdf))), 3) - delmo);
            double temp = delomg + delm;
            xmp = xmdf + temp;
            omega = omgadf - temp;
            double tcube = tsq * tSince;
            double tfour = tSince * tcube;
            tempa = tempa - (d2 * tsq) - (d3 * tcube) - (d4 * tfour);
            tempe = tempe + (bStar * c5 * (Math.sin(xmp) - sinmo));
            templ = templ + (t3cof * tcube) + (tfour * (t4cof + (tSince * t5cof)));
        }

        double a = aodp * Math.pow(tempa, 2);
        double e = eccentricity - tempe;
        double xl = xmp + omega + xnode + (xnodp * templ);
        double beta = Math.sqrt(1.0 - (e * e));
        double xn = XKE / Math.pow(a, 1.5);

        // Long period periodics
        double axn = e * Math.cos(omega);
        double temp = 1.0 / (a * beta * beta);
        double xll = temp * xlcof * axn;
        double aynl = temp * aycof;
        double xlt = xl + xll;
        double ayn = (e * Math.sin(omega)) + aynl;

        // Solve Kepler's Equation
        double capu = fmod2p(xlt - xnode);
        double temp2 = capu;
        double sinepw = 0, cosepw = 0;
        
        for (int i = 1; i <= 10; i++) {
            sinepw = Math.sin(temp2);
            cosepw = Math.cos(temp2);
            double temp3 = axn * sinepw;
            double temp4 = ayn * cosepw;
            double temp5 = axn * cosepw;
            double temp6 = ayn * sinepw;
            double epw = (capu - temp4 + temp3 - temp2) / (1.0 - temp5 - temp6) + temp2;
            if (Math.abs(epw - temp2) <= E6A) break;
            temp2 = epw;
        }

        // Short period preliminary quantities
        
        // V1:
        
        // V1:
        // CAPU = MathUtils.FMOD2P(XLT - XNODE);
        // ... (Newton-Raphson)
        // ECOSE = AXN * COSEPW + AYN * SINEPW;
        // ESINE = AXN * SINEPW - AYN * COSEPW;
        // ELSQ = AXN * AXN + AYN * AYN;
        // TEMP = 1. - ELSQ;
        // PL = A * TEMP;
        // R = A * (1. - ECOSE);
        // TEMP1 = 1. / R;
        // RDOT = XKE * Math.sqrt(A) * ESINE * TEMP1;
        // RFDOT = XKE * Math.sqrt(PL) * TEMP1;
        // TEMP2 = A * TEMP1;
        // BETAL = Math.sqrt(TEMP);
        // TEMP3 = 1. / (1. + BETAL);
        // COSU = TEMP2 * (COSEPW - AXN + AYN * ESINE * TEMP3);
        // SINU = TEMP2 * (SINEPW - AYN - AXN * ESINE * TEMP3);
        
        double ecose_v1 = axn * cosepw + ayn * sinepw;
        double esine = axn * sinepw - ayn * cosepw;
        double elsq = axn * axn + ayn * ayn;
        temp = 1.0 - elsq;
        double pl = a * temp;
        double r = a * (1.0 - ecose_v1);
        double temp1 = 1.0 / r;
        double rdot = XKE * Math.sqrt(a) * esine * temp1;
        double rfdot = XKE * Math.sqrt(pl) * temp1;
        temp2 = a * temp1;
        double betal = Math.sqrt(temp);
        double temp3 = 1.0 / (1.0 + betal);
        double cosu = temp2 * (cosepw - axn + ayn * esine * temp3);
        double sinu = temp2 * (sinepw - ayn - axn * esine * temp3);
        
        double u = actan(sinu, cosu);
        double sin2u = 2.0 * sinu * cosu;
        double cos2u = 2.0 * cosu * cosu - 1.0;
        
        double temp_k = 1.0 / pl;
        double temp1_k = CK2 * temp_k;
        double temp2_k = temp1_k * temp_k;
        
        // Short period periodics
        double rk = r * (1.0 - 1.5 * temp2_k * betal * x3thm1) + 
            0.5 * temp1_k * x1mth2 * cos2u;
        double uk = u - 0.25 * temp2_k * x7thm1 * sin2u;
        double xnodek = xnode + 1.5 * temp2_k * cosio * sin2u;
        double xinck = inclination + 1.5 * temp2_k * cosio * sinio * cos2u;
        double rdotk = rdot - n_val(a) * temp1_k * x1mth2 * sin2u; // n_val? XN
        rdotk = rdot - xn * temp1_k * x1mth2 * sin2u;
        double rfdotk = rfdot + xn * temp1_k * (x1mth2 * cos2u + 1.5 * x3thm1);
        
        // Orientation vectors
        double sinuk = Math.sin(uk);
        double cosuk = Math.cos(uk);
        double sinik = Math.sin(xinck);
        double cosik = Math.cos(xinck);
        double sinnok = Math.sin(xnodek);
        double cosnok = Math.cos(xnodek);
        
        double xmx = -sinnok * cosik;
        double xmy = cosnok * cosik;
        double ux = xmx * sinuk + cosnok * cosuk;
        double uy = xmy * sinuk + sinnok * cosuk;
        double uz = sinik * sinuk;
        double vx = xmx * cosuk - cosnok * sinuk;
        double vy = xmy * cosuk - sinnok * sinuk;
        double vz = sinik * cosuk;
        
        // Position and Velocity
        double x = rk * ux * XKMPER * 1000; // meters
        double y = rk * uy * XKMPER * 1000;
        double z = rk * uz * XKMPER * 1000;
        
        double xdot = (rdotk * ux + rfdotk * vx) * XKMPER / 60.0 * 1000; // m/s
        double ydot = (rdotk * uy + rfdotk * vy) * XKMPER / 60.0 * 1000;
        double zdot = (rdotk * uz + rfdotk * vz) * XKMPER / 60.0 * 1000;
        
        this.setPosition(Real.of(x), Real.of(y), Real.of(z));
        this.setVelocity(Real.of(xdot), Real.of(ydot), Real.of(zdot));
    }

    
    // Helper to get mean motion n from a (not needed if we have XN)
    private double n_val(double a) {
        return XKE / Math.pow(a, 1.5);
    }

    private static double fmod2p(double x) {
        double r = x;
        int i = (int) (r / TWOPI);
        r = r - (i * TWOPI);
        if (r < 0) r += TWOPI;
        return r;
    }
    
    private static double actan(double sinx, double cosx) {
        if (cosx == 0.0) {
            if (sinx > 0) return PIO2;
            else return X3PIO2;
        }
        if (cosx > 0.0) {
             if (sinx > 0) return Math.atan(sinx/cosx);
             else return Math.atan(sinx/cosx) + TWOPI;
        } else {
             return Math.atan(sinx/cosx) + PI;
        }
    }
}
