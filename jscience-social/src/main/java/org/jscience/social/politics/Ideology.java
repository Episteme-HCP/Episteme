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
 * Extensible enumeration for Political Ideologies.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class Ideology extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final Ideology LIBERALISM = new Ideology("LIBERALISM", true);
    public static final Ideology CONSERVATISM = new Ideology("CONSERVATISM", true);
    public static final Ideology SOCIALISM = new Ideology("SOCIALISM", true);
    public static final Ideology COMMUNISM = new Ideology("COMMUNISM", true);
    public static final Ideology FASCISM = new Ideology("FASCISM", true);
    public static final Ideology LIBERTARIANISM = new Ideology("LIBERTARIANISM", true);
    public static final Ideology ANARCHISM = new Ideology("ANARCHISM", true);
    public static final Ideology NATIONALISM = new Ideology("NATIONALISM", true);
    public static final Ideology GREEN_POLITICS = new Ideology("GREEN_POLITICS", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(Ideology.class, LIBERALISM);
        EnumRegistry.register(Ideology.class, CONSERVATISM);
        EnumRegistry.register(Ideology.class, SOCIALISM);
        EnumRegistry.register(Ideology.class, COMMUNISM);
        EnumRegistry.register(Ideology.class, FASCISM);
        EnumRegistry.register(Ideology.class, LIBERTARIANISM);
        EnumRegistry.register(Ideology.class, ANARCHISM);
        EnumRegistry.register(Ideology.class, NATIONALISM);
        EnumRegistry.register(Ideology.class, GREEN_POLITICS);
    }

    public Ideology(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(Ideology.class, this);
    }

    private Ideology(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static Ideology valueOf(String name) {
        return EnumRegistry.getRegistry(Ideology.class).valueOfRequired(name);
    }
    
    public static Ideology[] values() {
        return EnumRegistry.getRegistry(Ideology.class).values().toArray(new Ideology[0]);
    }
}

