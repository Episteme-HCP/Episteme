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

package org.jscience.natural.biology;

import org.jscience.natural.chemistry.Element;
import org.jscience.natural.chemistry.PeriodicTable;
import org.jscience.natural.biology.loaders.VitaminReader;
import java.util.List;

/**
 * Utility class for vitamins and essential minerals.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.2
 * @since 1.0
 */
public final class Vitamins {

    private Vitamins() {}

    // Essential Minerals
    public static final Element CALCIUM = PeriodicTable.getElement("Calcium");
    public static final Element PHOSPHORUS = PeriodicTable.getElement("Phosphorus");
    public static final Element IODINE = PeriodicTable.getElement("Iodine");
    public static final Element IRON = PeriodicTable.getElement("Iron");
    public static final Element MAGNESIUM = PeriodicTable.getElement("Magnesium");
    public static final Element ZINC = PeriodicTable.getElement("Zinc");
    public static final Element FLUORINE = PeriodicTable.getElement("Fluorine");
    public static final Element SELENIUM = PeriodicTable.getElement("Selenium");

    /**
     * Returns the list of all fat-soluble vitamins.
     */
    public static List<Vitamin> getFatSolubleVitamins() {
        return VitaminReader.getInstance().getFatSoluble();
    }

    /**
     * Returns the list of all water-soluble vitamins.
     */
    public static List<Vitamin> getWaterSolubleVitamins() {
        return VitaminReader.getInstance().getWaterSoluble();
    }
}


