/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


/**
 * JMoveList implements a special list containing game moves. As the
 * underlying model is not automatically updated when the game status changes,
 * the repaint() method is recreating the list model over again.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.JGamePlay
 */
class JMoveList extends JScrollPane {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 8806647623052862894L;

    /** DOCUMENT ME! */
    public static final int HISTORY = 1;

    /** DOCUMENT ME! */
    public static final int LEGAL = 2;

    /** DOCUMENT ME! */
    public static final int REDO = 3;

    /** DOCUMENT ME! */
    DefaultListModel model;

    /** DOCUMENT ME! */
    GameGUI frame;

    /** DOCUMENT ME! */
    JList list;

    /** DOCUMENT ME! */
    final int listType;

/**
     * DOCUMENT ME!
     *
     * @param frame    DOCUMENT ME!
     * @param listType use one of the three values: HISTORY, LEGAL or REDO
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public JMoveList(GameGUI frame, int listType) {
        super();
        this.frame = frame;
        this.listType = listType;
        model = new DefaultListModel();
        redraw();
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setPrototypeCellValue(frame.getJGamePlay().prototypeCellValue);

        switch (listType) {
        case HISTORY:
            list.addMouseListener(getHistoryListener());

            break;

        case LEGAL:
            list.addMouseListener(getLegalListener());

            break;

        case REDO:
            list.addMouseListener(getRedoListener());

            break;

        default:
            throw new IllegalArgumentException("listType is not valid");
        }

        setViewportView(list);
    }

    /**
     * overwritten to update all component windows with up-to-date game
     * info
     */
    public void repaint() {
        // avoiding redraw() to be called during super.<init>
        if (listType != 0) {
            redraw();
        }

        super.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    void redraw() {
        GameMove[] moves;

        switch (listType) {
        case HISTORY:
            moves = frame.getJGamePlay().getAutoPlay().getGame().getMoveHistory();

            break;

        case LEGAL:
            moves = frame.getJGamePlay().getAutoPlay().getGame().getLegalMoves();

            break;

        case REDO:
            moves = frame.getJGamePlay().getAutoPlay().getGame().getRedoList();

            break;

        default:
            throw new Error();
        }

        model.clear();

        for (int i = 0; i < moves.length; i++) {
            if (listType != LEGAL) {
                model.add(i, moves[moves.length - i - 1]);
            } else {
                model.add(i, moves[i]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    MouseListener getLegalListener() {
        MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        GameMove m = (GameMove) list.getSelectedValue();
                        frame.requestGUIMove(m);
                        repaint();
                    }
                }
            };

        return mouseListener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    MouseListener getHistoryListener() {
        MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int n = list.getSelectedIndex();

                        for (int i = 0; i <= n; i++) {
                            if (!frame.requestGUIUndoMove()) {
                                break;
                            }
                        }

                        repaint();
                    }
                }
            };

        return mouseListener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    MouseListener getRedoListener() {
        MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int n = list.getSelectedIndex();

                        for (int i = 0; i <= n; i++) {
                            if (!frame.requestGUIRedoMove()) {
                                break;
                            }
                        }

                        repaint();
                    }
                }
            };

        return mouseListener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JList getJList() {
        return list;
    }
}
