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

package org.jscience.mathematics.geometry.polyhedrons;

/**
 * List of UniformPolyhedrons Polyhedra and Their Kaleidoscopic Formulae.
 * <p/>
 * Each entry contains the following items:
 * <ol>
 * <li>Wythoff symbol.
 * <li>Polyhedron name.
 * <li>Dual name.
 * <li>Coxeter et al. reference figure.
 * <li>Wenninger reference figure.
 * </ol>
 * <p/>
 * Notes:
 * <ol>
 * <li>Cundy and Roulette’s trapezohedron has been renamed to deltohedron, as
 * its faces are deltoids, not trapezoids.
 * <li>The names of the non-dihedral polyhedra are those which appear in
 * Wenninger (1984). Some of them are slightly modified versions of those
 * in Wenninger (1971).
 * </ol>
 * <p/>
 * References:
 * <dl>
 * <dt>Coxeter, H.S.M., Longuet-Higgins, M.S. and Miller, J.C.P.,
 * <dd><i>UniformPolyhedrons polyhedra</i>, Phil. Trans. Royal Soc. London, Ser. A,
 * 246 (1953), 401-409.
 * <dt>Cundy, H.M. and Rollett, A.P.,
 * <dd>“Mathematical Models”, 3rd Ed., Tarquin, 1981.
 * <dt>Har’El, Z.
 * <dd><a href="http://www.math.technion.ac.il/~rl/docs/uniform.pdf">
 * <i>UniformPolyhedrons Solution for UniformPolyhedrons Polyhedra</i></a>, Geometriae
 * Dedicata, 47 (1993), 57-110.
 * <dt>Wenninger, M.J.,
 * <dd>“Polyhedron Models”, Cambridge University Press, 1971.
 * <dd>“Dual Models”, Cambridge University Press, 1984.
 * </dl>
 *
 * @author <a href="http://www.math.technion.ac.il/~rl/">Zvi Har’El</a>
 * @version $Id: UniformPolyhedrons.java,v 1.3 2007-10-21 21:08:22 virtualcall Exp $
 * @see <a href="UniformPolyhedrons.java">Class source code</a>
 */
public class UniformPolyhedrons {

    private UniformPolyhedrons() {
    }

    final static PolyhedronEntry[] list = {

            /******************************************************************************
             *		Dihedral Schwarz Triangles (D5 only)
             ******************************************************************************/

/* (2 2 5) (D1/5) */

/*1*/    new PolyhedronEntry("2 5|2", "pentagonal prism",
            "pentagonal dipyramid", 0, 0),
/*2*/    new PolyhedronEntry("|2 2 5", "pentagonal antiprism",
            "pentagonal deltohedron", 0, 0),

/* (2 2 5/2) (D2/5) */

/*3*/    new PolyhedronEntry("2 5/2|2", "pentagrammic prism",
            "pentagrammic dipyramid", 0, 0),
/*4*/    new PolyhedronEntry("|2 2 5/2", "pentagrammic antiprism",
            "pentagrammic deltohedron", 0, 0),

/* (5/3 2 2) (D3/5) */

/*5*/    new PolyhedronEntry("|2 2 5/3", "pentagrammic crossed antiprism",
            "pentagrammic concave deltohedron", 0, 0),

            /*******************************************************************************
             *		Tetrahedral Schwarz Triangles
             *******************************************************************************/

/* (2 3 3) (T1) */

/*6*/    new PolyhedronEntry("3|2 3", "tetrahedron",
            "tetrahedron", 15, 1),
/*7*/    new PolyhedronEntry("2 3|3", "truncated tetrahedron",
            "triakistetrahedron", 16, 6),

/* (3/2 3 3) (T2) */

/*8*/    new PolyhedronEntry("3/2 3|3", "octahemioctahedron",
            "octahemioctacron", 37, 68),

/* (3/2 2 3) (T3) */

/*9*/    new PolyhedronEntry("3/2 3|2", "tetrahemihexahedron",
            "tetrahemihexacron", 36, 67),

            /******************************************************************************
             *		Octahedral Schwarz Triangles
             ******************************************************************************/

/* (2 3 4) (O1) */

/*10*/    new PolyhedronEntry("4|2 3", "octahedron",
            "cube", 17, 2),
/*11*/    new PolyhedronEntry("3|2 4", "cube",
            "octahedron", 18, 3),
/*12*/    new PolyhedronEntry("2|3 4", "cuboctahedron",
            "rhombic dodecahedron", 19, 11),
/*13*/    new PolyhedronEntry("2 4|3", "truncated octahedron",
            "tetrakishexahedron", 20, 7),
/*14*/    new PolyhedronEntry("2 3|4", "truncated cube",
            "triakisoctahedron", 21, 8),
/*15*/    new PolyhedronEntry("3 4|2", "rhombicuboctahedron",
            "deltoidal icositetrahedron", 22, 13),
/*16*/    new PolyhedronEntry("2 3 4|", "truncated cuboctahedron",
            "disdyakisdodecahedron", 23, 15),
/*17*/    new PolyhedronEntry("|2 3 4", "snub cube",
            "pentagonal icositetrahedron", 24, 17),

/* (3/2 4 4) (O2b) */

/*18*/    new PolyhedronEntry("3/2 4|4", "small cubicuboctahedron",
            "small hexacronic icositetrahedron", 38, 69),

/* (4/3 3 4) (O4) */

/*19*/    new PolyhedronEntry("3 4|4/3", "great cubicuboctahedron",
            "great hexacronic icositetrahedron", 50, 77),
/*20*/    new PolyhedronEntry("4/3 4|3", "cubohemioctahedron",
            "hexahemioctacron", 51, 78),
/*21*/    new PolyhedronEntry("4/3 3 4|", "cubitruncated cuboctahedron",
            "tetradyakishexahedron", 52, 79),

/* (3/2 2 4) (O5) */

/*22*/    new PolyhedronEntry("3/2 4|2", "great rhombicuboctahedron",
            "great deltoidal icositetrahedron", 59, 85),
/*23*/    new PolyhedronEntry("3/2 2 4|", "small rhombihexahedron",
            "small rhombihexacron", 60, 86),

/* (4/3 2 3) (O7) */

/*24*/    new PolyhedronEntry("2 3|4/3", "stellated truncated hexahedron",
            "great triakisoctahedron", 66, 92),
/*25*/    new PolyhedronEntry("4/3 2 3|", "great truncated cuboctahedron",
            "great disdyakisdodecahedron", 67, 93),

/* (4/3 3/2 2) (O11) */

/*26*/    new PolyhedronEntry("4/3 3/2 2|", "great rhombihexahedron",
            "great rhombihexacron", 82, 103),

            /******************************************************************************
             *		Icosahedral Schwarz Triangles
             ******************************************************************************/

/* (2 3 5) (I1) */

/*27*/    new PolyhedronEntry("5|2 3", "icosahedron",
            "dodecahedron", 25, 4),
/*28*/    new PolyhedronEntry("3|2 5", "dodecahedron",
            "icosahedron", 26, 5),
/*29*/    new PolyhedronEntry("2|3 5", "icosidodecahedron",
            "rhombic triacontahedron", 28, 12),
/*30*/    new PolyhedronEntry("2 5|3", "truncated icosahedron",
            "pentakisdodecahedron", 27, 9),
/*31*/    new PolyhedronEntry("2 3|5", "truncated dodecahedron",
            "triakisicosahedron", 29, 10),
/*32*/    new PolyhedronEntry("3 5|2", "rhombicosidodecahedron",
            "deltoidal hexecontahedron", 30, 14),
/*33*/    new PolyhedronEntry("2 3 5|", "truncated icosidodechedon",
            "disdyakistriacontahedron", 31, 16),
/*34*/    new PolyhedronEntry("|2 3 5", "snub dodecahedron",
            "pentagonal hexecontahedron", 32, 18),

/* (5/2 3 3) (I2a) */

/*35*/    new PolyhedronEntry("3|5/2 3", "small ditrigonal icosidodecahedron",
            "small triambic icosahedron", 39, 70),
/*36*/    new PolyhedronEntry("5/2 3|3", "small icosicosidodecahedron",
            "small icosacronic hexecontahedron", 40, 71),
/*37*/    new PolyhedronEntry("|5/2 3 3", "small snub icosicosidodecahedron",
            "small hexagonal hexecontahedron", 41, 110),

/* (3/2 5 5) (I2b) */

/*38*/    new PolyhedronEntry("3/2 5|5", "small dodecicosidodecahedron",
            "small dodecacronic hexecontahedron", 42, 72),

/* (2 5/2 5) (I3) */

/*39*/    new PolyhedronEntry("5|2 5/2", "small stellated dodecahedron",
            "great dodecahedron", 43, 20),
/*40*/    new PolyhedronEntry("5/2|2 5", "great dodecahedron",
            "small stellated dodecahedron", 44, 21),
/*41*/    new PolyhedronEntry("2|5/2 5", "great dodecadodecahedron",
            "medial rhombic triacontahedron", 45, 73),
/*42*/    new PolyhedronEntry("2 5/2|5", "truncated great dodecahedron",
            "small stellapentakisdodecahedron", 47, 75),
/*43*/    new PolyhedronEntry("5/2 5|2", "rhombidodecadodecahedron",
            "medial deltoidal hexecontahedron", 48, 76),
/*44*/    new PolyhedronEntry("2 5/2 5|", "small rhombidodecahedron",
            "small rhombidodecacron", 46, 74),
/*45*/    new PolyhedronEntry("|2 5/2 5", "snub dodecadodecahedron",
            "medial pentagonal hexecontahedron", 49, 111),

/* (5/3 3 5) (I4) */

/*46*/    new PolyhedronEntry("3|5/3 5", "ditrigonal dodecadodecahedron",
            "medial triambic icosahedron", 53, 80),
/*47*/    new PolyhedronEntry("3 5|5/3", "great ditrigonal dodecicosidodecahedron",
            "great ditrigonal dodecacronic hexecontahedron", 54, 81),
/*48*/    new PolyhedronEntry("5/3 3|5", "small ditrigonal dodecicosidodecahedron",
            "small ditrigonal dodecacronic hexecontahedron", 55, 82),
/*49*/    new PolyhedronEntry("5/3 5|3", "icosidodecadodecahedron",
            "medial icosacronic hexecontahedron", 56, 83),
/*50*/    new PolyhedronEntry("5/3 3 5|", "icositruncated dodecadodecahedron",
            "tridyakisicosahedron", 57, 84),
/*51*/    new PolyhedronEntry("|5/3 3 5", "snub icosidodecadodecahedron",
            "medial hexagonal hexecontahedron", 58, 112),

/* (3/2 3 5) (I6b) */

/*52*/    new PolyhedronEntry("3/2|3 5", "great ditrigonal icosidodecahedron",
            "great triambic icosahedron", 61, 87),
/*53*/    new PolyhedronEntry("3/2 5|3", "great icosicosidodecahedron",
            "great icosacronic hexecontahedron", 62, 88),
/*54*/    new PolyhedronEntry("3/2 3|5", "small icosihemidodecahedron",
            "small icosihemidodecacron", 63, 89),
/*55*/    new PolyhedronEntry("3/2 3 5|", "small dodecicosahedron",
            "small dodecicosacron", 64, 90),

/* (5/4 5 5) (I6c) */

/*56*/    new PolyhedronEntry("5/4 5|5", "small dodecahemidodecahedron",
            "small dodecahemidodecacron", 65, 91),

/* (2 5/2 3) (I7) */

/*57*/    new PolyhedronEntry("3|2 5/2", "great stellated dodecahedron",
            "great icosahedron", 68, 22),
/*58*/    new PolyhedronEntry("5/2|2 3", "great icosahedron",
            "great stellated dodecahedron", 69, 41),
/*59*/    new PolyhedronEntry("2|5/2 3", "great icosidodecahedron",
            "great rhombic triacontahedron", 70, 94),
/*60*/    new PolyhedronEntry("2 5/2|3", "great truncated icosahedron",
            "great stellapentakisdodecahedron", 71, 95),
/*61*/    new PolyhedronEntry("2 5/2 3|", "rhombicosahedron",
            "rhombicosacron", 72, 96),
/*62*/    new PolyhedronEntry("|2 5/2 3", "great snub icosidodecahedron",
            "great pentagonal hexecontahedron", 73, 113),

/* (5/3 2 5) (I9) */

/*63*/    new PolyhedronEntry("2 5|5/3", "small stellated truncated dodecahedron",
            "great pentakisdodekahedron", 74, 97),
/*64*/    new PolyhedronEntry("5/3 2 5|", "truncated dodecadodecahedron",
            "medial disdyakistriacontahedron", 75, 98),
/*65*/    new PolyhedronEntry("|5/3 2 5", "inverted snub dodecadodecahedron",
            "medial inverted pentagonal hexecontahedron", 76, 114),

/* (5/3 5/2 3) (I10a) */

/*66*/    new PolyhedronEntry("5/2 3|5/3", "great dodecicosidodecahedron",
            "great dodecacronic hexecontahedron", 77, 99),
/*67*/    new PolyhedronEntry("5/3 5/2|3", "small dodecahemicosahedron",
            "small dodecahemicosacron", 78, 100),
/*68*/    new PolyhedronEntry("5/3 5/2 3|", "great dodecicosahedron",
            "great dodecicosacron", 79, 101),
/*69*/    new PolyhedronEntry("|5/3 5/2 3", "great snub dodecicosidodecahedron",
            "great hexagonal hexecontahedron", 80, 115),

/* (5/4 3 5) (I10b) */

/*70*/    new PolyhedronEntry("5/4 5|3", "great dodecahemicosahedron",
            "great dodecahemicosacron", 81, 102),

/* (5/3 2 3) (I13) */

/*71*/    new PolyhedronEntry("2 3|5/3", "great stellated truncated dodecahedron",
            "great triakisicosahedron", 83, 104),
/*72*/    new PolyhedronEntry("5/3 3|2", "great rhombicosidodecahedron",
            "great deltoidal hexecontahedron", 84, 105),
/*73*/    new PolyhedronEntry("5/3 2 3|", "great truncated icosidodecahedron",
            "great disdyakistriacontahedron", 87, 108),
/*74*/    new PolyhedronEntry("|5/3 2 3", "great inverted snub icosidodecahedron",
            "great inverted pentagonal hexecontahedron", 88, 116),

/* (5/3 5/3 5/2) (I18a) */

/*75*/    new PolyhedronEntry("5/3 5/2|5/3", "great dodecahemidodecahedron",
            "great dodecahemidodecacron", 86, 107),

/* (3/2 5/3 3) (I18b) */

/*76*/    new PolyhedronEntry("3/2 3|5/3", "great icosihemidodecahedron",
            "great icosihemidodecacron", 85, 106),

/* (3/2 3/2 5/3) (I22) */

/*77*/    new PolyhedronEntry("|3/2 3/2 5/2", "small retrosnub icosicosidodecahedron",
            "small hexagrammic hexecontahedron", 91, 118),

/* (3/2 5/3 2) (I23) */

/*78*/    new PolyhedronEntry("3/2 5/3 2|", "great rhombidodecahedron",
            "great rhombidodecacron", 89, 109),
/*79*/    new PolyhedronEntry("|3/2 5/3 2", "great retrosnub icosidodecahedron",
            "great pentagrammic hexecontahedron", 90, 117),

            /******************************************************************************
             *		Last But Not Least
             ******************************************************************************/

/*80*/    new PolyhedronEntry("3/2 5/3 3 5/2", "great dirhombicosidodecahedron",
            "great dirhombicosidodecacron", 92, 119)
    };

    static class PolyhedronEntry {
        final String wythoff;
        final String name;
        final String dual;
        final int coxeter;
        final int wenninger;

        PolyhedronEntry(String wythoff, String name, String dual,
                        int coxeter, int wenninger) {
            this.wythoff = wythoff;
            this.name = name;
            this.dual = dual;
            this.coxeter = coxeter;
            this.wenninger = wenninger;
        }
    }

}
