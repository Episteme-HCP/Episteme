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

package org.jscience.util.identity;

import java.security.SecureRandom;

/**
 * Generates ISBN-13 format identifiers.
 * <p>
 * Produces identifiers in the format 978-X-XXXX-XXXX-X with valid check digit.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ISBNGenerator implements IdGenerator {

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        int[] digits = new int[12];
        digits[0] = 9;
        digits[1] = 7;
        digits[2] = 8;

        for (int i = 3; i < 12; i++) {
            digits[i] = random.nextInt(10);
        }

        // Calculate check digit
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += digits[i] * (i % 2 == 0 ? 1 : 3);
        }
        int checkDigit = (10 - (sum % 10)) % 10;

        return String.format("%d%d%d-%d-%d%d%d%d-%d%d%d%d-%d",
                digits[0], digits[1], digits[2],
                digits[3],
                digits[4], digits[5], digits[6], digits[7],
                digits[8], digits[9], digits[10], digits[11],
                checkDigit);
    }

    @Override
    public String getFormat() {
        return "ISBN-13";
    }
}
