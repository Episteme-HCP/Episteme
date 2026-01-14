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

package org.jscience.biology;

import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;


/**
 * The class defines constants for molecules the (human) organism cannot
 * synthetize, these are mostly vitamins and several minerals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Vitamins extends Object {
    //Recommended Dietary Allowances
    //http://www.nal.usda.gov/fnic/dga/rda.pdf
    //see description at http://www.naturalhealthadvisor.com/herbs_vitamins.cfm
    /** DOCUMENT ME! */
    public final static int VITAMIN_A = 1; //1<<0;//fat-soluble

    /** DOCUMENT ME! */
    public final static int VITAMIN_D = 2; //1<<1;//fat-soluble

    /** DOCUMENT ME! */
    public final static int VITAMIN_E = 4; //1<<2;//fat-soluble

    /** DOCUMENT ME! */
    public final static int VITAMIN_K = 8; //fat-soluble

    /** DOCUMENT ME! */
    public final static int VITAMIN_C = 16; //water-soluble, known as ascorbic acid

    /** DOCUMENT ME! */
    public final static int VITAMIN_B1 = 32; //water-soluble, known as thiamin

    /** DOCUMENT ME! */
    public final static int VITAMIN_B2 = 64; //water-soluble, known as riboflavin

    /** DOCUMENT ME! */
    public final static int VITAMIN_B3 = 128; //water-soluble

    /** DOCUMENT ME! */
    public final static int VITAMIN_B5 = 256; //water-soluble, known as pantothenic acid

    /** DOCUMENT ME! */
    public final static int VITAMIN_B6 = 512; //water-soluble, known as pyridoxine

    /** DOCUMENT ME! */
    public final static int VITAMIN_B9 = 1024; //water-soluble, known as folic acid

    /** DOCUMENT ME! */
    public final static int VITAMIN_B12 = 2048; //water-soluble, known as cyanocobalamin

    /** DOCUMENT ME! */
    public final static int VITAMIN_H = 4096; //water-soluble, known as biotin

    //several minerals
    /** DOCUMENT ME! */
    public final static Element CALCIUM = PeriodicTable.getElement("Calcium");

    /** DOCUMENT ME! */
    public final static Element PHOSPHORUS = PeriodicTable.getElement(
            "Phosphorus");

    /** DOCUMENT ME! */
    public final static Element IODINE = PeriodicTable.getElement("Iodine");

    /** DOCUMENT ME! */
    public final static Element IRON = PeriodicTable.getElement("Iron");

    /** DOCUMENT ME! */
    public final static Element MAGNESIUM = PeriodicTable.getElement(
            "Magnesium");

    /** DOCUMENT ME! */
    public final static Element ZINC = PeriodicTable.getElement("Zinc");

    /** DOCUMENT ME! */
    public final static Element FLUORINE = PeriodicTable.getElement("Fluorine");

    /** DOCUMENT ME! */
    public final static Element SELENIUM = PeriodicTable.getElement("Selenium");
}
