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

package org.jscience.architecture.lift.gui;

import java.awt.*;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:54 $
 */
public class SimpleCarCanvas extends JPanel implements CarCanvas {
    /**
     * DOCUMENT ME!
     */
    boolean CloseMode = true;

    /**
     * DOCUMENT ME!
     */
    boolean CarPresent = true;

    /**
     * DOCUMENT ME!
     */
    int State = 0;

    /**
     * DOCUMENT ME!
     */
    double Progress = 0;

    /**
     * DOCUMENT ME!
     */
    boolean NeedsUpdate = true;

    /**
     * Creates a new SimpleCarCanvas object.
     */
    public SimpleCarCanvas() {
        //		super();
        setSize(36, 48);
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewActNumber DOCUMENT ME!
     */
    public void setActNumber(int NewActNumber) {
        ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewDstFloors DOCUMENT ME!
     */
    public void setDstFloors(int[] NewDstFloors) {
        ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewMaxNumber DOCUMENT ME!
     */
    public void setMaxNumber(int NewMaxNumber) {
        ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewState DOCUMENT ME!
     */
    public void setState(int NewState) {
        State = NewState;
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewProgress DOCUMENT ME!
     */
    public void setProgress(double NewProgress) {
        Progress = NewProgress;
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewCarPresent DOCUMENT ME!
     */
    public void setCarPresent(boolean NewCarPresent) {
        CarPresent = NewCarPresent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param G DOCUMENT ME!
     */
    public void paint(Graphics G) {
        int W = getSize().width;
        int H = getSize().height;

        if (!CarPresent) {
            G.setColor(Color.LIGHT_GRAY);
            G.fillRect(0, 0, W, H);

            return;
        }

        G.setColor(Color.WHITE);
        setBackground(Color.LIGHT_GRAY);

        int FillHeigth = -1;

        switch (State) {
        case CLOSE_OPEN:
            setBackground(Color.GRAY);

            int FillWidth = (int) (W * Progress * 0.5);

            G.fillRect(0, 0, FillWidth, H);
            G.fillRect(W - FillWidth, 0, FillWidth, H);
            G.setColor(getBackground());
            G.fillRect(FillWidth, 0, W - (2 * FillWidth), H);

            break;

        case GOING_UP:
            FillHeigth = (int) ((1 - Progress) * H);
            G.fillRect(0, 0, W, FillHeigth);
            G.setColor(getBackground());
            G.fillRect(0, FillHeigth, W, H - FillHeigth);

            break;

        case ARRIVING_UP:
            FillHeigth = (int) ((1 - Progress) * H);
            G.fillRect(0, FillHeigth, W, H - FillHeigth);
            G.setColor(getBackground());
            G.fillRect(0, 0, W, FillHeigth);

            break;

        case GOING_DOWN:
            FillHeigth = (int) ((1 - Progress) * H);
            G.fillRect(0, H - FillHeigth, W, FillHeigth);
            G.setColor(getBackground());
            G.fillRect(0, 0, W, H - FillHeigth);

            break;

        case ARRIVING_DOWN:
            FillHeigth = (int) ((1 - Progress) * H);
            G.fillRect(0, 0, W, H - FillHeigth);
            G.setColor(getBackground());
            G.fillRect(0, H - FillHeigth, W, FillHeigth);

            break;

        case PARKING:
            G.fillRect(0, 0, W, H);

            break;

        case WAITING:
            G.setColor(Color.GRAY);
            G.fillRect(0, 0, W, H);

            break;

        default:
            throw new RuntimeException("Not implemented!");
        }
    }
}
