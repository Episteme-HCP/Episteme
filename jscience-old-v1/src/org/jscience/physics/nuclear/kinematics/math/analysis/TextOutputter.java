/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/

/*
 * TextOutputter.java
 *
 * Created on June 18, 2001, 12:08 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


/**
 * Gives a framework for subclasses to use System.out to creat their output
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class TextOutputter extends Object {
    /**
     * DOCUMENT ME!
     */
    static public String DEFAULT = "default";

    /**
     * DOCUMENT ME!
     */
    PrintStream defaultOutput = System.out;

    /**
     * DOCUMENT ME!
     */
    FileOutputStream fos;

/**
     * Creates new TextOutputter
     */
    public TextOutputter(String output) throws FileNotFoundException {
        setOutput(output);
    }

    /**
     * DOCUMENT ME!
     *
     * @param output DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    void setOutput(String output) throws FileNotFoundException {
        if (!output.equals(DEFAULT)) {
            fos = new FileOutputStream(output);
            System.setOut(new PrintStream(fos));
            defaultOutput.println("Output now going to '" + output + "'");
        } //else if DEFAULT, leave it
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void closeOutput() throws IOException {
        if (fos != null) {
            fos.flush();
            fos.close();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void revertToDefaultOutput() {
        System.setOut(defaultOutput);
    }
}
