package org.jscience.economics;

/**
 * A class representing some useful constants for economics.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class EconomicsConstants extends Object {
    //could put the standard currencies here although they are in the money subpackage
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    //also look into Resource class
    /** DOCUMENT ME! */
    public final static int PRIMARY_INDUSTRY = 1; //Primary industry extracts natural materials and provides raw materials for secondary industry.

    /** DOCUMENT ME! */
    public final static int SECONDARY_INDUSTRY = 2; //Secondary industry processes raw materials or semi-finished goods into more valuable products.

    /** DOCUMENT ME! */
    public final static int TERTIARY_INDUSTRY = 4; //Tertiary industry is the provision of services.

    /** DOCUMENT ME! */
    public final static int QUATERNARY_INDUSTRY = 8; //Quaternary industry is the activity related to the application, manipulation and transmission of information.

    //also look into Task class
    /** DOCUMENT ME! */
    public final static int PRODUCTION_PROCESS = 1; //raw extraction, primary production

    /** DOCUMENT ME! */
    public final static int TRANSFORMATION_PROCESS = 2; //factory production

    /** DOCUMENT ME! */
    public final static int DISTRIBUTION_PROCESS = 4; //the service of moving the product to the place it is going to be sold

    /** DOCUMENT ME! */
    public final static int CONSUMPTION_PROCESS = 8; //the end user

    //kind of industry
    /** DOCUMENT ME! */
    public final static int TRANSPORT = 1;

    /** DOCUMENT ME! */
    public final static int HYGIENE = 2;

    /** DOCUMENT ME! */
    public final static int ALIMENTATION = 4;

    /** DOCUMENT ME! */
    public final static int HABITAT = 8;

    /** DOCUMENT ME! */
    public final static int TECHNOLOGY = 16;

    /** DOCUMENT ME! */
    public final static int ENERGY = 32;

    /** DOCUMENT ME! */
    public final static int SOCIETY = 64;

    //organization sub levels
    /** DOCUMENT ME! */
    public final static int HUMAN_RESOURCES = 1;

    /** DOCUMENT ME! */
    public final static int SALES = 2; //salesman, etc

    /** DOCUMENT ME! */
    public final static int MARKETING = 4; //and communication

    /** DOCUMENT ME! */
    public final static int ACCOUNTING = 8; //salaries, treasure, etc

    /** DOCUMENT ME! */
    public final static int RESEARCH = 16;

    /** DOCUMENT ME! */
    public final static int PRODUCTION = 32;

    /** DOCUMENT ME! */
    public final static int DISTRIBUTION = 64; //includes internal distribution and external shipping

    /** DOCUMENT ME! */
    public final static int BUYING_CENTRE = 128; //includes storage

    /** DOCUMENT ME! */
    public final static int SUPERVIsORY = 256; //board, commitee, head, etc

    /** DOCUMENT ME! */
    public final static int FINANCING = 512; //money
 
   /** DOCUMENT ME! */
    public final static int LEGAL = 1024; //taxs, administrative, legal
  
  /** DOCUMENT ME! */
    public final static int INFRASTRUCTURE = 2048; //and security
  
  /** DOCUMENT ME! */
    public final static int INFORMATION = 4096; //information systems

}
