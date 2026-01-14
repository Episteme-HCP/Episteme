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

package org.jscience.computing.game;

/**
 * The GameRuntimeException provides access to the GamePlay object that is
 * associated to the exception, so that Exception handing code can take
 * advantage of it.
 *
 * @author Holger Antelmann
 *
 * @see GameException
 */
public class GameRuntimeException extends RuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5620229047407791305L;

    /** DOCUMENT ME! */
    GamePlay game;

/**
     * Creates a new GameRuntimeException object.
     */
    public GameRuntimeException() {
        super();
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param text DOCUMENT ME!
     */
    public GameRuntimeException(String text) {
        super(text);
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param text  DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public GameRuntimeException(String text, Throwable cause) {
        super(text, cause);
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param game      DOCUMENT ME!
     * @param errorText DOCUMENT ME!
     */
    public GameRuntimeException(GamePlay game, String errorText) {
        super(errorText);
        this.game = game;
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param game      DOCUMENT ME!
     * @param errorText DOCUMENT ME!
     * @param cause     DOCUMENT ME!
     */
    public GameRuntimeException(GamePlay game, String errorText, Throwable cause) {
        super(errorText, cause);
        this.game = game;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GamePlay getGame() {
        return game;
    }
}
