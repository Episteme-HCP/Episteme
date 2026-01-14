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

package org.jscience.physics.solids.gui;

import org.jscience.physics.solids.gui.templates.AbstractTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Wegge
 */
public class AtlasMenu extends JMenuBar {

    private JMenu fileMenu = new JMenu("File");
    private JMenu aboutMenu = new JMenu("About");
    private JMenu prefMenu = new JMenu("Preferences");
    private JMenu templateMenu = new JMenu("Templates");

    private JFrame templateFrame;
    private AbstractTemplate currentTemplate;

    private AtlasGUI gui;
    private AtlasFileChooser fc = new AtlasFileChooser();

    /**
     * Creates a new instance of AtlasMenu
     */
    public AtlasMenu(AtlasGUI gui) {

        this.gui = gui;

        add(fileMenu);
        add(prefMenu);
        add(templateMenu);
        add(aboutMenu);

        initFileMenu();
        initTemplateMenu();

    }

    private void initTemplateFrame() {

        templateFrame = new JFrame();
        Container container = templateFrame.getContentPane();
        container.setLayout(new BorderLayout());
        templateFrame.setSize(300, 400);

        //Set up OK and cancel buttons
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                templateFrame.setVisible(false);
            }
        });

        //Finish panel layout
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel button2Panel = new JPanel(new BorderLayout());
        button2Panel.add(ok, BorderLayout.WEST);
        button2Panel.add(cancel, BorderLayout.EAST);
        buttonPanel.add(button2Panel, BorderLayout.EAST);
        container.add(buttonPanel, BorderLayout.SOUTH);


    }

    private void initTemplateMenu() {

        ArrayList<AbstractTemplate> templates = AbstractTemplate.getAvailableTemplates();

        for (int i = 0; i < templates.size(); i++) {

            JMenuItem item = new JMenuItem(templates.get(i).getName());
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    //Initialize template frame, if necessary
                    if (templateFrame == null) {
                        initTemplateFrame();
                    }

                    //Figure put which template is beoing called.
                    // This seems like a somewhat screwy manner of soing this
                    ArrayList<AbstractTemplate> t = AbstractTemplate.getAvailableTemplates();
                    String name = ((JMenuItem) e.getSource()).getText();

                    for (int i = 0; i < t.size(); i++) {
                        if (name.equals(t.get(i).getName())) {
                            currentTemplate = t.get(i);
                        }
                    }

                    //Set up the frame
                    templateFrame.setTitle(name);
                    templateFrame.setVisible(true);


                }
            });
            templateMenu.add(item);

        }

        return;
    }

    private void initFileMenu() {

        //Open
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int retVal = fc.showOpenDialog(gui);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    String filename = fc.getSelectedFile().getAbsolutePath();
                    gui.openAtlasModel(filename);
                }


            }
        });
        fileMenu.add(openItem);

        //Exit
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.exit();
            }
        });
        fileMenu.add(exitItem);
    }

}
