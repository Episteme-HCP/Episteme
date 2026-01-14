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

import java.util.*;

/**
 * Represents a medication/drug.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Medication {

    public enum Form {
        TABLET, CAPSULE, LIQUID, INJECTION, TOPICAL, INHALANT,
        SUPPOSITORY, PATCH, DROPS, SPRAY
    }

    public enum Route {
        ORAL, INTRAVENOUS, INTRAMUSCULAR, SUBCUTANEOUS, TOPICAL,
        INHALATION, RECTAL, SUBLINGUAL, TRANSDERMAL, OPHTHALMIC
    }

    private final String name;
    private String genericName;
    private String brandName;
    private Form form;
    private Route route;
    private String dosage;
    private String frequency;
    private final List<String> activeIngredients = new ArrayList<>();
    private final List<String> sideEffects = new ArrayList<>();
    private final List<String> contraindications = new ArrayList<>();
    private boolean prescriptionRequired;
    private String atcCode; // Anatomical Therapeutic Chemical code

    public Medication(String name) {
        this.name = name;
    }

    public Medication(String name, Form form, Route route) {
        this(name);
        this.form = form;
        this.route = route;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getGenericName() {
        return genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public Form getForm() {
        return form;
    }

    public Route getRoute() {
        return route;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getAtcCode() {
        return atcCode;
    }

    public boolean isPrescriptionRequired() {
        return prescriptionRequired;
    }

    public List<String> getActiveIngredients() {
        return Collections.unmodifiableList(activeIngredients);
    }

    public List<String> getSideEffects() {
        return Collections.unmodifiableList(sideEffects);
    }

    public List<String> getContraindications() {
        return Collections.unmodifiableList(contraindications);
    }

    // Setters
    public void setGenericName(String name) {
        this.genericName = name;
    }

    public void setBrandName(String name) {
        this.brandName = name;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setAtcCode(String code) {
        this.atcCode = code;
    }

    public void setPrescriptionRequired(boolean required) {
        this.prescriptionRequired = required;
    }

    public void addActiveIngredient(String ingredient) {
        activeIngredients.add(ingredient);
    }

    public void addSideEffect(String effect) {
        sideEffects.add(effect);
    }

    public void addContraindication(String contraindication) {
        contraindications.add(contraindication);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %s", name, form, route, dosage);
    }

    // Common medications
    public static Medication aspirin() {
        Medication m = new Medication("Aspirin", Form.TABLET, Route.ORAL);
        m.setGenericName("Acetylsalicylic acid");
        m.setDosage("325mg");
        m.addActiveIngredient("Acetylsalicylic acid");
        m.addSideEffect("Stomach upset");
        m.addContraindication("Bleeding disorders");
        return m;
    }

    public static Medication ibuprofen() {
        Medication m = new Medication("Ibuprofen", Form.TABLET, Route.ORAL);
        m.setDosage("200-400mg");
        m.addActiveIngredient("Ibuprofen");
        m.addSideEffect("Stomach upset");
        m.addSideEffect("Dizziness");
        return m;
    }
}


