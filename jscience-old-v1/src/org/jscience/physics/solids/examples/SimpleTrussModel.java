/*
 * SimpleTrussModel.java
 *
 * Created on December 29, 2004, 9:22 PM
 */

package org.jscience.physics.solids.examples;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

import org.jscience.physics.solids.AtlasModel;
import org.jscience.physics.solids.AtlasPreferences;

/**
 * Example problem of a simple truss sytem.
 * <p/>
 * Ref Przemieniecki Figure 9.6 for geometric setup
 *
 * @author Wegge
 */
public class SimpleTrussModel {


    //static Logger AtlasLogger = Logger.getLogger((SimpleTrussModel.class).getName());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AtlasPreferences pref = new AtlasPreferences();
        pref.setLoggingLevel(1);

        //
        // Start Logger
        //
        if (pref.getLoggingLevel() == 1) {
            //PropertyConfigurator.configure("atlasinfo_log4j.properties");
        } else if (pref.getLoggingLevel() == 2) {
            //PropertyConfigurator.configure("atlasdebug_log4j.properties");
        }
        //AtlasLogger.info("\n");
        //AtlasLogger.info("==================================");
        //AtlasLogger.info(" ");
        //AtlasLogger.info("  Beginning Atlas Solution Trace  ");
        //AtlasLogger.info(" ");
        //AtlasLogger.info("==================================");
        //AtlasLogger.info("\n");
        //AtlasLogger.info("Running Simple Truss Example Problem.");

        //Create a new AtlasModel, and populate it with nodes and elements
        AtlasModel fem = new AtlasModel("Simple Truss", pref);

        //AtlasLogger.info("Problem has finished.");

    }

}
