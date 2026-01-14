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

package org.jscience.computing.ai.vision;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


/**
 * This class implements a simple rank filter: allowing you to select the
 * median, minimum or maximum for any given neighbourhood size. Note that this
 * class is <i>unapologetically slow</i>! There are no optimizations at all,
 * to ensure the best code readability.
 *
 * @author James Matthews
 */
public class RankFilter extends Filter {
    /** Calculate the median for the neighbourhood. */
    public final static int MEDIAN = 0;

    /** Retrieve the minimum from the neighbourhood. */
    public final static int MINIMUM = 1;

    /** Retrieve the maximum from the neighbourhood. */
    public final static int MAXIMUM = 2;

    /** The neighbourhood size. */
    protected int neighbourhoodSize;

    /** The type of rank position (median, maximum, minimum). */
    protected int rankPosition;

/**
     * Creates a new instance of RankFilter
     */
    public RankFilter() {
        this(MEDIAN);
    }

/**
     * Creates a new instance of RankFilter, with the given rank type.
     *
     * @param rank the rank type.
     */
    public RankFilter(int rank) {
        this(rank, 3);
    }

/**
     * Creates a new instance of RankFilter, with the given rank type and
     * neighbourhood size.
     *
     * @param rank              the rank type.
     * @param neighbourhoodSize the neighbourhood size.
     */
    public RankFilter(int rank, int neighbourhoodSize) {
        setRankPosition(rank);
        setNeighbourhoodSize(neighbourhoodSize);
    }

    /**
     * Rank filter an image.
     *
     * @param image the input image.
     * @param output the output image (optional).
     *
     * @return the rank filtered output.
     */
    public BufferedImage filter(BufferedImage image, BufferedImage output) {
        output = verifyOutput(output, image);

        Raster src = image.getRaster();
        WritableRaster out = output.getRaster();

        int nb = neighbourhoodSize;
        int hnb = nb / 2;
        int[] pixel = new int[src.getNumBands()];
        int ppos = 0;
        int[] pixels = new int[nb * nb];

        int rank;

        switch (rankPosition) {
        default:
        case MEDIAN:
            rank = ((nb * nb) - 1) / 2;

            break;

        case MINIMUM:
            rank = 0;

            break;

        case MAXIMUM:
            rank = (nb * nb) - 1;

            break;
        }

        for (int y = hnb; y < (image.getHeight() - hnb); y++) {
            for (int x = hnb; x < (image.getWidth() - hnb); x++) {
                // Get the pixels
                ppos = 0;

                for (int j = -hnb; j < (hnb + 1); j++) {
                    for (int i = -hnb; i < (hnb + 1); i++) {
                        pixels[ppos++] = image.getRGB(x + i, y + j);
                    }
                }

                java.util.Arrays.sort(pixels);

                output.setRGB(x, y, pixels[rank]);
            }
        }

        return output;
    }

    /**
     * Get the neighbourhood size.
     *
     * @return the current neighbourhood size.
     */
    public int getNeighbourhoodSize() {
        return neighbourhoodSize;
    }

    /**
     * Set the neighbourhood size. Note that this must be an odd
     * number, an even numbers will be incremented.
     *
     * @param neighbourhoodSize the new neighbourhood size.
     */
    public void setNeighbourhoodSize(int neighbourhoodSize) {
        // Only odd-sized neighbourhoods allowed
        if ((neighbourhoodSize % 2) == 0) {
            neighbourhoodSize++;
        }

        this.neighbourhoodSize = neighbourhoodSize;
    }

    /**
     * Get the rank position.
     *
     * @return the current rank position.
     */
    public int getRankPosition() {
        return rankPosition;
    }

    /**
     * Set the rank position. See {@link #MEDIAN}, {@link #MINIMUM} or
     * {@link #MAXIMUM}.
     *
     * @param rankPosition the new rank position.
     */
    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        switch (rankPosition) {
        default:
        case MEDIAN:
            return "Median filter (" + neighbourhoodSize + ")";

        case MINIMUM:
            return "Minimum filter (" + neighbourhoodSize + ")";

        case MAXIMUM:
            return "Maximum filter (" + neighbourhoodSize + ")";
        }
    }

    /**
     * Utility method for the class.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println(
                "usage: java RankFilter <input> <output> {type} {neighbourhood}");

            return;
        }

        try {
            BufferedImage in = javax.imageio.ImageIO.read(new java.io.File(
                        args[0]));

            RankFilter rank = new RankFilter();

            //
            // Retrieve any possible optional parameters
            //
            if (args.length > 2) {
                if (args[2].compareToIgnoreCase("maximum") == 0) {
                    rank.setRankPosition(rank.MAXIMUM);
                }

                if (args[2].compareToIgnoreCase("minimum") == 0) {
                    rank.setRankPosition(rank.MINIMUM);
                }
            }

            if (args.length > 3) {
                rank.setNeighbourhoodSize(Integer.parseInt(args[3]));
            }

            // Do the filtering
            BufferedImage out = rank.filter(in);

            // Write the image (FIXME: currently always JPG...)
            javax.imageio.ImageIO.write(out, "jpg", new java.io.File(args[1]));
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }
}
