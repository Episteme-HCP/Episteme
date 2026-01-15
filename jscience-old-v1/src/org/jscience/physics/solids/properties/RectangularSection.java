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
