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

package org.jscience.economics.resources;

import java.util.Locale;


/**
 * A class representing an intelligent made sign.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//describes the fact that there are human readable signs associated to this thing
//the sign is then converted using localized knowledge when actually displaying the sign
//this could also be an audible sign, repeating over again at a location, (for example street crossing)
//you could provide sublasses: AudibleSign and VisualSign
public interface Sign {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Locale[] getSupportedLocales();

    //what the sign means as a string, represented in the given locale
    /**
     * DOCUMENT ME!
     *
     * @param locale DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSignMeaning(Locale locale);
}
