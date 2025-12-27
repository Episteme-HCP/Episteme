/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology.loaders;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Wavefront OBJ loader for JavaFX 3D.
 * <p>
 * Parses OBJ files and creates JavaFX TriangleMesh objects.
 * Supports vertices (v), texture coordinates (vt), normals (vn), and faces (f).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ObjMeshLoader {

    /**
     * Loads an OBJ model from a file.
     *
     * @param file the OBJ file to load
     * @return Group containing all mesh views from the model
     */
    public static Group load(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return parse(reader);
        }
    }

    /**
     * Loads an OBJ model from a URL (resource or http).
     *
     * @param url the URL to load
     * @return Group containing all mesh views from the model
     */
    public static Group load(URL url) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return parse(reader);
        }
    }

    /**
     * Loads an OBJ model from classpath resources.
     *
     * @param resourcePath the resource path (e.g.,
     *                     "/org/jscience/medicine/anatomy/models/Skeleton.obj")
     * @return Group containing all mesh views from the model
     */
    public static Group loadResource(String resourcePath) throws IOException {
        URL url = ObjMeshLoader.class.getResource(resourcePath);
        if (url == null) {
            throw new IOException("OBJ resource not found: " + resourcePath);
        }
        return load(url);
    }

    /**
     * Loads an OBJ model and applies a material color.
     *
     * @param file  the OBJ file
     * @param color the material color to apply
     * @return Group with colored meshes
     */
    public static Group load(File file, Color color) throws IOException {
        Group group = load(file);
        applyMaterial(group, color);
        return group;
    }

    /**
     * Loads an OBJ resource and applies a material color.
     *
     * @param resourcePath the resource path
     * @param color        the material color to apply
     * @return Group with colored meshes
     */
    public static Group loadResource(String resourcePath, Color color) throws IOException {
        Group group = loadResource(resourcePath);
        applyMaterial(group, color);
        return group;
    }

    private static Group parse(BufferedReader reader) throws IOException {
        List<float[]> vertices = new ArrayList<>();
        List<float[]> texCoords = new ArrayList<>();
        List<float[]> normals = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split("\\s+");
            switch (parts[0]) {
                case "v":
                    vertices.add(new float[] {
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3])
                    });
                    break;
                case "vt":
                    texCoords.add(new float[] {
                            Float.parseFloat(parts[1]),
                            parts.length > 2 ? Float.parseFloat(parts[2]) : 0f
                    });
                    break;
                case "vn":
                    normals.add(new float[] {
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3])
                    });
                    break;
                case "f":
                    parseFace(parts, faces);
                    break;
            }
        }

        // Build TriangleMesh
        TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);

        // Add vertices
        float[] points = new float[vertices.size() * 3];
        for (int i = 0; i < vertices.size(); i++) {
            points[i * 3] = vertices.get(i)[0];
            points[i * 3 + 1] = vertices.get(i)[1];
            points[i * 3 + 2] = vertices.get(i)[2];
        }
        mesh.getPoints().addAll(points);

        // Add texture coordinates (use dummy if none)
        if (texCoords.isEmpty()) {
            mesh.getTexCoords().addAll(0f, 0f);
        } else {
            float[] texs = new float[texCoords.size() * 2];
            for (int i = 0; i < texCoords.size(); i++) {
                texs[i * 2] = texCoords.get(i)[0];
                texs[i * 2 + 1] = texCoords.get(i)[1];
            }
            mesh.getTexCoords().addAll(texs);
        }

        // Add faces
        int[] faceData = new int[faces.size() * 2];
        for (int i = 0; i < faces.size(); i++) {
            faceData[i * 2] = faces.get(i)[0];
            faceData[i * 2 + 1] = faces.get(i)[1];
        }
        mesh.getFaces().addAll(faceData);

        MeshView meshView = new MeshView(mesh);
        PhongMaterial material = new PhongMaterial(Color.LIGHTGRAY);
        material.setSpecularColor(Color.WHITE);
        meshView.setMaterial(material);

        return new Group(meshView);
    }

    private static void parseFace(String[] parts, List<int[]> faces) {
        // Triangulate face (assuming convex polygon)
        int[] first = parseVertex(parts[1]);
        int[] prev = parseVertex(parts[2]);

        for (int i = 3; i < parts.length; i++) {
            int[] curr = parseVertex(parts[i]);
            // Triangle: first, prev, curr
            faces.add(new int[] { first[0], first[1] });
            faces.add(new int[] { prev[0], prev[1] });
            faces.add(new int[] { curr[0], curr[1] });
            prev = curr;
        }
    }

    private static int[] parseVertex(String vertex) {
        String[] parts = vertex.split("/");
        int v = Integer.parseInt(parts[0]) - 1; // OBJ is 1-indexed
        int vt = 0;
        if (parts.length > 1 && !parts[1].isEmpty()) {
            vt = Integer.parseInt(parts[1]) - 1;
        }
        return new int[] { v, vt };
    }

    private static void applyMaterial(Group group, Color color) {
        PhongMaterial material = new PhongMaterial(color);
        material.setSpecularColor(Color.WHITE);
        for (Node node : group.getChildren()) {
            if (node instanceof MeshView) {
                ((MeshView) node).setMaterial(material);
            }
        }
    }
}
