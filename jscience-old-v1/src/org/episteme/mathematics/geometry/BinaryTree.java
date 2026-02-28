/*
 * �񕪖�?�̊K�w?\����\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: BinaryTree.java,v 1.3 2007-10-21 21:08:06 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * �񕪖�?�̊K�w?\����\���N���X?B
 * <p/>
 * �����ň����񕪖؂�?A
 * ?� (root) �ƌĂ΂��?A�����̃m?[�h�쳂Ƃ���?A
 * �e�m?[�h��?��E��̎q�m?[�h��?���Ƃ��ł���f?[�^?\���ł���?B
 * </p>
 * <p/>
 * �񕪖�?�̃m?[�h��?A���̃N���X�̓Ք�N���X
 * {@link BinaryTree.Node BinaryTree.Node}
 * �̃C���X�^���X�ł���?B
 * ��̃m?[�h�ɂ�?A���̃m?[�h��?u�f?[�^?v�Ƃ���?A
 * �C�ӂ̃I�u�W�F�N�g���� (����) �֘A�Â��邱�Ƃ��ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:06 $
 * @see QuadTree
 */

class BinaryTree {
    /**
     * root �m?[�h?B
     */
    private Node root;

    /**
     * �^����ꂽ�I�u�W�F�N�g�� root �m?[�h��?u�f?[�^?v�Ƃ���񕪖؂�?\�z����?B
     *
     * @param data root �m?[�h��?u�f?[�^?v�Ƃ���I�u�W�F�N�g
     */
    BinaryTree(Object data) {
        super();
        root = new BinaryTree.Node(null, data);
    }

    /**
     * ���̓񕪖؂� root �m?[�h��Ԃ�?B
     *
     * @return root �m?[�h
     */
    Node rootNode() {
        return root;
    }

    /**
     * �񕪖�?�̂����̃m?[�h��\�� BinaryTree �̓Ք�N���X?B
     * <p/>
     * �m?[�h��?��E��̎q�m?[�h��?���Ƃ��ł��� (�?���Ȃ��Ă�?\��Ȃ�) ?B
     * </p>
     * <p/>
     * �܂�?A�m?[�h�ɂ�?A���̃m?[�h��?u�f?[�^?v�Ƃ���?A
     * �C�ӂ̃I�u�W�F�N�g���� (����) �֘A�Â��邱�Ƃ��ł���?B
     * </p>
     *
     * @see BinaryTree
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
         * ?��̎q�m?[�h?B
         */
        private Node left;

        /**
         * �E�̎q�m?[�h?B
         */
        private Node right;

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
         * ���̃m?[�h��?��̎q�m?[�h��Ԃ�?B
         * <p/>
         * ?��̎q�m?[�h�� null �ł��邩�µ��Ȃ�?B
         * </p>
         *
         * @return ?��̎q�m?[�h
         */
        synchronized Node left() {
            return left;
        }

        /**
         * ���̃m?[�h�̉E�̎q�m?[�h��Ԃ�?B
         * <p/>
         * �E�̎q�m?[�h�� null �ł��邩�µ��Ȃ�?B
         * </p>
         *
         * @return �E�̎q�m?[�h
         */
        synchronized Node right() {
            return right;
        }

        /**
         * �^����ꂽ�m?[�h��?A���̃m?[�h��?��̎q�m?[�h�Ƃ���?ݒ肷��?B
         * <p/>
         * left �� null �ł�?\��Ȃ�?B
         * </p>
         *
         * @param left ?��̎q�m?[�h�Ƃ���?ݒ肷��m?[�h
         */
        synchronized void left(Node left) {
            this.left = left;
        }

        /**
         * �^����ꂽ�m?[�h��?A���̃m?[�h�̉E�̎q�m?[�h�Ƃ���?ݒ肷��?B
         * <p/>
         * right �� null �ł�?\��Ȃ�?B
         * </p>
         *
         * @param right �E�̎q�m?[�h�Ƃ���?ݒ肷��m?[�h
         */
        synchronized void right(Node right) {
            this.right = right;
        }

        /**
         * ���̃m?[�h��?��̎q�m?[�h��?V����?�?�����?B
         * <p/>
         * ��?��ŗ^����ꂽ�I�u�W�F�N�g��?V����?�?�����?��̎q�m?[�h��?u�f?[�^?v�ƂȂ�?B
         * </p>
         * <p/>
         * ���̃m?[�h�ɂ��ł�?��̎q�m?[�h��?ݒ肳��Ă���?�?��ɂ�
         * FatalException �̗�O��?�����?B
         * </p>
         *
         * @param data ?V����?�?�����m?[�h��?u�f?[�^?v
         * @return ?��̎q�Ƃ���?V����?�?������m?[�h
         * @see FatalException
         */
        synchronized Node makeLeft(Object data) {
            if (left() != null)
                throw new FatalException();

            left(new Node(this, data));
            return left();
        }

        /**
         * ���̃m?[�h�ɉE�̎q�m?[�h��?V����?�?�����?B
         * <p/>
         * ��?��ŗ^����ꂽ�I�u�W�F�N�g��?V����?�?�����E�̎q�m?[�h��?u�f?[�^?v�ƂȂ�?B
         * </p>
         * <p/>
         * ���̃m?[�h�ɂ��łɉE�̎q�m?[�h��?ݒ肳��Ă���?�?��ɂ�
         * FatalException �̗�O��?�����?B
         * </p>
         *
         * @param data ?V����?�?�����m?[�h��?u�f?[�^?v
         * @return �E�̎q�Ƃ���?V����?�?������m?[�h
         * @see FatalException
         */
        synchronized Node makeRight(Object data) {
            if (right() != null)
                throw new FatalException();

            right(new Node(this, data));
            return right();
        }

        /**
         * ���̃m?[�h��艺��?L���镔���؂̊e�m?[�h�ɂ���?A�w���?��?��{��?B
         * <p/>
         * ���̃m?[�h��?g��܂ޕ����؂̊e�m?[�h�ɂ��� tproc.doit() ��Ă�?o��?B
         * ����m?[�h�ɂ���?Atproc.doit() ��Ă�?o��?��Ԃ�?A?u����/?��̎q/�E�̎q?v�ł���?B
         * </p>
         * <p/>
         * tproc.doit() �� false ��Ԃ���?Atproc.doit() �̌Ă�?o���𑱂�?A
         * ���ׂẴm?[�h�ɂ��Ă̌Ă�?o����?I�����邩?A
         * �܂��� tproc.doit() �� true �� �Ԃ������_��?I������?B
         * </p>
         * <p/>
         * ����m?[�h�ɂ��� tproc.doit() �� true ��Ԃ���?�?��ɂ�?A
         * ���̃m?[�h��Ԃ�?B
         * ������̃m?[�h�ɑ΂��� tproc.doit() �� true ��Ԃ��Ȃ��B�?�?��ɂ�
         * null ��Ԃ�?B
         * </p>
         *
         * @param tproc {@link BinaryTree.TraverseProc TraverseProc}
         *              �C���^?[�t�F�C�X��������N���X�̃C���X�^���X
         * @param pdata tproc.doit() �̑�O��?��ɗ^����C�ӂ̃I�u�W�F�N�g
         * @return tproc.doit() �� true ��Ԃ����m?[�h
         * @see #preOrderEnumeration()
         */
        synchronized Node preOrderTraverse(TraverseProc tproc, Object pdata) {
            return myPreOrderTraverse(0, this, tproc, pdata);
        }

        /**
         * ���̃m?[�h��艺��?L���镔���؂̊e�m?[�h�ɂ���?A�w���?��?��{��?B
         * <p/>
         * ���̃m?[�h��?g��܂ޕ����؂̊e�m?[�h�ɂ��� tproc.doit() ��Ă�?o��?B
         * ����m?[�h�ɂ���?Atproc.doit() ��Ă�?o��?��Ԃ�?A?u?��̎q/����/�E�̎q?v�ł���?B
         * </p>
         * <p/>
         * tproc.doit() �� false ��Ԃ���?Atproc.doit() �̌Ă�?o���𑱂�?A
         * ���ׂẴm?[�h�ɂ��Ă̌Ă�?o����?I�����邩?A
         * �܂��� tproc.doit() �� true �� �Ԃ������_��?I������?B
         * </p>
         * <p/>
         * ����m?[�h�ɂ��� tproc.doit() �� true ��Ԃ���?�?��ɂ�?A
         * ���̃m?[�h��Ԃ�?B
         * ������̃m?[�h�ɑ΂��� tproc.doit() �� true ��Ԃ��Ȃ��B�?�?��ɂ�
         * null ��Ԃ�?B
         * </p>
         *
         * @param tproc {@link BinaryTree.TraverseProc TraverseProc}
         *              �C���^?[�t�F�C�X��������N���X�̃C���X�^���X
         * @param pdata tproc.doit() �̑�O��?��ɗ^����C�ӂ̃I�u�W�F�N�g
         * @return tproc.doit() �� true ��Ԃ����m?[�h
         * @see #inOrderEnumeration()
         */
        synchronized Node inOrderTraverse(TraverseProc tproc, Object pdata) {
            return myInOrderTraverse(0, this, tproc, pdata);
        }

        /**
         * ���̃m?[�h��艺��?L���镔���؂̊e�m?[�h�ɂ���?A�w���?��?��{��?B
         * <p/>
         * ���̃m?[�h��?g����܂ޕ����؂̊e�m?[�h�ɂ��� tproc.doit() ��Ă�?o��?B
         * ����m?[�h�ɂ���?Atproc.doit() ��Ă�?o��?��Ԃ�?A?u?��̎q/�E�̎q/����?v�ł���?B
         * </p>
         * <p/>
         * tproc.doit() �� false ��Ԃ���?Atproc.doit() �̌Ă�?o���𑱂�?A
         * ���ׂẴm?[�h�ɂ��Ă̌Ă�?o����?I�����邩?A
         * �܂��� tproc.doit() �� true �� �Ԃ������_��?I������?B
         * </p>
         * <p/>
         * ����m?[�h�ɂ��� tproc.doit() �� true ��Ԃ���?�?��ɂ�?A
         * ���̃m?[�h��Ԃ�?B
         * ������̃m?[�h�ɑ΂��� tproc.doit() �� true ��Ԃ��Ȃ��B�?�?��ɂ�
         * null ��Ԃ�?B
         * </p>
         *
         * @param tproc {@link BinaryTree.TraverseProc TraverseProc}
         *              �C���^?[�t�F�C�X��������N���X�̃C���X�^���X
         * @param pdata tproc.doit() �̑�O��?��ɗ^����C�ӂ̃I�u�W�F�N�g
         * @return tproc.doit() �� true ��Ԃ����m?[�h
         * @see #postOrderEnumeration()
         */
        synchronized Node postOrderTraverse(TraverseProc tproc, Object pdata) {
            return myPostOrderTraverse(0, this, tproc, pdata);
        }

        /**
         * {@link #preOrderTraverse(BinaryTree.TraverseProc,Object) preOrderTraverse()} �̖{��?B
         *
         * @param ctl   �J�n�m?[�h�����?[��
         * @param node  ��?��?ۂƂȂ�m?[�h
         * @param tproc {@link BinaryTree.TraverseProc TraverseProc}
         *              �C���^?[�t�F�C�X��������N���X�̃C���X�^���X
         * @param pdata tproc.doit() �̑�O��?��ɗ^����C�ӂ̃I�u�W�F�N�g
         */
        private Node myPreOrderTraverse(int ctl,
                                        Node node,
                                        TraverseProc tproc,
                                        Object pdata) {
            boolean rvalh;
            Node leftVal = null;
            Node rightVal = null;

            if (node == null) return null;

            if (((rvalh = tproc.doit(node, ctl, pdata)) == true) ||
                    ((leftVal = myPreOrderTraverse(ctl + 1, node.left(), tproc, pdata)) != null) ||
                    ((rightVal = myPreOrderTraverse(ctl + 1, node.right(), tproc, pdata)) != null)) {
                if (rvalh == true)
                    return node;
                if (leftVal != null)
                    return leftVal;
                return rightVal;
            }

            return null;
        }

        /**
         * {@link #inOrderTraverse(BinaryTree.TraverseProc,Object) inOrderTraverse()} �̖{��?B
         *
         * @param ctl   �J�n�m?[�h�����?[��
         * @param node  ��?��?ۂƂȂ�m?[�h
         * @param tproc {@link BinaryTree.TraverseProc TraverseProc}
         *              �C���^?[�t�F�C�X��������N���X�̃C���X�^���X
         * @param pdata tproc.doit() �̑�O��?��ɗ^����C�ӂ̃I�u�W�F�N�g
         */
        private Node myInOrderTraverse(int ctl, Node node,
                                       TraverseProc tproc,
                                       Object pdata) {
            boolean rvalh = false;
            Node leftVal;
            Node rightVal = null;

            if (node == null) return null;

            if (((leftVal = myInOrderTraverse(ctl + 1, node.left(), tproc, pdata)) != null) ||
                    ((rvalh = tproc.doit(node, ctl, pdata)) == true) ||
                    ((rightVal = myInOrderTraverse(ctl + 1, node.right(), tproc, pdata)) != null)) {
                if (leftVal != null)
                    return leftVal;
                if (rvalh == true)
                    return node;
                return rightVal;
            }

            return null;
        }

        /**
         * {@link #postOrderTraverse(BinaryTree.TraverseProc,Object) postOrderTraverse()} �̖{��?B
         *
         * @param ctl   �J�n�m?[�h�����?[��
         * @param node  ��?��?ۂƂȂ�m?[�h
         * @param tproc {@link BinaryTree.TraverseProc TraverseProc}
         *              �C���^?[�t�F�C�X��������N���X�̃C���X�^���X
         * @param pdata tproc.doit() �̑�O��?��ɗ^����C�ӂ̃I�u�W�F�N�g
         */
        private Node myPostOrderTraverse(int ctl, Node node,
                                         TraverseProc tproc,
                                         Object pdata) {
            boolean rvalh;
            Node leftVal;
            Node rightVal = null;

            if (node == null) return null;

            if (((leftVal = myPostOrderTraverse(ctl + 1, node.left(), tproc, pdata)) != null) ||
                    ((rightVal = myPostOrderTraverse(ctl + 1, node.right(), tproc, pdata)) != null) ||
                    ((rvalh = tproc.doit(node, ctl, pdata)) == true)) {
                if (leftVal != null)
                    return leftVal;
                if (rightVal != null)
                    return rightVal;
                return node;
            }

            return null;
        }

        /**
         * ���̃m?[�h��艺��?L���镔���� (�̃m?[�h) �� Enumeration �Ƃ��ĕԂ�?B
         * <p/>
         * ���̃m?[�h��?g��܂ޕ����؂� Enumeration �Ƃ��ĕԂ�?B
         * �m?[�h���i�[�����?���?A����m?[�h�ɂ���?u����/?��̎q/�E�̎q?v�ł���?B
         * </p>
         * <p/>
         * ���̃?�\�b�h�̌��ʂƂ��ē����� Enumeration �� nextElement() ���Ԃ��I�u�W�F�N�g��?A
         * BinaryTree.Node �̃C���X�^���X�ł���?B
         * </p>
         *
         * @return �����؂� Enumeration
         * @see #preOrderTraverse(BinaryTree.TraverseProc,Object)
         */
        synchronized Enumeration preOrderEnumeration() {
            Vector nodes = new Vector();

            preOrderTraverse(new addNodeProc(), nodes);

            return nodes.elements();
        }

        /**
         * ���̃m?[�h��艺��?L���镔���� (�̃m?[�h) �� Enumeration �Ƃ��ĕԂ�?B
         * <p/>
         * ���̃m?[�h��?g��܂ޕ����؂� Enumeration �Ƃ��ĕԂ�?B
         * �m?[�h���i�[�����?���?A����m?[�h�ɂ���?u?��̎q/����/�E�̎q?v�ł���?B
         * </p>
         * <p/>
         * ���̃?�\�b�h�̌��ʂƂ��ē����� Enumeration �� nextElement() ���Ԃ��I�u�W�F�N�g��?A
         * BinaryTree.Node �̃C���X�^���X�ł���?B
         * </p>
         *
         * @return �����؂� Enumeration
         * @see #inOrderTraverse(BinaryTree.TraverseProc,Object)
         */
        synchronized Enumeration inOrderEnumeration() {
            Vector nodes = new Vector();

            inOrderTraverse(new addNodeProc(), nodes);

            return nodes.elements();
        }

        /**
         * ���̃m?[�h��艺��?L���镔���� (�̃m?[�h) �� Enumeration �Ƃ��ĕԂ�?B
         * <p/>
         * ���̃m?[�h��?g��܂ޕ����؂� Enumeration �Ƃ��ĕԂ�?B
         * �m?[�h���i�[�����?���?A����m?[�h�ɂ���?u?��̎q/�E�̎q/����?v�ł���?B
         * </p>
         * <p/>
         * ���̃?�\�b�h�̌��ʂƂ��ē����� Enumeration �� nextElement() ���Ԃ��I�u�W�F�N�g��?A
         * BinaryTree.Node �̃C���X�^���X�ł���?B
         * </p>
         *
         * @return �����؂� Enumeration
         * @see #postOrderTraverse(BinaryTree.TraverseProc,Object)
         */
        synchronized Enumeration postOrderEnumeration() {
            Vector nodes = new Vector();

            postOrderTraverse(new addNodeProc(), nodes);

            return nodes.elements();
        }
    }

    /**
     * �񕪖�?�̂���m?[�h�ɂ��Ď{��
     * �C�ӂ̑�?� (?��?) ��\�� BinaryTree �̓Ք�C���^?[�t�F?[�X?B
     *
     * @see BinaryTree.Node#preOrderTraverse(BinaryTree.TraverseProc,Object)
     * @see BinaryTree.Node#inOrderTraverse(BinaryTree.TraverseProc,Object)
     * @see BinaryTree.Node#postOrderTraverse(BinaryTree.TraverseProc,Object)
     */
    interface TraverseProc {
        /**
         * @param node  ��?��?ۂƂȂ�m?[�h
         * @param ctl   ?��?��J�n�����m?[�h���� node �܂ł�?[�� (?���?�?A�J�n�m?[�h�� 0 �Ƃ���)
         * @param pdata ��?[�U�̎w�肵���C�ӂ̃I�u�W�F�N�g
         * @return ���̃m?[�h��?��?�𑱂���Ȃ� true?A���̃m?[�h��?I������Ȃ� false
         */
        public boolean doit(Node node,
                            int ctl,
                            Object pdata);
    }

    /**
     * ����m?[�h��^����ꂽ���X�g�ɒǉB��� TraverseProc?B
     */
    private class addNodeProc implements TraverseProc {
        /**
         * �^����ꂽ���X�g�Ƀm?[�h��ǉB���?B
         * <p/>
         * pdata �� Vector �N���X�̃C���X�^���X�łȂ���΂Ȃ�Ȃ�?B
         * </p>
         *
         * @param node  pdata �ɒǉB���m?[�h
         * @param ctl   ?��?��J�n�����m?[�h���� node �܂ł�?[�� (�Q?Ƃ��Ȃ�)
         * @param pdata ���X�g
         * @return ?�� false
         */
        public boolean doit(Node node, int ctl, Object pdata) {
            Vector nodes = (Vector) pdata;

            nodes.addElement(node);

            return false;
        }
    }

    /**
     * ����m?[�h���w��̃m?[�h�ł��邩�ۂ���`�F�b�N���� TraverseProc?B
     */
    private class checkNode implements TraverseProc {
        /**
         * ����m?[�h���w��̃m?[�h�ł��邩�ۂ���`�F�b�N����?B
         * <p/>
         * pdata �� BinaryTree.Node �N���X�̃C���X�^���X�łȂ���΂Ȃ�Ȃ�?B
         * </p>
         *
         * @param node  pdata �Ɣ�r����m?[�h
         * @param ctl   ?��?��J�n�����m?[�h���� node �܂ł�?[�� (�Q?Ƃ��Ȃ�)
         * @param pdata �w��̃m?[�h
         * @return node �� pdata �ł���� true?A�����łȂ���� false
         */
        public boolean doit(Node node, int ctl, Object pdata) {
            Node givenNode = (Node) pdata;

            if (node == givenNode) {
                return true;
            }
            return false;
        }
    }
}
