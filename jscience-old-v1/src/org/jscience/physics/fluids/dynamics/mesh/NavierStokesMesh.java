/*
 * Program : ADFC�
 * Class : org.jscience.fluids.mesh.MeshNavierStokes
 *         Class that calcultes and stores all the information
 *         needed for a mesh Navier-Stokes 2D or 3D.
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 26/12/2000
 *
 * This program is distributed under LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.mesh;

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;
import org.jscience.physics.fluids.dynamics.util.Matrix;
import org.jscience.physics.fluids.dynamics.util.VectorFastInt;


/**
 * Load, storage and  manipulation of meshses for Navier-Stokes. The ideal
 * solution would be to unify this class and the Mesh class (lineal) in one
 * which implements an interface.
 * <p/>
 * <p/>
 * This class loads a mesh with triangular quadratic elements and from there,
 * composes the lineal mesh of pressures.
 * </p>
 * <p/>
 * <p></p>
 */
public class NavierStokesMesh {
    /**
     * Version of the class.
     */
    private static final String VERSION = "1.3";

    /**
     * DOCUMENT ME!
     */
    private KernelADFC kernel;

    /**
     * Nombre del problem.
     */
    private String fileGidData;

    /**
     * Vector con the quadratic nodes that form a given element.
     */
    protected int[][] nodesq;

    /**
     * Vector con the linear nodes that form a given element.
     */
    protected int[][] nodesl;

    /**
     * Vector with the coordinates of the quadratic nodes.
     */
    protected double[][] coordq;

    /**
     * Vector with the coordinates of the linear nodes.
     */
    protected double[][] coordl;

    /**
     * Array of conversion of linear to quadratic.
     * <p/>
     * <p/>
     * <code>Lineal = lq[Cuad]; </code>
     * </p>
     */
    public int[] lq;

    /**
     * Array of conversion of quadratic to lineal.
     * <p/>
     * <p/>
     * <code>Cuad = ql[Lineal]; </code>
     * </p>
     */
    public int[] ql;

    /**
     * DOCUMENT ME!
     */
    public int[] nDirichletq;

    /**
     * DOCUMENT ME!
     */
    public int[] nNeumannq;

    /**
     * DOCUMENT ME!
     */
    public int[] nDirichletl;

    /**
     * DOCUMENT ME!
     */
    public int[] nNeumannl;

    /**
     * DOCUMENT ME!
     */
    public double[] vNeumannq;

    /**
     * DOCUMENT ME!
     */
    public double[] vNeumannl;

    /**
     * DOCUMENT ME!
     */
    public double[][] vDirichletq;

    /**
     * DOCUMENT ME!
     */
    public double[][] vDirichletl;

    // public as hack in navier-stokes

    /**
     * DOCUMENT ME!
     */
    public int[] nDragLiftq;

    // public as hack in navier-stokes

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    public int[] nDragLiftl;

    /**
     * DOCUMENT ME!
     */
    public int[] nPerfilDirichletq;

    /**
     * DOCUMENT ME!
     */
    public int[] nSlipq;

    /**
     * DOCUMENT ME!
     */
    public int[] nSlipl;

    /**
     * Array NNVI quadratic.
     */
    private int[] nnviq;

    /**
     * Array NVPN quadratic.
     */
    private int[] nvpnq;

    /**
     * Array NVPN lineal.
     */
    private int[] nvpnl;

    /**
     * Array NNVI lineal.
     */
    private int[] nnvil;

    /**
     * Cache node-element: from a quadratic node tells us which elements
     * contain it.
     */

    //private int[][] cacheq;

    /**
     * Cache node-element: from a lineal node tells us which elements contain
     * it.
     */
    private int[] cnlce;

    /**
     * DOCUMENT ME!
     */
    private int[] iCnlce;

    /**
     * This value should be big enough not to cause an error. Besides, the
     * memory consumed by this array will be freed quickly with the GC.
     */
    private int CACHE_ELEMENTS = 10;

    /**
     * DOCUMENT ME!
     */
    private int CACHE_ELEMENTS_CUAD = 9;

    /**
     * DOCUMENT ME!
     */
    private int DELTA_REGENERATION = 8;

    /**
     * times needed regeneration cache node lineal element
     */
    private int timesRegeneratedCacheNodoElement = 0;

    // Matrices of masses and of rigidity, and of divergence

    /**
     * Matrix of masses lineal.
     */
    private Matrix masses;

    /**
     * Matrix of rigidity lineal.
     */
    private Matrix rigidity;

    /**
     * Matrix of masses quadratic.
     */
    private Matrix massesCuad;

    /**
     * Matrix of rigidity quadratic.
     */
    private Matrix rigidityCuad;

    /**
     * Divergence matrix, lineal, component X.
     */
    private Matrix divergenceX;

    /**
     * Divergence matrix, lineal, component Y.
     */
    private Matrix divergenceY;

    /**
     * Divergence matrix, lineal, component Z.
     */
    private Matrix divergenceZ;

    /**
     * Divergence matrix, quadratic, component X.
     */
    private Matrix divergenceXCuad;

    /**
     * Divergence matrix, quadratic, component Y.
     */
    private Matrix divergenceYCuad;

    /**
     * Derivative matrix, quadratic, component X.
     */
    private Matrix divergenceZCuad;

    /**
     * Derivative matrix, quadratic, component Z.
     */
    private Matrix derivativeXCuad;

    /**
     * Derivative matrix, quadratic, component Y.
     */
    private Matrix derivativeYCuad;

    // Storage of potencial, pressure, partial derivatives and f.
    // private double[] pot, p, dpotdx, dpotdy, f;

    /**
     * Storage of a funcion escalar <code>f</code>.
     */
    private double[] f;

    /**
     * size <code>h</code> or average size of the side of the elements. Used
     * for Smagorinsky.
     */
    private double[] hElement;

    ////////// SLIP DATA

    /**
     * Component list X of the normal in the slip contour.
     */
    public double[] nxSlip;

    /**
     * Component list Y of the normal in the slip contour.
     */
    public double[] nySlip;

    /**
     * List of lengths of the segments in the slip contour.
     */
    public double[] lengthSlip;

    /**
     * List of quadratic nodes <code>L1</code> in the slip contour.
     */
    public int[] l1Slip;

    /**
     * List of quadratic nodes <code>L2</code> in the slip contour.
     */
    public int[] l2Slip;

    /**
     * List of quadratic nodes <code>Q</code> in the slip contour.
     */
    public int[] qSlip;

    /**
     * reference element for a nodeQ dado.
     */
    private int[] elemRefNode;

    /**
     * reference element for a nodeQ dado.
     */
    private int[] elemRefNodeCuad;

    /**
     * true if we are in a cilinder 2D D=1 Drag Lift
     */
    private boolean isCilinderDL;

    /**
     * Constructor.
     *
     * @param kadfc DOCUMENT ME!
     */
    public NavierStokesMesh(KernelADFC kadfc) {
        kernel = kadfc;

        KernelADFCConfiguration c = kernel.getConfiguration();

        // get data
        fileGidData = c.meshName;
        nodesq = c.nodes;
        coordq = c.coordinates;
        nDirichletq = c.nodesDirichlet;
        vDirichletq = c.valuesDirichlet;
        nPerfilDirichletq = c.nodesPerfilDirichlet;
        nNeumannq = c.nodesNeumann;
        vNeumannq = c.valuesNeumann;
        nSlipq = c.nodesSlip;
        nDragLiftq = c.nodesDragLift;

        // check
        if ((coordq.length == 2) || (nodesq.length == 6)) {
            kernel.out("<B>MeshNavierStokes:</B> Elements 2D Taylor-Hood.");
        } else if (is3D()) {
            kernel.out("<B>MeshNavierStokes:</B> Elements Tetraedros 3D.");
        } else {
            kernel.out("<FONT COLOR=#FF0000 SIZE=4><B>MeshNavierStokes:</B> " +
                    "mesh incompatible. Solo sirven 2D Taylor-Hood o Tetraedros 3D.");
            kernel.newErrorFatalDialog("Incompatible mesh.\n" +
                    "The elements are not of the correct type, check\n" +
                    "in the program manual and the mesher.");

            // hang
            try {
                wait();
            } catch (Exception ex) {
            }
        }

        // Check if the nodes of Drag & Lift form a cilinder
        isCilinderDL = false;

        if (nDragLiftq != null) {
            isCilinderDL = true;

            for (int j = 0; j < nDragLiftq.length; j++) {
                double x = coordq[0][nDragLiftq[j]];
                double y = coordq[1][nDragLiftq[j]];

                double r = Math.sqrt((x * x) + (y * y));

                if ((r < 0.45) || (r > 0.55)) {
                    isCilinderDL = false;
                }
            }

            // �a cilinder?
            if (isCilinderDL) {
                kernel.out(
                        "<B>MeshNavierStokes:</B> cilinder </B>D=1</B> Drag&Lift detected.");

                // sort nodes DL.
                // bubble sort.
                // Before, calculate the angle
                double[] angle = new double[nDragLiftq.length];

                for (int j = 0; j < nDragLiftq.length; j++)
                    angle[j] = Math.atan2(coordq[1][nDragLiftq[j]],
                            coordq[0][nDragLiftq[j]]);

                for (int j = nDragLiftq.length - 1; j > 0; j--)
                    for (int i = 0; i < j; i++)
                        if (angle[i] < angle[i + 1]) {
                            int swi = nDragLiftq[i];
                            nDragLiftq[i] = nDragLiftq[i + 1];
                            nDragLiftq[i + 1] = swi;

                            double swd = angle[i];
                            angle[i] = angle[i + 1];
                            angle[i + 1] = swd;
                        }
            }
        }

        // Correct the orientation of the elements.
        new CorrectOrientation(kernel).correct(this);

        // unfold here.
        unfoldMeshs();
        generateMagnus();
        unfoldContourConditions();
        generateCacheNodeLinealElement();

        // Arrays NNVI, NVPN
        GenerateNVI nnviGen = new GenerateNVI(kernel, coordq[0].length, nodesq);
        nnviq = nnviGen.generateNNVI();
        nvpnq = nnviGen.generateNVPN();
        nnviGen = null;
        System.gc();
        nnviGen = new GenerateNVI(kernel, coordl[0].length, nodesl);
        nnvil = nnviGen.generateNNVI();
        nvpnl = nnviGen.generateNVPN();
        nnviGen = null;
        System.gc();

        calculateInfoSlip();

        // Create profileDirichlet if fine
        if ((coordq.length == 2) || (nodesq.length == 6)) {
            kernel.getInputProfile().createDirichletProfile(nPerfilDirichletq,
                    nDirichletq, vDirichletq, coordq[0], coordq[1], this);
        }

        // generate matrices
        masses = new Matrix(null, nvpnl, nnvil, true);
        rigidity = new Matrix(null, nvpnl, nnvil, true);
        rigidityCuad = new Matrix(null, nvpnq, nnviq, true);
        massesCuad = new Matrix(null, nvpnq, nnviq, true);

        if ((coordq.length == 2) || (nodesq.length == 6)) {
            MassRigididy2DLineal.generate(kernel, masses, rigidity, coordl,
                    nodesl);
            MassRigidity2DQuad.generate(kernel, massesCuad, rigidityCuad,
                    coordq, nodesq);

            divergenceX = Divergence2D.getStructureDivergence(kernel, coordl,
                    nodesl, coordq, nodesq);
            System.gc();
            divergenceY = new Matrix(divergenceX, true);
            divergenceXCuad = Divergence2D.getTransposedStructureDivergence(kernel,
                    coordl, nodesl, coordq, nodesq);
            System.gc();
            divergenceYCuad = new Matrix(divergenceXCuad, true);
            Divergence2D.generate(kernel, divergenceX, divergenceY,
                    divergenceXCuad, divergenceYCuad, coordl, nodesl, coordq, nodesq);
        } else if (is3D()) {
            MassRigidity3DLineal.generate(kernel, masses, rigidity, coordl,
                    nodesl);
            MassRigidity3DQuad.generate(kernel, massesCuad, rigidityCuad,
                    coordq, nodesq);

            divergenceX = Divergence3D.getStructureDivergence(kernel, coordl,
                    nodesl, coordq, nodesq);
            System.gc();
            divergenceY = new Matrix(divergenceX, true);
            divergenceZ = new Matrix(divergenceX, true);
            divergenceXCuad = Divergence3D.getTransposedStructureDivergence(kernel,
                    coordl, nodesl, coordq, nodesq);
            System.gc();
            divergenceYCuad = new Matrix(divergenceXCuad, true);
            divergenceZCuad = new Matrix(divergenceXCuad, true);
            Divergence3D.generate(kernel, divergenceX, divergenceY,
                    divergenceZ, divergenceXCuad, divergenceYCuad, divergenceZCuad,
                    coordl, nodesl, coordq, nodesq);
        }
    }

    /**
     * interpolate a lineal field to a quadratic one.
     *
     * @param quadField
     * @param linealField
     * @return quadratic field interpolated in omega
     */
    public double[] calculateInterpolatedField(double[] quadField,
                                               double[] linealField) {
        // mesh 2D triangular
        if ((coordq.length == 2) || (nodesq.length == 6)) {
            for (int e = 0; e < nodesl[0].length; e++) {
                double v1 = linealField[nodesl[0][e]];
                double v2 = linealField[nodesl[1][e]];
                double v3 = linealField[nodesl[2][e]];

                quadField[nodesq[0][e]] = v1;
                quadField[nodesq[1][e]] = v2;
                quadField[nodesq[2][e]] = v3;
                quadField[nodesq[3][e]] = (v1 + v2) / 2.0;
                quadField[nodesq[4][e]] = (v2 + v3) / 2.0;
                quadField[nodesq[5][e]] = (v3 + v1) / 2.0;
            }
        }
        // mesh 3D tetraedrico
        else if ((coordq.length == 3) || (nodesq.length == 10)) {
            for (int e = 0; e < nodesl[0].length; e++) {
                double v1 = linealField[nodesl[0][e]];
                double v2 = linealField[nodesl[1][e]];
                double v3 = linealField[nodesl[2][e]];
                double v4 = linealField[nodesl[3][e]];

                quadField[nodesq[0][e]] = v1;
                quadField[nodesq[1][e]] = v2;
                quadField[nodesq[2][e]] = v3;
                quadField[nodesq[3][e]] = v4;
                quadField[nodesq[4][e]] = (v1 + v2) / 2.0;
                quadField[nodesq[5][e]] = (v2 + v3) / 2.0;
                quadField[nodesq[6][e]] = (v1 + v3) / 2.0;
                quadField[nodesq[7][e]] = (v1 + v4) / 2.0;
                quadField[nodesq[8][e]] = (v2 + v4) / 2.0;
                quadField[nodesq[9][e]] = (v3 + v4) / 2.0;
            }
        }

        return quadField;
    }

    /**
     * Calculates all the information regarding Slip nodes.
     */

    // works if it is not 2D.
    private void calculateInfoSlip() {
        // nSlipq contains all the nodes in the quadratic mesh.
        // We need to know:
        // 1. In which element they are.
        // 2. Size of the side in which they are (integration domain)
        // 3. If quadratic, its  lineal neighbours.
        if ((coordq.length != 2) || (nodesq.length != 6)) {
            System.out.println("CaltulateInfoSlip: no mesh 2D.");

            return;
        }

        if (nSlipq == null) {
            return;
        }

        System.out.println("Caltulating Info SLIP...");

        System.out.println("\t* nodesSlip = " + nSlipq.length + " nodes");

        // for(int j=0; j<nSlipq.length; j++)
        //     System.out.println("nSlipQ["+j+"] = "+nSlipq[j]);
        int[] CNLE = getCacheNodeLinealElement();
        int[] iCNLE = getStartCacheNodeLinealElement();

        /* in the first pass create an array
        with the elements that contain the Slip nodes.
        Do not worry about repetitions. */
        VectorFastInt elemSlip = new VectorFastInt();

        for (int j = 0; j < nSlipl.length; j++) {
            int node = nSlipl[j];

            for (int k = iCNLE[node]; k < iCNLE[node + 1]; k++)

                //if(CNLE[k] != -1)
                elemSlip.addElementNoRep(CNLE[k]);
        }

        /* Done */
        int[] elementsSlip = elemSlip.getTruncatedArray();

        System.out.println("\t* elementsSlip = " + elementsSlip.length +
                " elements");

        /* Now we build an array that will tell us for each element:
           - Which nodes are slip.
           - Lengths of the sides.
           - normals out of the mesh.
         */

        // �How many segments slip do we have? */
        int segmentsSlip = 0;

        for (int e = 0; e < elementsSlip.length; e++) {
            // n1e = nodesq[0][e];
            // Check if the quadratic nodes of the element are
            // in the SLIP array. If the quadratic is there, this side will be there too.
            for (int t = 3; t < 6; t++) // (3,4 y 5)

                for (int i = 0; i < nSlipq.length; i++)
                    if (nodesq[t][elementsSlip[e]] == nSlipq[i]) {
                        segmentsSlip++;
                    }
        }

        System.out.println("\t* segmentsSlip = " + segmentsSlip + " segments");

        // Calculate Slip information.
        nxSlip = new double[segmentsSlip];
        nySlip = new double[segmentsSlip];
        lengthSlip = new double[segmentsSlip];
        l1Slip = new int[segmentsSlip];
        l2Slip = new int[segmentsSlip];
        qSlip = new int[segmentsSlip];

        int sla = 0;
        double[] xcoord = getCoordinatesQuad(0);
        double[] ycoord = getCoordinatesQuad(1);

        for (int e = 0; e < elementsSlip.length; e++) {
            for (int t = 3; t < 6; t++) // (3,4 y 5)

                for (int i = 0; i < nSlipq.length; i++)
                    if (nodesq[t][elementsSlip[e]] == nSlipq[i]) {
                        // Map nodes according to the side
                        l1Slip[sla] = nodesq[t - 3][elementsSlip[e]];
                        l2Slip[sla] = nodesq[(t - 3 + 1) % 3][elementsSlip[e]];

                        int l3Slip = nodesq[(t - 3 + 2) % 3][elementsSlip[e]];
                        qSlip[sla] = nodesq[t][elementsSlip[e]];

                        double tx = xcoord[l2Slip[sla]] - xcoord[l1Slip[sla]];
                        double ty = ycoord[l2Slip[sla]] - ycoord[l1Slip[sla]];

                        // Calculate distance
                        lengthSlip[sla] = Math.sqrt((tx * tx) + (ty * ty));

                        tx /= lengthSlip[sla];
                        ty /= lengthSlip[sla];

                        // Calculate normal
                        double nx = -ty;
                        double ny = tx;

                        // Is it pointing inwards ? We change it.
                        if (((nx * (xcoord[l3Slip] - xcoord[l1Slip[sla]])) +
                                (ny * (ycoord[l3Slip] - ycoord[l1Slip[sla]]))) > 0) {
                            nx *= -1;
                            ny *= -1;
                        }

                        nxSlip[sla] = nx;
                        nySlip[sla] = ny;

                        // System.out.println("se "+sla+"\tnx "+nx+"\tny "+ny);
                        // System.out.println("\ttam "+tamSlip[sla]);
                        // System.out.println("\t"+l1Slip[sla]+"\t"+qSlip[sla]+"\t"+l2Slip[sla]);
                        sla++;
                    }
        }
    }

    /**
     * Returns the conversion to a quadratic of the given linear nodes.
     *
     * @param lineals DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public int[] quadraticFromLineal(int[] lineals) {
        int[] quadratics = new int[lineals.length];

        for (int l = 0; l < lineals.length; l++)
            quadratics[l] = ql[lineals[l]];

        System.out.println("Translated Q<-L : " + lineals.length);

        return quadratics;
    }

    // valid for 2D/3D
    private void unfoldContourConditions() {
        int[] quadratics;
        System.out.println("MeshNavierStokes : unfold contour Conditions");

        // Dirichlet
        // see how many quadratic nodes of the given ones are
        // contained in the lineal mesh.
        if (nDirichletq != null) {
            quadratics = nDirichletq;

            int co = 0;

            for (int q = 0; q < quadratics.length; q++)
                if (lq[quadratics[q]] >= 0) {
                    co++;
                }

            // we have 'co' nodes that can be translated
            int[] lineals = new int[co];
            double[][] vLineals = new double[vDirichletq.length][co];

            int pos = 0;

            for (int q = 0; q < quadratics.length; q++)
                if (lq[quadratics[q]] >= 0) {
                    lineals[pos] = lq[quadratics[q]];

                    for (int d = 0; d < vDirichletq.length; d++)
                        vLineals[d][pos] = vDirichletq[d][q];

                    pos++;
                }

            if (pos != co) {
                new Exception().printStackTrace();
            }

            System.out.println("Translated Dirichlet : " + pos);

            //for(int j=0; j<co; j++)
            //  System.out.println(" ["+j+"] "+lineals[j]+"/"+ql[lineals[j]]+" "+vLineales[j]);
            nDirichletl = lineals;
            vDirichletl = vLineals;
        }

        // Neumann
        //////////////////////////////////////////////////////////////////
        // see how many quadratic nodes of the given ones are
        // contained in the lineal mesh.
        if (nNeumannq != null) {
            quadratics = nNeumannq;

            int co = 0;

            for (int q = 0; q < quadratics.length; q++)
                if (lq[quadratics[q]] >= 0) {
                    co++;
                }

            // we have 'co' nodes that can be translated
            int[] lineals = new int[co];
            double[] vLineals = new double[co];

            int pos = 0;

            for (int q = 0; q < quadratics.length; q++)
                if (lq[quadratics[q]] >= 0) {
                    lineals[pos] = lq[quadratics[q]];
                    vLineals[pos++] = vNeumannq[q];
                }

            if (pos != co) {
                new Exception().printStackTrace();
            }

            System.out.println("Translated Neumann : " + pos);

            //for(int j=0; j<co; j++)
            //  System.out.println(" ["+j+"] "+lineals[j]+"/"+ql[lineals[j]]+" "+vLineales[j]);
            nNeumannl = lineals;
            vNeumannl = vLineals;
        }

        // Drag Lift
        if (nDragLiftq != null) {
            nDragLiftl = linealFromQuadratics(nDragLiftq);
            System.out.println("Translated DragLift : " + nDragLiftl.length);
        }

        if (nSlipq != null) {
            nSlipl = linealFromQuadratics(nSlipq);
            System.out.println("Translated Slip : " + nSlipl.length);
        }
    }

    // The mother of the beast... good luck
    // modified for 2D/3D
    private void unfoldMeshs() {
        System.out.println("NavierStokesMesh: unfoldMeshs()");

        // several initializations
        int totalElements = nodesq[0].length;

        // conversion array quadratic to lineal : lq
        lq = new int[coordq[0].length];

        for (int j = 0; j < lq.length; j++)
            lq[j] = -1;

        // conversion array linear to quadratic : ql
        // The array is much smaller that the size given
        // A PRIORI. At the end, the method cuts!
        ql = new int[lq.length];

        // The lineal coordinates are sobredimensionated!
        coordl = new double[coordq.length][coordq[0].length];

        if (nodesq.length == 6) {
            nodesl = new int[3][totalElements];
        } else if (nodesq.length == 10) {
            nodesl = new int[4][totalElements];
        } else {
            new Exception().printStackTrace();
            System.exit(0);
        }

        // position in ql[il] (il starts at 0)
        int il = 0;

        for (int e = 0; e < totalElements; e++) {
            for (int i = 0; i < nodesl.length; i++) {
                int iq = nodesq[i][e];

                if (lq[iq] == -1) {
                    lq[iq] = il;
                    ql[il] = iq;

                    // transpose of coordinates l <- q
                    for (int c = 0; c < coordl.length; c++)
                        coordl[c][il] = coordq[c][iq];

                    il++;
                }

                nodesl[i][e] = lq[iq];
            }
        }

        // for the cuttings: we have sobredimensioned arrays,
        // with not profited elements at the end. It would give incorrect results
        // when accessing them
        // Brute cut of the QL array.
        int[] tmp = new int[il];

        for (int j = 0; j < il; j++)
            tmp[j] = ql[j];

        ql = tmp;

        // Brute cut of the coordinates
        for (int i = 0; i < coordl.length; i++) {
            double[] tdbl = new double[il];

            for (int j = 0; j < il; j++)
                tdbl[j] = coordl[i][j];

            coordl[i] = tdbl;
        }

        System.out.println("Meshs unfolded...\n" + "   * quadratic : " +
                coordq[0].length + " nodes\n" + "   * lineal     : " +
                coordl[0].length + " nodes");
    }

    /**
     * Returns the element that contains the given point. Uses for that the
     * lineal mesh.
     *
     * @param x0 coordinate x
     * @param y0 coordinate y
     * @return the index of the element containing it or -1 if nothing found.
     */
    public int containingElement(double x0, double y0) {
        int[] n1 = nodesl[0];
        int[] n2 = nodesl[1];
        int[] n3 = nodesl[2];
        double[] x = coordl[0];
        double[] y = coordl[1];

        for (int e = 0; e < n1.length; e++) {
            // Shortcuts
            int no1 = n1[e];
            int no2 = n2[e];
            int no3 = n3[e];

            // Directly from Aray. It Works.
            if (!((((x[no2] - x0) * (y[no2] - y[no1])) < ((y[no2] - y0) * (x[no2] -
                    x[no1]))) ||
                    (((x[no3] - x0) * (y[no3] - y[no2])) < ((y[no3] - y0) * (x[no3] -
                            x[no2]))) ||
                    (((x[no1] - x0) * (y[no1] - y[no3])) < ((y[no1] - y0) * (x[no1] -
                            x[no3]))))) {
                return e;
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] elementReferencesNode() {
        if (elemRefNode != null) {
            return elemRefNode;
        }

        elemRefNode = new int[coordl[0].length];

        for (int e = 0; e < nodesl[0].length; e++)
            for (int c = 0; c < nodesl.length; c++)
                elemRefNode[nodesl[c][e]] = e;

        return elemRefNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] elementReferencesNodeQuad() {
        if (elemRefNodeCuad != null) {
            return elemRefNodeCuad;
        }

        elemRefNodeCuad = new int[coordq[0].length];

        for (int e = 0; e < nodesq[0].length; e++)
            for (int c = 0; c < nodesq.length; c++)
                elemRefNodeCuad[nodesq[c][e]] = e;

        return elemRefNodeCuad;
    }

    /**
     * DOCUMENT ME!
     */
    private void generateMagnus() {
        // Magnus Effect
        int[] nMagnus = kernel.getConfiguration().nodesMagnus;
        double[] vMagnus = kernel.getConfiguration().valuesMagnus;

        if (nMagnus != null) {
            kernel.out("<B>Magnus:</B> calculating Dirichlet data of " +
                    nMagnus.length + " nodes magnus, V=" + vMagnus[0]);

            for (int j = 0; j < nMagnus.length; j++) {
                int n = nMagnus[j];
                double v = vMagnus[j];
                double x = coordq[0][n];
                double y = coordq[1][n];
                double d = Math.sqrt((x * x) + (y * y));
                x = (v * x) / d;
                y = (v * y) / d;

                for (int i = 0; i < nDirichletq.length; i++)
                    if (nDirichletq[i] == n) {
                        //System.out.println(" "+x+" "+y);
                        vDirichletq[0][i] = y;
                        vDirichletq[1][i] = -x;
                    }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getCacheNodeLinealElement() {
        return cnlce;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getStartCacheNodeLinealElement() {
        return iCnlce;
    }

    /* This works as a disperse matrix,
     * iCNLE[j] <-> iCNLE[j+1]
     * are the indexes in CNLE of los elements that contain the node j
     * � VERY OPTIMIZED !
     */
    private void generateCacheNodeLinealElement() {
        int[] indice;
        int[] containedElements;

        int totalNodes = coordl[0].length;
        int totalElements = nodesl[0].length;

        // cnlce = new int[totalNodos][CACHE_ELEMENTS];
        indice = new int[totalNodes];
        containedElements = new int[totalNodes];

        System.out.println("\tcache of search NodoLin-Element");

        // In the first pass we calculate the size
        for (int e = 0; e < totalElements; e++) {
            for (int node = 0; node < nodesl.length; node++) {
                int n = nodesl[node][e];
                containedElements[n]++;
            }
        }

        // Calculate the marks
        iCnlce = new int[totalNodes + 1];
        iCnlce[0] = 0;

        for (int i = 1; i < (totalNodes + 1); i++)
            iCnlce[i] = iCnlce[i - 1] + containedElements[i - 1];

        // Generate the contents
        cnlce = new int[iCnlce[totalNodes]];

        int n;
        int j = 0;

        for (int e = 0; e < totalElements; e++) {
            for (int node = 0; node < nodesl.length; node++) {
                n = nodesl[node][e];

                cnlce[iCnlce[n] + indice[n]++] = e;
            }
        }

        kernel.out("<B>CacheNodoLinealElement:</B> generated <B>CNLE[</B>" +
                cnlce.length + "<B>]</B> e <B>iCNLE[</B>" + iCnlce.length +
                "<B>]</B>");
    }

    /**
     * Returns the lineal coordinates i-esimas (0=x, 1=y, 2=z,...)
     *
     * @param iesima DOCUMENT ME!
     * @return matrix of coordinates
     */
    public double[] getCoordinates(int iesima) {
        // return cn.getCoordinates(iesima);
        return coordl[iesima];
    }

    /**
     * Returns the lineal coordinates.
     *
     * @return matrix of coordinates
     */
    public double[][] getCoordinates() {
        return coordl;
    }

    /**
     * Returns the quadratic coordinates i-esimas (0=x, 1=y, 2=z,...)
     *
     * @param iesima DOCUMENT ME!
     * @return matrix of quadratic coordinates
     */
    public double[] getCoordinatesQuad(int iesima) {
        return coordq[iesima];
    }

    /**
     * Returns the coordinates quadratics.
     *
     * @return matrix of coordinates
     */
    public double[][] getCoordinatesQuad() {
        return coordq;
    }

    /**
     * Returns the vector Ipos lineal (NVPN)
     *
     * @return DOCUMENT ME!
     */
    public int[] getIpos() {
        return nvpnl;
    }

    /**
     * Returns the vector Ipos quadratic (NVPN)
     *
     * @return DOCUMENT ME!
     */
    public int[] getIposQuad() {
        return nvpnq;
    }

    /**
     * Returns the vector Jpos lineal (NNVI)
     *
     * @return DOCUMENT ME!
     */
    public int[] getJpos() {
        return nnvil;
    }

    /**
     * Returns the vector Jpos quadratic (NNVI)
     *
     * @return DOCUMENT ME!
     */
    public int[] getJposQuad() {
        return nnviq;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDivergenceX() {
        return divergenceX;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDivergenceY() {
        return divergenceY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDivergenceZ() {
        return divergenceZ;
    }

    /**
     * returns the mass matrix of the associated lineal mesh. Needed to
     * calculte the divergence.
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMassMatrix() {
        return masses;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMassMatrixQuad() {
        return massesCuad;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getRigidityMatrix() {
        return rigidity;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getRigidityMatrixQuad() {
        return rigidityCuad;
    }

    /**
     * Returns the linear nodes i-esimos (0=1st, 1=2nd, 3=3rd,...)
     *
     * @param iesima DOCUMENT ME!
     * @return matrix of nodes
     */
    public int[] getNodes(int iesima) {
        // return ce.getNodes(iesima);
        return nodesl[iesima];
    }

    /**
     * Returns the linear nodes i-esimos (0=1st, 1=2nd, 3=3rd,...)
     *
     * @param iesima DOCUMENT ME!
     * @return matrix of nodes
     */
    public int[] getNodesQuad(int iesima) {
        return nodesq[iesima];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[][] getNodesQuad() {
        return nodesq;
    }

    /**
     * Returns the indexes of the Dirichlet quadratic nodes !
     *
     * @return matrix of indexes of nodes Dirichlet
     */
    public int[] getNodesDirichletQuad() {
        return nDirichletq;
    }

    /**
     * Returns the indexes of the quadratic nodes Neumann!
     *
     * @return matrix of indexes of nodes Neumann
     */
    public int[] getNodesNeumannQuad() {
        return nNeumannq;
    }

    /**
     * Returns the values of the contour conditions of the nodes Dirichlet.
     *
     * @return matrix of values Dirichlet
     */
    public double[][] getValuesDirichletQuad() {
        return vDirichletq;
    }

    /**
     * Returns the values of the contour conditions of the nodes Neumann.
     *
     * @return matrix of values Neumann
     */
    public double[] getValuesNeumannQuad() {
        return vNeumannq;
    }

    /**
     * Returns the vector of todas the coordinates lineals.
     *
     * @return coordinates lineals
     */
    public double[][] getVectorCoordinates() {
        return coordl;
    }

    /**
     * Returns the vector of todas the coordinates quadratics.
     *
     * @return coordinates quadratics
     */
    public double[][] getVectorCoordinatesQuad() {
        return coordq;
    }

    /**
     * Returns the vector of all the linear nodes.
     *
     * @return lineal nodes
     */
    public int[][] getNodeVector() {
        return nodesl;
    }

    /**
     * Returns the vector of all the quadratic nodes.
     *
     * @return quadratic nodes
     */
    public int[][] getVectorNodesQuad() {
        return nodesq;
    }

    /**
     * Returns the translation a lineal of the given quadratic nodes. if there
     * is no equivalence for some one, it is omited. For that reason, the size
     * of the returned vector is not equal to the  quadratics one. It works
     * with several arrays in parallel.
     *
     * @param quadratics
     * @return
     */
    public int[] linealFromQuadratics(int[] quadratics) {
        // check how many quadratic nodes of the given ones
        // are contained in the lineal mesh.
        int co = 0;

        for (int q = 0; q < quadratics.length; q++)
            if (lq[quadratics[q]] >= 0) {
                co++;
            }

        // we have 'co' nodes that can be translated
        int[] lineals = new int[co];

        int pos = 0;

        for (int q = 0; q < quadratics.length; q++)
            if (lq[quadratics[q]] >= 0.0) {
                lineals[pos++] = lq[quadratics[q]];
            }

        if (pos != co) {
            new Exception().printStackTrace();
        }

        System.out.println("Translated L<-Q : " + pos);

        return lineals;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getNodesDragLift() {
        return nDragLiftl;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getNodesDragLiftQuad() {
        return nDragLiftq;
    }

    /**
     * Returns the indexes of the nodes Dirichlet
     *
     * @return matrix of indexes of nodes Dirichlet
     */
    public int[] getNodesDirichlet() {
        //System.out.println("MeshNavierStokes : petition nodes Dirichlet lineals");
        return nDirichletl;
    }

    /**
     * Returns the indexes of the nodes Neumann
     *
     * @return matrix of indexes of nodes Neumann
     */
    public int[] getNodesNeumann() {
        //System.out.println("MeshNavierStokes : petition nodes Neumann lineals");
        return nNeumannl;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getNodesSlip() {
        return nSlipq;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getNodesPerfilDirichletQuad() {
        return nPerfilDirichletq;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDerivadaXQuad() {
        //if(derivativeXCuad == null)
        //generaMatrixDerivadaXQuad();
        return derivativeXCuad;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDerivadaYQuad() {
        //if(derivativeYCuad == null)
        //generaMatrixDerivadaYQuad();
        return derivativeYCuad;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDivergenceXQuad() {
        return divergenceXCuad;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDivergenceYQuad() {
        return divergenceYCuad;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrixDivergenceZQuad() {
        return divergenceZCuad;
    }

    /**
     * Returns the average sizes of the elements.
     *
     * @return vector of the average sizes of the elements.
     */
    public double[] getElementAverageSizes() {
        if (hElement == null) {
            System.out.println(
                    "NavierStokesMesh: calculating the average sizes of the elements...");

            int[][] n = getNodeVector();
            hElement = new double[n[0].length];

            double[] x = getCoordinates(0);
            double[] y = getCoordinates(1);

            for (int e = 0; e < n[0].length; e++) {
                int n1 = n[0][e];
                int n2 = n[1][e];
                int n3 = n[2][e];

                double x1 = x[n1];
                double y1 = y[n1];
                double x2 = x[n2];
                double y2 = y[n2];
                double x3 = x[n3];
                double y3 = y[n3];

                double h12 = Math.sqrt(((x1 - x2) * (x1 - x2)) +
                        ((y1 - y2) * (y1 - y2)));
                double h23 = Math.sqrt(((x2 - x3) * (x2 - x3)) +
                        ((y2 - y3) * (y2 - y3)));
                double h31 = Math.sqrt(((x3 - x1) * (x3 - x1)) +
                        ((y3 - y1) * (y3 - y1)));

                hElement[e] = (h12 + h23 + h31) / 3.0;

                if (hElement[e] == 0) {
                    System.out.println("\t* Element of size null : " + e);
                    System.out.println("\t" + n1 + "\t" + n2 + "\t" + n3);
                    System.out.println("\t" + x1 + " " + y1 + " " + x2 + " " +
                            y2 + " " + x3 + " " + y3);
                }
            }

            // Estadistics
            double min;

            // Estadistics
            double max;

            // Estadistics
            double med;

            min = max = hElement[0];
            med = 0.0;

            for (int e = 0; e < hElement.length; e++) {
                if (hElement[e] < min) {
                    min = hElement[e];
                }

                if (hElement[e] > max) {
                    max = hElement[e];
                }

                med += hElement[e];

                if (hElement[e] == 0) {
                    System.out.println("\t* Element of size null : " + e);
                }
            }

            med /= hElement.length;

            System.out.println("TAM_ELEM : (min/max/media)     (" + min + "/" +
                    max + "/" + med + ")");
        }

        return hElement;
    }

    /**
     * Returns values of the contour conditions of the nodes Dirichlet.
     *
     * @return matrix of values Dirichlet
     */
    public double[][] getDirichletValues() {
        return vDirichletl;
    }

    /**
     * Returns the values of the contour conditions of the nodes Neumann.
     *
     * @return matrix of values neumann
     */
    public double[] getNeumannValues() {
        return vNeumannl;
    }

    /**
     * DOCUMENT ME!
     *
     * @param par
     * @return
     */
    public double evaluateIntegral(double[] par) {
        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;

        double result = 0.0;

        for (int e = 0; e < nodesl[0].length; e++) {
            x1 = coordl[0][nodesl[0][e]];
            y1 = coordl[1][nodesl[0][e]];
            x2 = coordl[0][nodesl[1][e]];
            y2 = coordl[1][nodesl[1][e]];
            x3 = coordl[0][nodesl[2][e]];
            y3 = coordl[1][nodesl[2][e]];

            // previous calculations that will be needed.
            // derivatives of lambda with respect x and y.
            // Calculations of the surface, through the modulus of the
            // vectorial product, which give us the area of the paralelepipedo
            // (delta is twice the area of our element)
            double delta = ((x2 - x1) * (y3 - y2)) - ((y2 - y1) * (x3 - x2));

            double surface = Math.abs(delta / 2.0);

            result += ((surface * (par[nodesl[0][e]] + par[nodesl[1][e]] +
                    par[nodesl[2][e]])) / 3.0);
        }

        // System.out.println("result integral = "+resultado);
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a
     * @param b
     * @return
     */
    public double evaluateIntegralLinealProduct(double[] a, double[] b) {
        double result = 0.0;

        boolean classic = false;

        if (classic) {
            if ((coordq.length == 2) && (nodesq.length == 6)) {
                double x1;
                double y1;
                double x2;
                double y2;
                double x3;
                double y3;

                for (int e = 0; e < nodesl[0].length; e++) {
                    x1 = coordl[0][nodesl[0][e]];
                    y1 = coordl[1][nodesl[0][e]];
                    x2 = coordl[0][nodesl[1][e]];
                    y2 = coordl[1][nodesl[1][e]];
                    x3 = coordl[0][nodesl[2][e]];
                    y3 = coordl[1][nodesl[2][e]];

                    // previous calculations that will be needed.
                    // derivatives of lambda with respect x and y.
                    // Calculations of the surface, through the modulus of the
                    // vectorial product, which give us the area of the paralelepipedo
                    // (delta is twice the area of our element)
                    double delta = ((x2 - x1) * (y3 - y2)) -
                            ((y2 - y1) * (x3 - x2));

                    double surface = Math.abs(delta / 2.0);
                    double a1;
                    double a2;
                    double a3;
                    double b1;
                    double b2;
                    double b3;

                    a1 = a[nodesl[0][e]];
                    a2 = a[nodesl[1][e]];
                    a3 = a[nodesl[2][e]];
                    b1 = b[nodesl[0][e]];
                    b2 = b[nodesl[1][e]];
                    b3 = b[nodesl[2][e]];

                    result += ((surface / 12) * ((a1 * b1) + (a1 * b2) +
                            (a2 * b1) + (a2 * b2) + (a2 * b2) + (a2 * b3) + (a3 * b2) +
                            (a3 * b3) + (a1 * b1) + (a1 * b3) + (a3 * b1) + (a3 * b3)));
                }
            } else if (is3D()) {
                double x1;
                double y1;
                double z1;
                double x2;
                double y2;
                double z2;
                double x3;
                double y3;
                double z3;
                double x4;
                double y4;
                double z4;
                double X21;
                double X31;
                double X41;
                double Y21;
                double Y31;
                double Y41;
                double Z21;
                double Z31;
                double Z41;
                double a1;
                double a2;
                double a3;
                double a4;
                double b1;
                double b2;
                double b3;
                double b4;
                double volume;
                double jacobiano;

                for (int e = 0; e < nodesl[0].length; e++) {
                    // Coordinates
                    x1 = coordq[0][nodesq[0][e]];
                    y1 = coordq[1][nodesq[0][e]];
                    z1 = coordq[2][nodesq[0][e]];
                    x2 = coordq[0][nodesq[1][e]];
                    y2 = coordq[1][nodesq[1][e]];
                    z2 = coordq[2][nodesq[1][e]];
                    x3 = coordq[0][nodesq[2][e]];
                    y3 = coordq[1][nodesq[2][e]];
                    z3 = coordq[2][nodesq[2][e]];
                    x4 = coordq[0][nodesq[3][e]];
                    y4 = coordq[1][nodesq[3][e]];
                    z4 = coordq[2][nodesq[3][e]];

                    // differences (jacobian matrix)
                    X21 = x2 - x1;
                    X31 = x3 - x1;
                    X41 = x4 - x1;
                    Y21 = y2 - y1;
                    Y31 = y3 - y1;
                    Y41 = y4 - y1;
                    Z21 = z2 - z1;
                    Z31 = z3 - z1;
                    Z41 = z4 - z1;

                    // determinant of the jacobian matrix
                    jacobiano = ((X31 * Y41 * Z21) + (X41 * Y21 * Z31) +
                            (X21 * Y31 * Z41)) - (X41 * Y31 * Z21) -
                            (X21 * Y41 * Z31) - (X31 * Y21 * Z41);

                    // volume of the tetraedro
                    volume = Math.abs(jacobiano / 6.0);

                    a1 = a[nodesl[0][e]];
                    a2 = a[nodesl[1][e]];
                    a3 = a[nodesl[2][e]];
                    a4 = a[nodesl[3][e]];
                    b1 = b[nodesl[0][e]];
                    b2 = b[nodesl[1][e]];
                    b3 = b[nodesl[2][e]];
                    b4 = b[nodesl[3][e]];

                    result += ((volume / 20) * ((a1 * b1) + (a2 * b2) +
                            (a3 * b3) + (a4 * b4) +
                            ((a1 + a2 + a3 + a4) * (b1 + b2 + b3 + b4))));
                }
            }

            return result;
        } else {
            return masses.scalarProductOfMatrixProduct(a, b);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param xco
     * @param yco
     * @param field
     * @return
     */
    public double valueFieldQuad(double xco, double yco, double[] field) {
        int e = containingElement(xco, yco);

        if (e == -1) {
            return 0.0;
        }

        double A;
        double B;
        double C;
        double D;
        double DetCr;
        double x0;
        double y0;
        double p;
        double q;
        double l1;
        double l2;
        double l3;
        double phi1;
        double phi2;
        double phi3;
        double phi4;
        double phi5;
        double phi6;

        double[] xc = getCoordinatesQuad(0);
        double[] yc = getCoordinatesQuad(1);

        int[] n1 = getNodesQuad(0);
        int[] n2 = getNodesQuad(1);
        int[] n3 = getNodesQuad(2);
        int[] n4 = getNodesQuad(3);
        int[] n5 = getNodesQuad(4);
        int[] n6 = getNodesQuad(5);

        A = xc[n2[e]] - xc[n1[e]];
        B = xc[n3[e]] - xc[n1[e]];
        C = yc[n2[e]] - yc[n1[e]];
        D = yc[n3[e]] - yc[n1[e]];
        DetCr = (A * D) - (B * C);

        x0 = xc[n1[e]];
        y0 = yc[n1[e]];
        p = (((xco - x0) * D) - (B * (yco - y0))) / DetCr;
        q = ((A * (yco - y0)) - ((xco - x0) * C)) / DetCr;

        // Functions quadratic base
        l1 = 1.0 - p - q;
        l2 = p;
        l3 = q;
        phi1 = l1 * ((2.0 * l1) - 1.0);
        phi2 = l2 * ((2.0 * l2) - 1.0);
        phi3 = l3 * ((2.0 * l3) - 1.0);
        phi4 = 4.0 * l1 * l2;
        phi5 = 4.0 * l2 * l3;
        phi6 = 4.0 * l1 * l3;

        return (field[n1[e]] * phi1) + (field[n2[e]] * phi2) +
                (field[n3[e]] * phi3) + (field[n4[e]] * phi4) + (field[n5[e]] * phi5) +
                (field[n6[e]] * phi6);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean is3D() {
        return ((coordq.length == 3) || (nodesq.length == 10));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCilindroDragLift() {
        return isCilinderDL;
    }
}
