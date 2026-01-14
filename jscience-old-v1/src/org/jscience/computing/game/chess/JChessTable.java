/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.computing.game.chess;

import org.jscience.computing.game.*;

import org.jscience.io.ExtensionFileFilter;

import org.jscience.util.Settings;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;


/**
 * This class implements the functionality that lets you play chess within
 * the GUI environment of a JGameFrame using a JTable without listeners Note
 * that some of the images used by this class have been removed from the
 * source code, i.e. this class doesn't work well anymore; it's just kept for
 * reference.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.JGameFrame
 * @deprecated
 */
@Deprecated
class JChessTable extends AbstractTableModel implements JGamePlay,
    ActionListener {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -5063938119618359280L;

    /**
     * DOCUMENT ME!
     */
    ImageIcon whiteKingIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/wk.gif"), "white King");

    /**
     * DOCUMENT ME!
     */
    ImageIcon whiteQueenIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/wq.gif"), "white Queen");

    /**
     * DOCUMENT ME!
     */
    ImageIcon whiteRookIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/wr.gif"), "white Rook");

    /**
     * DOCUMENT ME!
     */
    ImageIcon whiteKnightIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/wn.gif"), "white Knight");

    /**
     * DOCUMENT ME!
     */
    ImageIcon whiteBishopIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/wb.gif"), "white Bishop");

    /**
     * DOCUMENT ME!
     */
    ImageIcon whitePawnIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/wp.gif"), "white Pawn");

    /**
     * DOCUMENT ME!
     */
    ImageIcon blackKingIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/bk.gif"), "black King");

    /**
     * DOCUMENT ME!
     */
    ImageIcon blackQueenIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/bq.gif"), "black Queen");

    /**
     * DOCUMENT ME!
     */
    ImageIcon blackRookIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/br.gif"), "black Rook");

    /**
     * DOCUMENT ME!
     */
    ImageIcon blackKnightIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/bn.gif"), "black Knight");

    /**
     * DOCUMENT ME!
     */
    ImageIcon blackBishopIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/bb.gif"), "black Bishop");

    /**
     * DOCUMENT ME!
     */
    ImageIcon blackPawnIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/bp.gif"), "black Pawn");

    // begin deleted images
    /**
     * DOCUMENT ME!
     */
    ImageIcon icon1 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/1.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon icon2 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/2.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon icon3 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/3.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon icon4 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/4.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon icon5 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/5.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon icon6 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/6.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon icon7 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/7.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon icon8 = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/8.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon aIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/a.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon bIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/b.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon cIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/c.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon dIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/d.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon eIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/e.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon fIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/f.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon gIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/g.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon hIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/h.gif"));

    /**
     * DOCUMENT ME!
     */
    ImageIcon emptyTileIcon = new ImageIcon(Settings.getResource(
                "org/jscience/swing/chess/emptyTile.gif"), "empty");

    // end deleted images
    /**
     * DOCUMENT ME!
     */
    GameGUI frame;

    /**
     * DOCUMENT ME!
     */
    AutoPlay play;

    /**
     * DOCUMENT ME!
     */
    ChessGame game;

    /**
     * DOCUMENT ME!
     */
    ExtensionFileFilter chessFilter;

    /**
     * DOCUMENT ME!
     */
    JTable table;

    /**
     * DOCUMENT ME!
     */
    JCheckBoxMenuItem flipped;

    /**
     * DOCUMENT ME!
     */
    JFileChooser fileChooser;

    /**
     * Creates a new JChessTable object.
     */
    public JChessTable() {
        this(new ChessGame(), new ChessPlayer("white"),
            new ChessPlayer("black"), 1);
    }

    /**
     * Creates a new JChessTable object.
     *
     * @param game DOCUMENT ME!
     * @param white DOCUMENT ME!
     * @param black DOCUMENT ME!
     * @param level DOCUMENT ME!
     */
    public JChessTable(ChessGame game, Player white, Player black, int level) {
        this.game = game;
        play = new GameDriver(game, new Player[] { white, black }, level);
        table = new JTable(this) {
                    static final long serialVersionUID = -5454996672901634251L;

                    public Class getColumnClass(int c) {
                        //return this.getValueAt(0, c).getClass();
                        return (ImageIcon.class);
                    }

                    public TableCellRenderer getCellRenderer(int row, int column) {
                        return (new MyRenderer());
                    }
                };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(32);

        for (int i = 0; i < getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(32);
        }

        flipped = new JCheckBoxMenuItem("Flipped Board");
        flipped.addActionListener(this);
        chessFilter = new ExtensionFileFilter("chess", "Chess Files (*.chess)");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JGamePlay getNewGame() {
        game = new ChessGame("new game through getNewGame() in JChessTable");
        play = new GameDriver(game,
                new Player[] {
                    play.getPlayer(ChessBoard.WHITE),
                    play.getPlayer(ChessBoard.BLACK)
                }, play.getLevel());

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return 9;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return 9;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(int row, int column) {
        BoardPosition tile = null;

        if (flipped.isSelected()) {
            row = getRowCount() - row - 1;
            column = getColumnCount() - column - 1;
        }

        int pos = ((10 * column) + 8) - row;

        try {
            tile = new BoardPosition(pos);

            return (getTileRepresentation(tile));
        } catch (IllegalArgumentException e) {
            //return (new Integer(pos));
            switch (pos) {
            case 1:
                return (icon1);

            case 2:
                return (icon2);

            case 3:
                return (icon3);

            case 4:
                return (icon4);

            case 5:
                return (icon5);

            case 6:
                return (icon6);

            case 7:
                return (icon7);

            case 8:
                return (icon8);

            case 10:
                return (aIcon);

            case 20:
                return (bIcon);

            case 30:
                return (cIcon);

            case 40:
                return (dIcon);

            case 50:
                return (eIcon);

            case 60:
                return (fIcon);

            case 70:
                return (gIcon);

            case 80:
                return (hIcon);

            case 0:

                switch (game.nextPlayer()) {
                case ChessBoard.WHITE:
                    return (whiteKingIcon);

                case ChessBoard.BLACK:
                    return (blackKingIcon);
                }
            }
        }

        return (new Integer(pos));
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Object getTileRepresentation(BoardPosition position) {
        ImageIcon image = getPieceIcon(game.getBoard().getPieceAt(position));

        if (image == null) {
            return (null);
        } else {
            return (image);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ImageIcon getPieceIcon(ChessPiece piece) {
        if (piece == null) {
            return emptyTileIcon;
        }

        switch (piece.getColor()) {
        case ChessBoard.WHITE:

            if (piece.isKing()) {
                return whiteKingIcon;
            }

            if (piece.isQueen()) {
                return whiteQueenIcon;
            }

            if (piece.isRook()) {
                return whiteRookIcon;
            }

            if (piece.isKnight()) {
                return whiteKnightIcon;
            }

            if (piece.isBishop()) {
                return whiteBishopIcon;
            }

            if (piece.isPawn()) {
                return whitePawnIcon;
            }

            throw new Error();

        case ChessBoard.BLACK:

            if (piece.isKing()) {
                return blackKingIcon;
            }

            if (piece.isQueen()) {
                return blackQueenIcon;
            }

            if (piece.isRook()) {
                return blackRookIcon;
            }

            if (piece.isKnight()) {
                return blackKnightIcon;
            }

            if (piece.isBishop()) {
                return blackBishopIcon;
            }

            if (piece.isPawn()) {
                return blackPawnIcon;
            }

            throw new Error();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Container getContainer(GameGUI parent) {
        this.frame = frame;

        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AutoPlay getAutoPlay() {
        return play;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FileFilter getFileFilter() {
        return chessFilter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultFileExtension() {
        return chessFilter.getDefaultType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JMenu getMenu() {
        JMenu specialMenu = new JMenu("Chess");
        specialMenu.setMnemonic(KeyEvent.VK_C);

        JMenuItem item;
        specialMenu.add(flipped);
        item = new JMenuItem("print game to Console");
        item.addActionListener(this);
        specialMenu.add(item);
        item = new JMenuItem("export game in PGN");
        item.addActionListener(this);
        specialMenu.add(item);

        return specialMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param play DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws GameRuntimeException DOCUMENT ME!
     */
    public JGamePlay setGame(AutoPlay play) throws GameRuntimeException {
        if (this.play.getGame().getClass() != ChessGame.class) {
            String text = "org.jscience.computing.game.chess.JChessTable cannot play the given game of class ";
            text = text + this.play.getGame().getClass().getName();
            throw (new GameRuntimeException(game, text));
        }

        game = (ChessGame) this.play.getGame();

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return "Chess";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        String s = "# of moves: " + game.getMoveHistory().length;
        s += (", available legal moves: " + game.getLegalMoves().length);

        if (game.getLegalMoves().length == 0) {
            s += " Game Over!";
        }

        int[] win = game.getWinner();

        if (win != null) {
            s += " Winner:";

            for (int i = 0; i < win.length; i++) {
                s += (" " + play.getPlayer(win[i]).getPlayerName());
            }
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Container getHelp() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Flip Board")) {
            say("Flipping Board Menu selected");
            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("print game to Console")) {
            say("Menu Option print game to Console selected");
            System.out.println(game);
            System.out.println("List of pieces:");

            ChessPiece[] pieces = game.getBoard().getPieces();
            int counter = 0;

            for (int i = 0; i < pieces.length; i++) {
                System.out.print(++counter + ": ");
                System.out.println(pieces[i]);
            }

            say("Legal Moves:");

            GameMove[] moves = game.getLegalMoves();

            for (int i = 0; i < moves.length; i++) {
                System.out.println(moves[i]);
            }

            say("Positions under attack by white:");

            for (char column = 'a'; column <= 'h'; column++) {
                for (int row = 1; row <= 8; row++) {
                    BoardPosition pos = new BoardPosition(column, row);

                    if (game.getBoard().positionAttackedBy(pos, ChessBoard.WHITE)) {
                        System.out.print(" " + pos);
                    }
                }
            }

            System.out.println();
            say("Positions under attack by black:");

            for (char column = 'a'; column <= 'h'; column++) {
                for (int row = 1; row <= 8; row++) {
                    BoardPosition pos = new BoardPosition(column, row);

                    if (game.getBoard().positionAttackedBy(pos, ChessBoard.BLACK)) {
                        System.out.print(" " + pos);
                    }
                }
            }

            System.out.println();
            say("White King under attack: " +
                game.getBoard().findKing(ChessBoard.WHITE).underAttack());
            say("Black King under attack: " +
                game.getBoard().findKing(ChessBoard.BLACK).underAttack());
            say("Last move: " +
                game.getMoveHistory()[game.getMoveHistory().length - 1]);
            say("List winners: " + game.getWinner());
            //say("Hint from white: " + game.hint(ChessBoard.WHITE));
            //say("Hint from black: " + game.hint(ChessBoard.BLACK));
            say("Done printing game to console.");

            return;
        }

        if (e.getActionCommand().equals("export game in PGN")) {
            say("Menu Option export game in PGN selected");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new ExtensionFileFilter("txt",
                    "Text Files (.txt)"));

            int chosen = fileChooser.showDialog(null, "Export");

            if (chosen == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();

                if (f.getName().indexOf('.') == -1) {
                    f = new File(f.getAbsolutePath() + ".txt");
                }

                try {
                    PGN.export(game, f.getAbsolutePath());
                    say("game exported in PGN to file: " + f.getAbsolutePath());
                } catch (Exception ex) {
                    complain(
                        "Sorry, exporting the Game didn't work out; Details follow:");
                    complain(ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                say("PGN file export canceled by user");
            }

            return;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    private void say(String text) {
        System.out.println("[JChessTable Message:] " + text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    private void complain(String text) {
        System.err.println("[JChessTable Error:] " + text);
    }
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class MyRenderer implements TableCellRenderer {
    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel c = new JLabel((ImageIcon) value);
        c.setOpaque(true);

        // determining the background color which doesn't work yet
        BoardPosition tile = null;
        int pos = ((10 * column) + 8) - row;

        try {
            tile = new BoardPosition(pos);

            switch (tile.getTileColor()) {
            case ChessBoard.WHITE:
                c.setBackground(Color.white);

                break;

            case ChessBoard.BLACK:
                c.setBackground(Color.lightGray);

                break;
            }
        } catch (IllegalArgumentException e) {
            c.setBackground(Color.gray);
        }

        return c;
    }
}
/*
class MyRenderer extends DefaultTableCellRenderer //implements TableCellRenderer
{
    MyRenderer () {
    }


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        // todo
        // this only returns the same component over and over again
        //Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);

        setValue(value);

        // determining the background color
        BoardPosition tile = null;
        int pos = (10*column) + 8 - row;
        try {
            tile = new BoardPosition(pos);
            switch (tile.getTileColor()) {
                case ChessBoard.WHITE: setBackground(Color.white);     break;
                case ChessBoard.BLACK: setBackground(Color.lightGray); break;
            }
        } catch (IllegalArgumentException e) {
            setBackground(Color.gray);
        }
        return this;
    }
}
*/
