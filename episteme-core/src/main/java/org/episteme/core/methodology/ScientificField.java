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

package org.episteme.core.methodology;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;

/**
 * Extensible enumeration for scientific fields/disciplines.
 * <p>
 * Moved to episteme-core/methodology to serve as a fundamental classification.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class ScientificField extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<ScientificField> REGISTRY = EnumRegistry.getRegistry(ScientificField.class);

    public static final ScientificField PHYSICS = new ScientificField("PHYSICS", true);
    public static final ScientificField CHEMISTRY = new ScientificField("CHEMISTRY", true);
    public static final ScientificField BIOLOGY = new ScientificField("BIOLOGY", true);
    public static final ScientificField MATHEMATICS = new ScientificField("MATHEMATICS", true);
    public static final ScientificField ASTRONOMY = new ScientificField("ASTRONOMY", true);
    public static final ScientificField MEDICINE = new ScientificField("MEDICINE", true);
    public static final ScientificField COMPUTER_SCIENCE = new ScientificField("COMPUTER_SCIENCE", true);
    public static final ScientificField ENGINEERING = new ScientificField("ENGINEERING", true);
    public static final ScientificField GEOLOGY = new ScientificField("GEOLOGY", true);
    public static final ScientificField PSYCHOLOGY = new ScientificField("PSYCHOLOGY", true);
    public static final ScientificField SOCIOLOGY = new ScientificField("SOCIOLOGY", true);
    public static final ScientificField ECONOMICS = new ScientificField("ECONOMICS", true);
    public static final ScientificField LINGUISTICS = new ScientificField("LINGUISTICS", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(ScientificField.class, PHYSICS);
        EnumRegistry.register(ScientificField.class, CHEMISTRY);
        EnumRegistry.register(ScientificField.class, BIOLOGY);
        EnumRegistry.register(ScientificField.class, MATHEMATICS);
        EnumRegistry.register(ScientificField.class, ASTRONOMY);
        EnumRegistry.register(ScientificField.class, MEDICINE);
        EnumRegistry.register(ScientificField.class, COMPUTER_SCIENCE);
        EnumRegistry.register(ScientificField.class, ENGINEERING);
        EnumRegistry.register(ScientificField.class, GEOLOGY);
        EnumRegistry.register(ScientificField.class, PSYCHOLOGY);
        EnumRegistry.register(ScientificField.class, SOCIOLOGY);
        EnumRegistry.register(ScientificField.class, ECONOMICS);
        EnumRegistry.register(ScientificField.class, LINGUISTICS);
    }

    public ScientificField(String name) {
        this(name, false);
    }
    
    private ScientificField(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static ScientificField valueOf(String name) {
        return REGISTRY.valueOfRequired(name);
    }
    
    public static ScientificField[] values() {
        return REGISTRY.values().toArray(new ScientificField[0]);
    }
}

