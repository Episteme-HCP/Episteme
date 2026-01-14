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

package org.jscience.physics.fluids.dynamics.mesh;

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.util.VectorFastInt;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class GenerateNVI extends Object {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    // number maximum of neightbours that a given node can have
    // It should be a reasonable and big number ... the memory
    // that is used here will be available when the class endes
    /** DOCUMENT ME! */
    private int MAX_NEIGHBOURS;

    /** DOCUMENT ME! */
    private int[] neighbours;

    // the first index indicates n1, n2, n3 ...
    /** DOCUMENT ME! */
    private int[][] n;

    /** DOCUMENT ME! */
    private int[] nnvi;

    /** DOCUMENT ME! */
    private int[] nvpn;

    /** DOCUMENT ME! */
    private int totalNodes;

    /** DOCUMENT ME! */
    private int totalElements;

    /** DOCUMENT ME! */
    private int nodesByElement;

    // If there was out of range in the NNVI array.
    /** DOCUMENT ME! */
    private boolean outOfRange;

    /** DOCUMENT ME! */
    private int timesRegenerated;

/**
     * Creates a new GenerateNVI object.
     *
     * @param kadfc      DOCUMENT ME!
     * @param totalNodes DOCUMENT ME!
     * @param nodesArray DOCUMENT ME!
     */
    public GenerateNVI(KernelADFC kadfc, int totalNodes, int[][] nodesArray) {
        kernel = kadfc;
        this.totalNodes = totalNodes;
        n = nodesArray;
        nodesByElement = n.length;

        totalElements = n[0].length;
        MAX_NEIGHBOURS = nodesByElement * nodesByElement;

        timesRegenerated = 0;
        nnvi = nvpn = null;
    }

    /**
     * This method generates the arrays NVI y NNVI from the elements
     * data. Care ! indexes start at 0!
     *
     * @return DOCUMENT ME!
     */
    public int[] generateNNVI() {
        // perhaps this is already calculated
        if (nnvi != null) {
            return nnvi;
        }

        long startTime = System.currentTimeMillis();

        // array initialisation. All nodes are neighbours of themselves
        // the other neighbours value is 0 at start.
        neighbours = new int[totalNodes * MAX_NEIGHBOURS];

        for (int j = 0; j < totalNodes; j++) {
            neighbours[(j * MAX_NEIGHBOURS) + 0] = j; // neighbour of itself

            for (int i = 1; i < MAX_NEIGHBOURS; i++)
                neighbours[(j * MAX_NEIGHBOURS) + i] = -1;
        }

        // We traverse all elements, and for each element node,
        // add all its neighbours to the list of neighbours.
        // hazneighbours() se encarga of cogerlo if the indice es menor y
        // of hacer the referencia simetrica.
        outOfRange = false;

        for (int e = 0; e < totalElements; e++)
            for (int nc1 = 0; nc1 < nodesByElement; nc1++)
                for (int nc2 = nc1 + 1; nc2 < nodesByElement; nc2++)
                    makeNeighbour(n[nc1][e], n[nc2][e]);

        if (outOfRange) {
            nnvi = null;
            neighbours = null;
            System.gc();
            MAX_NEIGHBOURS += nodesByElement;
            outOfRange = false;

            timesRegenerated++;

            return generateNNVI();
        }

        /* ORDER HERE FROM BIGGER TO SMALLER! */
        /* Obtein the NNVI: traverse the array of neighbours as it is, ignoring
         -1 and ordering rows from bigger to smaller. */
        VectorFastInt v = new VectorFastInt(2 * totalNodes * nodesByElement,
                totalNodes);
        VectorFastInt ristra = new VectorFastInt();

        for (int i = 0; i < totalNodes; i++) {
            ristra.reset();

            for (int j = 0; j < MAX_NEIGHBOURS; j++)
                if (neighbours[(i * MAX_NEIGHBOURS) + j] != -1) {
                    ristra.addElement(neighbours[(i * MAX_NEIGHBOURS) + j]);
                }

            ristra.sortArray(-1);

            int[] m = ristra.array();
            int ms = ristra.size();

            for (int k = 0; k < ms; k++)
                v.addElement(m[k]);
        }

        // convert from Vector to array
        nnvi = v.getTruncatedArray();

        // neighbours can be a quite big matrix, we
        // send it now for GC.
        neighbours = null;
        System.gc();

        if (timesRegenerated > 0) {
            kernel.out("<FONT COLOR=#FF0000><B>GenerateNVI:</B> needed " +
                timesRegenerated + " back round(s).</FONT>");
        }

        kernel.out("<B>GenerateNVI:</B> array <B>NNVI[</B>" + nnvi.length +
            "<B>]</B>");

        return nnvi;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] generateNVPN() {
        // we need the nnvi
        if (nnvi == null) {
            nnvi = generateNNVI();
        }

        /* We calculate the NVPN: each entry of the NVPN denotes a leap
        that can be seen with (value <= actualNode) */
        int value;

        /* We calculate the NVPN: each entry of the NVPN denotes a leap
        that can be seen with (value <= actualNode) */
        int neighbours;

        /* We calculate the NVPN: each entry of the NVPN denotes a leap
        that can be seen with (value <= actualNode) */
        int acumulated;

        /* We calculate the NVPN: each entry of the NVPN denotes a leap
        that can be seen with (value <= actualNode) */
        int actualNode;

        /* We calculate the NVPN: each entry of the NVPN denotes a leap
        that can be seen with (value <= actualNode) */
        int nnvipos = 1;
        long startTime = System.currentTimeMillis();

        value = nnvi[0];
        acumulated = 0;
        nvpn = new int[totalNodes + 1];

        int nvpnInd = 0;

        while (nnvipos < nnvi.length) {
            actualNode = value;
            value = nnvi[nnvipos++];
            neighbours = 1;

            while ((value <= actualNode) && (nnvipos < nnvi.length)) {
                value = nnvi[nnvipos++];
                neighbours++;
            }

            nvpn[nvpnInd++] = acumulated;
            acumulated += neighbours;
        }

        // the +1 is added to point to the start of the row that
        // does not exist now (end of the NNVI)
        nvpn[nvpnInd++] = acumulated + 1;

        kernel.out("<B>GenerateNVI:</B> array <B>NVPN[</B>" + nvpn.length +
            "<B>]</B>");

        return nvpn;
    }

    // Makes that two nodes become neighbours, we have to unfold
    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    private void makeNeighbour(int a, int b) {
        putNeighbour(a, b);
        putNeighbour(b, a);
    }

    // Assigns a node as neighbour of the other
    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param neighbour DOCUMENT ME!
     */
    private void putNeighbour(int node, int neighbour) {
        // If bigger we even do not consider, by definition
        // of the array NNVI
        if ((neighbour >= node) || outOfRange) {
            return;
        }

        // look if there... if not, add it
        for (int i = 1; i < MAX_NEIGHBOURS; i++) {
            if (neighbours[(node * MAX_NEIGHBOURS) + i] == neighbour) {
                return; // is here
            }

            if (neighbours[(node * MAX_NEIGHBOURS) + i] == -1) {
                neighbours[(node * MAX_NEIGHBOURS) + i] = neighbour;

                return; // we have added it
            }

            // if not, keep searching either hole or the node
        }

        // This error is fundamental.
        outOfRange = true;

        // We should launch an excepction !!
    }
}
