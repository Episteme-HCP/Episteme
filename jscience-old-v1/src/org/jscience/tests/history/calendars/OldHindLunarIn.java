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

//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
import org.jscience.history.calendars.MonthDayYear;

import java.awt.*;

import java.util.Enumeration;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class OldHindLunarIn extends MDYInput {
    /**
     * DOCUMENT ME!
     */
    Checkbox leapmo;

    /**
     * Creates a new OldHindLunarIn object.
     *
     * @param monthdayyear DOCUMENT ME!
     */
    public OldHindLunarIn(MonthDayYear monthdayyear) {
        initialize(monthdayyear);
    }

    /**
     * DOCUMENT ME!
     *
     * @param monthdayyear DOCUMENT ME!
     */
    protected void initialize(MonthDayYear monthdayyear) {
        super.mo = new Choice();

        for (Enumeration enumeration = monthdayyear.getMonths();
                enumeration.hasMoreElements();
                super.mo.addItem((String) enumeration.nextElement()))
            ;

        super.dy = new TextField(4);
        super.yr = new TextField(6);
        super.lay = new GridBagLayout();
        setLayout(super.lay);

        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        gridbagconstraints.ipadx = 5;
        gridbagconstraints.gridwidth = 2;
        gridbagconstraints.gridheight = 1;
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        gridbagconstraints.anchor = 13;

        Label label;
        add(label = new Label("Day:", 2));
        super.lay.setConstraints(label, gridbagconstraints);
        gridbagconstraints.gridx += 2;
        gridbagconstraints.anchor = 17;
        add(super.dy);
        super.lay.setConstraints(super.dy, gridbagconstraints);
        gridbagconstraints.gridx += 2;
        gridbagconstraints.anchor = 17;
        add(label = new Label("Month:", 2));
        super.lay.setConstraints(label, gridbagconstraints);
        gridbagconstraints.gridx += 2;
        gridbagconstraints.anchor = 17;
        add(super.mo);
        super.lay.setConstraints(super.mo, gridbagconstraints);
        gridbagconstraints.gridx += 2;

        double d = gridbagconstraints.weightx;
        gridbagconstraints.weightx = 0.0D;
        gridbagconstraints.gridwidth = 1;
        leapmo = new Checkbox("Leap?");
        leapmo.setState(false);
        leapmo.setFont(new Font("Helvetica", 0, 8));
        add(leapmo);
        super.lay.setConstraints(leapmo, gridbagconstraints);
        gridbagconstraints.weightx = d;
        gridbagconstraints.gridwidth = 2;
        gridbagconstraints.gridx++;
        gridbagconstraints.anchor = 17;
        add(label = new Label("Year:", 2));
        super.lay.setConstraints(label, gridbagconstraints);
        gridbagconstraints.gridx += 2;
        gridbagconstraints.anchor = 17;
        add(super.yr);
        super.lay.setConstraints(super.yr, gridbagconstraints);
        super.cal = monthdayyear;
    }

    /**
     * DOCUMENT ME!
     */
    public void updateCal() {
        ((OldHinduLunar) super.cal).set(getMo(), leapmo.getState(), getDy(),
            getYr());
        super.cal.set(super.cal.toRD());
    }

    /**
     * DOCUMENT ME!
     */
    public void updateDisplay() {
        super.mo.select(super.cal.getMonth() - 1);
        super.dy.setText(String.valueOf(super.cal.getDay()));
        super.yr.setText(String.valueOf(super.cal.getYear()));
        leapmo.setState(((OldHinduLunar) super.cal).getLeap());
    }
}
