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

package org.jscience.physics.solids.examples;

import org.jscience.physics.solids.*;
import org.jscience.physics.solids.constraint.NodeConstraint;
import org.jscience.physics.solids.constraint.UnsupportedConstraint;
import org.jscience.physics.solids.element.Beam2DElement;
import org.jscience.physics.solids.load.DistLoad;
import org.jscience.physics.solids.material.BeamMat;
import org.jscience.physics.solids.properties.RectangularSection;
import org.jscience.physics.solids.solution.LinearStaticSolution;

import java.io.IOException;

//import org.apache.log4j.Logger;

//import org.apache.log4j.PropertyConfigurator;
/**
 * Example problem of an extremely simple rod.
 * <p/>
 * It is ten inches long, with constraint on one end and an axial load at the other.
 *
 * @author Wegge
 */
public class SimpleBeamModel {

    //static Logger AtlasLogger = Logger.getLogger((SimpleBeamModel.class).getName());

    private AtlasModel fem;


    public SimpleBeamModel() {

        //Model definitions stuff
        int numelems = 4; //Number of beam elements;
        int numnodes = 5;

        AtlasPreferences pref = new AtlasPreferences();
        //Create a new AtlasModel
        fem = new AtlasModel("Simple Beam Model", pref);

        /*
        * This is the beginning of the model generation.
        * Normally, this would be handled by reading in a file, etc..
        */

        //Populate it with nodes - each node is one inch long and spaced on X axis
        AtlasNode[] nodes = new AtlasNode[numnodes];
        String sid;
        double xloc;
        double yloc;
        nodes[0] = new AtlasNode("0", 0.0, 0.0, 0.0);
        fem.addObject(nodes[0]);
        nodes[1] = new AtlasNode("1", 1.0, 0.0, 0.0);
        fem.addObject(nodes[1]);
        nodes[2] = new AtlasNode("2", 2.0, 0.0, 0.0);
        fem.addObject(nodes[2]);
        nodes[3] = new AtlasNode("3", 3.0, 0.0, 0.0);
        fem.addObject(nodes[3]);
        nodes[4] = new AtlasNode("4", 4.0, 0.0, 0.0);
        fem.addObject(nodes[4]);

        // Element properties
        AtlasSection[] props = new AtlasSection[1];
        props[0] = new RectangularSection("Prop 1", 1.0, 1.0);
        fem.addObject(props[0]);

        // Element Materials
        BeamMat[] mats = new BeamMat[1];
        mats[0] = new BeamMat("Mat1", 10.3e6);
        fem.addObject(mats[0]);

        //Populate it with elements
        AtlasElement[] elems = new AtlasElement[4];
        elems[0] = new Beam2DElement("0", nodes[0], nodes[1], props[0], mats[0]);
        elems[1] = new Beam2DElement("1", nodes[1], nodes[2], props[0], mats[0]);
        elems[2] = new Beam2DElement("2", nodes[2], nodes[3], props[0], mats[0]);
        elems[3] = new Beam2DElement("3", nodes[3], nodes[4], props[0], mats[0]);
        fem.addObject(elems[0]);
        fem.addObject(elems[1]);
        fem.addObject(elems[2]);
        fem.addObject(elems[3]);

        //Add constraints - first node at 0,0,0 is brickwalled
        int[] dof = {1, 2};
        fem.addObject(new NodeConstraint("Pin Support", nodes[0], dof));
        int[] dof1 = {2};
        fem.addObject(new NodeConstraint("SimpleSupport", nodes[4], dof1));
        int[] dof2 = {3, 4, 5};
        fem.addObject(new UnsupportedConstraint("C1", nodes[0], dof2));
        fem.addObject(new UnsupportedConstraint("C2", nodes[1], dof2));
        fem.addObject(new UnsupportedConstraint("C3", nodes[2], dof2));
        fem.addObject(new UnsupportedConstraint("C4", nodes[3], dof2));
        fem.addObject(new UnsupportedConstraint("C5", nodes[4], dof2));

        //Add loads - last node has axial force on it

        fem.addObject(new DistLoad("L1", (CurveElement) elems[0], nodes[0], nodes[1], 100.0));
        fem.addObject(new DistLoad("L2", (CurveElement) elems[1], nodes[1], nodes[2], 100.0));
        fem.addObject(new DistLoad("L3", (CurveElement) elems[2], nodes[2], nodes[3], 100.0));
        fem.addObject(new DistLoad("L4", (CurveElement) elems[3], nodes[3], nodes[4], 100.0));

        //AtlasLogger.info( fem.toString() );

        //Add linear static solution
        LinearStaticSolution solution = new LinearStaticSolution(
                "Test Solution");
        fem.addObject(solution);

        //Solve the model... one line of code
        try {
            solution.solveModel();

        } catch (InvalidSolutionException bomb) {
            //AtlasLogger.error("FATAL: " + bomb.getMessage());
        }

        //Then what?

        //AtlasLogger.info(" Problem has finished.");


    }

    public AtlasModel getModel() {
        return fem;
    }


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
        //AtlasLogger.info("Running Simple Beam Example Problem.\n\n");

        SimpleBeamModel srm = new SimpleBeamModel();

        AtlasModel fem = srm.getModel();

        //AtlasLogger.info( fem.toString() );

        try {
            fem.writeXML("C:\\temp\\model.xml");
        } catch (IOException io) {
            io.printStackTrace();
        }
        //AtlasLogger.info("Done");
        System.exit(0);

        /*
        * This is the end of the model generation
        */

        //Solve the model... one line of code
        try {

            //Add linear static solution
            LinearStaticSolution solution = new LinearStaticSolution("Test Solution");
            fem.addObject(solution);

            solution.solveModel();

        } catch (InvalidSolutionException bomb) {
            //AtlasLogger.error("FATAL: " + bomb.getMessage());
        }

        //Then what?

        //AtlasLogger.info("\n\nProblem has finished.");
    }


}
