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
 * StiffnessMatrix.java
 *
 * Created on December 31, 2004, 1:43 AM
 */

package org.jscience.physics.solids;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//import org.apache.log4j.Logger;

//import Jama.Matrix;

/**
 * Collection of matrices used in the solution sequence. This contains:
 * A stiffness matrix.
 * A loads matrix.
 * A mass matrix
 * A differential stiffness matrix.
 * <p/>
 * These are grouped together into a single class so that the
 * constraints, etc.. only have to be set once.
 *
 * @author Wegge
 */
public class SolutionMatrices {

    //static Logger AtlasLogger = Logger.getLogger((SolutionMatrices.class).getName());

    /**
     * Stiffness matrix
     */
    private DoubleMatrix K;        // total stiffness Matrix
    private DoubleMatrix Kg;         // differential stiffness Matrix
    private DoubleMatrix Kt;         // tangential stiffness Matrix

    /**
     * Loads Matrix
     */
    private DoubleMatrix R;        // total load Matrix
    private DoubleMatrix Rx;        // reaction forces;
    private DoubleMatrix Re;        // eliminated forces;
    private DoubleMatrix Rb;        // body forces
    private DoubleMatrix Rs;        // surface forces
    private DoubleMatrix Ri;        // forces due to initial stresses
    private DoubleMatrix Rc;         // concentrated forces

    /**
     * Mass Matrix
     */
    private DoubleMatrix M;

    /**
     * Damping Matrix
     */
    private DoubleMatrix C;

    /**
     * Displacement matrices
     */
    private DoubleMatrix U;        // displacement vector
    private DoubleMatrix Uc;         // constrained displacements
    private DoubleMatrix Ue;         // eliminated displacements
    private DoubleMatrix Udot;     // velocity vector
    private DoubleMatrix Uddot;     // acceleration vector

    private int numDOF = 0;
    private int numActive = 0;
    private int numConst = 0;
    private int numUnSup = 0;

    private ArrayList index = new ArrayList();
    private ArrayList constraints = new ArrayList();
    private ArrayList unsupported = new ArrayList();

    DecimalFormat efmt = new DecimalFormat("##.###E0");

    /**
     * Initializes all of the matrices to tge correct size.
     */
    public SolutionMatrices(AtlasNode[] nodes) {

        Arrays.sort(nodes);

        /** Count the total number of DOF. */
        for (int i = 0; i < nodes.length; i++) {
            numDOF = numDOF + nodes[i].getNumberDOF();
            for (int j = 0; j < nodes[i].getNumberDOF(); j++) {
                index.add(new AtlasDOF(nodes[i], j + 1));
            }
        }

        /** Initialize matrices. */
        K = new DoubleMatrix(numDOF, numDOF);
        //AtlasLogger.debug(" Initial Entire Stiffness Array: ");
        //AtlasLogger.debug(K.toString(efmt, 12));

        Kg = new DoubleMatrix(numDOF, numDOF);
        Kt = new DoubleMatrix(numDOF, numDOF);

        R = new DoubleMatrix(numDOF, 1);
        Rb = new DoubleMatrix(numDOF, 1);
        Rs = new DoubleMatrix(numDOF, 1);
        Ri = new DoubleMatrix(numDOF, 1);
        Rc = new DoubleMatrix(numDOF, 1);

        M = new DoubleMatrix(numDOF, numDOF);
        C = new DoubleMatrix(numDOF, numDOF);

        U = new DoubleMatrix(numDOF, 1);
//        Udot = new Matrix(numDOF,1);
//        Uddot = new Matrix(numDOF,1);

    }

    /**
     * Returns the rank of the stiffness Matrix.
     */
    public int getRank() {
        return numDOF;
    }

    /**
     * Counts the numbe of DOF that are not constrained.
     */
    public int getUnconstrainedRank() {

        int numUnconstrained = 0;

        for (int i = 0; i < index.size(); i++) {
            if (!isConstrained((AtlasDOF) index.get(i))) {
                numUnconstrained++;
            }
        }
        return numUnconstrained;
    }

    /**
     * Returns all DOF that are not constrained.
     */
    public AtlasDOF[] getUnconstrainedDOF() {
        ArrayList ret = new ArrayList();

        for (int i = 0; i < index.size(); i++) {

            if (!isConstrained((AtlasDOF) index.get(i))) {
                ret.add(index.get(i));
            }
        }
        return (AtlasDOF[]) ret.toArray(new AtlasDOF[0]);
    }


    public DoubleMatrix getStiffnessMatrix() {
        return K;
    }

    public DoubleMatrix getMassMatrix() {
        return M;
    }

    public DoubleMatrix getDampingMatrix() {
        return C;
    }

    public DoubleMatrix getForceMatrix() {
        return R;
    }

    public DoubleMatrix getDisplacementMatrix() {
        return U;
    }

    public void partitionAll() {
        numConst = constraints.size();
        numUnSup = unsupported.size();
        numActive = numDOF - numConst - numUnSup;
        //AtlasLogger.info(" numDOF:    " + numDOF);
        //AtlasLogger.info(" numActive: " + numActive);
        //AtlasLogger.info(" numConst:  " + numConst);
        //AtlasLogger.info(" numUnSup:  " + numUnSup);

        //AtlasLogger.debug(" Entire Stiffness Array: ");
        //AtlasLogger.debug(K.toString(efmt, 12));

        Collections.sort(constraints);

        for (int i = 0; i < numActive; i++) {
            //AtlasLogger.debug(" Row number(i): " + i);
            AtlasDOF tempDOF = (AtlasDOF) index.get(i);
            //AtlasLogger.debug(" AtlasDOF " + tempDOF.toString());

            if (isConstrained(tempDOF) || unSupported(tempDOF)) {
                DoubleVector tempKRow = (DoubleVector) K.getRow(i);
                DoubleVector tempKCol = (DoubleVector) K.getColumn(i);
                DoubleVector tempMRow = (DoubleVector) M.getRow(i);
                DoubleVector tempMCol = (DoubleVector) M.getColumn(i);
                DoubleVector tempCRow = (DoubleVector) C.getRow(i);
                DoubleVector tempCCol = (DoubleVector) C.getColumn(i);
                DoubleVector tempRRow = (DoubleVector) R.getRow(i);
                DoubleVector tempURow = (DoubleVector) U.getRow(i);

                for (int j = i; j < numDOF - 1; j++) {
                    //AtlasLogger.debug(" Row number(j): " + j);
                    K.setRow(j, K.getRow(j + 1));
                    K.setColumn(j, K.getColumn(j + 1));
                    M.setRow(j, M.getRow(j + 1));
                    M.setColumn(j, M.getColumn(j + 1));
                    C.setRow(j, C.getRow(j + 1));
                    C.setColumn(j, C.getColumn(j + 1));
                    R.setRow(j, R.getRow(j + 1));
                    U.setRow(j, U.getRow(j + 1));
                    index.set(j, index.get(j + 1));
                }

                K.setRow(numDOF - 1, tempKRow);
                K.setColumn(numDOF - 1, tempKCol);
                C.setRow(numDOF - 1, tempCRow);
                C.setColumn(numDOF - 1, tempCCol);
                M.setRow(numDOF - 1, tempMRow);
                M.setColumn(numDOF - 1, tempMCol);
                R.setRow(numDOF - 1, tempRRow);
                U.setRow(numDOF - 1, tempURow);
                index.set(numDOF - 1, tempDOF);
                i--;
            }
        }
        if (numUnSup > 0) {
            for (int i = numActive; i < (numActive + numConst); i++) {
                AtlasDOF tempDOF = (AtlasDOF) index.get(i);
                if (unSupported(tempDOF)) {
                    DoubleVector tempKRow = (DoubleVector) K.getRow(i);
                    DoubleVector tempKCol = (DoubleVector) K.getColumn(i);
                    DoubleVector tempMRow = (DoubleVector) M.getRow(i);
                    DoubleVector tempMCol = (DoubleVector) M.getColumn(i);
                    DoubleVector tempCRow = (DoubleVector) C.getRow(i);
                    DoubleVector tempCCol = (DoubleVector) C.getColumn(i);
                    DoubleVector tempRRow = (DoubleVector) R.getRow(i);
                    DoubleVector tempURow = (DoubleVector) U.getRow(i);

                    for (int j = i; j < numDOF - 1; j++) {
                        K.setRow(j, K.getRow(j + 1));
                        K.setColumn(j, K.getColumn(j + 1));
                        M.setRow(j, M.getRow(j + 1));
                        M.setColumn(j, M.getColumn(j + 1));
                        C.setRow(j, C.getRow(j + 1));
                        C.setColumn(j, C.getColumn(j + 1));
                        R.setRow(j, R.getRow(j + 1));
                        U.setRow(j, U.getRow(j + 1));
                        index.set(j, index.get(j + 1));
                    }

                    K.setRow(numDOF - 1, tempKRow);
                    K.setColumn(numDOF - 1, tempKCol);
                    C.setRow(numDOF - 1, tempCRow);
                    C.setColumn(numDOF - 1, tempCCol);
                    M.setRow(numDOF - 1, tempMRow);
                    M.setColumn(numDOF - 1, tempMCol);
                    R.setRow(numDOF - 1, tempRRow);
                    U.setRow(numDOF - 1, tempURow);
                    index.set(numDOF - 1, tempDOF);
                    i--;
                }
            }

        }

        //AtlasLogger.debug(" Entire Stiffness Array After Partitioning: ");
        //AtlasLogger.debug(K.toString(efmt, 12));

    }

    /**
     * Returns the active portion of the stiffness array.
     */
    public DoubleMatrix getReducedStiffnessMatrix() {
        DoubleMatrix Kr = (DoubleMatrix) K.getSubMatrix(0, numActive - 1, 0, numActive - 1);
        return Kr;
    }

    /**
     * Returns the active portion of the mass array.
     */
    public DoubleMatrix getReducedMassMatrix() {
        DoubleMatrix Mr = (DoubleMatrix) M.getSubMatrix(0, numActive - 1, 0, numActive - 1);
        return Mr;
    }

    /**
     * Returns the active portion of the force array.
     */
    public DoubleMatrix getReducedForceMatrix() {
        DoubleMatrix Rr = (DoubleMatrix) R.getSubMatrix(0, numActive - 1, 0, 0);
        return Rr;
    }

    /**
     * Returns the active portion of the force array.
     */
    public DoubleMatrix getReducedDispMatrix() {
        DoubleMatrix Ur = (DoubleMatrix) U.getSubMatrix(0, numActive - 1, 0, 0);
        return Ur;
    }

    /**
     * Sets the displacements.
     */
    public void setDisplacements(DoubleMatrix Us) {
        setMatrix(U, 0, numActive - 1, 0, 0, Us);
        return;
    }

    private void setMatrix(DoubleMatrix matrix, int i0, int i1, int j0, int j1, DoubleMatrix X) {
        try {
            for (int i = i0; i <= i1; i++) {
                for (int j = j0; j <= j1; j++) {
                    matrix.setElement(i, j, X.getPrimitiveElement(i - i0, j - j0));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    /**
     * Returns the internal location of a specified dof.
     */
    public int getDOFIndex(AtlasNode node, int dof) {
        return index.indexOf(new AtlasDOF(node, dof));
    }

    /**
     * Returns the external dof for a location in the Matrix.
     */
    public AtlasDOF getExternalDOF(int loc) {
        return (AtlasDOF) index.get(loc);
    }

    /**
     * Sets a DOF as contrained.
     */
    public void setConstrainedDOF(AtlasNode node, int dof, double value) {
        AtlasDOF adof = new AtlasDOF(node, dof);
        int ind = getDOFIndex(node, dof);
        U.setElement(ind, 0, value);
        constraints.add(adof);
    }

    /**
     * Sets a DOF as unsupported.
     */
    public void setUnsupportedDOF(AtlasNode node, int dof) {
        AtlasDOF adof = new AtlasDOF(node, dof);
        int ind = getDOFIndex(node, dof);
        U.setElement(ind, 0, 0.0);
        unsupported.add(adof);
    }

    /**
     * Determines whether the specified DOF is constrinaed.
     */
    public boolean isConstrained(AtlasDOF dof) {
        return constraints.contains(dof);
    }

    /**
     * Determines whether the specified DOF is supported.
     */
    public boolean isSupported(AtlasDOF dof) {
        return !unsupported.contains(dof);
    }

    /**
     * Determine if specified DOF is unsupported.
     */
    public boolean unSupported(AtlasDOF dof) {
        return unsupported.contains(dof);
    }

 }
