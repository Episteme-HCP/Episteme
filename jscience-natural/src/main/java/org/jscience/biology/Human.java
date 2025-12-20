/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology;

import java.time.LocalDate;

/**
 * Represents a human individual.
 * <p>
 * Extends Individual with human-specific attributes like blood type, ethnicity,
 * and vital signs. Designed to integrate with jscience-social Person class.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Human extends Individual {

    private HomoSapiens.BloodType bloodType;
    private String ethnicity;
    private double heightMeters;
    private double weightKg;
    private String eyeColor;
    private String hairColor;

    public Human(String id, Individual.Sex sex, LocalDate birthDate) {
        super(id, HomoSapiens.SPECIES, sex, birthDate);
    }

    public Human(String id, Individual.Sex sex) {
        super(id, HomoSapiens.SPECIES, sex);
    }

    /**
     * Creates a Human with a generated UUID.
     */
    public Human(Individual.Sex sex, LocalDate birthDate) {
        this(new org.jscience.util.id.UUIDGenerator().generate(), sex, birthDate);
    }

    /**
     * Creates a Human with a generated ID from a specific generator.
     */
    public Human(org.jscience.util.id.IdGenerator generator, Individual.Sex sex, LocalDate birthDate) {
        this(generator.generate(), sex, birthDate);
    }

    // ========== Human-specific getters/setters ==========
    public HomoSapiens.BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(HomoSapiens.BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public double getHeightMeters() {
        return heightMeters;
    }

    public void setHeightMeters(double heightMeters) {
        this.heightMeters = heightMeters;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    // ========== Derived metrics ==========

    /**
     * Calculates Body Mass Index (BMI).
     */
    public double getBMI() {
        if (heightMeters <= 0)
            return 0;
        return weightKg / (heightMeters * heightMeters);
    }

    /**
     * Returns BMI category.
     */
    public String getBMICategory() {
        double bmi = getBMI();
        if (bmi < 18.5)
            return "Underweight";
        if (bmi < 25)
            return "Normal weight";
        if (bmi < 30)
            return "Overweight";
        return "Obese";
    }

    /**
     * Calculates Body Surface Area (BSA) using Du Bois formula.
     * Result in square meters.
     */
    public double getBSA() {
        return 0.007184 * Math.pow(heightMeters * 100, 0.725) * Math.pow(weightKg, 0.425);
    }

    /**
     * Checks blood type compatibility for transfusion.
     * 
     * @param donor the potential donor's blood type
     * @return true if can receive blood from donor
     */
    public boolean canReceiveBloodFrom(HomoSapiens.BloodType donor) {
        if (bloodType == null || donor == null)
            return false;

        // O- is universal donor
        if (donor == HomoSapiens.BloodType.O_NEGATIVE)
            return true;

        // AB+ is universal recipient
        if (bloodType == HomoSapiens.BloodType.AB_POSITIVE)
            return true;

        // Same type always compatible
        if (bloodType == donor)
            return true;

        // Simplified compatibility - check ABO and Rh
        String recipientABO = bloodType.name().substring(0, bloodType.name().indexOf('_'));
        String donorABO = donor.name().substring(0, donor.name().indexOf('_'));
        boolean recipientRhPlus = bloodType.name().endsWith("POSITIVE");
        boolean donorRhPlus = donor.name().endsWith("POSITIVE");

        // Rh- can only receive Rh-
        if (!recipientRhPlus && donorRhPlus)
            return false;

        // O can donate to anyone (ABO), A to A/AB, B to B/AB
        if (donorABO.equals("O"))
            return true;
        if (recipientABO.equals("AB"))
            return true;
        return recipientABO.equals(donorABO);
    }

    @Override
    public String toString() {
        return String.format("Human[%s: %s, %d years, %.2fm, %.1fkg, BMI=%.1f]",
                getId(), getSex(), getAge(), heightMeters, weightKg, getBMI());
    }
}
