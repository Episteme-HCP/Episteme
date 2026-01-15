/*
 * Copyright (C) 2006 Matthew Funk
 * Licensed under the Academic Free License version 1.2
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Academic Free License version 1.2 for more details.
 */
package org.jscience.astronomy.solarsystem.artificialsatellites;

/**
 * Implementation of NORAD's SDP4 deep space propagator.
 */
public class SDP4 extends AbstractPropagator {
    /**
     * DOCUMENT ME!
     */
    private double A1;

    /**
     * DOCUMENT ME!
     */
    private double COSIO;

    /**
     * DOCUMENT ME!
     */
    private double THETA2;

    /**
     * DOCUMENT ME!
     */
    private double X3THM1;

    /**
     * DOCUMENT ME!
     */
    private double EOSQ;

    /**
     * DOCUMENT ME!
     */
    private double BETAO2;

    /**
     * DOCUMENT ME!
     */
    private double BETAO;

    /**
     * DOCUMENT ME!
     */
    private double DEL1;

    /**
     * DOCUMENT ME!
     */
    private double DELO;

    /**
     * DOCUMENT ME!
     */
    private double AO;

    /**
     * DOCUMENT ME!
     */
    private double XNODP;

    /**
     * DOCUMENT ME!
     */
    private double AODP;

    /**
     * DOCUMENT ME!
     */
    private double S4;

    /**
     * DOCUMENT ME!
     */
    private double QOMS24;

    /**
     * DOCUMENT ME!
     */
    private double PERIGE;

    /**
     * DOCUMENT ME!
     */
    private double PINVSQ;

    /**
     * DOCUMENT ME!
     */
    private double SING;

    /**
     * DOCUMENT ME!
     */
    private double COSG;

    /**
     * DOCUMENT ME!
     */
    private double TSI;

    /**
     * DOCUMENT ME!
     */
    private double ETA;

    /**
     * DOCUMENT ME!
     */
    private double ETASQ;

    /**
     * DOCUMENT ME!
     */
    private double EETA;

    /**
     * DOCUMENT ME!
     */
    private double PSISQ;

    /**
     * DOCUMENT ME!
     */
    private double COEF;

    /**
     * DOCUMENT ME!
     */
    private double COEF1;

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
    private double C4;

    /**
     * DOCUMENT ME!
     */
    private double SINIO;

    /**
     * DOCUMENT ME!
     */
    private double A3OVK2;

    /**
     * DOCUMENT ME!
     */
    private double X1MTH2;

    /**
     * DOCUMENT ME!
     */
    private double THETA4;

    /**
     * DOCUMENT ME!
     */
    private double TEMP1;

    /**
     * DOCUMENT ME!
     */
    private double TEMP2;

    /**
     * DOCUMENT ME!
     */
    private double TEMP3;

    /**
     * DOCUMENT ME!
     */
    private double XMDOT;

    /**
     * DOCUMENT ME!
     */
    private double X1M5TH;

    /**
     * DOCUMENT ME!
     */
    private double OMGDOT;

    /**
     * DOCUMENT ME!
     */
    private double XHDOT1;

    /**
     * DOCUMENT ME!
     */
    private double XNODOT;

    /**
     * DOCUMENT ME!
     */
    private double XNODCF;

    /**
     * DOCUMENT ME!
     */
    private double T2COF;

    /**
     * DOCUMENT ME!
     */
    private double XLCOF;

    /**
     * DOCUMENT ME!
     */
    private double AYCOF;

    /**
     * DOCUMENT ME!
     */
    private double X7THM1;

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XMDF = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef OMGADF = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private double XNODDF;

    /**
     * DOCUMENT ME!
     */
    private double TSQ;

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XNODE = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private double TEMPA;

    /**
     * DOCUMENT ME!
     */
    private double TEMPE;

    /**
     * DOCUMENT ME!
     */
    private double TEMPL;

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XN = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef EM = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XINC = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private double A;

    /**
     * DOCUMENT ME!
     */
    private DoubleRef E = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XMAM = new DoubleRef(0.0);

    /**
     * DOCUMENT ME!
     */
    private double XL;

    /**
     * DOCUMENT ME!
     */
    private double BETA;

    /**
     * DOCUMENT ME!
     */
    private double AXN;

    /**
     * DOCUMENT ME!
     */
    private double TEMP;

    /**
     * DOCUMENT ME!
     */
    private double XLL;

    /**
     * DOCUMENT ME!
     */
    private double AYNL;

    /**
     * DOCUMENT ME!
     */
    private double XLT;

    /**
     * DOCUMENT ME!
     */
    private double AYN;

    /**
     * DOCUMENT ME!
     */
    private double CAPU;

    /**
     * DOCUMENT ME!
     */
    private double SINEPW;

    /**
     * DOCUMENT ME!
     */
    private double COSEPW;

    /**
     * DOCUMENT ME!
     */
    private double TEMP4;

    /**
     * DOCUMENT ME!
     */
    private double TEMP5;

    /**
     * DOCUMENT ME!
     */
    private double TEMP6;

    /**
     * DOCUMENT ME!
     */
    private double EPW;

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
    private double ELSQ;

    /**
     * DOCUMENT ME!
     */
    private double PL;

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
    private double RFDOT;

    /**
     * DOCUMENT ME!
     */
    private double BETAL;

    /**
     * DOCUMENT ME!
     */
    private double COSU;

    /**
     * DOCUMENT ME!
     */
    private double SINU;

    /**
     * DOCUMENT ME!
     */
    private double U;

    /**
     * DOCUMENT ME!
     */
    private double COS2U;

    /**
     * DOCUMENT ME!
     */
    private double SIN2U;

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
    private double RDOTK;

    /**
     * DOCUMENT ME!
     */
    private double RFDOTK;

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
    private double SINIK;

    /**
     * DOCUMENT ME!
     */
    private double COSIK;

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
    public SDP4(ElementSet es) {
        this.es = es;
        initialize();
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        // RECOVER ORIGINAL MEAN MOTION (XNODP) AND SEMIMAJOR AXIS (AODP)
        // FROM INPUT ELEMENTS
        A1 = Math.pow((C1.XKE / es.XNO), C1.TOTHRD);
        COSIO = Math.cos(es.XINCL);
        THETA2 = COSIO * COSIO;
        X3THM1 = (3. * THETA2) - 1.;
        EOSQ = es.EO * es.EO;
        BETAO2 = 1. - EOSQ;
        BETAO = Math.sqrt(BETAO2);
        DEL1 = (1.5 * C1.CK2 * X3THM1) / (A1 * A1 * BETAO * BETAO2);
        AO = A1 * (1. -
            (DEL1 * ((.5 * C1.TOTHRD) + (DEL1 * (1. + (134. / 81. * DEL1))))));
        DELO = (1.5 * C1.CK2 * X3THM1) / (AO * AO * BETAO * BETAO2);
        XNODP = es.XNO / (1. + DELO);
        AODP = AO / (1. - DELO);

        // INITIALIZATION
        //
        // FOR PERIGEE BELOW 156 KM, THE VALUES OF
        // S AND QOMS2T ARE ALTERED
        S4 = C1.S;
        QOMS24 = C1.QOMS2T;
        PERIGE = ((AODP * (1. - es.EO)) - C1.AE) * C1.XKMPER;

        if (PERIGE < 156.) {
            S4 = PERIGE - 78.;

            if (PERIGE <= 98.) {
                S4 = 20.;
            }

            QOMS24 = Math.pow((((120. - S4) * C1.AE) / C1.XKMPER), 4);
            S4 = (S4 / C1.XKMPER) + C1.AE;
        }

        PINVSQ = 1. / (AODP * AODP * BETAO2 * BETAO2);
        SING = Math.sin(es.OMEGAO);
        COSG = Math.cos(es.OMEGAO);
        TSI = 1. / (AODP - S4);
        ETA = AODP * es.EO * TSI;
        ETASQ = ETA * ETA;
        EETA = es.EO * ETA;
        PSISQ = Math.abs(1. - ETASQ);
        COEF = QOMS24 * Math.pow(TSI, 4);
        COEF1 = COEF / Math.pow(PSISQ, 3.5);
        c2 = COEF1 * XNODP * ((AODP * (1. + (1.5 * ETASQ) +
            (EETA * (4. + ETASQ)))) +
            ((.75 * C1.CK2 * TSI) / PSISQ * X3THM1 * (8. +
            (3. * ETASQ * (8. + ETASQ)))));
        c1 = es.BSTAR * c2;
        SINIO = Math.sin(es.XINCL);
        A3OVK2 = -C1.XJ3 / C1.CK2 * Math.pow(C1.AE, 3);
        X1MTH2 = 1. - THETA2;
        C4 = 2. * XNODP * COEF1 * AODP * BETAO2 * (((ETA * (2. + (.5 * ETASQ))) +
            (es.EO * (.5 + (2. * ETASQ)))) -
            ((2. * C1.CK2 * TSI) / (AODP * PSISQ) * ((-3. * X3THM1 * (1. -
            (2. * EETA) + (ETASQ * (1.5 - (.5 * EETA))))) +
            (.75 * X1MTH2 * ((2. * ETASQ) - (EETA * (1. + ETASQ))) * Math.cos(2. * es.OMEGAO)))));
        THETA4 = THETA2 * THETA2;
        TEMP1 = 3. * C1.CK2 * PINVSQ * XNODP;
        TEMP2 = TEMP1 * C1.CK2 * PINVSQ;
        TEMP3 = 1.25 * C1.CK4 * PINVSQ * PINVSQ * XNODP;
        XMDOT = XNODP + (.5 * TEMP1 * BETAO * X3THM1) +
            (.0625 * TEMP2 * BETAO * (13. - (78. * THETA2) + (137. * THETA4)));
        X1M5TH = 1. - (5. * THETA2);
        OMGDOT = (-.5 * TEMP1 * X1M5TH) +
            (.0625 * TEMP2 * (7. - (114. * THETA2) + (395. * THETA4))) +
            (TEMP3 * (3. - (36. * THETA2) + (49. * THETA4)));
        XHDOT1 = -TEMP1 * COSIO;
        XNODOT = XHDOT1 +
            (((.5 * TEMP2 * (4. - (19. * THETA2))) +
            (2. * TEMP3 * (3. - (7. * THETA2)))) * COSIO);
        XNODCF = 3.5 * BETAO2 * XHDOT1 * c1;
        T2COF = 1.5 * c1;
        XLCOF = (.125 * A3OVK2 * SINIO * (3. + (5. * COSIO))) / (1. + COSIO);
        AYCOF = .25 * A3OVK2 * SINIO;
        X7THM1 = (7. * THETA2) - 1.;
        DEEP.DPINIT(es, EOSQ, SINIO, COSIO, BETAO, AODP, THETA2, SING, COSG,
            BETAO2, XMDOT, OMGDOT, XNODOT, XNODP);
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
        XMDF.value = es.XMO + (XMDOT * TSINCE);
        OMGADF.value = es.OMEGAO + (OMGDOT * TSINCE);
        XNODDF = es.XNODEO + (XNODOT * TSINCE);
        TSQ = TSINCE * TSINCE;
        XNODE.value = XNODDF + (XNODCF * TSQ);
        TEMPA = 1. - (c1 * TSINCE);
        TEMPE = es.BSTAR * C4 * TSINCE;
        TEMPL = T2COF * TSQ;
        XN.value = XNODP;
        DEEP.DPSEC(es, XMDF, OMGADF, XNODE, EM, XINC, XN, TSINCE);
        A = Math.pow((C1.XKE / XN.value), C1.TOTHRD) * Math.pow(TEMPA, 2);
        E.value = EM.value - TEMPE;
        XMAM.value = XMDF.value + (XNODP * TEMPL);
        DEEP.DPPER(E, XINC, OMGADF, XNODE, XMAM);
        XL = XMAM.value + OMGADF.value + XNODE.value;
        BETA = Math.sqrt(1. - (E.value * E.value));
        XN.value = C1.XKE / Math.pow(A, 1.5);

        // LONG PERIOD PERIODICS
        AXN = E.value * Math.cos(OMGADF.value);
        TEMP = 1. / (A * BETA * BETA);
        XLL = TEMP * XLCOF * AXN;
        AYNL = TEMP * AYCOF;
        XLT = XL + XLL;
        AYN = (E.value * Math.sin(OMGADF.value)) + AYNL;

        // SOLVE KEPLERS EQUATION
        CAPU = MathUtils.FMOD2P(XLT - XNODE.value);
        TEMP2 = CAPU;

        for (int I = 1; I <= 10; I++) {
            SINEPW = Math.sin(TEMP2);
            COSEPW = Math.cos(TEMP2);
            TEMP3 = AXN * SINEPW;
            TEMP4 = AYN * COSEPW;
            TEMP5 = AXN * COSEPW;
            TEMP6 = AYN * SINEPW;
            EPW = (((CAPU - TEMP4 + TEMP3) - TEMP2) / (1. - TEMP5 - TEMP6)) +
                TEMP2;

            if (Math.abs(EPW - TEMP2) <= C1.E6A) {
                break;
            }

            TEMP2 = EPW;
        }

        // SHORT PERIOD PRELIMINARY QUANTITIES
        ECOSE = TEMP5 + TEMP6;
        ESINE = TEMP3 - TEMP4;
        ELSQ = (AXN * AXN) + (AYN * AYN);
        TEMP = 1. - ELSQ;
        PL = A * TEMP;
        R = A * (1. - ECOSE);
        TEMP1 = 1. / R;
        RDOT = C1.XKE * Math.sqrt(A) * ESINE * TEMP1;
        RFDOT = C1.XKE * Math.sqrt(PL) * TEMP1;
        TEMP2 = A * TEMP1;
        BETAL = Math.sqrt(TEMP);
        TEMP3 = 1. / (1. + BETAL);
        COSU = TEMP2 * (COSEPW - AXN + (AYN * ESINE * TEMP3));
        SINU = TEMP2 * (SINEPW - AYN - (AXN * ESINE * TEMP3));
        U = MathUtils.ACTAN(SINU, COSU);
        SIN2U = 2. * SINU * COSU;
        COS2U = (2. * COSU * COSU) - 1.;
        TEMP = 1. / PL;
        TEMP1 = C1.CK2 * TEMP;
        TEMP2 = TEMP1 * TEMP;

        // UPDATE FOR SHORT PERIODICS
        RK = (R * (1. - (1.5 * TEMP2 * BETAL * X3THM1))) +
            (.5 * TEMP1 * X1MTH2 * COS2U);
        UK = U - (.25 * TEMP2 * X7THM1 * SIN2U);
        XNODEK = XNODE.value + (1.5 * TEMP2 * COSIO * SIN2U);
        XINCK = XINC.value + (1.5 * TEMP2 * COSIO * SINIO * COS2U);
        RDOTK = RDOT - (XN.value * TEMP1 * X1MTH2 * SIN2U);
        RFDOTK = RFDOT +
            (XN.value * TEMP1 * ((X1MTH2 * COS2U) + (1.5 * X3THM1)));

        // ORIENTATION VECTORS
        SINUK = Math.sin(UK);
        COSUK = Math.cos(UK);
        SINIK = Math.sin(XINCK);
        COSIK = Math.cos(XINCK);
        SINNOK = Math.sin(XNODEK);
        COSNOK = Math.cos(XNODEK);
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
        sv.XDOT = (RDOTK * UX) + (RFDOTK * VX);
        sv.YDOT = (RDOTK * UY) + (RFDOTK * VY);
        sv.ZDOT = (RDOTK * UZ) + (RFDOTK * VZ);

        return sv;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "SDP4";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDeep() {
        return true;
    }
}
