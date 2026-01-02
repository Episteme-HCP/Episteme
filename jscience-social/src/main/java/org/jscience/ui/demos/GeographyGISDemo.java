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

package org.jscience.ui.demos;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.AppProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jscience.mathematics.geometry.Point2D;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Geography GIS Demo using JScience geometry types.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeographyGISDemo implements AppProvider {

    private Canvas canvas;
    private WritableImage terrainCache;
    private int width = 800;
    private int height = 600;
    private long seed;
    private double[][] heightMap;
    private List<Point2D> cities = new ArrayList<>();

    private CheckBox terrainChk;
    private CheckBox roadsChk;
    private CheckBox popChk;

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return "Geography";
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("geo.gis.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("geo.gis.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");

        canvas = new Canvas(width, height);
        root.setCenter(canvas);

        // Controls
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.getStyleClass().add("dark-viewer-sidebar");
        controls.setPrefWidth(200);

        Label title = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("geo.gis.label.layers"));
        title.getStyleClass().add("dark-header");

        terrainChk = new CheckBox(org.jscience.ui.i18n.SocialI18n.getInstance().get("geo.gis.check.terrain"));
        terrainChk.setSelected(true);
        terrainChk.setOnAction(e -> draw());

        roadsChk = new CheckBox(org.jscience.ui.i18n.SocialI18n.getInstance().get("geo.gis.check.roads"));
        roadsChk.setSelected(true);
        roadsChk.setOnAction(e -> draw());

        popChk = new CheckBox(org.jscience.ui.i18n.SocialI18n.getInstance().get("geo.gis.check.pop"));
        popChk.setSelected(false);
        popChk.setOnAction(e -> draw());

        Button regenBtn = new Button("Regenerate Terrain");
        regenBtn.setMaxWidth(Double.MAX_VALUE);
        regenBtn.setOnAction(e -> generateWorld());

        controls.getChildren().addAll(title, terrainChk, roadsChk, popChk, new Separator(), regenBtn);
        root.setRight(controls);

        // Initial Generation
        generateWorld();

        Scene scene = new Scene(root, 1000, 650);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private void generateWorld() {
        seed = new Random().nextLong();
        heightMap = generateHeightMap(width, height, seed);
        cities.clear();

        // Place cities
        Random rand = new Random(seed);
        for (int i = 0; i < 15; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (heightMap[x][y] > 0.3 && heightMap[x][y] < 0.7) { // Habitable zone
                cities.add(Point2D.of(x, y));
            }
        }

        // Cache Terrain Image
        terrainCache = new WritableImage(width, height);
        PixelWriter pw = terrainCache.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pw.setColor(x, y, getBiomeColor(heightMap[x][y]));
            }
        }

        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // 1. Terrain Layer
        if (terrainChk.isSelected() && terrainCache != null) {
            gc.drawImage(terrainCache, 0, 0);
        } else {
            gc.setFill(Color.web("#111"));
            gc.fillRect(0, 0, width, height);
        }

        // 2. Roads Layer (Simplified MST approximation)
        if (roadsChk.isSelected()) {
            gc.setStroke(Color.web("#555", 0.8));
            gc.setLineWidth(2);
            for (Point2D c1 : cities) {
                Point2D closest = null;
                Real minDst = Real.of(Double.MAX_VALUE);
                for (Point2D c2 : cities) {
                    if (c1.equals(c2))
                        continue;
                    Real d = c1.distanceTo(c2);
                    if (d.compareTo(minDst) < 0) {
                        minDst = d;
                        closest = c2;
                    }
                }
                if (closest != null) {
                    gc.strokeLine(c1.getX().doubleValue(), c1.getY().doubleValue(),
                            closest.getX().doubleValue(), closest.getY().doubleValue());
                }
            }
        }

        // 3. Population Layer (Heatmap)
        if (popChk.isSelected()) {
            for (Point2D city : cities) {
                double cx = city.getX().doubleValue();
                double cy = city.getY().doubleValue();
                double r = 40;
                gc.setFill(new javafx.scene.paint.RadialGradient(
                        0, 0, cx, cy, r, false, javafx.scene.paint.CycleMethod.NO_CYCLE,
                        new javafx.scene.paint.Stop(0, Color.rgb(255, 50, 50, 0.8)),
                        new javafx.scene.paint.Stop(1, Color.TRANSPARENT)));
                gc.fillOval(cx - r, cy - r, r * 2, r * 2);
            }
        }
    }

    private Color getBiomeColor(double h) {
        if (h < 0.3)
            return Color.web("#1e3799");
        if (h < 0.35)
            return Color.web("#4a69bd");
        if (h < 0.38)
            return Color.web("#f6e58d");
        if (h < 0.55)
            return Color.web("#78e08f");
        if (h < 0.70)
            return Color.web("#079992");
        if (h < 0.85)
            return Color.web("#60a3bc");
        return Color.web("#ffffff");
    }

    private double[][] generateHeightMap(int w, int h, long seed) {
        double[][] map = new double[w][h];
        double scale = 0.02;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                double val = 0;
                double amp = 1.0;
                double freq = 1.0;
                for (int i = 0; i < 4; i++) {
                    val += noise(x * scale * freq, y * scale * freq) * amp;
                    amp *= 0.5;
                    freq *= 2.0;
                }
                val = (val / 2.0 + 1) / 2.0;
                double dx = x - w / 2.0;
                double dy = y - h / 2.0;
                double dist = Math.sqrt(dx * dx + dy * dy) / (Math.min(w, h) / 1.8);
                val = val * (1 - Math.pow(dist, 3));
                map[x][y] = Math.max(0, Math.min(1, val));
            }
        }
        return map;
    }

    private double noise(double x, double y) {
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        double sx = x - x0;
        double sy = y - y0;
        double n00 = pseudoRandom(x0, y0);
        double n10 = pseudoRandom(x0 + 1, y0);
        double n01 = pseudoRandom(x0, y0 + 1);
        double n11 = pseudoRandom(x0 + 1, y0 + 1);
        double ix0 = lerp(n00, n10, sx);
        double ix1 = lerp(n01, n11, sx);
        return lerp(ix0, ix1, sy);
    }

    private double pseudoRandom(int x, int y) {
        long h = seed + x * 374761393L + y * 668265263L;
        h = (h ^ (h >> 13)) * 1274126177L;
        return ((h ^ (h >> 16)) & 0xFFFFFF) / 16777215.0 * 2.0 - 1.0;
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }
}
