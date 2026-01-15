/*

 * (�Q������) ����?��݃O���t��\���N���X

 *

 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *

 * $Id: EmbeddedGraph.java,v 1.3 2006/03/01 21:15:57 virtualcall Exp $

 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * (�Q������) ����?��݃O���t��\���N���X?B
 * <p/>
 * <p/>
 * <p/>
 * ���̃N���X��?A�O���t�Ɋւ��� Euler �̌� (|V| - |E| + |F| = 2) ��
 * <p/>
 * �������O���t��\������?B
 * <p/>
 * ������?AV �͒��_?AE �͕�?AF �͖ʂ�Ӗ���?A|X| �� X �̌�?���Ӗ�����?B
 * <p/>
 * </p>
 * <p/>
 * <p/>
 * <p/>
 * ���̃N���X�̃C���X�^���X�� Euler �̌��ۑ����Ȃ���
 * <p/>
 * �O���t��?삷�� Euler Operator ����?B
 * <p/>
 * </p>
 * <p/>
 * <p/>
 * <p/>
 * �O���t�Ɋ܂܂�钸�_?^��?^�ʂ�?A���̃N���X�̓Ք�N���X�Ƃ��ĕ\������Ă���?B
 * <p/>
 * </p>
 * <p/>
 * <p/>
 * <p/>
 * ����璸�_?^��?^�ʂ�\������Ք�N���X��?A
 * <p/>
 * �݂���?ڑ��֌W��ێ?����݂̂ł���?A
 * <p/>
 * ���_��?W�l���̊�?��͒�?ڂɂ͕ێ?���Ȃ�?B
 * <p/>
 * ������?A
 * <p/>
 * �e���_?^��?^�ʂɂ�?A
 * <p/>
 * �C�ӂ̃I�u�W�F�N�g (java.lang.Object) ��֘A�t���邱�Ƃ��ł���?B
 * <p/>
 * </p>
 * <p/>
 * <p/>
 * <p/>
 * �Ȃ�?A
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * <p/>
 * �O���t��ł�?V���Ȓ��_?^��?^�ʂ�?�?���S������I�u�W�F�N�g
 * <p/>
 * graphItemMaker ({@link EmbeddedGraph.GraphItemMaker EmbeddedGraph.GraphItemMaker})
 * <p/>
 * ��ێ?����?B
 * <p/>
 * �O���t��?삷�� Euler Operation ��?ۂɂ�?A
 * <p/>
 * ���p�҂���?ۂƂȂ钸�_?^��?^�ʂ�?�?�?�����̂ł͂Ȃ�?A
 * <p/>
 * ���ꂼ��� Euler Operator ����?g�̓Ք��
 * <p/>
 * ���� graphItemMaker �̓��Y�@�\��Ă�?o����
 * <p/>
 * ���_?^��?^�ʂ�?�?�����?B
 * <p/>
 * </p>
 *
 * @author $JGCL_Author$
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:57 $
 */

public class EmbeddedGraph extends java.lang.Object implements Cloneable {

    /**
     * �O���t��?\?�����\���Ք�N���X?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃N���X��?A�O���t�̒��_?^��?^�ʂ�\���N���X�̃X?[�p?[�N���X�ł���?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * <p/>
     * ���p�҂����̃I�u�W�F�N�g�Ɋ֘A�t�������C�ӂ̃f?[�^ userData (java.lang.Object)
     * <p/>
     * ��ێ?����?B
     * <p/>
     * </p>
     */

    protected abstract class GraphItem extends java.lang.Object {

        /**
         * ���p�҂��֘A�t�����C�ӂ̃f?[�^?B
         */

        private java.lang.Object userData = null;

        /**
         * ��?g�̕�?�?B
         * <p/>
         * <p/>
         * <p/>
         * ���̃t�B?[���h��?A�O���t�̕�?���?��?ۂɗ��p����?B
         * <p/>
         * </p>
         *
         * @see #copy()
         * @see #copy(EmbeddedGraph)
         * @see #dualCopy()
         * @see #dualCopy(EmbeddedGraph)
         */

        private GraphItem replica = null;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * <p/>
         * <p/>
         * userData �� null ��?ݒ肳���?B
         * <p/>
         * </p>
         */

        GraphItem() {

            super();

        }

        /**
         * ���p�҂�����?u�O���t��?\?���?v�Ɋ֘A�t�������C�ӂ̃f?[�^�� userData ��?ݒ肷��?B
         * <p/>
         * <p/>
         * <p/>
         * ���̌Ă�?o���ȑO�� userData ��?ݒ肳��Ă����f?[�^�͖Y�ꋎ����?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * object �� null �ł�?\��Ȃ�?B
         * <p/>
         * ����?�?�?AuserData �ɂ� null ��?ݒ肳���?B
         * <p/>
         * </p>
         *
         * @param object �C�ӂ̃f?[�^ (�C�ӂ̃I�u�W�F�N�g)
         * @see #getUserData()
         */

        public synchronized void setUserData(java.lang.Object object) {

            this.userData = object;

        }

        /**
         * ����?u�O���t��?\?���?v�� userData ��Ԃ�?B
         *
         * @return ����?u�O���t��?\?���?v�Ɋ֘A�t�����Ă���C�ӂ̃I�u�W�F�N�g
         * @see #setUserData(java.lang.Object)
         */

        public synchronized java.lang.Object getUserData() {

            return this.userData;

        }

        /**
         * �^����ꂽ?u�O���t��?\?���?v��?g�̕�?��Ƃ���?ݒ肷��?B
         * <p/>
         * <p/>
         * <p/>
         * ���̌Ă�?o���ȑO��?ݒ肳��Ă�����?��͖Y�ꋎ����?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * itemAsReplica �� null �ł�?\��Ȃ�?B
         * <p/>
         * ����?�?�?A��?��͑�?݂��Ȃ���̂ƂȂ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * itemAsReplica �� null �łȂ�?�?�?A
         * <p/>
         * ��?g�� userData �� itemAsReplica �� userData ��?ݒ肷��?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * �Ȃ�?A��?�̕�?��ł͂Ȃ��o�Ε�?���?�?�����Ȃ��?A
         * <p/>
         * ���_�̕�?��Ƃ��Ė�?A�ʂ̕�?��Ƃ��Ē��_��^���邱�ƂɂȂ�?B
         * <p/>
         * </p>
         *
         * @param itemAsReplica ��?��Ƃ���?ݒ肷��?u�O���t��?\?���?v
         * @see #copy()
         * @see #copy(EmbeddedGraph)
         * @see #dualCopy()
         * @see #dualCopy(EmbeddedGraph)
         */

        protected synchronized void setReplica(GraphItem itemAsReplica) {

            if ((this.replica = itemAsReplica) != null) {

                this.replica.userData = this.userData;

            }

        }

        /**
         * ��?g�̕�?��Ƃ���?ݒ肳��Ă���?u�O���t��?\?���?v��Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * �Ȃ�?A��?�̕�?��ł͂Ȃ��o�Ε�?���?�?����Ă���Ȃ��?A
         * <p/>
         * ���_�̕�?��Ƃ��Ė�?A�ʂ̕�?��Ƃ��Ē��_���Ԃ邱�ƂɂȂ�?B
         * <p/>
         * </p>
         *
         * @return ��?��Ƃ���?ݒ肳��Ă���?u�O���t��?\?���?v
         * @see #copy()
         * @see #copy(EmbeddedGraph)
         * @see #dualCopy()
         * @see #dualCopy(EmbeddedGraph)
         */

        protected synchronized GraphItem getReplica() {

            return this.replica;

        }

    }

    /**
     * �O���t�̒��_��\���Ք�N���X?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * <p/>
     * ���̒��_���芪���ӂ̃��X�g (?����?ACCW) ��?�?��̕� firstEdge
     * <p/>
     * ��ێ?����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * �Ȃ�?A����ӂ��ӃT�C�N����?u?�?��̕�?v��?ݒ肳��Ă���Ƃ���?A
     * <p/>
     * ����͂��̕ӂƒ��_�Ƃ̊֌W�ɂ����ēUʂ̈Ӗ���?�킯�ł͂Ȃ�?A
     * <p/>
     * ���܂���?�?��̂�̂Ƃ��ċL������Ă��邾���ł���?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̕ӂ�?���� (CCW) �̃��X�g��?A���_��?u�ӃT�C�N�� (edge cycle)?v�Ƃ���?B
     * <p/>
     * �܂�?A���_���芪���ʂ�?���� (CCW) �̃��X�g��?u�ʃT�C�N�� (face cycle)?v�Ƃ���?B
     * <p/>
     * </p>
     */

    public class Vertex extends GraphItem {

        /**
         * ���̒��_�̕ӃT�C�N����?�?��̕�?B
         */

        private Edge firstEdge = null;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * <p/>
         * <p/>
         * ?\�z����̒��_�͑��̒��_?^��?^�ʂƂ̊֌W��?���Ȃ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * ���̃R���X�g���N�^��?A�O���t�̗��p�҂ɂ�BĒ�?ڌĂ�?o�����ׂ��ł͂Ȃ�?B
         * <p/>
         * �O���t�̗��p�҂�?A�����܂ł� Euler Operator (����) �𗘗p����?A
         * <p/>
         * �O���t��?삷���̂Ƃ��Ă���?B
         * <p/>
         * </p>
         */

        protected Vertex() {

            EmbeddedGraph.this.
                    super();

        }

        /**
         * ���̒��_��������O���t��Ԃ�?B
         *
         * @return ���̒��_��������O���t
         */

        public EmbeddedGraph getGraph() {

            return EmbeddedGraph.this;

        }

        /**
         * �^����ꂽ�ӂ�?A���̒��_�� firstEdge ��?ݒ肷��?B
         *
         * @param edge firstEdge ��?ݒ肷���
         */

        private void setFirstEdge(Edge edge) {

            firstEdge = edge;

        }

        /**
         * ���̒��_�� firstEdge ��?ݒ肳��Ă���ӂ�Ԃ�?B
         *
         * @return firstEdge ��?ݒ肳��Ă����
         */

        private Edge getFirstEdge() {

            Edge edge = firstEdge;

            if ((edge != null) && (edge.getVertex() != this))

                edge = edge.getMate();

            return edge;

        }

        /**
         * �^����ꂽ�ӂ����̒��_���芪���ӂ̓�̈�ł���Ƃ���?A
         * <p/>
         * ���̒��_�̕ӃT�C�N���ɂ�����?A���̈�O�̕ӂ�Ԃ�?B
         *
         * @param edge ��
         * @return �ӃT�C�N����?A��O�̕�
         */

        private Edge getPrevEdgeInCCW(Edge edge) {

            edge = edge.getMate().getNextEdge();

            if (this.isIdentWith(edge.getVertex()) != true)

                edge = edge.getMate();

            return edge;

        }

        /**
         * �^����ꂽ�ӂ����̒��_���芪���ӂ̓�̈�ł���Ƃ���?A
         * <p/>
         * ���̒��_�̕ӃT�C�N���ɂ�����?A���̈��̕ӂ�Ԃ�?B
         *
         * @param edge ��
         * @return �ӃT�C�N����?A���̕�
         */

        private Edge getNextEdgeInCCW(Edge edge) {

            edge = edge.getPrevEdge();

            if (this.isIdentWith(edge.getVertex()) != true)

                edge = edge.getMate();

            return edge;

        }

        /**
         * �^����ꂽ���_�����̒��_�Ɠ���ł��邩�ۂ���Ԃ�?B
         *
         * @param vrtx ���_
         * @return ����̒��_�Ȃ�� true?A�����łȂ���� false
         */

        public boolean isIdentWith(Vertex vrtx) {

            return (this == vrtx);

        }

        /**
         * ���̒��_�̕ӃT�C�N����\�����X�g��Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * ���ʂƂ��ē����� Vector �̊e�v�f��?A
         * <p/>
         * {@link EmbeddedGraph.Edge EmbeddedGraph.Edge}
         * <p/>
         * �̃C���X�^���X�ł���?B
         * <p/>
         * </p>
         *
         * @return ���̒��_�̕ӃT�C�N����\�����X�g
         */

        public synchronized Vector getEdgeCycleInCCW() {

            Vector result = new Vector();

            Edge firstE = getFirstEdge();

            Edge edge = firstE;

            if (edge == null)

                return result;

            do {

                result.addElement(edge);

                edge = getNextEdgeInCCW(edge);

            } while (edge.isIdentWith(firstE) != true);

            return result;

        }

        /**
         * ���̒��_�̖ʃT�C�N����\�����X�g��Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * ���ʂƂ��ē����� Vector �̊e�v�f��?A
         * <p/>
         * {@link EmbeddedGraph.Face EmbeddedGraph.Face}
         * <p/>
         * �̃C���X�^���X�ł���?B
         * <p/>
         * </p>
         *
         * @return ���̒��_�̖ʃT�C�N����\�����X�g
         */

        public synchronized Vector getFaceCycleInCCW() {

            Vector result = new Vector();

            Edge firstE = getFirstEdge();

            Edge edge = firstE;

            if (edge == null) {

                result.addElement(EmbeddedGraph.this.faceList.elementAt(0));

                return result;

            }

            do {

                if (result.contains(edge.getFace()) != true)

                    result.addElement(edge.getFace());

                edge = getNextEdgeInCCW(edge);

            } while (edge.isIdentWith(firstE) != true);

            return result;

        }

        /**
         * ���̒��_�̕�?��Ƃ���?ݒ肳��Ă��钸�_�̃t�B?[���h��?������l�����?B
         * <p/>
         * <p/>
         * <p/>
         * ���̒��_�� firstEdge �̕�?���
         * <p/>
         * ���̒��_�̕�?��� firstEdge �Ƃ���?B
         * <p/>
         * ���̒��_�� firstEdge �� null ��?�?��ɂ͉��µ�Ȃ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Vertex �̃T�u�N���X��?A
         * <p/>
         * �K�v�ɉ����Ă��̃?�\�b�h��I?[�o?[���C�h���ׂ��ł���?B
         * <p/>
         * �Ȃ�?A���̃I?[�o?[���C�h�����?�\�b�h�̓Ք�ł�?A
         * <p/>
         * super.fillFieldsOfReplica() �Ƃ���?A���̃?�\�b�h��Ă�?o�Ȃ���΂Ȃ�Ȃ�?B
         * <p/>
         * </p>
         *
         * @see EmbeddedGraph.GraphItem#setReplica(EmbeddedGraph.GraphItem)
         * @see EmbeddedGraph.Edge
         */

        protected void fillFieldsOfReplica() {

            if (this.firstEdge != null) {

                Vertex replica = (Vertex) this.getReplica();

                replica.firstEdge = this.firstEdge.getReplica();

            }

        }

        /**
         * ���̒��_�̑o�Ε�?��Ƃ���?ݒ肳��Ă���ʂ̃t�B?[���h��?������l�����?B
         * <p/>
         * <p/>
         * <p/>
         * ���̒��_�� firstEdge �̕�?��̑����
         * <p/>
         * ���̒��_�̑o�Ε�?��� firstEdge �Ƃ���?B
         * <p/>
         * ���̒��_�� firstEdge �� null ��?�?��ɂ͉��µ�Ȃ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Vertex �̃T�u�N���X��?A
         * <p/>
         * �K�v�ɉ����Ă��̃?�\�b�h��I?[�o?[���C�h���ׂ��ł���?B
         * <p/>
         * �Ȃ�?A���̃I?[�o?[���C�h�����?�\�b�h�̓Ք�ł�?A
         * <p/>
         * super.fillFieldsOfDualReplica() �Ƃ���?A���̃?�\�b�h��Ă�?o�Ȃ���΂Ȃ�Ȃ�?B
         * <p/>
         * </p>
         *
         * @see EmbeddedGraph.GraphItem#setReplica(EmbeddedGraph.GraphItem)
         * @see EmbeddedGraph.Face
         * @see EmbeddedGraph.Edge
         */

        protected void fillFieldsOfDualReplica() {

            if (this.firstEdge != null) {

                Face replica = (Face) this.getReplica();

                replica.firstEdge = this.firstEdge.getReplica().getMate();

            }

        }

    }

    /**
     * �O���t�̖ʂ�\���Ք�N���X?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * <p/>
     * ���̖ʂ��芪���ӂ̃��X�g (?����?ACCW) ��?�?��̕� firstEdge
     * <p/>
     * ��ێ?����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * �Ȃ�?A����ӂ�����?u?�?��̕�?v��?ݒ肳��Ă���Ƃ���?A
     * <p/>
     * ����͂��̕ӂƂ��̖ʂƂ̊֌W�ɂ����ēUʂ̈Ӗ���?�킯�ł͂Ȃ�?A
     * <p/>
     * ���܂���?�?��̂�̂Ƃ��ċL������Ă��邾���ł���?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̕ӂ�?���� (CCW) �̃��X�g��?A�ʂ�?u�ӃT�C�N�� (edge cycle)?v�Ƃ���?B
     * <p/>
     * �܂�?A�ʂ��芪�����_��?���� (CCW) �̃��X�g��?u���_�T�C�N�� (vertex cycle)?v�Ƃ���?B
     * <p/>
     * </p>
     */

    public class Face extends GraphItem {

        /**
         * ���̖ʂ̕ӃT�C�N��?�?��̕�?B
         */

        private Edge firstEdge = null;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * <p/>
         * <p/>
         * ?\�z����̖ʂ͑��̒��_?^��?^�ʂƂ̊֌W��?���Ȃ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * ���̃R���X�g���N�^��?A�O���t�̗��p�҂ɂ�BĒ�?ڌĂ�?o�����ׂ��ł͂Ȃ�?B
         * <p/>
         * �O���t�̗��p�҂�?A�����܂ł� Euler Operator (����) �𗘗p����?A
         * <p/>
         * �O���t��?삷���̂Ƃ��Ă���?B
         * <p/>
         * </p>
         */

        protected Face() {

            EmbeddedGraph.this.
                    super();

        }

        /**
         * ���̖ʂ�������O���t��Ԃ�?B
         *
         * @return ���̖ʂ�������O���t
         */

        public EmbeddedGraph getGraph() {

            return EmbeddedGraph.this;

        }

        /**
         * �^����ꂽ�ӂ�?A���̖ʂ� firstEdge ��?ݒ肷��?B
         *
         * @param edge firstEdge ��?ݒ肷���
         */

        private void setFirstEdge(Edge edge) {

            firstEdge = edge;

        }

        /**
         * ���̖ʂ� firstEdge ��?ݒ肳��Ă���ӂ�Ԃ�?B
         *
         * @return firstEdge ��?ݒ肳��Ă����
         */

        private Edge getFirstEdge() {

            Edge edge = firstEdge;

            if ((edge != null) && (edge.getFace() != this))

                edge = edge.getMate();

            return edge;

        }

        /**
         * �^����ꂽ�ʂ����̖ʂƓ���ł��邩�ۂ���Ԃ�?B
         *
         * @param face ��
         * @return ����̖ʂȂ�� true?A�����łȂ���� false
         */

        public boolean isIdentWith(Face face) {

            return (this == face);

        }

        /**
         * ���̖ʂ̕ӃT�C�N����\�����X�g��Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * ���ʂƂ��ē����� Vector �̊e�v�f��?A
         * <p/>
         * {@link EmbeddedGraph.Edge EmbeddedGraph.Edge}
         * <p/>
         * �̃C���X�^���X�ł���?B
         * <p/>
         * </p>
         *
         * @return ���̖ʂ̕ӃT�C�N����\�����X�g
         */

        public synchronized Vector getEdgeCycleInCCW() {

            Vector result = new Vector();

            Edge firstE = getFirstEdge();

            Edge edge = firstE;

            if (edge == null)

                return result;

            do {

                result.addElement(edge);

                edge = edge.getNextEdge();

            } while (edge != firstE);    // do not use isIdentWith()

            return result;

        }

        /**
         * ���̖ʂ̒��_�T�C�N����\�����X�g��Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * ���ʂƂ��ē����� Vector �̊e�v�f��?A
         * <p/>
         * {@link EmbeddedGraph.Vertex EmbeddedGraph.Vertex}
         * <p/>
         * �̃C���X�^���X�ł���?B
         * <p/>
         * </p>
         *
         * @return ���̖ʂ̒��_�T�C�N����\�����X�g
         */

        public synchronized Vector getVertexCycleInCCW() {

            Vector result = new Vector();

            Edge firstE = getFirstEdge();

            Edge edge = firstE;

            if (edge == null) {

                result.addElement(EmbeddedGraph.this.vrtxList.elementAt(0));

                return result;

            }

            do {

                result.addElement(edge.getVertex());

                edge = edge.getNextEdge();

            } while (edge != firstE);    // do not use isIdentWith()

            return result;

        }

        /**
         * ���̖ʂ̕�?��Ƃ���?ݒ肳��Ă���ʂ̃t�B?[���h��?������l�����?B
         * <p/>
         * <p/>
         * <p/>
         * ���̖ʂ� firstEdge �̕�?���
         * <p/>
         * ���̖ʂ̕�?��� firstEdge �Ƃ���?B
         * <p/>
         * ���̖ʂ� firstEdge �� null ��?�?��ɂ͉��µ�Ȃ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Face �̃T�u�N���X��?A
         * <p/>
         * �K�v�ɉ����Ă��̃?�\�b�h��I?[�o?[���C�h���ׂ��ł���?B
         * <p/>
         * �Ȃ�?A���̃I?[�o?[���C�h�����?�\�b�h�̓Ք�ł�?A
         * <p/>
         * super.fillFieldsOfReplica() �Ƃ���?A���̃?�\�b�h��Ă�?o�Ȃ���΂Ȃ�Ȃ�?B
         * <p/>
         * </p>
         *
         * @see EmbeddedGraph.GraphItem#setReplica(EmbeddedGraph.GraphItem)
         * @see EmbeddedGraph.Edge
         */

        protected void fillFieldsOfReplica() {

            if (this.firstEdge != null) {

                Face replica = (Face) this.getReplica();

                replica.firstEdge = this.firstEdge.getReplica();

            }

        }

        /**
         * ���̖ʂ̑o�Ε�?��Ƃ���?ݒ肳��Ă��钸�_�̃t�B?[���h��?������l�����?B
         * <p/>
         * <p/>
         * <p/>
         * ���̖ʂ� firstEdge �̕�?���
         * <p/>
         * ���̖ʂ̑o�Ε�?��� firstEdge �Ƃ���?B
         * <p/>
         * ���̖ʂ� firstEdge �� null ��?�?��ɂ͉��µ�Ȃ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Face �̃T�u�N���X��?A
         * <p/>
         * �K�v�ɉ����Ă��̃?�\�b�h��I?[�o?[���C�h���ׂ��ł���?B
         * <p/>
         * �Ȃ�?A���̃I?[�o?[���C�h�����?�\�b�h�̓Ք�ł�?A
         * <p/>
         * super.fillFieldsOfDualReplica() �Ƃ���?A���̃?�\�b�h��Ă�?o�Ȃ���΂Ȃ�Ȃ�?B
         * <p/>
         * </p>
         *
         * @see EmbeddedGraph.GraphItem#setReplica(EmbeddedGraph.GraphItem)
         * @see EmbeddedGraph.Vertex
         * @see EmbeddedGraph.Edge
         */

        protected void fillFieldsOfDualReplica() {

            if (this.firstEdge != null) {

                Vertex replica = (Vertex) this.getReplica();

                replica.firstEdge = this.firstEdge.getReplica();

            }

        }

    }

    /**
     * �O���t�̔��� (�ӂ̕Њ���) ��\���Ք�N���X?B
     * <p/>
     * <p/>
     * <p/>
     * �����̕ӂ�?A�w��?��킹�ɂȂB���g�̔��� (half edge) �ɂ�B�?\?�������̂�?l����?B
     * <p/>
     * ���̃N���X�͂��̔��ӂ�\������?B
     * <p/>
     * ��?�͂��̔��ӂ�P��?u��?v�ƌĂ�?B
     * <p/>
     * �O���t�̗��p�҂�?A����ӂ�?삷��?�?��ɂ�?A���̔��ӂ�?삷�邱�ƂɂȂ�?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * <p/>
     * <ul>
     * <p/>
     * <li>	���ӂ̎n�_�ƂȂ钸�_ vertex
     * <p/>
     * <li>	���ӂ�?�����?ڂ���� face
     * <p/>
     * <li>	face �ɂ������O�̔��� prevEdge
     * <p/>
     * <li>	face �ɂ�������̔��� nextEdge
     * <p/>
     * <li>	���ӂƋt��Ŕw��?��킹�ɂȂBĂ��锼�� (����) mate
     * <p/>
     * </ul>
     * <p/>
     * ��ێ?����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * �O���t�̒��_?^�ʂ�\���N���X
     * <p/>
     * {@link EmbeddedGraph.Vertex Vertex}/{@link EmbeddedGraph.Face Face}
     * <p/>
     * ��
     * <p/>
     * {@link EmbeddedGraph.GraphItem GraphItem}
     * <p/>
     * �̒�?ڂ̃T�u�N���X�ɂȂBĂ��邪?A
     * <p/>
     * (��) �ӂ�\�����̃N���X�ɂ��Ă�?A
     * <p/>
     * �w��?��킹�̓�̔��ӂ�܂� EmbeddedGraph.EdgeContainer �Ƃ����N���X��
     * <p/>
     * {@link EmbeddedGraph.GraphItem GraphItem}
     * <p/>
     * �̃T�u�N���X�ɂȂBĂ���?B
     * <p/>
     * �Ȃ�?AEmbeddedGraph.EdgeContainer ��
     * <p/>
     * EmbeddedGraph �� private �ȓՔ�N���X�ł���?B
     * <p/>
     * </p>
     */

    public class Edge extends java.lang.Object {

        /*

        * Half Edge Structure

        *

        *       \

        *        \ nextEdge

        *         \

        *          -

        *        ^ |

        *  face  | |

        *        | |

        *          + vertex

        *         /

        *        / prevEdge

        *       /

        *

        */


        /**
         * ���ӂ̎n�_�ƂȂ钸�_?B
         */

        private Vertex vertex;

        /**
         * ���ӂ�?�����?ڂ����?B
         */

        private Face face;

        /**
         * face �ɂ������O�̕�?B
         */

        private Edge prevEdge;

        /**
         * face �ɂ�������̕�?B
         */

        private Edge nextEdge;

        /**
         * ���ӂƋt��Ŕw��?��킹�ɂȂBĂ��锼�� : ����?B
         */

        private Edge mate;

        /**
         * �ӂ̃R���e�i?B
         */

        private EdgeContainer container;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * <p/>
         * <p/>
         * ?\�z����̕ӂ͑��̒��_?^��?^�ʂƂ̊֌W��?���Ȃ�?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * ���̃R���X�g���N�^��?A�O���t�̗��p�҂ɂ�BĒ�?ڌĂ�?o�����ׂ��ł͂Ȃ�?B
         * <p/>
         * �O���t�̗��p�҂�?A�����܂ł� Euler Operator (����) �𗘗p����?A
         * <p/>
         * �O���t��?삷���̂Ƃ��Ă���?B
         * <p/>
         * </p>
         */

        protected Edge() {

            super();

        }

        /**
         * ���̕ӂ�������O���t��Ԃ�?B
         *
         * @return ���̕ӂ�������O���t
         */

        public EmbeddedGraph getGraph() {

            return EmbeddedGraph.this;

        }

        /**
         * �^����ꂽ���_��?A���̕ӂ̎n�_��?ݒ肷��?B
         *
         * @param vertex �n�_��?ݒ肷�钸�_
         */

        private void setVertex(Vertex vertex) {

            this.vertex = vertex;

        }

        /**
         * ���̕ӂ̎n�_��?ݒ肳��Ă��钸�_��Ԃ�?B
         *
         * @return �n�_��?ݒ肳��Ă��钸�_
         */

        private Vertex getVertex() {

            return this.vertex;

        }

        /**
         * �^����ꂽ�ʂ�?A���̕ӂ�?��ʂ�?ݒ肷��?B
         *
         * @param face ?��ʂ�?ݒ肷���
         */

        private void setFace(Face face) {

            this.face = face;

        }

        /**
         * ���̕ӂ�?��ʂ�?ݒ肳��Ă���ʂ�Ԃ�?B
         *
         * @return ?��ʂ�?ݒ肳��Ă����
         */

        private Face getFace() {

            return this.face;

        }

        /**
         * �^����ꂽ�ӂ�?A���̕ӂ̈�O�̕ӂ�?ݒ肷��?B
         *
         * @param edge ��O�̕ӂ�?ݒ肷���
         */

        private void setPrevEdge(Edge edge) {

            this.prevEdge = edge;

        }

        /**
         * ���̕ӂ̈�O�̕ӂ�?ݒ肳��Ă���ӂ�Ԃ�?B
         *
         * @return ��O�̕ӂ�?ݒ肳��Ă����
         */

        private Edge getPrevEdge() {

            Edge prev = this.prevEdge;

            if (prev.vertex == this.vertex)

                prev = prev.mate;

            return prev;

        }

        /**
         * �^����ꂽ�ӂ�?A���̕ӂ̈��̕ӂ�?ݒ肷��?B
         *
         * @param edge ���̕ӂ�?ݒ肷���
         */

        private void setNextEdge(Edge edge) {

            this.nextEdge = edge;

        }

        /**
         * ���̕ӂ̈��̕ӂ�?ݒ肳��Ă���ӂ�Ԃ�?B
         *
         * @return ���̕ӂ�?ݒ肳��Ă����
         */

        private Edge getNextEdge() {

            Edge next = this.nextEdge;

            if (next.vertex != mate.vertex)

                next = next.mate;

            return next;

        }

        /**
         * �^����ꂽ�ӂ�?A���̕ӂ̑����?ݒ肷��?B
         *
         * @param edge ����Ƃ���?ݒ肷���
         */

        private void setMate(Edge edge) {

            this.mate = edge;

        }

        /**
         * ���̕ӂ̑����?ݒ肳��Ă���ӂ�Ԃ�?B
         *
         * @return ����Ƃ���?ݒ肳��Ă����
         */

        public Edge getMate() {

            return this.mate;

        }

        /**
         * �^����ꂽ?u�ӂ̃R���e�i?v��?A���̕ӂ̃R���e�i��?ݒ肷��?B
         *
         * @param container �ӂ̃R���e�i
         */

        private void setContainer(EdgeContainer container) {

            this.container = container;

        }

        /**
         * ���̕ӂ̃R���e�i��Ԃ�?B
         *
         * @return �ӂ̃R���e�i
         */

        private EdgeContainer getContainer() {

            return this.container;

        }

        /**
         * �^����ꂽ�ӂ�?A���̕ӂƓ���̕ӂ��ۂ���Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * edge �� this �� this �̑���ł���� true ��Ԃ�?B
         * <p/>
         * </p>
         *
         * @param edge ��
         * @return ����̕ӂȂ�� true?A�����łȂ���� false
         */

        public boolean isIdentWith(Edge edge) {

            return (this.container == edge.container);

        }

        /**
         * ���̕ӂ̎n�_?^?I�_��Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
         * <p/>
         * ?�?��̗v�f�ɂ͎n�_?A
         * <p/>
         * ��Ԗڂ̗v�f�ɂ�?I�_
         * <p/>
         * �����?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * ?I�_�Ƃ�?A����̎n�_�ł���?B
         * <p/>
         * </p>
         *
         * @return �n�_?^?I�_��܂ޔz��
         */

        public synchronized Vertex[] getVertices() {

            Vertex[] result = new Vertex[2];

            result[0] = getVertex();

            result[1] = getMate().getVertex();

            return result;

        }

        /**
         * ���̕ӂ̎n�_��Ԃ�?B
         *
         * @return �n�_
         */

        public synchronized Vertex getStartingVertex() {

            return getVertex();

        }

        /**
         * ���̕ӂ�?I�_��Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * ?I�_�Ƃ�?A����̎n�_�ł���?B
         * <p/>
         * </p>
         *
         * @return ?I�_
         */

        public synchronized Vertex getTerminalVertex() {

            return getMate().getVertex();

        }

        /**
         * ���̕ӂ�?���?^�E�ʂ�Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
         * <p/>
         * ?�?��̗v�f�ɂ�?���?A
         * <p/>
         * ��Ԗڂ̗v�f�ɂ͉E��
         * <p/>
         * �����?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * �E�ʂƂ�?A�����?��ʂł���?B
         * <p/>
         * </p>
         *
         * @return ?���?^�E�ʂ�܂ޔz��
         */

        public synchronized Face[] getFaces() {

            Face[] result = new Face[2];

            result[0] = getFace();

            result[1] = getMate().getFace();

            return result;

        }

        /**
         * ���̕ӂ�?��ʂ�Ԃ�?B
         *
         * @return ?���
         */

        public synchronized Face getLeftFace() {

            return getFace();

        }

        /**
         * ���̕ӂ̉E�ʂ�Ԃ�?B
         * <p/>
         * <p/>
         * <p/>
         * �E�ʂƂ�?A�����?��ʂł���?B
         * <p/>
         * </p>
         *
         * @return �E��
         */

        public synchronized Face getRightFace() {

            return getMate().getFace();

        }

        /**
         * ���p�҂����̕ӂɊ֘A�t�������C�ӂ̃f?[�^�� userData ��?ݒ肷��?B
         * <p/>
         * <p/>
         * <p/>
         * ���̌Ă�?o���ȑO�� userData ��?ݒ肳��Ă����f?[�^�͖Y�ꋎ����?B
         * <p/>
         * �Ȃ�?A���̕ӂƂ��̕ӂ̑���͓��� userData �뤗L����?B
         * <p/>
         * </p>
         * <p/>
         * <p/>
         * <p/>
         * object �� null �ł�?\��Ȃ�?B
         * <p/>
         * ����?�?�?AuserData �ɂ� null ��?ݒ肳���?B
         * <p/>
         * </p>
         *
         * @param object �C�ӂ̃f?[�^ (�C�ӂ̃I�u�W�F�N�g)
         * @see #getUserData()
         * @see EmbeddedGraph.GraphItem
         */

        public synchronized void setUserData(java.lang.Object object) {

            container.setUserData(object);

        }

        /**
         * ���̕ӂ� userData ��Ԃ�?B
         *
         * @return ���̕ӂɊ֘A�t�����Ă���C�ӂ̃I�u�W�F�N�g
         * @see #setUserData(java.lang.Object)
         * @see EmbeddedGraph.GraphItem
         */

        public synchronized java.lang.Object getUserData() {

            return container.getUserData();

        }

        /*

        * ���̕ӂ�?A��?g��܂ރR���e�i�̔��� A �ł��邩�ۂ���Ԃ�?B

        *

        * @return �R���e�i�̔��� A �ł���� true, ���� Z �ł���� false

        * @see	EmbeddedGraph.EdgeContainer

        */

        private boolean isHalfA() {

            return (this == container.getHalfA());

        }

        /**
         * ���� (��) �ӂ̕�?��Ƃ���?ݒ肳��Ă��锼�ӂ�Ԃ�?B
         *
         * @return ��?��Ƃ���?ݒ肳��Ă��锼��
         * @see #copy()
         * @see #copy(EmbeddedGraph)
         * @see #dualCopy()
         * @see #dualCopy(EmbeddedGraph)
         */

        protected Edge getReplica() {

            EdgeContainer replica = (EdgeContainer) container.getReplica();

            if (replica == null)

                return null;

            return (this.isHalfA()) ? replica.getHalfA() : replica.getHalfZ();

        }

        /**
         * ���� (��) �ӂ̕�?��Ƃ���?ݒ肳��Ă��锼�ӂ̃t�B?[���h��?������l�����?B
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Edge �̃T�u�N���X��?A
         * <p/>
         * �K�v�ɉ����Ă��̃?�\�b�h��I?[�o?[���C�h���ׂ��ł���?B
         * <p/>
         * �Ȃ�?A���̃I?[�o?[���C�h�����?�\�b�h�̓Ք�ł�?A
         * <p/>
         * super.fillFieldsOfReplica() �Ƃ���?A���̃?�\�b�h��Ă�?o�Ȃ���΂Ȃ�Ȃ�?B
         * <p/>
         * </p>
         *
         * @see EmbeddedGraph.GraphItem#setReplica(EmbeddedGraph.GraphItem)
         */

        protected void fillFieldsOfReplica() {

            Edge replica = this.getReplica();

            replica.vertex = (Vertex) this.vertex.getReplica();

            replica.face = (Face) this.face.getReplica();

            replica.prevEdge = this.prevEdge.getReplica();

            replica.nextEdge = this.nextEdge.getReplica();

        }

        /**
         * ���� (��) �ӂ̑o�Ε�?��Ƃ���?ݒ肳��Ă��锼�ӂ̃t�B?[���h��?������l�����?B
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Edge �̃T�u�N���X��?A
         * <p/>
         * �K�v�ɉ����Ă��̃?�\�b�h��I?[�o?[���C�h���ׂ��ł���?B
         * <p/>
         * �Ȃ�?A���̃I?[�o?[���C�h�����?�\�b�h�̓Ք�ł�?A
         * <p/>
         * super.fillFieldsOfDualReplica() �Ƃ���?A���̃?�\�b�h��Ă�?o�Ȃ���΂Ȃ�Ȃ�?B
         * <p/>
         * </p>
         *
         * @see EmbeddedGraph.GraphItem#setReplica(EmbeddedGraph.GraphItem)
         */

        protected void fillFieldsOfDualReplica() {

            Edge replica = this.getReplica();

            replica.vertex = (Vertex) this.face.getReplica();

            replica.face = (Face) this.mate.vertex.getReplica();

        }

    }

    /**
     * �w��?��킹�̓�̔��ӂ̃R���e�i��\���Ք�N���X?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃N���X�͊O���ɂ͌����Ȃ�?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̃N���X�̃C���X�^���X��?A
     * <p/>
     * <ul>
     * <p/>
     * <li>	����ӂ̈��̕Њ���ł���?u���� A?v halfA
     * <p/>
     * <li>	����ӂ̑���̕Њ���ł���?u���� Z?v halfZ
     * <p/>
     * </ul>
     * <p/>
     * ��ێ?����?B
     * <p/>
     * </p>
     */

    private class EdgeContainer extends GraphItem {

        /**
         * ����ӂ̈��̕Њ���ł��锼�� A ?B
         */

        private Edge halfA;

        /**
         * ����ӂ̑���̕Њ���ł��锼�� Z ?B
         */

        private Edge halfZ;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         * <p/>
         * <p/>
         * <p/>
         * ���̃R���X�g���N�^�̓Ք�ł�?A
         * <p/>
         * �O���t��?ݒ肳��Ă��� graphItemMaker �� newEdge() ��g�B�?A
         * <p/>
         * ��̔��ӂ�?�?�����?B
         * <p/>
         * </p>
         *
         * @see EmbeddedGraph#graphItemMaker
         * @see EmbeddedGraph.GraphItemMaker#newEdge()
         */

        private EdgeContainer() {

            EmbeddedGraph.this.
                    super();

            halfA = graphItemMaker.newEdge();

            halfZ = graphItemMaker.newEdge();

            halfA.setMate(halfZ);

            halfA.setContainer(this);

            halfZ.setMate(halfA);

            halfZ.setContainer(this);

        }

        /**
         * ���̃R���e�i�� halfA ��Ԃ�?B
         *
         * @return ���� A
         */

        private Edge getHalfA() {

            return halfA;

        }

        /**
         * ���̃R���e�i�� halfZ ��Ԃ�?B
         *
         * @return ���� Z
         */

        private Edge getHalfZ() {

            return halfZ;

        }

        /**
         * ���̃R���e�i���܂ޔ��ӂ̕�?��Ƃ���?ݒ肳��Ă��锼�ӂ̃t�B?[���h��?������l�����?B
         *
         * @see EmbeddedGraph.Edge#fillFieldsOfReplica()
         */

        private void fillFieldsOfReplica() {

            halfA.fillFieldsOfReplica();

            halfZ.fillFieldsOfReplica();

        }

        /**
         * ���̃R���e�i���܂ޔ��ӂ̑o�Ε�?��Ƃ���?ݒ肳��Ă��锼�ӂ̃t�B?[���h��?������l�����?B
         *
         * @see EmbeddedGraph.Edge#fillFieldsOfDualReplica()
         */

        private void fillFieldsOfDualReplica() {

            halfA.fillFieldsOfDualReplica();

            halfZ.fillFieldsOfDualReplica();

        }

    }

    /**
     * �O���t��ł�?V���Ȓ��_?^��?^�ʂ�?�?���S������I�u�W�F�N�g�������ׂ��C���^?[�t�F�C�X?B
     */

    public interface GraphItemMaker {

        /**
         * ?V���Ȓ��_��?�?�����?B
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Vertex
         * <p/>
         * ���邢�͂��̃T�u�N���X�̃R���X�g���N�^��?\�z�������_��Ԃ��?�\�b�h?B
         * <p/>
         * </p>
         *
         * @return ?V����?�?��������_
         */

        public abstract Vertex newVertex();

        /**
         * ?V���Ȗʂ�?�?�����?B
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Face
         * <p/>
         * ���邢�͂��̃T�u�N���X�̃R���X�g���N�^��?\�z�����ʂ�Ԃ��?�\�b�h?B
         * <p/>
         * </p>
         *
         * @return ?V����?�?�������
         */

        public abstract Face newFace();

        /**
         * ?V���ȕӂ�?�?�����?B
         * <p/>
         * <p/>
         * <p/>
         * EmbeddedGraph.Edge
         * <p/>
         * ���邢�͂��̃T�u�N���X�̃R���X�g���N�^��?\�z�����ӂ�Ԃ��?�\�b�h?B
         * <p/>
         * </p>
         *
         * @return ?V����?�?�������
         */

        public abstract Edge newEdge();

    }

    /**
     * ?V���Ȓ��_?^��?^�ʂ�?�?���S������I�u�W�F�N�g?B
     */

    private GraphItemMaker graphItemMaker;

    /**
     * ���_�̃��X�g?B
     */

    private Vector vrtxList;

    /**
     * �� (�̃R���e�i) �̃��X�g?B
     */

    private Vector edgeList;

    /**
     * �ʂ̃��X�g?B
     */

    private Vector faceList;

    /**
     * �^����ꂽ�I�u�W�F�N�g��?A
     * <p/>
     * ���̃O���t��?V���Ȓ��_?^��?^�ʂ�?�?���S������I�u�W�F�N�g�Ƃ���
     * <p/>
     * ?ݒ肷��?B
     *
     * @param ?V���Ȓ��_?^��?^�ʂ�?�?���S������I�u�W�F�N�g
     *
     */

    protected void setGraphItemMaker(GraphItemMaker maker) {

        graphItemMaker = maker;

    }

    /**
     * ���̃O���t���܂ޒ��_��?���Ԃ�?B
     *
     * @return ���_��?�
     */

    public synchronized int getNumberOfVertices() {

        return vrtxList.size();

    }

    /**
     * ���̃O���t���܂ޕӂ�?���Ԃ�?B
     *
     * @return �ӂ�?�
     */

    public synchronized int getNumberOfEdges() {

        return edgeList.size();

    }

    /**
     * ���̃O���t���܂ޖʂ�?���Ԃ�?B
     *
     * @return �ʂ�?�
     */

    public synchronized int getNumberOfFaces() {

        return faceList.size();

    }

    /**
     * ���̃O���t���܂ޒ��_�� Enumeration ��Ԃ�?B
     *
     * @return ���_�� Enumeration
     */

    public synchronized Enumeration vertexElements() {

        return vrtxList.elements();

    }

    /**
     * ���̃O���t���܂ޕӂ� Enumeration ��Ԃ�?B
     *
     * @return �ӂ� Enumeration
     */

    public synchronized Enumeration edgeElements() {

        return new Enumeration() {

            Enumeration e = edgeList.elements();

            public boolean hasMoreElements() {

                return e.hasMoreElements();

            }

            public java.lang.Object nextElement() {

                EdgeContainer edgeContainer = (EdgeContainer) e.nextElement();

                return edgeContainer.getHalfA();

            }

        };

    }

    /**
     * ���̃O���t���܂ޖʂ� Enumeration ��Ԃ�?B
     *
     * @return �ʂ� Enumeration
     */

    public synchronized Enumeration faceElements() {

        return faceList.elements();

    }

    /**
     * ���̃O���t��?V���Ȓ��_��?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h�ɂ�B�?�?���������̒��_�͑��̒��_?^��?^�ʂƂ̊֌W��?���Ȃ�?B
     * <p/>
     * </p>
     *
     * @return ?V����?�?����ꂽ���_
     * @see EmbeddedGraph.GraphItemMaker#newVertex()
     */

    private Vertex addVertex() {

        Vertex vrtx = graphItemMaker.newVertex();

        vrtxList.addElement(vrtx);

        return vrtx;

    }

    /**
     * ���̃O���t��?V���Ȗʂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h�ɂ�B�?�?���������̖ʂ͑��̒��_?^��?^�ʂƂ̊֌W��?���Ȃ�?B
     * <p/>
     * </p>
     *
     * @return ?V����?�?����ꂽ��
     * @see EmbeddedGraph.GraphItemMaker#newFace()
     */

    private Face addFace() {

        Face face = graphItemMaker.newFace();

        faceList.addElement(face);

        return face;

    }

    /**
     * ���̃O���t��?V���ȕӂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h�ɂ�B�?�?���������̕ӂ͑��̒��_?^��?^�ʂƂ̊֌W��?���Ȃ�?B
     * <p/>
     * </p>
     *
     * @return ?V����?�?����ꂽ��
     * @see EmbeddedGraph.EdgeContainer#EmbeddedGraph.EdgeContainer(EmbeddedGraph)
     * @see EmbeddedGraph.EdgeContainer#getHalfA()
     */

    private Edge addEdge() {

        EdgeContainer edgeContainer = this.new EdgeContainer();

        edgeList.addElement(edgeContainer);

        return edgeContainer.getHalfA();

    }

    /**
     * �^����ꂽ���_��?A���̃O���t����?�?�����?B
     *
     * @param vrtx ?�?����钸�_
     */

    private void removeVertex(Vertex vrtx) {

        vrtxList.removeElement(vrtx);

    }

    /**
     * �^����ꂽ�ʂ�?A���̃O���t����?�?�����?B
     *
     * @param face ?�?������
     */

    private void removeFace(Face face) {

        faceList.removeElement(face);

    }

    /**
     * �^����ꂽ���ӂ�܂ރR���e�i��?A���̃O���t����?�?�����?B
     *
     * @param edge ?�?������
     */

    private void removeEdge(Edge edge) {

        edgeList.removeElement(edge.getContainer());

    }

    /**
     * ���̃O���t��?A�^����ꂽ���_��܂ނ��ۂ���Ԃ�?B
     *
     * @param vrtx ��?����钸�_
     * @param vrtx ���O���t�Ɋ܂܂�Ă���� true?A�����łȂ���� false
     */

    public boolean contains(Vertex vrtx) {

        if (((vrtx != null) && (vrtx.getGraph() == this)) != true) {

            return false;

        }

        return vrtxList.contains(vrtx);

    }

    /**
     * ���̃O���t��?A�^����ꂽ�ʂ�܂ނ��ۂ���Ԃ�?B
     *
     * @param face ��?������
     * @param face ���O���t�Ɋ܂܂�Ă���� true?A�����łȂ���� false
     */

    public boolean contains(Face face) {

        if (((face != null) && (face.getGraph() == this)) != true) {

            return false;

        }

        return faceList.contains(face);

    }

    /**
     * ���̃O���t��?A�^����ꂽ�ӂ�܂ނ��ۂ���Ԃ�?B
     *
     * @param edge ��?������
     * @param edge ���O���t�Ɋ܂܂�Ă���� true?A�����łȂ���� false
     */

    public boolean contains(Edge edge) {

        if (((edge != null) && (edge.getGraph() == this)) != true) {

            return false;

        }

        return edgeList.contains(edge.getContainer());

    }

    /**
     * ����^������?A��̖���?��݃O���t�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃R���X�g���N�^�ł�?A
     * <p/>
     * ?\�z����C���X�^���X�� graphItemMaker �Ƃ���?A�P��
     * <p/>
     * {@link EmbeddedGraph.Vertex EmbeddedGraph.Vertex}
     * <p/>
     * /
     * <p/>
     * {@link EmbeddedGraph.Face EmbeddedGraph.Face}
     * <p/>
     * /
     * <p/>
     * {@link EmbeddedGraph.Edge EmbeddedGraph.Edge}
     * <p/>
     * �̃C���X�^���X��?�?�����I�u�W�F�N�g��?ݒ肷��?B
     * <p/>
     * </p>
     */

    public EmbeddedGraph() {

        graphItemMaker = new GraphItemMaker() {

            public Vertex newVertex() {
                return EmbeddedGraph.this.new Vertex();
            }

            public Face newFace() {
                return EmbeddedGraph.this.new Face();
            }

            public Edge newEdge() {
                return EmbeddedGraph.this.new Edge();
            }

        };

        vrtxList = new Vector();

        edgeList = new Vector();

        faceList = new Vector();

    }

    /**
     * graphItemMaker ��^����?A��̖���?��݃O���t�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param maker ?V���Ȓ��_?^��?^�ʂ�?�?���S������I�u�W�F�N�g
     */

    public EmbeddedGraph(GraphItemMaker maker) {

        graphItemMaker = maker;

        vrtxList = new Vector();

        edgeList = new Vector();

        faceList = new Vector();

    }

    /**
     * EmbeddedGraph ����� Euler Operator �̊e make �?�\�b�h�ɂ�����
     * <p/>
     * ?V����?�?����ꂽ���_?^��?^�ʂ��Ք�N���X?B
     */

    public class Result extends java.lang.Object {

        /**
         * ?V����?�?����ꂽ���_?B
         * <p/>
         * <p/>
         * <p/>
         * ���_��?�?�����Ȃ��?�\�b�h��?�?��ɂ� null �������?B
         * <p/>
         * </p>
         */

        public Vertex vrtx;

        /**
         * ?V����?�?����ꂽ��?B
         * <p/>
         * <p/>
         * <p/>
         * �ӂ�?�?�����Ȃ��?�\�b�h��?�?��ɂ� null �������?B
         * <p/>
         * </p>
         */

        public Edge edge;

        /**
         * ?V����?�?����ꂽ��?B
         * <p/>
         * <p/>
         * <p/>
         * �ʂ�?�?�����Ȃ��?�\�b�h��?�?��ɂ� null �������?B
         * <p/>
         * </p>
         */

        public Face face;

        /**
         * �e�t�B?[���h��?ݒ肷��l��^����?A�I�u�W�F�N�g��?\�z����?B
         * <p/>
         * <p/>
         * <p/>
         * vrtx, edge, face �̂��ꂼ��� null �ł�?\��Ȃ�?B
         * <p/>
         * </p>
         *
         * @param vrtx ?V����?�?����ꂽ���_
         * @param edge ?V����?�?����ꂽ��
         * @param face ?V����?�?����ꂽ��
         */

        private Result(Vertex vrtx, Edge edge, Face face) {

            super();

            this.vrtx = vrtx;

            this.edge = edge;

            this.face = face;

        }

    }

    /**
     * ���� (���) �O���t�ɒ��_�Ɩʂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃O���t����łȂ�?�?��ɂ�?A
     * <p/>
     * FatalException �̗�O��?�����?B
     * <p/>
     * </p>
     *
     * @return ?V����?�?����ꂽ���_�Ɩ�
     * @see FatalException
     * @see #killVertexFace()
     */

    public synchronized Result makeVertexFace() {

        if ((vrtxList.size() != 0) ||

                (edgeList.size() != 0) ||

                (faceList.size() != 0)) {

            // Illegal State

            throw new FatalException("The graph is not empty.");

        }

        Vertex newV = addVertex();

        Face newF = addFace();

        newV.setFirstEdge(null);

        newF.setFirstEdge(null);

        return this.new Result(newV, null, newF);

    }

    /**
     * ���� (���_���Ɩʂ������?��) �O���t����?A���̒��_�Ɩʂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃O���t�����_���Ɩʂ������?��̂łȂ�?�?��ɂ�?A
     * <p/>
     * FatalException �̗�O��?�����?B
     * <p/>
     * </p>
     *
     * @see FatalException
     * @see #makeVertexFace()
     */

    public synchronized void killVertexFace() {

        if ((vrtxList.size() != 1) ||

                (edgeList.size() != 0) ||

                (faceList.size() != 1)) {

            // Illegal State

            throw new FatalException("The graph does not have only 1 vertex & 1 face.");

        }

        removeVertex((Vertex) (vrtxList.elementAt(0)));

        removeFace((Face) (faceList.elementAt(0)));

    }

    /**
     * ���̃O���t�ɕӂƒ��_��?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ���_����?V���ȕӂ�?L�΂�?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * face ���邢�� vrtx ��?A���̃O���t�Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * vrtx �� face �̒��_�T�C�N���Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     *
     * @param face ���̓Ք��?V���ȕӂ�?L�΂���
     * @param vrtx ?V���ȕӂ̎n�_�ƂȂ钸�_
     * @return ?V����?�?����ꂽ�ӂƒ��_
     * @see InvalidArgumentValueException
     * @see #killEdgeVertex(EmbeddedGraph.Edge)
     */

    public synchronized Result makeEdgeVertex(Face face,

                                              Vertex vrtx) {

        if (contains(face) != true) {

            throw new InvalidArgumentValueException("Given face is not in the graph.");

        }

        if (contains(vrtx) != true) {

            throw new InvalidArgumentValueException("Given vertex is not in the graph.");

        }

        /*

        * determine the location of 'vrtx' in 'face's edge cycle

        */

        Edge firstE = face.getFirstEdge();    // first edge of 'face'

        Edge nextE = firstE;        // an edge that vertex is 'vrtx'

        Edge prevE = null;            // prev. of 'nextE'

        if (nextE != null) {

            while (nextE.getVertex() != vrtx) {

                nextE = nextE.getNextEdge();

                if (nextE == firstE)

                    throw new InvalidArgumentValueException("Given vertex is not in given face.");

            }

            prevE = nextE.getPrevEdge();

        }

        /*

        * make new edge and vertex

        *

        *			   newV +

        *			       ^ |

        *			  newE | | newM

        *			       | V

        *    vrtx +	  ====>	   vrtx +

        *        ^ \		       ^ \

        * prevE /   \ nextE	prevE /   \ nextE

        *      /     V		     /     V

        */

        Vertex newV = addVertex();

        Edge newE = addEdge();

        Edge newM = newE.getMate();

        newV.setFirstEdge(newM);

        newE.setVertex(vrtx);

        newE.setFace(face);

        newE.setPrevEdge((prevE != null) ? prevE : newM);

        newE.setNextEdge(newM);

        newM.setVertex(newV);

        newM.setFace(face);

        newM.setPrevEdge(newE);

        newM.setNextEdge((nextE != null) ? nextE : newE);

        if (nextE != null) {

            prevE.setNextEdge(newE);

            nextE.setPrevEdge(newM);

        } else {

            face.setFirstEdge(newE);

            vrtx.setFirstEdge(newE);

        }

        return this.new Result(newV, newE, null);

    }

    /**
     * ���̃O���t����ӂƒ��_��?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * �Ԃ牺���B��ӂ�?�?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * edge ��?A���̃O���t�Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * edge ���Ԃ牺���B��ӂłȂ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * �Ȃ�?A�������̕ӂ��q���BĂԂ牺���BĂ���悤��?󋵂ɂ����Ă�?A
     * <p/>
     * ����?�?�[�ȊO�̕ӂ�?u�Ԃ牺���BĂ���?v�Ƃ݂͂Ȃ��Ȃ�?B
     * <p/>
     * </p>
     *
     * @param edge ?�?������
     * @see InvalidArgumentValueException
     * @see #makeEdgeVertex(EmbeddedGraph.Face,EmbeddedGraph.Vertex)
     */

    public synchronized void killEdgeVertex(Edge edge) {

        if (contains(edge) != true) {

            throw new InvalidArgumentValueException("Given edge is not in the graph.");

        }

        Edge mate = edge.getMate();    // mate

        if (edge.getFace() != mate.getFace()) {

            throw new InvalidArgumentValueException("Given edge is not dangling.");

        }

        /*  */
        if ((edge.getNextEdge() == mate) && (mate.getPrevEdge() == edge)) {

            ;    // nop

        } else if ((edge.getPrevEdge() == mate) && (mate.getNextEdge() == edge)) {

            edge = mate;

            mate = edge.getMate();

        } else {

            throw new InvalidArgumentValueException("Given edge is not dangling.");

        }

        Face relatedF = edge.getFace();

        Vertex remainedV = edge.getVertex();

        Vertex removedV = mate.getVertex();

        Edge prevE = edge.getPrevEdge();

        Edge nextE = mate.getNextEdge();

        prevE.setNextEdge(nextE);

        nextE.setPrevEdge(prevE);

        if (edge.isIdentWith(nextE) != true) {

            if (edge.isIdentWith(remainedV.getFirstEdge()) == true)

                remainedV.setFirstEdge(nextE);

            if (edge.isIdentWith(relatedF.getFirstEdge()) == true)

                relatedF.setFirstEdge(nextE);

        } else {

            remainedV.setFirstEdge(null);

            relatedF.setFirstEdge(null);

        }

        removeVertex(removedV);

        removeEdge(edge);

    }

    /**
     * ���̃O���t�ɒ��_�ƕӂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * �ӂ��ɕ�������?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * edge ��?A���̃O���t�Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     *
     * @param edge ���������
     * @return ?V����?�?����ꂽ���_�ƕ�
     * @see InvalidArgumentValueException
     * @see #killVertexEdge(EmbeddedGraph.Vertex,EmbeddedGraph.Edge)
     */

    public synchronized Result makeVertexEdge(Edge edge) {

        if (contains(edge) != true) {

            throw new InvalidArgumentValueException("Given edge is not in the graph.");

        }

        Vertex newV = addVertex();

        Edge newE = addEdge();

        Edge newM = newE.getMate();

        Edge mate = edge.getMate();

        Edge nextE = edge.getNextEdge();

        Edge prevE = mate.getPrevEdge();

        /*

        *      ^      /	      ^      /

        * nextE \    / prevE	 nextE \    / prevE

        *        \  V		        \  V

        *          +		          +

        *         ^ |		         ^ |

        *         | |   	    newE | | newM

        *         | |	  ====>	         | V

        *         | |		     newV +

        *         | |		         ^ |

        *    edge | | mate	    edge | | mate

        *         | V		         | V

        *          +		          +

        */


        newE.setVertex(newV);

        newE.setFace(edge.getFace());

        newE.setPrevEdge(edge);

        newE.setNextEdge(nextE);

        newM.setVertex(mate.getVertex());

        newM.setFace(mate.getFace());

        newM.setPrevEdge(prevE);

        newM.setNextEdge(mate);

        edge.setNextEdge(newE);

        mate.setVertex(newV);

        mate.setPrevEdge(newM);

        nextE.setPrevEdge(newE);

        prevE.setNextEdge(newM);

        newV.setFirstEdge(newE);

        newM.getVertex().setFirstEdge(newM);

        return this.new Result(newV, newE, null);

    }

    /**
     * ���̃O���t���璸�_�ƕӂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ��̕ӂ싂Ԓ��_��?�?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * vrtx ���邢�� edge ��?A���̃O���t�Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * edge �� vrtx �Ɍq���BĂ��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * vrtx �Ɍq���BĂ���ӂ�?��� 2 �ȊO��?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     *
     * @param vrtx ?�?����钸�_
     * @param edge ?�?������
     * @see InvalidArgumentValueException
     * @see #makeVertexEdge(EmbeddedGraph.Edge)
     */

    public synchronized void killVertexEdge(Vertex vrtx,

                                            Edge edge) {

        if (contains(vrtx) != true) {

            throw new InvalidArgumentValueException("Given vertex is not in the graph.");

        }

        if (contains(edge) != true) {

            throw new InvalidArgumentValueException("Given edge is not in the graph.");

        }

        /*

        * target vertex should have 2 edges

        */

        Edge firstE = vrtx.getFirstEdge();

        if (firstE == null) {

            throw new InvalidArgumentValueException("No edge is attached to given vertex.");

        }

        Edge secondE = vrtx.getNextEdgeInCCW(firstE);

        if (secondE == null) {

            throw new InvalidArgumentValueException("Only 1 edge is attached to given vertex.");

        }

        Edge thirdE = vrtx.getNextEdgeInCCW(secondE);

        if (thirdE.isIdentWith(firstE) != true) {

            throw new InvalidArgumentValueException("3 or more edges are attached to given vertex.");

        }

        /*

        * determine the relations between edges

        */

        Edge edge1;

        Edge mate1;

        Edge edge2;

        Edge mate2;

        if (edge.isIdentWith(firstE) == true) {

            edge1 = firstE;

            mate2 = secondE;

        } else if (edge.isIdentWith(secondE) == true) {

            edge1 = secondE;

            mate2 = firstE;

        } else {

            throw new InvalidArgumentValueException("Given edge is not attached to given vertex.");

        }

        mate1 = edge1.getMate();

        edge2 = mate2.getMate();

        Edge nextE = edge1.getNextEdge();

        Edge prevE = mate1.getPrevEdge();

        Vertex vrtx1 = vrtx;

        Vertex vrtx2 = mate1.getVertex();

        /*

        *     ^      /	          ^      /

        *   ne \    / pm	ne \    / pm

        *       \  V		    \  V

        *  vrtx2  +		vrtx2 +

        *        ^ |		     ^ |

        *  edge1 | | mate1	     | |

        *        | V	  ====>	     | |

        *  vrtx1  +		     | |

        *        ^ |		     | |

        *  edge2 | | mate2    edge2 | | mate2

        *        | V		     | V

        *         +		      +

        */

        if (nextE != mate1) {

            edge2.setNextEdge(nextE);

            nextE.setPrevEdge(edge2);

        } else {

            edge2.setNextEdge(mate2);

        }

        if (prevE != edge1) {

            mate2.setPrevEdge(prevE);

            prevE.setNextEdge(mate2);

        } else {

            mate2.setPrevEdge(edge2);

        }

        mate2.setVertex(vrtx2);

        if (edge1.isIdentWith(vrtx2.getFirstEdge()) == true)

            vrtx2.setFirstEdge(mate2);

        removeVertex(vrtx1);

        removeEdge(edge1);

    }

    /**
     * ���̃O���t�ɕӂƖʂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * �ʂ��ɕ�������?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * �w�肵�� face ��?��?V����?�?������ӂ�?��ʂƂ��Ďc��?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h��?A�^����ꂽ���_?^��?^�ʂ̊Ԃ�?�?�?���`�F�b�N���Ȃ�?B
     * <p/>
     * </p>
     *
     * @param face     ���������
     * @param headVrtx ?V����?�?������ӂ̎n�_�ƂȂ钸�_
     * @param nextHead ?V����?�?������ӂ̉E�ʂ̎��ɂȂ��
     * @param prevHead ?V����?�?������ӂ�?��ʂ̑O�ɂȂ��
     * @param tailVrtx ?V����?�?������ӂ�?I�_�ƂȂ钸�_
     * @param nextTail ?V����?�?������ӂ�?��ʂ̎��ɂȂ��
     * @param prevTail ?V����?�?������ӂ̉E�ʂ̑O�ɂȂ��
     * @return ?V����?�?����ꂽ�ӂƖ�
     * @see #makeEdgeFace(EmbeddedGraph.Face,EmbeddedGraph.Vertex,EmbeddedGraph.Vertex)
     * @see #makeEdgeFace(EmbeddedGraph.Face,EmbeddedGraph.Vertex,EmbeddedGraph.Vertex,EmbeddedGraph.Edge,EmbeddedGraph.Edge)
     */

    private Result makeEdgeFace(Face face,

                                Vertex headVrtx,

                                Edge nextHead,

                                Edge prevHead,

                                Vertex tailVrtx,

                                Edge nextTail,

                                Edge prevTail) {

        Face newF = addFace();

        Edge newE = addEdge();

        Edge newM = newE.getMate();

        /*

        *          tailV                         tailV

        *     nextT    prevT		     nextT    prevT

        *    <----- + <-----		    <----- + <-----

        * 					  ^ |  newF

        *    			   ====>      newE| |newM

        *    face			    face  | V

        *    -----> + ----->		    -----> + ----->

        *    prevH    nextH		    prevH    nextH

        *         headV                         headV

        */


        face.setFirstEdge(newE);

        newF.setFirstEdge(newM);

        newE.setVertex(headVrtx);

        newE.setFace(face);

        newE.setPrevEdge(prevHead);

        newE.setNextEdge(nextTail);

        newM.setVertex(tailVrtx);

        newM.setFace(newF);

        newM.setPrevEdge(prevTail);

        newM.setNextEdge(nextHead);

        prevHead.setNextEdge(newE);

        prevTail.setNextEdge(newM);

        nextHead.setPrevEdge(newM);

        nextTail.setPrevEdge(newE);

        Edge edge = nextHead;

        while (edge != newM) {

            edge.setFace(newF);

            edge = edge.getNextEdge();

        }

        return this.new Result(null, newE, newF);

    }

    /**
     * ���̃O���t�ɕӂƖʂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * �ʂ��ɕ�������?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * �w�肵�� face ��?��?V����?�?������ӂ�?��ʂƂ��Ďc��?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h��?A���̓Ք�ɂԂ牺���B��ӂ�?���Ȃ��ʂ𕪊�����?�?���z�肵�Ă���?B
     * <p/>
     * �Ԃ牺���B��ӂ�?���Ȃ��ʂ�?A���̂Ԃ牺���B��ӂ�?��{�̒��_�ŕ�������?�?��ɂ�?A
     * <p/>
     * {@link #makeEdgeFace(EmbeddedGraph.Face,EmbeddedGraph.Vertex,EmbeddedGraph.Vertex,EmbeddedGraph.Edge,EmbeddedGraph.Edge)
     * <p/>
     * makeEdgeFace(Face, Vertex, Vertex, Edge, Edge)}
     * <p/>
     * �𗘗p���Ȃ���΂Ȃ�Ȃ�?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * face ���邢�� headVrtx, tailVrtx ��?A���̃O���t�Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * headVrtx, tailVrtx �� face �̒��_�T�C�N���Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * headVrtx �� tailVrtx ������̒��_�ł���?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     *
     * @param face     ���������
     * @param headVrtx ?V����?�?������ӂ̎n�_�ƂȂ钸�_
     * @param tailVrtx ?V����?�?������ӂ�?I�_�ƂȂ钸�_
     * @return ?V����?�?����ꂽ�ӂƖ�
     * @see InvalidArgumentValueException
     * @see #killEdgeFace(EmbeddedGraph.Edge,EmbeddedGraph.Face)
     */

    public synchronized Result makeEdgeFace(Face face,

                                            Vertex headVrtx,

                                            Vertex tailVrtx) {

        if (contains(face) != true) {

            throw new InvalidArgumentValueException("Given face is not in the graph.");

        }

        if ((contains(headVrtx) != true) || (contains(tailVrtx) != true)) {

            throw new InvalidArgumentValueException("Given vertex is not in the graph.");

        }

        if (headVrtx.isIdentWith(tailVrtx) == true) {

            throw new InvalidArgumentValueException("Given vertices are ideitical.");

        }

        Edge firstE = face.getFirstEdge();

        Edge edge = firstE;

        if (firstE == null) {

            throw new InvalidArgumentValueException("Given face can not be divided.");

        }

        Edge nextHead = null;

        Edge nextTail = null;

        Edge prevHead = null;

        Edge prevTail = null;

        while (true) {

            if ((headVrtx.isIdentWith(edge.getVertex()) == true) && (nextHead == null)) {

                nextHead = edge;

                prevHead = nextHead.getPrevEdge();

            }

            if ((tailVrtx.isIdentWith(edge.getVertex()) == true) && (nextTail == null)) {

                nextTail = edge;

                prevTail = nextTail.getPrevEdge();

            }

            if ((nextHead != null) && (nextTail != null))

                break;

            if ((edge = edge.getNextEdge()) == firstE) {

                throw new InvalidArgumentValueException("Given vertices are not in given face.");

            }

        }

        return makeEdgeFace(face, headVrtx, nextHead, prevHead, tailVrtx, nextTail, prevTail);

    }

    /**
     * ���̃O���t�ɕӂƖʂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * �ʂ��ɕ�������?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * �w�肵�� face ��?��?V����?�?������ӂ�?��ʂƂ��Ďc��?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h��?A���̓Ք�ɂԂ牺���B��ӂ�?�悤�Ȗʂ�
     * <p/>
     * ���̂Ԃ牺���B��ӂ�?��{�̒��_�ŕ�������悤��?�?���z�肵�Ă���?B
     * <p/>
     * �Ԃ牺���B��ӂ�?���Ȃ��ʂ𕪊�����?�?��ɂ�?A
     * <p/>
     * �킴�킴�ӂ�w�肷��K�v�̖����?�\�b�h
     * <p/>
     * {@link #makeEdgeFace(EmbeddedGraph.Face,EmbeddedGraph.Vertex,EmbeddedGraph.Vertex)
     * <p/>
     * makeEdgeFace(Face, Vertex, Vertex)}
     * <p/>
     * �𗘗p�����ȒP�ł���?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * �^����ꂽ��?^���_?^�ӂ̂����ꂩ��?A���̃O���t�Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * headVrtx, tailVrtx �� face �̒��_�T�C�N���Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * headVrtx �� tailVrtx ������̒��_�ł���?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * headVrtx �� headEdge �̒[�_�łȂ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * tailVrtx �� tailEdge �̒[�_�łȂ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     *
     * @param face     ���������
     * @param headVrtx ?V����?�?������ӂ̎n�_�ƂȂ钸�_
     * @param tailVrtx ?V����?�?������ӂ�?I�_�ƂȂ钸�_
     * @param headEdge ?V����?�?������ӂ̉E�ʂ̎��ɂȂ��
     * @param tailEdge ?V����?�?������ӂ�?��ʂ̎��ɂȂ��
     * @return ?V����?�?����ꂽ�ӂƖ�
     * @see InvalidArgumentValueException
     * @see #killEdgeFace(EmbeddedGraph.Edge,EmbeddedGraph.Face)
     */

    public synchronized Result makeEdgeFace(Face face,

                                            Vertex headVrtx,

                                            Vertex tailVrtx,

                                            Edge headEdge,

                                            Edge tailEdge) {

        if (contains(face) != true) {

            throw new InvalidArgumentValueException("Given face is not in the graph.");

        }

        if ((contains(headVrtx) != true) || (contains(tailVrtx) != true)) {

            throw new InvalidArgumentValueException("Given vertex is not in the graph.");

        }

        if ((contains(headEdge) != true) || (contains(tailEdge) != true)) {

            throw new InvalidArgumentValueException("Given edge is not in the graph.");

        }

        if (headVrtx.isIdentWith(tailVrtx) == true) {

            throw new InvalidArgumentValueException("Given vertices are ideitical.");

        }

        Edge nextHead = null;

        Edge prevHead = null;

        if (headVrtx.isIdentWith(headEdge.getVertex()) == true) {

            nextHead = headEdge;

        } else if (headVrtx.isIdentWith(headEdge.getMate().getVertex()) == true) {

            nextHead = headEdge.getMate();

        } else {

            throw new InvalidArgumentValueException("Given edge is not attached to given vertex.");

        }

        prevHead = nextHead.getPrevEdge();

        if ((face.isIdentWith(nextHead.getFace()) != true) ||

                (face.isIdentWith(prevHead.getFace()) != true)) {

            throw new InvalidArgumentValueException("Given edge is not attached to given face.");

        }

        Edge nextTail = null;

        Edge prevTail = null;

        if (tailVrtx.isIdentWith(tailEdge.getVertex()) == true) {

            nextTail = tailEdge;

        } else if (tailVrtx.isIdentWith(tailEdge.getMate().getVertex()) == true) {

            nextTail = tailEdge.getMate();

        } else {

            throw new InvalidArgumentValueException("Given edge is not attached to given vertex.");

        }

        prevTail = nextTail.getPrevEdge();

        if ((face.isIdentWith(nextTail.getFace()) != true) ||

                (face.isIdentWith(prevTail.getFace()) != true)) {

            throw new InvalidArgumentValueException("Given edge is not attached to given face.");

        }

        return makeEdgeFace(face, headVrtx, nextHead, prevHead, tailVrtx, nextTail, prevTail);

    }

    /**
     * ���̃O���t����ӂƖʂ�?�?�����?B
     * <p/>
     * <p/>
     * <p/>
     * ��̖ʂ𕪂��Ă���ӂ�?�?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * edge ���邢�� face ��?A���̃O���t�Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * edge �� face �̕ӃT�C�N���Ɋ܂܂�Ă��Ȃ�?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * </p>
     * <p/>
     * <p/>
     * <p/>
     * edge ���Ԃ牺���B��ӂł���?�?��ɂ�
     * <p/>
     * InvalidArgumentValueException �̗�O��?�����?B
     * <p/>
     * �Ȃ�?A�������̕ӂ��q���BĂԂ牺���BĂ���悤��?󋵂ɂ����Ă�?A
     * <p/>
     * ���̂��ׂĂ̕ӂ�?u�Ԃ牺���BĂ���?v�Ƃ݂Ȃ�?B
     * <p/>
     * </p>
     *
     * @param edge ?�?������
     * @param face ?�?������
     * @see InvalidArgumentValueException
     * @see #makeEdgeFace(EmbeddedGraph.Face,EmbeddedGraph.Vertex,EmbeddedGraph.Vertex)
     * @see #makeEdgeFace(EmbeddedGraph.Face,EmbeddedGraph.Vertex,EmbeddedGraph.Vertex,EmbeddedGraph.Edge,EmbeddedGraph.Edge)
     */

    public synchronized void killEdgeFace(Edge edge, Face face) {

        if (contains(edge) != true) {

            throw new InvalidArgumentValueException("Given edge is not in the graph.");

        }

        if (contains(face) != true) {

            throw new InvalidArgumentValueException("Given face is not in the graph.");

        }

        Edge mate = edge.getMate();

        if (edge.getFace().isIdentWith(mate.getFace()) == true) {

            throw new InvalidArgumentValueException("Given edge is dangling.");

        }

        /*  */
        if (face.isIdentWith(edge.getFace()) == true) {

            edge = mate;

            mate = edge.getMate();

        } else if (face.isIdentWith(mate.getFace()) == true) {

            ; // nop

        } else {

            throw new InvalidArgumentValueException("Given edge is not in given face.");

        }

        Face remainedF = edge.getFace();

        if (edge.isIdentWith(remainedF.getFirstEdge()) == true) {

            remainedF.setFirstEdge(edge.getPrevEdge());

        }

        /*

        *    <----- + <-----		    <----- + <-----

        * 	    ^ |  face

        *     edge | | mate	   ====>

        *  remainF | V			  remainF

        *    -----> + ----->		    -----> + ----->

        *    prevE    nextE		    prevE    nextE

        */


        for (int i = 0; i < 2; i++) {

            Edge prevE = edge.getPrevEdge();

            Edge nextE = mate.getNextEdge();

            prevE.setNextEdge(nextE);

            nextE.setPrevEdge(prevE);

            if (edge.isIdentWith(edge.getVertex().getFirstEdge()) == true) {

                edge.getVertex().setFirstEdge(nextE);

            }

            edge = mate;

            mate = edge.getMate();

        }

        removeFace(face);

        removeEdge(edge);

        Edge firstE = remainedF.getFirstEdge();

        edge = firstE;

        do {

            edge.setFace(remainedF);

            edge = edge.getNextEdge();

        } while (edge != firstE);

    }

    /**
     * ���̃O���t�̕�?���Ԃ�?B
     *
     * @return ��?����ꂽ�O���t
     * @see #copy()
     * @see #copy(EmbeddedGraph)
     */

    protected java.lang.Object clone() {

        return this.copy();

    }

    /**
     * ���̃O���t�̕�?���Ԃ�?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h��?��?�͈ȉ��̒ʂ�?B
     * <p/>
     * <pre>
     * <p/>
     * 		return copy(new EmbeddedGraph());
     * <p/>
     * </pre>
     * <p/>
     * </p>
     *
     * @return ��?����ꂽ�O���t
     * @see #clone()
     * @see #copy(EmbeddedGraph)
     * @see #EmbeddedGraph()
     */

    public synchronized EmbeddedGraph copy() {

        return copy(new EmbeddedGraph());

    }

    /**
     * ���̃O���t�̕�?���Ԃ�?B
     * <p/>
     * <p/>
     * <p/>
     * seed �ɂ�?A?\�z�����΂���̋�̃O���t��^����?B
     * <p/>
     * </p>
     *
     * @param seed ��?����ꂽ�ʑ�?���ێ?���邽�߂̃O���t
     * @return ��?����ꂽ�O���t
     * @see #clone()
     * @see #copy()
     */

    public synchronized EmbeddedGraph copy(EmbeddedGraph seed) {

        EmbeddedGraph replica = seed;

        Enumeration e;

        /*

        * make new items

        */

        for (e = this.vrtxList.elements(); e.hasMoreElements();) {

            Vertex src = (Vertex) e.nextElement();

            Vertex dst = replica.addVertex();

            src.setReplica(dst);

        }

        for (e = this.faceList.elements(); e.hasMoreElements();) {

            Face src = (Face) e.nextElement();

            Face dst = replica.addFace();

            src.setReplica(dst);

        }

        for (e = this.edgeList.elements(); e.hasMoreElements();) {

            EdgeContainer src = (EdgeContainer) e.nextElement();

            EdgeContainer dst = replica.addEdge().getContainer();

            src.setReplica(dst);

        }

        /*

        * fill new items

        */

        for (e = this.vrtxList.elements(); e.hasMoreElements();) {

            Vertex src = (Vertex) e.nextElement();

            src.fillFieldsOfReplica();

        }

        for (e = this.faceList.elements(); e.hasMoreElements();) {

            Face src = (Face) e.nextElement();

            src.fillFieldsOfReplica();

        }

        for (e = this.edgeList.elements(); e.hasMoreElements();) {

            EdgeContainer src = (EdgeContainer) e.nextElement();

            src.fillFieldsOfReplica();

        }

        /*

        * clear replica field

        */

        for (e = this.vrtxList.elements(); e.hasMoreElements();) {

            Vertex src = (Vertex) e.nextElement();

            src.setReplica(null);

        }

        for (e = this.faceList.elements(); e.hasMoreElements();) {

            Face src = (Face) e.nextElement();

            src.setReplica(null);

        }

        for (e = this.edgeList.elements(); e.hasMoreElements();) {

            EdgeContainer src = (EdgeContainer) e.nextElement();

            src.setReplica(null);

        }

        return replica;

    }

    /**
     * ���̃O���t�̑o�Ε�?���Ԃ�?B
     * <p/>
     * <p/>
     * <p/>
     * ���̃?�\�b�h��?��?�͈ȉ��̒ʂ�?B
     * <p/>
     * <pre>
     * <p/>
     * 		return dualCopy(new EmbeddedGraph());
     * <p/>
     * </pre>
     * <p/>
     * </p>
     *
     * @return �o�Ε�?����ꂽ�O���t
     * @see #dualCopy(EmbeddedGraph)
     * @see #EmbeddedGraph()
     */

    public synchronized EmbeddedGraph dualCopy() {

        return dualCopy(new EmbeddedGraph());

    }

    /**
     * ���̃O���t�̑o�Ε�?���Ԃ�?B
     * <p/>
     * <p/>
     * <p/>
     * seed �ɂ�?A?\�z�����΂���̋�̃O���t��^����?B
     * <p/>
     * </p>
     *
     * @param seed �o�Ε�?����ꂽ�ʑ�?���ێ?���邽�߂̃O���t
     * @return �o�Ε�?����ꂽ�O���t
     * @see #dualCopy()
     */

    public synchronized EmbeddedGraph dualCopy(EmbeddedGraph seed) {

        EmbeddedGraph replica = seed;

        Enumeration e;

        /*

        * make new items

        */

        for (e = this.vrtxList.elements(); e.hasMoreElements();) {

            Vertex src = (Vertex) e.nextElement();

            Face dst = replica.addFace();

            src.setReplica(dst);

        }

        for (e = this.faceList.elements(); e.hasMoreElements();) {

            Face src = (Face) e.nextElement();

            Vertex dst = replica.addVertex();

            src.setReplica(dst);

        }

        for (e = this.edgeList.elements(); e.hasMoreElements();) {

            EdgeContainer src = (EdgeContainer) e.nextElement();

            EdgeContainer dst = replica.addEdge().getContainer();

            src.setReplica(dst);

        }

        /*

        * fill new items

        */

        for (e = this.vrtxList.elements(); e.hasMoreElements();) {

            Vertex src = (Vertex) e.nextElement();

            src.fillFieldsOfDualReplica();

        }

        for (e = this.faceList.elements(); e.hasMoreElements();) {

            Face src = (Face) e.nextElement();

            src.fillFieldsOfDualReplica();

        }

        for (e = this.edgeList.elements(); e.hasMoreElements();) {

            EdgeContainer src = (EdgeContainer) e.nextElement();

            src.fillFieldsOfDualReplica();

        }

        /*

        * fille edge's previous and next edge fields

        */

        for (e = this.vrtxList.elements(); e.hasMoreElements();) {

            Vertex vrtx = (Vertex) e.nextElement();

            Edge firstE = vrtx.getFirstEdge();

            Edge edge = firstE;

            if (firstE == null)

                continue;

            Edge firstReplicaEdge = edge.getMate().getReplica();

            Edge prevReplicaEdge = firstReplicaEdge;

            Edge crntReplicaEdge = null;

            edge = vrtx.getNextEdgeInCCW(edge);

            while (edge.isIdentWith(firstE) != true) {

                crntReplicaEdge = edge.getMate().getReplica();

                prevReplicaEdge.setNextEdge(crntReplicaEdge);

                crntReplicaEdge.setPrevEdge(prevReplicaEdge);

                prevReplicaEdge = crntReplicaEdge;

                edge = vrtx.getNextEdgeInCCW(edge);

            }

            crntReplicaEdge = firstReplicaEdge;

            prevReplicaEdge.setNextEdge(crntReplicaEdge);

            crntReplicaEdge.setPrevEdge(prevReplicaEdge);

        }

        /*

        * clear replica field

        */

        for (e = this.vrtxList.elements(); e.hasMoreElements();) {

            Vertex src = (Vertex) e.nextElement();

            src.setReplica(null);

        }

        for (e = this.faceList.elements(); e.hasMoreElements();) {

            Face src = (Face) e.nextElement();

            src.setReplica(null);

        }

        for (e = this.edgeList.elements(); e.hasMoreElements();) {

            EdgeContainer src = (EdgeContainer) e.nextElement();

            src.setReplica(null);

        }

        return replica;

    }

}

// end of file

