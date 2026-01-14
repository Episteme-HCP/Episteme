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

package org.jscience.medicine.pharmacology;

import org.jscience.medicine.Diagnostic;
import org.jscience.medicine.Disease;
import org.jscience.medicine.Medication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Reads and manages pharmacology and medical data from various sources (loaders).
 * Acts as a central repository for medications, diagnostics, and diseases loaded from external databases.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PharmacologyDataReader {



    // Caches (if needed)
    private final List<Medication> medications = new ArrayList<>();
    private final List<Diagnostic> diagnostics = new ArrayList<>();
    private final List<Disease> diseases = new ArrayList<>();

    // Concrete Loaders
    private org.jscience.medicine.loaders.DrugBankReader medicationLoader;
    private org.jscience.medicine.loaders.ICD10Reader diseaseLoader;

    public PharmacologyDataReader() {
    }

    public void setMedicationLoader(org.jscience.medicine.loaders.DrugBankReader loader) {
        this.medicationLoader = loader;
    }

    public void setDiseaseLoader(org.jscience.medicine.loaders.ICD10Reader loader) {
        this.diseaseLoader = loader;
    }

    /**
     * Searches for medications explicitly.
     */
    public List<Medication> searchMedication(String query) {
        if (medicationLoader != null) {
            return medicationLoader.searchByName(query);
        }
        return Collections.emptyList();
    }
    
    /**
     * Searches for diseases explicitly.
     */
    public List<Disease> searchDisease(String query) {
        if (diseaseLoader != null) {
            return diseaseLoader.search(query);
        }
        return Collections.emptyList();
    }

    /**
     * Loads initial datasets.
     */
    public void load() {
        // Implementation logic for bulk loading
    }

    // --- Accessors ---

    public List<Medication> getMedications() {
        return Collections.unmodifiableList(medications);
    }

    public List<Diagnostic> getDiagnostics() {
        return Collections.unmodifiableList(diagnostics);
    }

    public List<Disease> getDiseases() {
        return Collections.unmodifiableList(diseases);
    }
    
    /**
     * Clears all loaded data.
     */
    public void clear() {
        medications.clear();
        diagnostics.clear();
        diseases.clear();
    }
}
