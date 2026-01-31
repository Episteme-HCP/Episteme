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

package org.jscience.social.arts;

/**
 * Constants and utility methods for the Arts module.
 * 
 * @deprecated Use {@link ArtForm} instead for art categories.
 */
public final class ArtsConstants {

    @Deprecated public static final int UNKNOWN = -1;
    @Deprecated public static final int ARCHITECTURE = 1;
    @Deprecated public static final int SCULPTURE = 2;
    @Deprecated public static final int PAINTING = 3;
    @Deprecated public static final int MUSIC = 4;
    @Deprecated public static final int POETRY = 5;
    @Deprecated public static final int DANCE = 6;
    @Deprecated public static final int CINEMA = 7;
    @Deprecated public static final int PHOTOGRAPHY = 8;
    @Deprecated public static final int COMICS = 9;
    @Deprecated public static final int TELEVISION = 10;
    @Deprecated public static final int RADIO = 11;
    @Deprecated public static final int GAMING = 12;
    @Deprecated public static final int LITERATURE = 20;
    @Deprecated public static final int THEATER = 21;
    @Deprecated public static final int CRAFTING = 22;

    private ArtsConstants() {}
    
    /**
     * @deprecated Use {@link ArtForm#toString()}
     */
    @Deprecated
    public static String getCategoryName(int value) {
        ArtForm form = ArtForm.fromValue(value);
        return form != null ? form.name() : "Unknown";
    }

    /**
     * Converts a legacy integer category to the new ArtForm enum.
     */
    public static ArtForm toArtForm(int value) {
        return ArtForm.fromValue(value);
    }
}

