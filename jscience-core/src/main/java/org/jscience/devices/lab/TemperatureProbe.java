/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.devices.lab;

import org.jscience.devices.Device;
import org.jscience.mathematics.numbers.real.Real;

import java.io.IOException;

/**
 * Represents a temperature probe for precise temperature measurement.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TemperatureProbe implements Device {

    public enum ProbeType {
        THERMOCOUPLE, RTD, THERMISTOR, INFRARED
    }

    private final String name;
    private final ProbeType type;
    private final Real accuracy;
    private final Real minTemp;
    private final Real maxTemp;
    private Real lastReading;
    private boolean connected;

    public TemperatureProbe(String name, ProbeType type, Real accuracy, Real minTemp, Real maxTemp) {
        this.name = name;
        this.type = type;
        this.accuracy = accuracy;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.lastReading = Real.of(293.15);
    }

    @Override
    public void connect() throws IOException {
        this.connected = true;
    }

    @Override
    public void disconnect() throws IOException {
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    public Real measure(Real actualTemp) {
        if (actualTemp.compareTo(minTemp) < 0 || actualTemp.compareTo(maxTemp) > 0) {
            throw new IllegalArgumentException("Temperature out of probe range");
        }
        Real variation = accuracy.multiply(Real.of(Math.random() * 2 - 1));
        lastReading = actualTemp.add(variation);
        return lastReading;
    }

    public Real read() {
        return lastReading;
    }

    public static Real kelvinToCelsius(Real kelvin) {
        return kelvin.subtract(Real.of(273.15));
    }

    public static Real celsiusToKelvin(Real celsius) {
        return celsius.add(Real.of(273.15));
    }

    public ProbeType getType() {
        return type;
    }

    public Real getAccuracy() {
        return accuracy;
    }

    public Real getMinTemp() {
        return minTemp;
    }

    public Real getMaxTemp() {
        return maxTemp;
    }

    public static TemperatureProbe thermocouple() {
        return new TemperatureProbe("Type K Thermocouple", ProbeType.THERMOCOUPLE,
                Real.of(2.2), Real.of(73), Real.of(1523));
    }
}
