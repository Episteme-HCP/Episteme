/*
 * �O�����̃I�t�Z�b�g��v�Z����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Ofst3D.java,v 1.3 2007-10-23 18:19:43 virtualcall Exp $
 */
package org.episteme.mathematics.geometry;

/**
 * �O�����̃I�t�Z�b�g��v�Z����N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:43 $
 */
class Ofst3D {
    /** u����\����?� */
    private final static int u_dir = 0;

    /** v����\����?� */
    private final static int v_dir = 1;

    /** �I�t�Z�b�g���镽�� */
    private ParametricSurface3D surface;

    /** �I�t�Z�b�g�͈� */
    private ParameterSection upint;

    /** DOCUMENT ME! */
    private ParameterSection vpint;

    /** �I�t�Z�b�g�̕�� */
    private int side;

    /** �I�t�Z�b�g�̒��� */
    private double magni;

    /** ��?� */
    private ToleranceForDistance tolerance;

    /** �?�b�V���z��(�T���v�����O) */
    private Mesh3D mesh;

/**
     * �R���X�g���N�^
     *
     * @param surface   �I�t�Z�b�g���镽��
     * @param upint     �I�t�Z�b�g���(u���)
     * @param vpint     �I�t�Z�b�g���(v���)
     * @param magni     �I�t�Z�b�g�̋���
     * @param side      �I�t�Z�b�g�̕��
     * @param tolerance ��?�
     */
    Ofst3D(ParametricSurface3D surface, ParameterSection upint,
        ParameterSection vpint, double magni, int side,
        ToleranceForDistance tolerance) {
        surface.checkUValidity(upint);
        surface.checkVValidity(vpint);
        this.surface = surface;
        this.upint = upint;
        this.vpint = vpint;
        this.magni = magni;
        this.side = side;
        this.tolerance = tolerance;
    }

    /**
     * �Ȗʂ̃I�t�Z�b�g��?�߂�
     *
     * @return �I�t�Z�b�g�Ȗ�
     */
    private BsplineSurface3D offset_bss() {
        // �T���v�����O����(�?�b�V���ɕϊ�����)
        set_sampling_points();

        // �I�t�Z�b�g�_���?�߂�
        Point3D[][] offset_mesh = set_offset_points();

        // �p���??[�^��?�߂�
        double[] uparams = make_own_parameter(offset_mesh, u_dir);
        double[] vparams = make_own_parameter(offset_mesh, v_dir);

        // ��ԋߎ�����
        BsplineSurface3D obss = approx_bss(offset_mesh, uparams, vparams);

        return obss;
    }

    /**
     * �?�b�V����?�߂�(�T���v�����O)
     */
    private void set_sampling_points() {
        // �T���v�����O�ɂ͕�?�tolerance�̔����̒l��p����
        ToleranceForDistance tol = new ToleranceForDistance(0.5 * tolerance.value());

        mesh = surface.toMesh(upint, vpint, tol);
    }

    /**
     * �?�b�V������I�t�Z�b�g�_���?�߂�(�Hٓ_�Ȃ�)
     *
     * @return �I�t�Z�b�g���ꂽ�?�b�V��
     */
    private Point3D[][] set_offset_points() {
        int i;
        int j;
        Point3D[][] offset_mesh = new Point3D[mesh.uNPoints()][mesh.vNPoints()];

        // �I�t�Z�b�g�_���?�߂�
        for (i = 0; i < mesh.uNPoints(); i++)
            for (j = 0; j < mesh.vNPoints(); j++) {
                offset_mesh[i][j] = make_offset_point(i, j);
            }

        return offset_mesh;
    }

    /**
     * �I�t�Z�b�g�_���B�X�v���C���ǖʂŋߎ�����
     *
     * @param offset_points �I�t�Z�b�g�_��
     * @param uparams u���̃p���??[�^
     * @param vparams v���̃p���??[�^
     *
     * @return �I�t�Z�b�g�Ȗ�
     */
    private BsplineSurface3D approx_bss(Point3D[][] offset_points,
        double[] uparams, double[] vparams) {
        BsplineSurface3D bss = new BsplineSurface3D(offset_points, uparams,
                vparams, mesh.uClosed(), mesh.vClosed(), tolerance);

        return bss;
    }

    /**
     * �I�t�Z�b�g�_��v�Z����
     *
     * @param i u���̃C���f�b�N�X
     * @param j v���̃C���f�b�N�X
     *
     * @return �I�t�Z�b�g�_
     *
     * @throws InvalidArgumentValueException DOCUMENT ME!
     */
    private Point3D make_offset_point(int i, int j) {
        // �܂�?A�I�t�Z�b�g�x�N�g����?�߂�
        PointOnSurface3D base_point = (PointOnSurface3D) mesh.pointAt(i, j);
        double[] params = base_point.parameters();

        Vector3D offset_vector = surface.normalVector(params[0], params[1]);

        offset_vector = offset_vector.unitized();
        offset_vector = offset_vector.multiply(magni);

        // �I�t�Z�b�g�_��?�߂�
        Point3D point = surface.coordinates(params[0], params[1]);
        Point3D offset_point;

        if (side == WhichSide.FRONT) {
            offset_point = point.add(offset_vector);
        } else if (side == WhichSide.BACK) {
            offset_vector = offset_vector.reverse();
            offset_point = point.add(offset_vector);
        } else {
            throw new InvalidArgumentValueException();
        }

        return offset_point;
    }

    /**
     * �I�t�Z�b�g�_��̃p���??[�^��?�߂�
     *
     * @param points �I�t�Z�b�g�z��
     * @param dir �I�t�Z�b�g�̕��
     *
     * @return �p���??[�^�z��
     */
    private double[] make_own_parameter(Point3D[][] points, int dir) {
        int longest_line; // ��Ԓ�����?�̃C���f�b�N�X
        double[] length; // �����̔z��
        Point3D[] one_line; // �_��(���C��)
        int num_of_line; // ���C����?�
        int i;
        int j;
        double inc;
        boolean is_closed;

        longest_line = get_longest_line(points, dir);

        // ��?��ɒl��Z�b�g
        if (dir == u_dir) {
            num_of_line = mesh.uNPoints();
            inc = upint.increase();
            is_closed = mesh.uClosed();
            one_line = new Point3D[num_of_line];

            for (i = 0; i < num_of_line; i++)
                one_line[i] = points[i][longest_line];
        } else {
            num_of_line = mesh.vNPoints();
            inc = vpint.increase();
            is_closed = mesh.vClosed();
            one_line = new Point3D[num_of_line];

            for (i = 0; i < num_of_line; i++)
                one_line[i] = points[longest_line][i];
        }

        // ������?�߂�
        double[] array_of_length = new double[num_of_line];
        array_of_length[0] = 0.0;

        Point3D source_point = one_line[0];

        for (i = 1; i < num_of_line; i++) {
            array_of_length[i] = array_of_length[i - 1] +
                one_line[i].distance(source_point);
            source_point = one_line[i];
        }

        // �P�ʒ�������̃p���??[�^�̑?���𓾂�
        double increase_per_length = inc / array_of_length[num_of_line - 1];

        double[] own_params = new double[num_of_line];
        own_params[0] = 0.0;

        for (i = 1; i < num_of_line; i++)
            own_params[i] = array_of_length[i] * increase_per_length;

        return own_params;
    }

    /**
     * �?�b�V���ň�Ԓ�����?��?�߂�
     *
     * @param points �I�t�Z�b�g�_��
     * @param dir ���
     *
     * @return ��Ԓ�����?�̃C���f�b�N�X
     */
    private int get_longest_line(Point3D[][] points, int dir) {
        double length;
        double max_length;
        int longest_line;
        int i;
        int j;

        max_length = 0.0;
        longest_line = -1;

        if (dir == u_dir) {
            for (j = 0; j < mesh.vNPoints(); j++) {
                length = 0.0;

                for (i = 1; i < mesh.uNPoints(); i++)
                    length += points[i][j].distance(points[i - 1][j]);

                if (mesh.uClosed() == true) {
                    length += points[0][j].distance(points[i - 1][j]);
                }

                if (length > max_length) {
                    max_length = length;
                    longest_line = j;
                }
            }
        } else {
            for (i = 0; i < mesh.uNPoints(); i++) {
                length = 0.0;

                for (j = 1; j < mesh.vNPoints(); j++)
                    length += points[i][j].distance(points[i][j - 1]);

                if (mesh.vClosed() == true) {
                    length += points[i][0].distance(points[i][j - 1]);
                }

                if (length > max_length) {
                    max_length = length;
                    longest_line = i;
                }
            }
        }

        return longest_line;
    }

    /**
     * �I�t�Z�b�g��Ԃ�
     *
     * @return �I�t�Z�b�g�Ȗ�
     */
    BsplineSurface3D offset() {
        if (magni == 0.0) {
            return surface.toBsplineSurface(upint, vpint);
        } else {
            return offset_bss();
        }
    }
}
