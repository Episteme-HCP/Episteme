package org.jscience.biology;

/**
 * A class representing useful constants in biology.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//see also Vitamins
public final class BiologyConstants extends Object {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    //human
    /** DOCUMENT ME! */
    public final static int SEEING = 1; //vision

    /** DOCUMENT ME! */
    public final static int HEARING = 2; //audition

    /** DOCUMENT ME! */
    public final static int TASTE = 4; //gustation

    /** DOCUMENT ME! */
    public final static int SMELL = 8; //olfaction

    /** DOCUMENT ME! */
    public final static int TACTITION = 16; //pressure perception

    /** DOCUMENT ME! */
    public final static int THERMOCEPTION = 32; //heat and the absence of heat (cold)

    /** DOCUMENT ME! */
    public final static int NOCIOCEPTION = 64; //pain

    /** DOCUMENT ME! */
    public final static int EQUILIBRIOCEPTION = 128; //balance

    /** DOCUMENT ME! */
    public final static int PROPRIOCETPTION = 256; //body awareness

    //non human
    /** DOCUMENT ME! */
    public final static int ELECTROCEPTION = 512; //detect electric fields

    /** DOCUMENT ME! */
    public final static int MAGNETOCEPTION = 1024; //detect fluctuations in magnetic fields

    /** DOCUMENT ME! */
    public final static int ECHOLOCATION = 2048; //sonar ability

    //inner perceptions could also be considered like
    //sensory awareness of hunger and thirst.
    /** DOCUMENT ME! */
    public final static int MALE = 1; //male only, this does not mean that this is a mature individual able to have sex

    /** DOCUMENT ME! */
    public final static int FEMALE = 2; //female only

    /** DOCUMENT ME! */
    public final static int ANDROGYN = 10; //no sex, also you may want to prefer MALE sex for example for ant workers, and FEMALE sex for some other species

    /** DOCUMENT ME! */
    public final static int HERMAPHRODITE = 11; //male and female sexs, for example snails

    //other combinations possible, see ciliates with more than 2 gametes

    //stage
    //only one valid stage at a time
    /** DOCUMENT ME! */
    public final static int EGG = 1;

    /** DOCUMENT ME! */
    public final static int YOUNG = 2; //cannot reproduce

    /** DOCUMENT ME! */
    public final static int LARVAE = 3;

    /** DOCUMENT ME! */
    public final static int CHRYSALID = 4;

    /** DOCUMENT ME! */
    public final static int ADULT = 5; //or mature

    /** DOCUMENT ME! */
    public final static int OTHER = 10;

    //may be we should include Maslow's Hierarchy of Needs like seen at http://www.deepermind.com/20maslow.htm
    //here or in org.jscience.psychology.social.PsychologicalConstants
    //use a combination of these to describe available information about the name of a species
    /** DOCUMENT ME! */
    public final static int KINGDOM = 1;

    /** DOCUMENT ME! */
    public final static int PHYLUM = 2; //or division

    /** DOCUMENT ME! */
    public final static int SUBPHYLUM = 4;

    /** DOCUMENT ME! */
    public final static int SUPERCLASS = 8;

    /** DOCUMENT ME! */
    public final static int CLASS = 16;

    /** DOCUMENT ME! */
    public final static int SUBCLASS = 32;

    /** DOCUMENT ME! */
    public final static int SUPERORDER = 64;

    /** DOCUMENT ME! */
    public final static int ORDER = 128;

    /** DOCUMENT ME! */
    public final static int SUBORDER = 256;

    /** DOCUMENT ME! */
    public final static int SUPERFAMILY = 512;

    /** DOCUMENT ME! */
    public final static int FAMILY = 1024;

    /** DOCUMENT ME! */
    public final static int SUBFAMILY = 2048;

    /** DOCUMENT ME! */
    public final static int GENUS = 4096;

    /** DOCUMENT ME! */
    public final static int SUBGENUS = 8192;

    /** DOCUMENT ME! */
    public final static int SPECIES = 16384;

    /** DOCUMENT ME! */
    public final static int AUTOTROPH = 1; //photoautotrophs, chemoautotrophs

    /** DOCUMENT ME! */
    public final static int HETEROTROPH = 2; //photoheterotroph, chemoheterotroph

    /** DOCUMENT ME! */
    public final static int HERBIVORE = 1; //Herbivore - plants

    /** DOCUMENT ME! */
    public final static int CARNIVORE = 2; //Carnivore - meat

    /** DOCUMENT ME! */
    public final static int DETRITIVORE = 4; //Detritivore - decomposing material

    /** DOCUMENT ME! */
    public final static int FOLIVORE = 8; //Folivore - leaves

    /** DOCUMENT ME! */
    public final static int FRUGIVORE = 16; //Frugivore - fruit

    /** DOCUMENT ME! */
    public final static int INSECTIVORE = 32; //Insectivore - insects

    /** DOCUMENT ME! */
    public final static int NECTARIVORE = 64; //Nectarivore - nectar

    /** DOCUMENT ME! */
    public final static int MUCIVORE = 128; //Mucivore - plant juices

    /** DOCUMENT ME! */
    public final static int GRANIVORE = 256; //Granivore - seeds

    /** DOCUMENT ME! */
    public final static int MYCOVORE = 512; //Mycovore - fungi

    /** DOCUMENT ME! */
    public final static int PISCIVORE = 1024; //Piscivore - fish

    /** DOCUMENT ME! */
    public final static int SANGUINIVORE = 2048; //Sanguinivore - blood

    /** DOCUMENT ME! */
    public final static int SAPROVORE = 4096; //Saprovore - dead matter

    /** DOCUMENT ME! */
    public final static int OMNIVORE = HERBIVORE + CARNIVORE; //Omnivore - plants and meat

    /** DOCUMENT ME! */
    public final static int AMENSALISM = 1;

    /** DOCUMENT ME! */
    public final static int COMMENSALISM = 2;

    /** DOCUMENT ME! */
    public final static int PREDATION = 4; //Carnivory, Herbivory, Parasitism, Parasitoidism

    /** DOCUMENT ME! */
    public final static int MUTUALISM = 8;

    /** DOCUMENT ME! */
    public final static int NEUTRALISM = 16;

    /** DOCUMENT ME! */
    public final static int SYNNECROSIS = 32;

    /** DOCUMENT ME! */
    public final static int SYMBIOSIS = 64;

    /** DOCUMENT ME! */
    public final static int COMPETITION = 128;

    /** DOCUMENT ME! */
    public final static int HOMEOTHERM = 1; //Endothermy, Homeothermy, Tachymetabolism

    /** DOCUMENT ME! */
    public final static int POIKILOTHERM = 2; //Ectothermy, Poikilothermy, Bradymetabolism

    //reproduction kinds, normally one mode per species
    //many possibilites here and combinations also http://en.wikipedia.org/wiki/Biological_reproduction
    /** DOCUMENT ME! */
    public final static int SEXUAL = 1; //No more information available (exclusive)

    /** DOCUMENT ME! */
    public final static int ASSEXUAL = 2; //No more information available (exclusive)

    /** DOCUMENT ME! */
    public final static int BINARY_FISSION = 4; //kind of assexual reproduction

    /** DOCUMENT ME! */
    public final static int CONJUGATION = 8; //kind of assexual reproduction

    /** DOCUMENT ME! */
    public final static int HOST_CONTROL = 16; //kind of assexual reproduction

    /** DOCUMENT ME! */
    public final static int BUDDING = 32; //kind of assexual reproduction

    /** DOCUMENT ME! */
    public final static int SPLITTING = 64; //kind of assexual reproduction

    /** DOCUMENT ME! */
    public final static int VEGETATIVE_REPRODUCTION = 128; //kind of assexual reproduction

    /** DOCUMENT ME! */
    public final static int PARTHOGENESIS = 256; //kind of assexual reproduction and apomixis, Gynogenesis, Hybridogenesis, Automictic parthenogenesis

    /** DOCUMENT ME! */
    public final static int FRAGMENTATION = 512; //kind of assexual reproduction

    /** DOCUMENT ME! */
    public final static int SPORE_FORMATION = 1024; //kind of assexual reproduction
}
