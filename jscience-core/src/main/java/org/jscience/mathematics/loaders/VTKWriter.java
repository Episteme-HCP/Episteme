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

package org.jscience.mathematics.loaders;

import org.jscience.io.AbstractResourceWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writer for VTK (Visualization Toolkit) Structured Points files.
 */
public class VTKWriter extends AbstractResourceWriter<double[][]> {
    
    @Override
    public Class<double[][]> getResourceType() {
        return double[][].class;
    }
    
    @Override
    public String getCategory() {
        return "Graphics";
    }

    @Override
    public String getName() {
        return "VTK Writer";
    }

    @Override
    public String getDescription() {
        return "Exports 2D data to VTK Structured Points format.";
    }

    /**
     * Saves 2D data matrix to VTK format.
     */
    @Override
    public void save(double[][] data, String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("# vtk DataFile Version 3.0\n");
            writer.write("JScience GCM Data\n");
            writer.write("ASCII\n");
            writer.write("DATASET STRUCTURED_POINTS\n");
            int rows = data.length;
            int cols = data[0].length;
            writer.write("DIMENSIONS " + cols + " " + rows + " 1\n");
            writer.write("ORIGIN 0 0 0\n");
            writer.write("SPACING 1 1 1\n");
            writer.write("POINT_DATA " + (rows * cols) + "\n");
            writer.write("SCALARS temperature double 1\n");
            writer.write("LOOKUP_TABLE default\n");
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(data[i][j] + " ");
                }
                writer.write("\n");
            }
        }
    }

    @Override
    public String getResourcePath() {
        return null;
    }
}
