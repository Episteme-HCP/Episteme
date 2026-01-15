/*
 * BorderTracingOp.java
 * Created on 29 November 2004, 13:59
 *
 * Copyright 2004, Generation5. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.jscience.computing.ai.vision;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import java.util.LinkedList;


/**
 * Placeholder - this class is not yet implemented!
 *
 * @author James Matthews
 */
public class BorderTracingOp extends Filter {
    /** DOCUMENT ME! */
    public final int INNER_TRACE = 0;

    /** DOCUMENT ME! */
    public final int OUTER_TRACE = 1;

    /** DOCUMENT ME! */
    public final int CONNECTIVITY_FOUR = 4;

    /** DOCUMENT ME! */
    public final int CONNECTIVITY_EIGHT = 8;

    /** DOCUMENT ME! */
    protected int scanStartX = 0;

    /** DOCUMENT ME! */
    protected int scanStartY = 0;

    /** DOCUMENT ME! */
    protected int connectivity = CONNECTIVITY_EIGHT;

    /** DOCUMENT ME! */
    protected LinkedList borderList = new LinkedList();

/**
     * Creates a new instance of BorderTracingOp
     */
    public BorderTracingOp() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     * @param output DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected BufferedImage innerBorder(BufferedImage input,
        BufferedImage output) {
        //   1      3 2 1
        // 2   0    4   0
        //   3      5 6 7
        Raster in = input.getRaster();
        int borderStartX = -1;
        int borderStartY = -1;

        // Firstly, find the border start
        for (int y = scanStartY; y < input.getHeight(); y++) {
            for (int x = scanStartX; x < input.getWidth(); x++) {
                if (in.getSample(x, y, 0) == 0) {
                    borderStartX = x;
                    borderStartY = y;

                    break;
                }
            }

            if ((borderStartX != -1) || (borderStartY != -1)) {
                break;
            }
        }

        // Secondly initiate the border trace
        borderList.add(new Integer(7));

        int b = ((Integer) (borderList.getFirst())).intValue();

        return output;
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     * @param output DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.image.BufferedImage filter(BufferedImage image,
        BufferedImage output) {
        if ((output == null) ||
                (output.getType() != BufferedImage.TYPE_BYTE_GRAY)) {
            output = new BufferedImage(image.getWidth(), image.getHeight(),
                    image.TYPE_BYTE_GRAY);
        }

        return innerBorder(image, output);
    }
}
