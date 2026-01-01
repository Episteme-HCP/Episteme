/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.chemistry;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HeadlessPeriodicTableCheck {
    public static void main(String[] args) {
        System.out.println("Starting PeriodicTable Check...");
        int count = PeriodicTable.getElementCount();
        System.out.println("Total Elements Loaded: " + count);

        Element e103 = PeriodicTable.getElement("Lawrencium");
        System.out.println("Element 103 (Lr): " + (e103 != null ? "Found" : "Missing"));

        Element e104 = PeriodicTable.getElement("Rutherfordium");
        System.out.println("Element 104 (Rf): " + (e104 != null ? "Found" : "Missing"));

        Element e118 = PeriodicTable.getElement("Oganesson");
        System.out.println("Element 118 (Og): " + (e118 != null ? "Found" : "Missing"));

        if (count >= 118 && e104 != null && e118 != null) {
            System.out.println("SUCCESS: Periodic Table limit fixed.");
        } else {
            System.out.println("FAILURE: Elements missing.");
        }
    }
}


