package org.jscience.biology.neuroscience;

/**
 * Synapse connecting two neurons.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Synapse {

    private final SpikingNeuron postSynaptic;
    private double weight;

    public Synapse(SpikingNeuron pre, SpikingNeuron post, double weight, double delay) {
        this.postSynaptic = post;
        this.weight = weight;

    }

    /**
     * Propagates spike from pre to post.
     * In a simple stepping simulation, this might be called if preSynaptic spiked.
     * 
     * @param current current injection magnitude (usually related to weight)
     */
    public void transmit() {
        // Simplified: instantaneous or handled by network queue
        // Here we just add current to post based on weight
        postSynaptic.addInputCurrent(weight);
        // Note: Realistically, weight is conductance or current multiplier.
        // If weight is Amps, we add directly.
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // STDP implementation could go here (modify weight based on spike timing)
}
