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

package org.jscience.util;

import java.awt.*;

import java.util.HashMap;


/**
 * A class representing javascript and netscape colors.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class NamedColors {
    /** DOCUMENT ME! */
    private static HashMap colors;

/**
     * Creates a new NamedColors object.
     */
    public NamedColors() {
        colors = new HashMap();

        colors.put("aliceblue",
            new Color(Integer.parseInt("F0", 16), Integer.parseInt("F8", 16),
                Integer.parseInt("FF", 16)));
        colors.put("antiquewhite",
            new Color(Integer.parseInt("FA", 16), Integer.parseInt("EB", 16),
                Integer.parseInt("D7", 16)));
        colors.put("aqua",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("FF", 16)));
        colors.put("aquamarine",
            new Color(Integer.parseInt("7F", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("D4", 16)));
        colors.put("azure",
            new Color(Integer.parseInt("F0", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("FF", 16)));
        colors.put("beige",
            new Color(Integer.parseInt("F5", 16), Integer.parseInt("F5", 16),
                Integer.parseInt("DC", 16)));
        colors.put("bisque",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("E4", 16),
                Integer.parseInt("C4", 16)));
        colors.put("black",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("00", 16),
                Integer.parseInt("00", 16)));
        colors.put("blanchedalmond",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("EB", 16),
                Integer.parseInt("CD", 16)));
        colors.put("blue",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("00", 16),
                Integer.parseInt("FF", 16)));
        colors.put("blueviolet",
            new Color(Integer.parseInt("8A", 16), Integer.parseInt("2B", 16),
                Integer.parseInt("E2", 16)));
        colors.put("brown",
            new Color(Integer.parseInt("A5", 16), Integer.parseInt("2A", 16),
                Integer.parseInt("2A", 16)));
        colors.put("burlywood",
            new Color(Integer.parseInt("DE", 16), Integer.parseInt("B8", 16),
                Integer.parseInt("87", 16)));
        colors.put("cadetblue",
            new Color(Integer.parseInt("5F", 16), Integer.parseInt("9E", 16),
                Integer.parseInt("A0", 16)));
        colors.put("chartreuse",
            new Color(Integer.parseInt("7F", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("00", 16)));
        colors.put("chocolate",
            new Color(Integer.parseInt("D2", 16), Integer.parseInt("69", 16),
                Integer.parseInt("1E", 16)));
        colors.put("coral",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("7F", 16),
                Integer.parseInt("50", 16)));
        colors.put("cornflowerblue",
            new Color(Integer.parseInt("64", 16), Integer.parseInt("95", 16),
                Integer.parseInt("ED", 16)));
        colors.put("cornsilk",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("F8", 16),
                Integer.parseInt("DC", 16)));
        colors.put("crimson",
            new Color(Integer.parseInt("DC", 16), Integer.parseInt("14", 16),
                Integer.parseInt("3C", 16)));
        colors.put("cyan",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("FF", 16)));
        colors.put("darkblue",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("00", 16),
                Integer.parseInt("8B", 16)));
        colors.put("darkcyan",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("8B", 16),
                Integer.parseInt("8B", 16)));
        colors.put("darkgoldenrod",
            new Color(Integer.parseInt("B8", 16), Integer.parseInt("86", 16),
                Integer.parseInt("0B", 16)));
        colors.put("darkgray",
            new Color(Integer.parseInt("A9", 16), Integer.parseInt("A9", 16),
                Integer.parseInt("A9", 16)));
        colors.put("darkgreen",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("64", 16),
                Integer.parseInt("00", 16)));
        colors.put("darkkhaki",
            new Color(Integer.parseInt("BD", 16), Integer.parseInt("B7", 16),
                Integer.parseInt("6B", 16)));
        colors.put("darkmagenta",
            new Color(Integer.parseInt("8B", 16), Integer.parseInt("00", 16),
                Integer.parseInt("8B", 16)));
        colors.put("darkolivegreen",
            new Color(Integer.parseInt("55", 16), Integer.parseInt("6B", 16),
                Integer.parseInt("2F", 16)));
        colors.put("darkorange",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("8C", 16),
                Integer.parseInt("00", 16)));
        colors.put("darkorchid",
            new Color(Integer.parseInt("99", 16), Integer.parseInt("32", 16),
                Integer.parseInt("CC", 16)));
        colors.put("darkred",
            new Color(Integer.parseInt("8B", 16), Integer.parseInt("00", 16),
                Integer.parseInt("00", 16)));
        colors.put("darksalmon",
            new Color(Integer.parseInt("E9", 16), Integer.parseInt("96", 16),
                Integer.parseInt("7A", 16)));
        colors.put("darkseagreen",
            new Color(Integer.parseInt("8F", 16), Integer.parseInt("BC", 16),
                Integer.parseInt("8F", 16)));
        colors.put("darkslateblue",
            new Color(Integer.parseInt("48", 16), Integer.parseInt("3D", 16),
                Integer.parseInt("8B", 16)));
        colors.put("darkslategray",
            new Color(Integer.parseInt("2F", 16), Integer.parseInt("4F", 16),
                Integer.parseInt("4F", 16)));
        colors.put("darkturquoise",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("CE", 16),
                Integer.parseInt("D1", 16)));
        colors.put("darkviolet",
            new Color(Integer.parseInt("94", 16), Integer.parseInt("00", 16),
                Integer.parseInt("D3", 16)));
        colors.put("deeppink",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("14", 16),
                Integer.parseInt("93", 16)));
        colors.put("deepskyblue",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("BF", 16),
                Integer.parseInt("FF", 16)));
        colors.put("dimgray",
            new Color(Integer.parseInt("69", 16), Integer.parseInt("69", 16),
                Integer.parseInt("69", 16)));
        colors.put("dodgerblue",
            new Color(Integer.parseInt("1E", 16), Integer.parseInt("90", 16),
                Integer.parseInt("FF", 16)));
        colors.put("firebrick",
            new Color(Integer.parseInt("B2", 16), Integer.parseInt("22", 16),
                Integer.parseInt("22", 16)));
        colors.put("floralwhite",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("FA", 16),
                Integer.parseInt("F0", 16)));
        colors.put("forestgreen",
            new Color(Integer.parseInt("22", 16), Integer.parseInt("8B", 16),
                Integer.parseInt("22", 16)));
        colors.put("fuchsia",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("00", 16),
                Integer.parseInt("FF", 16)));
        colors.put("gainsboro",
            new Color(Integer.parseInt("DC", 16), Integer.parseInt("DC", 16),
                Integer.parseInt("DC", 16)));
        colors.put("ghostwhite",
            new Color(Integer.parseInt("F8", 16), Integer.parseInt("F8", 16),
                Integer.parseInt("FF", 16)));
        colors.put("gold",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("D7", 16),
                Integer.parseInt("00", 16)));
        colors.put("goldenrod",
            new Color(Integer.parseInt("DA", 16), Integer.parseInt("A5", 16),
                Integer.parseInt("20", 16)));
        colors.put("gray",
            new Color(Integer.parseInt("80", 16), Integer.parseInt("80", 16),
                Integer.parseInt("80", 16)));
        colors.put("green",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("80", 16),
                Integer.parseInt("00", 16)));
        colors.put("greenyellow",
            new Color(Integer.parseInt("AD", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("2F", 16)));
        colors.put("honeydew",
            new Color(Integer.parseInt("F0", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("F0", 16)));
        colors.put("hotpink",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("69", 16),
                Integer.parseInt("B4", 16)));
        colors.put("indianred",
            new Color(Integer.parseInt("CD", 16), Integer.parseInt("5C", 16),
                Integer.parseInt("5C", 16)));
        colors.put("indigo",
            new Color(Integer.parseInt("4B", 16), Integer.parseInt("00", 16),
                Integer.parseInt("82", 16)));
        colors.put("ivory",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("F0", 16)));
        colors.put("khaki",
            new Color(Integer.parseInt("F0", 16), Integer.parseInt("E6", 16),
                Integer.parseInt("8C", 16)));
        colors.put("lavender",
            new Color(Integer.parseInt("E6", 16), Integer.parseInt("E6", 16),
                Integer.parseInt("FA", 16)));
        colors.put("lavenderblush",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("F0", 16),
                Integer.parseInt("F5", 16)));
        colors.put("lawngreen",
            new Color(Integer.parseInt("7C", 16), Integer.parseInt("FC", 16),
                Integer.parseInt("00", 16)));
        colors.put("lemonchiffon",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("FA", 16),
                Integer.parseInt("CD", 16)));
        colors.put("lightblue",
            new Color(Integer.parseInt("AD", 16), Integer.parseInt("D8", 16),
                Integer.parseInt("E6", 16)));
        colors.put("lightcoral",
            new Color(Integer.parseInt("F0", 16), Integer.parseInt("80", 16),
                Integer.parseInt("80", 16)));
        colors.put("lightcyan",
            new Color(Integer.parseInt("E0", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("FF", 16)));
        colors.put("lightgoldenrodyellow",
            new Color(Integer.parseInt("FA", 16), Integer.parseInt("FA", 16),
                Integer.parseInt("D2", 16)));
        colors.put("lightgreen",
            new Color(Integer.parseInt("90", 16), Integer.parseInt("EE", 16),
                Integer.parseInt("90", 16)));
        colors.put("lightgrey",
            new Color(Integer.parseInt("D3", 16), Integer.parseInt("D3", 16),
                Integer.parseInt("D3", 16)));
        colors.put("lightpink",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("B6", 16),
                Integer.parseInt("C1", 16)));
        colors.put("lightsalmon",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("A0", 16),
                Integer.parseInt("7A", 16)));
        colors.put("lightseagreen",
            new Color(Integer.parseInt("20", 16), Integer.parseInt("B2", 16),
                Integer.parseInt("AA", 16)));
        colors.put("lightskyblue",
            new Color(Integer.parseInt("87", 16), Integer.parseInt("CE", 16),
                Integer.parseInt("FA", 16)));
        colors.put("lightslategray",
            new Color(Integer.parseInt("77", 16), Integer.parseInt("88", 16),
                Integer.parseInt("99", 16)));
        colors.put("lightsteelblue",
            new Color(Integer.parseInt("B0", 16), Integer.parseInt("C4", 16),
                Integer.parseInt("DE", 16)));
        colors.put("lightyellow",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("E0", 16)));
        colors.put("lime",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("00", 16)));
        colors.put("limegreen",
            new Color(Integer.parseInt("32", 16), Integer.parseInt("CD", 16),
                Integer.parseInt("32", 16)));
        colors.put("linen",
            new Color(Integer.parseInt("FA", 16), Integer.parseInt("F0", 16),
                Integer.parseInt("E6", 16)));
        colors.put("magenta",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("00", 16),
                Integer.parseInt("FF", 16)));
        colors.put("maroon",
            new Color(Integer.parseInt("80", 16), Integer.parseInt("00", 16),
                Integer.parseInt("00", 16)));
        colors.put("mediumaquamarine",
            new Color(Integer.parseInt("66", 16), Integer.parseInt("CD", 16),
                Integer.parseInt("AA", 16)));
        colors.put("mediumblue",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("00", 16),
                Integer.parseInt("CD", 16)));
        colors.put("mediumorchid",
            new Color(Integer.parseInt("BA", 16), Integer.parseInt("55", 16),
                Integer.parseInt("D3", 16)));
        colors.put("mediumpurple",
            new Color(Integer.parseInt("93", 16), Integer.parseInt("70", 16),
                Integer.parseInt("DB", 16)));
        colors.put("mediumseagreen",
            new Color(Integer.parseInt("3C", 16), Integer.parseInt("B3", 16),
                Integer.parseInt("71", 16)));
        colors.put("mediumslateblue",
            new Color(Integer.parseInt("7B", 16), Integer.parseInt("68", 16),
                Integer.parseInt("EE", 16)));
        colors.put("mediumspringgreen",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("FA", 16),
                Integer.parseInt("9A", 16)));
        colors.put("mediumturquoise",
            new Color(Integer.parseInt("48", 16), Integer.parseInt("D1", 16),
                Integer.parseInt("CC", 16)));
        colors.put("mediumvioletred",
            new Color(Integer.parseInt("C7", 16), Integer.parseInt("15", 16),
                Integer.parseInt("85", 16)));
        colors.put("midnightblue",
            new Color(Integer.parseInt("19", 16), Integer.parseInt("19", 16),
                Integer.parseInt("70", 16)));
        colors.put("mintcream",
            new Color(Integer.parseInt("F5", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("FA", 16)));
        colors.put("mistyrose",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("E4", 16),
                Integer.parseInt("E1", 16)));
        colors.put("moccasin",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("E4", 16),
                Integer.parseInt("B5", 16)));
        colors.put("navajowhite",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("DE", 16),
                Integer.parseInt("AD", 16)));
        colors.put("navy",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("00", 16),
                Integer.parseInt("80", 16)));
        colors.put("oldlace",
            new Color(Integer.parseInt("FD", 16), Integer.parseInt("F5", 16),
                Integer.parseInt("E6", 16)));
        colors.put("olive",
            new Color(Integer.parseInt("80", 16), Integer.parseInt("80", 16),
                Integer.parseInt("00", 16)));
        colors.put("olivedrab",
            new Color(Integer.parseInt("6B", 16), Integer.parseInt("8E", 16),
                Integer.parseInt("23", 16)));
        colors.put("orange",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("A5", 16),
                Integer.parseInt("00", 16)));
        colors.put("orangered",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("45", 16),
                Integer.parseInt("00", 16)));
        colors.put("orchid",
            new Color(Integer.parseInt("DA", 16), Integer.parseInt("70", 16),
                Integer.parseInt("D6", 16)));
        colors.put("palegoldenrod",
            new Color(Integer.parseInt("EE", 16), Integer.parseInt("E8", 16),
                Integer.parseInt("AA", 16)));
        colors.put("palegreen",
            new Color(Integer.parseInt("98", 16), Integer.parseInt("FB", 16),
                Integer.parseInt("98", 16)));
        colors.put("paleturquoise",
            new Color(Integer.parseInt("AF", 16), Integer.parseInt("EE", 16),
                Integer.parseInt("EE", 16)));
        colors.put("palevioletred",
            new Color(Integer.parseInt("DB", 16), Integer.parseInt("70", 16),
                Integer.parseInt("93", 16)));
        colors.put("papayawhip",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("EF", 16),
                Integer.parseInt("D5", 16)));
        colors.put("peachpuff",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("DA", 16),
                Integer.parseInt("B9", 16)));
        colors.put("peru",
            new Color(Integer.parseInt("CD", 16), Integer.parseInt("85", 16),
                Integer.parseInt("3F", 16)));
        colors.put("pink",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("C0", 16),
                Integer.parseInt("CB", 16)));
        colors.put("plum",
            new Color(Integer.parseInt("DD", 16), Integer.parseInt("A0", 16),
                Integer.parseInt("DD", 16)));
        colors.put("powderblue",
            new Color(Integer.parseInt("B0", 16), Integer.parseInt("E0", 16),
                Integer.parseInt("E6", 16)));
        colors.put("purple",
            new Color(Integer.parseInt("80", 16), Integer.parseInt("00", 16),
                Integer.parseInt("80", 16)));
        colors.put("red",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("00", 16),
                Integer.parseInt("00", 16)));
        colors.put("rosybrown",
            new Color(Integer.parseInt("BC", 16), Integer.parseInt("8F", 16),
                Integer.parseInt("8F", 16)));
        colors.put("royalblue",
            new Color(Integer.parseInt("41", 16), Integer.parseInt("69", 16),
                Integer.parseInt("E1", 16)));
        colors.put("saddlebrown",
            new Color(Integer.parseInt("8B", 16), Integer.parseInt("45", 16),
                Integer.parseInt("13", 16)));
        colors.put("salmon",
            new Color(Integer.parseInt("FA", 16), Integer.parseInt("80", 16),
                Integer.parseInt("72", 16)));
        colors.put("sandybrown",
            new Color(Integer.parseInt("F4", 16), Integer.parseInt("A4", 16),
                Integer.parseInt("60", 16)));
        colors.put("seagreen",
            new Color(Integer.parseInt("2E", 16), Integer.parseInt("8B", 16),
                Integer.parseInt("57", 16)));
        colors.put("seashell",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("F5", 16),
                Integer.parseInt("EE", 16)));
        colors.put("sienna",
            new Color(Integer.parseInt("A0", 16), Integer.parseInt("52", 16),
                Integer.parseInt("2D", 16)));
        colors.put("silver",
            new Color(Integer.parseInt("C0", 16), Integer.parseInt("C0", 16),
                Integer.parseInt("C0", 16)));
        colors.put("skyblue",
            new Color(Integer.parseInt("87", 16), Integer.parseInt("CE", 16),
                Integer.parseInt("EB", 16)));
        colors.put("slateblue",
            new Color(Integer.parseInt("6A", 16), Integer.parseInt("5A", 16),
                Integer.parseInt("CD", 16)));
        colors.put("slategray",
            new Color(Integer.parseInt("70", 16), Integer.parseInt("80", 16),
                Integer.parseInt("90", 16)));
        colors.put("snow",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("FA", 16),
                Integer.parseInt("FA", 16)));
        colors.put("springgreen",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("7F", 16)));
        colors.put("steelblue",
            new Color(Integer.parseInt("46", 16), Integer.parseInt("82", 16),
                Integer.parseInt("B4", 16)));
        colors.put("tan",
            new Color(Integer.parseInt("D2", 16), Integer.parseInt("B4", 16),
                Integer.parseInt("8C", 16)));
        colors.put("teal",
            new Color(Integer.parseInt("00", 16), Integer.parseInt("80", 16),
                Integer.parseInt("80", 16)));
        colors.put("thistle",
            new Color(Integer.parseInt("D8", 16), Integer.parseInt("BF", 16),
                Integer.parseInt("D8", 16)));
        colors.put("tomato",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("63", 16),
                Integer.parseInt("47", 16)));
        colors.put("turquoise",
            new Color(Integer.parseInt("40", 16), Integer.parseInt("E0", 16),
                Integer.parseInt("D0", 16)));
        colors.put("violet",
            new Color(Integer.parseInt("EE", 16), Integer.parseInt("82", 16),
                Integer.parseInt("EE", 16)));
        colors.put("wheat",
            new Color(Integer.parseInt("F5", 16), Integer.parseInt("DE", 16),
                Integer.parseInt("B3", 16)));
        colors.put("white",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("FF", 16)));
        colors.put("whitesmoke",
            new Color(Integer.parseInt("F5", 16), Integer.parseInt("F5", 16),
                Integer.parseInt("F5", 16)));
        colors.put("yellow",
            new Color(Integer.parseInt("FF", 16), Integer.parseInt("FF", 16),
                Integer.parseInt("00", 16)));
        colors.put("yellowgreen",
            new Color(Integer.parseInt("9A", 16), Integer.parseInt("CD", 16),
                Integer.parseInt("32", 16)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Color getColor(String name) {
        return (Color) colors.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNetscapeColor(Color color) {
        int value;

        //grays
        if ((Math.IEEEremainder(color.getRed(), 16) == 0) &&
                (Math.IEEEremainder(color.getGreen(), 16) == 0) &&
                (Math.IEEEremainder(color.getBlue(), 16) == 0)) {
            value = color.getRed() / 16;

            if (((color.getGreen() / 16) == value) &&
                    ((color.getBlue() / 16) == value)) {
                return true;
            } else {
                return false;
            }
        } else {
            //colors
            if ((Math.IEEEremainder(color.getRed(), 6) == 0) ||
                    (Math.IEEEremainder(color.getGreen(), 6) == 0) ||
                    (Math.IEEEremainder(color.getBlue(), 6) == 0)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNetscapeColor(int r, int g, int b) {
        return isNetscapeColor(new Color(r, g, b));
    }
}
