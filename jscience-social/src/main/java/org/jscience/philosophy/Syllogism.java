package org.jscience.philosophy;

/**
 * Represents a logical syllogism (Major Premise, Minor Premise, Conclusion).
 */
public class Syllogism {

    private final Premise majorPremise;
    private final Premise minorPremise;
    private final Premise conclusion;

    public Syllogism(Premise majorPremise, Premise minorPremise, Premise conclusion) {
        this.majorPremise = majorPremise;
        this.minorPremise = minorPremise;
        this.conclusion = conclusion;
    }

    public Premise getMajorPremise() {
        return majorPremise;
    }

    public Premise getMinorPremise() {
        return minorPremise;
    }

    public Premise getConclusion() {
        return conclusion;
    }

    public boolean isSound() {
        // A sound argument is valid and its premises are true.
        // We only check premise truth here as validity requires advanced parsing.
        return majorPremise.isTrue() && minorPremise.isTrue() && conclusion.isTrue();
    }

    @Override
    public String toString() {
        return String.format("Major: %s\nMinor: %s\nTherefore: %s",
                majorPremise.getStatement(), minorPremise.getStatement(), conclusion.getStatement());
    }
}
