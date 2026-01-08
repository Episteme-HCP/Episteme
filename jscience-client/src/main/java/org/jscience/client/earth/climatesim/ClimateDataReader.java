package org.jscience.client.earth.climatesim;

import java.io.File;

public class ClimateDataReader {
    public ClimateState load(String path) {
        return new ClimateState(new double[0][0][0], new double[0][0]);
    }
    
    public void load(File file) {
        // Stub
    }

    public static class ClimateState {
        public double[][][] temperature;
        public double[][] humidity;

        public ClimateState(double[][][] temperature, double[][] humidity) {
            this.temperature = temperature;
            this.humidity = humidity;
        }

        public ClimateState() {}
    }
}
