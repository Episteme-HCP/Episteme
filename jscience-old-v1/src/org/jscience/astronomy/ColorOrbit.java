/*
  File: ColorOrbit.java

  University of Applied Science Berne,HTA-Biel/Bienne,
  Computer Science Department.

  Diploma thesis J3D Solar System Simulator
  Originally written by Marcel Portner & Bernhard Hari (c) 2000

  CVS - Information :

  $Header: /zpool01/javanet/scm/svn/tmp/cvs2svn/jade/v1/src/org/jscience/astronomy/ColorOrbit.java,v 1.3 2007-10-23 18:14:17 virtualcall Exp $
  $Author: virtualcall $
  $Date: 2007-10-23 18:14:17 $
  $State: Exp $

*/
package org.jscience.astronomy;

import java.awt.*;

import javax.media.j3d.Shape3D;

import javax.vecmath.Point3f;


/**
 * This is a abstract class for the orbit of the planet.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.3 $
 *
 * @see ColorPointOrbit
 * @see ColorLineOrbit
 */
public abstract class ColorOrbit extends Shape3D {
    /** DOCUMENT ME! */
    protected Color orbitColor;

    /** DOCUMENT ME! */
    protected int nbOfPoints;

/**
     * Initializes a new ColorOrbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public ColorOrbit(Color orbitColor) {
        this.orbitColor = orbitColor;
    }

    /**
     * This method set the color orbit with the given positions.
     *
     * @param pos the positions of the orbit.
     */
    public abstract void setPositions(Point3f[] pos);

    /**
     * This method re - set the color of the orbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public abstract void setColor(Color orbitColor);
}
