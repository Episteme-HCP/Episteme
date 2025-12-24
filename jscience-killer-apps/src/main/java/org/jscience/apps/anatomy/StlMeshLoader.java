/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.anatomy;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A simple loader for STL (Stereolithography) files.
 * Supports Binary STL format (most common for anatomy data).
 */
public class StlMeshLoader {

    public static MeshView load(String filePath) throws IOException {
        return load(new File(filePath));
    }

    public static MeshView load(File file) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            return load(is);
        }
    }

    public static MeshView load(InputStream in) throws IOException {
        // Read into buffer to check header or just assume binary for now
        // Standard Binary STL: 80 byte header, 4 byte int count

        byte[] allBytes = in.readAllBytes();

        if (isAscii(allBytes)) {
            throw new UnsupportedOperationException("ASCII STL not yet implemented. Please use Binary STL.");
        }

        return loadBinary(allBytes);
    }

    private static boolean isAscii(byte[] bytes) {
        // Simple heuristic: check if start with "solid"
        if (bytes.length < 5)
            return false;
        String start = new String(bytes, 0, 5);
        return "solid".equalsIgnoreCase(start);
    }

    private static MeshView loadBinary(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN); // STL is Little Endian

        // Skip header
        if (bb.remaining() < 84)
            return new MeshView(); // Empty

        bb.position(80);
        int triangleCount = bb.getInt();

        if (triangleCount <= 0)
            return new MeshView();

        float[] points = new float[triangleCount * 3 * 3]; // 3 vertices * 3 coords
        int[] faces = new int[triangleCount * 6]; // 3 vertices * (pointIdx + texIdx)

        // For flat shading, we might want unique vertices but STL duplicates them.
        // For smooth shading we'd need to weld vertices.
        // For simplicity and speed: just load as raw triangle soup (flat).
        // JavaFX TriangleMesh needs points and faces.
        // Since STL has no indexing, we just add every vertex.

        // Note: JavaFX 8/9+ TriangleMesh limit is rather high, but strictly < 64k
        // vertices for older versions.
        // Modern JavaFX supports larger meshes.

        // Optimization: We could use a map to weld vertices to reduce memory,
        // but for a viewer, raw triangle soup is fast to load.

        int pIdx = 0;
        int fIdx = 0;

        // Dummy Texture Coordinate (0,0)
        float[] texCoords = new float[] { 0, 0 };

        for (int i = 0; i < triangleCount; i++) {
            // Normal (3 floats) - skip, JavaFX calculates face normals
            bb.getFloat(); // nx
            bb.getFloat(); // ny
            bb.getFloat(); // nz

            // Vertex 1
            float v1x = bb.getFloat();
            float v1y = bb.getFloat();
            float v1z = bb.getFloat();

            // Vertex 2
            float v2x = bb.getFloat();
            float v2y = bb.getFloat();
            float v2z = bb.getFloat();

            // Vertex 3
            float v3x = bb.getFloat();
            float v3y = bb.getFloat();
            float v3z = bb.getFloat();

            // Attribute byte count (uint16) - skip
            bb.getShort();

            // Add points
            int baseV = pIdx / 3;

            points[pIdx++] = v1x;
            points[pIdx++] = v1y;
            points[pIdx++] = v1z;

            points[pIdx++] = v2x;
            points[pIdx++] = v2y;
            points[pIdx++] = v2z;

            points[pIdx++] = v3x;
            points[pIdx++] = v3y;
            points[pIdx++] = v3z;

            // Add face (v1, t0, v2, t0, v3, t0)
            faces[fIdx++] = baseV;
            faces[fIdx++] = 0;
            faces[fIdx++] = baseV + 1;
            faces[fIdx++] = 0;
            faces[fIdx++] = baseV + 2;
            faces[fIdx++] = 0;
        }

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);

        return new MeshView(mesh);
    }
}
