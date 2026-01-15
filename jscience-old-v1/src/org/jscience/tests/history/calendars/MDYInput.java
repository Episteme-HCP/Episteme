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
class MDYInput extends CalInputter {
    /**
     * DOCUMENT ME!
     */
    Choice mo;

    /**
     * DOCUMENT ME!
     */
    TextField dy;

    /**
     * DOCUMENT ME!
     */
    TextField yr;

    /**
     * DOCUMENT ME!
     */
    MonthDayYear cal;

    /**
     * DOCUMENT ME!
     */
    GridBagLayout lay;

    /**
     * Creates a new MDYInput object.
     */
    protected MDYInput() {
    }

    /**
     * Creates a new MDYInput object.
     *
     * @param monthdayyear DOCUMENT ME!
     */
    public MDYInput(MonthDayYear monthdayyear) {
        initialize(monthdayyear);
    }

    /**
     * DOCUMENT ME!
     *
     * @param monthdayyear DOCUMENT ME!
     */
    protected void initialize(MonthDayYear monthdayyear) {
        mo = new Choice();

        for (Enumeration enumeration = monthdayyear.getMonths();
                enumeration.hasMoreElements();
                mo.addItem((String) enumeration.nextElement()))
            ;

        dy = new TextField(4);
        yr = new TextField(6);
        lay = new GridBagLayout();
        setLayout(lay);

        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        gridbagconstraints.ipadx = 5;
        gridbagconstraints.gridwidth = 1;
        gridbagconstraints.gridheight = 1;
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        gridbagconstraints.anchor = 13;

        Label label;
        add(label = new Label("Day:", 2));
        lay.setConstraints(label, gridbagconstraints);
        gridbagconstraints.gridx++;
        gridbagconstraints.anchor = 17;
        add(dy);
        lay.setConstraints(dy, gridbagconstraints);
        gridbagconstraints.gridx++;
        gridbagconstraints.anchor = 17;
        add(label = new Label("Month:", 2));
        lay.setConstraints(label, gridbagconstraints);
        gridbagconstraints.gridx++;
        gridbagconstraints.anchor = 17;
        add(mo);
        lay.setConstraints(mo, gridbagconstraints);
        gridbagconstraints.gridx++;
        gridbagconstraints.anchor = 17;
        add(label = new Label("Year:", 2));
        lay.setConstraints(label, gridbagconstraints);
        gridbagconstraints.gridx++;
        gridbagconstraints.anchor = 17;
        add(yr);
        lay.setConstraints(yr, gridbagconstraints);
        cal = monthdayyear;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMo() {
        return mo.getSelectedIndex() + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDy() {
        return Integer.parseInt(dy.getText());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getYr() {
        return Integer.parseInt(yr.getText());
    }

    /**
     * DOCUMENT ME!
     */
    public void updateCal() {
        cal.set(getMo(), getDy(), getYr());
        cal.set(cal.toRD());
    }

    /**
     * DOCUMENT ME!
     */
    public void updateDisplay() {
        mo.select(cal.getMonth() - 1);
        dy.setText(String.valueOf(cal.getDay()));
        yr.setText(String.valueOf(cal.getYear()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param altcalendar DOCUMENT ME!
     */
    public void changeCal(AltCalendar altcalendar) {
        cal.set(altcalendar.toRD());
        updateDisplay();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AltCalendar getCal() {
        return cal;
    }
}
