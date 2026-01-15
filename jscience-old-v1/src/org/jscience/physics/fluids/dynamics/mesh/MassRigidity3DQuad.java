/*
 * Program : ADFC�
 * Class : org.jscience.fluids.mesh.MasasRigidez3DCuad
 *         Calcula las matrices of masses y rigidity quadratics for 3D.
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


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class MassRigidity3DQuad extends Object {
/**
     * Creates new MasasRigidez3DCuad
     */
    public MassRigidity3DQuad() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param masses DOCUMENT ME!
     * @param rigidity DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     */
    static public void generate(KernelADFC kernel, Matrix masses,
        Matrix rigidity, double[][] coordq, int[][] nodesq) {
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
        double r11;
        double r12;
        double r13;
        double r14;
        double r15;
        double r16;
        double r17;
        double r18;
        double r19;
        double r1a;
        double r22;
        double r23;
        double r24;
        double r25;
        double r26;
        double r27;
        double r28;
        double r29;
        double r2a;
        double r33;
        double r34;
        double r35;
        double r36;
        double r37;
        double r38;
        double r39;
        double r3a;
        double r44;
        double r45;
        double r46;
        double r47;
        double r48;
        double r49;
        double r4a;
        double r55;
        double r56;
        double r57;
        double r58;
        double r59;
        double r5a;
        double r66;
        double r67;
        double r68;
        double r69;
        double r6a;
        double r77;
        double r78;
        double r79;
        double r7a;
        double r88;
        double r89;
        double r8a;
        double r99;
        double r9a;
        double raa;

        kernel.out(
            "Generating quadratic matrices <B>masses</B> and <B>rigidity</B> (3D)");

        for (int e = 0; e < nodesq[0].length; e++) {
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

            // differences (matrix jacobiana)
            X21 = x2 - x1;
            X31 = x3 - x1;
            X41 = x4 - x1;
            Y21 = y2 - y1;
            Y31 = y3 - y1;
            Y41 = y4 - y1;
            Z21 = z2 - z1;
            Z31 = z3 - z1;
            Z41 = z4 - z1;

            // determinant of the jacobian  matrix
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

            // volume of the tetraedrus
            volume = Math.abs(jacobian / 6.0);

            // elements of the rigidity matrix
            r11 = (volume * 12 * ((dl1dx * dl1dx) + (dl1dy * dl1dy) +
                (dl1dz * dl1dz))) / 20;
            r12 = (-volume * 4 * ((dl1dx * dl2dx) + (dl1dy * dl2dy) +
                (dl1dz * dl2dz))) / 20;
            r13 = (-volume * 4 * ((dl1dx * dl3dx) + (dl1dy * dl3dy) +
                (dl1dz * dl3dz))) / 20;
            r14 = (-volume * 4 * ((dl1dx * dl4dx) + (dl1dy * dl4dy) +
                (dl1dz * dl4dz))) / 20;
            r15 = (volume * ((((12 * dl1dx * dl2dx) - (4 * dl1dx * dl1dx) +
                (12 * dl1dy * dl2dy)) - (4 * dl1dy * dl1dy) +
                (12 * dl1dz * dl2dz)) - (4 * dl1dz * dl1dz))) / 20;
            r16 = (-volume * 4 * ((dl1dx * dl3dx) + (dl1dx * dl2dx) +
                (dl1dy * dl3dy) + (dl1dy * dl2dy) + (dl1dz * dl3dz) +
                (dl1dz * dl2dz))) / 20;
            r17 = (volume * ((((12 * dl1dx * dl3dx) - (4 * dl1dx * dl1dx) +
                (12 * dl1dy * dl3dy)) - (4 * dl1dy * dl1dy) +
                (12 * dl1dz * dl3dz)) - (4 * dl1dz * dl1dz))) / 20;
            r18 = (volume * ((((12 * dl1dx * dl4dx) - (4 * dl1dx * dl1dx) +
                (12 * dl1dy * dl4dy)) - (4 * dl1dy * dl1dy) +
                (12 * dl1dz * dl4dz)) - (4 * dl1dz * dl1dz))) / 20;
            r19 = (-volume * 4 * ((dl1dx * dl4dx) + (dl1dx * dl2dx) +
                (dl1dy * dl4dy) + (dl1dy * dl2dy) + (dl1dz * dl4dz) +
                (dl1dz * dl2dz))) / 20;
            r1a = (-volume * 4 * ((dl1dx * dl3dx) + (dl1dx * dl4dx) +
                (dl1dy * dl3dy) + (dl1dy * dl4dy) + (dl1dz * dl3dz) +
                (dl1dz * dl4dz))) / 20;

            r22 = (volume * 12 * ((dl2dx * dl2dx) + (dl2dy * dl2dy) +
                (dl2dz * dl2dz))) / 20;
            r23 = (-volume * 4 * ((dl2dx * dl3dx) + (dl2dy * dl3dy) +
                (dl2dz * dl3dz))) / 20;
            r24 = (-volume * 4 * ((dl2dx * dl4dx) + (dl2dy * dl4dy) +
                (dl2dz * dl4dz))) / 20;
            r25 = (volume * ((((12 * dl2dx * dl1dx) - (4 * dl2dx * dl2dx) +
                (12 * dl2dy * dl1dy)) - (4 * dl2dy * dl2dy) +
                (12 * dl2dz * dl1dz)) - (4 * dl2dz * dl2dz))) / 20;
            r26 = (volume * ((((12 * dl2dx * dl3dx) - (4 * dl2dx * dl2dx) +
                (12 * dl2dy * dl3dy)) - (4 * dl2dy * dl2dy) +
                (12 * dl2dz * dl3dz)) - (4 * dl2dz * dl2dz))) / 20;
            r27 = (-volume * 4 * ((dl2dx * dl3dx) + (dl2dx * dl1dx) +
                (dl2dy * dl3dy) + (dl2dy * dl1dy) + (dl2dz * dl3dz) +
                (dl2dz * dl1dz))) / 20;
            r28 = (-volume * 4 * ((dl2dx * dl1dx) + (dl2dx * dl4dx) +
                (dl2dy * dl1dy) + (dl2dy * dl4dy) + (dl2dz * dl1dz) +
                (dl2dz * dl4dz))) / 20;
            r29 = (volume * ((((12 * dl2dx * dl4dx) - (4 * dl2dx * dl2dx) +
                (12 * dl2dy * dl4dy)) - (4 * dl2dy * dl2dy) +
                (12 * dl2dz * dl4dz)) - (4 * dl2dz * dl2dz))) / 20;
            r2a = (-volume * 4 * ((dl2dx * dl3dx) + (dl2dx * dl4dx) +
                (dl2dy * dl3dy) + (dl2dy * dl4dy) + (dl2dz * dl3dz) +
                (dl2dz * dl4dz))) / 20;

            r33 = (volume * 12 * ((dl3dx * dl3dx) + (dl3dy * dl3dy) +
                (dl3dz * dl3dz))) / 20;
            r34 = (-volume * 4 * ((dl3dx * dl4dx) + (dl3dy * dl4dy) +
                (dl3dz * dl4dz))) / 20;
            r35 = (-volume * 4 * ((dl3dx * dl1dx) + (dl3dx * dl2dx) +
                (dl3dy * dl1dy) + (dl3dy * dl2dy) + (dl3dz * dl1dz) +
                (dl3dz * dl2dz))) / 20;
            r36 = (volume * ((((12 * dl3dx * dl2dx) - (4 * dl3dx * dl3dx) +
                (12 * dl3dy * dl2dy)) - (4 * dl3dy * dl3dy) +
                (12 * dl3dz * dl2dz)) - (4 * dl3dz * dl3dz))) / 20;
            r37 = (volume * ((((12 * dl3dx * dl1dx) - (4 * dl3dx * dl3dx) +
                (12 * dl3dy * dl1dy)) - (4 * dl3dy * dl3dy) +
                (12 * dl3dz * dl1dz)) - (4 * dl3dz * dl3dz))) / 20;
            r38 = (-volume * 4 * ((dl3dx * dl1dx) + (dl3dx * dl4dx) +
                (dl3dy * dl1dy) + (dl3dy * dl4dy) + (dl3dz * dl1dz) +
                (dl3dz * dl4dz))) / 20;
            r39 = (-volume * 4 * ((dl3dx * dl4dx) + (dl3dx * dl2dx) +
                (dl3dy * dl4dy) + (dl3dy * dl2dy) + (dl3dz * dl4dz) +
                (dl3dz * dl2dz))) / 20;
            r3a = (volume * ((((12 * dl3dx * dl4dx) - (4 * dl3dx * dl3dx) +
                (12 * dl3dy * dl4dy)) - (4 * dl3dy * dl3dy) +
                (12 * dl3dz * dl4dz)) - (4 * dl3dz * dl3dz))) / 20;

            r44 = (volume * 12 * ((dl4dx * dl4dx) + (dl4dy * dl4dy) +
                (dl4dz * dl4dz))) / 20;
            r45 = (-volume * 4 * ((dl4dx * dl1dx) + (dl4dx * dl2dx) +
                (dl4dy * dl1dy) + (dl4dy * dl2dy) + (dl4dz * dl1dz) +
                (dl4dz * dl2dz))) / 20;
            r46 = (-volume * 4 * ((dl4dx * dl3dx) + (dl4dx * dl2dx) +
                (dl4dy * dl3dy) + (dl4dy * dl2dy) + (dl4dz * dl3dz) +
                (dl4dz * dl2dz))) / 20;
            r47 = (-volume * 4 * ((dl4dx * dl1dx) + (dl4dx * dl3dx) +
                (dl4dy * dl1dy) + (dl4dy * dl3dy) + (dl4dz * dl1dz) +
                (dl4dz * dl3dz))) / 20;
            r48 = (volume * ((((12 * dl4dx * dl1dx) - (4 * dl4dx * dl4dx) +
                (12 * dl4dy * dl1dy)) - (4 * dl4dy * dl4dy) +
                (12 * dl4dz * dl1dz)) - (4 * dl4dz * dl4dz))) / 20;
            r49 = (volume * ((((12 * dl4dx * dl2dx) - (4 * dl4dx * dl4dx) +
                (12 * dl4dy * dl2dy)) - (4 * dl4dy * dl4dy) +
                (12 * dl4dz * dl2dz)) - (4 * dl4dz * dl4dz))) / 20;
            r4a = (volume * ((((12 * dl4dx * dl3dx) - (4 * dl4dx * dl4dx) +
                (12 * dl4dy * dl3dy)) - (4 * dl4dy * dl4dy) +
                (12 * dl4dz * dl3dz)) - (4 * dl4dz * dl4dz))) / 20;

            r55 = (volume * 32 * ((dl1dx * dl1dx) + (dl2dx * dl2dx) +
                (dl1dx * dl2dx) + (dl1dy * dl1dy) + (dl2dy * dl2dy) +
                (dl1dy * dl2dy) + (dl1dz * dl1dz) + (dl2dz * dl2dz) +
                (dl1dz * dl2dz))) / 20;
            r56 = (volume * 16 * ((dl1dx * dl3dx) +
                ((dl1dx + dl2dx) * (dl3dx + dl2dx)) + (dl1dy * dl3dy) +
                ((dl1dy + dl2dy) * (dl3dy + dl2dy)) + (dl1dz * dl3dz) +
                ((dl1dz + dl2dz) * (dl3dz + dl2dz)))) / 20;
            r57 = (volume * 16 * ((dl2dx * dl3dx) +
                ((dl2dx + dl1dx) * (dl3dx + dl1dx)) + (dl2dy * dl3dy) +
                ((dl2dy + dl1dy) * (dl3dy + dl1dy)) + (dl2dz * dl3dz) +
                ((dl2dz + dl1dz) * (dl3dz + dl1dz)))) / 20;
            r58 = (volume * 16 * ((dl2dx * dl4dx) +
                ((dl2dx + dl1dx) * (dl4dx + dl1dx)) + (dl2dy * dl4dy) +
                ((dl2dy + dl1dy) * (dl4dy + dl1dy)) + (dl2dz * dl4dz) +
                ((dl2dz + dl1dz) * (dl4dz + dl1dz)))) / 20;
            r59 = (volume * 16 * ((dl1dx * dl4dx) +
                ((dl1dx + dl2dx) * (dl4dx + dl2dx)) + (dl1dy * dl4dy) +
                ((dl1dy + dl2dy) * (dl4dy + dl2dy)) + (dl1dz * dl4dz) +
                ((dl1dz + dl2dz) * (dl4dz + dl2dz)))) / 20;
            r5a = (volume * 16 * (((dl1dx + dl2dx) * (dl3dx + dl4dx)) +
                ((dl1dy + dl2dy) * (dl3dy + dl4dy)) +
                ((dl1dz + dl2dz) * (dl3dz + dl4dz)))) / 20;

            r66 = (volume * 32 * ((dl2dx * dl2dx) + (dl3dx * dl3dx) +
                (dl2dx * dl3dx) + (dl2dy * dl2dy) + (dl3dy * dl3dy) +
                (dl2dy * dl3dy) + (dl2dz * dl2dz) + (dl3dz * dl3dz) +
                (dl2dz * dl3dz))) / 20;
            r67 = (volume * 16 * ((dl2dx * dl1dx) +
                ((dl2dx + dl3dx) * (dl1dx + dl3dx)) + (dl2dy * dl1dy) +
                ((dl2dy + dl3dy) * (dl1dy + dl3dy)) + (dl2dz * dl1dz) +
                ((dl2dz + dl3dz) * (dl1dz + dl3dz)))) / 20;
            r68 = (volume * 16 * (((dl2dx + dl3dx) * (dl1dx + dl4dx)) +
                ((dl2dy + dl3dy) * (dl1dy + dl4dy)) +
                ((dl2dz + dl3dz) * (dl1dz + dl4dz)))) / 20;
            r69 = (volume * 16 * ((dl4dx * dl3dx) +
                ((dl4dx + dl2dx) * (dl3dx + dl2dx)) + (dl4dy * dl3dy) +
                ((dl4dy + dl2dy) * (dl3dy + dl2dy)) + (dl4dz * dl3dz) +
                ((dl4dz + dl2dz) * (dl3dz + dl2dz)))) / 20;
            r6a = (volume * 16 * ((dl2dx * dl4dx) +
                ((dl2dx + dl3dx) * (dl4dx + dl3dx)) + (dl2dy * dl4dy) +
                ((dl2dy + dl3dy) * (dl4dy + dl3dy)) + (dl2dz * dl4dz) +
                ((dl2dz + dl3dz) * (dl4dz + dl3dz)))) / 20;

            r77 = (volume * 32 * ((dl1dx * dl1dx) + (dl3dx * dl3dx) +
                (dl1dx * dl3dx) + (dl1dy * dl1dy) + (dl3dy * dl3dy) +
                (dl1dy * dl3dy) + (dl1dz * dl1dz) + (dl3dz * dl3dz) +
                (dl1dz * dl3dz))) / 20;
            r78 = (volume * 16 * ((dl4dx * dl3dx) +
                ((dl4dx + dl1dx) * (dl3dx + dl1dx)) + (dl4dy * dl3dy) +
                ((dl4dy + dl1dy) * (dl3dy + dl1dy)) + (dl4dz * dl3dz) +
                ((dl4dz + dl1dz) * (dl3dz + dl1dz)))) / 20;
            ;
            r79 = (volume * 16 * (((dl1dx + dl3dx) * (dl2dx + dl4dx)) +
                ((dl1dy + dl3dy) * (dl2dy + dl4dy)) +
                ((dl1dz + dl3dz) * (dl2dz + dl4dz)))) / 20;
            r7a = (volume * 16 * ((dl1dx * dl4dx) +
                ((dl1dx + dl3dx) * (dl4dx + dl3dx)) + (dl1dy * dl4dy) +
                ((dl1dy + dl3dy) * (dl4dy + dl3dy)) + (dl1dz * dl4dz) +
                ((dl1dz + dl3dz) * (dl4dz + dl3dz)))) / 20;

            r88 = (volume * 32 * ((dl1dx * dl1dx) + (dl4dx * dl4dx) +
                (dl1dx * dl4dx) + (dl1dy * dl1dy) + (dl4dy * dl4dy) +
                (dl1dy * dl4dy) + (dl1dz * dl1dz) + (dl4dz * dl4dz) +
                (dl1dz * dl4dz))) / 20;
            r89 = (volume * 16 * ((dl2dx * dl1dx) +
                ((dl2dx + dl4dx) * (dl1dx + dl4dx)) + (dl2dy * dl1dy) +
                ((dl2dy + dl4dy) * (dl1dy + dl4dy)) + (dl2dz * dl1dz) +
                ((dl2dz + dl4dz) * (dl1dz + dl4dz)))) / 20;
            r8a = (volume * 16 * ((dl1dx * dl3dx) +
                ((dl1dx + dl4dx) * (dl3dx + dl4dx)) + (dl1dy * dl3dy) +
                ((dl1dy + dl4dy) * (dl3dy + dl4dy)) + (dl1dz * dl3dz) +
                ((dl1dz + dl4dz) * (dl3dz + dl4dz)))) / 20;
            ;

            r99 = (volume * 32 * ((dl2dx * dl2dx) + (dl4dx * dl4dx) +
                (dl2dx * dl4dx) + (dl2dy * dl2dy) + (dl4dy * dl4dy) +
                (dl2dy * dl4dy) + (dl2dz * dl2dz) + (dl4dz * dl4dz) +
                (dl2dz * dl4dz))) / 20;
            r9a = (volume * 16 * ((dl2dx * dl3dx) +
                ((dl2dx + dl4dx) * (dl3dx + dl4dx)) + (dl2dy * dl3dy) +
                ((dl2dy + dl4dy) * (dl3dy + dl4dy)) + (dl2dz * dl3dz) +
                ((dl2dz + dl4dz) * (dl3dz + dl4dz)))) / 20;

            raa = (volume * 32 * ((dl3dx * dl3dx) + (dl4dx * dl4dx) +
                (dl3dx * dl4dx) + (dl3dy * dl3dy) + (dl4dy * dl4dy) +
                (dl3dy * dl4dy) + (dl3dz * dl3dz) + (dl4dz * dl4dz) +
                (dl3dz * dl4dz))) / 20;

            // NODE 1 -----
            rigidity.sumParallel(nodesq[0][e], nodesq[0][e], r11, masses,
                volume / 70);
            rigidity.sumParallel(nodesq[0][e], nodesq[1][e], r12, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[0][e], nodesq[2][e], r13, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[0][e], nodesq[3][e], r14, masses,
                volume / 420);

            rigidity.sumParallel(nodesq[0][e], nodesq[4][e], r15, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[0][e], nodesq[5][e], r16, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[0][e], nodesq[6][e], r17, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[0][e], nodesq[7][e], r18, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[0][e], nodesq[8][e], r19, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[0][e], nodesq[9][e], r1a, masses,
                -volume / 70);

            // NODE 2 -----
            rigidity.sumParallel(nodesq[1][e], nodesq[0][e], r12, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[1][e], nodesq[1][e], r22, masses,
                volume / 70);
            rigidity.sumParallel(nodesq[1][e], nodesq[2][e], r23, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[1][e], nodesq[3][e], r24, masses,
                volume / 420);

            rigidity.sumParallel(nodesq[1][e], nodesq[4][e], r25, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[1][e], nodesq[5][e], r26, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[1][e], nodesq[6][e], r27, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[1][e], nodesq[7][e], r28, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[1][e], nodesq[8][e], r29, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[1][e], nodesq[9][e], r2a, masses,
                -volume / 70);

            // NODE 3 -----
            rigidity.sumParallel(nodesq[2][e], nodesq[0][e], r13, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[2][e], nodesq[1][e], r23, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[2][e], nodesq[2][e], r33, masses,
                volume / 70);
            rigidity.sumParallel(nodesq[2][e], nodesq[3][e], r34, masses,
                volume / 420);

            rigidity.sumParallel(nodesq[2][e], nodesq[4][e], r35, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[2][e], nodesq[5][e], r36, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[2][e], nodesq[6][e], r37, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[2][e], nodesq[7][e], r38, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[2][e], nodesq[8][e], r39, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[2][e], nodesq[9][e], r3a, masses,
                -volume / 105);

            // NODE 4 -----
            rigidity.sumParallel(nodesq[3][e], nodesq[0][e], r14, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[3][e], nodesq[1][e], r24, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[3][e], nodesq[2][e], r34, masses,
                volume / 420);
            rigidity.sumParallel(nodesq[3][e], nodesq[3][e], r44, masses,
                volume / 70);

            rigidity.sumParallel(nodesq[3][e], nodesq[4][e], r45, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[3][e], nodesq[5][e], r46, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[3][e], nodesq[6][e], r47, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[3][e], nodesq[7][e], r48, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[3][e], nodesq[8][e], r49, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[3][e], nodesq[9][e], r4a, masses,
                -volume / 105);

            // NODE 5 -----
            rigidity.sumParallel(nodesq[4][e], nodesq[0][e], r15, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[4][e], nodesq[1][e], r25, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[4][e], nodesq[2][e], r35, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[4][e], nodesq[3][e], r45, masses,
                -volume / 70);

            rigidity.sumParallel(nodesq[4][e], nodesq[4][e], r55, masses,
                (8 * volume) / 105);
            rigidity.sumParallel(nodesq[4][e], nodesq[5][e], r56, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[4][e], nodesq[6][e], r57, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[4][e], nodesq[7][e], r58, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[4][e], nodesq[8][e], r59, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[4][e], nodesq[9][e], r5a, masses,
                (2 * volume) / 105);

            // NODE 6 -----
            rigidity.sumParallel(nodesq[5][e], nodesq[0][e], r16, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[5][e], nodesq[1][e], r26, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[5][e], nodesq[2][e], r36, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[5][e], nodesq[3][e], r46, masses,
                -volume / 70);

            rigidity.sumParallel(nodesq[5][e], nodesq[4][e], r56, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[5][e], nodesq[5][e], r66, masses,
                (8 * volume) / 105);
            rigidity.sumParallel(nodesq[5][e], nodesq[6][e], r67, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[5][e], nodesq[7][e], r68, masses,
                (2 * volume) / 105);
            rigidity.sumParallel(nodesq[5][e], nodesq[8][e], r69, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[5][e], nodesq[9][e], r6a, masses,
                (4 * volume) / 105);

            // NODE 7 -----
            rigidity.sumParallel(nodesq[6][e], nodesq[0][e], r17, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[6][e], nodesq[1][e], r27, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[6][e], nodesq[2][e], r37, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[6][e], nodesq[3][e], r47, masses,
                -volume / 70);

            rigidity.sumParallel(nodesq[6][e], nodesq[4][e], r57, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[6][e], nodesq[5][e], r67, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[6][e], nodesq[6][e], r77, masses,
                (8 * volume) / 105);
            rigidity.sumParallel(nodesq[6][e], nodesq[7][e], r78, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[6][e], nodesq[8][e], r79, masses,
                (2 * volume) / 105);
            rigidity.sumParallel(nodesq[6][e], nodesq[9][e], r7a, masses,
                (4 * volume) / 105);

            // NODE 8 -----
            rigidity.sumParallel(nodesq[7][e], nodesq[0][e], r18, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[7][e], nodesq[1][e], r28, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[7][e], nodesq[2][e], r38, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[7][e], nodesq[3][e], r48, masses,
                -volume / 105);

            rigidity.sumParallel(nodesq[7][e], nodesq[4][e], r58, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[7][e], nodesq[5][e], r68, masses,
                (2 * volume) / 105);
            rigidity.sumParallel(nodesq[7][e], nodesq[6][e], r78, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[7][e], nodesq[7][e], r88, masses,
                (8 * volume) / 105);
            rigidity.sumParallel(nodesq[7][e], nodesq[8][e], r89, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[7][e], nodesq[9][e], r8a, masses,
                (4 * volume) / 105);

            // NODE 9 -----
            rigidity.sumParallel(nodesq[8][e], nodesq[0][e], r19, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[8][e], nodesq[1][e], r29, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[8][e], nodesq[2][e], r39, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[8][e], nodesq[3][e], r49, masses,
                -volume / 105);

            rigidity.sumParallel(nodesq[8][e], nodesq[4][e], r59, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[8][e], nodesq[5][e], r69, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[8][e], nodesq[6][e], r79, masses,
                (2 * volume) / 105);
            rigidity.sumParallel(nodesq[8][e], nodesq[7][e], r89, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[8][e], nodesq[8][e], r99, masses,
                (8 * volume) / 105);
            rigidity.sumParallel(nodesq[8][e], nodesq[9][e], r9a, masses,
                (4 * volume) / 105);

            // NODE 10 -----
            rigidity.sumParallel(nodesq[9][e], nodesq[0][e], r1a, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[9][e], nodesq[1][e], r2a, masses,
                -volume / 70);
            rigidity.sumParallel(nodesq[9][e], nodesq[2][e], r3a, masses,
                -volume / 105);
            rigidity.sumParallel(nodesq[9][e], nodesq[3][e], r4a, masses,
                -volume / 105);

            rigidity.sumParallel(nodesq[9][e], nodesq[4][e], r5a, masses,
                (2 * volume) / 105);
            rigidity.sumParallel(nodesq[9][e], nodesq[5][e], r6a, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[9][e], nodesq[6][e], r7a, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[9][e], nodesq[7][e], r8a, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[9][e], nodesq[8][e], r9a, masses,
                (4 * volume) / 105);
            rigidity.sumParallel(nodesq[9][e], nodesq[9][e], raa, masses,
                (8 * volume) / 105);
        }
    }
}
