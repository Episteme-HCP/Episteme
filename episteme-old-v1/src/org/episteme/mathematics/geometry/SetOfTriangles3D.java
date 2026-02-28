/*
 * �R���� : ��q����̎O�p�`��?W?���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SetOfTriangles3D.java,v 1.3 2006/03/01 21:16:10 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * �R���� : ��q����̎O�p�`��?W?���\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:10 $
 */

public class SetOfTriangles3D extends NonParametricSurface3D {
    /**
     * �O�p�`�Q�̈ʑ���\���O���t?B
     *
     * @serial
     */
    private EmbeddedGraph graph;

    /**
     * ���E�̊O���ƂȂ��?B
     *
     * @serial
     */
    private Face outerFace;

    /**
     * �O�p�`�̒��_��\���Ք�N���X?B
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * <ul>
     * <li>	���_��?W�l coordinates
     * <li>	�S����̂Ƃ���Ă��邩�ۂ����t���O killed
     * </ul>
     * ��ێ?����?B
     * </p>
     */
    public class Vertex extends EmbeddedGraph.Vertex {
        /**
         * ?W�l?B
         */
        private Point3D coordinates;

        /**
         * �S����̂Ƃ���Ă��邩�ۂ����t���O?B
         */
        private boolean killed;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * coordinates �ɂ� null �����?B
         * killed �� false �Ƃ���?B
         * </p>
         */
        protected Vertex() {
            // call superclass's constructor with parent
            SetOfTriangles3D.this.graph.super();
            this.coordinates = null;
            this.killed = false;
        }

        /**
         * ���̒��_�̕�?��Ƃ���?ݒ肳��Ă��钸�_�̃t�B?[���h��?������l�����?B
         * <p/>
         * super.fillFieldsOfReplica() ��Ă�?o�������?A
         * ���̒��_�̊e�t�B?[���h�̒l��?A��?��̑Ή�����t�B?[���h�ɑ���?B
         * </p>
         */
        protected void fillFieldsOfReplica() {
            super.fillFieldsOfReplica();
            Vertex replica = (Vertex) this.getReplica();
            replica.coordinates = this.coordinates;
            replica.killed = this.killed;
        }

        /**
         * ���̒��_��?W�l��?ݒ肷��?B
         *
         * @param coordinates ?W�l
         */
        public void setCoordinates(Point3D coordinates) {
            this.coordinates = coordinates;
        }

        /**
         * ���̒��_��?ݒ肳��Ă���?W�l��Ԃ�?B
         *
         * @return ?W�l
         */
        public Point3D getCoordinates() {
            return this.coordinates;
        }

        /**
         * ���̒��_��?u�S����̂Ƃ��邩�ۂ�?v��?ݒ肷��?B
         *
         * @param killed �S����̂Ƃ��邩�ۂ�
         */
        void setKilled(boolean killed) {
            this.killed = killed;
        }

        /**
         * ���̒��_��?u�S����̂Ƃ���Ă��邩�ۂ�?v��Ԃ�?B
         *
         * @param killed �S����̂Ƃ���Ă��邩�ۂ�
         */
        boolean isKilled() {
            return this.killed;
        }

        /**
         * ���̒��_���芪���O�p�`�̔z���Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��ɂ�?A?�����?��ŎO�p�`���i�[�����?B
         * </p>
         * <p/>
         * �O�p�`�ɖʂ��Ȃ���?����v�f�ɂ� null �����?B
         * </p>
         *
         * @return ���_���芪���O�p�`�̔z��
         */
        public Face[] getFacesInCCW() {
            Vector faces = getFaceCycleInCCW();
            Face[] result = new Face[faces.size()];
            for (int i = 0; i < faces.size(); i++) {
                if ((result[i] = (Face) faces.elementAt(i)) == outerFace)
                    result[i] = null;
            }
            return result;
        }

        /**
         * ���̒��_���芪���ӂ̔z���Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��ɂ�?A?�����?��ŕӂ��i�[�����?B
         * </p>
         *
         * @return ���_���芪���ӂ̔z��
         */
        public Edge[] getEdgesInCCW() {
            Vector edges = getEdgeCycleInCCW();
            Edge[] result = new Edge[edges.size()];
            for (int i = 0; i < edges.size(); i++)
                result[i] = (Edge) edges.elementAt(i);
            return result;
        }
    }

    /**
     * ����O�p�`��\���Ք�N���X?B
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * �S����̂Ƃ���Ă��邩�ۂ����t���O killed
     * ��ێ?����?B
     * </p>
     */
    public class Face extends EmbeddedGraph.Face {
        /**
         * �S����̂Ƃ���Ă��邩�ۂ����t���O?B
         */
        private boolean killed;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * killed �� false �Ƃ���?B
         * </p>
         */
        protected Face() {
            // call superclass's constructor with parent
            SetOfTriangles3D.this.graph.super();
            this.killed = false;
        }

        /**
         * ���̎O�p�`�̕�?��Ƃ���?ݒ肳��Ă���O�p�`�̃t�B?[���h��?������l�����?B
         * <p/>
         * super.fillFieldsOfReplica() ��Ă�?o�������?A
         * ���̖ʂ� killed �̒l��?A��?��� killed �ɑ���?B
         * </p>
         */
        protected void fillFieldsOfReplica() {
            super.fillFieldsOfReplica();
            Face replica = (Face) this.getReplica();
            replica.killed = this.killed;
        }

        /**
         * ���̎O�p�`��?u�S����̂Ƃ��邩�ۂ�?v��?ݒ肷��?B
         *
         * @param killed �S����̂Ƃ��邩�ۂ�
         */
        void setKilled(boolean killed) {
            this.killed = killed;
        }

        /**
         * ���̎O�p�`��?u�S����̂Ƃ���Ă��邩�ۂ�?v��Ԃ�?B
         *
         * @param killed �S����̂Ƃ���Ă��邩�ۂ�
         */
        boolean isKilled() {
            return this.killed;
        }

        /**
         * ���̎O�p�`���芪���ӂ̔z���Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 3 ��?A
         * ?�����?��ŕӂ��i�[�����?B
         * </p>
         *
         * @return �O�p�`���芪���ӂ̔z��
         */
        public Edge[] getEdgesInCCW() {
            Edge[] result = new Edge[3];
            Vector edges = getEdgeCycleInCCW();
            for (int i = 0; i < 3; i++)
                result[i] = (Edge) edges.elementAt(i);
            return result;
        }

        /**
         * ���̎O�p�`���芪�����_�̔z���Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 3 ��?A
         * ?�����?��Œ��_���i�[�����?B
         * </p>
         *
         * @return �O�p�`���芪�����_�̔z��
         */
        public Vertex[] getVerticesInCCW() {
            Vertex[] result = new Vertex[3];
            Vector vertices = getVertexCycleInCCW();
            for (int i = 0; i < 3; i++)
                result[i] = (Vertex) vertices.elementAt(i);
            return result;
        }

        /**
         * �^����ꂽ�ӂ����̎O�p�`��?\?�����O�ӂ̓�̈�ӂł���Ƃ���?A
         * ���̕ӂ̑Ζʂɂ����钸�_��Ԃ�?B
         * <p/>
         * �^����ꂽ�ӂ����̎O�p�`���芪����̂ł͂Ȃ�?�?��ɂ� null ��Ԃ�?B
         * </p>
         *
         * @param edge �O�p�`���芪����
         * @return edge �̑Ζʂɂ��钸�_
         */
        public Vertex getFarVertex(Edge edge) {
            Vertex[] vrtcs = this.getVerticesInCCW();
            Vertex[] edgeVrtcs = edge.getVerticesOfStartEnd();

            for (int i = 0; i < 3; i++)
                if ((vrtcs[i].isIdentWith(edgeVrtcs[0]) != true) &&
                        (vrtcs[i].isIdentWith(edgeVrtcs[1]) != true))
                    return vrtcs[i];

            return null;
        }

        /**
         * �^����ꂽ�ӂ����̎O�p�`��?\?�����O�ӂ̓�̈�ӂł���Ƃ���?A
         * ���̕ӂ̎n�_�����?I�_�ł� (�Q������Ԃł�) �p�x��Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 2 �ł���?B
         * </p>
         * <p/>
         * �^����ꂽ�ӂ����̎O�p�`���芪����̂ł͂Ȃ�?�?��ɂ� null ��Ԃ�?B
         * </p>
         * <p/>
         * ���_�� coordinates �� PointOnSurface3D �̃C���X�^���X�łȂ���΂Ȃ�Ȃ�?B
         * �����łȂ�?�?��ɂ� ClassCastException �̗�O��?�����?B
         * </p>
         *
         * @param edge �O�p�`���芪����
         * @return �ӂ̎n�_?^?I�_�̂Q������Ԃł̊p�x
         */
        public double[] getAnglesOfStartEndIn2D(Edge edge) {
            Vertex[] vrtcs = this.getVerticesInCCW();
            Vertex[] edgeVrtcs = edge.getVerticesOfStartEnd();
            Vertex far = null;

            for (int i = 0; i < 3; i++) {
                if ((vrtcs[i].isIdentWith(edgeVrtcs[0]) != true) &&
                        (vrtcs[i].isIdentWith(edgeVrtcs[1]) != true)) {
                    far = vrtcs[i];
                    break;
                }
            }

            if (far == null)
                return null;

            PointOnSurface3D crd3D;
            Point2D[] crds2D = new Point2D[3];

            crd3D = (PointOnSurface3D) edgeVrtcs[0].getCoordinates();
            crds2D[0] = Point2D.of(crd3D.parameters());

            crd3D = (PointOnSurface3D) edgeVrtcs[1].getCoordinates();
            crds2D[1] = Point2D.of(crd3D.parameters());

            crd3D = (PointOnSurface3D) far.getCoordinates();
            crds2D[3] = Point2D.of(crd3D.parameters());

            Vector2D[] vctrs2D = new Vector2D[2];
            double[] angles2D = new double[2];

            vctrs2D[0] = crds2D[1].subtract(crds2D[0]);
            vctrs2D[1] = crds2D[2].subtract(crds2D[0]);
            angles2D[0] = vctrs2D[0].angleWith(vctrs2D[1]);
            if (angles2D[0] > Math.PI)
                angles2D[0] = (Math.PI * 2) - angles2D[0];

            vctrs2D[0] = crds2D[0].subtract(crds2D[1]);
            vctrs2D[1] = crds2D[2].subtract(crds2D[1]);
            angles2D[1] = vctrs2D[0].angleWith(vctrs2D[1]);
            if (angles2D[1] > Math.PI)
                angles2D[1] = (Math.PI * 2) - angles2D[1];

            return angles2D;
        }
    }

    /**
     * �O�p�`�̕ӂ�\���Ք�N���X?B
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * �S����̂Ƃ���Ă��邩�ۂ����t���O killed
     * ��ێ?����?B
     * </p>
     */
    public class Edge extends EmbeddedGraph.Edge {
        /**
         * �S����̂Ƃ���Ă��邩�ۂ����t���O?B
         */
        private boolean killed;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * killed �� false �Ƃ���?B
         * </p>
         */
        protected Edge() {
            // call superclass's constructor with parent
            SetOfTriangles3D.this.graph.super();
            this.killed = false;
        }

        /**
         * ���̕ӂ̕�?��Ƃ���?ݒ肳��Ă���ӂ̃t�B?[���h��?������l�����?B
         * <p/>
         * super.fillFieldsOfReplica() ��Ă�?o�������?A
         * ���̖ʂ� killed �̒l��?A��?��� killed �ɑ���?B
         * </p>
         */
        protected void fillFieldsOfReplica() {
            super.fillFieldsOfReplica();
            Edge replica = (Edge) this.getReplica();
            replica.killed = this.killed;
        }

        /**
         * ���̕ӂ�?u�S����̂Ƃ��邩�ۂ�?v��?ݒ肷��?B
         *
         * @param killed �S����̂Ƃ��邩�ۂ�
         */
        void setKilled(boolean killed) {
            this.killed = killed;
        }

        /**
         * ���̕ӂ�?u�S����̂Ƃ���Ă��邩�ۂ�?v��Ԃ�?B
         *
         * @param killed �S����̂Ƃ���Ă��邩�ۂ�
         */
        boolean isKilled() {
            return this.killed;
        }

        /**
         * ���̕ӂ̗��[�̒��_��Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 2 �ł���?B
         * </p>
         *
         * @return �ӂ̗��[�̒��_�̔z��
         */
        public Vertex[] getVerticesOfStartEnd() {
            EmbeddedGraph.Vertex[] vertices = getVertices();
            Vertex[] result = new Vertex[2];
            for (int i = 0; i < 2; i++)
                result[i] = (Vertex) vertices[i];
            return result;
        }

        /**
         * ���̕ӂ̗����̎O�p�`��Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 2 �ł���?B
         * </p>
         * <p/>
         * ���̕ӂ̂ǂ��瑤�����O�p�`�ɖʂ��Ȃ�?�?��ɂ�
         * �z���̗v�f�̒l�Ƃ��� null �����?B
         * </p>
         *
         * @return �ӂ̗����̎O�p�`�̔z��
         */
        public Face[] getFacesOfLeftRight() {
            EmbeddedGraph.Face[] faces = getFaces();
            Face[] result = new Face[2];
            for (int i = 0; i < 2; i++) {
                if ((result[i] = (Face) faces[i]) == outerFace)
                    result[i] = null;
            }
            return result;
        }

        /**
         * ���̕ӂ�Ίp?�Ƃ���ʎl�p�`�̑Ίp�ό`��?s��?B
         * <p/>
         * ���̕ӂ�Ίp?�Ƃ���ʎl�p�`�̕����̎O�p�`������
         * ���̕ӂƂ͈قȂ��̑Ίp?��p���������ɕ�?X����?B
         * </p>
         * <p/>
         * ���ʂƂ��ē�����ӂ�?A?V�����Ίp?��\��?B
         * ���̕ӂɗ����ɎO�p�`���Ȃ�?�?���?A
         * ���̕ӂ�Ίp?�Ƃ���l�p�`���ʂłȂ�?�?��ɂ�
         * �Ίp�ό`���ł��Ȃ��̂�?A null ��Ԃ�?B
         * </p>
         * <p/>
         * �Ίp�ό`��?�?��?s�Ȃ��?A���̕ӂƂ͈قȂ�?V�����ӂ��ԂB�?�?��ɂ�?A
         * ���̃?�\�b�h�̌Ă�?o����?~?A���̕ӂɃA�N�Z�X���邱�Ƃ͂ł��Ȃ�?B
         * </p>
         *
         * @return ?V����?�?�������
         */
        public Edge flipDiagonal() {
            Face[] faces = this.getFacesOfLeftRight();
            Face leftFace = faces[0];
            Face rightFace = faces[1];

            if ((leftFace == null) || (rightFace == null))
                return null;

            double[] anglesInLF = leftFace.getAnglesOfStartEndIn2D(this);
            double[] anglesInRF = leftFace.getAnglesOfStartEndIn2D(this);

            if ((!((anglesInLF[0] + anglesInRF[0]) < Math.PI)) ||
                    (!((anglesInLF[1] + anglesInRF[1]) < Math.PI)))
                return null;    // �ʂłȂ�

            Vertex startV = leftFace.getFarVertex(this);
            Vertex endV = rightFace.getFarVertex(this);

            SetOfTriangles3D.this.graph.killEdgeFace(this, rightFace);

            EmbeddedGraph.Result resultMEF =
                    SetOfTriangles3D.this.graph.makeEdgeFace(leftFace,
                            startV, endV);
            Edge newEdge = (Edge) resultMEF.edge;
            newEdge.setKilled(this.isKilled());
            newEdge.setUserData(this.getUserData());

            Face newFace = (Face) resultMEF.face;
            newFace.setKilled(rightFace.isKilled());
            newFace.setUserData(rightFace.getUserData());

            return newEdge;
        }
    }

    /**
     * �O���t��ł�?V���Ȓ��_?^��?^�ʂ�?�?�����?A
     * ���̃N���X�̊Y������Ք�N���X�̃C���X�^���X��?\�z���ĕԂ��I�u�W�F�N�g
     * ��?�?�����?B
     *
     * @return �O���t��ł�?V���Ȓ��_?^��?^�ʂ�?�?���S������I�u�W�F�N�g
     */
    private EmbeddedGraph.GraphItemMaker makeGraphItemMaker() {
        return new EmbeddedGraph.GraphItemMaker() {
            SetOfTriangles3D parent = SetOfTriangles3D.this;

            // ��?g�̓Ք�N���X��g��
            public EmbeddedGraph.Vertex newVertex() {
                return parent.new Vertex();
            }

            public EmbeddedGraph.Face newFace() {
                return parent.new Face();
            }

            public EmbeddedGraph.Edge newEdge() {
                return parent.new Edge();
            }
        };
    }

    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z���邱�Ƃ͂ł��Ȃ�?B
     */
    private SetOfTriangles3D() {
        super();
    }

    /**
     * �i�q?�̓_�Ԃ�^��?A
     * �����𒸓_�Ƃ���O�p�`��?W?��Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * mesh �̈ꎟ����?^�񎟌��ڂ̂����ꂩ�̗v�f?��� 2 ���?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O��Ԃ�?B
     * </p>
     *
     * @param mesh �i�q?�̓_��
     * @see InvalidArgumentValueException
     */
    public SetOfTriangles3D(Mesh3D mesh) {
        super();

        int columnSize = mesh.uNPoints();
        int rowSize = mesh.vNPoints();

        if (rowSize < 2) {
            throw new InvalidArgumentValueException("Row size of mesh is less than 2.");
        }

        if (columnSize < 2) {
            throw new InvalidArgumentValueException("Column size of mesh is less than 2.");
        }

        this.graph = new EmbeddedGraph(makeGraphItemMaker());

        EmbeddedGraph.Vertex vertices[][] = new EmbeddedGraph.Vertex[columnSize][rowSize];

        EmbeddedGraph.Result firstVF = graph.makeVertexFace();
        this.outerFace = (Face) firstVF.face;
        vertices[0][0] = firstVF.vrtx;
        ((Vertex) vertices[0][0]).setCoordinates(mesh.pointAt(0, 0));

        // debug
        // System.out.println(outerFace);

        try {
            for (int c = 1, c_ = 0; c < columnSize; c++, c_++) {
                vertices[c][0] = graph.makeEdgeVertex(outerFace, vertices[c_][0]).vrtx;
                ((Vertex) vertices[c][0]).setCoordinates(mesh.pointAt(c, 0));
            }

            for (int r = 1, r_ = 0; r < rowSize; r++, r_++) {
                for (int c = 0, c_ = (-1); c < columnSize; c++, c_++) {
                    vertices[c][r] = graph.makeEdgeVertex(outerFace, vertices[c][r_]).vrtx;
                    ((Vertex) vertices[c][r]).setCoordinates(mesh.pointAt(c, r));

                    if (c > 0) {
                        Face rectangle =
                                (Face) graph.makeEdgeFace(outerFace, vertices[c_][r], vertices[c][r]).face;
                        graph.makeEdgeFace(rectangle, vertices[c_][r_], vertices[c][r]);
                    }
                }
            }
        } catch (InvalidArgumentValueException e) {
        }
    }

    /**
     * ����Ȗ�?�̃����_���ȓ_�Q��^��?A
     * �����𒸓_�Ƃ���O�p�`��?W?��Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param pointsOnSurface ����Ȗ�?�̃����_���ȓ_�Q
     */
    public SetOfTriangles3D(Enumeration pointsOnSurface) {
        super();
        createDelaunay(pointsOnSurface, 1,
                neverBeAccessedArg,
                neverBeAccessedArg,
                neverBeAccessedArg);
    }

    /**
     * ����Ȗ�?�̃����_���ȓ_�Q��^��?A
     * �����𒸓_�Ƃ���O�p�`��?W?��Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param pointsOnSurface ����Ȗ�?�̃����_���ȓ_�Q
     * @param xScale          �O�p�`��?W?���?�?�����?ۂ̂Q����?W�l�� X ?�����?k�ڔ{��
     * @param yScale          �O�p�`��?W?���?�?�����?ۂ̂Q����?W�l�� Y ?�����?k�ڔ{��
     */
    public SetOfTriangles3D(Enumeration pointsOnSurface,
                            double xScale,
                            double yScale) {
        super();
        createDelaunay(pointsOnSurface, 2,
                xScale,
                yScale,
                neverBeAccessedArg);

    }

    /**
     * ����Ȗ�?�̃����_���ȓ_�Q��^��?A
     * �����𒸓_�Ƃ���O�p�`��?W?��Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param pointsOnSurface ����Ȗ�?�̃����_���ȓ_�Q
     * @param xScale          �O�p�`��?W?���?�?����錳�ƂȂ� Voronoi ?}��?�?�����?ۂ̂Q����?W�l�� X ?�����?k�ڔ{��
     * @param yScale          �O�p�`��?W?���?�?����錳�ƂȂ� Voronoi ?}��?�?�����?ۂ̂Q����?W�l�� Y ?�����?k�ڔ{��
     * @param radiusScale     �O�p�`��?W?���?�?����錳�ƂȂ� Voronoi ?}��͂މ~�̔��a�̑傫����K�肷��{��
     */
    public SetOfTriangles3D(Enumeration pointsOnSurface,
                            double xScale,
                            double yScale,
                            double radiusScale) {
        super();
        createDelaunay(pointsOnSurface, 3,
                xScale,
                yScale,
                radiusScale);
    }

    /**
     * �A�N�Z�X����邱�Ƃ̂Ȃ���?�?B
     */
    private static final double neverBeAccessedArg = Double.NaN;

    /**
     * ����Ȗ�?�̃����_���ȓ_�Q��^��?A
     * �����𒸓_�Ƃ���O�p�`��?W?���?�?�����?B
     *
     * @param pointsOnSurface          ����Ȗ�?�̃����_���ȓ_�Q
     * @param constractorTypeOfVoronoi �O�p�`��?W?���?�?����錳�ƂȂ� Voronoi ?}��?�?�����?ۂ̃R���X�g���N�^�̃^�C�v
     * @param xScale                   �O�p�`��?W?���?�?����錳�ƂȂ� Voronoi ?}��?�?�����?ۂ̂Q����?W�l�� X ?�����?k�ڔ{��
     * @param yScale                   �O�p�`��?W?���?�?����錳�ƂȂ� Voronoi ?}��?�?�����?ۂ̂Q����?W�l�� Y ?�����?k�ڔ{��
     * @param radiusScale              �O�p�`��?W?���?�?����錳�ƂȂ� Voronoi ?}��͂މ~�̔��a�̑傫����K�肷��{��
     */
    private void createDelaunay(final Enumeration pointsOnSurface,
                                int constractorTypeOfVoronoi,
                                double xScale,
                                double yScale,
                                double radiusScale) {
        final Vector points3D = new Vector();    // 3D AbstractPoint �̃��X�g

        Enumeration points2D = new Enumeration() {
            public boolean hasMoreElements() {
                return pointsOnSurface.hasMoreElements();
            }

            public java.lang.Object nextElement() {
                PointOnSurface3D pos =
                        (PointOnSurface3D) pointsOnSurface.nextElement();
                points3D.addElement(pos);
                return new CartesianPoint2D(pos.parameters());
            }
        };                    // 2D AbstractPoint �̗�

        /*
        * Voronoi ?}��?��
        */
        VoronoiDiagram2D voronoi;

        switch (constractorTypeOfVoronoi) {
            case 1:
                voronoi = new VoronoiDiagram2D(new EmbeddedGraph(), points2D);
                break;

            case 2:
                voronoi = new VoronoiDiagram2D(new EmbeddedGraph(), points2D,
                        xScale, yScale);
                break;

            case 3:
                voronoi = new VoronoiDiagram2D(new EmbeddedGraph(), points2D,
                        xScale, yScale, radiusScale);
                break;

            default:
                throw new InvalidArgumentValueException("constructor type of Voronoi diagram is wrong.");
        }

        /*
        // Debug
        int i = 0;
        for (Enumeration e = voronoi.getGraph().edgeElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Edge edge = (EmbeddedGraph.Edge)e.nextElement();
            EmbeddedGraph.Face[] faces = edge.getFaces();
            if (faces[0] == faces[1])
            System.out.println("error");

            EmbeddedGraph.Vertex[] vrtcs = edge.getVertices();
            VoronoiDiagram2D.VPoint vpnt0 =
            (VoronoiDiagram2D.VPoint)vrtcs[0].getUserData();
            VoronoiDiagram2D.VPoint vpnt1 =
            (VoronoiDiagram2D.VPoint)vrtcs[1].getUserData();
            Point2D crd0 = vpnt0.getCoordinates();
            Point2D crd1 = vpnt1.getCoordinates();

            if (crd0.identical(crd1) != true) {
            System.out.println("Line2D	lin" + i);
            System.out.println("\tpnt\t" + crd0.x() + " " + crd0.y());
            System.out.println("\tpnt\t" + crd1.x() + " " + crd1.y());
            System.out.println("End");
            }
            i++;
        }
        // Debug
        */

        /*
        * Delaunay ?}�ɕϊ�����
        */
        this.graph = new EmbeddedGraph(makeGraphItemMaker());

        DelaunayDiagram2D dulaunay =
                new DelaunayDiagram2D(this.graph, voronoi);

        /*
        * Vertex ��?������
        */
        for (Enumeration e = this.graph.vertexElements();
             e.hasMoreElements();) {
            Vertex vrtx = (Vertex) e.nextElement();
            DelaunayDiagram2D.DVertex dvrtx =
                    (DelaunayDiagram2D.DVertex) vrtx.getUserData();
            vrtx.setCoordinates((PointOnSurface3D) points3D.elementAt(dvrtx.getIndex()));
        }

        /*
        * outerFace �쩂���
        */
        this.outerFace = null;

        for (Enumeration e = this.graph.faceElements();
             e.hasMoreElements();) {
            Face face = (Face) e.nextElement();
            if (face.getUserData() == null) {
                this.outerFace = face;
                break;
            }
        }

        /*
        // Debug
        int i = 0;
        for (Enumeration e = this.graph.edgeElements();
             e.hasMoreElements();) {
            Edge edge = (Edge)e.nextElement();
            Vertex[] vrtcs = edge.getVerticesOfStartEnd();
            DelaunayDiagram2D.DVertex dvrtx0 =
            (DelaunayDiagram2D.DVertex)vrtcs[0].getUserData();
            DelaunayDiagram2D.DVertex dvrtx1 =
            (DelaunayDiagram2D.DVertex)vrtcs[1].getUserData();
            Point2D crd0 = dvrtx0.getCoordinates();
            Point2D crd1 = dvrtx1.getCoordinates();

            System.out.println("Line2D	lin" + i);
            System.out.println("\tpnt\t" + crd0.x() + " " + crd0.y());
            System.out.println("\tpnt\t" + crd1.x() + " " + crd1.y());
            System.out.println("End");
            i++;
        }
        // Debug
        */

        /*
        * this.graph ���� Delaunay ?}��?���?�?�����
        */
        dulaunay.stripGeometries();
    }

    /**
     * ���̎O�p�`��?W?����܂ގO�p�`�� Enumeration ��Ԃ�?B
     *
     * @return �O�p�`�� Enumeration
     */
    public Enumeration faceElements() {
        return new Enumeration() {
            Enumeration e = graph.faceElements();
            Object nextNonOuterFace = null;

            public boolean hasMoreElements() {
                if (nextNonOuterFace != null)
                    return true;

                if (e.hasMoreElements() == false)
                    return false;

                Object obj = e.nextElement();
                if ((Face) obj != outerFace) {
                    nextNonOuterFace = obj;
                    return true;
                } else {
                    nextNonOuterFace = null;
                    return e.hasMoreElements();
                }
            }

            public java.lang.Object nextElement() {
                Object obj;

                if (nextNonOuterFace != null) {
                    obj = nextNonOuterFace;
                    nextNonOuterFace = null;
                    return obj;
                }

                obj = e.nextElement();
                if ((Face) obj != outerFace)
                    return obj;
                else
                    return e.nextElement();
            }
        };
    }

    /**
     * ���̎O�p�`��?W?����܂ޒ��_�� Enumeration ��Ԃ�?B
     *
     * @return ���_�� Enumeration
     */
    public Enumeration vertexElements() {
        return graph.vertexElements();
    }

    /**
     * ���̎O�p�`��?W?����܂ޕӂ� Enumeration ��Ԃ�?B
     *
     * @return �ӂ� Enumeration
     */
    public Enumeration edgeElements() {
        return graph.edgeElements();
    }

    /**
     * ���̎O�p�`��?W?����܂ޒ��_��?���Ԃ�?B
     *
     * @return ���_��?�
     */
    public int getNumberOfVertices() {
        return graph.getNumberOfVertices();
    }

    /**
     * ���̎O�p�`��?W?����܂ގO�p�`��?���Ԃ�?B
     *
     * @return �O�p�`��?�
     */
    public int getNumberOfFaces() {
        return graph.getNumberOfFaces() - 1;
    }

    /**
     * ���̎O�p�`��?W?����܂ޕӂ�?���Ԃ�?B
     *
     * @return �ӂ�?�
     */
    public int getNumberOfEdges() {
        return graph.getNumberOfEdges();
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    public boolean isFreeform() {
        return true;
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        throw new UnsupportedOperationException();    // output
    }

    // Main Programs for Debugging
    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String[] args) {
        Plane3D surface = new Plane3D(new CartesianPoint3D(0.0, 0.0, 0.0),
                new LiteralVector3D(0.0, 0.0, 1.0));
        Vector pointsOnSurface = new Vector();

        for (int u = 0; u < 5; u++)
            for (int v = 0; v < 5; v++)
                if (v != u)
                    pointsOnSurface.addElement(new PointOnSurface3D(surface,
                            (u * 1.0),
                            (v * 1.0),
                            doCheckDebug));

        SetOfTriangles3D stri =
                new SetOfTriangles3D(pointsOnSurface.elements());

        int i = 0;
        for (Enumeration e = stri.edgeElements(); e.hasMoreElements();) {
            SetOfTriangles3D.Edge edge =
                    (SetOfTriangles3D.Edge) e.nextElement();
            SetOfTriangles3D.Vertex[] vrtcs = edge.getVerticesOfStartEnd();
            Point3D pnt0 = vrtcs[0].getCoordinates();
            Point3D pnt1 = vrtcs[1].getCoordinates();

            System.out.println("Line3D	lin" + i);
            System.out.println("\tpnt\t" + pnt0.x() + " " + pnt0.y() + " " + pnt0.z());
            System.out.println("\tpnt\t" + pnt1.x() + " " + pnt1.y() + " " + pnt1.z());
            System.out.println("End");
            i++;
        }
    }
}

/* end of file */
