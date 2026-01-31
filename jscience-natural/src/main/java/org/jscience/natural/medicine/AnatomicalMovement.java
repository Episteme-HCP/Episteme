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
 * Extensible enumeration for anatomical movements.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class AnatomicalMovement extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final AnatomicalMovement FLEXION = new AnatomicalMovement("FLEXION", true);
    public static final AnatomicalMovement EXTENSION = new AnatomicalMovement("EXTENSION", true);
    public static final AnatomicalMovement ABDUCTION = new AnatomicalMovement("ABDUCTION", true);
    public static final AnatomicalMovement ADDUCTION = new AnatomicalMovement("ADDUCTION", true);
    public static final AnatomicalMovement ROTATION = new AnatomicalMovement("ROTATION", true);
    public static final AnatomicalMovement SUPINATION = new AnatomicalMovement("SUPINATION", true);
    public static final AnatomicalMovement PRONATION = new AnatomicalMovement("PRONATION", true);
    public static final AnatomicalMovement ANTEROGRADE = new AnatomicalMovement("ANTEROGRADE", true);
    public static final AnatomicalMovement RETROGRADE = new AnatomicalMovement("RETROGRADE", true);

    static {
        EnumRegistry.register(AnatomicalMovement.class, FLEXION);
        EnumRegistry.register(AnatomicalMovement.class, EXTENSION);
        EnumRegistry.register(AnatomicalMovement.class, ABDUCTION);
        EnumRegistry.register(AnatomicalMovement.class, ADDUCTION);
        EnumRegistry.register(AnatomicalMovement.class, ROTATION);
        EnumRegistry.register(AnatomicalMovement.class, SUPINATION);
        EnumRegistry.register(AnatomicalMovement.class, PRONATION);
        EnumRegistry.register(AnatomicalMovement.class, ANTEROGRADE);
        EnumRegistry.register(AnatomicalMovement.class, RETROGRADE);
    }

    private final boolean builtIn;

    public AnatomicalMovement(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(AnatomicalMovement.class, this);
    }
    
    private AnatomicalMovement(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static AnatomicalMovement valueOf(String name) {
        return EnumRegistry.getRegistry(AnatomicalMovement.class).valueOfRequired(name);
    }
    
    public static AnatomicalMovement[] values() {
        return EnumRegistry.getRegistry(AnatomicalMovement.class).values().toArray(new AnatomicalMovement[0]);
    }
}

