/*
 * �l����?�̊K�w?\����\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: QuadTree.java,v 1.3 2007-10-21 21:08:19 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

/**
 * �l����?�̊K�w?\����\���N���X?B
 * <p/>
 * �����ň����l���؂�?A
 * ?� (root) �ƌĂ΂��?A�����̃m?[�h�쳂Ƃ���?A
 * �e�m?[�h���l�̎q�m?[�h��?���Ƃ��ł���f?[�^?\���ł���?B
 * </p>
 * <p/>
 * �l����?�̃m?[�h��?A���̃N���X�̓Ք�N���X
 * {@link QuadTree.Node QuadTree.Node}
 * �̃C���X�^���X�ł���?B
 * ��̃m?[�h�ɂ�?A���̃m?[�h��?u�f?[�^?v�Ƃ���?A
 * �C�ӂ̃I�u�W�F�N�g���� (����) �֘A�Â��邱�Ƃ��ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:19 $
 * @see BinaryTree
 */

class QuadTree {
    /**
     * root �m?[�h?B
     */
    private Node root;

    /**
     * �^����ꂽ�I�u�W�F�N�g�� root �m?[�h��?u�f?[�^?v�Ƃ���񕪖؂�?\�z����?B
     *
     * @param data root �m?[�h��?u�f?[�^?v�Ƃ���I�u�W�F�N�g
     */
    QuadTree(Object data) {
        super();
        root = new Node(null, data);
    }

    /**
     * ���̎l���؂� root �m?[�h��Ԃ�?B
     *
     * @return root �m?[�h
     */
    Node rootNode() {
        return root;
    }

    /**
     * �l����?�̂����̃m?[�h��\�� QuadTree �̓Ք�N���X?B
     * <p/>
     * �m?[�h�͎l�̎q�m?[�h��?���Ƃ��ł��� (�?���Ȃ��Ă�?\��Ȃ�) ?B
     * </p>
     * <p/>
     * �܂�?A�m?[�h�ɂ�?A���̃m?[�h��?u�f?[�^?v�Ƃ���?A
     * �C�ӂ̃I�u�W�F�N�g���� (����) �֘A�Â��邱�Ƃ��ł���?B
     * </p>
     *
     * @see QuadTree
     */
    class Node {
        /**
         * ���̃m?[�h��?u�f?[�^?v�Ƃ��Ċ֘A�t����ꂽ�I�u�W�F�N�g?B
         */
        private final Object data;

        /**
         * ?e�m?[�h?B
         */
        private final Node parent;

        /**
         * 0 �Ԃ߂̎q�m?[�h?B
         */
        private Node child0;

        /**
         * 1 �Ԃ߂̎q�m?[�h?B
         */
        private Node child1;

        /**
         * 2 �Ԃ߂̎q�m?[�h?B
         */
        private Node child2;

        /**
         * 3 �Ԃ߂̎q�m?[�h?B
         */
        private Node child3;

        /**
         * �^����ꂽ�I�u�W�F�N�g��?u�f?[�^?v�Ƃ���m?[�h��?\�z����?B
         * <p/>
         * data �� null �ł�?\��Ȃ�?B
         * </p>
         * <p/>
         * parent �� null �ł�?\��Ȃ�?B
         * </p>
         *
         * @param data   �m?[�h��?u�f?[�^?v�Ƃ���I�u�W�F�N�g
         * @param parent ?e�m?[�h
         */
        Node(Node parent, Object data) {
            super();
            this.data = data;
            this.parent = parent;
        }

        /**
         * ���̃m?[�h��?u�f?[�^?v��Ԃ�?B
         *
         * @return �m?[�h��?u�f?[�^?v
         */
        Object data() {
            return data;
        }

        /**
         * ���̃m?[�h��?e�m?[�h��Ԃ�?B
         * <p/>
         * ?e�m?[�h�� null �ł��邩�µ��Ȃ�?B
         * </p>
         *
         * @return ?e�m?[�h
         */
        Node parent() {
            return parent;
        }

        /**
         * n �Ԃ߂̎q�m?[�h��Ԃ�?B
         * <p/>
         * n �̒l�� 0 ���� 3 �łȂ���΂Ȃ�Ȃ�?B
         * �����łȂ�?�?��ɂ� InvalidArgumentValueException �̗�O��?�����?B
         * </p>
         *
         * @param n �q�m?[�h�̔�?�
         * @return n �Ԃ߂̎q�m?[�h
         * @see InvalidArgumentValueException
         */
        synchronized Node child(int n) {
            switch (n) {
                case 0:
                    return child0;
                case 1:
                    return child1;
                case 2:
                    return child2;
                case 3:
                    return child3;
            }
            throw new InvalidArgumentValueException();
        }

        /**
         * �^����ꂽ�m?[�h��?A���̃m?[�h�� n �Ԗڂ̎q�m?[�h�Ƃ���?ݒ肷��?B
         * <p/>
         * n �̒l�� 0 ���� 3 �łȂ���΂Ȃ�Ȃ�?B
         * �����łȂ�?�?��ɂ� InvalidArgumentValueException �̗�O��?�����?B
         * </p>
         * <p/>
         * child �� null �ł�?\��Ȃ�?B
         * </p>
         *
         * @param n     �q�m?[�h�̔�?�
         * @param child n �Ԃ߂̃m?[�h�Ƃ���?ݒ肷��m?[�h
         * @see InvalidArgumentValueException
         */
        synchronized void child(int n, Node child) {
            switch (n) {
                case 0:
                    child0 = child;
                    break;
                case 1:
                    child1 = child;
                    break;
                case 2:
                    child2 = child;
                    break;
                case 3:
                    child3 = child;
                    break;
                default:
                    throw new InvalidArgumentValueException();
            }
        }

        /**
         * ���̃m?[�h�� n �Ԗڂ̎q�m?[�h��?V����?�?�����?B
         * <p/>
         * data �ŗ^����ꂽ�I�u�W�F�N�g��?V����?�?�����q�m?[�h��?u�f?[�^?v�ƂȂ�?B
         * </p>
         * <p/>
         * n �̒l�� 0 ���� 3 �łȂ���΂Ȃ�Ȃ�?B
         * �����łȂ�?�?��ɂ� InvalidArgumentValueException �̗�O��?�����?B
         * </p>
         * <p/>
         * ���̃m?[�h�ɂ��ł� n �Ԗڂ̎q�m?[�h��?ݒ肳��Ă���?�?��ɂ�
         * FatalException �̗�O��?�����?B
         * </p>
         *
         * @param data ?V����?�?�����m?[�h��?u�f?[�^?v
         * @return n �Ԗڂ̎q�Ƃ���?V����?�?������m?[�h
         * @see InvalidArgumentValueException
         * @see FatalException
         */
        synchronized Node makeChild(int n, Object data) {
            if (child(n) != null)
                throw new FatalException();

            Node child = new Node(this, data);
            child(n, child);
            return child;
        }
    }
}
