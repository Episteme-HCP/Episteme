/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a high-precision Spin Valve structure.
 */
public class SpinValve {
    private final FerromagneticLayer pinnedLayer;
    private final SpintronicMaterial spacerMaterial;
    private final Real spacerThickness;
    private final FerromagneticLayer freeLayer;

    public SpinValve(FerromagneticLayer pinnedLayer, SpintronicMaterial spacerMaterial, Real spacerThickness,
            FerromagneticLayer freeLayer) {
        this.pinnedLayer = pinnedLayer;
        this.spacerMaterial = spacerMaterial;
        this.spacerThickness = spacerThickness;
        this.freeLayer = freeLayer;
    }

    public FerromagneticLayer getPinnedLayer() {
        return pinnedLayer;
    }

    public SpintronicMaterial getSpacerMaterial() {
        return spacerMaterial;
    }

    public Real getSpacerThickness() {
        return spacerThickness;
    }

    public FerromagneticLayer getFreeLayer() {
        return freeLayer;
    }
}
