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
