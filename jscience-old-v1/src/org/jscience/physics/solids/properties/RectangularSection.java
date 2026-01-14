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
package org.jscience.physics.solids.properties;

import org.jscience.physics.solids.AtlasSection;
import org.jscience.physics.solids.geom.AtlasPosition;
import org.jscience.physics.solids.result.StressResult;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class RectangularSection extends AtlasSection {


    protected String TYPE = "Rectangular Section";

    private double yL;
    private double zL;

    public RectangularSection(String ID, double yLen, double zLen) {
        this.setId(ID);
        this.yL = yLen;
        this.zL = zLen;
        initialize();
    }

    private void initialize() {
        this.setArea(yL * zL);
        this.srp.add(new AtlasPosition(yL / 2.0, zL / 2.0, 0.0));
        this.srp.add(new AtlasPosition(-yL / 2.0, zL / 2.0, 0.0));
        this.srp.add(new AtlasPosition(-yL / 2.0, -zL / 2.0, 0.0));
        this.srp.add(new AtlasPosition(yL / 2.0, -zL / 2.0, 0.0));
        this.setIy(yL * zL * zL * zL / 12.0);
        this.setIz(zL * yL * yL * yL / 12.0);
        this.setShearCenter(new AtlasPosition(0.0, 0.0, 0.0));
        //AtlasPosition[] cir = new AtlasPosition[4];
        //this.setOutLine(cir);

    }

    public StressResult[] computeSectionStress() {
        return new StressResult[0];
    }

    /**
     * Returns "Rectangular Section".
     */
    public String getType() {
        return TYPE;
    }

    public String toString() {

        String ret = getType() + " " + getId() + " :  ";
        ret = ret + " y Dim: " + yL + " z Dim: " + zL;
        return ret;
    }

}
