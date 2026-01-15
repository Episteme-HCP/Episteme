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
 * Implementation of NORAD's SDP8 deep space propagator.
 */
public class SDP8 extends AbstractPropagator {
    /**
     * DOCUMENT ME!
     */
    final private double RHO = .15696615;

    /**
     * DOCUMENT ME!
     */
    private double A1;

    /**
     * DOCUMENT ME!
     */
    private double COSI;

    /**
     * DOCUMENT ME!
     */
    private double THETA2;

    /**
     * DOCUMENT ME!
     */
    private double TTHMUN;

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
    private double AODP;

    /**
     * DOCUMENT ME!
     */
    private double XNODP;

    /**
     * DOCUMENT ME!
     */
    private double B;

    /**
     * DOCUMENT ME!
     */
    private double PO;

    /**
     * DOCUMENT ME!
     */
    private double POM2;

    /**
     * DOCUMENT ME!
     */
    private double SINI;

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
    private double TEMP;

    /**
     * DOCUMENT ME!
     */
    private double SINIO2;

    /**
     * DOCUMENT ME!
     */
    private double COSIO2;

    /**
     * DOCUMENT ME!
     */
    private double THETA4;

    /**
     * DOCUMENT ME!
     */
    private double UNM5TH;

    /**
     * DOCUMENT ME!
     */
    private double UNMTH2;

    /**
     * DOCUMENT ME!
     */
    private double A3COF;

    /**
     * DOCUMENT ME!
     */
    private double PARDT1;

    /**
     * DOCUMENT ME!
     */
    private double PARDT2;

    /**
     * DOCUMENT ME!
     */
    private double PARDT4;

    /**
     * DOCUMENT ME!
     */
    private double XMDT1;

    /**
     * DOCUMENT ME!
     */
    private double XGDT1;

    /**
     * DOCUMENT ME!
     */
    private double XHDT1;

    /**
     * DOCUMENT ME!
     */
    private double XLLDOT;

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
    private double TSI;

    /**
     * DOCUMENT ME!
     */
    private double ETA;

    /**
     * DOCUMENT ME!
     */
    private double ETA2;

    /**
     * DOCUMENT ME!
     */
    private double PSIM2;

    /**
     * DOCUMENT ME!
     */
    private double ALPHA2;

    /**
     * DOCUMENT ME!
     */
    private double EETA;

    /**
     * DOCUMENT ME!
     */
    private double COS2G;

    /**
     * DOCUMENT ME!
     */
    private double D5;

    /**
     * DOCUMENT ME!
     */
    private double D1;

    /**
     * DOCUMENT ME!
     */
    private double D2;

    /**
     * DOCUMENT ME!
     */
    private double D3;

    /**
     * DOCUMENT ME!
     */
    private double D4;

    /**
     * DOCUMENT ME!
     */
    private double B1;

    /**
     * DOCUMENT ME!
     */
    private double B2;

    /**
     * DOCUMENT ME!
     */
    private double B3;

    /**
     * DOCUMENT ME!
     */
    private double C0;

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
    private double C5;

    /**
     * DOCUMENT ME!
     */
    private double XNDT;

    /**
     * DOCUMENT ME!
     */
    private double XNDTN;

    /**
     * DOCUMENT ME!
     */
    private double EDOT;

    /**
     * DOCUMENT ME!
     */
    private double Z1;

    /**
     * DOCUMENT ME!
     */
    private double Z7;

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XMAMDF = new DoubleRef(0.0f);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef OMGASM = new DoubleRef(0.0f);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XNODES = new DoubleRef(0.0f);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XN = new DoubleRef(0.0f);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef EM = new DoubleRef(0.0f);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XINC = new DoubleRef(0.0f);

    /**
     * DOCUMENT ME!
     */
    private DoubleRef XMAM = new DoubleRef(0.0f);

    /**
     * DOCUMENT ME!
     */
    private double ZC2;

    /**
     * DOCUMENT ME!
     */
    private double SINE;

    /**
     * DOCUMENT ME!
     */
    private double COSE;

    /**
     * DOCUMENT ME!
     */
    private double ZC5;

    /**
     * DOCUMENT ME!
     */
    private double CAPE;

    /**
     * DOCUMENT ME!
     */
    private double AM;

    /**
     * DOCUMENT ME!
     */
    private double BETA2M;

    /**
     * DOCUMENT ME!
     */
    private double SINOS;

    /**
     * DOCUMENT ME!
     */
    private double COSOS;

    /**
     * DOCUMENT ME!
     */
    private double AXNM;

    /**
     * DOCUMENT ME!
     */
    private double AYNM;

    /**
     * DOCUMENT ME!
     */
    private double PM;

    /**
     * DOCUMENT ME!
     */
    private double G1;

    /**
     * DOCUMENT ME!
     */
    private double G2;

    /**
     * DOCUMENT ME!
     */
    private double G3;

    /**
     * DOCUMENT ME!
     */
    private double BETA;

    /**
     * DOCUMENT ME!
     */
    private double G4;

    /**
     * DOCUMENT ME!
     */
    private double G5;

    /**
     * DOCUMENT ME!
     */
    private double SNF;

    /**
     * DOCUMENT ME!
     */
    private double CSF;

    /**
     * DOCUMENT ME!
     */
    private double FM;

    /**
     * DOCUMENT ME!
     */
    private double SNFG;

    /**
     * DOCUMENT ME!
     */
    private double CSFG;

    /**
     * DOCUMENT ME!
     */
    private double SN2F2G;

    /**
     * DOCUMENT ME!
     */
    private double CS2F2G;

    /**
     * DOCUMENT ME!
     */
    private double ECOSF;

    /**
     * DOCUMENT ME!
     */
    private double G10;

    /**
     * DOCUMENT ME!
     */
    private double RM;

    /**
     * DOCUMENT ME!
     */
    private double AOVR;

    /**
     * DOCUMENT ME!
     */
    private double G13;

    /**
     * DOCUMENT ME!
     */
    private double G14;

    /**
     * DOCUMENT ME!
     */
    private double DR;

    /**
     * DOCUMENT ME!
     */
    private double DIWC;

    /**
     * DOCUMENT ME!
     */
    private double DI;

    /**
     * DOCUMENT ME!
     */
    private double SINI2;

    /**
     * DOCUMENT ME!
     */
    private double SNI2DU;

    /**
     * DOCUMENT ME!
     */
    private double XLAMB;

    /**
     * DOCUMENT ME!
     */
    private double Y4;

    /**
     * DOCUMENT ME!
     */
    private double Y5;

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
    private double SNLAMB;

    /**
     * DOCUMENT ME!
     */
    private double CSLAMB;

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
    public SDP8(ElementSet es) {
        this.es = es;
        initialize();
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        // RECOVER ORIGINAL MEAN MOTION (XNODP) AND SEMIMAJOR AXIS (AODP)
        // FROM INPUT ELEMENTS --------- CALCULATE BALLISTIC COEFFICIENT
        // (B TERM) FROM INPUT B* DRAG TERM
        A1 = Math.pow((C1.XKE / es.XNO), C1.TOTHRD);
        COSI = Math.cos(es.XINCL);
        THETA2 = COSI * COSI;
        TTHMUN = (3. * THETA2) - 1.;
        EOSQ = es.EO * es.EO;
        BETAO2 = 1. - EOSQ;
        BETAO = Math.sqrt(BETAO2);
        DEL1 = (1.5 * C1.CK2 * TTHMUN) / (A1 * A1 * BETAO * BETAO2);
        AO = A1 * (1. -
            (DEL1 * ((.5 * C1.TOTHRD) + (DEL1 * (1. + (134. / 81. * DEL1))))));
        DELO = (1.5 * C1.CK2 * TTHMUN) / (AO * AO * BETAO * BETAO2);
        AODP = AO / (1. - DELO);
        XNODP = es.XNO / (1. + DELO);
        B = (2. * es.BSTAR) / RHO;

        // INITIALIZATION
        PO = AODP * BETAO2;
        POM2 = 1. / (PO * PO);
        SINI = Math.sin(es.XINCL);
        SING = Math.sin(es.OMEGAO);
        COSG = Math.cos(es.OMEGAO);
        TEMP = .5 * es.XINCL;
        SINIO2 = Math.sin(TEMP);
        COSIO2 = Math.cos(TEMP);
        THETA4 = Math.pow(THETA2, 2);
        UNM5TH = 1. - (5. * THETA2);
        UNMTH2 = 1. - THETA2;
        A3COF = -C1.XJ3 / C1.CK2 * Math.pow(C1.AE, 3);
        PARDT1 = 3. * C1.CK2 * POM2 * XNODP;
        PARDT2 = PARDT1 * C1.CK2 * POM2;
        PARDT4 = 1.25 * C1.CK4 * POM2 * POM2 * XNODP;
        XMDT1 = .5 * PARDT1 * BETAO * TTHMUN;
        XGDT1 = -.5 * PARDT1 * UNM5TH;
        XHDT1 = -PARDT1 * COSI;
        XLLDOT = XNODP + XMDT1 +
            (.0625 * PARDT2 * BETAO * (13. - (78. * THETA2) + (137. * THETA4)));
        OMGDT = XGDT1 +
            (.0625 * PARDT2 * (7. - (114. * THETA2) + (395. * THETA4))) +
            (PARDT4 * (3. - (36. * THETA2) + (49. * THETA4)));
        XNODOT = XHDT1 +
            (((.5 * PARDT2 * (4. - (19. * THETA2))) +
            (2. * PARDT4 * (3. - (7. * THETA2)))) * COSI);
        TSI = 1. / (PO - C1.S);
        ETA = es.EO * C1.S * TSI;
        ETA2 = Math.pow(ETA, 2);
        PSIM2 = Math.abs(1. / (1. - ETA2));
        ALPHA2 = 1. + EOSQ;
        EETA = es.EO * ETA;
        COS2G = (2. * Math.pow(COSG, 2)) - 1.;
        D5 = TSI * PSIM2;
        D1 = D5 / PO;
        D2 = 12. + (ETA2 * (36. + (4.5 * ETA2)));
        D3 = ETA2 * (15. + (2.5 * ETA2));
        D4 = ETA * (5. + (3.75 * ETA2));
        B1 = C1.CK2 * TTHMUN;
        B2 = -C1.CK2 * UNMTH2;
        B3 = A3COF * SINI;
        C0 = (.5 * B * RHO * C1.QOMS2T * XNODP * AODP * Math.pow(TSI, 4) * Math.pow(PSIM2,
                3.5)) / Math.sqrt(ALPHA2);
        c1 = 1.5 * XNODP * Math.pow(ALPHA2, 2) * C0;
        C4 = D1 * D3 * B2;
        C5 = D5 * D4 * B3;
        XNDT = c1 * ((2. + (ETA2 * (3. + (34. * EOSQ))) +
            (5. * EETA * (4. + ETA2)) + (8.5 * EOSQ)) + (D1 * D2 * B1) +
            (C4 * COS2G) + (C5 * SING));
        XNDTN = XNDT / XNODP;
        EDOT = -C1.TOTHRD * XNDTN * (1. - es.EO);
        DEEP.DPINIT(es, EOSQ, SINI, COSI, BETAO, AODP, THETA2, SING, COSG,
            BETAO2, XLLDOT, OMGDT, XNODOT, XNODP);
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
        Z1 = .5 * XNDT * TSINCE * TSINCE;
        Z7 = (3.5 * C1.TOTHRD * Z1) / XNODP;
        XMAMDF.value = es.XMO + (XLLDOT * TSINCE);
        OMGASM.value = es.OMEGAO + (OMGDT * TSINCE) + (Z7 * XGDT1);
        XNODES.value = es.XNODEO + (XNODOT * TSINCE) + (Z7 * XHDT1);
        XN.value = XNODP;
        DEEP.DPSEC(es, XMAMDF, OMGASM, XNODES, EM, XINC, XN, TSINCE);
        XN.value = XN.value + (XNDT * TSINCE);
        EM.value = EM.value + (EDOT * TSINCE);
        XMAM.value = XMAMDF.value + Z1 + (Z7 * XMDT1);
        DEEP.DPPER(EM, XINC, OMGASM, XNODES, XMAM);
        XMAM.value = MathUtils.FMOD2P(XMAM.value);

        // SOLVE KEPLERS EQUATION
        ZC2 = XMAM.value +
            (EM.value * Math.sin(XMAM.value) * (1. +
            (EM.value * Math.cos(XMAM.value))));

        for (int I = 1; I <= 10; I++) {
            SINE = Math.sin(ZC2);
            COSE = Math.cos(ZC2);
            ZC5 = 1. / (1. - (EM.value * COSE));
            CAPE = (((XMAM.value + (EM.value * SINE)) - ZC2) * ZC5) + ZC2;

            if (Math.abs(CAPE - ZC2) <= C1.E6A) {
                break;
            }

            ZC2 = CAPE;
        }

        // SHORT PERIOD PRELIMINARY QUANTITIES
        AM = Math.pow((C1.XKE / XN.value), C1.TOTHRD);
        BETA2M = 1. - (EM.value * EM.value);
        SINOS = Math.sin(OMGASM.value);
        COSOS = Math.cos(OMGASM.value);
        AXNM = EM.value * COSOS;
        AYNM = EM.value * SINOS;
        PM = AM * BETA2M;
        G1 = 1. / PM;
        G2 = .5 * C1.CK2 * G1;
        G3 = G2 * G1;
        BETA = Math.sqrt(BETA2M);
        G4 = .25 * A3COF * SINI;
        G5 = .25 * A3COF * G1;
        SNF = BETA * SINE * ZC5;
        CSF = (COSE - EM.value) * ZC5;
        FM = MathUtils.ACTAN(SNF, CSF);
        SNFG = (SNF * COSOS) + (CSF * SINOS);
        CSFG = (CSF * COSOS) - (SNF * SINOS);
        SN2F2G = 2. * SNFG * CSFG;
        CS2F2G = (2. * Math.pow(CSFG, 2)) - 1.;
        ECOSF = EM.value * CSF;
        G10 = FM - XMAM.value + (EM.value * SNF);
        RM = PM / (1. + ECOSF);
        AOVR = AM / RM;
        G13 = XN.value * AOVR;
        G14 = -G13 * AOVR;
        DR = (G2 * ((UNMTH2 * CS2F2G) - (3. * TTHMUN))) - (G4 * SNFG);
        DIWC = (3. * G3 * SINI * CS2F2G) - (G5 * AYNM);
        DI = DIWC * COSI;
        SINI2 = Math.sin(.5 * XINC.value);

        // UPDATE FOR SHORT PERIOD PERIODICS
        SNI2DU = (SINIO2 * ((G3 * ((.5 * (1. - (7. * THETA2)) * SN2F2G) -
            (3. * UNM5TH * G10))) - (G5 * SINI * CSFG * (2. + ECOSF)))) -
            ((.5 * G5 * THETA2 * AXNM) / COSIO2);
        XLAMB = FM + OMGASM.value + XNODES.value +
            (G3 * ((.5 * ((1. + (6. * COSI)) - (7. * THETA2)) * SN2F2G) -
            (3. * (UNM5TH + (2. * COSI)) * G10))) +
            (G5 * SINI * (((COSI * AXNM) / (1. + COSI)) -
            ((2. + ECOSF) * CSFG)));
        Y4 = (SINI2 * SNFG) + (CSFG * SNI2DU) + (.5 * SNFG * COSIO2 * DI);
        Y5 = (SINI2 * CSFG) - (SNFG * SNI2DU) + (.5 * CSFG * COSIO2 * DI);
        R = RM + DR;
        RDOT = ((XN.value * AM * EM.value * SNF) / BETA) +
            (G14 * ((2. * G2 * UNMTH2 * SN2F2G) + (G4 * CSFG)));
        RVDOT = ((XN.value * Math.pow(AM, 2) * BETA) / RM) + (G14 * DR) +
            (AM * G13 * SINI * DIWC);

        // ORIENTATION VECTORS
        SNLAMB = Math.sin(XLAMB);
        CSLAMB = Math.cos(XLAMB);
        TEMP = 2. * ((Y5 * SNLAMB) - (Y4 * CSLAMB));
        UX = (Y4 * TEMP) + CSLAMB;
        VX = (Y5 * TEMP) - SNLAMB;
        TEMP = 2. * ((Y5 * CSLAMB) + (Y4 * SNLAMB));
        UY = (-Y4 * TEMP) + SNLAMB;
        VY = (-Y5 * TEMP) + CSLAMB;
        TEMP = 2. * Math.sqrt(1. - (Y4 * Y4) - (Y5 * Y5));
        UZ = Y4 * TEMP;
        VZ = Y5 * TEMP;

        // POSITION AND VELOCITY
        OrbitalState sv = new OrbitalState();

        sv.TSINCE = TSINCE;
        sv.X = R * UX;
        sv.Y = R * UY;
        sv.Z = R * UZ;
        sv.XDOT = (RDOT * UX) + (RVDOT * VX);
        sv.YDOT = (RDOT * UY) + (RVDOT * VY);
        sv.ZDOT = (RDOT * UZ) + (RVDOT * VZ);

        return sv;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "SDP8";
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
