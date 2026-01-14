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

package org.jscience.medicine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a patient in a medical context.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Patient {

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    private final String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    
    // Medical Record
    private final List<Diagnostic> medicalHistory = new ArrayList<>();
    private final List<Medication> currentMedications = new ArrayList<>();
    private final List<String> allergies = new ArrayList<>();
    private VitalSigns currentVitalSigns;

    public Patient(String firstName, String lastName) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void addCondition(Diagnostic condition) {
        medicalHistory.add(condition);
    }

    public List<Diagnostic> getMedicalHistory() {
        return Collections.unmodifiableList(medicalHistory);
    }

    public void prescribe(Medication medication) {
        currentMedications.add(medication);
    }
    
    public List<Medication> getCurrentMedications() {
        return Collections.unmodifiableList(currentMedications);
    }
    
    public void addAllergy(String allergy) {
        allergies.add(allergy);
    }
    
    public List<String> getAllergies() {
        return Collections.unmodifiableList(allergies);
    }

    public VitalSigns getCurrentVitalSigns() {
        return currentVitalSigns;
    }

    public void setCurrentVitalSigns(VitalSigns currentVitalSigns) {
        this.currentVitalSigns = currentVitalSigns;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + getFullName() + '\'' +
                '}';
    }
}
