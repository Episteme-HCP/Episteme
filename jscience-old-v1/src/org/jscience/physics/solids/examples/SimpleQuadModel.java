/*
 * Created on Feb 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids.examples;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

import org.jscience.physics.solids.AtlasModel;
import org.jscience.physics.solids.AtlasNode;
import org.jscience.physics.solids.AtlasPreferences;
import org.jscience.physics.solids.InvalidSolutionException;
import org.jscience.physics.solids.constraint.NodeConstraint;
import org.jscience.physics.solids.element.Quad2DElement;
import org.jscience.physics.solids.geom.AtlasVector;
import org.jscience.physics.solids.load.NodeLoad;
import org.jscience.physics.solids.material.PlaneStress;
import org.jscience.physics.solids.solution.LinearStaticSolution;

import java.io.IOException;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class SimpleQuadModel {

    //static Logger AtlasLogger = Logger.getLogger((SimpleQuadModel.class).getName());
    /*
      * SimpleRodModel.java
      *
      * Created on December 29, 2004, 9:22 PM
      */

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Model definitions stuff
        int numelems = 8; //Number of quad elements;
        double thickness = 0.10; //thickness of plates
        double modulus = 10.3e6; //Modulus of plates
        double nu = 0.30;
        int numnodes = 15;

        AtlasPreferences pref = new AtlasPreferences();
        pref.setLoggingLevel(1);

        //
        // Start Logger
        //
        //if (pref.getLoggingLevel() == 1) {
        //	PropertyConfigurator.configure("atlasinfo_log4j.properties");
        //} else if (pref.getLoggingLevel() == 2) {
        //	PropertyConfigurator.configure("atlasdebug_log4j.properties");
        //}
        //AtlasLogger.info("\n");
        //AtlasLogger.info("==================================");
        //AtlasLogger.info(" ");
        //AtlasLogger.info("  Beginning Atlas Solution Trace  ");
        //AtlasLogger.info(" ");
        //AtlasLogger.info("==================================");
        //AtlasLogger.info("\n");

        //Create a new AtlasModel
        AtlasModel fem = new AtlasModel("Simple Quad Model", pref);

        /*
           * This is the beginning of the model generation. Normally, this would
           * be handled by reading in a file, etc..
           */

        //Populate it with nodes - each node is one inch long and spaced on X
        // axis
        AtlasNode[] nodes = new AtlasNode[numnodes];
        nodes[0] = new AtlasNode("0", 0.0, 0.0, 0.0);
        fem.addObject(nodes[0]);
        nodes[1] = new AtlasNode("1", 0.0, 2.0, 0.0);
        fem.addObject(nodes[1]);
        nodes[2] = new AtlasNode("2", 0.0, 4.0, 0.0);
        fem.addObject(nodes[2]);
        nodes[3] = new AtlasNode("3", 2.0, 0.0, 0.0);
        fem.addObject(nodes[3]);
        nodes[4] = new AtlasNode("4", 2.0, 2.0, 0.0);
        fem.addObject(nodes[4]);
        nodes[5] = new AtlasNode("5", 2.0, 4.0, 0.0);
        fem.addObject(nodes[5]);
        nodes[6] = new AtlasNode("6", 4.0, 0.0, 0.0);
        fem.addObject(nodes[6]);
        nodes[7] = new AtlasNode("7", 4.0, 2.0, 0.0);
        fem.addObject(nodes[7]);
        nodes[8] = new AtlasNode("8", 4.0, 4.0, 0.0);
        fem.addObject(nodes[8]);
        nodes[9] = new AtlasNode("9", 6.0, 0.0, 0.0);
        fem.addObject(nodes[9]);
        nodes[10] = new AtlasNode("10", 6.0, 2.0, 0.0);
        fem.addObject(nodes[10]);
        nodes[11] = new AtlasNode("11", 6.0, 4.0, 0.0);
        fem.addObject(nodes[11]);
        nodes[12] = new AtlasNode("12", 8.0, 0.0, 0.0);
        fem.addObject(nodes[12]);
        nodes[13] = new AtlasNode("13", 8.0, 2.0, 0.0);
        fem.addObject(nodes[13]);
        nodes[14] = new AtlasNode("14", 8.0, 4.0, 0.0);
        fem.addObject(nodes[14]);

        //Create Material
        PlaneStress mat = new PlaneStress("Aluminum", modulus, nu);
        fem.addObject(mat);

        //Populate it with elements
        AtlasNode[] nodeList0 = new AtlasNode[4];
        nodeList0[0] = nodes[0];
        nodeList0[1] = nodes[3];
        nodeList0[2] = nodes[4];
        nodeList0[3] = nodes[1];
        Quad2DElement newEl = new Quad2DElement("0", nodeList0, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);
        AtlasNode[] nodeList1 = new AtlasNode[4];
        nodeList1[0] = nodes[1];
        nodeList1[1] = nodes[4];
        nodeList1[2] = nodes[5];
        nodeList1[3] = nodes[2];
        newEl = new Quad2DElement("1", nodeList1, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);
        AtlasNode[] nodeList2 = new AtlasNode[4];
        nodeList2[0] = nodes[3];
        nodeList2[1] = nodes[6];
        nodeList2[2] = nodes[7];
        nodeList2[3] = nodes[4];
        newEl = new Quad2DElement("2", nodeList2, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);
        AtlasNode[] nodeList3 = new AtlasNode[4];
        nodeList3[0] = nodes[4];
        nodeList3[1] = nodes[7];
        nodeList3[2] = nodes[8];
        nodeList3[3] = nodes[5];
        newEl = new Quad2DElement("3", nodeList3, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);
        AtlasNode[] nodeList4 = new AtlasNode[4];
        nodeList4[0] = nodes[6];
        nodeList4[1] = nodes[9];
        nodeList4[2] = nodes[10];
        nodeList4[3] = nodes[7];
        newEl = new Quad2DElement("4", nodeList4, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);
        AtlasNode[] nodeList5 = new AtlasNode[4];
        nodeList5[0] = nodes[7];
        nodeList5[1] = nodes[10];
        nodeList5[2] = nodes[11];
        nodeList5[3] = nodes[8];
        newEl = new Quad2DElement("5", nodeList5, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);
        AtlasNode[] nodeList6 = new AtlasNode[4];
        nodeList6[0] = nodes[9];
        nodeList6[1] = nodes[12];
        nodeList6[2] = nodes[13];
        nodeList6[3] = nodes[10];
        newEl = new Quad2DElement("6", nodeList6, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);
        AtlasNode[] nodeList7 = new AtlasNode[4];
        nodeList7[0] = nodes[10];
        nodeList7[1] = nodes[13];
        nodeList7[2] = nodes[14];
        nodeList7[3] = nodes[11];
        newEl = new Quad2DElement("7", nodeList7, mat);
        newEl.setThickness(0.10);
        fem.addObject(newEl);

        //Add constraints - first node at 0,0,0 is brickwalled
        int[] dof = {1, 2};
        fem.addObject(new NodeConstraint("Brickwall1", nodes[0], dof));
        fem.addObject(new NodeConstraint("Brickwall2", nodes[1], dof));
        fem.addObject(new NodeConstraint("Brickwall3", nodes[2], dof));
        int[] dof2 = {3, 4, 5, 6};
        fem.addObject(new NodeConstraint("C1", nodes[0], dof2));
        fem.addObject(new NodeConstraint("C2", nodes[1], dof2));
        fem.addObject(new NodeConstraint("C3", nodes[2], dof2));
        fem.addObject(new NodeConstraint("C4", nodes[3], dof2));
        fem.addObject(new NodeConstraint("C5", nodes[4], dof2));
        fem.addObject(new NodeConstraint("C6", nodes[5], dof2));
        fem.addObject(new NodeConstraint("C7", nodes[6], dof2));
        fem.addObject(new NodeConstraint("C8", nodes[7], dof2));
        fem.addObject(new NodeConstraint("C9", nodes[8], dof2));
        fem.addObject(new NodeConstraint("C10", nodes[9], dof2));
        fem.addObject(new NodeConstraint("C11", nodes[10], dof2));
        fem.addObject(new NodeConstraint("C12", nodes[11], dof2));
        fem.addObject(new NodeConstraint("C13", nodes[12], dof2));
        fem.addObject(new NodeConstraint("C14", nodes[13], dof2));
        fem.addObject(new NodeConstraint("C15", nodes[14], dof2));

        //Add loads - last node has axial force on it
        AtlasVector force = new AtlasVector(100.0, 0.0, 0.0);
        AtlasVector moment = new AtlasVector(0.0, 0.0, 0.0);
        fem.addObject(new NodeLoad("Load 1", nodes[12], force, moment));
        force = new AtlasVector(200.0, 0.0, 0.0);
        fem.addObject(new NodeLoad("Load 2", nodes[13], force, moment));
        force = new AtlasVector(100.0, 0.0, 0.0);
        fem.addObject(new NodeLoad("Load 3", nodes[14], force, moment));

        //Add linear static solution
        LinearStaticSolution solution = new LinearStaticSolution(
                "Test Solution");
        fem.addObject(solution);

        //
        // Dump model to xml file
        //
        try {
            fem.writeXML("C:\\temp\\model.xml");

        } catch (IOException bomb) {
            //AtlasLogger.error("FATAL: " + bomb.getMessage());
        }

        /*
           * This is the end of the model generation
           */

        //Solve the model... one line of code
        try {
            solution.solveModel();

        } catch (InvalidSolutionException bomb) {
            //AtlasLogger.error("FATAL: " + bomb.getMessage());
        }

    }

	
}
