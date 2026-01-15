/*
 * �Q������ Delaunay ?}��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: DelaunayDiagram2D.java,v 1.3 2006/03/01 21:15:56 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import java.util.Enumeration;
import java.util.Vector;

/**
 * �Q������ Delaunay ?}��\���N���X?B
 * <br><br>
 * ���̃N���X�̃C���X�^���X��?A
 * Delaunay ?}�̈ʑ���ێ?����
 * {@link EmbeddedGraph EmbeddedGraph}
 * �̃C���X�^���X graph ��?��?B
 * <br><br>
 * graph ��̊e���_ {@link EmbeddedGraph.Vertex EmbeddedGraph.Vertex} �� userData �ɂ�?A
 * {@link DelaunayDiagram2D.DVertex DelaunayDiagram2D.DVertex} �̃C���X�^���X��֘A�t����?B
 * <br><br>
 * ���l��?A
 * graph ��̊e�� {@link EmbeddedGraph.Face EmbeddedGraph.Face} �� userData �ɂ�?A
 * {@link DelaunayDiagram2D.DFace DelaunayDiagram2D.DFace} �̃C���X�^���X��֘A�t����?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:56 $
 */
public class DelaunayDiagram2D extends java.lang.Object {
    /**
     * �����_��\���Ք�N���X?B
     */
    public class DVertex {
        /*
        * ��_�̔�?�?B
        */
        private int index;

        /**
         * ��_��?W�l?B
         */
        private Point2D coordinates;

        /**
         * ��_��\�����_?B
         */
        private EmbeddedGraph.Vertex vrtx;

        /**
         * ��?[�U�̗^�����C�ӂ̃f?[�^?B
         */
        private java.lang.Object userData;

        /**
         * ��_��?���^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param index       ��_�̔�?�
         * @param coordinates ��_��?W�l
         */
        DVertex(int index,
                Point2D coordinates) {
            this.index = index;
            this.coordinates = coordinates;
            this.vrtx = null;
            this.userData = null;
        }

        /**
         * ���̕�_�̔�?���Ԃ�?B
         *
         * @return ��_�̔�?�
         */
        public int getIndex() {
            return this.index;
        }

        /**
         * ���̕�_��?W�l��Ԃ�?B
         *
         * @return ��_��?W�l
         */
        public Point2D getCoordinates() {
            return this.coordinates;
        }

        /**
         * ���̕�_��\��?u�O���t�̒��_?v��?ݒ肷��?B
         *
         * @param vrtx �O���t�̒��_
         */
        void setVertex(EmbeddedGraph.Vertex vrtx) {
            this.vrtx = vrtx;
        }

        /**
         * ���̕�_��\���Ă���?u�O���t�̒��_?v��Ԃ�?B
         *
         * @return �O���t�̒��_
         */
        public EmbeddedGraph.Vertex getVertex() {
            return this.vrtx;
        }

        /**
         * �^����ꂽ�I�u�W�F�N�g��?A���̕�_�Ɋ֌W����f?[�^�Ƃ���?ݒ肷��?B
         *
         * @param userData �C�ӂ̃I�u�W�F�N�g
         */
        public void setUserData(java.lang.Object userData) {
            this.userData = userData;
        }

        /**
         * ���̕�_�Ɋ֌W����f?[�^�Ƃ���?ݒ肳��Ă���I�u�W�F�N�g��Ԃ�?B
         *
         * @return �C�ӂ̃I�u�W�F�N�g
         */
        public java.lang.Object getUserData() {
            return this.userData;
        }

        /**
         * ���̕�_�̎�͂̎O�p�`�� Enumeration (CCW?A?����) ��Ԃ�?B
         * <p/>
         * ���ʂƂ��ē����� Enumeration ���܂ޗv�f��
         * {@link DelaunayDiagram2D.DFace DelaunayDiagram2D.DFace}
         * �̃C���X�^���X�ł���?B
         * </p>
         *
         * @return ��͂̎O�p�`�� Enumeration
         */
        public Enumeration getDFaceCycleInCCW() {
            return new Enumeration() {
                Enumeration e = vrtx.getFaceCycleInCCW().elements();

                public boolean hasMoreElements() {
                    return e.hasMoreElements();
                }

                public java.lang.Object nextElement() {
                    return ((EmbeddedGraph.Face) e.nextElement()).getUserData();
                }
            };
        }

    }

    /**
     * �O�p�`��\���Ք�N���X?B
     */
    public class DFace {
        /**
         * Delaunay ?}�̓Ք�̎O�p�`�ł��邩�ǂ���?B
         */
        private boolean inner;

        /**
         * ��?[�U�̗^�����C�ӂ̃f?[�^?B
         */
        private java.lang.Object userData;

        /**
         * �I�u�W�F�N�g��?\�z����?B
         *
         * @param inner Delaunay ?}�̓Ք�̖ʂł��邩�ǂ���
         */
        DFace(boolean inner) {
            this.inner = inner;
            this.userData = null;
        }

        /**
         * Delaunay ?}�̓Ք�̖ʂł��邩�ǂ�����?ݒ肷��?B
         *
         * @param inner Delaunay ?}�̓Ք�̖ʂł��邩�ǂ���
         */
        public void setInner(boolean inner) {
            this.inner = inner;
        }

        /**
         * Delaunay ?}�̓Ք�̖ʂł��邩�ǂ�����Ԃ�?B
         *
         * @return ���_��?W�l
         */
        public boolean isInner() {
            return this.inner;
        }

        /**
         * �^����ꂽ�I�u�W�F�N�g��?A���̎O�p�`�Ɋ֌W����f?[�^�Ƃ���?ݒ肷��?B
         *
         * @param userData �C�ӂ̃I�u�W�F�N�g
         */
        public void setUserData(java.lang.Object userData) {
            this.userData = userData;
        }

        /**
         * ���̎O�p�`�Ɋ֌W����f?[�^�Ƃ���?ݒ肳��Ă���I�u�W�F�N�g��Ԃ�?B
         *
         * @return �C�ӂ̃I�u�W�F�N�g
         */
        public java.lang.Object getUserData() {
            return this.userData;
        }
    }

    /**
     * Delaunay ?}�̈ʑ���ێ?����O���t?B
     */
    private EmbeddedGraph graph;

    /**
     * ��_�̔z��?B
     * <p/>
     * vertices[i] �� i �Ԗڂ̕�_�ɑΉ�����?B
     * </p>
     */
    private DVertex[] vertices;

    /**
     * ?d�������_�̃��X�g?B
     */
    private Vector coincidingVertices;

    /**
     * �^����ꂽ Voronoi ?}��o�Εϊ����� Delaunay ?}�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * seed �ɂ�?A?\�z�����΂���̋�̃O���t��^����?B
     * </p>
     *
     * @param seed           ?�?����ꂽ Delaunay ?}�̈ʑ�?���ێ?���邽�߂̃O���t
     * @param voronoiDiagram Voronoi ?}
     */
    public DelaunayDiagram2D(EmbeddedGraph seed,
                             VoronoiDiagram2D voronoiDiagram) {
        super();

        EmbeddedGraph voronoiGraph = voronoiDiagram.getGraph();
        int nFaces = voronoiGraph.getNumberOfFaces();
        int nCcfs = voronoiDiagram.getNumberOfPairsOfCoincidingRegions();

        /*
        * �C���X�^���X�̃t�B?[���h�ɒl��?ݒ肷��
        */
        this.graph = voronoiGraph.dualCopy(seed);

        this.vertices = new DVertex[nFaces + nCcfs];
        this.coincidingVertices = new Vector();

        /*
        * ?V���ȃO���t�̒��_�ɕ�_��?������
        */
        for (Enumeration e = this.graph.vertexElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Vertex vrtx =
                    (EmbeddedGraph.Vertex) e.nextElement();
            VoronoiDiagram2D.VRegion rgn =
                    (VoronoiDiagram2D.VRegion) vrtx.getUserData();

            if (rgn != null) {
                int index = rgn.getIndex();
                DVertex dvrtx = this.vertices[index] =
                        new DVertex(index, rgn.getCoordinates());
                dvrtx.setVertex(vrtx);
                dvrtx.setUserData(rgn.getUserData());
                vrtx.setUserData(dvrtx);
            } else {
                vrtx.setUserData(null);
            }
        }

        /*
        * ?V���ȃO���t�̖ʂɎO�p�`��?������
        */
        for (Enumeration e = this.graph.faceElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Face face =
                    (EmbeddedGraph.Face) e.nextElement();
            VoronoiDiagram2D.VPoint pnt =
                    (VoronoiDiagram2D.VPoint) face.getUserData();

            DFace dface = new DFace(true);
            dface.setUserData(pnt.getUserData());
            face.setUserData(dface);
        }

        /*
        * ?V���ȃO���t����s�v�ȃG�b�W��?�?�����
        */
        Vector uselessEdges = new Vector();

        for (Enumeration e = this.graph.edgeElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Edge edge =
                    (EmbeddedGraph.Edge) e.nextElement();
            EmbeddedGraph.Vertex[] vrtcs = edge.getVertices();
            if ((vrtcs[0].getUserData() == null) ||
                    (vrtcs[1].getUserData() == null))
                uselessEdges.addElement(edge);
        }

        int n_edges = uselessEdges.size();
        for (int i = 0; i < n_edges; i++) {
            EmbeddedGraph.Edge edge =
                    (EmbeddedGraph.Edge) uselessEdges.elementAt(i);
            EmbeddedGraph.Face rightFace = edge.getRightFace();
            if (i != (n_edges - 1)) {
                this.graph.killEdgeFace(edge, rightFace);
            } else {
                this.graph.killEdgeVertex(edge);
                DFace dface = (DFace) rightFace.getUserData();
                dface.setInner(false);
            }
        }

        /*
        * ?d�����钸�_��?������
        */
        for (Enumeration e = voronoiDiagram.coincidingRegionElements();
             e.hasMoreElements();) {
            VoronoiDiagram2D.VRegion rgn =
                    (VoronoiDiagram2D.VRegion) e.nextElement();
            VoronoiDiagram2D.VRegion mate =
                    (VoronoiDiagram2D.VRegion) rgn.getFace().getUserData();

            int index = rgn.getIndex();
            DVertex dvrtx = this.vertices[index] =
                    new DVertex(index, rgn.getCoordinates());
            dvrtx.setVertex(this.vertices[mate.getIndex()].getVertex());
            dvrtx.setUserData(rgn.getUserData());
            this.coincidingVertices.addElement(dvrtx);
        }
    }

    // I N S T A N C E   M E T H O D S

    /**
     * Delaunay ?}�̈ʑ���ێ?����O���t��Ԃ�?B
     *
     * @return Delaunay ?}�̈ʑ���ێ?����O���t
     */
    public EmbeddedGraph getGraph() {
        return graph;
    }

    /**
     * ?d�������_�̑g��?���Ԃ�?B
     *
     * @return ?d�������_�̑g��?�
     */
    public int getNumberOfPairsOfCoincidingVertices() {
        return coincidingVertices.size();
    }

    /**
     * ?d�������_�� Enumeration ��Ԃ�?B
     *
     * @return ?d�������_ (DelaunayDiagram2D.DVertex) �� Enumeration
     */
    public Enumeration getCoincidingVertexElements() {
        return coincidingVertices.elements();
    }

    /**
     * (?d����?�����) ��_�� Enumeration ��Ԃ�?B
     *
     * @return ��_ (DelaunayDiagram2D.DVertex) �� Enumeration
     */
    public Enumeration vertexElements() {
        return new Enumeration() {
            Enumeration e = graph.vertexElements();

            public boolean hasMoreElements() {
                return e.hasMoreElements();
            }

            public java.lang.Object nextElement() {
                return ((EmbeddedGraph.Vertex) e.nextElement()).getUserData();
            }
        };
    }

    /**
     * �O�p�`�� Enumeration ��Ԃ�?B
     *
     * @return �O�p�` (DelaunayDiagram2D.DFace) �� Enumeration
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
                DFace dface = (DFace) (((EmbeddedGraph.Face) obj).getUserData());
                if (dface.isInner() == true) {
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
                    return ((EmbeddedGraph.Face) obj).getUserData();
                }

                obj = e.nextElement();
                DFace dface = (DFace) (((EmbeddedGraph.Face) obj).getUserData());
                if (dface.isInner() == true) {
                    return ((EmbeddedGraph.Face) obj).getUserData();
                } else {
                    return ((EmbeddedGraph.Face) e.nextElement()).getUserData();
                }
            }
        };
    }

    /**
     * ��?���?�?�����?B
     * <p/>
     * ���_��?W�l?^?d��?��Ȃǂ̊�?���?�?���?A
     * ���� Delaunay ?}�̈ʑ�?�񂾂���^������?��݃O���t�Ƃ��Ďc��?B
     * ���̃?�\�b�h��Ă�?o�������?A���̃C���X�^���X�ɃA�N�Z�X���邱�Ƃ͂ł��Ȃ�?B
     * </p>
     *
     * @return �c�����ʑ�?��
     */
    public EmbeddedGraph stripGeometries() {

        // �O���t�̒��_�����_��?���?�?�
        for (Enumeration e = this.graph.vertexElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Vertex vrtx =
                    (EmbeddedGraph.Vertex) e.nextElement();
            DVertex dvrtx = (DVertex) vrtx.getUserData();
            if (dvrtx != null)
                vrtx.setUserData(dvrtx.getUserData());
        }

        // �O���t�̖ʂ���O�p�`��?���?�?�
        for (Enumeration e = this.graph.faceElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Face face =
                    (EmbeddedGraph.Face) e.nextElement();
            DFace dface = (DFace) face.getUserData();
            if (dface != null)
                face.setUserData(dface.getUserData());
        }

        // ��_�̔z���?�?�
        this.vertices = null;

        // ?d�������_�̃��X�g��?�?�
        this.coincidingVertices = null;

        return this.graph;
    }

    // Main Programs for Debugging
    /**
     * �f�o�b�O�p�?�C���v�?�O����
     */
    public static void main(String[] args) {
        Vector points = new Vector();

        points.addElement(new CartesianPoint2D(0.0, 0.0));
        points.addElement(new CartesianPoint2D(1.0, 0.0));
        points.addElement(new CartesianPoint2D(1.0, 1.0));
        points.addElement(new CartesianPoint2D(0.0, 1.0));

        VoronoiDiagram2D voronoi =
                new VoronoiDiagram2D(new EmbeddedGraph(), points.elements());
        DelaunayDiagram2D dulaunay =
                new DelaunayDiagram2D(new EmbeddedGraph(), voronoi);

        System.out.println("# Vertices");
        for (Enumeration e = dulaunay.vertexElements(); e.hasMoreElements();) {
            DVertex dvrtx = (DVertex) e.nextElement();
            int idx = dvrtx.getIndex();
            Point2D crd = dvrtx.getCoordinates();
            System.out.println("# [" + idx + "] " + crd.x() + ", " + crd.y());

            for (Enumeration e1 = dvrtx.getDFaceCycleInCCW();
                 e1.hasMoreElements();) {
                DFace dface = (DFace) e1.nextElement();
                if (dface.isInner() == true)
                    System.out.println("#\tface");
                else
                    System.out.println("#\tnull");
            }

        }

        System.out.println("# Faces");
        int j = 0;
        for (Enumeration e = dulaunay.faceElements(); e.hasMoreElements();) {
            DFace dface = (DFace) e.nextElement();
            System.out.println("# [" + j + "] ");
            j++;
        }

        EmbeddedGraph graph = dulaunay.getGraph();
        int i = 0;

        for (Enumeration e = graph.edgeElements(); e.hasMoreElements();) {
            EmbeddedGraph.Edge edge = (EmbeddedGraph.Edge) e.nextElement();
            EmbeddedGraph.Vertex[] vrtcs = edge.getVertices();

            DelaunayDiagram2D.DVertex vpnt0 =
                    (DelaunayDiagram2D.DVertex) vrtcs[0].getUserData();
            DelaunayDiagram2D.DVertex vpnt1 =
                    (DelaunayDiagram2D.DVertex) vrtcs[1].getUserData();

            Point2D pnt0 = vpnt0.getCoordinates();
            Point2D pnt1 = vpnt1.getCoordinates();

            System.out.println("Line2D	lin" + i);
            System.out.println("\tpnt\t" + pnt0.x() + " " + pnt0.y());
            System.out.println("\tpnt\t" + pnt1.x() + " " + pnt1.y());
            System.out.println("End");
            i++;
        }
    }
}

// end of file
