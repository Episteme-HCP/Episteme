package org.jscience.architecture;

/**
 * A class representing common constants used in architecture.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class ArchitectureConstants extends Object {
    /** kinds of parts */
    public final static int UNKNOWN = 0;

    /** kinds of parts */
    public final static int OTHER = -1;

    /** kinds of parts */
    public final static int DOOR = 1;

    /** kinds of parts */
    public final static int ROOF = 2;

    /** kinds of parts */
    public final static int WINDOW = 4;

    /** kinds of parts */
    public final static int SOIL = 8;

    /** kinds of parts */
    public final static int WALL = 16;

    /** kinds of parts */
    public final static int COLUMN = 32;

    /** kinds of paths */
    public final static int PATH = 1; //trail

    /** kinds of paths */
    public final static int ROAD = 2;

    /** kinds of paths */
    public final static int RAILROAD = 3;

    /** kinds of paths */
    public final static int SUBWAY = 4; //under earth rail road, may be a mag lev

    /** kinds of areas */
    public final static int NATURAL_PARK = 1;

    /** kinds of areas */
    public final static int WATER_AREA = 2; //pond, lake, sea

    /** kinds of areas */
    public final static int INDUSTRIAL = 3;

    /** kinds of areas */
    public final static int COMMERICAL = 4;

    /** kinds of areas */
    public final static int AGRICULTURAL = 5;

    /** kinds of areas */
    public final static int LIVING = 6;

    /** kinds of areas */
    public final static int WASTE = 7;

    /** kinds of areas */
    public final static int MILITARY = 8;

    /** kinds of pipes */
    public final static int GAS = 1;

    /** kinds of pipes */
    public final static int WATER = 2;

    /** kinds of pipes */
    public final static int SEWERS = 3;

    /** kinds of cables */
    public final static int ELECTRICITY = 1;

    /** kinds of cables */
    public final static int TELEPHONE = 2;

    /** kinds of building parts */
    public final static int HALL = 1;

    /** kinds of building parts */
    public final static int KITCHEN = 2;

    /** kinds of building parts */
    public final static int DINING_ROOM = 4;

    /** kinds of building parts */
    public final static int ROOM = 8; //sleeping room

    /** kinds of building parts */
    public final static int STORAGE_ROOM = 16; //cellar, where you store the food and beverage or the attic

    /** kinds of building parts */
    public final static int BATHROOM = 32;

    /** kinds of building parts */
    public final static int TOILETS = 64;

    /** kinds of building parts */
    public final static int MEETING_ROOM = 128; //where the TV set is

    /** kinds of building parts */
    public final static int DRESSING_ROOM = 256; //where you store clothes

    /** kinds of building parts */
    public final static int OFFICE = 512; //where people work, can be a specific room in a house

    /** kinds of construction */

    //see placesclassification.xml
    //Vienna convention
    //also see
    //http://www.trafficsign.us/
    //http://europa.eu.int/comm/transport/road/publications/trafficrules/countryreports/chapter7.htm
    //http://www1.securiteroutiere.gouv.fr/signaux/default.asp
    /** kinds of road signs */
    public final static int DANGER_WARNING_SIGNS = 1;

    /** kinds of road signs */
    public final static int PRIORITY_SIGNS = 2;

    /** kinds of road signs */
    public final static int PROHIBITORY_OR_RESTRICTIVE_SIGNS = 3;

    /** kinds of road signs */
    public final static int MANDATORY_SIGNS = 4;

    /** kinds of road signs */
    public final static int SPECIAL_REGULATION_SIGNS = 5;

    /** kinds of road signs */
    public final static int SERVICE_SIGNS = 6; // F. Information, facilities, or service signs 

    /** kinds of road signs */
    public final static int INDICATION_SIGNS = 7; //G. Direction, position, or indication signs 

    /** kinds of road signs */
    public final static int ADDITIONAL_PANELS = 8;
}
