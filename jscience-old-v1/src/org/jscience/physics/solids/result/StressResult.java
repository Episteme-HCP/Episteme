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
public class StressResult extends AtlasResult {

    protected static String TYPE = "Stress Result";

    private DoubleMatrix stress;
    private AtlasPosition loc;

    public StressResult() {
    }

    public StressResult(DoubleMatrix res, AtlasPosition locate) {
        this.stress = res;
        this.loc = locate;
    }


    public String getType() {
        return TYPE;
    }

    public DoubleMatrix getResult() {
        return stress;
    }

    public void setResult(DoubleMatrix res) {
        this.stress = res;
    }

    public String toString() {
        DecimalFormat efmt = new DecimalFormat("0.0000E0");
        double[] coord = this.loc.getGlobalPosition();
        String ret = getType() + " " + getId() + " : \n";
        ret = ret + " Position:  x: " + efmt.format(coord[0]) + "\n" +
                "            y: " + efmt.format(coord[1]) + "\n" +
                "            z: " + efmt.format(coord[2]) + "\n";
        ret = ret + " sigma  x: " + efmt.format(stress.getPrimitiveElement(0, 0)) + "\n";
        ret = ret + " sigma  y: " + efmt.format(stress.getPrimitiveElement(1, 0)) + "\n";
        ret = ret + " sigma  z: " + efmt.format(stress.getPrimitiveElement(2, 0)) + "\n";
        ret = ret + " sigma xy: " + efmt.format(stress.getPrimitiveElement(3, 0)) + "\n";
        ret = ret + " sigma xz: " + efmt.format(stress.getPrimitiveElement(4, 0)) + "\n";
        ret = ret + " sigma yz: " + efmt.format(stress.getPrimitiveElement(5, 0)) + "\n";

        return ret;
    }


}
