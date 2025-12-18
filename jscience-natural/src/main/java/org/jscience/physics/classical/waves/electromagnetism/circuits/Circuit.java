/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.classical.waves.electromagnetism.circuits;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;

/**
 * A headless circuit simulator using Modified Nodal Analysis (MNA).
 * 
 * <p>
 * This class manages a collection of {@link CircuitElement}s and provides
 * methods to analyze and simulate the circuit. The simulation uses sparse
 * matrix techniques for efficiency.
 * </p>
 * 
 * <h3>Usage Example:</h3>
 * 
 * <pre>
 * Circuit circuit = new Circuit();
 * circuit.add(new VoltageSource(10.0)); // 10V source
 * circuit.add(new Resistor(100.0)); // 100 ohm resistor
 * circuit.add(new Ground()); // Ground reference
 * circuit.analyze();
 * circuit.step();
 * double current = circuit.getElement(1).getCurrent();
 * </pre>
 * 
 * @author JScience (ported from Paul Falstad's Circuit Simulator)
 */
public class Circuit {

    /** List of all circuit elements */
    private final List<CircuitElement> elements = new ArrayList<>();

    /** List of all circuit nodes */
    private final List<CircuitNode> nodes = new ArrayList<>();

    /** Voltage source elements for current extraction */
    private CircuitElement[] voltageSources;

    /** The MNA matrix */
    private double[][] circuitMatrix;

    /** The right-hand side vector */
    private double[] circuitRightSide;

    /** Original matrix (before modification by nonlinear elements) */
    private double[][] origMatrix;

    /** Original right-hand side */
    private double[] origRightSide;

    /** Row information for matrix simplification */
    private RowInfo[] circuitRowInfo;

    /** Matrix size after simplification */
    private int circuitMatrixSize;

    /** Full matrix size before simplification */
    private int circuitMatrixFullSize;

    /** Whether the circuit contains nonlinear elements */
    private boolean circuitNonLinear;

    /** Whether the matrix needs node mapping */
    private boolean circuitNeedsMap;

    /** Current simulation time */
    private double time = 0;

    /** Time step for simulation */
    private double timeStep = 5e-6;

    /** Whether the simulation has converged */
    private boolean converged;

    /** Maximum sub-iterations for nonlinear convergence */
    private static final int MAX_SUBITER = 5000;

    /** Error message if simulation fails */
    private String stopMessage;

    /**
     * Creates a new empty circuit.
     */
    public Circuit() {
    }

    /**
     * Adds a circuit element to this circuit.
     * 
     * @param element The element to add
     */
    public void add(CircuitElement element) {
        element.setCircuit(this);
        elements.add(element);
    }

    /**
     * Removes a circuit element from this circuit.
     * 
     * @param element The element to remove
     * @return true if the element was removed
     */
    public boolean remove(CircuitElement element) {
        return elements.remove(element);
    }

    /**
     * Returns the element at the specified index.
     * 
     * @param index Element index
     * @return The circuit element
     */
    public CircuitElement getElement(int index) {
        return elements.get(index);
    }

    /**
     * Returns the number of elements in the circuit.
     * 
     * @return Element count
     */
    public int getElementCount() {
        return elements.size();
    }

    /**
     * Returns the circuit node at the specified index.
     * 
     * @param index Node index
     * @return The circuit node, or null if index is out of range
     */
    public CircuitNode getNode(int index) {
        if (index < 0 || index >= nodes.size()) {
            return null;
        }
        return nodes.get(index);
    }

    /**
     * Returns the number of nodes in the circuit.
     * 
     * @return Node count
     */
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * Returns the current simulation time.
     * 
     * @return Time in seconds
     */
    public double getTime() {
        return time;
    }

    /**
     * Sets the simulation time step.
     * 
     * @param dt Time step in seconds
     */
    public void setTimeStep(double dt) {
        this.timeStep = dt;
    }

    /**
     * Returns the simulation time step.
     * 
     * @return Time step in seconds
     */
    public double getTimeStep() {
        return timeStep;
    }

    /**
     * Returns any error message from the simulation.
     * 
     * @return Error message, or null if no error
     */
    public String getStopMessage() {
        return stopMessage;
    }

    /**
     * Analyzes the circuit and builds the MNA matrix.
     * Must be called before {@link #step()}.
     * 
     * @return true if analysis succeeded
     */
    public boolean analyze() {
        if (elements.isEmpty()) {
            return false;
        }

        stopMessage = null;
        nodes.clear();

        // Allocate ground node (node 0)
        CircuitNode groundNode = new CircuitNode();
        groundNode.x = -1;
        groundNode.y = -1;
        nodes.add(groundNode);

        int vscount = 0;

        // Build node list and connect elements to nodes
        for (CircuitElement ce : elements) {
            int posts = ce.getPostCount();
            int internalNodes = ce.getInternalNodeCount();

            // Match element posts to circuit nodes based on coordinates
            for (int j = 0; j < posts; j++) {
                int[] pos = ce.getPostPosition(j);
                int foundNode = -1;

                // Search for existing node at these coordinates
                for (int k = 0; k < nodes.size(); k++) {
                    CircuitNode cn = nodes.get(k);
                    if (cn.x == pos[0] && cn.y == pos[1] && !cn.internal) {
                        foundNode = k;
                        break;
                    }
                }

                if (foundNode != -1) {
                    // Reuse existing node
                    CircuitNode cn = nodes.get(foundNode);
                    CircuitNodeLink cnl = new CircuitNodeLink(ce, j);
                    cn.links.add(cnl);
                    ce.setNode(j, foundNode);
                } else {
                    // Create new node
                    CircuitNode cn = new CircuitNode(pos[0], pos[1]);
                    CircuitNodeLink cnl = new CircuitNodeLink(ce, j);
                    cn.links.add(cnl);
                    ce.setNode(j, nodes.size());
                    nodes.add(cn);
                }
            }

            // Allocate internal nodes
            for (int j = 0; j < internalNodes; j++) {
                CircuitNode cn = new CircuitNode(-1, -1);
                cn.internal = true;
                CircuitNodeLink cnl = new CircuitNodeLink(ce, j + posts);
                cn.links.add(cnl);
                ce.setNode(j + posts, nodes.size());
                nodes.add(cn);
            }

            vscount += ce.getVoltageSourceCount();
        }

        // Allocate voltage sources array
        voltageSources = new CircuitElement[vscount];
        vscount = 0;
        circuitNonLinear = false;

        // Assign voltage source indices and check for nonlinearity
        for (CircuitElement ce : elements) {
            if (ce.nonLinear()) {
                circuitNonLinear = true;
            }
            int ivs = ce.getVoltageSourceCount();
            for (int j = 0; j < ivs; j++) {
                voltageSources[vscount] = ce;
                ce.setVoltageSource(j, vscount++);
            }
        }
        // vscount is local now

        // Build MNA matrix
        int matrixSize = nodes.size() - 1 + vscount;
        circuitMatrix = new double[matrixSize][matrixSize];
        circuitRightSide = new double[matrixSize];
        origMatrix = new double[matrixSize][matrixSize];
        origRightSide = new double[matrixSize];
        circuitMatrixSize = circuitMatrixFullSize = matrixSize;
        circuitRowInfo = new RowInfo[matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            circuitRowInfo[i] = new RowInfo();
            circuitRowInfo[i].mapRow = i;
            circuitRowInfo[i].mapCol = i;
        }
        circuitNeedsMap = false;

        // Stamp all elements
        for (CircuitElement ce : elements) {
            ce.stamp();
        }

        // Store original matrix for nonlinear iterations
        for (int i = 0; i < matrixSize; i++) {
            origRightSide[i] = circuitRightSide[i];
            for (int j = 0; j < matrixSize; j++) {
                origMatrix[i][j] = circuitMatrix[i][j];
            }
        }
        circuitNeedsMap = true;

        // For linear circuits, we solve in step(), so no pre-calc needed in analyze()
        return true;
    }

    /**
     * Performs one simulation step.
     * 
     * @return true if the step succeeded
     */
    public boolean step() {
        if (circuitMatrix == null || elements.isEmpty()) {
            return false;
        }

        // Start iteration for all elements
        for (CircuitElement ce : elements) {
            ce.startIteration();
        }

        // Iterative solving for nonlinear circuits
        for (int subiter = 0; subiter < MAX_SUBITER; subiter++) {
            converged = true;

            // Reset matrix to original values
            for (int i = 0; i < circuitMatrixSize; i++) {
                circuitRightSide[i] = origRightSide[i];
            }

            if (circuitNonLinear) {
                for (int i = 0; i < circuitMatrixSize; i++) {
                    for (int j = 0; j < circuitMatrixSize; j++) {
                        circuitMatrix[i][j] = origMatrix[i][j];
                    }
                }
            }

            // Let elements update the matrix
            for (CircuitElement ce : elements) {
                ce.doStep();
            }

            if (stopMessage != null) {
                return false;
            }

            // Check for NaN/Infinity
            for (int i = 0; i < circuitMatrixSize; i++) {
                for (int j = 0; j < circuitMatrixSize; j++) {
                    if (Double.isNaN(circuitMatrix[i][j]) || Double.isInfinite(circuitMatrix[i][j])) {
                        stopMessage = "NaN/Infinite matrix value!";
                        return false;
                    }
                }
            }

            // Solve using LinearAlgebraProvider
            if (!solveMatrix()) {
                stopMessage = "Singular matrix or solver error!";
                return false;
            }

            // Update element voltages and currents
            for (int j = 0; j < circuitMatrixFullSize; j++) {
                RowInfo ri = circuitRowInfo[j];
                double res;
                if (ri.type == RowInfo.ROW_CONST) {
                    res = ri.value;
                } else {
                    res = circuitRightSide[ri.mapCol];
                }

                if (Double.isNaN(res)) {
                    converged = false;
                    break;
                }

                if (j < nodes.size() - 1) {
                    CircuitNode cn = getNode(j + 1);
                    if (cn != null) {
                        for (CircuitNodeLink cnl : cn.links) {
                            cnl.elm.setNodeVoltage(cnl.num, res);
                        }
                    }
                } else {
                    int ji = j - (nodes.size() - 1);
                    if (ji < voltageSources.length) {
                        voltageSources[ji].setCurrent(ji, res);
                    }
                }
            }

            if (!circuitNonLinear) {
                break;
            }

            if (converged && subiter > 0) {
                break;
            }
        }

        time += timeStep;
        return converged;
    }

    /**
     * Stamps a value into the right-hand side vector.
     * 
     * @param row   Row index (1-indexed, 0 = ground)
     * @param value Value to add
     */
    public void stampRightSide(int row, double value) {
        if (row > 0) {
            if (circuitNeedsMap) {
                row = circuitRowInfo[row - 1].mapRow;
            } else {
                row--;
            }
            circuitRightSide[row] += value;
        }
    }

    /**
     * Marks that the right-hand side of a row changes during doStep().
     * 
     * @param row Row index (1-indexed)
     */
    public void stampRightSide(int row) {
        if (row > 0) {
            circuitRowInfo[row - 1].rsChanges = true;
        }
    }

    /**
     * Stamps a value into the circuit matrix.
     * 
     * @param row   Row index (1-indexed, 0 = ground)
     * @param col   Column index (1-indexed, 0 = ground)
     * @param value Value to add
     */
    public void stampMatrix(int row, int col, double value) {
        if (row > 0 && col > 0) {
            if (circuitNeedsMap) {
                row = circuitRowInfo[row - 1].mapRow;
                RowInfo ri = circuitRowInfo[col - 1];
                if (ri.type == RowInfo.ROW_CONST) {
                    circuitRightSide[row] -= ri.value * value;
                    return;
                }
                col = ri.mapCol;
            } else {
                row--;
                col--;
            }
            circuitMatrix[row][col] += value;
        }
    }

    /**
     * Stamps a resistor between two nodes.
     * 
     * @param n1         First node
     * @param n2         Second node
     * @param resistance Resistance in ohms
     */
    public void stampResistor(int n1, int n2, double resistance) {
        double conductance = 1.0 / resistance;
        stampMatrix(n1, n1, conductance);
        stampMatrix(n2, n2, conductance);
        stampMatrix(n1, n2, -conductance);
        stampMatrix(n2, n1, -conductance);
    }

    /**
     * Stamps a voltage source between two nodes.
     * 
     * @param n1      Positive node
     * @param n2      Negative node
     * @param vs      Voltage source index
     * @param voltage Voltage value
     */
    public void stampVoltageSource(int n1, int n2, int vs, double voltage) {
        int vn = nodes.size() + vs;
        stampMatrix(vn, n1, -1);
        stampMatrix(vn, n2, 1);
        stampRightSide(vn, voltage);
        stampMatrix(n1, vn, 1);
        stampMatrix(n2, vn, -1);
    }

    /**
     * Stamps a current source between two nodes.
     * 
     * @param n1      Source node (current flows out)
     * @param n2      Sink node (current flows in)
     * @param current Current in amperes
     */
    public void stampCurrentSource(int n1, int n2, double current) {
        stampRightSide(n1, -current);
        stampRightSide(n2, current);
    }

    /**
     * Updates the value of a voltage source.
     * Used for dynamic elements that change voltage during simulation.
     * 
     * @param n1      Positive node
     * @param n2      Negative node
     * @param vs      Voltage source index
     * @param voltage New voltage value
     */

    public void updateVoltageSource(int n1, int n2, int vs, double voltage) {
        int vn = nodes.size() + vs;
        stampRightSide(vn, voltage);
    }

    /**
     * Resets all circuit elements.
     */
    public void reset() {
        for (CircuitElement ce : elements) {
            ce.reset();
        }
        time = 0;
        stopMessage = null;
    }

    /**
     * Solves the circuit matrix using the configured LinearAlgebraProvider.
     * Bridges the internal double arrays to the V2 Matrix types.
     */
    private boolean solveMatrix() {
        if (circuitMatrixSize == 0)
            return true;

        // 1. Convert double[][] A -> Matrix<Real>
        // Efficient construction from flat array would be better, but list-of-lists is
        // safe for now
        List<List<Real>> rows = new ArrayList<>(circuitMatrixSize);
        for (int i = 0; i < circuitMatrixSize; i++) {
            List<Real> row = new ArrayList<>(circuitMatrixSize);
            for (int j = 0; j < circuitMatrixSize; j++) {
                row.add(Real.of(circuitMatrix[i][j]));
            }
            rows.add(row);
        }
        Matrix<Real> A = DenseMatrix.of(rows, org.jscience.mathematics.sets.Reals.getInstance());

        // 2. Convert double[] b -> Vector<Real>
        List<Real> bList = new ArrayList<>(circuitMatrixSize);
        for (int i = 0; i < circuitMatrixSize; i++) {
            bList.add(Real.of(circuitRightSide[i]));
        }
        Vector<Real> b = new DenseVector<>(bList, org.jscience.mathematics.sets.Reals.getInstance());

        // 3. Solve using Provider
        try {
            LinearAlgebraProvider<Real> provider = new org.jscience.mathematics.linearalgebra.backends.CPUDenseLinearAlgebraProvider<>(
                    org.jscience.mathematics.sets.Reals.getInstance());
            Vector<Real> x = provider.solve(A, b);

            // 4. Write result back to circuitRightSide (which acts as solution holder)
            for (int i = 0; i < circuitMatrixSize; i++) {
                circuitRightSide[i] = x.get(i).doubleValue();
            }
            return true;
        } catch (ArithmeticException e) {
            // Singular matrix or other math error
            return false;
        }
    }
}
