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

package org.jscience.device.sim;

import org.jscience.device.instruments.WeatherStation;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Simulated implementation of WeatherStation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedWeatherStation extends SimulatedDevice implements WeatherStation {

    private final Random random = new Random();

    public SimulatedWeatherStation(String name) {
        super(name, "MeteoSim Corp.");
    }

    public SimulatedWeatherStation() {
        this("Simulated Weather Station");
    }

    @Override
    public double getTemperatureCelsius() {
        if (!isConnected())
            return 0;
        return 20.0 + random.nextGaussian() * 2.0;
    }

    @Override
    public double getHumidityPercent() {
        if (!isConnected())
            return 0;
        return 50.0 + random.nextGaussian() * 5.0;
    }

    @Override
    public double getPressureHPa() {
        if (!isConnected())
            return 0;
        return 1013.0 + random.nextGaussian() * 10.0;
    }
}
