/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.computing.ai;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genetic Algorithm Visualization: Evolving Pathfinders.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class GeneticAlgorithmViewer extends Application {

    private final int popSize = 50;
    private int lifespan = 200;
    private int count = 0;
    private int generation = 1;
    private int reachedCount = 0;
    private double bestFitness = 0;
    private int speed = 1;
    private final Random rand = new Random();

    private final double targetX = 400, targetY = 50;
    private List<DNA> population = new ArrayList<>();
    private Canvas canvas;

    private static class DNA {
        double[] genes;
        double x = 400, y = 550;
        double fitness = 0;
        boolean reached = false;
        boolean crashed = false;

        DNA(int length) {
            genes = new double[length * 2]; // store vx, vy pairs
            for (int i = 0; i < genes.length; i++) {
                genes[i] = (new Random().nextDouble() - 0.5) * 10;
            }
        }

        DNA(double[] genes) {
            this.genes = genes;
        }
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(180);
        sidebar.setStyle("-fx-background-color: #16213e;");

        Label title = new Label("🧬 Genetic Algorithm");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #00d9ff;");

        Label genLabel = new Label("Generation: 1");
        genLabel.setStyle("-fx-text-fill: #aaa;");

        Label fitLabel = new Label("Best Fitness: 0.00");
        fitLabel.setStyle("-fx-text-fill: #aaa;");

        Label reachLabel = new Label("Reached: 0/50");
        reachLabel.setStyle("-fx-text-fill: #aaa;");

        Separator sep1 = new Separator();

        Label speedLabel = new Label("Speed: 1x");
        speedLabel.setStyle("-fx-text-fill: #888;");
        javafx.scene.control.Slider speedSlider = new javafx.scene.control.Slider(1, 5, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.valueProperty().addListener((o, ov, nv) -> {
            speed = nv.intValue();
            speedLabel.setText("Speed: " + speed + "x");
        });

        Separator sep2 = new Separator();

        Button resetBtn = new Button("Restart");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        resetBtn.setOnAction(e -> restart());

        Label descLabel = new Label(
                "Evolves pathfinders to\nreach target (red dot).\n\n" +
                        "Blue = Active\nGreen = Reached\nGray = Crashed");
        descLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");
        descLabel.setWrapText(true);

        sidebar.getChildren().addAll(title, new Separator(), genLabel, fitLabel, reachLabel,
                sep1, speedLabel, speedSlider, sep2, resetBtn, new Separator(), descLabel);
        root.setRight(sidebar);

        restart();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int s = 0; s < speed; s++) {
                    if (count < lifespan) {
                        updatePhysics();
                        count++;
                    } else {
                        evaluate();
                        selection();
                        generation++;
                        genLabel.setText("Generation: " + generation);
                        fitLabel.setText(String.format("Best Fitness: %.2f", bestFitness));
                        reachLabel.setText("Reached: " + reachedCount + "/" + popSize);
                        count = 0;
                    }
                }
                render();
            }
        }.start();

        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("JScience - Genetic Algorithm Demo");
        stage.setScene(scene);
        stage.show();
    }

    private void restart() {
        population.clear();
        for (int i = 0; i < popSize; i++) {
            population.add(new DNA(lifespan));
        }
        generation = 1;
        count = 0;
    }

    private void updatePhysics() {
        for (DNA d : population) {
            if (d.reached || d.crashed)
                continue;

            d.x += d.genes[count * 2];
            d.y += d.genes[count * 2 + 1];

            // Check target
            double dist = Math.sqrt(Math.pow(d.x - targetX, 2) + Math.pow(d.y - targetY, 2));
            if (dist < 20)
                d.reached = true;

            // Check boundaries
            if (d.x < 0 || d.x > 800 || d.y < 0 || d.y > 600)
                d.crashed = true;
        }
    }

    private void evaluate() {
        bestFitness = 0;
        reachedCount = 0;
        for (DNA d : population) {
            double dist = Math.sqrt(Math.pow(d.x - targetX, 2) + Math.pow(d.y - targetY, 2));
            d.fitness = 1.0 / (dist + 1);
            if (d.reached) {
                d.fitness *= 10;
                reachedCount++;
            }
            if (d.crashed)
                d.fitness /= 2;
            bestFitness = Math.max(bestFitness, d.fitness);
        }
    }

    private void selection() {
        List<DNA> nextGen = new ArrayList<>();
        // Simple Mating Pool
        List<DNA> pool = new ArrayList<>();
        double maxFit = population.stream().mapToDouble(d -> d.fitness).max().orElse(1.0);

        for (DNA d : population) {
            int n = (int) ((d.fitness / maxFit) * 100);
            for (int j = 0; j < n; j++)
                pool.add(d);
        }

        for (int i = 0; i < popSize; i++) {
            DNA parentA = pool.get(rand.nextInt(pool.size()));
            DNA parentB = pool.get(rand.nextInt(pool.size()));
            DNA child = crossover(parentA, parentB);
            mutate(child);
            nextGen.add(child);
        }
        population = nextGen;
    }

    private DNA crossover(DNA a, DNA b) {
        double[] genes = new double[lifespan * 2];
        int mid = rand.nextInt(genes.length);
        for (int i = 0; i < genes.length; i++) {
            genes[i] = (i < mid) ? a.genes[i] : b.genes[i];
        }
        return new DNA(genes);
    }

    private void mutate(DNA d) {
        for (int i = 0; i < d.genes.length; i++) {
            if (rand.nextDouble() < 0.01) {
                d.genes[i] = (rand.nextDouble() - 0.5) * 10;
            }
        }
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#1a1a2e"));
        gc.fillRect(0, 0, 800, 600);

        // Target with glow
        gc.setFill(Color.web("#ff6b6b"));
        gc.fillOval(targetX - 15, targetY - 15, 30, 30);
        gc.setFill(Color.RED);
        gc.fillOval(targetX - 10, targetY - 10, 20, 20);

        // Population
        gc.setFill(Color.BLUE);
        for (DNA d : population) {
            if (d.reached)
                gc.setFill(Color.LIMEGREEN);
            else if (d.crashed)
                gc.setFill(Color.web("#333"));
            else
                gc.setFill(Color.web("#4dabf7"));
            gc.fillOval(d.x - 4, d.y - 4, 8, 8);
        }

        // Start position indicator
        gc.setStroke(Color.web("#666"));
        gc.strokeOval(400 - 12, 550 - 12, 24, 24);
    }

    public static void show(Stage stage) {
        new GeneticAlgorithmViewer().start(stage);
    }
}
