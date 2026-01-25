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

package org.jscience.sociology;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.temporal.TemporalCoordinate;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;
import org.jscience.util.identity.IDGenerator;
import org.jscience.util.identity.UUIDGenerator;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.psychology.social.Biography;
import org.jscience.biology.Individual;
import org.jscience.biology.HomoSapiens;

/**
 * Represents a human individual.
 * Combines biological biometric data with historical and biographical records.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Human extends Individual {

    private static final long serialVersionUID = 4L;

    /** Roles identifying the nature of a person's historical significance. */
    public enum Role {
        RULER, MILITARY, RELIGIOUS, PHILOSOPHER, SCIENTIST,
        ARTIST, EXPLORER, REVOLUTIONARY, REFORMER, INVENTOR
    }

    // Biometric Data
    @Attribute
    private HomoSapiens.BloodType bloodType;
    @Attribute
    private String ethnicity;
    @Attribute
    private Real heightMeters;
    @Attribute
    private Real weightKg;
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
    private final Set<Role> roles = EnumSet.noneOf(Role.class);
    @Attribute
    private String title;
    @Attribute
    private String nationality;
    
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TemporalCoordinate birthWhen;
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TemporalCoordinate deathWhen;
    
    @Attribute
    private Biography biography;
    
    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    /**
     * Creates a new human with a specific ID.
     */
    public Human(Identification id, Individual.Sex sex, LocalDate birthDate) {
        super(id, HomoSapiens.SPECIES, sex, birthDate);
        this.heightMeters = Real.ZERO;
        this.weightKg = Real.ZERO;
    }

    /**
     * Helper constructor for String IDs.
     */
    public Human(String id, Individual.Sex sex, LocalDate birthDate) {
        this(new SimpleIdentification(id), sex, birthDate);
    }

    /**
     * Creates a new human with a generated UUID.
     */
    public Human(Individual.Sex sex, LocalDate birthDate) {
        this(new UUIDGenerator().generate().getId(), sex, birthDate);
    }

    /**
     * Creates a new human with a custom ID generator.
     */
    public Human(IDGenerator generator, Individual.Sex sex, LocalDate birthDate) {
        this(generator.generate().getId(), sex, birthDate);
    }

    /**
     * Internal constructor for specific ID and sex.
     */
    public Human(Identification id, Individual.Sex sex) {
        this(id, sex, null);
    }

    /**
     * Helper constructor for specific String ID and sex.
     */
    public Human(String id, Individual.Sex sex) {
        this(new SimpleIdentification(id), sex, null);
    }

    // Biometric Getters & Setters
    public HomoSapiens.BloodType getBloodType() { return bloodType; }
    public void setBloodType(HomoSapiens.BloodType bloodType) { this.bloodType = bloodType; }

    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }

    public Real getHeightMeters() { return heightMeters; }
    public void setHeightMeters(Real heightMeters) { this.heightMeters = heightMeters; }
    public void setHeightMeters(double heightMeters) { this.heightMeters = Real.of(heightMeters); }

    public Real getWeightKg() { return weightKg; }
    public void setWeightKg(Real weightKg) { this.weightKg = weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = Real.of(weightKg); }

    public String getEyeColor() { return eyeColor; }
    public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }

    public String getHairColor() { return hairColor; }
    public void setHairColor(String hairColor) { this.hairColor = hairColor; }

    // Name Getters & Setters
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

    // Historical Getters & Setters
    public Set<Role> getRoles() { return Collections.unmodifiableSet(roles); }
    public void addRole(Role role) { roles.add(Objects.requireNonNull(role, "Role cannot be null")); }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public TemporalCoordinate getBirthWhen() { return birthWhen; }
    public void setBirthWhen(TemporalCoordinate birthWhen) { this.birthWhen = birthWhen; }

    public TemporalCoordinate getDeathWhen() { return deathWhen; }
    public void setDeathWhen(TemporalCoordinate deathWhen) { this.deathWhen = deathWhen; }

    public String getBiographicalSummary() { 
        return biography != null ? (String)biography.getTrait("summary") : (String)traits.get("biographicalSummary"); 
    }
    public void setBiographicalSummary(String summary) { 
        if (biography != null) biography.setTrait("summary", summary);
        else traits.put("biographicalSummary", summary);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAchievements() { 
        return (List<String>) traits.getOrDefault("achievements", new ArrayList<String>()); 
    }
    
    @SuppressWarnings("unchecked")
    public void addAchievement(String achievement) { 
        List<String> list = (List<String>) traits.get("achievements");
        if (list == null) {
            list = new ArrayList<>();
            traits.put("achievements", list);
        }
        list.add(Objects.requireNonNull(achievement)); 
    }

    /** BMI calculation */
    public Real getBMI() {
        if (heightMeters == null || heightMeters.isZero()) return Real.ZERO;
        return weightKg.divide(heightMeters.pow(2));
    }

    /** BMI category */
    public String getBMICategory() {
        double bmi = getBMI().doubleValue();
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal weight";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    /** Body Surface Area (Du Bois formula) */
    public Real getBSA() {
        if (heightMeters == null || weightKg == null) return Real.ZERO;
        Real heightCm = heightMeters.multiply(Real.of(100));
        return Real.of(0.007184)
                .multiply(Real.of(Math.pow(heightCm.doubleValue(), 0.725)))
                .multiply(Real.of(Math.pow(weightKg.doubleValue(), 0.425)));
    }

    /** Blood type compatibility check */
    public boolean canReceiveBloodFrom(HomoSapiens.BloodType donor) {
        if (bloodType == null || donor == null) return false;
        if (donor == HomoSapiens.BloodType.O_NEGATIVE) return true;
        if (bloodType == HomoSapiens.BloodType.AB_POSITIVE) return true;
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
        String base = String.format("Human[%s (%s): %s, %d years, %.2fm, %.1fkg]",
                getFullName(), getId(), getSex(), getAge(), 
                heightMeters != null ? heightMeters.doubleValue() : 0.0, 
                weightKg != null ? weightKg.doubleValue() : 0.0);
        if (title != null) base = title + " " + base;
        return base;
    }
}
