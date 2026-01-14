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

package org.jscience.physics.classical.waves.electromagnetism.circuit;

/**
 * Abstract base class for all circuit elements (resistors, capacitors, sources,
 * etc.).
 * This class defines the interface for circuit simulation using Modified Nodal
 * Analysis (MNA).
 *
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class CircuitElement {

    /** Node indices for each terminal of this element */
    protected int[] nodes;

    /** Voltage at each terminal */
    protected double[] volts;

    /** Current through the element */
    protected double current;

    /** Voltage source index (for elements that act as voltage sources) */
    protected int voltSource;

    /** Reference to the parent circuit (for stamping) */
    protected Circuit circuit;

    /** X coordinate of first terminal */
    protected int x;

    /** Y coordinate of first terminal */
    protected int y;

    /** X coordinate of second terminal */
    protected int x2;

    /** Y coordinate of second terminal */
    protected int y2;

    /**
     * Creates a new circuit element at (0,0) to (0,0).
     */
    public CircuitElement() {
        allocNodes();
    }

    /**
     * Sets the coordinates of the element's terminals.
     * 
     * @param x  X1
     * @param y  Y1
     * @param x2 X2
     * @param y2 Y2
     */
    public void setCoordinates(int x, int y, int x2, int y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Returns the coordinate of the specified post.
     * 
     * @param n Post index (0 or 1)
     * @return Array containing {x, y}
     */
    public int[] getPostPosition(int n) {
        if (n == 0)
            return new int[] { x, y };
        if (n == 1)
            return new int[] { x2, y2 };
        return new int[] { 0, 0 };
    }

    /**
     * Allocates arrays for nodes and voltages based on post count.
     */
    protected void allocNodes() {
        int totalNodes = getPostCount() + getInternalNodeCount();
        nodes = new int[totalNodes];
        volts = new double[totalNodes];
    }

    /**
     * Sets the parent circuit for this element.
     * 
     * @param circuit The circuit this element belongs to
     */
    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    /**
     * Returns the parent circuit.
     * 
     * @return The circuit this element belongs to
     */
    public Circuit getCircuit() {
        return circuit;
    }

    /**
     * Returns the number of external terminals (posts) on this element.
     * Most elements have 2 posts. Override for elements with more.
     * 
     * @return Number of external terminals
     */
    public int getPostCount() {
        return 2;
    }

    /**
     * Returns the number of internal nodes used by this element.
     * Internal nodes are used for complex devices like op-amps.
     * 
     * @return Number of internal nodes (default 0)
     */
    public int getInternalNodeCount() {
        return 0;
    }

    /**
     * Returns the number of independent voltage sources in this element.
     * Used for voltage sources and elements that model voltage sources.
     * 
     * @return Number of voltage sources (default 0)
     */
    public int getVoltageSourceCount() {
        return 0;
    }

    /**
     * Returns whether this element is nonlinear.
     * Nonlinear elements require iterative solving at each time step.
     * 
     * @return true if nonlinear, false otherwise
     */
    public boolean nonLinear() {
        return false;
    }

    /**
     * Sets the node index for a terminal.
     * 
     * @param post      Terminal number
     * @param nodeIndex Node index in the circuit
     */
    public void setNode(int post, int nodeIndex) {
        nodes[post] = nodeIndex;
    }

    /**
     * Returns the node index for a terminal.
     * 
     * @param post Terminal number
     * @return Node index
     */
    public int getNode(int post) {
        return nodes[post];
    }

    /**
     * Sets the voltage source index for this element.
     * 
     * @param n  Which voltage source (for multi-source elements)
     * @param vs The voltage source index
     */
    public void setVoltageSource(int n, int vs) {
        voltSource = vs;
    }

    /**
     * Returns the voltage source index.
     * 
     * @return Voltage source index
     */
    public int getVoltageSource() {
        return voltSource;
    }

    /**
     * Sets the voltage at a node.
     * Called by the circuit solver after computing voltages.
     * 
     * @param n       Node number
     * @param voltage The voltage
     */
    public void setNodeVoltage(int n, double voltage) {
        volts[n] = voltage;
    }

    /**
     * Calculates the current through this element.
     * Called after node voltages are updated.
     */
    public void calculateCurrent() {
        // Override in subclasses
    }

    /**
     * Returns the voltage difference across the element (post 0 to post 1).
     * 
     * @return Voltage difference
     */
    public double getVoltageDiff() {
        return volts[0] - volts[1];
    }

    /**
     * Returns the current through the element.
     * 
     * @return Current in amperes
     */
    public double getCurrent() {
        return current;
    }

    /**
     * Sets the current through the element.
     * 
     * @param n Which current (for multi-current elements)
     * @param c Current value
     */
    public void setCurrent(int n, double c) {
        current = c;
    }

    /**
     * Returns the power dissipated by this element.
     * 
     * @return Power in watts
     */
    public double getPower() {
        return getVoltageDiff() * current;
    }

    /**
     * Resets the element to initial state.
     */
    public void reset() {
        for (int i = 0; i < volts.length; i++) {
            volts[i] = 0;
        }
        current = 0;
    }

    /**
     * Stamps the element's contribution into the circuit matrix.
     * This is called once during circuit analysis to build the MNA matrix.
     * Linear elements stamp constant coefficients; nonlinear elements
     * stamp initial guesses that will be updated in {@link #doStep()}.
     */
    public abstract void stamp();

    /**
     * Performs one simulation step for this element.
     * Called during each iteration of the solver.
     * Nonlinear elements update their matrix contributions here.
     */
    public void doStep() {
        // Override in subclasses for nonlinear elements
    }

    /**
     * Called at the start of each simulation iteration.
     * Used for time-dependent elements to update their state.
     */
    public void startIteration() {
        // Override in subclasses if needed
    }

    /**
     * Returns whether terminals n1 and n2 are connected through this element.
     * Used for determining circuit connectivity.
     * 
     * @param n1 First terminal
     * @param n2 Second terminal
     * @return true if connected
     */
    public boolean getConnection(int n1, int n2) {
        return true;
    }

    /**
     * Returns whether terminal n has a ground connection.
     * 
     * @param n Terminal number
     * @return true if grounded
     */
    public boolean hasGroundConnection(int n) {
        return false;
    }
}


