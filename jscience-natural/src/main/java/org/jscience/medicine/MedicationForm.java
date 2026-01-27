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

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * Extensible enumeration for medication pharmaceutical forms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MedicationForm extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final MedicationForm TABLET = new MedicationForm("TABLET");
    public static final MedicationForm CAPSULE = new MedicationForm("CAPSULE");
    public static final MedicationForm LIQUID = new MedicationForm("LIQUID");
    public static final MedicationForm INJECTION = new MedicationForm("INJECTION");
    public static final MedicationForm TOPICAL = new MedicationForm("TOPICAL");
    public static final MedicationForm INHALANT = new MedicationForm("INHALANT");
    public static final MedicationForm SUPPOSITORY = new MedicationForm("SUPPOSITORY");
    public static final MedicationForm PATCH = new MedicationForm("PATCH");
    public static final MedicationForm DROPS = new MedicationForm("DROPS");
    public static final MedicationForm SPRAY = new MedicationForm("SPRAY");

    static {
        EnumRegistry.register(MedicationForm.class, TABLET);
        EnumRegistry.register(MedicationForm.class, CAPSULE);
        EnumRegistry.register(MedicationForm.class, LIQUID);
        EnumRegistry.register(MedicationForm.class, INJECTION);
        EnumRegistry.register(MedicationForm.class, TOPICAL);
        EnumRegistry.register(MedicationForm.class, INHALANT);
        EnumRegistry.register(MedicationForm.class, SUPPOSITORY);
        EnumRegistry.register(MedicationForm.class, PATCH);
        EnumRegistry.register(MedicationForm.class, DROPS);
        EnumRegistry.register(MedicationForm.class, SPRAY);
    }

    protected MedicationForm(String name) {
        super(name);
    }

    public static MedicationForm valueOf(String name) {
        return EnumRegistry.getRegistry(MedicationForm.class).valueOfRequired(name);
    }

    public static MedicationForm[] values() {
        return EnumRegistry.getRegistry(MedicationForm.class).values().toArray(new MedicationForm[0]);
    }
}
