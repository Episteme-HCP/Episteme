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

package org.jscience.io.scientific;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.jscience.io.AbstractResourceWriter;

/**
 * Simple loader/saver for VTK (Visualization Toolkit) files.
 * Currently supports saving 2D scalar fields (structured points).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VTKLoader extends AbstractResourceWriter<double[][]> {

    @Override
    public Class<double[][]> getResourceType() {
        return double[][].class;
    }

    @Override
    public String getResourcePath() {
        return null;
    }

    public VTKLoader() {
    }

    /**
     * Saves a 2D scalar field to a legacy VTK file.
     *
     * @param data     The 2D data array [height][width].
     * @param filePath The destination file path.
     * @throws IOException If an I/O error occurs.
     */
    public void save(double[][] data, String filePath) throws IOException {
        int height = data.length;
        int width = data[0].length;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("# vtk DataFile Version 2.0\n");
            writer.write("JScience Export\n");
            writer.write("ASCII\n");
            writer.write("DATASET STRUCTURED_POINTS\n");
            writer.write("DIMENSIONS " + width + " " + height + " 1\n");
            writer.write("ORIGIN 0 0 0\n");
            writer.write("SPACING 1 1 1\n");
            writer.write("POINT_DATA " + (width * height) + "\n");
            writer.write("SCALARS scalars double\n");
            writer.write("LOOKUP_TABLE default\n");

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(data[i][j] + " ");
                }
                writer.write("\n");
            }
        }
    }
}
