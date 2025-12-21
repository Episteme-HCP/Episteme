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
package org.jscience.biology;

import java.time.LocalDate;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a human individual.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Human extends Individual {

    private HomoSapiens.BloodType bloodType;
    private String ethnicity;
    private Real heightMeters;
    private Real weightKg;
    private String eyeColor;
    private String hairColor;

    public Human(String id, Individual.Sex sex, LocalDate birthDate) {
        super(id, HomoSapiens.SPECIES, sex, birthDate);
        this.heightMeters = Real.ZERO;
        this.weightKg = Real.ZERO;
    }

    public Human(String id, Individual.Sex sex) {
        super(id, HomoSapiens.SPECIES, sex);
        this.heightMeters = Real.ZERO;
        this.weightKg = Real.ZERO;
    }

    public Human(Individual.Sex sex, LocalDate birthDate) {
        this(new org.jscience.util.id.UUIDGenerator().generate(), sex, birthDate);
    }

    public Human(org.jscience.util.id.IdGenerator generator, Individual.Sex sex, LocalDate birthDate) {
        this(generator.generate(), sex, birthDate);
    }

    // Getters
    public HomoSapiens.BloodType getBloodType() {
        return bloodType;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public Real getHeightMeters() {
        return heightMeters;
    }

    public Real getWeightKg() {
        return weightKg;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    // Setters
    public void setBloodType(HomoSapiens.BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public void setHeightMeters(Real heightMeters) {
        this.heightMeters = heightMeters;
    }

    public void setHeightMeters(double heightMeters) {
        this.heightMeters = Real.of(heightMeters);
    }

    public void setWeightKg(Real weightKg) {
        this.weightKg = weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = Real.of(weightKg);
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    /** BMI calculation */
    public Real getBMI() {
        if (heightMeters.isZero())
            return Real.ZERO;
        return weightKg.divide(heightMeters.pow(2));
    }

    /** BMI category */
    public String getBMICategory() {
        double bmi = getBMI().doubleValue();
        if (bmi < 18.5)
            return "Underweight";
        if (bmi < 25)
            return "Normal weight";
        if (bmi < 30)
            return "Overweight";
        return "Obese";
    }

    /** Body Surface Area (Du Bois formula) */
    public Real getBSA() {
        Real heightCm = heightMeters.multiply(Real.of(100));
        return Real.of(0.007184)
                .multiply(Real.of(Math.pow(heightCm.doubleValue(), 0.725)))
                .multiply(Real.of(Math.pow(weightKg.doubleValue(), 0.425)));
    }

    /** Blood type compatibility check */
    public boolean canReceiveBloodFrom(HomoSapiens.BloodType donor) {
        if (bloodType == null || donor == null)
            return false;
        if (donor == HomoSapiens.BloodType.O_NEGATIVE)
            return true;
        if (bloodType == HomoSapiens.BloodType.AB_POSITIVE)
            return true;
        if (bloodType == donor)
            return true;

        String recipientABO = bloodType.name().substring(0, bloodType.name().indexOf('_'));
        String donorABO = donor.name().substring(0, donor.name().indexOf('_'));
        boolean recipientRhPlus = bloodType.name().endsWith("POSITIVE");
        boolean donorRhPlus = donor.name().endsWith("POSITIVE");

        if (!recipientRhPlus && donorRhPlus)
            return false;
        if (donorABO.equals("O"))
            return true;
        if (recipientABO.equals("AB"))
            return true;
        return recipientABO.equals(donorABO);
    }

    @Override
    public String toString() {
        return String.format("Human[%s: %s, %d years, %.2fm, %.1fkg, BMI=%.1f]",
                getId(), getSex(), getAge(), heightMeters.doubleValue(), weightKg.doubleValue(),
                getBMI().doubleValue());
    }
}