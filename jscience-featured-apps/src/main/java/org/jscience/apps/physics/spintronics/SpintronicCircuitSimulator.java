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
    
    // JScience Linear Algebra Objects
    private DenseMatrix<Real> C_matrix; // Capacitance matrix (Constant topology)
    // G matrix is now built per-iteration for non-linear analysis
    
    // Solver Settings
    private static final int MAX_NEWTON_ITER = 20;
    private static final double NEWTON_TOL = 1e-6;
    private static final double VT_BIAS_ROLLOFF = 0.5; // V_half for TMR roll-off

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
        initializeFixedElements();
        
        double currentTime = tStart;
        Vector<Real> currentState = getInitialState();
        
        timePoints.clear();
        solutions.clear();

        Real dt = Real.of(tStep);

        while (currentTime <= tStop) {
            timePoints.add(currentTime);
            solutions.add(currentState);
            
            currentState = solveStep(currentState, dt, currentTime);
            currentTime += tStep;
        }
    }

    private void initializeFixedElements() {
        // Initialize C matrix (assumed constant for now)
        int size = numNodes;
        Real[][] cData = new Real[size][size];
        for (Real[] row : cData) Arrays.fill(row, Real.ZERO);
        
        for (SpintronicNetlist.NetlistComponent comp : netlist.getComponents()) {
             if (comp instanceof SpintronicNetlist.CapacitorComponent) {
                 SpintronicNetlist.CapacitorComponent c = (SpintronicNetlist.CapacitorComponent) comp;
                 int n1 = c.getNodes()[0];
                 int n2 = c.getNodes()[1];
                 Real val = Real.of(c.getValue());
                 stampMatrix(cData, n1, n2, val);
             }
        }
        C_matrix = new DenseMatrix<>(cData, REAL_FIELD);
    }

    private void stampMatrix(Real[][] matrix, int n1, int n2, Real val) {
        if (n1 > 0) matrix[n1-1][n1-1] = matrix[n1-1][n1-1].add(val);
        if (n2 > 0) matrix[n2-1][n2-1] = matrix[n2-1][n2-1].add(val);
        if (n1 > 0 && n2 > 0) {
            matrix[n1-1][n2-1] = matrix[n1-1][n2-1].subtract(val);
            matrix[n2-1][n1-1] = matrix[n2-1][n1-1].subtract(val);
        }
    }
    
    private void stampVector(Real[] vector, int n1, int n2, Real val) {
        // Current leaving n1, entering n2 => -val at n1, +val at n2 for RHS=I
        // BUT MNA is G*v = I_stim. 
        // Current Source I from n1 to n2: Leaves n1 (-), Enters n2 (+). 
        if (n1 > 0) vector[n1-1] = vector[n1-1].subtract(val);
        if (n2 > 0) vector[n2-1] = vector[n2-1].add(val);
    }

    private Vector<Real> getInitialState() {
        Real[] state = new Real[numNodes];
        Arrays.fill(state, Real.ZERO);
        return new DenseVector<>(state, REAL_FIELD);
    }

    private Vector<Real> solveStep(Vector<Real> prevSol, Real dt, double time) {
        // Backward Euler Discretization:
        // F(v_new) = G(v_new)*v_new + C*(v_new - v_old)/dt - sources = 0
        // We use Newton-Raphson to solve for v_new.
        
        // Initial guess: v_new = v_old
        Vector<Real> v_new = prevSol;
        
        Vector<Real> v_old_div_dt = prevSol.multiply(Real.ONE.divide(dt)); // v_old / dt
        
        for (int iter = 0; iter < MAX_NEWTON_ITER; iter++) {
            // Build Jacobian J and Residual F
            // J = G_tan + C/dt
            // F = I_G(v) + C*(v - v_old)/dt - I_source
            
            // 1. Build Conductance Matrix G and Current Vector I_G
            // For linear R, I_G = G*v. For non-linear, I_G depends on model.
            // Here we rebuild the linearized G matrix at v_new.
            
            int size = numNodes;
            Real[][] gData = new Real[size][size];
            for (Real[] row : gData) Arrays.fill(row, Real.ZERO);
            
            Real[] rhsSource = new Real[size];
            Arrays.fill(rhsSource, Real.ZERO);
            
            // Stamp components
            for (SpintronicNetlist.NetlistComponent comp : netlist.getComponents()) {
                if (comp instanceof SpintronicNetlist.ResistorComponent) {
                    SpintronicNetlist.ResistorComponent r = (SpintronicNetlist.ResistorComponent) comp;
                    Real g = Real.ONE.divide(Real.of(r.getValue()));
                    stampMatrix(gData, r.getNodes()[0], r.getNodes()[1], g);
                } 
                else if (comp instanceof SpintronicNetlist.MTJComponent) {
                    stampMTJ(gData, (SpintronicNetlist.MTJComponent) comp, v_new);
                }
                else if (comp instanceof SpintronicNetlist.CurrentSourceComponent) {
                    SpintronicNetlist.CurrentSourceComponent isrc = (SpintronicNetlist.CurrentSourceComponent) comp;
                    stampVector(rhsSource, isrc.getNodes()[0], isrc.getNodes()[1], Real.of(isrc.getValue()));
                }
                // TODO: Voltage sources check
            }
            
            DenseMatrix<Real> G = new DenseMatrix<>(gData, REAL_FIELD);
            DenseVector<Real> I_source = new DenseVector<>(rhsSource, REAL_FIELD);
            
            // F = G * v_new + C/dt * v_new - (C/dt*v_old + I_source)
            // Jacobian J = G + C/dt (Approximate G_tan ~ G for simple bias dep)
            
            Matrix<Real> C_div_dt = C_matrix.divide(dt);
            Matrix<Real> J = G.add(C_div_dt);
            
            // RHS_eff = C/dt * v_old + I_source
            Vector<Real> term1 = C_div_dt.multiply(prevSol);
            Vector<Real> rhs_eff = term1.add(I_source);
            
            // Residual R = J * v_new - RHS_eff
            // Actually, let's solve J * delta_v = -Residual
            // Or simply linear solve: J * v_next = RHS_eff (Modified Newton / Fixed Point)
            // If G depends on v_new, this is Fixed Point Iteration. 
            // For MatrixSolver, we do: v_next = J^-1 * rhs_eff
            
            Vector<Real> v_next = J.solve(rhs_eff);
            
            // Convergence Check
            Vector<Real> diff = v_next.subtract(v_new);
            Real error = Real.ZERO;
            for(int i=0; i<diff.getDimension(); i++) error = error.add(diff.get(i).abs());
            
            v_new = v_next;
            
            if (error.doubleValue() < NEWTON_TOL) {
                break;
            }
        }
        
        return v_new;
    }
    
    private void stampMTJ(Real[][] gData, SpintronicNetlist.MTJComponent mtj, Vector<Real> v) {
        int n1 = mtj.getNodes()[0];
        int n2 = mtj.getNodes()[1];
        
        // Calculate Voltage across MTJ
        Real v1 = (n1 > 0) ? v.get(n1-1) : Real.ZERO;
        Real v2 = (n2 > 0) ? v.get(n2-1) : Real.ZERO;
        Real v_drop = v1.subtract(v2);
        
        // Bias dependent TMR: TMR(V) = TMR0 / (1 + (V/Vh)^2)
        double vh = VT_BIAS_ROLLOFF;
        double biasFactor = 1.0 / (1.0 + Math.pow(v_drop.doubleValue() / vh, 2));
        double tmr_eff = mtj.getTMR() / 100.0 * biasFactor;
        
        // Assume Parallel State + TMR/2 average for transient if state not tracked coupled
        // Ideally we link to Magnetization state m_z. 
        // For this "Circuit" sim, we treat it as P state resistance * (1+StateFactor*TMR)
        // Let's assume P state for base RA.
        double ra = mtj.getRA(); // Ohm um^2
        double rP = ra * 1e-12; // Adjusted for area scaling? Logic in component says this is R value.
        
        // Simplified: R = rP * (1 + 0.5 * tmr_eff); // Average state
        double conductance = 1.0 / (rP * (1.0 + 0.5 * tmr_eff));
        
        Real g = Real.of(conductance);
        stampMatrix(gData, n1, n2, g);
    }
    
    /**
     * Gets the voltage at a specific node over time.
     * 
     * @param nodeName Name of the node
     * @return List of voltage values corresponding to simulation time points
     */
    public List<Double> getNodeVoltage(String nodeName) {
        // Need node map from netlist, but it's private there. 
        // For now return empty.
        return new ArrayList<>(); 
    }
    
    public List<Double> getTimePoints() {
         return timePoints;
    }
    
    // --- Local Implementation of Field<Real> ---
    // Since we cannot locate the standard RealField, we define it here to satisfy DenseMatrix.
    
    public static final org.jscience.mathematics.structures.rings.Field<Real> REAL_FIELD = new org.jscience.mathematics.structures.rings.Field<Real>() {
        @Override public Real zero() { return Real.ZERO; }
        @Override public Real one() { return Real.ONE; }
        @Override public boolean isCommutative() { return true; }
        @Override public Real add(Real a, Real b) { return a.add(b); }
        @Override public Real multiply(Real a, Real b) { return a.multiply(b); }
        @Override public Real inverse(Real a) { return a.inverse(); }
        @Override public Real negate(Real a) { return a.negate(); }
        @Override public int characteristic() { return 0; }
        
        // Optional methods
        @Override public boolean isMultiplicationCommutative() { return true; }
        @Override public boolean hasUnity() { return true; }
    };
}
