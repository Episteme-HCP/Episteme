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
 * Beam.java
 *
 * Created on October 19, 2001, 11:00 PM
 */
package org.jscience.physics.nuclear.kinematics.nuclear;

/**
 * 
DOCUMENT ME!
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class Beam {
    /**
     * DOCUMENT ME!
     */
    public Nucleus nucleus;

    /**
     * DOCUMENT ME!
     */
    public double energy;

/**
     * Creates new Beam
     */
    public Beam(Nucleus nucleus, double energy) {
        this.nucleus = nucleus;
        this.energy = energy;
    }
}
