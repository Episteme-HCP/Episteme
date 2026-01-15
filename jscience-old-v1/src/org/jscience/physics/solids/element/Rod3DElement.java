/*
 * BasicRodElement.java
 *
 * Created on December 30, 2004, 1:46 AM
 */
package org.jscience.physics.solids.element;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;

import org.jscience.physics.solids.AtlasNode;
import org.jscience.physics.solids.CurveElement;
import org.jscience.physics.solids.SolutionMatrices;
import org.jscience.physics.solids.geom.AtlasCoordSys;
import org.jscience.physics.solids.load.DistLoad;

import java.util.ArrayList;


//import Jama.Matrix;
/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class Rod3DElement extends CurveElement {
    /**
     * DOCUMENT ME!
     */
    protected String TYPE = "Basic Rod Element";

    /**
     * DOCUMENT ME!
     */
    private double area;

    /**
     * DOCUMENT ME!
     */
    private double modulus;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix Ke = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix T = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix C = null;

    /**
     * DOCUMENT ME!
     */
    private ArrayList results = null;

/**
     * Full constructor of a BasicRodElement
     */
    public Rod3DElement(String id, AtlasNode endA, AtlasNode endB, double area,
        double modulus) {
        setId(id);
        setNodeA(endA);
        setNodeB(endB);
        setArea(area);
        setModulus(modulus);
    }

    /**
     * Sets the cross-sectional area of the element.
     *
     * @param area DOCUMENT ME!
     */
    public void setArea(double area) {
        this.area = area;
    }

    /**
     * Returns the cross-sectional area of the element.
     *
     * @return DOCUMENT ME!
     */
    public double getArea() {
        return area;
    }

    /**
     * Sets the material modulus of the element.
     *
     * @param modulus DOCUMENT ME!
     */
    public void setModulus(double modulus) {
        this.modulus = modulus;
    }

    /**
     * Returns the material modulus of the element.
     *
     * @return DOCUMENT ME!
     */
    public double getModulus() {
        return modulus;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void contributeMatrices(SolutionMatrices m) {
        this.contributeStiffness(m);

        //this.contributeMass(m);
        //this.contributeDamping(m);
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void contributeStiffness(SolutionMatrices m) {
        getKe();
        getT();

        int k1;
        int k2;
        int k3;
        int k4;

        DoubleMatrix K = (DoubleMatrix) (((DoubleMatrix) T.transpose()).multiply(Ke)).multiply(T);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                //m.addStiffness(this.getEndA(),1,K.getPrimitiveElement(i,j));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dl DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public void contributeDistLoad(DistLoad dl, SolutionMatrices m) {
    }

    /**
     * Returns element local coordinate system.  X axis is from endA to
     * endB, and Y axis is arbitrarily taken as a non-colinear vector.
     *
     * @return DOCUMENT ME!
     */
    public org.jscience.physics.solids.geom.AtlasCoordSys getLocalCoordSys() {
        return AtlasCoordSys.GLOBAL;
    }

    /**
     * Returns "Basic Rod Element".
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Volume of rod is cross-sectional area times length of element.
     *
     * @return DOCUMENT ME!
     */
    public double computeElementVolume() {
        double A = this.getArea();
        double h = this.computeLength();

        return A * h;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private DoubleMatrix getKe() {
        if (Ke == null) {
            Ke = new DoubleMatrix(4, 4);

            double length = this.computeLength();
            double val = (getArea() * getModulus()) / length;
            Ke.setElement(0, 0, val);
            Ke.setElement(0, 1, 0.0);
            Ke.setElement(0, 2, -val);
            Ke.setElement(0, 3, 0.0);
            Ke.setElement(1, 0, 0.0);
            Ke.setElement(1, 1, 0.0);
            Ke.setElement(1, 2, 0.0);
            Ke.setElement(1, 3, 0.0);
            Ke.setElement(2, 0, -val);
            Ke.setElement(2, 1, 0.0);
            Ke.setElement(2, 2, val);
            Ke.setElement(2, 3, 0.0);
            Ke.setElement(3, 0, 0.0);
            Ke.setElement(3, 1, 0.0);
            Ke.setElement(3, 2, 0.0);
            Ke.setElement(3, 3, 0.0);
        }

        return Ke;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private DoubleMatrix getT() {
        if (T == null) {
            T = new DoubleMatrix(4, 4);

            double alpha;
            double[] coordA = this.getEndA().getGlobalPosition();
            double[] coordB = this.getEndB().getGlobalPosition();
            double vx;
            double vy;
            double vz;
            vx = coordB[0] - coordA[0];
            vy = coordB[1] - coordA[2];
            vz = coordB[2] - coordA[3];

            alpha = Math.atan((coordB[1] - coordA[1]));

            double c = Math.cos(alpha);
            double s = Math.sin(alpha);
            T.setElement(0, 0, c);
            T.setElement(0, 1, s);
            T.setElement(0, 2, 0.0);
            T.setElement(0, 3, 0.0);
            T.setElement(1, 0, -s);
            T.setElement(1, 1, c);
            T.setElement(1, 2, 0.0);
            T.setElement(1, 3, 0.0);
            T.setElement(2, 0, 0.0);
            T.setElement(2, 1, 0.0);
            T.setElement(2, 2, c);
            T.setElement(2, 3, s);
            T.setElement(3, 0, 0.0);
            T.setElement(3, 1, 0.0);
            T.setElement(3, 2, -s);
            T.setElement(3, 3, c);
        }

        return T;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void computeResults(SolutionMatrices m) {
        return;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getResults() {
        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ret = getType() + " " + getId() + " :  ";

        AtlasNode[] nodes = this.getNodes();
        ret = ret + " N1=" + nodes[0].getId() + " N2=" + nodes[1].getId() +
            " : ";

        ret = ret + "Area = " + this.getArea() + " : Modulus =  " +
            this.getModulus();

        return ret;
    }
}
