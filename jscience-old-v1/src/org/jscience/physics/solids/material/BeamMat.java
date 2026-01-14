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

/*
 * Created on Mar 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids.material;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.physics.solids.AtlasMaterial;
import org.jscience.physics.solids.geom.AtlasCoordSys;
//import Jama.Matrix;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class BeamMat extends AtlasMaterial {

    protected static String TYPE = "Beam Material";

    private DoubleMatrix D = null;
    private double E;
    private AtlasCoordSys cs = AtlasCoordSys.GLOBAL;

    public BeamMat(String ID, double mod) {
        this.setId(ID);
        this.E = mod;
    }

    public DoubleMatrix getMatrix() {
        if (D == null) {
            D = new DoubleMatrix(1, 1);
            D.setElement(0, 0, E);
        }
        return D;
    }

    public double getModulus() {
        return E;
    }

    public String getType() {
        return TYPE;
    }

    /**
     * @return Returns the cs.
     */
    public AtlasCoordSys getCoordSys() {
        return cs;
    }

    /**
     * @param cs The cs to set.
     */
    public void setCoordSys(AtlasCoordSys cs) {
        this.cs = cs;
    }

    /**
     * @return Returns the e.
     */
    public double getE() {
        return E;
    }

    /**
     * @param e The e to set.
     */
    public void setE(double e) {
        E = e;
    }


    public String toString() {
        String ret = getType() + " " + getId() + " :  ";
        ret = ret + " Modulus: " + E;
        return ret;

    }
}
