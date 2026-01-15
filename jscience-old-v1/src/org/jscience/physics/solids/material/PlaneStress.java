/*
 * Created on Feb 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids.material;

import org.jdom.Element;
import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.physics.solids.AtlasMaterial;
import org.jscience.physics.solids.AtlasModel;
import org.jscience.physics.solids.AtlasObject;
import org.jscience.physics.solids.geom.AtlasCoordSys;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PlaneStress extends AtlasMaterial {

    protected static String TYPE = "Plane Stress Material";

    private DoubleMatrix D = null;
    private double E;
    private double G;
    private double nu;
    private AtlasCoordSys cs = AtlasCoordSys.GLOBAL;

    private boolean isotropic;

    public PlaneStress(String id, double e, double g, double n, boolean iso) {
        setId(id);
        this.E = e;
        this.G = g;
        this.nu = n;
        this.isotropic = iso;
    }

    public PlaneStress(String id, double e, double n) {
        setId(id);
        this.E = e;
        this.nu = n;
        this.G = E / (2.0 * (1.0 + nu));
        this.isotropic = true;
    }

    public DoubleMatrix getMatrix() {
        if (D == null) {
            D = new DoubleMatrix(3, 3);
            double mc = E / (1.0 - nu * nu);
            D.setElement(0, 0, mc);
            D.setElement(0, 1, mc * nu);
            D.setElement(0, 2, 0.0);
            D.setElement(1, 0, mc * nu);
            D.setElement(1, 1, mc);
            D.setElement(1, 2, 0.0);
            D.setElement(2, 0, 0.0);
            D.setElement(2, 1, 0.0);
            D.setElement(2, 2, mc * (1.0 - nu) / 2.0);
        }
        return D;
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

    /**
     * @return Returns the g.
     */
    public double getG() {
        return G;
    }

    /**
     * @param g The g to set.
     */
    public void setG(double g) {
        G = g;
    }

    /**
     * @return Returns the isotropic.
     */
    public boolean isIsotropic() {
        return isotropic;
    }

    /**
     * @param isotropic The isotropic to set.
     */
    public void setIsotropic(boolean isotropic) {
        this.isotropic = isotropic;
    }

    /**
     * @return Returns the nu.
     */
    public double getNu() {
        return nu;
    }

    /**
     * @param nu The nu to set.
     */
    public void setNu(double nu) {
        this.nu = nu;
    }

    public String toString() {
        String ret = getType() + " " + getId() + " :  ";
        ret = ret + " Modulus: " + E + "\n";
        ret = ret + " Shear Modulus: " + G + "\n";
        ret = ret + " Poisson's Ratio: " + nu + "\n";
        ret = ret + " Isotropic: " + isotropic;
        return ret;

    }


    /**
     * Marshalls object to XML.
     */
    public Element loadJDOMElement() {

        Element ret = new Element(this.getClass().getName());
        ret.setAttribute("Id", this.getId());

        ret.setAttribute("E", String.valueOf(E));
        ret.setAttribute("G", String.valueOf(G));
        ret.setAttribute("nu", String.valueOf(nu));
        ret.setAttribute("Isotropic", String.valueOf(isotropic));
        return ret;
    }

    public static AtlasObject unloadJDOMElement(AtlasModel parent, Element e) {

        String id = e.getAttributeValue("Id");

        double _e = Double.valueOf(e.getAttributeValue("E")).doubleValue();
        double _g = Double.valueOf(e.getAttributeValue("G")).doubleValue();
        double _nu = Double.valueOf(e.getAttributeValue("nu")).doubleValue();
        boolean _iso = Boolean.valueOf(e.getAttributeValue("Isotropic")).booleanValue();

        return new PlaneStress(id, _e, _g, _nu, _iso);
    }
}
