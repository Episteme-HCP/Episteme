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

package org.jscience.natural.medicine;

import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.persistence.Persistent;

/**
 * Extensible enumeration for anatomical directional terms.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class AnatomicalDirection extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final AnatomicalDirection CRANIAL = new AnatomicalDirection("CRANIAL", true);
    public static final AnatomicalDirection CAUDAL = new AnatomicalDirection("CAUDAL", true);
    public static final AnatomicalDirection ROSTRAL = new AnatomicalDirection("ROSTRAL", true);
    public static final AnatomicalDirection DORSAL = new AnatomicalDirection("DORSAL", true);
    public static final AnatomicalDirection VENTRAL = new AnatomicalDirection("VENTRAL", true);
    public static final AnatomicalDirection PROXIMAL = new AnatomicalDirection("PROXIMAL", true);
    public static final AnatomicalDirection DISTAL = new AnatomicalDirection("DISTAL", true);
    public static final AnatomicalDirection DEXTER = new AnatomicalDirection("DEXTER", true);
    public static final AnatomicalDirection SINISTER = new AnatomicalDirection("SINISTER", true);
    public static final AnatomicalDirection SUPERIOR = new AnatomicalDirection("SUPERIOR", true);
    public static final AnatomicalDirection INFERIOR = new AnatomicalDirection("INFERIOR", true);
    public static final AnatomicalDirection ANTERIOR = new AnatomicalDirection("ANTERIOR", true);
    public static final AnatomicalDirection POSTERIOR = new AnatomicalDirection("POSTERIOR", true);
    public static final AnatomicalDirection MEDIAL = new AnatomicalDirection("MEDIAL", true);
    public static final AnatomicalDirection LATERAL = new AnatomicalDirection("LATERAL", true);
    public static final AnatomicalDirection SUPERFICIAL = new AnatomicalDirection("SUPERFICIAL", true);
    public static final AnatomicalDirection PROFOUND = new AnatomicalDirection("PROFOUND", true);
    public static final AnatomicalDirection VISCERAL = new AnatomicalDirection("VISCERAL", true);
    public static final AnatomicalDirection PARIETAL = new AnatomicalDirection("PARIETAL", true);
    public static final AnatomicalDirection RADIAL = new AnatomicalDirection("RADIAL", true);
    public static final AnatomicalDirection ULNAR = new AnatomicalDirection("ULNAR", true);
    public static final AnatomicalDirection TIBIAL = new AnatomicalDirection("TIBIAL", true);
    public static final AnatomicalDirection FIBULAR = new AnatomicalDirection("FIBULAR", true);

    static {
        EnumRegistry.register(AnatomicalDirection.class, CRANIAL);
        EnumRegistry.register(AnatomicalDirection.class, CAUDAL);
        EnumRegistry.register(AnatomicalDirection.class, ROSTRAL);
        EnumRegistry.register(AnatomicalDirection.class, DORSAL);
        EnumRegistry.register(AnatomicalDirection.class, VENTRAL);
        EnumRegistry.register(AnatomicalDirection.class, PROXIMAL);
        EnumRegistry.register(AnatomicalDirection.class, DISTAL);
        EnumRegistry.register(AnatomicalDirection.class, DEXTER);
        EnumRegistry.register(AnatomicalDirection.class, SINISTER);
        EnumRegistry.register(AnatomicalDirection.class, SUPERIOR);
        EnumRegistry.register(AnatomicalDirection.class, INFERIOR);
        EnumRegistry.register(AnatomicalDirection.class, ANTERIOR);
        EnumRegistry.register(AnatomicalDirection.class, POSTERIOR);
        EnumRegistry.register(AnatomicalDirection.class, MEDIAL);
        EnumRegistry.register(AnatomicalDirection.class, LATERAL);
        EnumRegistry.register(AnatomicalDirection.class, SUPERFICIAL);
        EnumRegistry.register(AnatomicalDirection.class, PROFOUND);
        EnumRegistry.register(AnatomicalDirection.class, VISCERAL);
        EnumRegistry.register(AnatomicalDirection.class, PARIETAL);
        EnumRegistry.register(AnatomicalDirection.class, RADIAL);
        EnumRegistry.register(AnatomicalDirection.class, ULNAR);
        EnumRegistry.register(AnatomicalDirection.class, TIBIAL);
        EnumRegistry.register(AnatomicalDirection.class, FIBULAR);
    }

    private final boolean builtIn;

    public AnatomicalDirection(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(AnatomicalDirection.class, this);
    }
    
    private AnatomicalDirection(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static AnatomicalDirection valueOf(String name) {
        return EnumRegistry.getRegistry(AnatomicalDirection.class).valueOfRequired(name);
    }
    
    public static AnatomicalDirection[] values() {
        return EnumRegistry.getRegistry(AnatomicalDirection.class).values().toArray(new AnatomicalDirection[0]);
    }
}

