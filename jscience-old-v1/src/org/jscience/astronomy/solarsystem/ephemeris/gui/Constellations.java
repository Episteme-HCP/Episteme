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

//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class Constellations {
    /** DOCUMENT ME! */
    static final int count = 89;

    /** DOCUMENT ME! */
    static final String[] name = {
            "Andromeda", "Antlia", "Apus", "Aquarius", "Aquila", "Ara", "Aries",
            "Auriga", "Bootes", "Caelum", "Camelopardalis", "Cancer",
            "Canes Venatici", "Canis Major", "Canis Minor", "Capricornus",
            "Carina", "Cassiopeia", "Centaurus", "Cepheus", "Cetus", "Chameleon",
            "Circinus", "Columba", "Coma Berenices", "Corona Australis",
            "Corona Borealis", "Corvus", "Crater", "Crux", "Cygnus", "Delphinus",
            "Dorado", "Draco", "Equuleus", "Eridanus", "Fornax", "Gemini",
            "Grus", "Hercules", "Horlogium", "Hydra", "Hydrus", "Indus",
            "Lacerta", "Leo", "Leo Minor", "Lepus", "Libra", "Lupus", "Lynx",
            "Lyra", "Mensa", "Microscopium", "Monoceros", "Musca", "Norma",
            "Octans", "Ophiuchus", "Orion", "Pavo", "Pegasus", "Perseus",
            "Phoenix", "Pictor", "Pisces", "Piscis Austrinus", "Puppis", "Pyxis",
            "Reticulum", "Sagitta", "Sagittarius", "Scorpius", "Sculptor",
            "Scutum", "Serpens Caput", "Serpens Cauda", "Sextans", "Taurus",
            "Telescopium", "Triangulum", "Triangulum Australis", "Tucana",
            "Ursa Major", "Ursa Minor", "Vela", "Virgo", "Volans", "Vulpecula"
        };

    /** DOCUMENT ME! */
    static final String[] linkname = {
            "Andromeda", "Antlia", "Apus", "Aquarius", "Aquila", "Ara", "Aries",
            "Auriga", "Bootes", "Caelum", "Camelopardalis", "Cancer",
            "CanesVenatici", "CanisMajor", "CanisMinor", "Capricornus", "Carina",
            "Cassiopeia", "Centaurus", "Cepheus", "Cetus", "Chameleon",
            "Circinus", "Columba", "ComaBerenices", "CoronaAustr",
            "CoronaBorealis", "Corvus", "Crater", "Crux", "Cygnus", "Delphinus",
            "Dorado", "Draco", "Equuleus", "Eridanus", "Fornax", "Gemini",
            "Grus", "Hercules", "Horlogium", "Hydra", "Hydrus", "Indus",
            "Lacerta", "Leo", "LeoMinor", "Lepus", "Libra", "Lupus", "Lynx",
            "Lyra", "Mensa", "Microscop", "Monoceros", "Musca", "Norma",
            "Octans", "Ophiuchus", "Orion", "Pavo", "Pegasus", "Perseus",
            "Phoenix", "Pictor", "Pisces", "PisAustrinus", "Puppis", "Pyxis",
            "Reticulum", "Sagitta", "Sagittarius", "Scorpius", "Sculptor",
            "Scutum", "Serpens", "Serpens", "Sextans", "Taurus", "Telescopium",
            "Triangulum", "TriangulAustr", "Tucana", "UrsaMajor", "UrsaMinor",
            "Vela", "Virgo", "Volans", "Vulpecula"
        };

    /** DOCUMENT ME! */
    static final short[] raw_coord = {
            12550, 21062, -21738, 10271, -18794, 24797, -7344, -31650, 4240,
            -8191, -8480, -30572, -29575, 0, -14106, -18383, -26841, 3907, 21178,
            8480, -23520, 25100, 21062, 0, -19431, 17846, 19431, 24176, -19719,
            -10014, 11206, 30790, 0, 22882, 11206, 20603, -6496, 21062, 24245,
            29741, -11206, 7969, 29084, 2855, 14819, -21772, -11206, -21772,
            6923, -29696, 11992, 4984, 26509, -18603, -6496, -21062, 24245,
            -3584, 29696, -13376, 9780, -8480, -30101, 1472, -32269, 5496, -7472,
            -30381, 9738, 26841, -18794, 0, -7969, 11206, 29741, -23872, -21062,
            -7756, -24575, 16383, 14188, -4019, -11206, 30527, 4131, -8480,
            31379, -2138, -28377, 16243, -17749, 21062, -17749, -24537, 5689,
            -20957, 13729, -29696, -1807, -15825, 28377, 4240, -21841, 2855,
            -24257, 26665, -11206, -15395, 20065, -16383, -20065, 29741, 11206,
            7969, -8866, -23169, -21406, -29741, 11206, 7969, 16709, -25100,
            -12821, 15395, -11206, 26665, 7924, -30790, -7924, -11441, -26841,
            -14910, -8866, 23169, -21406, 7969, 11206, 29741, 10271, 18794,
            24797, 30527, -11206, -4019, -25110, -8480, 19267, -21294, -18794,
            16339, 19913, 21062, 15280, -25926, 18794, -6947, 4560, -32448, 0,
            -18979, -18794, -18979, 31530, -2855, 8448, -1462, -30790, 11111,
            -16276, -26841, 9397, -4023, -32269, -4023, -31530, -2855, 8448,
            32363, 2855, -4260, -11992, -29696, -6923, -7969, 11206, -29741,
            21738, 21062, -12550, 5451, -25100, -20344, 20882, -25100, -2749,
            8351, 5689, -31169, -10859, -16383, -26216, 23190, -21062, 9605,
            20065, -16383, 20065, 14188, -28377, -8191, -28791, 10125, -11925,
            -25926, -18794, -6947, -25926, -18794, 6947, 6947, -18794, -25926,
            -31649, -5689, -6295, -26593, 5689, 18277, -32050, -6812, 0, 12491,
            -2855, 30157, 27410, 8480, -15825, -19458, -25100, -8060, 13893,
            17363, -24065, -11992, -29696, 6923, 1807, -29696, -13729, 8060,
            25100, 19458, -5996, 31650, 5996, 10531, -25100, 18240, -8480, 0,
            31650, 9705, -30790, 5603, -25718, 13847, -14848
        };

/**
     * Creates a new Constellations object.
     */
    Constellations() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param hashtable DOCUMENT ME!
     */
    public void link(Hashtable hashtable) {
        for (int i = 0; i < name.length; i++) {
            String s = "http://www.seds.org/Maps/Stars_en/Fig/";
            String s1 = name[i].toLowerCase();
            String s2 = linkname[i].toLowerCase();
            LinkBody linkbody = new LinkBody(s, s2 + ".html");
            int j = 3 * i;
            linkbody.coordinate.set(-raw_coord[j + 2], raw_coord[j],
                raw_coord[j + 1]);
            hashtable.put(s1, linkbody);
        }
    }
}
