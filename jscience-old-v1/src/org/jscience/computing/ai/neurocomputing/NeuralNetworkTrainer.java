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

package org.jscience.computing.ai.neurocomputing;

import org.jscience.util.Steppable;

/**
 * This class is intended to provide a common interface for the more complicated
 * neural network training algorithms, such as a multi-stage Kohonen training
 * algorithm. {@link org.jscience.computing.ai.neurocomputing.NeuralNetwork#train(double[],double[])
 * should only implement one iteration of the training algorithm, and if this
 * algorithm has multiple stages, variables or other complicating factors, it
 * may be easier to use <code>NeuralNetworkTrainer</code> to provide the overall
 * framework.
 * <p/>
 * In addition to this, a <code>NeuralNetworkTrainer</code>-derived class can be
 * created to deal with a specific training situation. Continuing with our Kohonen
 * example, you could create a trainer for simple 2D shapes, mapping RGB colours,
 * or complex image association.
 * <p/>
 * Finally, <code>NeuralNetworkTrainer</code> also implements <code>Steppable</code>
 * adding greater flexibility/integration when creating demonstration applets/
 * applications.
 *
 * @author James Matthews
 * @see NeuralNetwork#train(double[],double[])
 */
public abstract class NeuralNetworkTrainer implements Steppable {
    /**
     * A simple interface that allows a <code>NeuralNetworkTrainer</code> to notify
     * additional classes about events.
     */
    protected Notifier notifier;

    /**
     * Creates a new instance of NeuralNetworkTrainer
     */
    public NeuralNetworkTrainer() {
    }

    /**
     * Set the network being used by the trainer.
     *
     * @param net the network to be trained.
     */
    public abstract void setNetwork(NeuralNetwork net);

    /**
     * Return the network being used.
     *
     * @return the network currently being trained.
     */
    public abstract NeuralNetwork getNetwork();

    /**
     * Set the notifier for this class.
     *
     * @param notify the neural network notifier.
     */
    public void setNotifier(Notifier notify) {
        notifier = notify;
    }

    /**
     * Returns true is training is complete.
     *
     * @return true, if training has finished, otherwise false.
     */
    public abstract boolean isComplete();

    /**
     * Advance the training one step.
     */
    public abstract void doStep();

    /**
     * Initialize the training algorithm.
     */
    public abstract void init();

    /**
     * Reset the training algorithm (defaults to calling <code>init</code>).
     */
    public void reset() {
        init();
    }

    /**
     * Run the training algorithm. This method is simply defined as:
     * <code>
     * <b>public void</b> run() {
     * <b>while</b> (!isComplete()) {
     * doStep();
     * }
     * }
     * </code>
     */
    public void run() {
        while (!isComplete()) {
            doStep();
        }
    }
}
