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
