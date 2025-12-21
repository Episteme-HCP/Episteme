/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.linguistics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinguisticsRestorationTest {

    @Test
    public void testLanguageAndPhonemes() {
        Language english = new Language("English");
        Phoneme p1 = new Phoneme(english, "th");
        Phoneme p2 = new Phoneme(english, "ng");

        assertTrue(english.getPhonemes().contains(p1));
        assertTrue(english.getPhonemes().contains(p2));
        assertEquals(2, english.getPhonemes().size());
        assertEquals("English", p1.getLanguage().getName());
    }

    @Test
    public void testSentenceTypes() {
        Sentence s1 = new Sentence("Hello world.");
        assertEquals(Sentence.Type.DECLARATIVE, s1.getType());

        Sentence s2 = new Sentence("Are you there?");
        assertEquals(Sentence.Type.INTERROGATIVE, s2.getType());

        Sentence s3 = new Sentence("Go away!");
        assertEquals(Sentence.Type.EXCLAMATORY, s3.getType());

        Sentence s4 = new Sentence("Plain text");
        assertEquals(Sentence.Type.UNKNOWN, s4.getType());
    }
}
