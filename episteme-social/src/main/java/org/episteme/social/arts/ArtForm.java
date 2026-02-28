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

package org.episteme.social.arts;


import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;

/**
 * Extensible enumeration of the different forms of art.
 * Includes the "Seven Arts" as well as modern digital and interactive media.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.2
 * @since 1.0
 */
public final class ArtForm extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<ArtForm> REGISTRY = EnumRegistry.getRegistry(ArtForm.class);

    public static final ArtForm ARCHITECTURE = new ArtForm("ARCHITECTURE", 1, true);
    public static final ArtForm SCULPTURE = new ArtForm("SCULPTURE", 2, true);
    public static final ArtForm PAINTING = new ArtForm("PAINTING", 3, true);
    public static final ArtForm MUSIC = new ArtForm("MUSIC", 4, true);
    public static final ArtForm POETRY = new ArtForm("POETRY", 5, true);
    public static final ArtForm LITERATURE = new ArtForm("LITERATURE", 5, true);
    public static final ArtForm DANCE = new ArtForm("DANCE", 6, true);
    public static final ArtForm CINEMA = new ArtForm("CINEMA", 7, true);
    public static final ArtForm PHOTOGRAPHY = new ArtForm("PHOTOGRAPHY", 8, true);
    public static final ArtForm COMICS = new ArtForm("COMICS", 9, true);
    public static final ArtForm TELEVISION = new ArtForm("TELEVISION", 10, true);
    public static final ArtForm RADIO = new ArtForm("RADIO", 11, true);
    public static final ArtForm GAMING = new ArtForm("GAMING", 12, true);
    public static final ArtForm CRAFTING = new ArtForm("CRAFTING", 13, true);
    public static final ArtForm GASTRONOMY = new ArtForm("GASTRONOMY", 14, true);
    public static final ArtForm THEATER = new ArtForm("THEATER", 15, true);
    public static final ArtForm DIGITAL_ART = new ArtForm("DIGITAL_ART", 16, true);
    public static final ArtForm PERFORMANCE_ART = new ArtForm("PERFORMANCE_ART", 17, true);
    
    public static final ArtForm OTHER = new ArtForm("OTHER", 0, true);
    public static final ArtForm UNKNOWN = new ArtForm("UNKNOWN", -1, true);

    private final int value;
    private final boolean builtIn;

    private ArtForm(String name, int value, boolean builtIn) {
        super(name);
        this.value = value;
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    public ArtForm(String name, int value) {
        this(name, value, false);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static ArtForm valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
    
    public static ArtForm fromValue(int value) {
        for (ArtForm form : REGISTRY.values()) {
            if (form.value == value) {
                return form;
            }
        }
        return UNKNOWN;
    }
}

