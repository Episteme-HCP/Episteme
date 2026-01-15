// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ImportDialog extends Dialog implements ActionListener {
    /**
     * DOCUMENT ME!
     */
    public CircuitFrame cframe;

    /**
     * DOCUMENT ME!
     */
    public Button importButton;

    /**
     * DOCUMENT ME!
     */
    public Button closeButton;

    /**
     * DOCUMENT ME!
     */
    public TextArea text;

    /**
     * Creates a new ImportDialog object.
     *
     * @param f DOCUMENT ME!
     * @param str DOCUMENT ME!
     */
    public ImportDialog(CircuitFrame f, String str) {
        super(f, (str.length() > 0) ? "Export" : "Import", false);
        cframe = f;
        setLayout(new ImportDialogLayout());
        add(text = new TextArea(str, 10, 60, TextArea.SCROLLBARS_BOTH));
        add(importButton = new Button("Import"));
        importButton.addActionListener(this);
        add(closeButton = new Button("Close"));
        closeButton.addActionListener(this);

        Point x = f.main.getLocationOnScreen();
        resize(400, 300);

        Dimension d = getSize();
        setLocation(x.x + ((f.winSize.width - d.width) / 2),
            x.y + ((f.winSize.height - d.height) / 2));
        show();

        if (str.length() > 0) {
            text.selectAll();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        int i;
        Object src = e.getSource();

        if (src == importButton) {
            cframe.readSetup(text.getText());
            setVisible(false);
        }

        if (src == closeButton) {
            setVisible(false);
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
}
