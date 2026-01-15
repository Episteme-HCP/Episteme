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
 * Implementation of NORAD's SGP8 near-Earth propagator.
 */
public class SGP8 extends AbstractPropagator {
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
    private double AO;

    /**
     * DOCUMENT ME!
     */
    private double DELO;

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
    private int ISIMP;

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
    private double D5;

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
    private double c0;

    /**
     * DOCUMENT ME!
     */
    private double c1;

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
    private double XNDT;

    /**
     * DOCUMENT ME!
     */
    private double XNDTN;

    /**
     * DOCUMENT ME!
     */
    private double D6;

    /**
     * DOCUMENT ME!
     */
    private double D7;

    /**
     * DOCUMENT ME!
     */
    private double D8;

    /**
     * DOCUMENT ME!
     */
    private double C8;

    /**
     * DOCUMENT ME!
     */
    private double C9;

    /**
     * DOCUMENT ME!
     */
    private double EDOT;

    /**
     * DOCUMENT ME!
     */
    private double D20;

    /**
     * DOCUMENT ME!
     */
    private double ALDTAL;

    /**
     * DOCUMENT ME!
     */
    private double TSDTTS;

    /**
     * DOCUMENT ME!
     */
    private double ETDT;

    /**
     * DOCUMENT ME!
     */
    private double PSDTPS;

    /**
     * DOCUMENT ME!
     */
    private double SIN2G;

    /**
     * DOCUMENT ME!
     */
    private double C0DTC0;

    /**
     * DOCUMENT ME!
     */
    private double C1DTC1;

    /**
     * DOCUMENT ME!
     */
    private double D9;

    /**
     * DOCUMENT ME!
     */
    private double D10;

    /**
     * DOCUMENT ME!
     */
    private double D11;

    /**
     * DOCUMENT ME!
     */
    private double D12;

    /**
     * DOCUMENT ME!
     */
    private double D13;

    /**
     * DOCUMENT ME!
     */
    private double D14;

    /**
     * DOCUMENT ME!
     */
    private double D15;

    /**
     * DOCUMENT ME!
     */
    private double D1DT;

    /**
     * DOCUMENT ME!
     */
    private double D2DT;

    /**
     * DOCUMENT ME!
     */
    private double D3DT;

    /**
     * DOCUMENT ME!
     */
    private double D4DT;

    /**
     * DOCUMENT ME!
     */
    private double D5DT;

    /**
     * DOCUMENT ME!
     */
    private double C4DT;

    /**
     * DOCUMENT ME!
     */
    private double C5DT;

    /**
     * DOCUMENT ME!
     */
    private double D16;

    /**
     * DOCUMENT ME!
     */
    private double XNDDT;

    /**
     * DOCUMENT ME!
     */
    private double EDDOT;

    /**
     * DOCUMENT ME!
     */
    private double D25;

    /**
     * DOCUMENT ME!
     */
    private double D17;

    /**
     * DOCUMENT ME!
     */
    private double TSDDTS;

    /**
     * DOCUMENT ME!
     */
    private double ETDDT;

    /**
     * DOCUMENT ME!
     */
    private double D18;

    /**
     * DOCUMENT ME!
     */
    private double D19;

    /**
     * DOCUMENT ME!
     */
    private double D23;

    /**
     * DOCUMENT ME!
     */
    private double D1DDT;

    /**
     * DOCUMENT ME!
     */
    private double XNTRDT;

    /**
     * DOCUMENT ME!
     */
    private double TMNDDT;

    /**
     * DOCUMENT ME!
     */
    private double PP;

    /**
     * DOCUMENT ME!
     */
    private double GAMMA;

    /**
     * DOCUMENT ME!
     */
    private double XND;

    /**
     * DOCUMENT ME!
     */
    private double QQ;

    /**
     * DOCUMENT ME!
     */
    private double ED;

    /**
     * DOCUMENT ME!
     */
    private double OVGPP;

    /**
     * DOCUMENT ME!
     */
    private double XMAM;

    /**
     * DOCUMENT ME!
     */
    private double OMGASM;

    /**
     * DOCUMENT ME!
     */
    private double XNODES;

    /**
     * DOCUMENT ME!
     */
    private double TEMP1;

    /**
     * DOCUMENT ME!
     */
    private double XN;

    /**
     * DOCUMENT ME!
     */
    private double EM;

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
    public SGP8(ElementSet es) {
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
        ISIMP = 0;
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
        c0 = (.5 * B * RHO * C1.QOMS2T * XNODP * AODP * Math.pow(TSI, 4) * Math.pow(PSIM2,
                3.5)) / Math.sqrt(ALPHA2);
        c1 = 1.5 * XNODP * Math.pow(ALPHA2, 2) * c0;
        c4 = D1 * D3 * B2;
        c5 = D5 * D4 * B3;
        XNDT = c1 * ((2. + (ETA2 * (3. + (34. * EOSQ))) +
            (5. * EETA * (4. + ETA2)) + (8.5 * EOSQ)) + (D1 * D2 * B1) +
            (c4 * COS2G) + (c5 * SING));
        XNDTN = XNDT / XNODP;

        // IF DRAG IS VERY SMALL, THE ISIMP FLAG IS SET AND THE
        // EQUATIONS ARE TRUNCATED TO LINEAR VARIATION IN MEAN
        // MOTION AND QUADRATIC VARIATION IN MEAN ANOMALY
        if (Math.abs(XNDTN * C1.XMNPDA) >= 2.16E-3) {
            D6 = ETA * (30. + (22.5 * ETA2));
            D7 = ETA * (5. + (12.5 * ETA2));
            D8 = 1. + (ETA2 * (6.75 + ETA2));
            C8 = D1 * D7 * B2;
            C9 = D5 * D8 * B3;
            EDOT = -c0 * ((ETA * (4. + ETA2 + (EOSQ * (15.5 + (7. * ETA2))))) +
                (es.EO * (5. + (15. * ETA2))) + (D1 * D6 * B1) + (C8 * COS2G) +
                (C9 * SING));
            D20 = .5 * C1.TOTHRD * XNDTN;
            ALDTAL = (es.EO * EDOT) / ALPHA2;
            TSDTTS = 2. * AODP * TSI * ((D20 * BETAO2) + (es.EO * EDOT));
            ETDT = (EDOT + (es.EO * TSDTTS)) * TSI * C1.S;
            PSDTPS = -ETA * ETDT * PSIM2;
            SIN2G = 2. * SING * COSG;
            C0DTC0 = (D20 + (4. * TSDTTS)) - ALDTAL - (7. * PSDTPS);
            C1DTC1 = XNDTN + (4. * ALDTAL) + C0DTC0;
            D9 = (ETA * (6. + (68. * EOSQ))) + (es.EO * (20. + (15. * ETA2)));
            D10 = (5. * ETA * (4. + ETA2)) + (es.EO * (17. + (68. * ETA2)));
            D11 = ETA * (72. + (18. * ETA2));
            D12 = ETA * (30. + (10. * ETA2));
            D13 = 5. + (11.25 * ETA2);
            D14 = TSDTTS - (2. * PSDTPS);
            D15 = 2. * (D20 + ((es.EO * EDOT) / BETAO2));
            D1DT = D1 * (D14 + D15);
            D2DT = ETDT * D11;
            D3DT = ETDT * D12;
            D4DT = ETDT * D13;
            D5DT = D5 * D14;
            C4DT = B2 * ((D1DT * D3) + (D1 * D3DT));
            C5DT = B3 * ((D5DT * D4) + (D5 * D4DT));
            D16 = (D9 * ETDT) + (D10 * EDOT) +
                (B1 * ((D1DT * D2) + (D1 * D2DT))) + (C4DT * COS2G) +
                (C5DT * SING) + (XGDT1 * ((c5 * COSG) - (2. * c4 * SIN2G)));
            XNDDT = (C1DTC1 * XNDT) + (c1 * D16);
            EDDOT = (C0DTC0 * EDOT) -
                (c0 * (((4. + (3. * ETA2) + (30. * EETA) +
                (EOSQ * (15.5 + (21. * ETA2)))) * ETDT) +
                ((5. + (15. * ETA2) + (EETA * (31. + (14. * ETA2)))) * EDOT) +
                (B1 * ((D1DT * D6) + (D1 * ETDT * (30. + (67.5 * ETA2))))) +
                (B2 * ((D1DT * D7) + (D1 * ETDT * (5. + (37.5 * ETA2)))) * COS2G) +
                (B3 * ((D5DT * D8) + (D5 * ETDT * ETA * (13.5 + (4. * ETA2)))) * SING) +
                (XGDT1 * ((C9 * COSG) - (2. * C8 * SIN2G)))));
            D25 = Math.pow(EDOT, 2);
            D17 = (XNDDT / XNODP) - Math.pow(XNDTN, 2);
            TSDDTS = (2. * TSDTTS * (TSDTTS - D20)) +
                (AODP * TSI * ((C1.TOTHRD * BETAO2 * D17) -
                (4. * D20 * es.EO * EDOT) + (2. * (D25 + (es.EO * EDDOT)))));
            ETDDT = ((EDDOT + (2. * EDOT * TSDTTS)) * TSI * C1.S) +
                (TSDDTS * ETA);
            D18 = TSDDTS - Math.pow(TSDTTS, 2);
            D19 = (-Math.pow(PSDTPS, 2) / ETA2) - (ETA * ETDDT * PSIM2) -
                Math.pow(PSDTPS, 2);
            D23 = ETDT * ETDT;
            D1DDT = (D1DT * (D14 + D15)) +
                (D1 * (D18 - (2. * D19) + (C1.TOTHRD * D17) +
                ((2. * (((ALPHA2 * D25) / BETAO2) + (es.EO * EDDOT))) / BETAO2)));
            XNTRDT = (XNDT * ((((2. * C1.TOTHRD * D17) +
                ((3. * (D25 + (es.EO * EDDOT))) / ALPHA2)) -
                (6. * Math.pow(ALDTAL, 2)) + (4. * D18)) - (7. * D19))) +
                (C1DTC1 * XNDDT) +
                (c1 * ((C1DTC1 * D16) + (D9 * ETDDT) + (D10 * EDDOT) +
                (D23 * (6. + (30. * EETA) + (68. * EOSQ))) +
                (ETDT * EDOT * (40. + (30. * ETA2) + (272. * EETA))) +
                (D25 * (17. + (68. * ETA2))) +
                (B1 * ((D1DDT * D2) + (2. * D1DT * D2DT) +
                (D1 * ((ETDDT * D11) + (D23 * (72. + (54. * ETA2))))))) +
                (B2 * ((D1DDT * D3) + (2. * D1DT * D3DT) +
                (D1 * ((ETDDT * D12) + (D23 * (30. + (30. * ETA2)))))) * COS2G) +
                (B3 * ((((D5DT * D14) + (D5 * (D18 - (2. * D19)))) * D4) +
                (2. * D4DT * D5DT) +
                (D5 * ((ETDDT * D13) + (22.5 * ETA * D23)))) * SING) +
                (XGDT1 * ((((7. * D20) + ((4. * es.EO * EDOT) / BETAO2)) * ((c5 * COSG) -
                (2. * c4 * SIN2G))) +
                (((2. * C5DT * COSG) - (4. * C4DT * SIN2G)) -
                (XGDT1 * ((c5 * SING) + (4. * c4 * COS2G))))))));
            TMNDDT = XNDDT * 1.E9;
            TEMP = Math.pow(TMNDDT, 2) - (XNDT * 1.E18 * XNTRDT);
            PP = (TEMP + Math.pow(TMNDDT, 2)) / TEMP;
            GAMMA = -XNTRDT / (XNDDT * (PP - 2.));
            XND = XNDT / (PP * GAMMA);
            QQ = 1. - (EDDOT / (EDOT * GAMMA));
            ED = EDOT / (QQ * GAMMA);
            OVGPP = 1. / (GAMMA * (PP + 1.));
        } else {
            ISIMP = 1;
            EDOT = -C1.TOTHRD * XNDTN * (1. - es.EO);
        }
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
        XMAM = MathUtils.FMOD2P(es.XMO + (XLLDOT * TSINCE));
        OMGASM = es.OMEGAO + (OMGDT * TSINCE);
        XNODES = es.XNODEO + (XNODOT * TSINCE);

        if (ISIMP != 1) {
            TEMP = 1. - (GAMMA * TSINCE);
            TEMP1 = Math.pow(TEMP, PP);
            XN = XNODP + (XND * (1. - TEMP1));
            EM = es.EO + (ED * (1. - Math.pow(TEMP, QQ)));
            Z1 = XND * (TSINCE + (OVGPP * ((TEMP * TEMP1) - 1.)));
        } else {
            XN = XNODP + (XNDT * TSINCE);
            EM = es.EO + (EDOT * TSINCE);
            Z1 = .5 * XNDT * TSINCE * TSINCE;
        }

        Z7 = (3.5 * C1.TOTHRD * Z1) / XNODP;
        XMAM = MathUtils.FMOD2P(XMAM + Z1 + (Z7 * XMDT1));
        OMGASM = OMGASM + (Z7 * XGDT1);
        XNODES = XNODES + (Z7 * XHDT1);

        // SOLVE KEPLERS EQUATION
        ZC2 = XMAM + (EM * Math.sin(XMAM) * (1. + (EM * Math.cos(XMAM))));

        for (int I = 1; I <= 10; I++) {
            SINE = Math.sin(ZC2);
            COSE = Math.cos(ZC2);
            ZC5 = 1. / (1. - (EM * COSE));
            CAPE = (((XMAM + (EM * SINE)) - ZC2) * ZC5) + ZC2;

            if (Math.abs(CAPE - ZC2) <= C1.E6A) {
                break;
            }

            ZC2 = CAPE;
        }

        // SHORT PERIOD PRELIMINARY QUANTITIES
        AM = Math.pow((C1.XKE / XN), C1.TOTHRD);
        BETA2M = 1. - (EM * EM);
        SINOS = Math.sin(OMGASM);
        COSOS = Math.cos(OMGASM);
        AXNM = EM * COSOS;
        AYNM = EM * SINOS;
        PM = AM * BETA2M;
        G1 = 1. / PM;
        G2 = .5 * C1.CK2 * G1;
        G3 = G2 * G1;
        BETA = Math.sqrt(BETA2M);
        G4 = .25 * A3COF * SINI;
        G5 = .25 * A3COF * G1;
        SNF = BETA * SINE * ZC5;
        CSF = (COSE - EM) * ZC5;
        FM = MathUtils.ACTAN(SNF, CSF);
        SNFG = (SNF * COSOS) + (CSF * SINOS);
        CSFG = (CSF * COSOS) - (SNF * SINOS);
        SN2F2G = 2. * SNFG * CSFG;
        CS2F2G = (2. * Math.pow(CSFG, 2)) - 1.;
        ECOSF = EM * CSF;
        G10 = FM - XMAM + (EM * SNF);
        RM = PM / (1. + ECOSF);
        AOVR = AM / RM;
        G13 = XN * AOVR;
        G14 = -G13 * AOVR;
        DR = (G2 * ((UNMTH2 * CS2F2G) - (3. * TTHMUN))) - (G4 * SNFG);
        DIWC = (3. * G3 * SINI * CS2F2G) - (G5 * AYNM);
        DI = DIWC * COSI;

        // UPDATE FOR SHORT PERIOD PERIODICS
        SNI2DU = (SINIO2 * ((G3 * ((.5 * (1. - (7. * THETA2)) * SN2F2G) -
            (3. * UNM5TH * G10))) - (G5 * SINI * CSFG * (2. + ECOSF)))) -
            ((.5 * G5 * THETA2 * AXNM) / COSIO2);
        XLAMB = FM + OMGASM + XNODES +
            (G3 * ((.5 * ((1. + (6. * COSI)) - (7. * THETA2)) * SN2F2G) -
            (3. * (UNM5TH + (2. * COSI)) * G10))) +
            (G5 * SINI * (((COSI * AXNM) / (1. + COSI)) -
            ((2. + ECOSF) * CSFG)));
        Y4 = (SINIO2 * SNFG) + (CSFG * SNI2DU) + (.5 * SNFG * COSIO2 * DI);
        Y5 = (SINIO2 * CSFG) - (SNFG * SNI2DU) + (.5 * CSFG * COSIO2 * DI);
        R = RM + DR;
        RDOT = ((XN * AM * EM * SNF) / BETA) +
            (G14 * ((2. * G2 * UNMTH2 * SN2F2G) + (G4 * CSFG)));
        RVDOT = ((XN * Math.pow(AM, 2) * BETA) / RM) + (G14 * DR) +
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
        return "SGP8";
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
