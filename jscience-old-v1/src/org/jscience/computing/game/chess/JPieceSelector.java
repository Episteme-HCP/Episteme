/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import org.jscience.computing.game.GameDriver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * used for piece selection if JChess is in setup mode
 *
 * @author Holger Antelmann
 */
class JPieceSelector extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 170206635326454381L;

    /** DOCUMENT ME! */
    JChess jplay;

    /** DOCUMENT ME! */
    JTable table;

    /** DOCUMENT ME! */
    JCheckBox whiteMoves;

/**
     * Creates a new JPieceSelector object.
     *
     * @param jplay DOCUMENT ME!
     */
    JPieceSelector(final JChess jplay) {
        super();
        this.jplay = jplay;

        Object[] header = new Object[] { "white", "black" };
        Object[][] cells = new Object[][] {
                new Object[] {
                    jplay.jboard.icons.getKingIcon(0),
                    jplay.jboard.icons.getKingIcon(1)
                },
                new Object[] {
                    jplay.jboard.icons.getQueenIcon(0),
                    jplay.jboard.icons.getQueenIcon(1)
                },
                new Object[] {
                    jplay.jboard.icons.getRookIcon(0),
                    jplay.jboard.icons.getRookIcon(1)
                },
                new Object[] {
                    jplay.jboard.icons.getKnightIcon(0),
                    jplay.jboard.icons.getKnightIcon(1)
                },
                new Object[] {
                    jplay.jboard.icons.getBishopIcon(0),
                    jplay.jboard.icons.getBishopIcon(1)
                },
                new Object[] {
                    jplay.jboard.icons.getPawnIcon(0),
                    jplay.jboard.icons.getPawnIcon(1)
                }, new Object[] { "white blank", "black blank" }
            };
        table = new JTable(cells, header) {
                    static final long serialVersionUID = 1645211344635250043L;

                    public Class getColumnClass(int c) {
                        //return this.getValueAt(0, c).getClass();
                        return (ImageIcon.class);
                    }

                    // make the cells uneditable
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
        table.setAlignmentY(table.CENTER_ALIGNMENT);

        int size = jplay.jboard.icons.getKingIcon(0).getImage()
                                     .getHeight(jplay.jboard.icons.getKingIcon(
                    0).getImageObserver());
        table.setRowHeight(size);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(size);
        }

        table.setMaximumSize(new java.awt.Dimension(size * 2, size * 7));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(new JLabel("select piece"));
        controls.add(new JLabel("and click"));
        controls.add(new JLabel("tile to place"));
        controls.add(new JLabel("on board"));
        controls.add(new JLabel(" "));

        JButton clear = new JButton("clear");
        clear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clear();
                }
            });
        controls.add(clear);

        JButton cancel = new JButton("cancel");
        cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });
        controls.add(cancel);

        JButton done = new JButton("done");
        done.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });
        controls.add(done);
        whiteMoves = new JCheckBox("white next");
        whiteMoves.setSelected(true);
        controls.add(whiteMoves);
        add(controls);
        add(table);
    }

    /**
     * DOCUMENT ME!
     */
    void clear() {
        if (jplay.setupGame == null) {
            return;
        }

        ChessPiece[][] board = jplay.setupGame.getBoard().board;

        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                board[c][r] = null;
            }
        }

        jplay.getFrame().repaint();
    }

    /**
     * DOCUMENT ME!
     */
    void done() {
        if (!jplay.setupGame.getBoard().isValid()) {
            JOptionPane.showMessageDialog(jplay.getFrame().getFrame(),
                "board is not valid", "validation error",
                JOptionPane.ERROR_MESSAGE);

            return;
        }

        jplay.setupGame.resetLists();

        if (isWhiteNext()) {
            jplay.setupGame.blackMovesFirst = false;
        } else {
            jplay.setupGame.blackMovesFirst = true;
        }

        GameDriver play = new GameDriver(jplay.setupGame,
                jplay.getAutoPlay().getPlayers(), jplay.getAutoPlay().getLevel());
        jplay.setGame(play);
        jplay.setupGame = null;
        jplay.setupPanel.dispose();
        jplay.setupPanel = null;
        jplay.getFrame().repaint();
    }

    /**
     * DOCUMENT ME!
     */
    void cancel() {
        jplay.setupGame = null;
        jplay.getFrame().repaint();
        jplay.setupPanel.dispose();
        jplay.setupPanel = null;
        jplay.getFrame().repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWhiteNext() {
        return whiteMoves.isSelected();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChessPiece getSelection() {
        if ((table.getSelectedRow() < 0) || (table.getSelectedColumn() < 0)) {
            return null;
        }

        int selection = (table.getSelectedRow() * 10) +
            table.getSelectedColumn();

        switch (selection) {
        case 00:
            return new King(0, null, null);

        case 01:
            return new King(1, null, null);

        case 10:
            return new Queen(0, null, null);

        case 11:
            return new Queen(1, null, null);

        case 20:
            return new Rook(0, null, null);

        case 21:
            return new Rook(1, null, null);

        case 30:
            return new Knight(0, null, null);

        case 31:
            return new Knight(1, null, null);

        case 40:
            return new Bishop(0, null, null);

        case 41:
            return new Bishop(1, null, null);

        case 50:
            return new Pawn(0, null, null);

        case 51:
            return new Pawn(1, null, null);

        default:
            return null;
        }
    }
}
