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
public class CircularSection extends AtlasSection {


    protected String TYPE = "Rectangular Section";

    private double rad;

    public CircularSection(double radius) {
        this.rad = radius;
        initialize();
    }

    private void initialize() {
        this.setArea(rad * rad * Math.PI);
        setIy(getArea() * rad * rad / 4.0);
        setIz(getIy());
        setJ(getIy() * 2.0);
        setShearCenter(new AtlasPosition(0.0, 0.0, 0.0));

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
        ret = ret + " Radius: " + rad;
        return ret;
    }


}
