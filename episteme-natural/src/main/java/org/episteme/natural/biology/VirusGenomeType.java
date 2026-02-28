/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.biology;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;

/**
 * Extensible enumeration for virus genome types (Baltimore classification).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VirusGenomeType extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final VirusGenomeType DNA_DOUBLE_STRANDED = new VirusGenomeType("DNA_DOUBLE_STRANDED");
    public static final VirusGenomeType DNA_SINGLE_STRANDED = new VirusGenomeType("DNA_SINGLE_STRANDED");
    public static final VirusGenomeType RNA_DOUBLE_STRANDED = new VirusGenomeType("RNA_DOUBLE_STRANDED");
    public static final VirusGenomeType RNA_SINGLE_STRANDED_POSITIVE = new VirusGenomeType("RNA_SINGLE_STRANDED_POSITIVE");
    public static final VirusGenomeType RNA_SINGLE_STRANDED_NEGATIVE = new VirusGenomeType("RNA_SINGLE_STRANDED_NEGATIVE");
    public static final VirusGenomeType RNA_REVERSE_TRANSCRIBING = new VirusGenomeType("RNA_REVERSE_TRANSCRIBING");
    public static final VirusGenomeType DNA_REVERSE_TRANSCRIBING = new VirusGenomeType("DNA_REVERSE_TRANSCRIBING");

    static {
        EnumRegistry.register(VirusGenomeType.class, DNA_DOUBLE_STRANDED);
        EnumRegistry.register(VirusGenomeType.class, DNA_SINGLE_STRANDED);
        EnumRegistry.register(VirusGenomeType.class, RNA_DOUBLE_STRANDED);
        EnumRegistry.register(VirusGenomeType.class, RNA_SINGLE_STRANDED_POSITIVE);
        EnumRegistry.register(VirusGenomeType.class, RNA_SINGLE_STRANDED_NEGATIVE);
        EnumRegistry.register(VirusGenomeType.class, RNA_REVERSE_TRANSCRIBING);
        EnumRegistry.register(VirusGenomeType.class, DNA_REVERSE_TRANSCRIBING);
    }

    protected VirusGenomeType(String name) {
        super(name);
    }

    public static VirusGenomeType valueOf(String name) {
        return EnumRegistry.getRegistry(VirusGenomeType.class).valueOfRequired(name);
    }

    public static VirusGenomeType[] values() {
        return EnumRegistry.getRegistry(VirusGenomeType.class).values().toArray(new VirusGenomeType[0]);
    }
}

