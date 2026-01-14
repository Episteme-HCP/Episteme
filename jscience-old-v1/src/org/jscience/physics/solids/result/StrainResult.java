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
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids.result;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.physics.solids.AtlasResult;
import org.jscience.physics.solids.geom.AtlasPosition;

import java.text.DecimalFormat;
//import Jama.Matrix;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class StrainResult extends AtlasResult {

    protected static String TYPE = "Strain Result";

    private DoubleMatrix strain;
    private AtlasPosition loc;

    public StrainResult() {
    }

    public StrainResult(DoubleMatrix res, AtlasPosition locate) {
        this.strain = res;
        this.loc = locate;
    }


    public String getType() {
        return TYPE;
    }

    public DoubleMatrix getResult() {
        return strain;
    }

    public void setResult(DoubleMatrix res) {
        this.strain = res;
    }

    public String toString() {
        DecimalFormat efmt = new DecimalFormat("0.0000E0");
        double[] coord = this.loc.getGlobalPosition();
        String ret = getType() + " " + getId() + " :  \n";
        ret = ret + " Position:  x: " + efmt.format(coord[0]) + "\n" +
                " y: " + efmt.format(coord[1]) + "\n" +
                " z: " + efmt.format(coord[2]) + "\n";
        ret = ret + "epsilon  x: " + efmt.format(strain.getPrimitiveElement(0, 0)) + "\n";
        ret = ret + "epsilon  y: " + efmt.format(strain.getPrimitiveElement(1, 0)) + "\n";
        ret = ret + "epsilon  z: " + efmt.format(strain.getPrimitiveElement(2, 0)) + "\n";
        ret = ret + "epsilon xy: " + efmt.format(strain.getPrimitiveElement(3, 0)) + "\n";
        ret = ret + "epsilon xz: " + efmt.format(strain.getPrimitiveElement(4, 0)) + "\n";
        ret = ret + "epsilon yz: " + efmt.format(strain.getPrimitiveElement(5, 0)) + "\n";

        return ret;
    }

    /**
     * @return Returns the loc.
     */
    public AtlasPosition getLoc() {
        return loc;
    }

    /**
     * @param loc The loc to set.
     */
    public void setLoc(AtlasPosition loc) {
        this.loc = loc;
    }
}
