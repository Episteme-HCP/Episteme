/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.social.sociology;


import java.time.LocalDate;
import java.util.*;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Area;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.quantity.Mass;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.social.psychology.social.Biography;
import org.jscience.social.psychology.BigFiveProfile;
import org.jscience.natural.biology.Individual;
import org.jscience.natural.biology.BiologicalSex;
import org.jscience.natural.biology.BloodType;
import org.jscience.natural.biology.HomoSapiens;

/**
 * Represents a human individual.
 * Combines biological biometric data with historical and biographical records.
 * Modernized to use Real for measurements and ExtensibleEnums for biometric traits.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Human extends Individual {

    private static final long serialVersionUID = 5L;

    // Biometric Data
    @Attribute
    private BloodType bloodType;
    @Attribute
    private String ethnicity;
    @Attribute
    private Quantity<Length> height;
    @Attribute
    private Quantity<Mass> weight;
    @Attribute
    private String eyeColor;
    @Attribute
    private String hairColor;

    // Names
    @Attribute
    private String givenName;
    @Attribute
    private String lastName;
    @Attribute
    private String usedName;

    // Historical & Biographical Data
    @Attribute
    private final Set<HumanRole> humanRoles = new HashSet<>();
    @Attribute
    private String title;
    @Attribute
    private String nationality;
    
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TimeCoordinate birthWhen;
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TimeCoordinate deathWhen;
    
    @Attribute
    private Biography biography;

    @Attribute
    private BigFiveProfile personality;

    /**
     * Creates a new human with a specific ID.
     */
    public Human(org.jscience.core.util.identity.Identification id, BiologicalSex sex, LocalDate birthDate) {
        super(id, HomoSapiens.SPECIES, sex, birthDate);
        this.height = org.jscience.core.measure.Quantities.create(Real.ZERO, Units.METER);
        this.weight = org.jscience.core.measure.Quantities.create(Real.ZERO, Units.KILOGRAM);
    }

    /**
     * Helper constructor for String IDs.
     */
    public Human(String id, BiologicalSex sex, LocalDate birthDate) {
        this(new org.jscience.core.util.identity.UUIDIdentification(UUID.fromString(id)), sex, birthDate);
    }

    /**
     * Creates a new human with a generated UUID.
     */
    public Human(BiologicalSex sex, LocalDate birthDate) {
        this(new org.jscience.core.util.identity.UUIDIdentification(UUID.randomUUID()), sex, birthDate);
    }

    public Human(org.jscience.core.util.identity.Identification id, BiologicalSex sex) {
        this(id, sex, null);
    }

    public Human(String id, BiologicalSex sex) {
        this(new org.jscience.core.util.identity.UUIDIdentification(UUID.fromString(id)), sex, null);
    }

    // Biometric Getters & Setters
    public BloodType getBloodType() { return bloodType; }
    public void setBloodType(BloodType bloodType) { this.bloodType = bloodType; }
 
    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }
 
    public Quantity<Length> getHeight() { return height; }
    public void setHeight(Quantity<Length> height) { this.height = height; }
    public void setHeightMeters(Real meters) { this.height = org.jscience.core.measure.Quantities.create(meters, Units.METER); }
    public void setHeightMeters(double meters) { setHeightMeters(Real.of(meters)); }
 
    public Quantity<Mass> getWeight() { return weight; }
    public void setWeight(Quantity<Mass> weight) { this.weight = weight; }
    public void setWeightKg(Real kg) { this.weight = org.jscience.core.measure.Quantities.create(kg, Units.KILOGRAM); }
    public void setWeightKg(double kg) { setWeightKg(Real.of(kg)); }

    public String getEyeColor() { return eyeColor; }
    public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }

    public String getHairColor() { return hairColor; }
    public void setHairColor(String hairColor) { this.hairColor = hairColor; }

    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsedName() { return usedName; }
    public void setUsedName(String usedName) { this.usedName = usedName; }

    public String getFullName() {
        if (usedName != null && !usedName.isBlank()) return usedName;
        return (givenName != null ? givenName + " " : "") + (lastName != null ? lastName : "");
    }

    public BigFiveProfile getPersonality() { return personality; }
    public void setPersonality(BigFiveProfile personality) { this.personality = personality; }

    public Set<HumanRole> getHumanRoles() { return Collections.unmodifiableSet(humanRoles); }
    public void addHumanRole(HumanRole role) { humanRoles.add(Objects.requireNonNull(role, "Role cannot be null")); }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public TimeCoordinate getBirthWhen() { return birthWhen; }
    public void setBirthWhen(TimeCoordinate birthWhen) { this.birthWhen = birthWhen; }

    public TimeCoordinate getDeathWhen() { return deathWhen; }
    public void setDeathWhen(TimeCoordinate deathWhen) { this.deathWhen = deathWhen; }

    public String getBiographicalSummary() { 
        return biography != null ? (String)biography.getTrait("summary") : (String)getTrait("biographicalSummary"); 
    }
    public void setBiographicalSummary(String summary) { 
        if (biography != null) biography.setTrait("summary", summary);
        else setTrait("biographicalSummary", summary);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAchievements() { 
        Object val = getTrait("achievements");
        return val != null ? (List<String>) val : new ArrayList<>();
    }
    
    @SuppressWarnings("unchecked")
    public void addAchievement(String achievement) { 
        List<String> list = (List<String>) getTrait("achievements");
        if (list == null) {
            list = new ArrayList<>();
            setTrait("achievements", list);
        }
        list.add(Objects.requireNonNull(achievement)); 
    }

    public Quantity<?> getBMI() {
        if (height == null || height.getValue().isZero()) return org.jscience.core.measure.Quantities.create(Real.ZERO, Units.ONE);
        // BMI = mass / height^2 [kg/m^2]
        return weight.divide(height.pow(2));
    }
 
    public String getBMICategory() {
        double bmi = getBMI().getValue().doubleValue();
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal weight";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }
 
    public Quantity<Area> getBSA() {
        if (height == null || weight == null) return org.jscience.core.measure.Quantities.create(Real.ZERO, Units.SQUARE_METER);
        
        // Du Bois formula: BSA = 0.007184 * height^0.725 * weight^0.425
        // height in cm, weight in kg. Result in m^2.
        double hCm = height.to(Units.CENTIMETER).getValue().doubleValue();
        double wKg = weight.to(Units.KILOGRAM).getValue().doubleValue();
        double bsa = 0.007184 * Math.pow(hCm, 0.725) * Math.pow(wKg, 0.425);
        
        return org.jscience.core.measure.Quantities.create(bsa, Units.SQUARE_METER);
    }

    public boolean canReceiveBloodFrom(BloodType donor) {
        if (bloodType == null || donor == null) return false;
        if (donor == BloodType.O_NEGATIVE) return true;
        if (bloodType == BloodType.AB_POSITIVE) return true;
        if (bloodType == donor) return true;

        String recipientABO = bloodType.name().substring(0, bloodType.name().indexOf('_'));
        String donorABO = donor.name().substring(0, donor.name().indexOf('_'));
        boolean recipientRhPlus = bloodType.name().endsWith("POSITIVE");
        boolean donorRhPlus = donor.name().endsWith("POSITIVE");

        if (!recipientRhPlus && donorRhPlus) return false;
        if (donorABO.equals("O")) return true;
        if (recipientABO.equals("AB")) return true;
        return recipientABO.equals(donorABO);
    }

    @Override
    public String toString() {
        String base = String.format("Human[%s (%s): %s, %d years, %s, %s]",
                getFullName(), getId(), getSex(), getAge(), 
                height != null ? height.toString() : "0m", 
                weight != null ? weight.toString() : "0kg");
        if (title != null) base = title + " " + base;
        return base;
    }
}

