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

import org.jscience.architecture.lift.Car;
import org.jscience.architecture.lift.World;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public class CarRenderer {
    /**
     * DOCUMENT ME!
     */
    Car C = null;

    /**
     * DOCUMENT ME!
     */
    CarCanvas[] CCs = null;

    /**
     * Creates a new CarRenderer object.
     *
     * @param MyCar DOCUMENT ME!
     * @param CarCanvases DOCUMENT ME!
     */
    public CarRenderer(Car MyCar, CarCanvas[] CarCanvases) {
        C = MyCar;
        CCs = CarCanvases;
    }

    /**
     * DOCUMENT ME!
     */
    public void renderCar() {
        int CarsFloor = C.getCrtF();

        for (int i = 0; i < CCs.length; i++) {
            CCs[i].setCarPresent(i == CarsFloor);
        }

        int PassNum = C.getNoPs();
        int Capacity = C.getCapacity();

        CCs[CarsFloor].setActNumber(PassNum);
        CCs[CarsFloor].setMaxNumber(Capacity);

        int[] DstFloors = World.getPassengerDstFloorsInCar(C);

        CCs[CarsFloor].setDstFloors(DstFloors);

        switch (C.getState()) {
        case Car.PARKING:
            CCs[CarsFloor].setState(CarCanvas.PARKING);

            break;

        case Car.CLOSING:
            CCs[CarsFloor].setState(CarCanvas.CLOSE_OPEN);
            CCs[CarsFloor].setProgress(C.getKinematicModel().getProgress(C));

            break;

        case Car.OPENING:
            CCs[CarsFloor].setState(CarCanvas.CLOSE_OPEN);
            CCs[CarsFloor].setProgress(C.getKinematicModel().getProgress(C));

            break;

        case Car.WAITING:
            CCs[CarsFloor].setState(CarCanvas.WAITING);

            break;

        case Car.GOING_UP:
        case Car.GOING_DOWN:

            double P = C.getKinematicModel().getProgress(C);
            CCs[CarsFloor].setProgress(P);

            if (C.getState() == Car.GOING_UP) {
                CCs[CarsFloor].setState(CarCanvas.GOING_UP);

                if (CarsFloor != (CCs.length - 1)) {
                    CCs[CarsFloor + 1].setCarPresent(true);
                    CCs[CarsFloor + 1].setState(CarCanvas.ARRIVING_UP);
                    CCs[CarsFloor + 1].setProgress(P);
                    CCs[CarsFloor + 1].setActNumber(PassNum);
                    CCs[CarsFloor + 1].setMaxNumber(Capacity);
                    CCs[CarsFloor + 1].setDstFloors(DstFloors);
                }
            } else {
                CCs[CarsFloor].setState(CarCanvas.GOING_DOWN);

                if (CarsFloor != 0) {
                    CCs[CarsFloor - 1].setCarPresent(true);
                    CCs[CarsFloor - 1].setState(CarCanvas.ARRIVING_DOWN);
                    CCs[CarsFloor - 1].setProgress(P);
                    CCs[CarsFloor - 1].setActNumber(PassNum);
                    CCs[CarsFloor - 1].setMaxNumber(Capacity);
                    CCs[CarsFloor - 1].setDstFloors(DstFloors);
                }
            }

            break;

        default:
            throw new RuntimeException("Not implemented!");
        }

        for (int i = CarsFloor - 1; i <= (CarsFloor + 1); i++) {
            if ((i >= 0) && (i < CCs.length)) {
                ((JPanel) CCs[i]).revalidate();
                ((JPanel) CCs[i]).repaint();
            }
        }
    }
}
