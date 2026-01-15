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
 * RadiusRange.java
 *
 * Created on October 23, 2001, 4:08 PM
 */
package org.jscience.physics.nuclear.kinematics.nuclear.gui;

/**
 * @author org.jscience.physics.nuclear.kinematics
 */
public interface RadiusRange {
    //public double rhoMin,rhoMax;
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRhoMin();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRhoMax();
}
