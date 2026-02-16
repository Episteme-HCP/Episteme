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
 * VMD-based molecular renderer backend.
 * <p>
 * Generates VMD scripts (.tcl) and controls an external VMD process.
 * VMD must be installed and accessible in the system PATH.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VMDMolecularRenderer implements MolecularRenderer {

    private final JPanel panel;
    private final JTextArea statusArea;
    private RenderStyle currentStyle = RenderStyle.BALL_AND_STICK;
    private final List<Atom> atoms = new ArrayList<>();
    
    public VMDMolecularRenderer() {
        panel = new JPanel(new BorderLayout());
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setText("VMD Renderer initialized.\nWaiting for commands...\n");
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        
        JButton launchButton = new JButton("Launch VMD");
        launchButton.addActionListener(e -> launchVMD());
        panel.add(launchButton, BorderLayout.SOUTH);
    }

    @Override
    public void clear() {
        atoms.clear();
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
        // VMD typically infers bonds or reads from file structure (PDB connect)
    }

    @Override
    public void setBackgroundColor(Color color) {
        // VMD script command: color Display Background <color_name>
        // Or using RGB index... complicate.
    }

    @Override
    public Object getViewComponent() {
        return panel;
    }

    @Override
    public MolecularBackend getBackend() {
        return new VMDMolecularBackend();
    }

    public void launchVMD() {
         try {
            File tempPdb = File.createTempFile("jscience_vmd", ".pdb");
            writePDB(tempPdb);
            
            File tempScript = File.createTempFile("jscience_vmd_run", ".tcl");
            try (PrintWriter out = new PrintWriter(new FileWriter(tempScript))) {
                out.println("mol new {" + tempPdb.getAbsolutePath().replace("\\", "/") + "} type pdb waitfor all");
                
                // Style
                out.println("mol delrep 0 top");
                out.println("mol representation " + getVMDStyle());
                out.println("mol addrep top");
            }
            
            statusArea.append("Launching VMD with script: " + tempScript.getAbsolutePath() + "\n");
            
            String executable = "vmd";
            ProcessBuilder pb = new ProcessBuilder(executable, "-e", tempScript.getAbsolutePath());
            pb.start();
            
        } catch (IOException e) {
            statusArea.append("Error launching VMD: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
    
    private String getVMDStyle() {
        switch (currentStyle) {
            case SPACEFILL: return "VDW";
            case WIREFRAME: return "Lines";
            case BALL_AND_STICK: return "CPK";
            default: return "Licorice";
        }
    }

    private void writePDB(File file) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            int serial = 1;
            for (Atom atom : atoms) {
                double x = atom.getPosition().get(0).doubleValue();
                double y = atom.getPosition().get(1).doubleValue();
                double z = atom.getPosition().get(2).doubleValue();
                String symbol = atom.getElement().getSymbol();
                
                out.printf("ATOM  %5d %-4s MOL     1    %8.3f%8.3f%8.3f  1.00  0.00          %2s%n",
                        serial++, symbol, x, y, z, symbol.toUpperCase());
            }
            out.println("END");
        }
    }
}
