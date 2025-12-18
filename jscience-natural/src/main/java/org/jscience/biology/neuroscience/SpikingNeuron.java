package org.jscience.biology.neuroscience;

/**
 * Spiking Neuron model.
 * Leaky Integrate-and-Fire (LIF) implementation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SpikingNeuron {

    // Parameters
    private final double membraneResistance; // Rm (Ohm)

    private final double thresholdPotential; // Vth (Volts)
    private final double restingPotential; // Vrest (Volts)
    private final double resetPotential; // Vreset (Volts)
    private final double refractoryPeriod; // seconds

    // State
    private double membranePotential; // Vm
    private double lastSpikeTime;
    private double currentInput; // I(t)

    // Derived
    private final double tau; // Time constant = Rm * Cm

    public SpikingNeuron(double rm, double cm, double vTh, double vRest, double vReset, double refract) {
        this.membraneResistance = rm;
        this.thresholdPotential = vTh;
        this.restingPotential = vRest;
        this.resetPotential = vReset;
        this.refractoryPeriod = refract;
        this.tau = rm * cm;

        reset();
    }

    public void reset() {
        this.membranePotential = restingPotential;
        this.lastSpikeTime = Double.NEGATIVE_INFINITY;
        this.currentInput = 0;
    }

    /**
     * Adds input current for this time step.
     */
    public void addInputCurrent(double current) {
        this.currentInput += current;
    }

    /**
     * Updates neuron state.
     * dV/dt = (-(V - Vrest) + R*I) / tau
     * 
     * @param dt          Time step (seconds)
     * @param currentTime Current simulation time
     * @return true if neuron spiked
     */
    public boolean update(double dt, double currentTime) {
        // Refractory period check
        if (currentTime - lastSpikeTime < refractoryPeriod) {
            membranePotential = resetPotential;
            currentInput = 0; // Clear input
            return false;
        }

        // Euler integration
        double dV = (-(membranePotential - restingPotential) + membraneResistance * currentInput) / tau * dt;
        membranePotential += dV;

        currentInput = 0; // Reset input for next step

        // Spike check
        if (membranePotential >= thresholdPotential) {
            membranePotential = resetPotential;
            lastSpikeTime = currentTime;
            return true;
        }

        return false;
    }

    public double getMembranePotential() {
        return membranePotential;
    }
}
