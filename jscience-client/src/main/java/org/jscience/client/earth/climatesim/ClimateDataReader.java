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

package org.jscience.client.earth.climatesim;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.io.AbstractResourceReader;

import java.io.File;

/**
 * Loader for Climate Model Data (JSON Format).
 */
public class ClimateDataReader extends AbstractResourceReader<ClimateDataReader.ClimateState> {

    private final ObjectMapper mapper = new ObjectMapper();

    public static class ClimateState {
        public double[][][] temperature;
        public double[][] humidity;

        public ClimateState() {
        }

        public ClimateState(double[][][] temperature, double[][] humidity) {
            this.temperature = temperature;
            this.humidity = humidity;
        }
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    public Class<ClimateState> getResourceType() {
        return ClimateState.class;
    }

    @Override
    protected ClimateState loadFromSource(String path) throws Exception {
        return mapper.readValue(new File(path), ClimateState.class);
    }
}
