// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.gui;

import org.jscience.physics.electricity.circuitry.CircuitElement;

import java.awt.*;
import java.awt.event.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class EditDialog extends Dialog implements AdjustmentListener,
    ActionListener, ItemListener {
    /**
     * DOCUMENT ME!
     */
    public CircuitElement elm;

    /**
     * DOCUMENT ME!
     */
    public CircuitFrame cframe;

    /**
     * DOCUMENT ME!
     */
    public Button applyButton;

    /**
     * DOCUMENT ME!
     */
    public Button okButton;

    /**
     * DOCUMENT ME!
     */
    public EditInfo[] einfos;

    /**
     * DOCUMENT ME!
     */
    public int einfocount;

    /**
     * DOCUMENT ME!
     */
    public final int barmax = 1000;

    /**
     * Creates a new EditDialog object.
     *
     * @param ce DOCUMENT ME!
     * @param f DOCUMENT ME!
     */
    public EditDialog(CircuitElement ce, CircuitFrame f) {
        super(f, "Edit Component", false);
        cframe = f;
        elm = ce;
        setLayout(new EditDialogLayout());
        einfos = new EditInfo[10];

        int i;

        for (i = 0;; i++) {
            einfos[i] = elm.getEditInfo(i);

            if (einfos[i] == null) {
                break;
            }

            EditInfo ei = einfos[i];
            add(new Label(ei.name));

            if (ei.choice != null) {
                add(ei.choice);
                ei.choice.addItemListener(this);
            } else if (ei.checkbox != null) {
                add(ei.checkbox);
                ei.checkbox.addItemListener(this);
            } else {
                add(ei.textf = new TextField(f.noCommaFormat.format(ei.value), 10));

                if (ei.text != null) {
                    ei.textf.setText(ei.text);
                }

                ei.textf.addActionListener(this);

                if (ei.text == null) {
                    add(ei.bar = new Scrollbar(Scrollbar.HORIZONTAL, 50, 10, 0,
                                barmax + 2));
                    setBar(ei);
                    ei.bar.addAdjustmentListener(this);
                }
            }
        }

        einfocount = i;
        add(applyButton = new Button("Apply"));
        applyButton.addActionListener(this);
        add(okButton = new Button("OK"));
        okButton.addActionListener(this);

        Point x = f.main.getLocationOnScreen();
        Dimension d = getSize();
        setLocation(x.x + ((cframe.winSize.width - d.width) / 2),
            x.y + ((cframe.winSize.height - d.height) / 2));
    }

    /**
     * DOCUMENT ME!
     */
    public void apply() {
        int i;

        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];

            if (ei.textf == null) {
                continue;
            }

            if (ei.text == null) {
                double d = new Double(ei.textf.getText()).doubleValue();
                ei.value = d;
            }

            elm.setEditValue(i, ei);

            if (ei.text == null) {
                setBar(ei);
            }
        }

        cframe.needAnalyze();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        int i;
        Object src = e.getSource();

        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];

            if (src == ei.textf) {
                if (ei.text == null) {
                    double d = new Double(ei.textf.getText()).doubleValue();
                    ei.value = d;
                }

                elm.setEditValue(i, ei);

                if (ei.text == null) {
                    setBar(ei);
                }

                cframe.needAnalyze();
            }
        }

        if (e.getSource() == okButton) {
            apply();
            cframe.main.requestFocus();
            setVisible(false);
            cframe.editDialog = null;
        }

        if (e.getSource() == applyButton) {
            apply();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        Object src = e.getSource();
        int i;

        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];

            if (ei.bar == src) {
                double v = ei.bar.getValue() / 1000.;

                if (v < 0) {
                    v = 0;
                }

                if (v > 1) {
                    v = 1;
                }

                ei.value = ((ei.maxval - ei.minval) * v) + ei.minval;

                if ((ei.maxval - ei.minval) > 100) {
                    ei.value = Math.round(ei.value);
                } else {
                    ei.value = Math.round(ei.value * 100) / 100.;
                }

                elm.setEditValue(i, ei);
                ei.textf.setText(cframe.noCommaFormat.format(ei.value));
                cframe.needAnalyze();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getItemSelectable();
        int i;

        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];

            if ((ei.choice == src) || (ei.checkbox == src)) {
                elm.setEditValue(i, ei);
                cframe.needAnalyze();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean handleEvent(Event ev) {
        if (ev.id == Event.WINDOW_DESTROY) {
            cframe.main.requestFocus();
            setVisible(false);
            cframe.editDialog = null;

            return true;
        }

        return super.handleEvent(ev);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ei DOCUMENT ME!
     */
    void setBar(EditInfo ei) {
        int x = (int) ((barmax * (ei.value - ei.minval)) / (ei.maxval -
            ei.minval));
        ei.bar.setValue(x);
    }
}
