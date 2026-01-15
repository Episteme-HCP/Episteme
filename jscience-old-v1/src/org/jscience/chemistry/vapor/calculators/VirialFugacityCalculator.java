/*
 * Virial equation implementation for fugacity calculation.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.calculators;

import org.jscience.chemistry.vapor.ComponentData;
import org.jscience.chemistry.vapor.Constants;
import org.jscience.chemistry.vapor.VLEContext;


/**
 * Virial equation implementation for fugacity calculation.
 */
public class VirialFugacityCalculator implements IFugacityCalculator {
    /**
     * DOCUMENT ME!
     */
    private final static double VIR_COEF_OA = 0.083;

    /**
     * DOCUMENT ME!
     */
    private final static double VIR_COEF_OB = 0.422;

    /**
     * DOCUMENT ME!
     */
    private final static double VIR_COEF_OC = -1.6;

    /**
     * DOCUMENT ME!
     */
    private final static double VIR_COEF_IA = 0.139;

    /**
     * DOCUMENT ME!
     */
    private final static double VIR_COEF_IB = 0.172;

    /**
     * DOCUMENT ME!
     */
    private final static double VIR_COEF_IC = -4.2;

    /**
     * DOCUMENT ME!
     */
    private VLEContext context = null;

    /**
     * DOCUMENT ME!
     */
    private double[][] omega = null;

    /**
     * DOCUMENT ME!
     */
    private double[][] Tc = null;

    /**
     * DOCUMENT ME!
     */
    private double[][] Zc = null;

    /**
     * DOCUMENT ME!
     */
    private double[][] Pc = null;

    /**
     * DOCUMENT ME!
     */
    private double[][] Vc = null;

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void setContext(VLEContext context) {
        int i = 0;
        int j = 0;

        this.context = context;

        int numOfComps = context.getNumOfComps();
        ComponentData[] components = context.getComponents();

        omega = new double[numOfComps][numOfComps];
        Zc = new double[numOfComps][numOfComps];
        Tc = new double[numOfComps][numOfComps];
        Vc = new double[numOfComps][numOfComps];
        Pc = new double[numOfComps][numOfComps];

        for (i = 0; i < numOfComps; i++) {
            omega[i][i] = components[i].omega;
            Zc[i][i] = components[i].Zc;
            Tc[i][i] = components[i].Tc;
            Vc[i][i] = components[i].Vc;
            Pc[i][i] = components[i].Pc;
        }

        for (i = 0; i < numOfComps; i++) {
            for (j = 0; j < numOfComps; j++) {
                if (i != j) {
                    omega[i][j] = (omega[i][i] + omega[j][j]) / 2;
                    Zc[i][j] = (Zc[i][i] + Zc[j][j]) / 2;
                    Tc[i][j] = Math.sqrt(Tc[i][i] * Tc[j][j]);
                    Vc[i][j] = Math.exp(3 * Math.log(
                                (Math.exp(Math.log(Vc[i][i]) / 3) +
                                Math.exp(Math.log(Vc[j][j]) / 3)) / 2));
                    Pc[i][j] = (Constants.R_J_PER_KMOL_K * Zc[i][j] * Tc[i][j]) / Vc[i][j];
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param T DOCUMENT ME!
     * @param P DOCUMENT ME!
     * @param Psat DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] calculateFugacity(double T, double P, double[] Psat) {
        int i = 0;
        int j = 0;
        int k = 0;
        int numOfComps = context.getNumOfComps();

        double[] phi = new double[numOfComps];
        double[] vapMoleFracs = context.getVapourMoleFractions();

        double[][] bo = new double[numOfComps][numOfComps];
        double[][] bi = new double[numOfComps][numOfComps];
        double[][] cb = new double[numOfComps][numOfComps];
        double[][] del = new double[numOfComps][numOfComps];

        for (i = 0; i < numOfComps; i++) {
            for (j = 0; j < numOfComps; j++) {
                bo[i][j] = VIR_COEF_OA -
                    (VIR_COEF_OB / Math.pow((T + Constants.K_C_DIFF) / Tc[i][j],
                        VIR_COEF_OC));
                bi[i][j] = VIR_COEF_IA -
                    (VIR_COEF_IB / Math.pow((T + Constants.K_C_DIFF) / Tc[i][j],
                        VIR_COEF_IC));
                cb[i][j] = ((Constants.R_J_PER_KMOL_K * Tc[i][j]) / Pc[i][j]) * (bo[i][j] +
                    (omega[i][j] * bi[i][j]));
            }
        }

        for (i = 0; i < numOfComps; i++)
            for (j = 0; j < numOfComps; j++)
                del[i][j] = (2 * cb[i][j]) - cb[i][i] - cb[j][j];

        for (i = 0; i < numOfComps; i++) {
            double sum = 0;

            for (j = 0; j < numOfComps; j++) {
                for (k = 0; k < numOfComps; k++) {
                    sum += (vapMoleFracs[j] * vapMoleFracs[k] * ((2 * del[j][i]) -
                    del[j][k]));
                }
            }

            phi[i] = Math.exp(((cb[i][i] * (P - Psat[i])) + (P / 2 * sum)) / (Constants.R_J_PER_KMOL_K * (T +
                    Constants.K_C_DIFF)));
        }

        return phi;
    }
}
