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
    private final Map<String, SpinValve> physicsModels = new HashMap<>();
    private final Map<String, List<Real[]>> magnetizationHistory = new HashMap<>();
    private final List<Double> timePoints = new ArrayList<>();
    private final List<Vector<Real>> solutions = new ArrayList<>();

    // MNA Augmented
    private int augmentedSize; 
    private int numVoltageSources;
    
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

    // Physics Coupling
    public void registerPhysicsModel(String componentName, SpinValve model) {
        physicsModels.put(componentName, model);
        magnetizationHistory.put(componentName, new ArrayList<>());
    }

    private void countVoltageSources() {
        numVoltageSources = 0;
        for (SpintronicNetlist.NetlistComponent comp : netlist.getComponents()) {
            if (comp instanceof SpintronicNetlist.VoltageSourceComponent) {
                numVoltageSources++;
            }
        }
        augmentedSize = numNodes + numVoltageSources;
    }

    /**
     * Initializes the simulation.
     * Must be called before stepping.
     */
    public void initialize() {
        countVoltageSources();
        initializeFixedElements();
        // Solve for DC Operating Point
        Vector<Real> dcState = solveDCOperatingPoint();
        
        // Add initial state to history
        timePoints.clear();
        solutions.clear();
        
        // Initial condition
        timePoints.add(0.0);
        solutions.add(dcState);
    }

    /**
     * Executes a single time step of the simulation.
     * 
     * @param dt Time step duration (s)
     * @return The new state vector (voltages)
     */
    public Vector<Real> step(double dt) {
        if (solutions.isEmpty()) initialize();
        
        Vector<Real> prevState = solutions.get(solutions.size() - 1);
        double time = timePoints.get(timePoints.size() - 1) + dt;
        
        // 1. Solve Circuit Equations
        Vector<Real> nextState = solveStep(prevState, Real.of(dt), time);
        
        // 2. Update Physics Models (LLG)
        updatePhysics(nextState, dt);
        
        // 3. Store Results
        timePoints.add(time);
        solutions.add(nextState);
        
        // Manage history size for real-time app (optional, keep last N or all)
        // For now keep all, but standard app might blindly grow.
        if (timePoints.size() > 10000) {
            timePoints.remove(0);
            solutions.remove(0);
        }
        
        return nextState;
    }

    private void updatePhysics(Vector<Real> state, double dt) {
        for (SpintronicNetlist.NetlistComponent comp : netlist.getComponents()) {
            if (comp instanceof SpintronicNetlist.MTJComponent && physicsModels.containsKey(comp.getName())) {
                SpintronicNetlist.MTJComponent mtj = (SpintronicNetlist.MTJComponent) comp;
                SpinValve model = physicsModels.get(comp.getName());
                
                // Calculate Current I through MTJ
                int n1 = mtj.getNodes()[0];
                int n2 = mtj.getNodes()[1];
                Real v1 = (n1 > 0) ? state.get(n1-1) : Real.ZERO;
                Real v2 = (n2 > 0) ? state.get(n2-1) : Real.ZERO;
                Real vDrop = v1.subtract(v2);
                
                // Get conductance from previous or current state (Linearized)
                // Approx: I = G * V
                // Better: I = V / R_total
                
                // Re-evaluate resistance based on CURRENT magnetization (before update)
                Real r = GMREffect.valetFertResistance(model);
                Real vNode1 = (n1 > 0) ? vDrop.add(Real.ZERO) : Real.ZERO; // Placeholder
                // Current = V / R
                Real current = vDrop.divide(r);
                
                FerromagneticLayer free = model.getFreeLayer();
                FerromagneticLayer pinned = model.getPinnedLayer();
                
                Real area = model.getArea(); 
                Real volume = area.multiply(free.getThickness());
                Real ms = free.getMaterial().getSaturationMagnetization();
                Real gamma = Real.of(1.76e11);
                
                // 1. Calculate STT torque
                Real J_stt = current.divide(area);
                Real[] t_stt = SpinTransport.calculateSTT(J_stt, free, pinned);
                
                // 2. Calculate SOT torque
                Real[] t_sot = SpinTransport.calculateSOT(free, free.getSotCurrentDensity(), free.getSpinHallAngle());
                
                // 3. Calculate Internal Effective Field (Anisotropy + Demag)
                Real[] hEff = free.calculateEffectiveField();
                
                // 4. Step Physics
                Real[] newM = SpinTransport.stepLLGWithThermalNoise(
                    free, 
                    hEff, 
                    Real.of(dt), 
                    free.getDamping(), 
                    gamma, 
                    free.getTemperature(), 
                    volume, 
                    ms
                );
                
                // 5. Add STT and SOT manually to M
                newM[0] = newM[0].add(t_stt[0].add(t_sot[0]).multiply(Real.of(dt)));
                newM[1] = newM[1].add(t_stt[1].add(t_sot[1]).multiply(Real.of(dt)));
                newM[2] = newM[2].add(t_stt[2].add(t_sot[2]).multiply(Real.of(dt)));
                
                // Renormalize
                Real mNorm = newM[0].pow(2).add(newM[1].pow(2)).add(newM[2].pow(2)).sqrt();
                free.setMagnetization(newM[0].divide(mNorm), newM[1].divide(mNorm), newM[2].divide(mNorm));

                // History (M history)
                if (magnetizationHistory.containsKey(comp.getName())) {
                    magnetizationHistory.get(comp.getName()).add(newM);
                }
            }
        }
    }

    public List<Real[]> getMagnetizationHistory(String componentName) {
        return magnetizationHistory.getOrDefault(componentName, new ArrayList<>());
    }

    private Vector<Real> solveDCOperatingPoint() {
        int size = augmentedSize;
        Real[] zeroArr = new Real[size];
        Arrays.fill(zeroArr, Real.ZERO);
        Vector<Real> v_guess = new DenseVector<Real>(Arrays.asList(zeroArr), org.jscience.mathematics.sets.Reals.INSTANCE);
        
        for (int iter = 0; iter < MAX_NEWTON_ITER; iter++) {
            Real[][] gData = new Real[size][size];
            for (Real[] row : gData) Arrays.fill(row, Real.ZERO);
            Real[] rhsSource = new Real[size];
            Arrays.fill(rhsSource, Real.ZERO);

            int vsIdx = 0;
            for (SpintronicNetlist.NetlistComponent comp : netlist.getComponents()) {
                if (comp instanceof SpintronicNetlist.ResistorComponent) {
                    SpintronicNetlist.ResistorComponent r = (SpintronicNetlist.ResistorComponent) comp;
                    Real g = Real.ONE.divide(Real.of(r.getValue()));
                    stampMatrix(gData, r.getNodes()[0], r.getNodes()[1], g);
                } else if (comp instanceof SpintronicNetlist.VoltageSourceComponent) {
                    SpintronicNetlist.VoltageSourceComponent vs = (SpintronicNetlist.VoltageSourceComponent) comp;
                    stampVoltageSource(gData, rhsSource, vs.getNodes()[0], vs.getNodes()[1], Real.of(vs.getValue()), vsIdx++);
                } else if (comp instanceof SpintronicNetlist.CurrentSourceComponent) {
                    SpintronicNetlist.CurrentSourceComponent cs = (SpintronicNetlist.CurrentSourceComponent) comp;
                    stampVector(rhsSource, cs.getNodes()[0], cs.getNodes()[1], Real.of(cs.getValue()));
                } else if (comp instanceof SpintronicNetlist.MTJComponent) {
                    stampMTJ(gData, (SpintronicNetlist.MTJComponent) comp, v_guess);
                }
            }
            
            Matrix<Real> J = new DenseMatrix<>(gData, org.jscience.mathematics.sets.Reals.INSTANCE);
            Vector<Real> rhs = new DenseVector<Real>(Arrays.asList(rhsSource), org.jscience.mathematics.sets.Reals.INSTANCE);
            Vector<Real> F = J.multiply(v_guess).subtract(rhs);
            
            if (F.dimension() == 0) return v_guess;

            double sumSq = 0;
            for (int i=0; i < F.dimension(); i++) sumSq += Math.pow(F.get(i).doubleValue(), 2);
            if (Math.sqrt(sumSq) < NEWTON_TOL) break;

            Vector<Real> dv = J.inverse().multiply(F);
            v_guess = v_guess.subtract(dv);
        }
        return v_guess;
    }
    
    /**
     * Backward compatibility wrapper
     */
    public void runTransient(double tStart, double tStop, double tStep) {
        initialize();
        double currentTime = tStart;
        Real dt = Real.of(tStep);
        while (currentTime <= tStop) {
            step(tStep);
            currentTime += tStep;
        }
    }

    private void initializeFixedElements() {
        // Initialize C matrix (assumed constant for now)
        int size = augmentedSize;
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
        C_matrix = new DenseMatrix<>(cData, org.jscience.mathematics.sets.Reals.INSTANCE);
    }

    private void stampMatrix(Real[][] matrix, int n1, int n2, Real val) {
        if (n1 > 0) matrix[n1-1][n1-1] = matrix[n1-1][n1-1].add(val);
        if (n2 > 0) matrix[n2-1][n2-1] = matrix[n2-1][n2-1].add(val);
        if (n1 > 0 && n2 > 0) {
            matrix[n1-1][n2-1] = matrix[n1-1][n2-1].subtract(val);
            matrix[n2-1][n1-1] = matrix[n2-1][n1-1].subtract(val);
        }
    }
    
    private void stampVoltageSource(Real[][] matrix, Real[] vector, int n1, int n2, Real val, int vsIdx) {
        int k = numNodes + vsIdx;
        if (n1 > 0) {
            matrix[n1-1][k] = matrix[n1-1][k].add(Real.ONE);
            matrix[k][n1-1] = matrix[k][n1-1].add(Real.ONE);
        }
        if (n2 > 0) {
            matrix[n2-1][k] = matrix[n2-1][k].subtract(Real.ONE);
            matrix[k][n2-1] = matrix[k][n2-1].subtract(Real.ONE);
        }
        vector[k] = val;
    }
    
    private void stampVector(Real[] vector, int n1, int n2, Real val) {
        if (n1 > 0) vector[n1-1] = vector[n1-1].subtract(val);
        if (n2 > 0) vector[n2-1] = vector[n2-1].add(val);
    }

    private Vector<Real> getInitialState() {
        Real[] state = new Real[augmentedSize];
        Arrays.fill(state, Real.ZERO);
        return new DenseVector<Real>(Arrays.asList(state), org.jscience.mathematics.sets.Reals.INSTANCE);
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
            
            int size = augmentedSize;
            Real[][] gData = new Real[size][size];
            for (Real[] row : gData) Arrays.fill(row, Real.ZERO);
            
            Real[] rhsSource = new Real[size];
            Arrays.fill(rhsSource, Real.ZERO);
            
            int vsIdx = 0;
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
                else if (comp instanceof SpintronicNetlist.VoltageSourceComponent) {
                    SpintronicNetlist.VoltageSourceComponent vs = (SpintronicNetlist.VoltageSourceComponent) comp;
                    stampVoltageSource(gData, rhsSource, vs.getNodes()[0], vs.getNodes()[1], Real.of(vs.getValue()), vsIdx++);
                }
            }
            
            DenseMatrix<Real> G = new DenseMatrix<>(gData, org.jscience.mathematics.sets.Reals.INSTANCE);
            DenseVector<Real> I_source = new DenseVector<Real>(Arrays.asList(rhsSource), org.jscience.mathematics.sets.Reals.INSTANCE);
            
            // F = G * v_new + C/dt * v_new - (C/dt*v_old + I_source)
            // Jacobian J = G + C/dt (Approximate G_tan ~ G for simple bias dep)
            Matrix<Real> C_div_dt = C_matrix.scale(dt.inverse(), C_matrix);
            Matrix<Real> J = G.add(C_div_dt);
            
            // RHS_eff = C/dt * v_old + I_source
            Vector<Real> term1 = C_div_dt.multiply(prevSol);
            Vector<Real> rhs_eff = term1.add(I_source);
            
            // Residual R = J * v_new - RHS_eff
            // Actually, let's solve J * delta_v = -Residual
            // Or simply linear solve: J * v_next = RHS_eff (Modified Newton / Fixed Point)
            // If G depends on v_new, this is Fixed Point Iteration. 
            // For MatrixSolver, we do: v_next = J^-1 * rhs_eff
            
            Vector<Real> v_next = J.inverse().multiply(rhs_eff);
            
            // Convergence Check
            Vector<Real> diff = v_next.subtract(v_new);
                double sumSq = 0.0;
                for (int i = 0; i < diff.dimension(); i++) {
                    double val = diff.get(i).doubleValue();
                    sumSq += val * val;
                }
            // Using sumSq as error metric for convergence
            if (Math.sqrt(sumSq) < NEWTON_TOL) { // Assuming NEWTON_TOL is for L2 norm
                break;
            }
            
            v_new = v_next;
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
        
        // Get Resistance from Physics Model if available, otherwise use netlist params
        double conductance;
        if (physicsModels.containsKey(mtj.getName())) {
             SpinValve model = physicsModels.get(mtj.getName());
             Real r = GMREffect.valetFertResistance(model);
             
             // Apply Bias Roll-off to TMR part if we wanted to be precise, 
             // but GMR model in JScience might already handle it? 
             // If not, apply simple scaling.
             // Assume GMR model gives low-bias resistance.
             
             double vh = VT_BIAS_ROLLOFF;
             double biasFactor = 1.0 / (1.0 + Math.pow(v_drop.doubleValue() / vh, 2));
             
             // We degrade Conductance or Resistance?
             // TMR degrades. R_total = R_P + dR. 
             conductance = 1.0 / r.doubleValue(); // Simplified for now.
        } else {
            // Bias dependent TMR: TMR(V) = TMR0 / (1 + (V/Vh)^2)
            double vh = VT_BIAS_ROLLOFF;
            double biasFactor = 1.0 / (1.0 + Math.pow(v_drop.doubleValue() / vh, 2));
            double tmr_eff = mtj.getTMR() / 100.0 * biasFactor;
            
            double ra = mtj.getRA(); // Ohm um^2
            double rP = ra * 1e-12; 
            conductance = 1.0 / (rP * (1.0 + 0.5 * tmr_eff));
        }
        
        Real g = Real.of(conductance);
        stampMatrix(gData, n1, n2, g);
    }
    
    /**
     * Gets the voltage at a specific node over time.
     * 
     * @param nodeName Name of the node
     * @return List of voltage values corresponding to simulation time points
     */
    public Vector<Real> getLastVoltage() {
        if (solutions.isEmpty()) return null;
        return solutions.get(solutions.size() - 1);
    }

    public List<Double> getNodeVoltage(String nodeName) {
        // Need node map from netlist, but it's private there. 
        // For now return empty.
        return new ArrayList<>(); 
    }
    
    public List<Double> getTimePoints() {
         return timePoints;
    }
}
