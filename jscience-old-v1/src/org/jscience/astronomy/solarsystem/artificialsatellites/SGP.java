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

package org.jscience.astronomy.solarsystem.artificialsatellites;

/**
 * Implementation of NORAD's SGP near-Earth propagator.
 */
public class SGP extends AbstractPropagator {
    /**
     * DOCUMENT ME!
     */
    private double AO;

    /**
     * DOCUMENT ME!
     */
    private double A1;

    /**
     * DOCUMENT ME!
     */
    private double c1;

    /**
     * DOCUMENT ME!
     */
    private double c2;

    /**
     * DOCUMENT ME!
     */
    private double c3;

    /**
     * DOCUMENT ME!
     */
    private double c4;

    /**
     * DOCUMENT ME!
     */
    private double c5;

    /**
     * DOCUMENT ME!
     */
    private double c6;

    /**
     * DOCUMENT ME!
     */
    private double D1;

    /**
     * DOCUMENT ME!
     */
    private double D1O;

    /**
     * DOCUMENT ME!
     */
    private double D2O;

    /**
     * DOCUMENT ME!
     */
    private double D3O;

    /**
     * DOCUMENT ME!
     */
    private double D4O;

    /**
     * DOCUMENT ME!
     */
    private double PO;

    /**
     * DOCUMENT ME!
     */
    private double QO;

    /**
     * DOCUMENT ME!
     */
    private double COSIO;

    /**
     * DOCUMENT ME!
     */
    private double SINIO;

    /**
     * DOCUMENT ME!
     */
    private double XLO;

    /**
     * DOCUMENT ME!
     */
    private double PO2NO;

    /**
     * DOCUMENT ME!
     */
    private double OMGDT;

    /**
     * DOCUMENT ME!
     */
    private double XNODOT;

    /**
     * DOCUMENT ME!
     */
    private double A;

    /**
     * DOCUMENT ME!
     */
    private double E;

    /**
     * DOCUMENT ME!
     */
    private double P;

    /**
     * DOCUMENT ME!
     */
    private double XNODES;

    /**
     * DOCUMENT ME!
     */
    private double OMGAS;

    /**
     * DOCUMENT ME!
     */
    private double XLS;

    /**
     * DOCUMENT ME!
     */
    private double AXNSL;

    /**
     * DOCUMENT ME!
     */
    private double AYNSL;

    /**
     * DOCUMENT ME!
     */
    private double XL;

    /**
     * DOCUMENT ME!
     */
    private double U;

    /**
     * DOCUMENT ME!
     */
    private int ITEM3;

    /**
     * DOCUMENT ME!
     */
    private double EO1;

    /**
     * DOCUMENT ME!
     */
    private double TEM5;

    /**
     * DOCUMENT ME!
     */
    private double SINEO1;

    /**
     * DOCUMENT ME!
     */
    private double COSEO1;

    /**
     * DOCUMENT ME!
     */
    private double TEM2;

    /**
     * DOCUMENT ME!
     */
    private double ECOSE;

    /**
     * DOCUMENT ME!
     */
    private double ESINE;

    /**
     * DOCUMENT ME!
     */
    private double EL2;

    /**
     * DOCUMENT ME!
     */
    private double PL;

    /**
     * DOCUMENT ME!
     */
    private double PL2;

    /**
     * DOCUMENT ME!
     */
    private double R;

    /**
     * DOCUMENT ME!
     */
    private double RDOT;

    /**
     * DOCUMENT ME!
     */
    private double RVDOT;

    /**
     * DOCUMENT ME!
     */
    private double TEMP;

    /**
     * DOCUMENT ME!
     */
    private double SINU;

    /**
     * DOCUMENT ME!
     */
    private double COSU;

    /**
     * DOCUMENT ME!
     */
    private double SU;

    /**
     * DOCUMENT ME!
     */
    private double SIN2U;

    /**
     * DOCUMENT ME!
     */
    private double COS2U;

    /**
     * DOCUMENT ME!
     */
    private double RK;

    /**
     * DOCUMENT ME!
     */
    private double UK;

    /**
     * DOCUMENT ME!
     */
    private double XNODEK;

    /**
     * DOCUMENT ME!
     */
    private double XINCK;

    /**
     * DOCUMENT ME!
     */
    private double SINUK;

    /**
     * DOCUMENT ME!
     */
    private double COSUK;

    /**
     * DOCUMENT ME!
     */
    private double SINNOK;

    /**
     * DOCUMENT ME!
     */
    private double COSNOK;

    /**
     * DOCUMENT ME!
     */
    private double SINIK;

    /**
     * DOCUMENT ME!
     */
    private double COSIK;

    /**
     * DOCUMENT ME!
     */
    private double XMX;

    /**
     * DOCUMENT ME!
     */
    private double XMY;

    /**
     * DOCUMENT ME!
     */
    private double UX;

    /**
     * DOCUMENT ME!
     */
    private double UY;

    /**
     * DOCUMENT ME!
     */
    private double UZ;

    /**
     * DOCUMENT ME!
     */
    private double VX;

    /**
     * DOCUMENT ME!
     */
    private double VY;

    /**
     * DOCUMENT ME!
     */
    private double VZ;

    /**
     * DOCUMENT ME!
     */
    private final ElementSet es;

/**
     * Constructs an instance of this class.
     */
    public SGP(ElementSet es) {
        this.es = es;
        initialize();
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        c1 = C1.CK2 * 1.5;
        c2 = C1.CK2 / 4.0;
        c3 = C1.CK2 / 2.0;
        c4 = (C1.XJ3 * Math.pow(C1.AE, 3)) / (4.0 * C1.CK2);
        COSIO = Math.cos(es.XINCL);
        SINIO = Math.sin(es.XINCL);
        A1 = Math.pow((C1.XKE / es.XNO), C1.TOTHRD);
        D1 = (c1 / A1 / A1 * ((3. * COSIO * COSIO) - 1.)) / Math.pow((1. -
                (es.EO * es.EO)), 1.5);
        AO = A1 * (1. - (1. / 3. * D1) - (D1 * D1) -
            (134. / 81. * D1 * D1 * D1));
        PO = AO * (1. - (es.EO * es.EO));
        QO = AO * (1. - es.EO);
        XLO = es.XMO + es.OMEGAO + es.XNODEO;
        D1O = c3 * SINIO * SINIO;
        D2O = c2 * ((7. * COSIO * COSIO) - 1.);
        D3O = c1 * COSIO;
        D4O = D3O * SINIO;
        PO2NO = es.XNO / (PO * PO);
        OMGDT = c1 * PO2NO * ((5. * COSIO * COSIO) - 1.);
        XNODOT = -2. * D3O * PO2NO;
        c5 = (.5 * c4 * SINIO * (3. + (5. * COSIO))) / (1. + COSIO);
        c6 = c4 * SINIO;
    }

    /**
     * DOCUMENT ME!
     *
     * @param TSINCE DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public OrbitalState propagate(double TSINCE) {
        // UPDATE FOR SECULAR GRAVITY AND ATMOSPHERIC DRAG
        A = es.XNO + (((2. * es.XNDT2O) + (3. * es.XNDD6O * TSINCE)) * TSINCE);
        A = AO * Math.pow((es.XNO / A), C1.TOTHRD);
        E = C1.E6A;

        if (A > QO) {
            E = 1. - (QO / A);
        }

        P = A * (1. - (E * E));
        XNODES = es.XNODEO + (XNODOT * TSINCE);
        OMGAS = es.OMEGAO + (OMGDT * TSINCE);
        XLS = MathUtils.FMOD2P(XLO +
                ((es.XNO + OMGDT + XNODOT +
                ((es.XNDT2O + (es.XNDD6O * TSINCE)) * TSINCE)) * TSINCE));

        // LONG PERIOD PERIODICS
        AXNSL = E * Math.cos(OMGAS);
        AYNSL = (E * Math.sin(OMGAS)) - (c6 / P);
        XL = MathUtils.FMOD2P(XLS - (c5 / P * AXNSL));

        // SOLVE KEPLERS EQUATION
        U = MathUtils.FMOD2P(XL - XNODES);
        ITEM3 = 0;
        EO1 = U;
        TEM5 = 1.;
line_20: 
        while (true) {
            SINEO1 = Math.sin(EO1);
            COSEO1 = Math.cos(EO1);

            if (Math.abs(TEM5) < C1.E6A) {
                break;
            }

            if (ITEM3 >= 10) {
                break;
            }

            ITEM3 = ITEM3 + 1;
            TEM5 = 1. - (COSEO1 * AXNSL) - (SINEO1 * AYNSL);
            TEM5 = ((U - (AYNSL * COSEO1) + (AXNSL * SINEO1)) - EO1) / TEM5;
            TEM2 = Math.abs(TEM5);

            if (TEM2 > 1.) {
                TEM5 = TEM2 / TEM5;
            }

            EO1 = EO1 + TEM5;
        }

        // SHORT PERIOD PRELIMINARY QUANTITIES
        ECOSE = (AXNSL * COSEO1) + (AYNSL * SINEO1);
        ESINE = (AXNSL * SINEO1) - (AYNSL * COSEO1);
        EL2 = (AXNSL * AXNSL) + (AYNSL * AYNSL);
        PL = A * (1. - EL2);
        PL2 = PL * PL;
        R = A * (1. - ECOSE);
        RDOT = (C1.XKE * Math.sqrt(A)) / R * ESINE;
        RVDOT = (C1.XKE * Math.sqrt(PL)) / R;
        TEMP = ESINE / (1. + Math.sqrt(1. - EL2));
        SINU = A / R * (SINEO1 - AYNSL - (AXNSL * TEMP));
        COSU = A / R * (COSEO1 - AXNSL + (AYNSL * TEMP));
        SU = MathUtils.ACTAN(SINU, COSU);

        // UPDATE FOR SHORT PERIODICS
        SIN2U = (COSU + COSU) * SINU;
        COS2U = 1. - (2. * SINU * SINU);
        RK = R + (D1O / PL * COS2U);
        UK = SU - (D2O / PL2 * SIN2U);
        XNODEK = XNODES + ((D3O * SIN2U) / PL2);
        XINCK = es.XINCL + (D4O / PL2 * COS2U);

        // ORIENTATION VECTORS
        SINUK = Math.sin(UK);
        COSUK = Math.cos(UK);
        SINNOK = Math.sin(XNODEK);
        COSNOK = Math.cos(XNODEK);
        SINIK = Math.sin(XINCK);
        COSIK = Math.cos(XINCK);
        XMX = -SINNOK * COSIK;
        XMY = COSNOK * COSIK;
        UX = (XMX * SINUK) + (COSNOK * COSUK);
        UY = (XMY * SINUK) + (SINNOK * COSUK);
        UZ = SINIK * SINUK;
        VX = (XMX * COSUK) - (COSNOK * SINUK);
        VY = (XMY * COSUK) - (SINNOK * SINUK);
        VZ = SINIK * COSUK;

        // POSITION AND VELOCITY
        OrbitalState sv = new OrbitalState();

        sv.TSINCE = TSINCE;
        sv.X = RK * UX;
        sv.Y = RK * UY;
        sv.Z = RK * UZ;
        sv.XDOT = RDOT * UX;
        sv.YDOT = RDOT * UY;
        sv.ZDOT = RDOT * UZ;
        sv.XDOT = (RVDOT * VX) + sv.XDOT;
        sv.YDOT = (RVDOT * VY) + sv.YDOT;
        sv.ZDOT = (RVDOT * VZ) + sv.ZDOT;

        return sv;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "SGP";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDeep() {
        return false;
    }
}
