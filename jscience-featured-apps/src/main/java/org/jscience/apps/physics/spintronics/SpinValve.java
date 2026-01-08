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
    private SpintronicMaterial spacerMaterial;
    private Real spacerThickness;
    private final FerromagneticLayer freeLayer;

    // SAF Extension
    private final boolean safEnabled;
    private final FerromagneticLayer safPinnedLayer2; // Reference layer (top of SAF)
    private final SpintronicMaterial safSpacer; // Ru or Ir
    private final Real safSpacerThickness;
    private final FerromagneticLayer safPinnedLayer1; // Bottom pinned layer

    public SpinValve(FerromagneticLayer pinnedLayer, SpintronicMaterial spacerMaterial, Real spacerThickness,
            FerromagneticLayer freeLayer) {
        this(pinnedLayer, null, null, null, spacerMaterial, spacerThickness, freeLayer);
    }

    public SpinValve(FerromagneticLayer pinned1, FerromagneticLayer pinned2, SpintronicMaterial safSpacerMat,
            Real safSpacerThick,
            SpintronicMaterial spacerMaterial, Real spacerThickness, FerromagneticLayer freeLayer) {
        if (pinned2 != null) {
            this.safEnabled = true;
            this.safPinnedLayer1 = pinned1;
            this.safPinnedLayer2 = pinned2;
            this.safSpacer = safSpacerMat;
            this.safSpacerThickness = safSpacerThick;
            this.pinnedLayer = pinned2; // The one interacting with free layer is the top one
        } else {
            this.safEnabled = false;
            this.safPinnedLayer1 = null;
            this.safPinnedLayer2 = null;
            this.safSpacer = null;
            this.safSpacerThickness = null;
            this.pinnedLayer = pinned1;
        }

        this.spacerMaterial = spacerMaterial;
        this.spacerThickness = spacerThickness;
        this.freeLayer = freeLayer;
    }

    public FerromagneticLayer getPinnedLayer() {
        return pinnedLayer;
    } // Effective pinned layer

    public boolean isSafEnabled() {
        return safEnabled;
    }

    public SpintronicMaterial getSpacerMaterial() {
        return spacerMaterial;
    }

    public void setSpacerMaterial(SpintronicMaterial m) {
        this.spacerMaterial = m;
    }

    public Real getSpacerThickness() {
        return spacerThickness;
    }

    public void setSpacerThickness(Real t) {
        this.spacerThickness = t;
    }

    public FerromagneticLayer getFreeLayer() {
        return freeLayer;
    }
}
