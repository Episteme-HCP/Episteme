package org.jscience.tests.net.ntp.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class ServerChooser extends JPanel implements ActionListener {
    /** DOCUMENT ME! */
    JTextField serverField = new JTextField();

    /** DOCUMENT ME! */
    JButton trace = new JButton("Trace");

    /** DOCUMENT ME! */
    TraceDialog traceDialog;

/**
     * Creates a new ServerChooser object.
     *
     * @param propertiesListener DOCUMENT ME!
     */
    protected ServerChooser(PropertiesListener propertiesListener) {
        traceDialog = new TraceDialog(propertiesListener);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        serverField.setAlignmentY((float) 0.5);
        trace.setAlignmentY((float) 0.5);
        add(serverField);
        serverField.setColumns(20);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(trace);
        trace.addActionListener(this);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ntp Server"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getServer() {
        return serverField.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param defaultServer DOCUMENT ME!
     */
    protected void setServer(String defaultServer) {
        serverField.setText(defaultServer);
        serverField.setColumns(20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ae) {
        //    trace.setEnabled(false);
        traceDialog.showTrace(getServer(), trace);

        //    trace.setEnabled(true);
    }
}
