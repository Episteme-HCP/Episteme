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
    private Real area = Real.of(100e-9 * 100e-9); // Default 100nm x 100nm

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

    public FerromagneticLayer getSafPinnedLayer1() {
        return safPinnedLayer1;
    }

    public FerromagneticLayer getSafPinnedLayer2() {
        return safPinnedLayer2;
    }

    public SpintronicMaterial getSafSpacer() {
        return safSpacer;
    }

    public Real getSafSpacerThickness() {
        return safSpacerThickness;
    }

    public Real getArea() { return area; }
    public void setArea(Real area) { this.area = area; }
}
