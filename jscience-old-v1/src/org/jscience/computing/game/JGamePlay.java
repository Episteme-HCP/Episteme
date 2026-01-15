/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;


/**
 * JGamePlay provides functionality that is used by the GUI based JGameFrame to
 * display the game specific features. All a specific game needs to do in
 * order to be used with the JGameFrame is to simply implement these simple
 * functions. All the rest is then done by the JGameFrame. For the object that
 * implements JGamePlay to work smoothly with JGameFrame, that object should
 * also be serializable. If a given GamePlay has no special needs for
 * displaying itself, it may suffice to simply use an instance of JDefaultGame
 * to provide the wrapper that implements this interface. Note that a
 * JGamePlay's container should not allow to directly perform a move on the
 * embedded AutoPlay object; instead it should delegate the handling of a move
 * to the GameGUI object that uses this JGamePlay.
 *
 * @author Holger Antelmann
 * @see JGameFrame
 * @see GamePlay
 * @see GameGUI
 * @see JDefaultGame
 */
public interface JGamePlay {
    /** x-coorinate of the upper left window corner */
    int windowLocationX = 100;

    /** y-coorinate of the upper left window corner */
    int windowLocationY = 100;

    /** width of the window */
    int windowWidth = 600;

    /** height of the window */
    int windowHeight = 400;

    /** horizontal offset for positioning the legal moves */
    int legalMovesFrameX = 380;

    /** vertical offset for positioning the legal moves */
    int legalMovesFrameY = 60;

    /** horizontal offset for positioning the history moves */
    int historyFrameX = 340;

    /** vertical offset for positioning the history moves */
    int historyFrameY = 30;

    /** horizontal offset for positioning the redo moves */
    int redoFrameX = 300;

    /** vertical offset for positioning the redo moves */
    int redoFrameY = 0;

    /** horizontal offset for positioning the game board */
    int gameFrameX = 0;

    /** vertical offset for positioning the game board */
    int gameFrameY = 0;

    /** used to size the legal, history and redo move list */
    String prototypeCellValue = "12345678901234567890";

    /**
     * getContainer() returns the GUI component used to display the
     * game representation.
     *
     * @param frame the object to be used to alter the game status by using the
     *        interface
     *
     * @return DOCUMENT ME!
     */
    public Container getContainer(GameGUI frame);

    /**
     * AutoPlay() returns the underlying game - to allow the calling
     * functions to perform operations on the game itself.
     *
     * @return DOCUMENT ME!
     */
    public AutoPlay getAutoPlay();

    /**
     * getFileFilter() provides the option to filter for certain file
     * types when saving or loading games.
     *
     * @return DOCUMENT ME!
     */
    public FileFilter getFileFilter();

    /**
     * getDefaultFileExtension() defines the String used as a file type
     * when saving files; it should correspond to getFileFlter. Example: if
     * getDefaultFileExtension() returns "test" and the user doesn't enter a
     * file extension for the game to save but simply "game1", then the file
     * saved will be name "game1.test".
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultFileExtension();

    /**
     * getMenu() provides the option to display a game specific menu
     * within the overall JMenuBar of the JGameFrame.
     *
     * @return DOCUMENT ME!
     */
    public JMenu getMenu();

    /**
     * getNewGame() will initialize a new game and return the JGamePlay
     * wrapper associated with the new game.
     *
     * @return DOCUMENT ME!
     */
    public JGamePlay getNewGame();

    /**
     * setGame(AbstractGameBoard board) returns a JGamePlay object that
     * embedds the given AutoPlay game. This function enables the JGameFrame
     * to load a game and transform it into a JGamePlay in a game specific
     * way. If the given board doesn't match the implementation of JGamePlay,
     * an exception is thrown.
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws GameRuntimeException
     */
    public JGamePlay setGame(AutoPlay game) throws GameRuntimeException;

    /**
     * getTitle() returns a string used in JGameFrame to be displayed
     * in the title bar of the top JFrame.
     *
     * @return DOCUMENT ME!
     */
    public String getTitle();

    /**
     * getStatusMessage () is used to display a game specific status
     * message, giving some information about the running game. JGameFrame
     * uses the return value for display in the status toolbar after a move is
     * carried out.
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage();

    /**
     * getHelp() is to return a component (such as JLabel, JTextArea,
     * etc) that can be displayed if a user selects the game specific help
     * menu from a JGameFrame. It is ok to return null, in which case the
     * JGameFrame will display a message that no specific help is available.
     *
     * @return DOCUMENT ME!
     */
    public Container getHelp();
}
