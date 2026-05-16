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

package org.episteme.natural.device.sim;

import org.episteme.core.device.sim.AbstractSimulatedActuator;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.core.measure.quantity.Frequency;
import org.episteme.core.measure.quantity.Length;
import org.episteme.natural.device.actuators.Centrifuge;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;

/**
 * Simulated implementation of Centrifuge.
 */
public class SimulatedCentrifuge extends AbstractSimulatedActuator<Quantity<Frequency>> implements Centrifuge {

    private final Quantity<Frequency> maxRPM;
    private final Quantity<Dimensionless> maxRCF;
    private final RotorType rotorType;
    private Quantity<Frequency> currentRPM = Quantities.create(0.0, Units.HERTZ);
    private boolean running = false;

    public SimulatedCentrifuge(Identification id, Quantity<Frequency> maxRPM, Quantity<Dimensionless> maxRCF, RotorType rotorType) {
        super(id);
        this.maxRPM = maxRPM;
        this.maxRCF = maxRCF;
        this.rotorType = rotorType;
    }

    @Override
    public void stop() {
        this.currentRPM = Quantities.create(0.0, Units.HERTZ);
        this.running = false;
    }

    @Override
    public Quantity<Dimensionless> calculateRCF(Quantity<Length> radius) {
        // RCF = 1.118e-5 * r_cm * (rpm)^2
        double r_cm = radius.to(Units.CENTIMETER).getValue().doubleValue();
        double rpm = currentRPM.to(Units.HERTZ).getValue().doubleValue() * 60.0;
        return Quantities.create(1.118e-5 * r_cm * rpm * rpm, Units.ONE);
    }

    @Override
    public Quantity<Frequency> getMaxRPM() {
        return maxRPM;
    }

    @Override
    public Quantity<Dimensionless> getMaxRCF() {
        return maxRCF;
    }

    @Override
    public RotorType getRotorType() {
        return rotorType;
    }

    @Override
    public Quantity<Frequency> getCurrentRPM() {
        return currentRPM;
    }

    @Override
    public void start(Quantity<Frequency> rpm) {
        if (rpm.compareTo(maxRPM) > 0) {
            throw new IllegalArgumentException("RPM exceeds maximum");
        }
        this.currentRPM = rpm;
        this.running = true;
    }

    @Override
    public boolean isRunning() {
        return running || currentRPM.getValue().doubleValue() > 0;
    }

    @Override
    public void send(Quantity<Frequency> command) throws IOException {
        start(command);
    }
}
