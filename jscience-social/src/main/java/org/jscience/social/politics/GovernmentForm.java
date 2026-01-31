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
 * Extensible enumeration for Government Forms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class GovernmentForm extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final GovernmentForm ANARCHISM = new GovernmentForm("ANARCHISM", true);
    public static final GovernmentForm AUTOCRACY = new GovernmentForm("AUTOCRACY", true);
    public static final GovernmentForm DEMOCRACY = new GovernmentForm("DEMOCRACY", true);
    public static final GovernmentForm OLIGARCHY = new GovernmentForm("OLIGARCHY", true);
    public static final GovernmentForm REPUBLIC = new GovernmentForm("REPUBLIC", true);
    public static final GovernmentForm THEOCRACY = new GovernmentForm("THEOCRACY", true);
    public static final GovernmentForm MONARCHY = new GovernmentForm("MONARCHY", true);
    public static final GovernmentForm DICTATORSHIP = new GovernmentForm("DICTATORSHIP", true);
    public static final GovernmentForm TOTALITARIANISM = new GovernmentForm("TOTALITARIANISM", true);
    public static final GovernmentForm PARLIAMENTARY_SYSTEM = new GovernmentForm("PARLIAMENTARY_SYSTEM", true);
    public static final GovernmentForm PRESIDENTIAL_SYSTEM = new GovernmentForm("PRESIDENTIAL_SYSTEM", true);
    public static final GovernmentForm SEMI_PRESIDENTIAL = new GovernmentForm("SEMI_PRESIDENTIAL", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(GovernmentForm.class, ANARCHISM);
        EnumRegistry.register(GovernmentForm.class, AUTOCRACY);
        EnumRegistry.register(GovernmentForm.class, DEMOCRACY);
        EnumRegistry.register(GovernmentForm.class, OLIGARCHY);
        EnumRegistry.register(GovernmentForm.class, REPUBLIC);
        EnumRegistry.register(GovernmentForm.class, THEOCRACY);
        EnumRegistry.register(GovernmentForm.class, MONARCHY);
        EnumRegistry.register(GovernmentForm.class, DICTATORSHIP);
        EnumRegistry.register(GovernmentForm.class, TOTALITARIANISM);
        EnumRegistry.register(GovernmentForm.class, PARLIAMENTARY_SYSTEM);
        EnumRegistry.register(GovernmentForm.class, PRESIDENTIAL_SYSTEM);
        EnumRegistry.register(GovernmentForm.class, SEMI_PRESIDENTIAL);
    }

    public GovernmentForm(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(GovernmentForm.class, this);
    }

    private GovernmentForm(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static GovernmentForm valueOf(String name) {
        return EnumRegistry.getRegistry(GovernmentForm.class).valueOfRequired(name);
    }
    
    public static GovernmentForm[] values() {
        return EnumRegistry.getRegistry(GovernmentForm.class).values().toArray(new GovernmentForm[0]);
    }
}

