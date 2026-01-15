/*
 * Created on Feb 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids.io;

import org.jscience.physics.solids.AtlasModel;
import org.jscience.physics.solids.AtlasPreferences;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AtlasInput {

    private String filename;

    public AtlasInput(String fname) {
        this.filename = fname;
    }


    public AtlasModel loadModel() throws Exception {
        //File inFile = new File

        AtlasPreferences pref = new AtlasPreferences();
        AtlasModel fem = new AtlasModel(" ", pref);


        return fem;
    }

}
