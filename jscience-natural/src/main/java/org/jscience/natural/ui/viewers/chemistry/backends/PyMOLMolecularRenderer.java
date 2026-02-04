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

package org.jscience.natural.ui.viewers.chemistry.backends;

import javafx.scene.paint.Color;
import org.jscience.natural.chemistry.Atom;
import org.jscience.natural.chemistry.Bond;
import org.jscience.natural.ui.viewers.chemistry.MolecularBackend;
import org.jscience.natural.ui.viewers.chemistry.MolecularRenderer;
import org.jscience.natural.ui.viewers.chemistry.RenderStyle;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * PyMOL-based molecular renderer backend.
 * <p>
 * Generates PyMOL scripts (.pml) and controls an external PyMOL process.
 * PyMOL must be installed and accessible in the system PATH.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PyMOLMolecularRenderer implements MolecularRenderer {

    private final JPanel panel;
    private final JTextArea statusArea;
    private final StringBuilder scriptBuffer = new StringBuilder();
    private RenderStyle currentStyle = RenderStyle.BALL_AND_STICK;
    private final List<Atom> atoms = new ArrayList<>();
    private final List<Bond> bonds = new ArrayList<>();

    public PyMOLMolecularRenderer() {
        panel = new JPanel(new BorderLayout());
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setText("PyMOL Renderer initialized.\nWaiting for commands...\n");
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        
        JButton launchButton = new JButton("Launch PyMOL");
        launchButton.addActionListener(e -> launchPyMOL());
        panel.add(launchButton, BorderLayout.SOUTH);
    }

    @Override
    public void clear() {
        scriptBuffer.setLength(0);
        atoms.clear();
        bonds.clear();
        statusArea.append("Cleared scene.\n");
    }

    @Override
    public void setStyle(RenderStyle style) {
        this.currentStyle = style;
        statusArea.append("Style set to: " + style + "\n");
    }

    @Override
    public void drawAtom(Atom atom) {
        atoms.add(atom);
    }

    @Override
    public void drawBond(Bond bond) {
        bonds.add(bond);
    }

    @Override
    public void setBackgroundColor(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        // PyMOL uses 0-1 range or color names, but we can define standard colors
        // set_color bg_color, [r, g, b]
        // actually 'bg_color' command takes name or rgb
        scriptBuffer.append(String.format("bg_color [%f, %f, %f]\n", color.getRed(), color.getGreen(), color.getBlue()));
    }

    @Override
    public Object getViewComponent() {
        return panel;
    }

    @Override
    public MolecularBackend getBackend() {
        return new PyMOLMolecularBackendProvider();
    }

    /**
     * Generates and launches PyMOL with the current scene.
     */
    public void launchPyMOL() {
        generateScript();
        
        try {
            File tempScript = File.createTempFile("jscience_pymol", ".pml");
            try (PrintWriter out = new PrintWriter(new FileWriter(tempScript))) {
                out.println(scriptBuffer.toString());
            }
            
            statusArea.append("Launching PyMOL with script: " + tempScript.getAbsolutePath() + "\n");
            
            // Attempt to find pymol executable
            String executable = "pymol";
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Common Windows paths? Or assume in PATH
                executable = "PyMOL.exe"; 
            }
            
            ProcessBuilder pb = new ProcessBuilder(executable, tempScript.getAbsolutePath());
            pb.start();
            
        } catch (IOException e) {
            statusArea.append("Error launching PyMOL: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void generateScript() {
        // Clear buffer partially but keep background settings if any
        // Actually, let's just rebuild the geometric part
        StringBuilder geom = new StringBuilder();
        
        // Define a pseudo-PDB or CGO (Compiled Graphics Object)
        // Creating CGOs is complex. Simpler is to write a PDB file and load it.
        
        try {
            File tempPdb = File.createTempFile("jscience_mol", ".pdb");
            writePDB(tempPdb);
            geom.append("load ").append(tempPdb.getAbsolutePath()).append("\n");
            
            // Apply style
            geom.append("hide all\n");
            switch (currentStyle) {
                case SPACEFILL:
                    geom.append("show spheres\n");
                    break;
                case WIREFRAME:
                    geom.append("show lines\n");
                    break;
                case BALL_AND_STICK:
                default:
                    geom.append("show sticks\n");
                    geom.append("show spheres\n");
                    geom.append("set sphere_scale, 0.25\n");
                    break;
            }
            
        } catch (IOException e) {
            statusArea.append("Error generating PDB: " + e.getMessage() + "\n");
        }
        
        scriptBuffer.append(geom);
    }

    private void writePDB(File file) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            int serial = 1;
            for (Atom atom : atoms) {
                // ATOM      1  N   ALA A   1      11.104   6.134  -6.504  1.00  0.00           N
                // Simple PDB generation
                double x = atom.getPosition().get(0).doubleValue();
                double y = atom.getPosition().get(1).doubleValue();
                double z = atom.getPosition().get(2).doubleValue();
                String symbol = atom.getElement().getSymbol();
                
                // Formatting is critical for PDB
                out.printf("ATOM  %5d %-4s MOL     1    %8.3f%8.3f%8.3f  1.00  0.00          %2s%n",
                        serial++, symbol, x, y, z, symbol.toUpperCase());
            }
            // Explicit bonds? PDB viewers usually infer checks distance, but we can write CONECT records
        }
    }
}
