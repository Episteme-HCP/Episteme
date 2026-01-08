/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Advanced Spin Transport and Spin Transfer Torque (STT) calculations.
 */
public class SpinTransport {

    /**
     * Calculates the adiabatic Spin Transfer Torque (STT) vector.
     * τ = (ℏ/2e) * η * J * (m_free × (m_pinned × m_free))
     * 
     * @param currentDensity Current density J (A/m²)
     * @param layerFree      Free layer
     * @param layerPinned    Pinned layer
     * @return Torque vector components
     */
    public static Real[] calculateSTT(Real currentDensity, FerromagneticLayer layerFree,
            FerromagneticLayer layerPinned) {
        Real hbar = PhysicalConstants.h_bar;
        Real e = PhysicalConstants.e;
        Real eta = layerPinned.getMaterial().getSpinPolarization(); // Efficiency

        Real factor = hbar.divide(Real.TWO.multiply(e)).multiply(eta).multiply(currentDensity);

        Real[] mF = layerFree.getMagnetization();
        Real[] mP = layerPinned.getMagnetization();

        // m_pinned x m_free
        Real[] cross1 = crossProduct(mP, mF);
        // m_free x (mP x mF)
        Real[] cross2 = crossProduct(mF, cross1);

        return new Real[] {
                cross2[0].multiply(factor),
                cross2[1].multiply(factor),
                cross2[2].multiply(factor)
        };
    }

    /**
     * Spin accumulation at the interface.
     * Δμ = 2 * P * ρ * λ_sf * J
     */
    public static Real spinAccumulation(Real currentDensity, SpintronicMaterial material) {
        return Real.TWO.multiply(material.getSpinPolarization())
                .multiply(material.getResistivity())
                .multiply(material.getSpinDiffusionLength())
                .multiply(currentDensity);
    }

    /**
     * Performs one time step of the LLG equation: dm/dt = ...
     * Returns the new magnetization vector.
     */
    public static Real[] stepLLG(FerromagneticLayer layer, Real[] hEff, Real dt, Real alpha, Real gamma) {
        Real[] m = layer.getMagnetization();

        // Precession: m x hEff
        Real[] precession = crossProduct(m, hEff);

        // Damping: m x (m x hEff)
        Real[] damping = crossProduct(m, precession);

        Real[] dm = new Real[3];
        for (int i = 0; i < 3; i++) {
            // dm/dt = -gamma/(1+alpha^2) * [ precession + alpha * damping ]
            Real term = gamma.divide(Real.ONE.add(alpha.pow(2)));
            dm[i] = term.multiply(precession[i].add(alpha.multiply(damping[i]))).multiply(dt);
        }

        return new Real[] {
                m[0].add(dm[0]),
                m[1].add(dm[1]),
                m[2].add(dm[2])
        };
    }

    private static Real[] crossProduct(Real[] a, Real[] b) {
        return new Real[] {
                a[1].multiply(b[2]).subtract(a[2].multiply(b[1])),
                a[2].multiply(b[0]).subtract(a[0].multiply(b[2])),
                a[0].multiply(b[1]).subtract(a[1].multiply(b[0]))
        };
    }
}
