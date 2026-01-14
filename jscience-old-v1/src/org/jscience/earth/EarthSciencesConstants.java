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

package org.jscience.earth;

/**
 * A class representing some useful constants for earth sciences
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class EarthSciencesConstants extends Object {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0; //unclassified

    //planets layers, mutually exclusives
    /** DOCUMENT ME! */
    public final static int CORE = 1;

    /** DOCUMENT ME! */
    public final static int MANTLE = 2; //MESOSPHERE and ASTHENOSPHERE

    //public final static int MESOSPHERE = 3;//there is something weird here
    /** DOCUMENT ME! */
    public final static int ASTHENOSPHERE = 4;

    /** DOCUMENT ME! */
    public final static int CRUST = 5;

    /** DOCUMENT ME! */
    public final static int LITHOSPHERE = 5; //Crust

    /** DOCUMENT ME! */
    public final static int HYDROSPHERE = 6;

    /** DOCUMENT ME! */
    public final static int ATMOSPHERE = 7; //all upper sphere

    /** DOCUMENT ME! */
    public final static int TROPOSPHERE = 20;

    /** DOCUMENT ME! */
    public final static int STRATOSPHERE = 21;

    /** DOCUMENT ME! */
    public final static int MESOSPHERE = 22;

    /** DOCUMENT ME! */
    public final static int THERMOSPHERE = 23;

    /** DOCUMENT ME! */
    public final static int EXOSPHERE = 24;

    //rocks, combinations at your own risk
    /** DOCUMENT ME! */
    public final static int SEDIMENTARY_ANY = 100;

    /** DOCUMENT ME! */
    public final static int SEDIMENTARY_CLASTIC = 101;

    /** DOCUMENT ME! */
    public final static int SEDIMENTARY_BIOGENIC = 102;

    /** DOCUMENT ME! */
    public final static int SEDIMENTARY_PRECIPITATE = 103;

    /** DOCUMENT ME! */
    public final static int METAMORPHIC_ANY = 200;

    /** DOCUMENT ME! */
    public final static int IGNEOUS_ANY = 300;

    /** DOCUMENT ME! */
    public final static int IGNEOUS_PLUTONIC = 301;

    /** DOCUMENT ME! */
    public final static int IGNEOUS_VOLCANIC = 302;

    //Wentworth Scale 
    /** DOCUMENT ME! */
    public final static int BOULDER = 1; //>256mm.

    /** DOCUMENT ME! */
    public final static int COBBLE = 2; //256-64mm.

    /** DOCUMENT ME! */
    public final static int PEBBLE = 3; //64-4mm

    /** DOCUMENT ME! */
    public final static int GRANULE = 4; // 4-2mm.

    /** DOCUMENT ME! */
    public final static int SAND = 5; //2-1/16mm.

    /** DOCUMENT ME! */
    public final static int SILT = 6; //1/16-1/256mm.

    /** DOCUMENT ME! */
    public final static int CLAY = 7; //<1/256mm.

    //Grain size
    /** DOCUMENT ME! */
    public final static int PEGMATIC = 1; // (very large grains)

    /** DOCUMENT ME! */
    public final static int PHANERITIC = 2; //  (only large grains)

    /** DOCUMENT ME! */
    public final static int PORPHYRITIC = 3; //  (some large grains and some small grains)

    /** DOCUMENT ME! */
    public final static int APHANITIC = 4; // ( only small grains) or glassy (no grains).

    //Crystal shapes
    //Crystal shape is also an important factor in the texture of an igneous rock. Crystals may be euhedral, subeuhedral or anhedral:
    /** DOCUMENT ME! */
    public final static int EUHEDRAL = 1; //if the crystallographic shape is preserved.

    /** DOCUMENT ME! */
    public final static int SUBEUHEDRAL = 2; //if only part is preserved.

    /** DOCUMENT ME! */
    public final static int ANHEDRAL = 3; //if the crystal presents no recognizable crystallographic direction.

    //sortes de volcans, mutually exlcusive
    /** DOCUMENT ME! */
    public final static int CINDER_CONE = 1;

    /** DOCUMENT ME! */
    public final static int ASH_CONE = 2;

    /** DOCUMENT ME! */
    public final static int SPATTER_CONE = 3;

    /** DOCUMENT ME! */
    public final static int SHIELD_VOLCANO = 4;

    /** DOCUMENT ME! */
    public final static int MUD_VOLCANO = 5;

    /** DOCUMENT ME! */
    public final static int SAND_VOLCANO = 6; //should perhaps be removed (different category)

    /** DOCUMENT ME! */
    public final static int STRATOVOLCANO = 7;

    /** DOCUMENT ME! */
    public final static int SUPERVOLCANO = 8; //should perhaps be removed

    //eruptions kind, combinations allowed
    /** DOCUMENT ME! */
    public final static int PHREATIC_ERUPTION = 1; // (steam)

    /** DOCUMENT ME! */
    public final static int EXPLOSIVE_ERUPTION = 2; // of high-silica lava  (e.g., rhyolite)

    /** DOCUMENT ME! */
    public final static int EFFUSIVE_ERUPTION = 4; // of low-silica lava (e.g., basalt)

    /** DOCUMENT ME! */
    public final static int PYROCLASTIC_FLOWS = 8; //

    /** DOCUMENT ME! */
    public final static int LAHARS = 16; // (debris flow)

    /** DOCUMENT ME! */
    public final static int CARBON_DIOXIDE_EMISSION = 32; //

    //climates, mutually exclusives
    /** DOCUMENT ME! */
    public final static int MEGATHERMAL_OR_TROPICAL_CLIMATES = 1;

    /** DOCUMENT ME! */
    public final static int DRY_CLIMATES = 2;

    /** DOCUMENT ME! */
    public final static int MESOTHERMAL_OR_TEMPERATE_CLIMATES = 3;

    /** DOCUMENT ME! */
    public final static int MEDITERRANEAN_CLIMATES = 4; //sub mesothermal

    /** DOCUMENT ME! */
    public final static int HUMID_SUBTROPICAL_CLIMATES = 5; //sub mesothermal

    /** DOCUMENT ME! */
    public final static int MARITIME_TEMPERATE_CLIMATES = 6; //sub mesothermal

    /** DOCUMENT ME! */
    public final static int MARITIME_SUBARCTIC_CLIMATES = 7; //sub mesothermal

    /** DOCUMENT ME! */
    public final static int MICROTHERMAL_OR_CONTINENTAL_CLIMATES = 8;

    /** DOCUMENT ME! */
    public final static int HOT_SUMMER_CONTINENTAL_CLIMATES = 9; //sub microthermal

    /** DOCUMENT ME! */
    public final static int WARM_SUMMER_CONTINENTAL_CLIMATES = 10; //sub microthermal

    /** DOCUMENT ME! */
    public final static int CONTINENTAL_SUBARCTIC_CLIMATES = 11; //sub microthermal

    /** DOCUMENT ME! */
    public final static int CONTINENTAL_SUBARCTIC_CLIMATES_WITH_SEVERE_WINTERS = 12; //sub microthermal

    /** DOCUMENT ME! */
    public final static int POLAR_CLIMATES = 13;

    //biomes, mutually exclusives
    //http://en.wikipedia.org/wiki/Global_200
    //http://en.wikipedia.org/wiki/List_of_the_Global_200
    /** DOCUMENT ME! */
    public final static int TUNDRA = 1; //(arctic, humid)

    /** DOCUMENT ME! */
    public final static int BOREAL_FORESTS_TAIGA = 2; //(subarctic, humid)

    /** DOCUMENT ME! */
    public final static int TEMPERATE_CONIFEROUS_FORESTS = 3; //(temperate cold, humid)

    /** DOCUMENT ME! */
    public final static int TEMPERATE_BROADLEAF_AND_MIXED_FORESTS = 4; //(temperate, humid)

    /** DOCUMENT ME! */
    public final static int TEMPERATE_GRASSLANDS_SAVANNAS_AND_SHRUBLANDS = 5; //(temperate, semi-arid)

    /** DOCUMENT ME! */
    public final static int MEDITERRANEAN_FORESTS_WOODLANDS_AND_SHRUB = 6; //(temperate warm, humid)

    /** DOCUMENT ME! */
    public final static int TROPICAL_AND_SUBTROPICAL_CONIFEROUS_FORESTS = 7;

    /** DOCUMENT ME! */
    public final static int TROPICAL_AND_SUBTROPICAL_MOIST_BROADLEAF_FORESTS = 8;

    /** DOCUMENT ME! */
    public final static int TROPICAL_AND_SUBTROPICAL_DRY_BROADLEAF_FORESTS = 9;

    /** DOCUMENT ME! */
    public final static int TROPICAL_AND_SUBTROPICAL_GRASSLANDS_SAVANNAS_AND_SHRUBLANDS =
        10;

    /** DOCUMENT ME! */
    public final static int DESERTS_AND_XERIC_SHRUBLANDS = 11;

    /** DOCUMENT ME! */
    public final static int MANGROVE = 12;

    /** DOCUMENT ME! */
    public final static int FLOODED_GRASSLANDS_AND_SAVANNAS = 13;

    /** DOCUMENT ME! */
    public final static int MONTANE_GRASSLANDS_AND_SHRUBLANDS = 14; //(high altitude)

    //clouds, mutually exclusives
    //there are in fact as many categories as there are clouds although scientific generally agree on theses
    /** DOCUMENT ME! */
    public final static int CIRRUS = 1;

    /** DOCUMENT ME! */
    public final static int STRATUS = 2;

    /** DOCUMENT ME! */
    public final static int CUMULUS = 3;

    /** DOCUMENT ME! */
    public final static int CIRROCUMULUS = 4;

    /** DOCUMENT ME! */
    public final static int CIRROSTRATUS = 5;

    /** DOCUMENT ME! */
    public final static int CUMULONIMBUS = 6;

    /** DOCUMENT ME! */
    public final static int NIMBOSTRATUS = 7;

    /** DOCUMENT ME! */
    public final static int ALTOSTRATUS = 8;

    /** DOCUMENT ME! */
    public final static int ALTOCUMULUS = 9;

    /** DOCUMENT ME! */
    public final static int STRATOCUMULUS = 10;
}
