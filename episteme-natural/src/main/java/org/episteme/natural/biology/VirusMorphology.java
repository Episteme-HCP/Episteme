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
 * Extensible enumeration for virus morphology.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VirusMorphology extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final VirusMorphology ICOSAHEDRAL = new VirusMorphology("ICOSAHEDRAL");
    public static final VirusMorphology HELICAL = new VirusMorphology("HELICAL");
    public static final VirusMorphology COMPLEX = new VirusMorphology("COMPLEX");
    public static final VirusMorphology ENVELOPED = new VirusMorphology("ENVELOPED");

    static {
        EnumRegistry.register(VirusMorphology.class, ICOSAHEDRAL);
        EnumRegistry.register(VirusMorphology.class, HELICAL);
        EnumRegistry.register(VirusMorphology.class, COMPLEX);
        EnumRegistry.register(VirusMorphology.class, ENVELOPED);
    }

    protected VirusMorphology(String name) {
        super(name);
    }

    public static VirusMorphology valueOf(String name) {
        return EnumRegistry.getRegistry(VirusMorphology.class).valueOfRequired(name);
    }

    public static VirusMorphology[] values() {
        return EnumRegistry.getRegistry(VirusMorphology.class).values().toArray(new VirusMorphology[0]);
    }
}

