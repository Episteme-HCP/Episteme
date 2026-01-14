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

package org.jscience.chemistry.gui.extended;

import org.jscience.chemistry.gui.extended.beans.CentralDisplayAdapter;
import org.jscience.chemistry.gui.extended.beans.CentralLookup;
import org.jscience.chemistry.gui.extended.graphics3d.Panel3D;
import org.jscience.chemistry.gui.extended.graphics3d.RenderStyle;
import org.jscience.chemistry.gui.extended.molecule.MolTranslateMOL2;
import org.jscience.chemistry.gui.extended.molecule.Molecule;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SimpleViewer extends Panel {
    /** DOCUMENT ME! */
    private MenuBar menubar;

    /** DOCUMENT ME! */
    private Container status;

    /** DOCUMENT ME! */
    private Panel3D molPanel;

    /** DOCUMENT ME! */
    CentralLookup lookup;

    /** DOCUMENT ME! */
    Frame parent;

/**
     * Creates a new SimpleViewer object.
     */
    SimpleViewer() {
        super();
        lookup = CentralLookup.getLookup();
        setLayout(new BorderLayout());

        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        molPanel = new Panel3D();

        CentralDisplayAdapter c = new CentralDisplayAdapter(molPanel);
        lookup.addObject("CentralDisplay", c);

        panel.add("Center", molPanel);
        add("Center", panel);
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected MenuBar createMenubar(Frame f) {
        parent = f;

        MenuBar mb = new MenuBar();
        Menu m = new Menu("File");
        m.add("Open");
        m.addSeparator();
        m.add("Exit");
        m.addActionListener(new FileActionHandler());
        mb.add(m);
        m = new Menu("View");
        m.add("Ball and Stick");
        m.add("Stick");
        m.add("Wire");
        m.add("Spacefill");
        m.addActionListener(new RenderChooser());
        mb.add(m);

        m = new Menu("Help");
        m.add("Usage");
        m.add("About");
        m.addActionListener(new HelpHandler());
        mb.add(m);

        f.setMenuBar(mb);

        return mb;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    protected void addMolecule(Molecule m) {
        molPanel.addMolecule(m);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            String vers = System.getProperty("java.version");

            if (vers.compareTo("1.2") < 0) {
                System.out.println(
                    "!!!WARNING: SimpleViewer must be run with a " +
                    "1.2 or higher version VM!!!");

                return;
            }

            Frame frame = new Frame();
            frame.setTitle("Tripos SimpleViewer");
            frame.setBackground(Color.lightGray);

            SimpleViewer mv = new SimpleViewer();
            mv.createMenubar(frame);

            if (args.length > 0) {
                FileInputStream inp = new FileInputStream(args[0]);
                Molecule m = MolTranslateMOL2.toMolecule(inp);
                mv.addMolecule(m);
            }

            frame.add("Center", mv);
            frame.addWindowListener(new AppCloser());
            frame.pack();
            frame.setSize(600, 500);
            frame.show();
        } catch (Throwable t) {
            System.out.println("uncaught exception: " + t);
            t.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected final class HelpHandler implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param resourceName DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws IOException DOCUMENT ME!
         */
        String readResource(String resourceName) throws IOException {
            InputStream is = getClass().getResourceAsStream(resourceName);

            if (is == null) {
                //System.out.println("Resource could not be found !!!");
                throw new IOException("Resource could not be found !!!");
            }

            InputStreamReader isr = new InputStreamReader(is);
            CharArrayWriter caw = new CharArrayWriter();
            int c = -1;

            while ((c = isr.read()) != -1) {
                caw.write(c);
            }

            return caw.toString();
        }

        /**
         * DOCUMENT ME!
         *
         * @param resourceName DOCUMENT ME!
         */
        void showMessage(String resourceName) {
            try {
                String usage = readResource(resourceName);
                Dialog box = new Dialog(parent, false);
                TextArea text = new TextArea();
                text.setText(usage);
                box.add("Center", text);

                Button b = new Button("OK");
                box.add("South", b);
                b.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            Button bb = (Button) e.getSource();
                            Dialog d = (Dialog) bb.getParent();
                            d.dispose();
                        }
                    });
                box.pack();
                box.show();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            //System.out.println(command);
            if (command.equalsIgnoreCase("Usage")) {
                showMessage("Usage.def");
            } else if (command.equalsIgnoreCase("About")) {
                showMessage("About.def");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected final class FileActionHandler implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            //System.out.println(command);
            if (command.equalsIgnoreCase("Exit")) {
                System.exit(0);
            } else if (command.equalsIgnoreCase("Open")) {
                FileDialog fd = new FileDialog(parent, "Open Mol2 File",
                        FileDialog.LOAD);
                fd.setModal(true);
                fd.setFilenameFilter(new Mol2FileFilter());
                fd.show();

                String file = fd.getFile();

                if (file != null) {
                    String dir = fd.getDirectory();

                    try {
                        FileInputStream finp = new FileInputStream(dir +
                                File.separator + file);
                        Molecule m = MolTranslateMOL2.toMolecule(finp);
                        molPanel.clear();
                        molPanel.addMolecule(m);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected final class RenderChooser implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            //System.out.println(command);
            if (command.equalsIgnoreCase("Ball and Stick")) {
                molPanel.setRenderStyle(RenderStyle.BALL_AND_STICK);
            } else if (command.equalsIgnoreCase("Stick")) {
                molPanel.setRenderStyle(RenderStyle.STICK);
            } else if (command.equalsIgnoreCase("Spacefill")) {
                molPanel.setRenderStyle(RenderStyle.CPK);
            } else if (command.equalsIgnoreCase("Wire")) {
                molPanel.setRenderStyle(RenderStyle.WIRE);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected static final class AppCloser extends WindowAdapter {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
