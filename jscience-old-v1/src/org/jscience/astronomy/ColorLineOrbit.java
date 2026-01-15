/*
  File: ColorLineOrbit.java

  University of Applied Science Berne,HTA-Biel/Bienne,
  Computer Science Department.

  Diploma thesis J3D Solar System Simulator
  Originally written by Marcel Portner & Bernhard Hari (c) 2000

  CVS - Information :

  $Header: /zpool01/javanet/scm/svn/tmp/cvs2svn/jade/v1/src/org/jscience/astronomy/ColorLineOrbit.java,v 1.2 2007-10-23 18:14:17 virtualcall Exp $
  $Author: virtualcall $
  $Date: 2007-10-23 18:14:17 $
  $State: Exp $

*/
package org.jscience.astronomy;

import java.awt.*;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


/**
 * This is a class for the color line orbit of the planet.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.2 $
 */
public class ColorLineOrbit extends ColorOrbit {
    /** DOCUMENT ME! */
    private LineStripArray orbit;

/**
     * Initializes a new ColorLineOrbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public ColorLineOrbit(Color orbitColor) {
        super(orbitColor);
    }

    /**
     * This method set the color orbit with the given positions.
     *
     * @param pos the positions of the orbit.
     */
    public void setPositions(Point3f[] pos) {
        nbOfPoints = pos.length + 1;

        Color3f[] colors = new Color3f[nbOfPoints];
        Color3f color = new Color3f(orbitColor);

        for (int i = 0; i < nbOfPoints; i++) {
            colors[i] = color;
        }

        int[] stripVertexCounts = { nbOfPoints };

        orbit = new LineStripArray(nbOfPoints,
                GeometryArray.COORDINATES | /*GeometryArray.NORMALS |*/
            GeometryArray.COLOR_3, stripVertexCounts);

        orbit.setCoordinates(0, pos);
        orbit.setCoordinate(nbOfPoints - 1, pos[0]);
        orbit.setColors(0, colors);
        orbit.setCapability(GeometryArray.ALLOW_COLOR_WRITE);

        this.setGeometry(orbit);
    }

    /**
     * This method re - set the color of the orbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public void setColor(Color orbitColor) {
        this.orbitColor = orbitColor;

        Color3f[] colors = new Color3f[nbOfPoints];
        Color3f color = new Color3f(orbitColor);

        for (int i = 0; i < nbOfPoints; i++) {
            colors[i] = color;
        }

        orbit.setColors(0, colors);
    }
}
