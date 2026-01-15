package org.jscience.biology;

/**
 * A class representing the codons (the transcription of a sequence of
 * three amino acids) for all known species.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this class is a kind of rRNA support
//all different codons at http://www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi?mode=c#SG1
public class Codons {
    /** DOCUMENT ME! */
    public final static Alphabet STANDARD = new Alphabet("Standard",
            "FFLLSSSSYY  CC WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "   M               M               M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet VERTEBRATE_MITOCHONDRIAL = new Alphabet("Vertebrate Mitochondrial",
            "FFLLSSSSYY  CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNKKSS  VVVVAAAADDEEGGGG",
            "                                MMMM               M            ");

    /** DOCUMENT ME! */
    public final static Alphabet YEAST_MITOCHONDRIAL = new Alphabet("Yeast Mitochondrial",
            "FFLLSSSSYY  CCWWTTTTPPPPHHQQRRRRIIMMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                                  MM                            ");

    /** DOCUMENT ME! */
    public final static Alphabet MOLD_MITOCHONDRIAL = new Alphabet("Mold Mitochondrial; Protozoan Mitochondrial; Coelenterate Mitochondrial; Mycoplasma; Spiroplasma",
            "FFLLSSSSYY  CCWWLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "  MM               M            MMMM               M            ");

    /** DOCUMENT ME! */
    public final static Alphabet INVERTEBRATE_MITOCHONDRIAL = new Alphabet("Invertebrate Mitochondrial",
            "FFLLSSSSYY  CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNKKSSSSVVVVAAAADDEEGGGG",
            "   M                            MMMM               M            ");

    /** DOCUMENT ME! */
    public final static Alphabet CILIATE_NUCLEAR = new Alphabet("Ciliate Nuclear; Dasycladacean Nuclear; Hexamita Nuclear",
            "FFLLSSSSYYQQCC WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                                   M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet ECHINODERM_MITOCHONDRIAL = new Alphabet("Echinoderm Mitochondrial",
            "FFLLSSSSYY  CCWWLLLLPPPPHHQQRRRRIIIMTTTTNNNKSSSSVVVVAAAADDEEGGGG",
            "                                   M               M            ");

    /** DOCUMENT ME! */
    public final static Alphabet EUPLOTID_NUCLEAR = new Alphabet("Euplotid Nuclear",
            "FFLLSSSSYY  CCCWLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                                   M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet BACTERIAL_AND_PLANT_PLASTID = new Alphabet("Bacterial and Plant Plastid",
            "FFLLSSSSYY  CC WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "   M               M            MMMM               M            ");

    /** DOCUMENT ME! */
    public final static Alphabet ALTERNATIVE_YEAST_NUCLEAR = new Alphabet("Alternative Yeast Nuclear",
            "FFLLSSSSYY  CC WLLLSPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                   M               M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet ASCIDIAN_MITOCHONDRIAL = new Alphabet("Ascidian Mitochondrial",
            "FFLLSSSSYY  CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNKKSSGGVVVVAAAADDEEGGGG",
            "                                   M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet FLATWORM_MITOCHONDRIAL = new Alphabet("Flatworm Mitochondrial",
            "FFLLSSSSYYY CCWWLLLLPPPPHHQQRRRRIIIMTTTTNNNKSSSSVVVVAAAADDEEGGGG",
            "                                   M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet BLEPHARISMA_MACRONUCLEAR = new Alphabet("Blepharisma Macronuclear",
            "FFLLSSSSYY QCC WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                                   M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet CHLOROPHYCEAN_MITOCHONDRIAL = new Alphabet("Chlorophycean Mitochondrial",
            "FFLLSSSSYY LCC WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                                   M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet TREMATODE_MITOCHONDRIAL = new Alphabet("Trematode Mitochondrial",
            "FFLLSSSSYY  CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNNKSSSSVVVVAAAADDEEGGGG",
            "                                   M               M            ");

    /** DOCUMENT ME! */
    public final static Alphabet SCENEDESMUS_MITOCHONDRIAL = new Alphabet("Scenedesmus obliquus mitochondrial",
            "FFLLSS SYY LCC WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                                   M                            ");

    /** DOCUMENT ME! */
    public final static Alphabet THRAUSTOCHYTRIUM_MITOCHONDRIAL = new Alphabet("Thraustochytrium mitochondrial code",
            "FF LSSSSYY  CC WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG",
            "                                M  M               M            ");
}
