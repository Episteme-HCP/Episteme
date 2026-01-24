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
 * Constants and enumerations for earth sciences (geology, meteorology, climatology).
 * <p>
 * Provides classifications for planetary layers, rock types, grain sizes, 
 * volcanic structures, climates, biomes, and cloud types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class EarthSciencesConstants {

    private EarthSciencesConstants() {}

    /**
     * Internal layers of terrestrial planets.
     */
    public enum PlanetaryLayer {
        INNER_CORE, OUTER_CORE, LOWER_MANTLE, UPPER_MANTLE, 
        ASTHENOSPHERE, LITHOSPHERE, CRUST, HYDROSPHERE, ATMOSPHERE, UNKNOWN
    }

    /**
     * Atmospheric layers.
     */
    public enum AtmosphereLayer {
        TROPOSPHERE, STRATOSPHERE, MESOSPHERE, THERMOSPHERE, EXOSPHERE, IONOSPHERE
    }

    /**
     * Major rock classifications.
     */
    public enum RockType {
        SEDIMENTARY_CLASTIC, SEDIMENTARY_BIOGENIC, SEDIMENTARY_CHEMICAL,
        METAMORPHIC_FOLIATED, METAMORPHIC_NON_FOLIATED,
        IGNEOUS_PLUTONIC, IGNEOUS_VOLCANIC, UNKNOWN
    }

    /**
     * Wentworth scale for sediment grain size.
     */
    public enum GrainSize {
        BOULDER, COBBLE, PEBBLE, GRANULE, SAND, SILT, CLAY
    }

    /**
     * Igneous rock texture/grain size.
     */
    public enum IgneousTexture {
        PEGMATITIC, PHANERITIC, PORPHYRITIC, APHANITIC, GLASSY
    }

    /**
     * Crystal development.
     */
    public enum CrystalForm {
        EUHEDRAL, SUBEUHEDRAL, ANHEDRAL
    }

    /**
     * Volcanic structures.
     */
    public enum VolcanoType {
        CINDER_CONE, ASH_CONE, SPATTER_CONE, SHIELD_VOLCANO, 
        STRATOVOLCANO, CALDERA, SUPERVOLCANO, MUD_VOLCANO, FISSURE_VENT
    }

    /**
     * Volcanic eruption types.
     */
    public enum EruptionType {
        PHREATIC, EXPLOSIVE, EFFUSIVE, HAWAIIAN, STROMBOLIAN, 
        VULCANIAN, PELEAN, PLINIAN, SURTSEYAN
    }

    /**
     * Climate classifications (Köppen-like).
     */
    public enum ClimateType {
        TROPICAL, DRY, TEMPERATE, CONTINENTAL, POLAR, 
        MEDITERRANEAN, SUBTROPICAL, SUBARCTIC, ALPINE
    }

    /**
     * Terrestrial biomes.
     */
    public enum Biome {
        TUNDRA, TAIGA, TEMPERATE_CONIFEROUS_FOREST, TEMPERATE_BROADLEAF_FOREST, 
        TEMPERATE_GRASSLAND, MEDITERRANEAN_FOREST, TROPICAL_CONIFEROUS_FOREST, 
        TROPICAL_MOIST_FOREST, TROPICAL_DRY_FOREST, TROPICAL_GRASSLAND, 
        DESERT, MANGROVE, FLOODED_GRASSLAND, MONTANE_GRASSLAND
    }

    /**
     * Basic cloud genera.
     */
    public enum CloudType {
        CIRRUS, CIRROCUMULUS, CIRROSTRATUS, 
        ALTOCUMULUS, ALTOSTRATUS, 
        STRATUS, STRATOCUMULUS, CUMULUS, 
        NIMBOSTRATUS, CUMULONIMBUS
    }
}
