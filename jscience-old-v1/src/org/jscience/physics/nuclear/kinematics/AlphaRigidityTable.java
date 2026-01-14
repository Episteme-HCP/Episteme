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

/*
 * Created on Jun 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jscience.physics.nuclear.kinematics;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearCollision;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;

/**
 * @author Administrator
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AlphaRigidityTable {

    UncertainNumber[] energies = new UncertainNumber[4];

    public AlphaRigidityTable() {
        try {
            Nucleus a = new Nucleus(2, 4);
            energies[0] = new UncertainNumber(5.44298, 0.00013);
            energies[1] = new UncertainNumber(5.48574, 0.00012);
            energies[2] = new UncertainNumber(5.762835, 0.000030);
            energies[3] = new UncertainNumber(5.80496, 0.00005);
            System.out.println("Ealpha, Brho");
            for (int i = energies.length - 1; i >= 0; i--) {
                System.out.println(energies[i] + "," +
                        NuclearCollision.getQBrho(a, energies[i], false).divide(a.Z));
            }
        } catch (NuclearException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new AlphaRigidityTable();
    }
}
