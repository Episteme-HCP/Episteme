/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.device.sim;

import org.episteme.core.device.Sensor;
import org.episteme.core.device.Actuator;
import org.episteme.core.device.sim.AbstractSimulatedDevice;
import org.episteme.social.device.actuators.VotingMachine;
import org.episteme.social.device.sensors.VoterScanner;
import org.episteme.social.device.actuators.BallotCaster;
import java.util.List;
import java.io.IOException;

/**
 * A simulated voting machine for testing social systems.
 * Implemented as a complex instrument with a scanner and a caster.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedVotingMachine extends AbstractSimulatedDevice implements VotingMachine {

    private final SimulatedVoterScanner scanner;
    private final SimulatedBallotCaster caster;

    public SimulatedVotingMachine(String name) {
        super(name);
        this.scanner = new SimulatedVoterScanner(name + " Scanner");
        this.caster = new SimulatedBallotCaster(name + " Caster");
    }

    @Override
    public VoterScanner getVoterScanner() {
        return scanner;
    }

    @Override
    public BallotCaster getBallotCaster() {
        return caster;
    }

    @Override
    public List<Sensor<?>> getSensors() {
        return List.of(scanner);
    }

    @Override
    public List<Actuator<?>> getActuators() {
        return List.of(caster);
    }

    @Override
    public void connect() throws IOException {
        super.connect();
        scanner.connect();
        caster.connect();
    }

    @Override
    public void disconnect() throws IOException {
        scanner.disconnect();
        caster.disconnect();
        super.disconnect();
    }
}



