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

package org.jscience.biology.loaders;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple Wavefront OBJ loader for JavaFX 3D.
 * <p>
 * Parses OBJ files and creates JavaFX TriangleMesh objects.
 * Supports vertices (v), texture coordinates (vt), normals (vn), and faces (f).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ObjMeshReader extends AbstractResourceReader<Group> {

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    public Class<Group> getResourceType() {
        return Group.class;
    }

    @Override
    protected Group loadFromSource(String id) throws Exception {
        if (id.startsWith("http") || id.startsWith("file:")) {
            return load(java.net.URI.create(id).toURL());
        }
        return loadResource(id);
    }

    @Override
    protected MiniCatalog<Group> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<Group> getAll() {
                return List.of(new Group());
            }

            @Override
            public Optional<Group> findByName(String name) {
                return Optional.of(new Group());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    /**
     * Loads an OBJ model from a file.
     */
    public static Group load(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return parse(reader);
        }
    }

    /**
     * Loads an OBJ model from a URL (resource or http).
     */
    public static Group load(URL url) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return parse(reader);
        }
    }

    /**
     * Loads an OBJ model from classpath resources.
     */
    public static Group loadResource(String resourcePath) throws IOException {
        URL url = ObjMeshReader.class.getResource(resourcePath);
        if (url == null) {
            throw new IOException("OBJ resource not found: " + resourcePath);
        }
        return load(url);
    }

    /**
     * Loads an OBJ model and applies a material color.
     */
    public static Group load(File file, Color color) throws IOException {
        Group group = load(file);
        applyMaterial(group, color);
        return group;
    }

    /**
     * Loads an OBJ resource and applies a material color.
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

        TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);

        float[] points = new float[vertices.size() * 3];
        for (int i = 0; i < vertices.size(); i++) {
            points[i * 3] = vertices.get(i)[0];
            points[i * 3 + 1] = vertices.get(i)[1];
            points[i * 3 + 2] = vertices.get(i)[2];
        }
        mesh.getPoints().addAll(points);

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
        int[] first = parseVertex(parts[1]);
        int[] prev = parseVertex(parts[2]);

        for (int i = 3; i < parts.length; i++) {
            int[] curr = parseVertex(parts[i]);
            faces.add(new int[] { first[0], first[1] });
            faces.add(new int[] { prev[0], prev[1] });
            faces.add(new int[] { curr[0], curr[1] });
            prev = curr;
        }
    }

    private static int[] parseVertex(String vertex) {
        String[] parts = vertex.split("/");
        int v = Integer.parseInt(parts[0]) - 1;
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
