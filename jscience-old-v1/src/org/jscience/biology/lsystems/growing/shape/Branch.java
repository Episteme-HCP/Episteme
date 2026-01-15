/*
---------------------------------------------------------------------------
VIRTUAL PLANTS
==============

This Diploma work is a computer graphics project made at the University
of applied sciences in Biel, Switzerland. http://www.hta-bi.bfh.ch
The taks is to simulate the growth of 3 dimensional plants and show
them in a virtual world.
This work is based on the ideas of Lindenmayer and Prusinkiewicz which
are taken from the book 'The algorithmic beauty of plants'.
The Java and Java3D classes have to be used for this work. This file
is one class of the program. For more information look at the VirtualPlants
homepage at: http://www.hta-bi.bfh.ch/Projects/VirtualPlants

Hosted by Claude Schwab

Developed by Rene Gressly
http://www.gressly.ch/rene

25.Oct.1999 - 17.Dec.1999

Copyright by the University of applied sciences Biel, Switzerland
----------------------------------------------------------------------------
*/
package org.jscience.biology.lsystems.growing.shape;

import org.jscience.biology.lsystems.common.TruncatedCone;

import javax.media.j3d.Appearance;


/**
 * This class represents a branch of a plant.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class Branch extends TruncatedCone {
/**
     * Pass all needed information to make the branch in the constructor.
     *
     * @param fAge    The age of the branch. This is also the length.
     * @param radius1 The bottom radius of the branch.
     * @param radius2 The top radius of the branch.
     * @param ap      The appearance to use with this branch.
     */
    public Branch(float fAge, float radius1, float radius2, Appearance ap) {
        super(radius1, radius2, fAge, ap);
    }
}
