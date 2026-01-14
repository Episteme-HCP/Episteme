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
