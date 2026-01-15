package org.jscience.mathematics.analysis.expressions;

import org.jscience.mathematics.analysis.expressions.symbolic.Addition;
import org.jscience.mathematics.analysis.expressions.symbolic.Division;
import org.jscience.mathematics.analysis.expressions.symbolic.Multiplication;
import org.jscience.mathematics.analysis.expressions.symbolic.Sin;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;


/**
 * The class ExpressionTreeModel is a TreeModel implementation that allows
 * Expressions to be displayed as tree structures. <BR><B>Example of
 * use:</B><BR><PRE>
 * Expression e = new Addition( new Multiplication( new Variable( "x" ),
 * new Parameter(" p ") ),new Division( new Variable( "y" ),
 * new Sin( new Variable( "t" ) ) ));
 * ExpressionTreeModel model = new ExpressionTreeModel( e );
 * Settings.recursivePrint = false;JTree tree = new JTree();
 * tree.setModel( model );JFrame frame = new JFrame( "Computational tree" );
 * frame.getContentPane().add( new JScrollPane( tree ), "Center" );
 * frame.pack();frame.show();</PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see Expression
 */
public class ExpressionTreeModel implements TreeModel {
    /** DOCUMENT ME! */
    private Expression root;

/**
     * Creates a new ExpressionTreeModel object.
     *
     * @param expr DOCUMENT ME!
     */
    public ExpressionTreeModel(Expression expr) {
        root = expr;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getRoot() {
        return root;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLeaf(Object node) {
        if (node instanceof Constant) {
            return true;
        }

        if (node instanceof Variable) {
            return true;
        }

        if (node instanceof Parameter) {
            return true;
        }

        if (node instanceof Auxiliary) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getChildCount(Object parent) {
        if (parent instanceof Constant) {
            return 0;
        }

        if (parent instanceof Variable) {
            return 0;
        }

        if (parent instanceof Parameter) {
            return 0;
        }

        if (parent instanceof UnaryOperator) {
            return 1;
        }

        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getChild(Object parent, int index) {
        if (parent instanceof UnaryOperator) {
            if (index == 0) {
                return ((UnaryOperator) parent).getFirstOperand();
            } else {
                return null;
            }
        }

        if (parent instanceof BinaryOperator) {
            if (index == 0) {
                return ((BinaryOperator) parent).getFirstOperand();
            } else if (index == 1) {
                return ((BinaryOperator) parent).getSecondOperand();
            } else {
                return null;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof UnaryOperator) {
            return 0;
        }

        if (parent instanceof BinaryOperator) {
            if (((BinaryOperator) parent).getFirstOperand() == child) {
                return 0;
            } else {
                return 1;
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param o DOCUMENT ME!
     */
    public void valueForPathChanged(javax.swing.tree.TreePath t,
        java.lang.Object o) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void addTreeModelListener(TreeModelListener l) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void removeTreeModelListener(TreeModelListener l) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Expression e = new Addition(new Multiplication(new Variable("x"),
                    new Parameter(" p ")),
                new Division(new Variable("y"), new Sin(new Variable("t"))));
        ExpressionTreeModel model = new ExpressionTreeModel(e);

        //Settings.recursivePrint = false;
        JTree tree = new JTree();
        tree.setModel(model);

        JFrame frame = new JFrame("Computational tree");
        frame.getContentPane().add(new JScrollPane(tree), "Center");
        frame.pack();
        frame.show();
    }
}
