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

import org.episteme.core.device.sim.SimulatedDevice;
import org.episteme.core.device.Sensor;
import org.episteme.natural.device.instruments.WeatherStation;
import org.episteme.natural.device.sensors.HumidityProbe;
import org.episteme.natural.device.sensors.PressureGauge;
import org.episteme.natural.device.sensors.TemperatureProbe;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;

import java.io.IOException;
import java.util.List;

/**
 * Simulated implementation of WeatherStation.
 * A WeatherStation is a complex instrument aggregating temperature, humidity and pressure sensors.
 */
public class SimulatedWeatherStation extends SimulatedDevice implements WeatherStation {

    private final TemperatureProbe temperatureProbe;
    private final HumidityProbe humidityProbe;
    private final PressureGauge pressureGauge;

    public SimulatedWeatherStation(Identification id) {
        super(id);
        String baseId = id.toString();
        this.temperatureProbe = new SimulatedTemperatureProbe(new SimpleIdentification(baseId + "-temp"), TemperatureProbe.ProbeType.THERMISTOR, 233.15, 358.15);
        this.humidityProbe = new SimulatedHumidityProbe(new SimpleIdentification(baseId + "-humidity"));
        this.pressureGauge = new SimulatedPressureGauge(new SimpleIdentification(baseId + "-pressure"), PressureGauge.GaugeType.BOURDON, 80000, 120000);
        
        setManufacturer("MeteoSim Corp.");
    }

    @Override
    public void connect() throws IOException {
        super.connect();
        temperatureProbe.connect();
        humidityProbe.connect();
        pressureGauge.connect();
    }

    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        temperatureProbe.disconnect();
        humidityProbe.disconnect();
        pressureGauge.disconnect();
    }

    @Override
    public TemperatureProbe getTemperatureProbe() {
        return temperatureProbe;
    }

    @Override
    public HumidityProbe getHumidityProbe() {
        return humidityProbe;
    }

    @Override
    public PressureGauge getPressureGauge() {
        return pressureGauge;
    }

    @Override
    public List<Sensor<?>> getSensors() {
        return List.of(temperatureProbe, humidityProbe, pressureGauge);
    }
}
