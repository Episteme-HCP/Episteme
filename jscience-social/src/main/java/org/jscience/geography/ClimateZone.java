package org.jscience.geography;

/**
 * Represents a simplified Köppen climate classification.
 */
public class ClimateZone {

    public enum Type {
        TROPICAL, ARID, TEMPERATE, CONTINENTAL, POLAR
    }

    private final Type type;
    private final double averageTempCelsius;
    private final double annualRainfallMm;

    public ClimateZone(Type type, double avgTemp, double rainfall) {
        this.type = type;
        this.averageTempCelsius = avgTemp;
        this.annualRainfallMm = rainfall;
    }

    public Type getType() {
        return type;
    }

    public boolean isHabitable() {
        // Simplified habitability logic
        if (type == Type.POLAR)
            return false;
        if (type == Type.ARID && annualRainfallMm < 50)
            return false;
        return true;
    }

    public boolean supportsAgriculture() {
        return annualRainfallMm > 300 && averageTempCelsius > 10;
    }
}
