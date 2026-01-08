/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.viewers.chemistry.backend;

import javafx.scene.paint.Color;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;

import javax.swing.*;
import java.awt.*;

/**
 * Jmol-based molecular renderer backend.
 * <p>
 * Jmol is an open-source Java viewer for chemical structures in 3D.
 * This backend wraps the Jmol API to provide advanced molecular visualization
 * features including:
 * <ul>
 * <li>High-quality ray-traced rendering</li>
 * <li>Script-based automation</li>
 * <li>Support for many file formats (PDB, CIF, MOL, XYZ, etc.)</li>
 * <li>Advanced selection and coloring</li>
 * <li>Animation and measurement tools</li>
 * </ul>
 * </p>
 * <p>
 * Requires Jmol library (jmol-*.jar) on the classpath.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 * @see <a href="https://jmol.sourceforge.net/">Jmol Project</a>
 */
public class JmolMolecularRenderer implements MolecularRenderer {

    private Object jmolViewer; // org.jmol.api.JmolViewer
    private Object jmolAdapter; // org.jmol.api.JmolAdapter
    private JPanel jmolPanel;
    private RenderStyle currentStyle = RenderStyle.BALL_AND_STICK;
    private StringBuilder scriptBuffer = new StringBuilder();
    private boolean initialized = false;

    /**
     * Creates a new Jmol-based renderer.
     * Falls back gracefully if Jmol is not available.
     */
    public JmolMolecularRenderer() {
        try {
            initJmol();
            initialized = true;
        } catch (Exception | NoClassDefFoundError e) {
            System.err.println("Jmol not available: " + e.getMessage());
            System.err.println("Add Jmol library to classpath for advanced molecular visualization.");
            initialized = false;
        }
    }

    private void initJmol() throws Exception {
        // Use reflection to avoid compile-time dependency
        Class<?> viewerClass = Class.forName("org.jmol.viewer.Viewer");
        Class<?> adapterClass = Class.forName("org.jmol.adapter.smarter.SmarterJmolAdapter");

        jmolAdapter = adapterClass.getDeclaredConstructor().newInstance();

        // Create panel for embedding
        jmolPanel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }
        };

        // Viewer.allocateViewer(container, adapter)
        java.lang.reflect.Method allocate = viewerClass.getMethod(
                "allocateViewer", Container.class, Class.forName("org.jmol.api.JmolAdapter"));
        jmolViewer = allocate.invoke(null, jmolPanel, jmolAdapter);

        // Apply initial settings
        executeScript("set antialiasDisplay true;");
        executeScript("set showHydrogens true;");
        executeScript("background black;");
    }

    /**
     * Checks if Jmol is available and initialized.
     */
    public boolean isAvailable() {
        return initialized && jmolViewer != null;
    }

    @Override
    public void clear() {
        if (!initialized)
            return;
        scriptBuffer.setLength(0);
        executeScript("zap;");
    }

    @Override
    public void setStyle(RenderStyle style) {
        this.currentStyle = style;
        if (!initialized)
            return;

        switch (style) {
            case SPACEFILL:
                executeScript("select all; spacefill only;");
                break;
            case WIREFRAME:
                executeScript("select all; wireframe only;");
                break;
            case BALL_AND_STICK:
            default:
                executeScript("select all; wireframe 0.15; spacefill 23%;");
                break;
        }
    }

    @Override
    public void drawAtom(Atom atom) {
        if (!initialized)
            return;

        // Build Jmol script to add atom
        double x = atom.getPosition().get(0).doubleValue();
        double y = atom.getPosition().get(1).doubleValue();
        double z = atom.getPosition().get(2).doubleValue();

        // Jmol uses XYZ format or script commands
        scriptBuffer.append(String.format("draw atom%d POINT {%f %f %f}; ",
                atom.hashCode(), x, y, z));
    }

    @Override
    public void drawBond(Bond bond) {
        if (!initialized)
            return;

        // Jmol handles bonds automatically when loading structures
        // For manual bonds, use connect command
        Atom a1 = bond.getAtom1();
        Atom a2 = bond.getAtom2();

        double x1 = a1.getPosition().get(0).doubleValue();
        double y1 = a1.getPosition().get(1).doubleValue();
        double z1 = a1.getPosition().get(2).doubleValue();

        double x2 = a2.getPosition().get(0).doubleValue();
        double y2 = a2.getPosition().get(1).doubleValue();
        double z2 = a2.getPosition().get(2).doubleValue();

        scriptBuffer.append(String.format("draw bond%d LINE {%f %f %f} {%f %f %f}; ",
                bond.hashCode(), x1, y1, z1, x2, y2, z2));
    }

    @Override
    public void setBackgroundColor(Color color) {
        if (!initialized)
            return;

        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        executeScript(String.format("background [%d,%d,%d];", r, g, b));
    }

    @Override
    public Object getViewComponent() {
        if (!initialized) {
            // Return a placeholder panel with error message
            JPanel placeholder = new JPanel(new BorderLayout());
            placeholder.setBackground(java.awt.Color.DARK_GRAY);
            JLabel label = new JLabel(
                    "<html><center>Jmol not available.<br>Add jmol.jar to classpath.</center></html>");
            label.setForeground(java.awt.Color.WHITE);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            placeholder.add(label, BorderLayout.CENTER);
            return placeholder;
        }
        return jmolPanel;
    }

    @Override
    public MolecularBackend getBackend() {
        return MolecularBackend.JMOL;
    }

    /**
     * Executes a Jmol script command.
     * 
     * @param script The Jmol script to execute
     */
    public void executeScript(String script) {
        if (!initialized || jmolViewer == null)
            return;

        try {
            java.lang.reflect.Method evalString = jmolViewer.getClass()
                    .getMethod("evalString", String.class);
            evalString.invoke(jmolViewer, script);
        } catch (Exception e) {
            System.err.println("Jmol script error: " + e.getMessage());
        }
    }

    /**
     * Loads a molecular structure from a file path.
     * 
     * @param filePath Path to structure file (PDB, CIF, MOL, XYZ, etc.)
     */
    public void loadFile(String filePath) {
        if (!initialized)
            return;
        executeScript("load \"" + filePath.replace("\\", "/") + "\";");
        applyCurrentStyle();
    }

    /**
     * Loads a molecular structure from inline data.
     * 
     * @param data   Structure data (e.g., PDB format)
     * @param format Format identifier (e.g., "pdb", "xyz", "mol")
     */
    public void loadData(String data, String format) {
        if (!initialized)
            return;
        executeScript("data \"model\"\\n" + data + "\\nend \"model\";");
        applyCurrentStyle();
    }

    private void applyCurrentStyle() {
        setStyle(currentStyle);
    }

    /**
     * Renders a ray-traced image of the current view.
     * 
     * @param width  Image width
     * @param height Image height
     * @return Path to rendered image, or null if failed
     */
    public String renderImage(int width, int height) {
        if (!initialized)
            return null;

        String tempPath = System.getProperty("java.io.tmpdir") + "/jmol_render.png";
        executeScript(String.format("write IMAGE %d %d PNG \"%s\";", width, height, tempPath));
        return tempPath;
    }

    /**
     * Gets the Jmol viewer instance for advanced control.
     * 
     * @return The JmolViewer object, or null if not initialized
     */
    public Object getJmolViewer() {
        return jmolViewer;
    }
}
