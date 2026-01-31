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
 * Extensible enumeration for anatomical planes.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class AnatomicalPlane extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final AnatomicalPlane TRANSVERSE = new AnatomicalPlane("TRANSVERSE", "Axial (X-Y)", true);
    public static final AnatomicalPlane CORONAL = new AnatomicalPlane("CORONAL", "Frontal (X-Z)", true);
    public static final AnatomicalPlane SAGITTAL = new AnatomicalPlane("SAGITTAL", "(Y-Z)", true);
    
    private final String description;
    private final boolean builtIn;

    static {
        EnumRegistry.register(AnatomicalPlane.class, TRANSVERSE);
        EnumRegistry.register(AnatomicalPlane.class, CORONAL);
        EnumRegistry.register(AnatomicalPlane.class, SAGITTAL);
    }

    public AnatomicalPlane(String name, String description) {
        super(name);
        this.description = description;
        this.builtIn = false;
        EnumRegistry.register(AnatomicalPlane.class, this);
    }

    private AnatomicalPlane(String name, String description, boolean builtIn) {
        super(name);
        this.description = description;
        this.builtIn = builtIn;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static AnatomicalPlane valueOf(String name) {
        return EnumRegistry.getRegistry(AnatomicalPlane.class).valueOfRequired(name);
    }
    
    public static AnatomicalPlane[] values() {
        return EnumRegistry.getRegistry(AnatomicalPlane.class).values().toArray(new AnatomicalPlane[0]);
    }
}

