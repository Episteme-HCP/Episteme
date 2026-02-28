/*
 * �Q���� : ��q����̎O�p�`��?W?���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SetOfTriangles2D.java,v 1.3 2006/03/01 21:16:10 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * �Q���� : ��q����̎O�p�`��?W?���\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:10 $
 */

public class SetOfTriangles2D extends NonParametricSurface2D {
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
     * ���_��?W�l coordinates
     * ��ێ?����?B
     * </p>
     */
    public class Vertex extends EmbeddedGraph.Vertex {
        /**
         * ?W�l?B
         */
        private Point2D coordinates;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * coordinates �ɂ� null �����?B
         * </p>
         */
        protected Vertex() {
            // call superclass's constructor with parent
            SetOfTriangles2D.this.graph.super();
            this.coordinates = null;
        }

        /**
         * ���̒��_�̕�?��Ƃ���?ݒ肳��Ă��钸�_�̃t�B?[���h��?������l�����?B
         * <p/>
         * super.fillFieldsOfReplica() ��Ă�?o�������?A
         * ���̒��_�� coordinates ��
         * ���̒��_�̕�?��� coordinates �Ƃ���?B
         * </p>
         */
        protected void fillFieldsOfReplica() {
            super.fillFieldsOfReplica();
            Vertex replica = (Vertex) this.getReplica();
            replica.coordinates = this.coordinates;
        }

        /**
         * ���̒��_��?W�l��?ݒ肷��?B
         *
         * @param coordinates ?W�l
         */
        public void setCoordinates(Point2D coordinates) {
            this.coordinates = coordinates;
        }

        /**
         * ���̒��_��?ݒ肳��Ă���?W�l��Ԃ�?B
         *
         * @return ?W�l
         */
        public Point2D getCoordinates() {
            return this.coordinates;
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
     */
    public class Face extends EmbeddedGraph.Face {
        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        protected Face() {
            // call superclass's constructor with parent
            SetOfTriangles2D.this.graph.super();
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
    }

    /**
     * �O�p�`�̕ӂ�\���Ք�N���X?B
     */
    public class Edge extends EmbeddedGraph.Edge {
        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        protected Edge() {
            // call superclass's constructor with parent
            SetOfTriangles2D.this.graph.super();
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
            SetOfTriangles2D parent = SetOfTriangles2D.this;

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
    private SetOfTriangles2D() {
        super();
    }

    /**
     * �i�q?�̓_�Ԃ�^��?A
     * �����𒸓_�Ƃ���O�p�`��?W?��Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * grid �̈ꎟ����?^�񎟌��ڂ̂����ꂩ�̗v�f?��� 2 ���?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O��Ԃ�?B
     * </p>
     *
     * @param grid �i�q?�̓_��
     * @see InvalidArgumentValueException
     */
    public SetOfTriangles2D(Point2D[][] grid) {
        super();

        int columnSize = grid.length;
        int rowSize = grid[0].length;

        if (rowSize < 2) {
            throw new InvalidArgumentValueException("Row size of grid is less than 2.");
        }

        if (columnSize < 2) {
            throw new InvalidArgumentValueException("Column size of grid is less than 2.");
        }

        graph = new EmbeddedGraph(makeGraphItemMaker());

        EmbeddedGraph.Vertex vertices[][] = new EmbeddedGraph.Vertex[columnSize][rowSize];

        EmbeddedGraph.Result firstVF = graph.makeVertexFace();
        outerFace = (Face) firstVF.face;
        vertices[0][0] = firstVF.vrtx;
        ((Vertex) vertices[0][0]).setCoordinates(grid[0][0]);

        // debug
        // System.out.println(outerFace);

        try {
            for (int c = 1, c_ = 0; c < columnSize; c++, c_++) {
                vertices[c][0] = graph.makeEdgeVertex(outerFace, vertices[c_][0]).vrtx;
                ((Vertex) vertices[c][0]).setCoordinates(grid[c][0]);
            }

            for (int r = 1, r_ = 0; r < rowSize; r++, r_++) {
                for (int c = 0, c_ = (-1); c < columnSize; c++, c_++) {
                    vertices[c][r] = graph.makeEdgeVertex(outerFace, vertices[c][r_]).vrtx;
                    ((Vertex) vertices[c][r]).setCoordinates(grid[c][r]);

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
}

/* end of file */
