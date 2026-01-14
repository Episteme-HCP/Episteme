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
