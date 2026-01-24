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

import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a medication or drug with clinical and pharmaceutical details.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Medication implements Identified<String>, Named, Serializable {

    private static final long serialVersionUID = 1L;

    public enum Form {
        TABLET, CAPSULE, LIQUID, INJECTION, TOPICAL, INHALANT,
        SUPPOSITORY, PATCH, DROPS, SPRAY
    }

    public enum Route {
        ORAL, INTRAVENOUS, INTRAMUSCULAR, SUBCUTANEOUS, TOPICAL,
        INHALATION, RECTAL, SUBLINGUAL, TRANSDERMAL, OPHTHALMIC
    }

    @Id
    private final String id;

    @Attribute
    private final String name;

    @Attribute
    private String genericName;

    @Attribute
    private String brandName;

    @Attribute
    private Form form;

    @Attribute
    private Route route;

    @Attribute
    private String dosage;

    @Attribute
    private String frequency;

    @Attribute
    private final List<String> activeIngredients = new ArrayList<>();

    @Attribute
    private final List<String> sideEffects = new ArrayList<>();

    @Attribute
    private final List<String> contraindications = new ArrayList<>();

    @Attribute
    private boolean prescriptionRequired;

    @Attribute
    private String atcCode;

    public Medication(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public List<String> getActiveIngredients() {
        return Collections.unmodifiableList(activeIngredients);
    }

    public void addActiveIngredient(String ingredient) {
        activeIngredients.add(ingredient);
    }

    public List<String> getSideEffects() {
        return Collections.unmodifiableList(sideEffects);
    }

    public void addSideEffect(String effect) {
        sideEffects.add(effect);
    }

    public List<String> getContraindications() {
        return Collections.unmodifiableList(contraindications);
    }

    public void addContraindication(String contraindication) {
        contraindications.add(contraindication);
    }

    public boolean isPrescriptionRequired() {
        return prescriptionRequired;
    }

    public void setPrescriptionRequired(boolean prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }

    public String getAtcCode() {
        return atcCode;
    }

    public void setAtcCode(String atcCode) {
        this.atcCode = atcCode;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", name, form, route);
    }

    // Factory methods
    public static Medication aspirin() {
        Medication m = new Medication("Aspirin");
        m.setForm(Form.TABLET);
        m.setRoute(Route.ORAL);
        m.setGenericName("Acetylsalicylic acid");
        m.setDosage("325mg");
        return m;
    }
}
