package org.jscience.medicine;

/**
 * The MedicineConstants class provides several useful constants for
 * medical applications.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class MedicineConstants extends Object {
    //Animal
    //heart, lungs, brain, eye, stomach, spleen, pancreas, kidneys, liver, intestines, skin, uterus, bladder, etc.
    //Common plant
    //stalk, stem, leaf, roots, and stamen.
    //Organ systems
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int CIRCULATORY_SYSTEM = 1;

    /** DOCUMENT ME! */
    public final static int DIGESTIVE_SYSTEM = 2;

    /** DOCUMENT ME! */
    public final static int ENDOCRINE_SYSTEM = 4;

    /** DOCUMENT ME! */
    public final static int INTEGUMENTARY_SYSTEM = 8;

    /** DOCUMENT ME! */
    public final static int IMMUNE_SYSTEM = 16;

    /** DOCUMENT ME! */
    public final static int LYMPHATIC_SYSTEM = 32;

    /** DOCUMENT ME! */
    public final static int MUSCULAR_SYSTEM = 64;

    /** DOCUMENT ME! */
    public final static int NERVOUS_SYSTEM = 128;

    /** DOCUMENT ME! */
    public final static int REPRODUCTIVE_SYSTEM = 256;

    /** DOCUMENT ME! */
    public final static int RESPIRATORY_SYSTEM = 512;

    /** DOCUMENT ME! */
    public final static int SKELETAL_SYSTEM = 1024;

    /** DOCUMENT ME! */
    public final static int URINARY_SYSTEM = 2048;

    ////http://en.wikipedia.org/wiki/Zootomical_terms_for_location
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int CRANIAL = 1;

    /** DOCUMENT ME! */
    public final static int CAUDAL = 2; //or palmar

    /** DOCUMENT ME! */
    public final static int ROSTRAL = 4;

    /** DOCUMENT ME! */
    public final static int DORSAL = 1;

    /** DOCUMENT ME! */
    public final static int VENTRAL = 2;

    /** DOCUMENT ME! */
    public final static int PROXIMAL = 1; //or central

    /** DOCUMENT ME! */
    public final static int DISTAL = 2;

    /** DOCUMENT ME! */
    public final static int DEXTER = 1;

    /** DOCUMENT ME! */
    public final static int SINISTER = 2;

    /** DOCUMENT ME! */
    public final static int SUPERIOR = 1;

    /** DOCUMENT ME! */
    public final static int INFERIOR = 2;

    /** DOCUMENT ME! */
    public final static int ANTERIOR = 1;

    /** DOCUMENT ME! */
    public final static int POSTERIOR = 2;

    /** DOCUMENT ME! */
    public final static int MEDIAL = 1;

    /** DOCUMENT ME! */
    public final static int LATERAL = 2;

    /** DOCUMENT ME! */
    public final static int SUPERFICIAL = 1; //(or external)

    /** DOCUMENT ME! */
    public final static int PROFOUND = 2; //or deep (or internal).

    /** DOCUMENT ME! */
    public final static int VISCERAL = 1;

    /** DOCUMENT ME! */
    public final static int PARIETAL = 2;

    /** DOCUMENT ME! */
    public final static int RADIAL = 1;

    /** DOCUMENT ME! */
    public final static int ULNAR = 2;

    /** DOCUMENT ME! */
    public final static int TIBIAL = 1;

    /** DOCUMENT ME! */
    public final static int FIBULAR = 2; //(or peroneal)

    /** DOCUMENT ME! */
    public final static int TRANSVERSE = 1; //or axial an X-Y plane

    /** DOCUMENT ME! */
    public final static int CORONAL = 2; //or frontal an X-Z plane

    /** DOCUMENT ME! */
    public final static int SAGITTAL = 4; //an Y-Z plane

    /** DOCUMENT ME! */
    public final static int ANTEROGRADE = 1;

    /** DOCUMENT ME! */
    public final static int RETROGRADE = 2;

    /** DOCUMENT ME! */
    public final static int FLEXION = 1;

    /** DOCUMENT ME! */
    public final static int EXTENSION = 2;

    /** DOCUMENT ME! */
    public final static int ABDUCTION = 4;

    /** DOCUMENT ME! */
    public final static int ADDUCTION = 8;

    /** DOCUMENT ME! */
    public final static int ROTATION = 16;

    /** DOCUMENT ME! */
    public final static int SUPINATION = 32;

    /** DOCUMENT ME! */
    public final static int PRONATION = 64;

    //Schmidt Sting Pain Index (unused as we use a different system for patients, but widely used, still)
    /** DOCUMENT ME! */
    public final static float SWEAT_BEE = 1.0f; // Sweat bee: Light, ephemeral, almost fruity. A tiny spark has singed a single hair on your arm.

    /** DOCUMENT ME! */
    public final static float FIRE_ANT = 1.2f; //Fire ant: Sharp, sudden, mildly alarming. Like walking across a shag carpet & reaching for the light switch.

    /** DOCUMENT ME! */
    public final static float BULLHORN_ACACIA_ANT = 1.8f; //Bullhorn acacia ant: A rare, piercing, elevated sort of pain. Someone has fired a staple into your cheek.

    /** DOCUMENT ME! */
    public final static float BALD_FACED_HORNET = 2.0f; //Bald-faced hornet: Rich, hearty, slightly crunchy. Similar to getting your hand mashed in a revolving door.

    /** DOCUMENT ME! */
    public final static float YELLOW_JACKET = 2.0f; //Yellowjacket: Hot and smoky, almost irreverent. Imagine WC Fields extinguishing a cigar on your tongue.

    /** DOCUMENT ME! */
    public final static float RED_HARVESTER_ANT = 3.0f; //Red harvester ant: Bold and unrelenting. Somebody is using a drill to excavate your ingrown toenail.

    /** DOCUMENT ME! */
    public final static float PAPER_WASP = 3.0f; //Paper wasp: Caustic & burning. Distinctly bitter aftertaste. Like spilling a beaker of Hydrochloric acid on a paper cut.

    /** DOCUMENT ME! */
    public final static float PEPSIS_WASP = 4.0f; //Pepsis wasp: Blinding, fierce, shockingly electric. A running hair drier has been dropped into your bubble bath (if you get stung by one you might as well lie down and scream).

    /** DOCUMENT ME! */
    public final static float BULLET_ANT = 4.1f; //Bullet ant: Pure, intense, brilliant pain. Like walking over flaming charcoal with a 3-inch nail in your heel. //normally this is noted 4.0+ but java can't support that sort of stuff

    //http://en.wikipedia.org/wiki/Anatomical_Therapeutic_Chemical_Classification_System
    /** DOCUMENT ME! */
    public final static String ADC_CODE_A = new String(
            "Alimentary tract and metabolism");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_B = new String(
            "Blood and blood forming organs");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_C = new String("Cardiovascular system");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_D = new String("Dermatologicals");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_G = new String(
            "Genito-urinary system and sex hormones");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_H = new String(
            "Systemic hormonal preparations, excluding sex hormones and insulins");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_J = new String(
            "Anti-infectives for systemic use");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_L = new String(
            "Antineoplastic and immunomodulating agents");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_M = new String(
            "Musculo-skeletal system");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_N = new String("Nervous system");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_P = new String(
            "Antiparasitic products, insecticides and repellents");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_Q = new String("Veterinary drugs");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_R = new String("Respiratory system");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_S = new String("Sensory organs");

    /** DOCUMENT ME! */
    public final static String ADC_CODE_V = new String("Various");
    
    //basic tastes
        //mutually exclusive
    /** DOCUMENT ME! */
    public final static int SWEETNESS = 1;

    /** DOCUMENT ME! */
    public final static int BITTERNESS = 2;

    /** DOCUMENT ME! */
    public final static int SOURNESS = 4;

    /** DOCUMENT ME! */
    public final static int SALTINESS = 8;

    /** DOCUMENT ME! */
    public final static int UMAMI = 16;

//basic ordours
    /** DOCUMENT ME! */
    public final static int ETHEREAL = 1;

    /** DOCUMENT ME! */
    public final static int CAMPHORACEOUS = 2;

    /** DOCUMENT ME! */
    public final static int MUSKY = 4;

    /** DOCUMENT ME! */
    public final static int FLORAL = 8;

    /** DOCUMENT ME! */
    public final static int MINTY = 16;

    /** DOCUMENT ME! */
    public final static int PUNGENT = 32;

    /** DOCUMENT ME! */
    public final static int PUTRID = 64;

}
