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

package org.jscience.social.politics;

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;

/**
 * Extensible enumeration for Political Spectrum.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class PoliticalSpectrum extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final PoliticalSpectrum FAR_LEFT = new PoliticalSpectrum("FAR_LEFT", true);
    public static final PoliticalSpectrum LEFT_WING = new PoliticalSpectrum("LEFT_WING", true);
    public static final PoliticalSpectrum CENTER_LEFT = new PoliticalSpectrum("CENTER_LEFT", true);
    public static final PoliticalSpectrum CENTER = new PoliticalSpectrum("CENTER", true);
    public static final PoliticalSpectrum CENTER_RIGHT = new PoliticalSpectrum("CENTER_RIGHT", true);
    public static final PoliticalSpectrum RIGHT_WING = new PoliticalSpectrum("RIGHT_WING", true);
    public static final PoliticalSpectrum FAR_RIGHT = new PoliticalSpectrum("FAR_RIGHT", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(PoliticalSpectrum.class, FAR_LEFT);
        EnumRegistry.register(PoliticalSpectrum.class, LEFT_WING);
        EnumRegistry.register(PoliticalSpectrum.class, CENTER_LEFT);
        EnumRegistry.register(PoliticalSpectrum.class, CENTER);
        EnumRegistry.register(PoliticalSpectrum.class, CENTER_RIGHT);
        EnumRegistry.register(PoliticalSpectrum.class, RIGHT_WING);
        EnumRegistry.register(PoliticalSpectrum.class, FAR_RIGHT);
    }

    public PoliticalSpectrum(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(PoliticalSpectrum.class, this);
    }

    private PoliticalSpectrum(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static PoliticalSpectrum valueOf(String name) {
        return EnumRegistry.getRegistry(PoliticalSpectrum.class).valueOfRequired(name);
    }
    
    public static PoliticalSpectrum[] values() {
        return EnumRegistry.getRegistry(PoliticalSpectrum.class).values().toArray(new PoliticalSpectrum[0]);
    }
}

