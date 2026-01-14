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

import org.jdom.Element;

import org.jscience.mathematics.algebraic.matrices.DoubleDiagonalMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;

import org.jscience.physics.solids.*;
import org.jscience.physics.solids.geom.AtlasCoordSys;
import org.jscience.physics.solids.geom.AtlasPosition;
import org.jscience.physics.solids.load.Traction1D;
import org.jscience.physics.solids.result.StrainResult;
import org.jscience.physics.solids.result.StressResult;

import java.awt.*;

import java.text.DecimalFormat;

import java.util.ArrayList;

import javax.media.j3d.*;

import javax.vecmath.Color3f;


//import Jama.Matrix;
/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class Quad2DElement extends AreaElement {
    //static Logger AtlasLogger = Logger.getLogger((Quad2DElement.class).getName());
    /**
     * DOCUMENT ME!
     */
    protected String TYPE = "2D Quad Element";

    /**
     * DOCUMENT ME!
     */
    private double thickness;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix Ke = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix C = new DoubleMatrix(3, 3);

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix Ue = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix H = new DoubleMatrix(2, 8);

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix B = new DoubleMatrix(3, 8);

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix Jac = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix JacInv = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix Der = new DoubleMatrix(2, 4);

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix DerB = new DoubleMatrix(3, 8);

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix NC = new DoubleMatrix(4, 2);

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix eps = new DoubleMatrix(3, 1);

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix sig = new DoubleMatrix(3, 1);

    /**
     * DOCUMENT ME!
     */
    private int numGP;

    /**
     * DOCUMENT ME!
     */
    private GaussPoint GP;

    /**
     * DOCUMENT ME!
     */
    double[] coord = new double[3];

    /**
     * DOCUMENT ME!
     */
    private ArrayList tractions = null;

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
    DecimalFormat efmt = new DecimalFormat("0.0000E0");

/**
     * Full constructor of a 2DQuadElement
     */
    public Quad2DElement(String id, AtlasNode[] nodes, AtlasMaterial mater) {
        setId(id);
        setNodes(nodes);
        this.mat = mater;
        this.area = 0.0;

        for (int i = 0; i < nodeList.length; i++) {
            coord = nodeList[i].getGlobalPosition();
            NC.setElement(i, 0, coord[0]);
            NC.setElement(i, 1, coord[1]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @throws AtlasException DOCUMENT ME!
     */
    public void contributeMatrices(SolutionMatrices m)
        throws AtlasException {
        this.contributeStiffness(m);

        //this.contributeMass(m);
        //this.contributeDamping(m);
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @throws AtlasException DOCUMENT ME!
     */
    public void contributeStiffness(SolutionMatrices m)
        throws AtlasException {
        numGP = 3;
        this.GP = new GaussPoint(numGP);
        //AtlasLogger.debug(" Element ID: " + this.getId());
        this.getKe();

        int[] ij = new int[8];

        DoubleMatrix Kg = m.getStiffnessMatrix();
        ij[0] = m.getDOFIndex(nodeList[0], 1);
        ij[1] = m.getDOFIndex(nodeList[0], 2);
        ij[2] = m.getDOFIndex(nodeList[1], 1);
        ij[3] = m.getDOFIndex(nodeList[1], 2);
        ij[4] = m.getDOFIndex(nodeList[2], 1);
        ij[5] = m.getDOFIndex(nodeList[2], 2);
        ij[6] = m.getDOFIndex(nodeList[3], 1);
        ij[7] = m.getDOFIndex(nodeList[3], 2);

        //AtlasLogger.debug(" Index 1: " + ij[0]);
        //AtlasLogger.debug(" Index 2: " + ij[1]);
        //AtlasLogger.debug(" Index 3: " + ij[2]);
        //AtlasLogger.debug(" Index 4: " + ij[3]);
        //AtlasLogger.debug(" Index 5: " + ij[4]);
        //AtlasLogger.debug(" Index 6: " + ij[5]);
        //AtlasLogger.debug(" Index 7: " + ij[6]);
        //AtlasLogger.debug(" Index 8: " + ij[7]);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double termIJ = Kg.getPrimitiveElement(ij[i], ij[j]);
                termIJ = termIJ + Ke.getPrimitiveElement(i, j);
                Kg.setElement(ij[i], ij[j], termIJ);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tract DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public void contributeTractionLoad(Traction1D tract, SolutionMatrices m) {
        DoubleMatrix F = m.getForceMatrix();

        if (((tract.getNode1() == nodeList[0]) &&
                (tract.getNode2() == nodeList[1])) ||
                ((tract.getNode2() == nodeList[0]) &&
                (tract.getNode1() == nodeList[1]))) {
        }
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws AtlasException DOCUMENT ME!
     */
    private DoubleMatrix getKe() throws AtlasException {
        area = 0.0;

        double r;
        double s;
        double wtr;
        double wts;
        double wt;
        double detJ;

        if (Ke == null) {
            Ke = new DoubleMatrix(8, 8);
        }

        for (int i = 0; i < numGP; i++) {
            r = GP.getPoint(i);
            wtr = GP.getWeight(i);

            for (int j = 0; j < numGP; j++) {
                s = GP.getPoint(j);
                wts = GP.getWeight(j);
                detJ = getDetJ(r, s);
                wt = wtr * wts * detJ;
                area = area + wt;

                DoubleMatrix B = getB(r, s);
                C = mat.getMatrix();

                DoubleMatrix Kterm = ((DoubleMatrix) ((DoubleMatrix) ((DoubleMatrix) B.transpose()).multiply(C)).multiply(B));
                Ke = (DoubleMatrix) Ke.add(Kterm.scalarMultiply(wt));
            }
        }

        Ke = (DoubleMatrix) Ke.scalarMultiply(thickness);

        if (area <= 0.0) {
            throw new AtlasException(" Negative or zero area. Element " +
                this.getId());
        }

        //AtlasLogger.debug(" Element Stiffness: ");
        //AtlasLogger.debug(Ke.toString(efmt,12));
        return Ke;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private DoubleMatrix setH(double r, double s) {
        H.setElement(0, 0, 0.25 * (1.0 - r) * (1.0 - s));
        H.setElement(0, 1, 0.0);
        H.setElement(0, 2, 0.25 * (1.0 + r) * (1.0 - s));
        H.setElement(0, 3, 0.0);
        H.setElement(0, 4, 0.25 * (1.0 + r) * (1.0 + s));
        H.setElement(0, 5, 0.0);
        H.setElement(0, 6, 0.25 * (1.0 - r) * (1.0 + s));
        H.setElement(0, 7, 0.0);
        H.setElement(1, 0, 0.0);
        H.setElement(1, 1, 0.25 * (1.0 + r) * (1.0 - s));
        H.setElement(1, 2, 0.0);
        H.setElement(1, 3, 0.25 * (1.0 + r) * (1.0 + s));
        H.setElement(1, 4, 0.0);
        H.setElement(1, 5, 0.25 * (1.0 - r) * (1.0 + s));
        H.setElement(1, 6, 0.0);
        H.setElement(1, 7, 0.25 * (1.0 - r) * (1.0 - s));

        return H;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param s DOCUMENT ME!
     */
    private void setDer(double r, double s) {
        Der.setElement(0, 0, -0.25 * (1.0 - s));
        Der.setElement(0, 1, 0.25 * (1.0 - s));
        Der.setElement(0, 2, 0.25 * (1.0 + s));
        Der.setElement(0, 3, -0.25 * (1.0 + s));
        Der.setElement(1, 0, -0.25 * (1.0 - r));
        Der.setElement(1, 1, -0.25 * (1.0 + r));
        Der.setElement(1, 2, 0.25 * (1.0 + r));
        Der.setElement(1, 3, 0.25 * (1.0 - r));
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getDetJ(double r, double s) {
        setDer(r, s);
        Jac = (DoubleMatrix) Der.multiply(NC);
        JacInv = new LUDecomposition(Jac).solve(DoubleDiagonalMatrix.identity(
                    Jac.numRows()));

        return new LUDecomposition(Jac).det();
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private DoubleMatrix getB(double r, double s) {
        double detJ = getDetJ(r, s);
        DoubleMatrix dU = (DoubleMatrix) JacInv.multiply(Der);
        B.setElement(0, 0, dU.getPrimitiveElement(0, 0));
        B.setElement(0, 1, 0.0);
        B.setElement(0, 2, dU.getPrimitiveElement(0, 1));
        B.setElement(0, 3, 0.0);
        B.setElement(0, 4, dU.getPrimitiveElement(0, 2));
        B.setElement(0, 5, 0.0);
        B.setElement(0, 6, dU.getPrimitiveElement(0, 3));
        B.setElement(0, 7, 0.0);
        B.setElement(1, 0, 0.0);
        B.setElement(1, 1, dU.getPrimitiveElement(1, 0));
        B.setElement(1, 2, 0.0);
        B.setElement(1, 3, dU.getPrimitiveElement(1, 1));
        B.setElement(1, 4, 0.0);
        B.setElement(1, 5, dU.getPrimitiveElement(1, 2));
        B.setElement(1, 6, 0.0);
        B.setElement(1, 7, dU.getPrimitiveElement(1, 3));

        B.setElement(2, 0, dU.getPrimitiveElement(1, 0));
        B.setElement(2, 1, dU.getPrimitiveElement(0, 0));
        B.setElement(2, 2, dU.getPrimitiveElement(1, 1));
        B.setElement(2, 3, dU.getPrimitiveElement(0, 1));
        B.setElement(2, 4, dU.getPrimitiveElement(1, 2));
        B.setElement(2, 5, dU.getPrimitiveElement(0, 2));
        B.setElement(2, 6, dU.getPrimitiveElement(1, 3));
        B.setElement(2, 7, dU.getPrimitiveElement(0, 3));

        return B;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void computeResults(SolutionMatrices m) {
        int[] ij = new int[8];
        DoubleMatrix U = m.getDisplacementMatrix();

        if (Ue == null) {
            Ue = new DoubleMatrix(8, 1);
        }

        ij[0] = m.getDOFIndex(nodeList[0], 1);
        ij[1] = m.getDOFIndex(nodeList[0], 2);
        ij[2] = m.getDOFIndex(nodeList[1], 1);
        ij[3] = m.getDOFIndex(nodeList[1], 2);
        ij[4] = m.getDOFIndex(nodeList[2], 1);
        ij[5] = m.getDOFIndex(nodeList[2], 2);
        ij[6] = m.getDOFIndex(nodeList[3], 1);
        ij[7] = m.getDOFIndex(nodeList[3], 2);
        Ue.setElement(0, 0, U.getPrimitiveElement(ij[0], 0));
        Ue.setElement(1, 0, U.getPrimitiveElement(ij[1], 0));
        Ue.setElement(2, 0, U.getPrimitiveElement(ij[2], 0));
        Ue.setElement(3, 0, U.getPrimitiveElement(ij[3], 0));
        Ue.setElement(4, 0, U.getPrimitiveElement(ij[4], 0));
        Ue.setElement(5, 0, U.getPrimitiveElement(ij[5], 0));
        Ue.setElement(6, 0, U.getPrimitiveElement(ij[6], 0));
        Ue.setElement(7, 0, U.getPrimitiveElement(ij[7], 0));

        // Compute Strain
        for (int i = 0; i < numGP; i++) {
            double r = GP.getPoint(i);
            double wtr = GP.getWeight(i);

            for (int j = 0; j < numGP; j++) {
                double s = GP.getPoint(j);
                double wts = GP.getWeight(j);
                B = getB(r, s);

                double detJ = getDetJ(r, s);
                eps = (DoubleMatrix) eps.add((B.multiply(Ue)).scalarMultiply(
                            detJ * wtr * wts));
            }
        }

        eps = (DoubleMatrix) eps.scalarMultiply(1.0 / area);

        DoubleMatrix epsilon = new DoubleMatrix(6, 1);
        epsilon.setElement(0, 0, eps.getPrimitiveElement(0, 0));
        epsilon.setElement(1, 0, eps.getPrimitiveElement(1, 0));
        epsilon.setElement(2, 0, 0.0);
        epsilon.setElement(3, 0, eps.getPrimitiveElement(2, 0));
        epsilon.setElement(4, 0, 0.0);
        epsilon.setElement(5, 0, 0.0);

        sig = (DoubleMatrix) (mat.getMatrix()).multiply(eps);

        DoubleMatrix sigma = new DoubleMatrix(6, 1);
        sigma.setElement(0, 0, sig.getPrimitiveElement(0, 0));
        sigma.setElement(1, 0, sig.getPrimitiveElement(1, 0));
        sigma.setElement(2, 0, 0.0);
        sigma.setElement(3, 0, sig.getPrimitiveElement(2, 0));
        sigma.setElement(4, 0, 0.0);
        sigma.setElement(5, 0, 0.0);

        if (results == null) {
            results = new ArrayList();
        }

        // find centroid
        DoubleMatrix xy = new DoubleMatrix(8, 1);
        xy.setElement(0, 0, NC.getPrimitiveElement(0, 0));
        xy.setElement(1, 0, NC.getPrimitiveElement(0, 1));
        xy.setElement(2, 0, NC.getPrimitiveElement(1, 0));
        xy.setElement(3, 0, NC.getPrimitiveElement(1, 1));
        xy.setElement(4, 0, NC.getPrimitiveElement(2, 0));
        xy.setElement(5, 0, NC.getPrimitiveElement(2, 1));
        xy.setElement(6, 0, NC.getPrimitiveElement(3, 0));
        xy.setElement(7, 0, NC.getPrimitiveElement(3, 1));
        setH(0, 0);

        DoubleMatrix cent = (DoubleMatrix) H.multiply(xy);
        centroid = new AtlasPosition(cent.getPrimitiveElement(0, 0),
                cent.getPrimitiveElement(1, 0), 0.0);

        results.add(new StrainResult(epsilon, centroid));
        results.add(new StressResult(sigma, centroid));

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
     * @param tract DOCUMENT ME!
     */
    public void addTraction(Traction1D tract) {
        this.tractions.add(tract);
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
            " N3=" + nodes[2].getId() + " N4=" + nodes[3].getId();

        return ret;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return Returns the thickness.
     */
    public double getThickness() {
        return thickness;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param thickness The thickness to setElement.
     */
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    /**
     * Draws a a quad array.
     *
     * @param geometryRoot DOCUMENT ME!
     */
    public void populateGeometry(BranchGroup geometryRoot) {
        QuadArray qa = new QuadArray(16,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        AtlasNode[] nodes = this.getNodes();

        Color3f white = new Color3f(Color.white);

        for (int i = 0; i < 4; i++) {
            qa.setCoordinate(i, nodes[i].getGlobalPosition());
            qa.setColor(i, white);
        }

        PolygonAttributes pa = new PolygonAttributes(PolygonAttributes.POLYGON_LINE,
                PolygonAttributes.CULL_NONE, 0);

        Appearance app = new Appearance();
        app.setPolygonAttributes(pa);

        geometryRoot.addChild(new Shape3D(qa, app));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element loadJDOMElement() {
        Element ret = new Element(this.getClass().getName());
        ret.setAttribute("Id", this.getId());

        double thickness = this.getThickness();
        ret.setAttribute("Thickness", String.valueOf(thickness));

        AtlasNode[] n = this.getNodes();
        ret.setAttribute("Nodes", AtlasUtils.convertAtlasObjects(n));

        ret.setAttribute("Material", this.getMat().getId());

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AtlasObject unloadJDOMElement(AtlasModel parent, Element e) {
        String id = e.getAttributeValue("Id");

        double thickness = Double.valueOf(e.getAttributeValue("Thickness"))
                                 .doubleValue();

        AtlasNode[] nodes = new AtlasNode[4];
        String[] nodeIds = AtlasUtils.convertStringtoIds(e.getAttributeValue(
                    "Nodes"));

        for (int i = 0; i < 4; i++) {
            AtlasNode n = (AtlasNode) parent.getObject(AtlasNode.TYPE,
                    nodeIds[i]);
            nodes[i] = n;
        }

        String matid = e.getAttributeValue("Material");
        AtlasMaterial mat = (AtlasMaterial) parent.getMaterial(matid);

        Quad2DElement ret = new Quad2DElement(id, nodes, mat);
        ret.setThickness(thickness);

        return ret;
    }
}
