/*
 * �Q������ Voronoi ?}��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: VoronoiDiagram2D.java,v 1.3 2006/03/01 21:16:12 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.util.Enumeration;
import java.util.Vector;

/**
 * �Q������ Voronoi ?}��\���N���X?B
 * <br><br>
 * ���̃N���X�̃C���X�^���X��?A
 * Voronoi ?}�̈ʑ���ێ?����
 * {@link EmbeddedGraph EmbeddedGraph}
 * �̃C���X�^���X graph ��?��?B
 * <br><br>
 * graph ��̊e�� {@link EmbeddedGraph.Face EmbeddedGraph.Face} �� userData �ɂ�?A
 * {@link VoronoiDiagram2D.VRegion VoronoiDiagram2D.VRegion} �̃C���X�^���X��֘A�t����?B
 * <br><br>
 * ���l��?A
 * graph ��̊e���_ {@link EmbeddedGraph.Vertex EmbeddedGraph.Vertex} �� userData �ɂ�?A
 * {@link VoronoiDiagram2D.VPoint VoronoiDiagram2D.VPoint} �̃C���X�^���X��֘A�t����?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:12 $
 */
public class VoronoiDiagram2D extends java.lang.Object {
    /**
     * �����_��?�߂�̈� (Voronoi �̈�) ��\���Ք�N���X?B
     */
    public class VRegion {
        /**
         * ��_�̔�?�?B
         */
        private int index;

        /**
         * ��_��?W�l?B
         * <p/>
         * �^����ꂽ?W�l�� xScale, yScale �⩂����l?B
         * </p>
         */
        private Point2D coordinates;

        /**
         * ��_��?�߂�̈��\����?B
         * <p/>
         * Face �� userData �ɂ� Voronoi �̈��֘A�t����?B
         * </p>
         */
        private EmbeddedGraph.Face face;

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
        VRegion(int index,
                Point2D coordinates) {
            this.index = index;
            this.coordinates = coordinates;
            this.face = null;
            this.userData = null;
        }

        /**
         * ���̗̈�̕�_�̔�?���Ԃ�?B
         *
         * @return ��_�̔�?�
         */
        public int getIndex() {
            return this.index;
        }

        /**
         * ���̗̈�̕�_��?W�l��Ԃ�?B
         *
         * @return ��_��?W�l
         */
        public Point2D getCoordinates() {
            return this.coordinates;
        }

        /**
         * ���̗̈�ɑΉ�����?u�O���t�̖�?v��?ݒ肷��?B
         *
         * @param face �O���t�̖�
         */
        void setFace(EmbeddedGraph.Face face) {
            this.face = face;
        }

        /**
         * ���̗̈�ɑΉ�����?u�O���t�̖�?v��Ԃ�?B
         *
         * @return �O���t�̖�
         */
        public EmbeddedGraph.Face getFace() {
            return this.face;
        }

        /**
         * �^����ꂽ�I�u�W�F�N�g��?A���̗̈�Ɋ֌W����f?[�^�Ƃ���?ݒ肷��?B
         *
         * @param userData �C�ӂ̃I�u�W�F�N�g
         */
        public void setUserData(java.lang.Object userData) {
            this.userData = userData;
        }

        /**
         * ���̗̈�Ɋ֌W����f?[�^�Ƃ���?ݒ肳��Ă���I�u�W�F�N�g��Ԃ�?B
         *
         * @return �C�ӂ̃I�u�W�F�N�g
         */
        public java.lang.Object getUserData() {
            return this.userData;
        }

        /**
         * ���̗̈�̎�͂� Voronoi �_�� Enumeration (CCW?A?����) ��Ԃ�?B
         * <p/>
         * ���ʂƂ��ē����� Enumeration ���܂ޗv�f��
         * {@link VoronoiDiagram2D.VPoint VoronoiDiagram2D.VPoint}
         * �̃C���X�^���X�ł���?B
         * </p>
         *
         * @return ��͂� Voronoi �_�� Enumeration
         */
        public Enumeration getVPointCycleInCCW() {
            return new Enumeration() {
                Enumeration e = face.getVertexCycleInCCW().elements();

                public boolean hasMoreElements() {
                    return e.hasMoreElements();
                }

                public java.lang.Object nextElement() {
                    return ((EmbeddedGraph.Vertex) e.nextElement()).getUserData();
                }
            };
        }
    }

    /**
     * Voronoi �̈��͂ޒ��_ (Voronoi �_) ��\���Ք�N���X?B
     */
    public class VPoint {
        /**
         * ���_��?W�l?B
         */
        private Point2D coordinates;

        /**
         * ���_��?ł�߂���_����̋���?B
         */
        private double distance;

        /**
         * ��?[�U�̗^�����C�ӂ̃f?[�^?B
         */
        private java.lang.Object userData;

        /**
         * ���_��?W�l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param coordinates ���_��?W�l
         */
        VPoint(Point2D coordinates) {
            this.coordinates = coordinates;
            this.distance = (-1.0);
            this.userData = null;
        }

        /**
         * ���̒��_��?W�l��Ԃ�?B
         *
         * @return ���_��?W�l
         */
        public Point2D getCoordinates() {
            return this.coordinates;
        }

        /**
         * ���̒��_��?ł�߂���_����̋�����?ݒ肷��?B
         *
         * @param distance ���_��?ł�߂���_����̋���
         */
        void setDistance(double distance) {
            this.distance = distance;
        }

        /**
         * ���̒��_��?ł�߂���_����̋�����Ԃ�?B
         *
         * @return ���_��?ł�߂���_����̋���
         */
        public double getDistance() {
            return this.distance;
        }

        /**
         * �^����ꂽ�I�u�W�F�N�g��?A���̒��_�Ɋ֌W����f?[�^�Ƃ���?ݒ肷��?B
         *
         * @param userData �C�ӂ̃I�u�W�F�N�g
         */
        public void setUserData(java.lang.Object userData) {
            this.userData = userData;
        }

        /**
         * ���̒��_�Ɋ֌W����f?[�^�Ƃ���?ݒ肳��Ă���I�u�W�F�N�g��Ԃ�?B
         *
         * @return �C�ӂ̃I�u�W�F�N�g
         */
        public java.lang.Object getUserData() {
            return this.userData;
        }
    }

    /**
     * Voronoi ?}�̈ʑ���ێ?����O���t?B
     */
    private EmbeddedGraph graph;

    /**
     * Voronoi �̈�̔z��?B
     * <p/>
     * regions[i] �� i �Ԗڂ̕�_�ɑΉ�����?B
     * </p>
     */
    private VRegion[] regions;

    /**
     * ?d�������_ (= Voronoi �̈�) �̃��X�g?B
     */
    private Vector coincidingRegions;

    /**
     * ��_��?W�l�� X ?�����?k�ڔ{��?B
     */
    private double xScale;

    /**
     * ��_��?W�l�� Y ?�����?k�ڔ{��?B
     */
    private double yScale;

    /**
     * ?o��?オ�� Voronoi ?}��͂މ~�̔��a�̑傫����K�肷��{��?B
     * <p/>
     * ���̒l���傫����?��?���肷��͂��ł���?B
     * </p>
     */
    private double radiusScale;

    /**
     * ?o��?オ�� Voronoi ?}��͂މ~�̔��a�̑傫����K�肷��{���̃f�t�H���g�l?B
     */
    public static final double radiusScaleDefault = 100.0;

    /**
     * ���_�̋�����K�肷��{��?B
     */
    private static final double farScale = 30.0;

    /**
     * �G�b�W�𕪊�����?ۂ̌�_���Z�ɓK�p���鉉�Z?�??B
     */
    private static ConditionOfOperation conditionWithSmallDTol = null;

    /**
     * �^����ꂽ�����_���ȓ_�Q�̊e�_���_�Ƃ��� Voronoi ?}�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * seed �ɂ�?A?\�z�����΂���̋�̃O���t��^����?B
     * </p>
     *
     * @param seed   ?�?����ꂽ Voronoi ?}�̈ʑ�?���ێ?���邽�߂̃O���t
     * @param points ��_��?W?� (�����_���ȓ_�Q)
     */
    public VoronoiDiagram2D(EmbeddedGraph seed,
                            Enumeration points) {
        super();
        createDiagram(seed, points, 1.0, 1.0, radiusScaleDefault);
    }

    /**
     * �^����ꂽ�����_���ȓ_�Q�̊e�_���_�Ƃ��� Voronoi ?}�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �e��_ points[i] ��?W�l��?A
     * points[i].x() * xScale, points[i].y() * yScale
     * �ƂȂ�?B
     * </p><p>
     * seed �ɂ�?A?\�z�����΂���̋�̃O���t��^����?B
     * </p>
     *
     * @param seed   ?�?����ꂽ Voronoi ?}�̈ʑ�?���ێ?���邽�߂̃O���t
     * @param points ��_��?W?� (�����_���ȓ_�Q)
     * @param xScale ��_��?W�l�� X ?�����?k�ڔ{��
     * @param yScale ��_��?W�l�� Y ?�����?k�ڔ{��
     */
    public VoronoiDiagram2D(EmbeddedGraph seed,
                            Enumeration points,
                            double xScale,
                            double yScale) {
        super();
        createDiagram(seed, points, xScale, yScale, radiusScaleDefault);
    }

    /**
     * �^����ꂽ�����_���ȓ_�Q�̊e�_���_�Ƃ��� Voronoi ?}�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �e��_ points[i] ��?W�l��?A
     * points[i].x() * xScale, points[i].y() * yScale
     * �ƂȂ�?B
     * </p><p>
     * seed �ɂ�?A?\�z�����΂���̋�̃O���t��^����?B
     * </p>
     *
     * @param seed        ?�?����ꂽ Voronoi ?}�̈ʑ�?���ێ?���邽�߂̃O���t
     * @param points      ��_��?W?� (�����_���ȓ_�Q)
     * @param xScale      ��_��?W�l�� X ?�����?k�ڔ{��
     * @param yScale      ��_��?W�l�� Y ?�����?k�ڔ{��
     * @param radiusScale ?o��?オ�� Voronoi ?}��͂މ~�̔��a�̑傫����K�肷��{��
     */
    public VoronoiDiagram2D(EmbeddedGraph seed,
                            Enumeration points,
                            double xScale,
                            double yScale,
                            double radiusScale) {
        super();
        createDiagram(seed, points, xScale, yScale, radiusScale);
    }

    /**
     * �^����ꂽ�����_���ȓ_�Q�̊e�_���_�Ƃ��� Voronoi ?}��?�?�����?B
     * <p/>
     * seed �ɂ�?A?\�z�����΂���̋�̃O���t��^����?B
     * </p>
     *
     * @param seed        ?�?����ꂽ Voronoi ?}�̈ʑ�?���ێ?���邽�߂̃O���t
     * @param givenPoints ��_��?W?� (�����_���ȓ_�Q)
     * @param xScale      ��_��?W�l�� X ?�����?k�ڔ{��
     * @param yScale      ��_��?W�l�� Y ?�����?k�ڔ{��
     * @param radiusScale ?o��?オ�� Voronoi ?}��͂މ~�̔��a�̑傫����K�肷��{��
     */
    private void createDiagram(EmbeddedGraph seed,
                               Enumeration givenPoints,
                               double xScale,
                               double yScale,
                               double radiusScale) {
        /*
        * ��?����Ó����ǂ�����`�F�b�N����
        */
        if (!(xScale > 0.0))
            throw new InvalidArgumentValueException("xScale should be positive.");

        if (!(yScale > 0.0))
            throw new InvalidArgumentValueException("yScale should be positive.");

        if (!(radiusScale > 0.0))
            throw new InvalidArgumentValueException("radiusScale should be positive.");

        /*
        * givenPoints �� Vector �ɕϊ�
        */
        Vector points = new Vector();
        for (; givenPoints.hasMoreElements();)
            points.addElement(givenPoints.nextElement());

        int nPoints = points.size();
        if (nPoints < 2)
            throw new InvalidArgumentValueException("The number of given points is too small.");

        /*
        * �C���X�^���X�̃t�B?[���h�ɒl��?ݒ肷��
        */
        this.graph = seed;

        this.regions = new VRegion[nPoints];
        this.coincidingRegions = new Vector();

        this.xScale = xScale;
        this.yScale = yScale;
        this.radiusScale = radiusScale;

        /*
        * �e��_��?W�l�𓾂�?A�X�P?[�����O��{��
        */
        Point2D[] scaledPoints = new Point2D[nPoints];

        for (int i = 0; i < nPoints; i++) {
            Point2D givenPoint = (Point2D) points.elementAt(i);
            scaledPoints[i] = new CartesianPoint2D(givenPoint.x() * this.xScale,
                    givenPoint.y() * this.yScale);
            this.regions[i] = this.new VRegion(i, scaledPoints[i]);
        }

        /*
        * ?���?}��?�邽�߂�?A?o��?オ�� Voronoi ?}��m�Ɉ͂ނ悤�ȉ~��?�߂�
        */
        EnclosingBox2D box = new EnclosingBox2D(scaledPoints);
        Point2D center = box.min().linearInterpolate(box.max(), 0.5);
        double radius = this.radiusScale *
                center.distance(center.longestPoint(scaledPoints));

        /*
        * ��?��̎O��_��?���?}��?��
        */
        EmbeddedGraph.Vertex[] extraPoints = new EmbeddedGraph.Vertex[3];
        VRegion[] extraRegions = new VRegion[3];
        VRegion lastAddedRegion;

        lastAddedRegion = makeInitialDiagram(center, radius,
                extraPoints, extraRegions);

        /*
        * ?���?}�쳂�?A�e��_�𑫂��Ă���
        */
        for (int i = 0; i < nPoints; i++)
            lastAddedRegion = addPoint(i, null, lastAddedRegion);

        /*
        * ?���?}�̎c�[��?�?�����
        */
        removeExtraFaces(extraPoints, extraRegions);
    }

    /**
     * ��?��̎O��_��?���?}��?��?B
     *
     * @param center       ?o��?オ�� Voronoi ?}��͂މ~�̒�?S
     * @param radius       ?o��?オ�� Voronoi ?}��͂މ~�̔��a
     * @param extraPoints  ?��� Voronoi �_��Ԃ����߂̔z�� (?o��)
     * @param extraRegions ?��� Voronoi �̈��Ԃ����߂̔z�� (?o��)
     */
    private VRegion makeInitialDiagram(Point2D center,
                                       double radius,
                                       EmbeddedGraph.Vertex[] extraPoints,
                                       VRegion[] extraRegions) {
        double degree30;    // 30 �x
        double cos30;        // cos(30 �x)
        double sin30;        // sin(30 �x)

        double farDist;        // ��?S���牓�_ (Voronoi �_) �܂ł̋���

        double rr;        // ?����_��?���
        double qq;        // ?����_��?���

        EmbeddedGraph.Result opResult;    // Euler ��?�̌���
        EmbeddedGraph.Vertex origVrtx;    // ��?S (Voronoi �_)
        EmbeddedGraph.Face outerFace;    // �O���̖�

        VPoint vpnt;    // Voronoi �_

        Vector2D vectorInitialPoint = null;    // ��?S����?����_�ւ̃x�N�g��
        Vector2D vectorFarPoint = null;    // ��?S���牓�_�ւ̃x�N�g��

        /*
        * �O��?���_��?W�l���߂邽�߂ɕK�v�Ȓl��p�ӂ���
        */
        degree30 = Math.PI / 6.0;
        cos30 = Math.cos(degree30);
        sin30 = Math.sin(degree30);
        farDist = radius * farScale;

        rr = (3.0 * Math.sqrt(2.0) * radius) / 4.0;
        qq = Math.sqrt(3.0) * rr;

        /*
        * �܂�?u��?S?v�� Voronoi �_���u��
        */
        opResult = this.graph.makeVertexFace();
        origVrtx = opResult.vrtx;
        outerFace = opResult.face;

        vpnt = new VPoint(center);
        vpnt.setDistance(2.0 * rr);    // ��?S?|?����_�Ԃ̋���
        origVrtx.setUserData(vpnt);

        // �O���̖ʂɑΉ����� Voronoi �̈�͂Ȃ�
        outerFace.setUserData(null);

        /*
        * �O��?����_ (Voronoi �̈�) & Voronoi �_��?��
        */
        for (int i = 0; i < 3; i++) {
            /*
            * �ϓ��ȊԊu��?W�l��?�߂�
            */
            switch (i) {
                case 0:
                    vectorInitialPoint = new LiteralVector2D(0.0, (2.0 * rr));
                    vectorFarPoint = new LiteralVector2D((farDist * cos30),
                            (farDist * sin30));
                    break;

                case 1:
                    vectorInitialPoint = new LiteralVector2D((-qq), (-rr));
                    vectorFarPoint = new LiteralVector2D((-farDist * cos30),
                            (farDist * sin30));
                    break;

                case 2:
                    vectorInitialPoint = new LiteralVector2D(qq, (-rr));
                    vectorFarPoint = new LiteralVector2D(0.0, (-farDist));
                    break;
            }

            /*
            * Voronoi �_��?��
            */
            opResult = this.graph.makeEdgeVertex(outerFace, origVrtx);
            extraPoints[i] = opResult.vrtx;

            vpnt = new VPoint(center.add(vectorFarPoint));
            vpnt.setDistance(-1.0);
            extraPoints[i].setUserData(vpnt);

            /*
            * ���� Voronoi �̈��?��
            */
            extraRegions[i] = new VRegion((-(i + 101)),
                    center.add(vectorInitialPoint));

            switch (i) {
                case 0:    // �܂������Ȃ�
                    break;

                case 1:    // �����
                    opResult = this.graph.makeEdgeFace(outerFace, extraPoints[1], extraPoints[0]);
                    extraRegions[0].setFace(opResult.face);
                    opResult.face.setUserData(extraRegions[0]);
                    break;

                case 2:    // �����
                    opResult = this.graph.makeEdgeFace(outerFace, extraPoints[2], extraPoints[1]);
                    extraRegions[1].setFace(opResult.face);
                    opResult.face.setUserData(extraRegions[1]);

                    opResult = this.graph.makeEdgeFace(outerFace, extraPoints[0], extraPoints[2]);
                    extraRegions[2].setFace(opResult.face);
                    opResult.face.setUserData(extraRegions[2]);
                    break;
            }
        }

        /*
        * (��芸����) ?Ō��?�B� Voronoi �̈��Ԃ�
        */
        return extraRegions[2];
    }

    /**
     * �̈�̈ꕔ��?���� Voronoi �̈��?A
     * ���� Voronoi �̈�𕪊����� Euler ��?��
     * ?V���ɂł�����̖ʂ̑g��\���Ք�N���X?B
     */
    private class RegionAndFace {
        /**
         * �̈�̈ꕔ��?���� Voronoi �̈�?B
         */
        VRegion rgn;

        /**
         * rgn �𕪊����� Euler ��?��?A?V���ɂł�����̖�?B
         */
        EmbeddedGraph.Face face;

        /**
         * rgn �𕪊����� Euler ��?��?~?Aface ��?�?����ꂽ���ۂ�?B
         */
        boolean faceKilled;

        /**
         * �I�u�W�F�N�g��?\�z����?B
         *
         * @param rgn  ��������� Voronoi �̈�
         * @param face rgn �𕪊����� Euler ��?��?A?V���ɂł�����̖�
         */
        RegionAndFace(VRegion rgn,
                      EmbeddedGraph.Face face) {
            this.rgn = rgn;
            this.face = face;
            this.faceKilled = false;
        }

        /**
         * �^����ꂽ�ʂ�?A���̃C���X�^���X�Ɋ֌W�����ʂł���Ȃ��?A
         * faceKilled ��?^�ɂ���?B
         *
         * @param face �����
         * @return face �����̃C���X�^���X�Ɋ֌W����Ȃ�?^
         */
        boolean killFaceIfGivenIsIt(EmbeddedGraph.Face face) {
            if (this.face != face)
                return false;

            this.faceKilled = true;
            return true;
        }

        /**
         * �ʂ𕪊�����?ۂ�?V���ɂł�����̖ʂ��c�BĂ���?A
         * ���̖ʂ�?u?V���ɂł��� Voronoi �̈�?v�łȂ��Ȃ��?A
         * �ꕔ��?��ꂽ��� Voronoi �̈�ƃO���t?�̖ʂ̑Ή��������?B
         *
         * @param newFace ?V���ɂł��� Voronoi �̈��\����
         */
        void reAssociate(EmbeddedGraph.Face newFace) {
            if ((this.faceKilled == true) || (this.face == newFace))
                return;

            /*
            * ?d�������_�̂��ꂼ�� (ccr) �ɂ���
            * ccr ���֌W����ʂ� rgn ���֌W����ʂ������Ȃ��?A
            * ccr ���֌W����ʂ� face �ɂ�������
            */
            VRegion ccr;
            for (Enumeration e = VoronoiDiagram2D.this.coincidingRegions.elements();
                 e.hasMoreElements();) {
                ccr = (VRegion) e.nextElement();
                if (ccr.getFace() == this.rgn.getFace())
                    ccr.setFace(this.face);
            }

            /*
            * rgn �� face �̑Ή��������
            */
            this.rgn.setFace(this.face);
            this.face.setUserData(this.rgn);
        }
    }

    /**
     * �G�b�W�̗����������ʂ��ۂ�?A��Ԃ�?B
     *
     * @param edge �G�b�W
     * @return �����ʂȂ��?A���̖�?A�����łȂ���� null
     */
    private EmbeddedGraph.Face edgeHasSameFace(EmbeddedGraph.Edge edge) {
        EmbeddedGraph.Face[] faces = edge.getFaces();

        if (faces[0] == faces[1])
            return faces[0];    // ?��̖�

        return null;
    }

    /**
     * �G�b�W�̂ǂ��炩�̒[���Ǘ��������_���ۂ�?A��Ԃ�?B
     *
     * @param edge �G�b�W
     * @return �Ǘ��������_�Ȃ�?A���̒��_?A�����łȂ���� null
     */
    private EmbeddedGraph.Vertex edgeHasIsolateVertex(EmbeddedGraph.Edge edge) {
        EmbeddedGraph.Vertex[] v = edge.getVertices();

        for (int i = 0; i < 2; i++) {
            if (v[i].getEdgeCycleInCCW().size() == 1)
                return v[i];
        }

        return null;
    }

    /**
     * �^����ꂽ�G�b�W��?�?�����?B
     *
     * @param edges ?�?�����G�b�W�̃��X�g
     * @param RandF �̈�̈ꕔ��?���� Voronoi �̈��?A?V���ɂł���ʂ̑g�̃��X�g
     * @return ?�?����ׂ��G�b�W��?�?���������?A?V���� Voronoi �̈�Ƃ��Ďc�B���
     */
    private EmbeddedGraph.Face killEdges(Vector edges,
                                         Vector RandF) {
        EmbeddedGraph.Face lastFace = null;

        // ?�?����ׂ��G�b�W�����邠����
        while (edges != null) {
            Vector remainedEdges = null;

            // ?�?����ׂ��G�b�W�̂��ꂼ��ɂ���
            for (Enumeration e = edges.elements(); e.hasMoreElements();) {
                EmbeddedGraph.Edge edge = (EmbeddedGraph.Edge) e.nextElement();
                if ((lastFace = edgeHasSameFace(edge)) != null) {
                    // �G�b�W�̗����������ʂȂ�?A
                    if (edgeHasIsolateVertex(edge) != null) {
                        // ���̃G�b�W�̂ǂ��炩�̒[���Ǘ��_�Ȃ�
                        // ���̃G�b�W��?�?�
                        this.graph.killEdgeVertex(edge);
                        // ���Ȃ݂Ƀ�?[�v��?Ō�͕K��������ʂ�
                    } else {
                        // ���̃G�b�W�̗��[�ɑ��̃G�b�W���Ȃ��BĂ���Ȃ�
                        // ���̃G�b�W��?�?���?�ɉ��΂�
                        if (remainedEdges == null)
                            remainedEdges = new Vector();
                        remainedEdges.addElement(edge);
                    }
                } else {
                    // �G�b�W�̗������Ⴄ�ʂȂ�?A
                    //  (�Ƃ肠����?A�E�̖ʂƋ���) ���̃G�b�W��?�?�
                    EmbeddedGraph.Face rightFace = edge.getRightFace();
                    for (Enumeration e1 = RandF.elements(); e1.hasMoreElements();) {
                        RegionAndFace RF = (RegionAndFace) e1.nextElement();
                        if (RF.killFaceIfGivenIsIt(rightFace) == true)
                            break;
                    }
                    this.graph.killEdgeFace(edge, rightFace);
                }
            }

            edges = remainedEdges;
        }

        // ?�?����ׂ��G�b�W��?�?���������?A?V���� Voronoi �̈�Ƃ���
        // �c�B��ʂ�Ԃ�
        return lastFace;
    }

    /**
     * Voronoi ?}��?V���� Voronoi �̈��͂�?���?B
     *
     * @param newRegion ?V���� Voronoi �̈�
     * @param T         ?�?������ׂ� Voronoi �_�̃��X�g
     */
    private void addRegion(VRegion newRegion,
                           Vector T) {
        Point2D newRegionCoord = newRegion.getCoordinates();

        Vector newVrtcs = new Vector();    // �G�b�W�𕪊����Ăł���?V���Ȓ��_
        Vector rmvEdges = new Vector();    // ?�?����ׂ��G�b�W
        Vector dvdFaces = new Vector();    // ���������
        Vector dvdRandF = new Vector();    // ���������̈��?V���ɂł����ʂ̑g

        EmbeddedGraph.Result opResult;    // Euler ��?�̌���

        /*
        * T ��� ?�?����ׂ� Voronoi �_�̂��ꂼ���
        */
        for (Enumeration e0 = T.elements(); e0.hasMoreElements();) {
            EmbeddedGraph.Vertex vrtx =
                    (EmbeddedGraph.Vertex) e0.nextElement();

            // ��͂̃G�b�W�̂��ꂼ��ɂ���
            for (Enumeration e1 = vrtx.getEdgeCycleInCCW().elements();
                 e1.hasMoreElements();) {
                EmbeddedGraph.Edge edge =
                        (EmbeddedGraph.Edge) e1.nextElement();

                // �G�b�W�̗��[�� T �Ɋ܂܂�Ă����?A
                // ?�?�����G�b�W�̃��X�g�ɉB���
                EmbeddedGraph.Vertex anotherVrtx = anotherEnd(edge, vrtx);
                if (T.contains(anotherVrtx) == true) {
                    if (edgeIsContained(rmvEdges, edge) != true)
                        rmvEdges.addElement(edge);
                    continue;
                }

                // �G�b�W�𕪊� : �G�b�W�̒��Ԃ�?V���� Voronoi �_��?��
                opResult = this.graph.makeVertexEdge(edge);
                EmbeddedGraph.Vertex newVrtx = opResult.vrtx;
                EmbeddedGraph.Edge newEdge = opResult.edge;
                setMiddlePoint(edge, vrtx, anotherVrtx, newVrtx, newRegionCoord);

                newVrtcs.addElement(newVrtx);

                // ���������G�b�W�̈�� (?�?����� Voronoi �_��܂ޕ�) ��
                // ?�?�����G�b�W�̃��X�g�ɉB���
                EmbeddedGraph.Edge rmvEdge =
                        (anotherEnd(edge, newVrtx) == vrtx) ? edge : newEdge;
                if (edgeIsContained(rmvEdges, rmvEdge) != true)
                    rmvEdges.addElement(rmvEdge);

                // �G�b�W�̗����̖ʂ�?u��������ʂ̃��X�g?v�ɉB���
                EmbeddedGraph.Face[] twoFaces = edge.getFaces();
                if (dvdFaces.contains(twoFaces[0]) != true)
                    dvdFaces.addElement(twoFaces[0]);
                if (dvdFaces.contains(twoFaces[1]) != true)
                    dvdFaces.addElement(twoFaces[1]);
            }
        }

        /*
        * �G�b�W�𕪊�����?�B�?V���� Voronoi �_�싂ԃG�b�W��?��
        * �܂�?A�������ׂ��ʂ𕪊�����
        */

        // �������ׂ��ʂ̂��ꂼ��ɂ���
        for (Enumeration e0 = dvdFaces.elements(); e0.hasMoreElements();) {
            EmbeddedGraph.Face face = (EmbeddedGraph.Face) e0.nextElement();

            // ���̖ʂɑΉ�����?u?V���� Voronoi �_ (���)?v��T��?o��
            EmbeddedGraph.Vertex tgtVrtx0 = null;
            EmbeddedGraph.Vertex tgtVrtx1 = null;

            for (Enumeration e1 = newVrtcs.elements(); e1.hasMoreElements();) {
                EmbeddedGraph.Vertex vrtx =
                        (EmbeddedGraph.Vertex) e1.nextElement();
                Vector faces = vrtx.getFaceCycleInCCW();
                if (faces.contains(face) == true) {
                    if (tgtVrtx0 == null)
                        tgtVrtx0 = vrtx;
                    else
                        tgtVrtx1 = vrtx;
                }
                if ((tgtVrtx0 != null) && (tgtVrtx1 != null))
                    break;
            }

            // ?V���� Voronoi �_���������Ȃ��͂��͂Ȃ��񂾂���?A?A?A
            if ((tgtVrtx0 == null) || (tgtVrtx1 == null))
                continue;

            // �T��?o������� Voronoi �_�싂ԃG�b�W��?�B�?A�ʂ𕪊�
            opResult = this.graph.makeEdgeFace(face, tgtVrtx0, tgtVrtx1);
            dvdRandF.addElement(new RegionAndFace((VRegion) face.getUserData(),
                    opResult.face));
        }

        /*
        * ?�?������ׂ��G�b�W��?�?�����
        */
        EmbeddedGraph.Face newFace = killEdges(rmvEdges, dvdRandF);

        /*
        * �������Ďc�B���̖ʂƊ�� Voronoi �̈�̑Ή���?��?����
        */
        for (Enumeration e = dvdRandF.elements(); e.hasMoreElements();)
            ((RegionAndFace) e.nextElement()).reAssociate(newFace);

        /*
        * ?V���� Voronoi �̈�Ɩʂ�Ή��Â���
        */
        newRegion.setFace(newFace);
        newFace.setUserData(newRegion);
    }

    /**
     * Voronoi ?}�ɕ�_�𑫂�?B
     *
     * @param index           ��_�̔�?�
     * @param nearRegionHint  ��_�ɋ߂��͂��� Voronoi �̈�
     * @param lastAddedRegion ?Ō�ɒǉB��ꂽ Voronoi �̈�
     * @return ��_�ɑΉ�����?V���ɒǉB��ꂽ Voronoi �̈�
     */
    private VRegion addPoint(int index,
                             VRegion nearRegionHint,
                             VRegion lastAddedRegion) {
        EmbeddedGraph.Face nearestFace;    // ��_�Ɉ�ԋ߂� Voronoi �̈�
        Vector T = new Vector();        // ?�?������ׂ� Voronoi �_�̃��X�g

        /*
        * Voronoi ?}��?V���ȕ�_��B��邱�Ƃ�?A?�?������ Voronoi �_��?�߂�
        */
        nearestFace = findT(this.regions[index].getCoordinates(),
                nearRegionHint, lastAddedRegion, T);

        if (T.size() > 0) {
            /*
            * ?�?������ׂ� Voronoi �_������Ȃ�?A�O���t���?W����
            */
            addRegion(this.regions[index], T);
            lastAddedRegion = this.regions[index];

        } else {
            /*
            * ?�?������ׂ� Voronoi �_���Ȃ��Ȃ�?A
            * ��_�����̕�_��?d�����Ă���̂�?A?d�����X�g�ɉB���
            */
            this.regions[index].setFace(nearestFace);
            this.coincidingRegions.addElement(this.regions[index]);
        }

        return lastAddedRegion;
    }

    /**
     * ?�?����ׂ����_�̃��X�g��\���Ք�N���X?B
     */
    private class TVertexInfo {
        /**
         * ?�?����ׂ����_?B
         */
        EmbeddedGraph.Vertex v;

        /**
         * v �ɗ�?ڂ��钸�_�����łɕ]��?ς݂��ۂ�?B
         */
        private boolean neighborsAreEvaluated;

        /**
         * ?�?����ׂ����̒��_?B
         */
        TVertexInfo next;

        /**
         * �I�u�W�F�N�g��?\�z����?B
         *
         * @param v    ���_
         * @param e    ��?ڂ��钸�_�����łɕ]��?ς݂��ǂ���
         * @param next ����܂ł̃��X�g
         */
        TVertexInfo(EmbeddedGraph.Vertex v,
                    TVertexInfo next) {
            this.v = v;
            this.next = next;
            this.neighborsAreEvaluated = false;
        }

        /**
         * ��?ڂ��钸�_�̕]�����܂�?ς�ł��Ȃ����_��Ԃ�?B
         */
        private TVertexInfo getTviHasUnevaledNeighbors() {
            for (TVertexInfo tvi = this; tvi != null; tvi = tvi.next) {
                if (tvi.neighborsAreEvaluated != true) {
                    tvi.neighborsAreEvaluated = true;
                    return tvi;
                }
            }

            return null;
        }

        /**
         * �^����ꂽ���_�����X�g�ɑ�?݂��邩�ۂ���Ԃ�?B
         *
         * @param tgt ���_
         */
        private boolean listHasThisVrtx(EmbeddedGraph.Vertex tgt) {
            for (TVertexInfo tvi = this; tvi != null; tvi = tvi.next) {
                if (tvi.v == tgt)
                    return true;
            }

            return false;
        }

        /**
         * �^����ꂽ���_��?�?����ׂ����_�̃��X�g�ɉB����Ƃ�?A
         * ���̌��ʂ��ʑ��I��?�������̂ł��邩�ۂ���Ԃ�?B
         *
         * @param tgt ���_
         */
        private boolean inspectTopologicalValidity(EmbeddedGraph.Vertex tgt) {
            Vector inspectedFaces = new Vector();

            /*
            * P5.5 & P5.6
            */
            for (TVertexInfo tvi = this; tvi != null; tvi = tvi.next) {
                for (Enumeration e = tvi.v.getFaceCycleInCCW().elements();
                     e.hasMoreElements();) {
                    EmbeddedGraph.Face face =
                            (EmbeddedGraph.Face) e.nextElement();
                    if (inspectedFaces.contains(face) == true)
                        continue;

                    boolean allAreHaved = true;
                    boolean havedByT = false;
                    boolean headIsHaved = false;
                    int connectionCount = 0;

                    Vector vrtcs = face.getVertexCycleInCCW();
                    int nVrtcs = vrtcs.size();

                    for (int i = 0; i < nVrtcs; i++) {
                        EmbeddedGraph.Vertex vrtx =
                                (EmbeddedGraph.Vertex) vrtcs.elementAt(i);
                        if ((this.listHasThisVrtx(vrtx) != true) && (vrtx != tgt)) {
                            /*
                            * vrtx is not included in (T & tgt)
                            */
                            allAreHaved = false;
                            havedByT = false;
                        } else {
                            /*
                            * vrtx is included in (T & tgt)
                            */
                            if (havedByT == false)
                                connectionCount++;
                            havedByT = true;
                            if (i == 0)
                                headIsHaved = true;
                            if (i == (nVrtcs - 1)) {
                                if ((headIsHaved == true) && (connectionCount > 1))
                                    connectionCount--;
                            }
                        }
                    }

                    if ((allAreHaved == true) || (connectionCount > 1))
                        return false;

                    inspectedFaces.addElement(face);
                }
            }

            return true;
        }
    }

    /**
     * Voronoi ?}��?V���ɕ�_��B��邱�Ƃ�?A
     * ?�?�����邱�ƂɂȂ� Voronoi �_��?�߂�?B
     *
     * @param tgt             ?V���ȕ�_
     * @param nearRegionHint  ��_�ɋ߂��͂��� Voronoi �̈�
     * @param lastAddedRegion ?Ō�ɒǉB��ꂽ Voronoi �̈�
     * @param TV              ?�?������ׂ� Voronoi �_�̃��X�g (?o��)
     * @return ��_�Ɉ�ԋ߂� Voronoi �̈�
     */
    private EmbeddedGraph.Face findT(Point2D tgt,
                                     VRegion nearRegionHint,
                                     VRegion lastAddedRegion,
                                     Vector TV) {
        EmbeddedGraph.Face nearestFace;
        // ��_�Ɉ�ԋ߂� Voronoi �̈�

        EmbeddedGraph.Vertex smallestV;    // H ��?�?��ƂȂ钸�_
        double smallestH;        // ?�?��� H

        TVertexInfo T;            // ?�?����ׂ����_�̃��X�g

        EmbeddedGraph.Vertex vrtx;    // ���钸�_
        TVertexInfo tvi;        // ����?�?����ׂ����_

        /*
        * Step 2.1 : ?V���ȕ�_�Ɉ�ԋ߂� Voronoi �̈�쩂���
        *
        * ?V���ȕ�_�Ƃ���Ɉ�ԋ߂� Voronoi �̈�̕�_�Ƃ̋����𒲂�?A
        * ����炪?d�����Ă���Ȃ�?A?�?����ׂ� Voronoi �_�͖������ƂɂȂ�
        */
        nearestFace = findNearestRegion(tgt, nearRegionHint, lastAddedRegion);

        Vector2D diff =
                tgt.subtract(((VRegion) nearestFace.getUserData()).getCoordinates());
        diff = new LiteralVector2D((diff.x() / this.xScale),
                (diff.y() / this.yScale));
        if (diff.length() <
                ConditionOfOperation.getCondition().getToleranceForDistance())
            return nearestFace;

        /*
        * Step 2.2 : ?V���ȕ�_�ɑ΂���?A���?����� H �����_�� T �ɉB���
        */
        smallestV = null;
        smallestH = Double.MAX_VALUE;

        for (Enumeration e = nearestFace.getVertexCycleInCCW().elements();
             e.hasMoreElements();) {
            vrtx = (EmbeddedGraph.Vertex) e.nextElement();
            try {
                double H = evaluateH(vrtx, tgt);
                if ((smallestV == null) || (H < smallestH)) {
                    smallestV = vrtx;
                    smallestH = H;
                }
            } catch (PointIsExtra exp) {
                continue;
            }
        }

        T = new TVertexInfo(smallestV, null);

        /*
        * Step 2.3 : �ȉ���?�?�𖞂������_ V �� T �ɉB���
        *
        *	0. V �̗�?ړ_�����ł� T �ɑ�?݂���
        *	1. ?V���ȕ�_�� V �Ƃ� H �̒l�����ł���
        *	2. V �� T �ɉB��Ă�?AT �̂���ׂ��ʑ���?�?��󂳂Ȃ�
        */

        /*
        * T ���?A��?ړ_�̕]����?ς�łȂ����_�̂��ꂼ���
        */
        while ((tvi = T.getTviHasUnevaledNeighbors()) != null) {
            // ��?ړ_�̂��ꂼ��ɑ΂���
            for (Enumeration e = tvi.v.getEdgeCycleInCCW().elements();
                 e.hasMoreElements();) {
                vrtx = anotherEnd((EmbeddedGraph.Edge) e.nextElement(), tvi.v);

                // ���ꂪ���ł� T �ɑ�?݂����?A�X�L�b�v
                if (T.listHasThisVrtx(vrtx) == true)
                    continue;

                // H �����łȂ����?A�X�L�b�v
                try {
                    if (!(evaluateH(vrtx, tgt) < 0.0))
                        continue;
                } catch (PointIsExtra exp) {
                    continue;
                }

                // T �̈ʑ���?���Ȃ��?A�X�L�b�v
                if (T.inspectTopologicalValidity(vrtx) != true)
                    continue;

                // �����łȂ����?AT �ɉB���
                T = new TVertexInfo(vrtx, T);
            }
        }

        for (tvi = T; tvi != null; tvi = tvi.next)
            TV.addElement(tvi.v);

        return nearestFace;
    }

    /**
     * ?V���ȕ�_��?ł�߂� Voronoi �̈�𓾂�?B
     *
     * @param tgt             ?V���ȕ�_
     * @param nearRegionHint  ?V���ȕ�_�ɋ߂��͂��� Voronoi �̈�
     * @param lastAddedRegion ?Ō�ɒǉB��ꂽ Voronoi �̈�
     * @return ?V���ȕ�_�Ɉ�ԋ߂� Voronoi �̈�
     */
    private EmbeddedGraph.Face findNearestRegion(Point2D tgt,
                                                 VRegion nearRegionHint,
                                                 VRegion lastAddedRegion) {
        /*
        * �߂����� Voronoi �̈��I��
        */
        EmbeddedGraph.Face nearFace;
        double nearDist2;

        if (nearRegionHint != null) {
            nearFace = nearRegionHint.getFace();
            nearDist2 = tgt.distance2(nearRegionHint.getCoordinates());
        } else {
            nearFace = lastAddedRegion.getFace();
            nearDist2 = tgt.distance2(lastAddedRegion.getCoordinates());
        }

        while (true) {
            EmbeddedGraph.Face crntFace = nearFace;

            /*
            * �b��� nearest (crntFace) �̎�͂̃G�b�W�̔��Α��̖ʂ�
            * ?V���ȕ�_�Ƃ̋����𒲂ׂ�?A
            * �b��� nearest ����߂� Voronoi �̈�쩂���
            */
            Vector edges = crntFace.getEdgeCycleInCCW();
            for (Enumeration e = edges.elements(); e.hasMoreElements();) {
                EmbeddedGraph.Edge edge = (EmbeddedGraph.Edge) e.nextElement();
                EmbeddedGraph.Face face = getAnotherFace(edge, crntFace);
                VRegion rgn = (VRegion) (face.getUserData());
                if (rgn == null) // �O���̖ʂ̓X�L�b�v
                    continue;

                double dist2 = tgt.distance2(rgn.getCoordinates());
                if (dist2 < nearDist2) {
                    nearFace = face;
                    nearDist2 = dist2;
                }
            }

            /*
            * crntFace ����ԋ߂����?A��?[�v�𔲂���
            */
            if (crntFace == nearFace)
                break;
        }

        return nearFace;
    }

    /**
     * �G�b�W�̑���̒��_��Ԃ�?B
     *
     * @param e �G�b�W
     * @param v �G�b�W�̈��̒��_
     * @return �G�b�W�̑���̒��_
     */
    private EmbeddedGraph.Vertex anotherEnd(EmbeddedGraph.Edge e,
                                            EmbeddedGraph.Vertex v) {
        EmbeddedGraph.Vertex[] vertices = e.getVertices();

        if (v == vertices[0]) return vertices[1];
        if (v == vertices[1]) return vertices[0];

        return null;
    }

    /**
     * �G�b�W�̑���̖ʂ�Ԃ�?B
     *
     * @param e �G�b�W
     * @param f �G�b�W�̈��̖�
     * @return �G�b�W�̑���̖�
     */
    private EmbeddedGraph.Face getAnotherFace(EmbeddedGraph.Edge e,
                                              EmbeddedGraph.Face f) {
        EmbeddedGraph.Face[] faces = e.getFaces();

        if (f == faces[0]) return faces[1];
        if (f == faces[1]) return faces[0];

        return null;
    }

    /**
     * Voronoi �_��?u��?�?v�ł��邱�Ƃ���O?B
     */
    private class PointIsExtra extends Exception {
        /**
         * �I�u�W�F�N�g��?\�z����
         */
        protected PointIsExtra() {
            super();
        }

        /**
         * ?־��^���ăI�u�W�F�N�g��?\�z����
         */
        public PointIsExtra(String s) {
            super(s);
        }
    }

    /**
     * H ��?�߂�?B
     * <p/>
     * H = (vrtx ���� addedPoint �ւ̋���) - (vrtx ����?ł�߂���_�ւ̋���)
     * </p>
     *
     * @param vrtx       Voronoi �_
     * @param addedPoint ?V���ȕ�_
     * @return H
     * @throws PointIsExtra Voronoi �_��?u��?�?v�ł���
     */
    private double evaluateH(EmbeddedGraph.Vertex vrtx,
                             Point2D addedPoint) throws PointIsExtra {
        VPoint pnt = (VPoint) vrtx.getUserData();

        // ��ԋ߂���_�Ƃ̋������}�C�i�X�̓_��?A��?��� Voronoi �_
        if (pnt.getDistance() < 0.0)
            throw new PointIsExtra();

        return addedPoint.distance(pnt.getCoordinates()) - pnt.getDistance();
    }

    /**
     * ����G�b�W���^����ꂽ���X�g�Ɋ܂܂�Ă��邩�ۂ�?A��Ԃ�?B
     *
     * @param list �G�b�W�̃��X�g
     * @param edge ���ׂ̑�?ۂƂȂ�G�b�W
     * @return edge �� list �Ɋ܂܂�Ă���� true
     */
    private boolean edgeIsContained(Vector list,
                                    EmbeddedGraph.Edge edge) {
        for (Enumeration e = list.elements(); e.hasMoreElements();)
            if (edge.isIdentWith((EmbeddedGraph.Edge) e.nextElement()) == true)
                return true;

        return false;
    }

    /**
     * ?V���� Voronoi �_��?W�l��?�߂�?B
     * <p/>
     * �?�߂�?W�l��?A���̃?�\�b�h�̓Ք�� VPoint �Ƃ���
     * newVrtx ��?ݒ肷��?B
     * </p>
     *
     * @param edge    ��������G�b�W
     * @param vrtx1   ��������G�b�W�̒[�_
     * @param vrtx2   ��������G�b�W�̒[�_
     * @param newVrtx �G�b�W�𕪊�����_ (?V���� Voronoi �_)
     * @param tgt     Voronoi ?}�ɉB��悤�Ƃ��Ă����_
     */
    private void setMiddlePoint(EmbeddedGraph.Edge edge,
                                EmbeddedGraph.Vertex vrtx1,
                                EmbeddedGraph.Vertex vrtx2,
                                EmbeddedGraph.Vertex newVrtx,
                                Point2D tgt) {
        // bln1 : �G�b�W�̊�?��ł���?�
        VPoint xy1 = (VPoint) vrtx1.getUserData();
        VPoint xy2 = (VPoint) vrtx2.getUserData();
        BoundedLine2D bln1 =
                new BoundedLine2D(xy1.getCoordinates(), xy2.getCoordinates());

        // lin2 : �^����ꂽ���_��?u�G�b�W�̂����ꂩ�̑��̖ʂ̕�_?v�Ƃ�?����񓙕�?�
        EmbeddedGraph.Face[] faces = edge.getFaces();
        Line2D lin2 = null;
        Point2D old = null;

        for (int i = 0; i < 2; i++) {
            VRegion rgn = (VRegion) faces[i].getUserData();
            old = rgn.getCoordinates();
            Vector2D dir = old.subtract(tgt);
            if (dir.length() <
                    ConditionOfOperation.getCondition().getToleranceForDistance())
                continue;

            lin2 = new Line2D(old.linearInterpolate(tgt, 0.5),
                    dir.verticalVector());
            break;
        }

        if (lin2 == null) // ��_��?d�����Ă��� : �����ł͂��蓾�Ȃ�
            return;

        // bln1 �� lin2 �̌�_��?�߂�
        IntersectionPoint2D ints;

        if (conditionWithSmallDTol == null) {
            conditionWithSmallDTol =
                    ConditionOfOperation.getDefaultCondition().
                            makeCopyWithToleranceForDistance(1.0e-8);
        }
        conditionWithSmallDTol.push();

        try {
            ints = bln1.intersect1AsInfiniteLine(lin2);
        } catch (IndefiniteSolutionException e) {
            ints = null;
        }

        ConditionOfOperation.pop();

        Point2D middlePoint;

        if (ints != null) {
            // ��_��?V���� Voronoi �_��?W�Ƃ���
            middlePoint = ints.coordinates();
        } else {
            // (xy1 �� xy2 �̋������ƂĂ�?�����) �µ���� (��?�?s)
            // ���̃P?[�X�͋N����Ȃ��͂�����?A?A?A
            // xy1 �� xy 2 �̒��_��?V���� Voronoi �_��?W�Ƃ���
            middlePoint = xy1.getCoordinates().
                    linearInterpolate(xy2.getCoordinates(), 0.5);
        }

        VPoint vpnt = new VPoint(middlePoint);
        newVrtx.setUserData(vpnt);
        vpnt.setDistance(Math.sqrt((tgt.distance2(middlePoint) +
                old.distance2(middlePoint)) / 2.0));
    }

    /**
     * ?���?}�̎c�[��?�?�����?B
     *
     * @param extraPoints  ?��� Voronoi �_�̔z��
     * @param extraRegions ?��� Voronoi �̈�̔z��
     */
    private void removeExtraFaces(EmbeddedGraph.Vertex[] extraPoints,
                                  VRegion[] extraRegions) {
        Vector RandF = new Vector();
        int i;

        for (i = 0; i < 3; i++)
            RandF.addElement(new RegionAndFace(extraRegions[i],
                    extraRegions[i].getFace()));

        for (i = 0; i < 3; i++)
            killEdges(extraPoints[i].getEdgeCycleInCCW(), RandF);

        // �O���̖ʂƂ��Ďc�B��ʂ� userData ��N���A
        for (Enumeration e = RandF.elements(); e.hasMoreElements();) {
            RegionAndFace RF = (RegionAndFace) e.nextElement();
            if (RF.faceKilled != true)
                RF.face.setUserData(null);
        }
    }

    // I N S T A N C E   M E T H O D S

    /**
     * Voronoi ?}�̈ʑ���ێ?����O���t��Ԃ�?B
     *
     * @return Voronoi ?}�̈ʑ���ێ?����O���t
     */
    public EmbeddedGraph getGraph() {
        return graph;
    }

    /**
     * ?d�������_�̑g��?���Ԃ�?B
     *
     * @return ?d�������_�̑g��?�
     */
    public int getNumberOfPairsOfCoincidingRegions() {
        return coincidingRegions.size();
    }

    /**
     * ?d�������_�� Enumeration ��Ԃ�?B
     *
     * @return ?d�������_ (VoronoiDiagram2D.VRegion) �� Enumeration
     */
    public Enumeration coincidingRegionElements() {
        return coincidingRegions.elements();
    }

    /**
     * (?d����?�����) Voronoi �̈�� Enumeration ��Ԃ�?B
     *
     * @return Voronoi �̈� (VoronoiDiagram2D.VRegion) �� Enumeration
     */
    public Enumeration regionElements() {
        return new Enumeration() {
            Enumeration e = graph.faceElements();
            Object nextNonOuterFace = null;

            public boolean hasMoreElements() {
                if (nextNonOuterFace != null)
                    return true;

                if (e.hasMoreElements() == false)
                    return false;

                Object obj = e.nextElement();
                if (((EmbeddedGraph.Face) obj).getUserData() != null) {
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
                if (((EmbeddedGraph.Face) obj).getUserData() != null) {
                    return ((EmbeddedGraph.Face) obj).getUserData();
                } else {
                    return ((EmbeddedGraph.Face) e.nextElement()).getUserData();
                }
            }
        };
    }

    /**
     * Voronoi �_�� Enumeration ��Ԃ�?B
     *
     * @return Voronoi �_ (VoronoiDiagram2D.VPoint) �� Enumeration
     */
    public Enumeration pointElements() {
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
     * ��?���?�?�����?B
     * <p/>
     * ��_��?W�l?^?d��?��� Voronoi �_��?W�l�Ȃǂ̊�?���?�?���?A
     * ���� Voronoi ?}�̈ʑ�?�񂾂���^������?��݃O���t�Ƃ��Ďc��?B
     * ���̃?�\�b�h��Ă�?o�������?A���̃C���X�^���X�ɃA�N�Z�X���邱�Ƃ͂ł��Ȃ�?B
     * </p>
     *
     * @return �c�����ʑ�?��
     */
    public EmbeddedGraph stripGeometries() {

        // �O���t�̒��_���� Voronoi �_��?���?�?�
        for (Enumeration e = this.graph.vertexElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Vertex vrtx =
                    (EmbeddedGraph.Vertex) e.nextElement();
            VPoint pnt = (VPoint) vrtx.getUserData();
            if (pnt != null)
                vrtx.setUserData(pnt.getUserData());
        }

        // �O���t�̖ʂ��� Voronoi �̈��?���?�?�
        for (Enumeration e = this.graph.faceElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Face face =
                    (EmbeddedGraph.Face) e.nextElement();
            VRegion rgn = (VRegion) face.getUserData();
            if (rgn != null)
                face.setUserData(rgn.getUserData());
        }

        // Voronoi �̈�̔z���?�?�
        this.regions = null;

        // ?d������ Voronoi �̈�̃��X�g��?�?�
        this.coincidingRegions = null;

        return this.graph;
    }

    /**
     * �G�b�W��v�����g����?B
     *
     * @param message �?�b�Z?[�W
     */
    private void debugPrint(String message) {
        System.out.println("// Start of " + message);

        int i = 0;
        for (Enumeration e = this.graph.vertexElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Vertex vrtx = (EmbeddedGraph.Vertex) e.nextElement();
            VoronoiDiagram2D.VPoint vpnt =
                    (VoronoiDiagram2D.VPoint) vrtx.getUserData();
            Point2D crd = vpnt.getCoordinates();

            System.out.println("CartesianPoint2D	pnt" + i);
            System.out.println(crd.x() + " " + crd.y());
            System.out.println("End");
            i++;
        }

        i = 0;
        for (Enumeration e = this.graph.edgeElements();
             e.hasMoreElements();) {
            EmbeddedGraph.Edge edge = (EmbeddedGraph.Edge) e.nextElement();
            EmbeddedGraph.Vertex[] vrtcs = edge.getVertices();
            VoronoiDiagram2D.VPoint vpnt0 =
                    (VoronoiDiagram2D.VPoint) vrtcs[0].getUserData();
            VoronoiDiagram2D.VPoint vpnt1 =
                    (VoronoiDiagram2D.VPoint) vrtcs[1].getUserData();
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

        System.out.println("// End of " + message);
    }

    // Main Programs for Debugging
    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String[] args) {
        Vector points = new Vector();

        points.addElement(new CartesianPoint2D(-0.5, -0.5));
        points.addElement(new CartesianPoint2D(0.5, -0.5));
        points.addElement(new CartesianPoint2D(0.5, 0.5));
        points.addElement(new CartesianPoint2D(-0.5, 0.5));

        VoronoiDiagram2D voronoi =
                new VoronoiDiagram2D(new EmbeddedGraph(),
                        points.elements());

        System.out.println("# Regions");
        for (Enumeration e = voronoi.regionElements(); e.hasMoreElements();) {
            VRegion rgn = (VRegion) e.nextElement();
            int idx = rgn.getIndex();
            Point2D crd = rgn.getCoordinates();
            System.out.println("# [" + idx + "] " + crd.x() + ", " + crd.y());

            for (Enumeration e1 = rgn.getVPointCycleInCCW();
                 e1.hasMoreElements();) {
                VPoint pnt1 = (VPoint) e1.nextElement();
                Point2D crd1 = pnt1.getCoordinates();
                System.out.println("#\t" + crd1.x() + ", " + crd1.y());
            }
        }

        System.out.println("# Coinciding Regions");
        for (Enumeration e = voronoi.coincidingRegionElements(); e.hasMoreElements();) {
            VRegion rgn = (VRegion) e.nextElement();
            int idx = rgn.getIndex();
            Point2D crd = rgn.getCoordinates();
            System.out.println("# [" + idx + "] " + crd.x() + ", " + crd.y());

            for (Enumeration e1 = rgn.getVPointCycleInCCW();
                 e1.hasMoreElements();) {
                VPoint pnt1 = (VPoint) e1.nextElement();
                Point2D crd1 = pnt1.getCoordinates();
                System.out.println("#\t" + crd1.x() + ", " + crd1.y());
            }
        }

        System.out.println("# Points");
        for (Enumeration e = voronoi.pointElements(); e.hasMoreElements();) {
            VPoint pnt = (VPoint) e.nextElement();
            Point2D crd = pnt.getCoordinates();
            System.out.println("# " + crd.x() + ", " + crd.y());
        }

        EmbeddedGraph graph = voronoi.getGraph();
        int i = 0;

        for (Enumeration e = graph.edgeElements(); e.hasMoreElements();) {
            EmbeddedGraph.Edge edge = (EmbeddedGraph.Edge) e.nextElement();
            EmbeddedGraph.Vertex[] vrtcs = edge.getVertices();

            VoronoiDiagram2D.VPoint vpnt0 =
                    (VoronoiDiagram2D.VPoint) vrtcs[0].getUserData();
            VoronoiDiagram2D.VPoint vpnt1 =
                    (VoronoiDiagram2D.VPoint) vrtcs[1].getUserData();

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
