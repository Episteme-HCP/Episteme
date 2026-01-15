/*
 * Program : ADFC�
 * Class : org.jscience.fluids.mesh.Divergence3D
 *         Calcula las matrices of divergence 3D.
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 2/08/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.mesh;

/**
 */
import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.util.Matrix;
import org.jscience.physics.fluids.dynamics.util.Vector2DFastInt;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class Divergence3D extends Object {
/**
     * Creates new Divergence3D
     */
    public Divergence3D() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Matrix getStructureDivergence(KernelADFC kernel,
        double[][] coordl, int[][] nodesl, double[][] coordq, int[][] nodesq) {
        // Firstly we should generate the estructure NNVI/NVPN
        // the rows will be stored in Vector2DFastInt
        Vector2DFastInt v2Dri = new Vector2DFastInt(coordl[0].length, 140, 20);

        // For all elements, add in the row of the speed nodes
        // the pressure nodes that are also included in the element.
        for (int e = 0; e < nodesl[0].length; e++) {
            for (int v = 0; v < 10; v++)
                for (int p = 0; p < 4; p++)
                    v2Dri.addElementNoRep(nodesl[p][e], nodesq[v][e]);
        }

        // Building NNVI/NVPN
        int[] nvpn = new int[coordl[0].length + 1];
        int tam = 0;
        nvpn[0] = 0;

        for (int j = 0; j < coordl[0].length; j++) {
            tam += v2Dri.size(j);
            nvpn[j + 1] = tam;
        }

        int[] nnvi = new int[tam];
        int pos = 0;
        v2Dri.sortArrayRows(-1);

        int[] data = v2Dri.array();

        for (int j = 0; j < coordl[0].length; j++) {
            int offset = v2Dri.getStartOfRow(j);

            for (int k = 0; k < v2Dri.size(j); k++)
                nnvi[pos++] = data[offset + k];
        }

        // Building divergenceX, y from su estructura, divergenceY
        return new Matrix(null, nvpn, nnvi, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Matrix getTransposedStructureDivergence(KernelADFC kernel,
        double[][] coordl, int[][] nodesl, double[][] coordq, int[][] nodesq) {
        // En primer lugar debemos generar the estructura NNVI/NVPN
        // las rows las almacenaremos in Vector2DRapidoInt
        Vector2DFastInt v2Dri = new Vector2DFastInt(coordq[0].length, 30, 10);

        // Para todo element, a�adimos in the row of the speed nodes
        // los nodes of presion que tambien estan incluidos in the element.
        for (int e = 0; e < nodesq[0].length; e++) {
            for (int v = 0; v < 10; v++)
                for (int p = 0; p < 4; p++)
                    v2Dri.addElementNoRep(nodesq[v][e], nodesl[p][e]);
        }

        // Building NNVI/NVPN
        int[] nvpn = new int[coordq[0].length + 1];
        int tam = 0;
        nvpn[0] = 0;

        for (int j = 0; j < coordq[0].length; j++) {
            tam += v2Dri.size(j);
            nvpn[j + 1] = tam;
        }

        int[] nnvi = new int[tam];
        int pos = 0;
        v2Dri.sortArrayRows(-1);

        int[] data = v2Dri.array();

        for (int j = 0; j < coordq[0].length; j++) {
            int offset = v2Dri.getStartOfRow(j);

            for (int k = 0; k < v2Dri.size(j); k++)
                nnvi[pos++] = data[offset + k];
        }

        // Building divergenceX, and from its structure, divergenceY
        return new Matrix(null, nvpn, nnvi, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param divergenceX DOCUMENT ME!
     * @param divergenceY DOCUMENT ME!
     * @param divergenceZ DOCUMENT ME!
     * @param divergenceXCuad DOCUMENT ME!
     * @param divergenceYCuad DOCUMENT ME!
     * @param divergenceZCuad DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     */
    static public void generate(KernelADFC kernel, Matrix divergenceX,
        Matrix divergenceY, Matrix divergenceZ, Matrix divergenceXCuad,
        Matrix divergenceYCuad, Matrix divergenceZCuad, double[][] coordl,
        int[][] nodesl, double[][] coordq, int[][] nodesq) {
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
        double dl1dx;
        double dl1dy;
        double dl1dz;
        double dl2dx;
        double dl2dy;
        double dl2dz;
        double dl3dx;
        double dl3dy;
        double dl3dz;
        double dl4dx;
        double dl4dy;
        double dl4dz;
        double volume;
        double jacobian;
        double[][] divx;
        double[][] divy;
        double[][] divz;
        divx = new double[4][10];
        divy = new double[4][10];
        divz = new double[4][10];

        kernel.out(
            "Generating matrices <B>divergenceX<SUB>i</SUB></B>,<B>divergenceX<SUB>i</SUB><SUP>t</SUP></B> (3D)");

        for (int e = 0; e < nodesq[0].length; e++) {
            // Coordinates for speed
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

            // diferences (jacobian matrix)
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
            jacobian = ((X31 * Y41 * Z21) + (X41 * Y21 * Z31) +
                (X21 * Y31 * Z41)) - (X41 * Y31 * Z21) - (X21 * Y41 * Z31) -
                (X31 * Y21 * Z41);

            // inverse Jacobian matrix
            dl2dx = ((Y31 * Z41) - (Y41 * Z31)) / jacobian;
            dl2dy = ((X41 * Z31) - (X31 * Z41)) / jacobian;
            dl2dz = ((X31 * Y41) - (X41 * Y31)) / jacobian;

            dl3dx = ((Y41 * Z21) - (Y21 * Z41)) / jacobian;
            dl3dy = ((X21 * Z41) - (X41 * Z21)) / jacobian;
            dl3dz = ((X41 * Y21) - (X21 * Y41)) / jacobian;

            dl4dx = ((Y21 * Z31) - (Y31 * Z21)) / jacobian;
            dl4dy = ((X31 * Z21) - (X21 * Z31)) / jacobian;
            dl4dz = ((X21 * Y31) - (X31 * Y21)) / jacobian;

            dl1dx = -dl2dx - dl3dx - dl4dx;
            dl1dy = -dl2dy - dl3dy - dl4dy;
            dl1dz = -dl2dz - dl3dz - dl4dz;

            // volume of the tetraedro
            volume = Math.abs(jacobian / 6.0);

            //System.out.println(" --> "+(volume/jacobiano));
            divx[0][0] = (volume * 3 * dl1dx) / 20;
            divx[0][1] = (-volume * dl2dx) / 20;
            divx[0][2] = (-volume * dl3dx) / 20;
            divx[0][3] = (-volume * dl4dx) / 20;

            divx[0][4] = (volume * 4 * (dl1dx + (2 * dl2dx))) / 20;
            divx[0][5] = (volume * 4 * (dl2dx + dl3dx)) / 20;
            divx[0][6] = (volume * 4 * (dl1dx + (2 * dl3dx))) / 20;
            divx[0][7] = (volume * 4 * (dl1dx + (2 * dl4dx))) / 20;
            divx[0][8] = (volume * 4 * (dl2dx + dl4dx)) / 20;
            divx[0][9] = (volume * 4 * (dl3dx + dl4dx)) / 20;

            divx[1][0] = (-volume * dl1dx) / 20;
            divx[1][1] = (volume * 3 * dl2dx) / 20;
            divx[1][2] = (-volume * dl3dx) / 20;
            divx[1][3] = (-volume * dl4dx) / 20;

            divx[1][4] = (volume * 4 * (dl2dx + (2 * dl1dx))) / 20;
            divx[1][5] = (volume * 4 * (dl2dx + (2 * dl3dx))) / 20;
            divx[1][6] = (volume * 4 * (dl1dx + dl3dx)) / 20;
            divx[1][7] = (volume * 4 * (dl1dx + dl4dx)) / 20;
            divx[1][8] = (volume * 4 * (dl2dx + (2 * dl4dx))) / 20;
            divx[1][9] = (volume * 4 * (dl3dx + dl4dx)) / 20;

            divx[2][0] = (-volume * dl1dx) / 20;
            divx[2][1] = (-volume * dl2dx) / 20;
            divx[2][2] = (volume * 3 * dl3dx) / 20;
            divx[2][3] = (-volume * dl4dx) / 20;

            divx[2][4] = (volume * 4 * (dl1dx + dl2dx)) / 20;
            divx[2][5] = (volume * 4 * (dl3dx + (2 * dl2dx))) / 20;
            divx[2][6] = (volume * 4 * (dl3dx + (2 * dl1dx))) / 20;
            divx[2][7] = (volume * 4 * (dl1dx + dl4dx)) / 20;
            divx[2][8] = (volume * 4 * (dl2dx + dl4dx)) / 20;
            divx[2][9] = (volume * 4 * (dl3dx + (2 * dl4dx))) / 20;

            divx[3][0] = (-volume * dl1dx) / 20;
            divx[3][1] = (-volume * dl2dx) / 20;
            divx[3][2] = (-volume * dl3dx) / 20;
            divx[3][3] = (volume * 3 * dl4dx) / 20;

            divx[3][4] = (volume * 4 * (dl1dx + dl2dx)) / 20;
            divx[3][5] = (volume * 4 * (dl2dx + dl3dx)) / 20;
            divx[3][6] = (volume * 4 * (dl1dx + dl3dx)) / 20;
            divx[3][7] = (volume * 4 * (dl4dx + (2 * dl1dx))) / 20;
            divx[3][8] = (volume * 4 * (dl4dx + (2 * dl2dx))) / 20;
            divx[3][9] = (volume * 4 * (dl4dx + (2 * dl3dx))) / 20;

            divy[0][0] = (volume * 3 * dl1dy) / 20;
            divy[0][1] = (-volume * dl2dy) / 20;
            divy[0][2] = (-volume * dl3dy) / 20;
            divy[0][3] = (-volume * dl4dy) / 20;

            divy[0][4] = (volume * 4 * (dl1dy + (2 * dl2dy))) / 20;
            divy[0][5] = (volume * 4 * (dl2dy + dl3dy)) / 20;
            divy[0][6] = (volume * 4 * (dl1dy + (2 * dl3dy))) / 20;
            divy[0][7] = (volume * 4 * (dl1dy + (2 * dl4dy))) / 20;
            divy[0][8] = (volume * 4 * (dl2dy + dl4dy)) / 20;
            divy[0][9] = (volume * 4 * (dl3dy + dl4dy)) / 20;

            divy[1][0] = (-volume * dl1dy) / 20;
            divy[1][1] = (volume * 3 * dl2dy) / 20;
            divy[1][2] = (-volume * dl3dy) / 20;
            divy[1][3] = (-volume * dl4dy) / 20;

            divy[1][4] = (volume * 4 * (dl2dy + (2 * dl1dy))) / 20;
            divy[1][5] = (volume * 4 * (dl2dy + (2 * dl3dy))) / 20;
            divy[1][6] = (volume * 4 * (dl1dy + dl3dy)) / 20;
            divy[1][7] = (volume * 4 * (dl1dy + dl4dy)) / 20;
            divy[1][8] = (volume * 4 * (dl2dy + (2 * dl4dy))) / 20;
            divy[1][9] = (volume * 4 * (dl3dy + dl4dy)) / 20;

            divy[2][0] = (-volume * dl1dy) / 20;
            divy[2][1] = (-volume * dl2dy) / 20;
            divy[2][2] = (volume * 3 * dl3dy) / 20;
            divy[2][3] = (-volume * dl4dy) / 20;

            divy[2][4] = (volume * 4 * (dl1dy + dl2dy)) / 20;
            divy[2][5] = (volume * 4 * (dl3dy + (2 * dl2dy))) / 20;
            divy[2][6] = (volume * 4 * (dl3dy + (2 * dl1dy))) / 20;
            divy[2][7] = (volume * 4 * (dl1dy + dl4dy)) / 20;
            divy[2][8] = (volume * 4 * (dl2dy + dl4dy)) / 20;
            divy[2][9] = (volume * 4 * (dl3dy + (2 * dl4dy))) / 20;

            divy[3][0] = (-volume * dl1dy) / 20;
            divy[3][1] = (-volume * dl2dy) / 20;
            divy[3][2] = (-volume * dl3dy) / 20;
            divy[3][3] = (volume * 3 * dl4dy) / 20;

            divy[3][4] = (volume * 4 * (dl1dy + dl2dy)) / 20;
            divy[3][5] = (volume * 4 * (dl2dy + dl3dy)) / 20;
            divy[3][6] = (volume * 4 * (dl1dy + dl3dy)) / 20;
            divy[3][7] = (volume * 4 * (dl4dy + (2 * dl1dy))) / 20;
            divy[3][8] = (volume * 4 * (dl4dy + (2 * dl2dy))) / 20;
            divy[3][9] = (volume * 4 * (dl4dy + (2 * dl3dy))) / 20;

            divz[0][0] = (volume * 3 * dl1dz) / 20;
            divz[0][1] = (-volume * dl2dz) / 20;
            divz[0][2] = (-volume * dl3dz) / 20;
            divz[0][3] = (-volume * dl4dz) / 20;

            divz[0][4] = (volume * 4 * (dl1dz + (2 * dl2dz))) / 20;
            divz[0][5] = (volume * 4 * (dl2dz + dl3dz)) / 20;
            divz[0][6] = (volume * 4 * (dl1dz + (2 * dl3dz))) / 20;
            divz[0][7] = (volume * 4 * (dl1dz + (2 * dl4dz))) / 20;
            divz[0][8] = (volume * 4 * (dl2dz + dl4dz)) / 20;
            divz[0][9] = (volume * 4 * (dl3dz + dl4dz)) / 20;

            divz[1][0] = (-volume * dl1dz) / 20;
            divz[1][1] = (volume * 3 * dl2dz) / 20;
            divz[1][2] = (-volume * dl3dz) / 20;
            divz[1][3] = (-volume * dl4dz) / 20;

            divz[1][4] = (volume * 4 * (dl2dz + (2 * dl1dz))) / 20;
            divz[1][5] = (volume * 4 * (dl2dz + (2 * dl3dz))) / 20;
            divz[1][6] = (volume * 4 * (dl1dz + dl3dz)) / 20;
            divz[1][7] = (volume * 4 * (dl1dz + dl4dz)) / 20;
            divz[1][8] = (volume * 4 * (dl2dz + (2 * dl4dz))) / 20;
            divz[1][9] = (volume * 4 * (dl3dz + dl4dz)) / 20;

            divz[2][0] = (-volume * dl1dz) / 20;
            divz[2][1] = (-volume * dl2dz) / 20;
            divz[2][2] = (volume * 3 * dl3dz) / 20;
            divz[2][3] = (-volume * dl4dz) / 20;

            divz[2][4] = (volume * 4 * (dl1dz + dl2dz)) / 20;
            divz[2][5] = (volume * 4 * (dl3dz + (2 * dl2dz))) / 20;
            divz[2][6] = (volume * 4 * (dl3dz + (2 * dl1dz))) / 20;
            divz[2][7] = (volume * 4 * (dl1dz + dl4dz)) / 20;
            divz[2][8] = (volume * 4 * (dl2dz + dl4dz)) / 20;
            divz[2][9] = (volume * 4 * (dl3dz + (2 * dl4dz))) / 20;

            divz[3][0] = (-volume * dl1dz) / 20;
            divz[3][1] = (-volume * dl2dz) / 20;
            divz[3][2] = (-volume * dl3dz) / 20;
            divz[3][3] = (volume * 3 * dl4dz) / 20;

            divz[3][4] = (volume * 4 * (dl1dz + dl2dz)) / 20;
            divz[3][5] = (volume * 4 * (dl2dz + dl3dz)) / 20;
            divz[3][6] = (volume * 4 * (dl1dz + dl3dz)) / 20;
            divz[3][7] = (volume * 4 * (dl4dz + (2 * dl1dz))) / 20;
            divz[3][8] = (volume * 4 * (dl4dz + (2 * dl2dz))) / 20;
            divz[3][9] = (volume * 4 * (dl4dz + (2 * dl3dz))) / 20;

            // We use Matrix.sumParallel
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 10; j++) {
                    divergenceX.sumParallel(nodesl[i][e], nodesq[j][e],
                        divx[i][j], divergenceY, divy[i][j]);

                    divergenceZ.sumElement(nodesl[i][e], nodesq[j][e],
                        divz[i][j]);

                    divergenceXCuad.sumParallel(nodesq[j][e], nodesl[i][e],
                        divx[i][j], divergenceYCuad, divy[i][j]);

                    divergenceZCuad.sumElement(nodesq[j][e], nodesl[i][e],
                        divz[i][j]);
                }
        }
    }
}
