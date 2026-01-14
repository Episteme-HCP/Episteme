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
import org.jscience.physics.solids.geom.AtlasPosition;
import org.jscience.physics.solids.load.DistLoad;

import java.text.DecimalFormat;

import java.util.ArrayList;


//import org.apache.log4j.Logger;

//import Jama.Matrix;
/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class Rod2DElement extends CurveElement {
    //static Logger AtlasLogger = Logger.getLogger((Rod2DElement.class).getName());
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
    private double length;

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
    private DoubleMatrix Ue = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix B = null;

    /**
     * DOCUMENT ME!
     */
    private ArrayList results = null;

    /**
     * DOCUMENT ME!
     */
    private AtlasPosition centroid;

    /**
     * DOCUMENT ME!
     */
    DecimalFormat efmt = new DecimalFormat("##.###E0");

/**
     * Full constructor of a BasicRodElement
     */
    public Rod2DElement(String id, AtlasNode endA, AtlasNode endB, double area,
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
        this.getKe();
        this.getT();

        int[] ij = new int[4];

        DoubleMatrix K = (DoubleMatrix) (((DoubleMatrix) T.transpose()).multiply(Ke)).multiply(T);
        DoubleMatrix Kg = m.getStiffnessMatrix();
        ij[0] = m.getDOFIndex(getEndA(), 1);
        ij[1] = m.getDOFIndex(getEndA(), 2);
        ij[2] = m.getDOFIndex(getEndB(), 1);
        ij[3] = m.getDOFIndex(getEndB(), 2);

        //AtlasLogger.debug(" Index 1: " + ij[0]);
        //AtlasLogger.debug(" Index 2: " + ij[1]);
        //AtlasLogger.debug(" Index 3: " + ij[2]);
        //AtlasLogger.debug(" Index 4: " + ij[3]);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double termIJ = Kg.getPrimitiveElement(ij[i], ij[j]);
                termIJ = termIJ + K.getPrimitiveElement(i, j);
                Kg.setElement(ij[i], ij[j], termIJ);
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
    public AtlasCoordSys getLocalCoordSys() {
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
            length = this.computeLength();

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

        //AtlasLogger.debug(" Element Stiffness: ");
        //AtlasLogger.debug(Ke.toString(efmt,12));
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
            double deltaX = (coordB[0] - coordA[0]);
            double deltaY = (coordB[1] - coordA[1]);

            if ((deltaY < 0.0) && (deltaX >= 0.0)) {
                alpha = Math.atan(deltaY / deltaX) + (Math.PI / 2.0);
            } else if ((deltaX < 0.0) && (deltaY < 0.0)) {
                alpha = Math.atan(deltaY / deltaX) + Math.PI;
            } else {
                alpha = Math.atan(deltaY / deltaX);
            }

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

        //AtlasLogger.debug(" Transformation Matrix: ");
        //AtlasLogger.debug(T.toString(efmt,12));
        return T;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void computeResults(SolutionMatrices m) {
        computeResults(m, 0.0, 0.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param s DOCUMENT ME!
     */
    public void computeResults(SolutionMatrices m, double r, double s) {
        int[] ij = new int[4];
        DoubleMatrix U = m.getDisplacementMatrix();

        if (Ue == null) {
            Ue = new DoubleMatrix(4, 1);
        }

        ij[0] = m.getDOFIndex(getEndA(), 1);
        ij[1] = m.getDOFIndex(getEndA(), 2);
        ij[2] = m.getDOFIndex(getEndB(), 1);
        ij[3] = m.getDOFIndex(getEndB(), 2);
        Ue.setElement(0, 0, U.getPrimitiveElement(ij[0], 0));
        Ue.setElement(1, 0, U.getPrimitiveElement(ij[1], 0));
        Ue.setElement(2, 0, U.getPrimitiveElement(ij[2], 0));
        Ue.setElement(3, 0, U.getPrimitiveElement(ij[3], 0));
        Ue = (DoubleMatrix) T.multiply(Ue);

        if (B == null) {
            B = new DoubleMatrix(1, 4);
        }

        B.setElement(0, 0, -1.0 / length);
        B.setElement(0, 1, 0.0);
        B.setElement(0, 2, 1.0 / length);
        B.setElement(0, 3, 0.0);

        DoubleMatrix eps = (DoubleMatrix) B.multiply(Ue);

        DoubleMatrix sig = (DoubleMatrix) eps.scalarMultiply(modulus);

        if (results == null) {
            results = new ArrayList();
        }

        // compute centroid

        //results.add(new StrainResult(eps));
        //results.add(new StressResult(sig));
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
