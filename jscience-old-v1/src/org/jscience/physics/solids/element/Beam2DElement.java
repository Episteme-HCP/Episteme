/*
 * BasicRodElement.java
 *
 * Created on December 30, 2004, 1:46 AM
 */
package org.jscience.physics.solids.element;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;

import org.jscience.physics.solids.AtlasNode;
import org.jscience.physics.solids.AtlasSection;
import org.jscience.physics.solids.CurveElement;
import org.jscience.physics.solids.SolutionMatrices;
import org.jscience.physics.solids.geom.AtlasCoordSys;
import org.jscience.physics.solids.geom.AtlasPosition;
import org.jscience.physics.solids.load.DistLoad;
import org.jscience.physics.solids.material.BeamMat;
import org.jscience.physics.solids.result.StrainResult;

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
public class Beam2DElement extends CurveElement {
    //static Logger AtlasLogger = Logger.getLogger((Beam2DElement.class).getName());
    /**
     * DOCUMENT ME!
     */
    protected String TYPE = "Basic Beam Element";

    /**
     * DOCUMENT ME!
     */
    private AtlasSection xSect;

    /**
     * DOCUMENT ME!
     */
    private BeamMat beamMat;

    /**
     * DOCUMENT ME!
     */
    private double length;

    /**
     * DOCUMENT ME!
     */
    private double phi = 0.0;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix Ke = null;

    /**
     * DOCUMENT ME!
     */
    private DoubleMatrix Kg = null;

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
    private DoubleMatrix Fe = null;

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
     * Full constructor of a BasicBeamElement
     */
    public Beam2DElement(String id, AtlasNode endA, AtlasNode endB,
        AtlasSection sect, BeamMat mat1) {
        setId(id);
        setNodeA(endA);
        setNodeB(endB);
        setProp(sect);
        setMat(mat1);
    }

    /**
     * Sets the cross-sectional area of the element.
     *
     * @param sect DOCUMENT ME!
     */
    public void setProp(AtlasSection sect) {
        this.xSect = sect;
    }

    /**
     * Returns the cross-sectional area of the element.
     *
     * @return DOCUMENT ME!
     */
    public AtlasSection getProp() {
        return xSect;
    }

    /**
     * Sets the material modulus of the element.
     *
     * @param mat DOCUMENT ME!
     */
    public void setMat(BeamMat mat) {
        this.beamMat = mat;
    }

    /**
     * Returns the material modulus of the element.
     *
     * @return DOCUMENT ME!
     */
    public BeamMat getMat() {
        return beamMat;
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
        //AtlasLogger.debug(" Element Matrices: " + this.getId());
        this.getKe();
        this.getT();

        int[] ij = new int[6];
        DoubleMatrix K = (DoubleMatrix) (((DoubleMatrix) T.transpose()).multiply(Ke)).multiply(T);

        //AtlasLogger.debug(K.toString(efmt, 12));
        DoubleMatrix Kg = m.getStiffnessMatrix();
        ij[0] = m.getDOFIndex(getEndA(), 1);
        ij[1] = m.getDOFIndex(getEndA(), 2);
        ij[2] = m.getDOFIndex(getEndA(), 6);
        ij[3] = m.getDOFIndex(getEndB(), 1);
        ij[4] = m.getDOFIndex(getEndB(), 2);
        ij[5] = m.getDOFIndex(getEndB(), 6);

        //AtlasLogger.debug(" Index 1: " + ij[0]);
        //AtlasLogger.debug(" Index 2: " + ij[1]);
        //AtlasLogger.debug(" Index 3: " + ij[2]);
        //AtlasLogger.debug(" Index 4: " + ij[3]);
        //AtlasLogger.debug(" Index 5: " + ij[4]);
        //AtlasLogger.debug(" Index 6: " + ij[5]);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                double termIJ = Kg.getPrimitiveElement(ij[i], ij[j]);
                termIJ = termIJ + K.getPrimitiveElement(i, j);
                Kg.setElement(ij[i], ij[j], termIJ);
            }
        }
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
        double A = this.xSect.getArea();
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
            Ke = new DoubleMatrix(6, 6);
            length = this.computeLength();

            double val1 = (xSect.getArea() * beamMat.getModulus()) / length;
            double ts = (1.0 + phi);
            double EI = beamMat.getModulus() * xSect.getIy();
            double val2 = (12.0 * EI) / (length * length * length * ts);
            double val3 = (6.0 * EI) / (length * length * ts);
            double val4 = ((4.0 + phi) * EI) / (length * ts);
            double val5 = ((2.0 - phi) * EI) / (length * ts);

            Ke.setElement(0, 0, val1);
            Ke.setElement(0, 1, 0.0);
            Ke.setElement(0, 2, 0.0);
            Ke.setElement(0, 3, -val1);
            Ke.setElement(0, 4, 0.0);
            Ke.setElement(0, 5, 0.0);
            Ke.setElement(1, 0, 0.0);
            Ke.setElement(1, 1, val2);
            Ke.setElement(1, 2, val3);
            Ke.setElement(1, 3, 0.0);
            Ke.setElement(1, 4, -val2);
            Ke.setElement(1, 5, val3);
            Ke.setElement(2, 0, 0.0);
            Ke.setElement(2, 1, val3);
            Ke.setElement(2, 2, val4);
            Ke.setElement(2, 3, 0.0);
            Ke.setElement(2, 4, -val3);
            Ke.setElement(2, 5, val5);
            Ke.setElement(3, 0, -val1);
            Ke.setElement(3, 1, 0.0);
            Ke.setElement(3, 2, 0.0);
            Ke.setElement(3, 3, val1);
            Ke.setElement(3, 4, 0.0);
            Ke.setElement(3, 5, 0.0);
            Ke.setElement(4, 0, 0.0);
            Ke.setElement(4, 1, -val2);
            Ke.setElement(4, 2, -val3);
            Ke.setElement(4, 3, 0.0);
            Ke.setElement(4, 4, val2);
            Ke.setElement(4, 5, -val3);
            Ke.setElement(5, 0, 0.0);
            Ke.setElement(5, 1, val3);
            Ke.setElement(5, 2, val5);
            Ke.setElement(5, 3, 0.0);
            Ke.setElement(5, 4, -val3);
            Ke.setElement(5, 5, val4);
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
    private DoubleMatrix getKg() {
        if (Kg == null) {
            Kg = new DoubleMatrix(4, 4);
            length = this.computeLength();

            double pav = (Fe.getPrimitiveElement(0, 0) +
                Fe.getPrimitiveElement(3, 0)) / 2.0;
            double val1 = (6.0 * pav) / (5.0 * length);
            double val2 = (2.0 * pav * length) / 15.0;
            double val3 = pav / 10.0;
            double val4 = (-pav * length) / 30.0;

            Kg.setElement(0, 0, 0.0);
            Kg.setElement(0, 1, 0.0);
            Kg.setElement(0, 2, 0.0);
            Kg.setElement(0, 3, 0.0);
            Kg.setElement(0, 4, 0.0);
            Kg.setElement(0, 5, 0.0);
            Kg.setElement(1, 0, 0.0);
            Kg.setElement(1, 1, 0.0);
            Kg.setElement(1, 2, val1);
            Kg.setElement(1, 3, val3);
            Kg.setElement(1, 4, -val1);
            Kg.setElement(1, 5, val3);
            Kg.setElement(2, 0, 0.0);
            Kg.setElement(2, 1, val3);
            Kg.setElement(2, 2, val2);
            Kg.setElement(2, 3, 0.0);
            Kg.setElement(2, 4, -val3);
            Kg.setElement(2, 5, val4);
            Kg.setElement(3, 0, 0.0);
            Kg.setElement(3, 1, 0.0);
            Kg.setElement(3, 2, 0.0);
            Kg.setElement(3, 3, 0.0);
            Kg.setElement(3, 4, 0.0);
            Kg.setElement(3, 5, 0.0);
            Kg.setElement(4, 0, 0.0);
            Kg.setElement(4, 1, -val1);
            Kg.setElement(4, 2, -val3);
            Kg.setElement(4, 3, 0.0);
            Kg.setElement(4, 4, val1);
            Kg.setElement(4, 5, -val3);
            Kg.setElement(5, 0, 0.0);
            Kg.setElement(5, 1, val3);
            Kg.setElement(5, 2, val4);
            Kg.setElement(5, 3, 0.0);
            Kg.setElement(5, 4, -val3);
            Kg.setElement(5, 5, val2);
        }

        //AtlasLogger.debug(" Differential Stiffness: " + this.getId());
        //AtlasLogger.debug(Kg.toString(efmt,12));
        return Kg;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private DoubleMatrix getT() {
        if (T == null) {
            T = new DoubleMatrix(6, 6);

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
            T.setElement(0, 4, 0.0);
            T.setElement(0, 5, 0.0);
            T.setElement(1, 0, -s);
            T.setElement(1, 1, c);
            T.setElement(1, 2, 0.0);
            T.setElement(1, 3, 0.0);
            T.setElement(1, 4, 0.0);
            T.setElement(1, 5, 0.0);
            T.setElement(2, 0, 0.0);
            T.setElement(2, 1, 0.0);
            T.setElement(2, 2, 1.0);
            T.setElement(2, 3, 0.0);
            T.setElement(2, 4, 0.0);
            T.setElement(2, 5, 0.0);
            T.setElement(3, 0, 0.0);
            T.setElement(3, 1, 0.0);
            T.setElement(3, 2, 0.0);
            T.setElement(3, 3, c);
            T.setElement(3, 4, s);
            T.setElement(3, 5, 0.0);
            T.setElement(4, 0, 0.0);
            T.setElement(4, 1, 0.0);
            T.setElement(4, 2, 0.0);
            T.setElement(4, 3, -s);
            T.setElement(4, 4, c);
            T.setElement(4, 5, 0.0);
            T.setElement(5, 0, 0.0);
            T.setElement(5, 1, 0.0);
            T.setElement(5, 2, 0.0);
            T.setElement(5, 3, 0.0);
            T.setElement(5, 4, 0.0);
            T.setElement(5, 5, 1.0);
        }

        //AtlasLogger.debug(" Transformation Matrix: " + this.getId());
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
        int[] ij = new int[6];
        DoubleMatrix U = m.getDisplacementMatrix();

        if (Ue == null) {
            Ue = new DoubleMatrix(6, 1);
        }

        ij[0] = m.getDOFIndex(getEndA(), 1);
        ij[1] = m.getDOFIndex(getEndA(), 2);
        ij[2] = m.getDOFIndex(getEndA(), 6);
        ij[3] = m.getDOFIndex(getEndB(), 1);
        ij[4] = m.getDOFIndex(getEndB(), 2);
        ij[5] = m.getDOFIndex(getEndB(), 6);
        Ue.setElement(0, 0, U.getPrimitiveElement(ij[0], 0));
        Ue.setElement(1, 0, U.getPrimitiveElement(ij[1], 0));
        Ue.setElement(2, 0, U.getPrimitiveElement(ij[2], 0));
        Ue.setElement(3, 0, U.getPrimitiveElement(ij[3], 0));
        Ue.setElement(4, 0, U.getPrimitiveElement(ij[4], 0));
        Ue.setElement(5, 0, U.getPrimitiveElement(ij[5], 0));
        //
        // compute element internal forces
        //
        Fe = (DoubleMatrix) Ke.multiply(Ue);

        Ue = (DoubleMatrix) T.multiply(Ue);

        if (B == null) {
            B = new DoubleMatrix(1, 6);
        }

        B.setElement(0, 0, -1.0 / length);
        B.setElement(0, 1, 0.0);
        B.setElement(0, 2, 1.0 / length);
        B.setElement(0, 3, 0.0);

        DoubleMatrix eps = (DoubleMatrix) B.multiply(Ue);

        DoubleMatrix sig = (DoubleMatrix) eps.scalarMultiply(beamMat.getModulus());

        if (results == null) {
            results = new ArrayList();
        }

        // compute centroid
        centroid = new AtlasPosition(0.0, 0.0, 0.0);

        results.add(new StrainResult(eps, centroid));

        //results.add(new StressResult(sig));
        return;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dl DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public void contributeDistLoad(DistLoad dl, SolutionMatrices m) {
        this.getFe(dl);
        this.getT();

        int[] ij = new int[6];
        DoubleMatrix Rg = (DoubleMatrix) ((DoubleMatrix) T.transpose()).multiply(Fe);
        DoubleMatrix R = m.getForceMatrix();
        ij[0] = m.getDOFIndex(getEndA(), 1);
        ij[1] = m.getDOFIndex(getEndA(), 2);
        ij[2] = m.getDOFIndex(getEndA(), 6);
        ij[3] = m.getDOFIndex(getEndB(), 1);
        ij[4] = m.getDOFIndex(getEndB(), 2);
        ij[5] = m.getDOFIndex(getEndB(), 6);

        //AtlasLogger.debug(" Index 1: " + ij[0]);
        //AtlasLogger.debug(" Index 2: " + ij[1]);
        //AtlasLogger.debug(" Index 3: " + ij[2]);
        //AtlasLogger.debug(" Index 4: " + ij[3]);
        //AtlasLogger.debug(" Index 5: " + ij[4]);
        //AtlasLogger.debug(" Index 6: " + ij[5]);
        for (int i = 0; i < 6; i++) {
            double termIJ = R.getPrimitiveElement(ij[i], 0);
            termIJ = termIJ + Rg.getPrimitiveElement(i, 0);
            R.setElement(ij[i], 0, termIJ);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dl DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleMatrix getFe(DistLoad dl) {
        if (Fe == null) {
            Fe = new DoubleMatrix(6, 1);
        }

        double q1 = dl.getValue1();
        double q2 = dl.getValue2();
        double l = this.getEndA().computeDistance(this.getEndB());
        Fe.setElement(0, 0, 0.0);
        Fe.setElement(1, 0, (l / 20.0) * ((7.0 * q1) + (3.0 * q2)));
        Fe.setElement(2, 0, ((l * l) / 60.0) * ((3.0 * q1) + (2.0 * q2)));
        Fe.setElement(3, 0, 0.0);
        Fe.setElement(4, 0, (l / 20.0) * ((3.0 * q1) + (7.0 * q2)));
        Fe.setElement(5, 0, -((l * l) / 60.0) * ((2.0 * q1) + (3.0 * q2)));

        return Fe;
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
            "\n";
        ret = ret + " Area: " + xSect.getArea() + "\n";
        ret = ret + " MOI:  " + xSect.getIy() + "\n";
        ret = ret + " ";

        return ret;
    }
}
