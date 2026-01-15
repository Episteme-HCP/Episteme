package org.jscience.earth.geosphere.ground;

/**
 */
import javax.media.j3d.Bounds;


//A class used to tie land characterisation to Bounds
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class LandCharacterization extends Object {
    //from http://edcdaac.usgs.gov/glcc/globdoc2_0.html
    /** DOCUMENT ME! */
    public final static int URBAN = 1;

    /** DOCUMENT ME! */
    public final static int LOW_SPARSE_GRASSLAND = 2;

    /** DOCUMENT ME! */
    public final static int CONIFEROUS_FOREST = 3;

    /** DOCUMENT ME! */
    public final static int DECIDUOUS_CONIFER_FOREST = 4;

    /** DOCUMENT ME! */
    public final static int COLD_DECIDUOUS_BROADLEAF_FOREST = 5; //noted as DECIDUOUS_BROADLEAF_FOREST on the website

    /** DOCUMENT ME! */
    public final static int EVERGREEN_BROADLEAF_FORESTS = 6;

    /** DOCUMENT ME! */
    public final static int TALL_GRASSES_AND_SHRUBS = 7;

    /** DOCUMENT ME! */
    public final static int BARE_DESERT = 8;

    /** DOCUMENT ME! */
    public final static int UPLAND_TUNDRA = 9;

    /** DOCUMENT ME! */
    public final static int IRRIGATED_GRASSLAND = 10;

    /** DOCUMENT ME! */
    public final static int SEMI_DESERT = 11;

    /** DOCUMENT ME! */
    public final static int GLACIER_ICE = 12;

    /** DOCUMENT ME! */
    public final static int WOODED_WET_SWAMP = 13;

    /** DOCUMENT ME! */
    public final static int INLAND_WATER = 14;

    /** DOCUMENT ME! */
    public final static int SEA_WATER = 15;

    /** DOCUMENT ME! */
    public final static int SHRUB_EVERGREEN = 16;

    /** DOCUMENT ME! */
    public final static int SHRUB_DECIDUOUS = 17;

    /** DOCUMENT ME! */
    public final static int MIXED_FOREST_AND_FIELD = 18;

    /** DOCUMENT ME! */
    public final static int EVERGREEN_FOREST_AND_FIELDS = 19;

    /** DOCUMENT ME! */
    public final static int COOL_RAIN_FOREST = 20;

    /** DOCUMENT ME! */
    public final static int CONIFER_BOREAL_FOREST = 21;

    /** DOCUMENT ME! */
    public final static int COOL_CONIFER_FOREST = 22;

    /** DOCUMENT ME! */
    public final static int COOL_MIXED_FOREST = 23;

    /** DOCUMENT ME! */
    public final static int MIXED_FOREST = 24;

    /** DOCUMENT ME! */
    public final static int COOL_BROADLEAF_FOREST = 25;

    /** DOCUMENT ME! */
    public final static int DECIDUOUS_BROADLEAF_FOREST = 26;

    /** DOCUMENT ME! */
    public final static int CONIFER_FOREST = 27;

    /** DOCUMENT ME! */
    public final static int MONTANE_TROPICAL_FORESTS = 28;

    /** DOCUMENT ME! */
    public final static int SEASONAL_TROPICAL_FOREST = 29;

    /** DOCUMENT ME! */
    public final static int COOL_CROPS_AND_TOWNS = 30;

    /** DOCUMENT ME! */
    public final static int CROPS_AND_TOWN = 31;

    /** DOCUMENT ME! */
    public final static int DRY_TROPICAL_WOODS = 32;

    /** DOCUMENT ME! */
    public final static int TROPICAL_RAINFOREST = 33;

    /** DOCUMENT ME! */
    public final static int TROPICAL_DEGRADED_FOREST = 34;

    /** DOCUMENT ME! */
    public final static int CORN_AND_BEANS_CROPLAND = 35;

    /** DOCUMENT ME! */
    public final static int RICE_PADDY_AND_FIELD = 36;

    /** DOCUMENT ME! */
    public final static int HOT_IRRIGATED_CROPLAND = 37;

    /** DOCUMENT ME! */
    public final static int COOL_IRRIGATED_CROPLAND = 38;

    /** DOCUMENT ME! */
    public final static int COLD_IRRIGATED_CROPLAND = 39;

    /** DOCUMENT ME! */
    public final static int COOL_GRASSES_AND_SHRUBS = 40;

    /** DOCUMENT ME! */
    public final static int HOT_AND_MILK_GRASSES_AND_SHRUBS = 41;

    /** DOCUMENT ME! */
    public final static int COLD_GRASSLAND = 42;

    /** DOCUMENT ME! */
    public final static int SAVANNA = 43;

    /** DOCUMENT ME! */
    public final static int MIRE_BOG_FEN = 44;

    /** DOCUMENT ME! */
    public final static int MARSH_WETLAND = 45;

    /** DOCUMENT ME! */
    public final static int MEDITERRANEAN_SCRUB = 46;

    /** DOCUMENT ME! */
    public final static int DRY_WOODY_SCRUB = 47;

    /** DOCUMENT ME! */
    public final static int DRY_EVERGREEN_WOODS = 48;

    /** DOCUMENT ME! */
    public final static int VOLCANIC_ROCK = 49;

    /** DOCUMENT ME! */
    public final static int SAND_DESERT = 50;

    /** DOCUMENT ME! */
    public final static int SEMI_DESERT_SHRUBS = 51;

    /** DOCUMENT ME! */
    public final static int SEMI_DESERT_SAGE = 52;

    /** DOCUMENT ME! */
    public final static int BARREN_TUNDRA = 53;

    /** DOCUMENT ME! */
    public final static int COOL_SOUTHERN_HEMISPHERE_MIXED_FORESTS = 54;

    /** DOCUMENT ME! */
    public final static int COOL_FIELDS_AND_WOODS = 55;

    /** DOCUMENT ME! */
    public final static int FOREST_AND_FIELD = 56;

    /** DOCUMENT ME! */
    public final static int COOL_FOREST_AND_FIELD = 57;

    /** DOCUMENT ME! */
    public final static int FIELDS_AND_WOODY_SAVANNA = 58;

    /** DOCUMENT ME! */
    public final static int SUCCULENT_AND_THORN_SCRUB = 59;

    /** DOCUMENT ME! */
    public final static int SMALL_LEAF_MIXED_WOODS = 60;

    /** DOCUMENT ME! */
    public final static int DECIDUOUS_AND_MIXED_BOREAL_FOREST = 61;

    /** DOCUMENT ME! */
    public final static int NARROW_CONIFERS = 62;

    /** DOCUMENT ME! */
    public final static int WOODED_TUNDRA = 63;

    /** DOCUMENT ME! */
    public final static int HEATH_SCRUB = 64;

    /** DOCUMENT ME! */
    public final static int COASTAL_WETLAND_NW = 65;

    /** DOCUMENT ME! */
    public final static int COASTAL_WETLAND_NE = 66;

    /** DOCUMENT ME! */
    public final static int COASTAL_WETLAND_SE = 67;

    /** DOCUMENT ME! */
    public final static int COASTAL_WETLAND_SW = 68;

    /** DOCUMENT ME! */
    public final static int POLAR_AND_APLINE_DESERT = 69;

    /** DOCUMENT ME! */
    public final static int GLACIER_ROCK = 70;

    /** DOCUMENT ME! */
    public final static int SALT_PLAYAS = 71;

    /** DOCUMENT ME! */
    public final static int MANGROVE = 72;

    /** DOCUMENT ME! */
    public final static int WATER_AND_ISLAND_FRINGE = 73;

    /** DOCUMENT ME! */
    public final static int LAND_WATER_AND_SHORE = 74;

    /** DOCUMENT ME! */
    public final static int LAND_AND_WATER_RIVERS = 75;

    /** DOCUMENT ME! */
    public final static int CROP_AND_WATER_MIXTURES = 76;

    /** DOCUMENT ME! */
    public final static int SOUTHERN_HEMISPHERE_CONIFERS = 77;

    /** DOCUMENT ME! */
    public final static int SOUTHERN_HEMISPHERE_MIXED_FOREST = 78;

    /** DOCUMENT ME! */
    public final static int WET_SCLEROPHYLIC_FOREST = 79;

    /** DOCUMENT ME! */
    public final static int COASTLINE_FRINGE = 80;

    /** DOCUMENT ME! */
    public final static int BEACHES_AND_DUNES = 81;

    /** DOCUMENT ME! */
    public final static int SPARSE_DUNES_AND_RIDGES = 82;

    /** DOCUMENT ME! */
    public final static int BARE_COASTAL_DUNES = 83;

    /** DOCUMENT ME! */
    public final static int RESIDUAL_DUNES_AND_BEACHES = 84;

    /** DOCUMENT ME! */
    public final static int COMPOUND_COASTLINES = 85;

    /** DOCUMENT ME! */
    public final static int ROCKY_CLIFFS_AND_SLOPES = 86;

    /** DOCUMENT ME! */
    public final static int SANDY_GRASSLAND_AND_SHRUBS = 87;

    /** DOCUMENT ME! */
    public final static int BAMBOO = 88;

    /** DOCUMENT ME! */
    public final static int MOIST_EUCALYPTUS = 89;

    /** DOCUMENT ME! */
    public final static int RAIN_GREEN_TROPICAL_FOREST = 90;

    /** DOCUMENT ME! */
    public final static int WOODY_SAVANNA = 91;

    /** DOCUMENT ME! */
    public final static int BROADLEAF_CROPS = 92;

    /** DOCUMENT ME! */
    public final static int GRASS_CROPS = 93;

    /** DOCUMENT ME! */
    public final static int CROPS_GRASS_SHRUBS = 94;

    /** DOCUMENT ME! */
    public final static int EVERGREEN_TREE_CROP = 95;

    /** DOCUMENT ME! */
    public final static int DECIDUOUS_TREE_CROP = 96;

    //public final static int INTERRUPTED_AREAS = 99;
    //public final static int MISSING_DATA = 100;
    /** DOCUMENT ME! */
    private Bounds[] bounds;

    /** DOCUMENT ME! */
    private int characterization;

    //characterization should be a valid value from the constants
    /**
     * Creates a new LandCharacterization object.
     *
     * @param bounds DOCUMENT ME!
     * @param characterization DOCUMENT ME!
     */
    public LandCharacterization(Bounds[] bounds, int characterization) {
        this.characterization = characterization;
        this.bounds = bounds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Bounds[] getBounds() {
        return bounds;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bounds DOCUMENT ME!
     */
    public void setBounds(Bounds[] bounds) {
        this.bounds = bounds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCharacterization() {
        return characterization;
    }

    /**
     * DOCUMENT ME!
     *
     * @param characterization DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLandCharacterizationName(int characterization) {
        String result;

        switch (characterization) {
        case URBAN:
            result = "Urban";

            break;

        case LOW_SPARSE_GRASSLAND:
            result = "Low Sparse Grassland";

            break;

        case CONIFEROUS_FOREST:
            result = "Coniferous Forest";

            break;

        case DECIDUOUS_CONIFER_FOREST:
            result = "Deciduous Conifer Forest";

            break;

        case COLD_DECIDUOUS_BROADLEAF_FOREST:
            result = "Cold Deciduous Broadleaf Forest";

            break;

        case EVERGREEN_BROADLEAF_FORESTS:
            result = "Evergreen Broadleaf Forests";

            break;

        case TALL_GRASSES_AND_SHRUBS:
            result = "Tall Grasses and Shrubs";

            break;

        case BARE_DESERT:
            result = "Bare Desert";

            break;

        case UPLAND_TUNDRA:
            result = "Upland Tundra";

            break;

        case IRRIGATED_GRASSLAND:
            result = "Irrigated Grassland";

            break;

        case SEMI_DESERT:
            result = "Semi Desert";

            break;

        case GLACIER_ICE:
            result = "Glacier Ice";

            break;

        case WOODED_WET_SWAMP:
            result = "Wooded Wet Swamp";

            break;

        case INLAND_WATER:
            result = "Inland Water";

            break;

        case SEA_WATER:
            result = "Sea Water";

            break;

        case SHRUB_EVERGREEN:
            result = "Shrub Evergreen";

            break;

        case SHRUB_DECIDUOUS:
            result = "Shrub Deciduous";

            break;

        case MIXED_FOREST_AND_FIELD:
            result = "Mixed Forest and Field";

            break;

        case EVERGREEN_FOREST_AND_FIELDS:
            result = "Evergreen Forest and Fields";

            break;

        case COOL_RAIN_FOREST:
            result = "Cool Rain Forest";

            break;

        case CONIFER_BOREAL_FOREST:
            result = "Conifer Boreal Forest";

            break;

        case COOL_CONIFER_FOREST:
            result = "Cool Conifer Forest";

            break;

        case COOL_MIXED_FOREST:
            result = "Cool Mixed Forest";

            break;

        case MIXED_FOREST:
            result = "Mixed Forest";

            break;

        case COOL_BROADLEAF_FOREST:
            result = "Cool Broadleaf Forest";

            break;

        case DECIDUOUS_BROADLEAF_FOREST:
            result = "Deciduous Broadleaf Forest";

            break;

        case CONIFER_FOREST:
            result = "Conifer Forest";

            break;

        case MONTANE_TROPICAL_FORESTS:
            result = "Montane Tropical Forests";

            break;

        case SEASONAL_TROPICAL_FOREST:
            result = "Seasonal Tropical Forest";

            break;

        case COOL_CROPS_AND_TOWNS:
            result = "Cool Crops and Towns";

            break;

        case CROPS_AND_TOWN:
            result = "Crops and Town";

            break;

        case DRY_TROPICAL_WOODS:
            result = "Dry Tropical Woods";

            break;

        case TROPICAL_RAINFOREST:
            result = "Tropical Rainforest";

            break;

        case TROPICAL_DEGRADED_FOREST:
            result = "Tropical Degraded Forest";

            break;

        case CORN_AND_BEANS_CROPLAND:
            result = "Corn and Beans Cropland";

            break;

        case RICE_PADDY_AND_FIELD:
            result = "Rice Paddy and Field";

            break;

        case HOT_IRRIGATED_CROPLAND:
            result = "Hot Irrigated Cropland";

            break;

        case COOL_IRRIGATED_CROPLAND:
            result = "Cool Irrigated Cropland";

            break;

        case COLD_IRRIGATED_CROPLAND:
            result = "Cold Irrigated Cropland";

            break;

        case COOL_GRASSES_AND_SHRUBS:
            result = "Cool Grasses and Shrubs";

            break;

        case HOT_AND_MILK_GRASSES_AND_SHRUBS:
            result = "Hot and Mild Grasses and Shrubs";

            break;

        case COLD_GRASSLAND:
            result = "Cold Grassland";

            break;

        case SAVANNA:
            result = "Savanna (Woods)";

            break;

        case MIRE_BOG_FEN:
            result = "Mire, Bog, Fen";

            break;

        case MARSH_WETLAND:
            result = "Marsh Wetland";

            break;

        case MEDITERRANEAN_SCRUB:
            result = "Mediterranean Scrub";

            break;

        case DRY_WOODY_SCRUB:
            result = "Dry Woody Scrub";

            break;

        case DRY_EVERGREEN_WOODS:
            result = "Dry Evergreen Woods";

            break;

        case VOLCANIC_ROCK:
            result = "Volcanic Rock";

            break;

        case SAND_DESERT:
            result = "Sand Desert";

            break;

        case SEMI_DESERT_SHRUBS:
            result = "Semi Desert Shrubs";

            break;

        case SEMI_DESERT_SAGE:
            result = "Semi Desert Sage";

            break;

        case BARREN_TUNDRA:
            result = "Barren Tundra";

            break;

        case COOL_SOUTHERN_HEMISPHERE_MIXED_FORESTS:
            result = "Cool Southern Hemisphere Mixed Forests";

            break;

        case COOL_FIELDS_AND_WOODS:
            result = "Cool Fields and Woods";

            break;

        case FOREST_AND_FIELD:
            result = "Forest and Field";

            break;

        case COOL_FOREST_AND_FIELD:
            result = "Cool Forest and Field";

            break;

        case FIELDS_AND_WOODY_SAVANNA:
            result = "Fields and Woody Savanna";

            break;

        case SUCCULENT_AND_THORN_SCRUB:
            result = "Succulent and Thorn Scrub";

            break;

        case SMALL_LEAF_MIXED_WOODS:
            result = "Small Leaf Mixed Woods";

            break;

        case DECIDUOUS_AND_MIXED_BOREAL_FOREST:
            result = "Deciduous and Mixed Boreal Forest";

            break;

        case NARROW_CONIFERS:
            result = "Narrow Conifers";

            break;

        case WOODED_TUNDRA:
            result = "Wooded Tundra";

            break;

        case HEATH_SCRUB:
            result = "Heath Scrub";

            break;

        case COASTAL_WETLAND_NW:
            result = "Coastal Wetland, NW";

            break;

        case COASTAL_WETLAND_NE:
            result = "Coastal Wetland, NE";

            break;

        case COASTAL_WETLAND_SE:
            result = "Coastal Wetland, SE";

            break;

        case COASTAL_WETLAND_SW:
            result = "Coastal Wetland, SW";

            break;

        case POLAR_AND_APLINE_DESERT:
            result = "Polar and Alpine Desert";

            break;

        case GLACIER_ROCK:
            result = "Glacier Rock";

            break;

        case SALT_PLAYAS:
            result = "Salt Playas";

            break;

        case MANGROVE:
            result = "Mangrove";

            break;

        case WATER_AND_ISLAND_FRINGE:
            result = "Water and Island Fringe";

            break;

        case LAND_WATER_AND_SHORE:
            result = "Land, Water, and Shore";

            break;

        case LAND_AND_WATER_RIVERS:
            result = "Land and Water, Rivers";

            break;

        case CROP_AND_WATER_MIXTURES:
            result = "Crop and Water Mixtures";

            break;

        case SOUTHERN_HEMISPHERE_CONIFERS:
            result = "Southern Hemisphere Conifers";

            break;

        case SOUTHERN_HEMISPHERE_MIXED_FOREST:
            result = "Southern Hemisphere Mixed Forest";

            break;

        case WET_SCLEROPHYLIC_FOREST:
            result = "Wet Sclerophylic Forest";

            break;

        case COASTLINE_FRINGE:
            result = "Coastline Fringe";

            break;

        case BEACHES_AND_DUNES:
            result = "Beaches and Dunes";

            break;

        case SPARSE_DUNES_AND_RIDGES:
            result = "Sparse Dunes and Ridges";

            break;

        case BARE_COASTAL_DUNES:
            result = "Bare Coastal Dunes";

            break;

        case RESIDUAL_DUNES_AND_BEACHES:
            result = "Residual Dunes and Beaches";

            break;

        case COMPOUND_COASTLINES:
            result = "Compound Coastlines";

            break;

        case ROCKY_CLIFFS_AND_SLOPES:
            result = "Rocky Cliffs and Slopes";

            break;

        case SANDY_GRASSLAND_AND_SHRUBS:
            result = "Sandy Grassland and Shrubs";

            break;

        case BAMBOO:
            result = "Bamboo";

            break;

        case MOIST_EUCALYPTUS:
            result = "Moist Eucalyptus";

            break;

        case RAIN_GREEN_TROPICAL_FOREST:
            result = "Rain Green Tropical Forest";

            break;

        case WOODY_SAVANNA:
            result = "Woody Savanna";

            break;

        case BROADLEAF_CROPS:
            result = "Broadleaf Crops";

            break;

        case GRASS_CROPS:
            result = "Grass Crops";

            break;

        case CROPS_GRASS_SHRUBS:
            result = "Crops, Grass, Shrubs";

            break;

        case EVERGREEN_TREE_CROP:
            result = "Evergreen Tree Crop";

            break;

        case DECIDUOUS_TREE_CROP:
            result = "Deciduous Tree Crop";

            break;

        //                    case INTERRUPTED_AREAS: result = "Interrupted Areas";
        //            break;
        //case MISSING_DATA: result = "Missing Data";
        //break;
        default:
            result = null;

            break;
        }

        return result;
    }
}
