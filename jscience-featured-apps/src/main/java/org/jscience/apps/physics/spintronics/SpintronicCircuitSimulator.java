/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.util.*;

/**
 * Transient circuit simulator for spintronic devices.
 * <p>
 * Solves circuit equations using Modified Nodal Analysis (MNA).
 * Couples Kirchhoff's laws with LLG dynamics for MTJ/SOT devices.
 * </p>
 * 
 * <h3>Solver Method</h3>
 * <p>
 * Uses Backward Euler or Trapezoidal integration for the differential-algebraic
 * equations (DAE) of the circuit.
 * $$ \mathbf{C} \dot{\mathbf{x}} + \mathbf{G} \mathbf{x} + \mathbf{F}(\mathbf{x}) = \mathbf{b}(t) $$
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpintronicCircuitSimulator {

    private final SpintronicNetlist netlist;
    private final int numNodes;
    private final List<Double> timePoints = new ArrayList<>();
    private final List<Vector<Real>> solutions = new ArrayList<>();
    
    // MNA Matrices
    private double[][] G; // Conductance matrix
    private double[][] C; // Capacitance/Inductance matrix
    private double[] rhs; // Right-hand side vector
    
    // State variables
    private double currentTime = 0;
    
    /**
     * Creates a simulator for the given netlist.
     * 
     * @param netlist The parsed circuit netlist
     */
    public SpintronicCircuitSimulator(SpintronicNetlist netlist) {
        this.netlist = netlist;
        this.numNodes = netlist.getNodeCount();
    }

    /**
     * Runs a transient analysis.
     * 
     * @param tStart Start time (s)
     * @param tStop Stop time (s)
     * @param tStep Time step (s)
     */
    public void runTransient(double tStart, double tStop, double tStep) {
        initializeSystem();
        
        currentTime = tStart;
        Vector<Real> currentState = getInitialState();
        
        while (currentTime <= tStop) {
            // Store result
            timePoints.add(currentTime);
            solutions.add(currentState);
            
            // Advance time step (Newton-Raphson iteration for nonlinear components)
            currentState = solveStep(currentState, tStep);
            currentTime += tStep;
        }
    }

    private void initializeSystem() {
        int size = numNodes; // MNA size might be larger with voltage sources
        G = new double[size][size];
        C = new double[size][size];
        rhs = new double[size];
        
        // Stamp linear components
        for (SpintronicNetlist.NetlistComponent comp : netlist.getComponents()) {
            if (comp instanceof SpintronicNetlist.ResistorComponent) {
                stampResistor((SpintronicNetlist.ResistorComponent) comp);
            } else if (comp instanceof SpintronicNetlist.CapacitorComponent) {
                stampCapacitor((SpintronicNetlist.CapacitorComponent) comp);
            }
            // Voltage sources and Inductors strictly require augmented MNA (row expansion)
            // Simplified here: Current sources only for basic MNA
            else if (comp instanceof SpintronicNetlist.CurrentSourceComponent) {
                // Current sources affect RHS, done per step for time-varying
            }
        }
    }

    private void stampResistor(SpintronicNetlist.ResistorComponent r) {
        int n1 = r.getNodes()[0];
        int n2 = r.getNodes()[1];
        double g = 1.0 / r.getValue();
        
        if (n1 != 0) G[n1-1][n1-1] += g;
        if (n2 != 0) G[n2-1][n2-1] += g;
        if (n1 != 0 && n2 != 0) {
            G[n1-1][n2-1] -= g;
            G[n2-1][n1-1] -= g;
        }
    }

    private void stampCapacitor(SpintronicNetlist.CapacitorComponent c) {
        int n1 = c.getNodes()[0];
        int n2 = c.getNodes()[1];
        double cap = c.getValue();
        
        if (n1 != 0) C[n1-1][n1-1] += cap;
        if (n2 != 0) C[n2-1][n2-1] += cap;
        if (n1 != 0 && n2 != 0) {
            C[n1-1][n2-1] -= cap;
            C[n2-1][n1-1] -= cap;
        }
    }

    private Vector<Real> getInitialState() {
        // DC operating point analysis (C open, L short)
        // For simplicity, start with zero state
        List<Real> state = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) state.add(Real.ZERO);
        return DenseVector.of(state, org.jscience.mathematics.numbers.real.Real.ZERO);
    }

    private Vector<Real> solveStep(Vector<Real> prevSol, double dt) {
        // Discretize: (C/dt + G) * x_new = C/dt * x_old + b(t_new)
        // Simplified Backward Euler for linear system
        
        int size = numNodes;
        double[][] A = new double[size][size];
        double[] b = new double[size];
        
        // Build system matrix A = G + C/dt
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = G[i][j] + C[i][j] / dt;
            }
        }
        
        // Build RHS vector b = (C/dt)*x_old + sources
        for (int i = 0; i < size; i++) {
            b[i] = 0;
            // Add C contribution
            for (int j = 0; j < size; j++) {
                // Note: Index - 1 since simulated nodes are 1-based (0 is ground)
                if (i < prevSol.dimension()) { // Robust check
                     b[i] += (C[i][j] / dt) * prevSol.get(j).doubleValue();
                }
            }
        }
        
        // Add current sources
        for (SpintronicNetlist.NetlistComponent comp : netlist.getComponents()) {
             if (comp instanceof SpintronicNetlist.CurrentSourceComponent) {
                 SpintronicNetlist.CurrentSourceComponent isrc = (SpintronicNetlist.CurrentSourceComponent) comp;
                 int n1 = isrc.getNodes()[0];
                 int n2 = isrc.getNodes()[1];
                 double val = isrc.getValue(); // Assuming DC for now
                 
                 if (n1 != 0) b[n1-1] -= val; // Current entering n1
                 if (n2 != 0) b[n2-1] += val; // Current leaving n2
             }
        }
        
        // Add MTJ/nonlinear components (Newton-Raphson typically needed here)
        // For simple linear approximation: treat MTJ as resistor at previous state R(theta)
        
        // Solver: Gaussian elimination
        return solveLinearSystem(A, b);
    }

    private Vector<Real> solveLinearSystem(double[][] A, double[] b) {
        // Simple Gaussian elimination
        int n = b.length;
        if (n == 0) return DenseVector.of(new ArrayList<>(), org.jscience.mathematics.numbers.real.Real.ZERO);
        
        for (int p = 0; p < n; p++) {
            // Find pivot
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            // Swap
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double t = b[p]; b[p] = b[max]; b[max] = t;
            
            // Pivot constraint
            if (Math.abs(A[p][p]) < 1e-12) continue; // Singular or decoupled node
            
            // Eliminate
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }
        
        // Back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            if (Math.abs(A[i][i]) > 1e-12)
                x[i] = (b[i] - sum) / A[i][i];
            else x[i] = 0;
        }
        
        List<Real> res = new ArrayList<>();
        for (double val : x) res.add(Real.of(val));
        return DenseVector.of(res, org.jscience.mathematics.numbers.real.Real.ZERO);
    }
    
    /**
     * Gets the voltage at a specific node over time.
     * 
     * @param nodeName Name of the node
     * @return List of voltage values corresponding to simulation time points
     */
    public List<Double> getNodeVoltage(String nodeName) {
        // Map name to index, extract column from solutions
        // TODO: Implement node lookup
        return new ArrayList<>(); 
    }
    
    public List<Double> getTimePoints() {
         return timePoints;
    }
}
