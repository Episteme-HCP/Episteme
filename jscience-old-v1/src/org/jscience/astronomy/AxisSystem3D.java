/*
  File: CoordinateSystem.java

  University of Applied Science Berne,HTA-Biel/Bienne,
  Computer Science Department.

  Diploma thesis J3D Solar System Simulator
  Originally written by Marcel Portner & Bernhard Hari (c) 2000

  CVS - Information :

  $Header: /zpool01/javanet/scm/svn/tmp/cvs2svn/jade/v1/src/org/jscience/astronomy/AxisSystem3D.java,v 1.2 2007-10-23 18:14:17 virtualcall Exp $
  $Author: virtualcall $
  $Date: 2007-10-23 18:14:17 $
  $State: Exp $

*/
package org.jscience.astronomy;

import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;


/**
 * This class creates a right-handed 3D coordinate system.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.2 $
 */
public class AxisSystem3D extends Shape3D {
    /** Definition of the geometry of the three axis. */
    private static final float[] EXTREMITES = {
            // x-axis
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            
            // y-axis
            0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            
            // z-axis
            0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
        };

    /** Colors of the three axis. */
    private static final float[] COLOR = {
            // x-axis
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            
            // y-axis
            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            
            // z-axis
            1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f
        };

    /** The scale factor */
    private static final float SCALE = 1.0f;

    /** DOCUMENT ME! */
    private float scale;

/**
     * Initializes a new coordinate system with the length of the axis to one
     * meter.
     */
    public AxisSystem3D() {
        this(SCALE);
    }

/**
     * Initializes a new coordinate system.
     *
     * @param scale the scale factor to adjust the axis's length in meter.
     */
    public AxisSystem3D(float scale) {
        this.scale = scale;
        this.setGeometry(createGeometry());
    }

    /**
     * Returns the Geometry of the coordinate system.
     *
     * @return the Geometry of a coordinate system. The axis has three
     *         different colors and is scaled.
     */
    private Geometry createGeometry() {
        // Construction of the axis (LineArray).
        LineArray axis = new LineArray(6,
                LineArray.COORDINATES | LineArray.COLOR_3);

        // Scalling of the vertices of the 3 axis using scale.
        float[] scaledExtremites = new float[EXTREMITES.length];

        for (int i = 0; i < EXTREMITES.length; i++) {
            scaledExtremites[i] = EXTREMITES[i] * scale;
        }

        axis.setCoordinates(0, scaledExtremites);
        axis.setColors(0, COLOR);

        return axis;
    }
}
